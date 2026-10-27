# Dum

Simple ray casting game made with libGDX.

## Building / running

(./gradlew or whatever ide you want)

- `./gradlew lwjgl3:run`: starts the application.
- `./gradlew lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `./gradlew build`: builds sources and archives of every project.
- `./gradlew clean`: removes `build` folders, which store compiled classes and built archives.
- `html:dist`: compiles GWT sources. The compiled application can be found at `html/build/dist`: you can use any HTTP server to deploy it.
- `html:superDev`: compiles GWT sources and runs the application in SuperDev mode. It will be available at [localhost:8080/html](http://localhost:8080/html). Use only during development.
