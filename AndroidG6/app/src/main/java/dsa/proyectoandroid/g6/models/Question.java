package dsa.proyectoandroid.g6.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Question {
    private String title;
    private String date;
    private String message;
    private String sender;

    public Question() {//constructor vacio
    }

    public Question(String title, String message, String sender) {
        this.title = title;
        Date date1 = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = formatter.format(date1);
        this.message = message;
        this.sender = sender;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
