package com.commerceapp.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.commerceapp.Main;
import com.commerceapp.app.JPAControllerProduct;
import com.commerceapp.controller.helpers.NavigableControllerHelper;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.ceDoc;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.domain.idioma.ElementosIdiomaC;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.domain.legalizacion.cDatos;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.domain.legalizacion.kLegalizacion.enumFormato;
import com.commerceapp.gui.custom.combobox.CustomCombobox;
import com.commerceapp.gui.custom.datePicker.CustomDatePicker;
import com.commerceapp.gui.custom.imageview.CustomImageView;
import com.commerceapp.maestros.MaestroCodigoDescripcion;
import com.commerceapp.maestros.MaestroCodigoDescripcionConverter;
import com.commerceapp.model.Product;
import com.commerceapp.reporting.instancia.ReportingPreviewService;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.service.LegalizacionService.EnumResultadoZip;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;
import com.commerceapp.util.Utilidades.TimeLineClass;
import com.lowagie.text.Table;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.css.converter.StringConverter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class ProductosController implements Initializable, NavigableControllerHelper {
	private MenuPrincipalController parentController;
	public double ancho;

	public String campoPonerFoco = "";

	private boolean pendienteGuardar = false;
	Tooltip tooltip = new Tooltip("Casilla obligatoria");

	Utilidades objTimeline = new Utilidades();

	@FXML
	private GridPane GridSoli;

	@FXML
	private AnchorPane anchorpaneEntradaDatos;

	@FXML
	private AnchorPane anchorpaneScroll;

	@FXML
	private HBox frmEntradaDatos;

	@FXML
	private Label gbxDatosRegistrales;

	@FXML
	private Label gbxEntidadEmpresario;

	@FXML
	private Label gbxPresentante;

	@FXML
	private GridPane EntidadEmpresarioGB;
	@FXML
	private GridPane PresentanteGB;

	@FXML
	private VBox idVOBXprueba;

	@FXML
	private ScrollPane idpruebascroll;

	@FXML
	private Pane lblPaneTitulo;

	@FXML
	private ImageView logo;

	@FXML
	private Tooltip tpGuardar;

	@FXML
	private TextField idBuscarProducto;

	@FXML
	private TableView<Product> idTableViewProductos;

	@FXML
	private TableColumn<Product, Number> columnId;
	@FXML
	private TableColumn<Product, String> columnCode;

	@FXML
	private TableColumn<Product, Number> columName;
	@FXML
	private TableColumn<Product, String> columnDescription;

	@FXML
	private TableColumn<Product, Number> columnSale;
	@FXML
	private TableColumn<Product, String> columnUM;
	@FXML
	private TableColumn<Product, Number> columnEAN;

	@FXML
	private TextField idCodigoProducto;

	@FXML
	private TextField idNombreProducto;

	@FXML
	private TextArea idDescProducto;

	@FXML
	private TextField idPrecioVenta;

	@FXML
	private Button idGuardarProductoButton;

	@FXML
	private CustomCombobox<String> idFamiliaProducto;

	@FXML
	private CustomCombobox<String> idUM;

	@FXML
	private TextField idEanProducto;
	@FXML
	private Button idaniadirProducto;
	@FXML
	private Button idActualizarProducto;

	@FXML
	private Button idEliminarProducto;

	@FXML
	private Button idDeshacerCreacionProducto;
	@FXML
	private RowConstraints idRowFamilia;

	@FXML
	private Button idCrearProducto;

	@FXML
	private ImageView idIconCodigo;
	@FXML
	private ImageView idIconNombre;

	private ArrayList<Product> selectedProducts = new ArrayList<>();

	public Control[] controlsInOrderToNavigate;

	JPAControllerProduct objJPAControllerProduct;

	private ChangeListener<String> textChangeListener;

	// Método para añadir el listener
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

	// Método para eliminar el listener
	public void removeListener(TextField tf, Tooltip tp) {
		tf.textProperty().removeListener(textChangeListener);
		Tooltip.uninstall(tf, tp);
	}

	public enum EnumTipoOperacion {
		Imprimir(1), Enviar(2), GenerarZip(3), GenerarHuellas(4), EncriptarTodo(5);

		private final int value;

		EnumTipoOperacion(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum EnumActivacionIconos {
		HayLegalizacionCargada(1), NoHayLegalizacionCargada(2), DesactivarTodo(3);

		private final int value;

		EnumActivacionIconos(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public void setParentController(MenuPrincipalController parentController) {
		this.parentController = parentController;

	}

	public MenuPrincipalController getParentController() {
		return parentController;
	}

	public boolean isPendienteGuardar() {
		return pendienteGuardar;
	}

	private void setPendienteGuardar(boolean value) {
		pendienteGuardar = value;
		idGuardarProductoButton.setDisable(!value);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Image image = new Image(getClass().getResourceAsStream("/imagenes/LogoECommerce.png"));

		logo.setFitWidth(150);
		logo.setFitHeight(70);
		logo.setImage(image);
		initializeControlsInOrderToNavigate();
		registerKeyPressENTERInControlsToNavigate();
		controlsInOrderToNavigate = new Control[] { idCodigoProducto, idNombreProducto, idDescProducto, idPrecioVenta,
				idUM, idEanProducto };

		columnId.setCellValueFactory(new PropertyValueFactory<>("tb_product_id"));
		columnCode.setCellValueFactory(new PropertyValueFactory<>("code"));

		columName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

		columnSale.setCellValueFactory(new PropertyValueFactory<>("salesPriceWithTax"));
		columnUM.setCellValueFactory(new PropertyValueFactory<>("defaultUom"));
		columnEAN.setCellValueFactory(new PropertyValueFactory<>("ean"));

		cargarProductos();
		cargarComboUM();

		objJPAControllerProduct = new JPAControllerProduct();
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
		idBuscarProducto.requestFocus();

	}

	public void validarTextChangesGuardar(boolean aux) {
		// Crear un AtomicBoolean para rastrear si se han realizado cambios
		AtomicBoolean cambiosRealizados = new AtomicBoolean(false);

		for (int i = 0; i < controlsInOrderToNavigate.length; i++) {
			if (controlsInOrderToNavigate[i] instanceof TextField) {
				TextField controlfinal = (TextField) controlsInOrderToNavigate[i];
				String originalValue = selectedProducts.get(0).getCode(); // Guardar valor original
				controlfinal.textProperty().addListener((observable, oldValue, newValue) -> {
					// Verificar si hay un cambio en el campo
					if (!newValue.equalsIgnoreCase(originalValue)) {
						cambiosRealizados.set(true);
						// Establecer el botón de guardar en consecuencia
						setPendienteGuardar(aux);
					}
				});
			} else if (controlsInOrderToNavigate[i] instanceof ComboBox) {
				ComboBox controlfinal = (ComboBox) controlsInOrderToNavigate[i];
				if (controlsInOrderToNavigate[i].getId().equalsIgnoreCase("idUM")) {
					String originalValue = selectedProducts.get(0).getDefaultUom(); // Guardar valor original
					controlfinal.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
						// Verificar si hay un cambio en el campo
						if (!newValue.equalsIgnoreCase(originalValue)) {
							cambiosRealizados.set(true);
							// Establecer el botón de guardar en consecuencia
							setPendienteGuardar(aux);
						}
					});
				}
			} else if (controlsInOrderToNavigate[i] instanceof TextArea) {
				TextArea controlfinal = (TextArea) controlsInOrderToNavigate[i];
				String originalValue = selectedProducts.get(0).getCode(); // Guardar valor original
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

	private void iniciarImageIcon() {

	}

	private boolean validarTodosLosControles(Node control, boolean MensajeSN, boolean MostrarMensajePrimerControl) {
		Control primerControlNoValido = null;
		try {
			for (int i = 0; i < controlsInOrderToNavigate.length; i++) {

				if (!validaControl(controlsInOrderToNavigate[i], MensajeSN)) {
					if (primerControlNoValido == null) {
						primerControlNoValido = controlsInOrderToNavigate[i];
					}
				}

			}
			if (MostrarMensajePrimerControl) {
				if (primerControlNoValido != null) {

					String cadenaMensaje = "";
					String textoLabel = dameTextoLabelAsociadaAlControl(primerControlNoValido);
					if (!textoLabel.equals("")) {
						cadenaMensaje = textoLabel;
						if (!textoLabel.endsWith(":")) {
							cadenaMensaje += " :";
						}
						cadenaMensaje += " ";
					}

					MGeneral.Idioma.MostrarMensaje(EnumMensajes.CampoObligatorio,
							primerControlNoValido.getId().replaceAll("txt", ""), cadenaMensaje, textoLabel);
					primerControlNoValido.requestFocus();

				}
			}

			if (primerControlNoValido != null) {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	private void scroolPane() {
		idpruebascroll.setContent(idVOBXprueba);
		idpruebascroll.setFitToWidth(true);
		idpruebascroll.setFitToHeight(true);

	}

	public boolean cancelarPendienteGuardar() {
		boolean cancelar = false;
		if (isPendienteGuardar()) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ConfirmacionGuardar, "", "", "");

		}
		return cancelar;
	}

	public boolean cancelaGuardar() {
		boolean cancelar = false;

		if (pendienteGuardar) {
			// aqui va un mostrarMen sajes
			// Mostrar un Alert de confirmación

			boolean result = MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ConfirmacionGuardar, "", "", "");

			// Manejar la respuesta del usuario
			if (result == false) {
				cancelar = true;
			}
		}
		return cancelar;
	}

	private void cargarProductos() {
		JPAControllerProduct controller = new JPAControllerProduct();
		List<Product> productos = controller.obtenerTodosProductos();

		// Crea una ObservableList de productos y añádela al TableView
		ObservableList<Product> productList = FXCollections.observableArrayList(productos);
		idTableViewProductos.setItems(productList);
	}

	@SuppressWarnings("unlikely-arg-type")
	public void cerrar(MenuPrincipalController m, ProductosController p) {

		m.AnchorPane3.getChildren().remove(p);

	}

	@FXML
	public void EliminarProducto(ActionEvent e) throws Exception {
		if (selectedProducts.size() != 0) {
			if (IdiomaC.MostrarMensaje(EnumMensajes.EliminarProducto, "", "", "")) {

				objJPAControllerProduct.eliminarProducto(selectedProducts.get(0));
				cargarProductos();
				DeshabilitarHabilitarBotonesCreacionProducto(false);
				validacionestextfieldsobligatorias(false);
				idGuardarProductoButton.setDisable(true);
			}
		}
	}

	@FXML
	public void ActualizarProductos(ActionEvent e) throws Exception {
		cargarProductos();
	}

	@FXML
	public void CrearProducto(ActionEvent e) throws Exception {
		DeshabilitarHabilitarBotonesCreacionProducto(true);

		// Configurar un Tooltip para idCodigoProducto
		validacionestextfieldsobligatorias(true);
		// Añadir listener a la propiedad del textoaddListener

	}

	private void validacionestextfieldsobligatorias(boolean aux) {
		if (aux == true) {
			idCodigoProducto.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
			Tooltip.install(idCodigoProducto, tooltip);
			idNombreProducto.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
			Tooltip.install(idNombreProducto, tooltip);
			addListener(idCodigoProducto, tooltip);
			addListener(idNombreProducto, tooltip);
		} else {
			idCodigoProducto.setStyle("");
			Tooltip.uninstall(idCodigoProducto, tooltip);
			idNombreProducto.setStyle("");
			Tooltip.uninstall(idNombreProducto, tooltip);
			removeListener(idCodigoProducto, tooltip);
			removeListener(idNombreProducto, tooltip);
		}
	}

	@FXML
	public void DeshacerProducto(ActionEvent e) throws Exception {
		DeshabilitarHabilitarBotonesCreacionProducto(false);
		validacionestextfieldsobligatorias(false);
	}

	@FXML
	public void AniadirProducto(ActionEvent e) {
		try {

			if (!idCodigoProducto.getText().isEmpty()) {
				if (!idNombreProducto.getText().isEmpty()) {
					if (idPrecioVenta.getText().isEmpty()) {
						idPrecioVenta.setText("0");
					}
					Product objProduct = new Product();
					objProduct.setCode(idCodigoProducto.getText());
					objProduct.setName(idNombreProducto.getText());
					objProduct.setDescription(idDescProducto.getText());
					objProduct.setSalesPriceWithTax(new BigDecimal(idPrecioVenta.getText()));
					objProduct.setDefaultUom(idUM.getValue());
					objProduct.setEan(idEanProducto.getText());
					objJPAControllerProduct.crearProducto(objProduct);
					cargarProductos();
					DeshabilitarHabilitarBotonesCreacionProducto(false);
					validacionestextfieldsobligatorias(false);
				} else {
					IdiomaC.MostrarMensaje(EnumMensajes.CampoObligatorio, "Nombre Producto", "", "");
				}
			} else {
				IdiomaC.MostrarMensaje(EnumMensajes.CampoObligatorio, "Codigo Producto", "", "");
			}
		} catch (Exception ex) {
			IdiomaC.MostrarMensaje(EnumMensajes.Excepcion, ex.toString(), "", "");
		}
	}

	private void DeshabilitarHabilitarBotonesCreacionProducto(boolean aux) {
		idCrearProducto.setVisible(aux);

		if (aux) {
			idGuardarProductoButton.setVisible(!aux);
		} else {
			idGuardarProductoButton.setVisible(!aux);
			idGuardarProductoButton.setDisable(!aux);
		}

		idTableViewProductos.setDisable(aux);
		idaniadirProducto.setDisable(aux);
		idActualizarProducto.setDisable(aux);
		idEliminarProducto.setDisable(aux);
		idDeshacerCreacionProducto.setDisable(!aux);
		idBuscarProducto.setDisable(aux);
		idCodigoProducto.setText("");
		idNombreProducto.setText("");
		idDescProducto.setText("");
		idPrecioVenta.setText("");
		idUM.setValue("");
		idEanProducto.setText("");
		if (aux) {
			idCodigoProducto.requestFocus();
		} else {
			idTableViewProductos.requestFocus();
		}
	}

	@FXML
	public void GuardarProductos(ActionEvent e) throws Exception {

		Product objProduct = new Product();
		objProduct.setTb_product_id(selectedProducts.get(0).getTb_product_id());
		objProduct.setCode(idCodigoProducto.getText());
		objProduct.setName(idNombreProducto.getText());
		objProduct.setDescription(idDescProducto.getText());
		objProduct.setSalesPriceWithTax(new BigDecimal(idPrecioVenta.getText()));
		objProduct.setDefaultUom(idUM.getValue());
		objProduct.setEan(idEanProducto.getText());
		objJPAControllerProduct.actualizarProducto(objProduct);
		cargarProductos();
		idGuardarProductoButton.setDisable(true);
	}

	private void iniciarValidaciones() {
		idTableViewProductos.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {

						selectedProducts.clear();
						selectedProducts.add(newSelection);
						rellenarCampos();
						validarTextChangesGuardar(true);
					}
				});
		idBuscarProducto.textProperty().addListener((observable, oldValue, newValue) -> {
			buscarProductos(newValue);
		});

	}

	private void buscarProductos(String searchText) {
		JPAControllerProduct controller = new JPAControllerProduct();
		List<Product> productos = controller.buscarProductoPorNombre(searchText);

		ObservableList<Product> productList = FXCollections.observableArrayList(productos);
		idTableViewProductos.setItems(productList);
	}

	private void rellenarCampos() {
		idCodigoProducto.setText(selectedProducts.get(0).getCode());

		idNombreProducto.setText(selectedProducts.get(0).getName());

		idDescProducto.setText(selectedProducts.get(0).getDescription());

		idPrecioVenta.setText(String.valueOf(selectedProducts.get(0).getSalesPriceWithTax()));

		// idFamiliaProducto.getItems().set g(selectedProducts.get(0).getCode());

		idUM.setValue(selectedProducts.get(0).getDefaultUom());

		idEanProducto.setText(selectedProducts.get(0).getEan());

	}

	private void cargarComboUM() {
		idUM.getItems().addAll("Bien", "Servicio");

	}

	public LocalDate convertirAFecha(String fechaString) {
		List<DateTimeFormatter> formateadores = Arrays.asList(DateTimeFormatter.ofPattern("ddMMyyyy"),
				DateTimeFormatter.ofPattern("dd-MM-yyyy"), DateTimeFormatter.ofPattern("dd/MM/yyyy"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd"), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		LocalDate fecha = null;

		for (DateTimeFormatter formatter : formateadores) {
			try {
				fecha = LocalDate.parse(fechaString, formatter);
				// Si no se lanza ninguna excepción, se encontró el formato adecuado
				break;
			} catch (Exception e) {
				// Si ocurre una excepción, intenta con el siguiente formato
				continue;
			}
		}

		return fecha;
	}

//Activado en menu principal
	public boolean guardar(boolean preguntarguardado, boolean msjValidar) {
//Verificando
		boolean guardado = true;

		try {
			if (preguntarguardado) {

				if (cancelaGuardar()) {

					guardado = false;
					return false;
				}
			}

			// AjustaValoresCampo(this);

			validarTodosLosControles(frmEntradaDatos, msjValidar, false);

		} catch (Exception ex) {

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
			ex.printStackTrace();
		}

		return true;
	}

	public void validar(boolean msjValidar) {

	}

	public String dameTextoLabelAsociadaAlControl(Control control) {

		String textoLabel = "";
		try {
			String nombreCampoLabel;
			String sufijo = "";

			sufijo = control.getId().substring(4);

			Control controlAux;
			// Caso especial para txtCifNif, se usa tanto para lblCif como para lblNif
			if (sufijo.equals("CifNif")) {
				// Aquí deberías sustituir con la lógica correspondiente para determinar la
				// visibilidad de los labels

			}

			nombreCampoLabel = "lbl" + sufijo;

			// Aquí deberías implementar el método para buscar el control por su nombre
			controlAux = Utilidades.buscaControlPorNombre(frmEntradaDatos, nombreCampoLabel);

			if (controlAux != null) {
				Label aux = (Label) controlAux;
				textoLabel = aux.getText().trim();
			}
		} catch (Exception ex) {

		}

		return textoLabel;
	}

	private void evValidarAlValidar(Control control) {
		validaControl(control, true);

	}

	private boolean validaControl(Control control, boolean validarObligatoriedad) {
		boolean validarPrimerControlNoValido = true;
		try {
			if (control.getId() == null) {
				return false;
			}
			switch (control.getId().toString()) {

			default:
				break;
			}

			return validarPrimerControlNoValido;
		} catch (Exception ex) {

			ex.printStackTrace();
			return false;
		}

	}

	public void evSoloNumerosInterfaz(KeyEvent event) {
		Utilidades.evSoloNumeros(null, event);
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

}
