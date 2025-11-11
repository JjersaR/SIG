# Sistema de Información Gerencial

## Descripción
Sistema de información gerencial diseñado para recopilar, procesar y presentar datos comerciales (ventas, productos más vendidos, desempeño por sucursal, etc.) que permite a los dueños y gerentes tomar decisiones informadas basadas en análisis de datos en tiempo real.

## Tecnologías Utilizadas
- Java 21
- Spring Boot 3.x
- Spring Data, JPA
- Base de datos: MySQL
- Maven

## Endpoints

### Dashboard Principal

#### Obtener Dashboard Principal
- **Método:** `GET`
- **URL:** `/dashboard/principal`
- **Descripción:** Retorna las métricas principales del negocio para el día y mes actual

**Ejemplo de Response:**
```json
{
  "ventasHoy": 25480.50,
  "ventasMesActual": 452360.75,
  "crecimientoVsMesAnterior": 15.2,
  "productosVendidosHoy": 189,
  "sucursalTop": "Sucursal Centro",
  "productoTop": "Hamburguesa Especial"
}
```

### Dashboard por Sucursal

#### Obtener Dashboard de Sucursal Específica
- **Método:** `GET`
- **URL:** `/dashboard/sucursales/{sucursalId}`
- **Descripción:** Retorna las métricas detalladas de una sucursal específica

**Ejemplo de Response:**
```json
{
  "sucursal": "Sucursal Norte",
  "ventasHoy": 12540.25,
  "ticketPromedio": 245.80,
  "productosMasVendidos": [
    {"nombre": "Hamburguesa Clásica", "cantidad": 45},
    {"nombre": "Papas Grandes", "cantidad": 38}
  ],
  "horarioPico": "14:00-16:00"
}
```

### Análisis de Ventas

#### Obtener Análisis de Ventas por Período
- **Método:** `GET`
- **URL:** `/analytics/ventas?fechaInicio=2024-01-01&fechaFin=2024-01-31&agrupacion=DIARIA`
- **Parámetros:**
  - `fechaInicio` (requerido): Fecha de inicio del análisis
  - `fechaFin` (requerido): Fecha de fin del análisis
  - `agrupacion` (opcional): DIARIA, SEMANAL, MENSUAL
- **Descripción:** Retorna el análisis de ventas agrupado por el período especificado

**Ejemplo de Response:**
```json
[
  {
    "fecha": "2024-01-15",
    "totalVentas": 28450.75,
    "cantidadVentas": 125,
    "ticketPromedio": 227.61
  }
]
```

### Análisis de Productos

#### Obtener Análisis de Productos
- **Método:** `GET`
- **URL:** `/analisis/productos`
- **Descripción:** Retorna el análisis de rendimiento de todos los productos

**Ejemplo de Response:**
```json
[
  {
    "producto": "Hamburguesa Doble Queso",
    "cantidadVendida": 456,
    "totalVendido": 91200.00,
    "margenPromedio": 65.2
  }
]
```

#### Obtener Análisis de Márgenes de Productos
- **Método:** `GET`
- **URL:** `/analisis/productos/margenes`
- **Descripción:** Retorna el análisis detallado de márgenes por producto

**Ejemplo de Response:**
```json
[
  {
    "producto": "Hamburguesa Especial",
    "costoPromedio": 45.50,
    "precioVentaPromedio": 120.00,
    "margen": 74.50,
    "margenPorcentaje": 62.1
  }
]
```

### Análisis de Horarios

#### Obtener Análisis por Horarios
- **Método:** `GET`
- **URL:** `/analisis/horarios`
- **Descripción:** Retorna el análisis de ventas por horarios del día

**Ejemplo de Response:**
```json
[
  {
    "hora": "14:00",
    "ventasTotales": 12540.25,
    "cantidadVentas": 58,
    "productosMasVendidos": ["Combo Mediodía"]
  }
]
```

### Análisis por Días de la Semana

#### Obtener Análisis por Días de la Semana
- **Método:** `GET`
- **URL:** `/analisis/dias-semana`
- **Descripción:** Retorna el análisis comparativo por días de la semana

**Ejemplo de Response:**
```json
[
  {
    "dia": "VIERNES",
    "ventasPromedio": 28950.75,
    "cantidadVentas": 450,
    "totalVentas": 46879.50
  }
]
```

### Análisis Comparativo

#### Obtener Comparativa entre Sucursales
- **Método:** `GET`
- **URL:** `/analisis/comparativa`
- **Descripción:** Retorna análisis comparativo entre todas las sucursales

**Ejemplo de Response:**
```json
[
  {
    "sucursal": "Sucursal Centro",
    "ventasTotales": 458920.25,
    "ventasPromedioDiaria": 15297.34,
    "productosVendidos": 3821,
    "ticketPromedio": 240.15
  }
]
```

### Análisis de Tendencia

#### Obtener Tendencia de Sucursal
- **Método:** `GET`
- **URL:** `/analisis/{sucursalId}/tendencia`
- **Descripción:** Retorna el análisis de tendencia mensual para una sucursal específica

**Ejemplo de Response:**
```json
[
  {
    "mes": "2024-01",
    "ventas": 458920.25,
    "crecimiento": 12.5,
    "productosVendidos": 3821
  }
]
```

## Configuración y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6+
- Base de datos PostgreSQL o MySQL

### Instalación
1. Clonar el repositorio
2. Configurar la base de datos en `application.properties`
3. Ejecutar `mvn spring-boot:run`
4. La aplicación estará disponible en `http://localhost:8080`

### Variables de Entorno
```properties
spring.datasource.url=jdbc:mysql://localhost:5432/sistema_gerencial
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```
