package com.example.DAMGenerator.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.DAMGenerator.database.model.FMColumn;
import com.example.DAMGenerator.database.model.FMDatabaseMetadata;
import com.example.DAMGenerator.database.model.FMTable;
import com.example.DAMGenerator.transformer.helper.ClassNameHelper;
import com.example.DAMGenerator.transformer.model.ClassData;
import com.example.DAMGenerator.transformer.model.Field;
import com.example.DAMGenerator.transformer.model.MakeClasses;
import com.example.DAMGenerator.transformer.model.MakeNewProject;
import com.example.DAMGenerator.transformer.model.NewProjectDbConnection;
import com.example.DAMGenerator.transformer.model.NewProjectInfo;
import com.example.DAMGenerator.transformer.model.enumeration.Visibility;

@Component
public class Transformer {

	
	
	public MakeClasses transforMetadataToClasses(FMDatabaseMetadata databaseMetadata) {
		
		MakeClasses result = new MakeClasses();
		
		List<ClassData> classData = new ArrayList<>();
		for (FMTable table : databaseMetadata.getTables()) {
			classData.add(processClass(table));
		}
		result.setClasses(classData);
		
		return result;
	} 
	
	public MakeNewProject transformMetadataForNewProject(FMDatabaseMetadata databaseMetadata) {
		
		MakeNewProject result = new MakeNewProject();
		
		result.setNewProjectInfo(new NewProjectInfo());
		
		NewProjectDbConnection dbConnection = NewProjectDbConnection.builder()
				.url(databaseMetadata.getUrl())
				.password(null)
				.username(databaseMetadata.getUsername())
				.driverName(databaseMetadata.getDriverName())
				.build();
		
		result.setDatabaseConnection(dbConnection);
		List<ClassData> classData = new ArrayList<>();
		for (FMTable table : databaseMetadata.getTables()) {
			classData.add(processClass(table));
		}
		result.setClasses(classData);
		//determineRelationTables(classes);
		
		return result;
	}
	
	
	
	
	public ClassData processClass(FMTable table) {
		
		ClassData classData = ClassData.builder()
				.tableName(table.getTableName())
				.className(ClassNameHelper.toClassName(table.getTableName()))
				.compositePks(new ArrayList<>())
				.build();
		
		processField(classData, table.getTableColumns());

		return classData;
	}
	
	
	public void processField(ClassData classData, List<FMColumn> columns) {
		List<FMColumn> fmColumns = new ArrayList<>();
		for (FMColumn column : columns) {
			if(!column.getIsEnum() && column.getForeignKeyInfo() == null) {
				fmColumns.add(column);
			}
		}
		List<Field> result = new ArrayList<>();
		for (FMColumn fmColumn : fmColumns) {
			Field field = Field.builder()
					.columnName(fmColumn.getColumnName())
					.fieldName(ClassNameHelper.toFieldName(fmColumn.getColumnName()))
					.precision(fmColumn.getDecimalDigits())
					.visibility(Visibility.PRIVATE)
					.isUnique(fmColumn.getIsUnique())
					.isGenerated(fmColumn.getIsGenerated())
					.size(fmColumn.getColumnSize())
					.isNullable(fmColumn.getIsNullable())
					.type(determineDataType(fmColumn.getColumnTypeName()))
					.isPrimaryKey(fmColumn.getIsPrimaryKey())
					.build();
			result.add(field);
		}
		classData.setFields(result);
	}
	
	
	
	private String determineDataType(String dbDataType) {

		switch (dbDataType) {
		case "TIMESTAMP":
		case "DATE":
		case "DATETIME":
		case "YEAR":
			return "Date";
		case "CHAR":
		case "VARCHAR":
		case "TEXT":
			return "String";
		case "BIT":
		case "BOOLEAN":
			return "Boolean";
		case "DOUBLE":
		case "FLOAT":
		case "DECIMAL":
			return "Double";
		case "MEDIUMBLOB":
		case "MEDIUMTEXT":
		case "GEOMETRY":
		case "BLOB":
			return "Blob";
		case "INTEGER":
		case "INT":
		case "NUMERIC":
		case "SMALLINT":
		case "SMALLINT UNSIGNED":
		case "TINYINT UNSIGNED":
		case "MEDIUMINT UNSIGNED":
			return "Integer";
		case "BIGINT":
			return "Long";
		default:
			return "Unknown data type";
		}
	}
}



