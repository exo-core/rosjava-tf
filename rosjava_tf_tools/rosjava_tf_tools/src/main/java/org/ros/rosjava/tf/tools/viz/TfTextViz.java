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

import org.ros.namespace.GraphName;
import org.ros.node.*;
import org.ros.rosjava.tf.Transform;
import org.ros.rosjava.tf.TransformBuffer;
import org.ros.rosjava.tf.pubsub.TransformListener;

import com.google.common.base.Preconditions;

import java.net.URI;
import java.util.HashMap;

import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;

/**
 * @author nick@heuristiclabs.com (Nick Armstrong-Crews)
 * @brief real-time, interactive graph visualizer for rosjava_tf
 *
 * @since Sep 5, 2011
 */
public class TfTextViz extends AbstractNodeMain implements GraphListener<String,TransformBuffer> {

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("rosjava/tfTextViz");
	}

	private ConnectedNode node;
	private TransformListener tfl;

	//protected final HashMap<String,Edge> edges;

	public TfTextViz() {
		//edges = new HashMap<String,Edge>();
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		try {
			node = connectedNode;
			tfl = new TransformListener(node);
			tfl.addListener(this);
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
		//this.node.shutdown();
		this.node = null;
	}

	@Override
	public void edgeAdded(GraphEdgeChangeEvent<String, TransformBuffer> e) {
		try {
			// TODO: use topological ordering to print neatly
			// TopologicalOrderIterator(DirectedGraph<V,E> dg)
			for( TransformBuffer txBuff : tfl.getTree().getGraph().edgeSet() ) {
				Transform tx = tfl.getTree().lookupMostRecent(txBuff.parentFrame, txBuff.childFrame);
				System.out.println(tx.toString());
			}
			//System.out.println(e.getEdge().getId());
		} catch(Exception exp) {exp.printStackTrace();}
	}

	@Override
	public void edgeRemoved(GraphEdgeChangeEvent<String, TransformBuffer> e) {} // we never remove edges

	@Override
	public void vertexAdded(GraphVertexChangeEvent<String> e) {}

	@Override
	public void vertexRemoved(GraphVertexChangeEvent<String> e) {}  // we never remove nodes

}
