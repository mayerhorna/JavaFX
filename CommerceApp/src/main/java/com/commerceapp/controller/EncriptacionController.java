package com.commerceapp.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import com.commerceapp.domain.ConfiguracionC;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.cEncriptacion;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.domain.idioma.ElementosIdiomaC;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.maestros.MaestroAlgoritmoEncriptacion;
import com.commerceapp.maestros.MaestroModoEncriptacion;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;

import ch.qos.logback.classic.pattern.Util;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EncriptacionController implements Initializable {
	private static final Logger logger = Logger.getLogger(EncriptacionController.class.getName());
	MenuPrincipalController parentController;
	cEncriptacion objcEncriptacion = new cEncriptacion();
	ConfiguracionC objConfiguracionC = new ConfiguracionC();
	private Stage stage;
	private Scene scene;
	private static File lastDirectory = null;

	public static enum EModoApertura {
		ENCRYPTAR_MANUAL(1), ENCRYPTAR_FICHERO_EN_LA_LEGALIZACION(2), DESENCRYPTAR_FICHERO_EN_LA_LEGALIZACION(3),
		DESENCRYPTAR_FICHERO_EN_DIRECTORIO_AUXILIAR(4), ENCRYPTAR_TODOS_LOS_LIBROS(5), CONSULTAR_VECTOR(6);

		private final int value;

		EModoApertura(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private EModoApertura _ModoApertura;
	String _Ficheroentrada;
	String _NombreOPrefijoNombreFicheroSalida;

	public void set_ModoApertura(EModoApertura pModoApertura) {
		this._ModoApertura = pModoApertura;
	}

	public void set_Ficheroentrada(String _Ficheroentrada) {
		this._Ficheroentrada = _Ficheroentrada;
	}

	public void set_NombreOPrefijoNombreFicheroSalida(String _NombreOPrefijoNombreFicheroSalida) {
		this._NombreOPrefijoNombreFicheroSalida = _NombreOPrefijoNombreFicheroSalida;
	}

	public void setParentController(MenuPrincipalController parentController) {
		this.parentController = parentController;
	}

	public MenuPrincipalController getParentController() {
		return parentController;
	}

	@FXML
	private Button btnBuscar;

	@FXML
	private TextField txtRutaFichero;

	@FXML
	private PasswordField txtpassClave;
	@FXML
	private PasswordField txtpassClaveConfirmacion;

	@FXML
	private TextField txtClave;

	@FXML
	private CheckBox chkMostrarClave;

	@FXML
	private ComboBox<String> cboAlgoritmoEncriptacion;

	@FXML
	private Label lblClaveConfirmacion;

	@FXML
	private AnchorPane frmEncriptacion;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (_ModoApertura != null) {
			cargaAlgoritmoEncriptacion();
			chkMostrarClave.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					chkMostrarClave_CheckedChanged(chkMostrarClave);
				}
			});
			sincronizarTextFieldsClave();
			controlesVisibles();
			compruebaInicial();
		}
	}

	private void sincronizarTextFieldsClave() {
		txtClave.addEventHandler(KeyEvent.KEY_RELEASED, this::sincronizarTextClave);
		txtpassClave.addEventHandler(KeyEvent.KEY_RELEASED, this::sincronizarTextClave);
	}

	@FXML
	public void btnSalir_Click(ActionEvent e) {

		close(e);

	}

	@FXML
	public void btnAceptar_Click(ActionEvent e) throws FileNotFoundException {
		try {
			if (comprueba()) {
				activar(false);
				switch (_ModoApertura) {

				case ENCRYPTAR_MANUAL:
					// _vProgreso.inicializa(100);
					encriptaFichero();
					MGeneral.RetornoFormulario = true;
					break;

				case ENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
					// _vProgreso.inicializa(100);
					MGeneral.RetornoFormulario = true;
					close(e);
					break;

				case DESENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:

					MGeneral.RetornoFormulario = true;
					close(e);

					break;

				case DESENCRYPTAR_FICHERO_EN_DIRECTORIO_AUXILIAR:
					desencriptaFichero();
					MGeneral.RetornoFormulario = true;
					close(e);

					break;

				case ENCRYPTAR_TODOS_LOS_LIBROS:

					MGeneral.RetornoFormulario = true;
					close(e);

					break;

				case CONSULTAR_VECTOR:

					MGeneral.RetornoFormulario = true;
					close(e);

					break;
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			activar(true);
		}

	}

	@FXML
	private void botonBuscarFichero(ActionEvent event) throws IOException {
		try {
			FileChooser fileChooser = new FileChooser();
			String filtros = kLegalizacion.dameCadenaExtensionesPermitidas(false);

			if (lastDirectory == null) {
				FileSystemView fileSystemView = FileSystemView.getFileSystemView();
				File desktopDir = fileSystemView.getHomeDirectory();
				fileChooser.setInitialDirectory(desktopDir);
			}

			if (lastDirectory != null && lastDirectory.exists()) {
				fileChooser.setInitialDirectory(lastDirectory);
			}
			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("win")) {
				fileChooser.getExtensionFilters()
						.addAll(new FileChooser.ExtensionFilter("Archivos permitidos", filtros));
			} else if (os.contains("nux")) {
				fileChooser.getExtensionFilters()
						.addAll(new FileChooser.ExtensionFilter("Archivos permitidos", filtros.replaceAll(";", ",")));

			}
			File selectedFile = fileChooser.showOpenDialog(null);
			if (selectedFile != null) {
				lastDirectory = selectedFile.getParentFile();
			}
			if (selectedFile != null) {

				if (compruebaFicheroAEncriptar(selectedFile.getAbsolutePath())) {
					txtRutaFichero.setText(selectedFile.getAbsolutePath());

				} else {
				}

			}

		} catch (Exception ex) {

			ex.printStackTrace();
		}

	}

	private boolean compruebaFicheroAEncriptar(String pathFicheroOrigen) {

		try {

			String extensionFichero;
			int indiceExtension;

			if (Formato.ValorNulo(pathFicheroOrigen)) {
				return false;
			}

			extensionFichero = new File(pathFicheroOrigen).getName();
			int lastDotIndex = extensionFichero.lastIndexOf(".");
			if (lastDotIndex >= 0) {
				extensionFichero = extensionFichero.substring(lastDotIndex + 1);
			}

			indiceExtension = kLegalizacion.dameIndiceDeExtensionFicheroSegunExtension(extensionFichero);


			if (indiceExtension == -1) {

				MGeneral.Idioma.MostrarMensaje(EnumMensajes.ExtensionFicheroNoAdmitida, "", "", "");
				return false;
			}

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private void cargaAlgoritmoEncriptacion() {
		MaestroAlgoritmoEncriptacion mMAE = new MaestroAlgoritmoEncriptacion();

		// Obtener lista de algoritmos
		ObservableList<MaestroAlgoritmoEncriptacion> listaMaestro = FXCollections
				.observableArrayList(mMAE.obtenerListaMaestro());

		for (int i = 0; i < listaMaestro.size(); i++) {
			cboAlgoritmoEncriptacion.getItems().add(listaMaestro.get(i).getDescripcion().toString());
		}
		cboAlgoritmoEncriptacion.setValue(mMAE.getAlgoritmoPorDefecto().toString());
	}

	public boolean desencriptaFichero() {
		try {
			StringBuilder cadError = new StringBuilder();
			String ficheroEntrada = "";
			String directorioSalida = "";
			String nombreFicheroSalida = "";

			String[] DatosFichero = calcularFicherosEntradaSalida(ficheroEntrada, directorioSalida,
					nombreFicheroSalida);

			if (_ModoApertura == null) {

				return false;
			}

			switch (_ModoApertura) {

			case DESENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
				// Añade la lógica correspondiente para DESENCRYPTAR_FICHERO_EN_LA_LEGALIZACION
				break;

			case DESENCRYPTAR_FICHERO_EN_DIRECTORIO_AUXILIAR:

				if (!objcEncriptacion.desencriptarFichero(DatosFichero[0], DatosFichero[1], DatosFichero[2],
						txtpassClave.getText(), cadError, null)) {
					IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.ErrorAlDesencriptar, "", "", "");
					return false;
				}
				break;
			default:
				break;

			}

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
			return false;
		}
	}

	public boolean encriptaFichero() {
		try {
			StringBuilder cadError = new StringBuilder();
			String ficheroEntrada = "";
			String directorioSalida = "";
			String nombreFicheroSalida = "";

			String[] DatosFichero = calcularFicherosEntradaSalida(ficheroEntrada, directorioSalida,
					nombreFicheroSalida);

			if (_ModoApertura == null) {

				return false;
			}

			switch (_ModoApertura) {

			case ENCRYPTAR_MANUAL:
				// _vProgreso.inicializa(100);
				if (!objcEncriptacion.encriptarFichero(DatosFichero[0], DatosFichero[1], DatosFichero[2],
						txtpassClave.getText(), cboAlgoritmoEncriptacion.getValue().toString(),
						MaestroModoEncriptacion.kModoPorDefecto.toString(), cadError, null, 0, 0)) {

					IdiomaC.MostrarMensaje(EnumMensajes.ErrorAlEncriptar, cadError.toString(), "", "");

					return false;
				} else {
					mostrarResultado();
					txtRutaFichero.setText("");
				}
				break;

			case ENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
				// _vProgreso.inicializa(100);
				if (!objcEncriptacion.encriptarFichero(ficheroEntrada, directorioSalida, nombreFicheroSalida, "",
						cboAlgoritmoEncriptacion.getValue().toString(), "", cadError, null, 0, 0)) {

					IdiomaC.MostrarMensaje(EnumMensajes.ErrorAlEncriptar, cadError.toString(), "", "");

					return false;
				}
				break;

			case DESENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
				// Añade la lógica correspondiente para DESENCRYPTAR_FICHERO_EN_LA_LEGALIZACION
				break;

			case DESENCRYPTAR_FICHERO_EN_DIRECTORIO_AUXILIAR:
				// Añade la lógica correspondiente para
				// DESENCRYPTAR_FICHERO_EN_DIRECTORIO_AUXILIAR
				break;

			case ENCRYPTAR_TODOS_LOS_LIBROS:
				// Añade la lógica correspondiente para ENCRYPTAR_TODOS_LOS_LIBROS
				break;

			case CONSULTAR_VECTOR:
				// Añade la lógica correspondiente para CONSULTAR_VECTOR
				break;
			}

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
			return false;
		}
	}

	private String[] calcularFicherosEntradaSalida(String ficheroEntrada, String directorioSalida,
			String nombreFicheroSalida) {
		switch (_ModoApertura) {
		case ENCRYPTAR_MANUAL:

			ficheroEntrada = txtRutaFichero.getText();

			directorioSalida = new File(ficheroEntrada).getParent();

			nombreFicheroSalida = new File(ficheroEntrada).getName();

			String[] DatosFicheroEntradaSalida = { ficheroEntrada, directorioSalida, nombreFicheroSalida };

			return DatosFicheroEntradaSalida;

		case ENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
			// Fichero de entrada el recibido en la instanciación del formulario
			// ficheroEntrada = _FicheroEntrada;
			// Directorio de salida es el path de la legalización actual
			// directorioSalida = mlform.getPathDatos();
			// El nombre del fichero de salida se ha recibido en la instanciación del
			// formulario
			// nombreFicheroSalida = _NombreOPrefijoNombreFicheroSalida;
			break;

		case DESENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
			// Fichero de entrada el recibido en la instanciación del formulario
			// ficheroEntrada[0] = _FicheroEntrada;
			// Directorio de salida es el path de la legalización actual
			// directorioSalida[0] = mlform.getPathDatos();
			// Se ha podido recibir en la instanciación un prefijo de nombre del fichero de
			// salida
			// Ejemplo: si se ha recibido un nombre de fichero de entrada
			// "Diario.Pdf.ECB.AES128" y un prefijo nombre "_aux"
			// el nombre del fichero de salida será "_aux_Diario.Pdf"
			// nombreFicheroSalida[0] = _NombreOPrefijoNombreFicheroSalida;
			break;

		case DESENCRYPTAR_FICHERO_EN_DIRECTORIO_AUXILIAR:
			ficheroEntrada = txtRutaFichero.getText();

			directorioSalida = ConfiguracionC.getPathAuxiliar();

			nombreFicheroSalida = "";

			String[] DatosFicheroEntradaSalidaDirAux = { ficheroEntrada, directorioSalida, nombreFicheroSalida };

			return DatosFicheroEntradaSalidaDirAux;

		case CONSULTAR_VECTOR:
			break;
		case ENCRYPTAR_TODOS_LOS_LIBROS:
			break;
		default:
			break;

		}
		return null;
	}

	private boolean comprueba() throws FileNotFoundException {

		boolean comprobarFicheroEntrada = true;
		boolean comprobarClave = true;
		boolean comprobarClaveRepeticion = true;
		boolean comprobarAlgoritmoEncriptacion = true;
		boolean comprobarModoEncriptacion = true;
		boolean comprobarFicheroSalida = true;

		StringBuilder cadError = new StringBuilder();

		String ficheroEntrada = "";
		String directorioSalida = "";
		String nombreFicheroSalida = "";
		String ficheroSalida;
		String extensionesEncriptacion;

		switch (_ModoApertura) {
		case ENCRYPTAR_MANUAL:
			break;
		case ENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
			break;
		case DESENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
			break;
		case DESENCRYPTAR_FICHERO_EN_DIRECTORIO_AUXILIAR:
			break;
		case ENCRYPTAR_TODOS_LOS_LIBROS:
			comprobarFicheroEntrada = false;
			comprobarFicheroSalida = false;
			break;
		case CONSULTAR_VECTOR:
			comprobarFicheroEntrada = false;
			comprobarFicheroSalida = false;
			break;
		default:

			break;
		}
		if (comprobarFicheroEntrada) {
			if (Formato.ValorNulo(txtRutaFichero.getText())) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, "Fichero",
						ButtonType.OK.toString(), IdiomaC.obtenerValor(ObjetosIdioma.FORMULARIOS,
								frmEncriptacion.toString(), ElementosIdiomaC.TEXT_CONTROLES, "", ""));
				btnBuscar.requestFocus();
				return false;
			}

			ficheroEntrada = txtRutaFichero.getText();
			if (!(Ficheros.FicheroExiste(ficheroEntrada) == Ficheros.ExistFicheroEnum.Existe)) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FicheroInexistente,
						Alert.AlertType.ERROR.toString(), ButtonType.OK.toString(), ficheroEntrada);
				return false;
			}
			if (_ModoApertura == EModoApertura.ENCRYPTAR_MANUAL) {

				if (compruebaFicheroAEncriptar(txtRutaFichero.getText()) == false) {
					return false;
				}

			}
		}
		if (comprobarAlgoritmoEncriptacion) {
			if (Formato.ValorNulo(cboAlgoritmoEncriptacion.getValue())) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, "", "", "");
				return false;
			}
		}

		if (comprobarModoEncriptacion) {
			/*
			 * if (Formato.ValorNulo(cboModoEncriptacion.getText())) { // Adaptar según sea
			 * necesario
			 * 
			 * Idioma.MostrarMensaje(IdiomaC.enumMensajes.CampoObligatorio,
			 * MessageBoxIcon.Exclamation, MessageBoxButtons.OK,
			 * Idioma.ObtenerValor(ObjetosIdioma.Formularios, this.getName(),
			 * ElementosIdiomaC.TextControles, lblModoEncriptacion.getName()));
			 * cboModoEncriptacion.requestFocus(); return false; }
			 */
		}

		if (comprobarClave) {
			if (Formato.ValorNulo(txtClave.getText())) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, "Clave", ButtonType.OK.toString(),
						IdiomaC.obtenerValor(ObjetosIdioma.FORMULARIOS, frmEncriptacion.toString(),
								ElementosIdiomaC.TEXT_CONTROLES, "", ""));
				return false;
			}

			if (!objcEncriptacion.tamanioClaveValido(txtpassClave.getText(), cboAlgoritmoEncriptacion.getValue())) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ErrorTamanioClaveEncriptacion,
						objcEncriptacion.getAlgoritmo().getDescripcion(),
						String.valueOf(objcEncriptacion.getAlgoritmo().getTamanioMinimoKey()),
						String.valueOf(objcEncriptacion.getAlgoritmo().getTamanioKey()));
				return false;
			}

			if (!objcEncriptacion.reglasClaveValidas(txtpassClave.getText())) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ErrorReglasClaveEncriptacion, "", "", "");
				return false;
			}
		}

		if (comprobarClaveRepeticion) {
			// Si se ha marcado chkMostrarClave no se comprueba la repetición de la clave
			if (!chkMostrarClave.isSelected()) {
				if (Formato.ValorNulo(txtpassClaveConfirmacion.getText())) {

					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, "", "", "");
					txtpassClaveConfirmacion.requestFocus();
					return false;
					// Adaptar según sea necesario
					/*
					 * Idioma.MostrarMensaje(IdiomaC.enumMensajes.CampoObligatorio,
					 * MessageBoxIcon.Exclamation, MessageBoxButtons.OK,
					 * Idioma.ObtenerValor(ObjetosIdioma.Formularios, this.getName(),
					 * ElementosIdiomaC.TextControles, lblClaveConfirmacion.getName()));
					 * txtClaveConfirmacion.requestFocus(); return false;
					 */
				}
				if (!txtpassClaveConfirmacion.getText().equals(txtpassClave.getText())) {

					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ClaveEncriptacionyConfirmacionDistintas, "", "",
							"");
					txtpassClaveConfirmacion.requestFocus();

					return false;
					// Adaptar según sea necesario
					/*
					 * Idioma.MostrarMensaje(IdiomaC.enumMensajes.
					 * ClaveEncriptacionyConfirmacionDistintas, MessageBoxIcon.Exclamation,
					 * MessageBoxButtons.OK); txtClaveConfirmacion.requestFocus(); return false;
					 */
				}
			}
		}

		/*
		 * if (comprobarFicheroSalida) { calcularFicherosEntradaSalida(ficheroEntrada,
		 * directorioSalida, nombreFicheroSalida);
		 * 
		 * extensionesEncriptacion =
		 * objcEncriptacion.obtenerExtensionesEncriptacion(cboAlgoritmoEncriptacion.
		 * getValue(), cboAlgoritmoEncriptacion.getText(), cadError); if
		 * (Util.Formato.ValorNulo(extensionesEncriptacion)) { // Adaptar según sea
		 * necesario return false; }
		 * 
		 * ficheroSalida = Paths.get(directorioSalida, nombreFicheroSalida +
		 * extensionesEncriptacion).toString(); if
		 * (Ficheros.FicheroExiste(ficheroSalida) == Ficheros.ExistFicheroEnum.Existe) {
		 * 
		 * } if (Ficheros.FicheroBorra(ficheroSalida)) { // Adaptar según sea necesario
		 * 
		 * Idioma.MostrarMensaje(IdiomaC.enumMensajes.ErrorAlBorrarFichero,
		 * MessageBoxIcon.Error, MessageBoxButtons.OK, ficheroSalida); return false;
		 * 
		 * } }
		 */

		return true;

	}

	private void chkMostrarClave_CheckedChanged(CheckBox chkMostrarClave) {
		if (chkMostrarClave.isSelected()) {
			lblClaveConfirmacion.setVisible(false);
			txtpassClaveConfirmacion.setVisible(false);

			txtpassClave.setVisible(false);
			txtClave.setVisible(true);

			// txtClave.setDisable(false);
			// txtpassClave.setManaged(true);
			// txtClave.setManaged(true);
		} else {
			lblClaveConfirmacion.setVisible(true);

			txtClave.setVisible(false);
			txtpassClaveConfirmacion.setVisible(true);
			txtpassClave.setVisible(true);
			// txtpassClave.setManaged(false);
			// txtClave.setManaged(false);

			// txtClave.setDisable(true);
		}
	}

	public void sincronizarTextClave(KeyEvent event) {

		if (event.getSource() == txtClave) {

			txtpassClave.setText(txtClave.getText());

		} else if (event.getSource() == txtpassClave) {

			txtClave.setText(txtpassClave.getText());

		}
	}

	private void mostrarResultado() {
		try {
			String cad = objcEncriptacion.obtenerResultadoEncriptacion();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EncriptacionResultado.fxml"));
			Parent EncriptacionResultado = loader.load(); // Load antes de configurar los parámetros

			EncriptacionResultadoController encriptacionResultadoController = loader.getController();

			switch (_ModoApertura) {
			case ENCRYPTAR_MANUAL:
				encriptacionResultadoController
						.set_ModoApertura(EncriptacionResultadoController.EModoApertura.EncriptadoManual);
				encriptacionResultadoController.set_FicheroSalida(objcEncriptacion.getFicheroSalida());
				encriptacionResultadoController.set_TextoResultado(cad);
				encriptacionResultadoController.initialize(null, null);
				break;
			case CONSULTAR_VECTOR:
				// Configurar parámetros para el caso CONSULTAR_VECTOR si es necesario
				break;
			default:
				break;
			}

			stage = new Stage();
			scene = new Scene(EncriptacionResultado);

			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UTILITY);

			stage.getIcons().clear();
			stage.setScene(scene);

			stage.showAndWait();

		} catch (Exception ex) {
			ex.printStackTrace();
			// Manejar la excepción de manera adecuada si es necesario
		}
	}

	private void activar(boolean activar) {

		// pnlContenedor.setDisable(!activar);

		frmEncriptacion.setDisable(!activar);

		Platform.runLater(() -> {
			frmEncriptacion.setDisable(!activar);
			Utilidades.cursorEspera((Stage) frmEncriptacion.getScene().getWindow(), !activar);
		});

	}

	private void close(ActionEvent e) {

		Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
		Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el elemento
		stage.close();
	}

	public boolean compruebaInicial() {
		try {
			switch (_ModoApertura) {
			case ENCRYPTAR_MANUAL:
			case ENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
			case ENCRYPTAR_TODOS_LOS_LIBROS:
			case CONSULTAR_VECTOR:
				// No hay acciones específicas para estos casos
				break;

			case DESENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
				/*
				 * if (!mlform.FicheroEncriptadoConLegalia(_FicheroEntrada)) {
				 * Idioma.MostrarMensaje(IdiomaC.enumMensajes.NoEncriptadoConLegalia,
				 * MessageBoxIcon.Error, MessageBoxButtons.OK); return false; }
				 * 
				 * Encriptacion.ObtenerDatosEncriptacionSegunNombreFichero(_FicheroEntrada, "");
				 * 
				 * cboAlgoritmoEncriptacion.setValue(Encriptacion.Algoritmo.Codigo);
				 * cboModoEncriptacion.setValue(Encriptacion.Modo.Codigo);
				 */
				break;

			case DESENCRYPTAR_FICHERO_EN_DIRECTORIO_AUXILIAR:
				/*
				 * if (!LegalizacionService.FicheroEncriptadoConLegalia(_Ficheroentrada)) {
				 * IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.NoEncriptadoConLegalia, "", "",
				 * ""); return false; }
				 */

				String fichero;
				StringBuilder nombrefichero = new StringBuilder();

				objcEncriptacion.obtenerDatosEncriptacionSegunNombreFichero(_Ficheroentrada, nombrefichero);

				fichero = new File(ConfiguracionC.getPathAuxiliar(), nombrefichero.toString()).getPath();

				if (!Ficheros.FicheroBorra(fichero)) {
					IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.ErrorAlBorrarFichero, "", "", fichero);
					return false;
				}

				cboAlgoritmoEncriptacion.setValue(objcEncriptacion.getAlgoritmo().getDescripcion().toString());
				// cboModoEncriptacion.setValue(Encriptacion.Modo.Codigo);
				break;
			}

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "", "", ex.getMessage());
			return false;
		}
	}

	private void controlesVisibles() {
		/*
		 * lblFicheroEncriptar.setVisible(false);
		 * lblFicheroDesencriptar.setVisible(false);
		 */

		switch (_ModoApertura) {
		case ENCRYPTAR_MANUAL:
			/*
			 * lblEncriptacion.setText("Encriptación");
			 * lblFicheroEncriptar.setVisible(true);
			 */
			btnBuscar.setVisible(true);
			break;

		case ENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
			/*
			 * lblEncriptacion.setText("Encriptación");
			 * lblFicheroEncriptar.setVisible(true); txtFichero.setText(_FicheroEntrada);
			 * btnBuscar.setVisible(false);
			 */
			break;

		case DESENCRYPTAR_FICHERO_EN_LA_LEGALIZACION:
			/*
			 * lblDesencriptacion.setText("Desencriptación");
			 * lblFicheroDesencriptar.setVisible(true); txtFichero.setText(_FicheroEntrada);
			 * btnBuscar.setVisible(false); cboAlgoritmoEncriptacion.setDisable(true);
			 * cboModoEncriptacion.setDisable(true);
			 */
			break;

		case DESENCRYPTAR_FICHERO_EN_DIRECTORIO_AUXILIAR:
			/*
			 * lblDesencriptacion.setText("Desencriptación");
			 * lblFicheroDesencriptar.setVisible(true);
			 */
			txtRutaFichero.setText(_Ficheroentrada);
			btnBuscar.setVisible(false);
			cboAlgoritmoEncriptacion.setDisable(true);
			// cboModoEncriptacion.setDisable(true);
			break;

		case ENCRYPTAR_TODOS_LOS_LIBROS:
			/*
			 * lblEncriptacion.setText("Encriptación");
			 * lblFicheroEncriptar.setVisible(true);
			 * txtFichero.setText("Encriptar Todos Los Libros");
			 */
			btnBuscar.setVisible(false);
			break;

		case CONSULTAR_VECTOR:
			/*
			 * lblConsultaVector.setText("Consulta Vector");
			 * txtFichero.setText("Consulta Vector"); btnBuscar.setVisible(false);
			 */
			break;
		}
	}

}
