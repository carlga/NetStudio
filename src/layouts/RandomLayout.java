package layouts;

import java.util.ArrayList;
import java.util.Random;
import core.NetNode;
import visualization.Visualization;


/**
 * RandomLayout.java
 * @author CarlosGallardo
 */

public class RandomLayout extends Layout {

	Visualization visualization;
    Random rnd = new Random();
    
    /**
     * constructor
     * @param visualization
     */
    public RandomLayout(Visualization visualization) {
        this.visualization = visualization;
    }
    
    /**
     * randomizes the location of the nodes visualized
     */
    public void execute() {
    	ArrayList<NetNode> nodes = visualization.getNetwork().getNodes();
        for (NetNode node : nodes) {
            double x = rnd.nextDouble() * visualization.getScrollPane().getWidth();
            double y = rnd.nextDouble() * visualization.getScrollPane().getHeight();
            node.relocate(x, y);
        }
    }
}

