package com.commerceapp.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.commerceapp.Main;
import com.commerceapp.controller.ConfiguracionController;
import com.commerceapp.controller.MenuPrincipalController;
import com.commerceapp.domain.Batch;
import com.commerceapp.domain.MGeneral;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

	private static final Logger logger = Logger.getLogger(StageInitializer.class.getName());

	private final CustomResourceLoader customResourceLoader;

	private ApplicationContext applicationContext;

	public Stage stage;

	@Value("classpath:/fxml/MenuPrincipal.fxml")
	private Resource menuPrincipal;

	public StageInitializer(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.customResourceLoader = new CustomResourceLoader();

	}

	public void onApplicationEvent(StageReadyEvent event) {

		try {

			logger.info("onApplicationEvent");
			logger.info(event.toString());
			// logger.info(SpringVersion.getVersion());

			FXMLLoader fxmlLoader = new FXMLLoader(menuPrincipal.getURL());
			fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));

			stage = event.getStage();

			Parent root = fxmlLoader.load();
			MenuPrincipalController menuPrincipalController = fxmlLoader.getController();
			menuPrincipalController.setStagePrincipal(stage);
			/*
			 * Parent root =
			 * SpringFXMLLoader.create().applicationContext(applicationContext)
			 * .location(getClass().getResource("/fxml/MenuPrincipal.fxml")).charset(Charset
			 * .forName("UTF-8")) .load();
			 */

			// Añade la hoja de estilos (css)
			// Scene scene = customResourceLoader.load(root, "/estilos/ToolBar.css");
			Scene scene = new Scene(root);

			scene.widthProperty().addListener((observable, oldValue, newValue) -> {
				double width = newValue.doubleValue();
				String cssFile = "";
				logger.info("ancho: " + width);
				if (width > 1680) {
					cssFile = "/estilos/Grande.css";
					// imageView.setFitWidth(300); // Nuevo ancho
					// imageView.setFitHeight(200); // Nuevo alto
				}

				if (width > 1280 && width <= 1680) {
					cssFile = "/estilos/Mediano3.css";

				}
				if (width > 1152 && width <= 1280) {
					cssFile = "/estilos/Mediano2.css";

				}
				if (width > 1024 && width <= 1152) {
					cssFile = "/estilos/Mediano1.css";

				}
				if (width > 800 && width <= 1024) {
					cssFile = "/estilos/Pequeño2.css";
				}
				if (width < 800) {
					cssFile = "/estilos/Pequeño1.css";
				}
				logger.info(cssFile);
				scene.getStylesheets().clear();
				scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
				scene.getStylesheets().add(getClass().getResource("/estilos/ToolBar.css").toExternalForm());
			});

			// obtiene el icono
			Image icon = customResourceLoader.loadImage("IconoColegio.png");

			stage.setTitle("Legalización de libros");
			stage.setScene(scene);
			stage.setResizable(true);
			stage.setMaximized(true);

			// String classpath = System.getProperty("java.class.path");
			// System.out.println("Classpath: " + classpath);
			stage.getIcons().add(icon);

			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {
				MGeneral.Idioma.cargarIdiomaControles(stage, null);
			} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
				MGeneral.CargaGlobales();
				logger.info("Se cargó globales");
				MGeneral.Idioma.cargarIdiomaControles(stage, null);
			}

			stage.toFront();
			stage.show();
			// logger.info(stage.toString());
		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}
