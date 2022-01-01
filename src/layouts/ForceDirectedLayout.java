package layouts;

import java.util.ArrayList;
import core.NetNode;
import visualization.Visualization;


/**
 * ForceDirectedLayout.java
 * Algorithm implementation from pseudocode found in publication:
 * Simple Algorithms for Network Visualization: A Tutorial by Michael J. McGuffin
 * Available from: https://ieeexplore.ieee.org/stamp/stamp.jsp?arnumber=6297585&tag=1
 * @author CarlosGallardo
 */

public class ForceDirectedLayout extends Layout {

	Visualization visualization;
    
	/**
     * constructor
     * @param visualization
     */
    public ForceDirectedLayout(Visualization visualization) {
        this.visualization = visualization;
    }
    
    /**
     * locates nodes according to a force directed positioning model
     */
	public void execute() {
		ArrayList<NetNode> nodes = visualization.getNetwork().getNodes();
		int N = nodes.size();
		double l = 50;		// spring rest length
		double kr = 8250;	// repulsive force constant
		double ks = 1;		// spring constant
		double dt = 0.4;	// time step
		double maxDisplacementSquared = 25;
		double maxRounds = 200;
		
		// iterative loop for node positioning
		for(int round = 0; round < maxRounds; round++) {
			
			// force initialization
			for(int i = 0; i < N; i++) {
				nodes.get(i).forceX = 0;
				nodes.get(i).forceY = 0;
			}
			
			// repulsion between all node pairs
			for(int i1 = 0; i1 < N-1; i1++) {
				NetNode node1 = nodes.get(i1);
				for(int i2 = i1+1; i2 < N; i2++) {
					NetNode node2 = nodes.get(i2);
					double dx = node2.getLayoutX() - node1.getLayoutX();
					double dy = node2.getLayoutY() - node1.getLayoutY();
					if(dx != 0 || dy != 0) {
						double distanceSquared = dx*dx + dy*dy;
						double distance = Math.sqrt(distanceSquared);
						double force = kr / distanceSquared;
						double fx = force * dx / distance;
						double fy = force * dy / distance;
						node1.forceX = node1.forceX - fx;
						node1.forceY = node1.forceY - fy;
						node2.forceX = node2.forceX + fx;
						node2.forceY = node2.forceY + fy;
					}
				}
			}
			
			// spring force between linked node pairs
			for(int i1 = 0; i1 < N; i1++) {
				NetNode node1 = nodes.get(i1);
				for(int j = 0; j < node1.getLinkedNodes().size(); j++) {
					int i2 = nodes.indexOf(node1.getLinkedNodes().get(j));
					NetNode node2 = nodes.get(i2);
					if(i1 < i2) {
						double dx = node2.getLayoutX() - node1.getLayoutX();
						double dy = node2.getLayoutY() - node1.getLayoutY();
						if(dx != 0 || dy != 0) {
							double distance = Math.sqrt(dx*dx + dy*dy);
							double force = ks * (distance - l);
							double fx = force * dx / distance;
							double fy = force * dy / distance;
							node1.forceX = node1.forceX + fx;
							node1.forceY = node1.forceY + fy;
							node2.forceX = node2.forceX - fx;
							node2.forceY = node2.forceY - fy;
						}
					}
				}
			}
			
			// node position update
			for(int i = 0; i < N; i++) {
				NetNode node = nodes.get(i);
				double dx = dt * node.forceX;
				double dy = dt * node.forceY;
				double displacementSquared = dx*dx + dy*dy;
				if(displacementSquared > maxDisplacementSquared) {
					double s = Math.sqrt(maxDisplacementSquared / displacementSquared);
					dx = dx * s;
					dy = dy * s;
				}
				node.relocate(node.getLayoutX() + dx, node.getLayoutY() + dy);
			}
		}
	}
}
