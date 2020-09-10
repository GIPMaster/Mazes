package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
    }

    //returns a shortest paths tree containing the shortest path from start to end in given graph.
    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Set<V> known = new HashSet<>();
        Map<V, E> edgeTo = new HashMap<>();
        Map<V, Double> distTo = new HashMap<>();
        ExtrinsicMinPQ<V> perimeter = createMinPQ();

        perimeter.add(start, 0.0);
        distTo.put(start, 0.0);

        //Dijkstra's algorithm to find the shortest path
        while (!perimeter.isEmpty()) {
            V curr = perimeter.removeMin();
            if (curr == end || (curr != null && curr.equals(end))) {
                return edgeTo;
            }
            known.add(curr);

            //for each edge in the graph, check whether the current path is shorter than any previously found path, and
            //if it is, update distTo and edgeTo and the perimeter to reflect the current path
            for (E edge : graph.outgoingEdgesFrom(curr)) {
                double newDist = distTo.get(curr) + edge.weight();
                if (!known.contains(edge.to())) {
                    //if this object isn't in perimeter, add it to the perimeter, distTo and edgeTo
                    if (!perimeter.contains(edge.to())) {
                        perimeter.add(edge.to(), newDist);
                        distTo.put(edge.to(), newDist);
                        edgeTo.put(edge.to(), edge);
                    } else if (distTo.get(edge.to()) > newDist) {
                        perimeter.changePriority(edge.to(), newDist);
                        distTo.put(edge.to(), newDist);
                        edgeTo.put(edge.to(), edge);
                    }
                }
            }
        }
        return edgeTo;
    }

    //convert the inputted map to ShortestPath object
    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        E edge = spt.get(end);
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }

        List<E> edges = new LinkedList<E>();
        E curr = spt.get(end);
        while ((curr.from() == null && start != null) || (curr.from() != null && !curr.from().equals(start))) {
            edges.add(0, curr);
            curr = spt.get(curr.from());
        }
        edges.add(0, curr);

        return new ShortestPath.Success<>(edges);
    }

}
