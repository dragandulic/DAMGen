package com.example.DAMGenerator.transformer.model;

import javax.validation.constraints.NotEmpty;

import com.example.DAMGenerator.transformer.model.enumeration.Visibility;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Attribute {

	@NotEmpty
	protected Visibility visibility;
	
	@NotEmpty
	protected String type;
	
	@NotEmpty
	protected Integer size;
	
	protected Integer precision;
	
	@NotEmpty
	protected String fieldName;
	
	@NotEmpty
	protected String columnName;
	
	@NotEmpty
	protected Boolean isNullable;
	
	@NotEmpty
	protected Boolean isPrimaryKey;
	
	@NotEmpty
	protected Boolean isUnique;
	
	@NotEmpty
	protected Boolean isGenerated;
}
