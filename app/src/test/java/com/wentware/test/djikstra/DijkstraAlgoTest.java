package com.wentware.test.djikstra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DijkstraAlgoTest {

    DijkstraAlgo classUnderTest;

    @BeforeEach
    void setup() throws Exception {
        Graph graph = Fixture.createTestGraph();
        classUnderTest = new DijkstraAlgo(graph, "N1", "N4");
    }

    @Test
    void init_noSource_shouldThrow() {
        Graph graph = Fixture.createTestGraph();

        Exception e = assertThrows(Exception.class, () -> new DijkstraAlgo(graph, "Vx", "V4"));
        assertEquals("Failed to find source by name Vx", e.getMessage());
    }

    @Test
    void init_noGoal_shouldThrow() {
        Graph graph = Fixture.createTestGraph();
        Exception e = assertThrows(Exception.class, () -> new DijkstraAlgo(graph, "N1", "Nx"));
        assertEquals("Failed to find goal by name Nx", e.getMessage());
    }

    @Test
    void traverse_withIsolatedVertex_shouldThrowf() throws Exception {

        Graph badGraph = new Graph();
        Graph.Node n5 = badGraph.addNode("N5");
        Graph.Node n4 = badGraph.addNode("N4");
        Graph.Node n3 = badGraph.addNode("N3")
                .addEdgeTo(n4, 2);
        Graph.Node n2 = badGraph.addNode("N2")
                .addEdgeTo(n4, 1)
                .addEdgeTo(n3, 2);
        badGraph.addNode("N1")
                .addEdgeTo(n2, 3)
                .addEdgeTo(n3, 4);
        DijkstraAlgo dijkstraAlgoWBadGoal = new DijkstraAlgo(badGraph, "N1", "N5");

        Exception badGraphException = assertThrows(Exception.class, dijkstraAlgoWBadGoal::solve);

        assertEquals("Failed to find path with bad graph", badGraphException.getMessage());
    }

    @Test
    void traverse() throws Exception {
        Graph graph = Fixture.createTestGraph();

        classUnderTest = new DijkstraAlgo(graph, "N1", "N4");
        DijkstraAlgo.Solution solution = classUnderTest.solve();

        assertNotNull(solution);
        assertEquals(5, solution.cost());
        assertEquals(3, solution.path().size());
        Graph.Node n1 = graph.findNodeById("N1").orElseThrow();
        Graph.Node n2 = graph.findNodeById("N2").orElseThrow();
        Graph.Node n4 = graph.findNodeById("N4").orElseThrow();
        assertTrue(solution.path().contains(n1));
        assertTrue(solution.path().contains(n2));
        assertTrue(solution.path().contains(n4));
    }

    @Test
    void getNextVert_ShouldReturnFirstVert() {
        DijkstraAlgo.DNode nextV = classUnderTest.getNext().orElseThrow();
        assertEquals("N1", nextV.getGNode().getId());
    }

    @Test
    void priorityQueue() {
        PriorityQueue<DijkstraAlgo.DNode> queue = new PriorityQueue<>(Comparator.comparingInt(DijkstraAlgo.DNode::getCostFromSource));
        Graph.Node n1 = new Graph.Node("N1");
        DijkstraAlgo.DNode dn1 = new DijkstraAlgo.DNode(n1);
        dn1.setCostFromSource(1);

        Graph.Node n2 = new Graph.Node("N3");
        DijkstraAlgo.DNode dn2 = new DijkstraAlgo.DNode(n2);
        dn2.setCostFromSource(2);

        queue.add(dn1);
        queue.add(dn2);
        DijkstraAlgo.DNode lowest = queue.remove();

        assertEquals(1, lowest.getCostFromSource());
    }

}