package visualization;

import core.NetNode;
import core.Network;
import handlers.Controller.MouseGestures;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;


/**
 * Visualization.java
 * Architecture for the visualization of draggable nodes in zoomable scroll pane.
 * @author CarlosGallardo
 */

public class Visualization {

	private Network network;
    private Group canvas;
    private ZoomableScrollPane scrollPane;
    MouseGestures mouseGestures;
    Pane nodeLayer; // wrapper for nodes in ScrollPane

    /**
     * default constructor
     */
    public Visualization() {
        this.network = new Network();
        canvas = new Group();
        nodeLayer = new Pane();
        canvas.getChildren().add(nodeLayer);
        mouseGestures = new MouseGestures(this);
        scrollPane = new ZoomableScrollPane(canvas);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    /**
     * retrieve zoomable scroll pane
     * @return
     */
    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }

    /**
     * retrieve node wrapping pane
     * @return
     */
    public Pane getNodeLayer() {
        return this.nodeLayer;
    }

    /**
     * retrieve visualized network
     * @return
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * update network visualization
     */
    public void update() {

        // add components to pane
    	getNodeLayer().getChildren().addAll(network.getAddedEdges());
    	getNodeLayer().getChildren().addAll(network.getAddedNodes());
    	
    	// set new edges underneath existing nodes
    	network.getAddedEdges().forEach(edge -> {edge.toBack();});

        // remove components from pane
    	getNodeLayer().getChildren().removeAll(network.getRemovedEdges());
    	getNodeLayer().getChildren().removeAll(network.getRemovedNodes());

        // enable dragging and randomly locate new nodes
        for (NetNode node : network.getAddedNodes()) {
            mouseGestures.makeDraggable(node);
            node.relocate(Math.random() * getScrollPane().getWidth(), Math.random() * getScrollPane().getHeight());
        }

        // update network
        getNetwork().merge();
    }
    
    /**
     * empties the visualization content
     */
    public void clear() {
    	scrollPane.zoomToActual();
    	getNodeLayer().getChildren().clear();
    }

    /**
     * retrieves zooming scale
     * @return
     */
    public double getScale() {
        return this.scrollPane.getScaleValue();
    }
}