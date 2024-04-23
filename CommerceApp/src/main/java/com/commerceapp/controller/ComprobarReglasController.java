package com.commerceapp.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.legalizacion.MensajesReglasC;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.util.Formato;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ComprobarReglasController implements Initializable {
	private static final Logger logger = Logger.getLogger(ComprobarReglasController.class.getName());
	MenuPrincipalController menuPrincipalController;
	private boolean limpiar = true;
	@FXML
	private Button btnCorregirError;
	@FXML
	private Button btnSalir;

	@FXML
	private Label lblTitulo1;

	@FXML
	private Label lblTitulo2;

	@FXML
	private AnchorPane frmComprobarReglas;

	@FXML
	private ListView<String> lstAvisos;

	@FXML
	private ListView<String> lstErrores;

	public MenuPrincipalController getParentController() {
		return menuPrincipalController;
	}

	public void setParentController(MenuPrincipalController menuPrincipalController) {
		this.menuPrincipalController = menuPrincipalController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MGeneral.Idioma.cargarIdiomaControles(null, frmComprobarReglas);
		validar();

	}

	private void validar() {
		kLegalizacion.enumResultadoValidacion resultado = MGeneral.mlform.valida();

		if (resultado != kLegalizacion.enumResultadoValidacion.Valida) {
			for (int i = 0; i < MGeneral.mlform.MensajesReglas.length; i++) {
				if (MGeneral.mlform.MensajesReglas[i].getGrado() == MensajesReglasC.Grado.EsError) {
					lstErrores.getItems().add(MGeneral.mlform.MensajesReglas[i].getTextoMensaje());
				}
				if (MGeneral.mlform.MensajesReglas[i].getGrado() == MensajesReglasC.Grado.EsAviso) {
					lstAvisos.getItems().add(MGeneral.mlform.MensajesReglas[i].getTextoMensaje());
				}
			}
		}
	}

	@FXML
	public void lstAvisos_MouseDoubleClick(MouseEvent event) {
		if (event.getClickCount() == 2) {
			String selectedItem = lstAvisos.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {

				veteAError(selectedItem);
			}
		}
		// lstAvisos_SelectedIndexChanged(event);
	}

	@FXML
	public void lstErrores_MouseDoubleClick(MouseEvent event) {

		if (event.getClickCount() == 2) {
			String selectedItem = lstErrores.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				veteAError(selectedItem);
			}
		}
	}

	public void veteAError(String textoControl) {
		if (Formato.ValorNulo(textoControl))
			return;

		for (int i = 0; i < MGeneral.mlform.MensajesReglas.length; i++) {
			if (MGeneral.mlform.MensajesReglas[i].getTextoMensaje().equals(textoControl)) {
				if (!Formato.ValorNulo(MGeneral.mlform.MensajesReglas[i].getCampoFoco())) {

					getParentController().form.campoPonerFoco = MGeneral.mlform.MensajesReglas[i].getCampoFoco();

					// cerrar
					Stage stage = (Stage) lblTitulo1.getScene().getWindow();
					stage.close();
					return;
				} else {
					break;
				}
			}
		}
	}

	public void lstErrores_SelectedIndexChanged(MouseEvent event) {
		if (!limpiar)
			return;

		limpiar = false;
		lstAvisos.getSelectionModel().clearSelection();
		limpiar = true;
	}

	public void lstAvisos_SelectedIndexChanged(MouseEvent event) {
		if (!limpiar)
			return;

		limpiar = false;
		lstErrores.getSelectionModel().clearSelection();
		limpiar = true;
	}

	@FXML
	public void btnCorregirError_Click(ActionEvent event) {
		if (lstErrores.getSelectionModel().getSelectedItems().isEmpty()
				&& lstAvisos.getSelectionModel().getSelectedItems().isEmpty()) {
			return;
		}

		if (!lstErrores.getSelectionModel().getSelectedItems().isEmpty()) {
			veteAError(lstErrores.getSelectionModel().getSelectedItem().toString());
		}

		if (!lstAvisos.getSelectionModel().getSelectedItems().isEmpty()) {
			veteAError(lstAvisos.getSelectionModel().getSelectedItem().toString());
		}
	}

	@FXML
	public void salir(ActionEvent e) throws Exception {

		// Me cierra la ventana
		if (e != null) {
			Node source = (Node) e.getSource();
			Stage stage = (Stage) source.getScene().getWindow();

			stage.close();
		}

	}
}
