package com.commerceapp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.commerceapp.app.JPAControllerCustomer;
import com.commerceapp.app.JPAControllerProduct;
import com.commerceapp.app.JPAControllerTsSaleOrder;
import com.commerceapp.controller.helpers.NavigableControllerHelper;
import com.commerceapp.model.Customer;
import com.commerceapp.model.Product;
import com.commerceapp.model.TsSaleOrder;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class BuscarPedidoController implements Initializable, NavigableControllerHelper {
	PedidoVentaController pvc;

	public PedidoVentaController getPvc() {
		return pvc;
	}

	public void setPvc(PedidoVentaController pvc) {
		this.pvc = pvc;
	}

	public Control[] controlsInOrderToNavigate;

	private ArrayList<TsSaleOrder> selectedCustomers = new ArrayList<>();

	@FXML
	private Button btnNext;

	@FXML
	private Button btnPrevio;

	@FXML
	private TableView<TsSaleOrder> tblClientes;

	@FXML
	private TableColumn<TsSaleOrder, String> colID;

	@FXML
	private TableColumn<TsSaleOrder, String> colFecha;

	@FXML
	private TableColumn<TsSaleOrder, String> colCliente;

	@FXML
	private TextField txtBusquedaCliente;

	JPAControllerTsSaleOrder objTsSaleOrder;
	JPAControllerCustomer objCustomer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		objTsSaleOrder = new JPAControllerTsSaleOrder();
		objCustomer = new JPAControllerCustomer();
		colID.setCellValueFactory(new PropertyValueFactory<>("ts_sale_order_id"));

		colFecha.setCellValueFactory(new PropertyValueFactory<>("date_order"));

		colCliente.setCellValueFactory(cellData -> {
			Long id = cellData.getValue().getBa_customer_id();
			JPAControllerCustomer objCliente = new JPAControllerCustomer();
			List<Customer> lista = objCliente.buscarClientePorID(id);

			return new SimpleStringProperty(lista.get(0).getName().toString());
		});

		tblClientes.setRowFactory(tv -> {

			TableRow<TsSaleOrder> row = new TableRow<>();

			row.setOnKeyPressed(event -> {

				logger.info("test");

			});

			row.setOnMouseClicked(event -> {

				if (event.getClickCount() == 2 && !row.isEmpty()) {

					TsSaleOrder rowData = row.getItem();

					try {

						TsSaleOrder objAuxtsSaleOrder = objTsSaleOrder.leerOrdenVenta(rowData.getTs_sale_order_id());
						Customer objAuxCliente = objCustomer.leerCliente((int) objAuxtsSaleOrder.getBa_customer_id());
						pvc.nombreComercialCliente = objAuxCliente.getCommercialName();
						pvc.ponerDetallePedido(rowData.getTs_sale_order_id());

						Node source = (Node) event.getSource();

						Stage stage = (Stage) source.getScene().getWindow();

						stage.close();

					} catch (Exception e) {

						e.printStackTrace();

					}

				}

			});

			return row;

		});
		cargarPedidos();
		iniciarValidaciones();
	}

	private void cargarPedidos() {
		JPAControllerTsSaleOrder controller = new JPAControllerTsSaleOrder();
		List<TsSaleOrder> ventas = controller.obtenerTodasOrdenesVenta();

		// Crea una ObservableList de productos y añádela al TableView
		ObservableList<TsSaleOrder> ventasList = FXCollections.observableArrayList(ventas);
		tblClientes.setItems(ventasList);

	}

	private void iniciarValidaciones() {
		tblClientes.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				Object selectedItem = tblClientes.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					selectedCustomers.clear();
					// selectedCustomers.add((Customer) selectedItem);
					// txtBusquedaCliente.setText(selectedCustomers.get(0).getName());

				}
			}
		});
		tblClientes.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				TsSaleOrder selectedItem = tblClientes.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					try {
						TsSaleOrder objAuxtsSaleOrder = objTsSaleOrder
								.leerOrdenVenta(selectedItem.getTs_sale_order_id());
						Customer objAuxCliente = objCustomer.leerCliente((int) objAuxtsSaleOrder.getBa_customer_id());
						pvc.nombreComercialCliente = objAuxCliente.getCommercialName();
						pvc.ponerDetallePedido(selectedItem.getTs_sale_order_id());
						Stage stage = (Stage) tblClientes.getScene().getWindow();
						stage.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	@Override
	public void initializeControlsInOrderToNavigate() {
		controlsInOrderToNavigate = new Control[] {}; // TODO Auto-generated

	}

	@Override
	public Control[] getControlsInOrderToNavigate() {
		// TODO Auto-generated method stub
		return controlsInOrderToNavigate;
	}

}
