package org.example;

import java.util.Scanner;

public class Consulta {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // PEDIR AL USUARIO EL NOMBRE O SIMBOLO DE LA CRIPTOMONEDA
        System.out.println("INTRODUCE NOMBRE O SIMBOLO DE LA CRIPTOMONEDA CRIPTOBRO:");
        String entrada = scanner.nextLine().trim();

        ManejaApiCripto manejador = new ManejaApiCripto();

        // OBTENER LOS DATOS DE LA API
        String datosJson = manejador.obtenerDatosApi();

        // VERIFICAR SI HUBO ERROR DE CONEXION
        if (datosJson == null || datosJson.isEmpty()) {
            System.out.println("ERRORCH CONECTANDO CON LA API");
            scanner.close();
            return;
        }

        // BUSCAR LA MONEDA ESPECIFICA
        String[] datosMoneda = manejador.buscarMoneda(datosJson, entrada);

        if (datosMoneda[0] != null) {
            manejador.mostrarDatos(datosMoneda);
        } else {
            System.out.println("MONEDA NO ENCONTRADA ;(");
        }

        scanner.close();
    }
}
