package com.example.DAMGenerator.generator.service;

public class OldFile {
	
	private String filename;
	private String content;
	private String path;
	
	
	public OldFile(String filename, String content, String path) {
		super();
		this.filename = filename;
		this.content = content;
		this.path = path;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	

}
