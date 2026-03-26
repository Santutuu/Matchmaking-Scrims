package com.api.Scrims_Valorant.Facade.Utils;

import com.api.Scrims_Valorant.Model.Usuario;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScrimUtils {

    private static final String ULTIMO_SCRIM_ID_FILE = "src/main/resources/data/ultimo_scrim_id.txt";
    private static final String SCRIMS_JSON_FILE = "src/main/resources/data/scrims.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static int generarNuevoIdScrim() {
        int id = leerUltimoId(ULTIMO_SCRIM_ID_FILE);
        id = id + 1;
        actualizarUltimoId(ULTIMO_SCRIM_ID_FILE, id);
        return id;
    }

    private static int leerUltimoId(String archivo) {
        try {
            File file = new File(archivo);
            if (!file.exists()) {
                // Crear directorio si no existe
                file.getParentFile().mkdirs();
                return 0;
            }

            String content = new String(Files.readAllBytes(Paths.get(archivo)));
            return Integer.parseInt(content.trim());
        } catch (Exception e) {
            System.out.println("❌ Error leyendo último ID: " + e.getMessage());
            return 0;
        }
    }

    private static void actualizarUltimoId(String archivo, int id) {
        try {
            File file = new File(archivo);
            file.getParentFile().mkdirs(); // Crear directorio si no existe

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(String.valueOf(id));
            }
        } catch (Exception e) {
            System.out.println("❌ Error actualizando último ID: " + e.getMessage());
        }
    }

    public static void guardarScrimEnBD(ObjectNode scrimData) {
        try {
            // Cargar scrims existentes
            ArrayNode scrimsArray = cargarScrimsExistente();
            scrimsArray.add(scrimData);

            // Guardar
            File file = new File(SCRIMS_JSON_FILE);
            file.getParentFile().mkdirs(); // Crear directorio si no existe

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(scrimsArray));
                System.out.println("✅ Scrim guardada en BD: " + SCRIMS_JSON_FILE);
            }
        } catch (IOException e) {
            System.out.println("❌ Error al guardar scrim: " + e.getMessage());
        }
    }

    private static ArrayNode cargarScrimsExistente() {
        try {
            File file = new File(SCRIMS_JSON_FILE);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                return mapper.createArrayNode();
            }

            try (FileReader reader = new FileReader(file)) {
                JsonNode root = mapper.readTree(reader);
                if (root.isArray()) {
                    return (ArrayNode) root;
                } else {
                    return mapper.createArrayNode();
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error cargando scrims existentes: " + e.getMessage());
            return mapper.createArrayNode();
        }
    }

    public static Usuario obtenerUsuarioPorId(int userId) {
        // Implementar lógica para buscar usuario en BD
        // Similar a GestorUsuarios.buscarPorId()
        // Por ahora retornamos null como placeholder
        System.out.println("⚠️  Buscando usuario con ID: " + userId + " (implementar lógica real)");
        return null;
    }

    public static void actualizarEstadoUsuario(Usuario usuario) {
        // Implementar actualización de usuario en BD
        System.out.println("⚠️  Actualizando estado del usuario: " + usuario.getIdUsuario() + " (implementar lógica real)");
    }

    // Método adicional útil para crear un ObjectNode (similar a JSONObject)
    public static ObjectNode crearScrimObjectNode() {
        return mapper.createObjectNode();
    }

    // Método para buscar scrim por ID
    public static ObjectNode buscarScrimPorId(int scrimId) {
        try {
            ArrayNode scrimsArray = cargarScrimsExistente();

            for (int i = 0; i < scrimsArray.size(); i++) {
                JsonNode scrimNode = scrimsArray.get(i);
                if (scrimNode.has("idScrim") && scrimNode.get("idScrim").asInt() == scrimId) {
                    return (ObjectNode) scrimNode;
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error buscando scrim por ID: " + e.getMessage());
        }
        return null;
    }

    // Método para actualizar scrim existente
    public static void actualizarScrimEnBD(int scrimId, ObjectNode scrimDataActualizado) {
        try {
            ArrayNode scrimsArray = cargarScrimsExistente();
            boolean encontrado = false;

            for (int i = 0; i < scrimsArray.size(); i++) {
                JsonNode scrimNode = scrimsArray.get(i);
                if (scrimNode.has("idScrim") && scrimNode.get("idScrim").asInt() == scrimId) {
                    scrimsArray.set(i, scrimDataActualizado);
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                File file = new File(SCRIMS_JSON_FILE);
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(scrimsArray));
                    System.out.println("✅ Scrim actualizada en BD: " + scrimId);
                }
            } else {
                System.out.println("⚠️  Scrim no encontrada para actualizar: " + scrimId);
            }
        } catch (IOException e) {
            System.out.println("❌ Error al actualizar scrim: " + e.getMessage());
        }
    }
}