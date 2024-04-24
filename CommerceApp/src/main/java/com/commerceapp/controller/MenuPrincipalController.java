package com.commerceapp.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.commerceapp.controller.EntradaDatosController.EnumActivacionIconos;
import com.commerceapp.controller.EntradaDatosController.EnumTipoOperacion;
import com.commerceapp.domain.ConfiguracionC;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.IdiomaC.AyudaUtils;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.domain.idioma.ElementosIdiomaC;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.reporting.instancia.ReportingPreviewService;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.util.Utilidades;

import jakarta.annotation.Resources;
import javafx.scene.input.KeyCode;

import javafx.scene.input.KeyEvent;

@Component
public class MenuPrincipalController implements Initializable {

	LegalizacionService legalizacion = new LegalizacionService();
	EntradaDatosController form;
	private boolean esPrimeraCargaMDI = true;

	private Stage stage;
	private Scene scene;
	private Stage stagePrincipal;

	public Stage getStagePrincipal() {
		return stagePrincipal;
	}

	public void setStagePrincipal(Stage stagePrincipal) {
		this.stagePrincipal = stagePrincipal;
	}

	private static final Logger logger = Logger.getLogger(MenuPrincipalController.class.getName());
	@FXML
	HBox frmEntradaDatos;

	@FXML
	private MenuItem AcercaDeToolStripMenuItem;

	@FXML
	private AnchorPane AnchorPane1;

	@FXML
	private AnchorPane AnchorPane2;

	@FXML
	public AnchorPane AnchorPane3;

	@FXML
	private AnchorPane Anchorpane4;

	@FXML
	private Menu AyudaMenu;

	@FXML
	private MenuItem AyudaToolStripMenuItem;

	@FXML
	public Button ComprobarReglasToolStrip;

	@FXML
	public MenuItem ConfiguracionToolStripMenuItem;

	@FXML
	public MenuItem DatosLegalizacionStripMenuItem;

	@FXML
	private Menu EdicionMenu;

	@FXML
	private Menu EncriptacionMenu;

	@FXML
	public MenuItem EncriptarOtrosFicherosToolStripMenuItem;

	@FXML
	public Button EnviarToolStrip;

	@FXML
	public Button EspecificarLibrosToolStripButton;

	@FXML
	public MenuItem EspecificarLibrosToolStripMenuItem;

	@FXML
	private Menu FormularioMenu;

	@FXML
	private ImageView GenerarHuellasZipEnvio;

	@FXML
	public Button GenerarZipToolStrip;

	@FXML
	public Button GuardarToolStrip;

	@FXML
	public Button ImportarToolStripButton;

	@FXML
	public MenuBar MenuPrincipal;

	@FXML
	public Button NuevoToolStrip;

	@FXML
	public Button OpenToolStripButton;

	@FXML
	private CheckMenuItem StatusBarToolStripMenuItem;

	@FXML
	public ProgressBar StatusProgressBar;

	@FXML
	public MenuItem SubItemAbrir;

	@FXML
	public MenuItem SubItemCerrar;

	@FXML
	public MenuItem SubItemComprobarReglas;

	@FXML
	public MenuItem SubItemEnviar;

	@FXML
	public MenuItem SubItemGenerarZip;

	@FXML
	public MenuItem SubItemGuardar;

	@FXML
	public MenuItem SubItemImportar;

	@FXML
	public MenuItem SubItemImprimir;

	@FXML
	public MenuItem SubItemNuevo;

	@FXML
	private MenuItem SubItemRecepcion;

	@FXML
	private MenuItem SubItemSalir;

	@FXML
	private ToolBar ToolBarMenu;

	@FXML
	private CheckMenuItem ToolBarToolStripMenuItem;

	@FXML
	public Button ToolStripButtonImprimir;

	@FXML
	private VBox Vbox;

	@FXML
	public MenuItem VerHuellasDeLosLibrosToolStripMenuItem;

	@FXML
	private Menu VerMenu;

	@FXML
	private ImageView btnComprobar;

	@FXML
	private ImageView btnEspecificar;

	@FXML
	private ImageView btnGenerarHuelllasZip;

	@FXML
	private ImageView btnGenerarInstancia;

	@FXML
	private ImageView btnImportar;

	@FXML
	private ImageView btnNuevo;

	@FXML
	private ImageView iconGuardar;

	@FXML
	private AnchorPane frmMDIPrincipal;
	public double anchoLogo;
	public double altoLogo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("Iniciando menu");

	}

	@FXML
	private void abrirConfiguracion(ActionEvent event) throws IOException {
		if (form != null) {
			form.eliminarListener();
		}

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Configuracion.fxml"));
		Parent root = fxmlLoader.load();

		ConfiguracionController configuracionController = fxmlLoader.getController();
		configuracionController.setParentController(this);

		Stage stage = new Stage();

		Scene scene = new Scene(root);

		// quitando el maximizar y minimizar
		stage.initStyle(StageStyle.UTILITY);
		// quitando iconos
		stage.getIcons().clear();
		// bloquea la interacción con otras ventanas de la aplicación
		stage.initModality(Modality.APPLICATION_MODAL);

		stage.setScene(scene);
		MGeneral.Idioma.cargarIdiomaControles(stage, null);
		// stage.setTitle("Configuracion");
		stage.showAndWait();
		if (form != null) {
			form.aniadirListener();
		}
	}

	@FXML
	private void Ayuda(ActionEvent event) throws IOException {

		try {
			if (form != null) {
				form.eliminarListener();
			}
			ayuda();
			if (form != null) {
				form.aniadirListener();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@FXML
	private void ocultarMostrarBarraHerramientas(ActionEvent event) throws IOException {

		if (ToolBarToolStripMenuItem.isSelected()) {

			ToolBarMenu.setVisible(true);
			ToolBarMenu.setManaged(true);
		} else {

			ToolBarMenu.setVisible(false);
			ToolBarMenu.setManaged(false);

		}
	}

	@FXML
	private void ocultarMostrarEstado(ActionEvent event) throws IOException {

		if (StatusBarToolStripMenuItem.isSelected()) {

			Anchorpane4.setVisible(true);
			Anchorpane4.setManaged(true);
		} else {

			Anchorpane4.setVisible(false);
			Anchorpane4.setManaged(false);
		}
	}

	@FXML
	public void keyPressed(KeyEvent event) throws IOException {
		if (event.isControlDown() && event.getCode() == KeyCode.F1) {

			String ayudaPdf = MGeneral.Idioma.new AyudaUtils().obtenerFicheroAyuda();

			Utilidades.ProccessStarURL(ayudaPdf);

		}
	}

	@FXML
	private void importarLegalizacion(ActionEvent event) throws IOException {
		if (form != null) {

			if (!form.cerrar()) {
				form.eliminarListener();
				return;
			}

		}
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ImportarLegalizacion.fxml"));
		Parent root = fxmlLoader.load();

		ImportarLegalizacionController importarLegalizacionController = fxmlLoader.getController();
		importarLegalizacionController.setParentController(this);

		Stage stage = new Stage();

		Scene scene = new Scene(root);

		stage.initStyle(StageStyle.UTILITY);

		stage.getIcons().clear();

		stage.initModality(Modality.APPLICATION_MODAL);

		stage.setScene(scene);
		MGeneral.Idioma.cargarIdiomaControles(stage, null);

		stage.showAndWait();

	}

	@FXML
	public void nuevaLegalizacion() throws Exception {
		if (form != null) {

			form.eliminarListener();
			if (!form.cerrar()) {
				return;
			}

		}

		cerrar(null);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaLegalizacion.fxml"));
		Parent nuevaLegalizacion = loader.load();

		NuevaLegalizacionController nuevaLegalizacionController = loader.getController();
		nuevaLegalizacionController.setParentController(this);

		stage = new Stage();
		scene = new Scene(nuevaLegalizacion);

		// quitando el maximizar y minimizar
		stage.initModality(Modality.APPLICATION_MODAL);
		// bloquea la interacción con otras ventanas de la aplicación
		stage.initStyle(StageStyle.UTILITY);
		// quitando iconos
		stage.getIcons().clear();
		stage.setScene(scene);
		MGeneral.Idioma.cargarIdiomaControles(stage, null);
		stage.showAndWait();

	}

	@FXML
	private void datosLegalizacion() {
		try {
			if (form != null) {
				form.eliminarListener();
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DatosLegalizacion.fxml"));
			Parent datosLegalizacion = loader.load();

			DatosLegalizacionController datosLegalizacionController = loader.getController();
			datosLegalizacionController.setParentController(this);

			stage = new Stage();
			scene = new Scene(datosLegalizacion);

			// quitando el maximizar y minimizar
			stage.initModality(Modality.APPLICATION_MODAL);
			// bloquea la interacción con otras ventanas de la aplicación
			stage.initStyle(StageStyle.UTILITY);
			// quitando iconos
			stage.getIcons().clear();
			stage.setScene(scene);
			MGeneral.Idioma.cargarIdiomaControles(stage, null);

			stage.showAndWait();
			if (form != null) {
				form.aniadirListener();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void abrirLegalizacion() throws Exception {
		if (form != null) {

			if (!form.cerrar()) {
				return;
			}
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AbrirLegalizacion.fxml"));
		Parent abrirLegalizacion = loader.load();

		AbrirLegalizacionController abrirLegalizacionController = loader.getController();
		abrirLegalizacionController.setParentController(this);

		stage = new Stage();
		scene = new Scene(abrirLegalizacion);

		// quitando el maximizar y minimizar
		stage.initModality(Modality.APPLICATION_MODAL);
		// bloquea la interacción con otras ventanas de la aplicación
		stage.initStyle(StageStyle.UTILITY);
		// quitando iconos
		stage.getIcons().clear();
		stage.setScene(scene);
		MGeneral.Idioma.cargarIdiomaControles(stage, null);
		stage.showAndWait();
	}

	@FXML
	public void encriptacionFichero() throws Exception {
		if (form != null) {
			form.eliminarListener();
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Encriptacion.fxml"));
		Parent abrirEncriptacion = loader.load();

		EncriptacionController encriptacionController = loader.getController();
		encriptacionController.set_ModoApertura(EncriptacionController.EModoApertura.ENCRYPTAR_MANUAL);
		encriptacionController.setParentController(this);

		encriptacionController.initialize(null, null);

		stage = new Stage();
		scene = new Scene(abrirEncriptacion);

		// quitando el maximizar y minimizar
		stage.initModality(Modality.APPLICATION_MODAL);
		// bloquea la interacción con otras ventanas de la aplicación
		stage.initStyle(StageStyle.UTILITY);
		// quitando iconos
		stage.getIcons().clear();
		stage.setScene(scene);
		MGeneral.Idioma.cargarIdiomaControles(stage, null);
		stage.showAndWait();
		if (form != null) {
			form.aniadirListener();
		}
	}

	private void verHuellasLibros() {
		if (form != null) {
		}
		form.operacion(EnumTipoOperacion.GenerarHuellas);

	}

	@FXML
	public void VerHuella() throws Exception {
		// form.eliminarListener();
		verHuellasLibros();
		// form.aniadirListener();
	}

	@FXML
	public void Recepcion() {

		if (form != null) {

			if (!form.cerrar()) {
				form.eliminarListener();
				return;
			}

		}

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RecepcionAbrir.fxml"));
			Parent recepcionAbrir = loader.load();
			RecepcionAbrirController recepcionController = loader.getController();
			recepcionController.setParentController(this);
			stage = new Stage();
			scene = new Scene(recepcionAbrir);

			// quitando el maximizar y minimizar
			stage.initModality(Modality.APPLICATION_MODAL);
			// bloquea la interacción con otras ventanas de la aplicación
			stage.initStyle(StageStyle.UTILITY);
			// quitando iconos
			stage.getIcons().clear();
			stage.setScene(scene);
			MGeneral.Idioma.cargarIdiomaControles(stage, null);
			stage.showAndWait();

			if (form != null) {
				form.aniadirListener();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@FXML
	public void AcercaDe() {

		try {
			if (form != null) {
				form.eliminarListener();
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AcercaDe.fxml"));
			Parent abrirAcercaDe = loader.load();
			AcercaDeController objAcerController = loader.getController();
			stage = new Stage();
			scene = new Scene(abrirAcercaDe);

			// quitando el maximizar y minimizar
			stage.initModality(Modality.APPLICATION_MODAL);
			// bloquea la interacción con otras ventanas de la aplicación
			stage.initStyle(StageStyle.UTILITY);
			// quitando iconos
			stage.getIcons().clear();
			stage.setScene(scene);
			MGeneral.Idioma.cargarIdiomaControles(stage, null);
			objAcerController.initialize(null, null);
			stage.showAndWait();

			if (form != null) {
				form.aniadirListener();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void EntradaDatos(String path) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EntradaDatos.fxml"));

		try {
			frmEntradaDatos = loader.load();
			form = loader.getController();
			form.setParentController(this);
			form.abrir(path);
			AnchorPane3.getChildren().add(frmEntradaDatos);
			AnchorPane.setTopAnchor(frmEntradaDatos, 0.0);
			AnchorPane.setBottomAnchor(frmEntradaDatos, 0.0);
			AnchorPane.setLeftAnchor(frmEntradaDatos, 0.0);
			AnchorPane.setRightAnchor(frmEntradaDatos, 0.0);

			MGeneral.Idioma.cargarIdiomaControles(null, frmEntradaDatos);

			establecerTitulo();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void cargaLibros() {
		form.cargaLibros();
	}

	@FXML
	private void guardar(ActionEvent event) {

		form.guardar(false, true);
	}

	@FXML
	public void cerrar(ActionEvent event) {
		// activacionIconosBarra(EnumActivacionIconos.NoHayLegalizacionCargada);
		// activacionIconosGuardado(true);

		if (form != null) {
			form.cerrar();
		}

	}

	public void ayuda() throws IOException, SAXException, ParserConfigurationException {

		String fichero = MGeneral.Idioma.new AyudaUtils().obtenerFicheroAyuda();

		File archivo = new File(fichero);
		if (!archivo.exists()) {
			MGeneral.Idioma.MostrarMensaje(EnumMensajes.FicheroInexistente, "", "", "");
			return;
		}

		Utilidades.ProccessStarURL(fichero);
	}

	@FXML
	public void generarZIP() {
		form.operacion(EnumTipoOperacion.GenerarZip);
	}

	public void establecerTitulo() {

		try {
			if (MGeneral.mlform == null)
				return;

			String cadtit = MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,
					stagePrincipal.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_FORMULARIO, "", "");
			StringBuilder cadaux;

			if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.Recepcion) {
				cadaux = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.Recepcion);
				if (cadaux.isEmpty())
					cadaux.append("Recepción");
				cadtit = cadaux + " " + cadtit;

			}

			String cad = cadtit + " - " + MGeneral.mlform.Datos.getNombreDirectorio();
			cad = cad + " - " + MGeneral.mlform.Datos.get_Descripcion();

			if (MGeneral.mlform.Datos.get_Ejercicio() != 0) {
				cad = cad + " - " + MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.Ejercicio) + " "
						+ MGeneral.mlform.Datos.get_Ejercicio();
			}

			if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloLectura) {
				cadaux = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.SoloLectura);
				cad = cad + " " + cadaux;
			}

			if (MGeneral.mlform.getModo() == LegalizacionService.EnumModo.SoloReenviar) {
				cadaux = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.SoloReenviar);
				cad = cad + " " + cadaux;
			}

			stagePrincipal.setTitle(cad);

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	@FXML
	public void validar() {
		form.eliminarListener();
		form.validar(true);
	}

	public void activacionIconosGuardado(boolean activacion) {
		GuardarToolStrip.setDisable(!activacion);
		SubItemGuardar.setDisable(!activacion);
	}

	public void pinta() {

		if (esPrimeraCargaMDI) {
			esPrimeraCargaMDI = false;

			Utilidades.cursorEspera(stagePrincipal, true);
			MGeneral.Configuracion.actualizarVersion();
			Utilidades.cursorEspera(stagePrincipal, false);

			if (!MGeneral.Configuracion.aceptarPoliticaPrivacidad()) {
				return;
			}

			MGeneral.Configuracion.mostrarMensajeSiEsPrimeraEjecucionDeLaVersion();

		}
	}

	@FXML
	private void imprimir(ActionEvent e) {
		imprimir();

	}

	private void imprimir() {

		form.operacion(EnumTipoOperacion.Imprimir);
	}

	@FXML
	private void enviar(ActionEvent e) {
		enviar();

	}

	private void enviar() {
		form.operacion(EnumTipoOperacion.Enviar);
	}

	@FXML
	private void salir(ActionEvent e) {
		System.exit(0);
	}
	
	@FXML
	public void frmProductos() {

		try {
		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Productos.fxml"));
			Parent abrirfrmProductos = loader.load();
			
			stage = new Stage();
			scene = new Scene(abrirfrmProductos);

			// quitando el maximizar y minimizar
			stage.initModality(Modality.APPLICATION_MODAL);
			// bloquea la interacción con otras ventanas de la aplicación
			stage.initStyle(StageStyle.UTILITY);
			// quitando iconos
			stage.getIcons().clear();
			stage.setScene(scene);
			
		
			stage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}