package com.commerceapp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.IdiomaC.EnumLiterales;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.domain.legalizacion.cFicheroLibro;
import com.commerceapp.domain.legalizacion.cLibro;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.util.Formato;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class EspecificarHuellasController implements Initializable {
	private MenuPrincipalController parentController;

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
	@FXML
	private TableView<cFicheroLibro> lsvLibros;
	@FXML
	private RadioButton rdbCorrecta;
	@FXML
	private RadioButton rdbErronea;
	@FXML
	private TextField txtHuellaDigital;
	int itemseleccionado = -1;
	int longitudHuella;

	public void setParentController(MenuPrincipalController parentController) {
		this.parentController = parentController;

	}

	public MenuPrincipalController getParentController() {
		return parentController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setHandlers();

		if (MGeneral.mlform.Datos.getFormato() == kLegalizacion.enumFormato.Legalia) {
			longitudHuella = kLegalizacion.kLongitudHuellaMD5;
		}
		if (MGeneral.mlform.Datos.getFormato() == kLegalizacion.enumFormato.Legalia2) {
			longitudHuella = kLegalizacion.kLongitudHuellaSHA256;
		}

		ToggleGroup toggleGroup = new ToggleGroup();
		rdbCorrecta.setToggleGroup(toggleGroup);
		rdbErronea.setToggleGroup(toggleGroup);
		rdbCorrecta.setSelected(true);
		// TODO Auto-generated method stub

		cTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
		cNumero.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(data.getValue().getNumero())));
		cFichero.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreFichero()));
		cHuella.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHuellaManual()));
		cVisualizacion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisualizacionCorrecta()
				? MGeneral.Idioma.obtenerLiteral(EnumLiterales.Correcta).toString()
				: MGeneral.Idioma.obtenerLiteral(EnumLiterales.Erronea).toString()));

		MGeneral.Idioma.cargarIdiomaControles(null, lsvLibros);

		cargarLibrosEnGrid();
		lsvLibros.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			llenarDesdeGrid();
		});
		lsvLibros.getSelectionModel().select(0);
		txtHuellaDigital.requestFocus();
	
	}

	private void setHandlers() {
		txtHuellaDigital.setOnKeyPressed(this::evAsigarHuellaAvanceItem);
		lsvLibros.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			llenarDesdeGrid();
		});
	}

	private void evAsigarHuellaAvanceItem(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			if (txtHuellaDigital.getText().length() >= longitudHuella) {
				modificar();
			} else {

				e.consume();
			}
		}
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

			rdbCorrecta
					.setSelected(MGeneral.mlform.Libros.get(indices[1]).Ficheros[indices[2]].isVisualizacionCorrecta());
			rdbErronea.setSelected(
					!MGeneral.mlform.Libros.get(indices[1]).Ficheros[indices[2]].isVisualizacionCorrecta());

			itemseleccionado = lsvLibros.getSelectionModel().getSelectedIndices().get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "", "", ex.getMessage());
		}
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

	@FXML
	private void btnAbrir_Click(ActionEvent e) {
		try {
			if (lsvLibros.getSelectionModel().getSelectedItems().size() == 0)
				return;

			int indicelibro = 0;
			int indicefichero = 0;

			int indice = lsvLibros.getSelectionModel().getSelectedIndex();

			int[] indices = dameIndicesLibroDeGrid(indice, indicelibro, indicefichero);
			if (indices == null)
				return;

			indicelibro = indices[1];
			indicefichero = indices[2];

			MGeneral.mlform.verLibroFichero(indicelibro, indicefichero);

		} catch (Exception ex) {
			IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "", "", ex.getMessage());
		}
	}

	@FXML
	private void btnSalir_Click(ActionEvent e) {
		getParentController().form.validar(false);
		close(e);

	}

	@FXML
	private void btnModificar_Click(ActionEvent e) {
		modificar();
	}

	public boolean comprueba() {
		boolean resultado = true;

		if (lsvLibros.getSelectionModel().getSelectedItems().isEmpty())
			return resultado;

		if (!Formato.ValorNulo(txtHuellaDigital.getText())) {
			if (txtHuellaDigital.getText().length() != longitudHuella) {
				IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.HuellaDigitalMaximo, String.valueOf(longitudHuella), "",
						"");
				resultado = false;
			} // Es para que se pueda Modificar sin la huella
		}

		return resultado;
	}

	public void modificar() {
		try {
			if (lsvLibros.getSelectionModel().getSelectedItems().isEmpty())
				return;

			int indicelibro = 0;
			int indicefichero = 0;

			int indice = lsvLibros.getSelectionModel().getSelectedIndices().get(0);

			int[] indices = dameIndicesLibroDeGrid(indice, indicelibro, indicefichero);
			if (indices == null) {
				return;
			}

			indicelibro = indices[1];
			indicefichero = indices[2];

			if (!comprueba()) {
				return;
			}

			if (!Formato.ValorNulo(txtHuellaDigital.getText())) {
				MGeneral.mlform.Libros.get(indicelibro).Ficheros[indicefichero]
						.setHuellaManual(txtHuellaDigital.getText());
				// MGeneral.mlform.Libros.get(indicelibro).getFicheros().get(indicefichero).setHuellaManual(txtHuellaDigital.getText());
			}
			MGeneral.mlform.Libros.get(indicelibro).Ficheros[indicefichero]
					.setVisualizacionCorrecta(rdbCorrecta.isSelected());
			// MGeneral.mlform.Libros.get(indicelibro).getFicheros().get(indicefichero).setVisualizacionCorrecta(rdbCorrecta.isSelected());

			cargarLibrosEnGrid();

			seleccionaSiguiente();

			txtHuellaDigital.clear();
			txtHuellaDigital.requestFocus();

		} catch (

		Exception ex) {
			IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "", "", ex.getMessage());
		}
	}

	private void close(ActionEvent e) {
		Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
		Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el elemento
		stage.close();
	}

	public void seleccionaSiguiente() {
		if (lsvLibros.getItems().size() <= 0) {
			return;
		}

		itemseleccionado++;

		if (itemseleccionado == lsvLibros.getItems().size()) {
			itemseleccionado = 0;
		}

		lsvLibros.getSelectionModel().select(itemseleccionado);
	}

}
