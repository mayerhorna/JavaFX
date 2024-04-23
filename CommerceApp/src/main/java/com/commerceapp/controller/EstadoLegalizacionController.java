package com.commerceapp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.IdiomaC.EnumLiterales;
import com.commerceapp.domain.legalizacion.MensajesReglasC;
import com.commerceapp.domain.legalizacion.cFicheroLibro;
import com.commerceapp.domain.legalizacion.cLibro;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.reporting.instancia.ReportingPreviewService;
import com.commerceapp.util.Utilidades;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EstadoLegalizacionController implements Initializable {

	@FXML
	private Button btnImprimir;

	@FXML
	private Button btnSalir;

	@FXML
	private AnchorPane frmEstadoLegalizacion;

	@FXML
	private Label lblDetalleLegalizacion;

	@FXML
	private Label lblIdentificador;

	@FXML
	private Label lblTitulo1;

	@FXML
	private Label lblTitulo2;

	@FXML
	private ListView<String> lstAvisos;

	@FXML
	private ListView<String> lstErrores;

	@FXML
	private TableView<cFicheroLibro> lsvLibros;

	@FXML
	private Label txtDetalleLegalizacion;

	@FXML
	private Label txtIdentificador;
	@FXML
	private TableColumn<cFicheroLibro, String> cTipo;
	@FXML
	private TableColumn<cFicheroLibro, String> cNumero;
	@FXML
	private TableColumn<cFicheroLibro, String> cFichero;
	@FXML
	private TableColumn<cFicheroLibro, String> cHuella;
	@FXML
	private TableColumn<cFicheroLibro, String> cVisualizacion;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		cTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
		cNumero.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(data.getValue().getNumero())));
		cFichero.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreFichero()));
		cHuella.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHuellaManual()));
		cVisualizacion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisualizacionCorrecta()
				? MGeneral.Idioma.obtenerLiteral(EnumLiterales.Correcta).toString()
				: MGeneral.Idioma.obtenerLiteral(EnumLiterales.Erronea).toString()));

		// Define la fábrica de celdas personalizada
		cVisualizacion.setCellFactory(column -> {
			return ManejarCeldaColumnaVisualizacion();
		});

		cHuella.setCellFactory(column -> {
			return ManejarCeldaColumnaHuella();
		});

		cargarLibrosEnGrid();
		validar();
		establecerValoresIniciales();
	}

	public void btnSalir_Click(ActionEvent e) throws Exception {

		close(e);

	}

	public void btnImprimir_Click(ActionEvent e) throws Exception {

		ReportingPreviewService.generarReporteHojaDiagnostico();

	}

	public void cargarLibrosEnGrid() {
		ArrayList<cFicheroLibro> listaFicheros = new ArrayList<>();
		try {
			lsvLibros.getItems().clear();

			for (cLibro libro : MGeneral.mlform.Libros) {
				for (cFicheroLibro fichero : libro.Ficheros) {
					listaFicheros.add(fichero);

				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ObservableList<cFicheroLibro> data = FXCollections.observableArrayList(listaFicheros);
		lsvLibros.setItems(data);
	}

	private void close(ActionEvent e) {
		Node source = (Node) e.getSource();
		Stage stage = (Stage) source.getScene().getWindow();

		stage.close();
	}

	public static TableCell<cFicheroLibro, String> ManejarCeldaColumnaVisualizacion() {
		return new TableCell<cFicheroLibro, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
					setStyle("");
				} else {
					setText(item);

					// Cambia el color de fondo de la celda según el contenido
					if (item.equals(MGeneral.Idioma.obtenerLiteral(EnumLiterales.Correcta).toString())) {
						setStyle("-fx-background-color: darkgreen; -fx-text-fill: white;");
					} else if (item.equals(MGeneral.Idioma.obtenerLiteral(EnumLiterales.Erronea).toString())) {
						setStyle("-fx-background-color: red; -fx-text-fill: white;");
					} else {
						setStyle(""); // Opcional: Restaura el color de fondo predeterminado
					}
				}
			}
		};
	}

	public static TableCell<cFicheroLibro, String> ManejarCeldaColumnaHuella() {
		return new TableCell<cFicheroLibro, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
					setStyle("");
				} else {
					setText(item);

					cFicheroLibro ficheroLibro = getTableView().getItems().get(getIndex());

					// Obtiene la huella manual y la huella calculada del libro actual
					String huellaManual = ficheroLibro.getHuellaManual();
					String huellaCalculada = ficheroLibro.getHuellaCalculada();

					// Cambia el color de fondo y de texto de la celda según la comparación entre
					// huellas
					if (huellaManual.equals(huellaCalculada)) {
						setStyle("-fx-background-color: darkgreen; -fx-text-fill: white;");
					} else {
						setStyle("-fx-background-color: red; -fx-text-fill: white;");
					}
				}
			}
		};
	}

	private void establecerValoresIniciales() {
		txtIdentificador.setText(MGeneral.mlform.Datos.get_IdentificadorEntrada());
		txtDetalleLegalizacion.setText((MGeneral.mlform.getDetalleLegalizacion()));
	}

	private void validar() {
		kLegalizacion.enumResultadoValidacion resultado = MGeneral.mlform.valida();

		if (resultado != kLegalizacion.enumResultadoValidacion.Valida) {
			for (int i = 0; i < MGeneral.mlform.MensajesReglas.length; i++) {
				if (MGeneral.mlform.MensajesReglas[i].getGrado() == MensajesReglasC.Grado.EsError) {
					lstErrores.getItems().add(MGeneral.mlform.MensajesReglas[i].getTextoMensaje());
				}
				if (MGeneral.mlform.MensajesReglas[i].getGrado() == MensajesReglasC.Grado.EsAviso) {
					lstAvisos.getItems().add(MGeneral.mlform.MensajesReglas[i].getTextoMensaje());
				}
			}
		}
	}

}
