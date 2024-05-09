package com.commerceapp.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.commerceapp.app.JPAControllerBa_user;
import com.commerceapp.app.JPAControllerCustomer;
import com.commerceapp.app.JPAControllerProduct;
import com.commerceapp.app.JPAControllerTsSaleOrder;
import com.commerceapp.app.JPAControllerTsSaleOrderLine;
import com.commerceapp.controller.BusquedaProductosController.VentaModelo;
import com.commerceapp.domain.IdiomaC;
import com.commerceapp.domain.MGeneral;
import com.commerceapp.model.BaUser;
import com.commerceapp.model.Customer;
import com.commerceapp.model.Product;
import com.commerceapp.model.TsSaleOrder;
import com.commerceapp.model.TsSaleOrderLine;
import com.commerceapp.reporting.instancia.ReportReciboData;
import com.commerceapp.reporting.instancia.ReportingPreviewService;
import com.commerceapp.util.Utilidades;
import com.commerceapp.domain.IdiomaC.AyudaUtils;
import com.commerceapp.domain.IdiomaC.EnumMensajes;
import com.commerceapp.domain.legalizacion.kLegalizacion;

import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
	private Button idBotonEliminarPedido;

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
	@FXML
	private Button idConfirmarPedido;

	@FXML
	HBox frmVenta;
	PedidoVentaController venta;

	JPAControllerTsSaleOrder objTsSaleOrder;

	JPAControllerBa_user objBauser;

	JPAControllerTsSaleOrderLine objTsSaleOrderLine;

	JPAControllerProduct objProduct;

	JPAControllerCustomer objCustomer;

	public long idobjCliente = 0;

	public String nombreComercialCliente = "";

	MenuPrincipalController mpc;

	public MenuPrincipalController getMpc() {
		return mpc;
	}

	public void setMpc(MenuPrincipalController mpc) {
		this.mpc = mpc;
	}

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
		objTsSaleOrder = new JPAControllerTsSaleOrder();
		objTsSaleOrderLine = new JPAControllerTsSaleOrderLine();
		objProduct = new JPAControllerProduct();
		objCustomer = new JPAControllerCustomer();
		BaUser objbus = objBauser.leerUsuario(MGeneral.Configuracion.objBaUser.getBa_user_id());
		idLabelUser.setText("Usuario: " + objbus.getName());
		txtProducto.requestFocus();
		iniciarValidaciones();

	}

	private void updateTotal(VentaModelo item) {
		// Calcular el nuevo total basado en la cantidad y el precio actualizados
		double cantidad = Double.parseDouble(item.getCantidad());
		double precio = Double.parseDouble(item.getPrecio());
		double nuevoTotal = cantidad * precio;

		// Actualizar el total en el modelo
		item.setTotal(String.valueOf(nuevoTotal));
		recalcularTotal();
		// Actualizar la celda en la tabla
		tblVenta.refresh();
	}

	private void guardarDatosReciboVenta() {
		MGeneral.mlform.objDtosRecibo.setNroventa(txtPedidoVenta.getText());
		MGeneral.mlform.objDtosRecibo.setVendedor(idLabelUser.getText().replaceAll("Usuario:", ""));
		MGeneral.mlform.objDtosRecibo.setNombreCliente(txtCliente.getText());
		MGeneral.mlform.objDtosRecibo.setDocumentoCliente(nombreComercialCliente);
		List<TsSaleOrderLine> valoresTabla = obtenerValoresTabla(Integer.parseInt(txtPedidoVenta.getText()));
		double totalApagar = 0;
		for (int i = 0; i < valoresTabla.size(); i++) {
			MGeneral.mlform.objDtosRecibo.objListaProduc.add(new ReportReciboData(valoresTabla.get(i).getName(),
					String.valueOf(valoresTabla.get(i).getPrice_with_tax()), String.valueOf(i + 1),
					valoresTabla.get(i).getQuantity().toString()));
			totalApagar += valoresTabla.get(i).getPrice_with_tax();
		}

		MGeneral.mlform.objDtosRecibo.setTotalapagar(String.valueOf(totalApagar));
	}

	private void iniciarValidaciones() {
		txtPedidoVenta.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				btnImprimir.setDisable(newValue.isEmpty());
				idBotonEliminarPedido.setDisable(newValue.isEmpty());
				MGeneral.mlform.objDtosRecibo.objListaProduc.clear();
				guardarDatosReciboVenta();
			} else {
				btnImprimir.setDisable(!newValue.isEmpty());
				idBotonEliminarPedido.setDisable(!newValue.isEmpty());
			}

		});
		tblVenta.getItems()
				.addListener((ListChangeListener.Change<? extends BusquedaProductosController.VentaModelo> c) -> {
					recalcularTotal();
				});

		tblVenta.getItems().forEach(item -> {
			item.totalProperty()
					.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
						recalcularTotal();
					});
		});

		txtCliente.textProperty().addListener((observable, oldValue, newValue) -> {
			validarCamposConfirmacionPedido();
		});

		tblVenta.getItems().addListener((ListChangeListener.Change<? extends VentaModelo> c) -> {
			validarCamposConfirmacionPedido();
		});
	}

	private void validarCamposConfirmacionPedido() {

		if (!txtCliente.getText().isEmpty() && !tblVenta.getItems().isEmpty()) {
			idConfirmarPedido.setDisable(false);
		} else {
			idConfirmarPedido.setDisable(true);
		}
	}

	private void recalcularTotal() {
		double total = 0.0;
		for (BusquedaProductosController.VentaModelo item : tblVenta.getItems()) {
			total += Double.parseDouble(item.getTotal());
		}
		txtTotal.setText(String.valueOf(total));
	}

	private void scroolPane() {
		idpruebascroll.setContent(idVOBXprueba);
		idpruebascroll.setFitToWidth(true);
		idpruebascroll.setFitToHeight(true);

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
			MGeneral.Idioma.cargarIdiomaControles(stage, null);
			// objAcerController.initialize(null, null);
			stage.showAndWait();

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	@FXML
	public void buscarPedido(ActionEvent e) throws Exception {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BuscarPedido.fxml"));
			Parent abrir = loader.load();
			BuscarPedidoController objController = loader.getController();
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
	public void eliminarPedido(ActionEvent e) throws Exception {

		if (IdiomaC.MostrarMensaje(EnumMensajes.AvisoEliminarPedido, null, null, null)) {
			if (!txtPedidoVenta.getText().isEmpty()) {
				int idOrder = 0;
				idOrder = objTsSaleOrder.obtenerIdOrdenVentaPorCodigo(txtPedidoVenta.getText());

				objTsSaleOrderLine.eliminarLineasOrdenVentaPorId(idOrder);
				objTsSaleOrder.eliminarOrdenVentaPorCodigo(txtPedidoVenta.getText());
				generarNuevoPedido();

			}
		}
	}

	@FXML
	public void nuevoPedido(ActionEvent e) throws Exception {
		generarNuevoPedido();

	}

	@FXML
	public void imprimirRecibo(ActionEvent e) throws Exception {

		ReportingPreviewService.generarReporteReciboVenta();

	}

	private void generarNuevoPedido() {
		try {
			if (venta != null) {
				getMpc().AnchorPane3.getChildren().remove(frmVenta);
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PedidoVenta.fxml"));

			frmVenta = loader.load();
			venta = loader.getController();
			venta.setMpc(getMpc());
			// form.abrir(path);
			getMpc().AnchorPane3.getChildren().add(frmVenta);

			AnchorPane.setTopAnchor(frmVenta, 0.0);
			AnchorPane.setBottomAnchor(frmVenta, 0.0);
			AnchorPane.setLeftAnchor(frmVenta, 0.0);
			AnchorPane.setRightAnchor(frmVenta, 0.0);

		} catch (IOException ex1) {
			ex1.printStackTrace();
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
			MGeneral.Idioma.cargarIdiomaControles(stage, null);
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

	void ponerClienteDesdeVentana(String nombre) {
		txtCliente.setText(nombre);
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

	@FXML
	public void confirmarPedido(ActionEvent event) throws IOException {

		if (IdiomaC.MostrarMensaje(EnumMensajes.ConfirmPedido, null, null, null)) {
			TsSaleOrder objts = new TsSaleOrder();
			objts.setDate_order(new Date());
			objts.setTotal_amount_with_tax(Double.parseDouble(txtTotal.getText()));
			objts.setObservation(TxtObservaciones.getText());
			objts.setBa_customer_id(idobjCliente);
			String pedidoCode = objTsSaleOrder.crearOrdenVenta(objts);
			List<TsSaleOrderLine> valoresTabla = obtenerValoresTabla(Integer.parseInt(pedidoCode));

			for (int i = 0; i < valoresTabla.size(); i++) {
				objTsSaleOrderLine.crearLineaOrdenVenta(valoresTabla.get(i));
			}

			txtPedidoVenta.setText(pedidoCode);
			idConfirmarPedido.setDisable(true);
		}

	}

	void ponerDetallePedido(int id) {
		JPAControllerTsSaleOrderLine controller = new JPAControllerTsSaleOrderLine();
		List<TsSaleOrderLine> detalles = controller.buscarDetallePorId(id);

		List<VentaModelo> ventaViewModels = new ArrayList<>();

		for (TsSaleOrderLine saleOrderLine : detalles) {
			String IdProd = String.valueOf(saleOrderLine.getTbProductId());
			VentaModelo ventaViewModel = new VentaModelo(String.valueOf(saleOrderLine.getQuantity()),
					objProduct.buscarProductoPorID(IdProd).getCode(), String.valueOf(saleOrderLine.getTbProductId()),
					String.valueOf(saleOrderLine.getPrice_with_tax()), saleOrderLine.getName(),
					String.valueOf(saleOrderLine.getTotal_price_with_tax()));
			ventaViewModels.add(ventaViewModel);
		}

		ObservableList<VentaModelo> detallesList = FXCollections.observableArrayList(ventaViewModels);
		tblVenta.setItems(detallesList);

		TsSaleOrder objAuxtsSaleOrder = objTsSaleOrder.leerOrdenVenta(id);
		Customer objAuxCliente = objCustomer.leerCliente((int) objAuxtsSaleOrder.getBa_customer_id());

		txtCliente.setText(objAuxCliente.getName());
		TxtObservaciones.setText(objAuxtsSaleOrder.getObservation());
		if (objAuxtsSaleOrder.getCode() == null) {
			JPAControllerTsSaleOrder aux = new JPAControllerTsSaleOrder();
			txtPedidoVenta.setText(aux.obtenerCodigoOrdenVentaPorId(id));
		} else {
			txtPedidoVenta.setText(objAuxtsSaleOrder.getCode());
		}
		recalcularTotal();

		idConfirmarPedido.setDisable(true);

	}

	private List<TsSaleOrderLine> obtenerValoresTabla(int idPedido) {

		List<TsSaleOrderLine> valoresTabla = new ArrayList<>();

		for (VentaModelo item : tblVenta.getItems()) {
			TsSaleOrderLine tsSaleOrderLine = new TsSaleOrderLine();
			tsSaleOrderLine.setQuantity(Integer.parseInt(item.getCantidad()));
			tsSaleOrderLine.setName(item.getProducto());
			tsSaleOrderLine.setPrice_with_tax(Double.parseDouble(item.getPrecio()));
			tsSaleOrderLine.setTotal_price_with_tax(Double.parseDouble(item.getTotal()));

			int idTsSaleOrder = objTsSaleOrder.obtenerIdOrdenVentaPorCodigo(String.valueOf(idPedido));

			tsSaleOrderLine.setTsSaleOrderId(idTsSaleOrder);
			long idProducto = objProduct.obtenerIdProductoPorCodigo(item.getCodigo());
			tsSaleOrderLine.setTbProductId(idProducto);

			valoresTabla.add(tsSaleOrderLine);
		}

		return valoresTabla;
	}

}
