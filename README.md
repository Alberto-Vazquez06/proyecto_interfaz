# proyecto_interfaz
Repositorio para proyecto de DI
# Gestor de Incidencias

## Descripción

Esta aplicación permite gestionar incidencias mediante una interfaz gráfica desarrollada en JavaFX. Permite consultar las incidencias registradas, buscar por texto, filtrar por estado y añadir nuevas incidencias.

También permite consultar los detalles de una incidencia, modificar sus datos y guardar los cambios o cancelar la edición.

## Requisitos

- Tener instalado Java JDK 21.
- Tener instalado Maven.
- Disponer del proyecto descargado en el equipo.

## Cómo ejecutar la aplicación

1. Descargar o clonar el repositorio de GitHub.
2. Abrir una terminal en la carpeta raíz del proyecto, donde se encuentra el archivo `pom.xml`.
3. Ejecutar el siguiente comando para iniciar la aplicación:

   ```bash
   mvn clean javafx:run
   ```

4. Esperar a que Maven compile el proyecto y se abra la ventana de la aplicación.

## Funcionalidades

- **Añadir:** permite introducir los datos de una nueva incidencia.
- **Buscar:** permite buscar incidencias escribiendo texto en el campo de búsqueda y pulsando el botón correspondiente.
- **Filtrar:** permite mostrar las incidencias según su estado.
- **Detalles:** permite consultar y modificar los datos de una incidencia.
- **Guardar:** guarda los cambios realizados en una incidencia.
- **Cancelar:** cierra la ventana de edición sin aplicar los cambios.

## Tecnologías utilizadas

- Java 21.
- JavaFX 21.
- Maven.
