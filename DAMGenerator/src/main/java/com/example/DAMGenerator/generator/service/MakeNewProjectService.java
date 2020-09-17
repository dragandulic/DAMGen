package com.example.DAMGenerator.generator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.DAMGenerator.generator.Generator;
import com.example.DAMGenerator.transformer.model.NewProjectDbConnection;
import com.example.DAMGenerator.transformer.model.NewProjectInfo;

import freemarker.template.Template;

@Service
public class MakeNewProjectService extends Generator{

	
	public String makeProjectStructure(NewProjectInfo info, NewProjectDbConnection connection, String path) {
		
		StringBuilder basePath = new StringBuilder();
		
		try {
			String newProjectPath = path + File.separator + info.getProjectName().toString();
			Files.createDirectories(Paths.get(newProjectPath));
			makePackages(newProjectPath);
			makePomFile(info, newProjectPath);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void makePomFile(NewProjectInfo newProjectInfo, String newProjectPath) 
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
	
	
}
