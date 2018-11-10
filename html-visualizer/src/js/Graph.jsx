import React from 'react'
import PropTypes from 'prop-types';

import dagreD3 from 'dagre-d3';
import * as d3 from 'd3'

export default class Graph extends React.Component {

    static propTypes = {
        providedGraph: PropTypes.object.isRequired,
    };

    constructor(props) {
        super(props);
        this.state = {
            selectedNode: null
        };
    }

    selectNode(nodeName) {
        d3.event.stopPropagation();
        this.setState({
            selectedNode: nodeName,
        });
    }

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
        const neighboursOfSelectedNodes = this.state.selectedNode ? new Set(...[providedGraph.links.map(link => {
            if (link.from === this.state.selectedNode) {
                return link.to;
            } else if (link.to === this.state.selectedNode) {
                return link.from;
            }
        }).filter(node => node !== undefined)]) : new Set();

        var g = new dagreD3.graphlib.Graph({ multigraph: false, compound: true })
            // for params see: https://github.com/dagrejs/dagre/wiki#configuring-the-layout
            .setGraph({
                rankdir: 'TB',
                edgesep: 10,
                ranksep: 60,
                nodesep: 18,
                ranker: 'tight-tree'
            })
            .setDefaultEdgeLabel(function () { return {}; });

        categories.forEach(category => {
            g.setNode(category, { label: category });
        });

        providedGraph.nodes.forEach(node => {
            const shape = node.type === 'COMPONENT' ? 'ellipse' : 'rect';
            var clazz = node.type;
            if (this.state.selectedNode === node.name) {
                clazz += ' selected';
            } else if (neighboursOfSelectedNodes.has(node.name)) {
                clazz += ' neighbourSelected';
            }
            g.setNode(node.name, { label: node.name, class: clazz, shape: shape, onNodeClick: () => alert(node) });
            if (node.category) {
                g.setParent(node.name, node.category);
            }
        });

        providedGraph.links.forEach(link => {
            var clazz = link.type;
            if (this.state.selectedNode) {
                if (this.state.selectedNode === link.from || this.state.selectedNode === link.to) {
                    clazz += ' selected'
                } else {
                    clazz += ' unselected'
                }
            }
            g.setEdge(link.from, link.to, { class: clazz });
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

        //Register click handler
        svg.selectAll("g.node").on("click", this.selectNode.bind(this));
        svg.on("click", this.selectNode.bind(this));

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