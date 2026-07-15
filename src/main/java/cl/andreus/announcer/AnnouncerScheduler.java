package cl.andreus.announcer;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnnouncerScheduler {

    //atributos
    private final Announcer plugin;
    private List<MessageGroup> groups;
    private HashMap<String, ScheduledTask> tasks;

    //constructor
    public AnnouncerScheduler(Announcer plugin){
        this.plugin = plugin;//guardar el plugin (usar cosas de paper)
        this.groups = new ArrayList<>();//cargar los mensajes
        this.loadGroups();
        this.tasks = new HashMap<>();//preparar los temporizadores
        this.createSchedulers();
    }

    //función encargada de cargar todos los grupos de mensajes
    public void loadGroups(){
        //cargar el archivo de configuración
        File configFile = new File(plugin.getDataFolder(),"messages.yml");
        //pasar el archivo a formato yml
        YamlConfiguration messagesConfig = YamlConfiguration.loadConfiguration(configFile);

        //verificar si el archivo está vacío o mal formado
        ConfigurationSection groupsSection = messagesConfig.getConfigurationSection("groups");
        if (groupsSection == null) {
            plugin.getLogger().warning("Hubo un error al cargar el archivo messages.yml");
            return;
        }

        //cargar la sección groups y cada nombre de grupos de esta misma
        groupsSection.getKeys(false).forEach(groupName -> {
            //cargar cada grupo
            //cargar el intervalo de tiempo (en segundos)
            int interval = messagesConfig.getInt("groups."+groupName+".interval");
            //cargar si es random o no
            boolean random = messagesConfig.getBoolean("groups."+groupName+".random");
            //crear el nuevo grupo de mensaje
            MessageGroup group = new MessageGroup(groupName, interval, random);
            //guardar todos los mensajes del messages.yml
            List<String> messagesList = messagesConfig.getStringList("groups."+groupName+".messages");
            //agregar cada mensaje a su respectivo grupo
            for (String message : messagesList) {
                group.addMessage(message);
            }
            //agregar el grupo a la lista
            this.groups.add(group);
        });
    }

    //función para crear el scheduler de cada grupo de mensajes
    public void createSchedulers() {
        //cargar el scheduler global del sv
        GlobalRegionScheduler globalScheduler = plugin.getServer().getGlobalRegionScheduler();

        //crear un scheduler para cada grupo de mensajes
        ScheduledTask task; //declarar la tarea
        for(MessageGroup group : this.groups){
            //crear la tarea
            task = globalScheduler.runAtFixedRate(plugin,
                    scheduledTask -> {
                        //aquí va el código que se ejecuta cada vez
                        //obtener el siguiente mensaje que toca mostrar
                        Component message = Component.text(group.getCurrentMessage());
                        //mostrar el mensaje
                        Bukkit.broadcast(message);

                    },
                    20L, //delay inicial para ejecutar la tarea
                     20L * group.getInterval() //segundos indicados en el atributo del grupo
            );

            //agregar la tarea al Hash
            this.tasks.put(group.getName(),task);
        }
    }
}
