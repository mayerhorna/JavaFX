package com.commerceapp.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.ceDoc;
import com.commerceapp.domain.meDoc;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrevioEnvioController implements Initializable {
	@FXML
	private Button btnAbrirPdf;

	@FXML
	private Button btnAgregarPdf;

	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnEnviar;

	@FXML
	private Button btnQuitarPdf;

	@FXML
	private AnchorPane frmPrevioEnvio;

	@FXML
	private Label grpEntradaSubsanada;

	@FXML
	private Label grpEntradaTipo;

	@FXML
	private Label grpEnvio;

	@FXML
	private Label grpNumeroDocumento;

	@FXML
	private Label lblEntradaSubsanadaAnyo;

	@FXML
	private Label lblEntradaSubsanadaLibro;

	@FXML
	private Label lblEntradaSubsanadaNumero;

	@FXML
	private Label lblGridFicherosPdf;

	@FXML
	private ListView<String> lsvFicheros;

	@FXML
	private RadioButton rbtEntradaTipoPrimera;

	@FXML
	private RadioButton rbtEntradaTipoSubsanacion;

	@FXML
	private RadioButton rbtEnvioDirecto;

	@FXML
	private RadioButton rbtEnvioPortal;

	@FXML
	private TextArea txtAviso;

	@FXML
	private TextField txtEntradaSubsanadaAnyo;

	@FXML
	private TextField txtEntradaSubsanadaLibro;

	@FXML
	private TextField txtEntradaSubsanadaNumero;

	@FXML
	private TextField txtNumeroDocumento;

	@FXML
	private HBox grpEntradaSubsanadaCaja;

	@FXML
	private VBox grpEntradaTipoCaja;

	@FXML
	private HBox grpNumeroDocumentoCaja;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MGeneral.RetornoFormulario = false;
		MGeneral.RetornoFormaDeEnvioDirecto = true;

		Platform.runLater(() -> {

			cargar();

			cargarFicherosEnGrid();
			lsvFicheros.requestFocus();

			proponerReferenciaDocumento();

			txtEntradaSubsanadaAnyo.addEventFilter(KeyEvent.KEY_TYPED, event -> {
				soloNumeros(event);
			});
			txtEntradaSubsanadaNumero.addEventFilter(KeyEvent.KEY_TYPED, event -> {
				soloNumeros(event);
			});
			txtAviso.focusedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					btnAgregarPdf.requestFocus();
				}
			});

			rbtEnvioPortal.selectedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					controlesVisibles();
				}
			});

			rbtEnvioDirecto.selectedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					controlesVisibles();
				}
			});
			rbtEntradaTipoPrimera.selectedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					controlesVisibles();
					proponerReferenciaDocumento();
				}
			});

			rbtEntradaTipoSubsanacion.selectedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					controlesVisibles();
					proponerReferenciaDocumento();
				}
			});

			ToggleGroup toggleGroup = new ToggleGroup();

			rbtEntradaTipoPrimera.setToggleGroup(toggleGroup);
			rbtEntradaTipoSubsanacion.setToggleGroup(toggleGroup);

			ToggleGroup toggleGroup2 = new ToggleGroup();

			rbtEnvioDirecto.setToggleGroup(toggleGroup2);
			rbtEnvioPortal.setToggleGroup(toggleGroup2);

			String osName = System.getProperty("os.name").toLowerCase();

			// Si el sistema operativo es Windows, se activa el radioButton1, de lo
			// contrario, se activa el radioButton2
			/*
			 * if (osName.contains("win")) {
			 * 
			 * rbtEnvioPortal.setVisible(false); } else {
			 * 
			 * 
			 * }
			 */
			rbtEnvioDirecto.setVisible(false);
			rbtEnvioDirecto.setManaged(false);
			rbtEnvioPortal.setVisible(true);
			rbtEnvioPortal.setSelected(true);

		});
		lsvFicheros.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.intValue() >= 0) {
				controlesActivos();
			}
		});
		// cargarHandlers()

		// lsvFicheros.HideSelection = False

	}

	private void cargar() {
		txtNumeroDocumento.setText(MGeneral.mlform.Datos.get_eDocNumeroDocumento());

		if (MGeneral.mlform.Datos.get_eDocEntradaTipo() == meDoc.kValorIdentificacion_DocumentoEntradaTipoSUBSANACION) {
			rbtEntradaTipoSubsanacion.setSelected(true);
		} else {
			rbtEntradaTipoPrimera.setSelected(true);
		}

		ceDoc ced = new ceDoc();
		StringBuilder libro = new StringBuilder();
		StringBuilder anyo = new StringBuilder();
		StringBuilder numero = new StringBuilder();

		ced.dameDatosEntrada(MGeneral.mlform.Datos.get_eDocEntradaSubsanada(), libro, anyo, numero);

		txtEntradaSubsanadaLibro.setText(libro.toString());
		txtEntradaSubsanadaAnyo.setText(anyo.toString());
		txtEntradaSubsanadaNumero.setText(numero.toString());

		if (Formato.ValorNulo(txtEntradaSubsanadaLibro.getText())) {
			// Valor del libro para entradas de mercantil de legalización de libros
			txtEntradaSubsanadaLibro.setText(meDoc.kValorIdentificacion_DocumentoEntradaLIBROMERCANTIL);
		}
	}

	private void cargarFicherosEnGrid() {
		MGeneral.mlform.Adjuntos.cargaFicheros();
		lsvFicheros.getItems().clear();
		controlesActivos();

		for (int i = 0; i < MGeneral.mlform.Adjuntos.getNumeroFicheros(); i++) {
			String nombreFichero = MGeneral.mlform.Adjuntos.getFicheros()[i].nombreFichero;
			lsvFicheros.getItems().add(nombreFichero);
		}
	}

	private void controlesActivos() {
		int indiceFichero = dameFicheroSeleccionado();
		boolean hayFicheroSeleccionado = (indiceFichero >= 0);

		btnAbrirPdf.setDisable(!hayFicheroSeleccionado);
		btnQuitarPdf.setDisable(!hayFicheroSeleccionado);
	}

	private int dameFicheroSeleccionado() {
		if (lsvFicheros.getSelectionModel().getSelectedItems().isEmpty()) {
			return -1;
		} else {
			return lsvFicheros.getSelectionModel().getSelectedIndex();
		}
	}

	@FXML
	protected void btnQuitarPdf_Click(ActionEvent event) {
		try {
			int indicefichero = dameFicheroSeleccionado();

			if (indicefichero < 0)
				return;

			boolean confirmacion = MGeneral.Idioma.MostrarMensaje(EnumMensajes.AvisoEliminarFichero, "", "", "");
			if (!confirmacion)
				return;

			// Suponiendo que mlform.Adjuntos.QuitaFichero devuelve un booleano
			if (MGeneral.mlform.Adjuntos.quitaFichero(lsvFicheros.getItems().get(indicefichero))) {
				cargarFicherosEnGrid();
			}

		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}
	}

	@FXML
	protected void btnAbrirPdf_Click(ActionEvent event) {
		try {
			int indicefichero = dameFicheroSeleccionado();

			if (indicefichero < 0)
				return;

			String fichero = MGeneral.mlform.Adjuntos.getFicheros()[indicefichero].pathFichero;

			File file = new File(fichero);

			if (!file.exists()) {
				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FicheroInexistente, fichero, "", "");
				return;
			}

			Utilidades.ProcessStartFichero(fichero);

		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}
	}

	protected boolean insertar() {
		boolean insertado = false;

		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("AcrobatReader", "*.pdf"));

			File selectedFile = fileChooser.showOpenDialog(null);

			if (selectedFile != null) {
				String pathFicheroOrigen = selectedFile.getAbsolutePath();

				if (MGeneral.mlform.Adjuntos.getNumeroFicheros() >= MGeneral.Configuracion.getAdjuntosMaximos()) {
					throw new Exception(
							MGeneral.Idioma.obtenerMensaje(IdiomaC.EnumMensajes.NumeroMaximoAdjuntosSuperado,
									String.valueOf(MGeneral.Configuracion.getAdjuntosMaximos()), "", ""));
				}

				if (MGeneral.mlform.Adjuntos.aniadeFichero(pathFicheroOrigen,
						MGeneral.mlform.hayQueCopiarFicheroConProgreso(pathFicheroOrigen))) {
					cargarFicherosEnGrid();
					insertado = true;
				}
			}
		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}

		return insertado;
	}

	@FXML
	protected void btnEnviar_Click(ActionEvent event) {
		try {
			if (!comprueba())
				return;

			if (rbtEnvioDirecto.isSelected()) {
				MGeneral.mlform.Datos.set_eDocNumeroDocumento(txtNumeroDocumento.getText());
				if (rbtEntradaTipoPrimera.isSelected()) {
					MGeneral.mlform.Datos
							.set_eDocEntradaTipo(meDoc.kValorIdentificacion_DocumentoEntradaTipoENTRADA_NUEVA);
					MGeneral.mlform.Datos.set_eDocEntradaSubsanada("");
				} else {
					MGeneral.mlform.Datos
							.set_eDocEntradaTipo(meDoc.kValorIdentificacion_DocumentoEntradaTipoSUBSANACION);
					MGeneral.mlform.Datos.set_eDocEntradaSubsanada(txtEntradaSubsanadaLibro.getText() + "/"
							+ txtEntradaSubsanadaAnyo.getText() + "/" + txtEntradaSubsanadaNumero.getText());
				}
			} else {
				MGeneral.mlform.Datos.set_eDocNumeroDocumento("");
				MGeneral.mlform.Datos.set_eDocEntradaTipo("");
				MGeneral.mlform.Datos.set_eDocEntradaSubsanada("");
			}

			if (!MGeneral.mlform.guardaDatosGenerales())
				return;

			MGeneral.RetornoFormaDeEnvioDirecto = rbtEnvioDirecto.isSelected();
			MGeneral.RetornoFormulario = true;

			// Cierra el escenario actual
			Stage stage = (Stage) txtNumeroDocumento.getScene().getWindow();
			stage.close();

		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");

		}
	}

	private boolean comprueba() {
		try {
			if (!MGeneral.mlform.Adjuntos.valida())
				return false;

			if (rbtEnvioDirecto.isSelected()) {

				if (rbtEntradaTipoSubsanacion.isSelected()) {

					if (Formato.ValorNulo(txtEntradaSubsanadaLibro.getText())) {
						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,
								grpEntradaSubsanada.getText() + ": " + lblEntradaSubsanadaLibro.getText(), "", "");
						txtEntradaSubsanadaLibro.requestFocus();
						return false;
					}

					if (Formato.ValorNulo(txtEntradaSubsanadaAnyo.getText())) {
						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,
								grpEntradaSubsanada.getText() + ": " + lblEntradaSubsanadaAnyo.getText(), "", "");
						txtEntradaSubsanadaAnyo.requestFocus();
						return false;
					}

					if (Formato.ValorNulo(txtEntradaSubsanadaNumero.getText())) {
						MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,
								grpEntradaSubsanada.getText() + ": " + lblEntradaSubsanadaNumero.getText(), "", "");
						txtEntradaSubsanadaNumero.requestFocus();
						return false;
					}
				}
			}
			return true;
		} catch (Exception ex) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
			return false;
		}
	}

	private void controlesVisibles() {
		boolean verDatosEnvioDirecto = rbtEnvioDirecto.isSelected();

		if (grpNumeroDocumento.isVisible() != verDatosEnvioDirecto) {
			grpNumeroDocumento.setVisible(verDatosEnvioDirecto);
			grpNumeroDocumentoCaja.setVisible(verDatosEnvioDirecto);

		}

		if (grpEntradaTipo.isVisible() != verDatosEnvioDirecto) {
			grpEntradaTipo.setVisible(verDatosEnvioDirecto);
			grpEntradaTipoCaja.setVisible(verDatosEnvioDirecto);

		}

		boolean verDatosEntradaSubsanada = false;
		if (verDatosEnvioDirecto) {
			verDatosEntradaSubsanada = rbtEntradaTipoSubsanacion.isSelected();
		}

		if (grpEntradaSubsanada.isVisible() != verDatosEntradaSubsanada) {
			grpEntradaSubsanada.setVisible(verDatosEntradaSubsanada);
			grpEntradaSubsanadaCaja.setVisible(verDatosEntradaSubsanada);

		}

	}

	@FXML
	public void btnCancelar_Click(ActionEvent e) throws Exception {

		// Me cierra la ventana
		if (e != null) {
			Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
			Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el
																	// elemento
			stage.close();
		}

	}

	@FXML
	public void btnAgregarPdf_Click(ActionEvent e) throws Exception {
		insertar();
	}

	private void proponerReferenciaDocumento() {
		String cad = "";

		if (rbtEnvioDirecto.isSelected()) {
			cad = MGeneral.mlform.Presentacion.getNifCif() + MGeneral.mlform.Datos.get_Ejercicio();
			if (rbtEntradaTipoSubsanacion.isSelected()) {
				cad += MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.Subsanacion);
			}

			if (Formato.ValorNulo(MGeneral.mlform.Datos.get_eDocNumeroDocumento())) {
				txtNumeroDocumento.setText(cad);
			}
		}
	}

	private void soloNumeros(KeyEvent event) {
		String character = event.getCharacter();
		if (!character.matches("[0-9]")) {
			event.consume(); // Esto evita que se agregue el carácter no numérico al TextField
		}
	}
}
