package com.commerceapp.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Report {
	private String templatePath;
	private String absoluteTemplatePath;
	private String outDirectory;
	private String outFileName;
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getAbsoluteTemplatePath() {
		return absoluteTemplatePath;
	}

	public void configure() {
		determinateAbsulteTempaltePath();
    }
	
	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}
	
	public void setOutDirectory(String outDirectory) {
		this.outDirectory = outDirectory;
	}
	
	public String getOutDirectory() {
		return outDirectory;
	}
	
	public String getOutFileName() {
		return outFileName;
	}

	private void determinateAbsulteTempaltePath() {
        InputStream inputStream = getClass().getResourceAsStream(templatePath);
		try {
			File tempFile = File.createTempFile("reporte", ".jrxml");
			tempFile.deleteOnExit();
			try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
			}
			this.absoluteTemplatePath = tempFile.getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException("Error al obtener el JRXML");
		}
	}
}
