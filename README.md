# Announcer

paper plugin that broadcasts configurable messages to players at set intervals.

## Commands

| Command                            | Description                             | Permissions |
|------------------------------------|-----------------------------------------|-------------|
| `/announcer reload`                | Reload messages.yml and config.yml      | op          |
| `/announcer reload all`            | Reload messages.yml and config.yml      | op          |
| `/announcer reload config`         | Reload only config.yml                  | op          |
| `/announcer reload messages`       | Reload only messages.yml                | op          |
| `/announcer stop <MessagesGroup>`  | Stop to send message of a MessageGroup  | op          |
| `/announcer start <MessagesGroup>` | Start to send message of a MessageGroup | op          |

## Configuration

### Messages

After the first run, a `messages.yml` will be generated in `plugins/Announcer/". In this file you can create your own messages and configurate them

```yaml
groups:
  #if you want to create a message, copy this example an modify values
  example1: #name of group, you can change it
    interval: 60 #time in seconds to send a message
    random: false #if true, the plugin will send a random message from the list, if false, it will send the messages in order
    #mensajes del grupo
    messages: #list of messages, you can add as many as you want
      - "Message 1"
      - "Message 2"
      - "Message 3"

  nameGroup:
    interval: 120
    random: true
    messages:
      - "Message A"
      - "Message B"
      - "Message C"
```
### Config

After the first run, a `config.yml` will be generated in `plugins/Announcer/". In this file you can custom the messages for the commands

```yaml
messages: #{message_group}: place the name of message_group used in the command
  reload_success: "Se recargó messages.yml y config.yml"
  reload_error: "Hubo un error al recargar messages.yml"
  reload_config: "Se recargó config.yml"
  reload_messages_success: "Se recargó messages.yml"
  reload_messages_error: "Hubo un error al recargar messages.yml"
  stop_success: "El grupo de mensajes {message_group} se pausó"
  stop_error: "El grupo de mensajes {message_group} no existe o ya está pausado"
  start_success: "El grupo de mensajes {message_group} se reanudó"
  start_error: "El grupo de mensajes {message_group} no existe o ya está reanudado"
  unknown_command: "Comando no reconocido"
cooldown: #this cooldown its for EVERY command in the plugin
  message: "Debes esperar {cooldown} para usar el comando"
  time: 3000 #milliseconds
```

## Download

Go to the [Releases](../../releases/latest) page and download the latest `.jar`.

## Requirements

- Minecraft 26.1.2
- Paper build #60+
- Java 25

## Installation

1. Download the `.jar`
2. Place it in your server's `plugins/` folder
3. Restart the server

## License

MIT