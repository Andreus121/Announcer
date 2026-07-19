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
- [x] Permisos en `plugin.yml`
- [x] Agregar relleno de comando `/announcer`
- [x] Arreglar bug, /announcer start no funciona
- [X] README
- [x] Crear `config.yml`
  - [x] añadir cooldown a todos los comandos (personalizable desde config.yml)
  - [x] Mensajes de configuraciones personalizados `messagesCommands.yml`
- [x] Separar /announcer reload message y /announcer reload config

## Mejoras futuras
- [ ] Implementar soporte para colores con `&`
- [ ] Implementar GUI para `/announcer messages`
  - [ ] Editar configuración desde GUI
  