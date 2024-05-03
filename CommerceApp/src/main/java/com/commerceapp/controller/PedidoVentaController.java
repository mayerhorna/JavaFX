package com.commerceapp.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PedidoVentaController implements Initializable{

    @FXML
    private TextField TxtObservaciones;

    @FXML
    private Button btnCaja;

    @FXML
    private Button btnCliente;

    @FXML
    private Button btnClienteConfig;

    @FXML
    private Button btnImprimir;

    @FXML
    private Button btnPedidoVenta;

    @FXML
    private Button btnProducto;

    @FXML
    private Button btnUsuario;

    @FXML
    private TableColumn<?, ?> colCantidad;

    @FXML
    private TableColumn<?, ?> colCodigo;

    @FXML
    private TableColumn<?, ?> colNumero;

    @FXML
    private TableColumn<?, ?> colPrecio;

    @FXML
    private TableColumn<?, ?> colProducto;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private Label lblPuntoVenta;

    @FXML
    private Label lblSincronizado;

    @FXML
    private Label lblSino;

    @FXML
    private Label lblTotal;

    @FXML
    private Menu mnuAdministrar;

    @FXML
    private Menu mnuAyuda;

    @FXML
    private MenuItem mnuItemClose;

    @FXML
    private MenuItem mnuItemDelete;

    @FXML
    private TableView<?> tblVenta;

    @FXML
    private TextField txtCliente;

    @FXML
    private TextField txtPedidoVenta;

    @FXML
    private TextField txtProducto;

    @FXML
    private TextField txtTotal;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	public void setParentController(MenuPrincipalController menuPrincipalController) {
		// TODO Auto-generated method stub
		
	}

}
