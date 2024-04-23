package com.commerceapp.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MHuella {

    /**
     * Calcula y devuelve la huella basada en el MD5 del fichero que recibe como parámetro
     *
     * @param path        Path completo al fichero
     * @param textoError  Variable de salida para mensajes de error
     * @return            Huella MD5 tratada del fichero, en caso de cualquier error retorna ""
     */
    public static String obtenerHuellaFicheroMD5(String path, StringBuilder textoError) {
        try {
            textoError.setLength(0);

            if (!java.nio.file.Files.exists(java.nio.file.Paths.get(path))) {
                return "";
            }

            String[] caracteresValidos = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "X", "Y", "Z"};
            int valor;
            StringBuilder huella = new StringBuilder();

            try (FileInputStream fis = new FileInputStream(path)) {
                byte[] bytesFichero = MessageDigest.getInstance("MD5").digest(fis.readAllBytes());

                StringBuilder cadenaBits = new StringBuilder();

                for (byte b : bytesFichero) {
                    String bitsByte = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
                    cadenaBits.append(bitsByte);
                }

                cadenaBits.insert(0, "00");

                for (int i = 0; i < 26; i++) {
                    valor = 0;
                    for (int j = 0; j < 5; j++) {
                        if (cadenaBits.charAt(i * 5 + j + 1) == '1') {
                            valor += Math.pow(2, 4 - j);
                        }
                    }
                    huella.append(caracteresValidos[valor]);
                }
            }

            return huella.toString();
        } catch (IOException | NoSuchAlgorithmException ex) {
        	ex.printStackTrace();
            textoError.append(ex.getMessage());
            return "";
        }
    }

    /**
     * Calcula y devuelve la huella SHA256 del fichero que recibe como parámetro
     *
     * @param path        Path completo al fichero
     * @param textoError  Variable de salida para mensajes de error
     * @return            Huella SHA256 del fichero, en caso de cualquier error retorna ""
     */
    public static String obtenerHuellaFicheroSHA256(String path, StringBuilder textoError) {
        textoError.setLength(0); // Reinicia el StringBuilder

        File file = new File(path);
        if (!file.exists()) {
            return "";
        }

        String huella = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            FileInputStream inputStream = new FileInputStream(file);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            byte[] hash = digest.digest();
            inputStream.close();

            // Convertir los bytes a cadena en base 64
            huella = Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException | IOException ex) {
            textoError.append(ex.getMessage());
        }

        return huella;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}