package com.commerceapp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.commerceapp.app.JPAControllerBa_user;
import com.commerceapp.app.JPAControllerCustomer;
import com.commerceapp.controller.helpers.NavigableControllerHelper;
import com.commerceapp.model.BaUser;
import com.commerceapp.model.Customer;
import com.commerceapp.util.Utilidades;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClientesCommerceController implements Initializable,NavigableControllerHelper{
	
	private MenuPrincipalController parentController;
	
	public void setParentController(MenuPrincipalController parentController) {
		this.parentController = parentController;

	}

	public MenuPrincipalController getParentController() {
		return parentController;
	}
	
	public double ancho;

	public String campoPonerFoco = "";

	private boolean pendienteGuardar = false;
	Tooltip tooltip = new Tooltip("Casilla obligatoria");

	Utilidades objTimeline = new Utilidades();

	private ArrayList<Customer> selectedCustomers= new ArrayList<>();

	public Control[] controlsInOrderToNavigate;

	JPAControllerCustomer objJPAControllerCustomer ;

	private ChangeListener<String> textChangeListener;

    @FXML
    private GridPane EntidadEmpresarioGB;

    @FXML
    private GridPane PresentanteGB;

    @FXML
    private AnchorPane anchorpaneEntradaDatos;

    @FXML
    private TableColumn<Customer, String> columName;

    @FXML
    private TableColumn<Customer, String>columnCode;

    @FXML
    private TableColumn<Customer, String>columnDescuento;

    @FXML
    private TableColumn<Customer, String>columnId;

    @FXML
    private TableColumn<Customer, String> columnNombreComercial;

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
    private TextField idNombreCliente;

    @FXML
    private TextField idNombreComercialCliente;
    
    @FXML
    private TextField idDescuentoCliente;

    @FXML
    private Button idCrearCliente;

  

    @FXML
    private Button idDeshacerCreacionCliente;

    @FXML
    private Button idEliminarCliente;

    @FXML
    private Button idGuardarCliente;

 
    @FXML
    private TableView<Customer> idTableViewClientes;

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
		controlsInOrderToNavigate = new Control[] {}; // TODO Auto-generated method
														// stub

	}

	@Override
	public Control[] getControlsInOrderToNavigate() {
		// TODO Auto-generated method stub
		return controlsInOrderToNavigate;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		objJPAControllerCustomer = new JPAControllerCustomer();
		Image image = new Image(getClass().getResourceAsStream("/imagenes/LogoECommerce.png"));

		logo.setFitWidth(150);
		logo.setFitHeight(70);
		logo.setImage(image);
		initializeControlsInOrderToNavigate();
		registerKeyPressENTERInControlsToNavigate();
		controlsInOrderToNavigate = new Control[] {idBuscarCliente,idCodigoCliente,idNombreCliente,idNombreComercialCliente};

		columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnCode.setCellValueFactory(new PropertyValueFactory<>("code"));

		columName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnNombreComercial.setCellValueFactory(new PropertyValueFactory<>("commercialName"));

		columnDescuento.setCellValueFactory(new PropertyValueFactory<>("discountProduct"));

		cargarClientes();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				pendienteGuardar = false;

				getParentController().getStagePrincipal().getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
					if (event.getCode() == KeyCode.TAB) {
						event.consume(); // Consumir el evento de teclado para evitar la navegación por TAB en todo el
											// form
					}
				});

			}
		});

		iniciarValidaciones();

		iniciarImageIcon();
		scroolPane();
		idBuscarCliente.requestFocus();
		
	}

	private void scroolPane() {
		// TODO Auto-generated method stub
		
	}

	private void iniciarImageIcon() {
		// TODO Auto-generated method stub
		
	}

	private void iniciarValidaciones() {
		// TODO Auto-generated method stub
		
	}

	private void cargarClientes() {
		JPAControllerCustomer objAux = new JPAControllerCustomer();
		List<Customer> usuarios = objAux.obtenerTodosClientes();

		// Crea una ObservableList de productos y añádela al TableView
		ObservableList<Customer> usuarioso = FXCollections.observableArrayList(usuarios);
		idTableViewClientes.setItems(usuarioso);
		
	}

}
