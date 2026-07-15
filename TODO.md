# TODO - Announcer Plugin

## En progreso
- [x] `loadGroups()` en `AnnouncerScheduler.java`
  - [x] Agregar verificación de null para `getConfigurationSection("groups")`
  - [x] Usar la clave del grupo como nombre (se eliminará el campo `name` del yml)
- [x] Implementar schedulers en `AnnouncerScheduler`
  - [x] Corregir bug de shadowing en `createSchedulers()`
  - [x] Llamar `createSchedulers()` en el constructor

## Pendiente
- [ ] Eliminar el campo `name` de `messages.yml`
- [ ] Funciones para detener/reanudar un scheduler específico (para `/announcer stop` y `/announcer start`)
- [ ] Implementar comandos en `AnnouncerCommands`
- [ ] Implementar GUI para `/announcer messages`
- [ ] Clase principal `Announcer.java`
- [ ] Migración automática de `messages.yml`
- [ ] Permisos en `plugin.yml`
- [ ] README

## Mejoras futuras
- [ ] Soporte para colores con `&`
- [ ] Editar configuración desde GUI