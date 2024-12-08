package marvel.junitTests;

import graph.GraphDesign;
import marvel.MarvelPaths;
import org.junit.*;
import static org.junit.Assert.*;

public class MarvelTest {
    private GraphDesign<String, String> staff;

    /**
     * Create a new graph
     */
    @Before
    public void setUp() {
        staff = new GraphDesign<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadNullGraph() {
        MarvelPaths.loadGraph(staff, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNullGraph() {
        MarvelPaths.findPath(null, "Ernst-the-Bicycling-Wizard", "Notkin-of-the-Superhuman-Beard");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNullPath() {
        MarvelPaths.findPath(staff, null, "Notkin-of-the-Superhuman-Beard");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNullPath2() {
        MarvelPaths.findPath(staff, "Ernst-the-Bicycling-Wizard", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNotHas1() {
        MarvelPaths.findPath(staff, "Ernst-the-Bicycling-Wizard", "b");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNotHas2() {
        MarvelPaths.findPath(staff, "c", "Notkin-of-the-Superhuman-Beard");
    }

    @Test
    public void testSameNode() {
        assertTrue(MarvelPaths.findPath(staff,"Ernst-the-Bicycling-Wizard", "Ernst-the-Bicycling-Wizard").isEmpty());
    }

}
