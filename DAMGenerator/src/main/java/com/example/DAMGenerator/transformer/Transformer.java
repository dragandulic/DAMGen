package com.example.DAMGenerator.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.DAMGenerator.database.model.FMColumn;
import com.example.DAMGenerator.database.model.FMDatabaseMetadata;
import com.example.DAMGenerator.database.model.FMForeignKey;
import com.example.DAMGenerator.database.model.FMTable;
import com.example.DAMGenerator.transformer.helper.ClassNameHelper;
import com.example.DAMGenerator.transformer.model.ClassData;
import com.example.DAMGenerator.transformer.model.CompositeKey;
import com.example.DAMGenerator.transformer.model.Field;
import com.example.DAMGenerator.transformer.model.MakeClasses;
import com.example.DAMGenerator.transformer.model.MakeNewProject;
import com.example.DAMGenerator.transformer.model.NewProjectDbConnection;
import com.example.DAMGenerator.transformer.model.NewProjectInfo;
import com.example.DAMGenerator.transformer.model.Property;
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
		processForeignKeys(classData, table.getTableColumns());
		processCompositeKeyColumns(classData, table);
		
		return classData;
	}
	
	private void processField(ClassData classData, List<FMColumn> columns) {
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
					.type(determineDataType(fmColumn.getColumnTypeName()))
					.visibility(Visibility.PRIVATE)
					.size(fmColumn.getColumnSize())
					.precision(fmColumn.getDecimalDigits())
					.isUnique(fmColumn.getIsUnique())
					.isGenerated(fmColumn.getIsGenerated())
					.isNullable(fmColumn.getIsNullable())
					.isPrimaryKey(fmColumn.getIsPrimaryKey())
					.build();
			result.add(field);
		}
		classData.setFields(result);
	}
	
	private void processForeignKeys(ClassData classData, List<FMColumn> columns) {
		List<FMColumn> fmColumns = new ArrayList<>();
		for(FMColumn column : columns) {
			if(column.getForeignKeyInfo() != null) {
				fmColumns.add(column);
			}
		}
		
		List<Property> result = new ArrayList<>();
		for(FMColumn fmColumn : fmColumns) {
			FMForeignKey foreignKey = fmColumn.getForeignKeyInfo();
			Property property = Property.builder()
					.columnName(foreignKey.getFkColumnName())
					.fieldName(ClassNameHelper.toFieldName(foreignKey.getFkColumnName()))
					.pkTableName(foreignKey.getPkTableName())
					.pkClassName(ClassNameHelper.toClassName(foreignKey.getPkTableName()))
					.pkColumnName(foreignKey.getPkColumnName())
					.type(determineDataType(fmColumn.getColumnTypeName()))
					.visibility(Visibility.PRIVATE)
					.size(fmColumn.getColumnSize())
					.precision(fmColumn.getDecimalDigits())
					.isNullable(fmColumn.getIsNullable())
					.isPrimaryKey(fmColumn.getIsPrimaryKey())
					.isGenerated(fmColumn.getIsGenerated())
					.isUnique(fmColumn.getIsUnique())
					.fetch("LAZY")
					.orphanRemoval(false)
					.isSelfReferenced(false)
					.build();
			if(fmColumn.getTableName().toLowerCase().equals(foreignKey.getPkTableName().toLowerCase())){
				property.setIsSelfReferenced(true);
			}
			result.add(property);
		}
		classData.setProperties(result);
	}
	
	private void processCompositeKeyColumns(ClassData classData, FMTable table) {
		
		if(table.getCompositePrimaryKeyColumns() != null) {
			for(String name : table.getCompositePrimaryKeyColumns()) {
				for(Field field  : classData.getFields()) {
					if(field.getColumnName().equals(name)) {
						classData.getCompositePks().add(field.getColumnName());
					}
				}
				for(Property property  : classData.getProperties()) {
					if(property.getColumnName().equals(name)) {
						classData.getCompositePks().add(property.getColumnName());
					}
				}
			}
		}
		
		if(!classData.getCompositePks().isEmpty()) {
			
			CompositeKey compositeKey = new CompositeKey(classData.getTableName(), new ArrayList<Field>(), 
											new ArrayList<Property>());
			
			for(String compositePks : classData.getCompositePks()) {
				for(Field field : classData.getFields()) {
					if(field.getColumnName().equals(compositePks)) {
						compositeKey.getFields().add(field);
					}
				}
				for(Property property : classData.getProperties()) {
					if(property.getColumnName().equals(compositePks)) {
						compositeKey.getProperties().add(property);
					}
				}
			}
			classData.setCompositeKey(compositeKey);
		}	
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



