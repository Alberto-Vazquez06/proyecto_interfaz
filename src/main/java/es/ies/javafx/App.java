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
    
    private final ObservableList<Incidencia> incidencias = FXCollections.observableArrayList(); // Lista observable de incidencias
    
    // Lista filtrada de incidencias        
    private FilteredList<Incidencia> incidenciasFiltradas;
    private TableView<Incidencia> tabla;
    private TextField cajaBuscar;
    private ComboBox<String> filtroEstado;

    // Etiquetas para mostrar el resumen de incidencias
    private Label totalLabel;
    private Label abiertasLabel;
    private Label procesoLabel;
    private Label cerradasLabel;

    // Variable para llevar un seguimiento del siguiente ID disponible
    private int siguienteId = 6;

    // Método principal de la aplicación
    @Override
    public void start(Stage escenario) {

        // Cargar datos de ejemplo en la lista de incidencias
        cargarDatosDeEjemplo();

        incidenciasFiltradas = new FilteredList<>(incidencias, incidencia -> true);

        // Crear la interfaz de usuario
        Label titulo = new Label("GESTOR DE INCIDENCIAS");
        titulo.setStyle(
                "-fx-background-color: black;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 27px;" +
                "-fx-padding: 10 17 10 17;" +
                "-fx-background-radius: 35;"
        );

        // Crear la caja de búsqueda y los botones de búsqueda y añadir
        cajaBuscar = new TextField();
        cajaBuscar.setPromptText("Buscar incidencia...");
        cajaBuscar.setPrefWidth(500);
        cajaBuscar.setStyle(
                "-fx-border-color: black;" +
                "-fx-border-width: 2;"
        );

        // Crear los botones de búsqueda y añadir
        Button botonBuscar = crearBoton("Buscar");
        botonBuscar.setOnAction(e -> aplicarFiltros());

        // Crear el botón de añadir incidencia
        Button botonAnadir = crearBoton("Añadir");
        botonAnadir.setOnAction(e -> abrirFormulario(escenario, null));

        // Crear la fila de búsqueda con la caja de búsqueda y los botones
        HBox filaBusqueda = new HBox(25, cajaBuscar, botonBuscar, botonAnadir);

        // Establecer el alineamiento de la fila de búsqueda y permitir que la caja de búsqueda se expanda
        filaBusqueda.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(cajaBuscar, Priority.ALWAYS);

        // Crear el combo box para filtrar por estado
        filtroEstado = new ComboBox<>();
        filtroEstado.getItems().addAll(
                "Todos los estados",
                "Abierta",
                "En proceso",
                "Cerrada"
        );
        filtroEstado.setValue("Todos los estados"); // Establecer el valor predeterminado del combo box
        filtroEstado.setPrefWidth(170); // Establecer el ancho preferido del combo box
        filtroEstado.setOnAction(e -> aplicarFiltros()); // Aplicar filtros cuando se seleccione un estado

        // Crear la fila de filtro con el combo box y establecer su alineamiento
        HBox filaFiltro = new HBox(filtroEstado);
        filaFiltro.setAlignment(Pos.CENTER_RIGHT);

        // Crear la tabla para mostrar las incidencias
        tabla = new TableView<>();
        tabla.setItems(incidenciasFiltradas); // Establecer la lista filtrada como elementos de la tabla
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN ); // Establecer la política de redimensionamiento de columnas de la tabla
        tabla.setPlaceholder( new Label("No hay incidencias que mostrar")); // Establecer el texto que se muestra cuando no hay incidencias

        // Crear las columnas de la tabla y establecer sus propiedades
        TableColumn<Incidencia, Integer> columnaId = new TableColumn<>("ID"); 
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id")); // Establecer la propiedad de la columna para mostrar el ID de la incidencia
        columnaId.setMaxWidth(75);

        // Crear las columnas de la tabla y establecer sus propiedades
        TableColumn<Incidencia, String> columnaTitulo = new TableColumn<>("Título");
        columnaTitulo.setCellValueFactory( new PropertyValueFactory<>("titulo"));

        TableColumn<Incidencia, String> columnaEstado =  new TableColumn<>("Estado");
        columnaEstado.setCellValueFactory( new PropertyValueFactory<>("estado") );

        TableColumn<Incidencia, String> columnaPrioridad = new TableColumn<>("Prioridad");
        columnaPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));

        TableColumn<Incidencia, String> columnaResponsable = new TableColumn<>("Responsable");
        columnaResponsable.setCellValueFactory( new PropertyValueFactory<>("responsable"));

        // Agregar las columnas a la tabla
        tabla.getColumns().addAll(
                columnaId,
                columnaTitulo,
                columnaEstado,
                columnaPrioridad,
                columnaResponsable
        );

        // Crear el botón de detalle y establecer su acción al hacer clic
        Button botonDetalle = crearBoton("Detalle");

        // Establecer la acción del botón de detalle para abrir el formulario con los detalles de la incidencia seleccionada
        botonDetalle.setOnAction(e -> {
            Incidencia seleccionada = tabla.getSelectionModel().getSelectedItem();

            if (seleccionada == null) {
                mostrarAviso("Selecciona una incidencia de la tabla.");

            } else {
                abrirFormulario(escenario, seleccionada);
            }
        });

        // Crear la fila de detalle con el botón de detalle y establecer su alineamiento
        HBox filaDetalle = new HBox(botonDetalle);
        filaDetalle.setAlignment(Pos.CENTER);

        // Crear el contenedor principal de la aplicación con un VBox y establecer su contenido y estilo
        VBox contenido = new VBox(
                20,
                titulo,
                filaBusqueda,
                filaFiltro,
                tabla,
                filaDetalle
        );

        // Establecer el padding del contenedor principal
        contenido.setPadding(new Insets(26, 42, 20, 42));

        // Establecer que la tabla ocupe todo el espacio disponible en el VBox
        VBox.setVgrow(tabla, Priority.ALWAYS);

        // Crear las etiquetas para mostrar el resumen de incidencias
        totalLabel = new Label();
        abiertasLabel = new Label();
        procesoLabel = new Label();
        cerradasLabel = new Label();

        // Crear el contenedor HBox para el resumen de incidencias y establecer su contenido y estilo
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

        // Crear el contenedor BorderPane para la aplicación y establecer su contenido
        BorderPane raiz = new BorderPane();
        raiz.setCenter(contenido);
        raiz.setBottom(resumen);

        // Actualizar el resumen de incidencias al iniciar la aplicación
        actualizarResumen();

        // Crear la escena principal de la aplicación con el contenedor BorderPane y establecer su tamaño
        Scene escena = new Scene(raiz, 1100, 650);

        // Configurar el escenario principal de la aplicación con el título, tamaño mínimo y escena
        escenario.setTitle("Gestor de incidencias");
        escenario.setMinWidth(850);
        escenario.setMinHeight(560);
        escenario.setScene(escena);
        escenario.show();
    }

    // ======MÉTODOS AUXILIARES======
    // Método para crear un botón con estilo personalizado
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

    // Método para cargar datos de ejemplo en la lista de incidencias (Estas incidencias son un ejemplo)
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

    // Método para aplicar los filtros de búsqueda y estado a la lista de incidencias
    private void aplicarFiltros() {
        String texto = cajaBuscar.getText().trim().toLowerCase(Locale.ROOT);

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

    // Método para abrir el formulario de añadir o editar una incidencia
    private void abrirFormulario( Stage propietario, Incidencia incidenciaExistente) {

        boolean esNueva = incidenciaExistente == null;

        Stage ventana = new Stage();
        ventana.initOwner(propietario);
        ventana.initModality(Modality.APPLICATION_MODAL);

        ventana.setTitle(
                esNueva
                        ? "Añadir incidencia"
                        : "Detalles de la incidencia"
        );

        TextField campoTitulo = new TextField(esNueva ? "" : incidenciaExistente.getTitulo());

        TextArea campoDescripcion = new TextArea(esNueva ? "" : incidenciaExistente.getDescripcion());
        campoDescripcion.setWrapText(true);
        campoDescripcion.setPrefRowCount(4);

        ComboBox<String> campoEstado = new ComboBox<>();
        campoEstado.getItems().addAll(
                "Abierta",
                "En proceso",
                "Cerrada"
        );
        campoEstado.setValue(esNueva ? "Abierta" : incidenciaExistente.getEstado());

        ComboBox<String> campoPrioridad = new ComboBox<>();
        campoPrioridad.getItems().addAll(
                "Baja",
                "Media",
                "Alta",
                "Crítica"
        );
        campoPrioridad.setValue(esNueva ? "Media" : incidenciaExistente.getPrioridad());

        TextField campoResponsable = new TextField(esNueva ? "" : incidenciaExistente.getResponsable());

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
        botones.setPadding(new Insets(0, 22, 20, 22));

        botonGuardar.setOnAction(e -> {

            String titulo = campoTitulo.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            String responsable = campoResponsable.getText().trim();

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

    // Método para actualizar el resumen de incidencias en las etiquetas
    private void actualizarResumen() {
        long abiertas = incidencias.stream().filter(i -> i.getEstado().equals("Abierta")).count();

        long enProceso = incidencias.stream().filter(i -> i.getEstado().equals("En proceso")).count();

        long cerradas = incidencias.stream() .filter(i -> i.getEstado().equals("Cerrada")).count();

        totalLabel.setText("Número de incidencias: " + incidencias.size());

        abiertasLabel.setText("Incidencias abiertas: " + abiertas);

        procesoLabel.setText("Incidencias en proceso: " + enProceso);

        cerradasLabel.setText("Incidencias cerradas: " + cerradas);
    }

    // Método para mostrar un aviso al usuario con un mensaje específico
    private void mostrarAviso(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);// Crear una alerta de tipo información
        alerta.setTitle("Gestor de incidencias");// Establecer el título de la alerta
        alerta.setHeaderText(null);// Establecer el encabezado de la alerta como nulo (sin encabezado)
        alerta.setContentText(mensaje);// Establecer el contenido de la alerta con el mensaje proporcionado
        alerta.showAndWait();// Mostrar la alerta y esperar a que el usuario la cierre
    }
// Método principal de la aplicación
    public static void main(String[] args) {
        launch(args);
    }
}
