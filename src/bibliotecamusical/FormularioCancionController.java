/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package bibliotecamusical;

import Clases.Cancion;
import Excepciones.CancionInvalidaException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormularioCancionController implements Initializable {

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextField txtGenero;

    @FXML
    private TextField txtDuracion;

    private Cancion cancion;
    private boolean confirmado = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
        if (cancion != null) {
            txtTitulo.setText(cancion.getTitulo());
            txtArtista.setText(cancion.getArtista());
            txtGenero.setText(cancion.getGenero());
            txtDuracion.setText(String.valueOf(cancion.getDuracionSegundos()));
        }
    }

    public Cancion getCancion() {
        return cancion;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    @FXML
    private void guardar(ActionEvent event) {
        try {
            String titulo = txtTitulo.getText().trim();
            String artista = txtArtista.getText().trim();
            String genero = txtGenero.getText().trim();
            int duracion = Integer.parseInt(txtDuracion.getText().trim());

            if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty()) {
                throw new CancionInvalidaException("Todos los campos de texto son obligatorios.");
            }
            if (duracion <= 0) {
                throw new CancionInvalidaException("La duración debe ser mayor a 0 segundos.");
            }

            if (this.cancion == null) {
                this.cancion = new Cancion(titulo, artista, genero, duracion);
            } else {
                this.cancion.setTitulo(titulo);
                this.cancion.setArtista(artista);
                this.cancion.setGenero(genero);
                this.cancion.setDuracionSegundos(duracion);
            }

            this.confirmado = true;
            cerrarVentana();
        } catch (NumberFormatException e) {
            mostrarError("La duración debe ser un número entero en segundos.");
        } catch (CancionInvalidaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtTitulo.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
