package com.example.DAMGenerator.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
import com.example.DAMGenerator.generator.service.OldFile;
import com.example.DAMGenerator.transformer.Transformer;
import com.example.DAMGenerator.transformer.model.MakeClasses;
import com.example.DAMGenerator.transformer.model.MakeNewProject;
import com.example.DAMGenerator.transformer.model.NewProjectInfo;

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
		
		List<OldFile> oldFiles = getAllCurrentFiles(path + File.separator + makeNewProject.getNewProjectInfo().getProjectName().toString(),makeNewProject.getNewProjectInfo());
		String resultPath = makeNewProjectService.makeProjectStructure(makeNewProject.getNewProjectInfo(), makeNewProject.getDatabaseConnection(), path, oldFiles);
	
		generatorService.generate(makeNewProject, resultPath, oldFiles);
		
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private List<OldFile> getAllCurrentFiles(String projectPath, NewProjectInfo info)
	{
		List<OldFile> listFiles = new ArrayList<OldFile>();
		Path path = Paths.get(projectPath + File.separator + "src/main/java"+ File.separator +info.getBasePackageName()+ File.separator +"model");
		if (Files.notExists(path)) {
		   return listFiles;
		}
		File dirModel = new File(projectPath + File.separator + "src/main/java"+ File.separator +info.getBasePackageName()+ File.separator +"model");
		
		
		
		for (File file : dirModel.listFiles()) {

			try {
				String oldVersionContent = new String(Files.readAllBytes(file.toPath()));
				OldFile oldfile = new OldFile(file.getName(), oldVersionContent, file.getPath());
				listFiles.add(oldfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	                                            }
		File dirRepository = new File(projectPath + File.separator + "src/main/java"+ File.separator +info.getBasePackageName()+ File.separator +"repository");
		if(dirRepository != null)
		{
		for (File file : dirRepository.listFiles()) {

			try {
				String oldVersionContent = new String(Files.readAllBytes(file.toPath()));
				OldFile oldfile = new OldFile(file.getName(), oldVersionContent, file.getPath());
				listFiles.add(oldfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	                                            }
		}
		
		File dirService = new File(projectPath + File.separator + "src/main/java"+ File.separator +info.getBasePackageName()+ File.separator +"service");
		if(dirService != null)
		{
		for (File file : dirService.listFiles()) {

			try {
				String oldVersionContent = new String(Files.readAllBytes(file.toPath()));
				OldFile oldfile = new OldFile(file.getName(), oldVersionContent, file.getPath());
				listFiles.add(oldfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	                                            }
		}
		
		File dirController = new File(projectPath + File.separator + "src/main/java"+ File.separator +info.getBasePackageName()+ File.separator +"controller");
		if(dirController != null)
		{
		for (File file : dirController.listFiles()) {

			try {
				String oldVersionContent = new String(Files.readAllBytes(file.toPath()));
				OldFile oldfile = new OldFile(file.getName(), oldVersionContent, file.getPath());
				listFiles.add(oldfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	                                            }
		}
		
		File dirPackageName = new File(projectPath + File.separator + "src/main/java"+ File.separator +info.getBasePackageName());
		if(dirPackageName != null)
		{
		for (File file : dirPackageName.listFiles()) {
			if(!file.isDirectory()) {
			try {
				String oldVersionContent = new String(Files.readAllBytes(file.toPath()));
				
				OldFile oldfile = new OldFile(file.getName(), oldVersionContent, file.getPath());
				listFiles.add(oldfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	                                            }
		}
		File dirStatic = new File(projectPath + File.separator + "src/main/resources/static");
		if(dirStatic != null)
		{
		for (File file : dirStatic.listFiles()) {
			if(!file.isDirectory()) {
			try {
				String oldVersionContent = new String(Files.readAllBytes(file.toPath()));
				
				OldFile oldfile = new OldFile(file.getName(), oldVersionContent, file.getPath());
				listFiles.add(oldfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	                                            }
		}
		
		File dirResources = new File(projectPath + File.separator + "src/main/resources");
		if(dirResources != null)
		{
		for (File file : dirResources.listFiles()) {
			if(!file.isDirectory()) {
			try {
				String oldVersionContent = new String(Files.readAllBytes(file.toPath()));
				
				OldFile oldfile = new OldFile(file.getName(), oldVersionContent, file.getPath());
				listFiles.add(oldfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	                                            }
		}
		File dirProjectPath = new File(projectPath);
		if(dirProjectPath != null)
		{
		for (File file : dirProjectPath.listFiles()) {
			if(!file.isDirectory()) {
			try {
				String oldVersionContent = new String(Files.readAllBytes(file.toPath()));
				
				OldFile oldfile = new OldFile(file.getName(), oldVersionContent, file.getPath());
				listFiles.add(oldfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	                                            }
		}
	 return listFiles;
	}






}



















