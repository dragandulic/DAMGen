package com.example.DAMGenerator.transformer.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassData {

	@NotEmpty
	private String tableName;
	
	private String className;
	
	private List<Field> fields;
	
	private List<String> compositePks;
	
	private List<Property> properties;
	
	private CompositeKey compositeKey;
	
	@Builder.Default 
	private Boolean generateClass = true;
	  
	@Builder.Default 
	private Boolean generateRepository = true;
	
	private Service service;

	private Relationship relationship;
	
	private Controller controller;
	
	  //private List<Enumeration> enums;
	  
	  //private Property manyToManyProperty;
	  
	  //private Relationship relationship;
	 
	  
	  
	 
	 
}
