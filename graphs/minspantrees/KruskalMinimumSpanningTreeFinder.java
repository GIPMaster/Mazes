package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.QuickFindDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new QuickFindDisjointSets<>();
    }

    //finds the minimum spanning tree on a graph using Kruskal's algorithm
    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        List<E> edgesMST = new ArrayList<>();
        List<E> edges = new ArrayList<>(graph.allEdges());

        //sorts the edges from lowest value to highest
        edges.sort(Comparator.comparingDouble(E::weight));

        DisjointSets<V> disjointSets = createDisjointSets();

        int numVertices = 0;

        //makes a separate disjoint set for each vertex
        for (V vertex : graph.allVertices()) {
            disjointSets.makeSet(vertex);
            numVertices++;
        }

        //for each edge, check if the two vertices are already connected and if not, add the edge into the MST
        for (int i = 0; (!edges.isEmpty()) && (edgesMST.size() < numVertices - 1); i++) {
            E curr = edges.remove(0);
            if (disjointSets.findSet(curr.from()) != (disjointSets.findSet(curr.to()))) {
                disjointSets.union(curr.from(), curr.to());
                edgesMST.add(curr);
            }
        }
        //if the number of edges is less than the number of vertices minus 1, then two vertices are not connected,
        //so a MST does not exist
        if (edgesMST.size() < numVertices - 1) {
            return new MinimumSpanningTree.Failure<>();
        }
        return new MinimumSpanningTree.Success<>(edgesMST);
    }
}
