# Apuntes TDS
---
---
# Patrones de Creación



## Factoría Abstracta

**Etiquetas:** #patrones-diseño #creacional #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas **palabras clave** o situaciones en el enunciado:

1.  **"Familias de objetos":** El problema menciona que hay varios objetos que *deben ir juntos* (ej: un motor, una rueda y un chasis).
2.  **"Compatibilidad/Coherencia":** Se enfatiza que no se pueden mezclar objetos de diferentes familias (ej: "No se puede usar una pieza de la Familia A con una de la Familia B").
3.  **"Multi-tema" o "Multi-plataforma":** El sistema debe cambiar completamente de comportamiento o apariencia según una configuración inicial (ej: Modo Oscuro vs Claro, Windows vs Linux, Conexión Local vs Remota).

> [!TIP] Regla de Oro
> Si el problema pide crear **UN** solo tipo de objeto, es **Método Factoría**.
> Si pide crear **VARIOS** tipos de objetos que están **relacionados**, es **Factoría Abstracta**.

---

### 🔑 Definición Genérica
Interfaz que permite crear **familias de objetos relacionados** o dependientes sin especificar sus clases concretas.

*El cliente trabaja con la fábrica, pero no sabe qué familia específica está usando.*

---

### 🏗️ Estructura Genérica (UML Mental)

Para el examen, dibuja este esquema mentalmente:

1.  **AbstractFactory**: Interfaz que define `createProductoA()` y `createProductoB()`.
2.  **ConcreteFactory1**: Implementa la interfaz y crea `ProductoA1` y `ProductoB1`.
3.  **ConcreteFactory2**: Implementa la interfaz y crea `ProductoA2` y `ProductoB2`.
4.  **AbstractProductA** y **AbstractProductB**: Interfaces de los productos.


---

### 💻 Ejemplo de Código (Caso: Mueblería)

*Problema: Evitar que un cliente reciba una Silla Victoriana con un Sofá Moderno.*

```java
// INTERFACES DE PRODUCTOS (Abstract Product)
interface Silla {
    void sentarse();
}

interface Sofa {
    void tumbarse();
}

// INTERFAZ DE LA FÁBRICA (Abstract Factory)
interface FabricaMuebles {
    Silla crearSilla();
    Sofa crearSofa();
}

// PRODUCTOS CONCRETOS (Concrete Products)
class SillaVictoriana implements Silla {
    @Override
    public void sentarse() {
        System.out.println("Sentado en una Silla Victoriana.");
    }
}

class SofaVictoriano implements Sofa {
    @Override
    public void tumbarse() {
        System.out.println("Tumbado en un Sofá Victoriano.");
    }
}

// FÁBRICA CONCRETA (Concrete Factory)
class FabricaVictoriana implements FabricaMuebles {
    @Override
    public Silla crearSilla() {
        return new SillaVictoriana();
    }
    @Override
    public Sofa crearSofa() {
        return new SofaVictoriano();
    }
}

// CLIENTE
class Tienda {
    private FabricaMuebles fabrica;

    public Tienda(FabricaMuebles fabrica) {
        this.fabrica = fabrica;
    }

    public void venderSala() {
        // La fábrica garantiza que silla y sofá combinen.
        Silla silla = fabrica.crearSilla();
        Sofa sofa = fabrica.crearSofa();
        silla.sentarse();
        sofa.tumbarse();
    }
}
```
---
## Builder

**Etiquetas:** #patrones-diseño #creacional #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas señales de alarma en el enunciado:

1.  **"Constructor Monstruoso":** Un objeto tiene un constructor con muchísimos parámetros, y la mayoría son opcionales o `null`.
    * *Ejemplo:* `new Casa(paredes, techo, null, null, true, false, null...)`
2.  **"Paso a paso":** El problema dice que el objeto debe construirse en varias etapas secuenciales.
3.  **"Mismo proceso, diferente resultado":** Necesitas construir objetos muy distintos (ej: Una Casa de Madera y un Castillo de Piedra) pero los pasos son los mismos (poner paredes -> poner techo -> poner ventanas).

> [!TIP] Diferencia Clave con Abstract Factory
> * **Abstract Factory:** Te da el objeto "ya hecho" en una sola llamada.
> * **Builder:** Tú controlas la construcción **paso a paso** (`ponerParedes()`, luego `ponerTecho()`...).
> * **Regla:** ¿Objeto complejo con muchas configuraciones? -> **Builder**.

---

### 🔑 Definición
Patrón que permite construir objetos complejos **paso a paso**. Separa la *construcción* de un objeto de su *representación*, permitiendo que el mismo proceso de construcción cree representaciones diferentes.

---

### 💻 Ejemplo de Código (Caso: Construir una Casa)

*Problema: Una casa puede tener piscina, garaje, estatuas, jardín... Si ponemos todo en el constructor, es un caos.*

```java
// PRODUCTO (La Casa que estamos construyendo)
class Casa {
    private String paredes = "sin construir";
    private boolean tienePiscina = false;
    private boolean tieneGaraje = false;

    public void setParedes(String paredes) { this.paredes = paredes; }
    public void setPiscina(boolean tienePiscina) { this.tienePiscina = tienePiscina; }
    public void setGaraje(boolean tieneGaraje) { this.tieneGaraje = tieneGaraje; }

    @Override
    public String toString() {
        return "Casa [Paredes: " + paredes + ", Piscina: " + (tienePiscina ? "Sí" : "No") + ", Garaje: " + (tieneGaraje ? "Sí" : "No") + "]";
    }
}

// INTERFAZ BUILDER (El plano de pasos)
interface ConstructorCasa {
    void reset();
    void construirParedes();
    void construirGaraje();
    void construirPiscina();
    Casa obtenerResultado();
}

// CONCRETE BUILDER (El albañil específico)
class ConstructorCasaLujo implements ConstructorCasa {
    private Casa casa;

    public ConstructorCasaLujo() {
        this.reset();
    }

    @Override
    public void reset() {
        this.casa = new Casa();
    }

    @Override
    public void construirParedes() { this.casa.setParedes("Mármol"); }
    @Override
    public void construirGaraje() { this.casa.setGaraje(true); }
    @Override
    public void construirPiscina() { this.casa.setPiscina(true); }

    @Override
    public Casa obtenerResultado() {
        Casa producto = this.casa;
        this.reset(); // Listo para construir la siguiente casa
        return producto;
    }
}

// DIRECTOR (El Jefe de Obra)
class Director {
    private ConstructorCasa builder;

    public void setBuilder(ConstructorCasa builder) {
        this.builder = builder;
    }

    // Método para construir una 'receta' popular
    public void construirMansion() {
        this.builder.construirParedes();
        this.builder.construirGaraje();
        this.builder.construirPiscina();
    }
}

// CLIENTE
class DemoBuilder {
    public static void main(String[] args) {
        Director director = new Director();
        ConstructorCasaLujo constructorLujo = new ConstructorCasaLujo();

        director.setBuilder(constructorLujo);
        director.construirMansion(); // Ejecuta los pasos

        Casa miCasa = constructorLujo.obtenerResultado();
        System.out.println(miCasa);
    }
}
```
---

## Método Factoría

**Etiquetas:** #patrones-diseño #creacional #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas pistas en el enunciado:

1.  **"Desconocimiento del tipo exacto":** El código base no sabe qué clase exacta tendrá que instanciar hasta que el programa esté corriendo.
2.  **"Extensibilidad vía Herencia":** Se pide que sea fácil añadir nuevos tipos de productos creando **subclases** de una clase creadora, sin modificar el código existente.
3.  **"Delegar la creación":** Una clase padre define una operación estándar, pero delega la creación del objeto específico a sus hijos.

> [!TIP] Diferencia con Abstract Factory
> * **Factory Method:** Se basa en **Herencia** (una subclase decide qué crear). Crea **UN** solo producto.
> * **Abstract Factory:** Se basa en **Composición** (un objeto fábrica crea cosas). Crea **FAMILIAS** de productos.
> * **Regla:** ¿Solo necesitas desacoplar la creación de un único producto (ej: un Botón o un Transporte)? -> **Factory Method**.

---

### 🔑 Definición
Patrón que define una interfaz (o método abstracto) para crear un objeto, pero deja que sean las **subclases** quienes decidan qué clase concreta instanciar. Permite que una clase delegue la instanciación a sus subclases.

---

### 💻 Ejemplo de Código (Caso: Logística)

*Problema: Una empresa de logística solo usaba Camiones. Ahora quiere añadir Barcos, pero todo su código está acoplado a la clase `Camion`.*

```java
// 1. PRODUCTO (Interfaz común)
// Todos los transportes hacen lo mismo: entregar.
interface Transporte {
    void entrega();
}

// 2. PRODUCTOS CONCRETOS
class Camion implements Transporte {
    @Override
    public void entrega() {
        System.out.println("Entrega por tierra en una caja.");
    }
}

class Barco implements Transporte {
    @Override
    public void entrega() {
        System.out.println("Entrega por mar en un contenedor.");
    }
}

// 3. CREATOR (Clase Base)
// Define el "Factory Method" abstracto.
abstract class Logistica {
    
    // ESTE es el Factory Method
    // La clase padre no sabe qué transporte se creará.
    public abstract Transporte crearTransporte();

    // Lógica de negocio que usa el producto
    public void planificarEntrega() {
        // Llama al factory method para obtener un objeto transporte
        Transporte t = crearTransporte();
        // Usa el objeto (sin saber si es camión o barco)
        t.entrega();
    }
}

// 4. CONCRETE CREATORS (Subclases)
// Cada subclase decide qué transporte concreto devolver.

class LogisticaTerrestre extends Logistica {
    @Override
    public Transporte crearTransporte() {
        return new Camion();
    }
}

class LogisticaMaritima extends Logistica {
    @Override
    public Transporte crearTransporte() {
        return new Barco();
    }
}

// 5. CLIENTE
class DemoLogistica {
    public static void main(String[] args) {
        // Podemos cambiar la logística sin tocar el resto del código
        Logistica logistica = new LogisticaMaritima();
        
        // El cliente solo llama a planificarEntrega()
        // No sabe que por dentro se creó un Barco.
        logistica.planificarEntrega();
    }
}
```
---

## Singleton

**Etiquetas:** #patrones-diseño #creacional #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas situaciones clave en el enunciado:

1.  **"Instancia Única":** El requisito explícito de que **solo puede existir un objeto** de una clase en todo el programa (ej: Una única conexión a la base de datos, un único gestor de archivos, un Gobierno).
2.  **"Acceso Global":** Se necesita acceder a ese objeto desde cualquier parte del código, pero sin usar variables globales sucias.
3.  **"Recurso Compartido":** Varios clientes deben usar *exactamente el mismo objeto* para coordinarse.

> [!TIP] Regla de Oro
> Si ves que el constructor es **privado** (`private Constructor()`), es un **Singleton**.
> Es el único patrón que "se bloquea a sí mismo" para que nadie más pueda crearlo con `new`.

---

### 🔑 Definición
Patrón que garantiza que una clase tenga **una única instancia** y proporciona un punto de acceso global a ella.

---

### 💻 Ejemplo de Código (Caso: Conexión Base de Datos)

*Problema: No queremos abrir 100 conexiones a la base de datos. Queremos abrir una sola y que todos usen esa.*

```java
public class BaseDeDatos {
    // 1. VARIABLE ESTÁTICA PRIVADA
    // Aquí se guardará la única instancia que existirá.
    private static BaseDeDatos instancia;
    
    public String estado;

    // 2. CONSTRUCTOR PRIVADO
    // ¡Crucial! Evita que nadie haga 'new BaseDeDatos()' desde fuera.
    private BaseDeDatos(String valor) {
        this.estado = valor;
        System.out.println("Iniciando conexión a BD...");
        // Simular operación costosa
    }

    // 3. MÉTODO ESTÁTICO DE ACCESO (getInstance)
    // Es la única puerta de entrada para obtener el objeto.
    public static BaseDeDatos getInstance() {
        // Lazy Loading (Carga Perezosa):
        // Solo creamos el objeto si no existe aún.
        if (instancia == null) {
            instancia = new BaseDeDatos("Conectado");
        }
        return instancia;
    }
    
    public void ejecutarQuery(String sql) {
        System.out.println("Ejecutando en " + estado + ": " + sql);
    }
}

// 4. CLIENTE
class DemoSingleton {
    public static void main(String[] args) {
        // BaseDeDatos bd = new BaseDeDatos(); // ¡ERROR DE COMPILACIÓN!
        
        // Obtenemos la instancia
        BaseDeDatos bd1 = BaseDeDatos.getInstance();
        bd1.ejecutarQuery("SELECT * FROM usuarios");

        // Intentamos obtener "otra" instancia
        BaseDeDatos bd2 = BaseDeDatos.getInstance();
        
        // Comprobación: Ambas variables apuntan AL MISMO objeto en memoria.
        System.out.println(bd1 == bd2); // Imprime: true
    }
}
```
---
---

# Patrones Estructurales



## Adaptador

**Etiquetas:** #patrones-diseño #estructural #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas pistas claras:

1.  **"Incompatibilidad":** Tienes dos clases que deberían trabajar juntas pero sus interfaces son diferentes (ej: una devuelve XML y la otra espera JSON).
2.  **"Código Legacy" o "Librería de terceros":** Quieres usar una clase antigua o una librería externa que **no puedes modificar**, pero su interfaz no encaja con tu sistema actual.
3.  **"Wrapper" / "Envoltorio":** El problema sugiere crear una clase intermedia que traduzca las llamadas de un formato a otro.

> [!TIP] Regla de Oro
> * Piensa en un **Adaptador de Viaje** para enchufes.
> * Tu sistema (El Cliente) espera un enchufe de 2 patas planas (Interfaz A).
> * La herramienta (El Adaptee) tiene 3 patas redondas (Interfaz B).
> * El **Adapter** conecta ambos traduciendo la forma.
> * **Clave:** Si dice "conectar interfaces incompatibles" -> **Adapter**.

---

### 🔑 Definición
Patrón estructural que permite que objetos con interfaces incompatibles colaboren. Actúa como un intermediario que traduce las llamadas del código cliente a un formato que la clase envuelta (Adaptee) pueda entender.

---

### 💻 Ejemplo de Código (Caso: Sensor de Temperatura)

*Problema: Nuestra aplicación lee temperatura en **Celsius**, pero compramos un sensor avanzado americano que solo da datos en **Fahrenheit**.*

```java
// 1. TARGET (La interfaz que nuestro sistema YA usa)
// Nuestro sistema espera leer grados Celsius.
interface SensorCelsius {
    double obtenerTemperaturaC();
}

// 2. ADAPTEE (La clase incompatible / Librería externa)
// Es útil, pero su interfaz es diferente (Fahrenheit).
// ¡Imagina que NO podemos cambiar este código!
class SensorFahrenheit {
    public double getTemperatureF() {
        return 98.6; // Simula devolver grados F
    }
}

// 3. ADAPTER (El traductor)
// Implementa la interfaz que espera el cliente (Target)
// y "envuelve" al objeto incompatible (Adaptee).
class AdaptadorSensor implements SensorCelsius {
    private SensorFahrenheit sensorFahrenheit;

    public AdaptadorSensor(SensorFahrenheit sensor) {
        this.sensorFahrenheit = sensor;
    }

    @Override
    public double obtenerTemperaturaC() {
        // A. Llama al método incompatible
        double tempF = this.sensorFahrenheit.getTemperatureF();
        
        // B. Traduce/Convierte los datos
        double tempC = (tempF - 32) * 5 / 9;
        
        // C. Devuelve el dato en el formato esperado
        return tempC;
    }
}

// 4. CLIENTE
class AppClima {
    public static void main(String[] args) {
        // Situación vieja:
        // SensorCelsius sensor = new SensorAntiguo(); // OK
        
        // Situación nueva con el adaptador:
        SensorFahrenheit sensorGringo = new SensorFahrenheit();
        
        // El cliente usa la interfaz SensorCelsius, no sabe que dentro hay un sensor Fahrenheit
        SensorCelsius adaptador = new AdaptadorSensor(sensorGringo);
        
        System.out.println("Temperatura: " + adaptador.obtenerTemperaturaC() + " ºC");
    }
}
```
---

## Composite

**Etiquetas:** #patrones-diseño #estructural #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas pistas inconfundibles:

1.  **"Estructura de Árbol":** El problema describe una jerarquía "Parte-Todo" (ej: Sistema de archivos con Carpetas y Archivos; Menús y Submenús; Un ejército con Generales, Capitanes y Soldados).
2.  **"Trato Uniforme":** El enunciado pide que el cliente pueda tratar a un objeto simple (Hoja) y a un grupo de objetos (Compuesto) **de la misma manera**.
    * *Ejemplo:* Quiero llamar a `calcularPrecio()` y que funcione igual si es un solo producto o una caja llena de productos y otras cajas.
3.  **"Recursividad":** La solución implica recorrer una estructura anidada.

> [!TIP] Regla de Oro
> * ¿Tienes objetos dentro de objetos (como cajas dentro de cajas)?
> * ¿Quieres ignorar si estás manejando uno solo o un grupo?
> * **-> Composite.**
> * *Analogía:* Un sistema de archivos. Una carpeta puede contener archivos u otras carpetas. Al hacer "copiar", no te importa qué hay dentro, copias todo el árbol.

---

### 🔑 Definición
Patrón estructural que permite componer objetos en estructuras de árbol para representar jerarquías de parte-todo. Permite a los clientes tratar a objetos individuales y a composiciones de objetos de manera uniforme.

---

[Image of Composite design pattern UML structure]

### 💻 Ejemplo de Código (Caso: Caja de Productos)

*Problema: Tenemos un pedido. Un pedido puede tener productos sueltos, pero también cajas que contienen productos u OTRAS cajas más pequeñas. Queremos saber el precio total.*

```java
import java.util.ArrayList;
import java.util.List;

// 1. COMPONENT (Interfaz Común)
// Define las operaciones comunes tanto para hojas como para contenedores.
interface ComponentePedido {
    double obtenerPrecio();
}

// 2. LEAF (La Hoja - El objeto final)
// Es un producto simple, no tiene nada dentro. Sabe su precio.
class Producto implements ComponentePedido {
    private String nombre;
    private double precio;

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public double obtenerPrecio() {
        return this.precio;
    }
}

// 3. COMPOSITE (El Contenedor / La Rama)
// Tiene una lista de hijos (que pueden ser Productos u otras Cajas).
// Delega el cálculo a sus hijos.
class Caja implements ComponentePedido {
    // La magia: La lista es de la INTERFAZ, no de una clase concreta.
    private List<ComponentePedido> contenido = new ArrayList<>();

    public void agregar(ComponentePedido componente) {
        contenido.add(componente);
    }

    public void quitar(ComponentePedido componente) {
        contenido.remove(componente);
    }

    @Override
    public double obtenerPrecio() {
        double precioTotal = 0;
        // RECURSIVIDAD: Recorre sus hijos.
        // Si el hijo es Producto, suma su precio.
        // Si el hijo es Caja, esa Caja sumará lo suyo internamente.
        for (ComponentePedido c : contenido) {
            precioTotal += c.obtenerPrecio();
        }
        return precioTotal;
    }
}

// 4. CLIENTE
class Tienda {
    public static void main(String[] args) {
        // Crear productos simples (Hojas)
        Producto martillo = new Producto("Martillo", 10.0);
        Producto clavos = new Producto("Pack Clavos", 2.0);
        Producto telefono = new Producto("iPhone", 1000.0);

        // Crear una caja pequeña
        Caja cajaHerramientas = new Caja();
        cajaHerramientas.agregar(martillo);
        cajaHerramientas.agregar(clavos);

        // Crear una caja grande (contiene un teléfono y la caja de herramientas)
        Caja cajaEnvio = new Caja();
        cajaEnvio.agregar(telefono);
        cajaEnvio.agregar(cajaHerramientas); // ¡Añadimos una caja dentro de otra!

        // El cliente solo pide el precio, no necesita saber la estructura interna.
        System.out.println("Precio total del pedido: " + cajaEnvio.obtenerPrecio());
        // Resultado: 1000 + (10 + 2) = 1012
    }
}
```
---

## Decorator

**Etiquetas:** #patrones-diseño #estructural #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas situaciones:

1.  **"Explosión de Subclases":** El problema describe que intentar añadir características mediante herencia genera demasiadas combinaciones.
    * *Ejemplo:* `NotificadorEmail`, `NotificadorSMS`, `NotificadorEmailYSMS`, `NotificadorEmailYSlack`... ¡Es inmanejable!
2.  **"Añadir responsabilidades dinámicamente":** Se necesita añadir o quitar funcionalidades a un objeto **en tiempo de ejecución** (sin compilar de nuevo).
3.  **"Capas de cebolla" o "Matryoshka":** Se habla de envolver un objeto dentro de otro para añadirle comportamiento antes o después de la operación principal.

> [!TIP] Regla de Oro
> * Piensa en **Vestimeta** o **Ingredientes de Pizza**.
> * Tú eres el objeto base.
> * Te pones una camiseta (Decorador 1).
> * Te pones una chaqueta encima (Decorador 2).
> * Si hace calor, te quitas la chaqueta.
> * **Clave:** Si necesitas sumar comportamientos sin usar herencia masiva -> **Decorator**.

---

### 🔑 Definición
Patrón estructural que permite añadir funcionalidades a objetos colocando estos objetos dentro de objetos encapsuladores especiales que contienen estas funcionalidades.

---

### 💻 Ejemplo de Código (Caso: Notificaciones)

*Problema: Tenemos una app que notifica por Email. Ahora quieren SMS, Facebook y Slack. Un usuario puede querer combinar Email + SMS, o solo Slack, o los tres.*

```java
// 1. COMPONENT (Interfaz Común)
// Define qué hace el objeto básico y los decoradores.
interface Notificador {
    void enviar(String mensaje);
}

// 2. CONCRETE COMPONENT (El objeto base)
// La implementación básica (ej: solo email).
class NotificadorEmail implements Notificador {
    @Override
    public void enviar(String mensaje) {
        System.out.println("Enviando Email: " + mensaje);
    }
}

// 3. BASE DECORATOR (El envoltorio genérico)
// Tiene un campo para guardar el objeto que envuelve (wrappee).
abstract class DecoradorBase implements Notificador {
    private Notificador envuelto;

    public DecoradorBase(Notificador n) {
        this.envuelto = n;
    }

    @Override
    public void enviar(String mensaje) {
        // Delega la llamada al objeto original
        envuelto.enviar(mensaje);
    }
}

// 4. CONCRETE DECORATORS (Las capas extra)
// Añaden funcionalidad antes o después de llamar al envuelto.

class DecoradorSMS extends DecoradorBase {
    public DecoradorSMS(Notificador n) { super(n); }

    @Override
    public void enviar(String mensaje) {
        super.enviar(mensaje); // Llama al original
        System.out.println("Enviando SMS: " + mensaje); // Añade lo suyo
    }
}

class DecoradorFacebook extends DecoradorBase {
    public DecoradorFacebook(Notificador n) { super(n); }

    @Override
    public void enviar(String mensaje) {
        super.enviar(mensaje);
        System.out.println("Publicando en Facebook: " + mensaje);
    }
}

// 5. CLIENTE
class AppMensajeria {
    public static void main(String[] args) {
        // Caso 1: Usuario básico
        Notificador n1 = new NotificadorEmail();
        n1.enviar("Hola Mundo"); 
        // Salida: Email
        
        System.out.println("---");

        // Caso 2: Usuario Premium (Email + SMS + Facebook)
        // ¡Se envuelven unos dentro de otros!
        Notificador n2 = new DecoradorFacebook(
                            new DecoradorSMS(
                                new NotificadorEmail()
                            )
                         );
                         
        n2.enviar("Alerta Crítica");
        // Salida: 
        // 1. Enviando Email (el del centro)
        // 2. Enviando SMS (el del medio)
        // 3. Publicando en Facebook (el de fuera)
    }
}
```
---

## Fachada

**Etiquetas:** #patrones-diseño #estructural #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas pistas en el enunciado:

1.  **"Complejidad abrumadora":** El cliente tiene que interactuar con muchas clases distintas, con configuraciones complejas y en un orden específico para hacer una tarea simple.
2.  **"Punto de entrada único":** Se busca una clase que agrupe funcionalidades de un subsistema completo.
3.  **"Desacoplar librerías":** Quieres usar una librería compleja (ej: codificación de vídeo, compresión) pero no quieres que tu código se "ensucie" con las clases de esa librería por todas partes.

> [!TIP] Regla de Oro
> * Piensa en un **Mando a Distancia Universal** o en el mostrador de **Atención al Cliente**.
> * Tú (Cliente) no quieres ir al almacén, luego a facturación y luego a envíos.
> * Tú hablas con Atención al Cliente (Facade) y dices "Quiero hacer una devolución". Ellos se encargan del lío interno.
> * **Clave:** Interfaz simple para un subsistema complejo.

---

### 🔑 Definición
Patrón estructural que proporciona una interfaz simplificada a una biblioteca, un framework o un conjunto complejo de clases. La fachada no hace el trabajo real, solo dirige las peticiones a las clases adecuadas del subsistema.

---

### 💻 Ejemplo de Código (Caso: Cine en Casa)

*Problema: Para ver una peli tienes que bajar luces, encender TV, poner HDMI, encender altavoces, poner volumen... Si lo haces manual, es un caos.*

```java
// === EL SUBSISTEMA COMPLEJO (Lo que queremos ocultar) ===
class Luces {
    void on() { System.out.println("Luces ON"); }
    void atenuar(int nivel) { System.out.println("Luces al " + nivel + "%"); }
}

class Televisor {
    void on() { System.out.println("TV ON"); }
    void setEntradaHDMI() { System.out.println("Entrada: HDMI 1"); }
}

class EquipoSonido {
    void on() { System.out.println("Sonido ON"); }
    void setVolumen(int vol) { System.out.println("Volumen al " + vol); }
}

class NetflixService {
    void buscarPeli(String peli) { System.out.println("Buscando " + peli); }
    void reproducir() { System.out.println("Reproduciendo..."); }
}

// === FACADE (La Fachada) ===
// Ofrece un botón mágico para hacerlo todo de golpe.
class HomeTheaterFacade {
    // Referencias a los componentes del subsistema
    private Luces luces;
    private Televisor tv;
    private EquipoSonido sonido;
    private NetflixService netflix;

    public HomeTheaterFacade(Luces l, Televisor t, EquipoSonido s, NetflixService n) {
        this.luces = l;
        this.tv = t;
        this.sonido = s;
        this.netflix = n;
    }

    // Método Simplificado
    public void verPelicula(String titulo) {
        System.out.println("--- Preparando modo cine ---");
        luces.atenuar(30);
        tv.on();
        tv.setEntradaHDMI();
        sonido.on();
        sonido.setVolumen(50);
        netflix.buscarPeli(titulo);
        netflix.reproducir();
        System.out.println("--- ¡Disfruta! ---");
    }
}

// === CLIENTE ===
// El cliente NO necesita saber qué métodos llamar ni en qué orden.
class Usuario {
    public static void main(String[] args) {
        // Inicialización de componentes (o inyección de dependencias)
        Luces l = new Luces();
        Televisor t = new Televisor();
        EquipoSonido s = new EquipoSonido();
        NetflixService n = new NetflixService();

        // El cliente solo interactúa con la fachada
        HomeTheaterFacade cineEnCasa = new HomeTheaterFacade(l, t, s, n);
        
        cineEnCasa.verPelicula("Inception");
    }
}
```
---
---

# Patrón de Comportamiento


## Método Plantilla


**Etiquetas:** #patrones-diseño #comportamiento #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas pistas en el enunciado:

1.  **"Algoritmo con estructura fija":** Tienes varias clases que hacen casi lo mismo. El "esqueleto" del proceso es idéntico, pero cambian los detalles de algunos pasos internos.
    * *Ejemplo:* Procesar documentos (Abrir -> **Extraer** -> **Analizar** -> Cerrar). La extracción cambia si es PDF o Excel, pero abrir y cerrar es igual.
2.  **"Código duplicado en subclases":** El problema menciona que hay mucho "copia-pega" entre clases hermanas y solo cambian un par de líneas en medio de un método largo.
3.  **"Control desde el padre":** Se requiere que la clase base controle el *orden* de ejecución (el flujo), pero las hijas controlen la *implementación* concreta de cada paso.

> [!TIP] Regla de Oro
> * Piensa en una **Receta de Cocina** o un **Protocolo de Seguridad**.
> * El protocolo dice: 1. Identificarse. 2. **Realizar Acción**. 3. Registrar salida.
> * El paso 1 y 3 son iguales para todos.
> * El paso 2 cambia (un Bombero *apaga fuego*, un Médico *cura*).
> * La clase padre define el orden inmutable. Las hijas rellenan los huecos.

---

### 🔑 Definición
Patrón de comportamiento que define el **esqueleto de un algoritmo** en una operación de la superclase, delegando la implementación de algunos pasos a las subclases. Permite redefinir ciertos pasos de un algoritmo sin cambiar su estructura general.

---

### 💻 Ejemplo de Código (Caso: Procesar Archivos de Datos)

*Problema: Tenemos que procesar archivos CSV y PDF. El proceso de abrir y cerrar el archivo es igual, pero "extraer los datos" es muy diferente.*

```java
// 1. CLASE ABSTRACTA (Define el esqueleto)
abstract class ProcesadorDatos {
    
    // ESTE ES EL TEMPLATE METHOD
    // Es 'final' para que las subclases NO puedan romper el orden de los pasos.
    public final void procesarArchivo(String path) {
        abrirArchivo(path);
        extraerDatos();      // Paso abstracto (Las hijas lo definen)
        analizarDatos();     // Paso común
        cerrarArchivo();
    }

    // Pasos Concretos (Comunes para todos)
    void abrirArchivo(String path) {
        System.out.println("Abriendo archivo en ruta: " + path);
    }

    void analizarDatos() {
        System.out.println("Analizando los datos crudos...");
    }

    void cerrarArchivo() {
        System.out.println("Cerrando archivo y liberando memoria.");
    }

    // Pasos Abstractos (Los "huecos" a rellenar)
    abstract void extraerDatos();
}

// 2. CLASES CONCRETAS (Implementan los detalles específicos)

class ProcesadorCSV extends ProcesadorDatos {
    @Override
    void extraerDatos() {
        System.out.println("Leyendo líneas separadas por comas...");
    }
}

class ProcesadorPDF extends ProcesadorDatos {
    @Override
    void extraerDatos() {
        System.out.println("Escaneando binarios del PDF y convirtiendo a texto...");
    }
}

// 3. CLIENTE
class AppAnalisis {
    public static void main(String[] args) {
        System.out.println("--- REPORTE CSV ---");
        ProcesadorDatos csv = new ProcesadorCSV();
        csv.procesarArchivo("datos.csv"); // El padre controla el flujo

        System.out.println("\n--- REPORTE PDF ---");
        ProcesadorDatos pdf = new ProcesadorPDF();
        pdf.procesarArchivo("informe.pdf");
    }
}
```
---

## Estrategia

**Etiquetas:** #patrones-diseño #comportamiento #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas pistas infalibles:

1.  **"Múltiples formas de hacer lo mismo":** El enunciado describe una tarea (ej: ordenar una lista, calcular impuestos, rutear un mapa) que se puede hacer con varios algoritmos diferentes.
2.  **"Intercambiable en tiempo de ejecución":** El usuario debe poder cambiar el comportamiento "al vuelo" (ej: cambiar de "Ruta más rápida" a "Ruta más económica" sin reiniciar la app).
3.  **"Eliminar condicionales gigantes":** El código actual tiene un monstruo `if (tipo == 'A') ... else if (tipo == 'B') ...` para elegir qué algoritmo ejecutar.

> [!TIP] Regla de Oro
> * Piensa en un **Navegador GPS**.
> * El objetivo es el mismo: "Ir de A a B".
> * Las estrategias son: "En Coche", "A Pie", "En Transporte Público".
> * Tú (el Contexto) eliges la estrategia y el navegador calcula la ruta usándola.
> * Puedes cambiar de estrategia en mitad del viaje.

---

### 🔑 Definición
Patrón de comportamiento que permite definir una familia de algoritmos, encapsular cada uno de ellos en una clase separada y hacer que sus objetos sean **intercambiables**. Permite que el algoritmo varíe independientemente de los clientes que lo usan.

---

### 💻 Ejemplo de Código (Caso: Calculadora de Descuentos)

*Problema: Una tienda tiene diferentes promociones (Navidad, Black Friday, Socio VIP). Queremos aplicar el descuento correcto sin llenar el código de `if/else`.*

```java
// 1. STRATEGY (Interfaz Común)
// Define qué hacen todas las estrategias (calcular un precio final).
interface EstrategiaDescuento {
    double aplicarDescuento(double precioOriginal);
}

// 2. CONCRETE STRATEGIES (Los Algoritmos)

// Estrategia: Sin descuento (Precio normal)
class SinDescuento implements EstrategiaDescuento {
    @Override
    public double aplicarDescuento(double precio) {
        return precio;
    }
}

// Estrategia: Descuento de Navidad (10%)
class DescuentoNavidad implements EstrategiaDescuento {
    @Override
    public double aplicarDescuento(double precio) {
        return precio * 0.90;
    }
}

// Estrategia: Descuento VIP (20%)
class DescuentoVIP implements EstrategiaDescuento {
    @Override
    public double aplicarDescuento(double precio) {
        return precio * 0.80;
    }
}

// 3. CONTEXT (El que usa la estrategia)
// No sabe CÓMO se calcula, solo llama al método de la interfaz.
class CestaCompra {
    private EstrategiaDescuento estrategia;
    private double montoTotal;

    public CestaCompra(double monto) {
        this.montoTotal = monto;
        this.estrategia = new SinDescuento(); // Estrategia por defecto
    }

    // Permite cambiar la estrategia en tiempo de ejecución
    public void setEstrategia(EstrategiaDescuento nuevaEstrategia) {
        this.estrategia = nuevaEstrategia;
    }

    public void calcularTotal() {
        // Delega el cálculo al objeto estrategia
        double precioFinal = estrategia.aplicarDescuento(montoTotal);
        System.out.println("Total a pagar: " + precioFinal);
    }
}

// 4. CLIENTE
class TiendaApp {
    public static void main(String[] args) {
        CestaCompra cesta = new CestaCompra(100.0);
        
        System.out.println("--- Día Normal ---");
        cesta.calcularTotal(); // Usa SinDescuento (100.0)

        System.out.println("--- Es Navidad ---");
        cesta.setEstrategia(new DescuentoNavidad()); // Cambiamos comportamiento
        cesta.calcularTotal(); // Usa DescuentoNavidad (90.0)

        System.out.println("--- Cliente VIP entra ---");
        cesta.setEstrategia(new DescuentoVIP()); // Cambiamos comportamiento otra vez
        cesta.calcularTotal(); // Usa DescuentoVIP (80.0)
    }
}
```
---

## Estado

**Etiquetas:** #patrones-diseño #comportamiento #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas situaciones clave:

1.  **"Comportamiento dependiente del valor":** Una clase gigante tiene un campo que representa su estado (ej: `estado = 'reproduciendo'`, `estado = 2`). El comportamiento de todos sus métodos (`pulsarBoton()`) cambia radicalmente según ese valor.
2.  **"Máquina de Estados":** El problema describe un flujo de trabajo donde un objeto pasa de un estado a otro de manera predefinida (ej: Pedido -> Enviando -> Entregado).
3.  **"Eliminar switch/if anidados":** El código actual está lleno de grandes estructuras `switch` o `if/else if` que violan el principio **Open/Closed** (tienes que modificar el código cada vez que añades un nuevo estado).

> [!TIP] Regla de Oro
> * Piensa en un **Semáforo** (Luz Roja -> Luz Amarilla -> Luz Verde).
> * El objeto es el semáforo, pero su comportamiento (`cambiarLuz()`) es totalmente diferente dependiendo de si su estado interno es Rojo o Verde.
> * **Clave:** El objeto **parece cambiar de clase** cuando su estado interno cambia.

---

### 🔑 Definición
Patrón de comportamiento que permite a un objeto alterar su comportamiento cuando su **estado interno** cambia. El patrón extrae la lógica específica de cada estado en clases separadas, haciendo que el objeto (Contexto) se comporte como si hubiera cambiado de clase.

---

### 💻 Ejemplo de Código (Caso: Reproductor de Música)

*Problema: El reproductor reacciona de forma diferente al pulsar el botón PLAY/PAUSE si está Encendido, Reproduciendo o Pausado.*

```java
// 1. STATE (Interfaz Común)
// Define las acciones que se pueden realizar en cualquier estado.
interface EstadoReproductor {
    void pulsarReproducir(ReproductorMusica reproductor);
    void pulsarParar(ReproductorMusica reproductor);
}

// 2. CONTEXT (El Objeto Principal)
class ReproductorMusica {
    private EstadoReproductor estado;

    // Inicialización con estado por defecto
    public ReproductorMusica() {
        this.estado = new EstadoDetenido();
    }

    // Permite que los estados cambien el contexto
    public void cambiarEstado(EstadoReproductor nuevoEstado) {
        System.out.println("Cambiando de estado a: " + nuevoEstado.getClass().getSimpleName());
        this.estado = nuevoEstado;
    }

    // El reproductor solo delega la acción al objeto estado actual
    public void clickReproducir() {
        estado.pulsarReproducir(this);
    }
    
    public void clickParar() {
        estado.pulsarParar(this);
    }
}

// 3. CONCRETE STATES (Lógica de cada estado)

class EstadoDetenido implements EstadoReproductor {
    @Override
    public void pulsarReproducir(ReproductorMusica reproductor) {
        System.out.println("-> Iniciando reproducción...");
        reproductor.cambiarEstado(new EstadoReproduciendo());
    }
    @Override
    public void pulsarParar(ReproductorMusica reproductor) {
        System.out.println("-> El reproductor ya está detenido.");
    }
}

class EstadoReproduciendo implements EstadoReproductor {
    @Override
    public void pulsarReproducir(ReproductorMusica reproductor) {
        System.out.println("-> Pausando reproducción.");
        reproductor.cambiarEstado(new EstadoPausado());
    }
    @Override
    public void pulsarParar(ReproductorMusica reproductor) {
        System.out.println("-> Deteniendo por completo.");
        reproductor.cambiarEstado(new EstadoDetenido());
    }
}

class EstadoPausado implements EstadoReproductor {
    @Override
    public void pulsarReproducir(ReproductorMusica reproductor) {
        System.out.println("-> Reanudando reproducción.");
        reproductor.cambiarEstado(new EstadoReproduciendo());
    }
    @Override
    public void pulsarParar(ReproductorMusica reproductor) {
        System.out.println("-> Deteniendo por completo.");
        reproductor.cambiarEstado(new EstadoDetenido());
    }
}

// 4. CLIENTE
class AppReproductor {
    public static void main(String[] args) {
        ReproductorMusica player = new ReproductorMusica(); // Inicia en Detenido

        player.clickReproducir(); // Detenido -> Reproduciendo
        player.clickReproducir(); // Reproduciendo -> Pausado
        player.clickReproducir(); // Pausado -> Reproduciendo
    }
}
```
---

## Command 

**Etiquetas:** #patrones-diseño #comportamiento #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas pistas claras:

1.  **"Deshacer/Rehacer" (Undo/Redo):** Es la señal número 1. Si el sistema necesita revertir operaciones, necesitas guardar la operación como un objeto.
2.  **"Parametrizar objetos con acciones":** Tienes botones en una interfaz gráfica (GUI) y quieres asignarles acciones distintas en tiempo de ejecución (ej: cambiar qué hace el botón "F1").
3.  **"Colas de tareas" o "Programación diferida":** Necesitas poner tareas en una lista para ejecutarlas más tarde, en otro hilo, o por la noche.

> [!TIP] Regla de Oro
> * Piensa en un **Restaurante**.
> * Tú (Cliente) le das la orden al **Camarero** (Invoker).
> * El Camarero no sabe cocinar, solo apunta la orden en una **Comanda** (Command).
> * La Comanda llega a la cocina y el **Cocinero** (Receiver) es quien realmente cocina.
> * El Camarero (Botón) está desacoplado del Cocinero (Lógica de Negocio).

---

### 🔑 Definición
Patrón de comportamiento que convierte una solicitud en un objeto independiente que contiene toda la información sobre la solicitud. Esta transformación permite parametrizar métodos con diferentes solicitudes, retrasar o poner en cola la ejecución de una solicitud y soportar operaciones que no se pueden realizar.

---

### 💻 Ejemplo de Código (Caso: Control Remoto Inteligente)

*Problema: Tenemos un mando a distancia. Queremos que los botones sirvan para encender luces, poner música o abrir el garaje, sin cambiar el código del mando cada vez.*

```java
// 1. COMMAND (Interfaz Común)
// Todos los comandos deben tener un método ejecutar.
// A menudo se añade 'deshacer' (undo).
interface Comando {
    void ejecutar();
    void deshacer();
}

// 2. RECEIVER (La Lógica de Negocio Real)
// Estos son los objetos que realmente hacen el trabajo sucio.
class Luz {
    void encender() { System.out.println("Luz encendida"); }
    void apagar() { System.out.println("Luz apagada"); }
}

class Estereo {
    void on() { System.out.println("Estéreo ON"); }
    void off() { System.out.println("Estéreo OFF"); }
}

// 3. CONCRETE COMMANDS (Los envoltorios)
// Vinculan una acción específica con un receptor específico.

class ComandoEncenderLuz implements Comando {
    private Luz luz;

    public ComandoEncenderLuz(Luz luz) {
        this.luz = luz;
    }

    @Override
    public void ejecutar() { luz.encender(); }

    @Override
    public void deshacer() { luz.apagar(); } // Lo opuesto
}

class ComandoEncenderEstereo implements Comando {
    private Estereo estereo;

    public ComandoEncenderEstereo(Estereo estereo) {
        this.estereo = estereo;
    }

    @Override
    public void ejecutar() { estereo.on(); }

    @Override
    public void deshacer() { estereo.off(); }
}

// 4. INVOKER (El disparador / Botón)
// No sabe QUÉ hace, solo sabe que tiene que llamar a ejecutar().
class ControlRemoto {
    private Comando comandoSlot; // El botón programable

    public void setComando(Comando comando) {
        this.comandoSlot = comando;
    }

    public void pulsarBoton() {
        if (comandoSlot != null) {
            comandoSlot.ejecutar();
        }
    }

    public void pulsarDeshacer() {
        if (comandoSlot != null) {
            comandoSlot.deshacer();
        }
    }
}

// 5. CLIENTE
class AppDomotica {
    public static void main(String[] args) {
        // Crear los receptores
        Luz luzSalon = new Luz();
        Estereo radio = new Estereo();

        // Crear los comandos
        Comando encenderLuz = new ComandoEncenderLuz(luzSalon);
        Comando prenderRadio = new ComandoEncenderEstereo(radio);

        // Configurar el invoker (El mando)
        ControlRemoto mando = new ControlRemoto();

        // CASO 1: Usar el mando para la luz
        mando.setComando(encenderLuz);
        mando.pulsarBoton();    // "Luz encendida"
        mando.pulsarDeshacer(); // "Luz apagada"

        System.out.println("--- Cambiando configuración del botón ---");

        // CASO 2: Reconfigurar el MISMO botón para la radio
        mando.setComando(prenderRadio);
        mando.pulsarBoton();    // "Estéreo ON"
    }
}
```
---

## Iterator 

**Etiquetas:** #patrones-diseño #comportamiento #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas pistas en el enunciado:

1.  **"Recorrer estructuras complejas":** Tienes una colección que no es una simple lista (ej: un Árbol, un Grafo, una lista enlazada compleja) y quieres recorrer sus elementos uno a uno.
2.  **"Ocultar la estructura interna":** El cliente quiere obtener los elementos (`next()`) sin saber si por dentro hay un `Array`, un `ArrayList` o una `HashTable`.
3.  **"Múltiples formas de recorrer":** Necesitas recorrer la misma colección de formas distintas (ej: "Recorrido en anchura" vs "Recorrido en profundidad", o "Filtrando inactivos").

> [!TIP] Regla de Oro
> * Piensa en un **Mando a Distancia de TV**.
> * Tú pulsas "Canal Siguiente" (Next).
> * No te importa cómo la TV guarda los canales, ni la frecuencia electrónica, ni si están ordenados por nombre.
> * Solo quieres el **siguiente** elemento.
> * Si ves un bucle `for` o `while` que accede a una colección compleja -> **Iterator**.

---

### 🔑 Definición
Patrón de comportamiento que permite recorrer los elementos de una colección sin exponer su representación subyacente (lista, pila, árbol, etc.). Extrae la lógica de recorrido en un objeto separado llamado "Iterador".

---

### 💻 Ejemplo de Código (Caso: Playlist de Canciones)

*Problema: Tenemos una lista de canciones. Queremos recorrerla, pero no queremos que el cliente toque el array interno directamente.*

```java
// 1. INTERFAZ ITERATOR (El mando a distancia)
// Define cómo nos movemos por la colección.
interface Iterator {
    boolean tieneSiguiente();
    Object siguiente();
}

// 2. INTERFAZ COLLECTION (El contenedor)
// Define que este objeto puede crear iteradores.
interface ColeccionCanciones {
    Iterator crearIterador();
}

// 3. CONCRETE COLLECTION (La Playlist real)
class Playlist implements ColeccionCanciones {
    private String[] canciones;
    private int contador = 0;

    public Playlist(int capacidad) {
        this.canciones = new String[capacidad];
    }

    public void agregarCancion(String cancion) {
        if (contador < canciones.length) {
            canciones[contador++] = cancion;
        }
    }

    // Aquí está la clave: La colección crea su propio iterador
    @Override
    public Iterator crearIterador() {
        return new PlaylistIterator(this.canciones);
    }
}

// 4. CONCRETE ITERATOR (El cursor)
// Mantiene la posición actual. Sabe cómo moverse.
class PlaylistIterator implements Iterator {
    private String[] canciones;
    private int posicion = 0;

    public PlaylistIterator(String[] canciones) {
        this.canciones = canciones;
    }

    @Override
    public boolean tieneSiguiente() {
        // Verifica si hay algo en la siguiente posición y no es null
        return posicion < canciones.length && canciones[posicion] != null;
    }

    @Override
    public Object siguiente() {
        String cancion = canciones[posicion];
        posicion++;
        return cancion;
    }
}

// 5. CLIENTE
class AppMusica {
    public static void main(String[] args) {
        Playlist miPlaylist = new Playlist(5);
        miPlaylist.agregarCancion("Bohemian Rhapsody");
        miPlaylist.agregarCancion("Stairway to Heaven");
        miPlaylist.agregarCancion("Hotel California");

        // El cliente NO usa un bucle for(int i=0...) accediendo al array.
        // Pide el iterador y lo usa.
        Iterator it = miPlaylist.crearIterador();

        while (it.tieneSiguiente()) {
            String cancion = (String) it.siguiente();
            System.out.println("Reproduciendo: " + cancion);
        }
    }
}
```
---

## Observer

**Etiquetas:** #patrones-diseño #comportamiento #examen

---

### 🎯 ¿Cómo identificarlo en el Examen?

Busca estas pistas infalibles:

1.  **"Uno a muchos" (One-to-Many):** El enunciado dice explícitamente que cuando **un** objeto cambia de estado, **muchos** otros deben enterarse y actualizarse automáticamente.
2.  **"Suscripción / Notificación":** Se habla de un mecanismo donde objetos pueden "apuntarse" o "darse de baja" para recibir alertas.
3.  **"Evitar Polling (Sondeo)":** Quieres evitar que un objeto pregunte cada 5 milisegundos "¿Ya has terminado? ¿Ya has terminado?". Prefieres que el otro objeto avise: "¡Ya he terminado!".

> [!TIP] Regla de Oro
> * Piensa en **YouTube** o un **Boletín de Noticias (Newsletter)**.
> * El **Canal** (Subject) publica un video nuevo.
> * No sabe quiénes son sus suscriptores (Juan, María, Bot3000...), solo tiene una lista de emails.
> * Al publicar, el Canal recorre la lista y dice "¡Video Nuevo!" a todos.
> * Los **Suscriptores** (Observers) reciben el aviso y reaccionan (lo ven, lo ignoran, etc.).

---

### 🔑 Definición
Patrón de comportamiento que define un mecanismo de suscripción para notificar a múltiples objetos (observadores) sobre cualquier evento que le suceda al objeto que están observando (sujeto).

---

### 💻 Ejemplo de Código (Caso: Canal de YouTube)

*Problema: Tenemos un Canal. Queremos que cuando suba un video, todos los usuarios suscritos reciban una alerta, sin que el Canal tenga que conocer el código específico de cada usuario.*

```java
import java.util.ArrayList;
import java.util.List;

// 1. OBSERVER (La Interfaz del Suscriptor)
// Todos los que quieran recibir alertas deben implementar esto.
interface Suscriptor {
    void actualizar(String nombreVideo);
}

// 2. SUBJECT (El Publicador / El Canal)
// Mantiene la lista de suscriptores y los notifica.
class CanalYouTube {
    private String nombreCanal;
    private List<Suscriptor> suscriptores = new ArrayList<>();

    public CanalYouTube(String nombre) {
        this.nombreCanal = nombre;
    }

    // Método para suscribirse
    public void suscribir(Suscriptor s) {
        suscriptores.add(s);
    }

    // Método para desuscribirse
    public void desuscribir(Suscriptor s) {
        suscriptores.remove(s);
    }

    // La magia: Notificar a todos
    public void subirVideo(String tituloVideo) {
        System.out.println("\n[" + nombreCanal + "] Subiendo video: " + tituloVideo);
        notificarSuscriptores(tituloVideo);
    }

    private void notificarSuscriptores(String titulo) {
        for (Suscriptor s : suscriptores) {
            s.actualizar(titulo); // Llama al método del observador
        }
    }
}

// 3. CONCRETE OBSERVERS (Los Usuarios)
class Usuario implements Suscriptor {
    private String nombre;

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void actualizar(String tituloVideo) {
        System.out.println(nombre + " recibió alerta: ¡Nuevo video '" + tituloVideo + "'!");
    }
}

// 4. CLIENTE
class AppYouTube {
    public static void main(String[] args) {
        CanalYouTube canalMisterios = new CanalYouTube("Misterios TV");

        Usuario user1 = new Usuario("Pepito");
        Usuario user2 = new Usuario("Maria");
        Usuario user3 = new Usuario("Hater_123");

        // Suscripción
        canalMisterios.suscribir(user1);
        canalMisterios.suscribir(user2);
        canalMisterios.suscribir(user3);

        // Evento 1
        canalMisterios.subirVideo("La verdad sobre los Aliens");
        
        // Un usuario se da de baja
        canalMisterios.desuscribir(user3); // El hater se va

        // Evento 2
        canalMisterios.subirVideo("Fantasmas en mi casa");
        // Solo Pepito y Maria reciben la segunda alerta
    }
}
```