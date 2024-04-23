package com.commerceapp.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.ZipUtil;
import com.commerceapp.domain.idioma.ElementosIdiomaC;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.reporting.instancia.ReportingPreviewService.Reportes;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class RecepcionAbrirController implements Initializable {
	@FXML
	private Button btnAceptar;

	@FXML
	private Button btnBuscar;

	@FXML
	private Button btnSalir;

	@FXML
	private AnchorPane frmRecepcionAbrir;

	@FXML
	private Label lblIdentificador;

	@FXML
	private Label lblSituacion;

	@FXML
	private TextField txtFichero;

	@FXML
	private TextField txtIdentificador;

	private String rutaFichero;
	private static File lastDirectory = null;

	@FXML
	private GridPane pnlContenedor;

	private MenuPrincipalController parentController;

	public void setParentController(MenuPrincipalController parentController) {
		this.parentController = parentController;
	}

	public MenuPrincipalController getParentController() {
		return parentController;
	}

	private static final Logger logger = Logger.getLogger(RecepcionAbrirController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	private List<ExtensionFilter> parseCustomFilter(String customFilterString) {
		List<ExtensionFilter> filters = new ArrayList<>();
		String[] parts = customFilterString.split("\\|");

		// Obtener las extensiones de cada filtro
		String[] extensions1 = parts[1].trim().split(";");

		// Crear los ExtensionFilter correspondientes
		ExtensionFilter filter1 = new ExtensionFilter(parts[0].trim(), extensions1);

		filters.add(filter1);

		return filters;
	}

	@FXML
	void btnBuscar_Click(ActionEvent event) {
		try {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Seleccionar archivo");

			List<ExtensionFilter> filters = parseCustomFilter(dameCadenaExtensionesRecepcionarPermitidas());

			if (lastDirectory == null) {
				FileSystemView fileSystemView = FileSystemView.getFileSystemView();
				File desktopDir = fileSystemView.getHomeDirectory();
				chooser.setInitialDirectory(desktopDir);
			}

			if (lastDirectory != null && lastDirectory.exists()) {
				chooser.setInitialDirectory(lastDirectory);
			}

			chooser.getExtensionFilters().addAll(filters);

			File selectedFile = chooser.showOpenDialog(null);
			if (selectedFile != null) {
				lastDirectory = selectedFile.getParentFile();
			}
			if (selectedFile != null) {
				String filePath = selectedFile.getAbsolutePath();
				String directoryPath = selectedFile.getParent();
				
				txtFichero.setText(filePath);
				rutaFichero = directoryPath;
				
				txtIdentificador.requestFocus();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		
		}
	}

	public void btnSalir_Click(ActionEvent e) throws Exception {

		close(e);

	}

	private void close(ActionEvent e) {
		Node source = (Node) e.getSource();
		Stage stage = (Stage) source.getScene().getWindow();

		stage.close();
	}

	@FXML
	void btnAceptar_Click(ActionEvent event) {

		try {
			String extension;
			String fichero;
			String directorio;
			String caderror = "";

			if (!comprueba())
				return;

			activar(false);
			LegalizacionService mlZip = null;
			mlZip = new LegalizacionService(true, null);
			fichero = txtFichero.getText();

			Path ficheroPath = Paths.get(fichero);

			extension = getFileExtension(ficheroPath);
			if (extension.startsWith("."))
				extension = extension.substring(1);

			if (extension.toUpperCase().equals("ZIP")) {

				// ml.vProgreso.Inicializa(100);

				if (!Ficheros.DirectorioBorra(MGeneral.Configuracion.getPathAuxiliar(), false, false, null)) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NoSeHaPodidoBorrarContenidoDirectorio,
							MGeneral.Configuracion.getPathAuxiliar(), "", "");
					return;
				}

				try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(fichero)))) {
					ZipEntry entry;
					while ((entry = zis.getNextEntry()) != null) {
						Path filePath = Paths.get(MGeneral.Configuracion.getPathAuxiliar(), entry.getName());
						Files.createDirectories(filePath.getParent());
						Files.copy(zis, filePath);
					}
				} catch (ZipException ex) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NoSeHaPodidoDescomprimirZip, ex.getMessage(),
							"", "");

					return;
				} catch (IOException er) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NoSeHaPodidoDescomprimirZip, er.getMessage(),
							"", "Acceso denegado");

					return;
				}
				// mlZip.vProgreso.finaliza();

				directorio = MGeneral.Configuracion.getPathAuxiliar();

			} else {
				directorio = Paths.get(fichero).getParent().toString();
				if (!mlZip.carga(directorio)) {
					if (!mlZip.validarEstructura()) {
						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ElDirectorioNoContieneUnaLegalizacionValida,
								"", "", "");
						return;
					}

				}

			}
			if (!mlZip.carga(MGeneral.Configuracion.getPathAuxiliar())) {
				if (!mlZip.validarEstructura()) {
					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ElZipNoContieneUnaLegalizacionValida, "", "",
							"");
					return;
				}

			}
			getParentController().EntradaDatos(directorio);
			getParentController().form.abrirRecepcion(directorio, txtIdentificador.getText());
			close(event);

		} catch (Exception ex) {
			ex.printStackTrace();
			/*
			 * Idioma.MostrarMensaje(IdiomaC.enumMensajes.Excepcion, MessageBoxIcon.Error,
			 * MessageBoxButtons.OK, ex.getMessage());
			 */
		} finally {
			/*
			 * if (ml != null) ml.vProgreso.Finaliza();
			 */
			activar(true);
		}
	}

	private static String getFileExtension(Path path) {
		String fileName = path.getFileName().toString();
		String extension = "";

		int index = fileName.lastIndexOf('.');
		if (index > 0) {
			extension = fileName.substring(index + 1);
		}
		return extension;
	}

	private boolean comprueba() {
		boolean comprueba = false;

		try {
			String fichero;
			String extension;
			String directorio;

			if (Formato.ValorNulo(txtFichero.getText())) {
				IdiomaC.MostrarMensaje(
						IdiomaC.EnumMensajes.CampoObligatorio, MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,
								frmRecepcionAbrir.getId(), ElementosIdiomaC.TEXT_CONTROLES, lblSituacion.getId(), ""),
						"", "");
				btnBuscar.requestFocus();
				return comprueba;
			}

			if (Formato.ValorNulo(txtIdentificador.getText())) {
				IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,
						MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS, frmRecepcionAbrir.getId(),
								ElementosIdiomaC.TEXT_CONTROLES, lblIdentificador.getId(), ""),
						"", "");
				txtIdentificador.requestFocus();
				return comprueba;
			}

			fichero = txtFichero.getText();

			// Comprobacion de que existe el fichero seleccionado
			if (Ficheros.FicheroExiste(fichero) == Ficheros.ExistFicheroEnum.Existe) {
			} else {
				IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.FicheroInexistente, "", "", fichero);
				return comprueba;
			}
			Path path = Paths.get(fichero);

			extension = getFileExtension(path);

			if (extension.toUpperCase().equals("ZIP")) {

				// Comprueba que el zip tenga contenidos los ficheros obligatorios (fichero con
				// los datos y fichero con los nombres)

				if (!ZipUtil.compruebaContenido(fichero)) {
					IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.ElZipNoContieneUnaLegalizacionValida, "", "", "");
					return comprueba;
				}

			} else if (extension.toUpperCase().equals("TXT")) {

				directorio = Paths.get(fichero).getParent().toString();

				// Si la estructura no es válida no vale

			} else {
				// Si el fichero no tienen una de las extensiones permitidas no vale
				return comprueba;
			}

			comprueba = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}

		return comprueba;
	}

	private String dameCadenaExtensionesRecepcionarPermitidas() {
		String cad1 = "";
		String cad2 = "";
		String cadaux;

		// Sección de extensiones de archivo .txt
		cadaux = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.FicherosTxt).toString();
		cad1 += String.format("|%s |DATOS.TXT;NOMBRES.TXT", cadaux);
		cad2 = "DATOS.TXT;NOMBRES.TXT";

		// Sección de extensiones de archivo .zip
		cadaux = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.FicherosZip).toString();
		cad1 += String.format("|%s (*.%s)|*.%s", cadaux, "ZIP", "ZIP");
		cad2 += ";*.ZIP";

		cadaux = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.FicherosZip).toString();
		cad1 += String.format("|%s (*.%s)|*.%s", cadaux, "zip", "zip");
		cad2 += ";*.zip";

		// Sección de extensiones de archivo "Todos"
		cadaux = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.Todos).toString();
		if (cadaux.isEmpty())
			cadaux = "Todos";
		cadaux += " |";

		cad2 = cadaux + cad2;

		return cad2 + cad1;
	}

	private void activar(boolean activar) {
		Platform.runLater(() -> {
			pnlContenedor.setDisable(!activar);
			Utilidades.cursorEspera((Stage) pnlContenedor.getScene().getWindow(), !activar);
		});
	}
}
