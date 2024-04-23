package com.commerceapp.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.commerceapp.domain.MGeneral;

import io.micrometer.observation.Observation.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ZipGeneradoController implements Initializable {

	@FXML
	AnchorPane frmZipGenerado;

	@FXML
	TextField txtPathZip;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//MGeneral.Idioma.cargarIdiomaControles(null, frmZipGenerado);
		txtPathZip.setText(MGeneral.mlform.getPathFicheroZip());
	}

	@FXML
	private void btnCerrar_Click(ActionEvent e) {
		//MGeneral.mlform.vProgreso.finaliza();
		close(e);
	}

	@FXML
	private void btnCopiarPortapaleles_Click(ActionEvent e) {

		ClipboardContent contentEncriptado = new ClipboardContent();
		contentEncriptado.putString(MGeneral.mlform.getPathFicheroZip());
		Clipboard.getSystemClipboard().setContent(contentEncriptado);
		close(e);
	}

	private void close(ActionEvent e) {

		Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
		Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el elemento
		stage.close();
	}

}
