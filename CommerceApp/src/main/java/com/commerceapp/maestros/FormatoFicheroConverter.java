package com.commerceapp.maestros;

import com.commerceapp.domain.legalizacion.kLegalizacion;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class FormatoFicheroConverter extends StringConverter<FormatoFichero> {
	ComboBox<FormatoFichero> combobox;

	public FormatoFicheroConverter(ComboBox<FormatoFichero> combo) {
		this.combobox = combo;
	}

	@Override
	public String toString(FormatoFichero object) {
		// TODO Auto-generated method stub
		return object == null ? null : object.getDescripcion();
	}

	@Override
	public FormatoFichero fromString(String string) {
		try {

			FormatoFichero aux = combobox.getValue();
			/*if (aux.existeDescripcion(aux.getDescripcion())) {
				return aux;
			} else {
				return null;
			}*/
			return aux;
		} catch (Exception e) {
			return null;
		}
	
	}

}
