# ETAPA 14 - Perfil propio y cambio de contraseña

## Objetivo

Extender la API cerrada en `etapa13` con los endpoints mínimos de autoservicio que necesita frontend para la vista de cuenta activa:

- actualizar perfil del usuario autenticado
- cambiar contraseña del usuario autenticado

La meta es completar la integración de cuenta sin alterar el contrato de `auth` existente.

---

## Punto de partida

`etapa13` ya dejó implementado:

- base URL `/api/v1`
- sesión opaca vía `Authorization: Bearer <sessionToken>`
- `auth/register`, `auth/login`, `auth/me` y `auth/logout`
- `UserResponse` con `firstName`, `lastName` y `fullName`
- manejo uniforme de errores HTTP
- autorización básica para endpoints administrativos

---

## Qué implementa etapa14

### 1. Perfil del usuario autenticado
Se agrega:

- `PUT /api/v1/users/me`

Body esperado:

```json
{
  "firstName": "Ada",
  "lastName": "Lovelace",
  "phone": "3001234567"
}
```

Response `200 OK`:

```json
{
  "id": 1,
  "email": "ada@cesde.edu.co",
  "firstName": "Ada",
  "lastName": "Lovelace",
  "fullName": "Ada Lovelace",
  "role": "CUSTOMER",
  "phone": "3001234567",
  "status": "ACTIVE",
  "createdAt": "2026-04-05T14:30:00"
}
```

### 2. Cambio de contraseña del usuario autenticado
Se agrega:

- `PUT /api/v1/users/me/password`

Body esperado:

```json
{
  "currentPassword": "secret123",
  "newPassword": "secret456"
}
```

Response `204 No Content`

### 3. Confirmaciones de contrato para frontend
Se deja explícito que:

1. `register` y `login` devuelven `sessionToken`, `sessionId`, `expiresAt`, `user` y `cart`.
2. `auth/me` devuelve `firstName`, `lastName` y `fullName`.
3. `fullName` lo entrega backend; frontend no necesita construirlo.
4. La base final sigue siendo `/api/v1`.

### 4. Pruebas de integración HTTP
Se agregan pruebas para cubrir:

- actualización de perfil exitosa
- cambio de contraseña exitoso
- login fallido con contraseña anterior
- login exitoso con nueva contraseña
- `400` por validación
- `401` sin token o con contraseña actual inválida

---

## Archivos principales incorporados o modificados

### Web / rutas
- `src/main/java/co/edu/cesde/pps/web/controller/ApiRoutes.java`
- `src/main/java/co/edu/cesde/pps/web/controller/UserProfileController.java`

### Application
- `src/main/java/co/edu/cesde/pps/application/UserProfileApplicationService.java`

### Request DTOs
- `src/main/java/co/edu/cesde/pps/web/dto/request/UpdateMyProfileRequest.java`
- `src/main/java/co/edu/cesde/pps/web/dto/request/ChangeMyPasswordRequest.java`

### Servicio de usuario
- `src/main/java/co/edu/cesde/pps/service/UserService.java`

### Testing
- `src/test/java/co/edu/cesde/pps/Etapa14UserSelfServiceIntegrationTest.java`

### Documentación
- `BACKEND_ENDPOINTS_REFRENCE.md`
- `ETAPA14_SUMMARY.md`

---

## Criterio de terminado

`etapa14` se considera terminada cuando:

- existe `PUT /api/v1/users/me`
- existe `PUT /api/v1/users/me/password`
- ambos endpoints requieren sesión autenticada
- el perfil actualizado se refleja también en `auth/me`
- la contraseña anterior deja de funcionar en login
- la nueva contraseña funciona en login
- la documentación pública queda alineada para frontend

---

## Validación recomendada

```bash
mvn -q -DskipTests compile
mvn -q -Dtest=Etapa14UserSelfServiceIntegrationTest test
mvn -q test
```

---

**Fecha:** 5 de abril de 2026  
**Rama objetivo:** `etapa14`  
**Estado:** ✅ Implementación de perfil propio y cambio de contraseña

