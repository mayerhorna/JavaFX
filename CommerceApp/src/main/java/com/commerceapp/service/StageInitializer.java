package com.commerceapp.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.commerceapp.Main;
import com.commerceapp.controller.LoginCommerceController;
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

	private CustomResourceLoader customResourceLoader;
	private ApplicationContext applicationContext;

	public Stage stage;

	@Value("classpath:/fxml/MenuPrincipal.fxml")
	private Resource menuPrincipal;

	public StageInitializer(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.customResourceLoader = new CustomResourceLoader();

	}

	private void getFormLogin() throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/LoginCommerce.fxml"));

		Image icon = customResourceLoader.loadImage("LogoECommerce.png");
		Parent rootLogin = loader.load();
		LoginCommerceController objLoginController = loader.getController();
		
		Stage stageLogin = new Stage();

		Scene sceneLogin = new Scene(rootLogin);
		stageLogin.setTitle("Login");
		stageLogin.getIcons().add(icon);
		stageLogin.setScene(sceneLogin);
		stageLogin.showAndWait();

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
			Scene scene = new Scene(root);

			// obtiene el icono
			Image icon = customResourceLoader.loadImage("LogoECommerce.png");

			stage.setTitle("Legalización de libros");
			stage.setScene(scene);
			stage.setResizable(true);
			stage.setMaximized(true);

			stage.getIcons().add(icon);

			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {
				MGeneral.Idioma.cargarIdiomaControles(stage, null);
			} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
				MGeneral.CargaGlobales();
				logger.info("Se cargó globales");
				MGeneral.Idioma.cargarIdiomaControles(stage, null);
			}

			getFormLogin();
			stage.toFront();
			stage.show();

		} catch (IOException e) {

			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
		}

	}

}
