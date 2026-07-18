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
        if(args.length == 2){
            //crear una lista con todos los nombres de los grupos
            List<String> namesGroups = new ArrayList<String>();
            for(MessageGroup group : scheduler.getGroups()){
                namesGroups.add(group.getName());
            }
            //retornar los nombres de los grupos
            return namesGroups;
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
            long cooldownTime = plugin.getConfig().getLong("cooldown",3000); //3 segundos por defecto

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
                }
            }
        }

        //ya no tiene cooldown o no es un usuario el que ejecutó el comando


        //comprobar si ejecutó /announcer reload
        if(args.length == 1 && args[0].equals("reload")){
            //ejecutar el comando
            boolean result = this.scheduler.reload();
            if(result){ //se ejecutó bien
                sender.sendPlainMessage("Grupo de mensajes actualizado");
            }else {
                sender.sendPlainMessage("Hubo un error al recargar el grupo de mensajes");
            }
            return;
        }
        //comprobar si ejecutó /announcer stop nombre
        if(args.length == 2 && args[0].equals("stop")){
            //ejecutar el comando
            boolean result = this.scheduler.stopScheduler(args[1]);
            //indicarle al usuario el resultado
            if(result){ //éxito
                sender.sendPlainMessage("El grupo de mensajes "+args[1]+" se pausó");
            } else { //error
                sender.sendPlainMessage("El grupo de mensajes "+args[1]+" no existe o ya está pausado");
            }
            return;
        }
        //comprobar si ejecutó /announcer start nombre
        if(args.length == 2 && args[0].equals("start")){
            //ejecutar el comando
            boolean result = this.scheduler.startScheduler(args[1]);
            //indicarle al usuario el resultado
            if(result){ //éxito
                sender.sendPlainMessage("El grupo de mensajes "+args[1]+" se reanudó");
            } else { //error
                sender.sendPlainMessage("El grupo de mensajes "+args[1]+" no existe o ya está reanudado");
            }
            return;
        }
        sender.sendPlainMessage("Comando no reconocido");
    }

}
