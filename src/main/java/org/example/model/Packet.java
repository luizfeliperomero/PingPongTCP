package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

@Data
public class Packet implements Serializable {
    Long timeNano;
    String type;

    public Packet() {
        this.timeNano = System.nanoTime();
    }
}
