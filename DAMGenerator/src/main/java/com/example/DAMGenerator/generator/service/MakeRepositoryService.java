package com.example.DAMGenerator.generator.service;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.DAMGenerator.generator.Generator;
import com.example.DAMGenerator.transformer.model.ClassData;
import com.example.DAMGenerator.transformer.model.Field;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MakeRepositoryService extends Generator{
	
	private List<String> imports = new ArrayList<>();
	
	public void generateRepository(String path, String packageName, List<ClassData> classes) {
		
		List<ClassData> generateRepositoryClasses = new ArrayList<>();
		for(ClassData classData : classes) {
			if(classData.getGenerateRepository() && !classData.getRelationship().getIsRelationshipClass()) {
				generateRepositoryClasses.add(classData);
			}
		}
		
		for(ClassData classData : generateRepositoryClasses) {
			generateRepositoryForClasses(classData, path, packageName);
		}
	}
	
	private void generateRepositoryForClasses(ClassData classData, String path, String packageName) {
		
		String idType = getIdColumnType(classData, packageName);
		imports.add(packageName + ".model." + classData.getClassName());
		
		Template template = getTemplate(TemplateType.REPOSITORY);
		
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter(path + File.separator + TemplateType.REPOSITORY.toString().toLowerCase() 
					+ File.separator + classData.getClassName().concat("Repository") + ".java");
			context.clear();
			context.put("packageName", packageName.concat(".repository"));
			context.put("imports", imports);
			context.put("repoClassName", classData.getClassName());
			context.put("idType", idType);
			template.process(context, out);
			out.flush();
		}catch (TemplateException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		imports.clear();
	}

	private String getIdColumnType(ClassData classData, String packageName) {
		
		String idType = "";
		if(classData.getCompositeKey() == null) {
			for(Field field : classData.getFields())
				if(field.getIsPrimaryKey()) {
					idType = field.getType();
				}
		}else {
			imports.add(packageName + ".model." + classData.getClassName() + "Id");
			idType = classData.getClassName() + "Id";
		}
		return idType;
	}
}
