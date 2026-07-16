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
        //crear un scheduler para cada grupo de mensajes
        for(MessageGroup group : this.groups){
            createScheduledTask(group);
        }
    }

    //función para buscar un grupo de mensajes específico
    public MessageGroup findGroupMessages(String name){
        //revisar cada grupo de mensajes
        for(MessageGroup group : this.groups){
            //se encuentra el grupo de mensajes deseado
            if(group.getName().equals(name)){
                return group;
            }
        }
        //no se encuentra el grupo de mensajes necesitado
        return null;
    }

    //función para detener un scheduler específico
    public boolean stopScheduler(String name){
        ScheduledTask task = this.tasks.get(name);
        if(task == null) return false;
        task.cancel();
        this.tasks.remove(name);
        return true;
    }

    //función para reanudar una tarea (llama a crear un ScheduledTask nuevo)
    public boolean startScheduler(String name){
        //buscar el grupo a reanudar
        MessageGroup group = this.findGroupMessages(name);
        //si el grupo no existe, termina la operación
        if(group == null){ return false;}
        //crear la task
        createScheduledTask(group);
        return true;
    }

    //función agregar crear y agregar un ScheduledTask al objeto
    private void createScheduledTask(MessageGroup group){
        //cargar el scheduler global del sv
        GlobalRegionScheduler globalScheduler = plugin.getServer().getGlobalRegionScheduler();

        //crear un scheduler para cada grupo de mensajes
        ScheduledTask task = globalScheduler.runAtFixedRate(plugin,
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

        this.tasks.put(group.getName(),task);
    }

    //recarga todos los datos del plugin actualmente
    public void reload(){
        //cancelar todas las tareas activas
        for(ScheduledTask task : this.tasks.values()){
            task.cancel();
        }
        //limpiar las listas
        this.tasks.clear();
        this.groups.clear();
        //recargar todo desde cero
        this.loadGroups();
        this.createSchedulers();
    }

}


