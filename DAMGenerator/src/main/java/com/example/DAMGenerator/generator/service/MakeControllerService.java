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
public class MakeControllerService extends Generator{
	
	private List<String> imports = new ArrayList<>();
	
	public void generateController(String path, String packageName, List<ClassData> classes, List<OldFile> oldFiles) {
		
		List<ClassData> generateControllerClasses = new ArrayList<>();
		for(ClassData classData : classes) {
			if(classData.getController().getGenerateController() && !classData.getRelationship().getIsRelationshipClass()) {
				generateControllerClasses.add(classData);
			}
		}
		
		for(ClassData classData : generateControllerClasses) {
			generateControllerForClasses(classData, path, packageName, oldFiles);
		}
	}
	
	private void generateControllerForClasses(ClassData classData, String path, String packageName, List<OldFile> oldFiles) {
		importsForOperations(classData);
		Field field = getIdColumn(classData, packageName);
		imports.add(packageName + ".model." + classData.getClassName());
		imports.add(packageName + ".service." + classData.getClassName() + "Service");
		
		Template template = getTemplate(TemplateType.CONTROLLER);
		
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter(path + File.separator + TemplateType.CONTROLLER.toString().toLowerCase()
					+ File.separator + classData.getClassName().concat("Controller") + ".java");
			context.clear();
			context.put("class", classData);
			context.put("fieldName", ClassNameHelper.toFieldName(classData.getClassName()));
			context.put("idField", field);
			context.put("packageName", packageName.concat(".controller"));
			context.put("controllerPath", classData.getController().getControllerOperations().getControllerPath());
			context.put("imports", imports);
			template.process(context, out);
			out.flush();
			if(!oldFiles.isEmpty())
			{
			for (OldFile oldFile : oldFiles) {
			    if (oldFile.getFilename().equals(classData.getClassName().concat("Controller")+".java")) {
			    	File output = new File(path + File.separator + TemplateType.CONTROLLER.toString().toLowerCase()
							+ File.separator + classData.getClassName().concat("Controller") + ".java");
			    	String generatedFile = new String(Files.readAllBytes(output.toPath()));
			    	if(!oldFile.getContent().equals(generatedFile))
			    	{
			     		output.delete();
			    		byte[] bytes = oldFile.getContent().getBytes();
						Files.write(output.toPath(), bytes);
						oldFile.setFilename("/");
			    	}	}
			    }
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
	
	private void importsForOperations(ClassData classData) {
		
		if(classData.getController().getControllerOperations().getGet()) {
			imports.add("org.springframework.web.bind.annotation.GetMapping");
		}
		if(classData.getController().getControllerOperations().getPost()) {
			imports.add("org.springframework.web.bind.annotation.PostMapping");
			imports.add("org.springframework.web.bind.annotation.RequestBody");
		}
		if(classData.getController().getControllerOperations().getPut()) {
			imports.add("org.springframework.web.bind.annotation.PutMapping");
		}
		if(classData.getController().getControllerOperations().getDelete()) {
			imports.add("org.springframework.web.bind.annotation.DeleteMapping");
		}
	}
	
	private Field getIdColumn(ClassData classData, String packageName) {
		
		if(classData.getCompositeKey() == null) {
			for(Field field : classData.getFields()) {
				if(field.getIsPrimaryKey()) {
					String idFieldName = field.getFieldName();
					String cap = idFieldName.substring(0, 1).toUpperCase() + idFieldName.substring(1);
					field.setFieldName(cap);
					imports.add("org.springframework.web.bind.annotation.PathVariable");
					return field;
				}
				else {
					return null;
				}
			}
		}
		else {
			Field compositeField = Field.builder().type(classData.getClassName() + "Id")
					.fieldName(classData.getClassName() + "Id").build();
			imports.add(packageName + ".model." + classData.getClassName() + "Id");
			imports.add("org.springframework.web.bind.annotation.RequestParam");
			return compositeField;
		}
		
		return null;
	}

}
