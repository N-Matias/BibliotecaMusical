/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import java.io.IOException;
import java.util.List;

public interface IRepositorio<T> {
    void guardar(List<T> elementos) throws IOException;
    List<T> cargar() throws IOException;
}
