package com.commerceapp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.commerceapp.app.JPAControllerCustomer;
import com.commerceapp.app.JPAControllerProduct;
import com.commerceapp.controller.helpers.NavigableControllerHelper;
import com.commerceapp.model.Customer;
import com.commerceapp.model.Product;

import javafx.application.Platform;
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

public class BuscarClienteController implements Initializable, NavigableControllerHelper {
	PedidoVentaController pvc;

	public PedidoVentaController getPvc() {
		return pvc;
	}

	public void setPvc(PedidoVentaController pvc) {
		this.pvc = pvc;
	}

	public Control[] controlsInOrderToNavigate;

	private ArrayList<Customer> selectedCustomers = new ArrayList<>();

	@FXML
	private Button btnNext;

	@FXML
	private Button btnPrevio;

	@FXML
	private TableView<Customer> tblClientes;

	@FXML
	private TableColumn<Customer, String> colCodigo;

	@FXML
	private TableColumn<Customer, String> colDescuento;

	@FXML
	private TableColumn<Customer, String> colNombre;

	@FXML
	private TextField txtBusquedaCliente;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colCodigo.setCellValueFactory(new PropertyValueFactory<>("code"));

		colNombre.setCellValueFactory(new PropertyValueFactory<>("name"));

		colDescuento.setCellValueFactory(new PropertyValueFactory<>("discountProduct"));

		tblClientes.setRowFactory(tv -> {

			TableRow<Customer> row = new TableRow<>();

			row.setOnKeyPressed(event -> {

				logger.info("test");

			});

			row.setOnMouseClicked(event -> {

				if (event.getClickCount() == 2 && !row.isEmpty()) {

					Customer rowData = row.getItem();

					try {

						pvc.ponerClienteDesdeVentana(rowData.getName());
						pvc.idobjCliente = rowData.getId();
						pvc.nombreComercialCliente = rowData.getCommercialName();
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
		cargarClientes();
		iniciarValidaciones();
	}

	private void iniciarValidaciones() {
		tblClientes.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				Object selectedItem = tblClientes.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					selectedCustomers.clear();
					selectedCustomers.add((Customer) selectedItem);
					txtBusquedaCliente.setText(selectedCustomers.get(0).getName());

				}
			}
		});
		txtBusquedaCliente.textProperty().addListener((observable, oldValue, newValue) -> {
			buscarClientes(newValue);
		});
		tblClientes.setOnKeyPressed(event -> {
	        if (event.getCode() == KeyCode.ENTER) {
	            Customer selectedItem = tblClientes.getSelectionModel().getSelectedItem();
	            if (selectedItem != null) {
	                try {
	                    pvc.ponerClienteDesdeVentana(selectedItem.getName());
	                    pvc.idobjCliente = selectedItem.getId();
	                    pvc.nombreComercialCliente = selectedItem.getCommercialName();
	                    Stage stage = (Stage) tblClientes.getScene().getWindow();
	                    stage.close();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    });

	}

	private void buscarClientes(String searchText) {
		JPAControllerCustomer controller = new JPAControllerCustomer();
		List<Customer> clientes = controller.buscarClientePorNombre(searchText);

		ObservableList<Customer> clientesList = FXCollections.observableArrayList(clientes);
		tblClientes.setItems(clientesList);

	}

	private void cargarClientes() {
		JPAControllerCustomer controller = new JPAControllerCustomer();
		List<Customer> clientes = controller.obtenerTodosClientes();
		System.out.println(clientes);
		// Crea una ObservableList de productos y añádela al TableView
		ObservableList<Customer> clientesList = FXCollections.observableArrayList(clientes);
		tblClientes.setItems(clientesList);

	}

	private String busqueda() {
		return null;

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
