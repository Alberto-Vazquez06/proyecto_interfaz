
package es.ies.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class Incidencia {

    private final IntegerProperty id;
    private final StringProperty titulo;
    private final StringProperty descripcion;
    private final StringProperty estado;
    private final StringProperty prioridad;
    private final StringProperty responsable;

    public Incidencia(int id, String titulo, String descripcion,
                      String estado, String prioridad,
                      String responsable) {

        this.id = new SimpleIntegerProperty(id);
        this.titulo = new SimpleStringProperty(titulo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.estado = new SimpleStringProperty(estado);
        this.prioridad = new SimpleStringProperty(prioridad);
        this.responsable = new SimpleStringProperty(responsable);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getTitulo() {
        return titulo.get();
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public String getPrioridad() {
        return prioridad.get();
    }

    public void setPrioridad(String prioridad) {
        this.prioridad.set(prioridad);
    }

    public StringProperty prioridadProperty() {
        return prioridad;
    }

    public String getResponsable() {
        return responsable.get();
    }

    public void setResponsable(String responsable) {
        this.responsable.set(responsable);
    }

    public StringProperty responsableProperty() {
        return responsable;
    }
}
