package com.example.DAMGenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DAMGenerator.database.model.FMDatabaseMetadata;
import com.example.DAMGenerator.database.service.DatabaseMetadataService;
import com.example.DAMGenerator.generator.service.GeneratorService;
import com.example.DAMGenerator.generator.service.MakeNewProjectService;
import com.example.DAMGenerator.transformer.Transformer;
import com.example.DAMGenerator.transformer.model.MakeClasses;
import com.example.DAMGenerator.transformer.model.MakeNewProject;

@RestController
@RequestMapping("/app")
public class DAMGeneratorController {

	@Autowired
	public DatabaseMetadataService databaseMetadataService;
	
	@Autowired
	public Transformer transformer; 
	
	@Autowired
	public MakeNewProjectService makeNewProjectService;
	
	@Autowired
	public GeneratorService generatorService;
	
	
	@GetMapping("/metadata")
	public ResponseEntity<FMDatabaseMetadata> getMetadata(){
		FMDatabaseMetadata databaseMetadata = databaseMetadataService.getDatabaseMetadata();
		
		if(databaseMetadata != null)
		{
			return new ResponseEntity<FMDatabaseMetadata>(databaseMetadata, HttpStatus.OK);	
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/transform/metadata")
	public ResponseEntity<?> transforMetaData(@RequestBody FMDatabaseMetadata databaseMetadata, @RequestHeader String generatorType){
		
		if(generatorType.equals("ExistingProject"))
		{
			MakeClasses makeClasses = transformer.transforMetadataToClasses(databaseMetadata);
			return new ResponseEntity<MakeClasses>(makeClasses, HttpStatus.OK);
		}
		else if(generatorType.equals("NewProject"))
		{
			MakeNewProject makeNewProject = transformer.transformMetadataForNewProject(databaseMetadata);
			return new ResponseEntity<MakeNewProject>(makeNewProject, HttpStatus.OK);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/create/project")
	public ResponseEntity<?> makeNewProject(@RequestBody final MakeNewProject makeNewProject){
		
		//TODO: validacija podataka iz body-a
		String path = makeNewProject.getNewProjectInfo().getBasePath().replace("\\\\", "\\");
		makeNewProject.getNewProjectInfo().setBasePath(path);
		
		String resultPath = makeNewProjectService.makeProjectStructure(makeNewProject.getNewProjectInfo(), makeNewProject.getDatabaseConnection(), path);
	
		generatorService.generate(makeNewProject, resultPath);
		
		
		return new ResponseEntity<>(HttpStatus.OK);
	}








}



















