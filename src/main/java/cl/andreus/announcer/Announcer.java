package cl.andreus.announcer;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Announcer extends JavaPlugin {

    //guardar el scheduler como atributo
    private AnnouncerScheduler scheduler;

    @Override
    public void onEnable() {
        //lo que pasa cuando el plugin inicia
        try {
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
}
