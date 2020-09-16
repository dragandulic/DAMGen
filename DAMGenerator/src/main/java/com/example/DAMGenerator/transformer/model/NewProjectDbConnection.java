package com.example.DAMGenerator.transformer.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewProjectDbConnection {

	@NotEmpty
	private String driverName;
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	@Pattern(regexp = "jdbc:[a-zA-Z]*:\\/\\/[a-zA-Z0-9]*:[0-9]{4}\\/[a-zA-Z0-9]+.*")
	private String url;
	
	@NotEmpty
	private String password;
	
	@Builder.Default
	private Boolean overrideNamingConvention = false;
}
