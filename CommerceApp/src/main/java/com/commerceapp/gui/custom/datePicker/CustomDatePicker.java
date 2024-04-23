package com.commerceapp.gui.custom.datePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class CustomDatePicker extends TextField {
	private static final Logger logger = Logger.getLogger(CustomDatePicker.class.getName());
	private static final String MASK = "__/__/____";
	private int lastCaretPosition;
	private int caretPosition;
	private String text;
	private String lastText;
	private TextField textField;
	private int maxLengthDate = 10;
	private int maxLengthDateWithoutEspecialChars = 8;
	final KeyCodeCombination CTRL_V_COMBINATION = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
	private String patternDateFormat = "dd/MM/yyyy";
	private boolean pressingCTRL_V = false;
	private boolean pressingDELETE = false;
	
	public CustomDatePicker() {
		super();
		textField = getEditor();
		textField.setPromptText(MASK);
		textField.setText(MASK);
		textField.setOnKeyPressed(event -> handleKeyPressed(event));
		textField.setOnKeyTyped(event -> handleKeyTyped(event));
		textField.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
            	textField.selectAll();
            }
        });

	}
	
 	private TextField getEditor() {
		return this;
	}

	private void handleKeyPressed(KeyEvent event) {
		lastText = textField.getText();
		if (CTRL_V_COMBINATION.match(event)) {
			pressingCTRL_V = true;
			Clipboard clipboard = Clipboard.getSystemClipboard();
            String clipboardText = clipboard.getString();
            text = extractNumbers(clipboardText);
            distributeCharactersInMmask();
            String finalText = textField.getText();
            int lastIndexOfUnderline = finalText.lastIndexOf("_");
            if(lastIndexOfUnderline < 0) {
            	lastIndexOfUnderline = finalText.length();
            }
            textField.positionCaret(lastIndexOfUnderline);
            event.consume();
            return;
		}else if(event.getCode() == KeyCode.DELETE) {
			pressingDELETE = true;
			event.consume();
		}
	}
	
	private boolean isEnterPressed(KeyEvent event) {
		return event.getCode() == KeyCode.ENTER || event.getCharacter().charAt(0) == '\n' || event.getCharacter().charAt(0) == '\r';
	}
	
	private boolean isTabPressed(KeyEvent event) {
		return event.getCode() == KeyCode.TAB || event.getCharacter().charAt(0) == '\t' ;
	}

	private void handleKeyTyped(KeyEvent event) {
		text = textField.getText();
		if(pressingCTRL_V == true) {
			event.consume();
			pressingCTRL_V = false;
			return;
		}
		if(isEnterPressed(event) || isTabPressed(event)) {
			event.consume();
			return;
		}
		caretPosition = textField.getCaretPosition();
		lastCaretPosition = caretPosition - 1;
		char charTyped = event.getCharacter().charAt(0);
		boolean hasBeenReplacedByACharacter = text.length() == 1;
		if(hasBeenReplacedByACharacter && !Character.isDigit(charTyped)) {
			setText(lastText);
			textField.positionCaret(0);
			return;
		}
		if(hasBeenReplacedByACharacter && Character.isDigit(charTyped)) {
            distributeCharactersInMmask();
            textField.positionCaret(1);
            event.consume();
			return;
		}
		if(pressingDELETE == true) {
			pressingDELETE = false;
			event.consume();
			int caretPosition = textField.getCaretPosition();
			if (caretPosition > 0) {
				textField.deleteText(caretPosition - 1, caretPosition);
			}
			distributeCharactersInMmask();
			return;
		}
		
		if (isBackspacePressed(event)) {
			distributeCharactersInMmask();
			return;
		}
		
		if (isTypedIsSlashOverSlashCharText(event) ) {
			undoKeyTyped();
			textField.positionCaret(caretPosition);
			event.consume();
			return;
		}
		if(!Character.isDigit(charTyped) && event.getCode() == KeyCode.UNDEFINED) {
			undoKeyTyped();
			event.consume();
			return;
		}
		if (isAllowedCharacter(charTyped) == false
					|| exceededMaximumNumberOfCharacters()) {
	    	undoKeyTyped();
	    	event.consume();
	    	return;
		}
		if (Character.isDigit(charTyped) && (text.charAt(caretPosition) == '_' || text.charAt(caretPosition) == '/')) {
			StringBuilder newText = new StringBuilder(text);
			newText.replace(lastCaretPosition, caretPosition, "");
			if (text.charAt(caretPosition) == '/') {
				lastCaretPosition++;
				caretPosition++;
			}
			newText.replace(lastCaretPosition, lastCaretPosition + 1, event.getCharacter());
			textField.setText(newText.toString());
			textField.positionCaret(caretPosition); // Mueve el cursor una posición hacia adelante
			return;
		}
		if(text.endsWith("_")) {
			text = text.substring(0,text.length() - 1);
			distributeCharactersInMmask();
			return;
		}
		undoKeyTyped();
		event.consume();
	}

	private boolean isTypedIsSlashOverSlashCharText(KeyEvent event) {
		return event.getCharacter().equals("/") && text.charAt(caretPosition) == '/';
	}

	private boolean exceededMaximumNumberOfCharacters() {
		return caretPosition > maxLengthDate;
	}

	private boolean isAllowedCharacter(char charTyped) {
		return Character.toString(charTyped).matches("\\d") 
				 || charTyped == '/' 	
				 	|| charTyped == '_';
	}

	private void distributeCharactersInMmask() {
		String textWithoutSlashAndUnderline = text.replaceAll("/", "");
		textWithoutSlashAndUnderline = textWithoutSlashAndUnderline.replaceAll("_", "");
		StringBuilder newText = new StringBuilder();
		for (int charIndex = 0; charIndex < maxLengthDateWithoutEspecialChars; charIndex++) {
			boolean indexWithSlash = charIndex == 2 || charIndex == 4;
			if (indexWithSlash) {
				newText.append("/");
				if (textWithoutSlashAndUnderline.length() > charIndex) {
					char caracter = textWithoutSlashAndUnderline.charAt(charIndex);
					newText.append(caracter);
				} else {
					newText.append("_");
				}
			} else if (charIndex >= textWithoutSlashAndUnderline.length()) {
				newText.append("_");
			} else {
				char caracter = textWithoutSlashAndUnderline.charAt(charIndex);
				newText.append(caracter);
			}
		}
		textField.setText(newText.toString());
		textField.positionCaret(caretPosition);
	}

	private void undoKeyTyped() {
		StringBuilder newText = new StringBuilder(text);
		newText.replace(lastCaretPosition, caretPosition, "");
		textField.setText(newText.toString());
		//textField.positionCaret(caretPosition);
		textField.positionCaret(lastCaretPosition);
	}

	private boolean isBackspacePressed(KeyEvent event) {
		return event.getCode() == KeyCode.BACK_SPACE || event.getCharacter().charAt(0) == '\b';
	}
	
	private String extractNumbers(String text) {
        Pattern patron = Pattern.compile("\\d+");
        Matcher matcher = patron.matcher(text);
        StringBuilder resultado = new StringBuilder();
        while (matcher.find()) {
            resultado.append(matcher.group());
        }
        return resultado.toString();
    }
	
	public void setLocalDate(LocalDate value) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternDateFormat);
		textField.setText(formatter.format(value));
	}
	
	public void setValue(LocalDate value){
		//logger.info("setValue: " + value);
		if(value == null) {
			textField.setText(MASK);
			return;
		}
		setLocalDate(value);
	}
	
	public LocalDate getValue() {
		LocalDate localDate = getLocalDate();
		//logger.info("getValue: " + localDate);
		return localDate; 
	}
	
	public LocalDate getLocalDate() {
		String inputDate = textField.getText();
		if(inputDate.contains("_")) {
			return null;
		}
		try {
			LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern(patternDateFormat));
			return date;
		}catch(Exception ex) {
		}
		return null;
	}
}
