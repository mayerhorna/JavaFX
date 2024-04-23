package com.commerceapp.controller;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.util.Formato;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

public class EncriptacionResultadoController implements Initializable {
	EncriptacionController parentController;

	public static enum EModoApertura {
		EncriptadoEnLaLegalizacion(1), EncriptadoManual(2), ConsultaVector(3), EncriptadosTodosLibros(4);

		private final int value;

		EModoApertura(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private EModoApertura _ModoApertura;
	private String _TextoResultado;
	private String _FicheroSalida;

	public void set_ModoApertura(EModoApertura _ModoApertura) {
		this._ModoApertura = _ModoApertura;
	}

	public void set_TextoResultado(String _TextoResultado) {
		this._TextoResultado = _TextoResultado;
	}

	public void set_FicheroSalida(String _FicheroSalida) {
		this._FicheroSalida = _FicheroSalida;
	}

	public void setParentController(EncriptacionController parentController) {
		this.parentController = parentController;
	}

	@FXML
	TextArea txtResultado;
	@FXML
	TextField txtFicheroSalida;
	@FXML
	Button btnCerrar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		if (_TextoResultado != null && _FicheroSalida != null) {

			txtResultado.setText(_TextoResultado.replace("/crlf", "\n").replace("null", ""));

			switch (_ModoApertura) {
			case EncriptadoEnLaLegalizacion:
				/*
				 * setTitle(lblTituloEncriptadoEnLegalizacion.getText());
				 * lblResultado.setText(lblResultadoOk.getText());
				 * txtExplicativo.setText(lblTextoExplicativoEncriptadoEnLegalizacion.getText())
				 * ; txtFicheroSalida.setText(new File(_FicheroSalida).getName());
				 * txtFicheroSalida.setVisible(true); lblFicheroSalida.setVisible(true);
				 */
				// chkMensajeEncriptadoLegalizacion.setVisible(true);
				// lblMensajeEncriptadoLegalizacion.setVisible(true);
				break;

			case EncriptadoManual:
				// setTitle(lblTituloEncriptadoManual.getText());
				// lblResultado.setText(lblResultadoOkConFichero.getText());
				// txtExplicativo.setText(lblTextoExplicativoEncriptadoManual.getText());
				txtFicheroSalida.setText(_FicheroSalida);
				txtFicheroSalida.setVisible(true);
				txtFicheroSalida.setVisible(true);
				break;

			case ConsultaVector:
				/*
				 * setTitle(lblTituloConsultaVector.getText()); lblResultado.setText("");
				 * txtExplicativo.setText(lblTextoExplicativoConsultaVector.getText());
				 * picbox1.setVisible(false);
				 */
				break;

			case EncriptadosTodosLibros:
				/*
				 * setTitle(lblTituloEncriptadoEnLegalizacion.getText());
				 * lblResultado.setText(lblResultadoOKTodosLosLibros.getText());
				 * txtExplicativo.setText(lblTextoExplicativoEncriptadoEnLegalizacion.getText())
				 * ; txtFicheroSalida.setText(_FicheroSalida);
				 * txtFicheroSalida.setVisible(true);
				 * lblFicheroSalida.setText(lblFicherosSalida.getText());
				 * lblFicheroSalida.setVisible(true);
				 */
				// chkMensajeEncriptadoLegalizacion.setVisible(true);
				// lblMensajeEncriptadoLegalizacion.setVisible(true);
				break;
			}

		}

	}

	private void GuardaConfiguracion() {
		try {
			// v1.1.6 no se da opción al usuario de no mostrar mensaje
			return;

			// No se ejecuta el código siguiente si el ModoApertura no es
			// EncriptadoEnLaLegalizacion
			// ni EncriptadosTodosLibros
			/*
			 * if (_ModoApertura != EModoApertura.EncriptadoEnLaLegalizacion &&
			 * _ModoApertura != EModoApertura.EncriptadosTodosLibros) { return; } /* // Solo
			 * se guarda la configuración si el chk está marcado if
			 * (!chkMensajeEncriptadoLegalizacion.isChecked()) { return; }
			 */

			/*
			 * Configuracion.setMostrarMensajeAlEncriptarEnLegalizacion(
			 * !chkMensajeEncriptadoLegalizacion.isChecked()); cCon.guardaConfiguracion();
			 */
		} catch (Exception ex) {
			IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "Error", "", ex.toString());
		}
	}

	@FXML
	private void btnCopiarPortapapeles_Click(ActionEvent event) {
		try {
			String cad;

			switch (_ModoApertura) {

			case EncriptadoEnLaLegalizacion:
				cad = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.DirectorioDatosLegalizacion) + ": "
						+ Formato.ponComillas(Paths.get(_FicheroSalida).getFileName().toString())
						+ System.lineSeparator();
				cad += MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.Fichero) + ": "
						+ Formato.ponComillas(_FicheroSalida) + System.lineSeparator();
				cad += _TextoResultado;
				ClipboardContent contentEncriptado = new ClipboardContent();
				contentEncriptado.putString(cad);
				Clipboard.getSystemClipboard().setContent(contentEncriptado);
				break;

			case EncriptadosTodosLibros:
				cad = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.DirectorioDatosLegalizacion) + ": "
						+ Formato.ponComillas(Paths.get(_FicheroSalida).getFileName().toString())
						+ System.lineSeparator();
				cad += MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.Fichero) + ": "
						+ Formato.ponComillas(_FicheroSalida) + System.lineSeparator();
				cad += _TextoResultado;
				ClipboardContent contentTodosLibros = new ClipboardContent();
				contentTodosLibros.putString(cad);
				Clipboard.getSystemClipboard().setContent(contentTodosLibros);
				break;

			case EncriptadoManual:
				cad = MGeneral.Idioma.obtenerLiteral(IdiomaC.EnumLiterales.Fichero) + ": "
						+ Formato.ponComillas(txtFicheroSalida.getText()) + System.lineSeparator();
				cad += _TextoResultado.replace("/crlf", "\n").replace("null", "");
				ClipboardContent contentEncriptadoManual = new ClipboardContent();
				contentEncriptadoManual.putString(cad);
				Clipboard.getSystemClipboard().setContent(contentEncriptadoManual);
				break;

			case ConsultaVector:
				cad = _TextoResultado;
				ClipboardContent contentConsultaVector = new ClipboardContent();
				contentConsultaVector.putString(cad);
				Clipboard.getSystemClipboard().setContent(contentConsultaVector);
				break;
			}

			GuardaConfiguracion();
			Close(event);
		
		} catch (Exception ex) {
			IdiomaC.MostrarMensaje(IdiomaC.EnumMensajes.Excepcion, "Error", "", ex.toString());

		}
	}

	@FXML
	private void btnCerrar_Click(ActionEvent e) {
		GuardaConfiguracion();
		Close(e);
	}

	private void Close(ActionEvent e) {

		Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
		Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el elemento
		stage.close();
	}

}
