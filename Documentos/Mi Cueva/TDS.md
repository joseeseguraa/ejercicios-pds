# Patrones de Creación

## Índex

- [[#Factoría Abstracta|Factoría Abstracta]]
	- [[#Factoría Abstracta#🎯 ¿Cómo identificarlo en el Examen?|Identificación]]
	- [[#Factoría Abstracta#🔑 Definición Genérica|Definición]]
	- [[#Factoría Abstracta#🏗️ Estructura Genérica (UML Mental)|Estructura Genérica]]
	- [[#Factoría Abstracta#💻 Ejemplo de Código (Caso: Mueblería)|Ejemplo de Código]]
- [[#Builder|Builder]]
	- [[#Builder#🎯 ¿Cómo identificarlo en el Examen?|Identificación]]
	- [[#Builder#🔑 Definición|Definición]]
	- [[#Builder#💻 Ejemplo de Código (Caso: Construir una Casa)|Ejemplo de Código]]
- [[#Método Factoría|Método Factoría]]
	- [[#Método Factoría#🎯 ¿Cómo identificarlo en el Examen?|Identificación]]
	- [[#Método Factoría#🔑 Definición|Definición]]
	- [[#Método Factoría#💻 Ejemplo de Código (Caso: Logística)|Ejemplo de Código]]
- [[#Singleton|Singleton]]
	- [[#Singleton#🎯 ¿Cómo identificarlo en el Examen?|Identificación]]
	- [[#Singleton#🔑 Definición|Definición]]
	- [[#Singleton#💻 Ejemplo de Código (Caso: Conexión Base de Datos)|Ejemplo de Código]]

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



