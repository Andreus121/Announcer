package cl.andreus.announcer;

import java.util.List;
import java.util.ArrayList;

public class MessageGroup {
    private String name;
    private List<String> messages;
    private int interval; //tiempo en milisegundos (segundos)
    private boolean random;

    //constructor
    public MessageGroup(String name, int interval, boolean random){
        this.name = name;
        this.messages = new ArrayList<>(); //iniciar lista vacía
        this.interval = interval;
        this.random = random;
    }

    //getters
    public String getName() {
        return name;
    }

    public List<String> getMessages(){
        return messages;
    }

    public int getMessagesCount(){
        return messages.size();
    }

    public int getInterval(){
        return interval;
    }

    public boolean isRandom(){
        return random;
    }

    //setters
    public void setName(String newName){
        this.name = newName;
    }

    public void setInterval(int newInterval){
        this.interval = newInterval;
    }

    public void toggleRandom() {
        this.random = !random;
    }

    //funciones mensajes
    public void addMessage(String message){
        this.messages.add(message);
    }

    public void removeMessage(String message){
        this.messages.remove(message);
    }
}
