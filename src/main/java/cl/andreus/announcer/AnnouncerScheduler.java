package cl.andreus.announcer;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnnouncerScheduler {

    //atributos
    private List<MessageGroup> groups;
    private HashMap<String, ScheduledTask> tasks;
    private final Announcer plugin;

    //constructor
    public AnnouncerScheduler(Announcer plugin){
        this.plugin = plugin;//guardar el plugin (usar cosas de paper)
        this.groups = new ArrayList<>(LoadGroups());//cargar los mensajes
        this.tasks = new HashMap<>();//preparar los temporizadores
    }

    //función encargada de cargar todos los grupos de mensajes
    public List<MessageGroup> LoadGroups(){
        //cargar el archivo de configuración
        File configFile = new File(plugin.getDataFolder(),"messages.yml");
        //pasar el archivo a formato yml
        YamlConfiguration messagesConfig = YamlConfiguration.loadConfiguration(configFile);

        //preparar la lista con los grupos de mensajes cargados
        List<MessageGroup> listGroups = new ArrayList<>();

        //verificar si el archivo está vacío o mal formado
        ConfigurationSection groupsSection = messagesConfig.getConfigurationSection("groups");
        if (groupsSection == null) {
            plugin.getLogger().warning("Hubo un error al cargar el archivo messages.yml");
            return listGroups; //retornar lista vacía
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
            //cada string de ese grupo guardarlo en su respectivo objeto
            messagesConfig.getStringList("groups."+groupName+".messages").forEach(message -> {
                group.addMessage(message);
            });
            //agregar el grupo a la lista
            listGroups.add(group);
        });

        return listGroups;
    }
}
