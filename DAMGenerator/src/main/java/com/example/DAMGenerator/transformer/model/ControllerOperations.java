package com.example.DAMGenerator.transformer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ControllerOperations {
	
	@Builder.Default
	private String controllerPath = "generated";
	
	@Builder.Default
	private Boolean get = true;
	
	@Builder.Default
	private Boolean post = true;
	
	@Builder.Default
	private Boolean put = true;
	
	@Builder.Default
	private Boolean delete = true;

}
