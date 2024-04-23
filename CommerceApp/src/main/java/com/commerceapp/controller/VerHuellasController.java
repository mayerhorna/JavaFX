package com.commerceapp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.legalizacion.cFicheroLibro;
import com.commerceapp.domain.legalizacion.cLibro;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.service.LegalizacionService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VerHuellasController implements Initializable {
	MenuPrincipalController parentController;
	private static final Logger logger = Logger.getLogger(VerHuellasController.class.getName());

	@FXML
	private TableColumn<cFicheroLibro, String> cTipo;
	@FXML
	private TableColumn<cFicheroLibro, String> cNumero;
	@FXML
	private TableColumn<cFicheroLibro, String> cFichero;
	@FXML
	private TableColumn<cFicheroLibro, String> cHuella;
	@FXML
	private TableView<cFicheroLibro> lsvLibros;
	@FXML
	private TextField txtHuellaDigital;
	int itemseleccionado = -1;

	public MenuPrincipalController getParentController() {
		return parentController;
	}

	public void setParentController(MenuPrincipalController parentController) {
		this.parentController = parentController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		cTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
		cNumero.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(data.getValue().getNumero())));
		cFichero.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreFichero()));
		cHuella.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHuella()));

		MGeneral.Idioma.cargarIdiomaControles(null, lsvLibros);

		cargarLibrosEnGrid();
		lsvLibros.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			llenarDesdeGrid();
		});
		lsvLibros.getSelectionModel().select(0);
		// seleccionarSiguiente();

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

	public int[] dameIndicesLibroDeGrid(int indiceEnGrid, int indiceLibro, int indiceFichero) {

		try {
			indiceLibro = -1;
			indiceFichero = -1;

			int cont = -1;

			for (int i = 0; i < MGeneral.mlform.getNumeroLibros(); i++) {
				for (int j = 0; j < MGeneral.mlform.Libros.get(i).getNumeroFicheros(); j++) {
					cont++;

					if (cont == indiceEnGrid) {
						indiceLibro = i;
						indiceFichero = j;
						int indices[] = { indiceEnGrid, indiceLibro, indiceFichero };
						return indices;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return null;

	}

	@FXML
	private void btnSalir_Click(ActionEvent e) {
		close(e);
	}

	private void close(ActionEvent e) {
		Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
		Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el elemento
		stage.close();
	}

	private void llenarDesdeGrid() {
		try {
			int indicelibro = 0;
			int indicefichero = 0;
			int indices[];
			if (lsvLibros.getSelectionModel().getSelectedItems().isEmpty()) {
				return;
			}
			int indice = lsvLibros.getSelectionModel().getSelectedIndices().get(0);
			if ((dameIndicesLibroDeGrid(indice, indicelibro, indicefichero) == null)) {
				return;
			} else {
				indices = dameIndicesLibroDeGrid(indice, indicelibro, indicefichero);
			}

			txtHuellaDigital.setText(MGeneral.mlform.Libros.get(indices[1]).Ficheros[indices[2]].getHuella());

			itemseleccionado = lsvLibros.getSelectionModel().getSelectedIndices().get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "", "", ex.getMessage());
		}
	}

	private void seleccionarSiguiente() {

		if (lsvLibros.getItems().isEmpty())
			return;

		itemseleccionado++;
		if (itemseleccionado >= lsvLibros.getItems().size())
			itemseleccionado = 0;

		lsvLibros.getSelectionModel().select(itemseleccionado);
	}

}
