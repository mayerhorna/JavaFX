package com.commerceapp.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.commerceapp.controller.helpers.NavigableControllerHelper;
import com.commerceapp.domain.ConfiguracionC;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.gui.custom.combobox.CustomCombobox;
import com.commerceapp.gui.custom.imageview.CustomImageView;
import com.commerceapp.maestros.MaestroCodigoDescripcion;
import com.commerceapp.maestros.MaestroCodigoDescripcionConverter;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;
import com.commerceapp.util.Utilidades.TimeLineClass;

import ch.qos.logback.core.boolex.Matcher;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;

import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;

public class ConfiguracionController implements Initializable, NavigableControllerHelper {
	private static final Logger logger = Logger.getLogger(ConfiguracionController.class.getName());

	boolean necesitareiniciar = false;
	Utilidades objTimeline = new Utilidades();
	MenuPrincipalController parentController;

	public void setParentController(MenuPrincipalController parentController) {
		this.parentController = parentController;
	}

	public MenuPrincipalController getParentController() {
		return parentController;
	}

	@FXML
	private CustomCombobox<MaestroCodigoDescripcion> myComboBox;
	@FXML
	private AnchorPane frmConfiguracion;

	@FXML
	private Button btnSeleccionDirectorio;

	@FXML
	private CheckBox CheckboxEncriptarFicheros;

	@FXML
	private Button btnGuardar;

	@FXML
	private CustomCombobox<String> cboxMunicipio;

	@FXML
	private CustomCombobox<MaestroCodigoDescripcion> cboPresentanteProvincia;

	@FXML
	private CustomCombobox<MaestroCodigoDescripcion> cboxProvinciaDefecto;

	@FXML
	private CustomCombobox<MaestroCodigoDescripcion> cboxRegistroMercantil;

	@FXML
	private CustomCombobox<MaestroCodigoDescripcion> cboxRetencion;
	@FXML
	private TextField txtPresentanteApellido1;

	@FXML
	private TextField txtPresentanteApellido2;

	@FXML
	private TextField txtPresentanteNif;

	@FXML
	private TextField txtPresentanteCodigoPostal;

	@FXML
	private TextField txtDirectorioLegislacion;

	@FXML
	private TextField txtDomicilio;

	@FXML
	private TextField txtPresentanteEmail;

	@FXML
	private TextField txtFactorAjusteH;

	@FXML
	private TextField txtFactorAjusteV;

	@FXML
	private TextField txtFax;

	@FXML
	private TextField txtPresentanteNombre;

	@FXML
	private TextField txtTelefono;
	@FXML
	private CustomImageView imgVApell1;

	@FXML
	private CustomImageView imgVNombre;

	@FXML
	private CustomImageView idErrorProviderCustomImage;

	@FXML
	private CustomImageView idErrorProviderNIF;

	@FXML
	private CustomImageView idErrorProviderEmail;

	@FXML
	private Label idlabelfactorpantallahorizontal;

	@FXML
	private Label idlabelfactorpantallavertical;

	@FXML
	private Pane iconPanelHoverEmail;

	@FXML
	private Pane iconPaneCodigoPostal;

	@FXML
	private Pane iconPaneNif;

	@FXML
	private GridPane GridPanePresentante;

	private boolean pendienteGuardar = false;

	private boolean primeraVez = false;

	private Control[] controlsInOrderToNavigate;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeControlsInOrderToNavigate();
		registerKeyPressENTERInControlsToNavigate();
		validarTextChangesGuardar();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cboxRegistroMercantil.requestFocus();
				pendienteGuardar = false;
				primeraVez = true;
				frmConfiguracion.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
					if (event.getCode() == KeyCode.TAB) {
						event.consume(); // Consumir el evento de teclado para evitar la navegación por TAB en todo el
											// form
					}
				});
				Stage stage = (Stage) frmConfiguracion.getScene().getWindow();
				stage.setOnCloseRequest(event -> {
					event.consume(); // Consume the event to prevent automatic closing
					if (pendienteGuardar) {

						cboxRegistroMercantil.eliminarListener();
						Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
						alert.setHeaderText(null);
						alert.setTitle("Confirmación");
						alert.setContentText("¿Estas seguro de confirmar la acción?");
						Optional<ButtonType> action = alert.showAndWait();

						if (action.get() == ButtonType.OK) {
							getDatosVista();
							MGeneral.Configuracion.GuardaConfiguracion();
							stage.close();

						} else if (action.get() == ButtonType.CANCEL) {
							stage.close();
						}

					} else {
						stage.close();
					}
					cboxRegistroMercantil.aniadirListener();
				});

			}
		});

		setPendienteGuardar(false);

		GridPane.setColumnSpan(txtPresentanteEmail, 4);
		GridPane.setColumnSpan(txtDomicilio, 4);
		GridPane.setColumnSpan(cboxMunicipio, 4);

		cboxRegistroMercantil.cargarCombo("Registros");
		cboPresentanteProvincia.cargarCombo("Provincias");
		cboxProvinciaDefecto.cargarCombo("Provincias");
		cboxRetencion.cargarCombo("SolicitaRetencion");

		idErrorProviderCustomImage.setImage(new Image(getClass().getResourceAsStream("/imagenes/aviso.png")));
		idErrorProviderEmail.setImage(new Image(getClass().getResourceAsStream("/imagenes/error.png")));
		idErrorProviderNIF.setImage(new Image(getClass().getResourceAsStream("/imagenes/aviso.png")));

		idErrorProviderCustomImage.setVisible(false);
		idErrorProviderEmail.setVisible(false);
		idErrorProviderNIF.setVisible(false);

		cboPresentanteProvincia.setConverter(new MaestroCodigoDescripcionConverter(cboPresentanteProvincia));
		cboxProvinciaDefecto.setConverter(new MaestroCodigoDescripcionConverter(cboxProvinciaDefecto));
		cboxRegistroMercantil.setConverter(new MaestroCodigoDescripcionConverter(cboxRegistroMercantil));
		cboxRetencion.setConverter(new MaestroCodigoDescripcionConverter(cboxRetencion));

		txtDirectorioLegislacion.setText(MGeneral.Configuracion.getPathDatos());

		setDatosVista();

		// Llenando municipio
		MaestroCodigoDescripcion provincia = new MaestroCodigoDescripcion("Provincias");
		String prov = MGeneral.Configuracion.getValorDefectoPresentanteCodigoProvincia();

		if (provincia.existeCodigo(prov)) {

			List<String> Lista = MGeneral.Idioma.obtenerMunicipiosDeProvincia(prov);

			cboxMunicipio.cargarCombo(Lista);
		}

		cboPresentanteProvincia.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				// Se ejecutará cuando el ComboBox pierda el foco
				try {
					String filtro = cboPresentanteProvincia.getEditor().getText().toUpperCase();

					if (cboPresentanteProvincia.validarSeleccion()) {

						MaestroCodigoDescripcion aux = new MaestroCodigoDescripcion("Provincias");
						String codigo = aux.obtenerCodigoDeDescripcion(filtro);

						List<String> Lista = MGeneral.Idioma.obtenerMunicipiosDeProvincia(codigo);

						cboxMunicipio.cargarCombo(Lista);

					}

				} catch (Exception e) {

					// e.printStackTrace();
				}
			}
		});

		InicializaHandlers();
		formatoTextfieldDatosPersona();
		formatoTextfieldEmail();
		formatoTextfieldNIFFaxTelefono();
		formatoTextfieldPostal();

		validarTodosLosControles(frmConfiguracion);

	}

	private void formatoTextfieldDatosPersona() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 35) {
				return change;
			}
			return null;
		};

		txtPresentanteNombre.setTextFormatter(new TextFormatter<>(filter));
		txtPresentanteApellido1.setTextFormatter(new TextFormatter<>(filter));
		txtPresentanteApellido2.setTextFormatter(new TextFormatter<>(filter));
		txtDomicilio.setTextFormatter(new TextFormatter<>(filter));
	}

	private void formatoTextfieldPostal() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 5) {
				return change;
			}
			return null;
		};

		txtPresentanteCodigoPostal.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldEmail() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 60) {
				return change;
			}
			return null;
		};

		txtPresentanteEmail.setTextFormatter(new TextFormatter<>(filter));

	}

	private void formatoTextfieldNIFFaxTelefono() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			if (change.getControlNewText().length() <= 10) {
				return change;
			}
			return null;
		};

		txtFax.setTextFormatter(new TextFormatter<>(filter));
		txtPresentanteNif.setTextFormatter(new TextFormatter<>(filter));
		txtTelefono.setTextFormatter(new TextFormatter<>(filter));

	}

	private void pruebaWindow() {
		Stage stage = (Stage) frmConfiguracion.getScene().getWindow();
		stage.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				// Coloca aquí tu lógica para manejar el cierre de la ventana
				System.out.println("Ventana cerrada");
			}
		});
	}

	private void InicializaHandlers() {

		cboPresentanteProvincia.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				evValidarAlValidar(cboPresentanteProvincia);
				if (cboPresentanteProvincia.validarSeleccion()) {
					cboxMunicipio.setValue(null);
				}
			}
		});

		txtPresentanteNif.focusedProperty().addListener((observable, oldValue, newValue) -> {
			logger.info("llegamos a perdida de foco NIF");
			if (!newValue) {

				evValidarAlValidar(txtPresentanteNif);
			}
		});

		txtPresentanteNif.textProperty().addListener(
				(observable, oldValue, newValue) -> evValidarInput(txtPresentanteNif, idErrorProviderNIF, iconPaneNif));

		txtPresentanteCodigoPostal.addEventHandler(KeyEvent.KEY_TYPED, this::evSoloNumerosInterfaz);
		txtPresentanteCodigoPostal.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				evValidarAlValidar(txtPresentanteCodigoPostal);
			}
		});

		txtPresentanteCodigoPostal.textProperty()
				.addListener((observable, oldValue, newValue) -> evValidarInput(txtPresentanteCodigoPostal,
						idErrorProviderCustomImage, iconPaneCodigoPostal));

		txtFax.addEventHandler(KeyEvent.KEY_TYPED, this::evSoloNumerosInterfaz);

		txtTelefono.addEventHandler(KeyEvent.KEY_TYPED, this::evSoloNumerosInterfaz);

		txtPresentanteEmail.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				evValidarAlValidar(txtPresentanteEmail);
			}
		});

		txtPresentanteEmail.textProperty().addListener((observable, oldValue,
				newValue) -> evValidarInput(txtPresentanteEmail, idErrorProviderEmail, iconPanelHoverEmail));

	}

	public void evSoloNumerosInterfaz(KeyEvent event) {
		Utilidades.evSoloNumeros(null, event);
	}

	private void evValidarInput(Control control, CustomImageView cview, Pane iconPane) {

		if (((TextField) control).getText().isEmpty()) {
			ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(), iconPane,
					cview, true, control);

		} else {
			ErrorProvider(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.CampoObligatorio).toString(), iconPane,
					cview, false, control);

		}

	}

	private void evValidarAlValidar(Control control) {
		validaControl(control, true);

	}

	private void getDatosVista() {

		try {

			MGeneral.Configuracion.setPathDatos(txtDirectorioLegislacion.getText());
			MGeneral.Configuracion.setValorDefectoPresentanteNombre(txtPresentanteNombre.getText());
			MGeneral.Configuracion.setValorDefectoPresentanteApellido1(txtPresentanteApellido1.getText());
			MGeneral.Configuracion.setValorDefectoPresentanteApellido2(txtPresentanteApellido2.getText());
			MGeneral.Configuracion.setValorDefectoPresentanteNif(txtPresentanteNif.getText());
			MGeneral.Configuracion.setValorDefectoPresentanteCodigoPostal(txtPresentanteCodigoPostal.getText());
			MGeneral.Configuracion.setValorDefectoPresentanteDomicilio(txtDomicilio.getText());
			MGeneral.Configuracion.setValorDefectoPresentanteEmail(txtPresentanteEmail.getText());
			MGeneral.Configuracion.setMostrarMensajeAlEncriptarEnLegalizacion(CheckboxEncriptarFicheros.isSelected());
			MGeneral.Configuracion.setValorDefectoPresentanteFax(txtFax.getText());
			MGeneral.Configuracion.setValorDefectoPresentanteTelefono(txtTelefono.getText());

			if (cboxProvinciaDefecto.getEditor().getText().trim() == "") {
				MGeneral.Configuracion.setValorDefectoCodigoProvincia(" ");
			} else {
				MGeneral.Configuracion.setValorDefectoCodigoProvincia(cboxProvinciaDefecto.getValue().getCodigo());
			}

			if (cboPresentanteProvincia.getEditor().getText().trim() == "") {

				MGeneral.Configuracion.setValorDefectoPresentanteCodigoProvincia(" ");
			} else {

				MGeneral.Configuracion
						.setValorDefectoPresentanteCodigoProvincia(cboPresentanteProvincia.getValue().getCodigo());
			}
			if (cboxRegistroMercantil.getEditor().getText().trim() == "") {
				MGeneral.Configuracion.setValorDefectoCodigoRegistro(" ");
			} else {
				MGeneral.Configuracion.setValorDefectoCodigoRegistro(cboxRegistroMercantil.getValue().getCodigo());
			}

			if (cboxMunicipio.getEditor().getText().trim() == "") {
				MGeneral.Configuracion.setValorDefectoPresentanteCiudad(" ");
			} else {
				MGeneral.Configuracion.setValorDefectoPresentanteCiudad(cboxMunicipio.getValue().toString());
			}

			if (cboxRetencion.getEditor().getText().trim() == "") {
				MGeneral.Configuracion.setValorDefectoPresentanteSolicitaRetencion(" ");
			} else {
				MGeneral.Configuracion
						.setValorDefectoPresentanteSolicitaRetencion(cboxRetencion.getValue().getCodigo());
			}
			// aber2
			MGeneral.Configuracion.setMostrarMensajeAlEncriptarEnLegalizacion(CheckboxEncriptarFicheros.isSelected());

		} catch (Exception e) {

		}

	}

	private void setDatosVista() {

		if (MGeneral.Configuracion.cargaConfiguracion()) {

			txtPresentanteApellido1.setText(MGeneral.Configuracion.getValorDefectoPresentanteApellido1());
			txtPresentanteApellido2.setText(MGeneral.Configuracion.getValorDefectoPresentanteApellido2());
			txtPresentanteNif.setText(MGeneral.Configuracion.getValorDefectoPresentanteNif());
			txtPresentanteCodigoPostal.setText(MGeneral.Configuracion.getValorDefectoPresentanteCodigoPostal());
			txtDirectorioLegislacion.setText(MGeneral.Configuracion.getPathDatos());
			txtDomicilio.setText(MGeneral.Configuracion.getValorDefectoPresentanteDomicilio());
			txtPresentanteEmail.setText(MGeneral.Configuracion.getValorDefectoPresentanteEmail());
			txtFax.setText(MGeneral.Configuracion.getValorDefectoPresentanteFax());
			txtPresentanteNombre.setText(MGeneral.Configuracion.getValorDefectoPresentanteNombre());
			txtTelefono.setText(MGeneral.Configuracion.getValorDefectoPresentanteTelefono());

			CheckboxEncriptarFicheros.setSelected(MGeneral.Configuracion.isMostrarMensajeAlEncriptarEnLegalizacion());

			// Comboboxes
			String codigo = MGeneral.Configuracion.getValorDefectoPresentanteCodigoProvincia();
			MaestroCodigoDescripcion provincia = new MaestroCodigoDescripcion("Provincias");
			// Provincia Presentante

			if (provincia.existeCodigo(codigo)) {
				provincia.setCodigo(codigo);
				provincia.setDescripcion(provincia.obtenerDescripcion(codigo));
				cboPresentanteProvincia.setValue(provincia);
			}
			// Municipio Presentante
			cboxMunicipio.setValue(MGeneral.Configuracion.getValorDefectoPresentanteCiudad());// cboxMunicipio.getSelectionModel().clearSelection();

			// Provincia Defecto
			MaestroCodigoDescripcion provinciadefecto = new MaestroCodigoDescripcion("Provincias");
			codigo = MGeneral.Configuracion.getValorDefectoCodigoProvincia();

			if (provinciadefecto.existeCodigo(codigo)) {
				provinciadefecto.setCodigo(codigo);
				provinciadefecto.setDescripcion(provinciadefecto.obtenerDescripcion(codigo));
				cboxProvinciaDefecto.setValue(provinciadefecto);
			}

			// Registro Mercantil

			codigo = MGeneral.Configuracion.getValorDefectoCodigoRegistro();

			MaestroCodigoDescripcion registro = new MaestroCodigoDescripcion("Registros");

			if (registro.existeCodigo(codigo)) {
				registro.setCodigo(codigo);
				registro.setDescripcion(registro.obtenerDescripcion(codigo));
				cboxRegistroMercantil.setValue(registro);
			}

			// Retencion
			codigo = MGeneral.Configuracion.getValorDefectoPresentanteSolicitaRetencion();

			MaestroCodigoDescripcion retencion = new MaestroCodigoDescripcion("SolicitaRetencion");
			if (retencion.existeCodigo(codigo)) {
				retencion.setCodigo(codigo);
				retencion.setDescripcion(retencion.obtenerDescripcion(codigo));
				cboxRetencion.setValue(retencion);
			}

		}
	}

	@FXML
	private void abrirDirectoryChooser() {
		Stage stage = new Stage();
		DirectoryChooser directoryChooser = new DirectoryChooser();

		// Configurar el diálogo, si es necesario (esto debe salir de recursosapp.xml)
		directoryChooser.setTitle("Seleccionar Directorio");// recursos.xml

		// Mostrar el diálogo y obtener el directorio seleccionado
		File selectedDirectory = directoryChooser.showDialog(stage);

		if (selectedDirectory != null) {
			String selectedPath = selectedDirectory.getAbsolutePath();
			System.out.println("Directorio seleccionado: " + selectedPath);

			// Colocar la ruta del directorio en el TextField
			txtDirectorioLegislacion.setText(selectedPath);
		} else {
			System.out.println("No se ha seleccionado ningún directorio.");
		}
	}

	@FXML
	private void botonGuardar(ActionEvent event) throws IOException {

		if (comprueba()) {

			if (necesitareiniciar) {
				if (MGeneral.mlform == null) {
					try {

						ProcessBuilder processBuilder = new ProcessBuilder("");
						processBuilder.start();
					} catch (IOException e) {
						e.printStackTrace();

					}
				} else {
					cerrarAplicacion(event);
				}
			}

			getDatosVista();
			MGeneral.Configuracion.GuardaConfiguracion();
			Node source = (Node) event.getSource();
			Stage stage = (Stage) source.getScene().getWindow();
			stage.close();

		}

	}

	private boolean comprueba() {

		boolean resultado = false;

		if (Formato.ValorNulo(txtDirectorioLegislacion.getText())) {

			btnSeleccionDirectorio.requestFocus();
			return resultado;
		}

		if (!Ficheros.DirectorioComprueba(txtDirectorioLegislacion.getText())) {

			btnSeleccionDirectorio.requestFocus();
			return resultado;
		}

		if (!validacion1()) {
			return resultado;
		}
		if (!validacion2()) {
			return resultado;
		}
		if (!validacion3()) {
			return resultado;
		}
		if (!validacion5()) {
			return resultado;
		}

		resultado = true;

		return resultado;
	}

	public static boolean existeDescripcionEnLista(String valorIngresado,
			ObservableList<MaestroCodigoDescripcion> lista) {
		for (MaestroCodigoDescripcion miObjeto : lista) {
			if (miObjeto.getDescripcion().equals(valorIngresado)) {
				return true; // Encuentra una coincidencia
			}
		}
		return false; // No se encuentra ninguna coincidencia
	}

	public void vaciarComboBoxMuni() {
		ObservableList<String> items = FXCollections.observableArrayList();
		cboxMunicipio.setItems(items);
		cboxMunicipio.setValue(null); // Establecer el valor en null para que no haya ninguna selección
	}

	@FXML
	public boolean validacion1() {

		List<MaestroCodigoDescripcion> maestros = MGeneral.Idioma.obtenerListaMaestro("Registros");
		ObservableList<MaestroCodigoDescripcion> observableList = FXCollections.observableArrayList(maestros);

		try {
			String filter = cboxRegistroMercantil.getEditor().getText();
			if (!existeDescripcionEnLista(filter, observableList) && !filter.equals("")) {
				MGeneral.Idioma.MostrarMensaje(EnumMensajes.VerificarElementoLista, "", "", "");

				cboxRegistroMercantil.requestFocus();
				return false;
			}
			return true;
		} catch (Exception e) {

			return false;
		}

	}

	@FXML
	public boolean validacion2() {

		List<MaestroCodigoDescripcion> maestros = MGeneral.Idioma.obtenerListaMaestro("Provincias");
		ObservableList<MaestroCodigoDescripcion> observableList = FXCollections.observableArrayList(maestros);

		try {
			String filter = cboxProvinciaDefecto.getEditor().getText();
			if (!existeDescripcionEnLista(filter, observableList) && !filter.equals("")) {
				MGeneral.Idioma.MostrarMensaje(EnumMensajes.VerificarElementoLista, "", "", "");

				// Preguntar
				cboxProvinciaDefecto.requestFocus();
				return false;
			}
			return true;
		} catch (Exception e) {
			// e.printStackTrace();

			return false;
		}

	}

	@FXML
	public boolean validacion3() {

		List<MaestroCodigoDescripcion> maestros = MGeneral.Idioma.obtenerListaMaestro("Provincias");
		ObservableList<MaestroCodigoDescripcion> observableList = FXCollections.observableArrayList(maestros);

		try {
			String filter = cboPresentanteProvincia.getEditor().getText();
			if (!existeDescripcionEnLista(filter, observableList) && !filter.equals("")) {
				MGeneral.Idioma.MostrarMensaje(EnumMensajes.VerificarElementoLista, "", "", "");
				cboPresentanteProvincia.requestFocus();
				return false;
			}
			return true;
		} catch (Exception e) {
			// e.printStackTrace();

			return false;
		}

	}

	@FXML
	public boolean validacion4() {

		try {
			String muni = cboPresentanteProvincia.getValue().getCodigo();
			List<String> maestros = MGeneral.Idioma.obtenerMunicipiosDeProvincia(muni);
			ObservableList<String> observableList = FXCollections.observableArrayList(maestros);

			String filter = cboxMunicipio.getEditor().getText();

			if (!observableList.contains(filter) && !filter.equals("")) {
				MGeneral.Idioma.MostrarMensaje(EnumMensajes.VerificarElementoLista, "", "", "");

				return false;
			}
			return true;
		} catch (Exception e) {
			// e.printStackTrace();

			return false;
		}

	}

	@FXML
	public boolean validacion5() {

		List<MaestroCodigoDescripcion> maestros = MGeneral.Idioma.obtenerListaMaestro("SolicitaRetencion");
		ObservableList<MaestroCodigoDescripcion> observableList = FXCollections.observableArrayList(maestros);

		try {
			String filter = cboxRetencion.getEditor().getText();
			if (!existeDescripcionEnLista(filter, observableList) && !filter.equals("")) {
				MGeneral.Idioma.MostrarMensaje(EnumMensajes.VerificarElementoLista, "", "", "");

				cboxRetencion.requestFocus();
				return false;
			}
			return true;
		} catch (Exception e) {

			return false;
		}

	}

	@FXML
	public void cancelar(ActionEvent e) {
		if (pendienteGuardar) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Confirmación");
			alert.setHeaderText(null);
			alert.setContentText("¿Desea guardar los datos?");

			// Mostrar el Alert y esperar la respuesta del usuario
			ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

			// Manejar la respuesta del usuario
			if (result == ButtonType.OK) {
				getDatosVista();
				MGeneral.Configuracion.GuardaConfiguracion();
				close(e);
			} else {
				close(e);
			}
		} else {
			close(e);
		}
	}

	private void close(ActionEvent e) {
		Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
		Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el elemento
		stage.close();
	}

	private void activaGuardado(boolean activar) {
		btnGuardar.setDisable(!activar);
	}

	private boolean validaControl(Control control, boolean validarObligatoriedad) {

		try {

			switch (control.getId().toString()) {

			case "txtPresentanteNif":

				if (!((TextField) control).getText().isEmpty()) {

					if (!Formato.validaNif(((TextField) control).getText())) {

						ErrorProvider(IdiomaC
								.obtenerMensaje(IdiomaC.EnumMensajes.FormatoNifIncorrecto, null, null, null).toString(),
								iconPaneNif, idErrorProviderNIF, true, control);

						return false;
					} else {

						ErrorProvider(IdiomaC
								.obtenerMensaje(IdiomaC.EnumMensajes.FormatoNifIncorrecto, null, null, null).toString(),
								iconPaneNif, idErrorProviderNIF, false, control);
					}
				}
				break;

			case "txtPresentanteCodigoPostal":

				if (!((TextField) control).getText().isEmpty()) {

					if ((((TextField) control).getText().length()) != 5) {

						ErrorProvider(
								IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoTiene5Digitos, null, null,
										null).toString(),
								iconPaneCodigoPostal, idErrorProviderCustomImage, true, control);

						return false;
					} else {
						ErrorProvider(
								IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoTiene5Digitos, null, null,
										null).toString(),
								iconPaneCodigoPostal, idErrorProviderCustomImage, false, control); // Imagen(true,
						// idErrorProviderEmail);

						if ((Formato.verificaProvinciaYCodigoPostal(txtPresentanteCodigoPostal,
								cboPresentanteProvincia)) == false) {

							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia,
											null, null, null).toString(),
									iconPaneCodigoPostal, idErrorProviderCustomImage, true, control);
							return true;

						} else {
							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia,
											null, null, null).toString(),
									iconPaneCodigoPostal, idErrorProviderCustomImage, false, control);

						}
					}
				}
				break;

			case "cboPresentanteProvincia":

				if (!txtPresentanteCodigoPostal.getText().isEmpty()) {

					if (txtPresentanteCodigoPostal.getText().length() != 5) {

						ErrorProvider(
								IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoTiene5Digitos, null, null,
										null).toString(),
								iconPaneCodigoPostal, idErrorProviderCustomImage, true, control);

						return false;
					} else {
						ErrorProvider(
								IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoTiene5Digitos, null, null,
										null).toString(),
								iconPaneCodigoPostal, idErrorProviderCustomImage, false, control); // Imagen(true,
						// idErrorProviderEmail);

						if ((Formato.verificaProvinciaYCodigoPostal(txtPresentanteCodigoPostal,
								cboPresentanteProvincia)) == false) {

							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia,
											null, null, null).toString(),
									iconPaneCodigoPostal, idErrorProviderCustomImage, true, control);
							return true;

						} else {
							ErrorProvider(
									IdiomaC.obtenerMensaje(IdiomaC.EnumMensajes.CodigoPostalNoConcuerdaConProvincia,
											null, null, null).toString(),
									iconPaneCodigoPostal, idErrorProviderCustomImage, false, control);

						}
					}
				}
				break;

			case "txtPresentanteEmail":

				if (!Formato.ValorNulo(txtPresentanteEmail.getText())) {

					if (!Formato.validaEmail(txtPresentanteEmail.getText())) {
						ErrorProvider(
								MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.FormatoEmailIncorrecto).toString(),
								iconPanelHoverEmail, idErrorProviderEmail, true, control);
						return false;

					} else {
						ErrorProvider(
								MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.FormatoEmailIncorrecto).toString(),
								iconPanelHoverEmail, idErrorProviderEmail, false, control);
						// Imagen(true,idErrorProviderEmail);

					}
				}
				break;

			}

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
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

	private boolean validarTodosLosControles(Node control) {
		Parent primerControlNoValido = null;
		try {
			for (int i = 0; i < controlsInOrderToNavigate.length; i++) {

				validaControl(controlsInOrderToNavigate[i], true);
			}

			primerControlNoValido = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
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
				controlfinal.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
					if (primeraVez) {
						setPendienteGuardar(true);
					}

				});
			}
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

	public void cerrarAplicacion(ActionEvent e) {

		if (e != null) {
			Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
			Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el
																	// elemento
			stage.close();
		}
	}

	private void setPendienteGuardar(boolean value) {
		pendienteGuardar = value;

		btnGuardar.setDisable(!value);
	}

	@Override
	public void initializeControlsInOrderToNavigate() {
		controlsInOrderToNavigate = new Control[] { cboxRegistroMercantil, cboxProvinciaDefecto, txtPresentanteNombre,
				txtPresentanteApellido1, txtPresentanteApellido2, txtPresentanteNif, txtDomicilio,
				txtPresentanteCodigoPostal, cboPresentanteProvincia, cboxMunicipio, txtTelefono, txtFax,
				txtPresentanteEmail, cboxRetencion };// TODO Auto-generated method stub

	}

	@Override
	public Control[] getControlsInOrderToNavigate() {
		// TODO Auto-generated method stub
		return controlsInOrderToNavigate;
	}

}
