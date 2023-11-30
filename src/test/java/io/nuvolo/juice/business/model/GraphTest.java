package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    private record Node(String name) {}

    private final List<Graph.Edge<Node>> edges = new ArrayList<>();

    private Graph<Node> createGraph() {
        return new Graph<>(edges);
    }

    @Test
    public void getNeighbors_whenEmptyGraph_thenEmptyList() {
        // Given
        final Graph<Node> graph = createGraph();

        // When
        final List<Node> neighbors = graph.getNeighbors(new Node("A"));

        // Then
        assertTrue(neighbors.isEmpty());
    }

    @Test
    public void getNeighbors_whenSingleNode_thenListIsNotEmpty() {
        // Given
        edges.add(new Graph.Edge<>(new Node("A"), new Node("A")));
        final Graph<Node> graph = createGraph();

        // When
        final List<Node> neighbors = graph.getNeighbors(new Node("A"));

        // Then
        assertFalse(neighbors.isEmpty());
    }

    @Test
    public void getNeighbors_whenSingleEdge_thenTargetNode() {
        // Given
        edges.add(new Graph.Edge<>(new Node("A"), new Node("B")));
        final Graph<Node> graph = createGraph();

        // When
        final List<Node> neighbors = graph.getNeighbors(new Node("A"));

        // Then
        assertEquals(List.of(new Node("B")), neighbors);
    }

    @Test
    public void getNeighbors_whenMultipleEdges_thenTargetNodes() {
        // Given
        edges.add(new Graph.Edge<>(new Node("A"), new Node("B")));
        edges.add(new Graph.Edge<>(new Node("A"), new Node("C")));
        final Graph<Node> graph = createGraph();

        // When
        final List<Node> neighbors = graph.getNeighbors(new Node("A"));

        // Then
        assertEquals(List.of(new Node("B"), new Node("C")), neighbors);
    }

    @Test
    public void getShortestPath_whenEmptyGraph_thenEmptyList() {
        // Given
        final Graph<Node> graph = createGraph();

        // When
        final List<Node> path = graph.getShortestPath(new Node("A"), new Node("B"));

        // Then
        assertTrue(path.isEmpty());
    }

    @Test
    public void getShortestPath_whenSingleNode_thenEmptyList() {
        // Given
        edges.add(new Graph.Edge<>(new Node("A"), new Node("A")));
        final Graph<Node> graph = createGraph();

        // When
        final List<Node> path = graph.getShortestPath(new Node("A"), new Node("B"));

        // Then
        assertTrue(path.isEmpty());
    }

    @Test
    public void getShortestPath_whenSingleEdge_thenTargetNode() {
        // Given
        edges.add(new Graph.Edge<>(new Node("A"), new Node("B")));
        final Graph<Node> graph = createGraph();

        // When
        final List<Node> path = graph.getShortestPath(new Node("A"), new Node("B"));

        // Then
        assertEquals(List.of(new Node("A"), new Node("B")), path);
    }

    @Test
    public void getShortestPath_noPathAvailable_thenEmptyList() {
        // Given
        edges.add(new Graph.Edge<>(new Node("A"), new Node("B")));
        final Graph<Node> graph = createGraph();

        // When
        final List<Node> path = graph.getShortestPath(new Node("B"), new Node("A"));

        // Then
        assertTrue(path.isEmpty());
    }

    @Test
    public void getShortestPath_withMultiplePathsAvailable_thenPathWithLeastNodesIsReturned() {
        // Given
        edges.add(new Graph.Edge<>(new Node("A"), new Node("B")));
        edges.add(new Graph.Edge<>(new Node("B"), new Node("C")));
        edges.add(new Graph.Edge<>(new Node("C"), new Node("D")));
        edges.add(new Graph.Edge<>(new Node("B"), new Node("D")));
        final Graph<Node> graph = createGraph();

        // When
        final List<Node> path = graph.getShortestPath(new Node("A"), new Node("D"));

        // Then
        assertEquals(List.of(new Node("A"), new Node("B"), new Node("D")), path);
    }
}