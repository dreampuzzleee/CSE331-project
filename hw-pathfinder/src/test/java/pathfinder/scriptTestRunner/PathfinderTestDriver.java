/*
 * Copyright (C) 2022 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.GraphDesign;
import pathfinder.FinderPaths;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    private final PrintWriter output;
    private final BufferedReader input;
    private final Map<String, GraphDesign<String, Double>> graphs = new HashMap<>();

    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new GraphDesign<>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        GraphDesign<String, Double> temp = graphs.get(graphName);
        temp.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, Double.parseDouble(edgeLabel));
    }

    private void addEdge(String graphName, String parentName, String childName,
                         double edgeLabel) {
        GraphDesign<String, Double> temp = graphs.get(graphName);
        temp.addEdge(parentName, childName, edgeLabel);
        output.println("added edge " + String.format("%.3f", edgeLabel) + " from " + parentName
                + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        GraphDesign<String, Double> temp = graphs.get(graphName);
        String result = graphName + " contains:";
        List<String> curr = temp.listNodes();
        for (String n : curr) {
            result += " " + n;
        }
        output.println(result);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        GraphDesign<String, Double> temp = graphs.get(graphName);
        String result = "the children of " + parentName + " in " + graphName + " are:";
        List<GraphDesign<String, Double>.LabelEdge> edgeList = temp.getEdges(parentName);
        edgeList.sort(new EdgeComparator());
        //edgeList.sort(Comparator.comparing(GraphDesign.LabelEdge::getDestination).thenComparing(GraphDesign.LabelEdge::getLabel));

        output.print(result);
        for (GraphDesign<String, Double>.LabelEdge l : edgeList) {
            output.print(" " + l.getDestination() + "(" + String.format("%.3f", l.getLabel()) + ")");
        }
        output.println();
    }


    private void findPath(List<String> arguments) {
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        String startNode = arguments.get(1);
        String endNode = arguments.get(2);
        findPath(graphName, startNode, endNode);
    }

    private void findPath(String graphName, String startNode, String endNode) {
        GraphDesign<String, Double> graph = graphs.get(graphName);
        String result = "path from " + startNode + " to " + endNode + ":";

        if (!graph.hasNode(startNode) && !graph.hasNode(endNode)) {
            output.println("unknown: " + startNode);
            output.println("unknown: " + endNode);
            return;
        } else if (!graph.hasNode(startNode)) {
            output.println("unknown: " + startNode);
            return;
        }else if (!graph.hasNode(endNode)) {
            output.println("unknown: " + endNode);
            return;
        }

        Path<String> path = FinderPaths.findShortestPath(graph, startNode, endNode);
        output.println(result);
        if (path == null) {
            output.println("total cost: 0.000");
        } else {
            double total = 0.0;
            Iterator<Path<String>.Segment> shortPath = path.iterator();
            while (shortPath.hasNext()) {
                Path<String>.Segment temp = shortPath.next();
                output.println(temp.getStart() + " to " + temp.getEnd() + " with weight " + String.format("%.3f", temp.getCost()));
                total += temp.getCost();
            }
            output.println("total cost: " + String.format("%.3f", total));
        }
    }

    private static class EdgeComparator implements Comparator<GraphDesign<String, Double>.LabelEdge> {
        public int compare(GraphDesign<String, Double>.LabelEdge o1, GraphDesign<String, Double>.LabelEdge o2) {
            if(!o1.getDestination().equals(o2.getDestination())) {
                return o1.getDestination().compareTo(o2.getDestination());
            } else if (!o1.getLabel().equals(o2.getLabel())) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
            return 0;
        }
    }


    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }


}
