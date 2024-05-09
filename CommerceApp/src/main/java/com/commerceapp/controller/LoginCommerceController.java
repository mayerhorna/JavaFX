package com.commerceapp.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.commerceapp.app.JPAControllerBa_user;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.domain.MGeneral;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginCommerceController implements Initializable {

	@FXML
	TextField loginCommerce;

	@FXML
	TextField passCommerce;

	@FXML
	AnchorPane frmLogin;
	private Stage stagePrincipal;

	JPAControllerBa_user objControllerBa_user;

	public Stage getStagePrincipal() {
		return stagePrincipal;
	}

	public void setStagePrincipal(Stage stagePrincipal) {
		this.stagePrincipal = stagePrincipal;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		objControllerBa_user = new JPAControllerBa_user();
		loginCommerce.requestFocus();
		Platform.runLater(() -> {

			Stage stage = (Stage) frmLogin.getScene().getWindow();

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					System.exit(0);
				}
			});

		});

		frmLogin.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				try {
					MGeneral.Configuracion.objBaUser = objControllerBa_user
							.buscarUsuarioPorCredenciales(loginCommerce.getText(), passCommerce.getText());
					if (MGeneral.Configuracion.objBaUser != null) {

						Stage stage = (Stage) frmLogin.getScene().getWindow(); // Me devuelve la ventana donde se
																				// encuentra el elemento
						stage.close();

					} else {
						MGeneral.Idioma.MostrarMensaje(EnumMensajes.UserPassIncorrectos, null, null, null);
						loginCommerce.setText("");
						passCommerce.setText("");
						loginCommerce.requestFocus();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@FXML
	public void btnCancelar_Click(ActionEvent e) {

		System.exit(0);

	}

	@FXML
	public void btnLogin_Click(ActionEvent e) throws Exception {

		MGeneral.Configuracion.objBaUser = objControllerBa_user.buscarUsuarioPorCredenciales(loginCommerce.getText(),
				passCommerce.getText());
		if (MGeneral.Configuracion.objBaUser != null) {

			close(e);

		} else {
			MGeneral.Idioma.MostrarMensaje(EnumMensajes.UserPassIncorrectos, null, null, null);
			loginCommerce.setText("");
			passCommerce.setText("");
			loginCommerce.requestFocus();
		}

	}

	private void close(ActionEvent e) {

		Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
		Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el elemento
		stage.close();
	}

}
