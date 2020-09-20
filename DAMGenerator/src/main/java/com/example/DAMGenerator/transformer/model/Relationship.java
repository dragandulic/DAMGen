package com.example.DAMGenerator.transformer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Relationship {

	@Builder.Default
	private Boolean isRelationshipClass = false;

	@Builder.Default
	private String relationship = "";
}
