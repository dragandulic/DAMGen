package com.example.DAMGenerator.database.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DAMGenerator.database.DatabaseMetadataRepository;
import com.example.DAMGenerator.database.model.FMDatabaseMetadata;


@Service
public class DatabaseMetadataService {

	@Autowired
	public DatabaseMetadataRepository databaseMetadataRepository;
	 
	public FMDatabaseMetadata getDatabaseMetadata() {
		FMDatabaseMetadata result = databaseMetadataRepository.getDatabaseMetadata();
		return result;
	}
	
}
