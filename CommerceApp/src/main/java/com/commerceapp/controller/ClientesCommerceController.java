package com.commerceapp.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import com.commerceapp.app.JPAControllerBa_user;
import com.commerceapp.app.JPAControllerCustomer;
import com.commerceapp.controller.helpers.NavigableControllerHelper;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.model.BaUser;
import com.commerceapp.model.Customer;
import com.commerceapp.util.Utilidades;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class ClientesCommerceController implements Initializable, NavigableControllerHelper {

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

	private void setPendienteGuardar(boolean value) {
		pendienteGuardar = value;
		idGuardarCliente.setDisable(!value);

	}

	Tooltip tooltip = new Tooltip("Casilla obligatoria");

	Utilidades objTimeline = new Utilidades();

	private ArrayList<Customer> selectedCustomers = new ArrayList<>();

	public Control[] controlsInOrderToNavigate;

	JPAControllerCustomer objJPAControllerCustomer;

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
	private TableColumn<Customer, String> columnCode;

	@FXML
	private TableColumn<Customer, String> columnDescuento;

	@FXML
	private TableColumn<Customer, String> columnId;

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
		cargarClientes();
	}

	@FXML
	void aniadirCliente(ActionEvent event) {
		try {

			if (!idCodigoCliente.getText().isEmpty()) {
				if (!idNombreCliente.getText().isEmpty()) {

					Customer objCliente = new Customer();
					objCliente.setCode(idCodigoCliente.getText());
					objCliente.setName(idNombreCliente.getText());
					objCliente.setCommercialName(idNombreComercialCliente.getText());

					objCliente.setFiscalNumber(idCodigoCliente.getText() + "-" + idNombreCliente.getText());

					if (!idDescuentoCliente.getText().isEmpty()) {

						BigDecimal decimal = new BigDecimal(idDescuentoCliente.getText());
						objCliente.setDiscountProduct(decimal);
					} else {
						objCliente.setDiscountProduct(new BigDecimal(0));
					}
					objCliente.setCreatedAt(LocalDateTime.now()); // Fecha y hora actual
					objCliente.setUpdatedAt(LocalDateTime.now()); //

					objJPAControllerCustomer.crearCliente(objCliente);
					cargarClientes();
					deshabilitarHabilitarBotonesCreacionCliente(false);
					validacionesTextfieldsObligatorias(false);
				} else {
					IdiomaC.MostrarMensaje(EnumMensajes.CampoObligatorio, "Nombre Usuario", "", "");
				}
			} else {
				IdiomaC.MostrarMensaje(EnumMensajes.CampoObligatorio, "Codigo Usuario", "", "");
			}
		} catch (Exception ex) {
			IdiomaC.MostrarMensaje(EnumMensajes.Excepcion, ex.toString(), "", "");
		}
	}

	private void validacionesTextfieldsObligatorias(boolean aux) {
		if (aux == true) {
			idCodigoCliente.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
			Tooltip.install(idCodigoCliente, tooltip);
			idNombreCliente.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
			Tooltip.install(idNombreCliente, tooltip);
			addListener(idCodigoCliente, tooltip);
			addListener(idNombreCliente, tooltip);
		} else {
			idCodigoCliente.setStyle("");
			Tooltip.uninstall(idCodigoCliente, tooltip);
			idNombreCliente.setStyle("");
			Tooltip.uninstall(idNombreCliente, tooltip);
			removeListener(idCodigoCliente, tooltip);
			removeListener(idNombreCliente, tooltip);
		}

	}

	public void addListener(TextField tf, Tooltip tp) {
		textChangeListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			if (newValue.isEmpty()) {
				// Cambiar el borde a rojo y mostrar el tooltip
				tf.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
				Tooltip.install(tf, tp);
			} else {
				System.out.println("casilla no está vacía");
				// Cambiar el borde a azul y ocultar el tooltip
				tf.setStyle("");
				Tooltip.uninstall(tf, tp);
			}
		};

		tf.textProperty().addListener(textChangeListener);
	}

	public void removeListener(TextField tf, Tooltip tp) {
		tf.textProperty().removeListener(textChangeListener);
		Tooltip.uninstall(tf, tp);
	}

	private void deshabilitarHabilitarBotonesCreacionCliente(boolean aux) {
		idCrearCliente.setVisible(aux);

		if (aux) {
			idGuardarCliente.setVisible(!aux);
		} else {
			idGuardarCliente.setVisible(!aux);
			idGuardarCliente.setDisable(!aux);
		}

		idTableViewClientes.setDisable(aux);
		idaniadirCliente.setDisable(aux);
		idActualizarCliente.setDisable(aux);
		idEliminarCliente.setDisable(aux);
		idDeshacerCreacionCliente.setDisable(!aux);
		idBuscarCliente.setDisable(aux);
		idCodigoCliente.setText("");
		idNombreCliente.setText("");
		idNombreComercialCliente.setText("");
		idDescuentoCliente.setText("");

		if (aux) {
			idCodigoCliente.requestFocus();
		} else {
			idTableViewClientes.requestFocus();
		}

	}

	@FXML
	void crearCliente(ActionEvent event) {
		deshabilitarHabilitarBotonesCreacionCliente(true);

		// Configurar un Tooltip para idCodigoUser
		validacionesTextfieldsObligatorias(true);
	}

	@FXML
	void deshacerCliente(ActionEvent event) {
		deshabilitarHabilitarBotonesCreacionCliente(false);
		validacionesTextfieldsObligatorias(false);
	}

	@FXML
	void eliminarCliente(ActionEvent event) {
		if (selectedCustomers.size() != 0) {
			if (IdiomaC.MostrarMensaje(EnumMensajes.EliminarProducto, "", "", "")) {

				objJPAControllerCustomer.eliminarCliente(selectedCustomers.get(0));
				cargarClientes();
				deshabilitarHabilitarBotonesCreacionCliente(false);
				validacionesTextfieldsObligatorias(false);
				idGuardarCliente.setDisable(true);
			}
		}
	}

	@FXML
	void guardarCliente(ActionEvent event) {
		Customer objCliente = new Customer();
		objCliente.setId(selectedCustomers.get(0).getId());
		objCliente.setCode(idCodigoCliente.getText());
		objCliente.setName(idNombreCliente.getText());
		objCliente.setCommercialName(idNombreComercialCliente.getText());
		BigDecimal decimal = new BigDecimal(idDescuentoCliente.getText());
		objCliente.setDiscountProduct(decimal);
		objJPAControllerCustomer.actualizarCliente(objCliente);
		cargarClientes();
		idGuardarCliente.setDisable(true);
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
		controlsInOrderToNavigate = new Control[] { idBuscarCliente, idCodigoCliente, idNombreCliente,
				idNombreComercialCliente };

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

		scroolPane();
		idBuscarCliente.requestFocus();

	}

	private void scroolPane() {
		idpruebascroll.setContent(idVOBXprueba);
		idpruebascroll.setFitToWidth(true);
		idpruebascroll.setFitToHeight(true);

	}

	private void iniciarValidaciones() {
		idTableViewClientes.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {

						selectedCustomers.clear();
						selectedCustomers.add(newSelection);
						rellenarCampos();
						validarTextChangesGuardar(true);
					}
				});
		idBuscarCliente.textProperty().addListener((observable, oldValue, newValue) -> {
			buscarClientes(newValue);
		});

	}

	private void buscarClientes(String searchText) {
		JPAControllerCustomer controller = new JPAControllerCustomer();
		List<Customer> productos = controller.buscarClientePorNombre(searchText);

		ObservableList<Customer> productList = FXCollections.observableArrayList(productos);
		idTableViewClientes.setItems(productList);

	}

	private void validarTextChangesGuardar(boolean aux) {
		AtomicBoolean cambiosRealizados = new AtomicBoolean(false);

		for (int i = 0; i < controlsInOrderToNavigate.length; i++) {
			if (controlsInOrderToNavigate[i] instanceof TextField) {
				TextField controlfinal = (TextField) controlsInOrderToNavigate[i];
				String originalValue = selectedCustomers.get(0).getCode(); // Guardar valor original
				controlfinal.textProperty().addListener((observable, oldValue, newValue) -> {
					// Verificar si hay un cambio en el campo
					if (!newValue.equalsIgnoreCase(originalValue)) {
						cambiosRealizados.set(true);
						// Establecer el botón de guardar en consecuencia
						setPendienteGuardar(aux);
					}
				});
			}
		}

		// Después de iterar sobre todos los campos, establecer el estado del botón de
		// guardar
		if (!cambiosRealizados.get()) {
			// No se han realizado cambios, deshabilitar el botón de guardar
			setPendienteGuardar(!aux);
		}

	}

	private void rellenarCampos() {
		idCodigoCliente.setText(selectedCustomers.get(0).getCode());

		idNombreCliente.setText(selectedCustomers.get(0).getName());

		idNombreComercialCliente.setText(selectedCustomers.get(0).getCommercialName());

		idDescuentoCliente.setText(String.valueOf(selectedCustomers.get(0).getDiscountProduct()));

	}

	private void cargarClientes() {
		JPAControllerCustomer objAux = new JPAControllerCustomer();
		List<Customer> usuarios = objAux.obtenerTodosClientes();

		// Crea una ObservableList de productos y añádela al TableView
		ObservableList<Customer> usuarioso = FXCollections.observableArrayList(usuarios);
		idTableViewClientes.setItems(usuarioso);

	}

}
