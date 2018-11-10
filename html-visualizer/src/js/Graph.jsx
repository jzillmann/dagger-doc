import React from 'react'
import PropTypes from 'prop-types';

import dagreD3 from 'dagre-d3';
import * as d3 from 'd3'

export default class Graph extends React.Component {

    static propTypes = {
        providedGraph: PropTypes.object.isRequired,
    };

    componentDidMount() {
        this.renderDag();
    }

    componentDidUpdate() {
        this.renderDag();
    }

    renderDag() {
        var g = new dagreD3.graphlib.Graph()
            .setGraph({})
            .setDefaultEdgeLabel(function () { return {}; });

        this.props.providedGraph.nodes.forEach(node => {
            const shape = node.type === 'COMPONENT' ? 'ellipse' : 'rect';
            g.setNode(node.name, { label: node.name, class: node.type, shape: shape });
        });

        this.props.providedGraph.links.forEach(link => {
            g.setEdge(link.from, link.to, { class: link.type });
        });


        var render = new dagreD3.render();
        let svg = d3.select(this.nodeTree);// eslint-disable-line no-unused-vars
        let inner = d3.select(this.nodeTreeGroup);

        render(inner, g);
    }

    render() {
        return (
            <svg className="dagre-d3" ref={ (r) => { this.nodeTree = r } }
                width={ 999 }
                height={ 900 }>
                <g ref={ (r) => { this.nodeTreeGroup = r } } />
            </svg>
        );
    }
}