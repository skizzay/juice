package io.nuvolo.juice.business.model;

import com.google.common.collect.ImmutableList;

import java.util.*;

public class Graph<T> {
    public record Edge<T>(T source, T target) {}
    private final Map<T, List<T>> nodes = new HashMap<>();

    public enum VisitationResult {
        CONTINUE,
        STOP
    }

    public interface VertexVisitor<T> {
        VisitationResult visit(T vertex);
        void visitationFinished(Map<T, T> predecessors);
    }

    public void addEdge(T source, T target) {
        nodes.computeIfAbsent(source, k -> new ArrayList<>()).add(target);
        nodes.computeIfAbsent(target, k -> new ArrayList<>());
    }

    public List<T> getNeighbors(T node) {
        return ImmutableList.copyOf(nodes.getOrDefault(node, List.of()));
    }

    public void breadthFirstVisitation(T root, VertexVisitor<T> visitor) {
        if (nodes.containsKey(root)) {
            breadthFirstVisitationImplementation(root, visitor);
        }
    }

    private void breadthFirstVisitationImplementation(T root, VertexVisitor<T> visitor) {
        final Queue<T> queue = new LinkedList<>();
        final Map<T, T> predecessors = new HashMap<>();

        predecessors.put(root, null);
        queue.add(root);
        while (!queue.isEmpty()) {
            final T vertex = queue.remove();
            if (visitor.visit(vertex) == VisitationResult.STOP) {
                break;
            } else {
                for (final T neighbor : getNeighbors(vertex)) {
                    if (!predecessors.containsKey(neighbor)) {
                        predecessors.put(neighbor, vertex);
                        queue.add(neighbor);
                    }
                }
            }
        }
        visitor.visitationFinished(predecessors);
    }

    public List<T> getShortestPath(T source, T target) {
        final List<T> path = new ArrayList<>();
        breadthFirstVisitation(source, new VertexVisitor<>() {
            @Override
            public VisitationResult visit(T vertex) {
                if (vertex.equals(target)) {
                    return VisitationResult.STOP;
                } else {
                    return VisitationResult.CONTINUE;
                }
            }
            @Override
            public void visitationFinished(Map<T, T> predecessors) {
                if (predecessors.containsKey(target)) {
                    T vertex = target;
                    while (vertex != null) {
                        path.add(vertex);
                        vertex = predecessors.get(vertex);
                    }
                    Collections.reverse(path);
                }
            }
        });
        return path;
    }
}
