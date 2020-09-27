package com.example.DAMGenerator.generator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.DAMGenerator.generator.Generator;
import com.example.DAMGenerator.transformer.model.NewProjectDbConnection;
import com.example.DAMGenerator.transformer.model.NewProjectInfo;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MakeNewProjectService extends Generator{

	
	public String makeProjectStructure(NewProjectInfo info, NewProjectDbConnection connection, String path, List<OldFile> oldFiles) {
		
		StringBuilder basePath = new StringBuilder();
		
		try {
			String newProjectPath = path + File.separator + info.getProjectName().toString();
			Files.createDirectories(Paths.get(newProjectPath));
			makePackages(newProjectPath);
			makePomFile(info, newProjectPath, oldFiles);
			String basepath = generateApplicationMainClass(newProjectPath, info, oldFiles);
			basePath.append(basepath);
			
			createYamlFile(newProjectPath, connection, info.getBasePackageName(), oldFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return basePath.toString();
	}
	
	private void makePackages(final String path) {
		try {
			Files.createDirectories(Paths.get(path + File.separator + "src/main/java"));
			Files.createDirectories(Paths.get(path + File.separator + "src/main/resources"));
			Files.createDirectories(Paths.get(path + File.separator + "src/test/java"));
			Files.createDirectories(Paths.get(path + File.separator + "src/test/resources"));
			Files.createDirectories(Paths.get(path + File.separator + "src/main/resources/static"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void makePomFile(NewProjectInfo newProjectInfo, String newProjectPath, List<OldFile> oldFiles) 
	{
		Template t = getTemplate(TemplateType.POM);
		Writer out = null;
		Map<String, Object> data = new HashMap<String, Object>();
		File outputFile = new File(newProjectPath + File.separator + "pom.xml");
		outputFile.getParentFile().mkdirs();
		try {
			out = new OutputStreamWriter(new FileOutputStream(outputFile));
			data.clear();
			data.put("project", newProjectInfo);
			t.process(data, out);
			out.flush();
			if(!oldFiles.isEmpty())
			{
			for (OldFile oldFile : oldFiles) {
			    if (oldFile.getFilename().equals(outputFile.getName())) {
			    	String generatedFile = new String(Files.readAllBytes(outputFile.toPath()));
			    	if(!oldFile.getContent().equals(generatedFile))
			    	{
			    		outputFile.delete();
			    		byte[] bytes = oldFile.getContent().getBytes();
						Files.write(outputFile.toPath(), bytes);
						oldFile.setFilename("/");
			    	}
			    }  }
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private String generateApplicationMainClass(final String newProjectPath, final NewProjectInfo newProjectInfo, List<OldFile> oldFiles) {
		Template template = getTemplate(TemplateType.APPLICATION);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		String mainPackagePath = newProjectPath + "\\src\\main\\java\\"
				+ newProjectInfo.getBasePackageName().replace(".", "\\");
		File outputFile = new File(mainPackagePath + File.separator + "Application.java");
		System.out.println(mainPackagePath);
		outputFile.getParentFile().mkdirs();
		try {
			out = new OutputStreamWriter(new FileOutputStream(outputFile));
			context.clear();
			context.put("project", newProjectInfo);
			template.process(context, out);
			out.flush();
			if(!oldFiles.isEmpty())
			{
			for (OldFile oldFile : oldFiles) {
			    if (oldFile.getFilename().equals(outputFile.getName())) {
			    	String generatedFile = new String(Files.readAllBytes(outputFile.toPath()));
			    	if(!oldFile.getContent().equals(generatedFile))
			    	{
			    		outputFile.delete();
			    		byte[] bytes = oldFile.getContent().getBytes();
						Files.write(outputFile.toPath(), bytes);
						oldFile.setFilename("/");
			    	}
			    }  }
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
		return mainPackagePath;
	}
	private void createYamlFile(String path, NewProjectDbConnection connection, String basePackageName, List<OldFile> oldFiles) {
		Template template = getTemplate(TemplateType.YAML);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		File outputFile = new File(path + "\\src\\main\\resources" + File.separator + "application.yml");
		outputFile.getParentFile().mkdirs();
		try {
			out = new OutputStreamWriter(new FileOutputStream(outputFile));
			context.clear();
			context.put("database", connection);
			context.put("package", basePackageName);
			template.process(context, out);
			out.flush();
			{
			for (OldFile oldFile : oldFiles) {
			    if (oldFile.getFilename().equals(outputFile.getName())) {
			    	String generatedFile = new String(Files.readAllBytes(outputFile.toPath()));
			    	if(!oldFile.getContent().equals(generatedFile))
			    	{
			    		outputFile.delete();
			    		byte[] bytes = oldFile.getContent().getBytes();
						Files.write(outputFile.toPath(), bytes);
						oldFile.setFilename("/");
			    	}
			    }
			}}
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
	}
	
}
