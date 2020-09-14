package com.example.DAMGenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DAMGenerator.database.model.FMDatabaseMetadata;
import com.example.DAMGenerator.database.service.DatabaseMetadataService;

@RestController
@RequestMapping("/app")
public class DAMGeneratorController {

	@Autowired
	public DatabaseMetadataService databaseMetadataService;
	
	
	@GetMapping("/metadata")
	public ResponseEntity<FMDatabaseMetadata> getMetadata(){
		FMDatabaseMetadata databaseMetadata = databaseMetadataService.getDatabaseMetadata();
		
		if(databaseMetadata != null)
		{
			return new ResponseEntity<FMDatabaseMetadata>(databaseMetadata, HttpStatus.OK);	
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}
