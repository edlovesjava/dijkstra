package com.wentware.test.djikstra;

public final class Fixture {
    static final String[] GRAPH_DEF = {
            "N1,N2,3",
            "N1,N3,4",
            "N2,N3,2",
            "N2,N4,1",
            "N3,N4,2"
    };

    private Fixture() {
        //utility class, static members only
    }

    public static Graph createTestGraph() {
        Graph testGraph = new Graph();
        Graph.Node n4 = testGraph.addNode("N4");
        Graph.Node n3 = testGraph.addNode("N3")
                .addEdgeTo(n4, 4);
        Graph.Node n2 = testGraph.addNode("N2")
                .addEdgeTo(n4, 2)
                .addEdgeTo(n3,1);
        testGraph.addNode("N1")
                .addEdgeTo(n2, 3)
                .addEdgeTo(n3,2);
        return testGraph;
    }
}
