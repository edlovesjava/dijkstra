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
    final String sourceNodeId;
    final String goalNodeId;
    final Graph graph;

    List<DNode> dNodes;
    DNode sourceDNode;
    DNode goalDNode;

    public DijkstraAlgo(Graph g, String sourceNodeId, String goalNodeId) throws Exception {
         this.graph = g;
         this.sourceNodeId = sourceNodeId;
         this.goalNodeId = goalNodeId;
         init();
    }

    private static Exception getNoSourceException(String nodeId) {
        return new Exception(String.format("Failed to find source by name %s", nodeId));
    }

    private static Exception getNoGoalException(String nodeId) {
        return new Exception(String.format("Failed to find goal by name %s",nodeId));
    }

    private void init() throws Exception {
        dNodes = graph.getNodes()
                .stream()
                .map(DNode::new)
                .toList();
        sourceDNode = findDNodeByNode(graph.findNodeById(getSourceNodeId()).orElseThrow(() -> getNoSourceException(getSourceNodeId())));
        sourceDNode.setCostFromSource(0);
        goalDNode = findDNodeByNode(graph.findNodeById(getGoalNodeId()).orElseThrow(() -> getNoGoalException(getGoalNodeId())));
    }

    public Solution traverse() throws Exception {

        while (true) {
            Optional<DNode> currentDNodeOpt = getNext();

            if (currentDNodeOpt.isPresent()) {
                DNode current = currentDNodeOpt.get();
                if (!getGoalDNode().equals(current)) { // keep looking
                    visitAll(current);
                } else {
                    //im done?
                    if (current.getPrevDNode() == null) {
                        throw new Exception("Failed to find path with bad graph");
                    }
                    int costFromSource = current.getCostFromSource();
                    out.println("Found " + current.getGNode().getId() + " with weight " + costFromSource);
                    List<Graph.Node> path = computePath(current);
                    return new Solution(path, costFromSource);
                }
            } else {
                throw new Exception("Failed to compute");
            }

        }
    }

    Optional<DNode> getNext() {
        //not yet visited, lowest
        return this.dNodes
                .stream()
                .filter(not(DNode::isVisited))
                .min(Comparator.comparingInt(DNode::getCostFromSource));

    }

    private List<Graph.Node> computePath(DNode dNode) {
        List<Graph.Node> path = new ArrayList<>();
        DNode backDNode = dNode.getPrevDNode();
        do {
            path.add(backDNode.getGNode());
            backDNode = backDNode.getPrevDNode();
        } while (backDNode != null);
        return path;
    }

    private void visitAll(DNode fromDNode) {
        for(Graph.Edge e : fromDNode.getGNode().getEdges()) {
            DNode toDNode = findDNodeByNode(e.getToNode());
            visit(fromDNode, toDNode, e.getWeight());
        }
        fromDNode.setVisited(true);
    }

    private void visit(DNode fromDNode, DNode toDNode, int edgeWeight) {
        if (fromDNode != null) {
            int newCost = fromDNode.getCostFromSource() + edgeWeight;
            if (newCost < toDNode.getCostFromSource()) {
                toDNode.updateCost(fromDNode, newCost);
            }
        }
    }

    private DNode findDNodeByNode(Graph.Node gNode) {
        return dNodes.stream()
                .filter(dv -> dv.getGNode().equals(gNode))
                .findFirst()
                .orElseThrow();
    }

    @Data
    @RequiredArgsConstructor
    static class DNode {
        final Graph.Node gNode;
        DNode prevDNode;
        int costFromSource = Integer.MAX_VALUE;
        boolean visited;

        private void updateCost(DNode fromDNode, int newWeight) {
            setCostFromSource(newWeight);
            setPrevDNode(fromDNode);
        }
    }

    record Solution(List<Graph.Node> path, int cost) {
    }

}
