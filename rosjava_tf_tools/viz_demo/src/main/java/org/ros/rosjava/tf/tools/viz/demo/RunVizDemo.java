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

/**
 * @author nick@heuristiclabs.com (Nick Armstrong-Crews)
 * @brief demonstrates use of StaticTransformPublisher and TfViz graphical visualization tool
 * 
 * @since Sep 7, 2011
 */

public class RunVizDemo {
	public static void main(String [] args) {
		try {
			VizDemo demo = new VizDemo();
			demo.startNodes();
		} catch(Exception e) {
			e.printStackTrace();
		}
	  }

}
