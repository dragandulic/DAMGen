package com.example.DAMGenerator.generator.service;

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
	
	public void generate(MakeNewProject makeNewProject, String path) {
		
		makeModelService.generateModel(path, makeNewProject.getNewProjectInfo().getBasePackageName(), makeNewProject.getClasses());
		makeRepositoryService.generateRepository(path, makeNewProject.getNewProjectInfo().getBasePackageName(), makeNewProject.getClasses());
		makeServiceService.generateService(path, makeNewProject.getNewProjectInfo().getBasePackageName(), makeNewProject.getClasses());
	}
	
}
