package es.ies.javafx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Locale;

public class App extends Application {
    
    private final ObservableList<Incidencia> incidencias =
            FXCollections.observableArrayList();

    private FilteredList<Incidencia> incidenciasFiltradas;
    private TableView<Incidencia> tabla;
    private TextField cajaBuscar;
    private ComboBox<String> filtroEstado;

    private Label totalLabel;
    private Label abiertasLabel;
    private Label procesoLabel;
    private Label cerradasLabel;

    private int siguienteId = 6;

    @Override
    public void start(Stage escenario) {

        cargarDatosDeEjemplo();

        incidenciasFiltradas =
                new FilteredList<>(incidencias, incidencia -> true);

        Label titulo = new Label("GESTOR DE INCIDENCIAS");
        titulo.setStyle(
                "-fx-background-color: black;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 27px;" +
                "-fx-padding: 10 17 10 17;" +
                "-fx-background-radius: 35;"
        );

        cajaBuscar = new TextField();
        cajaBuscar.setPromptText("Buscar incidencia...");
        cajaBuscar.setPrefWidth(500);
        cajaBuscar.setStyle(
                "-fx-border-color: black;" +
                "-fx-border-width: 2;"
        );

        Button botonBuscar = crearBoton("Buscar");
        botonBuscar.setOnAction(e -> aplicarFiltros());

        Button botonAnadir = crearBoton("Añadir");
        botonAnadir.setOnAction(e ->
                abrirFormulario(escenario, null)
        );

        HBox filaBusqueda = new HBox(
                25, cajaBuscar, botonBuscar, botonAnadir
        );
        filaBusqueda.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(cajaBuscar, Priority.ALWAYS);

        filtroEstado = new ComboBox<>();
        filtroEstado.getItems().addAll(
                "Todos los estados",
                "Abierta",
                "En proceso",
                "Cerrada"
        );
        filtroEstado.setValue("Todos los estados");
        filtroEstado.setPrefWidth(170);
        filtroEstado.setOnAction(e -> aplicarFiltros());

        HBox filaFiltro = new HBox(filtroEstado);
        filaFiltro.setAlignment(Pos.CENTER_RIGHT);

        tabla = new TableView<>();
        tabla.setItems(incidenciasFiltradas);
        tabla.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );
        tabla.setPlaceholder(
                new Label("No hay incidencias que mostrar")
        );

        TableColumn<Incidencia, Integer> columnaId =
                new TableColumn<>("ID");
        columnaId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        columnaId.setMaxWidth(75);

        TableColumn<Incidencia, String> columnaTitulo =
                new TableColumn<>("Título");
        columnaTitulo.setCellValueFactory(
                new PropertyValueFactory<>("titulo")
        );

        TableColumn<Incidencia, String> columnaEstado =
                new TableColumn<>("Estado");
        columnaEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado")
        );

        TableColumn<Incidencia, String> columnaPrioridad =
                new TableColumn<>("Prioridad");
        columnaPrioridad.setCellValueFactory(
                new PropertyValueFactory<>("prioridad")
        );

        TableColumn<Incidencia, String> columnaResponsable =
                new TableColumn<>("Responsable");
        columnaResponsable.setCellValueFactory(
                new PropertyValueFactory<>("responsable")
        );

        tabla.getColumns().addAll(
                columnaId,
                columnaTitulo,
                columnaEstado,
                columnaPrioridad,
                columnaResponsable
        );

        Button botonDetalle = crearBoton("Detalle");

        botonDetalle.setOnAction(e -> {
            Incidencia seleccionada =
                    tabla.getSelectionModel().getSelectedItem();

            if (seleccionada == null) {
                mostrarAviso(
                        "Selecciona una incidencia de la tabla."
                );
            } else {
                abrirFormulario(escenario, seleccionada);
            }
        });

        HBox filaDetalle = new HBox(botonDetalle);
        filaDetalle.setAlignment(Pos.CENTER);

        VBox contenido = new VBox(
                20,
                titulo,
                filaBusqueda,
                filaFiltro,
                tabla,
                filaDetalle
        );

        contenido.setPadding(
                new Insets(26, 42, 20, 42)
        );

        VBox.setVgrow(tabla, Priority.ALWAYS);

        totalLabel = new Label();
        abiertasLabel = new Label();
        procesoLabel = new Label();
        cerradasLabel = new Label();

        HBox resumen = new HBox(
                35,
                totalLabel,
                abiertasLabel,
                procesoLabel,
                cerradasLabel
        );

        resumen.setAlignment(Pos.CENTER);
        resumen.setPadding(new Insets(18));
        resumen.setStyle(
                "-fx-border-color: black transparent transparent transparent;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;"
        );

        BorderPane raiz = new BorderPane();
        raiz.setCenter(contenido);
        raiz.setBottom(resumen);

        actualizarResumen();

        Scene escena = new Scene(raiz, 1100, 650);

        escenario.setTitle("Gestor de incidencias");
        escenario.setMinWidth(850);
        escenario.setMinHeight(560);
        escenario.setScene(escena);
        escenario.show();
    }

    private Button crearBoton(String texto) {
        Button boton = new Button(texto);

        boton.setStyle(
                "-fx-background-color: #252525;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 28;" +
                "-fx-padding: 10 28 10 28;" +
                "-fx-cursor: hand;"
        );

        return boton;
    }

    private void cargarDatosDeEjemplo() {
        incidencias.addAll(
                new Incidencia(
                        1,
                        "Ordenador no enciende",
                        "El equipo de recepción no arranca.",
                        "Abierta",
                        "Alta",
                        "Marta"
                ),
                new Incidencia(
                        2,
                        "Error al iniciar sesión",
                        "El usuario no puede acceder a la aplicación.",
                        "Abierta",
                        "Media",
                        "Carlos"
                ),
                new Incidencia(
                        3,
                        "Impresora atascada",
                        "Hay papel atascado en la bandeja principal.",
                        "En proceso",
                        "Media",
                        "Lucía"
                ),
                new Incidencia(
                        4,
                        "Actualizar aplicación",
                        "Instalar la última versión en el equipo.",
                        "Cerrada",
                        "Baja",
                        "Diego"
                ),
                new Incidencia(
                        5,
                        "Problema de red",
                        "Conexión restablecida y comprobada.",
                        "Cerrada",
                        "Alta",
                        "Marta"
                )
        );
    }

    private void aplicarFiltros() {
        String texto = cajaBuscar.getText()
                .trim()
                .toLowerCase(Locale.ROOT);

        String estado = filtroEstado.getValue();

        incidenciasFiltradas.setPredicate(incidencia -> {

            boolean coincideTexto =
                    texto.isEmpty()
                    || String.valueOf(incidencia.getId())
                            .contains(texto)
                    || incidencia.getTitulo()
                            .toLowerCase(Locale.ROOT)
                            .contains(texto)
                    || incidencia.getDescripcion()
                            .toLowerCase(Locale.ROOT)
                            .contains(texto)
                    || incidencia.getEstado()
                            .toLowerCase(Locale.ROOT)
                            .contains(texto)
                    || incidencia.getPrioridad()
                            .toLowerCase(Locale.ROOT)
                            .contains(texto)
                    || incidencia.getResponsable()
                            .toLowerCase(Locale.ROOT)
                            .contains(texto);

            boolean coincideEstado =
                    estado == null
                    || estado.equals("Todos los estados")
                    || incidencia.getEstado().equals(estado);

            return coincideTexto && coincideEstado;
        });
    }

    private void abrirFormulario(
            Stage propietario,
            Incidencia incidenciaExistente
    ) {
        boolean esNueva = incidenciaExistente == null;

        Stage ventana = new Stage();
        ventana.initOwner(propietario);
        ventana.initModality(Modality.APPLICATION_MODAL);

        ventana.setTitle(
                esNueva
                        ? "Añadir incidencia"
                        : "Detalles de la incidencia"
        );

        TextField campoTitulo = new TextField(
                esNueva ? "" : incidenciaExistente.getTitulo()
        );

        TextArea campoDescripcion = new TextArea(
                esNueva ? "" : incidenciaExistente.getDescripcion()
        );
        campoDescripcion.setWrapText(true);
        campoDescripcion.setPrefRowCount(4);

        ComboBox<String> campoEstado = new ComboBox<>();
        campoEstado.getItems().addAll(
                "Abierta",
                "En proceso",
                "Cerrada"
        );
        campoEstado.setValue(
                esNueva ? "Abierta" : incidenciaExistente.getEstado()
        );

        ComboBox<String> campoPrioridad = new ComboBox<>();
        campoPrioridad.getItems().addAll(
                "Baja",
                "Media",
                "Alta",
                "Crítica"
        );
        campoPrioridad.setValue(
                esNueva ? "Media" : incidenciaExistente.getPrioridad()
        );

        TextField campoResponsable = new TextField(
                esNueva ? "" : incidenciaExistente.getResponsable()
        );

        GridPane formulario = new GridPane();
        formulario.setHgap(12);
        formulario.setVgap(12);
        formulario.setPadding(new Insets(22));

        formulario.add(new Label("Título:"), 0, 0);
        formulario.add(campoTitulo, 1, 0);

        formulario.add(new Label("Descripción:"), 0, 1);
        formulario.add(campoDescripcion, 1, 1);

        formulario.add(new Label("Estado:"), 0, 2);
        formulario.add(campoEstado, 1, 2);

        formulario.add(new Label("Prioridad:"), 0, 3);
        formulario.add(campoPrioridad, 1, 3);

        formulario.add(new Label("Responsable:"), 0, 4);
        formulario.add(campoResponsable, 1, 4);

        GridPane.setHgrow(campoTitulo, Priority.ALWAYS);
        GridPane.setHgrow(campoDescripcion, Priority.ALWAYS);
        GridPane.setHgrow(campoResponsable, Priority.ALWAYS);

        campoTitulo.setMaxWidth(Double.MAX_VALUE);
        campoDescripcion.setMaxWidth(Double.MAX_VALUE);
        campoResponsable.setMaxWidth(Double.MAX_VALUE);

        Button botonGuardar = crearBoton("Guardar");
        Button botonCancelar = crearBoton("Cancelar");

        HBox botones = new HBox(
                12,
                botonGuardar,
                botonCancelar
        );

        botones.setAlignment(Pos.CENTER_RIGHT);
        botones.setPadding(
                new Insets(0, 22, 20, 22)
        );

        botonGuardar.setOnAction(e -> {

            String titulo = campoTitulo.getText().trim();
            String descripcion =
                    campoDescripcion.getText().trim();
            String responsable =
                    campoResponsable.getText().trim();

            if (titulo.isEmpty()) {
                mostrarAviso("El título es obligatorio.");
                campoTitulo.requestFocus();
                return;
            }

            if (esNueva) {
                Incidencia nueva = new Incidencia(
                        siguienteId++,
                        titulo,
                        descripcion,
                        campoEstado.getValue(),
                        campoPrioridad.getValue(),
                        responsable
                );

                incidencias.add(nueva);

            } else {
                incidenciaExistente.setTitulo(titulo);
                incidenciaExistente.setDescripcion(descripcion);
                incidenciaExistente.setEstado(campoEstado.getValue());
                incidenciaExistente.setPrioridad(campoPrioridad.getValue());
                incidenciaExistente.setResponsable(responsable);

                tabla.refresh();
            }

            aplicarFiltros();
            actualizarResumen();
            ventana.close();
        });

        botonCancelar.setOnAction(e -> ventana.close());

        VBox raiz = new VBox(formulario, botones);

        Scene escena = new Scene(raiz, 540, 390);

        ventana.setScene(escena);
        ventana.setMinWidth(500);
        ventana.showAndWait();
    }

    private void actualizarResumen() {
        long abiertas = incidencias.stream()
                .filter(i -> i.getEstado().equals("Abierta"))
                .count();

        long enProceso = incidencias.stream()
                .filter(i -> i.getEstado().equals("En proceso"))
                .count();

        long cerradas = incidencias.stream()
                .filter(i -> i.getEstado().equals("Cerrada"))
                .count();

        totalLabel.setText(
                "Número de incidencias: " + incidencias.size()
        );

        abiertasLabel.setText(
                "Incidencias abiertas: " + abiertas
        );

        procesoLabel.setText(
                "Incidencias en proceso: " + enProceso
        );

        cerradasLabel.setText(
                "Incidencias cerradas: " + cerradas
        );
    }

    private void mostrarAviso(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Gestor de incidencias");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
