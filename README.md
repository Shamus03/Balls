# Balls
Ball collision and spring physics simulator.

## Usage
Running the program with no arguments (either through command-line or by double
clicking the jar file) will prompt for a scene file.  Otherwise a path to a
scene file can be given as a command-line argument.  The simulation will start
in a running state by default, with no gravity or friction of any type (energy
will be conserved), and the total center of mass of the system will not be drawn.

## Scene Files
A scene file contains lines of code that initialize a scenario for the simulation.  
Some example scenes are included.  
Lines beginning with `#` will be treated as comments, and invalid commands will be ignored.  
The following options are available:

| Command | Parameters | Description |
| ------- | ---------- | ----------- |
| `ball` | `xPos yPos xVel yVel radius [name]` | Adds a ball with the given values.  Name is optional and is used for attaching other objects to the ball. |
| `spring` | `ball1 ball2 springConstant [springLength]` | Adds a spring between the two named balls specified.  The optional spring length will set the target length of the spring, which will otherwise be set to the initial separation distance of the balls. |
| `randomBall` | `minRadius maxRadius maxSpeed` | Adds a random ball within the bounds of the simulation with the given limits.  It is a good idea to use `seed` before any random calls for consistency between runs of the same scene |
| `seed` | `seed` | Sets a seed for the simulation.  It is a good idea to set a seed in the first few lines of a scene to ensure consistency between simulations of the same scene. |
| `repeat` | `numRepeats` | This will cause the single command immediately after this line to be repeated `numRepeats` times.  This is only useful for `randomBall` at the moment. |
| `stop` | none | Causes the simulation to start in a paused state. |
| `system` | `[ball1 ball2 ball3...]` | Adds the specified balls to a system, which will cause their combined center of mass to be drawn.  For example, a group of balls all interconnected by springs should be added to one system to view the center of mass. |
| `drawSystem` | `(true\|false)` | If true, a system will automatically be added containing every ball in the simulation (see `system`).  False by default. |
| `gravity` | `xGrav yGrav` | Sets a gravity vector that will affect all bodies in the simulation |
| `restitution` | `restitution` | Sets the restitution value for all balls created after this line.  A ball's relative velocity is multiplied by this amount after every collision. |
| `kineticFriction` | `friction` | Sets the coefficient of kinetic friction for all balls created after this line.  This will cause balls to slow down over time. |
| `staticFriction` | `friction` | Sets the coefficient of static friction for all balls created after this line.  This will cause balls to immediately stop if they are traveling slow enough. |
