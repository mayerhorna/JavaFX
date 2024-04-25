package com.commerceapp.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class ProductosController implements Initializable {

    @FXML
    private GridPane GPDetalleProducto;
    @FXML
    private GridPane GPTablaProductos;

    @FXML
    private ComboBox<String> ConfigurarDetalle;
    
    
    @FXML
    private AnchorPane anchorRoot;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void ConfigurarDetalle(ActionEvent e) {
    	 GPDetalleProducto.setVisible(false);
    	    GPDetalleProducto.setManaged(false);
    	    
    	    // Ajusta el anclaje inferior de GPTablaProductos para dejar espacio por encima del botón
    	    // Suponiendo que el botón está a 610.0 y quieres dejar 10 píxeles de espacio
    	    double espacioSobreBoton = 10.0;
    	    double alturaBoton = 30.0; // Suponiendo que la altura del botón sea de 30 píxeles
    	    double alturaAnchorPane = anchorRoot.getHeight();
    	    double posicionBotonY = 610.0; // La posición Y del botón según tu FXML

    	    // Ajusta el anclaje inferior para dejar el espacio deseado
    	    AnchorPane.setBottomAnchor(GPTablaProductos, alturaAnchorPane - posicionBotonY + alturaBoton + espacioSobreBoton);
    }
}
