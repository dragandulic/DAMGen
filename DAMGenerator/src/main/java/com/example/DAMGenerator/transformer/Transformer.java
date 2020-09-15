package com.example.DAMGenerator.transformer;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.example.DAMGenerator.database.model.FMTable;
import com.example.DAMGenerator.transformer.helper.ClassNameHelper;
import com.example.DAMGenerator.transformer.model.ClassData;

@Component
public class Transformer {

	
	public ClassData createClass(FMTable table) {
		
		ClassData classData = ClassData.builder()
				.tableName(table.getTableName())
				.className(ClassNameHelper.toClassName(table.getTableName()))
				.build();

		return classData;
	}
}
