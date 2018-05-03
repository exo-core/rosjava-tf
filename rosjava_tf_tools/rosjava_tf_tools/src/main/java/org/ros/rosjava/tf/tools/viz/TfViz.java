/*
 * Copyright 2011 Heuristic Labs, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ros.rosjava.tf.tools.viz;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.*;
import org.ros.node.*;
import org.ros.rosjava.tf.TransformBuffer;
import org.ros.rosjava.tf.pubsub.TransformListener;
import org.ros.namespace.GraphName;

import com.google.common.base.Preconditions;
//import com.touchgraph.graphlayout.Edge;
import com.mxgraph.layout.*;
import com.mxgraph.swing.*;

import java.awt.Dimension;
import java.net.URI;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JApplet;

import org.jgrapht.Graph;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;
//import org.jgrapht.experimental.touchgraph.TouchgraphPanel;
import org.jgrapht.ext.JGraphXAdapter;


/**
 * A real-time, interactive graph visualizer for rosjava_tf
 *
 * @author nick@heuristiclabs.com (Nick Armstrong-Crews)
 * @since Sep 5, 2011
 */
public class TfViz extends AbstractNodeMain implements GraphListener<String, TransformBuffer> {

	private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

	public class JGraphXApplet extends JApplet {
		private TfViz tfViz;

		public JGraphXApplet(TfViz tfViz) {
			this.tfViz = tfViz;
		}

		@Override
		public void init() {
			setPreferredSize(DEFAULT_SIZE);
			mxGraphComponent component = new mxGraphComponent(tfViz.jgxAdapter);
			component.setConnectable(false);
			component.getGraph().setAllowDanglingEdges(false);
			getContentPane().add(component);
			resize(DEFAULT_SIZE);

			// positioning via jgraphx layouts
			mxCircleLayout layout = new mxCircleLayout(tfViz.jgxAdapter);

			// center the circle
			int radius = 100;
			layout.setX0((DEFAULT_SIZE.width / 2.0) - radius);
			layout.setY0((DEFAULT_SIZE.height / 2.0) - radius);
			layout.setRadius(radius);
			layout.setMoveCircle(true);

			layout.execute(jgxAdapter.getDefaultParent());
		}
	}

	protected static String laptopMasterUri = "http://localhost:11311";

	public static void main(String[] args) {
		try {
			NodeMainExecutor nodeRunner = DefaultNodeMainExecutor.newDefault();
			URI masterUri = new URI(laptopMasterUri);
			NodeConfiguration nodeConfiguration =
					NodeConfiguration.newPublic("localhost", masterUri);
			nodeRunner.execute(new TfViz(), nodeConfiguration);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("rosjava/tf_viz");
	}

	private Node node;
	private TransformListener tfl;

	private final JFrame frame;

	protected JGraphXAdapter<String, DefaultEdge> jgxAdapter;
	protected final ListenableGraph<String, DefaultEdge> g;
	//protected final HashMap<String,Edge> edges;
	protected final HashMap<String, com.mxgraph.model.mxCell> vertices;

	public TfViz() {
		frame = new JFrame();
		g = new DefaultListenableGraph<String, DefaultEdge>(new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class));
		g.addVertex("NONE");
		jgxAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
		//edges = new HashMap<String,Edge>();
		vertices = new HashMap<String, com.mxgraph.model.mxCell>();
	}

	@Override
	public void onStart(ConnectedNode node) {
		try {
			this.node = node;
			tfl = new TransformListener(node);
			tfl.addListener(this);

			frame.getContentPane().add(new JGraphXApplet(this));

			frame.setPreferredSize(new Dimension(800, 800));
			frame.setSize(600, 600);
			frame.setTitle("tfviz");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		        frame.addWindowListener(new WindowAdapter() {
//		            public void windowClosing(WindowEvent e) {this.shutdown(); System.exit(0);}
//		        });
			frame.pack();
			frame.setVisible(true);

		}
		catch (Exception e) {
			if (node != null) {
				node.getLog().fatal(e);
			}
			else {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onShutdown(Node node) {
		this.node.shutdown();
		this.node = null;
		frame.dispose();
	}

	@Override
	public void edgeAdded(GraphEdgeChangeEvent<String, TransformBuffer> e) {
		try {
			if (vertices.size() == 0) {
				jgxAdapter.removeCells();
			}

			TransformBuffer txBuff = e.getEdge();

			/*com.mxgraph.model.mxCell parentVizNode;
			com.mxgraph.model.mxCell childVizNode;

			if (!vertices.containsKey(txBuff.parentFrame)) {
				parentVizNode = new com.touchgraph.graphlayout.Node(txBuff.parentFrame);
			}
			else {
				parentVizNode = vertices.get(txBuff.parentFrame);
			}

			if (!vertices.containsKey(txBuff.childFrame)) {
				childVizNode = new com.touchgraph.graphlayout.Node(txBuff.childFrame);
			}
			else {
				childVizNode = vertices.get(txBuff.childFrame);
			}*/

			//Edge vizEdge = new Edge(parentVizNode, childVizNode, 1);
			g.addEdge(txBuff.parentFrame, txBuff.childFrame);
		}
		catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	@Override
	public void edgeRemoved(GraphEdgeChangeEvent<String, TransformBuffer> e) {
	} // we never remove edges

	@Override
	public void vertexAdded(GraphVertexChangeEvent<String> e) {
		try {
			if (vertices.size() == 0) {
				jgxAdapter.removeCells();
			}
			String frame = e.getVertex();
			g.addVertex(frame);
		}
		catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	@Override
	public void vertexRemoved(GraphVertexChangeEvent<String> e) {
	}  // we never remove nodes

}
