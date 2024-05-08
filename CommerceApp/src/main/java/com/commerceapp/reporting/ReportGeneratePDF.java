package com.commerceapp.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.logging.Logger;

import com.commerceapp.reporting.instancia.ReportInstanciaParameters;
import com.commerceapp.reporting.instancia.ReportReciboParameter;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportGeneratePDF {
	private static final Logger logger = Logger.getLogger(ReportGeneratePDF.class.getName());

	public File generate(Report report, Collection<?> beanCollection, ReportReciboParameter reportParameters) {
		try {
			logger.info("Iniciando generacion de reporte.");
			String jasperFilePath = JasperCompileManager.compileReportToFile(report.getAbsoluteTemplatePath());
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(beanCollection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilePath, reportParameters.toMap(), dataSource);
			File pdfFile = new File(report.getOutDirectory(),  report.getOutFileName());
			OutputStream outputStream = new FileOutputStream(pdfFile);
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			outputStream.close();
			logger.info("Reporte exportado a reporte.pdf correctamente.");
			return pdfFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	


}
