package edu.upc.dsa.models;

import java.util.Date;

public class InterestPoint {
    private int x;
    private int y;
    private Date timestamp;  // Fecha del interés
    private ElementType type;  // Tipo de interés (por ejemplo, PARK, MUSEUM, etc.)

    // Constructor con todos los parámetros
    public InterestPoint(int x, int y, Date timestamp, ElementType type) {
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
        this.type = type;
    }

    // Getters y setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }
}
