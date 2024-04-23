package com.commerceapp.controller;

import java.io.File;

import java.net.URL;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.time.LocalDate;

import java.util.List;

import java.util.ResourceBundle;

import java.util.logging.Logger;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.Legalizaciones;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.legalizacion.cDatos;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;

import javafx.application.Platform;

import javafx.beans.binding.Bindings;

import javafx.beans.property.ReadOnlyObjectWrapper;

import javafx.beans.property.SimpleStringProperty;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import javafx.concurrent.Service;

import javafx.concurrent.Task;

import javafx.event.ActionEvent;

import javafx.event.EventHandler;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.Cursor;

import javafx.scene.Node;

import javafx.scene.control.Alert;

import javafx.scene.control.Button;

import javafx.scene.control.ButtonBase;

import javafx.scene.control.ButtonType;

import javafx.scene.control.CheckBox;

import javafx.scene.control.Label;

import javafx.scene.control.Labeled;

import javafx.scene.control.TableCell;

import javafx.scene.control.TableColumn;

import javafx.scene.control.TableRow;

import javafx.scene.control.TableView;

import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.KeyCode;

import javafx.scene.input.KeyEvent;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;

import javafx.util.Callback;

public class AbrirLegalizacionController implements Initializable {

	private static final Logger logger = Logger.getLogger(AbrirLegalizacionController.class.getName());

	MenuPrincipalController parentController;

	private boolean primerClick = true;

	public void setParentController(MenuPrincipalController parentController) {

		this.parentController = parentController;

	}

	public MenuPrincipalController getParentController() {

		return parentController;

	}

	private ObservableList<cDatos> arrayLegalizaciones;

	@FXML

	private TableView<cDatos> lsvLegalizaciones;

	@FXML

	private TextField txtEjercicio = new TextField();

	@FXML

	TableColumn<cDatos, String> columna1;

	@FXML

	TableColumn<cDatos, String> columna2;

	@FXML

	TableColumn<cDatos, String> columna3;

	@FXML

	TableColumn<cDatos, String> columna4;

	@FXML

	private CheckBox chkNoEnviados;

	@FXML

	private CheckBox chkEnvioDirecto;

	@FXML

	private CheckBox chkEnvioPortal;

	@FXML

	private Button btnEliminar;

	@FXML

	private Button btnFiltrar;

	@FXML

	private Button btnNuevo;

	@FXML

	private Button btnSalir;

	@FXML

	private TextField txtNumeroLegalizaciones = new TextField();

	@FXML

	private AnchorPane frmAbrirLegalizacion;

	private Callback<TableColumn.CellDataFeatures<cDatos, String>, ObservableValue<String>> callbackCalculo = new Callback<>() {

		@Override

		public ObservableValue<String> call(TableColumn.CellDataFeatures<cDatos, String> param) {

			String valor = obtenerValorParaCelda(param.getValue());

			return new SimpleStringProperty(valor);

		}

	};

	@FXML

	private Label lblNumeroLegalizaciones;

	@FXML

	private Button runButton;

	@FXML

	private Button cancelButton;

	private static String obtenerInformacion() {

		// de proceso para obtener

		return "Informacion importante";

	}

	// para actualizar la interfaz de usuario con la

	private static void actualizarInterfazUsuario(String informacion) {

		// puedes actualizar la interfaz de usuario con la recibida

		System.out.println("Informacion recibida en el hilo de la aplicacion de JavaFX: " + informacion);

	}

	@Override

	public void initialize(URL location, ResourceBundle resources) {

		chkNoEnviados.setSelected(true);

		chkEnvioDirecto.setSelected(true);

		lsvLegalizaciones.setRowFactory(tv -> {

			TableRow<cDatos> row = new TableRow<>();

			row.setOnKeyPressed(event -> {

				logger.info("test");

			});

			row.setOnMouseClicked(event -> {

				if (event.getClickCount() == 2 && !row.isEmpty()) {

					cDatos rowData = row.getItem();

					try {

						abrir(null);

						Node source = (Node) event.getSource();

						Stage stage = (Stage) source.getScene().getWindow();

						stage.close();

					} catch (Exception e) {

						e.printStackTrace();

					}

				}

			});

			return row;

		});

		txtEjercicio.textProperty().addListener((observable, oldValue, newValue) -> {

			if (!newValue.matches("\\d{0,4}")) { // Solo permite hasta 4 caracteres que sean números

				txtEjercicio.setText(oldValue);

			}

		});

		columna1.setCellValueFactory(cellData -> {

			cDatos datos = cellData.getValue();

			String valor = primeraCelda(datos);

			if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

				return new javafx.beans.property.SimpleStringProperty(valor);

			} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

				String[] partes = valor.split("/");

				String ultimoDirectorio = partes[partes.length - 1];

				return new javafx.beans.property.SimpleStringProperty(ultimoDirectorio);

			} else {

				return new javafx.beans.property.SimpleStringProperty(valor);

			}

		});

		columna2.setCellValueFactory(new PropertyValueFactory<>("_Descripcion"));

		columna3.setCellValueFactory(cellData -> {

			String ejercicio = "";

			if (cellData.getValue().get_Ejercicio() > 0) {

				ejercicio = String.valueOf(cellData.getValue().get_Ejercicio());

			}

			return new javafx.beans.property.SimpleStringProperty(ejercicio);

		});

		columna4.setCellValueFactory(cellData -> {

			cDatos datos = cellData.getValue();

			String valor = obtenerValorParaCelda(datos);

			return new javafx.beans.property.SimpleStringProperty(valor);

		});

		cargarListaLegalizaciones();

	}

	@FXML

	public void primerClick() {

		if (primerClick) {

			LocalDate fechaActual = LocalDate.now();

			// Obtener el año de la fecha actual

			int anio = fechaActual.getYear() - 1;

			txtEjercicio.setText(String.valueOf(anio));

			// Cambiar el estado para indicar que ya se ha hecho clic una vez

			primerClick = false;

		}

	}

	@FXML

	public void enterPress_Tableview(KeyEvent event) {

		if (event.getCode() == KeyCode.ENTER) {

			cDatos rowData = lsvLegalizaciones.getSelectionModel().getSelectedItem();

			// Llamar a la función en el controlador

			try {

				abrir(null);

				Node source = (Node) event.getSource();

				Stage stage = (Stage) source.getScene().getWindow();

				stage.close();

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

	}

	@FXML

	private void cargarListaLegalizaciones() {

		try {

			Legalizaciones legalizaciones = new Legalizaciones();

			int ejercicio = Formato.ValorNulo(txtEjercicio.getText()) ? 0 : Integer.parseInt(txtEjercicio.getText());

			arrayLegalizaciones = FXCollections.observableArrayList();

			lsvLegalizaciones.getItems().clear();

			legalizaciones.cargaDirectorio(MGeneral.Configuracion.getPathDatos(), arrayLegalizaciones, ejercicio,

					chkNoEnviados.isSelected(), chkEnvioDirecto.isSelected(), chkEnvioPortal.isSelected());

			if (arrayLegalizaciones != null) {

				ObservableList<cDatos> data = FXCollections.observableArrayList();

				lsvLegalizaciones.setItems(arrayLegalizaciones);

			}

			ponNumeroLegalizaciones();

		} catch (Exception ex) {

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

			ex.printStackTrace();

		}

	}

	private String obtenerValorParaCelda(cDatos legalizacion) {

		StringBuilder cadestadoenvio = new StringBuilder();

		switch (legalizacion.getEstadoEnvio()) {

		case SinEnviar, EnviadaPorServicioErrorNoReintentable, EnviadaPorServicioErrorReintentable:

			return "";

		case EnviadaPorServicioCorrectamente:

			cadestadoenvio = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.EnvioDirecto);

			return cadestadoenvio.toString();

		case EnviadaPorPortal:

			cadestadoenvio = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.EnvioPortal);

			return cadestadoenvio.toString();

		default:

			return "Error";

		}

	}

	private String primeraCelda(cDatos legalizacion) {

		return legalizacion.getNombreDirectorio();

	}

	private void ponNumeroLegalizaciones() {

		if (arrayLegalizaciones != null) {

			String texto = arrayLegalizaciones.size() > 0 ? String.valueOf(arrayLegalizaciones.size()) : "";

			txtNumeroLegalizaciones.setText(texto);

		} else {

			txtNumeroLegalizaciones.setText("");

		}

	}

	@FXML

	public void salir(ActionEvent e) throws Exception {

		// Me cierra la ventana

		if (e != null) {

			Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click

			Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el

			// elemento

			stage.close();

		}

	}

	@FXML

	public void nuevo(ActionEvent e) throws Exception {

		getParentController().nuevaLegalizacion();

		salir(e);

		// poner el formulario

	}

	@FXML

	public void eliminar(ActionEvent e) throws Exception {

		try {

			// Verificar si hay elementos seleccionados en la lista de legalizaciones

			if (!lsvLegalizaciones.getSelectionModel().isEmpty()) {

				// Obtener el índice del elemento seleccionado

				int indiceSeleccionado = lsvLegalizaciones.getSelectionModel().getSelectedIndex();

				// Obtener la ruta del elemento seleccionado

				String ruta = arrayLegalizaciones.get(indiceSeleccionado).get_PathDatos();

				// Esto se debe sustituir con el mostrar mensaje de idiomaC

				Alert alerta = new Alert(Alert.AlertType.WARNING);

				alerta.setTitle("Aviso");

				alerta.setHeaderText(null);

				alerta.setContentText("¿Está seguro de que desea eliminar la legalización?");

				alerta.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

				// Esperar la respuesta del usuario

				ButtonType respuesta = alerta.showAndWait().orElse(null);

				// Si el usuario confirma la eliminación

				if (respuesta == ButtonType.OK) {

					// Hacer que el directorio sea escribible para eliminarlo

					Ficheros.DirectorioSoloLectura(ruta, false, true);

					// Eliminar el directorio y todos sus contenidos

					File directorio = new File(ruta);

					eliminarDirectorio(directorio);

					// Remover el elemento de la lista de legalizaciones y el correspondiente Array

					// lsvLegalizaciones.getItems().remove(indiceSeleccionado);

					arrayLegalizaciones.remove(indiceSeleccionado);

					cargarListaLegalizaciones();

					// Actualizar el contador de legalizaciones

				}

			} else {

				// Si no hay elementos seleccionados, mostrar un mensaje de error

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.SeleccionarElemento, "", "", "");

			}

		} catch (Exception ex) {

			// Manejar excepciones mostrando un mensaje de advertencia

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

			ex.printStackTrace();

		}

		// poner el formulario

	}

	public static void eliminarDirectorio(File directorio) {

		if (directorio.isDirectory()) {

			File[] archivos = directorio.listFiles();

			if (archivos != null) {

				for (File archivo : archivos) {

					eliminarDirectorio(archivo);

				}

			}

		}

		if (!directorio.delete()) {

			System.out.println("No se pudo eliminar el directorio: " + directorio);

		} else {

			System.out.println("Directorio eliminado: " + directorio);

		}

	}

	@FXML

	public void abrir(ActionEvent ev) throws Exception {

		try {

			if (!lsvLegalizaciones.getSelectionModel().isEmpty()) {

				// Obtener el índice del elemento seleccionado

				int indiceSeleccionado = lsvLegalizaciones.getSelectionModel().getSelectedIndex();

				// Crear una nueva instancia del formulario de entrada de datos

				// Me devuelve el elemento al que hice click

				getParentController().EntradaDatos(arrayLegalizaciones.get(indiceSeleccionado).get_PathDatos());

				// Cerrar el formulario actual

				salir(ev);

			}

		} catch (Exception ex) {

			// Manejar excepciones mostrando un mensaje

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

			ex.printStackTrace();

		}

		// poner el formulario

	}

	@FXML

	public void ejercicioFiltro(KeyEvent event) {

		if (event.getCode() == KeyCode.ENTER) {

			cargarListaLegalizaciones();

		}

	}

}
