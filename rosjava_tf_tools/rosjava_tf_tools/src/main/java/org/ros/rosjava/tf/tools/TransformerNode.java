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

/**
 * @author nick@heuristiclabs.com (Nick Armstrong-Crews)
 * @brief node that does tf processing and storage to support cross-platform use
 * @since Sep 5, 2011
 * 
 * node that does transform math, lookups, and conversions from Pose2D to tfMessages
 * and publishes result on /tf
 * intended to alleviate burden of learning quaternions and "time travel" for novice users
 * also intended to alleviate need to have native tf library for each language/platform
 * 
 */
public class TransformerNode {

}
