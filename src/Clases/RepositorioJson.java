/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import Interfaces.IRepositorio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepositorioJson<T> implements IRepositorio<T> {
    private final String rutaArchivo;
    private final Gson gson;
    private final Type tipoLista;

    public RepositorioJson(String rutaArchivo, Type tipoLista) {
        this.rutaArchivo = rutaArchivo;
        this.tipoLista = tipoLista;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void guardar(List<T> elementos) throws IOException {
        try (Writer writer = new FileWriter(rutaArchivo)) {
            gson.toJson(elementos, writer);
        }
    }

    @Override
    public List<T> cargar() throws IOException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(archivo)) {
            List<T> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        }
    }
}
