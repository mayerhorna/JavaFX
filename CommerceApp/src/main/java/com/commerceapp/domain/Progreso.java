package com.commerceapp.domain;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToolBar;
import javafx.util.Duration;

public class Progreso {
	private int numeroProcesos = 0; // Número total de procesos
	private Proceso[] procesos; // Array con todos los procesos
	private int numeroProcesoActual = -1; // Número del proceso actual
	private Status statusBar;
	private ProgressBar progressBar;
	private ToolBar toolBar;
	private Label statusLabel;
	private static final int kMaximo = 1000; // La barra de progreso va a ir de 0 a kMaximo
	private boolean haSidoIniciado = false;

	public Progreso(Status ss, ProgressBar pb, ToolBar ts, Label tssl) {
		this.statusBar = ss;
		this.progressBar = pb;
		this.toolBar = ts;
		this.statusLabel = tssl;
	}

	static class Proceso {
		private int porcentajeSobreElTotal; // Porcentaje que supone el proceso, sobre el total (peso del proceso)
		private int maximo; // Valor máximo que puede tener el proceso (nº de elementos o iteraciones
							// máximas

		public Proceso(int porcentajeSobreElTotal, int maximo) {
			this.porcentajeSobreElTotal = porcentajeSobreElTotal;
			this.maximo = maximo;
		}

		public int getPorcentajeSobreElTotal() {
			return porcentajeSobreElTotal;
		}

		// Setter para porcentajeSobreElTotal
		public void setPorcentajeSobreElTotal(int porcentajeSobreElTotal) {
			this.porcentajeSobreElTotal = porcentajeSobreElTotal;
		}

		// Getter para maximo
		public int getMaximo() {
			return maximo;
		}

		// Setter para maximo
		public void setMaximo(int maximo) {
			this.maximo = maximo;
		}
	}

	public boolean haSidoIniciado() {
		return haSidoIniciado;
	}

	public void siguienteProceso(int maximo, String textoStatusLabel) {
		numeroProcesoActual++;
		if (numeroProcesoActual >= numeroProcesos) {
			numeroProcesoActual = numeroProcesos - 1;
		}
		// procesos[numeroProcesoActual].setMaximo(maximo);
		// statusLabel.setText(textoStatusLabel);
		// ajustaAnchoBarraProgreso();
		// Application.DoEvents() no es necesario en Java
	}

	public void procesoTerminado() {
		try {
			for (int i = 1; i <= 5; i++) {
				Thread.sleep(100);
				mostrarProgreso(procesos[numeroProcesoActual].getMaximo());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void mostrarProgreso(int valor) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(kMaximo / 100);

		KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), event -> {
			// Incrementar el valor de la barra de progreso

			int porcentajeTotalYaRealizado = 0;
			int valorYaRealizado;
			double porcentajeActual = 0;
			int valorActual;

			valorYaRealizado = (int) ((double) porcentajeTotalYaRealizado / 100 * kMaximo);

			porcentajeActual = (double) procesos[numeroProcesoActual].getPorcentajeSobreElTotal() / 100;
			System.out.print(porcentajeActual);

			valorActual = (int) ((double) porcentajeActual / 100 * kMaximo);

			for (int i = 0; i < numeroProcesoActual; i++) {
				porcentajeTotalYaRealizado += procesos[i].getPorcentajeSobreElTotal();
			}

			progressBar.setProgress(porcentajeActual);
			siguienteProceso(numeroProcesoActual, "");

		});

		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}
	

	private void ajustaAnchoBarraProgreso() {
		// pb.setWidth(ts.getWidth() - tssl.getWidth() - 20);
	}

	public void finaliza() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (haSidoIniciado) {
					// Se hace solo si ha sido iniciado para no cambiar el estado inicial
					// if (!statusStripVisibleAlIniciar) ss.setVisible(false);
					haSidoIniciado = false;
				}
				// pb.setVisible(false);
				// tssl.setText("");
				progressBar.setProgress(0.0);
				numeroProcesos = 0;
				//procesos = null;
				numeroProcesoActual = -1;
				progressBar.setProgress(0.0);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
		});
		
	}

	public void inicializa(int porcentajeProceso1, int porcentajeProceso2, int porcentajeProceso3,
			int porcentajeProceso4, int porcentajeProceso5) {
		int[] porcentajes = { porcentajeProceso1, porcentajeProceso2, porcentajeProceso3, porcentajeProceso4,
				porcentajeProceso5 };

		numeroProcesos = 0;
		procesos = null;
		numeroProcesoActual = -1;

		for (int i = 0; i < 5; i++) {
			int porcentajeProceso = porcentajes[i];

			if (porcentajeProceso <= 0) {
				break;
			}

			numeroProcesos++;

			if (procesos != null) {
				Proceso[] nuevoArreglo = new Proceso[numeroProcesos];
				System.arraycopy(procesos, 0, nuevoArreglo, 0, procesos.length);
				procesos = nuevoArreglo;
			} else {
				procesos = new Proceso[numeroProcesos];
			}
			procesos[numeroProcesos - 1] = new Proceso(porcentajeProceso, porcentajeProceso5);
		}

		haSidoIniciado = true;
		progressBar.setVisible(true);
		progressBar.setProgress(0.1);

	}

}
