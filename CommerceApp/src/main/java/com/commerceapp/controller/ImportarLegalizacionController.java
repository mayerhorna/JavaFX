package com.commerceapp.controller;

import java.io.File;

import java.io.IOException;

import java.net.URL;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.nio.file.StandardCopyOption;

import java.util.Arrays;

import java.util.Calendar;

import java.util.Date;

import java.util.List;

import java.util.ResourceBundle;

import java.util.logging.Logger;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import java.util.zip.ZipEntry;

import java.util.zip.ZipException;

import java.util.zip.ZipInputStream;

import javax.swing.filechooser.FileSystemView;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.ZipUtil;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.domain.idioma.ElementosIdiomaC;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;

import javafx.application.Platform;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.Node;

import javafx.scene.control.CheckBox;

import javafx.scene.control.Label;

import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;

import javafx.stage.DirectoryChooser;

import javafx.stage.FileChooser;

import javafx.stage.Stage;

public class ImportarLegalizacionController implements Initializable {

	private static final Logger logger = Logger.getLogger(IdiomaC.class.getName());

	EntradaDatosController form;

	@FXML

	private TextField txtDescripcionLega;

	@FXML

	private TextField txtNombreLega;

	@FXML

	private AnchorPane pnlContenedor;

	@FXML

	private CheckBox chkPresentantePorDefecto;

	@FXML

	private CheckBox chkIncluirLibros;

	@FXML

	private TextField txtRuta;

	@FXML

	private Label lblNombreLega;

	@FXML

	private TextField txtRutaZip;

	@FXML

	private Label lblSelecRuta, lblSelecRutaZip;

	private MenuPrincipalController parentController;

	private static File lastDirectory = null;

	public void setParentController(MenuPrincipalController parentController) {

		this.parentController = parentController;

	}

	public MenuPrincipalController getParentController() {

		return parentController;

	}

	@Override

	public void initialize(URL location, ResourceBundle resources) {

		// TODO Auto-generated method stub

	}

	@FXML

	public boolean seleccionarRuta() {

		try {

			DirectoryChooser directoryChooser = new DirectoryChooser();

			directoryChooser.setInitialDirectory(new File(MGeneral.Configuracion.getPathDatos()));

			directoryChooser.setTitle("Seleccionar Ruta");

			File selectedDirectory = directoryChooser.showDialog(new Stage());

			if (selectedDirectory != null) {

				LegalizacionService ml = new LegalizacionService(true, null);

				// Para dar el directorio como bueno se debe poder cargar la legalizacion y se

				// debe cumplir la validación de estructura

				if (!ml.carga(selectedDirectory.getPath())) {

					if (!ml.isValidaEstructura()) {

						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ElDirectorioNoContieneUnaLegalizacionValida,

								"", "", "");

						return false;

					}

				}

				txtRuta.setText(ml.getPathDatos());

				txtRutaZip.setText("");

				// Valor por defecto de nombre y descripción de la legalización

				txtNombreLega.setText(actualizaAnioEnCadena(new File(ml.getPathDatos()).getName()));

				txtDescripcionLega.setText(actualizaAnioEnCadena(ml.Datos.get_Descripcion()));

				txtNombreLega.requestFocus();

				return true;

			} else {

				return false;

			}

		} catch (Exception ex) {

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

			ex.printStackTrace();

			return false;

		}

	}

	@FXML

	private void importarLegalizacion(ActionEvent e) {

		Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click

		Stage stage = (Stage) source.getScene().getWindow();

		String directorioLegalizacion = "";

		String cad1, cad2;

		boolean esOrigenDirectorio = false;

		boolean esOrigenZip = false;

		String ficheroZip;

		String cadError = "";

		LegalizacionService mlZip = null;

		try {

			// Nombre legalización obligatoria

			if (txtNombreLega.getText().isEmpty()) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,

						MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,

								stage.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,

								lblNombreLega.getId(), ""),

						"", "");

				txtNombreLega.requestFocus();

				return;

			}

			// El nombre de la legalización debe ser un nombre de directorio válido

			if (!Ficheros.NombreValido(txtNombreLega.getText())) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NombreFicheroDirectorioNoValido, "", "", "");

				txtNombreLega.requestFocus();

				txtNombreLega.requestFocus();

				return;

			}

			// Descripción legalización obligatoria

			if (txtDescripcionLega.getText().isEmpty()) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,

						MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,

								stage.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,

								lblNombreLega.getId(), ""),

						"", "");

				txtDescripcionLega.requestFocus();

				return;

			}

			// Haber seleccionado un directorio o un zip origen

			if (Formato.ValorNulo(txtRuta.getText()) && Formato.ValorNulo(txtRutaZip.getText())) {

				cad1 = MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,

						stage.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,

						lblSelecRuta.getId(), "");

				cad2 = MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,

						stage.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,

						lblSelecRutaZip.getId(), "");

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio, (cad1 + "|" + cad2), "", "");

				return;

			}

			if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

				directorioLegalizacion = MGeneral.Configuracion.getPathDatos() + "\\" + txtNombreLega.getText();

			} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

				directorioLegalizacion = MGeneral.Configuracion.getPathDatos() + "/" + txtNombreLega.getText();

			}

			// El directorio destino no debe existir

			if (Files.exists(Paths.get(directorioLegalizacion))) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NombreLegalizacionExistente, "", "", "");

				txtNombreLega.requestFocus();

				return;

			}

			// Determinar si el origen es un directorio o un zip

			esOrigenDirectorio = false;

			esOrigenZip = false;

			if (!Formato.ValorNulo(txtRuta.getText())) {

				esOrigenDirectorio = true;

			} else {

				if (!Formato.ValorNulo(txtRutaZip.getText())) {

					esOrigenZip = true;

				}

			}

			// Legalización origen es un directorio

			if (esOrigenDirectorio) {

				activar(false);

				File destino = new File(directorioLegalizacion);

				destino.mkdirs();

				String rutaOrigen = txtRuta.getText();

				if (chkIncluirLibros.isSelected()) {

					// Copiar directorio recursivamente

					copyDirectory(rutaOrigen, directorioLegalizacion);

				} else {

					String nombreFicheroDatos = kLegalizacion.kNombreFicheroDatos;

					String nombreFicheroNombres = kLegalizacion.kNombreFicheroNombres;

					String nombreFicheroDesc = kLegalizacion.kNombreFicheroDesc;

					if (new File(rutaOrigen, nombreFicheroDatos).exists()) {

						Files.copy(new File(rutaOrigen, nombreFicheroDatos).toPath(),

								new File(directorioLegalizacion, nombreFicheroDatos).toPath(),

								StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

					}

					if (new File(rutaOrigen, nombreFicheroNombres).exists()) {

						Files.copy(new File(rutaOrigen, nombreFicheroNombres).toPath(),

								new File(directorioLegalizacion, nombreFicheroNombres).toPath(),

								StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

					}

					if (new File(rutaOrigen, nombreFicheroDesc).exists()) {

						Files.copy(new File(rutaOrigen, nombreFicheroDesc).toPath(),

								new File(directorioLegalizacion, nombreFicheroDesc).toPath(),

								StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

					}

				}

			}

			if (esOrigenZip) {

				activar(false);

				mlZip = new LegalizacionService(true, null);

				String ficherozip = txtRutaZip.getText();

				if (chkIncluirLibros.isSelected()) {

					if (!Ficheros.DirectorioBorra(MGeneral.Configuracion.getPathAuxiliar(), false, false, null)) {

						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NoSeHaPodidoBorrarContenidoDirectorio,

								MGeneral.Configuracion.getPathAuxiliar(), "", "");

						return;

					}

					try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(ficherozip)))) {

						ZipEntry entry;

						while ((entry = zis.getNextEntry()) != null) {

							Path filePath = Paths.get(MGeneral.Configuracion.getPathAuxiliar(), entry.getName());

							Files.createDirectories(filePath.getParent());

							Files.copy(zis, filePath);

						}

					} catch (ZipException ex) {

						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NoSeHaPodidoDescomprimirZip,

								ex.getMessage(), "", "");

						return;

					} catch (IOException er) {

						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NoSeHaPodidoDescomprimirZip,

								er.getMessage(), "", "Acceso denegado");

						return;

					}

					// mlZip.vProgreso.finaliza();

				}

				if (!mlZip.carga(MGeneral.Configuracion.getPathAuxiliar())) {

					if (!mlZip.validarEstructura()) {

						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.ElZipNoContieneUnaLegalizacionValida, "",

								"", "");

						return;

					}

				}

				mlZip = null;

				File directorio = new File(directorioLegalizacion);

				if (!directorio.exists()) {

					directorio.mkdirs();

				}

				try {

					Utilidades.copyDirectory(new File(MGeneral.Configuracion.getPathAuxiliar()),

							new File(directorioLegalizacion));

				} catch (IOException ek) {

					ek.printStackTrace();

				}

			}

			LegalizacionService ml = new LegalizacionService(true, null);

			if (ml.carga(directorioLegalizacion)) {

				ml.Datos.set_Descripcion(txtDescripcionLega.getText());

				ml.Datos.set_Enviado("");

				if (!Formato.ValorNulo(ml.Datos.get_NombreZip())) {

					Ficheros.FicheroBorra(ml.getPathFicheroeDocInicial());

					Ficheros.FicheroBorra(ml.getPathFicheroZip());

				}

				Ficheros.FicheroBorra(ml.getPathFicheroInstancia());

				ml.Datos.set_NombreZip("");

				ml.Datos.set_eDocNumeroDocumento("");

				ml.Datos.set_eDocIdTramite("");

				ml.Datos.set_eDocEntradaTipo("");

				ml.Datos.set_eDocEntradaSubsanada("");

				ml.Datos.set_EnvioReintentable("");

				ml.Datos.set_CodAccesoNif("");

				ml.Datos.set_PresentanteNombreConfirmado("");

				ml.Datos.set_PresentanteApellidosConfirmados("");

				ml.Datos.set_PresentanteCorreoElectronicoConfirmado("");

				if (!Formato.ValorNulo(ml.Datos.get_eDocNombreFicheroEnviado()))

					Ficheros.FicheroBorra(ml.getPathFicheroEnviado());

				ml.Datos.set_eDocNombreFicheroEnviado("");

				if (!Formato.ValorNulo(ml.Datos.get_eDocNombreFicheroAcuseEntrada()))

					Ficheros.FicheroBorra(ml.getPathFicheroAcuseEntrada());

				ml.Datos.set_eDocNombreFicheroAcuseEntrada("");

				if (!Formato.ValorNulo(ml.Datos.get_eDocNombreFicheroNE()))

					Ficheros.FicheroBorra(ml.getPathFicheroNE());

				ml.Datos.set_eDocNombreFicheroNE("");

				ml.Presentacion.setFechaSolicitud(LegalizacionService.dameCadenaFecha(new Date()));

				if (!MGeneral.Idioma.existeMunicipioDeProvincia(ml.Presentacion.getProvinciaCodigo(),

						ml.Presentacion.getCiudad()))

					ml.Presentacion.setCiudad("");

				if (!MGeneral.Idioma.existeMunicipioDeProvincia(ml.Presentacion.getPresentante().get_ProvinciaCodigo(),

						ml.Presentacion.getPresentante().get_Ciudad()))

					ml.Presentacion.getPresentante().set_Ciudad("");

				if (chkPresentantePorDefecto.isSelected())

					ml.Presentacion.getPresentante().ponDatosPresentantePorDefecto();

				if (!chkIncluirLibros.isSelected())

					ml.quitaTodosLosLibros();

				ml.guarda();

			}

			getParentController().EntradaDatos(directorioLegalizacion);

			salir(e);

			if (form != null) {

				form.aniadirListener();

			}

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

			if (mlZip != null) {

				// mlZip.vProgreso.finaliza();

			}

			activar(true);

		}

	}

	@FXML

	private void btnSelecRutaZip_Click(ActionEvent e) {

		try {

			FileChooser buscar = new FileChooser();

			if (lastDirectory == null) {

				FileSystemView fileSystemView = FileSystemView.getFileSystemView();

				File desktopDir = fileSystemView.getHomeDirectory();

				buscar.setInitialDirectory(desktopDir);

			}

			if (lastDirectory != null && lastDirectory.exists()) {

				buscar.setInitialDirectory(lastDirectory);

			}

			if (MGeneral.Configuracion.getOperatingsystem().contains("win")) {

				FileChooser.ExtensionFilter extensiones = new FileChooser.ExtensionFilter(

						dameCadenaExtensionesImportarPermitidas(), "*zip");

				buscar.getExtensionFilters().add(extensiones);

			} else if (MGeneral.Configuracion.getOperatingsystem().contains("nux")) {

				FileChooser.ExtensionFilter extensiones = new FileChooser.ExtensionFilter(

						dameCadenaExtensionesImportarPermitidas() + " (*.zip, *.ZIP)", "*zip", "*.ZIP");

				// Agregar el filtro al FileChooser

				buscar.getExtensionFilters().add(extensiones);

			}

			Node source = (Node) e.getSource();

			Stage stage = (Stage) source.getScene().getWindow();

			File fichero = buscar.showOpenDialog(stage);

			if (fichero != null) {

				lastDirectory = fichero.getParentFile();

			}

			if (fichero != null) {

				String rutaZip = fichero.getAbsolutePath();

				if (!compruebaZip(rutaZip)) {

					return;

				}

				txtRutaZip.setText(rutaZip);

				txtRuta.setText("");

				txtNombreLega.requestFocus();

			}

		} catch (Exception ex) {

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

			ex.printStackTrace();

		}

	}

	private String dameCadenaExtensionesImportarPermitidas() {

		return MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.FicherosZip).toString();

	}

	public String actualizaAnioEnCadena(String cadenaOrigen) {

		String resultado = cadenaOrigen;

		try {

			int anioAnterior = Calendar.getInstance().get(Calendar.YEAR) - 1;

			String cadAnioOrigen = "";

			Pattern patron = Pattern.compile("\\d{4}"); // Buscar 4 dígitos seguidos

			Matcher coincidencias = patron.matcher(cadenaOrigen);

			if (coincidencias.find()) {

				cadAnioOrigen = coincidencias.group();

			}

			if (!cadAnioOrigen.isEmpty()) {

				if (cadAnioOrigen.equals(String.valueOf(anioAnterior))) {

					resultado = cadenaOrigen.replace(cadAnioOrigen, String.valueOf(anioAnterior + 1));

				} else {

					resultado = cadenaOrigen.replace(cadAnioOrigen, String.valueOf(anioAnterior));

				}

			}

			return resultado;

		} catch (Exception ex) {

			// Manejar la excepción aquí, si es necesario

			return cadenaOrigen;

		}

	}

	private void activar(boolean activar) {

		pnlContenedor.setDisable(!activar);

		Platform.runLater(() -> {

			pnlContenedor.setDisable(!activar);

			Utilidades.cursorEspera((Stage) pnlContenedor.getScene().getWindow(), !activar);

		});

	}

	public static void copyDirectory(String sourceDirectory, String destinationDirectory) throws IOException {

		Path sourcePath = Paths.get(sourceDirectory);

		Path destinationPath = Paths.get(destinationDirectory);

		Files.walk(sourcePath).forEach(source -> {

			try {

				Path destination = destinationPath.resolve(sourcePath.relativize(source));

				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING,

						StandardCopyOption.COPY_ATTRIBUTES);

			} catch (IOException e) {

				e.printStackTrace();

			}

		});

	}

	public boolean compruebaZip(String ficheroZip) throws IOException {

		String extension;

		String directorioAuxiliar = null;

		StringBuilder cadError = new StringBuilder();

		LegalizacionService mlZip = null;

		try {

			// activar(false);

			mlZip = new LegalizacionService(true, null);

			extension = obtenerExtension(ficheroZip);

			if (extension != null && extension.equalsIgnoreCase("zip")) {

				// Borra el contenido del directorio auxiliar

				if (!Ficheros.DirectorioBorra(MGeneral.Configuracion.getPathAuxiliar(), false, false, null)) {

					// mostrarAlerta("No se ha podido borrar el contenido del directorio",

					// AlertType.ERROR);

					return false;

				}

				// Descomprime el ZIP en el directorio auxiliar

				// mlZip.vProgreso.inicializa(100, 0, 0, 0, 0);

				String[] nombresADescomprimir = { kLegalizacion.kNombreFicheroDatos,

						kLegalizacion.kNombreFicheroNombres, kLegalizacion.kNombreFicheroDesc };

				ZipUtil z = new ZipUtil();

				if (!z.descomprimirSelectivo(ficheroZip, MGeneral.Configuracion.getPathAuxiliar(), nombresADescomprimir,

						cadError, mlZip.vProgreso)) {

				}

				directorioAuxiliar = MGeneral.Configuracion.getPathAuxiliar();

				if (mlZip != null) {

					// mlZip.vProgreso.finaliza();

				}

			} else {

			}

			// Carga de la legalización para validarla

			if (!mlZip.carga(directorioAuxiliar)) {

				/*
				 * 
				 * if (!mlZip.validarEstructura()) {
				 * 
				 * MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.
				 * 
				 * ElZipNoContieneUnaLegalizacionValida, "", "", ""); return false; }
				 * 
				 */

			}

			// Valor por defecto de nombre y descripción de la legalización

			if (mlZip.Datos.get_Descripcion().equals(new File(MGeneral.Configuracion.getPathAuxiliar()).getName())) {

				// Si no hay descripción de legalización, se coge el nombre de legalización, y

				// en este caso sería el directorio auxiliar

				txtDescripcionLega.setText(txtNombreLega.getText());

			} else {

				txtDescripcionLega.setText(actualizaAnioEnCadena(mlZip.Datos.get_Descripcion()));

			}

			mlZip = null;

			eliminarContenidoCarpeta(new File(MGeneral.Configuracion.getPathAuxiliar()));

			return true;

		} finally {

			if (mlZip != null) {

				// mlZip.vProgreso.finaliza();

				// activar(true);

			}

		}

	}

	public static void eliminarContenidoCarpeta(File carpeta) {

		// Verifica si la carpeta existe

		if (carpeta.exists()) {

			// Obtiene la lista de archivos dentro de la carpeta

			File[] archivos = carpeta.listFiles();

			// Verifica si la lista de archivos no es nula y no está vacía

			if (archivos != null && archivos.length > 0) {

				for (File archivo : archivos) {

					// Elimina cada archivo dentro de la carpeta

					archivo.delete();

				}

			} else {

				System.out.println("La carpeta está vacía.");

			}

		} else {

			System.out.println("La carpeta no existe.");

		}

	}

	private String obtenerExtension(String rutaArchivo) {

		int lastDotIndex = rutaArchivo.lastIndexOf(".");

		if (lastDotIndex > 0 && lastDotIndex < rutaArchivo.length() - 1) {

			return rutaArchivo.substring(lastDotIndex + 1).toLowerCase();

		}

		return null;

	}

	@FXML

	public void salir(ActionEvent e) throws Exception {

		// Me cierra la ventana

		if (e != null) {

			Node source = (Node) e.getSource();

			Stage stage = (Stage) source.getScene().getWindow();

			stage.close();

		}

	}

}
