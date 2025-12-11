# 🌱 Huerto Hogar - Frontend Móvil (Android)

Aplicación móvil Android para marketplace de productos orgánicos y frescos, desarrollada en **Kotlin** con **Jetpack Compose**.

---

## 📋 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Funcionalidades Principales](#-funcionalidades-principales)
- [API Endpoints](#-api-endpoints)
- [Testing](#-testing)
- [Documentación Adicional](#-documentación-adicional)

---

## 🎯 Descripción General

**Huerto Hogar** es una aplicación de comercio electrónico móvil que conecta a productores locales con consumidores, ofreciendo productos frescos, orgánicos y de temporada. La app incluye funcionalidades completas de e-commerce con gestión de usuarios, carrito de compras, favoritos, y un panel administrativo integrado.

### Roles de Usuario

- **👤 Cliente**: Navegación de catálogo, carrito, favoritos, historial de compras
- **👨‍💼 Admin**: Panel completo de gestión (productos, usuarios, órdenes)

---

## ✨ Características

### Para Clientes

- 🛍️ **Catálogo de Productos**: Navegación por categorías (Frutas, Verduras, Productos Orgánicos, Lácteos)
- 🔍 **Búsqueda**: Filtrado de productos por nombre
- ❤️ **Favoritos**: Sistema de favoritos persistente con backend
- 🛒 **Carrito de Compras**: Gestión de carrito con validación de productos activos
- 💳 **Checkout**: Proceso de compra
- 👤 **Perfil**: Gestión de información personal y foto de perfil (Cloudinary)
- 📝 **Blog**: Sección de artículos informativos

### Para Administradores

- 📊 **Dashboard**: Estadísticas de ventas y métricas clave
- 📦 **Gestión de Productos**: CRUD completo con carga de imágenes
- 👥 **Gestión de Usuarios**: CRUD de usuarios con roles
- 🧾 **Gestión de Órdenes**: Visualización y actualización de estados
- ♻️ **Reactivación**: Recuperación de productos/usuarios desactivados
- 🔄 **Vista de Tienda**: Los admins pueden alternar entre panel admin y vista de cliente

---

## 🛠️ Tecnologías

### Lenguaje y Framework

- **Kotlin** 1.9.0
- **Jetpack Compose** (UI moderna y declarativa)
- **Compose Navigation** (navegación entre pantallas)

### Arquitectura

- **MVVM** (Model-View-ViewModel)
- **Repository Pattern** (capa de abstracción de datos)
- **StateFlow / Flow** (manejo reactivo de estados)
- **Coroutines** (operaciones asíncronas)

### Networking

- **Retrofit** 2.9.0 (cliente HTTP)
- **OkHttp** 4.11.0 (logging de requests)
- **Gson** 2.10.1 (serialización JSON)

### UI/UX

- **Material Design 3** (Material You)
- **Coil** 2.5.0 (carga de imágenes con AsyncImage)
- **Accompanist SystemUIController** (gestión de barras de sistema)

### Características Especiales

- **Camera/Gallery** (captura y selección de imágenes)
- **Custom Animations** (transiciones y efectos visuales)

### Testing

- **JUnit 4** 4.13.2
- **MockK** 1.13.8 (mocking para Kotlin)
- **Espresso** (UI testing)

---

## 🏗️ Arquitectura

### Patrón MVVM

```
┌─────────────────────────────────────────────────────────┐
│                     UI Layer                            │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Screens (Composables)                          │    │
│  │  - HomeScreen, CartScreen, FavScreen, etc.      │    │
│  └────────────────┬────────────────────────────────┘    │
│                   │ observes State                      │
│                   ▼                                     │
│  ┌─────────────────────────────────────────────────┐    │
│  │  ViewModels                                     │    │
│  │  - CartViewModel, FavoritesViewModel, etc.      │    │
│  │  (StateFlow, business logic)                    │    │
│  └────────────────┬────────────────────────────────┘    │
└───────────────────┼─────────────────────────────────────┘
                    │ calls Repository
                    ▼
┌─────────────────────────────────────────────────────────┐
│                  Data Layer                             │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Repositories                                   │    │
│  │  - ProductRepository, OrderRepository, etc.     │    │
│  │  (Flow<Resource<T>>, error handling)            │    │
│  └────────────────┬────────────────────────────────┘    │
│                   │ uses ApiService                     │
│                   ▼                                     │
│  ┌─────────────────────────────────────────────────┐    │
│  │  ApiServices (Retrofit)                         │    │
│  │  - ProductApiService, OrderApiService, etc.     │    │
│  └────────────────┬────────────────────────────────┘    │
└───────────────────┼─────────────────────────────────────┘
                    │ HTTP Requests
                    ▼
            ┌───────────────┐
            │    Backend    │
            │  Spring Boot  │
            └───────────────┘
```

### Flujo de Datos

1. **UI** (Screen) observa `StateFlow` del **ViewModel**
2. **ViewModel** llama métodos del **Repository**
3. **Repository** emite estados `Resource<T>` (Loading, Success, Error)
4. **Repository** usa **ApiService** (Retrofit) para llamadas HTTP
5. **ApiService** se comunica con el **Backend**
6. Respuesta fluye de vuelta: Backend → ApiService → Repository → ViewModel → UI

---

## 📋 Requisitos

### Software

- **Android Studio** Hedgehog | 2023.1.1 o superior
- **JDK** 17 o superior
- **Gradle** 8.2
- **Android SDK** mínimo 24 (Android 7.0) - target 34 (Android 14)

### Hardware

- Dispositivo Android físico o emulador
- Conexión a internet (para comunicación con backend)
- Cámara (opcional, para fotos de perfil/productos)

### Backend

- Servidor backend corriendo en `http://10.0.2.2:8080` (emulador) o IP configurada
- Ver: [Huerto_Hogar_Backend_Movil](https://github.com/ZalkiRyon/Huerto_Hogar_Backend_Movil)

---

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/ZalkiRyon/Huerto_Hogar_Frontend_Movil.git
cd Huerto_Hogar_Frontend_Movil/Huerto_Hogar_Frontend_Movil
```

### 2. Abrir en Android Studio

1. Abre Android Studio
2. `File > Open` → Selecciona la carpeta `Huerto_Hogar_Frontend_Movil`
3. Espera a que Gradle sincronice las dependencias

### 3. Configurar Backend URL

Edita `NetworkModule.kt`:

```kotlin
// Para emulador Android
private const val BASE_URL = "http://10.0.2.2:8080/"

// Para dispositivo físico (usa tu IP local)
// private const val BASE_URL = "http://192.168.1.X:8080/"
```

### 4. Compilar y Ejecutar

```bash
# Desde Android Studio
Run > Run 'app' (Shift + F10)

# O desde terminal
./gradlew installDebug
```

---

## ⚙️ Configuración

### `build.gradle.kts` (app)

```kotlin
android {
    namespace = "com.example.huerto_hogar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.huerto_hogar"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}
```

### Variables de Entorno

No se requieren archivos `.env` adicionales. Toda la configuración está en:

- `NetworkModule.kt` → URL del backend
- `build.gradle.kts` → Configuración de compilación

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/example/huerto_hogar/
│
├── AppScreens/
│   └── AppScreens.kt                    # Definición de rutas de navegación
│
├── data/
│   ├── api/                             # Interfaces Retrofit
│   │   ├── AuthApiService.kt
│   │   ├── BlogApiService.kt
│   │   ├── CategoryApiService.kt
│   │   ├── FavoriteApiService.kt
│   │   ├── OrderApiService.kt
│   │   ├── ProductApiService.kt
│   │   └── UserApiService.kt
│   │
│   └── di/
│       └── NetworkModule.kt             # Configuración Retrofit/OkHttp
│
├── model/                               # Modelos de datos
│   ├── Blog.kt
│   ├── CartItem.kt
│   ├── Favorite.kt
│   ├── Order.kt
│   ├── Product.kt
│   └── User.kt
│
├── repository/                          # Capa de acceso a datos
│   ├── BlogRepository.kt
│   ├── CategoryRepository.kt
│   ├── FavoriteRepository.kt
│   ├── OrderRepository.kt
│   ├── ProductRepository.kt
│   └── UserRepository.kt
│
├── screen/                              # Pantallas UI (Composables)
│   ├── HomeScreen.kt
│   ├── LoginScreen.kt
│   ├── CartScreen.kt
│   ├── FavScreen.kt
│   ├── CatalogoScreen.kt
│   ├── UsSetScreen.kt
│   ├── BlogScreen.kt
│   │
│   └── admin/                           # Pantallas del panel admin
│       ├── AdminDashboardScreen.kt
│       ├── AdminInventoryScreen.kt
│       ├── AdminUsersScreen.kt
│       ├── AdminOrdersScreen.kt
│       ├── CreateProductScreen.kt
│       ├── EditProductScreen.kt
│       ├── CreateUserScreen.kt
│       ├── EditUserScreen.kt
│       ├── ReactivateProductsScreen.kt
│       └── ReactivateUsersScreen.kt
│
├── viewmodel/                           # ViewModels (lógica de negocio)
│   ├── CartViewModel.kt
│   ├── FavoritesViewModel.kt
│   ├── LoginViewModel.kt
│   ├── ProductViewModel.kt
│   ├── UserViewModel.kt
│   └── SalesViewModel.kt
│
├── ui/theme/
│   ├── components/                      # Componentes reutilizables
│   │   ├── AppNavigationContainer.kt
│   │   ├── Header.kt
│   │   ├── BottomNavBar.kt
│   │   ├── animations/                  # Animaciones custom
│   │   ├── admin/                       # Componentes admin
│   │   └── dialogs/                     # Diálogos (Receipt, etc.)
│   │
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
│
├── utils/                               # Utilidades
│   ├── FileUtils.kt                     # Conversión de imágenes
│   ├── NetworkUtils.kt                  # Manejo de errores de red
│   ├── Resource.kt                      # Wrapper de estados
│   └── ValidationUtils.kt               # Validación de formularios
│
└── MainActivity.kt                      # Actividad principal
```

---

## 🎨 Funcionalidades Principales

### 1. Sistema de Autenticación

**Login/Registro:**
- Email y contraseña
- Validación de campos
- Manejo de roles (admin/cliente)
- Sesión persistente con `UserViewModel`

**Archivos:**
- `LoginScreen.kt` → UI de login
- `RegistroScreen.kt` → UI de registro
- `LoginViewModel.kt` → Lógica de autenticación
- `AuthApiService.kt` → Endpoints de auth

---

### 2. Catálogo de Productos

**Navegación por Categorías:**
```kotlin
// Categorías disponibles
- Frutas frescas
- Verduras orgánicas
- Productos orgánicos
- Productos lácteos
```

**Pantallas:**
- `HomeScreen.kt` → Listado de productos destacados
- `CatalogoScreen.kt` → Navegación por categorías
- `FrutasScreen.kt`, `VerdurasScreen.kt`, etc. → Filtrado por categoría

**Flujo:**
```kotlin
ProductRepository.getActiveProducts() 
    → ProductApiService.getActiveProducts()
    → Backend: GET /api/productos/activos
```

---

### 3. Carrito de Compras

**Funcionalidades:**
- ➕ Agregar productos desde catálogo o favoritos
- 📊 Incrementar/decrementar cantidades
- 🗑️ Eliminar productos individuales o vaciar carrito
- 💰 Cálculo automático de subtotal, descuento y total
- 🚚 Costo de envío fijo: $3,000

**Validación Automática:**
```kotlin
// Cada vez que se abre CartScreen
LaunchedEffect(Unit) {
    cartViewModel.validateAndRefreshCart()
}

// Verifica:
// 1. Productos aún están activos
// 2. Precios actualizados
// 3. Elimina productos desactivados
```

**Archivos:**
- `CartScreen.kt` → UI del carrito
- `CartViewModel.kt` → Lógica del carrito (local + validación backend)
- `OrderRepository.kt` → Creación de órdenes

---

### 4. Sistema de Favoritos

**Características:**
- ❤️ Agregar/remover productos de favoritos
- 🔄 Persistencia en backend (tabla favoritos)
- ✅ Validación automática de productos activos
- 🛒 Agregar al carrito desde favoritos
- 📱 Sincronización en tiempo real

**Flujo de Validación:**
```kotlin
// FavoritesViewModel.kt
fun reloadFavorites() {
    // 1. Consulta backend: GET /api/favoritos/usuario/{id}
    // 2. Backend hace JOIN con productos
    // 3. Filtra solo productos activos
    // 4. Retorna datos actualizados (precio, nombre, stock)
}

// Se ejecuta en:
LaunchedEffect(Unit) {
    favoritesViewModel.reloadFavorites()
}
```

**Archivos:**
- `FavScreen.kt` → UI de favoritos
- `FavoritesViewModel.kt` → Lógica de favoritos
- `FavoriteRepository.kt` → Comunicación con backend

---

### 5. Panel de Administración

**Dashboard:**
- 📊 Métricas de ventas
- 📈 Estadísticas de productos y usuarios
- 🔄 Acceso rápido a gestión

**Gestión de Productos:**
- ➕ Crear productos
- ✏️ Editar productos (nombre, precio, stock, descripción)
- 📸 Actualizar imagen (galería o cámara)
- 🔴 Desactivar productos (soft delete)
- ♻️ Reactivar productos desactivados

**Gestión de Usuarios:**
- ➕ Crear usuarios con rol específico
- ✏️ Editar información de usuarios
- 📸 Actualizar foto de perfil
- 🔴 Desactivar usuarios (soft delete)
- ♻️ Reactivar usuarios

**Gestión de Órdenes:**
- 📋 Lista completa de órdenes
- 🔍 Detalle de cada orden
- 🔴 Eliminar órdenes

**Navegación Admin:**
```kotlin
AdminNavigationContainer
    ├── AdminDashboardScreen        (inicio)
    ├── AdminInventoryScreen        (productos)
    ├── AdminUsersScreen            (usuarios)
    ├── AdminOrdersScreen           (órdenes)
    ├── ReactivateProductsScreen    (productos inactivos)
    └── ReactivateUsersScreen       (usuarios inactivos)
```

---

### 6. Gestión de Imágenes

**Carga de Imágenes:**
- 📷 Captura con cámara
- 🖼️ Selección desde galería
- ☁️ Subida a Cloudinary (vía backend)
- 🔗 Almacenamiento de URL en BD

**Flujo:**
```kotlin
// 1. Frontend: Seleccionar imagen
val imageUri = // (cámara o galería)

// 2. Frontend: Convertir a MultipartBody.Part
val file = FileUtils.prepareImagePart(context, imageUri, "file")

// 3. Frontend: Enviar al backend
ProductApiService.uploadProductImage(productId, file)

// 4. Backend: Subir a Cloudinary
cloudinaryService.uploadProductImage(file)

// 5. Backend: Guardar URL en BD
producto.setImagenUrl(cloudinaryUrl)

// 6. Backend: Retornar URL
return ResponseEntity.ok(cloudinaryUrl)

// 7. Frontend: Mostrar imagen
AsyncImage(model = imageUrl, ...)
```

**Archivos:**
- `FileUtils.kt` → Conversión de imágenes
- `EditProductScreen.kt` → UI de carga de imagen producto
- `UsSetScreen.kt` → UI de carga de imagen perfil

---



---

## 🌐 API Endpoints

### Base URL

```
http://10.0.2.2:8080/api/
```

### Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/auth/login` | Login de usuario |
| POST | `/auth/register` | Registro de usuario |

### Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/productos/activos` | Productos activos (catálogo) |
| GET | `/productos/categoria/{id}` | Productos por categoría |
| GET | `/productos/{id}` | Detalle de producto |
| POST | `/productos` | Crear producto (admin) |
| PUT | `/productos/{id}` | Actualizar producto (admin) |
| PUT | `/productos/{id}/desactivar` | Desactivar producto (admin) |
| PUT | `/productos/{id}/activar` | Reactivar producto (admin) |
| POST | `/productos/{id}/imagen` | Subir imagen producto |

### Favoritos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/favoritos/usuario/{id}` | Favoritos del usuario |
| POST | `/favoritos` | Agregar a favoritos |
| DELETE | `/favoritos` | Remover de favoritos |

### Órdenes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/ordenes` | Crear orden |
| GET | `/ordenes/usuario/{id}` | Órdenes del usuario |
| GET | `/ordenes` | Todas las órdenes (admin) |
| GET | `/ordenes/{id}` | Detalle de orden |
| PUT | `/ordenes/{id}/estado` | Actualizar estado orden |

### Usuarios

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/usuarios/{id}` | Detalle de usuario |
| PUT | `/usuarios/{id}` | Actualizar usuario |
| POST | `/usuarios/{id}/imagen` | Subir foto de perfil |
| GET | `/usuarios/activos` | Usuarios activos (admin) |
| PUT | `/usuarios/{id}/desactivar` | Desactivar usuario (admin) |

### Categorías

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/categorias` | Todas las categorías |

### Blogs

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/blogs` | Todos los blogs |
| GET | `/blogs/{id}` | Detalle de blog |

---

## 🧪 Testing

### Ejecutar Tests

```bash
# Tests unitarios
./gradlew test

# Tests de UI (Espresso)
./gradlew connectedAndroidTest
```

### Tests Implementados

**Ubicación:** `app/src/test/java/`

1. **ContadorTest.kt** - Test básico de contador
2. **GreetingTest.kt** - Test de composable de saludo
3. **PostRepositoryTest.kt** - Test de repositorio con MockK

**Ejemplo:**
```kotlin
@Test
fun `test cart total calculation`() {
    val cartViewModel = CartViewModel()
    val product = Product(id = 1, price = 1000.0, ...)
    
    cartViewModel.addToCart(product)
    cartViewModel.addToCart(product)
    
    assertEquals(2000.0, cartViewModel.calculateSubtotal())
}
```


## 👥 Credenciales de Prueba

### Usuario Admin

```
Email: admin@duocuc.cl
Password: admin123
```

### Usuario Cliente

```
Email: ana.martinez@duocuc.cl
Password: cliente123
```

---

## 🐛 Troubleshooting

### Error de conexión con backend

```kotlin
// Verificar BASE_URL en NetworkModule.kt
private const val BASE_URL = "http://10.0.2.2:8080/"

// Para dispositivo físico, usar IP de tu máquina
private const val BASE_URL = "http://192.168.1.X:8080/"
```

### Imágenes no cargan

- Verificar que Cloudinary esté configurado en el backend
- Revisar URLs en la base de datos
- Comprobar conexión a internet

---

## 📝 Notas de Desarrollo

### Soft Delete

El sistema implementa **soft delete** (borrado lógico):
- Productos: `activo = false` (se ocultan del catálogo)
- Usuarios: `activo = false` (no pueden hacer login)
- Se mantienen en BD para historial de órdenes

### Validación de Carrito/Favoritos

- Se validan en **cada apertura** de pantalla
- Consultan productos por ID al backend
- Eliminan automáticamente productos inactivos
- Actualizan precios/nombres con datos frescos

### Costo de Envío

- **Fijo:** $3,000 por orden
- Se suma al total en checkout
- Se registra en la tabla `ordenes`

---


## 👥 Autores

Desarrollado por:
- **Sebastián Valdivia** - [GitHub](https://github.com/ZalkiRyon)
- **Paula Frías** - [GitHub](https://github.com/paufriasest)

**Institución:** DUOC UC  
**Año:** 2025  
**Curso:** Desarrollo de Aplicaciones Móviles

---

## 🔗 Enlaces

- **Backend Repository:** [Huerto_Hogar_Backend_Movil](https://github.com/ZalkiRyon/Huerto_Hogar_Backend_Movil)
- **Frontend Repository:** [Huerto_Hogar_Frontend_Movil](https://github.com/ZalkiRyon/Huerto_Hogar_Frontend_Movil)

---

```
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠛⠛⠉⠙⠛⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠟⠉⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠘⠿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠿⠛⠁⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠛⢿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠿⣿⣟⣛⣻⡿⣿⣿⣿⣫⡿⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠘⢿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⣫⠴⠿⠛⡛⠣⠤⣤⡤⣠⣾⣿⣷⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⢠⣀⠄⠄⠄⠘⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠈⣃⢌⣭⣬⣭⠻⠁⣴⠆⣿⣿⣿⣿⣷⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠹⣿⡖⣀⣀⣾⣯⡻⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⠃⠄⠄⢫⣬⣭⠛⢱⣾⣶⢤⣾⠙⣿⣿⣿⣿⣿⣦⣄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⢀⣽⢸⣿⣿⣿⣿⣿⡝⣿
⣿⣿⣿⣿⣿⣿⣿⣿⡄⠄⠄⠈⠉⢉⠄⡟⣿⡏⣸⣿⣷⡜⢿⣿⣿⣿⣿⣿⣷⣶⣤⣀⠄⠄⠄⠄⠄⠄⢀⡐⣿⣿⡘⣿⣿⣿⣿⣿⣰⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣇⠄⠐⠄⠄⢸⡄⣷⡘⢰⣿⣿⣿⣿⣷⡹⢿⣿⣿⣿⣿⣿⣿⣿⣿⣟⣿⣿⣿⣾⣿⣧⢸⣿⣿⣮⣙⣛⣫⣴⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⢰⠄⢀⠐⢷⣸⣿⣾⣿⣿⣿⣿⡏⣿⣷⣍⡛⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡀⠄⣀⠚⢧⡁⣿⣿⣿⣿⣿⡇⡫⢅⢰⡶⣀⢰⡦⡈⢉⡛⡛⡛⣛⠛⡛⡉⠄⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⡿⠟⠛⠋⠉⠁⠄⠄⢀⠄⡁⣿⣿⣿⣿⣿⠃⠐⠛⠈⠐⠻⢬⣴⣷⣬⣴⠃⠐⠉⢀⡀⣡⣤⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⡀⠄⠄⠄⠄⠄⠄⠄⡀⠄⠄⣿⣿⣿⣿⣿⢰⠄⠄⠄⠄⢲⣾⣿⣿⣿⣿⠄⠄⠄⠄⡇⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣧⡀⠄⠄⠄⠄⠄⠄⠯⠈⠄⢿⣿⣿⡿⡿⢸⠄⠄⠄⠄⣸⣿⣿⣿⣿⣿⡀⠄⠄⠄⣧⡙⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣄⠄⠄⠄⠄⠄⠑⠠⠈⢘⢻⣿⡇⠁⢸⣧⡀⠄⣠⣿⣿⣿⣿⣿⣿⣷⣄⣀⣴⣿⡇⡟⢹⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣤⣠⣤⣶⠄⠄⠈⠄⠙⢿⡇⠄⣸⣿⣿⣿⣿⠏⣼⣿⣿⣿⣿⣼⣿⣿⠟⠋⡀⠄⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⠄⠄⠄⠄⡁⠈⠉⠉⠉⠛⠛⠃⠹⠻⠛⠋⠁⠉⠁⠄⡆⢀⠨⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⢀⣀⡀⢀⣄⣀⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠐⡶⢾⣿⣿⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣾⣿⣿⣿⣗⠻⣿⣿⣿⣿⣷⣶⠄⠄⠄⡀⠄⢀⣸⣿⣮⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⠿⣟⣫⣥⣶⣶⣶⣦⣤⣭⣛⡿⣿⣿⣿⡿⠗⠄⠄⠉⠉⠛⠃⠄⢒⣛⣛⡂⠛⠉⢡⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⢛⣥⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣬⠛⠁⠄⠄⠄⠄⠄⠄⠄⠄⠄⠘⣿⣿⣷⡄⠄⠄⠉⠛⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⡿⢃⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⣿⡄⢀⣤⣴⣶⣶⣶⣦⣀⠄⠄⢻⣿⣿⣿⡀⠄⠄⠄⠄⠄⠛⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⡇⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣝⢣⠘⠿⠟⠛⠛⠛⢛⠛⠄⠄⢸⣿⣿⣿⣧⠄⠄⠄⠄⣀⣀⣬⣭⢻⣿⣿⣿⣿⣿⣿⣿
⡇⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⠄⣶⢀⡀⠲⣶⣯⣹⣶⣰⣾⣿⣿⣿⣿⣶⣶⣿⣿⣿⣿⣿⣿⣷⡽⣿⣿⣿⣿⣿⣿
⣧⠹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣯⡅⣠⣤⡅⣤⣴⣬⡝⢫⣬⣥⣿⣿⣿⣿⣿⡿⠿⠿⠟⢛⣛⣻⣯⣥⣴⣿⣿⣿⣿⣿⣿
⣿⣷⡙⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣴⣽⣷⣧⣿⣟⣿⡗⣰⣶⣶⣶⣶⣦⣀⠄⣶⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣧⣤⣛⣛⣛⣛⣛⣛⣛⣛⣛⣋⣉⣉⣉⣉⣉⣉⣉⣉⣉⣥⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
```

