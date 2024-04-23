package com.commerceapp.domain;

public class meDoc {
    // Nombre de la codificación utilizada
    public static final String kNombreCodificacioneDoc = "UTF-8";

    public static final String kNodoCORPMEeDoc = "CORPME-eDoc";

    // Atributos de la cabecera
    public static final String kAtributoVersionCORPMEeDoc = "Version";
    public static final String kAtributoTipo_Entidad = "Tipo_Entidad";
    public static final String kAtributoCodigo_Entidad = "Codigo_Entidad";
    public static final String kAtributoId_Tramite = "Id_Tramite";
    public static final String kAtributoTipo_Mensaje = "Tipo_Mensaje";
    public static final String kAtributoTipo_Registro = "Tipo_Registro";
    public static final String kAtributoCodigo_Registro = "Codigo_Registro";
    public static final String kAtributoxsinoNamespaceSchemaLocation = "xsi:noNamespaceSchemaLocation";
    public static final String kAtributoxmlnsxsi = "xmlns:xsi";

    public static final String kNodoId_Tramite = "Id_Tramite";
    public static final String kNodoDatos_PRIVADO = "Datos_PRIVADO";
    public static final String kNodoEnvio_Tramite = "Envio_Tramite";

    // Identificacion presentante
    public static final String kNodoIdentificacion_Presentante = "Identificacion_Presentante";
    public static final String kNodoIdentificacion_PresentantePresentante = "Presentante";

    // Datos facturación
    public static final String kNodoDatos_Facturacion = "Datos_Facturacion";
    public static final String kNodoDatos_FacturacionIRPF = "IRPF";
    public static final String kNodoDatos_FacturacionIRPFAplicar_IRPF = "Aplicar_IRPF";
    public static final String kNodoDatos_FacturacionIRPFPorcentaje_IRPF = "Porcentaje_IRPF";

    // Destinatario
    public static final String kNodoDestinatario = "Destinatario";
    public static final String kNodoTipo_Personalidad = "Tipo_Personalidad";
    public static final String kNodoNombre = "Nombre";
    public static final String kNodoApellidos = "Apellidos";
    public static final String kNodoIdentificador_Administrativo = "Identificador_Administrativo";
    public static final String kNodoIdentificador_AdministrativoNumero_Documento = "Numero_Documento";
    public static final String kNodoIdentificador_AdministrativoTipo = "Tipo";

    // Valores de los tipos de personalidad
    public static final String kTipo_PersonalidadFisica = "1";
    public static final String kTipo_PersonalidadJuridica = "3";
    public static final String kTipoIdentificador_AdministrativoDNI = "1";
    public static final String kTipoIdentificador_AdministrativoCIF = "2";
    public static final String kTipoIdentificador_AdministrativoNIF = "3";
    public static final String kTipoIdentificador_AdministrativoPasaporte = "4";

    public static final String kNodoForma_Pago_Factura = "Forma_Pago_Factura";
    public static final String kAtributoTipo_Forma_Pago = "Tipo_Forma_Pago";
    public static final String kNodoUsuario_Abonado_Corpme = "Usuario_Abonado_Corpme";

    // Identificacion_Documento
    public static final String kNodoIdentificacion_Documento = "Identificacion_Documento";
    public static final String kNodoIdentificacion_DocumentoEntrada = "Entrada";
    public static final String kNodoIdentificacion_DocumentoEntradaTipo = "Tipo";
    public static final String kNodoIdentificacion_DocumentoEntradaNumEntradaMercantil = "NumEntradaMercantil";
    public static final String kNodoIdentificacion_DocumentoEntradaNumEntradaMercantilLibro = "Libro";
    public static final String kNodoIdentificacion_DocumentoEntradaNumEntradaMercantilAnyo = "Anyo";
    public static final String kNodoIdentificacion_DocumentoEntradaNumEntradaMercantilNumero = "Numero";
    public static final String kNodoIdentificacion_DocumentoFechaDocumento = "Fecha_Documento";
    public static final String kNodoIdentificacion_DocumentoNumeroDocumento = "Numero_Documento";
    public static final String kNodoIdentificacion_DocumentoTipoOperacion = "Tipo_Operacion";
    public static final String kNodoIdentificacion_DocumentoTipoOperacionMercantil = "Mercantil";
    public static final String kNodoIdentificacion_DocumentoTipoOperacionMercantilOperacion = "Operacion";
    public static final String kNodoIdentificacion_DocumentoTipoOperacionMercantilDatos_Operacion = "Datos_Operacion";

    // Legalización_Libros
    public static final String kNodoLegalizacion_Libros = "Legalizacion_Libros";
    public static final String kNodoLegalizacion_LibrosLibros = "Libros";
    public static final String kAtributoLegalizacion_LibrosLibrosNumero_Libros = "Numero_Libros";
    public static final String kNodoLegalizacion_LibrosLibrosLibro = "Libro";
    public static final String kNodoLegalizacion_LibrosLibrosLibroNombre = "Nombre";
    public static final String kNodoLegalizacion_LibrosLibrosLibroNombre_Fichero = "Nombre_Fichero";
    public static final String kNodoLegalizacion_LibrosLibrosLibroNumero = "Numero";
    public static final String kNodoLegalizacion_LibrosLibrosLibroFecha_Apertura = "Fecha_Apertura";
    public static final String kNodoLegalizacion_LibrosLibrosLibroFecha_Cierre = "Fecha_Cierre";
    public static final String kNodoLegalizacion_LibrosLibrosLibroFecha_Cierre_Anterior = "Fecha_Cierre_Anterior";

    // Depositante
    public static final String kNodoLegalizacion_LibrosDepositante = "Depositante";
    public static final String kNodoLegalizacion_LibrosDepositanteIdentificacion_Depositante = "Identificacion_Depositante";
    public static final String kNodoLegalizacion_LibrosDepositanteDatos_Contacto = "Datos_Contacto";

    // Datos contacto
    public static final String kNodoDatos_Contacto = "Datos_Contacto";
    public static final String kNodoDatos_ContactoDomicilio = "Domicilio";
    public static final String kNodoDatos_ContactoCorreo_Electronico = "Correo_Electronico";
    public static final String kNodoDatos_ContactoTelefono = "Telefono";
    public static final String kNodoDatos_ContactoFax = "Fax";

    // Domicilio
    public static final String kNodoDomicilio = "Domicilio";
    public static final String kNodoDomicilioCod_Provincia = "Cod_Provincia";
    public static final String kNodoDomicilioCodigo_Postal = "Codigo_Postal";
    public static final String kNodoDomicilioDes_Municipio = "Des_Municipio";
    public static final String kNodoDomicilioCod_Tipo_Via = "Cod_Tipo_Via";
    public static final String kNodoDomicilioNombre_Via = "Nombre_Via";
    public static final String kNodoDomicilioPais = "Pais";

    // Valores fijos
    public static final String kValorIdentificacion_DocumentoEntradaTipoENTRADA_NUEVA = "1"; // Entrada nueva
    public static final String kValorIdentificacion_DocumentoEntradaTipoSUBSANACION = "2";  // Subsanación de un documento ya presentado
    public static final String kValorIdentificacion_DocumentoEntradaLIBROMERCANTIL = "3";  // Libro correspondiente a las entradas de libros de mercantil
    public static final String kValorIdentificacion_DocumentoTipoOperacionMercantilOperacionLEGALIZACION = "112"; // Legalización de libros
    public static final String kValorDomicilioCod_Tipo_ViaCALLE = "Calle";
    public static final String kValorDomicilioPaisESPANIA = "ES"; // España

    // Nodo <Objetos>
    public static final String kNodoObjetos = "Objetos";
    public static final String kNodoSociedades = "Sociedades";
    public static final String kAtributoNumero_Sociedades = "Numero_Sociedades";
    public static final String kNodoSociedad = "Sociedad";
    public static final String KNodoSociedadDenominacion_Social = "Denominacion_Social";
    public static final String KNodoSociedadCIF = "CIF";
    public static final String kNodoSociedadSeccion = "Seccion";
    public static final String kNodoSociedadHoja = "Hoja";
    public static final String kNodoSociedadHoja_Bis = "Hoja_Bis";
    public static final String kNodoSociedadIUS = "IUS";

    // Nodo <Documentos>
    public static final String kNodoDocumentos = "Documentos";
    public static final String kAtributoNumero_Documentos = "Numero_Documentos";
    public static final String kNodoDocumento = "Documento";
    public static final String kNodoFirma_Electronica = "Firma_Electronica";
    public static final String kNodoFicheros = "Ficheros";
    public static final String kAtributoNumero_Ficheros = "Numero_Ficheros";
    public static final String kNodoFichero = "Fichero";
    public static final String kNodoTipo_Formato = "Tipo_Formato";
    public static final String kNodoNombreFichero = "Nombre_Fichero";
    public static final String kNodoOcupacion_Bytes = "Ocupacion_Bytes";
    public static final String kNodoBase64 = "Base64";
    public static final String kNodoContenido = "Contenido";
    public static final String kNodoFirma = "Firma";
    public static final String kNodoTimeStamp = "TimeStamp";
    public static final String kNodoFirma_Datos_Mensaje = "Firma_Datos_Mensaje";

    // Valores de los nodos tipo Sí/No
    public static final String kValorSi = "S";
    public static final String KValorNo = "N";
}
