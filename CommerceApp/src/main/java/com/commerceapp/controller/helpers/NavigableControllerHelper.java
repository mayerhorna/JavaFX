package com.commerceapp.controller.helpers;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public interface NavigableControllerHelper {
	static final Logger logger = Logger.getLogger(NavigableControllerHelper.class.getName());
	abstract void initializeControlsInOrderToNavigate();
	abstract Control[] getControlsInOrderToNavigate();
	 
	public default void init(URL location, ResourceBundle resources) {
		initializeControlsInOrderToNavigate();
		registerKeyPressENTERInControlsToNavigate();
	}

	public default void moveFocusToNextElement(KeyEvent event) {
		Control currentField = (Control) event.getSource();
		if (event.getCode() == KeyCode.ENTER && currentField instanceof Button) {
	        Button button = (Button) currentField;
	        button.fire();
	        return;
	    }
		int currentIndex = getIndexOfControl(currentField);
		Control[] controlsInOrderToNavigate = getControlsInOrderToNavigate();
		int nextIndex = (currentIndex + 1) % controlsInOrderToNavigate.length;
		Control control = controlsInOrderToNavigate[nextIndex];
		if(control.isDisabled()) {
			boolean startInIndexZero = false;
			while(controlsInOrderToNavigate.length > nextIndex && control.isDisabled()) {
				++nextIndex;
				if(nextIndex == controlsInOrderToNavigate.length) {
					if(startInIndexZero == true) {
						break;
					}
					nextIndex = 0;
					startInIndexZero = true;
				}
				control = controlsInOrderToNavigate[nextIndex];
			}
		}
		control.requestFocus();
	}

	public default int getIndexOfControl(Control field) {
		Control[] controlsInOrderToNavigate = getControlsInOrderToNavigate();
		for (int i = 0; i < controlsInOrderToNavigate.length; i++) {
			if (controlsInOrderToNavigate[i] == field) {
				return i;
			}
		}
		return -1;
	}

	public default void registerKeyPressENTERInControlsToNavigate() {
		Control[] controlsInOrderToNavigate = getControlsInOrderToNavigate();
		for (Control control : controlsInOrderToNavigate) {
			control.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
				logger.info("KeyTyped.event.getCode() : " + event.getCode());
				char charAt = event.getCharacter().charAt(0);
				if (event.getCode() == KeyCode.ENTER || charAt == '\n'
						|| charAt == '\r' || event.getCode() == KeyCode.TAB || charAt == '\t') {
					logger.info("event.getCode(): " + event.getCode());
					logger.info("Controles: "+Arrays.toString(getControlsInOrderToNavigate()));
					event.consume();
					moveFocusToNextElement(event);
					return;
				}
			});
		}
	}
	
	public default void onRenderStage(Scene scene){
		scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.TAB) {
				event.consume();  
			}
		});
	}
}
