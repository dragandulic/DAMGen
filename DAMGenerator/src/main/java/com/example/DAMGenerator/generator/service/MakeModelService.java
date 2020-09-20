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
import com.example.DAMGenerator.transformer.model.Property;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MakeModelService extends Generator{

	private List<String> imports = new ArrayList<>();
	
	public void generateModel(String path, String packageName, List<ClassData> classDatas) {
		
		List<ClassData> classesForGenerate = new ArrayList<>();
		for (ClassData classData : classDatas) {
			if(classData.getGenerateClass()) {
				classesForGenerate.add(classData);
			}
		}
		
		for (ClassData classData : classesForGenerate) {
			generateModelClass(packageName, path, classData);
		}
		
	}
	
	private void generateModelClass(String packageName, String path, ClassData classData) {
		imports.add("javax.persistence.Table");
		imports.add("javax.persistence.Entity");
		imports.add("javax.persistence.Column");
		generateImportList(classData);
		
		Template t = getTemplate(TemplateType.MODEL);
		Writer out = null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			out = getWriter(path + File.separator + TemplateType.MODEL.toString().toLowerCase()
					+ File.separator + classData.getClassName() + ".java");

			data.clear();
			data.put("class", classData);
			data.put("packageName", packageName.concat(".model"));
			data.put("imports", imports);
			t.process(data, out);
			out.flush();

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
	
	
	private void generateImportList(ClassData classData) {
		
		if(!classData.getFields().isEmpty()) {
			for (Field field : classData.getFields()) {
				if(field.getIsPrimaryKey())
				{
					imports.add("javax.persistence.Id");
					break;
				}
			}
		}
		for (Field field : classData.getFields()) {
			if(field.getType().equals("Date"))
			{
				imports.add("java.sql.Date");
				break;
			}
		}
		
		if(classData.getCompositeKey() != null)
		{
			imports.add("javax.persistence.EmbeddedId");	
		}
		
		if (!classData.getProperties().isEmpty() && classData.getCompositeKey() == null) {
			imports.add("javax.persistence.FetchType");
			imports.add("javax.persistence.JoinColumn");
			imports.add("javax.persistence.ManyToOne");
			
			for (Property property : classData.getProperties()) {
				if(property.getIsSelfReferenced())
				{
					imports.add("javax.persistence.OneToMany");
					imports.add("javax.persistence.CascadeType");
					imports.add("java.util.Set");
					imports.add("java.util.HashSet");
					break;
				}
			}	
		}
		//TODO: ManyToManyPropery	
	}
	
	
	
	
	
}



