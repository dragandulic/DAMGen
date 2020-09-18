package com.example.DAMGenerator.transformer.model;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;

@Data
public class MakeNewProject {

	private List<ClassData> classes;
	
	@Valid
	private NewProjectInfo newProjectInfo;
	
	@Valid
	private NewProjectDbConnection databaseConnection;
}
