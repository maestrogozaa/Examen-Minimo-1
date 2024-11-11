package edu.upc.dsa.models;

import java.util.Date;

public class VisitRecord {
    private String userId;
    private int x;
    private int y;
    private Date timestamp; // Asegúrate de que el campo timestamp sea de tipo Date

    // Constructor con los parámetros necesarios
    public VisitRecord(String userId, int x, int y, Date timestamp) {
        this.userId = userId;
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
    }

    // Métodos getter y setter
    public String getUserId() {
        return userId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
