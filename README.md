# Astra Server ¡DOCUMENTACIÓN EN PROCESO!
Servidor de scrapping web basado en arquitectura cliente-servidor. Permite prcesar peticiones de 
scrapping de un cliente limitadas a modelos JSON precargados en el servidor

Permite al cliente realizar busquedas automáticas

>[!NOTE]
>El servicio lanzado por Astra no es una API, por lo que para el uso del un servidor Astra se requiere de un conector

## 🔧 Instalación del servidor
### Requisitos previos
- Navegador Google Chrome versión 100 o superior instalado
- Java 18 o superior

### Procedimiento de instalación
1. En una terminal lance el servidor con el comando
```java -jar astra-server-xxx-fullserver-jar-with-dependencies.jar [<puerto>]```. Si no se especifica puerto, por defecto se usará el 26700
2. Al primer arranque se configurará la carpeta de donde el servidor cargará los modelos de scrapping y podrá configurar un token para limitar el acceso (opcional)
3. El programa se cerrará solo. Pegue los modelos .json en la carpeta 'models' y vuelva a ejecutar el programa servidor


## 💻 Instalación del conector (Maven)
Puede agregar la dependencia a su proyecto Maven con el siguiente código en su fichero pom.xml. Recuerde sustituir "xxxxx" por la versión de su preferencia.

```xml
<dependency>
  <groupId>com.asierso</groupId>
  <artifactId>astra-connector</artifactId>
  <version>xxxxx</version>
</dependency>
```

Recuerde que para poder usar el paquete debe de agregar la URL del repositorio a su fichero pom.xml.

```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <name>GitHub Asierso Apache Maven Packages</name>
        <url>https://maven.pkg.github.com/Asierso/astra</url>
    </repository>
</distributionManagement>
```
