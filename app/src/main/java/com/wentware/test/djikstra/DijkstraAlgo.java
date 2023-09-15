package com.wentware.test.djikstra;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.lang.System.out;
import static java.util.function.Predicate.not;

@Data
public class DijkstraAlgo {
    final String sourceName;
    final String goalName;
    final Graph graph;

    List<DijVert> dijVertices;
    DijVert dijSource;
    DijVert dijGoal;

    public DijkstraAlgo(Graph g, String sourceName, String goalName) throws Exception {
         this.graph = g;
         this.sourceName = sourceName;
         this.goalName = goalName;
         init();
    }

    private static Exception getNoSourceException(String sourceName) {
        return new Exception(String.format("Failed to find source by name %s", sourceName));
    }

    private static Exception getNoGoalException(String goalName) {
        return new Exception(String.format("Failed to find goal by name %s",goalName));
    }

    private void init() throws Exception {
        dijVertices = graph.getVertices()
                .stream()
                .map(DijVert::new)
                .toList();
        dijSource = findDijVertByGVert(graph.findVertByName(getSourceName()).orElseThrow(() -> getNoSourceException(getSourceName())));
        dijSource.setCostFromSource(0);
        dijGoal = findDijVertByGVert(graph.findVertByName(getGoalName()).orElseThrow(() -> getNoGoalException(getGoalName())));
    }

    public Solution traverse() throws Exception {

        while (true) {
            Optional<DijVert> currentVertOpt = getNextVert();

            if (currentVertOpt.isPresent()) {
                DijVert currentVert = currentVertOpt.get();
                if (!getDijGoal().equals(currentVert)) { // keep looking
                    visitAll(currentVert);
                } else {
                    //im done?
                    if (currentVert.getPrevDVert() == null) {
                        throw new Exception("Failed to find path with bad graph");
                    }
                    int costFromSource = currentVert.getCostFromSource();
                    out.println("Found " + currentVert.getGVert().getName() + " with weight " + costFromSource);
                    List<Vert> path = computePath(currentVert);
                    return new Solution(path, costFromSource);
                }
            } else {
                throw new Exception("Failed to compute");
            }

        }
    }

    Optional<DijVert> getNextVert() {
        //not yet visited, lowest
        return this.dijVertices
                .stream()
                .filter(not(DijVert::isVisited))
                .min(Comparator.comparingInt(DijVert::getCostFromSource));

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

    private void visitAll(DijVert fromVert) {
        for(Edge e : fromVert.getGVert().getEdges()) {
            DijVert toVert = findDijVertByGVert(e.getToVert());
            visit(toVert, fromVert, e.getWeight());
        }
        fromVert.setVisited(true);
    }

    private void visit(DijVert dijVert, DijVert fromVert, int edgeWeight) {
        if (fromVert != null) {
            int newCost = fromVert.getCostFromSource() + edgeWeight;
            if (newCost < dijVert.getCostFromSource()) {
                dijVert.updateCost(fromVert, newCost);
            }
        }
    }

    private DijVert findDijVertByGVert(Vert gVert) {
        return dijVertices.stream()
                .filter(dv -> dv.getGVert().equals(gVert))
                .findFirst()
                .orElseThrow();
    }

    @Data
    @RequiredArgsConstructor
    static class DijVert {
        final Vert gVert;
        DijVert prevDVert;
        int costFromSource = Integer.MAX_VALUE;
        boolean visited;

        private void updateCost(DijVert fromVert, int newWeight) {
            setCostFromSource(newWeight);
            setPrevDVert(fromVert);
        }
    }

    record Solution(List<Vert> path, int cost) {
    }

}
