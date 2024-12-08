package pathfinder.junitTests;

import graph.GraphDesign;
import marvel.MarvelPaths;
import org.junit.*;
import pathfinder.FinderPaths;
import pathfinder.datastructures.Point;


public class PathfinderTest {
    private GraphDesign<Point, Double> graph;
    Point p1;
    Point p2;
    Point p3;

    @Before
    public void setUp() {
        graph = new GraphDesign<>();
        p1 = new Point(1536.287, 1786.613);
        p2 = new Point(1561.0528, 1786.6467);
        p3 = new Point(2307.6622, 2390.3141);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindNullPath() {
        FinderPaths.findShortestPath(null, p1, p2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNullPoint1() {
        FinderPaths.findShortestPath(graph, null, p2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNullPoint2() {
        FinderPaths.findShortestPath(graph, p1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNotHas1() {
        graph.addNode(p1);
        graph.addNode(p2);
        graph.addEdge(p1, p2, 51.9858177);
        FinderPaths.findShortestPath(graph, p1, p3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNotHas2() {
        graph.addNode(p1);
        graph.addNode(p2);
        graph.addEdge(p1, p2, 51.9858177);
        FinderPaths.findShortestPath(graph, p3, p2);
    }

}
