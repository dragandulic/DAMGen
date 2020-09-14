package com.example.DAMGenerator.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.DAMGenerator.database.model.FMDatabaseMetadata;
import com.example.DAMGenerator.database.model.FMTable;

@Repository
public class DatabaseMetadataRepository {

	@Autowired 
	public Connection connection;
	 
	
	public FMDatabaseMetadata getDatabaseMetadata() {
		FMDatabaseMetadata result = new FMDatabaseMetadata();
		
		try {
						
			  DatabaseMetaData databaseMetaData = connection.getMetaData();
			  ResultSet tableMetadata = databaseMetaData.getTables(null, null, "%", new String[] { "TABLE" }); 
			  List<FMTable> tables = processTables(tableMetadata);
			  result.setTables(tables);
			 
		} catch (Exception ex) {
			result = null;
		}
		
		return result;
	}
	
	
	
	  private List<FMTable> processTables(ResultSet resultSet) throws SQLException{
		  List<FMTable> tables = new ArrayList<>(); while (resultSet.next()) {
		  FMTable table = new FMTable();
		  table.setTableSchema(resultSet.getString("TABLE_CAT"));
		  table.setTableName(resultSet.getString("TABLE_NAME"));
		  table.setTableType(resultSet.getString("TABLE_TYPE")); tables.add(table); }
		  return tables; 
	  }
	 
	
}
