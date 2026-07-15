package cl.andreus.announcer;

import java.util.List;
import java.util.ArrayList;

public class MessageGroup {
    private String name;
    private List<String> messages;
    private int interval; //tiempo en milisegundos (segundos)
    private boolean random; //indicar si el mensaje es random
    private int currentIndex; //indice del mensaje siguiente a mostrar

    //constructor
    public MessageGroup(String name, int interval, boolean random){
        this.name = name;
        this.messages = new ArrayList<>(); //iniciar lista vacía
        this.interval = interval;
        this.random = random;
        this.currentIndex = 0;
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

    //Retorna el siguiente mensaje en salir por pantalla
    public String getCurrentMessage(){
        String message;
        if(!this.isRandom()){
            message = this.messages.get(this.currentIndex);
            this.currentIndex++;
            this.currentIndex = this.currentIndex % this.getMessagesCount();
        }
        else {
            int index = (int) (Math.random()* this.getMessagesCount());
            message = this.messages.get(index);
        }
        return message;
    }

}
