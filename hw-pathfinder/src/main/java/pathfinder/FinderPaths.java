package pathfinder;

import graph.GraphDesign;
import pathfinder.datastructures.Path;
import java.util.*;

/**
 * <b>FinderPaths</b> represents finding shortest the path between two nodes.
 */
public class FinderPaths {

    /**
     * Finds the shortest path from node_a <var>start</var> to node_b <var>end</var> in the <var>graph</var>
     *
     * @param graph the existing graph
     * @param start a type of starting node to find path
     * @param end a type of end node to find path
     * @param <N> a generic type
     * @spec.requires graph != null, start != null, and end != null
     * @throws IllegalArgumentException the graph doesn't contain node<var>start</var> and <var>end</var>
     * @return list of a type of edges
     */
    public static <N> Path<N> findShortestPath(GraphDesign<N, Double> graph, N start, N end) {
        if (graph == null || start == null || end == null) {
            throw new IllegalArgumentException();
        }

        if (!graph.hasNode(start) || !graph.hasNode(end)) {
            throw new IllegalArgumentException();
        }

        Set<N> finished = new HashSet<>();
        Queue<Path<N>> active = new PriorityQueue<>(Comparator.comparingDouble(Path::getCost));

        active.add(new Path<>(start));
        while (!active.isEmpty()) {
            Path<N> minPath = active.poll();
            N minDest = minPath.getEnd();
            if (minDest.equals(end)) {
                return minPath;
            }
            if (finished.contains(minDest)) {
                continue;
            }

            List<GraphDesign<N,Double>.LabelEdge> edges = graph.getEdges(minDest);
            for (GraphDesign<N, Double>.LabelEdge e : edges) {
                if (!finished.contains(e.getDestination())) {
                    Path<N> newPath = minPath.extend(e.getDestination(), e.getLabel());
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
        return null;
    }
}
