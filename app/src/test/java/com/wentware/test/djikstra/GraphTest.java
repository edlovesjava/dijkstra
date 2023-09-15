package com.wentware.test.djikstra;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void addNOdeById_withDup_shouldNotAddDup() {
        Graph graph = new Graph();
        graph.addNode("V1");
        graph.addNode("V1");

        assertEquals(1, graph.getNodes().size());
    }

    @Test
    void findNodeById() {
        Graph graph = new Graph();
        graph.addNode("V1");
        graph.addNode("V2");

        assertTrue(graph.findNodeById("V1").isPresent());
        assertNotNull(graph.findNodeById("V2").orElseThrow());
        assertTrue(graph.findNodeById("V3").isEmpty());
    }
}