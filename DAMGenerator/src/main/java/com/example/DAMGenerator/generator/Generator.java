package com.example.DAMGenerator.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.example.DAMGenerator.generator.service.TemplateType;

import freemarker.template.Configuration;
import freemarker.template.Template;

public abstract class Generator {

	@Autowired
	public FreeMarkerConfig freemarkerConfig;
	
	protected Template getTemplate(TemplateType templateType) {
		
		Configuration configuration = freemarkerConfig.getConfiguration();
		Template template = null;
		String templateName = findTemplateName(templateType);
		try {
			template = configuration.getTemplate(templateName);
		} catch (Exception e) {
			System.out.println(e);
		}
		return template;
	}

	protected Writer getWriter(final String packagePath) throws IOException {
		File outputFile = new File(packagePath);
		outputFile.getParentFile().mkdirs();
		return new OutputStreamWriter(new FileOutputStream(outputFile));
	}

	private String findTemplateName(TemplateType type) {
		switch (type) {
		case ENUMERATION:
			return "enum.ftl";
		case MODEL:
			return "class.ftl";
		case EMBEDDED_KEY:
			return "embeddedkey.ftl";
		case REPOSITORY:
			return "repository.ftl";
		case SERVICE:
			return "service.ftl";
		case CONTROLLER:
			return "controller.ftl";
		case POM:
			return "pom.ftl";
		case APPLICATION:
			return "application.ftl";
		case YAML:
			return "yaml.ftl";
		case HTML:
			return "html.ftl";
		case CRUD: 
			return "crud.ftl";
		case NAMING_STRATEGY:
			return "naming.ftl";
		default:
			return "";
		}
	}
	
}
