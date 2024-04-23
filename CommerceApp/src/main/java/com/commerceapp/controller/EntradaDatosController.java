package com.commerceapp.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
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
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.commerceapp.Main;
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
import com.commerceapp.reporting.instancia.ReportingPreviewService;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.service.LegalizacionService.EnumResultadoZip;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;
import com.commerceapp.util.Utilidades.TimeLineClass;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.control.ScrollPane;

@Component
public class EntradaDatosController implements Initializable, NavigableControllerHelper {
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
	private CustomCombobox<String> cboMunicipio;

	@FXML
	private CustomCombobox<String> cboPresentanteMunicipio;

	@FXML
	private CustomCombobox<MaestroCodigoDescripcion> cboPresentanteSolicitaRetencion;

	@FXML
	private CustomCombobox<MaestroCodigoDescripcion> cboProvincia;

	@FXML
	private CustomCombobox<MaestroCodigoDescripcion> cboTipoPersona;

	@FXML
	private CustomCombobox<MaestroCodigoDescripcion> cboxProvinciaPres;

	@FXML
	public CustomCombobox<MaestroCodigoDescripcion> cboxRegistroMercantil;
	@FXML
	private CustomCombobox<MaestroCodigoDescripcion> cboPresentanteProvincia;

	@FXML
	private HBox frmEntradaDatos;

	@FXML
	private Label gbxDatosRegistrales;

	@FXML
	private Label gbxEntidadEmpresario;

	@FXML
	private Label gbxPresentante;

	@FXML
	private CustomImageView iconErrorEmailPres;

	@FXML
	private Pane iconPanErrorEmailPres;

	@FXML
	private CustomImageView iconAlertNIFPres;

	@FXML
	private CustomImageView iconAlertCIFSoli;
	@FXML
	private GridPane EntidadEmpresarioGB;
	@FXML
	private GridPane PresentanteGB;

	@FXML
	private Pane iconPaneApellidoSoli;

	@FXML
	private CustomImageView iconApellidoSoli;

	@FXML
	private Pane iconPaneNombreSoli;

	@FXML
	private CustomImageView iconNombreSoli;

	@FXML
	private Pane iconPaneNIFPres;

	@FXML
	private CustomImageView iconAlertPostalPres;

	@FXML
	private Pane iconPaneAlertPostalPres;

	@FXML
	private CustomImageView iconDomicilioSoli;

	@FXML
	private Pane iconPaneDomicilioSoli;

	@FXML
	private CustomImageView iconDenominacionJuridicoApellidoSoli;

	@FXML
	private Pane iconPaneApellidoDenominacionJuridicoSoli;

	@FXML
	private CustomImageView iconAlertCIFNIFSoli;

	@FXML
	private Pane iconPaneFechaSoli;

	@FXML
	private CustomImageView iconFechaSoli;

	@FXML
	private Pane iconPaneAlertCodPostal;

	@FXML
	private CustomImageView iconAlertCodPostal;

	@FXML
	private Pane iconPaneRegistroMercantil;

	@FXML
	private CustomImageView iconRegistroMercantil;

	@FXML
	private Pane iconPaneAlertCIFNIFSoli;

	@FXML
	private CustomImageView iconMunicipioSoli;

	@FXML
	private Pane iconPaneMunicipioSoli;

	@FXML
	private CustomImageView iconProvincia;

	@FXML
	private CustomImageView iconProvinciaError;

	@FXML
	private Pane iconPaneProvincia;

	@FXML
	private VBox idVOBXprueba;

	@FXML
	private ScrollPane idpruebascroll;

	@FXML
	private Label lblApellido1;

	@FXML
	private Label lblApellido2;

	@FXML
	private Label lblCif;

	@FXML
	private Label lblCiudad;

	@FXML
	private Label lblCodigoPostal;

	@FXML
	private Label lblDatosRegistralesFolio;

	@FXML
	private Label lblDatosRegistralesHoja;

	@FXML
	private Label lblDatosRegistralesOtros;

	@FXML
	private Label lblDatosRegistralesTomo;

	@FXML
	private Label lblDenominacion;

	@FXML
	private Label lblDomicilio;

	@FXML
	private Label lblFax;

	@FXML
	private Label lblFechaSolicitud;

	@FXML
	private Label lblNif;

	@FXML
	private Label lblNombre;

	@FXML
	private Label lblPresentanteApellido1;

	@FXML
	private Label lblPresentanteApellido2;

	@FXML
	private Label lblPresentanteCiudad;

	@FXML
	private Label lblPresentanteCodigoPostal;

	@FXML
	private Label lblPresentanteDomicilio;

	@FXML
	private Label lblPresentanteEmail;

	@FXML
	private Label lblPresentanteFax;

	@FXML
	private Label lblPresentanteNif;

	@FXML
	private Label lblPresentanteNombre;

	@FXML
	private Label lblPresentanteProvincia;

	@FXML
	private Label lblPresentanteSolicitaRetencion;

	@FXML
	private Label lblPresentanteTelefono;

	@FXML
	private Label lblProvincia;

	@FXML
	private Label lblRegistroMercantil;

	@FXML
	private Label lblTelefono;

	@FXML
	private Label lblTipoPersona;

	@FXML
	private Label lblTipoRegistroPublico;

	@FXML
	private Label lblTitulo;

	@FXML
	private Label lblTotalLibros;

	@FXML
	private TextField txtApellido1;

	@FXML
	private TextField txtApellido2;

	@FXML
	private TextField txtCifNif;

	@FXML
	private TextField txtCodigoPostal;

	@FXML
	private TextField txtDatosRegistralesFolio;

	@FXML
	private TextField txtDatosRegistralesHoja;

	@FXML
	private TextField txtDatosRegistralesOtros;

	@FXML
	private TextField txtDatosRegistralesTomo;

	@FXML
	private TextField txtDenominacion;

	@FXML
	private TextField txtDomicilio;

	@FXML
	private TextField txtFax;

	@FXML
	private CustomDatePicker txtFechaSolicitud;

	@FXML
	public TextField txtLibrosPresentados;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtPresentanteApellido1;

	@FXML
	private TextField txtPresentanteApellido2;

	@FXML
	private TextField txtPresentanteCodigoPostal;

	@FXML
	private TextField txtPresentanteDomicilio;

	@FXML
	private TextField txtPresentanteEmail;

	@FXML
	private TextField txtPresentanteFax;

	@FXML
	private TextField txtPresentanteNif;

	@FXML
	private TextField txtPresentanteNombre;

	@FXML
	private Pane lblPaneTitulo;

	@FXML
	private TextField txtPresentanteTelefono;

	@FXML
	private TextField txtTelefono;

	@FXML
	private TextField txtTipoRegistroPublico;

	@FXML
	private ImageView logo;

	@FXML
	private Tooltip tpGuardar;

	public Control[] controlsInOrderToNavigate = new Control[] {};

	public ChangeListener<Boolean> focusedListenerRegistroProvincia = (observable, oldValue, newValue) -> {
		if (!newValue) {
			evValidarAlValidar(cboxRegistroMercantil);
		}
	};

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
		parentController.GuardarToolStrip.setDisable(!value);
		parentController.SubItemGuardar.setDisable(!value);
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
					cboxRegistroMercantil.setDisable(true);
					gbxEntidadEmpresario.setDisable(true);
					gbxPresentante.setDisable(true);
					lblTitulo.setDisable(true);
					lblRegistroMercantil.setDisable(true);
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
					cboxRegistroMercantil.setDisable(true);
					gbxEntidadEmpresario.setDisable(true);
					gbxPresentante.setDisable(true);
					lblTitulo.setDisable(true);
					lblRegistroMercantil.setDisable(true);
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

	public void validarTextChangesGuardar() {

		for (int i = 0; i < controlsInOrderToNavigate.length; i++) {
			if (controlsInOrderToNavigate[i] instanceof TextField) {

				TextField controlfinal = (TextField) controlsInOrderToNavigate[i];
				controlfinal.textProperty().addListener((observable, oldValue, newValue) -> {
					if (primeraVez) {
						setPendienteGuardar(true);
					}

				});

			} else if (controlsInOrderToNavigate[i] instanceof ComboBox) {

				ComboBox controlfinal = (ComboBox) controlsInOrderToNavigate[i];
				if (controlsInOrderToNavigate[i].getId().equalsIgnoreCase("cboTipoPersona")) {
					controlfinal.valueProperty().addListener((observable, oldValue, newValue) -> {
						if (primeraVez) {
							setPendienteGuardar(true);
						}
					});
				}

				controlfinal.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
					if (primeraVez) {
						setPendienteGuardar(true);
					}

				});
			}
		}

	}

	private static final Logger logger = Logger.getLogger(EntradaDatosController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Image image = new Image(getClass().getResourceAsStream("/imagenes/Logo_REG.png"));

		logo.setFitWidth(200);
		logo.setFitHeight(98);
		logo.setImage(image);
		initializeControlsInOrderToNavigate();
		registerKeyPressENTERInControlsToNavigate();
		registerCamposVisibles();
		validarTextChangesGuardar();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				registerEventControl();
				cboxRegistroMercantil.requestFocus();
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

		GridPane.setColumnSpan(txtDomicilio, 5);
		GridPane.setColumnSpan(cboMunicipio, 5);

		GridPane.setColumnSpan(txtPresentanteDomicilio, 5);
		GridPane.setColumnSpan(cboPresentanteMunicipio, 5);
		GridPane.setColumnSpan(txtPresentanteEmail, 5);
		GridPane.setColumnSpan(txtDatosRegistralesOtros, 4);
		GridPane.setColumnSpan(lblPaneTitulo, 1);
		GridPane.setColumnSpan(txtApellido1, 2);
		GridPane.setColumnSpan(txtPresentanteApellido1, 2);
		iniciarValidaciones();
		iniciarValidacionesTextFormatter();
		iniciarImageIcon();
		scroolPane();

	}

	private void registerEventControl() {
		this.cboProvincia.addEventHandler(ActionEvent.ACTION, this::recargaMuniSoli);
		this.cboPresentanteProvincia.addEventHandler(ActionEvent.ACTION, this::recargaMuniPres);

	}

	private void registerCamposVisibles() {
		this.cboTipoPersona.addEventHandler(ActionEvent.ACTION, this::camposVisiblesSegunTipoPersona);
	}

	private void changeControlsInOrderToNavigate() {
		if (esPersonaFisica()) {
			controlsInOrderToNavigate = new Control[] { cboxRegistroMercantil, cboTipoPersona, txtNombre, txtApellido1,
					txtApellido2, txtCifNif, txtDomicilio, txtCodigoPostal, cboProvincia, cboMunicipio, txtTelefono,
					txtFax, txtFechaSolicitud, txtDatosRegistralesTomo, txtDatosRegistralesFolio,
					txtDatosRegistralesHoja, txtDatosRegistralesOtros, txtTipoRegistroPublico, txtPresentanteNombre,
					txtPresentanteApellido1, txtPresentanteApellido2, txtPresentanteNif, txtPresentanteDomicilio,
					txtPresentanteCodigoPostal, cboPresentanteProvincia, cboPresentanteMunicipio,
					txtPresentanteTelefono, txtPresentanteFax, txtPresentanteEmail, cboPresentanteSolicitaRetencion };
		} else if (esPersonaJuridica()) {
			controlsInOrderToNavigate = new Control[] { cboxRegistroMercantil, cboTipoPersona, txtDenominacion,
					txtCifNif, txtDomicilio, txtCodigoPostal, cboProvincia, cboMunicipio, txtTelefono, txtFax,
					txtFechaSolicitud, txtDatosRegistralesTomo, txtDatosRegistralesFolio, txtDatosRegistralesHoja,
					txtDatosRegistralesOtros, txtTipoRegistroPublico, txtPresentanteNombre, txtPresentanteApellido1,
					txtPresentanteApellido2, txtPresentanteNif, txtPresentanteDomicilio, txtPresentanteCodigoPostal,
					cboPresentanteProvincia, cboPresentanteMunicipio, txtPresentanteTelefono, txtPresentanteFax,
					txtPresentanteEmail, cboPresentanteSolicitaRetencion };
		}

	}

	private void iniciarImageIcon() {
		iconErrorEmailPres.setImage(new Image(getClass().getResourceAsStream("/imagenes/error.png")));
		iconErrorEmailPres.setVisible(false);

		iconAlertNIFPres.setImage(new Image(getClass().getResourceAsStream("/imagenes/aviso.png")));
		iconAlertNIFPres.setVisible(false);

		iconAlertPostalPres.setImage(new Image(getClass().getResourceAsStream("/imagenes/aviso.png")));
		iconAlertPostalPres.setVisible(false);

		iconAlertCIFNIFSoli.setImage(new Image(getClass().getResourceAsStream("/imagenes/aviso.png")));
		iconAlertCIFNIFSoli.setVisible(false);

		iconDenominacionJuridicoApellidoSoli.setImage(new Image(getClass().getResourceAsStream("/imagenes/error.png")));
		iconDenominacionJuridicoApellidoSoli.setVisible(false);

		iconDomicilioSoli.setImage(new Image(getClass().getResourceAsStream("/imagenes/error.png")));
		iconDomicilioSoli.setVisible(false);

		iconApellidoSoli.setImage(new Image(getClass().getResourceAsStream("/imagenes/error.png")));
		iconApellidoSoli.setVisible(false);

		iconNombreSoli.setImage(new Image(getClass().getResourceAsStream("/imagenes/error.png")));
		iconNombreSoli.setVisible(false);

		iconAlertCodPostal.setImage(new Image(getClass().getResourceAsStream("/imagenes/aviso.png")));
		iconAlertCodPostal.setVisible(false);

		iconFechaSoli.setImage(new Image(getClass().getResourceAsStream("/imagenes/error.png")));
		iconFechaSoli.setVisible(false);

		iconRegistroMercantil.setImage(new Image(getClass().getResourceAsStream("/imagenes/error.png")));
		iconRegistroMercantil.setVisible(false);

		iconProvincia.setImage(new Image(getClass().getResourceAsStream("/imagenes/aviso.png")));
		iconProvincia.setVisible(false);

		iconMunicipioSoli.setImage(new Image(getClass().getResourceAsStream("/imagenes/error.png")));
		iconMunicipioSoli.setVisible(false);

		iconProvinciaError.setImage(new Image(getClass().getResourceAsStream("/imagenes/error.png")));
		iconProvinciaError.setVisible(false);

	}

	private void iniciarValidacionesTextFormatter() {
		formatoTextfieldDatosPersonaSoli();
		formatoTextfieldDatosPersonaPres();
		formatoTextfieldPostalPres();
		formatoTextfieldNIFFaxTelefonoPres();
		formatoTextfieldEmailPres();
		formatoTextfieldNIFFaxTelefonoSoli();
		formatoTextfieldPostalSoli();
		formatoTextfieldTIPOREGISTRO();
		formatoTextfieldOTROS();
		formatoTextfieldHOJA();
		formatoTextfieldFOLIO();
		formatoTextfieldTOMO();

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

	void abrir(String pathDatos) {
		getParentController().cerrar(null);

		try {
			// aqui se inicializan las combos

			Utilidades.cursorEspera(getParentController().getStagePrincipal(), true);

			pendienteGuardar = false;
			activacionIconosBarra(EnumActivacionIconos.DesactivarTodo);

			getParentController().getStagePrincipal().getScene().setCursor(Cursor.WAIT);

			MGeneral.mlform = new LegalizacionService(true, null);

			MGeneral.mlform.carga(pathDatos);

			if (!MGeneral.mlform.isValidaEstructura())
				return;

			if (MGeneral.mlform.Datos.getEstadoEnvio() == cDatos.eEstadoEnvio.EnviadaPorServicioCorrectamente) {

				MGeneral.mlform = new LegalizacionService(true, LegalizacionService.EnumModo.SoloLectura);
				MGeneral.mlform.carga(pathDatos);
				if (!MGeneral.mlform.isValidaEstructura())
					return;
			}

			if (MGeneral.mlform.Datos.getEstadoEnvio() == cDatos.eEstadoEnvio.EnviadaPorServicioErrorReintentable) {

				MGeneral.mlform = new LegalizacionService(true, LegalizacionService.EnumModo.SoloReenviar);
				MGeneral.mlform.carga(pathDatos);
				if (!MGeneral.mlform.isValidaEstructura())
					return;
			}

			cboPresentanteProvincia.cargarCombo("Provincias");

			cboProvincia.cargarCombo("Provincias");

			cboxRegistroMercantil.cargarCombo("Registros");

			cboPresentanteSolicitaRetencion.cargarCombo("SolicitaRetencion");

			cboTipoPersona.cargarCombo("TipoPersona");

			cboPresentanteProvincia.setConverter(new MaestroCodigoDescripcionConverter(cboPresentanteProvincia));
			cboProvincia.setConverter(new MaestroCodigoDescripcionConverter(cboProvincia));
			cboxRegistroMercantil.setConverter(new MaestroCodigoDescripcionConverter(cboxRegistroMercantil));
			cboPresentanteSolicitaRetencion
					.setConverter(new MaestroCodigoDescripcionConverter(cboPresentanteSolicitaRetencion));
			cboTipoPersona.setConverter(new MaestroCodigoDescripcionConverter(cboTipoPersona));

			String prov = MGeneral.mlform.Presentacion.getProvinciaCodigo();

			MaestroCodigoDescripcion provincia = new MaestroCodigoDescripcion("Provincias");

			if (provincia.existeCodigo(prov)) {

				List<String> Lista = MGeneral.Idioma.obtenerMunicipiosDeProvincia(prov);

				cboMunicipio.cargarCombo(Lista);
			}

			String prov2 = MGeneral.mlform.Presentacion.getPresentante().get_ProvinciaCodigo();
			if (provincia.existeCodigo(prov2)) {

				List<String> Lista = MGeneral.Idioma.obtenerMunicipiosDeProvincia(prov);

				cboPresentanteMunicipio.cargarCombo(Lista);
			}
			cargaDatosPresentacion();
			pendienteGuardar = false;

			activacionIconosBarra(EnumActivacionIconos.HayLegalizacionCargada);
			// getParentController().activacionIconosBarra(EnumActivacionIconos.HayLegalizacionCargada);

			// Si la legalización abierta es de formato anterior se fuerza el pendiente de
			// guardar
			// (al guardar quedará ya con el nuevo formato)

			if (MGeneral.mlform.Datos.getFormato() == enumFormato.Legalia)
				pendienteGuardar = true;

			if (!new java.io.File(MGeneral.mlform.getPathFicheroDesc()).exists())
				pendienteGuardar = true;

			Utilidades.cursorEspera(getParentController().getStagePrincipal(), false);

			if (!MGeneral.mlform.isValidaCargaDeLibros()) {
				validar(true);
			}

			cboTipoPersona.requestFocus();

		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
			ex.printStackTrace();
		} finally {

			if (txtTipoRegistroPublico.getText().isEmpty()) {
				txtTipoRegistroPublico.setText(
						MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS, frmEntradaDatos.getId().toString(),
								ElementosIdiomaC.TEXT_CONTROLES, txtTipoRegistroPublico.getId(), ""));

			}
			validarTodosLosControles(frmEntradaDatos, true, false);
			Utilidades.cursorEspera(getParentController().getStagePrincipal(), false);
		}
	}

	public void camposVisiblesSegunTipoPersona(ActionEvent event) {
		String tipoPersona = "";
		txtDenominacion.setVisible(false);
		lblDenominacion.setVisible(false);
		txtNombre.setVisible(false);
		lblNombre.setVisible(false);
		txtApellido1.setVisible(false);
		lblApellido1.setVisible(false);
		txtApellido2.setVisible(false);
		lblApellido2.setVisible(false);
		lblCif.setVisible(false);
		lblNif.setVisible(false);

		txtDenominacion.setManaged(false);
		lblDenominacion.setManaged(false);
		txtNombre.setManaged(false);
		lblNombre.setManaged(false);
		txtApellido1.setManaged(false);
		lblApellido1.setManaged(false);
		txtApellido2.setManaged(false);
		lblApellido2.setManaged(false);
		lblCif.setManaged(false);
		lblNif.setManaged(false);
		iconApellidoSoli.setVisible(false);
		iconApellidoSoli.setManaged(false);

		iconNombreSoli.setVisible(false);
		iconNombreSoli.setManaged(false);

		if (cboTipoPersona.getValue() != null) {
			tipoPersona = cboTipoPersona.getValue().getCodigo();
		}

		switch (tipoPersona) {
		case kLegalizacion.kTipoPersonaFisica:

			txtNombre.setVisible(true);
			lblNombre.setVisible(true);
			txtApellido1.setVisible(true);
			lblApellido1.setVisible(true);
			txtApellido2.setVisible(true);
			lblApellido2.setVisible(true);
			lblNif.setVisible(true);

			txtNombre.setManaged(true);
			lblNombre.setManaged(true);
			txtApellido1.setManaged(true);
			lblApellido1.setManaged(true);
			txtApellido2.setManaged(true);
			lblApellido2.setManaged(true);
			lblNif.setManaged(true);

			GridPane.setColumnSpan(txtDenominacion, 1);
			break;
		case kLegalizacion.kTipoPersonaJuridica:

			txtDenominacion.setVisible(true);
			lblDenominacion.setVisible(true);
			lblCif.setVisible(true);

			txtDenominacion.setManaged(true);
			lblDenominacion.setManaged(true);
			lblCif.setManaged(true);
			GridPane.setColumnSpan(txtDenominacion, 8);
			break;
		}
	}

	void iniciarValidaciones() {
		aniadirListener();
		// Agregar controlador para el evento de cambio de texto
		txtTipoRegistroPublico.textProperty().addListener((observable, oldValue, newValue) -> {
			evValidarRegistroPublico(txtTipoRegistroPublico);
		});

		// Agregar controlador para el evento de salir del control
		txtTipoRegistroPublico.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				evValidarRegistroPublico(txtTipoRegistroPublico);
			}

		});

		cboTipoPersona.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {

				evValidarAlValidar(cboTipoPersona);
			}
		});

		cboProvincia.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				evValidarAlValidar(cboProvincia);

			}
		});

		txtNombre.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				evValidarAlValidar(txtNombre);
			}
		});

		cboProvincia.getEditor().textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInputCombobox(cboProvincia, iconProvinciaError, iconPaneProvincia));

		cboProvincia.getEditor().textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInputComboboxSegundavalidacion(cboProvincia, iconProvincia, iconPaneProvincia));

		txtCodigoPostal.textProperty().addListener((observable, oldValue, newValue) -> evValidarInput(txtCodigoPostal,
				iconAlertCodPostal, iconPaneAlertCodPostal));

		txtCifNif.textProperty().addListener((observable, oldValue, newValue) -> evValidarInput(txtCifNif,
				iconAlertCIFNIFSoli, iconPaneAlertCIFNIFSoli));

		txtNombre.textProperty().addListener(
				(observable, oldValue, newValue) -> evValidarInput(txtNombre, iconNombreSoli, iconPaneNombreSoli));

		txtApellido1.textProperty().addListener((observable, oldValue, newValue) -> evValidarInput(txtApellido1,
				iconApellidoSoli, iconPaneApellidoSoli));

		txtApellido2.textProperty().addListener((observable, oldValue, newValue) -> evValidarInput(txtApellido2,
				iconDenominacionJuridicoApellidoSoli, iconPaneApellidoDenominacionJuridicoSoli));

		txtDomicilio.textProperty().addListener((observable, oldValue, newValue) -> evValidarInput(txtDomicilio,
				iconDomicilioSoli, iconPaneDomicilioSoli));

		txtDenominacion.textProperty().addListener((observable, oldValue, newValue) -> evValidarInput(txtDenominacion,
				iconDenominacionJuridicoApellidoSoli, iconPaneApellidoDenominacionJuridicoSoli));

		txtPresentanteEmail.textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInput(txtPresentanteEmail, iconErrorEmailPres, iconPanErrorEmailPres));
		txtPresentanteCodigoPostal.textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInput(txtPresentanteCodigoPostal, iconAlertPostalPres, iconPaneAlertPostalPres));
		txtPresentanteNif.textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInput(txtPresentanteEmail, iconAlertNIFPres, iconPaneNIFPres));

		txtCodigoPostal.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				evValidarAlValidar(txtCodigoPostal);
			}
		});

		txtApellido1.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				evValidarAlValidar(txtApellido1);
			}
		});

		txtApellido2.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				evValidarAlValidar(txtApellido2);
			}
		});

		txtDomicilio.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				evValidarAlValidar(txtDomicilio);
			}
		});

		txtDenominacion.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				evValidarAlValidar(txtDenominacion);
			}
		});

		cboxRegistroMercantil.getEditor().textProperty()
				.addListener((observable, oldValue, newValue) -> evValidarInputCombobox(cboxRegistroMercantil,
						iconRegistroMercantil, iconPaneRegistroMercantil));

		cboMunicipio.getEditor().textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInputCombobox(cboMunicipio, iconMunicipioSoli, iconPaneMunicipioSoli));// revisao

		cboMunicipio.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				evValidarAlValidar(cboMunicipio);
			}
		});

		cboTipoPersona.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			changeControlsInOrderToNavigate();
			if (esPersonaJuridica()) {

				ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
						iconPaneApellidoSoli, iconApellidoSoli, false, txtApellido1);
				ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
						iconPaneNombreSoli, iconNombreSoli, false, txtNombre);
				ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
						iconPaneApellidoDenominacionJuridicoSoli, iconDenominacionJuridicoApellidoSoli, false,
						txtDenominacion);

			}

		});

		txtCifNif.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				evValidarAlValidar(txtCifNif);
			}
		});
		txtPresentanteNif.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				// Se ejecuta cuando el TextField pierde el foco
				evValidarAlValidar(txtPresentanteNif);
			}
		});
		txtFechaSolicitud.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				// Se ejecuta cuando el TextField pierde el foco
				evValidarAlValidar(txtFechaSolicitud);
			}
		});

		txtFechaSolicitud.textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInput(txtFechaSolicitud, iconFechaSoli, iconPaneFechaSoli));

		txtPresentanteCodigoPostal.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				// Se ejecuta cuando el TextField pierde el foco
				evValidarAlValidar(txtPresentanteCodigoPostal);
			}
		});

		cboPresentanteMunicipio.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				// Se ejecuta cuando el TextField pierde el foco
				evValidarAlValidar(cboPresentanteMunicipio);
			}
		});

		txtPresentanteCodigoPostal.addEventHandler(KeyEvent.KEY_TYPED, this::evSoloNumerosInterfaz);

		txtCodigoPostal.addEventHandler(KeyEvent.KEY_TYPED, this::evSoloNumerosInterfaz);

		txtDatosRegistralesTomo.addEventHandler(KeyEvent.KEY_TYPED, this::evSoloNumerosInterfaz);

		txtDatosRegistralesFolio.addEventHandler(KeyEvent.KEY_TYPED, this::evSoloNumerosInterfaz);

		txtDatosRegistralesTomo.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				// Se ejecuta cuando el TextField pierde el foco
				evValidarAlValidar(txtDatosRegistralesTomo);
			}
		});

		txtDatosRegistralesFolio.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				// Se ejecuta cuando el TextField pierde el foco
				evValidarAlValidar(txtDatosRegistralesFolio);
			}
		});

		txtDatosRegistralesHoja.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				// Se ejecuta cuando el TextField pierde el foco
				evValidarAlValidar(txtDatosRegistralesHoja);
			}
		});

		txtDatosRegistralesOtros.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				// Se ejecuta cuando el TextField pierde el foco
				evValidarAlValidar(txtDatosRegistralesOtros);
			}
		});

		txtPresentanteEmail.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				// Se ejecuta cuando el TextField pierde el foco
				evValidarAlValidar(txtPresentanteEmail);
			}
		});

		txtPresentanteEmail.textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInput(txtPresentanteEmail, iconErrorEmailPres, iconPanErrorEmailPres));
		txtPresentanteCodigoPostal.textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInput(txtPresentanteCodigoPostal, iconAlertPostalPres, iconPaneAlertPostalPres));
		txtPresentanteNif.textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInput(txtPresentanteNif, iconAlertNIFPres, iconPaneNIFPres));

		txtLibrosPresentados.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

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

		if (MGeneral.mlform.Datos.get_TipoPersona().equals(kLegalizacion.kTipoPersonaFisica)) {
			txtNombre.setText(MGeneral.mlform.Presentacion.getNombreSociedadoEmpresario());
		} else if (MGeneral.mlform.Datos.get_TipoPersona().equals(kLegalizacion.kTipoPersonaJuridica)) {
			txtDenominacion.setText(MGeneral.mlform.Presentacion.getNombreSociedadoEmpresario());
		}
		// Comboboxes
		String codigo = MGeneral.mlform.Presentacion.getProvinciaCodigo();

		MaestroCodigoDescripcion provincia = new MaestroCodigoDescripcion("Provincias");

		if (provincia.existeCodigo(codigo)) {
			provincia.setCodigo(codigo);
			provincia.setDescripcion(provincia.obtenerDescripcion(codigo));
			cboProvincia.setValue(provincia);
		}
		cboMunicipio.setValue(MGeneral.mlform.Presentacion.getCiudad());

		System.out.print(cboProvincia.getValue());
		System.out.print(cboMunicipio.getValue());

		String codigo2 = MGeneral.mlform.Presentacion.getPresentante().get_ProvinciaCodigo();
		MaestroCodigoDescripcion provinciados = new MaestroCodigoDescripcion("Provincias");
		if (provinciados.existeCodigo(codigo2)) {
			provinciados.setCodigo(codigo2);
			provinciados.setDescripcion(provinciados.obtenerDescripcion(codigo2));
			cboPresentanteProvincia.setValue(provinciados);
		}

		System.out.print(cboProvincia.getValue());
		System.out.print(cboMunicipio.getValue());
		cboPresentanteMunicipio.setValue(MGeneral.mlform.Presentacion.getPresentante().get_Ciudad());

		String codigo3 = MGeneral.mlform.Presentacion.getRegistroMercantilDestinoCodigo();

		MaestroCodigoDescripcion registros = new MaestroCodigoDescripcion("Registros");

		if (registros.existeCodigo(codigo3)) {
			registros.setCodigo(codigo3);
			registros.setDescripcion(registros.obtenerDescripcion(codigo3));
			cboxRegistroMercantil.setValue(registros);
		}

		MaestroCodigoDescripcion retencion = new MaestroCodigoDescripcion("SolicitaRetencion");
		String codigo4 = MGeneral.mlform.Presentacion.getPresentante().get_SolicitaRetencion();

		if (retencion.existeCodigo(codigo4)) {
			retencion.setCodigo(codigo4);
			retencion.setDescripcion(retencion.obtenerDescripcion(codigo4));
			cboPresentanteSolicitaRetencion.setValue(retencion);
		}
		MaestroCodigoDescripcion tipo = new MaestroCodigoDescripcion("TipoPersona");
		String codigo5 = MGeneral.mlform.Datos.get_TipoPersona();

		if (tipo.existeCodigo(codigo5)) {
			tipo.setCodigo(codigo5);
			tipo.setDescripcion(tipo.obtenerDescripcion(codigo5));
			cboTipoPersona.setValue(tipo);
		}

		// DatePicker
		// Convertir el String a LocalDate
		LocalDate fecha = convertirAFecha(MGeneral.mlform.Presentacion.getFechaSolicitud());
		txtFechaSolicitud.setValue(fecha);
		// //Covertir a localdate

		// TextField
		txtPresentanteNombre.setText(MGeneral.mlform.Presentacion.getPresentante().get_Nombre());
		txtPresentanteApellido1.setText(MGeneral.mlform.Presentacion.getPresentante().get_Apellido1());
		txtPresentanteApellido2.setText(MGeneral.mlform.Presentacion.getPresentante().get_Apellido2());
		txtPresentanteCodigoPostal.setText(MGeneral.mlform.Presentacion.getPresentante().get_CodigoPostal());
		txtPresentanteDomicilio.setText(MGeneral.mlform.Presentacion.getPresentante().get_Domicilio());
		txtPresentanteEmail.setText(MGeneral.mlform.Presentacion.getPresentante().get_Email());
		txtPresentanteFax.setText(MGeneral.mlform.Presentacion.getPresentante().get_Fax());
		txtPresentanteNif.setText(MGeneral.mlform.Presentacion.getPresentante().get_Nif());
		txtPresentanteTelefono.setText(MGeneral.mlform.Presentacion.getPresentante().get_Telefono());

		txtApellido1.setText(MGeneral.mlform.Presentacion.getApellido1());
		txtApellido2.setText(MGeneral.mlform.Presentacion.getApellido2());
		txtCodigoPostal.setText(MGeneral.mlform.Presentacion.getCodigoPostal());
		txtDomicilio.setText(MGeneral.mlform.Presentacion.getDomicilio());
		txtFax.setText(MGeneral.mlform.Presentacion.getFax());
		txtDatosRegistralesFolio.setText(MGeneral.mlform.Presentacion.getDatosRegistralesFolio());
		txtDatosRegistralesHoja.setText(MGeneral.mlform.Presentacion.getDatosRegistralesHoja());
		txtLibrosPresentados.setText(MGeneral.mlform.Presentacion.getDatosRegistralesLibro());
		txtCifNif.setText(MGeneral.mlform.Presentacion.getNifCif());

		txtDatosRegistralesOtros.setText(MGeneral.mlform.Presentacion.getDatosRegistralesOtros());
		txtTelefono.setText(MGeneral.mlform.Presentacion.getTelefono());
		txtTipoRegistroPublico.setText(MGeneral.mlform.Presentacion.getTipoRegistroPublico());
		txtDatosRegistralesTomo.setText(MGeneral.mlform.Presentacion.getDatosRegistralesTomo());
		txtLibrosPresentados.setText(String.valueOf(MGeneral.mlform.getNumeroTotalFicherosPresentados()));

		txtTipoRegistroPublico.setText(MGeneral.mlform.Presentacion.getTipoRegistroPublico());
		System.out.print(cboProvincia.getValue());
		System.out.print(cboMunicipio.getValue());

		System.out.print(cboPresentanteProvincia.getValue());
		System.out.print(cboPresentanteMunicipio.getValue());
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

			if (cboxRegistroMercantil.getValue() == null) {
				registroMercantilDestinoCodigo = "";
			} else {
				MaestroCodigoDescripcion maestro = new MaestroCodigoDescripcion("Registros");
				registroMercantilDestinoCodigo = maestro
						.obtenerCodigoDeDescripcion(cboxRegistroMercantil.getValue().getDescripcion());
				MGeneral.mlform.Presentacion
						.setRegistroMercantilDestinoDescripcion(cboxRegistroMercantil.getValue().getDescripcion());
			}

			MGeneral.mlform.Presentacion.setRegistroMercantilDestinoCodigo(registroMercantilDestinoCodigo);

//Tipo Persona
			if (cboTipoPersona.getValue() == null) {
				MGeneral.mlform.Datos.set_TipoPersona("");
			} else {
				MGeneral.mlform.Datos.set_TipoPersona(cboTipoPersona.getValue().getCodigo().toUpperCase());
			}

			if (MGeneral.mlform.Datos.get_TipoPersona().equals(kLegalizacion.kTipoPersonaFisica)) {
				// Nombre Soli
				MGeneral.mlform.Presentacion.setNombreSociedadoEmpresario(txtNombre.getText());
				// Apellido1 Soli
				MGeneral.mlform.Presentacion.setApellido1(txtApellido1.getText());
				// Apelldo 2 Soli
				MGeneral.mlform.Presentacion.setApellido2(txtApellido2.getText());

			}

			if (MGeneral.mlform.Datos.get_TipoPersona().equals(kLegalizacion.kTipoPersonaJuridica)) {
				MGeneral.mlform.Presentacion.setNombreSociedadoEmpresario(txtDenominacion.getText());
				MGeneral.mlform.Presentacion.setApellido1("");
				MGeneral.mlform.Presentacion.setApellido2("");
			}
//Nif Soli
			MGeneral.mlform.Presentacion.setNifCif(txtCifNif.getText());
//Domicilio Soli
			MGeneral.mlform.Presentacion.setDomicilio(txtDomicilio.getText());
//MuniSoli (quiza falte seguridad)
			if (cboMunicipio.getValue() == null) {
				MGeneral.mlform.Presentacion.setCiudad("");
			} else {
				MGeneral.mlform.Presentacion.setCiudad(cboMunicipio.getValue());
			}

//CodigoPostalSoli
			MGeneral.mlform.Presentacion.setCodigoPostal(txtCodigoPostal.getText());

//ProvinciaSoli
			String provinciaCodigo;

			if (cboProvincia.getValue() == null) {
				provinciaCodigo = "";
			} else {
				MaestroCodigoDescripcion maestro = new MaestroCodigoDescripcion("Provincias");
				provinciaCodigo = maestro.obtenerCodigoDeDescripcion(cboProvincia.getValue().getDescripcion());
			}
			MGeneral.mlform.Presentacion.setProvinciaCodigo(provinciaCodigo);
//FaxSoli
			MGeneral.mlform.Presentacion.setFax(txtFax.getText());
//Telefono Soli
			MGeneral.mlform.Presentacion.setTelefono(txtTelefono.getText());
//FechaSoli DatePicker

//setDatosRegistralesLibro
			MGeneral.mlform.Presentacion.setDatosRegistralesLibro(txtLibrosPresentados.getText());
//setDatosRegistralesTomo
			MGeneral.mlform.Presentacion.setDatosRegistralesTomo(txtDatosRegistralesTomo.getText());
//setDatosRegistralesFolio
			MGeneral.mlform.Presentacion.setDatosRegistralesFolio(txtDatosRegistralesFolio.getText());
//setDatosRegistralesHoja
			MGeneral.mlform.Presentacion.setDatosRegistralesHoja(txtDatosRegistralesHoja.getText());
//setDatosRegistralesOtros
			MGeneral.mlform.Presentacion.setDatosRegistralesOtros(txtDatosRegistralesOtros.getText());
//setTipoRegistroPublico
			MGeneral.mlform.Presentacion.setTipoRegistroPublico(txtTipoRegistroPublico.getText());

//Presentante
//NombrePres
			MGeneral.mlform.Presentacion._Presentante.set_Nombre(txtPresentanteNombre.getText());
//Apellido1 Pres
			MGeneral.mlform.Presentacion._Presentante.set_Apellido1(txtPresentanteApellido1.getText());
//Apellido2 Pres
			MGeneral.mlform.Presentacion._Presentante.set_Apellido2(txtPresentanteApellido2.getText());
//Nif Presentante			
			MGeneral.mlform.Presentacion._Presentante.set_Nif(txtPresentanteNif.getText());
//Domicilio			
			MGeneral.mlform.Presentacion._Presentante.set_Domicilio(txtPresentanteDomicilio.getText());
//Ciudad		
			if (cboPresentanteMunicipio.getValue() == null) {
				MGeneral.mlform.Presentacion._Presentante.set_Ciudad("");
			} else {
				MGeneral.mlform.Presentacion._Presentante.set_Ciudad(cboPresentanteMunicipio.getValue());
			}

//Postal			
			MGeneral.mlform.Presentacion._Presentante.set_CodigoPostal(txtPresentanteCodigoPostal.getText());

			String presentanteProvinciaCodigo;
			if (cboPresentanteProvincia.getValue() == null
					|| cboPresentanteProvincia.getValue().getDescripcion().isEmpty()) {
				presentanteProvinciaCodigo = "";
			} else {
				MaestroCodigoDescripcion maestro = new MaestroCodigoDescripcion("Provincias");
				presentanteProvinciaCodigo = maestro
						.obtenerCodigoDeDescripcion(cboPresentanteProvincia.getValue().getDescripcion());
			}
			MGeneral.mlform.Presentacion._Presentante.set_ProvinciaCodigo(presentanteProvinciaCodigo);

			MGeneral.mlform.Presentacion._Presentante.set_Fax(txtPresentanteFax.getText());
			MGeneral.mlform.Presentacion._Presentante.set_Telefono(txtPresentanteTelefono.getText());
			MGeneral.mlform.Presentacion._Presentante.set_Email(txtPresentanteEmail.getText());

			String presentanteSolicitaRetencion;
			if (cboPresentanteSolicitaRetencion.getValue() == null
					|| cboPresentanteSolicitaRetencion.getValue().getDescripcion().isEmpty()) {
				presentanteSolicitaRetencion = "";
			} else {
				MaestroCodigoDescripcion maestro = new MaestroCodigoDescripcion("SolicitaRetencion");
				presentanteSolicitaRetencion = maestro
						.obtenerCodigoDeDescripcion(cboPresentanteSolicitaRetencion.getValue().getDescripcion());
			}
			MGeneral.mlform.Presentacion._Presentante.set_SolicitaRetencion(presentanteSolicitaRetencion);

			if (txtFechaSolicitud.getValue() == null) {
				MGeneral.mlform.Presentacion.setFechaSolicitud("");

			} else {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
				LocalDate fecha = txtFechaSolicitud.getValue();
				String formattedDate = fecha.format(formatter);

				MGeneral.mlform.Presentacion.setFechaSolicitud(formattedDate);

			}

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
			String filtro = cboProvincia.getEditor().getText().toUpperCase();

			if (cboProvincia.validarSeleccion()) {
				cboMunicipio.setValue(null);
				MaestroCodigoDescripcion aux = new MaestroCodigoDescripcion("Provincias");
				String codigo = aux.obtenerCodigoDeDescripcion(filtro);

				List<String> Lista = MGeneral.Idioma.obtenerMunicipiosDeProvincia(codigo);

				cboMunicipio.cargarCombo(Lista);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void recargaMuniPres(ActionEvent event) {
		try {

			String filtro = cboPresentanteProvincia.getEditor().getText().toUpperCase();

			if (cboPresentanteProvincia.validarSeleccion()) {
				cboPresentanteMunicipio.setValue(null);
				MaestroCodigoDescripcion aux = new MaestroCodigoDescripcion("Provincias");
				String codigo = aux.obtenerCodigoDeDescripcion(filtro);

				List<String> Lista = MGeneral.Idioma.obtenerMunicipiosDeProvincia(codigo);

				cboPresentanteMunicipio.cargarCombo(Lista);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void operacion(EnumTipoOperacion tipoOperacion) {
		eliminarListener();

		try {
			if (!guardar(true, true)) {
				aniadirListener();
				return;
			}
			Utilidades.cursorEspera(getParentController().getStagePrincipal(), true);
			MGeneral.mlform.iniciarProgressBar(parentController.StatusProgressBar);

			// Para el modo Recepción, solo se puede imprimir
			if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Recepcion) {
				verificarRegistroYProvincia(true);
				if (tipoOperacion == EnumTipoOperacion.Imprimir) {
					// ReportePreviewService reportDiagnostico = new ReportePreviewService();
					// reportDiagnostico.cargarReporteDiagnostico();
				}
				aniadirListener();
				return;
			}

			if (!validarTodosLosControles(frmEntradaDatos, false, false)) {
				aniadirListener();
				return;
			}

			kLegalizacion.enumResultadoValidacion resul;
			resul = MGeneral.mlform.valida();

			if (resul == kLegalizacion.enumResultadoValidacion.NoValida) {
				validar(false);
				aniadirListener();
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
					aniadirListener();
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

				//Utilidades.cursorEsperaJavaFX(getParentController().getStagePrincipal(), true, Duration.INDEFINITE);

				resulZip = MGeneral.mlform.generarZip("");

				if (resulZip == LegalizacionService.EnumResultadoZip.Correcto) {
					if (!MGeneral.mlform.generaInstancia()) {
						aniadirListener();
						return;
					}

				}
				//Utilidades.cursorEsperaJavaFX(getParentController().getStagePrincipal(), false, Duration.ZERO);

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
					aniadirListener();
				}

				activacionIconosBarra(EnumActivacionIconos.DesactivarTodo);

				Ficheros.FicheroBorra(MGeneral.mlform.getPathFicheroInstancia());

				break;
			case Enviar:
				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloLectura)
					return; // No se puede enviar en modo solo lectura

				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloReenviar) {
					boolean resenvio;
					ceDoc veDoc = new ceDoc();

					resenvio = veDoc.enviarPorServicio(true);

					abrir(MGeneral.mlform.getPathDatos()); // Se vuelve a abrir para que se cargue como corresponda

					if (resenvio) {
						if (MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.EnvioServicioCorrecto, "", "",
								"") == true) {
							if (!new File(MGeneral.mlform.getPathFicheroAcuseEntrada()).exists()) {
								MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FicheroInexistente,
										MGeneral.mlform.getPathFicheroAcuseEntrada(), "", "");
								return;
							}
							try {
								Utilidades.ProcessStartFichero(MGeneral.mlform.getPathFicheroAcuseEntrada());

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

					return;
				}

				if (MGeneral.mlform.Datos.getEstadoEnvio() == cDatos.eEstadoEnvio.EnviadaPorServicioCorrectamente) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.EnvioServicioYaRealizado, "", "", "");
					return;
				}

				File ficheroZipEnvio = new File(MGeneral.mlform.getPathFicheroZip());
				if (!ficheroZipEnvio.isDirectory()) {
					if (ficheroZipEnvio.exists()) {

						IdiomaC.MostrarMensaje(EnumMensajes.ZIPYaExiste, "", "", "");

						if (!IdiomaC.MostrarMensaje(EnumMensajes.FicheroZipYaGenerado, "", "", "")) {

						}

					}
				}

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PrevioEnvio.fxml"));
				Parent previoEnvio = loader.load();

				PrevioEnvioController previoEnvioController = loader.getController();
				Stage stage = new Stage();
				Scene scene = new Scene(previoEnvio);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.initStyle(StageStyle.UTILITY);
				stage.getIcons().clear();
				stage.setScene(scene);
				MGeneral.Idioma.cargarIdiomaControles(stage, null);
				stage.toFront();
				stage.showAndWait();

				if (!MGeneral.RetornoFormulario)
					return;

				if (MGeneral.RetornoFormaDeEnvioDirecto) {
					ceDoc veDoc = new ceDoc();
					if (!veDoc.compruebaTodosLosNodos())
						return;
				}
				Utilidades.cursorEspera(getParentController().getStagePrincipal(), true);

				activacionIconosBarra(EnumActivacionIconos.DesactivarTodo);

				// MGeneral.mlform.getProgreso().inicializa(10, 80, 10);

				Ficheros.FicheroBorra(MGeneral.mlform.getPathFicheroInstancia());

				resulZip = MGeneral.mlform.generarZip("");

				if (resulZip == LegalizacionService.EnumResultadoZip.Correcto) {
					if (!MGeneral.mlform.generaInstancia())
						return; // Después de GenerarZip para que las huellas estén generadas
				}

				Utilidades.cursorEspera(getParentController().getStagePrincipal(), false);

				// MGeneral.mlform.getProgreso().finaliza();
				if (MGeneral.RetornoFormaDeEnvioDirecto) {
					boolean resenvio;
					ceDoc veDoc = new ceDoc();

					resenvio = veDoc.enviarPorServicio(false);

					abrir(MGeneral.mlform.getPathDatos()); // Se vuelve a abrir para que se cargue como corresponda

					if (resenvio) {
						if (MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.EnvioServicioCorrecto, "", "",
								"") == true) {
							if (!new File(MGeneral.mlform.getPathFicheroAcuseEntrada()).exists()) {
								MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FicheroInexistente,
										MGeneral.mlform.getPathFicheroAcuseEntrada(), "", "");
								return;
							}
							try {
								Utilidades.ProcessStartFichero(MGeneral.mlform.getPathFicheroAcuseEntrada());

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					switch (resulZip) {
					case Correcto:
						MGeneral.mlform.enviarZipPorPortal();
						break;
					default:
						break;
					}
				}
				break;
			case Imprimir:

				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Normal) {

					Utilidades.cursorEspera(getParentController().getStagePrincipal(), true);

					activacionIconosBarra(EnumActivacionIconos.DesactivarTodo);
					// Application.DoEvents(); // No hay equivalente directo en Java
					// MGeneral.mlform.getVProgreso().Inicializa(50, 50);

					Ficheros.FicheroBorra(MGeneral.mlform.getPathFicheroInstancia());
					if (MGeneral.mlform.generarHuellas()) {
						if (MGeneral.mlform.generaInstancia()) {

							HostServices hostServices = Main.getHostService();

							hostServices.showDocument(MGeneral.mlform.getPathFicheroInstancia());
							aniadirListener();
							// MGeneral.mlform.getVProgreso().Finaliza();

						}
					}
					Utilidades.cursorEspera(getParentController().getStagePrincipal(), false);

					// mlform.getVProgreso().Finaliza();
				}
				// Se abre el Pdf de la instancia
				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloLectura
						|| MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloReenviar) {
					File file = new File(MGeneral.mlform.getPathFicheroInstancia());
					if (file.exists()) {
						HostServices hostServices = Main.getHostService();

						hostServices.showDocument(MGeneral.mlform.getPathFicheroInstancia());
						aniadirListener();
					} else {
						IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.FicheroInexistente, "", "",
								MGeneral.mlform.getPathFicheroInstancia());
						aniadirListener();
					}
				}
				break;

			// Agregar los otros casos de operación aquí...
			case GenerarHuellas:
				
				boolean resulBool = false;

				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Normal) {
					Utilidades.cursorEspera(getParentController().getStagePrincipal(), true);

					// ActivacionIconosBarra(EnumActivacionIconos.DesactivarTodo);
					// mlform.vProgreso.Inicializa(100);
					resulBool = MGeneral.mlform.generarHuellas();

					Utilidades.cursorEspera(getParentController().getStagePrincipal(), false);

					// mlform.vProgreso.Finaliza();
				}

				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloLectura) {
					resulBool = true;
				}

				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloReenviar) {
					resulBool = true;
				}

				if (resulBool) {

					FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/fxml/VerHuellas.fxml"));
					Parent verHuellas = loader1.load();

					VerHuellasController verHuellasController = loader1.getController();
					stage = new Stage();
					scene = new Scene(verHuellas);
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.initStyle(StageStyle.UTILITY);
					stage.getIcons().clear();
					stage.setScene(scene);
					MGeneral.Idioma.cargarIdiomaControles(stage, null);
					stage.showAndWait();
					aniadirListener();
				}

				break;
			case EncriptarTodo:
				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloLectura)
					return;
				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloReenviar)
					return;

				resulBool = false;
				String cadNombres = "";

				if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Normal) {

					/*
					 * Frm_Encriptacion f = new
					 * Frm_Encriptacion(Frm_Encriptacion.eModoApertura.EncriptarTodosLosLibros, "",
					 * ""); f.WindowState = FormWindowState.Normal; f.StartPosition =
					 * FormStartPosition.CenterScreen; f.showAndWait();
					 */

					if (!MGeneral.RetornoFormulario)
						return;

					Utilidades.cursorEspera(getParentController().getStagePrincipal(), true);

					activacionIconosBarra(EnumActivacionIconos.DesactivarTodo);

					// MGeneral.mlform.getProgreso().inicializa(100);

					// resulBool = MGeneral.mlform.encriptarTodosLosLibros(cadNombres);

					Utilidades.cursorEspera(getParentController().getStagePrincipal(), false);

					// MGeneral.mlform.getProgreso().finaliza();

					if (resulBool) {
						if (!MGeneral.Configuracion.isMostrarMensajeAlEncriptarEnLegalizacion())
							return;

						String cad = MGeneral.Encriptacion.obtenerResultadoEncriptacion();

						/*
						 * Frm_EncriptacionResultado fr = new
						 * Frm_EncriptacionResultado(Frm_EncriptacionResultado.eModoApertura.
						 * EncriptadosTodosLibros, cad, cadNombres); fr.WindowState =
						 * FormWindowState.Normal; fr.StartPosition = FormStartPosition.CenterScreen;
						 * fr.showAndWait();
						 */
					}
				}
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

				ComprobarReglasController comprobarReglasController = loader.getController();
				comprobarReglasController.setParentController(getParentController());

				Stage stage = new Stage();
				Scene scene = new Scene(comprobarReglas);

				// quitando el maximizar y minimizar
				stage.initModality(Modality.APPLICATION_MODAL);
				// bloquea la interacción con otras ventanas de la aplicación
				stage.initStyle(StageStyle.UTILITY);
				// quitando iconos
				stage.getIcons().clear();
				stage.setScene(scene);
				MGeneral.Idioma.cargarIdiomaControles(stage, null);
				stage.showAndWait();
				aniadirListener();
				irACampoFoco();
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

				if (lblCif.isVisible()) {
					sufijo = "Cif";
				} else if (lblNif.isVisible()) {
					sufijo = "Nif";
				}
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

			case "cboTipoPersona":

				if (!txtCifNif.getText().isEmpty()) {
					if (esPersonaJuridica()) {
						if (!Formato.validaCif(txtCifNif.getText())) {
							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.FormatoCifIncorrecto, null, null, null)
											.toString(),
									iconPaneAlertCIFNIFSoli, iconAlertCIFSoli, true, control);

						} else {
							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.FormatoCifIncorrecto, null, null, null)
											.toString(),
									iconPaneAlertCIFNIFSoli, iconAlertCIFNIFSoli, false, control);

						}
					} else if (esPersonaFisica()) {
						if (!Formato.validaNif(txtCifNif.getText())) {
							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.FormatoNifIncorrecto, null, null, null)
											.toString(),
									iconPaneAlertCIFNIFSoli, iconAlertCIFNIFSoli, true, control);

						} else {
							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.FormatoNifIncorrecto, null, null, null)
											.toString(),
									iconPaneAlertCIFNIFSoli, iconAlertCIFNIFSoli, false, control);
						}
					}
				}

				break;

			case "txtCifNif":

				if (!((TextField) control).getText().isEmpty()) {
					ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
							iconPaneAlertCIFNIFSoli, iconAlertCIFNIFSoli, false, control);
					if (esPersonaJuridica()) {
						if (!Formato.validaCif(txtCifNif.getText())) {
							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.FormatoCifIncorrecto, null, null, null)
											.toString(),
									iconPaneAlertCIFNIFSoli, iconAlertCIFNIFSoli, true, control);

						} else {
							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.FormatoCifIncorrecto, null, null, null)
											.toString(),
									iconPaneAlertCIFNIFSoli, iconAlertCIFNIFSoli, false, control);

						}
					} else if (esPersonaFisica()) {
						if (!Formato.validaNif(txtCifNif.getText())) {
							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.FormatoNifIncorrecto, null, null, null)
											.toString(),
									iconPaneAlertCIFNIFSoli, iconAlertCIFNIFSoli, true, control);

						} else {
							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.FormatoNifIncorrecto, null, null, null)
											.toString(),
									iconPaneAlertCIFNIFSoli, iconAlertCIFNIFSoli, false, control);

						}
					}
				} else {
					ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
							iconPaneAlertCIFNIFSoli, iconAlertCIFNIFSoli, true, control);

				}
				break;
			case "txtPresentanteNif":

				if (!((TextField) control).getText().isEmpty()) {

					if (!Formato.validaNif(((TextField) control).getText())) {
						ErrorProvider(IdiomaC
								.obtenerMensaje(IdiomaC.EnumMensajes.FormatoNifIncorrecto, null, null, null).toString(),
								iconPaneNIFPres, iconAlertNIFPres, true, control);

					} else {

						ErrorProvider(IdiomaC
								.obtenerMensaje(IdiomaC.EnumMensajes.FormatoNifIncorrecto, null, null, null).toString(),
								iconPaneNIFPres, iconAlertNIFPres, false, control);
					}
				} else {
					ErrorProvider(IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.FormatoNifIncorrecto, null, null, null)
							.toString(), iconPaneNIFPres, iconAlertNIFPres, false, control);
				}
				break;

			case "txtPresentanteCodigoPostal":

				if (!((TextField) control).getText().isEmpty()) {

					if ((((TextField) control).getText().length()) != 5
							|| (((TextField) control).getText().length()) == 0) {

						ErrorProvider(
								MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoTiene5Digitos, null,
										null, null).toString(),
								iconPaneAlertPostalPres, iconAlertPostalPres, true, control);

					} else {

						ErrorProvider(
								MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoTiene5Digitos, null,
										null, null).toString(),
								iconPaneAlertPostalPres, iconAlertPostalPres, false, control);

						if ((Formato.verificaProvinciaYCodigoPostal(txtPresentanteCodigoPostal,
								cboPresentanteProvincia)) == false) {
							ErrorProvider(MGeneral.Idioma.obtenerMensaje(
									IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia, null, null, null)
									.toString(), iconPaneAlertPostalPres, iconAlertPostalPres, true, control);

						} else {
							ErrorProvider(MGeneral.Idioma.obtenerMensaje(
									IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia, null, null, null)
									.toString(), iconPaneAlertPostalPres, iconAlertPostalPres, false, control);

						}
					}
				} else {
					ErrorProvider(
							MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia,
									null, null, null).toString(),
							iconPaneAlertPostalPres, iconAlertPostalPres, false, control);
				}
				break;

			case "cboxProvinciaPres":

				if (txtPresentanteCodigoPostal.getText().isEmpty()) {

					if (txtPresentanteCodigoPostal.getText().length() != 5) {

						ErrorProvider(
								MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoTiene5Digitos, null,
										null, null).toString(),
								iconPaneAlertPostalPres, iconAlertPostalPres, true, control);

					} else {

						ErrorProvider(
								MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoTiene5Digitos, null,
										null, null).toString(),
								iconPaneAlertPostalPres, iconAlertPostalPres, false, control);

						if ((Formato.verificaProvinciaYCodigoPostal(txtPresentanteCodigoPostal,
								cboPresentanteProvincia)) == false) {
							ErrorProvider(MGeneral.Idioma.obtenerMensaje(
									IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia, null, null, null)
									.toString(), iconPaneAlertPostalPres, iconAlertPostalPres, true, control);

						} else {
							ErrorProvider(MGeneral.Idioma.obtenerMensaje(
									IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia, null, null, null)
									.toString(), iconPaneAlertPostalPres, iconAlertPostalPres, false, control);

						}
					}
				}
				break;

			case "txtPresentanteEmail":

				if (!Formato.ValorNulo(txtPresentanteEmail.getText())) {

					if (!Formato.validaEmail(txtPresentanteEmail.getText())) {

						ErrorProvider(
								MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.FormatoEmailIncorrecto).toString(),
								iconPanErrorEmailPres, iconErrorEmailPres, true, control);

					} else {
						ErrorProvider(
								MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.FormatoEmailIncorrecto).toString(),
								iconPanErrorEmailPres, iconErrorEmailPres, false, control);
					}
				} else {
					ErrorProvider(
							MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.FormatoEmailIncorrecto).toString(),
							iconPanErrorEmailPres, iconErrorEmailPres, false, control);

				}
				break;

			case "txtDenominacion":

				if (esPersonaJuridica()) {

					if (!((TextField) control).getText().isEmpty()) {
						ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
								iconPaneApellidoDenominacionJuridicoSoli, iconDenominacionJuridicoApellidoSoli, false,
								control);
					} else {

						ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
								iconPaneApellidoDenominacionJuridicoSoli, iconDenominacionJuridicoApellidoSoli, true,
								control);
						validarPrimerControlNoValido = false;
					}
				}
				break;
			case "txtDomicilio":

				if (!((TextField) control).getText().isEmpty()) {
					ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
							iconPaneDomicilioSoli, iconDomicilioSoli, false, control);
				} else {
					ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
							iconPaneDomicilioSoli, iconDomicilioSoli, true, control);
					validarPrimerControlNoValido = false;
				}
				break;

			case "txtNombre":

				if (esPersonaFisica()) {

					if (!((TextField) control).getText().isEmpty()) {
						ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
								iconPaneNombreSoli, iconNombreSoli, false, control);
					} else {
						ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
								iconPaneNombreSoli, iconNombreSoli, true, control);
						validarPrimerControlNoValido = false;
					}
				}
				break;

			case "cboxMunicipioPres":

				if (!Formato.ValorNulo(cboPresentanteProvincia) && !Formato.ValorNulo(control)) {

					if (!new IdiomaC("es").existeMunicipioDeProvincia(cboPresentanteProvincia.getValue().getCodigo(),
							cboPresentanteMunicipio.getValue())) {

					}

				}
				break;
			case "txtApellido1":
				if (esPersonaFisica()) {
					if (!((TextField) control).getText().isEmpty()) {
						ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
								iconPaneApellidoSoli, iconApellidoSoli, false, control);
					} else {
						ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
								iconPaneApellidoSoli, iconApellidoSoli, true, control);
						validarPrimerControlNoValido = false;
					}
				}
				break;
			case "txtApellido2":
				if (esPersonaFisica()) {
					if (!((TextField) control).getText().isEmpty()) {
						ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
								iconPaneApellidoDenominacionJuridicoSoli, iconDenominacionJuridicoApellidoSoli, false,
								control);
					} else {
						ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
								iconPaneApellidoDenominacionJuridicoSoli, iconDenominacionJuridicoApellidoSoli, true,
								control);

					}
				}
				break;

			case "txtCodigoPostal":

				if ((((TextField) control).getText().length()) != 5
						|| (((TextField) control).getText().length()) == 0) {

					ErrorProvider(MGeneral.Idioma
							.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoTiene5Digitos, null, null, null)
							.toString(), iconPaneAlertCodPostal, iconAlertCodPostal, true, control);
					validarPrimerControlNoValido = false;
				} else {

					ErrorProvider(MGeneral.Idioma
							.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoTiene5Digitos, null, null, null)
							.toString(), iconPaneAlertCodPostal, iconAlertCodPostal, false, control);

					if ((Formato.verificaProvinciaYCodigoPostal(txtCodigoPostal, cboProvincia)) == false) {
						ErrorProvider(
								MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia,
										null, null, null).toString(),
								iconPaneAlertCodPostal, iconAlertCodPostal, true, control);

					} else {
						ErrorProvider(
								MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia,
										null, null, null).toString(),
								iconPaneAlertCodPostal, iconAlertCodPostal, false, control);

					}

				}
				break;

			case "txtFechaSolicitud":

				if (txtFechaSolicitud.getValue() == null) {
					ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
							iconPaneFechaSoli, iconFechaSoli, true, control);
					validarPrimerControlNoValido = false;
				} else if (!Formato.ValorNulo(control)) {

					if (!LegalizacionService.comprobarFecha(txtFechaSolicitud.getValue().toString(), null)) {

						ErrorProvider(MGeneral.Idioma
								.obtenerMensaje(IdiomaC.EnumMensajes.FechaNoValida, null, null, null).toString(),
								iconPaneFechaSoli, iconFechaSoli, false, control);

					}
				}
				break;

			case "cboxRegistroMercantil":

				if (!cboProvincia.getEditor().getText().isEmpty()) {

					verificarRegistroYProvincia(validarObligatoriedad);

				}

				if (cboxRegistroMercantil.getEditor().getText().isEmpty()) {
					ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
							iconPaneRegistroMercantil, iconRegistroMercantil, true, control);
					validarPrimerControlNoValido = false;

				}

				break;

			case "cboMunicipio":

				if (cboMunicipio.getEditor().getText().isEmpty()) {
					ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
							iconPaneMunicipioSoli, iconMunicipioSoli, true, control);
					validarPrimerControlNoValido = false;
				}

				break;

			case "cboProvincia":

				if (!cboProvincia.getEditor().getText().isEmpty()) {

					if (!cboxRegistroMercantil.getEditor().getText().isEmpty()) {

						String xReg = cboxRegistroMercantil.getSelectionModel().getSelectedItem().getCodigo()
								.toString();
						String xPro = cboProvincia.getSelectionModel().getSelectedItem().getCodigo().toString();
						String xCodPro;

						if (xReg.length() == 4) {
							xCodPro = "0" + xReg.substring(0, 1);
						} else {
							xCodPro = xReg.substring(0, 2);
						}
					
						if (!xPro.equals(xCodPro)) {
							ErrorProvider(
									MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.RegistroProvinciaDiferentes,
											null, null, null).toString(),
									iconPaneProvincia, iconProvincia, true, control);
						

						} else {
							ErrorProvider(
									MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.RegistroProvinciaDiferentes,
											null, null, null).toString(),
									iconPaneProvincia, iconProvincia, false, control);
						}
					}
				} else {
					ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(),
							iconPaneProvincia, iconProvinciaError, true, control);
					validarPrimerControlNoValido = false;
				}
				break;
			default:
				break;
			}

			return validarPrimerControlNoValido;
		} catch (Exception ex) {

			ex.printStackTrace();
			return false;
		}

	}

	public String procesarFecha(CustomDatePicker dtp) {
		LocalDate fechaSeleccionada = dtp.getValue();

		if (fechaSeleccionada != null) {
			String fechaFormateada = fechaSeleccionada.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
			return fechaFormateada;
		} else {
			return "error";
		}
	}

	public boolean esPersonaJuridica() {
		try {
			String tipoPersona = "";

			if (cboTipoPersona.getValue() != null) {
				tipoPersona = cboTipoPersona.getValue().getCodigo();

				if (tipoPersona.equals(kLegalizacion.kTipoPersonaJuridica)) {

					return true;
				}
			}

			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean esPersonaFisica() {
		try {
			String tipoPersona = "";

			if (cboTipoPersona.getValue() != null) {
				tipoPersona = cboTipoPersona.getValue().getCodigo();
				if (tipoPersona.equals(kLegalizacion.kTipoPersonaFisica)) {
					return true;
				}
			}

			return false;
		} catch (Exception ex) {
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

	private void formatoTextfieldDatosPersonaSoli() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 35) {
				return change;
			}
			return null;
		};

		txtNombre.setTextFormatter(new TextFormatter<>(filter));
		txtApellido1.setTextFormatter(new TextFormatter<>(filter));
		txtApellido2.setTextFormatter(new TextFormatter<>(filter));
		txtDomicilio.setTextFormatter(new TextFormatter<>(filter));
	}

	private void formatoTextfieldDatosPersonaPres() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 35) {
				return change;
			}
			return null;
		};

		txtPresentanteNombre.setTextFormatter(new TextFormatter<>(filter));
		txtPresentanteApellido1.setTextFormatter(new TextFormatter<>(filter));
		txtPresentanteApellido2.setTextFormatter(new TextFormatter<>(filter));
		txtPresentanteDomicilio.setTextFormatter(new TextFormatter<>(filter));
	}

	private void formatoTextfieldPostalPres() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 5) {
				return change;
			}
			return null;
		};

		txtPresentanteCodigoPostal.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldPostalSoli() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 5) {
				return change;
			}
			return null;
		};

		txtCodigoPostal.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldTOMO() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 6) {
				return change;
			}
			return null;
		};

		txtDatosRegistralesTomo.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldFOLIO() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 6) {
				return change;
			}
			return null;
		};

		txtDatosRegistralesFolio.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldHOJA() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 10) {
				return change;
			}
			return null;
		};

		txtDatosRegistralesHoja.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldOTROS() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 32) {
				return change;
			}
			return null;
		};

		txtDatosRegistralesOtros.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldTIPOREGISTRO() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 32) {
				return change;
			}
			return null;
		};

		txtTipoRegistroPublico.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldEmailPres() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 60) {
				return change;
			}
			return null;
		};

		txtPresentanteEmail.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldNIFFaxTelefonoPres() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 10) {
				return change;
			}
			return null;
		};

		txtPresentanteFax.setTextFormatter(new TextFormatter<>(filter));
		txtPresentanteNif.setTextFormatter(new TextFormatter<>(filter));
		txtPresentanteTelefono.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldNIFFaxTelefonoSoli() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 10) {
				return change;
			}
			return null;
		};

		txtFax.setTextFormatter(new TextFormatter<>(filter));
		txtCifNif.setTextFormatter(new TextFormatter<>(filter));
		txtTelefono.setTextFormatter(new TextFormatter<>(filter));

	}

	public boolean verificarRegistroYProvincia(boolean mostrarMensaje) {

		if (cboxRegistroMercantil.getEditor().getText().isEmpty() || cboProvincia.getEditor().getText().isEmpty()) {
			return false;
		}

		if (MGeneral.mlform == null || MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Recepcion
				|| MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloLectura
				|| MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloReenviar) {
			return false;
		}

		if (Formato.ValorNulo(cboxRegistroMercantil.getValue())) {
			return false;
		}

		if (Formato.ValorNulo(cboProvincia.getValue())) {
			return false;
		}

		if (cboxRegistroMercantil.getSelectionModel().getSelectedItem() == null
				|| cboProvincia.getSelectionModel().getSelectedItem() == null) {
			return false;
		}

		String xReg = cboxRegistroMercantil.getSelectionModel().getSelectedItem().getCodigo().toString();
		String xPro = cboProvincia.getSelectionModel().getSelectedItem().getCodigo().toString();
		String xCodPro;

		if (xReg.length() == 4) {
			xCodPro = "0" + xReg.substring(0, 1);
		} else {
			xCodPro = xReg.substring(0, 2);
		}

		if (!xPro.equals(xCodPro)) {
			if (mostrarMensaje) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.RegistroProvinciaDiferentes, "", "", "");
			}
			return false;
		}

		return true;
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
			cargaLibros();

		} catch (Exception ex) {
			// Mostrar mensaje de error
			// MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion,
			// ex.getMessage(), "", "");
		}
	}

	public void cargaLibros() {
		try {
			eliminarListener();
			if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Normal) {
				if (!guardar(true, true))
					return;

				if (!MGeneral.mlform.isValidaCargaDeLibros()) {
					// Si la carga de libros no se puede realizar, se muestra el formulario de
					// validación
					validar(true);
				} else {
					// Mostrar formulario para especificar libros

					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EspecificarLibros.fxml"));
					Parent root = fxmlLoader.load();

					EspecificarLibrosController especificarLibrosController = fxmlLoader.getController();
					especificarLibrosController.setParentController(getParentController());

					Stage stage = new Stage();

					Scene scene = new Scene(root);
					// scene.getStylesheets().add(getClass().getResource("/estilos/Grande.css").toExternalForm());

					// quitando el maximizar y minimizar
					stage.initStyle(StageStyle.UTILITY);
					// quitando iconos
					stage.getIcons().clear();
					// bloquea la interacción con otras ventanas de la aplicación
					stage.initModality(Modality.APPLICATION_MODAL);

					stage.setScene(scene);
					MGeneral.Idioma.cargarIdiomaControles(stage, null);

					stage.showAndWait();
					aniadirListener();

				}
			}

			if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Recepcion) {
				// Mostrar formulario para especificar huellas

				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EspecificarHuellas.fxml"));
				Parent root = fxmlLoader.load();

				EspecificarHuellasController especificarhuellasController = fxmlLoader.getController();
				especificarhuellasController.setParentController(getParentController());

				Stage stage = new Stage();

				Scene scene = new Scene(root);
				// scene.getStylesheets().add(getClass().getResource("/estilos/Grande.css").toExternalForm());

				// quitando el maximizar y minimizar
				stage.initStyle(StageStyle.UTILITY);
				// quitando iconos
				stage.getIcons().clear();
				// bloquea la interacción con otras ventanas de la aplicación
				stage.initModality(Modality.APPLICATION_MODAL);

				stage.setScene(scene);
				MGeneral.Idioma.cargarIdiomaControles(stage, null);

				stage.showAndWait();

			}

			if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloLectura
					|| MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloReenviar) {
				if (!MGeneral.mlform.isValidaCargaDeLibros()) {
					// Si la carga de libros no se puede realizar, se muestra el formulario de
					// validación
					validar(true);
				} else {

					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EspecificarLibros.fxml"));
					Parent root = fxmlLoader.load();

					EspecificarLibrosController especificarLibrosController = fxmlLoader.getController();
					especificarLibrosController.setParentController(getParentController());

					Stage stage = new Stage();

					Scene scene = new Scene(root);

					// quitando el maximizar y minimizar
					stage.initStyle(StageStyle.UTILITY);
					// quitando iconos
					stage.getIcons().clear();
					stage.initModality(Modality.APPLICATION_MODAL);

					stage.setScene(scene);
					MGeneral.Idioma.cargarIdiomaControles(stage, null);

					stage.showAndWait();

				}
			}
		} catch (Exception ex) {

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

			ex.printStackTrace();
		}
	}

	public void irACampoFoco() {
		try {
			Control controlAux;

			if (campoPonerFoco == null || campoPonerFoco.isEmpty())
				return;

			if (campoPonerFoco.equals("LIBROS")) {
				cargaLibros();
			} else {

				switch (campoPonerFoco) {
				case "txtNombreODenominacion":

					if (lblNombre.isVisible()) {
						campoPonerFoco = "txtNombre";
					} else if (lblDenominacion.isVisible()) {
						campoPonerFoco = "txtDenominacion";
					}
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

	public void aniadirListener() {
		cboxRegistroMercantil.focusedProperty().addListener(focusedListenerRegistroProvincia);
	}

	public void eliminarListener() {
		cboxRegistroMercantil.focusedProperty().removeListener(focusedListenerRegistroProvincia);
	}

	@Override
	public void initializeControlsInOrderToNavigate() {
		controlsInOrderToNavigate = new Control[] { cboxRegistroMercantil, cboTipoPersona, txtDenominacion, txtNombre,
				txtApellido1, txtApellido2, txtCifNif, txtDomicilio, txtCodigoPostal, cboProvincia, cboMunicipio,
				txtTelefono, txtFax, txtFechaSolicitud, txtDatosRegistralesTomo, txtDatosRegistralesFolio,
				txtDatosRegistralesHoja, txtDatosRegistralesOtros, txtTipoRegistroPublico, txtPresentanteNombre,
				txtPresentanteApellido1, txtPresentanteApellido2, txtPresentanteNif, txtPresentanteDomicilio,
				txtPresentanteCodigoPostal, cboPresentanteProvincia, cboPresentanteMunicipio, txtPresentanteTelefono,
				txtPresentanteFax, txtPresentanteEmail, cboPresentanteSolicitaRetencion }; // TODO Auto-generated method
																							// stub

	}

	@Override
	public Control[] getControlsInOrderToNavigate() {
		// TODO Auto-generated method stub
		return controlsInOrderToNavigate;
	}

}
