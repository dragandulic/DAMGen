package com.example.DAMGenerator.generator.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DAMGenerator.transformer.model.MakeNewProject;

@Service
public class GeneratorService {

	@Autowired
	public MakeModelService makeModelService;
	
	@Autowired
	public MakeRepositoryService makeRepositoryService;
	
	@Autowired
	public MakeServiceService makeServiceService;
	
	@Autowired
	public MakeControllerService makeControllerService;
	
	@Autowired
	public MakePageService makePageService;
	
	public void generate(MakeNewProject makeNewProject, String path, List<OldFile> oldFiles) {
		
		makeModelService.generateModel(path, makeNewProject.getNewProjectInfo().getBasePackageName(), makeNewProject.getClasses(), oldFiles);
		makeRepositoryService.generateRepository(path, makeNewProject.getNewProjectInfo().getBasePackageName(), makeNewProject.getClasses(), oldFiles);
		makeServiceService.generateService(path, makeNewProject.getNewProjectInfo().getBasePackageName(), makeNewProject.getClasses(), oldFiles);
		makeControllerService.generateController(path, makeNewProject.getNewProjectInfo().getBasePackageName(), makeNewProject.getClasses(), oldFiles);
		makePageService.generatePage(path, makeNewProject.getNewProjectInfo().getBasePackageName(), makeNewProject.getClasses(), oldFiles);
		
		if(!oldFiles.isEmpty())
		{
		new File(makeNewProject.getNewProjectInfo().getBasePath()+File.separator+"ChangedFiles").mkdirs();
		
		for(int i=0;i< oldFiles.size();i++)
		{
			if(!oldFiles.get(i).getFilename().equals("/"))
			{
				String fpath = makeNewProject.getNewProjectInfo().getBasePath()+File.separator+"/ChangedFiles"+File.separator+oldFiles.get(i).getFilename();
				File f = new File(fpath);
				f.getParentFile().mkdirs(); 
				try {
					f.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				try {
					Path p = Paths.get(fpath);
					byte[] bytes = oldFiles.get(i).getContent().getBytes();
					Files.write(p, bytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		}
	}
	
}
