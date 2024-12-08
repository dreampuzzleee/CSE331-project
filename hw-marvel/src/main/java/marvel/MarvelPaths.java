package marvel;

import graph.GraphDesign;

import java.util.*;

/**
 * <b>MarvelPaths</b> represents the graph by using file's data
 * and finding the path between two nodes.
 */

public class MarvelPaths {

    /**
     * Creates new graph based on the file <var>filename</var>
     *
     * @param result the new graph
     * @param filename the file to convert to new graph
     * @throws IllegalArgumentException if <var>filename </var> is null
     */
    public static void loadGraph(GraphDesign<String, String> result, String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }

        Map<String, List<String>> books = MarvelParser.parseData(filename);

        // get all character into a list
        for (String b : books.keySet()) {
            for(String c : books.get(b)) {
                result.addNode(c);
            }
        }

        for (String book : books.keySet()) {
            // get the list of character of this book
            List<String> character = books.get(book);
            for (int i = 0; i < character.size(); i++) {
                for (int j = i +1; j < character.size(); j++) {
                    result.addEdge(character.get(i), character.get(j), book);
                    result.addEdge(character.get(j), character.get(i), book);
                }
            }
        }
    }

    /**
     * Finds the shortest path from node_a <var>start</var> to node_b <var>end</var> in the graph
     *
     * @param graph the existing graph
     * @param start the starting node to find path
     * @param end the end node to find path
     * @throws IllegalArgumentException if any <var>start</var>, <var>end</var>, <var>graph</var> is
     * null or the graph doesn't contain node<var>start</var> and <var>end</var>
     * @return list of labeled edges
     */
    public static List<GraphDesign<String, String>.LabelEdge> findPath(GraphDesign<String, String> graph, String start, String end) {
        if (start == null || end == null || graph == null) {
            throw new IllegalArgumentException("Has Null value");
        }

        Map<String, List<GraphDesign<String, String>.LabelEdge>> edgeTo = new HashMap<>();
        Queue<String> contents = new LinkedList<>();

        edgeTo.put(start, new ArrayList<>());
        contents.add(start);
        while (!contents.isEmpty()) {
            String from = contents.remove();
            if (from.equals(end)) {
                return edgeTo.get(from);
            } else {
                List<GraphDesign<String, String>.LabelEdge> edges = graph.getEdges(from);
                edges.sort(new EdgeComparator());
                for (GraphDesign<String, String>.LabelEdge e : edges) {
                    String to = e.getDestination();
                    if (!edgeTo.containsKey(to)) {
                        edgeTo.put(to, new ArrayList<>(edgeTo.get(from)));
                        edgeTo.get(to).add(e);
                        contents.add(to);
                    }
                }
            }
        }
        return null;
    }

    /**
     * <var>EdgeComparator</var> represents a class for comparing two edges
     */
    private static class EdgeComparator implements Comparator<GraphDesign<String, String>.LabelEdge> {
        /**
         * Compare two edges' end node and label to order, first order by destination,
         * if same then order by label
         *
         * @param o1 first edge
         * @param o2 second edge
         * @return negative, positive integer or zero if the first
         * less than, equal to, or greater than the second
         */
        public int compare(GraphDesign<String, String>.LabelEdge o1, GraphDesign<String, String>.LabelEdge o2) {
            if(!o1.getDestination().equals(o2.getDestination())) {
                return o1.getDestination().compareTo(o2.getDestination());
            } else if (!o1.getLabel().equals(o2.getLabel())) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
            return 0;
        }
    }

    /**
     * Allows user to find path of two characters based on their type for the character
     *
     * @param args an array of command line arguments for the application
     */
    public static void main(String[] args) {
        GraphDesign<String, String> marvelGraph = new GraphDesign<>();
        loadGraph(marvelGraph, "marvel.csv");
        boolean reply = true;

        System.out.println("--> Find the path between two characters in Marvel");
        Scanner reader = new Scanner(System.in);

        while (reply) {
            System.out.println("Please type first character in upper case");
            System.out.println("PS: use \"-\" instead of space");
            String start = reader.nextLine();
            System.out.println("Please type second character in upper case");
            System.out.println("PS: use \"-\" instead of space");
            String end = reader.nextLine();

            if (!marvelGraph.hasNode(start) && !marvelGraph.hasNode(end)) {
                System.out.println("unknown: " + start);
                System.out.println("unknown: " + end);
                System.out.println("Sorry, I don't have these character");
            } else if (!marvelGraph.hasNode(start)) {
                System.out.println("unknown: " + start);
                System.out.println("Sorry, I don't have this character");
            }else if (!marvelGraph.hasNode(end)) {
                System.out.println("unknown: " + end);
                System.out.println("Sorry, I don't have this character");
            } else {
                String result = "path from " + start + " to " + end + ":";
                List<GraphDesign<String, String>.LabelEdge> path = MarvelPaths.findPath(marvelGraph, start, end);
                if (path == null) {
                    System.out.println(result);
                    System.out.println("Sorry, no path found");
                }else if (path.isEmpty()) {
                    System.out.println(result);
                    System.out.println("Sorry...it's empty");
                } else {
                    System.out.println(result);
                    for (GraphDesign<String, String>.LabelEdge e : path) {
                        System.out.println(e.getStart() + " to " + e.getDestination() + " via " + e.getLabel());
                    }
                }
            }

            System.out.println("Do you want play again? (yes/no)");
            String replying = reader.nextLine();
            if (replying.equals("no")) {
                reply = false;
            }
        }
        System.out.println();
        System.out.println("Thanks for using, goodbye!");
    }
}
