package com.wentware.test.djikstra;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void addVertByName_withDup_shouldNotAddDup() {
        Graph graph = new Graph();
        graph.addVert("V1");
        graph.addVert("V1");

        assertEquals(1, graph.getVertices().size());
    }

    @Test
    void findVertByName() {
        Graph graph = new Graph();
        graph.addVert("V1");
        graph.addVert("V2");

        assertTrue(graph.findVertByName("V1").isPresent());
        assertNotNull(graph.findVertByName("V2").orElseThrow());
        assertTrue(graph.findVertByName("V3").isEmpty());
    }
}