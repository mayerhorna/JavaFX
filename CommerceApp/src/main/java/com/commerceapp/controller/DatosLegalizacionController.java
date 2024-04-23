package com.commerceapp.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.ceDoc;
import com.commerceapp.domain.meDoc;
import com.commerceapp.domain.idioma.ElementosIdiomaC;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.domain.legalizacion.cDatos;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;
import com.commerceapp.util.Utilidades;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DatosLegalizacionController implements Initializable {
	private static final Logger logger = Logger.getLogger(DatosLegalizacionController.class.getName());
	@FXML
	private Button btnAceptar;

	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnVerAcuse;

	@FXML
	private RadioButton rbtEntradaTipoSubsanacion;

	@FXML
	private RadioButton rbtEntradaTipoPrimera;

	@FXML
	private Label grpEntradaSubsanada;

	@FXML
	private Label grpEnvioDirecto;

	@FXML
	private HBox grpEntradaSubsanada1;

	@FXML
	private VBox grpEnvioDirecto1;

	@FXML
	private Label lblAcuseEntrada;

	@FXML
	private Label lblPendeReenvio;

	@FXML
	private Label lblDescripcionLega;

	@FXML
	private Label lblEjercicio;

	@FXML
	private Label lblEnviado;

	@FXML
	private Label lblNombreLega;

	@FXML
	private Label lblPathDatos;

	@FXML
	private TextField txtDescripcionLega;

	@FXML
	private TextField txtEjercicio;

	@FXML
	private TextField txtEntradaSubsanadaAnyo;

	@FXML
	private TextField txtEntradaSubsanadaLibro;

	@FXML
	private TextField txtEntradaSubsanadaNumero;

	@FXML
	private TextField txtEnviado;

	@FXML
	private TextField txtIdTramite;

	@FXML
	private TextField txtNombreLega;

	@FXML
	private TextField txtNumeroDocumento;

	@FXML
	private TextField txtPathDatos;

	MenuPrincipalController parentController;

	public void setParentController(MenuPrincipalController parentController) {
		this.parentController = parentController;

	}

	public MenuPrincipalController getParentController() {
		return parentController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cargar();

	}

	private void cargar() {
		txtNombreLega.setText(MGeneral.mlform.Datos.getNombreDirectorio());
		txtDescripcionLega.setText(MGeneral.mlform.Datos.get_Descripcion());
		txtPathDatos.setText(MGeneral.mlform.Datos.get_PathDatos());
		if (MGeneral.mlform.Datos.get_Ejercicio() == 0) {
			txtEjercicio.setText("");
		} else {
			txtEjercicio.setText(String.valueOf(MGeneral.mlform.Datos.get_Ejercicio()));
		}
		txtEnviado.setText(MGeneral.mlform.Datos.get_Enviado());

		grpEnvioDirecto.setVisible(false);
		grpEnvioDirecto1.setVisible(false);
		grpEnvioDirecto.setManaged(false);
		grpEnvioDirecto1.setManaged(false);

		switch (MGeneral.mlform.Datos.getEstadoEnvio()) {
		case EnviadaPorServicioCorrectamente:
		case EnviadaPorServicioErrorReintentable:
			txtNumeroDocumento.setText(MGeneral.mlform.Datos.get_eDocNumeroDocumento());
			txtIdTramite.setText(MGeneral.mlform.Datos.get_eDocIdTramite());
			if (MGeneral.mlform.Datos
					.get_eDocEntradaTipo() == meDoc.kValorIdentificacion_DocumentoEntradaTipoSUBSANACION) {
				rbtEntradaTipoSubsanacion.setSelected(true);
				grpEntradaSubsanada.setVisible(true);
				grpEntradaSubsanada1.setVisible(true);
				grpEntradaSubsanada.setManaged(true);
				grpEntradaSubsanada1.setManaged(true);
			} else {
				rbtEntradaTipoPrimera.setSelected(true);
				grpEntradaSubsanada.setVisible(false);
				grpEntradaSubsanada1.setVisible(false);
				grpEntradaSubsanada.setManaged(false);
				grpEntradaSubsanada1.setManaged(false);
			}

			ceDoc ced = new ceDoc();
			StringBuilder libro = new StringBuilder();
			StringBuilder anyo = new StringBuilder();
			StringBuilder numero = new StringBuilder();

			ced.dameDatosEntrada(MGeneral.mlform.Datos.get_eDocEntradaSubsanada(), libro, anyo, numero);

			txtEntradaSubsanadaLibro.setText(libro.toString());
			txtEntradaSubsanadaAnyo.setText(anyo.toString());
			txtEntradaSubsanadaNumero.setText(numero.toString());

			if (!new File(MGeneral.mlform.Datos.get_eDocNombreFicheroAcuseEntrada()).exists()) {
				lblAcuseEntrada.setVisible(false);
				btnVerAcuse.setVisible(false);
			}

			txtIdTramite.setStyle("-fx-text-fill: green;");
			lblPendeReenvio.setVisible(false);

			if (MGeneral.mlform.Datos.getEstadoEnvio() == cDatos.eEstadoEnvio.EnviadaPorServicioErrorReintentable) {
				txtIdTramite.setStyle("-fx-text-fill: red;");
				lblPendeReenvio.setStyle("-fx-text-fill: green;");

				lblPendeReenvio.setVisible(true);
			}

			grpEnvioDirecto.setVisible(true);
			grpEnvioDirecto1.setVisible(true);
			grpEnvioDirecto.setManaged(true);
			grpEnvioDirecto1.setManaged(true);
			break;
		case EnviadaPorPortal:
			// v1.5.1 ya no hay opción de enviar por el portal
			break;
		}
	}

	@FXML
	private void btnAceptar_Click(ActionEvent e) throws Exception {
		try {
			if (!comprueba()) {
				return;
			}

			MGeneral.mlform.Datos.set_Descripcion(txtDescripcionLega.getText());
			MGeneral.mlform.guardaDatosGenerales();

			if (txtNombreLega.getText().equals(MGeneral.mlform.Datos.getNombreDirectorio())) {

				// Cerrar el formulario actual
				salir(e);

				// Se actualiza el título ya que en él se ve la descripción de la legalización
				getParentController().establecerTitulo();

				return;
			}

			// Ha cambiado el nombre del directorio
			String directorioLegalizacion = MGeneral.Configuracion.getPathDatos() + "\\"
					+ txtNombreLega.getText();
			String rutaOrigen = MGeneral.mlform.getPathDatos();

			// Control de que el nuevo directorio no exista
			if (Files.exists(Paths.get(directorioLegalizacion))) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Nombre de legalización ya existente");
				alert.showAndWait();
				txtNombreLega.requestFocus();
				return;
			}

			Files.move(Paths.get(rutaOrigen), Paths.get(directorioLegalizacion));

			// Cerrar la legalización actual

			getParentController().EntradaDatos(directorioLegalizacion);
			salir(e);
			// Cerrar la ventana actual de JavaFX
		} catch (IOException ex) {
			ex.printStackTrace();
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, ex.getMessage(), "", "");
		}
	}

	private boolean comprueba() {
		boolean valido = true;

		if (Formato.ValorNulo(txtNombreLega.getText())) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,
					MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,
							lblNombreLega.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,
							lblNombreLega.getId().toString(), ""),
					"", "");
			txtNombreLega.requestFocus();
			valido = false;
		} else if (!Ficheros.NombreValido(txtNombreLega.getText())) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NombreFicheroDirectorioNoValido,"",	"", "");
			txtNombreLega.requestFocus();
			valido = false;
		} else if (Formato.ValorNulo(txtDescripcionLega.getText())) {
			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,
					MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,
							lblDescripcionLega.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,
							lblDescripcionLega.getId().toString(), ""),
					"", "");
			txtDescripcionLega.requestFocus();
			valido = false;
		}

		return valido;
	}

	@FXML
	public void salir(ActionEvent e) throws Exception {

		if (e != null) {
			Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
			Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el
																	// elemento
			stage.close();
		}
		// Me cierra la ventana
	}
	
	@FXML
	private void btnVerAcuse_Click(ActionEvent e) {
        File ficheroAcuseEntrada = new File(MGeneral.mlform.getPathFicheroAcuseEntrada());
        if (!ficheroAcuseEntrada.exists()) {
            MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.FicheroInexistente,                                   
            		MGeneral.mlform.getPathFicheroAcuseEntrada(),"","");
        } else {
            try {
            	Utilidades.ProcessStartFichero(MGeneral.mlform.getPathFicheroAcuseEntrada());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
