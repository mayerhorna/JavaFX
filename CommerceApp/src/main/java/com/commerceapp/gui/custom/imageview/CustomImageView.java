package com.commerceapp.gui.custom.imageview;

import java.util.logging.Logger;

import com.commerceapp.gui.custom.combobox.CustomCombobox;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CustomImageView extends ImageView {
	private static final Logger logger = Logger.getLogger(CustomImageView.class.getName());
	private Tooltip tooltip;
	private Image image;
	private Node node;

	@FXML
	private void initialize() {
		
		setVisible(true);// Establecer texto de sugerencia

	}

	public CustomImageView() {
		super();
		
	}

	
	public CustomImageView(Node node,Image i, String description) {
		super(i);
		
		image = i;
		setImage(image);
		tooltip = new Tooltip();
		Tooltip.install(this, tooltip);
		setTooltipDescription(description);
		this.node=node;
	}

	public void setTooltipDescription(String description) {
		tooltip.setText(description);
	}
	public void setImagen(Image i) {
		setImage(image);
		image = i;
	}
}