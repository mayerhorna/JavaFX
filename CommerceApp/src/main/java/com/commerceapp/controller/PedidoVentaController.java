package com.commerceapp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.commerceapp.app.JPAControllerBa_user;
import com.commerceapp.app.JPAControllerProduct;
import com.commerceapp.controller.BusquedaProductosController.VentaModelo;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.model.BaUser;
import com.commerceapp.model.Product;
import com.commerceapp.util.Utilidades;
import com.commerceapp.domain.IdiomaC.AyudaUtils;
import com.commerceapp.domain.IdiomaC.EnumMensajes;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PedidoVentaController implements Initializable {

	@FXML
	private TextField TxtObservaciones;

	@FXML
	private Button btnCaja;

	@FXML
	private Button btnCliente;

	@FXML
	private Button btnClienteConfig;

	@FXML
	private Button btnImprimir;

	@FXML
	private Button btnPedidoVenta;

	@FXML
	private Button btnProducto;

	@FXML
	private Button btnUsuario;

	@FXML
	public TableColumn<VentaModelo, String> colCantidad;

	@FXML
	public TableColumn<VentaModelo, String> colCodigo;

	@FXML
	public TableColumn<VentaModelo, String> colNumero;

	@FXML
	public TableColumn<VentaModelo, String> colPrecio;

	@FXML
	public TableColumn<VentaModelo, String> colProducto;

	@FXML
	public TableColumn<VentaModelo, String> colTotal;

	@FXML
	private Label lblPuntoVenta;

	@FXML
	private Label lblSincronizado;

	@FXML
	private Label lblSino;

	@FXML
	private Label lblTotal;

	@FXML
	private Menu mnuAdministrar;

	@FXML
	private Menu mnuAyuda;

	@FXML
	private MenuItem mnuItemClose;

	@FXML
	private MenuItem mnuItemDelete;

	@FXML
	public TableView<VentaModelo> tblVenta;

	@FXML
	private TextField txtCliente;

	@FXML
	private TextField txtPedidoVenta;

	@FXML
	private TextField txtProducto;

	@FXML
	private TextField txtTotal;
	@FXML
	private VBox idVOBXprueba;
	@FXML
	private ScrollPane idpruebascroll;
	@FXML
	private Label idLabelUser;

	JPAControllerBa_user objBauser;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		scroolPane();
		colCantidad.setCellValueFactory(data -> data.getValue().cantidadProperty());
		colCantidad.setCellFactory(TextFieldTableCell.forTableColumn());
		colCantidad.setOnEditCommit(event -> {
			VentaModelo item = event.getRowValue();
			item.setCantidad(event.getNewValue());
			updateTotal(item);
		});
		colCodigo.setCellValueFactory(data -> data.getValue().codigoProperty());
		colNumero.setCellValueFactory(data -> data.getValue().numeroProperty());
		colPrecio.setCellValueFactory(data -> data.getValue().precioProperty());
		colPrecio.setCellFactory(TextFieldTableCell.forTableColumn());
		colPrecio.setOnEditCommit(event -> {
			VentaModelo item = event.getRowValue();
			item.setPrecio(event.getNewValue());
			updateTotal(item);
		});

		colProducto.setCellValueFactory(data -> data.getValue().productoProperty());
		colTotal.setCellValueFactory(data -> data.getValue().totalProperty());
		objBauser = new JPAControllerBa_user();

		BaUser objbus = objBauser.leerUsuario(MGeneral.Configuracion.objBaUser.getBa_user_id());
		idLabelUser.setText("Usuario: " + objbus.getName());
		iniciarValidaciones();
	}

	private void updateTotal(VentaModelo item) {
		// Calcular el nuevo total basado en la cantidad y el precio actualizados
		double cantidad = Double.parseDouble(item.getCantidad());
		double precio = Double.parseDouble(item.getPrecio());
		double nuevoTotal = cantidad * precio;

		// Actualizar el total en el modelo
		item.setTotal(String.valueOf(nuevoTotal));

		// Actualizar la celda en la tabla
		tblVenta.refresh();
	}

	private void iniciarValidaciones() {
		// Listener para detectar cambios en la lista de elementos de la tabla
		tblVenta.getItems()
				.addListener((ListChangeListener.Change<? extends BusquedaProductosController.VentaModelo> c) -> {
					recalcularTotal();
				});

		// Listener para detectar cambios en la propiedad 'totalProperty' de cada
		// elemento
		tblVenta.getItems().forEach(item -> {
			item.totalProperty()
					.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
						recalcularTotal();
					});
		});
	}

	private void recalcularTotal() {
		double total = 0.0;
		for (BusquedaProductosController.VentaModelo item : tblVenta.getItems()) {
			total += Double.parseDouble(item.getTotal());
		}
		txtTotal.setText(String.valueOf(total)); // Actualizar el label con el nuevo total
	}

	private void scroolPane() {
		idpruebascroll.setContent(idVOBXprueba);
		idpruebascroll.setFitToWidth(true);
		idpruebascroll.setFitToHeight(true);

	}

	public void setParentController(MenuPrincipalController menuPrincipalController) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void buscarProducto(ActionEvent e) throws Exception {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BusquedaProductos.fxml"));
			Parent abrir = loader.load();
			BusquedaProductosController objController = loader.getController();
			objController.setPvc(this);
			Stage stage = new Stage();
			Scene scene = new Scene(abrir);

			// quitando el maximizar y minimizar
			stage.initModality(Modality.APPLICATION_MODAL);
			// bloquea la interacción con otras ventanas de la aplicación
			stage.initStyle(StageStyle.UTILITY);
			// quitando iconos
			stage.getIcons().clear();
			stage.setScene(scene);
			// MGeneral.Idioma.cargarIdiomaControles(stage, null);
			// objAcerController.initialize(null, null);
			stage.showAndWait();

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	@FXML
	public void buscarCliente(ActionEvent e) throws Exception {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BuscarCliente.fxml"));
			Parent abrir = loader.load();
			BuscarClienteController objController = loader.getController();
			objController.setPvc(this);
			Stage stage = new Stage();
			Scene scene = new Scene(abrir);

			// quitando el maximizar y minimizar
			stage.initModality(Modality.APPLICATION_MODAL);
			// bloquea la interacción con otras ventanas de la aplicación
			stage.initStyle(StageStyle.UTILITY);
			// quitando iconos
			stage.getIcons().clear();
			stage.setScene(scene);
			// MGeneral.Idioma.cargarIdiomaControles(stage, null);
			// objAcerController.initialize(null, null);
			stage.showAndWait();

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	@FXML
	private void crearCliente() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CrearClientes.fxml"));
			Parent abrir = loader.load();
			CrearClientesController objController = loader.getController();
			objController.setPvc(this);
			Stage stage = new Stage();
			Scene scene = new Scene(abrir);

			// quitando el maximizar y minimizar
			stage.initModality(Modality.APPLICATION_MODAL);
			// bloquea la interacción con otras ventanas de la aplicación
			stage.initStyle(StageStyle.UTILITY);
			// quitando iconos
			stage.getIcons().clear();
			stage.setScene(scene);
			// MGeneral.Idioma.cargarIdiomaControles(stage, null);
			// objAcerController.initialize(null, null);
			stage.showAndWait();

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}

	@FXML
	public void PressedEnterProducto(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.ENTER) {

			JPAControllerProduct controller = new JPAControllerProduct();
			Product objproducto = controller.buscarProductoPorCodigo(txtProducto.getText());
			if (objproducto != null) {
				tblVenta.getItems().add(new VentaModelo("1", objproducto.getCode().toString(),
						objproducto.getTb_product_id().toString(), objproducto.getSalesPriceWithTax().toString(),
						objproducto.getName(), objproducto.getSalesPriceWithTax().toString()));
			} else {
				IdiomaC.MostrarMensaje(EnumMensajes.NoexisteProducto, null, null, null);
			}
		}
	}

}
