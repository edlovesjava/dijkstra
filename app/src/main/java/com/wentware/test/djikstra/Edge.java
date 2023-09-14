package com.wentware.test.djikstra;

import lombok.Value;

@Value
public class Edge {
    Vert fromVert;
    Vert toVert;
    int weight;

    public Edge(Vert fromVert, Vert toVert, int weight) {
        this.fromVert = fromVert;
        this.toVert = toVert;
        this.weight = weight;
    }


}
