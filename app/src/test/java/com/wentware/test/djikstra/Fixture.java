package com.wentware.test.djikstra;

public final class Fixture {
    static final String[] GRAPH_DEF = {
            "V1,V2,3",
            "V1,V3,4",
            "V2,V3,2",
            "V2,V4,1",
            "V3,V4,2"
    };

    private Fixture() {
        //utility class, static members only
    }

    public static Graph createTestGraph() {
        Graph testGraph = new Graph();
        Vert vert4 = testGraph.addVert("V4");
        Vert vert3 = testGraph.addVert("V3")
                .addEdgeTo(vert4, 2);
        Vert vert2 = testGraph.addVert("V2")
                .addEdgeTo(vert4, 1)
                .addEdgeTo(vert3,2);
        testGraph.addVert("V1")
                .addEdgeTo(vert2, 3)
                .addEdgeTo(vert3,4);
        return testGraph;
    }
}
