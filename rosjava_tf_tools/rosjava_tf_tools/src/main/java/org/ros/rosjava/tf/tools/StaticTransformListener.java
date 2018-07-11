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

package org.ros.rosjava.tf.tools;

import org.ros.RosCore;
import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.rosjava.tf.Transform;
import org.ros.rosjava.tf.pubsub.TransformBroadcaster;
import org.ros.rosjava.tf.pubsub.TransformListener;

import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

/**
 * @author nick@heuristiclabs.com (Nick Armstrong-Crews)
 * @brief publishes (repeatedly) an unchanging transform (just like your favorite command-line tool!)
 * @since Sep 5, 2011
 */
public class StaticTransformListener extends AbstractNodeMain {

	public final String nodeName;
	protected ConnectedNode node;
	protected TransformListener tfl;

	protected double rate;
	protected Transform tx;

	protected String from;
	protected String to;

	/**
	 *
	 * @param nodeName
	 * @param rate         Hz
	 * @param parentFrame
	 * @param childFrame
	 */
	public StaticTransformListener(String nodeName, double rate, String fromFrame, String toFrame)	{
		this.nodeName = nodeName;
		this.rate = rate;
		this.from = fromFrame;
		this.to = toFrame;
		this.tx = new Transform(
				fromFrame, toFrame,
				new Vector3d(0, 0, 0),
				new Quat4d(0, 0, 0, 1)
		);
	}

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("rosjava/"+nodeName);
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		System.out.print("Starting StaticTransformListener: " + tx.getId() + "...");

		node = connectedNode;
		tfl = new TransformListener(node);

		System.out.println("...started.");

		// This CancellableLoop will be canceled automatically when the node shuts
		// down.
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			private long now;

			@Override
			protected void setup() {
				now = (long) System.currentTimeMillis() * 1000000;
			}

			@Override
			protected void loop() throws InterruptedException {
				now = (long) System.currentTimeMillis() * 1000000; // nanoseconds

				try {
					tx = tfl.getTree().lookupTransformBetween(from, to, now);
					System.out.println(tx);
				}
				catch (Exception e) {
					System.err.println("Error: "+e.toString());
				}

				Thread.sleep((long) (1000 / rate)); // convert rate in Hz to period in milliseconds
			}
		});
	}
}
