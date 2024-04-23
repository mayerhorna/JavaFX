package com.commerceapp.util;

public class Barcode128 {
	private Barcode128() {
    }

    /**
     * Convierte la cadena recibida al string necesario para su utilización con la fuente de código de barras 'Code 128'.
     *
     * @param cadena Cadena a convertir
     * @return Cadena convertida para su uso con fuente Code 128
     */
    public static String convierteParaCode128(String cadena) {
        int posicionCaracter;
        int minimaPosicionCaracter;
        int caracterActual;
        int checkSum;
        boolean esCodeB = true;
        boolean sonCaracteresValidos = true;
        StringBuilder resultado = new StringBuilder();

        try {
            if (cadena == null || cadena.isEmpty()) {
                return "";
            }

            // Comprobar si todos los caracteres de la cadena son válidos
            for (int contadorCaracteres = 0; contadorCaracteres < cadena.length(); contadorCaracteres++) {
                caracterActual = cadena.charAt(contadorCaracteres);
                if (!(caracterActual >= 32 && caracterActual <= 126)) {
                    sonCaracteresValidos = false;
                    break;
                }
            }

            if (sonCaracteresValidos) {
                posicionCaracter = 0;
                while (posicionCaracter < cadena.length()) {
                    if (esCodeB) {
                        // Comprobar si se cambia a CodeC: interesa para 4 dígitos al comienzo o final, sino para 6 dígitos
                        if (posicionCaracter == 0 || posicionCaracter + 4 == cadena.length()) {
                            minimaPosicionCaracter = 4;
                        } else {
                            minimaPosicionCaracter = 6;
                        }

                        minimaPosicionCaracter = esNumero(cadena, posicionCaracter, minimaPosicionCaracter);

                        if (minimaPosicionCaracter < 0) {
                            // Se selecciona CodeC
                            if (posicionCaracter == 0) {
                                // Comienzo con CodeC
                                resultado.append((char) 205);
                            } else {
                                // Cambio a CodeC
                                resultado.append((char) 199);
                            }
                            esCodeB = false;
                        } else {
                            if (posicionCaracter == 0) {
                                // Comienzo con CodeB
                                resultado.append((char) 204);
                            }
                        }
                    }

                    if (!esCodeB) {
                        // Es CodeC, intentamos procesar 2 dígitos
                        minimaPosicionCaracter = 2;
                        minimaPosicionCaracter = esNumero(cadena, posicionCaracter, minimaPosicionCaracter);
                        if (minimaPosicionCaracter < 0) {
                            // OK para 2 dígitos, se procesa
                            caracterActual = Integer.parseInt(cadena.substring(posicionCaracter, posicionCaracter + 2));
                            caracterActual = (caracterActual < 95) ? caracterActual + 32 : caracterActual + 100;
                            resultado.append((char) caracterActual);
                            posicionCaracter += 2;
                        } else {
                            // No tenemos 2 dígitos, cambiamos a CodeB
                            resultado.append((char) 200);
                            esCodeB = true;
                        }
                    }
                    if (esCodeB) {
                        // Procesamos 1 dígito con CodeB
                        resultado.append(cadena.charAt(posicionCaracter));
                        posicionCaracter++;
                    }
                }

                // Cálculo del CheckSum
                checkSum = 0;
                for (int i = 0; i < resultado.length(); i++) {
                    caracterActual = resultado.charAt(i);
                    caracterActual = (caracterActual < 127) ? caracterActual - 32 : caracterActual - 100;
                    if (i == 0) {
                        checkSum = caracterActual;
                    } else {
                        checkSum = (checkSum + (i * caracterActual)) % 103;
                    }
                }

                // Calcular el ASCCI del CheckSum
                checkSum = (checkSum < 95) ? checkSum + 32 : checkSum + 100;

                // Añadir al resultado el CheckSum y el STOP
                resultado.append((char) checkSum).append((char) 206);
            }
            return resultado.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    private static int esNumero(String cadena, int posicion, int longitud) {
        int contador = 0;
        while (contador < longitud && posicion + contador < cadena.length()) {
            if (!Character.isDigit(cadena.charAt(posicion + contador))) {
                return contador;
            }
            contador++;
        }
        return -1;
    }
}
