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
import java.time.LocalDateTime;
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
import com.commerceapp.app.JPAControllerCustomer;
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
import com.commerceapp.model.Customer;
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
	

	private ArrayList<Customer> selectedClientes = new ArrayList<>();

	public Control[] controlsInOrderToNavigate;

	JPAControllerCustomer objJPAControllerCustomer;

	 @FXML
	    private GridPane PresentanteGB;

	    @FXML
	    private AnchorPane anchorpaneEntradaDatos;

	    @FXML
	    private Button btnBuscar;

	    @FXML
	    private Button btnNuevo;

	    @FXML
	    private HBox frmCrearClientes;

	    @FXML
	    private Label gbxPresentante;

	    @FXML
	    private TextField idCodigoCliente;

	    @FXML
	    private TextField idDescuentoCliente;

	    @FXML
	    private Button idGuardarCliente;

	    @FXML
	    private TextField idNombreCliente;

	    @FXML
	    private TextField idNombreComercialCliente;

	    @FXML
	    private VBox idVOBXprueba;

	    @FXML
	    private ScrollPane idpruebascroll;

	    @FXML
	    void buscarCliente(ActionEvent event) {

	    }

	    @FXML
	    void guardaCliente(ActionEvent event) {

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
		idGuardarCliente.setDisable(!value);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		objJPAControllerCustomer = new JPAControllerCustomer();
		initializeControlsInOrderToNavigate();
		registerKeyPressENTERInControlsToNavigate();
		controlsInOrderToNavigate = new Control[] { };

		

		
	
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				pendienteGuardar = false;
				primeraVez = true;

				/*getParentController().getStagePrincipal().getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
					if (event.getCode() == KeyCode.TAB) {
						event.consume(); // Consumir el evento de teclado para evitar la navegación por TAB en todo el
											// form
					}
				});*/

				
						
			}
		});

		

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
					
			       
					objCliente.setFiscalNumber(idCodigoCliente.getText()+"-"+idNombreCliente.getText());
			        
					BigDecimal decimal = new BigDecimal(idDescuentoCliente.getText());
					objCliente.setDiscountProduct(decimal);
					
					objCliente.setCreatedAt(LocalDateTime.now()); // Fecha y hora actual
					objCliente.setUpdatedAt(LocalDateTime.now()); // 
					
					
					objJPAControllerCustomer.crearCliente(objCliente);
					
					idCodigoCliente.setText("");
					idNombreCliente.setText("");
					idNombreComercialCliente.setText("");
					idDescuentoCliente.setText("");
					//cargarClientes();
					//deshabilitarHabilitarBotonesCreacionCliente(false);
					//validacionesTextfieldsObligatorias(false);
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
	

	

	private void rellenarCampos() {
		

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

	public void setPvc(PedidoVentaController pedidoVentaController) {
		// TODO Auto-generated method stub
		
	}

}
