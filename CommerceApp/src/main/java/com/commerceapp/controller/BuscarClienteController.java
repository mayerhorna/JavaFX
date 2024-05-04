package com.commerceapp.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.commerceapp.app.JPAControllerProduct;
import com.commerceapp.controller.helpers.NavigableControllerHelper;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BuscarClienteController implements Initializable, NavigableControllerHelper {
	public Control[] controlsInOrderToNavigate;
	@FXML
	private Button btnNext;

	@FXML
	private Button btnPrevio;

	@FXML
	private TableView<?> tblClientes;

	@FXML
	private TextField txtBusquedaCliente;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		 * initializeControlsInOrderToNavigate();
		 * registerKeyPressENTERInControlsToNavigate(); controlsInOrderToNavigate = new
		 * Control[] { idCodigoProducto, idNombreProducto, idDescProducto,
		 * idPrecioVenta, idUM, idEanProducto };
		 * 
		 * columnId.setCellValueFactory(new PropertyValueFactory<>("tb_product_id"));
		 * columnCode.setCellValueFactory(new PropertyValueFactory<>("code"));
		 * 
		 * columName.setCellValueFactory(new PropertyValueFactory<>("name"));
		 * columnDescription.setCellValueFactory(new
		 * PropertyValueFactory<>("description"));
		 * 
		 * columnSale.setCellValueFactory(new
		 * PropertyValueFactory<>("salesPriceWithTax"));
		 * columnUM.setCellValueFactory(new PropertyValueFactory<>("defaultUom"));
		 * columnEAN.setCellValueFactory(new PropertyValueFactory<>("ean"));
		 * 
		 * cargarProductos(); cargarComboUM();
		 * 
		 * objJPAControllerProduct = new JPAControllerProduct(); Platform.runLater(new
		 * Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * pendienteGuardar = false;
		 * 
		 * getParentController().getStagePrincipal().getScene().addEventFilter(KeyEvent.
		 * KEY_PRESSED, event -> { if (event.getCode() == KeyCode.TAB) {
		 * event.consume(); // Consumir el evento de teclado para evitar la navegación
		 * por TAB en todo el // form } });
		 * 
		 * } });
		 * 
		 * iniciarValidaciones();
		 * 
		 * iniciarImageIcon(); scroolPane(); idBuscarProducto.requestFocus();
		 * 
		 */
	}

	private String busqueda() {
		return null;

	}

	@Override
	public void initializeControlsInOrderToNavigate() {
		controlsInOrderToNavigate = new Control[] {}; // TODO Auto-generated

	}

	@Override
	public Control[] getControlsInOrderToNavigate() {
		// TODO Auto-generated method stub
		return controlsInOrderToNavigate;
	}

}
