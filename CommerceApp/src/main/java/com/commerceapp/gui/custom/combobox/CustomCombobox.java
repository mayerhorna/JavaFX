package com.commerceapp.gui.custom.combobox;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.domain.legalizacion.kLegalizacion;
import com.commerceapp.maestros.FormatoFichero;
import com.commerceapp.maestros.MaestroCodigoDescripcion;
import com.commerceapp.maestros.TipoLibro;
import com.commerceapp.util.Utilidades;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;

public class CustomCombobox<T> extends ComboBox<T> {
	private static final Logger logger = Logger.getLogger(CustomCombobox.class.getName());
	@FXML
	private Boolean cleanTextWhenNotFound = false;
	@FXML
	private Boolean writable = true;
	private int cursorPosition = 0;
	private String maestro = "";
	private ObservableList<T> originalItems;
	private Utilidades utilidades = new Utilidades();
	private ChangeListener<Boolean> focusChangeListener = new ChangeListener<Boolean>() {
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			if (!newValue) {
				try {

					validarSeleccion();

				} catch (Exception e) {
				}
			}
		}
	};

	public void setCleanTextWhenNotFound(Boolean cleanTextWhenNotFound) {
		this.cleanTextWhenNotFound = cleanTextWhenNotFound == null ? this.cleanTextWhenNotFound : cleanTextWhenNotFound;
	}

	public Boolean getCleanTextWhenNotFound() {
		return this.cleanTextWhenNotFound;
	}

	@Override
	protected ObservableList<Node> getChildren() {
		return super.getChildren();
	}

	public void setWritable(Boolean writable) {
		this.writable = writable == null ? this.writable : writable;
	}

	public Boolean getWritable() {
		return this.writable;
	}

	public <T> T buscarMiembro(kLegalizacion.enumExtensionFichero codigo) {

		/*
		 * if (originalItems.get(0) instanceof TipoLibro) { ObservableList<TipoLibro>
		 * lista=(ObservableList<TipoLibro>) originalItems; for (TipoLibro elemento :
		 * lista) { if (elemento.getDescripcion().equals(codigo)) { return (T) elemento;
		 * // Devuelve el primer elemento que cumple con la condición } } }
		 */

		if (originalItems.get(0) instanceof FormatoFichero) {
			ObservableList<FormatoFichero> lista = (ObservableList<FormatoFichero>) originalItems;
			for (FormatoFichero elemento : lista) {
				logger.info("buscarMiembro: " + elemento.getDescripcion());
				if (elemento.getCodigo() == codigo) {
					return (T) elemento; // Devuelve el primer elemento que cumple con la condición
				}
			}
		}

		return null; // Devuelve null si no se encuentra ningún elemento que cumpla con la condición
	}

	public CustomCombobox() {
		super();
		initialize();
	}

	// @FXML
	private void initialize() {
		setPromptText("Seleccionar opción");
		setVisible(true);
		Platform.runLater(() -> {
			setEditable(writable);
			if (writable) {
				setupAutoComplete();
			}
		});

		ComboBoxListViewSkin<T> comboBoxListViewSkin = new ComboBoxListViewSkin<T>(this);
		comboBoxListViewSkin.getPopupContent().addEventFilter(KeyEvent.ANY, (event) -> {
			if (event.getCode() == KeyCode.SPACE) {
				event.consume();
			}
		});
		this.setSkin(comboBoxListViewSkin);
		this.focusedProperty().addListener(focusChangeListener);

		/*
		 * this.focusedProperty().addListener((observable, oldValue, newValue) -> { if
		 * (!newValue) { // Se ejecutará cuando el ComboBox pierda el foco try {
		 * logger.info("llegamos al foco property"); validarSeleccion(); } catch
		 * (Exception e) { } } });
		 */
		this.setOnShown(event -> {
			this.getEditor().selectAll();
		});

		this.getEditor().setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				this.getEditor().selectAll();
			}
		});
	}

	public void eliminarListener() {
		this.focusedProperty().removeListener(focusChangeListener);
	}

	public void aniadirListener() {
		this.focusedProperty().addListener(focusChangeListener);
	}

	public boolean validarSeleccion() {
		String filter = this.getEditor().getText().toUpperCase();
		if (originalItems.get(0) instanceof MaestroCodigoDescripcion) {
			MaestroCodigoDescripcion aux = new MaestroCodigoDescripcion(maestro);
			if (!aux.existeCodigo(aux.obtenerCodigoDeDescripcion(filter)) && !filter.equals("")) {
				MGeneral.Idioma.MostrarMensaje(EnumMensajes.VerificarElementoLista, "", "", "");
				if (cleanTextWhenNotFound) {
					this.setValue(null);
					this.requestFocus();
				} else {
					this.requestFocus();
					this.getEditor().selectAll();
				}
				return false;
			}
			return true;
		}
		if (originalItems.get(0) instanceof String) {
			String stringElemento = "";
			boolean encontrado = false;
			if (!filter.equals("")) {
				for (T elemento : originalItems) {
					stringElemento = elemento.toString(); // Convertir el elemento a String
					if (stringElemento.equals(filter)) {
						encontrado = true;
						return true;
					}
				}
				if (!encontrado) {
					MGeneral.Idioma.MostrarMensaje(EnumMensajes.VerificarElementoLista, "", "", "");
					if (cleanTextWhenNotFound) {
						this.setValue(null);
						this.requestFocus();
					} else {
						this.requestFocus();
						this.getEditor().selectAll();
					}
					return false;
				}
				return true;
			}
			return true;
		}
		return false;
	}

	public void cargarCombo(String maestro) {

		this.maestro = maestro;
		List<T> maestros = (List<T>) MGeneral.Idioma.obtenerListaMaestro(maestro);
		ObservableList<T> observableList = FXCollections.observableArrayList(maestros);
		this.originalItems = FXCollections.observableArrayList(maestros);
		this.getItems().setAll(observableList);
	}

	public void cargarCombo(List<T> list) {
		if (list.get(0) instanceof TipoLibro) {

			Collections.sort((List<TipoLibro>) list, new Comparator<TipoLibro>() {

				@Override
				public int compare(TipoLibro o1, TipoLibro o2) {
					// TODO Auto-generated method stub
					return o1.getDescripcion().compareTo(o2.getDescripcion());
				}
			});

		}

		ObservableList<T> observableList = FXCollections.observableArrayList(list);
		this.originalItems = observableList;
		this.getItems().setAll(observableList);
	}

	private void setupAutoComplete() {

		TextField editor = this.getEditor();

		editor.setOnKeyTyped(event -> {
			String input = editor.getText();

			if (isEnterPressed(event) || isTabPressed(event)) {
				String text = editor.getText().toUpperCase();
				editor.setText(text);
				selectItem(text);
				event.consume();
				return;
			}
		});

		editor.setOnKeyReleased(event -> {
			if (isEnterPressed(event) || isTabPressed(event)) {
				return;
			}
			String input = editor.getText();
			cursorPosition = input.length();
			if ("".equals(input)) {
				this.setValue(null);
				return;
			}
			if (event.getCode() == KeyCode.DOWN) {
				selectNext();
				event.consume();
				return;
			} else if (event.getCode() == KeyCode.UP) {
				selectPrevious();
				event.consume();
				return;
			} else if (isBackspacePressed(event)) {
				event.consume();
				return;
			}

			T item = search(input);
			if (item == null) {
				return;
			}
			String suggestedText = "";
			if (item instanceof TipoLibro) {
				suggestedText = ((TipoLibro) item).getDescripcion();
			}
			if (item instanceof MaestroCodigoDescripcion) {
				suggestedText = ((MaestroCodigoDescripcion) item).getDescripcion();
			}
			if (item instanceof String) {
				suggestedText = ((String) item).toString();
			}
			if (suggestedText.toLowerCase().startsWith(input.toLowerCase())
					&& !suggestedText.toLowerCase().equals(input.toLowerCase())) {
				int $cursorPosition = cursorPosition;
				editor.setText(input + suggestedText.substring(input.length()));
				editor.positionCaret($cursorPosition);
				editor.selectRange($cursorPosition, editor.getText().length());
			}
		});
		this.setOnShowing(event -> {
			String textoEscrito = this.getEditor().getText();
			selectItem(textoEscrito);
			int selectedIndex = this.getSelectionModel().getSelectedIndex();

			ComboBoxListViewSkin<?> skin = (ComboBoxListViewSkin<?>) this.getSkin();
			if (skin != null) {
				((ListView<?>) skin.getPopupContent()).scrollTo(selectedIndex);
			}
		});

	}

	private boolean isEnterPressed(KeyEvent event) {
		return event.getCode() == KeyCode.ENTER || event.getCharacter().charAt(0) == '\n'
				|| event.getCharacter().charAt(0) == '\r';
	}

	private boolean isTabPressed(KeyEvent event) {
		return event.getCode() == KeyCode.TAB || event.getCharacter().charAt(0) == '\t';
	}

	private boolean isBackspacePressed(KeyEvent event) {
		return event.getCode() == KeyCode.BACK_SPACE || event.getCharacter().charAt(0) == '\b';
	}

	private T search(String input) {
		if (originalItems == null) {
			return null;
		}
		for (T item : originalItems) {
			String text = "";
			if (item instanceof TipoLibro) {
				text = ((TipoLibro) item).getDescripcion();
			}
			if (item instanceof MaestroCodigoDescripcion) {
				text = ((MaestroCodigoDescripcion) item).getDescripcion();
			}
			if (item instanceof String) {
				text = ((String) item).toString();
			}
			if (text.toLowerCase().startsWith(input.toLowerCase())) {
				return item;
			}
		}
		return null;
	}

	private void selectNext() {
		if (this.getSelectionModel().isEmpty()) {
			this.getSelectionModel().selectFirst();
		} else {
			int currentIndex = this.getSelectionModel().getSelectedIndex();
			if (currentIndex < this.getItems().size() - 1) {
				this.getSelectionModel().select(currentIndex + 1);
			}
		}
		this.getEditor().selectAll();
	}

	private void selectPrevious() {
		if (this.getSelectionModel().isEmpty()) {
			this.getSelectionModel().selectLast();
		} else {
			int currentIndex = this.getSelectionModel().getSelectedIndex();
			if (currentIndex > 0) {
				this.getSelectionModel().select(currentIndex - 1);
			}
		}
		this.getEditor().selectAll();
	}

	private void selectItem(String inputText) {
		if (inputText == null)
			return;
		for (T item : this.getItems()) {
			String text = "";

			if (item instanceof TipoLibro) {
				text = ((TipoLibro) item).getDescripcion();
			}
			if (item instanceof MaestroCodigoDescripcion) {
				text = ((MaestroCodigoDescripcion) item).getDescripcion();
			}
			if (item instanceof String) {
				text = ((String) item).toString();
			}
			if (text.equalsIgnoreCase(inputText)) {

				this.setValue(item);
				break;
			}
		}
	}
}
