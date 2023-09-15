package com.wentware.test.djikstra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
        Graph testGraph = createBadGraph();
        DijkstraAlgo dijkstraAlgoWBadGoal = new DijkstraAlgo(testGraph, "N1", "N5");

        Exception badGraphException = assertThrows(Exception.class, dijkstraAlgoWBadGoal::traverse);

        assertEquals("Failed to find path with bad graph", badGraphException.getMessage());
    }

    private Graph createBadGraph() {
        Graph testGraph = new Graph();
        Graph.Node n5 = testGraph.addNode("N5");
        Graph.Node n4 = testGraph.addNode("N4");
        Graph.Node n3 = testGraph.addNode("N3")
                .addEdgeTo(n4, 2);
        Graph.Node n2 = testGraph.addNode("N2")
                .addEdgeTo(n4, 1)
                .addEdgeTo(n3,2);
        testGraph.addNode("N1")
                .addEdgeTo(n2, 3)
                .addEdgeTo(n3,4);
        return testGraph;
    }

    @Test
    void traverse() throws Exception {
        Graph graph = Fixture.createTestGraph();

        classUnderTest = new DijkstraAlgo(graph, "N1", "N4");
        DijkstraAlgo.Solution solution = classUnderTest.traverse();

        assertNotNull(solution);
        assertEquals(4, solution.cost());
        assertEquals(2, solution.path().size());
        Graph.Node n1 = graph.findNodeById("N1").orElseThrow();
        Graph.Node n2 = graph.findNodeById("N2").orElseThrow();
        assertTrue(solution.path().contains(n1));
        assertTrue(solution.path().contains(n2));
    }

    @Test
    void getNextVert_ShouldReturnFirstVert() {
        DijkstraAlgo.DNode nextV = classUnderTest.getNext().orElseThrow();
        assertEquals("N1", nextV.getGNode().getId());
    }

    @Test
    void gGetNextVert_allVisited_ShouldReturnEmpty() {
        classUnderTest.getDNodes().forEach(v -> v.setVisited(true));
        Optional<DijkstraAlgo.DNode> nextV = classUnderTest.getNext();
        assertTrue(nextV.isEmpty());
    }

}