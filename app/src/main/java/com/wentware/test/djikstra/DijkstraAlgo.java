package com.wentware.test.djikstra;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.System.out;
import static java.util.function.Predicate.not;

@Data
public class DijkstraAlgo {
    private final String sourceName;
    private final String goalName;
    Graph graph;
    List<DijVert> dijVertices;
    DijVert dijSource;
    DijVert dijGoal;

    public DijkstraAlgo(Graph g, String sourceName, String goalName) {
         this.graph = g;
         this.sourceName = sourceName;
         this.goalName = goalName;
         init();
    }

    void init() {
        dijVertices = graph.getVertices()
                .stream()
                .map(DijVert::new)
                .toList();
        Vert source = graph.findVertByName(getSourceName()).orElseThrow();
        Vert goal = graph.findVertByName(getGoalName()).orElseThrow();
        dijSource = getDijVert(source);
        dijSource.setCostFromSource(0);
        dijGoal = getDijVert(goal);
    }

    private DijVert getDijVert(Vert source) {
        return dijVertices.stream()
                .filter(dv -> dv.getGVert().equals(source))
                .findFirst()
                .orElseThrow();
    }

    @Data
    static class DijVert {
        Vert gVert;
        DijVert prevDVert;
        int costFromSource = Integer.MAX_VALUE;
        boolean visited;

        DijVert(Vert gVert) {
            this.gVert = gVert;
        }
    }

    record Solution(List<Vert> path, int cost) {
    }

    public Solution traverse() {
        int bestCost = 0;
        List<Vert> path = new ArrayList<>();
        boolean done = false;
        while (!done) {
            DijVert currentVert = getNextVert();
            out.println("Visiting vert " + currentVert.getGVert().getName());
            if (!getDijGoal().equals(currentVert)) { // keep looking
                visitAll(currentVert);
            } else {
                //im done
                bestCost = currentVert.getCostFromSource();
                out.println("Found " + currentVert.getGVert().getName() + " with weight " + currentVert.getCostFromSource());
                done = true;
                path = computePath(currentVert);

            }

        }
        return new Solution(path, bestCost);
    }

    private List<Vert> computePath(DijVert currentVert) {
        List<Vert> path = new ArrayList<>();
        DijVert backVert = currentVert.getPrevDVert();
        do {
            out.println("Came from vert " + backVert.getGVert().getName());
            path.add(backVert.getGVert());
            backVert = backVert.getPrevDVert();
        } while (backVert != null);
        return path;
    }

    void visitAll(DijVert fromVert) {
        for(Edge e : fromVert.getGVert().getEdges()) {
            DijVert toVert = getDijVert(e.getToVert());
            visit(toVert, fromVert, e.getWeight());
        }
        fromVert.setVisited(true);
    }

    void visit(DijVert dijVert, DijVert fromVert, int edgeWeight) {
        if (fromVert != null) {
            int newWeight = fromVert.getCostFromSource() + edgeWeight;
            if (newWeight < dijVert.getCostFromSource()) {
                dijVert.setCostFromSource(newWeight);
                dijVert.setPrevDVert(fromVert);
            }
        }
    }

    public DijVert getNextVert() {
        //not yet visited, lowest
        return this.dijVertices
                .stream()
                .filter(not(DijVert::isVisited))
                .min(Comparator.comparingInt(DijVert::getCostFromSource))
                .orElseThrow();

    }

}
