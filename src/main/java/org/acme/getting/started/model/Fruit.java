package org.acme.getting.started.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@RegisterForReflection
@Entity
public class Fruit {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fruitSeq")
    private Long id;
    @NotBlank(message = "Name may not be blank")
    private String name;
    @NotBlank(message = "Description may not be blank")
    private String description;

    public Fruit() {
    }

    public Fruit(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Fruit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
