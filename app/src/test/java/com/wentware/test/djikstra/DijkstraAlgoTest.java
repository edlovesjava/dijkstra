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
        classUnderTest = new DijkstraAlgo(graph, "V1", "V4");
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
        Exception e = assertThrows(Exception.class, () -> new DijkstraAlgo(graph, "V1", "Vx"));
        assertEquals("Failed to find goal by name Vx", e.getMessage());
    }

    @Test
    void traverse_withIsolatedVertex_shouldThrowf() throws Exception {
        Graph testGraph = new Graph();
        Vert vert5 = testGraph.addVert("V5");
        Vert vert4 = testGraph.addVert("V4");
        Vert vert3 = testGraph.addVert("V3")
                .addEdgeTo(vert4, 2);
        Vert vert2 = testGraph.addVert("V2")
                .addEdgeTo(vert4, 1)
                .addEdgeTo(vert3,2);
        testGraph.addVert("V1")
                .addEdgeTo(vert2, 3)
                .addEdgeTo(vert3,4);
        DijkstraAlgo dijkstraAlgoWBadGoal = new DijkstraAlgo(testGraph, "V1", "V5");

        Exception badGraphException = assertThrows(Exception.class, dijkstraAlgoWBadGoal::traverse);

        assertEquals("Failed to find path with bad graph", badGraphException.getMessage());
    }

    @Test
    void traverse() throws Exception {
        Graph graph = Fixture.createTestGraph();

        classUnderTest = new DijkstraAlgo(graph, "V1", "V4");
        DijkstraAlgo.Solution solution = classUnderTest.traverse();

        assertNotNull(solution);
        assertEquals(4, solution.cost());
        assertEquals(2, solution.path().size());
        Vert vert1 = graph.findVertByName("V1").orElseThrow();
        Vert vert2 = graph.findVertByName("V2").orElseThrow();
        assertTrue(solution.path().contains(vert1));
        assertTrue(solution.path().contains(vert2));
    }

    @Test
    void getNextVert_ShouldReturnFirstVert() {
        DijkstraAlgo.DijVert nextV = classUnderTest.getNextVert().orElseThrow();
        assertEquals("V1", nextV.getGVert().getName());
    }

    @Test
    void gGetNextVert_allVisited_ShouldReturnEmpty() {
        classUnderTest.getDijVertices().forEach(v -> v.setVisited(true));
        Optional<DijkstraAlgo.DijVert> nextV = classUnderTest.getNextVert();
        assertTrue(nextV.isEmpty());
    }

}