package com.commerceapp.util;

import java.awt.Component;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import com.commerceapp.Main;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.domain.IdiomaC.EnumLiterales;
import com.commerceapp.domain.legalizacion.cFicheroLibro;
import com.commerceapp.gui.custom.imageview.CustomImageView;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Utilidades {
	private static final Logger logger = Logger.getLogger(Utilidades.class.getName());
	public ArrayList<TimeLineClass> arrayListTimeline = new ArrayList<>();
	public Thread hilo;

	public static <T> boolean arrayRemoveAt(T[] arr, int index) {
		int uBound = arr.length - 1;
		int lBound = 0;
		int arrLen = uBound - lBound;

		if (index < lBound || index > uBound) {
			return false;
			// throw new IndexOutOfBoundsException(String.format("Index must be from %d to
			// %d.", lBound, uBound));
		} else {
			// create an array 1 element less than the input array
			T[] outArr = (T[]) new Object[arrLen];

			// copy the first part of the input array
			System.arraycopy(arr, 0, outArr, 0, index);

			// then copy the second part of the input array
			System.arraycopy(arr, index + 1, outArr, index, uBound - index);

			System.arraycopy(outArr, 0, arr, 0, arrLen);

			return true;
			// return outArr;
		}
	}

	public void avanceControlesForm(Node f, Object sender, KeyEvent event) {

		if (event.getCode() == KeyCode.ENTER) {
			Node currentNode = (Node) event.getSource();
			Node nextNode = getNextNode(currentNode);
			if (nextNode != null) {
				nextNode.requestFocus();
			}
		}
	}

	private Node getNextNode(Node currentNode) {

		// Obtiene todos los nodos hijos del contenedor
		javafx.scene.Parent parent = currentNode.getParent();
		java.util.List<Node> children = parent.getChildrenUnmodifiable();

		for (int i = 0; i < children.size(); i++) {
			logger.info(children.get(i).toString());
		}

		// Encuentra el índice del nodo actual
		int currentNodeIndex = children.indexOf(currentNode);

		// Encuentra el próximo nodo enfocable
		for (int i = currentNodeIndex + 1; i < children.size(); i++) {
			Node node = children.get(i);
			if (node.isFocusTraversable()) {
				return node;
			}
		}
		return null;
	}

	public static void evSoloNumeros(Object sender, KeyEvent event) {
		logger.info("validadndo numero");

		if (!(Character.isDigit(event.getCharacter().charAt(0))
				|| Character.isISOControl(event.getCharacter().charAt(0))
				|| Character.isWhitespace(event.getCharacter().charAt(0)))) {
			event.consume();
		}

		/*
		 * if (!event.getCode().isDigitKey() || event.getCode() != KeyCode.BACK_SPACE) {
		 * event.consume(); }
		 */
	}

	/*
	 * public static void evVerificarContenidoCombo(Object sender, KeyEvent e) { if
	 * (e.getKeyCode() == KeyEvent.VK_ENTER) { javax.swing.JComboBox<?> comboBox =
	 * (javax.swing.JComboBox<?>) sender;
	 * 
	 * if (comboBox.getSelectedItem() == null) { return; }
	 * 
	 * String inputText = comboBox.getEditor().getItem().toString(); int result =
	 * -1;
	 * 
	 * for (int i = 0; i < comboBox.getItemCount(); i++) { if
	 * (comboBox.getItemAt(i).toString().equals(inputText)) { result = i; break; } }
	 * 
	 * if (result < 0) { // Mostrar el mensaje equivalente a
	 * Idioma.MostrarMensaje(IdiomaC.enumMensajes.VerificarElementoLista,
	 * MessageBoxIcon.Exclamation, MessageBoxButtons.OK)
	 * System.out.println("Verificar Elemento Lista");
	 * comboBox.getEditor().getEditorComponent().requestFocus(); } } }
	 */

	public static void cursorEspera(Stage stage, boolean bool) {

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				if (bool) {
					Thread.sleep(3000);
				}

				return null;
			}
		};

		task.setOnSucceeded(e -> stage.getScene().setCursor(Cursor.DEFAULT));
		task.setOnFailed(e -> stage.getScene().setCursor(Cursor.DEFAULT));

		stage.getScene().setCursor(Cursor.WAIT);
		new Thread(task).start();

	}

	public static void cursorEsperaJavaFX(Stage stage, boolean bool, Duration duracionEspera) {
		if (bool) {
			// Mostrar el cursor de espera
			Platform.runLater(() -> stage.getScene().setCursor(Cursor.WAIT));

			// Ejecutar la tarea que lleva tiempo en un hilo
			Thread taskThread = new Thread(() -> {
				// Simular una tarea que tarda un tiempo en completarse
				try {
					Thread.sleep(3000); // Simula una tarea que tarda 3 segundos
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Restaurar el cursor a su estado predeterminado después de completar la tarea
				Platform.runLater(() -> stage.getScene().setCursor(Cursor.DEFAULT));
			});
			taskThread.start();
		} else {
			// Cambiar el cursor directamente a DEFAULT
			Platform.runLater(() -> stage.getScene().setCursor(Cursor.DEFAULT));
		}
	}

	public static void controlHabilitado(Control c, boolean habilitado) {
		c.setDisable(!habilitado);
	}

	public static <T> T buscaControlPorNombre(Parent parent, String id) {

		String nodeId = null;

		if (parent instanceof TitledPane) {
			TitledPane titledPane = (TitledPane) parent;
			Node content = titledPane.getContent();
			nodeId = content.idProperty().get();

			if (nodeId != null && nodeId.equals(id)) {
				return (T) content;
			}

			if (content instanceof Parent) {
				T child = buscaControlPorNombre((Parent) content, id);

				if (child != null) {
					return child;
				}
			}
		}

		for (Node node : parent.getChildrenUnmodifiable()) {
			nodeId = node.idProperty().get();
			if (nodeId != null && nodeId.equals(id)) {
				return (T) node;
			}

			if (node instanceof SplitPane) {
				SplitPane splitPane = (SplitPane) node;
				for (Node itemNode : splitPane.getItems()) {
					nodeId = itemNode.idProperty().get();

					if (nodeId != null && nodeId.equals(id)) {
						return (T) itemNode;
					}

					if (itemNode instanceof Parent) {
						T child = buscaControlPorNombre((Parent) itemNode, id);

						if (child != null) {
							return child;
						}
					}
				}
			} else if (node instanceof Accordion) {
				Accordion accordion = (Accordion) node;
				for (TitledPane titledPane : accordion.getPanes()) {
					nodeId = titledPane.idProperty().get();

					if (nodeId != null && nodeId.equals(id)) {
						return (T) titledPane;
					}

					T child = buscaControlPorNombre(titledPane, id);

					if (child != null) {
						return child;
					}
				}
			} else if (node instanceof Parent) {
				T child = buscaControlPorNombre((Parent) node, id);

				if (child != null) {
					return child;
				}
			}
		}
		return null;
	}

	public static <T extends Node> T getNodeCSS(Node root, String id) {
		final Node node = root.lookup(id);

		if (node == null) {
			throw new NullPointerException("cannot find child node fx:id for argument: " + id);
		} /* www . j av a2 s . c o m */

		return (T) node;
	}

	

	public static Control getChildByID(Parent parent, String id) {
		for (Node node : parent.getChildrenUnmodifiable()) {
			if (node instanceof ScrollPane) {
				ScrollPane control = (ScrollPane) node;
				Node nodoAux = control.getContent();
				Control aux = buscaControlPorNombre((Parent) nodoAux, id);
				if (aux != null) {
					return aux;
				}
			} else if (node instanceof Parent) {

				Parent padre = (Parent) node;
				for (Node nodD : padre.getChildrenUnmodifiable()) {
					logger.info("Elemento:" + nodD.toString());
					if (nodD instanceof ImageView) {
						return null;
					}

					Control control = null;
					try {
						if (nodD.getId().equals(id)) {
							return (Control) nodD;
						}
						control = buscaControlPorNombre((Parent) nodD, id);
					} catch (Exception ex) {
						logger.info("Error");
					} finally {

						if (control != null) {
							return control;
						}
					}

				}

			}
		}

		return null;
	}

	public static Control buscarControlPorId(Parent formulario, String nombreControlBuscado) {
		logger.info("\nbuscaControlPorNombre: " + formulario.toString() + "\nBuscando: " + nombreControlBuscado);
		Node nodoAux = null;
		Parent parent;
		if (formulario instanceof Parent) {

			if (formulario instanceof ScrollPane) {
				ScrollPane scroll = (ScrollPane) formulario;
				nodoAux = scroll.getContent();
			} else {
				nodoAux = formulario;
			}
			parent = (Parent) nodoAux;

			for (Node node : parent.getChildrenUnmodifiable()) {

				try {
					Control aux = buscaSubControlPorNombre(node, nombreControlBuscado);

					if (aux != null && aux.getId().equals(nombreControlBuscado)) {

						return aux;

					}

				} catch (Exception e) {
					logger.info("buscaControlPorNombre Error: " + formulario.toString());

				}

			}

		}
		logger.info(
				"\nbuscaControlPorNombre nulo: " + formulario.toString() + "\nNo se encontro:" + nombreControlBuscado);
		return null;
	}

	public static Control buscaSubControlPorNombre(Node node, String nombreControlBuscado) {
		Control controlAux = null;
		Node nodoAux = null;
		Parent parent;
		logger.info("buscaSubControlPorNombre:" + node.toString());
		try {
			if (node.getId().equals(nombreControlBuscado)) {
				return (Control) node;
			}
		} catch (Exception ex) {
			logger.info("No hay ID");
			// ex.printStackTrace();
		} finally {

			if (node instanceof Parent) {
				if (node instanceof ScrollPane) {
					ScrollPane scroll = (ScrollPane) node;
					nodoAux = scroll.getContent();
				} else {
					nodoAux = node;
				}
				parent = (Parent) nodoAux;

				for (javafx.scene.Node subControl : parent.getChildrenUnmodifiable()) {
					if (subControl instanceof Label) {
						return (Control) subControl;
					}
					if (subControl instanceof Label) {
						return (TextField) subControl;
					}

					controlAux = buscaSubControlPorNombre(subControl, nombreControlBuscado);
					if (controlAux != null) {
						logger.info("Resultado: " + controlAux.toString());
						return controlAux;
					}

				}

			}

		}
		logger.info("buscaSubControlPorNombre Resultado nulo: " + node.toString());
		return controlAux;

	}

	public static void evVerificarContenidoCombo(Object sender, KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			ComboBox<?> comboBox = (ComboBox<?>) sender;

			if (comboBox.getValue() == null) {
				return;
			}

			String inputText = comboBox.getEditor().toString();
			int result = -1;

			for (int i = 0; i < comboBox.getItems().size(); i++) {
				if (comboBox.getItems().get(i).toString().equals(inputText)) {
					result = i;
					break;
				}
			}

			if (result < 0) { // Mostrar el mensaje equivalente a
				// MGeneral.Idioma.MostrarMensaje(IdiomaC.EnumMensajes.VerificarElementoLista,
				// "", "", "");

				comboBox.requestFocus();
			}
		}
	}

	public static void obtenerFactoresEscala(Pane contenedor, double factorDeEscalaX, double factorDeEscalaY) {
		try {
			final double anchoDisenio = 1280;
			final double altoDisenio = 768;

			// Obtener la resolución actual de la pantalla
			double ancho = Screen.getPrimary().getBounds().getWidth();
			double alto = Screen.getPrimary().getBounds().getHeight();

			factorDeEscalaX = ancho / anchoDisenio;
			factorDeEscalaY = alto / altoDisenio;

			// Obtener la ventana asociada al Pane
			Stage stage = (Stage) contenedor.getScene().getWindow();

			// Obtener la configuración del tamaño de fuentes de Windows usando la pantalla
			// principal
			int userDpi = (int) Screen.getPrimary().getDpi();

			if (userDpi > 125) {
				userDpi = userDpi - 15; // Un 15% menos de reducción
			}

			// Si el tamaño de fuentes es distinto al de diseño (96), se calcula el factor
			int ajusteDpi = userDpi - 96;
			if (ajusteDpi > 0) {
				factorDeEscalaX = factorDeEscalaX * (100 - ajusteDpi) / 100;
				factorDeEscalaY = factorDeEscalaY * (100 - ajusteDpi) / 100;
			} else if (ajusteDpi < 0) {
				factorDeEscalaX = factorDeEscalaX * (100 - ajusteDpi) / 100;
				factorDeEscalaY = factorDeEscalaY * (100 - ajusteDpi) / 100;
			}

		} catch (Exception ex) {
			factorDeEscalaX = 1.0;
			factorDeEscalaY = 1.0;
		}
	}

	public static void ProccessStarURL(String url) {
		try {
			HostServices hostServices = Main.getHostService();

			hostServices.showDocument(url);

		} catch (Exception e) {
			System.out.println("Error al abrir el enlace: " + e.getMessage());
		}
	}

	public static void ProcessStartFichero(String file) throws IOException {
		// Codigo multiplataforma - Obtener el sistema operativo
		String os = System.getProperty("os.name").toLowerCase();

		String comando = "";
		if (os.contains("win")) {
			// Sistema Windows
			comando = "explorer \"" + file + "\"";
		} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
			// Sistemas basados en Unix (Linux, Mac)
			comando = "xdg-open " + file;
			logger.info(comando);
		} else {
			// Sistema operativo no compatible
			System.out.println("Sistema operativo no compatible para abrir el archivo PDF.");
			return;
		}

		// Ejecutar el comando
		try {
			Process process = Runtime.getRuntime().exec(comando);
			int exitCode = process.waitFor();
			if (exitCode != 0) {

				BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String line;
				while ((line = errorReader.readLine()) != null) {
					System.out.println(line);
				}
				errorReader.close();
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static class TimeLineClass {
		public Timeline timeline;
		public Control ctrl;

		public TimeLineClass(Timeline timeline, Control ctrl) {
			this.timeline = timeline;
			this.ctrl = ctrl;
		}
	}

	public static void copyDirectory(File sourceDir, File targetDir) throws IOException {
		if (sourceDir.isDirectory()) {
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}

			String[] children = sourceDir.list();
			for (String child : children) {
				copyDirectory(new File(sourceDir, child), new File(targetDir, child));
			}
		} else {
			// Si es un archivo, simplemente copia el archivo
			Files.copy(sourceDir.toPath(), targetDir.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	public static void downloadFileUsingBrowser(String urlArchivo, String rutaDestino) {
		try {

			ProccessStarURL(urlArchivo);

			URL url = new URL(urlArchivo);
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);

			InputStream in = conn.getInputStream();
			FileOutputStream out = new FileOutputStream(rutaDestino);

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}

			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void downloadFile(String urlArchivo, String rutaDestino) {
		try {
			logger.info(urlArchivo);
			logger.info(rutaDestino);
			URL url = new URL(urlArchivo);
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false); // Para evitar la caché

			InputStream in = conn.getInputStream();
			FileOutputStream out = new FileOutputStream(rutaDestino);

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}

			out.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
