package com.example.DAMGenerator.transformer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CompositeKey {

	private String tableName;
	
	private List<Field> fields;
	
	private List<Property> properties;
}
