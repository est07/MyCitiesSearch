
[![CI](https://github.com/est07/MyCitiesSearch/actions/workflows/CI.yml/badge.svg)](https://github.com/est07/MyCitiesSearch/actions/workflows/CI.yml)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io)

# My Cities Search

Esta aplicación fue realizada con el objetivo de descargar desde una URL un archivo .json que contiene una lista de ciudades con su información, la información
se organiza para ofrecer una buena experiencia al usuario, además permite que el usuario vea la ubicación de cada ciudad en un mapa al seleccionarla de la lista.

Para el desarrollo de la aplicación se utilizaron diferentes componentes como ViewModel corrutinas para el manejo de hilos Flow para
la programación reactiva Room para la base de datos local Koin para la inyección de dependencias y la interfaz de usuario en Compose Android.

## Using :

- [Retrofit](http://square.github.io/retrofit/)

- [Moshi](https://github.com/square/moshi)

- [Room](https://developer.android.com/training/data-storage/room?hl=es-419)

- [Coroutines](https://kotlinlang.org/docs/coroutines-basics.html)

- [Flow](https://developer.android.com/kotlin/flow?hl=es-419)

- [Koin](https://github.com/InsertKoinIO/koin)

- [AndroidX](https://developer.android.com/jetpack/androidx)

### Project structure 

El proyecto fue realizado con el patrón de diseño de clean architecture por lo cual esta compuesto por tres package los cuales son data, domain,  y presentation.

En el package de datos se encuentra el paquete de network que contiene los componentes para descargar desde una URL el archivo .json, en el package
de database están los componentes para manejar todos los procesos y configuraciones en la base de datos local, y en el package de repositories se encuentran
las clases que conectan la capa de dominio con la de datos.

En el package de dominio se encuentran los componentes que permiten la comunicación entre esta capa y las de datos y la capa de presentación
además de los datos que se intercambian entre estas capas, si existe una lógica de negocio también se manejaría en esta capa, debido a que la
consulta a la URL para descargar el archivo .json y guardarlo en la base de datos es lógica de negocio esta tarea se maneja en un caso de uso,
por lo tanto se tiene un package de usecases, en el package de repositories están las interfaces que conectan el dominio con la capa de datos
y en la de models están las data class que se utilizan en las capas de dominio y presentación.

En el package de presentation se encuentran todos los componentes que son utilizados para mostrar las vistas.

Todas las tareas desarrolladas en el proyecto se organizaron utilizando GitHub Projects y el board utilizado fue [Board](https://github.com/users/est07/projects/1/views/1)
el cual estaba organizado en los diferentes estados en los que puede estar una tarea durante el desarrollo de un proyecto.

![Board](https://github.com/est07/MyCitiesSearch/blob/feature/clean-project/images/screenshot_board.png)

## Getting Started
1. Clonar el repositorio
2. Abrir the project en Android Studio
3. En el archivo local.properties, agregar la URL suministrada para la prueba, para descargar el .json sin el cities.json al final de la URL, de la siguiente forma:
 `JSON_URL_API=https://gist.githubusercontent.com/hernan-uala/{your_key}/raw/{your_key}/`

![LoacalProperties](https://github.com/est07/MyCitiesSearch/blob/feature/clean-project/images/screenshot_local.properties.png)

4. En el archivo strings.xml, agregar una Google_Maps_Key válida para poder visualizar el mapa en el string google_maps_key, de la siguiente forma:
 `<string name="google_maps_key">your_google_maps_key</string>`

![LoacalProperties](https://github.com/est07/MyCitiesSearch/blob/feature/clean-project/images/screenshot_strings.png)

5. Build and run la aplicacion

## Preview

![MainView](https://github.com/est07/MyCitiesSearch/blob/feature/clean-project/images/main_view.gif)

![FavoritesFilter](https://github.com/est07/MyCitiesSearch/blob/feature/clean-project/images/favorites_filter.gif)

![MapView](https://github.com/est07/MyCitiesSearch/blob/feature/clean-project/images/map_view.gif)

![ErrorView](https://github.com/est07/MyCitiesSearch/blob/feature/clean-project/images/map_view.gif)

![LadscapeView](https://github.com/est07/MyCitiesSearch/blob/feature/clean-project/images/landscape_view.gif)

## Resources:
- https://developer.android.com/develop/ui/compose/components/search-bar?hl=es-419
- https://m3.material.io/
- https://developer.android.com/topic/libraries/architecture/paging/v3-network-db?hl=es-419
- https://developer.android.com/courses/pathways/jetpack-compose-for-android-developers-4?hl=es-419
- https://developers.google.com/maps/documentation/android-sdk?hl=es-419
- https://developers.google.com/maps/documentation/android-sdk/maps-compose?hl=es-419
- https://medium.com/@janand1991/mastering-device-orientation-detection-in-jetpack-compose-7bedf4679e9b
- https://mockk.io/
- https://developer.android.com/develop/ui/compose/testing?hl=es-419
