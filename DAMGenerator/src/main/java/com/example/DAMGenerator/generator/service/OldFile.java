package com.example.DAMGenerator.generator.service;

public class OldFile {
	
	private String filename;
	private String content;
	
	public OldFile(String filename, String content) {
		super();
		this.filename = filename;
		this.content = content;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
