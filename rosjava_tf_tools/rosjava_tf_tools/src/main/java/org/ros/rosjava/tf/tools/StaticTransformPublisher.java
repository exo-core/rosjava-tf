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

import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import org.ros.concurrent.CancellableLoop;
import org.ros.node.DefaultNodeFactory;
import org.ros.node.Node;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.AbstractNodeMain;
import org.ros.namespace.GraphName;
import org.ros.rosjava.tf.Transform;
import org.ros.rosjava.tf.pubsub.TransformBroadcaster;

/**
 * @author nick@heuristiclabs.com (Nick Armstrong-Crews)
 * @brief publishes (repeatedly) an unchanging transform (just like your favorite command-line tool!)
 * @since Sep 5, 2011
 */
public class StaticTransformPublisher extends AbstractNodeMain {

	public final String nodeName;
	protected ConnectedNode node;
	protected TransformBroadcaster tfb;

	protected double rate;
	protected final Transform tx;
	
	public StaticTransformPublisher(
										String nodeName,
										double rate, // Hz
										String parentFrame, String childFrame,
										double v_x, double v_y, double v_z,
										double q_x, double q_y, double q_z, double q_w // quaternion
									)
	{
		this.nodeName = nodeName;
		this.rate = rate;
		this.tx = new Transform(
									parentFrame, childFrame,
									new Vector3d(v_x, v_y, v_z),
									new Quat4d(q_x, q_y, q_z, q_w)
								);
	}

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("rosjava/"+nodeName);
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		System.out.print("Starting StaticTransformPublisher: " + tx.getId() + "...");

		node = connectedNode;
		tfb = new TransformBroadcaster(node);

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
				//System.out.println("Sending static transform: " + tx.getId());
				tfb.sendTransform(
						tx.parentFrame, tx.childFrame,
						now,
						tx.translation.x, tx.translation.y, tx.translation.z,
						tx.rotation.x, tx.rotation.y, tx.rotation.z, tx.rotation.w
				);
				Thread.sleep((long) (1000 / rate)); // convert rate in Hz to period in milliseconds
			}
		});
	}
}
