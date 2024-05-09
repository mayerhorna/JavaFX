package com.commerceapp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.commerceapp.app.JPAControllerProduct;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.model.Product;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BusquedaProductosController implements Initializable {
	@FXML
	private TableView<Product> idTableViewProductos;

	@FXML
	private TableColumn<Product, Number> columnSale;
	@FXML
	private TableColumn<Product, String> columnCode;

	@FXML
	private TableColumn<Product, Number> columName;

	@FXML
	private TableColumn<Product, Number> idCantidad;

	@FXML
	private TableColumn<Product, Number> idPrecio;

	@FXML
	private TextField idBuscarProducto;
	@FXML
	private TextField idPreciotxt;
	@FXML
	private TextField idCantidadtxt;
	@FXML
	private Label idtotalprecio;
	private ArrayList<Product> selectedProducts = new ArrayList<>();

	PedidoVentaController pvc;

	public PedidoVentaController getPvc() {
		return pvc;
	}

	public void setPvc(PedidoVentaController pvc) {
		this.pvc = pvc;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		columnCode.setCellValueFactory(new PropertyValueFactory<>("code"));

		columName.setCellValueFactory(new PropertyValueFactory<>("name"));

		columnSale.setCellValueFactory(new PropertyValueFactory<>("salesPriceWithTax"));
		cargarProductos();
		iniciarValidaciones();
	}

	private void cargarProductos() {
		JPAControllerProduct controller = new JPAControllerProduct();
		List<Product> productos = controller.obtenerTodosProductos();
		// Crea una ObservableList de productos y añádela al TableView
		ObservableList<Product> productList = FXCollections.observableArrayList(productos);
		idTableViewProductos.setItems(productList);
	}

	private void iniciarValidaciones() {

		idTableViewProductos.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				Object selectedItem = idTableViewProductos.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					selectedProducts.clear();
					selectedProducts.add((Product) selectedItem);
					idBuscarProducto.setText(selectedProducts.get(0).getName());
					idCantidadtxt.setText("1");
					idPreciotxt.setText(String.valueOf(selectedProducts.get(0).getSalesPriceWithTax()));
					if (!idCantidadtxt.getText().isEmpty() && !idPreciotxt.getText().isEmpty()) {
						idtotalprecio.setText(String.valueOf(Double.parseDouble(idCantidadtxt.getText())
								* Double.parseDouble(idPreciotxt.getText())));
					}

					idCantidadtxt.requestFocus();
					cargarProductos();
				}
			}
		});
		idBuscarProducto.textProperty().addListener((observable, oldValue, newValue) -> {
			buscarProductos(newValue);
		});
		idCantidadtxt.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!idCantidadtxt.getText().isEmpty() && !idPreciotxt.getText().isEmpty()) {
				idtotalprecio.setText(String.valueOf(
						Double.parseDouble(idCantidadtxt.getText()) * Double.parseDouble(idPreciotxt.getText())));
			}
		});

	}

	private void buscarProductos(String searchText) {
		JPAControllerProduct controller = new JPAControllerProduct();
		List<Product> productos = controller.buscarProductoPorNombre(searchText);

		ObservableList<Product> productList = FXCollections.observableArrayList(productos);
		idTableViewProductos.setItems(productList);
	}

	@FXML
	private void manejarTeclaPresionada(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {

			getPvc().tblVenta.getItems()
					.add(new VentaModelo(idCantidadtxt.getText(), selectedProducts.get(0).getCode().toString(),
							selectedProducts.get(0).getTb_product_id().toString(),
							selectedProducts.get(0).getSalesPriceWithTax().toString(),
							selectedProducts.get(0).getName(), idtotalprecio.getText()));
			limpiarCasillas();
		}
	}

	private void limpiarCasillas() {
		idCantidadtxt.setText("");
		idPreciotxt.setText("");
		idtotalprecio.setText("");
		idBuscarProducto.setText("");
		idBuscarProducto.requestFocus();
	}

	public static class VentaModelo {
		private final StringProperty id;
		private final StringProperty codigo;
		private final StringProperty producto;
		private final StringProperty cantidad;
		private final StringProperty precio;
		private final StringProperty total;

		public VentaModelo(String cantidad, String codigo, String numero, String precio, String producto,
				String total) {
			this.cantidad = new SimpleStringProperty(cantidad);
			this.codigo = new SimpleStringProperty(codigo);
			this.id = new SimpleStringProperty(numero);
			this.precio = new SimpleStringProperty(precio);
			this.producto = new SimpleStringProperty(producto);
			this.total = new SimpleStringProperty(total);
		}

		

		public String getCantidad() {
			return cantidad.get();
		}

		public void setCantidad(String cantidad) {
			this.cantidad.set(cantidad);
		}

		public StringProperty cantidadProperty() {
			return cantidad;
		}

		public String getCodigo() {
			return codigo.get();
		}

		public void setCodigo(String codigo) {
			this.codigo.set(codigo);
		}

		public StringProperty codigoProperty() {
			return codigo;
		}

		public String getNumero() {
			return id.get();
		}

		public void setNumero(String numero) {
			this.id.set(numero);
		}

		public StringProperty numeroProperty() {
			return id;
		}

		public String getPrecio() {
			return precio.get();
		}

		public void setPrecio(String precio) {
			this.precio.set(precio);
		}

		public StringProperty precioProperty() {
			return precio;
		}

		public String getProducto() {
			return producto.get();
		}

		public void setProducto(String producto) {
			this.producto.set(producto);
		}

		public StringProperty productoProperty() {
			return producto;
		}

		public String getTotal() {
			return total.get();
		}

		public void setTotal(String total) {
			this.total.set(total);
		}

		public StringProperty totalProperty() {
			return total;
		}
	}
}
