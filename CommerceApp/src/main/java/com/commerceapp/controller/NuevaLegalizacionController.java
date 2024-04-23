package com.commerceapp.controller;

import java.net.URL;

import java.nio.file.Files;

import java.nio.file.Paths;

import java.time.LocalDate;

import java.time.ZoneId;

import java.util.Date;

import java.util.ResourceBundle;

import java.util.logging.Logger;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.idioma.ElementosIdiomaC;
import com.commerceapp.domain.idioma.ObjetosIdioma;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.maestros.MaestroCodigoDescripcion;
import com.commerceapp.service.LegalizacionService;
import com.commerceapp.util.Ficheros;
import com.commerceapp.util.Formato;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.Node;

import javafx.scene.control.Label;

import javafx.scene.control.TextField;

import javafx.stage.Stage;

public class NuevaLegalizacionController implements Initializable {

	private static final Logger logger = Logger.getLogger(NuevaLegalizacionController.class.getName());

	EntradaDatosController form;

	@FXML

	private TextField txtDescripcionLega;

	@FXML

	private TextField txtNombreLega;

	@FXML

	private Label lblNombreLega;

	@FXML

	private Label lblDescripcionLega;

	private MenuPrincipalController parentController;

	@Override

	public void initialize(URL location, ResourceBundle resources) {

		txtNombreLega.requestFocus();

	}

	public void setParentController(MenuPrincipalController parentController) {

		this.parentController = parentController;

	}

	public MenuPrincipalController getParentController() {

		return parentController;

	}

	@FXML

	public void Nuevo(ActionEvent e) throws Exception {

		Node source = (Node) e.getSource();

		Stage stage = (Stage) source.getScene().getWindow();

		try {

			if (Formato.ValorNulo(txtNombreLega.getText())) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,

						MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,

								stage.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,

								lblNombreLega.getId(), ""),

						"", "");

				txtNombreLega.requestFocus();

				return;

			}

			if (Formato.ValorNulo(txtDescripcionLega.getText())) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.CampoObligatorio,

						MGeneral.Idioma.obtenerValor(ObjetosIdioma.FORMULARIOS,

								stage.getScene().getRoot().getId().toString(), ElementosIdiomaC.TEXT_CONTROLES,

								lblDescripcionLega.getId(), ""),

						"", "");

				txtDescripcionLega.requestFocus();

				return;

			}

			// El nombre de la nueva legalización debe ser un nombre de directorio válido

			if (!Ficheros.NombreValido(txtNombreLega.getText())) {

				MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NombreFicheroDirectorioNoValido, "", "", "");

				txtNombreLega.requestFocus();

				return;

			}

			String directorioLegalizacion = "";

			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {

				directorioLegalizacion = MGeneral.Configuracion.getPathDatos() + "\\"

						+ txtNombreLega.getText();

			} else if ((os.contains("nix") || os.contains("nux"))) {

				directorioLegalizacion = MGeneral.Configuracion.getPathDatos() + "/" + txtNombreLega.getText();

			}

			if (Files.exists(Paths.get(MGeneral.Configuracion.getPathDatos()))) {

				// El directorio a crear no debe existir

				if (Files.exists(Paths.get(directorioLegalizacion))) {

					MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.NombreLegalizacionExistente, "", "", "");

					txtNombreLega.requestFocus();

					return;

				}

				Files.createDirectory(Paths.get(directorioLegalizacion));

				LegalizacionService ml = new LegalizacionService(true, null);

				ml.inicializa(directorioLegalizacion);

				ml.Datos.set_Descripcion(txtDescripcionLega.getText());

				// Asignación de los valores por defecto de algunos campos de la legalización

				ml.Datos.set_TipoPersona(kLegalizacion.KTipoPersonaDefecto);

				ml.Presentacion.setFechaSolicitud(ml

						.dameCadenaFecha(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())));

				ml.Presentacion

						.setRegistroMercantilDestinoCodigo(MGeneral.Configuracion.getValorDefectoCodigoRegistro());

				MaestroCodigoDescripcion maestro = new MaestroCodigoDescripcion("Registros");

				ml.Presentacion.setRegistroMercantilDestinoDescripcion(

						maestro.obtenerDescripcion(ml.Presentacion.getRegistroMercantilDestinoCodigo()));

				ml.Presentacion.setProvinciaCodigo(MGeneral.Configuracion.getValorDefectoCodigoProvincia());

				ml.Presentacion._Presentante.ponDatosPresentantePorDefecto();

				ml.guarda();

				// Apertura de la nueva legalización

				getParentController().EntradaDatos(directorioLegalizacion);

				// Cierre del formulario actual

				stage.close();

				if (form != null) {

					form.aniadirListener();

				}

			}

		} catch (Exception ex) {

			MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "", "", "");

		}

	}

	@FXML

	public void Cancelar(ActionEvent e) throws Exception {

		Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click

		Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el elemento

		stage.close(); // Me cierra la ventana

	}

}
