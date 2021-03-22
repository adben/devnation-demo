package org.acme;

import java.util.List;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

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

    public static List<Fruit> findByColor(String color) {
        return list("color", color);
    }
}
