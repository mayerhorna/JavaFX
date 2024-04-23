package com.commerceapp.maestros;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class TipoLibroConverter extends StringConverter<TipoLibro> {
	ComboBox<TipoLibro> combobox;

	public TipoLibroConverter(ComboBox<TipoLibro> combo) {
		this.combobox = combo;
	}

	@Override
	public String toString(TipoLibro object) {
		// TODO Auto-generated method stub
		return object == null ? null : object.getDescripcion();
	}

	@Override
	public TipoLibro fromString(String string) {
		try {

			TipoLibro aux = combobox.getValue();
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
