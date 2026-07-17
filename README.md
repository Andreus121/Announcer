# Announcer

paper plugin that broadcasts configurable messages to players at set intervals.

## Commands

| Command                        | Description                             | Permissions |
|--------------------------------|-----------------------------------------|-------------|
| `/hello reload`                | Reload the messages from messages.yml   | op          |
| `/hello stop <MessagesGroup>`  | Stop to send message of a MessageGroup  | op          |
| `/hello start <MessagesGroup>` | Start to send message of a MessageGroup | op          |

## Configuration

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

> Coming soon: a config.yml file to customize the messages shown to the OP when using the commands.

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