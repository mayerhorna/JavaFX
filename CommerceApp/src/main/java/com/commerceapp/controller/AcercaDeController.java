package com.commerceapp.controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import com.commerceapp.Main;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.IdiomaC.AyudaUtils;
import com.commerceapp.util.Utilidades;

public class AcercaDeController implements Initializable {

	@FXML
	Pane frmAcercaDe;

	@FXML
	Label lblVersion;

	@FXML
	Label lblCopyright;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lblVersion.setText(lblVersion.getText() + " " + MGeneral.Configuracion.getVersion());
		String companyAttr = MGeneral.Configuracion.getAssemblycompanyattribute();
		String copyrtAttr = MGeneral.Configuracion.getAssemblycopyrightattribute();
		lblCopyright.setText(companyAttr + " " + copyrtAttr);

	}

	@FXML
	private void pbxColegio_Click() {
		Utilidades.ProccessStarURL(MGeneral.KUrlRegistradores);
	}

	@FXML
	private void pbxSip_Click() {
		Utilidades.ProccessStarURL(MGeneral.kUrlSip2000);
	}

	@FXML
	private void pbx3eTrade_Click() {
		Utilidades.ProccessStarURL(MGeneral.kUrl3eTrade);
	}

	@FXML
	public void atajoVerUrlCentral(KeyEvent event) throws IOException {
		if (event.isControlDown() && event.getCode() == KeyCode.D) {

			Alert alert = new Alert(AlertType.INFORMATION,MGeneral.Configuracion.getUrlXmlCentral());
			
			alert.setTitle("Informacion URL XML CENTRAL");
			alert.setHeaderText(null);
			
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// stage.getIcons().clear();
					stage.getIcons().add(new Image(getClass().getResource("/imagenes/IconoColegioR.png").toString()));

				}
			});
			
			alert.showAndWait();

		}
	}

}
