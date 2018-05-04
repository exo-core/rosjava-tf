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

package org.ros.rosjava.tf.tools.viz.demo;

import java.net.URI;
import java.net.URISyntaxException;

import org.ros.RosCore;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.rosjava.tf.tools.StaticTransformPublisher;
import org.ros.rosjava.tf.tools.viz.TfViz;

/**
 * @author nick@heuristiclabs.com (Nick Armstrong-Crews)
 * @brief demonstrates use of StaticTransformPublisher and TfViz graphical visualization tool
 * 
 * @since Sep 7, 2011
 */

public class VizDemo {
	
	protected static final String laptopIp = System.getenv("ROS_IP");
	protected static final int masterPort = 11311;
	protected static final String laptopMasterUriString = "http://" + laptopIp + ":" + masterPort;
	protected final NodeMainExecutor nodeRunner;
	protected StaticTransformPublisher moonToEarth;

	protected static boolean spawnRosCore = false;
	protected RosCore rosCore;

	public VizDemo() {
		nodeRunner = DefaultNodeMainExecutor.newDefault();
		moonToEarth = new StaticTransformPublisher(
				"moonToEarth",		// node name
				10.0,				// rate in Hz
				"/moon", "/earth",	// parent->child
				0.0, 0.0, 0.0,		// translation vector
				0.0, 0.0, 0.0, 1.0d // rotation (quaternion)
		);
	}
	
	public void startNodes() throws URISyntaxException {
		URI masterUri = new URI(laptopMasterUriString);

		/*if(spawnRosCore) {
			rosCore = RosCore.newPublic(laptopIp, masterPort);
			System.out.print("Starting roscore on " + laptopMasterUriString + "...");
			nodeRunner.execute(rosCore, NodeConfiguration.newPublic("localhost"));
			rosCore.awaitStart();
			System.out.println("...roscore started.");
		}*/

		nodeRunner.execute(moonToEarth, NodeConfiguration.newPublic("localhost", masterUri));
		//nodeRunner.run(new TfTextViz(), NodeConfiguration.newPublic("localhost", masterUri));
		nodeRunner.execute(new TfViz(), NodeConfiguration.newPublic("localhost", masterUri));

	}
}
