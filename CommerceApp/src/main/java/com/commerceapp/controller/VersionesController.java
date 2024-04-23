package com.commerceapp.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;

import ch.qos.logback.classic.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VersionesController implements Initializable {

	private MenuPrincipalController parentController;

	MenuPrincipalController getParentController() {
		return parentController;
	}

	void setParentController(MenuPrincipalController parentController) {
		this.parentController = parentController;
	}

	private boolean actualizacionObligatoria;
	private String contenido;

	@FXML
	private Label lblContenido;
	@FXML
	private Label lblNoObligatoria;

	@FXML
	private Label lblObligatoria;

	@FXML
	private TextArea txtContenido;

	@FXML
	private Button btnActualizar;

	@FXML
	private Button btnNoActualizar;

	@FXML
	private VBox frmVersiones;

	public boolean isActualizacionObligatoria() {
		return actualizacionObligatoria;
	}

	public void setActualizacionObligatoria(boolean actualizacionObligatoria) {
		this.actualizacionObligatoria = actualizacionObligatoria;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MGeneral.RetornoFormulario = false;

		cargar();
		Platform.runLater(() -> {
			
			Stage stage = (Stage) frmVersiones.getScene().getWindow();

		
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					System.exit(0);
				}
			});

		});

		// TODO Auto-generated method stub

	}

	private void cargar() {

		btnNoActualizar.setDisable(actualizacionObligatoria);
		lblNoObligatoria.setVisible(!actualizacionObligatoria);
		lblNoObligatoria.setManaged(!actualizacionObligatoria);

		txtContenido.setText(contenido);

		if (contenido == null || contenido.isEmpty()) {
			lblContenido.setVisible(false);
			txtContenido.setVisible(false);
			lblContenido.setManaged(false);
			txtContenido.setManaged(false);

		}
	}

	@FXML
	void btnActualizar_Click(ActionEvent event) {
		MGeneral.RetornoFormulario = true;
		Stage stage = (Stage) btnActualizar.getScene().getWindow();
		stage.close();
	}

	@FXML
	void btnNoActualizar_Click(ActionEvent event) {
		Stage stage = (Stage) btnNoActualizar.getScene().getWindow();
		stage.close();
		System.exit(0);
	}

}
