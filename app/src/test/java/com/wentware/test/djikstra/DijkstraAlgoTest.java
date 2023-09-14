package com.wentware.test.djikstra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DijkstraAlgoTest {

    DijkstraAlgo classUnderTest;

    @BeforeEach
    void setup() {
        Graph graph = Fixture.createTestGraph();
        classUnderTest = new DijkstraAlgo(graph, "V1", "V4");
    }

    @Test
    void traverse() {
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
    void testGetNextVert_SouldReturnFirstVert() {
        DijkstraAlgo.DijVert nextV = classUnderTest.getNextVert();
        assertEquals("V1", nextV.getGVert().getName());
    }

}