package com.example.DAMGenerator.generator.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.example.DAMGenerator.generator.Generator;
import com.example.DAMGenerator.transformer.model.ClassData;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MakePageService extends Generator {
	
	private List<String> imports = new ArrayList<>();
	
	public void generatePage(String path, String packageName, List<ClassData> classes, List<OldFile> oldFiles) {
		
		Template t = getTemplate(TemplateType.HTML);
		Writer out = null;
		path = path.split("java")[0];
		generateBackgroundImage(path);
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter(path+"resources/static/dashboard.html");
			context.put("title", "Welcome");
			t.process(context, out);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ClassData classData : classes) {
			generatePageForClass(classData, path, packageName, oldFiles);
		}
	}

	private void generatePageForClass(ClassData classData, String path, String packageName, List<OldFile> oldFiles) {

		Template t = getTemplate(TemplateType.CRUD);
		Writer out = null;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter(path+"resources/static/"+classData.getClassName()+".html");
			context.put("className", classData.getClassName());
			t.process(context, out);
			out.flush();
			{
			for (OldFile oldFile : oldFiles) {
			    if (oldFile.getFilename().equals(classData.getClassName()+".html")) {
			    	File output = new File(path+"resources/static/"+classData.getClassName()+".html");
			    	String generatedFile = new String(Files.readAllBytes(output.toPath()));
			    	if(oldFile.getContent().equals(generatedFile))
			    	{
			    		oldFile.setFilename("/");
			    	}
			    }  }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private void generateBackgroundImage(String path)
	{
		BufferedImage bImage = null;
        try {
        	ClassLoader classLoader = getClass().getClassLoader();
            File initialImage = new File(classLoader.getResource("images/background.png").getFile());
            bImage = ImageIO.read(initialImage);

            ImageIO.write(bImage, "png", new File(path+"/resources/static/background.png"));
            

        } catch (IOException e) {
              System.out.println("Exception occured :" + e.getMessage());
        }
        System.out.println("Images were written succesfully.");
	}

}