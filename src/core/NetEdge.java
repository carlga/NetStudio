package core;

import visualization.NetNodeShape;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;


/**
 * NetEdge.java
 * @author CarlosGallardo
 */

public class NetEdge extends Group{
	
	private NetNode node1;
	private NetNode node2;
	Line Interaction;
	Ellipse selfInteraction;
	
	/**
	 * constructor
	 * @param node1
	 * @param node2
	 */
	public NetEdge(NetNode node1, NetNode node2) {
        this.node1 = node1;
        this.node2 = node2;
        
        // add node linkages
        node1.addLinkedNode(node2);
        node2.addLinkedNode(node1);
        
        // non-self-interaction view
        if (!node1.equals(node2)) {
	        Interaction = new Line();
	        Interaction.startXProperty().bind(node1.layoutXProperty());
	        Interaction.startYProperty().bind(node1.layoutYProperty());
	        Interaction.endXProperty().bind(node2.layoutXProperty());
	        Interaction.endYProperty().bind(node2.layoutYProperty());
	        getChildren().add(Interaction);
	        
	    // self-interaction view
        } else {
        	selfInteraction = new Ellipse(node1.size / 2 * 0.75, node1.size/2);
        	selfInteraction.centerXProperty().bind(node1.layoutXProperty());
        	selfInteraction.centerYProperty().bind(node1.layoutYProperty().subtract(node1.size));
        	selfInteraction.setFill(Color.TRANSPARENT);
        	selfInteraction.setStroke(Color.BLACK);
        	getChildren().add(selfInteraction);
        }
    }
	
	/**
	 * retrieve first node in edge
	 * @return
	 */
	public NetNode getNode1() {
        return node1;
    }
	
	/**
	 * retrieve second node in edge
	 * @return
	 */
	public NetNode getNode2() {
        return node2;
    }
	
	/**
	 * check if edge contain a node
	 * @param targetNode
	 * @return
	 */
	public boolean hasNode(NetNode targetNode) {
        if (node1.equals(targetNode)) return true;
        if (node2.equals(targetNode)) return true;
        return false;
    }
	
	/**
	 * update interaction view
	 * @param targetNode
	 */
	public void updateInteractionView(NetNode targetNode) {
		
		// non-self-interaction view
		if (!node1.equals(node2)) {
			if(targetNode.shape.equals(NetNodeShape.SQUARE) || targetNode.shape.equals(NetNodeShape.TRIANGLE)) {
				if(targetNode.equals(node1)) {
					Interaction.startXProperty().bind(targetNode.layoutXProperty().add(targetNode.size / 2));
					Interaction.startYProperty().bind(targetNode.layoutYProperty().add(targetNode.size / 2));
				} else {
					Interaction.endXProperty().bind(targetNode.layoutXProperty().add(targetNode.size / 2));
					Interaction.endYProperty().bind(targetNode.layoutYProperty().add(targetNode.size / 2));
				}
			} else {
				if(targetNode.equals(node1)) {
			        Interaction.startXProperty().bind(targetNode.layoutXProperty());
			        Interaction.startYProperty().bind(targetNode.layoutYProperty());
				} else {
			        Interaction.endXProperty().bind(targetNode.layoutXProperty());
			        Interaction.endYProperty().bind(targetNode.layoutYProperty());
				}
			}
			
		// self-interaction view
		} else {
			getChildren().remove(selfInteraction);
			selfInteraction = new Ellipse(targetNode.size / 2 * 0.75, targetNode.size/2);
			if(targetNode.shape.equals(NetNodeShape.SQUARE) || targetNode.shape.equals(NetNodeShape.TRIANGLE)) {
				selfInteraction.centerXProperty().bind(targetNode.layoutXProperty().add(targetNode.size / 2));
		    	selfInteraction.centerYProperty().bind(targetNode.layoutYProperty().subtract(targetNode.size / 2));
		    	selfInteraction.setFill(Color.TRANSPARENT);
		    	selfInteraction.setStroke(Color.BLACK);
			} else {
				selfInteraction.centerXProperty().bind(targetNode.layoutXProperty());
		    	selfInteraction.centerYProperty().bind(targetNode.layoutYProperty().subtract(targetNode.size));
		    	selfInteraction.setFill(Color.TRANSPARENT);
		    	selfInteraction.setStroke(Color.BLACK);
			}
	    	getChildren().add(selfInteraction);
		}
	}
	
	@Override
    public boolean equals(Object edgeObj) {
        if (edgeObj == null) return false;
        if (!(edgeObj instanceof NetEdge)) return false;
        NetEdge edgeToCompare = (NetEdge) edgeObj;
        if (node1.equals(edgeToCompare.node1) && node2.equals(edgeToCompare.node2)) return true;
        else if (node1.equals(edgeToCompare.node2) && node2.equals(edgeToCompare.node1)) return true;
        else return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31*hash + node1.hashCode() + node2.hashCode();
        return hash;
    }
}