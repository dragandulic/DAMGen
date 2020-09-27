package com.example.DAMGenerator.generator.service;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.DAMGenerator.generator.Generator;
import com.example.DAMGenerator.transformer.helper.ClassNameHelper;
import com.example.DAMGenerator.transformer.model.ClassData;
import com.example.DAMGenerator.transformer.model.Field;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MakeServiceService extends Generator{

	private List<String> imports = new ArrayList<>();
	
	public void generateService(String path, String packageName, List<ClassData> classDatas, List<OldFile> oldFiles) {
		
		List<ClassData> classesForGenerate = new ArrayList<>();
		for (ClassData classData : classDatas) {
			if(classData.getService().getGenerateService() && !classData.getRelationship().getIsRelationshipClass())
			{
				classesForGenerate.add(classData);
			}
		}
		
		for (ClassData classData : classesForGenerate) {
			generateServiceClass(packageName, path, classData, oldFiles);
		}
	}
	
	
	private void generateServiceClass(String packageName, String path, ClassData classData, List<OldFile> oldFiles) {
		
		imports.add(packageName + ".model." + classData.getClassName());
		imports.add(packageName + ".repository." + classData.getClassName() + "Repository");
		String idType = getIdType(packageName, classData);
		Template template = getTemplate(TemplateType.SERVICE);
		Writer out = null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			out = getWriter(path + File.separator + TemplateType.SERVICE.toString().toLowerCase()
					+ File.separator + classData.getClassName().concat("Service") + ".java");
			data.clear();
			data.put("class", classData);
			data.put("fieldName", ClassNameHelper.toFieldName(classData.getClassName()));
			data.put("idType", idType);
			data.put("packageName", packageName.concat(".service"));
			data.put("imports", imports);
			template.process(data, out);
			out.flush();
			{
			for (OldFile oldFile : oldFiles) {
			    if (oldFile.getFilename().equals(classData.getClassName().concat("Service")+".java")) {
			    	File output = new File(path + File.separator + TemplateType.SERVICE.toString().toLowerCase()
							+ File.separator + classData.getClassName().concat("Service") + ".java");
			    	String generatedFile = new String(Files.readAllBytes(output.toPath()));
			    	if(!oldFile.getContent().equals(generatedFile))
			    	{
			    		output.delete();
			    		byte[] bytes = oldFile.getContent().getBytes();
						Files.write(output.toPath(), bytes);
						oldFile.setFilename("/");
			    	}
			    }}
			}
		} catch (TemplateException e) {
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
	
	
	private String getIdType(String packageName, ClassData classData) {
		if(classData.getCompositeKey() == null)
		{
			for (Field field : classData.getFields()) {
				if(field.getIsPrimaryKey())
				{
					return field.getType();
				}
			}
			return "";
		}
		else
		{
			imports.add(packageName + ".model." + classData.getClassName() + "Id");
			return classData.getClassName() + "Id";
		}
	}
	
	
	
	
}






