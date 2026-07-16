# TODO - Announcer Plugin

## En progreso
- [x] `loadGroups()` en `AnnouncerScheduler.java`
  - [x] Agregar verificación de null para `getConfigurationSection("groups")`
  - [x] Usar la clave del grupo como nombre
- [x] Implementar schedulers en `AnnouncerScheduler`
  - [x] Corregir bug de shadowing en `createSchedulers()`
  - [x] Llamar `createSchedulers()` en el constructor
- [x] Eliminar el campo `name` de `messages.yml`
- [x] Funciones en `AnnouncerScheduler`
  - [x] Buscar un grupo de mensajes especifico por nombre
  - [x] Detener un scheduler específico por nombre
  - [x] Reanudar un scheduler específico por nombre
  - [x] Reload (recargar `messages.yml` y reiniciar schedulers)
- [x] Crear clase `AnnouncerCommands.java`
  - [x] `/announcer reload`
  - [x] `/announcer stop <grupo>`
  - [x] `/announcer start <grupo>`
- [x] Clase principal `Announcer.java`
- [x] Migración automática de `messages.yml`

## Pendiente
- [ ] Permisos en `plugin.yml`
- [ ] README

## Mejoras futuras
- [ ] Crear `config.yml`
  - [ ] añadir cooldown a todos los comandos (personalizable desde config.yml)
  - [ ] Mensajes de configuraciones personalizados `messagesCommands.yml`
- [ ] Implementar soporte para colores con `&`
- [ ] Implementar GUI para `/announcer messages`
  - [ ] Editar configuración desde GUI
  