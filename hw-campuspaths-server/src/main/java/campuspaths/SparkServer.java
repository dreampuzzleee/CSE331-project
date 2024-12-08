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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.Map;

public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();

        CampusMap map = new CampusMap();

        // Get the shortest path between start building and end building
        Spark.get("/findPath", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception{
                String startString = request.queryParams("start");
                String endString = request.queryParams("end");
                if(startString == null || endString == null) {
                    // You can also have a message in "halt" that is displayed in the page.
                    Spark.halt(400, "must have start and end");
                }
                Path<Point> result = null;
                try {
                    result = map.findShortestPath(startString, endString);
                } catch(IllegalArgumentException e) {
                    Spark.halt(400, "list dose not contain start or end");
                }
                Gson gson = new Gson();
                return gson.toJson(result);
            }
        });

        // Get all buildings in UW
        Spark.get("/getBuilding", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                Map<String, String> all = map.buildingNames();
                Gson gson = new Gson();
                return gson.toJson(all.keySet());
            }
        });
    }

}
