package graph.junitTests;

import graph.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.Timeout;

/**
 * This class contains a set of test cases that can be used to test the implementation of the
 * GraphDesign class.
 *
 * @see graph.GraphDesign
 */
public class GraphDesignTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    //private field
    private GraphDesign<String, String> graph;
    private GraphDesign<String, String>.LabelEdge edge;
    private String node1 = "a";
    private String node2 = "b";
    private String edge12 = "e12";

    /**
     * Create a new graph and labeled edge
     */
    @Before
    public void setUp() {
        graph = new GraphDesign<>();
        edge = graph.new LabelEdge("c", "d", "e22");
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////  Null Situation
    ///////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testAddNodeNull() {
        assertFalse(graph.addNode(null));
    }

    @Test
    public void testAddNullEdge() {
        assertFalse(graph.addEdge(null, null, null));
    }

    @Test
    public void testAddTwoNullEdge() {
        assertFalse(graph.addEdge(node1, null, null));
    }

    @Test
    public void testAddOneNullEdge() {
        assertFalse(graph.addEdge(node1, node2, null));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullNode() {
        assertFalse(graph.hasNode(null));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullChildren() {
        graph.childrenOf(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullGetEdges() {
        graph.getEdges(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testChildrenOfWithoutAdd() {
        graph.getEdges(node1);
    }

    @Test
    public void testEmpty() {
        assertTrue(graph.isEmpty());
    }
    
    // Test whether the node exist before add this node
    @Test
    public void testHasNoBeforeAddNode() {
        assertFalse(graph.hasNode(node1));
    }

    // Test add a node
    @Test
    public void testAddNode() {
        graph.addNode(node1);
        assertTrue(graph.hasNode(node1));
    }

    // Test the graph can't have same node
    @Test
    public void testAddSameNode() {
        assertTrue(graph.addNode(node1));
        assertFalse(graph.addNode(node1));
    }

    // Test total node number
    @Test
    public void testNumberOneNode() {
        graph.addNode(node1);
        assertEquals(1, graph.countNode());
    }

    // Test total edge number
    @Test
    public void testNumberOneEdge() {
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addEdge(node1,node2,edge12);
        assertEquals(1,graph.countEdge());
    }


    // Test add a edge to an non-existing node
    @Test
    public void testAddEdgeOneNode() {
        graph.addNode(node1);
        assertFalse(graph.addEdge(node1, node2, edge12));
    }

    // Test can't have two same edge
    @Test
    public void testAddSameEdge() {
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addEdge(node1,node2,edge12);
        graph.addEdge(node1,node2,edge12);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////  Inner Class Test
    ///////////////////////////////////////////////////////////////////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullLabel() {
        graph.new LabelEdge("c", "d", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullNode() {
        graph.new LabelEdge(null, "d", "e22");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullNode2() {
        graph.new LabelEdge("c", null, "e22");
    }

    @Test
    public void testGetStart() {
        assertEquals("c", edge.getStart());
    }

    @Test
    public void testGetDestination() {
        assertEquals("d", edge.getDestination());
    }

    @Test
    public void testGetLabel() {
        assertEquals("e22", edge.getLabel());
    }
}
