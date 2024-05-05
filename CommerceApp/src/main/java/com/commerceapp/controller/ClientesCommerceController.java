package com.commerceapp.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.commerceapp.controller.helpers.NavigableControllerHelper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClientesCommerceController implements Initializable,NavigableControllerHelper{

    @FXML
    private GridPane EntidadEmpresarioGB;

    @FXML
    private GridPane PresentanteGB;

    @FXML
    private AnchorPane anchorpaneEntradaDatos;

    @FXML
    private TableColumn<?, ?> columName;

    @FXML
    private TableColumn<?, ?> columnCode;

    @FXML
    private TableColumn<?, ?> columnDescuento;

    @FXML
    private TableColumn<?, ?> columnId;

    @FXML
    private TableColumn<?, ?> columnNombreComercial;

    @FXML
    private HBox frmClientes;

    @FXML
    private Label gbxEntidadEmpresario;

    @FXML
    private Label gbxPresentante;

    @FXML
    private Button idActualizarCliente;

    @FXML
    private TextField idBuscarCliente;

    @FXML
    private TextField idCodigoCliente;

    @FXML
    private Button idCrearCliente;

    @FXML
    private TextField idDescuentoCliente;

    @FXML
    private Button idDeshacerCreacionCliente;

    @FXML
    private Button idEliminarCliente;

    @FXML
    private Button idGuardarCliente;

    @FXML
    private TextField idNombreCliente;

    @FXML
    private TextField idNombreComercialCliente;

    @FXML
    private TableView<?> idTableViewClientes;

    @FXML
    private VBox idVOBXprueba;

    @FXML
    private Button idaniadirCliente;

    @FXML
    private ScrollPane idpruebascroll;

    @FXML
    private Label lblTitulo;

    @FXML
    private ImageView logo;

    @FXML
    void actualizarCliente(ActionEvent event) {

    }

    @FXML
    void aniadirCliente(ActionEvent event) {

    }

    @FXML
    void crearCliente(ActionEvent event) {

    }

    @FXML
    void deshacerCliente(ActionEvent event) {

    }

    @FXML
    void eliminarCliente(ActionEvent event) {

    }

    @FXML
    void guardarCliente(ActionEvent event) {

    }

	@Override
	public void initializeControlsInOrderToNavigate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Control[] getControlsInOrderToNavigate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
