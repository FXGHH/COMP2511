package unsw.graph;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Comparator;

public class DepthFirstGraphIterator<N extends Comparable<N>> implements Iterator<N> {
    Stack<N> stack = new Stack<N>();
    List<N> visited = new ArrayList<N>();
    private Graph<N> g;
    private N currNode;

    public DepthFirstGraphIterator(Graph<N> g, N startingNode) {
        this.g = g;
        currNode = startingNode;
        stack.push(startingNode);
    }   

    @Override
    public boolean hasNext() {
        return (!visited.containsAll(g.getAdjacentNodes(currNode))) || 
                !stack.isEmpty();
    }

    @Override
    public N next() {
        if(!stack.isEmpty()) {
            N vertex = stack.pop();
            currNode = vertex;
            visited.add(vertex);
            if (this.hasNext()) {
                List<N> nextVertices = g.getAdjacentNodes(vertex);

                nextVertices.sort(new Comparator<N>() {
                    @Override
                    public int compare(N o1, N o2) {
                        return o1.compareTo(o2);
                    }
                });

                nextVertices.removeAll(visited);
                nextVertices.removeAll(stack);
                for (N node : nextVertices) {
                    stack.push(node);
                }
            }
            return vertex;
        }
        return null;
    }
}