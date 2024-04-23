package com.commerceapp.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MensajeController implements Initializable{
    @FXML
    private Button btnAceptar;

    @FXML
    private GridPane frmMensaje;

    @FXML
    private TextArea txtMensaje;
    
	private String _mensaje;


	public String get_mensaje() {
		return _mensaje;
	}
	public void set_mensaje(String _mensaje) {
		this._mensaje = _mensaje;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 txtMensaje.setText(_mensaje); 
		 registerEventControl() ;
	}
	private void registerEventControl() {
		btnAceptar.addEventHandler(ActionEvent.ACTION, event -> {
			try {
				aceptar(event);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	public void aceptar(ActionEvent e) throws Exception {

		if (e != null) {
			Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
			Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el
																	// elemento
			stage.close();
		}
		// Me cierra la ventana
	}
}
