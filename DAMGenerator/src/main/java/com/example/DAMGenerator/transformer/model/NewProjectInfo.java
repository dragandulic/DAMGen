package com.example.DAMGenerator.transformer.model;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class NewProjectInfo {

	@NotEmpty
	private String basePath;
	
	@NotEmpty
	private String artifactId;
	
	@NotEmpty
	private String groupId;
	
	@NotEmpty
	private String projectName;
	
	@NotEmpty
	private String basePackageName;
	
	private String description;
	
	@NotEmpty
	private String version;
	
}
