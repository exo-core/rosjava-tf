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

import org.ros.node.*;
import java.net.URI;


/**
 * A real-time, interactive graph visualizer for rosjava_tf
 *
 * @author nick@heuristiclabs.com (Nick Armstrong-Crews)
 * @since Sep 5, 2011
 */
public class RunTfViz {

	protected static String laptopMasterUri = "http://"+System.getenv("ROS_IP")+":11311";

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
}
