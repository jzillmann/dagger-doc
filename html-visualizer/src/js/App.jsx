import React from "react";

import Graph from "Graph.jsx";

const providedGraph = graphPayload;// eslint-disable-line no-undef

const App = () => {

    return (
        <div className="centered">
            <h1>Dagger Doc</h1>
            <Graph providedGraph={ providedGraph } />
        </div>
    );
};
export default App;