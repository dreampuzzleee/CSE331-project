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

package pathfinder;

import graph.GraphDesign;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <var>CampusMap</var> represents a map for the campus which contains buildings
 * and corresponding distance
 */

public class CampusMap implements ModelAPI {

    private final GraphDesign<Point, Double> graph;
    private final List<CampusBuilding> buildings;

    //Rep invariant: graph != null && all items(points and distance) in graph >= 0
    // && buildings != null && all items in CampusBuildings != null && its x and y >= 0
    // && all point and buildings are unique
    //Abstract function:
    //      AF(this) = a graph is a GraphDesign type that storing
    //      the coordinate of buildings and corresponding distance.
    //      buildings is a list of building in campus
    //      The graph be like {PointA = [d1, d2,...], b = [...], ...} such that
    //      a, b, ect. are points with corresponding distance to the end building
    //      as edges, respectively.
    //      The buildings be like {building1, building2, ...}.


    public CampusMap() {
        graph = new GraphDesign<>();
        buildings = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        List<CampusPath> campusPaths = CampusPathsParser.parseCampusPaths("campus_paths.csv");
        for (CampusPath campusPath : campusPaths) {
            Point x1 = new Point(campusPath.getX1(), campusPath.getY1());
            Point x2 = new Point(campusPath.getX2(), campusPath.getY2());
            graph.addNode(x1);
            graph.addNode(x2);
            graph.addEdge(x1, x2, campusPath.getDistance());
        }
    }

    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert graph != null;
        assert buildings != null;
    }


    /**
     * @param shortName The short name of a building to query.
     * @return {@literal true} iff the short name provided exists in this campus map.
     */
    @Override
    public boolean shortNameExists(String shortName) {
        checkRep();
        for (CampusBuilding name : buildings) {
            if (name.getShortName().equals(shortName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param shortName The short name of a building to look up.
     * @return The long name of the building corresponding to the provided short name.
     * @throws IllegalArgumentException if the short name provided does not exist.
     */
    @Override
    public String longNameForShort(String shortName) {
        checkRep();
        if (!shortNameExists(shortName)) {
            throw new IllegalArgumentException("Name doesn't exist");
        }
        for (CampusBuilding name : buildings) {
            if (name.getShortName().equals(shortName)) {
                return name.getLongName();
            }
        }
        return null;
    }

    /**
     * @return A mapping from all the buildings' short names to their long names in this campus map.
     */
    @Override
    public Map<String, String> buildingNames() {
        checkRep();
        Map<String, String> result = new HashMap<>();
        for (CampusBuilding name : buildings) {
            result.put(name.getShortName(), name.getLongName());
        }
        return result;
    }

    /**
     * Finds the shortest path, by distance, between the two provided buildings.
     *
     * @param startShortName The short name of the building at the beginning of this path.
     * @param endShortName   The short name of the building at the end of this path.
     * @return A path between {@code startBuilding} and {@code endBuilding}, or {@literal null}
     * if none exists.
     * @throws IllegalArgumentException if {@code startBuilding} or {@code endBuilding} are
     *                                  {@literal null}, or not valid short names of buildings in
     *                                  this campus map.
     */
    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        checkRep();
        if (startShortName == null || endShortName == null ||
                !shortNameExists(startShortName) || !shortNameExists(endShortName)) {
            throw new IllegalArgumentException();
        }
        double x1 = 0.0;
        double y1 = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;
        for (CampusBuilding name : buildings) {
            if (name.getShortName().equals(startShortName)) {
                x1 = name.getX();
                y1 = name.getY();
            }
            if (name.getShortName().equals(endShortName)) {
                x2 = name.getX();
                y2 = name.getY();
            }
        }

        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        return FinderPaths.findShortestPath(graph, p1, p2);
    }

}
