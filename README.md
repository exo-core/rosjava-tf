# rosjava_tf

This package provides an implementation of TF for rosjava.

### Dependencies / Installation

We use [jgrapht](https://jgrapht.org/) to represent the tree of TF frames and [javax.vecmath](https://docs.oracle.com/cd/E17802_01/j2se/javase/technologies/desktop/java3d/forDevelopers/j3dapi/javax/vecmath/package-tree.html) for linear algebra.
All dependencies are handled via gradle. Just clone this package into your rosjava workspace and run `catkin_make`.

If you need cross-compile compatability for older java versions have a look at the [java6](https://github.com/exo-core/rosjava_tf/tree/java6) branch.

### Usage
#### Creating a subscriber

```
import org.ros.rosjava.tf.Transform;
import org.ros.rosjava.tf.pubsub.TransformListener;

...

TransformListener tfl = new TransformListener(connectedNode);
long now = (long) System.currentTimeMillis() * 1000000; // nanoseconds
String from = "map";
String to = "base_link";

try {
  Transform tx = tfl.getTree().lookupTransformBetween(from, to, now);
  System.out.println(tx);
}
catch (Exception e) {
  System.err.println("Error: "+e.toString());
}
```

### Aknowledgments

Most of this software is based on the previous works of Nick Armstrong Crews' [rosjava-tf](https://github.com/nickarmstrongcrews/rosjava-tf) package.
It has been updated to Kinetic and many unfinished and/or broken methods have been fixed.
