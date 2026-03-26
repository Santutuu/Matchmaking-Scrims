package com.api.Scrims_Valorant.Utils;

import java.io.*;

public class IdGenerator {
    // PATH CORREGIDO según tu estructura
    private static final String ULTIMO_SCRIM_ID_FILE = "src/main/resources/ultimo_scrim_id.txt";

    public static int generarNuevoIdScrim() {
        int id = leerUltimoId();
        id = id + 1;
        actualizarUltimoId(id);
        System.out.println("🆔 Nuevo ID generado: " + id);
        return id;
    }

    private static int leerUltimoId() {
        try {
            File file = new File(ULTIMO_SCRIM_ID_FILE);
            // Crear directorio si no existe
            file.getParentFile().mkdirs();

            if (!file.exists()) {
                System.out.println("ℹ️  Creando archivo de IDs: " + ULTIMO_SCRIM_ID_FILE);
                actualizarUltimoId(0);
                return 0;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String linea = br.readLine();
            br.close();

            if (linea != null && !linea.isEmpty()) {
                return Integer.parseInt(linea.trim());
            } else {
                return 0;
            }
        } catch (IOException e) {
            System.out.println("❌ Error leyendo último ID: " + e.getMessage());
            return 0;
        } catch (NumberFormatException e) {
            System.out.println("❌ Formato inválido en archivo de IDs, reiniciando a 0");
            return 0;
        }
    }

    private static void actualizarUltimoId(int nuevoId) {
        try {
            File file = new File(ULTIMO_SCRIM_ID_FILE);
            // Asegurar que el directorio existe
            file.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(file, false)) { // false = sobrescribir
                writer.write(String.valueOf(nuevoId));
                System.out.println("✅ Último ID actualizado: " + nuevoId);
            }
        } catch (IOException e) {
            throw new RuntimeException("❌ Error al actualizar ID: " + e.getMessage());
        }
    }

    // Método adicional para resetear IDs (útil para testing)
    public static void resetearIds() {
        actualizarUltimoId(0);
        System.out.println("🔄 IDs reseteados a 0");
    }

    // Método para obtener el último ID sin incrementar
    public static int obtenerUltimoId() {
        return leerUltimoId();
    }
}