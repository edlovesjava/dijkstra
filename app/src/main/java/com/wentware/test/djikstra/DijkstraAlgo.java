package com.wentware.test.djikstra;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

@Data
public class DijkstraAlgo {

    Logger log = LogManager.getLogger(DijkstraAlgo.class);
    final String sourceNodeId;
    final String goalNodeId;
    final Graph graph;

    List<DNode> dNodes;
    PriorityQueue<DNode> toVisit;
    List<DNode> visited;
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
        toVisit = new PriorityQueue<>(Comparator.comparingInt(DNode::getCostFromSource));
        dNodes = graph.getNodes()
                .stream()
                .map(DNode::new)
                .toList();
        sourceDNode = findDNodeByNode(graph.findNodeById(getSourceNodeId()).orElseThrow(() -> getNoSourceException(getSourceNodeId())));
        sourceDNode.setCostFromSource(0);
        goalDNode = findDNodeByNode(graph.findNodeById(getGoalNodeId()).orElseThrow(() -> getNoGoalException(getGoalNodeId())));
        this.toVisit.addAll(dNodes);
        this.visited = new ArrayList<>();

    }

    public Solution solve() throws Exception {

        while (true) {
            Optional<DNode> currentDNodeOpt = getNext();

            if (currentDNodeOpt.isPresent()) {
                DNode current = currentDNodeOpt.get();
                if (!getGoalDNode().equals(current)) { // keep looking
                    visitAll(current);
                } else {
                    //im done?
                    if (current.getPrevDNode() == null) {
                        String message = "Failed to find path with bad graph";
                        log.error(message);
                        throw new Exception(message);
                    }
                    int costFromSource = current.getCostFromSource();
                    log.info("Found goal " + current.getGNode().getId() + " with cost " + costFromSource + " from source " + getSourceNodeId());
                    List<Graph.Node> path = computePath(current);
                    return new Solution(path, costFromSource);
                }
            } else {
                String message = "Failed to compute";
                log.error(message);
                throw new Exception(message);
            }
        }
    }

    Optional<DNode> getNext() {
        //not yet visited, lowest
        return Optional.ofNullable(getToVisit().remove());
    }

    private List<Graph.Node> computePath(DNode dNode) {
        List<Graph.Node> path = new ArrayList<>();
        path.add(dNode.getGNode());
        DNode backDNode = dNode.getPrevDNode();
        do {
            path.add(backDNode.getGNode());
            backDNode = backDNode.getPrevDNode();
        } while (backDNode != null);
        Collections.reverse(path);
        return path;
    }

    private void visitAll(DNode fromDNode) {
        for(Graph.Edge e : fromDNode.getGNode().getEdges()) {
            DNode toDNode = findDNodeByNode(e.getToNode());
            visit(fromDNode, toDNode, e.getWeight());
        }
        visited.add(fromDNode);
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

        private void updateCost(DNode fromDNode, int newWeight) {
            setCostFromSource(newWeight);
            setPrevDNode(fromDNode);
        }
    }

    record Solution(List<Graph.Node> path, int cost) {
    }

}
