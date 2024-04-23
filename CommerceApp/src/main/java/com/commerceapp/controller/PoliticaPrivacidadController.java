package com.commerceapp.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.commerceapp.domain.MGeneral;
import com.commerceapp.util.Utilidades;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PoliticaPrivacidadController implements Initializable {

	@FXML
	private Button btnAceptar;

	@FXML
	private VBox grpRespuesta;

	@FXML
	private Hyperlink lnklblPoliticaPrivacidad;

	@FXML
	private CheckBox rbtAceptar;

	@FXML
	private CheckBox rbtRechazar;

	@FXML
	private TextArea txtAviso;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		MGeneral.RetornoFormulario = false;

		controlesActivos();

		

		Platform.runLater(new Runnable() {
			public void run() {

				lnklblPoliticaPrivacidad.addEventHandler(ActionEvent.ACTION,
						event -> lnklblPoliticaPrivacidad_LinkClicked(event));

				btnAceptar.addEventHandler(ActionEvent.ACTION, event -> btnAceptarClicked(event));

				rbtAceptar.setOnAction(event -> {
					controlesActivos();
					if (rbtAceptar.isSelected()) {
						rbtRechazar.setSelected(false);
					}
				});

				rbtRechazar.setOnAction(event -> {
					controlesActivos();
					if (rbtRechazar.isSelected()) {
						rbtAceptar.setSelected(false);
					}
				});
			}
		});
	}

	private void controlesActivos() {
		btnAceptar.setDisable(!rbtAceptar.isSelected() && !rbtRechazar.isSelected());

	}

	void btnAceptarClicked(ActionEvent event) {

		MGeneral.RetornoFormulario = rbtAceptar.isSelected();

		Node source = (Node) event.getSource(); 
		Stage stage = (Stage) source.getScene().getWindow(); 
		stage.close();
	}

	private void lnklblPoliticaPrivacidad_LinkClicked(ActionEvent event) {
		Utilidades.ProccessStarURL("https://www.registradores.org/registroVirtual/privacidad.do");
	}
}
