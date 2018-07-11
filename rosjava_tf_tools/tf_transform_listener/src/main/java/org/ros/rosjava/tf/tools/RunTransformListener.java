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
public class RunTransformListener {

	protected static String laptopMasterUri = "http://"+System.getenv("ROS_IP")+":11311";

	public static void main(String [] args) {
		try {
			NodeMainExecutor nodeRunner = DefaultNodeMainExecutor.newDefault();
			URI masterUri = new URI(laptopMasterUri);
			NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic("localhost", masterUri);

			String source = "base_link";
			String target = "iiwa_link_ee";

			nodeRunner.execute(new StaticTransformListener("static_tranform_listener", 1.0,source, target), nodeConfiguration);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
