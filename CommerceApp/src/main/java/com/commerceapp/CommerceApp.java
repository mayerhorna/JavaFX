package com.commerceapp;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import com.commerceapp.domain.Batch;
import com.commerceapp.domain.MGeneral;

import javafx.application.Application;
import javafx.application.Platform;

//stockUI
@SpringBootApplication
@EntityScan("com.commerceapp.model")
public class CommerceApp {

	private static final Logger logger = Logger.getLogger(CommerceApp.class.getName());

	public static void main(String[] args) throws Exception {
		logger.info("inicio");
		boolean[] cancel = { false };

		for (String s : args) {
			cancel[0] = true;
			if (s.toLowerCase().startsWith("o=")) {
				switch (s.toLowerCase().substring(2)) {
				case "oa":
					// Si es abrir no es modo batch
					MGeneral.ModoBatch = false;
					break;
				case "ob":
					// Si es abrir frmProcesosBatch no es modo batch
					MGeneral.ModoBatch = false;
					break;
				default:
					MGeneral.ModoBatch = true;
					break;
				}
			}
		}

		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			if (!MGeneral.CargaGlobales()) {

				cancel[0] = true;
				return;
			}
			logger.info("Se cargó globales");

			if (cancel[0]) {
				Batch c = new Batch();
				c.manejaCommandLineArgs(cancel, args);
			}
			if (!MGeneral.ModoBatch) {
				Application.launch(Main.class, args);

			}
		} else if (os.contains("nux")) {
			if (!MGeneral.ModoBatch) {
				Application.launch(Main.class, args);
			}

			Platform.runLater(() -> {

				if (cancel[0]) {
					Batch c = new Batch();
					c.manejaCommandLineArgs(cancel, args);
				}

			});
		}
	}

}
