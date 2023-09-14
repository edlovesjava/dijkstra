package com.wentware.test.djikstra;

import lombok.Data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
public class Graph {
    Set<Vert> vertices;

    public Graph() {
        this.vertices = new HashSet<>();
    }

    public Vert addVert(String name) {
        Optional<Vert> vertByName = findVertByName(name);
        if (vertByName.isPresent()) {
            return vertByName.get();
        }
        Vert vertToAdd = new Vert(name);
        vertices.add(vertToAdd);
        return vertToAdd;
    }

    public Optional<Vert> findVertByName(String name) {
        return vertices.stream().filter(v -> name.equals(v.getName())).findFirst();
    }
}
