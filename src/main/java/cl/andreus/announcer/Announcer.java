package cl.andreus.announcer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class Announcer extends JavaPlugin {

    //guardar el scheduler como atributo
    private AnnouncerScheduler scheduler;

    @Override
    public void onEnable() {
        //lo que pasa cuando el plugin inicia
        try {
            //guardar el archivo config.yml
            saveDefaultConfig();
            //actualizar el config.yml
            migrationConfig();
            //guardar el messages.yml en la carpeta del plugin
            saveResource("messages.yml",false);//false para que no reemplace si ya existe
            //crear el scheduler que controla los tiempos
            this.scheduler = new AnnouncerScheduler(this);//aquí carga el messages.yml
            //registrar el comando announcer en el servidor
            registerCommand("announcer", new AnnouncerCommands(this,this.scheduler));
            //indicar que cargó correctamente
            getLogger().info("plugin iniciado correctamente");
        }
        catch (Exception e) {
            //desactivar el plugin
            getServer().getPluginManager().disablePlugin(this);
            //indicar que algo salió mal
            getLogger().severe("El plugin no cargó correctamente, el plugin queda desactivado");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    //actualizar el config.yml en caso de actualizar el plugin
    private void migrationConfig(){
        //obtener el config.yml del jar
        InputStream defaultStream = getResource("config.yml");
        if (defaultStream == null) return;//si no hay archivo config, no hagas nada

        //convierte el config.yml a un archivo yml (antes eran bits en el jar)
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(defaultStream)
        );

        //cargar el config del servidor desde el archivo físico
        File configFile = new File(getDataFolder(), "config.yml");
        //si no existe el archivo,
        if(!configFile.exists()) {
            try {
                //crea el config.yml y escribe los datos por defecto
                defaultConfig.save(configFile);
                //avisa que lea el archivo físico de nuevo para guardarlo
                reloadConfig();
                //avisar por consola
                getLogger().info("[Announcer] Config creado de cero");
            }
            catch (IOException e){//error al escribir el config.yml
                //avisar por consola
                getLogger().severe("[Announcer] Error al guardar el config migrado: "+ e.getMessage());
            }
            return;
        }

        /*
        ====SI EL ARCHIVO SI EXISTE====
        verifica las key para escribir las nuevas
        la idea es no cambiar los valores de las key
        existentes en el config.yml del servidor
        */

        //pasar el archivo físico a un yml
        YamlConfiguration serverConfig = YamlConfiguration.loadConfiguration(configFile);

        //comparar si hay alguna key distinta
        boolean changed = false;
        //cada key del config.yml por defecto del plugin verificar si está en el físico del sv
        for(String key : defaultConfig.getKeys(true)){
            //si no existe la key, añadela y marca el cambio
            if(!serverConfig.contains(key)){
                serverConfig.set(key, defaultConfig.get(key));
                changed = true;
            }
        }

        //si hubo algún cambio guarda el nuevo config.yml
        if(changed){
            try{
                serverConfig.save(configFile);
                reloadConfig();
                getLogger().info("[Announcer] Config migrado exitosamente");
            }
            catch (IOException e){
                getLogger().severe("[Announcer] Error al guardar el config migrado: "+ e.getMessage());
            }
        }else{//si no cambió nada
            getLogger().info("[Announcer] No hay keys nuevas que escribir en el config.yml");
        }
    }
}
