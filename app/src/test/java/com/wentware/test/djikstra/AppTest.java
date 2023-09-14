/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.wentware.test.djikstra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    private App classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new App();
    }

    @Test
    void parseGoodData() throws Exception {
        String[] args = Fixture.GRAPH_DEF;

        Graph g = classUnderTest.parse(args);
        assertNotNull(g);
        assertEquals(4, g.getVertices().size());
        Vert v1 = g.findVertByName("V1").orElseThrow();
        assertEquals(2, v1.getEdges().size());
        Vert v2 = g.findVertByName("V2").orElseThrow();
        assertEquals(2, v2.getEdges().size());
        Vert v3 = g.findVertByName("V3").orElseThrow();
        assertEquals(1, v3.getEdges().size());
        assertNotNull(v3.getEdges().stream().filter(e -> "V4".equals(e.getToVert().getName())).findFirst());
    }

    @Test
    void run() {

        DijkstraAlgo algo = createAlgo();
        DijkstraAlgo.Solution solution = algo.traverse();
        out.println("Cost was "+solution.cost());
        out.println("Path was "+solution.path());

        assertNotNull(solution);
        assertEquals(4,solution.cost());
        assertEquals(2, solution.path().size());
        Graph graph = algo.getGraph();
        Vert v1 = graph.findVertByName("V1").orElseThrow();
        Vert v2 = graph.findVertByName("V2").orElseThrow();
        assertTrue(solution.path().contains(v1));
        assertTrue(solution.path().contains(v2));
    }

    private DijkstraAlgo createAlgo() {
        Graph graph = Fixture.createTestGraph();
        return new DijkstraAlgo(graph,"V1", "V4");
    }
}
