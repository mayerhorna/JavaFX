package com.commerceapp;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import com.commerceapp.domain.MGeneral;
import com.commerceapp.service.StageReadyEvent;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;

//chartApplication
public class Main extends Application {
	private static HostServices hostServices;

	private static final Logger logger = Logger.getLogger(Main.class.getName());

	private ConfigurableApplicationContext applicationContext;

	

	@Override
	public void init() {
		hostServices = getHostServices();
		logger.info("init");
		applicationContext = new SpringApplicationBuilder(CommerceApp.class).run();

	}

	@Override
	public void stop() {

		logger.info("stop");

		applicationContext.close();
		Platform.exit();
	}

	@Override
	public void start(Stage stage) throws IOException {
		logger.info("start");
		//logger.info(stage.toString());
		applicationContext.publishEvent(new StageReadyEvent(stage));
		applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
		
	}

	public static HostServices getHostService() {
		return hostServices;
	}

}