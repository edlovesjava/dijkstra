package com.wentware.test.djikstra;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class Vert {
    @EqualsAndHashCode.Include String name;
    Set<Edge> edges;
    boolean isVisited;

    public Vert(String name) {
        this.edges = new HashSet<>();
        this.name = name;
    }

    public Vert addEdgeTo(Vert toVert, int weight) {
        Edge edge = new Edge(this, toVert, weight);
        edges.add(edge);
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

}
