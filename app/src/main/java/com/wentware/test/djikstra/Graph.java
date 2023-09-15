package com.wentware.test.djikstra;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
public class Graph {
    Set<Node> nodes;

    public Graph() {
        this.nodes = new HashSet<>();
    }

    public Node addNode(String id) {
        Optional<Node> nodeById = findNodeById(id);
        if (nodeById.isPresent()) {
            return nodeById.get();
        }
        Node nodeToAdd = new Node(id);
        nodes.add(nodeToAdd);
        return nodeToAdd;
    }

    public Optional<Node> findNodeById(String id) {
        return nodes.stream().filter(v -> id.equals(v.getId())).findFirst();
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Data
    public static class Node {
        @EqualsAndHashCode.Include String id;
        Set<Edge> edges;
        boolean isVisited;

        public Node(String id) {
            this.edges = new HashSet<>();
            this.id = id;
        }

        public Node addEdgeTo(Node toNode, int weight) {
            Edge edge = new Edge(this, toNode, weight);
            edges.add(edge);
            return this;
        }

        @Override
        public String toString() {
            return id;
        }

    }

    @Value
    public static class Edge {
        Node fromNode;
        Node toNode;
        int weight;
    }
}
