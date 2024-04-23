package com.commerceapp.service;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
@Component
public class CustomResourceLoader implements ResourceLoaderAware{
	
	private final ResourcePatternResolver resourcePatternResolver;	
	private ResourceLoader resourceLoader;
	private ApplicationContext applicationContext;
	
	public Scene load(Parent parent, String cssPath) throws IOException {
        
		Scene scene= new Scene(parent);

        // Aplicar el archivo CSS si se proporciona
        if (cssPath != null && !cssPath.isEmpty()) {
        	scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());           
        }

        return scene;
    }

    
    public CustomResourceLoader() {
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
    }

    public Image loadImage(String location) throws IOException{
        Resource resource = resourcePatternResolver.getResource("classpath:imagenes/" + location);
        return new Image(resource.getURL().toString());
    }

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		 this.resourceLoader = resourceLoader;		
	}
}
