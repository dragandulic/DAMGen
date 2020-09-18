package com.example.DAMGenerator.transformer.model;

import com.example.DAMGenerator.transformer.model.enumeration.Visibility;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //konstruktor bez parametara
@EqualsAndHashCode(callSuper = true) //Generates hashCode and equals implementations from the fields of your object
public class Field extends Attribute{

	@Builder
	private Field(Visibility visibility, String type, Integer size, Integer precision, String fieldName,
			String columnName, Boolean isNullable, Boolean isPrimaryKey, Boolean isUnique, Boolean isGenerated) {
		this.visibility = visibility;
		this.type = type;
		this.size = size;
		this.precision = precision;
		this.fieldName = fieldName;
		this.columnName = columnName;
		this.isNullable = isNullable;
		this.isPrimaryKey = isPrimaryKey;
		this.isUnique = isUnique;
		this.isGenerated = isGenerated;
	}
}
