package core;

import java.util.ArrayList;
import visualization.NetNodeShape;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


/**
 * NetNode.java
 * @author CarlosGallardo
 */

public class NetNode extends Pane{
	
	private String nodeName;
	private Node view;
	private Text nameTag;
	public boolean nameToggle;
	public int size;
	public NetNodeShape shape;
	public Color color;
	public double forceX, forceY;
	private ArrayList<NetNode> linkedNodes = new ArrayList<>();
	
	/**
     * default constructor
     */
	public NetNode() {
        this.nodeName = "";
        this.nameTag = new Text();
        this.nameToggle = false;
        this.size = 10;
        this.shape = NetNodeShape.CIRCLE;
        this.color = Color.PURPLE;
        setCircleView(size, color);
    }
	
	/**
     * constructor
     * @param nodeName
     */
	public NetNode(String nodeName) {
        this.nodeName = nodeName;
        this.nameTag = new Text(nodeName);
        this.nameToggle = false;
        this.size = 10;
        this.shape = NetNodeShape.CIRCLE;
        this.color = Color.PURPLE;
        setCircleView(size, color);
    }
	
	/**
     * retrieve the name of the node
     * @return
     */
	public String getName() {
        return nodeName;
    }
	
	/**
     * adds a new linked node
     * @param node
     */
	public void addLinkedNode(NetNode node) {
		linkedNodes.add(node);
    }
	
	/**
     * removes a linked node
     * @param node
     */
	public void removeLinkedNode(NetNode node) {
    	linkedNodes.remove(node);
    }
	
	/**
     * retrieve all linked nodes
     * @return
     */
    public ArrayList<NetNode> getLinkedNodes() {
        return linkedNodes;
    }
    
    /**
     * sets view of node
     * @param view
     */
    public void setView(Node view) {
    	if(this.view != null) getChildren().remove(this.view);
        this.view = view;
        getChildren().add(view);
    }
    
    /**
     * retrieve view of node
     * @return
     */
    public Node getView() {
        return this.view;
    }
    
    /**
     * shows the name of the node
     */
    public void showNameView() {
    	if(shape.equals(NetNodeShape.SQUARE) || shape.equals(NetNodeShape.TRIANGLE)) {
    		nameTag.setLayoutX(view.getLayoutX() + size);
    		nameTag.setLayoutY(view.getLayoutY());
    	} else {
	        nameTag.setLayoutX(view.getLayoutX() + size / 2);
	        nameTag.setLayoutY(view.getLayoutY() - size / 2);
    	}
    	this.nameToggle = true;
        getChildren().add(nameTag);
    }
    
    /**
     * hides the name of the node
     */
    public void hideNameView() {
    	this.nameToggle = false;
    	getChildren().remove(nameTag);
    }
    
    /**
     * sizes the node by its degree
     */
    public void sizeByDegree() {
    	updateView(shape, 10 + linkedNodes.size() * 2, color);
    }
    
    /**
     * resets node size to default
     */
    public void resetSize() {
    	updateView(shape, 10, color);
    }
    
    /**
     * update node parameters
     * @param nodeShape
     * @param nodeSize
     * @param nodeColor
     */
    public void updateNode(NetNodeShape nodeShape, int nodeSize, Color nodeColor) {
    	this.size = nodeSize;
    	this.shape = nodeShape;
        this.color = nodeColor;
    }
    
    /**
     * update view of the node
     * @param nodeShape
     * @param nodeSize
     * @param nodeColor
     */
    public void updateView(NetNodeShape nodeShape, int nodeSize, Color nodeColor) {

        switch (nodeShape) {

        case CIRCLE:
        	updateNode(nodeShape, nodeSize, nodeColor);
        	setCircleView(nodeSize, nodeColor);
        	if (nameToggle) {
	        	hideNameView();
	        	showNameView();
        	}
            break;

        case SQUARE:
        	updateNode(nodeShape, nodeSize, nodeColor);
        	setSquareView(nodeSize, nodeColor);
        	if (nameToggle) {
	        	hideNameView();
	        	showNameView();
        	}
            break;
            
        case TRIANGLE:
        	updateNode(nodeShape, nodeSize, nodeColor);
        	setTriangleView(nodeSize, nodeColor);
        	if (nameToggle) {
	        	hideNameView();
	        	showNameView();
        	}
            break;

        default:
            throw new UnsupportedOperationException("Unsupported type: " + nodeShape);
        }
    }
    
    /**
     * set circle view for node
     * @param nodeSize
     * @param nodeColor
     */
    public void setCircleView(int nodeSize, Color nodeColor) {
    	double radius = nodeSize / 2;
        Circle view = new Circle(radius);
        view.setStroke(nodeColor);
        view.setFill(nodeColor);
        setView(view);
    }
    
    /**
     * set square view for node
     * @param nodeSize
     * @param nodeColor
     */
    public void setSquareView(int nodeSize, Color nodeColor) {
    	Rectangle view = new Rectangle(nodeSize,nodeSize);
        view.setStroke(nodeColor);
        view.setFill(nodeColor);
        setView(view);
    }
    
    /**
     * set triangle view for node
     * @param nodeSize
     * @param nodeColor
     */
    public void setTriangleView(int nodeSize, Color nodeColor) {
    	double width = nodeSize;
        double height = nodeSize;
        Polygon view = new Polygon(width / 2, 0, width, height, 0, height);
        view.setStroke(nodeColor);
        view.setFill(nodeColor);
        setView(view);
    }
	
	@Override
    public boolean equals(Object nodeObj) {
        if (nodeObj == null) return false;
        if (!(nodeObj instanceof NetNode)) return false;
        NetNode nodeToCompare = (NetNode) nodeObj;
        if(!nodeName.equals(nodeToCompare.nodeName)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31*hash + nodeName.hashCode();
        return hash;
    }
}