package com.commerceapp.maestros;

import com.commerceapp.gui.custom.combobox.CustomCombobox;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MaestroCodigoDescripcionConverter extends StringConverter<MaestroCodigoDescripcion> {
	ComboBox<MaestroCodigoDescripcion> combobox;

	public MaestroCodigoDescripcionConverter(ComboBox<MaestroCodigoDescripcion> combo) {
		this.combobox = combo;
	}

	@Override
	public String toString(MaestroCodigoDescripcion object) {

		return object == null ? null : object.getDescripcion();
	}

	@Override
	public MaestroCodigoDescripcion fromString(String string) {
		try {
			
			MaestroCodigoDescripcion aux = combobox.getValue();
			if (aux.existeDescripcion(aux.getDescripcion())) {
				return aux;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

	}
}
