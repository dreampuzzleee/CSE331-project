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

interface EdgeListProps {
    onChange:(edges: string) => any;  // called when a new edge list is ready
}

interface EdgeListState {
    inputValue: string; // a string represents the input in the text box
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, EdgeListState> {
    constructor(props: any) {
        super(props);
        this.state = {
            inputValue: ''
        };
    }

    // called when the input texts into
    handleInputChange = (event: any) => {
        this.setState({
            inputValue: event.target.value
        });
    };

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    placeholder={'Please enter coordinate: x1 y1 x2 y2 color'}
                    value={this.state.inputValue}
                    onChange={this.handleInputChange}
                /> <br/>
                <button
                    onClick={() => {console.log('Draw onClick was called');
                    this.props.onChange(this.state.inputValue)}}
                >Draw</button>
                <button
                    onClick={() => {console.log('Clear onClick was called');
                    this.props.onChange('')}}
                >Clear</button>
            </div>
        );
    }
}

export default EdgeList;
