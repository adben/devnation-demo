package org.acme;

import java.util.List;

import javax.persistence.Entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;

@Entity
public class Fruit extends PanacheEntity {
    public String name;
    public String color;

    public Fruit() {
    }
    
    public Fruit(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Uni<List<Fruit>> findByColor(String color) {
        return list("color", color);
    }
}
