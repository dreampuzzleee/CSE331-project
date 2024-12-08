package graph;

import java.util.*;

/**
 * <b>GraphDesign</b> represents a generic, mutable graph which contains nodes and edges.
 *
 * Specification fields:
 * @spec.specfield nodes: N
 * @spec.specfield edges: E
 */

public class GraphDesign<N, E> {
    public static final boolean DEBUG = false;

    private Map<N,List<LabelEdge>> graph;

    //Rep invariant: graph != null && all items(nodes and labeled edges) in graph != null
    //&& all nodes are unique in the key set of graph
    //Abstract function:
    //      AF(this) = a graph is a directly graph with a map where having
    //      unique nodes and out-going edges
    //      A graph d such that {} if d is an empty graph;
    //      if a is a node in graph without any edges, then {a = []};
    //      The graph be like {a = [edge1, edge2,...], b = [...], ...} such that
    //      a, b, ect. are nodes with edges, respectively

    /**
     * Constructs a new graph
     *
     * @spec.effects Constructs a new graph, "0".
     */
    public GraphDesign() {
        graph = new HashMap<>();
    }

    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert graph != null : "Graph can not be null";
        if (DEBUG) {
            Set<N> nodes = graph.keySet();
            for (N n : nodes) {
                assert (n != null) : "Node is null";
                for (int i = 0; i < graph.get(n).size(); i++) {
                    assert (graph.get(n).get(i) != null);
                    for(int j = i+1; j < graph.get(n).size(); j++) {
                        assert (!graph.get(n).get(i).equals(graph.get(n).get(j)));
                    }
                }
            }
        }
    }

    /**
     * Returns true if adding node <var>n</var> to the graph if the graph doesn't contain before
     *
     * @param n a type of node to be added
     * @spec.requires n != null
     * @spec.modifies this.graph
     * @spec.effects adds node <var>n</var> to this.nodes if doesn't contain before
     * @return true if adding node to the graph successfully
     */
    public boolean addNode(N n) {
        checkRep();
        if (n == null || graph.containsKey(n)) {
            return false;
        }
        graph.put(n, new ArrayList<>());
        return true;
    }

    /**
     * Returns true if adding edge from node <var>from</var> to node <var>to</var> with
     * label <var>label</var> into the graph  if the graph doesn't contain before
     *
     * @param from a type of node to be added a edge with as source
     * @param to a type of node to be added a edge with as destination
     * @param label a type of label of the edge
     * @spec.requires from != null, to != null, label != null
     * @spec.modifies this.graph
     * @spec.effects adds edge from node <var>from</var> to node <var>to</var> with the
     * label <var>label</var> into this.nodes if doesn't contain before
     * @return true if adding edge in the graph successfully
     */
    public boolean addEdge(N from, N to, E label) {
        checkRep();
        if (!graph.containsKey(from) || !graph.containsKey(to)){
            return false;
        }

        LabelEdge temp = new LabelEdge(from, to, label);

        if (graph.get(from).contains(temp)) {
            return false;
        }
        graph.get(from).add(temp);
        checkRep();
        return true;
    }

    /**
     * Returns true if the graph has the node <var>n</var>
     *
     * @param n a type of node
     * @throws IllegalArgumentException if <var>n</var> is null
     * @return true if the node exists in the graph
     */
    public boolean hasNode(N n) {
        checkRep();
        if (n == null) {
            throw new IllegalArgumentException("Node can not be null");
        }
        checkRep();
        return graph.containsKey(n);
    }

    /**
     * Gets list of all the type of nodes in the graph
     *
     * @return the list of all nodes in the graph
     */
    public List<N> listNodes() {
        checkRep();
        Set<N> temp = graph.keySet();
        return new ArrayList<>(temp);
    }

    /**
     * Gets list of edges of a type of node <var>n</var> as parent node
     *
     * @param n a type of parent node
     * @throws IllegalArgumentException if node <var>n</var> isn't in this.graph
     * or node <var>n</var> is null
     * @return list of LabelEdge represents children of node <var>n</var> with corresponding edge
     */
    public List<LabelEdge> getEdges(N n) {
        checkRep();
        if (n == null) {
            throw new IllegalArgumentException("Node is null");
        }
        if (!graph.containsKey(n)) {
            throw new IllegalArgumentException("Graph doesn't contain this node");
        }
        List<LabelEdge> temp = graph.get(n);
        return new ArrayList<>(temp);
    }

    /**
     * Gets list of all children nodes of a type of parent node <var>n</var>
     *
     * @param n a type of parent node
     * @throws IllegalArgumentException if <var>n</var> is null
     * @return list of all children nodes
     */
    public List<N> childrenOf(N n) {
        checkRep();
        if (n == null) {
            throw new IllegalArgumentException();
        }
        List<N> temp = new ArrayList<>();
        for (LabelEdge e : graph.get(n)) {
            temp.add(e.getDestination());
        }
        return temp;
    }


    /**
     * Returns true if  the graph is empty
     *
     * @return true if the graph is empty
     */
    public boolean isEmpty() {
        checkRep();
        return graph.isEmpty();
    }

    /**
     * Gets the total number of nodes in the graph
     *
     * @return the number of nodes in the graph
     */
    public int countNode() {
        checkRep();
        Set<N> nodes = graph.keySet();
        return nodes.size();
    }

    /**
     * Gets the total number of edges in the graph
     *
     * @return the number of edges in the graph
     */
    public int countEdge() {
        checkRep();
        int num = 0;
        Set<N> nodes = graph.keySet();
        for (N n : nodes) {
            List<LabelEdge> temp = graph.get(n);
            num += temp.size();
        }
        return num;
    }

    /**
     * This inner class represents outgoing edge with label and
     * a node as destination of this edge
     *
     * Specification field:
     * @spec.specfield destination node: N
     * @spec.specfield label: E
     */
    public class LabelEdge {

        //Rep invariant: start != null && label != null && dest != null
        //Abstraction function:
        //    AF(this) = a labeled edge node1 with node1.from = this.start
        //    node1.label = this.label
        //    and node1.to = this.dest

        private final N start;
        private final N dest;
        private final E label;

        /**
         * Throws an exception if the representation invariant is violated.
         */
        private void checkRep() {
            assert (start != null);
            assert (label != null);
            assert (dest != null);
        }

        /**
         * Creates a new labeled edge
         *
         * @spec.requires from != null, destination != null, l != null
         * @spec.effects Constructs a new labeled edge with start node <var>from</var>
         * destination node <var>destination</var> and label <var>l</var>
         * @param from a type of start node of edge
         * @param destination a type of destination node of edge
         * @param l a type of label of edge
         */
        public LabelEdge(N from, N destination, E l) {
            if (from == null || destination == null || l == null) {
                throw new IllegalArgumentException();
            }
            start = from;
            dest = destination;
            label = l;
            checkRep();
        }

        /**
         * Gets a type of start node of the edge
         *
         * @return a type of start node of the edge
         */
        public N getStart() {
            checkRep();
            return start;
        }

        /**
         * Gets a type of label of the edge
         *
         * @return a type of label of the edge
         */
        public E getLabel() {
            checkRep();
            return label;
        }

        /**
         * Gets a type of destination node of the edge
         *
         * @return a type of destination node of the edge
         */
        public N getDestination() {
            checkRep();
            return dest;
        }

        /**
         * Checks whether two object are equal
         *
         * @param o the given object be checked
         * @return true if two objects are equal
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            GraphDesign<?,?>.LabelEdge o2 = (GraphDesign<?, ?>.LabelEdge) o;
            return this.start.equals(o2.start)&& this.label.equals(o2.label) && this.dest.equals(o2.dest);
        }

        @Override
        public int hashCode() {
            return this.label.hashCode() + this.dest.hashCode() + this.start.hashCode();
        }
    }
}
