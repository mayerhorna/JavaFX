package com.commerceapp.controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import com.commerceapp.controller.helpers.NavigableControllerHelper;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.cEncriptacion;
import com.commerceapp.domain.legalizacion.MensajesReglasC;
import com.commerceapp.domain.legalizacion.cFicheroLibro;
import com.commerceapp.domain.legalizacion.cLibro;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.gui.custom.combobox.CustomCombobox;
import com.commerceapp.gui.custom.datePicker.CustomDatePicker;
import com.commerceapp.maestros.FormatoFichero;
import com.commerceapp.maestros.FormatoFicheroConverter;
import com.commerceapp.maestros.TipoLibro;
import com.commerceapp.maestros.TipoLibroConverter;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EspecificarLibrosController implements Initializable, NavigableControllerHelper {
	private static final Logger logger = Logger.getLogger(EspecificarLibrosController.class.getName());
	private static File lastDirectory = null;
	private MenuPrincipalController parentController;
	private Control[] controlsInOrderToNavigate;

	public void setParentController(MenuPrincipalController parentController) {
		this.parentController = parentController;

	}

	public MenuPrincipalController getParentController() {
		return parentController;
	}

	ArrayList<cFicheroLibro> listaFicheros;
	private String[] indices; // cambie a string para que no se repitiera el indice 0
	private String[] fechas;
	private ArrayList<String> tipos;

	@FXML
	private Button btnAbrirLibro;

	@FXML
	private Button btnBuscar;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnInsertar;

	@FXML
	private Button btnLimpiar;

	@FXML
	private Button btnModificar;

	@FXML
	private TableColumn<cFicheroLibro, String> cApertura;

	@FXML
	private TableColumn<cFicheroLibro, String> cCierre;

	@FXML
	private TableColumn<cFicheroLibro, String> cCierreUltima;

	@FXML
	private TableColumn<cFicheroLibro, String> cEncriptado;

	@FXML
	private TableColumn<cFicheroLibro, String> cNombre;

	@FXML
	private TableColumn<cFicheroLibro, String> cNumero;

	@FXML
	private TableColumn<cFicheroLibro, String> cTipo;

	@FXML
	private CustomCombobox<FormatoFichero> cboFormato;

	@FXML
	private CustomCombobox<TipoLibro> cboTipo;

	@FXML
	private CheckBox chkEncriptado;

	@FXML
	private AnchorPane frmEspecificarLibros;

	@FXML
	private Label lblEncriptado;

	@FXML
	private Label lblFechaApertura;

	@FXML
	private Label lblFechaCierre;

	@FXML
	private Label lblFechaCierreUltimoLegalizado;

	@FXML
	private Label lblFichero;

	@FXML
	private Label lblFormato;

	@FXML
	private Label lblFormatoShort;

	@FXML
	private Label lblNombre;

	@FXML
	private Label lblNumero;

	@FXML
	private Label lblTipo;

	@FXML
	private Label lblTipoShort;

	@FXML
	private TableView<cFicheroLibro> lsvLibros;

	@FXML
	private CustomDatePicker txtFechaApertura;

	@FXML
	private CustomDatePicker txtFechaCierre;

	@FXML
	private CustomDatePicker txtFechaCierreUltimoLegalizado;

	@FXML
	private TextField txtFichero;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtNumero;
	private boolean _soloLectura;
	private Stage stage;

	public void setStage(Stage stage) {
		this.stage = stage;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lsvLibros.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		init(location, resources);
		GridPane.setColumnSpan(cboTipo, 4);
		GridPane.setColumnSpan(txtFichero, 4);
		GridPane.setColumnSpan(txtNombre, 4);
		GridPane.setColumnSpan(lblFechaCierreUltimoLegalizado, 3);

		lsvLibros.setRowFactory(tv -> {
			TableRow<cFicheroLibro> row = new TableRow<>();

			row.setOnMouseClicked(event -> {
				if (!row.isEmpty()) {
					llenarDesdeGrid();
					if (event.getClickCount() == 2) {
						try {
							btnAbrirLibroClicked(null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			});
			return row;
		});

		cboTipo.cargarCombo(kLegalizacion.Tiposlibros);
		cboTipo.setConverter(new TipoLibroConverter(cboTipo));

		cboTipo.focusedProperty().addListener((obs, oldValue, newValue) -> {
			if (!newValue) { // Si la ComboBox pierde el foco

				activaNumero();
				activaFechaCierreUltimoLegalizado();
			}
		});

		txtNumero.focusedProperty().addListener((obs, oldValue, newValue) -> {

			if (!newValue) { // Si la ComboBox pierde el foco

				activaFechaCierreUltimoLegalizado();
			}
		});

		cboFormato.setConverter(new FormatoFicheroConverter(cboFormato));
		cboFormato.cargarCombo(kLegalizacion.ExtensionesFicheros);

		cTipo.setCellValueFactory(data -> {
			Integer index = listaFicheros.indexOf(data.getValue());

			return new SimpleStringProperty(tipos.get(index));
		});

		cNumero.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getNumero())));

		cNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
		cApertura.setCellValueFactory(data -> {
			String fecha = data.getValue().getFechaApertura();
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
				LocalDate fechaParse = LocalDate.parse(fecha, formatter);

				DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				String fechaFormateada = fechaParse.format(formatter2);
				return new SimpleStringProperty(fechaFormateada);
			} catch (Exception e) {
				LocalDate localdate = LocalDate.of(1, 1, 1);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				String fechaFormateada = localdate.format(formatter);
				return new SimpleStringProperty(fechaFormateada);
			}

		});

		cCierre.setCellValueFactory(data -> {

			String fecha = data.getValue().getFechaCierre();
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
				LocalDate fechaParse = LocalDate.parse(fecha, formatter);

				DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				String fechaFormateada = fechaParse.format(formatter2);
				return new SimpleStringProperty(fechaFormateada);
			} catch (Exception e) {
				LocalDate localdate = LocalDate.of(1, 1, 1);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				String fechaFormateada = localdate.format(formatter);
				return new SimpleStringProperty(fechaFormateada);
			}
		});

		cEncriptado.setCellValueFactory(data -> {
			StringBuilder sino = new StringBuilder("");

			if (data.getValue().isEsEncriptado()) {
				sino.append(MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.Si));
			} else {
				sino.append("");
			}
			return new SimpleStringProperty(sino.toString());
		});

		cargarLibrosEnGrid();

		cCierreUltima.setCellValueFactory(data -> {

			String fecha = "";
			String fechaFormateada = "";
			Integer index = listaFicheros.indexOf(data.getValue());

			int i = 0;
			for (String num : indices) {
				if (num.equals(index.toString())) {
					fecha = fechas[i];
				}

				i++;
			}
			if (!fecha.trim().equals("")) {
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
					LocalDate fechaParse = LocalDate.parse(fecha, formatter);

					DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					String fechaFormateada1 = fechaParse.format(formatter2);
					return new SimpleStringProperty(fechaFormateada1);
				} catch (Exception e) {
					LocalDate localdate = LocalDate.of(1, 1, 1);
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					String fechaFormateada1 = localdate.format(formatter);
					return new SimpleStringProperty(fechaFormateada1);
				}
			}

			return new SimpleStringProperty(fechaFormateada);
		}

		);
		if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloLectura
				|| MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloReenviar) {

			_soloLectura = true;

			Platform.runLater(() -> {
				Stage stage = (Stage) lblEncriptado.getScene().getWindow();
				stage.setTitle(
						stage.getTitle() + " " + MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.SoloLectura));

			});

			botonesActivos();
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				registerEventControl();
				onRenderStage(frmEspecificarLibros.getScene());
				Stage stage = (Stage) frmEspecificarLibros.getScene().getWindow();
				stage.setOnCloseRequest(event -> {
					// Consume the event to prevent automatic closing
					frmEspecificarLibros_FormClosed();
				});

				frmEspecificarLibros.getScene().widthProperty().addListener((observable, oldValue, newValue) -> {
					double width = newValue.doubleValue();
					String cssFile = "";
					logger.info("ancho: " + width);
					if (width > 1000) {
						cssFile = "/estilos/Grande.css";

					}

					if (width > 900 && width <= 1000) {
						cssFile = "/estilos/Mediano3.css";

					}
					if (width > 800 && width <= 900) {
						cssFile = "/estilos/Mediano2.css";

					}
					if (width <= 800) {
						cssFile = "/estilos/Mediano1.css";

					}

					logger.info(cssFile);
					frmEspecificarLibros.getStylesheets().clear();
					frmEspecificarLibros.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());

				});
			}
		});
	}

	@FXML
	public void frmEspecificarLibros_FormClosed() {
		// Actualizar el número de libros presentados
		// 'Se actualiza el título ya que en él se ve el ejercicio
		try {

			if (getParentController().form != null) {
				getParentController().form.txtLibrosPresentados
						.setText(String.valueOf(MGeneral.mlform.getNumeroTotalFicherosPresentados()));
				getParentController().establecerTitulo();
			}
		} catch (Exception e) {

		}
	}

	@FXML
	public boolean cargarFolderDialog() {
		boolean retorno = false;
		FormatoFichero formatoFichero = new FormatoFichero();
		String os = System.getProperty("os.name").toLowerCase();
		try {
			if (cboFormato.getSelectionModel().getSelectedItem() instanceof FormatoFichero) {
				formatoFichero = (FormatoFichero) cboFormato.getSelectionModel().getSelectedItem();
			}

			FileChooser fileChooser = new FileChooser();

			if (lastDirectory == null) {
				FileSystemView fileSystemView = FileSystemView.getFileSystemView();
				File desktopDir = fileSystemView.getHomeDirectory();
				fileChooser.setInitialDirectory(desktopDir);
			}

			if (lastDirectory != null && lastDirectory.exists()) {
				fileChooser.setInitialDirectory(lastDirectory);
			}
			if (os.contains("win")) {
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos ",
						kLegalizacion.dameCadenaExtensionesPermitidas(true), cEncriptacion.filtroDes));
			} else if (os.contains("nux")) {
				fileChooser.getExtensionFilters()
						.add(new FileChooser.ExtensionFilter(
								kLegalizacion.dameCadenaExtensionesPermitidas(true).replace(";", ","),
								cEncriptacion.filtroDes));
			}
			File selectedFile = fileChooser.showOpenDialog(new Stage());
			if (selectedFile != null) {
				lastDirectory = selectedFile.getParentFile();
			}
			if (selectedFile != null) {
				String filePath = selectedFile.getAbsolutePath();
				String extension = filePath.substring(filePath.lastIndexOf(".") + 1);

				// Obtiene la extensión sin el punto inicial

				kLegalizacion.enumExtensionFichero tipoextension = kLegalizacion
						.dameCodigoDeExtensionFicheroSegunExtension(extension);

				if (tipoextension == null) {

					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ExtensionFicheroNoAdmitida, extension, "", "");
					return false;
				}

				txtFichero.setText(filePath);

				FormatoFichero elemento = cboFormato
						.buscarMiembro(kLegalizacion.dameCodigoDeExtensionFicheroSegunExtension(extension));

				cboFormato.setValue(elemento);

				int indiceextension = kLegalizacion.dameIndiceDeExtensionFichero(
						kLegalizacion.dameCodigoDeExtensionFicheroSegunExtension(extension));
				chkEncriptado.setSelected(kLegalizacion.ExtensionesFicheros.get(indiceextension).isEsDeEncriptacion());

				txtNumero.requestFocus(); // Establece el foco en el campo txtNumero

				return retorno;
			} else {
				return false;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
			return false;
		}
	}

	@FXML
	private void eliminarLibro(ActionEvent e) {
		try {
			// Verificar si se ha seleccionado algún ítem en la lista
			if (lsvLibros.getSelectionModel().getSelectedItems().isEmpty()) {
				return;
			}
//aca va un mostrar mensaje con opciones
			// Mostrar un diálogo de confirmación antes de eliminar el libro
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Confirmación");
			alert.setHeaderText(null);
			alert.setContentText("¿Estás seguro de que quieres eliminar el libro seleccionado?");
			if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
				return;
			}
			int[] referencias = new int[3];

			// Obtener el índice del libro seleccionado en la lista
			referencias[0] = lsvLibros.getSelectionModel().getSelectedIndex();
			referencias[1] = 0;
			referencias[2] = 0;
			// Eliminar el libro
			boolean eliminado = dameIndicesLibroDeGrid(referencias);

			// Mostrar mensajes de error si es necesario
			if (!eliminado) {
				return;
			}
			boolean resultado = MGeneral.mlform.quitaFichero(referencias[1], referencias[2]);
			if (!resultado) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NoSeHaPodidoRealizarLaOperacion, "", "", "");
				return;
			}
			if (!MGeneral.mlform.guarda()) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NoSeHaPodidoGuardar, "", "", "");
				return;
			}

			cargarLibrosEnGrid();

		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}
	}

	@FXML
	public void insertar(ActionEvent e) {
		try {
			if (!comprueba(-1, -1))
				return;

			String fcultleg = "";

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

			if (fechaCierreUltimoLegalizadoActivado()) {

				LocalDate fechaU = txtFechaCierreUltimoLegalizado.getValue();

				fcultleg = fechaU.format(formatter);
			}

			activarTodo(false);

			// Convertir LocalDate a String
			String fApertura = txtFechaApertura.getValue().format(formatter);
			String fCierre = txtFechaCierre.getValue().format(formatter);

			if (!MGeneral.mlform.importaFichero(txtFichero.getText(), cboTipo.getValue().getCodigo(),
					txtNombre.getText(), Integer.parseInt(txtNumero.getText()), fcultleg,
					cboFormato.getValue().getCodigo(), fApertura, fCierre, chkEncriptado.isSelected())) {
				return;
			}

			if (!MGeneral.mlform.guarda()) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NoSeHaPodidoGuardar, "", "", "");
				return;
			}

			if (MGeneral.mlform.isUltimoFicheroSeHaEncriptado()) {
				mostrarResultadoEncriptacion();
			}

			cargarLibrosEnGrid();
			avisosValidacion(); // aqui bota error por lo de traduce validaciones
			avisoBytesFicheros();

			cboTipo.requestFocus();
		} catch (Exception ex) {
			// ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		} finally {
			activarTodo(true);
		}
	}

	public void avisosValidacion() {
		// revisar luego
		kLegalizacion.enumResultadoValidacion resultado = MGeneral.mlform.valida();

		if (resultado != kLegalizacion.enumResultadoValidacion.Valida) {
			StringBuilder cadena = new StringBuilder();

			for (int i = 0; i < MGeneral.mlform.MensajesReglas.length; i++) {
				if (MGeneral.mlform.MensajesReglas[i].getOrigen() == MensajesReglasC.Origen.PrimariasLibros) {
					cadena.append(MGeneral.mlform.MensajesReglas[i].getTextoMensaje()).append("\n");
				}
			}

			if (cadena.length() > 0) {
				MGeneral.Idioma.mostrarMensajeCadena(cadena.toString(), "", "");
			}
		}
	}

	public void avisoBytesFicheros() {
		if (MGeneral.Configuracion.getBytesAvisoZip() > 0) {
			if (MGeneral.mlform.getBytes() >= MGeneral.Configuracion.getBytesAvisoZip()) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.BytesAvisoZip, "", "", "");
			}
		}
	}

	public boolean comprueba(int indicelibro, int indicefichero) {
		try {
			LocalDate fapertura, fcierre;
			kLegalizacion.enumTipoLibro tipolibro;
			cLibro vlibro = new cLibro();

			// Tipo de libro es obligatorio
			if (cboTipo.getValue() == null || cboTipo.getValue().toString().isEmpty()) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.VerificarElementoLista, "", "", "");
				cboTipo.requestFocus();
				return false;
			}
			tipolibro = cboTipo.getValue().getCodigo();
			vlibro.setTipoLibro(tipolibro);

			// Seleccionar un fichero es obligatorio
			if (txtFichero.getText().isEmpty()) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, lblFichero.getText(), "", "");
				btnBuscar.requestFocus();
				return false;
			}

			// Existencia de un formato de fichero es obligatorio
			if (cboFormato.getValue() == null || cboFormato.getValue().toString().isEmpty()) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, lblFormato.getText(), "", "");
				cboFormato.requestFocus();
				return false;
			}

			// Si el número de libro está habilitado, es obligatorio introducir un nombre
			if (!txtNumero.isDisabled() && txtNombre.getText().isEmpty()) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, lblNumero.getText(), "", "");
				txtNombre.requestFocus();
				return false;
			}

			// Número de libro es obligatorio (esté o no habilitado)
			if (txtNumero.getText().isEmpty()) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, lblNumero.getText(), "", "");
				txtNumero.requestFocus();
				return false;
			}

			// Fecha de apertura es obligatoria
			if (txtFechaApertura.getValue() == null) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, lblFechaApertura.getText(), "",
						"");
				txtFechaApertura.requestFocus();
				return false;
			}

			// Fecha de cierre es obligatoria
			if (txtFechaCierre.getValue() == null) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, lblFechaCierre.getText(), "", "");
				txtFechaCierre.requestFocus();
				return false;
			}

			fapertura = txtFechaApertura.getValue();
			fcierre = txtFechaCierre.getValue();

			// Control de que la fecha de apertura sea válida

			if (fapertura.isEqual(LocalDate.of(1, 1, 1))) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FechaNoValida, "", "", "");

				txtFechaApertura.requestFocus();
				return false;
			}

			// Control de que la fecha de cierre sea válida

			if (fcierre.isEqual(LocalDate.of(1, 1, 1))) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FechaNoValida, "", "", "");
				txtFechaCierre.requestFocus();
				return false;
			}

			// La fecha de apertura no puede ser mayor que la fecha de cierre

			if (fapertura.compareTo(fcierre) > 0) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ErrorFechas, "", "", "");
				txtFechaApertura.requestFocus();
				return false;
			}

			// Control de la existencia del fichero y su contenido
			File file = new File(txtFichero.getText());
			if (file.exists()) {
				if (file.length() == 0) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FicheroSinContenido, txtFichero.getText(), "",
							"");

					btnBuscar.requestFocus();
					return false;
				}

			} else {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FicheroInexistente, txtFichero.getText(), "", "");

				btnBuscar.requestFocus();
				return false;
			}

			// Comprobaciones en caso de que la fecha de último cierre esté habilitada
			if (!txtFechaCierreUltimoLegalizado.isDisabled()) {
				// Es obligatorio introducir la fecha de último cierre
				if (txtFechaCierreUltimoLegalizado.getValue() == null) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,
							lblFechaCierreUltimoLegalizado.getText(), "", "");

					txtFechaCierreUltimoLegalizado.requestFocus();
					return false;
				}

				// Control de que la fecha de último cierre sea válida

				if (txtFechaCierreUltimoLegalizado.getValue() == null) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FechaNoValida, "", "", "");

					txtFechaCierreUltimoLegalizado.requestFocus();
					return false;
				}

				// La fecha de último cierre debe ser menor que la fecha de apertura

				if (txtFechaCierreUltimoLegalizado.getValue().compareTo(fapertura) >= 0) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FechaCierreUltimoLegalizadoMayorFechaApertura,
							"", "", "");

					txtFechaCierreUltimoLegalizado.requestFocus();
					return false;
				}

			}

			// Entre la fecha de apertura y la de cierre no puede haber más de un año
			// natural
			if (vlibro.comprobarFechaApertura()) {

				if (fapertura.plusYears(1).compareTo(fcierre) <= 0) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FechaAperturaFechaCierreMasDeUnAnio, "", "",
							"");

					txtFechaCierre.requestFocus();
					return false;
				}

			}

			LocalDate minfa, maxfc;

			// Libros de diferentes ejercicios, según la mínima fecha de apertura. Es un
			// aviso, si se muestra, continúa

			minfa = MGeneral.mlform.getMenorFechaApertura(indicelibro, indicefichero);
			maxfc = fcierre;

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			if (!minfa.isEqual(LocalDate.MAX) && !maxfc.isEqual(LocalDate.of(1, 1, 1))) {
				LocalDate fecha = minfa.plusYears(1);
				if (maxfc.compareTo(fecha) >= 0) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.LibrosDeEjerciciosDiferentesApertura,
							minfa.format(formatter), fecha.minusDays(1).format(formatter), "");

				}
			}

			// Libros de diferentes ejercicios, según la máxima fecha de cierre.
			// Es un aviso, si se muestra, continúa

			if (vlibro.comprobarFechaApertura()) {
				minfa = fapertura;
				maxfc = MGeneral.mlform.getMayorFechaCierre(indicelibro, indicefichero);
				if (!minfa.isEqual(LocalDate.MAX) && !maxfc.isEqual(LocalDate.of(1, 1, 1))) {
					LocalDate fecha = maxfc.minusYears(1);
					if (minfa.compareTo(fecha) <= 0) {
						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.LibrosDeEjerciciosDiferentesCierre,
								maxfc.format(formatter), fecha.plusDays(1).format(formatter), "");

					}
				}
			}

			return true;

		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
			ex.printStackTrace();
			return false;
		}
	}

	public String dameValorPorDefectoFechaApertura() {
		// Lógica para determinar el valor por defecto de la fecha de apertura
		// Por ejemplo:
		// int anio = LocalDate.now().getYear() - 1;
		// String fechaApertura = "0101" + anio;
		// return fechaApertura;
		return "";
	}

	public String dameValorPorDefectoFechaCierre() {
		// Lógica para determinar el valor por defecto de la fecha de cierre
		// Por ejemplo:
		// int anio = LocalDate.now().getYear() - 1;
		// String fechaCierre = "3112" + anio;
		// return fechaCierre;
		return "";
	}

	public String dameValorPorDefectoFechaCierreUltimoLegalizado() {
		// Lógica para determinar el valor por defecto de la fecha de cierre del último
		// legalizado
		// Por ejemplo:
		// int anio = LocalDate.now().getYear() - 1;
		// LocalDate fecha = LocalDate.of(anio, 1, 1).minusDays(1);
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		// String fechaCierreUltimoLegalizado = fecha.format(formatter);
		// return fechaCierreUltimoLegalizado;
		return "";
	}

	@FXML
	private void modificar(ActionEvent e) {
		try {
			if (lsvLibros.getSelectionModel().getSelectedItems().isEmpty()) {
				return;
			}
			int[] referencia = new int[3];
			referencia[0] = lsvLibros.getSelectionModel().getSelectedIndex();
			referencia[1] = 0;
			referencia[2] = 0;

			if (!dameIndicesLibroDeGrid(referencia)) {
				return;
			}

			if (!comprueba(referencia[1], referencia[2])) {
				return;
			}

			String fcultleg = "";

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

			if (fechaCierreUltimoLegalizadoActivado()) {
				LocalDate fechaU = txtFechaCierreUltimoLegalizado.getValue();

				fcultleg = fechaU.format(formatter);
			}

			activarTodo(false);

			String fApertura = txtFechaApertura.getValue().format(formatter);
			String fCierre = txtFechaCierre.getValue().format(formatter);

			if (!MGeneral.mlform.modificaFichero(referencia[1], referencia[2], txtFichero.getText(),
					cboTipo.getValue().getCodigo(), txtNombre.getText(), Integer.parseInt(txtNumero.getText()),
					fcultleg, cboFormato.getValue().getCodigo(), fApertura, fCierre, chkEncriptado.isSelected())) {
				return;
			}

			if (!MGeneral.mlform.guarda()) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NoSeHaPodidoGuardar, "", "", "");
				return;
			}

			if (MGeneral.mlform.isUltimoFicheroSeHaEncriptado()) {
				mostrarResultadoEncriptacion();
			}

			cargarLibrosEnGrid();

			avisosValidacion();

			avisoBytesFicheros();

			cboTipo.requestFocus();

		} catch (Exception ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "", "", "");
		} finally {
			activarTodo(true);
		}
	}

	@FXML
	private void btnAbrirLibroClicked(ActionEvent e) {
		try {
			if (lsvLibros.getSelectionModel().getSelectedItems().isEmpty()) {
				return;
			}
			int[] referencia = new int[3];
			referencia[0] = lsvLibros.getSelectionModel().getSelectedIndices().get(0);
			referencia[1] = 0;
			referencia[2] = 0;

			if (!dameIndicesLibroDeGrid(referencia)) {
				return;
			}

			MGeneral.mlform.verLibroFichero(referencia[1], referencia[2]);

		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "", "", "");
			ex.printStackTrace();
		}
	}

	public void mostrarResultadoEncriptacion() {
		String cad = MGeneral.Encriptacion.obtenerResultadoEncriptacion();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EncriptacionResultado.fxml"));
			Parent root = fxmlLoader.load();

			EncriptacionResultadoController encriptacionResultadoController = fxmlLoader.getController();
			// settear esto
			// frmEncriptacionResultado.eModoApertura.EncriptadoEnLaLegalizacion, cad,
			// Encriptacion.FicheroSalida
			// y esto
			// encriptacionResultadoController.setParentController(getParentController());

			Stage stage = new Stage();

			Scene scene = new Scene(root);

			// quitando el maximizar y minimizar
			stage.initStyle(StageStyle.UTILITY);
			// quitando iconos
			stage.getIcons().clear();
			// bloquea la interacción con otras ventanas de la aplicación
			stage.initModality(Modality.APPLICATION_MODAL);

			stage.setScene(scene);
			// MGeneral.Idioma.cargarIdiomaControles(stage, null);
			// stage.setTitle("Configuracion");
			stage.showAndWait();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void activarTodo(boolean activar) {
		Utilidades.cursorEspera((Stage) frmEspecificarLibros.getScene().getWindow(), !activar);

		// groupBox1.setDisable(!activar);
		// groupBox2.setDisable(!activar);
		lsvLibros.setDisable(!activar);
	}

	public boolean dameIndicesLibroDeGrid(int[] referencias) {
		boolean resultado = false;

		try {
			// 1 es libro 2 es fichero
			referencias[1] = -1;
			referencias[2] = -1;
			int cont = -1;

			for (int i = 0; i < MGeneral.mlform.getNumeroLibros(); i++) {
				for (int j = 0; j < MGeneral.mlform.Libros.get(i).getNumeroFicheros(); j++) {
					cont++;

					if (cont == referencias[0]) {
						referencias[1] = i;
						referencias[2] = j;
						return true;
					}
				}
			}
		} catch (Exception ex) {
			// No mostrar error
		}

		return resultado;
	}

	public void cargarLibrosEnGrid() {

		listaFicheros = new ArrayList<>();
		try {
			lsvLibros.getItems().clear();
			// botonesActivos();
			indices = new String[MGeneral.mlform.Libros.size()];
			fechas = new String[MGeneral.mlform.Libros.size()];
			tipos = new ArrayList<>();
			int i = 0;
			int j = 0;

			for (cLibro libro : MGeneral.mlform.Libros) {

				if (!Formato.ValorNulo(MGeneral.mlform.Libros.get(i).getFechaCierreUltimoLegalizado())) {
					fechas[i] = MGeneral.mlform.Libros.get(i).getFechaCierreUltimoLegalizado();
				} else {
					fechas[i] = "";
				}
				int k = 0;
				for (cFicheroLibro fichero : libro.Ficheros) {

					listaFicheros.add(fichero);

					int indice = kLegalizacion.dameIndiceDeTipoLibro(MGeneral.mlform.Libros.get(i).getTipoLibro());
					tipos.add(kLegalizacion.Tiposlibros.get(indice).getDescripcion());

					if (fechas[i] != null && k == 0) {
						// k == libro.Ficheros.length - 1 si queremos el ultimo
						indices[i] = String.valueOf(j);
					}

					k++;
					j++;
				}
				i++;
			}

			limpiar(null);
		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}
		ObservableList<cFicheroLibro> data = FXCollections.observableArrayList(listaFicheros);
		lsvLibros.setItems(data);
	}

	/*
	 * public void cargarLibroEnGrid(int indicelibro, int indicefichero) { try { int
	 * indicetipolibro = kLegalizacion
	 * .dameIndiceDeTipoLibro(MGeneral.mlform.Libros.get(indicefichero).getTipoLibro
	 * ());
	 * 
	 * kLegalizacion.Tiposlibros.get(indicetipolibro).getDescripcion();
	 * 
	 * // MGeneral.mlform.Libros.get(indicelibro).Ficheros[indicefichero];
	 * MGeneral.mlform.Libros.get(indicelibro).Ficheros[indicefichero].
	 * getDescripcion(); LegalizacionService
	 * .dameFecha(MGeneral.mlform.Libros.get(indicelibro).Ficheros[indicefichero].
	 * getFechaApertura()); LegalizacionService
	 * .dameFecha(MGeneral.mlform.Libros.get(indicelibro).Ficheros[indicefichero].
	 * getFechaCierre());
	 * 
	 * if (indicefichero == 0 &&
	 * !Formato.ValorNulo(MGeneral.mlform.Libros.get(indicelibro).
	 * getFechaCierreUltimoLegalizado())) {
	 * LegalizacionService.dameFecha(MGeneral.mlform.Libros.get(indicelibro).
	 * getFechaCierreUltimoLegalizado()); } else { // añade vacio ("") }
	 * 
	 * String sino; if
	 * (MGeneral.mlform.Libros.get(indicelibro).Ficheros[indicefichero].
	 * isEsEncriptado()) { sino =
	 * MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.Si).toString(); } else {
	 * sino = ""; // Para no encriptados no se pone } //
	 * lsvItem.getSubItems().add(sino);
	 * 
	 * } catch (Exception ex) { //
	 * MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, //
	 * ex.getMessage(),"",""); } }
	 */

	@FXML
	public boolean limpiar(ActionEvent e) {
		try {
			cboTipo.getSelectionModel().clearSelection();
			lblTipoShort.setText("");

			txtFichero.setText("");

			cboFormato.getSelectionModel().clearSelection();
			lblFormatoShort.setText("");

			txtNombre.setText("");

			txtNumero.setText("");

			txtFechaApertura.setValue(null);

			txtFechaCierre.setValue(null);

			txtFechaCierreUltimoLegalizado.setValue(null);

			chkEncriptado.setSelected(false);

			quitaSeleccionadoLista();

			botonesActivos();

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}

		return false;
	}

	public void botonesActivos() {
		boolean haySeleccionadoEnLista = !lsvLibros.getSelectionModel().getSelectedItems().isEmpty();

		if (!_soloLectura) {
			btnInsertar.setDisable(haySeleccionadoEnLista); // Inserción si no hay seleccionado
			btnModificar.setDisable(!haySeleccionadoEnLista); // Modificación si hay seleccionado
			btnEliminar.setDisable(!haySeleccionadoEnLista); // Eliminación si hay seleccionado
			btnLimpiar.setDisable(false); // Siempre activo
			btnAbrirLibro.setDisable(!haySeleccionadoEnLista); // Visualizar fichero si hay seleccionado

			activaTipo();
			activaNumero();
			activaFechaCierreUltimoLegalizado();
		} else {
			btnInsertar.setDisable(true);
			btnModificar.setDisable(true);
			btnEliminar.setDisable(true);
			btnLimpiar.setDisable(false); // Siempre activo
			btnAbrirLibro.setDisable(!haySeleccionadoEnLista); // Visualizar fichero si hay seleccionado

			cboTipo.setDisable(true);
			btnBuscar.setDisable(true);
			txtNombre.setDisable(true);
			txtFechaApertura.setDisable(true);
			txtFechaCierre.setDisable(true);
			txtNumero.setDisable(true);
			txtFechaCierreUltimoLegalizado.setDisable(true);
		}
	}

	public void activaTipo() {
		try {
			boolean haySeleccionadoEnLista = !lsvLibros.getSelectionModel().getSelectedItems().isEmpty();
			cboTipo.setDisable(haySeleccionadoEnLista);
		} catch (Exception ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}
	}

	public void activaNumero() {
		try {
			boolean haySeleccionadoEnLista = !lsvLibros.getSelectionModel().getSelectedItems().isEmpty();
			txtNumero.setDisable(false);

			if (haySeleccionadoEnLista) {
				int[] referencia = new int[3];

				referencia[0] = lsvLibros.getSelectionModel().getSelectedIndices().get(0);
				referencia[1] = 0;
				referencia[2] = 0;

				if (!dameIndicesLibroDeGrid(referencia)) {
					return;
				}

				// Si no es el primer fichero para el tipo de libro se desactiva
				if (referencia[2] > 0) {
					txtNumero.setDisable(true);
				}
			} else { // No se encuentra un libro seleccionado en lsvLibros (se va a introducir uno
						// nuevo)
				if (cboTipo.getSelectionModel().getSelectedItem() == null) {
					// Si no se ha indicado el tipo de libro, no se deja introducir el número de
					// libro
					txtNumero.setText("");
					txtNumero.setDisable(true);
				} else {
					// Se mira si el tipo de libro seleccionado ya existe en la legalización actual
					int indicelibrodeltipodelibro = MGeneral.mlform
							.dameIndiceLibroDeTipoLibro(cboTipo.getValue().getCodigo());

					if (indicelibrodeltipodelibro > -1) {
						// Ya hay existe un libro del mismo tipo de libro

						// Número, se calcula automáticamente: número del primer fichero + número de
						// ficheros
						int numtoca = MGeneral.mlform.Libros.get(indicelibrodeltipodelibro).Ficheros[0].getNumero();
						numtoca += MGeneral.mlform.Libros.get(indicelibrodeltipodelibro).getNumeroFicheros();
						txtNumero.setText(String.valueOf(numtoca));
						txtNumero.setDisable(true);

						// Proponer como fecha de apertura la fecha de cierre del anterior + 1 día
						int ultimofichero = MGeneral.mlform.Libros.get(indicelibrodeltipodelibro).getNumeroFicheros()
								- 1;

						String fecha = MGeneral.mlform.Libros.get(indicelibrodeltipodelibro).Ficheros[ultimofichero]
								.getFechaCierre();
						LocalDate date = LegalizacionService.dameFecha(fecha);
						if (date != null) {
							date = date.plusDays(1);
							txtFechaApertura.setValue(date);
						}
					} else {
						// No existe un libro del mismo tipo de libro

						txtFechaApertura.setValue(LegalizacionService.dameFecha(dameValorPorDefectoFechaApertura()));
						txtFechaCierre.setValue(LegalizacionService.dameFecha(dameValorPorDefectoFechaCierre()));
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}
	}

	public void activaFechaCierreUltimoLegalizado() {
		try {
			boolean activar = fechaCierreUltimoLegalizadoActivado();
			txtFechaCierreUltimoLegalizado.setDisable(!activar);

			if (txtFechaCierreUltimoLegalizado.isDisabled()) {
				txtFechaCierreUltimoLegalizado.setValue(null);

			}

			boolean haySeleccionadoEnLista = !lsvLibros.getSelectionModel().getSelectedItems().isEmpty();

			if (!haySeleccionadoEnLista) {
				txtFechaCierreUltimoLegalizado
						.setValue(LegalizacionService.dameFecha(dameValorPorDefectoFechaCierreUltimoLegalizado()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}
	}

	public boolean fechaCierreUltimoLegalizadoActivado() {
		boolean activado = false;

		try {
			boolean haySeleccionadoEnLista = !lsvLibros.getSelectionModel().getSelectedItems().isEmpty();

			if (haySeleccionadoEnLista) {
				int[] referencia = new int[3];

				referencia[0] = lsvLibros.getSelectionModel().getSelectedIndices().get(0);
				referencia[1] = 0;
				referencia[2] = 0;

				if (!dameIndicesLibroDeGrid(referencia)) {
					return false;
				}

				// Si el fichero es el primero del tipo de libro
				if (referencia[2] == 0) {
					if (!txtNumero.getText().equals("1")) {
						// Y el número especificado es distinto (será mayor) que 1
						activado = true;

					}
				}
			} else {
				if (!Formato.ValorNulo(cboTipo.getValue())) {
					// Se mira si el tipo de libro seleccionado ya existe en la legalización actual
					int indicelibrodeltipodelibro = MGeneral.mlform
							.dameIndiceLibroDeTipoLibro(cboTipo.getValue().getCodigo());

					if (indicelibrodeltipodelibro == -1) {
						// Si existe en la legalización actual
						if (!txtNumero.getText().equals("1")) {
							// Y el número especificado es distinto (será mayor) que 1
							activado = true;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}

		return activado;
	}

	public void quitaSeleccionadoLista() {
		lsvLibros.getSelectionModel().clearSelection();
	}

	private void llenarDesdeGrid() {
		try {
			int[] referencia = new int[3];
			referencia[0] = lsvLibros.getSelectionModel().getSelectedIndices().get(0);
			referencia[1] = 0;
			referencia[2] = 0;

			if (lsvLibros.getSelectionModel().getSelectedItems().isEmpty()) {
				limpiar(null);
				return;
			}

			if (!dameIndicesLibroDeGrid(referencia)) {
				return;
			}

			TipoLibro tipo = null;
			for (TipoLibro libro : kLegalizacion.Tiposlibros) {
				if (libro.getCodigo() == MGeneral.mlform.Libros.get(referencia[1]).getTipoLibro()) {
					tipo = libro;
				}
			}

			cboTipo.setValue(tipo);
			cboFormato.getSelectionModel().select(MGeneral.mlform.Libros.get(referencia[1]).Ficheros[referencia[2]]
					.getTipoExtensionFichero().ordinal());
			txtFichero.setText(MGeneral.mlform.Libros.get(referencia[1]).Ficheros[referencia[2]].getPathFichero());
			txtNombre.setText(MGeneral.mlform.Libros.get(referencia[1]).Ficheros[referencia[2]].getDescripcion());
			txtNumero.setText(
					String.valueOf(MGeneral.mlform.Libros.get(referencia[1]).Ficheros[referencia[2]].getNumero()));

			// Fechas

			txtFechaApertura.setValue(LegalizacionService
					.dameFecha(MGeneral.mlform.Libros.get(referencia[1]).Ficheros[referencia[2]].getFechaApertura()));
			txtFechaCierre.setValue(LegalizacionService
					.dameFecha(MGeneral.mlform.Libros.get(referencia[1]).Ficheros[referencia[2]].getFechaCierre()));

			if (referencia[2] == 0) {
				txtFechaCierreUltimoLegalizado.setValue(LegalizacionService
						.dameFecha(MGeneral.mlform.Libros.get(referencia[1]).getFechaCierreUltimoLegalizado()));
			} else {
				txtFechaCierreUltimoLegalizado.setValue(null);
			}

			chkEncriptado
					.setSelected(MGeneral.mlform.Libros.get(referencia[1]).Ficheros[referencia[2]].isEsEncriptado());

			botonesActivos();
		} catch (Exception ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "", "", "");
		}
	}

	void cboTipoSelected(ActionEvent event) {
		TipoLibro selectedTipoLibro = cboTipo.getValue();
		if (selectedTipoLibro != null) {
			lblTipoShort.setText(selectedTipoLibro.getNombreFichero());
			txtNombre.setText(selectedTipoLibro.getDescripcion());
		}
	}

	private void registerEventControl() {
		cboTipo.addEventHandler(ActionEvent.ACTION, this::cboTipoSelected);

	}

	@FXML
	void cboFormatoSelected(ActionEvent event) {
		FormatoFichero selectedFormatoFichero = cboFormato.getValue();
		if (selectedFormatoFichero != null) {
			lblFormatoShort.setText(selectedFormatoFichero.getExtension());
		}
	}

	@FXML
	void soloNumeros(KeyEvent event) {
		// Utilidades.evSoloNumeros(event, event);
		if (event.getCode() == KeyCode.SLASH) {
			event.consume();
		}
	}

	@Override
	public void initializeControlsInOrderToNavigate() {
		controlsInOrderToNavigate = new Control[] { cboTipo, txtNombre, txtNumero, txtFechaApertura, txtFechaCierre,
				txtFechaCierreUltimoLegalizado, btnInsertar, btnModificar,
				// btnEliminar,
				btnLimpiar,
				// btnAbrirLibro
		};

	}

	@Override
	public Control[] getControlsInOrderToNavigate() {
		return controlsInOrderToNavigate;
	}

}
