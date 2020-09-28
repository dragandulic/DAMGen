package com.example.DAMGenerator.generator.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.http.fileupload.FileUtils;
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
		generateScriptsAndStylesheets(path);
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
			    	if(!oldFile.getContent().equals(generatedFile))
			    	{
			    		output.delete();
			    		byte[] bytes = oldFile.getContent().getBytes();
						Files.write(output.toPath(), bytes);
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
            File initialImage = new File(classLoader.getResource("images/background.jpg").getFile());
            bImage = ImageIO.read(initialImage);

            ImageIO.write(bImage, "png", new File(path+"/resources/static/background.jpg"));
            

        } catch (IOException e) {
              System.out.println("Exception occured :" + e.getMessage());
        }
        System.out.println("Images were written succesfully.");
	}
	private void generateScriptsAndStylesheets(String path)
	{
		ClassLoader classLoader = getClass().getClassLoader();
        File images = new File(classLoader.getResource("images").getFile());
        BufferedImage bImage = null;
        new File(path+"/resources/static/images").mkdirs();
		
		for (File file : images.listFiles()) {
			if(!file.isDirectory() && !file.getName().equals("background.jpg")) {
			  try {
				bImage = ImageIO.read(file);
				if(file.getName().contains(".png")) {
				ImageIO.write(bImage, "png", new File(path+"/resources/static/images/"+file.getName()));
				}
				else
				{
			    ImageIO.write(bImage, "gif", new File(path+"/resources/static/images/"+file.getName()));	
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	      }
		new File(path+"/resources/static/scripts").mkdirs();
		File scripts = new File(classLoader.getResource("static/scripts").getFile());
		
		for (File file : scripts.listFiles()) {

			try {
			    
			    Files.copy(file.toPath(),
			            (new File(path+"/resources/static/scripts/"+file.getName()).toPath()),
			            StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
			    e.printStackTrace();
			}
	     
			
		
	      }
		
		new File(path+"/resources/static/stylesheets").mkdirs();
		File stylesheets = new File(classLoader.getResource("static/stylesheets").getFile());
		
		for (File file : stylesheets.listFiles()) {

			try {
			    
			    Files.copy(file.toPath(),
			            (new File(path+"/resources/static/stylesheets/"+file.getName()).toPath()),
			            StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
			    e.printStackTrace();
			}
	     
			
		
	      }
		
		
		
		
		
		}
	}

