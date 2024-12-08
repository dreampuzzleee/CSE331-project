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

interface BuildingProps {
    onChange:(edges: string) => any;  // called when new edges list is ready
}

interface BuildingState {
    color: string; // a string represents the color of edge
    buildings: Array<any>; // array represents all buildings in UW
    start: string; // string represents the start building
    end: string; // string represents the end building
}

/**
 * A text field that allows the user to choose the start and end building, and color of path
 * Also contains the buttons that the user will use to interact with the app.
 */
class BuildingList extends Component<BuildingProps, BuildingState> {
    constructor(props: any) {
        super(props);
        this.state = {
            color: '',
            buildings: [],
            start: '',
            end: ''
        };
    }

    // Gets all buildings in UW
    getAll = async () => {
        try {
            let response = await fetch("http://localhost:4567/getBuilding");
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }
            let text = await response.json();
            this.setState({
                buildings: text
            });
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    componentDidMount() {
        this.getAll();
    }

    render() {
        let option:any = [];
        for(let i = 0; i < this.state.buildings.length; i++) {
            let temp = this.state.buildings[i];
            option.push(<option key={i} value={temp}>{temp}</option>)
        }

        return (
            <div id="edge-list">
                Start Building <br/>
                <select id = "start"
                        value={this.state.start}
                        onChange = {(event) => {this.setState({start: event.target.value})}}>
                    <option key={-1} value={""} selected>Select start</option>
                    {
                        option
                    }
                </select>
                <br/>
                End Building <br/>
                <select id = "end"
                        value={this.state.end}
                        onChange = {(event) => {this.setState({end: event.target.value})}}>
                    <option key={-1} value={""} selected>Select end</option>
                    {
                        option
                    }
                </select>
                <br/>
                Path Color <br/>
                <select id = "color"
                        value={this.state.color}
                        onChange = {(event) => {this.setState({color: event.target.value})}}>
                    <option selected>Select color</option>
                    <option>Blue</option>
                    <option value ="red">Red</option>
                    <option value ="orange">Orange</option>
                    <option value ="purple">Purple</option>
                    <option value ="yellow">Yellow</option>
                    <option value ="green">Green</option>
                    <option value ="black">Black</option>
                    <option value ="white">White</option>
                </select>
                <br/>

                <button
                    onClick={() => {console.log('Draw onClick was called');
                        this.props.onChange(this.state.start + "\n" + this.state.end + "\n" + this.state.color)}}
                >Draw</button>
                <button
                    onClick={() => {console.log('Clear onClick was called');
                        this.props.onChange('');
                        this.handleClear();
                    }}
                >Clear</button>

            </div>
        );
    }

    handleClear = () => {
        this.setState({
            start: "", end: "", color: ""
        })
        this.props.onChange("");
    }

}

export default BuildingList;
