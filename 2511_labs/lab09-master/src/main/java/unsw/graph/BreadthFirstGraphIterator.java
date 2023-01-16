package unsw.graph;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class BreadthFirstGraphIterator<N extends Comparable<N>> implements Iterator<N> {
    Queue<N> queue = new ArrayDeque<>();
    List<N> visited = new ArrayList<N>();
    private N currNode;
    private Graph<N> g;

    public BreadthFirstGraphIterator(Graph<N> g, N startingNode) {
        this.g = g;
        currNode = startingNode;
        queue.add(startingNode);
    }   

    @Override
    public boolean hasNext() {
        return (!visited.containsAll(g.getAdjacentNodes(currNode))) || 
                !queue.isEmpty();
    }

    @Override
    public N next() {
        if (queue.size() != 0) {
            N vertex = queue.poll();
            currNode = vertex;
            visited.add(vertex);
            if (this.hasNext()) {
                List<N> nextVertices = g.getAdjacentNodes(vertex);
                nextVertices.removeAll(visited);
                nextVertices.removeAll(queue);
                queue.addAll(nextVertices);
            }
            return vertex;
        }
        return null;
    }
}