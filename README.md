# Astra Server
Servidor de scrapping web basado en arquitectura cliente-servidor. Permite procesar peticiones de 
scrapping de un cliente limitadas a modelos JSON precargados en el servidor

Permite al cliente realizar busquedas automáticas

>[!NOTE]
>El servicio lanzado por Astra no es una API, por lo que para el uso del un servidor Astra se requiere de un conector

## 🔧 Instalación del servidor
### Requisitos previos
- Navegador Google Chrome versión 120 o superior instalado
- Java 18 o superior

### Procedimiento de instalación
1. En una terminal lance el servidor con el comando
```java -jar astra-server-xxx-fullserver-jar-with-dependencies.jar [<puerto>]```. Si no se especifica puerto, por defecto se usará el 26700
2. Al primer arranque se configurará la carpeta de donde el servidor cargará los modelos de scrapping y podrá configurar un token para limitar el acceso (opcional)
3. El programa se cerrará solo. Pegue los modelos .json en la carpeta 'models' y vuelva a ejecutar el programa servidor

>[!NOTE]
>Las conexiones entre el servidor y los distintos clientes funcionan por Sockets y van cifrados de extremo a extremo

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
## ⚒️ Building del proyecto
### Requisitos previos
- Apache Maven instalado
- Java JDK 18 o superior

### Procedimiento de compilación
1. Primeramente clone el repositorio usando `git clone https://github.com/Asierso/astra` o descargue alguna de las versiones de lanzamiento del proyecto
2. Acceda a la carpeta raíz del repositorio de Astra
3. Ejecute el comando `mvn package` para generar los ficheros .jar tanto del servidor como del conector cliente



## 📋 Definicion de modelos JSON para scrapping 
Los modelos de scrapping se definen de lado del servidor y se cargan de la carpeta `models` creada en el primer arranque. Todo JSON de modelos tiene la siguiente sintaxis:

```json
{
  "name":"<nombre-modelo>",
  "version":"<version-modelo>",
  "args":[
    //argumentos del navegador
  ], 
  "actions":[
    //acciones de arranque del modelo
  ],
  "hooks":[
    //acciones de funcionalidad del modelo (llamados desde conector)
  ]
}
```
>[!NOTE]
>Es obligatorio que todos los modelos cargados en el servidor tengan un nombre distinto

### ⚓ Definicion de acciones y acciones de hooks
Las acciones tanto de arranque del modelo (actions) como de funcionalidad de modelo (hooks) van definidas de la siguiente forma:

```json
  {
    "type":"<tipo-accion>",
    "parameters":"<parametros>",
    "element":"<expresion-afectada>",
    "finder":"<regla-busqueda>"
  }
```

Las acciones pueden definir más propiedades. Se muestra una tabla donde se recogen estas
|Nombre|Parametros|Descripcion|Elementos de control|
|--|--|--|--|
|GOTO| url :String  | Redirige el navegador a una URL | No requerido |
|PROMPT| texto :String  | Escribe texto en un control que soporte texto | Requerido |
|CLICK| -  | Pulsa sobre un control que soporte clicks | Requerido |
|SUMBIT| -  | Realiza el envío de un formulario | Requerido |
|GET_TEXT| -  | Toma el texto del control (en caso de especificarse varios, el texto se tomará solamente del primero) | Requerido |
|GET_ALL_TEXT| -  | Toma el texto del control (en caso de especificarse varios, se toma el texto de todos separados por espacios) | Requerido |
|DELAY| mills : int  | Realiza una espera | No requerido |
|HOOK| nombre-hook : String  | Ejecuta un hook del mismo modelo (solo ejecutable desde un hook) | No requerido |
|JS| fichero : String  | Ejecuta codigo JS de un fichero .js (no hace falta especificar extensión) | No requerido |
|GET_ATTRIBUTE| nombre: String  | Toma el valor según argumento especificado (en caso de especificarse varios, el argumento se tomará solamente del primero) | Requerido |
|GET_ALL_ATTRIBUTE| nombre: String  | Toma el valor según argumento especificado | Requerido |

>[!WARNING]
>En la versión de lanzamiento algunas de estas acciones podrían cambiar de nombre o incluso variar en funcionalidad. Se tiene previsto agregar nuevas funciones


### 👌 Selector de controles (element & finder)
La seleccion de controles de la página cargada en el scrapper se debe de especificar un modo de seleccion o **finder** y los elementos a seleccionar

Se puede realizar la selección por uno de los siguientes discriminantes por acción:
- ID: Por nombre del identificador
- CLASS: Por nombre de la clase
- TAG: Por nombre de la etiqueta
- XPATH: Indica que se usará una expresión XPATH

En la sintaxis del JSON sería indicado de la siguiente forma:

```json
    "element":"<expresion>",
    "finder":"<ID/CSS/TAG/XPATH>"
```

### ⚓ Definición de hooks
Un hook es un conjunto de acciones representadas por un nombre. Los hooks pueden ser ejecutados por el cliente para poder realizar las consultas al modelo correspondiente

Un hook tiene la siguiente estructura:

```json
  {
    "name":"<nombre-hook>",
    "output":"[<formato-salida>]",
    "actions":[
      //acciones secuenciales del hook
    ]
  }
```

La salida tanto de los hooks como de las acciones se envía com una lista con los distintos retornos de las consultas realizadas, pero a mayores, los hooks permiten devolver texto con la salida a la que se le aplica una máscara para formatear las líneas en un único texto usando el comodín `$<n>` donde "n" hace referencia al numero de salida de la línea ejecutada en secuencia

### 📂 Parametros de hooks 
Los hooks pueden tomar una lista con n número de parámetros (proporcionados por el usuario) para ejecutar las acciones. Para poder acceder a la entrada de parámetros desde una accion interna al hook, use `"useparams":"true"`. Para acceder al parámetro se usa el comodín `$<n>` donde n es la posicion del argumento en la lista.

Un hook puede tomar un resultado de ejecución de la secuencia como parámetro, definiendo en la accion: `"useoutput":"true"`. Para acceder, del mismo modo se usa el comodín `$<n>`.


## 🌐 Argumentos del navegador
Todos los argumentos soportados por defecto por el navegador Google Chrome se pueden especificar dentro de `args` en el JSON del modelo correspondiente.

>[!TIP]
>Para habilitar la depuración (fines de testeo del modelo) puede incluir el argumento `degubmode`