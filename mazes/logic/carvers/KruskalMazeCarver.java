package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    //randomly weights walls to find a path through the maze
    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        Collection<EdgeWithData<Room, Wall>> edges = new HashSet<>();
        for (Wall wall : walls) {
            edges.add(new EdgeWithData<>(wall.getRoom1(), wall.getRoom2(), rand.nextDouble(), wall));
        }

        edges = this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges)).edges();

        //adds the information to a HashSet to return the walls to remove
        Set<Wall> wallsToRemove = new HashSet<>();
        for (EdgeWithData<Room, Wall> edge : edges) {
            wallsToRemove.add(edge.data());
        }
        return wallsToRemove;
    }
}
