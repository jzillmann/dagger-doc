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
        // TB - top bottom
        // BT - bottom top
        // RL - right left
        // LR - left right
        const providedGraph = this.props.providedGraph;
        const categories = [...new Set(providedGraph.nodes.map(node => node.category).filter(category => category !== undefined))];


        var g = new dagreD3.graphlib.Graph({ multigraph: false, compound: true })
            .setGraph({ rankdir: 'TB' })
            .setDefaultEdgeLabel(function () { return {}; });

        categories.forEach(category => {
            g.setNode(category, { label: category });
        });

        providedGraph.nodes.forEach(node => {
            const shape = node.type === 'COMPONENT' ? 'ellipse' : 'rect';
            g.setNode(node.name, { label: node.name, class: node.type, shape: shape });
            if (node.category) {
                g.setParent(node.name, node.category);
            }
        });

        providedGraph.links.forEach(link => {
            g.setEdge(link.from, link.to, { class: link.type });
        });


        var render = new dagreD3.render();
        let svg = d3.select(this.nodeTree);// eslint-disable-line no-unused-vars
        let inner = d3.select(this.nodeTreeGroup);

        render(inner, g);

        // Re-position cluster/category labels
        const clusters = document.querySelectorAll('.cluster');
        clusters.forEach(cluster => {
            const rect = cluster.children[0];
            const label = cluster.children[1].children[0];
            label.setAttribute('transform', `translate(${rect.getAttribute('x')},${rect.getAttribute('y') - 20})`);
        });


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