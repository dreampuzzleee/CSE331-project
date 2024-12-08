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

import React, { Component } from "react";
import EdgeList from "./EdgeList";
import Map from "./Map";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";

interface AppState {
    edges: string; // The edge should be displayed between two points.
}

// App operates text input and map edges
class App extends Component<{}, AppState> { // <- {} means no props.

  constructor(props: any) {
    super(props);
    this.state = {
        edges: ''
    };
  }

  render() {
      let updatedMap: Array<Array<any>> = [];
      outloop:
      if (this.state.edges.length !== 0) {
          let edge = this.state.edges.trim().split('\n'); // array
          for (let i = 0; i < edge.length; i++) {
              let num = edge[i].trim().split(' '); //array
              console.log(num);
              if (num.length !== 5) {
                  updatedMap = [];
                  window.alert("Error: Please check input length");
                  break;
              } else {
                  let data = [];
                  for (let j = 0; j < 4; j++) {
                      let curr = Number(num[j]);
                      if(isNaN(curr)) {
                          updatedMap = [];
                          window.alert("Error: Please check input format");
                          break outloop;
                      } else {
                          data.push(curr);
                      }
                  }
                  if (data[0] < 0 || data[0] > 4000 ||
                      data[1] > 4000 || data[1] < 0 ||
                      data[2] > 4000 || data[2] < 0 ||
                      data[3] > 4000 || data[3] < 0) {
                      updatedMap = [];
                      window.alert("Input Error");
                      break;
                  }
                  updatedMap.push(num);
              }
          }
      }


    return (
      <div>
        <h1 id="app-title">Line Mapper!</h1>
        <div>
          <Map inputMap = {updatedMap}/>
        </div>
        <EdgeList
          onChange={(value: string) => {
              this.draw(value);
          }}
        />
      </div>
    );
  }

  //called when update edges
  draw(edge: string) {
      this.setState({edges:edge});
  }
}

export default App;
