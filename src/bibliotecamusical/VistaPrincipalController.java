/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package bibliotecamusical;

import Clases.Cancion;
import Clases.RepositorioJson;
import Interfaces.IRepositorio;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VistaPrincipalController implements Initializable {

    @FXML
    private TextField txtBuscar;

    @FXML
    private ListView<Cancion> listViewCanciones;

    @FXML
    private Label lblEstadisticas;

    private List<Cancion> cancionesTotales;
    private ObservableList<Cancion> cancionesVisibles;
    private IRepositorio<Cancion> repositorio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Type tipoLista = new TypeToken<ArrayList<Cancion>>() {}.getType();
        repositorio = new RepositorioJson<>("biblioteca.json", tipoLista);
        cancionesTotales = new ArrayList<>();
        cancionesVisibles = FXCollections.observableArrayList();
        listViewCanciones.setItems(cancionesVisibles);

        try {
            cancionesTotales.addAll(repositorio.cargar());
            cancionesVisibles.setAll(cancionesTotales);
            actualizarEstadisticas();
        } catch (IOException e) {
            mostrarAviso("No se pudo cargar la biblioteca: " + e.getMessage());
        }
    }

    @FXML
    private void filtrarCanciones(KeyEvent event) {
        String filtro = txtBuscar.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            cancionesVisibles.setAll(cancionesTotales);
        } else {
            List<Cancion> filtradas = cancionesTotales.stream()
                    .filter(c -> c.getTitulo().toLowerCase().contains(filtro)
                            || c.getArtista().toLowerCase().contains(filtro)
                            || c.getGenero().toLowerCase().contains(filtro))
                    .collect(Collectors.toList());
            cancionesVisibles.setAll(filtradas);
        }
        actualizarEstadisticas();
    }

    @FXML
    private void agregarCancion(ActionEvent event) {
        abrirFormulario(null);
    }

    @FXML
    private void modificarCancion(ActionEvent event) {
        Cancion seleccionada = listViewCanciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAviso("Seleccione una canción para modificar.");
            return;
        }
        abrirFormulario(seleccionada);
    }

    @FXML
    private void eliminarCancion(ActionEvent event) {
        Cancion seleccionada = listViewCanciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAviso("Seleccione una canción para eliminar.");
            return;
        }
        cancionesTotales.remove(seleccionada);
        cancionesVisibles.remove(seleccionada);
        actualizarEstadisticas();
        guardarCambios();
    }

    private void abrirFormulario(Cancion cancionAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioCancion.fxml"));
            Parent root = loader.load();

            FormularioCancionController controller = loader.getController();
            controller.setCancion(cancionAEditar);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(cancionAEditar == null ? "Nueva Canción" : "Modificar Canción");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isConfirmado()) {
                if (cancionAEditar == null) {
                    cancionesTotales.add(controller.getCancion());
                } else {
                    listViewCanciones.refresh();
                }
                filtrarCanciones(null);
                guardarCambios();
            }
        } catch (IOException e) {
            mostrarAviso("Error al abrir el formulario: " + e.getMessage());
        }
    }

    private void actualizarEstadisticas() {
        int cantidad = cancionesVisibles.size();
        int duracionTotal = cancionesVisibles.stream()
                .mapToInt(Cancion::getDuracionSegundos)
                .sum();
        lblEstadisticas.setText("Mostrando: " + cantidad + " canciones | Duración total: " + duracionTotal + " seg");
    }

    private void guardarCambios() {
        try {
            repositorio.guardar(cancionesTotales);
        } catch (IOException e) {
            mostrarAviso("Error al guardar en JSON: " + e.getMessage());
        }
    }

    private void mostrarAviso(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
