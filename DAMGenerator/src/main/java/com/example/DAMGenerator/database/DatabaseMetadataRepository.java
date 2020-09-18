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
import com.example.DAMGenerator.database.model.FMForeignKey;
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
			  getPrimaryKeys(tables, databaseMetaData);
			  getForeignKeys(tables, databaseMetaData);
			  getIndexedColumns(tables, databaseMetaData);
			  
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
	
	private void getPrimaryKeys(List<FMTable> tables, DatabaseMetaData databaseMetaData) {
		
		for(FMTable table : tables) {
			
			List<String> primaryKeysPerTable = new ArrayList<>();
			table.setCompositePrimaryKeyColumns(new ArrayList<>());
			try {
				ResultSet primaryKeyMetadata = databaseMetaData.getPrimaryKeys(null, null, table.getTableName());
				while(primaryKeyMetadata.next()) {
					String primaryKeyColumn = primaryKeyMetadata.getString("COLUMN_NAME");
					for(FMColumn column : table.getTableColumns()) {
						if(column.getColumnName().equals(primaryKeyColumn)) {
							column.setIsPrimaryKey(true);
							column.setIsUnique(true);
							primaryKeysPerTable.add(primaryKeyColumn);
						}
					}
				}
			}catch(Exception e) {
				System.out.println("EXCEPTION: Something went wrong, Method: GetPrimaryKeys");
			}
			
			if(primaryKeysPerTable.size() >= 2) {
				table.getCompositePrimaryKeyColumns().addAll(primaryKeysPerTable);
			}
			primaryKeysPerTable.clear();
		}
	}
	
	private void getForeignKeys(List<FMTable> tables, DatabaseMetaData databaseMetaData) {
		
		for(FMTable table : tables) {
			
			try {
				ResultSet foreignKeyMetadata = databaseMetaData.getImportedKeys(null, null, table.getTableName());
				List<FMForeignKey> foreignKeys = new ArrayList<>();
				while(foreignKeyMetadata.next()) {
					FMForeignKey foreignKey = new FMForeignKey();
					foreignKey.setPkTableSchema(foreignKeyMetadata.getString("PKTABLE_CAT"));
					foreignKey.setPkTableName(foreignKeyMetadata.getString("PKTABLE_NAME"));
					foreignKey.setPkColumnName(foreignKeyMetadata.getString("PKCOLUMN_NAME"));
					
					foreignKey.setFkTableSchema(foreignKeyMetadata.getString("FKTABLE_CAT"));
					foreignKey.setFkTableName(foreignKeyMetadata.getString("FKTABLE_NAME"));
					foreignKey.setFkColumnName(foreignKeyMetadata.getString("FKCOLUMN_NAME"));
					
					foreignKey.setUpdateRule(determineUpdateRule(foreignKeyMetadata.getString("UPDATE_RULE")));
					foreignKey.setDeleteRule(determineDeleteRule(foreignKeyMetadata.getString("DELETE_RULE")));
				
					for(FMColumn column : table.getTableColumns()) {
						if(column.getColumnName().toUpperCase().equals(foreignKey.getFkColumnName().toUpperCase())) {
							column.setForeignKeyInfo(foreignKey);
						}
					}
					foreignKeys.add(foreignKey);
				}
			} catch(Exception e) {
				System.out.println("EXCEPTION: Something went wrong, Method: GetForeignKeys");
			}
		}
	}
	
	private void getIndexedColumns(List<FMTable> tables, DatabaseMetaData metadata) {
		
		for(FMTable table : tables) {
			
			try {
				ResultSet indexedColumnMetadata = metadata.getIndexInfo(null, null, table.getTableName(), true, false);
				List<String> uniqueColumns = new ArrayList<>();
				while(indexedColumnMetadata.next()) {
					String uniqueColumn = indexedColumnMetadata.getString("COLUMN_NAME");
					uniqueColumns.add(uniqueColumn);
					for(FMColumn column : table.getTableColumns()) {
						if(column.getColumnName().equals(uniqueColumn)) {
							column.setIsUnique(true);
						}
					}
				}
				table.setUniqueColumns(uniqueColumns);
			}catch(Exception e) {
				System.out.println("EXCEPTION: Something went wrong, Method: GetIndexedColumns");
			}
		}
	}
	
	private String determineUpdateRule(String index) {
		switch (index) {
		case "0":
			return "importedNoAction";
		case "1":
			return "importedKeyCascade";
		case "2":
			return "importedKeySetNull";
		case "3":
			return "importedKeySetDefault";
		case "4":
			return "importedKeyRestrict";
		default:
			return "";
		}
	}

	private String determineDeleteRule(String index) {
		switch (index) {
		case "0":
			return "importedKeyNoAction";
		case "1":
			return "importedKeyCascade";
		case "2":
			return "importedKeySetNull";
		case "3":
			return "importedKeyRestrict";
		case "4":
			return "importedKeySetDefault";
		default:
			return "";
		}
	}
	
}
