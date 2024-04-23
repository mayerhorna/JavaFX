package com.commerceapp.service;

import java.util.logging.Logger;


import org.springframework.context.ApplicationEvent;

import javafx.stage.Stage;

public class StageReadyEvent extends ApplicationEvent {
	private static final Logger logger = Logger.getLogger(StageReadyEvent.class.getName());
	
	public StageReadyEvent(Stage stage) {		
		super(stage);				
		logger.info("StageReadyEvent");		
	}
	public Stage getStage() {
		logger.info("getStage");
		return ((Stage) getSource());
	}
	
}

