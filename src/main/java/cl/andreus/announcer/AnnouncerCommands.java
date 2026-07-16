package cl.andreus.announcer;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;

//imports de java
import java.util.HashMap;
import java.util.UUID;

public class AnnouncerCommands implements BasicCommand {
    //guardar el padre para acceder a message.yml
    private final Announcer plugin;
    private final AnnouncerScheduler scheduler;

    //constructor
    public AnnouncerCommands(Announcer plugin, AnnouncerScheduler scheduler){
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    //función a ejecutar si usan el comando
    @Override
    public void execute(CommandSourceStack source, String[] args){
        CommandSender sender = source.getSender(); //quien ejecutó el mensaje

        //comprobar si ejecutó /announcer reload
        if(args.length == 1 && args[0].equals("reload")){
            //ejecutar el comando
            this.scheduler.reload();
            sender.sendPlainMessage("Grupo de mensajes actualizado");
            return;
        }
        //comprobar si ejecutó /announcer stop nombre
        if(args.length == 2 && args[0].equals("stop")){
            //ejecutar el comando
            boolean result = this.scheduler.stopScheduler(args[2]);
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
            boolean result = this.scheduler.startScheduler(args[2]);
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
