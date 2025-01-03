package dsa.proyecto.G4.models;

public class Question {
    private String title;
    private String date;
    private String message;
    private String sender;

    public Question() {
    }

    public Question(String title, String date, String message, String sender) {
        this.title = title;
        this.date = date;
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
