package cl.andreus.announcer;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

//imports de java
import java.util.*;

public class AnnouncerCommands implements BasicCommand {
    //guardar el padre para acceder a message.yml
    private final Announcer plugin;
    private final AnnouncerScheduler scheduler;
    //hashmap para el cooldown de /announcer
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    //constructor
    public AnnouncerCommands(Announcer plugin, AnnouncerScheduler scheduler){
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    //cosas que muestra al escribir el comando
    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args){
        //sugerencias para el comando en la posicion 1
        if(args.length == 1){
            //retornar una lista de strings con las 3 opciones
            return new ArrayList<String>(List.of("reload","start","stop"));
        }
        //si usa start o stop, mostrar los grupos de mensajes existentes
        if(args.length == 2 && (args[0].equals("start") || args[0].equals("stop"))){
            //crear una lista con todos los nombres de los grupos
            List<String> namesGroups = new ArrayList<String>();
            for(MessageGroup group : scheduler.getGroups()){
                namesGroups.add(group.getName());
            }
            //retornar los nombres de los grupos
            return namesGroups;
        }
        //si usa /announcer reload, mostrar config,messages y all
        if(args.length == 2 && args[0].equals("reload")){
            //retornar una lista de strings con las 3 opciones
            return new ArrayList<String>(List.of("all","config","messages"));
        }
        //retornar ninguna opción
        return new ArrayList<String>();
    }

    //función a ejecutar si usan el comando
    @Override
    public void execute(CommandSourceStack source, String[] args){
        CommandSender sender = source.getSender(); //quien ejecutó el mensaje
        Entity executor = source.getExecutor(); //qué entidad ejecutó el comando?

        //si es un jugador, tiene cooldown, verifica el cooldown antes de ejecutar comandos
        if ((executor instanceof Player player)) {
            //calcular los cooldowns
            long currentTime = System.currentTimeMillis();
            long cooldownTime = plugin.getConfig().getLong("cooldown.time",3000); //3 segundos por defecto

            //si el jugador ya usó el comando antes
            if(cooldowns.containsKey(player.getUniqueId())){
                //obtener la última vez que usó el comando
                long lasUsed = cooldowns.get(player.getUniqueId());
                //si todavía no puede usar el comando
                if(currentTime - lasUsed < cooldownTime){
                    //tomar el mensaje del config.yml
                    String message = plugin.getConfig().getString("cooldown.message","Debes esperar {cooldown} para usar el comando");
                    int secondsLeft = (int) ((cooldownTime - (currentTime-lasUsed))/1000);
                    message = message.replace("{cooldown}", String.valueOf(secondsLeft));
                    sender.sendPlainMessage(message);
                    //cancelar el comando, terminar la funcion
                    return;
                }
            }

            //ya pasó el cooldown definido, ejecutar comando
            //antes de ejecutar comando, actualizar la última vez que uso el comando, poner hora actual
            cooldowns.put(player.getUniqueId(),currentTime);
        }

        //ya no tiene cooldown o no es un usuario el que ejecutó el comando

        //declarar variable para el mensaje de salida
        String message;

        //comprobar si ejecutó /announcer reload
        if(args.length >= 1 && args[0].equals("reload")) {
            //si usó solo /announcer reload o /announcer reload all
            if (args.length == 1 || (args.length==2 && args[1].equals("all"))) {
                //reload de los mensajes
                boolean result = this.scheduler.reload();
                //reload del config
                plugin.reloadConfig();
                if (result) { //se ejecutó bien
                    //extraer el mensaje del config.yml o usar el default
                    message = plugin.getConfig().getString("messages.reload_success", "Se recargó messages.yml y config.yml");
                } else {
                    //extraer el mensaje del config.yml o usar el default
                    message = plugin.getConfig().getString("messages.reload_error", "Hubo un error al recargar messages.yml");
                }
                //mandar mensaje al que ejecutó el comando
                sender.sendPlainMessage(message);
                return;
            }
            //si usó /announcer reload config
            else if (args.length == 2 && args[1].equals("config")) {
                //recargar el config y prepara el mensaje
                plugin.reloadConfig();
                message = plugin.getConfig().getString("messages.reload_config", "Se recargó config.yml");
                //mandar mensaje al que ejecutó el comando
                sender.sendPlainMessage(message);
                return;
            }
            //si usó /announcer reload messages
            else if (args.length == 2 && args[1].equals("messages")) {
                //reload de los mensajes
                boolean result = this.scheduler.reload();
                if(result){ //se ejecutó bien
                    //extraer el mensaje del config.yml o usar el default
                    message = plugin.getConfig().getString("messages.reload_messages_success", "Se recargó messages.yml");
                } else {
                    //extraer el mensaje del config.yml o usar el default
                    message = plugin.getConfig().getString("messages.reload_messages_error", "Hubo un error al recargar messages.yml");
                }
                //mandar mensaje al que ejecutó el comando
                sender.sendPlainMessage(message);
                return;
            }
        }

        //comprobar si ejecutó /announcer stop nombre
        if(args.length == 2 && args[0].equals("stop")){
            //ejecutar el comando
            boolean result = this.scheduler.stopScheduler(args[1]);
            //indicarle al usuario el resultado
            if(result){ //éxito
                //extraer el mensaje del config.yml o usar el default
                message = plugin.getConfig().getString("messages.stop_success","El grupo de mensajes {message_group} se pausó");
            } else { //error
                //extraer el mensaje del config.yml o usar el default
                message = plugin.getConfig().getString("messages.stop_error","El grupo de mensajes {message_group} no existe o ya está pausado");
            }
            //reemplazar {message_group} con el nombre del grupo usado al llamar el comando
            message = message.replace("{message_group}",args[1]);
            //mandar mensaje al que ejecutó el comando
            sender.sendPlainMessage(message);
            return;
        }

        //comprobar si ejecutó /announcer start nombre
        if(args.length == 2 && args[0].equals("start")){
            //ejecutar el comando
            boolean result = this.scheduler.startScheduler(args[1]);
            //indicarle al usuario el resultado
            if(result){ //éxito
                //extraer el mensaje del config.yml o usar el default
                message = plugin.getConfig().getString("messages.start_success","El grupo de mensajes {message_group} se reanudó");
            } else { //error
                //extraer el mensaje del config.yml o usar el default
                message = plugin.getConfig().getString("messages.start_error","El grupo de mensajes {message_group} no existe o ya está reanudado");
            }
            //reemplazar {message_group} con el nombre del grupo usado al llamar el comando
            message = message.replace("{message_group}",args[1]);
            //mandar mensaje al que ejecutó el comando
            sender.sendPlainMessage(message);
            return;
        }

        //el comando no se reconoce o no existe
        //extraer el mensaje del config.yml o usar el default
        message = plugin.getConfig().getString("messages.unknown_command","Comando no reconocido");
        sender.sendPlainMessage(message);
    }
}
