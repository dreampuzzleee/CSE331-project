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

import React, {Component} from 'react';
import Map from "./Map";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import BuildingList from "./BuildingList";

interface AppState {
    building: string; //path should be displayed between two buildings
    requestResult: Array<Array<any>>; //Contain corresponding points of the path.
}

class App extends Component<{}, AppState> {

    constructor(props: {}) {
        super(props);
        this.state = {
            building: '',
            requestResult: []
        };
    }

    // Helper method to check building status.
    convert = (buildings: string) => {
        if (buildings.length === 0) {
            this.setState({
                requestResult: []
            })
            return ;
        }
        this.makeRequest(buildings);
    }

    // Gets the path between to buildings
    makeRequest = async (buildings: string) => {
        let building = buildings.trim().split("\n");
        let start = building[0];
        let end = building[1];
        let color = building[2];

        try {
            let response = await fetch("http://localhost:4567/findPath?start="+start+"&end="+ end);
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }
            let text = await response.json();
            let paths = text.path;

            let result = [];
            console.log(paths);
            for (let i = 0; i < paths.length; i++) {
                let num = [];
                let startX = paths[i].start.x;
                let startY = paths[i].start.y;
                let endX = paths[i].end.x;
                let endY = paths[i].end.y;
                num.push(startX);
                num.push(startY);
                num.push(endX);
                num.push(endY);
                num.push(color);
                result.push(num);
            }

            this.setState({
                requestResult: result
            });
            console.log(this.state.requestResult);
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };


    render() {
        return (
        <div>
            <h1 id="app-title">Finding Path!</h1>
            <div>
                <Map inputMap = {this.state.requestResult}/>
            </div>
            <BuildingList
                onChange={(value: string) => {
                    this.convert(value);
                }}
            />
        </div>
        );
    }

}

export default App;
