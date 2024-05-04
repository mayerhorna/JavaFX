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

@Component
public class CrearClientesController implements Initializable, NavigableControllerHelper {
	private MenuPrincipalController parentController;
	public double ancho;

	public String campoPonerFoco = "";

	private boolean pendienteGuardar = false;

	private boolean primeraVez = false;

	private Stage stage;
	private Scene scene;

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	Utilidades objTimeline = new Utilidades();
	// Utilizada en la función irACampoFoco
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
	private RowConstraints idRowFamilia;

	private ArrayList<Product> selectedProducts = new ArrayList<>();

	public Control[] controlsInOrderToNavigate;

	JPAControllerProduct objJPAControllerProduct;

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
				primeraVez = true;

				getParentController().getStagePrincipal().getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
					if (event.getCode() == KeyCode.TAB) {
						event.consume(); // Consumir el evento de teclado para evitar la navegación por TAB en todo el
											// form
					}
				});

				getParentController().getStagePrincipal().getScene().widthProperty()
						.addListener((observable, oldValue, newValue) -> {
							double width = newValue.doubleValue();
							String cssFile = "";
							ancho = width;

							try {

								if (width > 1680) {
									cssFile = "/estilos/Grande.css";

								}

								if (width > 1280 && width <= 1680) {
									cssFile = "/estilos/Mediano3.css";

								}
								if (width > 1152 && width <= 1280) {
									cssFile = "/estilos/Mediano2.css";

								}
								if (width > 1024 && width <= 1152) {
									cssFile = "/estilos/Mediano1.css";

								}
								if (width > 801 && width <= 1024) {
									cssFile = "/estilos/Pequeño2.css";
								}
								if (width <= 801) {
									cssFile = "/estilos/Pequeño.css";
								}

								frmEntradaDatos.getStylesheets().clear();
								frmEntradaDatos.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
								frmEntradaDatos.getStylesheets()
										.add(getClass().getResource("/estilos/ToolBar.css").toExternalForm());
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
			}
		});

		iniciarValidaciones();

		iniciarImageIcon();
		scroolPane();
		idBuscarProducto.requestFocus();

	}

	public void activacionIconosBarra(EnumActivacionIconos activacion) {
		switch (activacion) {

		case HayLegalizacionCargada:

			// Iconos no dependientes de que una legalizacion esté cargada
			parentController.NuevoToolStrip.setDisable(false);
			parentController.SubItemNuevo.setDisable(false);
			parentController.OpenToolStripButton.setDisable(false);
			parentController.SubItemAbrir.setDisable(false);
			parentController.ImportarToolStripButton.setDisable(false);
			parentController.SubItemImportar.setDisable(false);
			parentController.EncriptarOtrosFicherosToolStripMenuItem.setDisable(false);
			parentController.ConfiguracionToolStripMenuItem.setDisable(false);

			try {
				switch (MGeneral.mlform.getModo()) {

				case Normal:

					parentController.EspecificarLibrosToolStripButton.setDisable(false);
					parentController.EspecificarLibrosToolStripMenuItem.setDisable(false);
					parentController.SubItemCerrar.setDisable(false);
					parentController.SubItemComprobarReglas.setDisable(false);
					parentController.ComprobarReglasToolStrip.setDisable(false);
					parentController.SubItemImprimir.setDisable(false);
					parentController.ToolStripButtonImprimir.setDisable(false);
					parentController.GenerarZipToolStrip.setDisable(false);
					parentController.SubItemGenerarZip.setDisable(false);
					parentController.EnviarToolStrip.setDisable(false);
					parentController.SubItemEnviar.setDisable(false);

					parentController.DatosLegalizacionStripMenuItem.setDisable(false);
					parentController.VerHuellasDeLosLibrosToolStripMenuItem.setDisable(false);
					// parentController.EncriptarTodosLosLibrosToolStripMenuItem.setDisable(false);
					parentController.MenuPrincipal.setDisable(false);

					/*
					 * gbxEntidadEmpresario.setDisable(false); gbxPresentante.setDisable(false);
					 * cboRegistroMercantil.setDisable(false);
					 */
					break;

				case Recepcion:

					parentController.EspecificarLibrosToolStripButton.setDisable(false);
					parentController.EspecificarLibrosToolStripMenuItem.setDisable(false);
					parentController.SubItemCerrar.setDisable(false);
					parentController.SubItemComprobarReglas.setDisable(false);
					parentController.ComprobarReglasToolStrip.setDisable(false);
					parentController.SubItemImprimir.setDisable(true);
					parentController.ToolStripButtonImprimir.setDisable(true);
					parentController.GenerarZipToolStrip.setDisable(true);
					parentController.SubItemGenerarZip.setDisable(true);
					parentController.EnviarToolStrip.setDisable(true);
					parentController.SubItemEnviar.setDisable(true);
					parentController.DatosLegalizacionStripMenuItem.setDisable(true);
					parentController.VerHuellasDeLosLibrosToolStripMenuItem.setDisable(true);
					// EncriptarTodosLosLibrosToolStripMenuItem.setDisable(true);
					parentController.MenuPrincipal.setDisable(false);

					EntidadEmpresarioGB.setDisable(true);
					PresentanteGB.setDisable(true);

					gbxEntidadEmpresario.setDisable(true);
					gbxPresentante.setDisable(true);

					break;

				case SoloLectura, SoloReenviar:

					parentController.EspecificarLibrosToolStripButton.setDisable(false);
					parentController.EspecificarLibrosToolStripMenuItem.setDisable(false);
					parentController.SubItemCerrar.setDisable(false);
					parentController.SubItemComprobarReglas.setDisable(false);
					parentController.ComprobarReglasToolStrip.setDisable(false);
					parentController.SubItemImprimir.setDisable(false);

					parentController.ToolStripButtonImprimir.setDisable(false);
					parentController.GenerarZipToolStrip.setDisable(true);
					parentController.SubItemGenerarZip.setDisable(true);

					switch (MGeneral.mlform.getModo()) {
					case SoloLectura:
						parentController.EnviarToolStrip.setDisable(true);
						parentController.SubItemEnviar.setDisable(true);

						break;
					case SoloReenviar:
						parentController.EnviarToolStrip.setDisable(false);
						parentController.SubItemEnviar.setDisable(false);
						break;
					}

					parentController.DatosLegalizacionStripMenuItem.setDisable(false);
					parentController.VerHuellasDeLosLibrosToolStripMenuItem.setDisable(false);
					// parentController.EncriptarTodosLosLibrosToolStripMenuItem.setDisable(true);
					parentController.MenuPrincipal.setDisable(false);

					EntidadEmpresarioGB.setDisable(true);
					PresentanteGB.setDisable(true);
					gbxEntidadEmpresario.setDisable(true);
					gbxPresentante.setDisable(true);

					break;

				default:
					break;
				}
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		case NoHayLegalizacionCargada:

			// Iconos dependientes de que una legalizacion o una recepcion esté cargada
			parentController.EspecificarLibrosToolStripButton.setDisable(true);
			parentController.EspecificarLibrosToolStripMenuItem.setDisable(true);
			parentController.SubItemCerrar.setDisable(true);
			parentController.SubItemComprobarReglas.setDisable(true);
			parentController.ComprobarReglasToolStrip.setDisable(true);
			parentController.SubItemImprimir.setDisable(true);
			parentController.ToolStripButtonImprimir.setDisable(true);
			parentController.GenerarZipToolStrip.setDisable(true);
			parentController.SubItemGenerarZip.setDisable(true);
			parentController.EnviarToolStrip.setDisable(true);

			// Iconos no dependientes de que una legalizacion esté cargada
			parentController.SubItemEnviar.setDisable(true);
			parentController.DatosLegalizacionStripMenuItem.setDisable(true);

			parentController.VerHuellasDeLosLibrosToolStripMenuItem.setDisable(true);
			// EncriptarTodosLosLibrosToolStripMenuItem.setDisable(true);
			parentController.NuevoToolStrip.setDisable(false);
			parentController.SubItemNuevo.setDisable(false);

			parentController.OpenToolStripButton.setDisable(false);
			parentController.SubItemAbrir.setDisable(false);
			parentController.ImportarToolStripButton.setDisable(false);
			parentController.SubItemImportar.setDisable(false);
			parentController.EncriptarOtrosFicherosToolStripMenuItem.setDisable(false);
			parentController.ConfiguracionToolStripMenuItem.setDisable(false);
			parentController.MenuPrincipal.setDisable(false);

			/*
			 * gbxEntidadEmpresario.setDisable(true); gbxPresentante.setDisable(true);
			 * cboRegistroMercantil.setDisable(true);
			 */

			break;

		case DesactivarTodo:

			// Iconos dependientes de que una legalizacion esté cargada
			parentController.EspecificarLibrosToolStripButton.setDisable(true);
			parentController.EspecificarLibrosToolStripMenuItem.setDisable(true);
			parentController.SubItemCerrar.setDisable(true);
			parentController.SubItemComprobarReglas.setDisable(true);
			parentController.ComprobarReglasToolStrip.setDisable(true);
			parentController.SubItemImprimir.setDisable(true);
			parentController.ToolStripButtonImprimir.setDisable(true);
			parentController.GenerarZipToolStrip.setDisable(true);
			parentController.SubItemGenerarZip.setDisable(true);
			parentController.EnviarToolStrip.setDisable(true);

			// Iconos no dependientes de que una legalizacion esté cargada
			parentController.SubItemEnviar.setDisable(true);
			parentController.DatosLegalizacionStripMenuItem.setDisable(true);
			parentController.VerHuellasDeLosLibrosToolStripMenuItem.setDisable(true);
			// EncriptarTodosLosLibrosToolStripMenuItem.setDisable(true);
			parentController.NuevoToolStrip.setDisable(true);

			parentController.SubItemNuevo.setDisable(true);
			parentController.OpenToolStripButton.setDisable(true);
			parentController.SubItemAbrir.setDisable(true);
			parentController.ImportarToolStripButton.setDisable(true);
			parentController.SubItemImportar.setDisable(true);
			parentController.EncriptarOtrosFicherosToolStripMenuItem.setDisable(true);
			parentController.ConfiguracionToolStripMenuItem.setDisable(true);
			parentController.MenuPrincipal.setDisable(true);
			/*
			 * gbxEntidadEmpresario.setDisable(true); gbxPresentante.setDisable(true);
			 * cboRegistroMercantil.setDisable(true);
			 */
			break;
		}
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

	public boolean cerrar() {
		boolean cerrar = true;
		if (cancelaGuardar()) {

			return false;
		}

		guardarDatosLegalizacion();

		String cad = null;
		MGeneral.mlform = null;

		cad = MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,
				getParentController().getStagePrincipal().getScene().getRoot().getId().toString(),
				ElementosIdiomaC.TEXT_FORMULARIO, "", "");

		getParentController().getStagePrincipal().setTitle(cad);

		activacionIconosBarra(EnumActivacionIconos.NoHayLegalizacionCargada);
		parentController.AnchorPane3.getChildren().remove(frmEntradaDatos);
		return cerrar;
	}

	@FXML
	public void EliminarProducto(ActionEvent e) throws Exception {
		if (selectedProducts.size() != 0) {
			if (IdiomaC.MostrarMensaje(EnumMensajes.EliminarProducto, "", "", "")) {

				objJPAControllerProduct.eliminarProducto(selectedProducts.get(0));
				cargarProductos();
			}
		}
	}

	@FXML
	public void ActualizarProductos(ActionEvent e) throws Exception {
		cargarProductos();
	}

	@FXML
	public void GuardarProductos(ActionEvent e) throws Exception {
		
		Product objProduct=new Product();
		objProduct.setTb_product_id(selectedProducts.get(0).getTb_product_id());
		objProduct.setCode(idCodigoProducto.getText());
		objProduct.setName(idNombreProducto.getText());
		objProduct.setDescription(idDescProducto.getText());
		objProduct.setSalesPriceWithTax(new BigDecimal( idPrecioVenta.getText()));
		objProduct.setDefaultUom(idUM.getValue());
		objProduct.setEan(idEanProducto.getText());
		objJPAControllerProduct.actualizarProducto(objProduct);
		cargarProductos();
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

	private void evValidarInput(Control control, CustomImageView cview, Pane iconPane) {

		if (!((TextField) control).getText().isEmpty()) {
			ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(), iconPane,
					cview, false, control);

		}

	}

	private void evValidarInputCombobox(CustomCombobox control, CustomImageView cview, Pane iconPane) {

		if (!control.getEditor().getText().isEmpty()) {
			ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(), iconPane,
					cview, false, control);

		}
	}

	private void evValidarInputComboboxSegundavalidacion(CustomCombobox control, CustomImageView cview, Pane iconPane) {

		if (control.getEditor().getText().isEmpty()) {
			ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(), iconPane,
					cview, false, control);

		}
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

	private void cargaDatosPresentacion() {

		// Comboboxes
		String codigo = MGeneral.mlform.Presentacion.getProvinciaCodigo();

		MaestroCodigoDescripcion provincia = new MaestroCodigoDescripcion("Provincias");

		String codigo2 = MGeneral.mlform.Presentacion.getPresentante().get_ProvinciaCodigo();
		MaestroCodigoDescripcion provinciados = new MaestroCodigoDescripcion("Provincias");

		String codigo3 = MGeneral.mlform.Presentacion.getRegistroMercantilDestinoCodigo();

		MaestroCodigoDescripcion registros = new MaestroCodigoDescripcion("Registros");

		if (registros.existeCodigo(codigo3)) {
			registros.setCodigo(codigo3);
			registros.setDescripcion(registros.obtenerDescripcion(codigo3));

		}

		MaestroCodigoDescripcion retencion = new MaestroCodigoDescripcion("SolicitaRetencion");
		String codigo4 = MGeneral.mlform.Presentacion.getPresentante().get_SolicitaRetencion();

		MaestroCodigoDescripcion tipo = new MaestroCodigoDescripcion("TipoPersona");
		String codigo5 = MGeneral.mlform.Datos.get_TipoPersona();

		// DatePicker
		// Convertir el String a LocalDate
		LocalDate fecha = convertirAFecha(MGeneral.mlform.Presentacion.getFechaSolicitud());

		// //Covertir a localdate

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

			guardarDatosLegalizacion();

		} catch (Exception ex) {

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
			ex.printStackTrace();
		}

		return true;
	}

	public void guardarDatosLegalizacion() {
		// nota: Solo falta ajustar lo del tipo persona visualizacion de las combos y
		// eso (juridica y fisica)
//Verificando
		try {
			if (MGeneral.mlform == null) {

				return;
			}
			if (pendienteGuardar == false) {

				return;
			}
//Registro Mercantil			
//Aca creo que con un get codigo y get descripcion funca igual
			String registroMercantilDestinoCodigo;

//CodigoP

//ProvinciaSoli
			String provinciaCodigo;

//FaxSoli

			MGeneral.mlform.guarda();

			pendienteGuardar = false;

		} catch (Exception ex) {
			ex.printStackTrace();
			// MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion,
			// ex.getMessage(), "", "");
		} finally {
			setPendienteGuardar(false);
		}
	}

	public void recargaMuniSoli(ActionEvent event) {
		try {

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void operacion(EnumTipoOperacion tipoOperacion) {

		try {
			if (!guardar(true, true)) {

				return;
			}
			Utilidades.cursorEspera(getParentController().getStagePrincipal(), true);
			MGeneral.mlform.iniciarProgressBar(parentController.StatusProgressBar);

			// Para el modo Recepción, solo se puede imprimir
			if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Recepcion) {

				if (tipoOperacion == EnumTipoOperacion.Imprimir) {
					// ReportePreviewService reportDiagnostico = new ReportePreviewService();
					// reportDiagnostico.cargarReporteDiagnostico();
				}

				return;
			}

			if (!validarTodosLosControles(frmEntradaDatos, false, false)) {

				return;
			}

			kLegalizacion.enumResultadoValidacion resul;
			resul = MGeneral.mlform.valida();

			if (resul == kLegalizacion.enumResultadoValidacion.NoValida) {
				validar(false);

				return;
			}

			if (resul == kLegalizacion.enumResultadoValidacion.ValidaConAvisos) {
				if (!MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ExistenErroresSecundarios, "", "", "")) {
					validar(false);

					return;
				}

			}

			LegalizacionService.EnumResultadoZip resulZip = null;

			switch (tipoOperacion) {
			case GenerarZip:
				// getParentController().StatusProgressBar;
				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloLectura
						|| MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloReenviar) {

					return;
				}
				File ficheroZip = new File(MGeneral.mlform.getPathFicheroZip());
				if (!ficheroZip.isDirectory()) {
					if (ficheroZip.exists()) {

						IdiomaC.MostrarMensaje(EnumMensajes.ZIPYaExiste, "", "", "");

						if (!IdiomaC.MostrarMensaje(EnumMensajes.FicheroZipYaGenerado, "", "", "")) {
							return;
						}

					}
				}

				// Utilidades.cursorEsperaJavaFX(getParentController().getStagePrincipal(),
				// true, Duration.INDEFINITE);

				resulZip = MGeneral.mlform.generarZip("");

				if (resulZip == LegalizacionService.EnumResultadoZip.Correcto) {
					if (!MGeneral.mlform.generaInstancia()) {

						return;
					}

				}
				// Utilidades.cursorEsperaJavaFX(getParentController().getStagePrincipal(),
				// false, Duration.ZERO);

				// getParentController().StatusProgressBar;
				if (resulZip == LegalizacionService.EnumResultadoZip.Correcto) {

					FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ZipGenerado.fxml"));
					Parent ZipGenerado = loader.load();

					ZipGeneradoController zipGeneradoController = loader.getController();
					Stage stage = new Stage();
					Scene scene = new Scene(ZipGenerado);

					stage.initModality(Modality.APPLICATION_MODAL);
					stage.initStyle(StageStyle.UTILITY);

					stage.getIcons().clear();
					stage.setScene(scene);

					stage.showAndWait();

				}

				activacionIconosBarra(EnumActivacionIconos.DesactivarTodo);

				Ficheros.FicheroBorra(MGeneral.mlform.getPathFicheroInstancia());

				break;

			default:
				break;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		} finally {
			activacionIconosBarra(EnumActivacionIconos.HayLegalizacionCargada);
			// MGeneral.mlform.getVProgreso().finaliza();
			// estado("");
			Utilidades.cursorEspera(getParentController().getStagePrincipal(), false);

		}
	}

	public void validar(boolean msjValidar) {
		try {
			if (MGeneral.mlform == null)
				return;

			if (!guardar(true, msjValidar))
				return;

			if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Normal
					|| MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloLectura
					|| MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloReenviar) {

				if (!validarTodosLosControles(frmEntradaDatos, false, true)) {
					return;
				}

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ComprobarReglas.fxml"));
				Parent comprobarReglas = loader.load();

			
			}

			if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Recepcion) {
				// Mostrar formulario para estado de legalización
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EstadoLegalizacion.fxml"));
				Parent estadoLegalizacion = loader.load();

				stage = new Stage();
				scene = new Scene(estadoLegalizacion);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.initStyle(StageStyle.UTILITY);
				stage.getIcons().clear();
				stage.setScene(scene);
				MGeneral.Idioma.cargarIdiomaControles(stage, null);
				stage.showAndWait();
				// aniadirListener();
			}
		} catch (Exception ex) {
			// Mostrar mensaje de error
			ex.printStackTrace();
		}
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

	private void ErrorProvider(String error, Pane paneIconImageView, CustomImageView cbImageView,
			boolean validarParpadeoImagen, Control c) {
		Tooltip tooltip = new Tooltip(error);
		if (validarParpadeoImagen) {
			Tooltip.install(paneIconImageView, tooltip);
			imagenParpadeo(validarParpadeoImagen, cbImageView, c);
		} else {
			Tooltip.uninstall(paneIconImageView, tooltip);
			imagenParpadeo(validarParpadeoImagen, cbImageView, c);
		}

	}

	public void evSoloNumerosInterfaz(KeyEvent event) {
		Utilidades.evSoloNumeros(null, event);
	}

	public void abrirRecepcion(String pathDatos, String identificadorEntrada) {
		try {

			pendienteGuardar = false;

			activacionIconosBarra(EnumActivacionIconos.DesactivarTodo);

			MGeneral.mlform = new LegalizacionService(true, LegalizacionService.EnumModo.Recepcion);

			MGeneral.mlform.carga(pathDatos);

			MGeneral.mlform.Datos.set_IdentificadorEntrada(identificadorEntrada);

			MGeneral.mlform.Datos.setFormato(kLegalizacion.enumFormato.Legalia2);

			if (!MGeneral.mlform.validarEstructura()) {
				return;
			}

			// Cargar datos de presentación
			cargaDatosPresentacion();
			pendienteGuardar = false;
			// Establecer el título
			getParentController().establecerTitulo();

			activacionIconosBarra(EnumActivacionIconos.HayLegalizacionCargada);
			

		} catch (Exception ex) {
			// Mostrar mensaje de error
			// MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion,
			// ex.getMessage(), "", "");
		}
	}



	public void irACampoFoco() {
		try {
			Control controlAux;

			if (campoPonerFoco == null || campoPonerFoco.isEmpty())
				return;

			if (campoPonerFoco.equals("LIBROS")) {
				
			} else {

				switch (campoPonerFoco) {
				case "txtNombreODenominacion":

					break;
				default:
					break;
				}

				controlAux = Utilidades.buscaControlPorNombre(frmEntradaDatos, campoPonerFoco);

				if (controlAux != null) {
					controlAux.requestFocus();
				}

			}

			campoPonerFoco = "";

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void evValidarRegistroPublico(Control control) {
		if (((TextField) control).getText().isEmpty()) {
			((TextField) control).setText(MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,
					frmEntradaDatos.getId().toString(), ElementosIdiomaC.TEXT_CONTROLES, control.getId(), ""));
		}
	}

	public void aniadirTimelines() {

		for (int i = 0; i < controlsInOrderToNavigate.length; i++) {
			objTimeline.arrayListTimeline.add(new TimeLineClass(new Timeline(), controlsInOrderToNavigate[i]));
		}
	}

	public int obtenerindex(Control c) {
		for (int i = 0; i < objTimeline.arrayListTimeline.size(); i++) {
			if (objTimeline.arrayListTimeline.get(i).ctrl == c) {
				return i;
			}
		}
		return 0;
	}

	private void imagenParpadeo(boolean validarImagenParpadeo, CustomImageView cbImageView, Control c) {
		aniadirTimelines();
		int index = obtenerindex(c);
		objTimeline.arrayListTimeline.get(index).timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, event -> cbImageView.setVisible(true)),
				new KeyFrame(Duration.seconds(0.5), event -> cbImageView.setVisible(false)),
				new KeyFrame(Duration.seconds(1), event -> cbImageView.setVisible(true)),
				new KeyFrame(Duration.seconds(1.5), event -> cbImageView.setVisible(false)),
				new KeyFrame(Duration.seconds(2), event -> cbImageView.setVisible(true)),
				new KeyFrame(Duration.seconds(2.5), event -> cbImageView.setVisible(false)),
				new KeyFrame(Duration.seconds(3), event -> cbImageView.setVisible(true)));

		if (validarImagenParpadeo) {
			objTimeline.arrayListTimeline.get(index).timeline.setCycleCount(1);
			objTimeline.arrayListTimeline.get(index).timeline.play();
		} else {
			objTimeline.arrayListTimeline.get(index).timeline.stop();
			objTimeline.arrayListTimeline.get(index).timeline.getKeyFrames().clear();
			cbImageView.setVisible(false);
			cbImageView.setManaged(false);
		}
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
