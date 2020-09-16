package com.example.DAMGenerator.transformer.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MakeClasses {

	@NotNull
	public String path;

	@NotNull
	private List<ClassData> classes;
}
