package com.example.DAMGenerator.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.DAMGenerator.database.model.FMColumn;
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
			  ResultSet tableMetadata = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"}); 
			  List<FMTable> tables = getTables(tableMetadata);
			  getColumns(tables, databaseMetaData);
			  
			  
			  result.setTables(tables);
			  result.setDriverName("com.mysql.jdbc.Driver");
			  result.setUrl(databaseMetaData.getURL());
			  result.setUsername(databaseMetaData.getUserName().split("@")[0]);
			  
		} catch (Exception ex) {
			System.out.println("EXCEPTION: Something went wrong, Method: GetDatabaseMetadata");
			result = null;
		}
		
		return result;
	}
	
	
	private List<FMTable> getTables(ResultSet resultSet) throws SQLException{
		  List<FMTable> tables = new ArrayList<>(); 
		  while (resultSet.next()) {
			  FMTable table = new FMTable();
			  table.setTableSchema(resultSet.getString("TABLE_CAT"));
			  table.setTableName(resultSet.getString("TABLE_NAME"));
			  table.setTableType(resultSet.getString("TABLE_TYPE"));
			  tables.add(table); 
		  }
		  return tables; 
	}
	
	private void getColumns(List<FMTable> tables, DatabaseMetaData databaseMetaData) {
		
		for (FMTable table : tables) {
			String tableName = table.getTableName();
			try {
				ResultSet columnMetadata = databaseMetaData.getColumns(null, null, tableName, null);
				List<FMColumn> columns = new ArrayList<>();
				while (columnMetadata.next()) {
					FMColumn column = new FMColumn();
					column.setTableName(tableName);
					column.setColumnName(columnMetadata.getString("COLUMN_NAME"));
					column.setColumnTypeName(columnMetadata.getString("TYPE_NAME"));
					/*
					 * if (column.getColumnTypeName().equals("ENUM")) { column.setIsEnum(true);
					 * retrieveEnumValues(column, connection); }
					 */
					column.setColumnSize(columnMetadata.getInt("COLUMN_SIZE"));
					column.setColumnDefault(columnMetadata.getString("COLUMN_DEF"));
					column.setDecimalDigits(columnMetadata.getInt("DECIMAL_DIGITS"));
					column.setIsNullable(columnMetadata.getBoolean("IS_NULLABLE"));
					column.setIsAutoincrement(columnMetadata.getBoolean("IS_AUTOINCREMENT"));
					column.setIsGenerated(columnMetadata.getBoolean("IS_GENERATEDCOLUMN"));
					columns.add(column);
				}
				table.setTableColumns(columns);
			} catch (Exception e) {
				System.out.println("EXCEPTION: Something went wrong, Method: GetColumns");
			}
		}
	}
	 
	
}
