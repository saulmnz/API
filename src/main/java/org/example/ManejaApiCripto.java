package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManejaApiCripto {

    // METODO PARA OBTENER DATOS DE LA API
    public String obtenerDatosApi() {
        try {
            // CREAR LA URL DE LA API
            URL url = new URL("https://api.coinlore.net/api/tickers/");

            // ABRIR LA CONEXION
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            // CONFIGURAR EL METODO DE PETICION COMO GET
            conexion.setRequestMethod("GET");

            // VERIFICAR RESPUESTA ( CON ESE CODIGO SE VERIFICA SI ES EXITOSA)
            if (conexion.getResponseCode() == 200) {

                // READER PARA RESPUESTA DE LA API
                BufferedReader lector = new BufferedReader(
                        new InputStreamReader(conexion.getInputStream())
                );

                // CONSTRUIR EL STRING CON STRINGBUILDER PARA CONTENER EL JSON COMPLETO
                StringBuilder respuestaCompleta = new StringBuilder();
                String lineaActual;

                // LEE LINEA POR LINEA
                while ((lineaActual = lector.readLine()) != null) {
                    respuestaCompleta.append(lineaActual);
                }
                lector.close();

                // DEVOLVER JSON COMO STRING
                return respuestaCompleta.toString();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return null;
    }

    // METODO PARA BUSCAR MONEDA ESPECIFICA EN EL JSON
    public String[] buscarMoneda(String jsonCompleto, String busqueda) {

        // ARRAY PARA ALMACENAR LOS DATOS ENCONTRADOS
        String[] datosEncontrados = new String[5];

        // CONVERTIR MINUSCULAS
        String jsonMinusculas = jsonCompleto.replace(" ", "").toLowerCase();
        String busquedaMinusculas = busqueda.toLowerCase();

        // BUSCAR POR SIMBOLO
        String pSimbolo = "\"symbol\":\"" + busquedaMinusculas + "\"";
        int posicionSimbolo = jsonMinusculas.indexOf(pSimbolo);

        // BUSCAR POR NOMBRE SI NO SE ENCUENTRA POR SIMBOLO
        String pNombre = "\"name\":\"" + busquedaMinusculas + "\"";
        int posicionNombre = jsonMinusculas.indexOf(pNombre);

        // USAR LA POSICION ENCONTRADA
        int posicionEncontrada = -1;
        if (posicionSimbolo != -1) {
            posicionEncontrada = posicionSimbolo;
        } else if (posicionNombre != -1) {
            posicionEncontrada = posicionNombre;
        }

        // DEVOLVER ARRAY VACIO DE NO ENCONTRARSE NADA
        if (posicionEncontrada == -1) {
            return datosEncontrados;
        }


        // EXTRAER LA SUBCADENA TOTAL DESDE DONDE SE ENCONTRO LA MONEDA
        String subcadenaMoneda = jsonMinusculas.substring(posicionEncontrada);


        // SIMBOLO
        int inicioSimbolo = subcadenaMoneda.indexOf("\"symbol\":\"") + 10;
        int finSimbolo = subcadenaMoneda.indexOf("\"", inicioSimbolo);
        String simbolo = subcadenaMoneda.substring(inicioSimbolo, finSimbolo).toUpperCase();
        datosEncontrados[0] = simbolo;


        // NOMBRE
        int inicioNombre = subcadenaMoneda.indexOf("\"name\":\"") + 8;
        int finNombre = subcadenaMoneda.indexOf("\"", inicioNombre);
        String nombre = subcadenaMoneda.substring(inicioNombre, finNombre);

        // FORMATEAR EL NOMBRE A MAYUSCULAS
        nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
        datosEncontrados[1] = nombre;


        // PRECIO
        int inicioPrecio = subcadenaMoneda.indexOf("\"price_usd\":\"") + 13;
        int finPrecio = subcadenaMoneda.indexOf("\"", inicioPrecio);
        datosEncontrados[2] = subcadenaMoneda.substring(inicioPrecio, finPrecio);


        // RANKING
        int inicioRank = subcadenaMoneda.indexOf("\"rank\":") + 7;
        int finRank = subcadenaMoneda.indexOf(",", inicioRank);
        datosEncontrados[3] = subcadenaMoneda.substring(inicioRank, finRank);


        // CAMBIO DE 24 HORAS
        int inicioCambio = subcadenaMoneda.indexOf("\"percent_change_24h\":\"") + 22;
        int finCambio = subcadenaMoneda.indexOf("\"", inicioCambio);
        datosEncontrados[4] = subcadenaMoneda.substring(inicioCambio, finCambio);

        return datosEncontrados;
    }

    // METODO PARA MOSTRAR LOS DATOS DE LA MONEDA ESPECIFICA
    public void mostrarDatos(String[] datosMoneda) {

        // OBTENER LOS VALORES DEL ARRAY
        String simbolo = datosMoneda[0];
        String nombre = datosMoneda[1];
        String precio = datosMoneda[2];
        String ranking = datosMoneda[3];
        String cambio24h = datosMoneda[4];

        // MOSTRAR INFO
        System.out.println("NOMBRE: " + nombre);
        System.out.println("SIMBOLO: " + simbolo);
        System.out.println("PRECIO USD: $" + precio);
        System.out.println("RANKING: " + ranking);

        // CALCULAR EL SIGNO DEL CAMBIO
        double valorCambio = Double.parseDouble(cambio24h);
        String signo = valorCambio >= 0 ? "+" : "";

        // SUBE O BAJA?¿?
        String renta = valorCambio >= 0 ? "SUBE" : "BAJA";

        // MOSTRAR EL CAMBIO CON LA TENDENCIA
        System.out.println("VARIACION 24H: " + signo + cambio24h + "% (" + renta + ")");
    }
}