package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import handlers.Controller.AlertBox;
import javafx.scene.control.Alert.AlertType;


/**
 * Network.java
 * @author CarlosGallardo
 */

public class Network {

	private ArrayList<NetNode> netNodes;
	private ArrayList<NetNode> addedNetNodes;
	private ArrayList<NetNode> removedNetNodes;
    private ArrayList<NetEdge> netEdges;
    private ArrayList<NetEdge> addedNetEdges;
    private ArrayList<NetEdge> removedNetEdges;
    private HashMap<String, NetNode> nodeMap;
    
    /**
     * default constructor
     */
    public Network() {
    	clear();
    }
    
    /**
     * empties the network content
     */
    public void clear() {
        this.netNodes = new ArrayList<NetNode>();
        this.addedNetNodes = new ArrayList<NetNode>();
        this.removedNetNodes = new ArrayList<NetNode>();
        this.netEdges = new ArrayList<NetEdge>();
        this.addedNetEdges = new ArrayList<NetEdge>();
        this.removedNetEdges = new ArrayList<NetEdge>();
        this.nodeMap = new HashMap<String, NetNode>();
    }
    
    /**
     * empties nodes and edges to be added to network
     */
    public void clearAdditions() {
    	addedNetNodes.clear();
    	addedNetEdges.clear();
    }
    
    /**
     * retrieve node from network
     * @param targetNode
     * @return
     */
    public NetNode getNode(NetNode targetNode) {
    	NetNode node = new NetNode();
    	for(NetNode n : getNodes()) {
    		if(n.equals(targetNode)) node = n;
    	}
    	return node;
    }
    
    /**
     * retrieve all nodes from network
     * @return
     */
    public ArrayList<NetNode> getNodes(){
    	return netNodes;
    }
    
    /**
     * retrieve nodes added to network
     * @return
     */
    public ArrayList<NetNode> getAddedNodes(){
    	return addedNetNodes;
    }
    
    /**
     * retrieve nodes removed from network
     * @return
     */
    public ArrayList<NetNode> getRemovedNodes(){
    	return removedNetNodes;
    }
    
    /**
     * retrieve edge from network
     * @param targetEdge
     * @return
     */
    public NetEdge getEdge(NetEdge targetEdge) {
    	NetEdge edge = null;
    	for(NetEdge e : getEdges()) {
    		if(e.equals(targetEdge)) edge = e;
    	}
    	return edge;
    }
    
    /**
     * retrieve all edges from network
     * @return
     */
    public ArrayList<NetEdge> getEdges(){
    	return netEdges;
    }
    
    /**
     * retrieve edges added to network
     * @return
     */
    public ArrayList<NetEdge> getAddedEdges(){
    	return addedNetEdges;
    }
    
    /**
     * retrieve edges removed from network
     * @return
     */
    public ArrayList<NetEdge> getRemovedEdges(){
    	return removedNetEdges;
    }
    
    /**
     * add node to network
     * @param nodeName
     */
    public void addNode(String nodeName) {
    	NetNode node = new NetNode(nodeName);
    	addedNetNodes.add(node);
    	nodeMap.put(node.getName(), node);
    }
    
    /**
     * remove node from network
     * @param node
     */
    public void removeNode(NetNode node) {
    	removedNetNodes.add(node);
    	nodeMap.remove(node.getName());
    }
    
    /**
     * add edge to network
     * @param nodeName1
     * @param nodeName2
     */
    public void addEdge(String nodeName1, String nodeName2) {
    	NetNode node1 = nodeMap.get(nodeName1);
    	NetNode node2 = nodeMap.get(nodeName2);
        NetEdge edge = new NetEdge(node1, node2);
        edge.updateInteractionView(node1);
        edge.updateInteractionView(node2);
        addedNetEdges.add(edge);
    }
    
    /**
     * remove edge from network
     * @param nodeName1
     * @param nodeName2
     */
    public void removeEdge(String nodeName1, String nodeName2) {
    	NetNode node1 = nodeMap.get(nodeName1);
    	NetNode node2 = nodeMap.get(nodeName2);
        NetEdge edge = new NetEdge(new NetNode(nodeName1), new NetNode(nodeName2));
        node1.removeLinkedNode(node2);
        node2.removeLinkedNode(node1);
        removedNetEdges.add(getEdge(edge));
    }
    
    /**
     * update network content and clear addition/removal lists
     */
    public void merge() {
        // nodes
        netNodes.addAll(addedNetNodes);
        netNodes.removeAll(removedNetNodes);
        addedNetNodes.clear();
        removedNetNodes.clear();

        // edges
        netEdges.addAll(addedNetEdges);
        netEdges.removeAll(removedNetEdges);
        addedNetEdges.clear();
        removedNetEdges.clear();
    }
        
    /**
     * loads network from a tabular interaction file
     * @param file
     */
    public void loadFromFile(File file) {
        try(BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            
            while((line = reader.readLine()) != null) {
                String[] nodesInEdge = line.split("\t");
                if(nodesInEdge.length != 2 || !nodesInEdge[0].matches("[\\w]+") || !nodesInEdge[1].matches("[\\w]+") ) {
                	clearAdditions();
                	AlertBox.display(AlertType.ERROR, "Please, check file is appropriate.");
                	break;
                }
                if(!addedNetNodes.contains(new NetNode(nodesInEdge[0]))) addNode(nodesInEdge[0]);
                if(!addedNetNodes.contains(new NetNode(nodesInEdge[1]))) addNode(nodesInEdge[1]);
                if(!addedNetEdges.contains(new NetEdge(new NetNode(nodesInEdge[0]), new NetNode(nodesInEdge[1])))) addEdge(nodesInEdge[0], nodesInEdge[1]);
            }
            reader.close();
        } catch (IOException e) {
        	System.err.println(e.getMessage());
        }
    }
    
    /**
     * retrieve the degree of a node
     * @param node
     * @return
     */
    public int getDegree(NetNode node) {
        return getNode(node).getLinkedNodes().size();
    }
    
    /**
     * retrieve the average degree of the network
     * @return
     */
    public double getAverageDegree() {
        double cumulative = 0;
        for (NetNode node : netNodes) {
        	cumulative += node.getLinkedNodes().size(); 
        }
        return cumulative/netNodes.size();
    }
    
    /**
     * retrieve the degree categories of the network in ascending order
     * @return
     */
    public List<Integer> getDegreeCategories() {
    	List<Integer> degreeCategories = new ArrayList<Integer>();
    	for(NetNode node : netNodes) {
    		degreeCategories.add(node.getLinkedNodes().size());
    	}
    	degreeCategories = degreeCategories.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
    	Collections.sort(degreeCategories);
    	return degreeCategories;
    }
    
    /**
     * retrieve the hub nodes of the network
     * @return
     */
    public HashSet<NetNode> getHubNodes() {
    	HashSet<NetNode> hubNodes = new HashSet<NetNode>();
        int topDegree = getDegreeCategories().get(getDegreeCategories().size() - 1);
        netNodes.forEach(node -> { if(node.getLinkedNodes().size() == topDegree) hubNodes.add(node); });
        return hubNodes;
    }
    
    /**
	 * retrieve the number of nodes in each degree category
	 * @return
	 */
	public HashMap<Integer, Integer> getDegreeDistribution() {
		HashMap<Integer, Integer> degreeDistributionCounter = new HashMap<Integer, Integer>();
		getDegreeCategories().forEach(degreeCategory -> {degreeDistributionCounter.put(degreeCategory, 0); });
		netNodes.forEach(node -> { degreeDistributionCounter.put(node.getLinkedNodes().size(), 
				degreeDistributionCounter.get(node.getLinkedNodes().size()) + 1); });
		return degreeDistributionCounter;
	}
    
	/**
	 * export degree distribution to file in tabular format
	 * @param file
	 */
	public void exportDegreeDistribution(File file) {
		try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
			writer.write("Degree\tNodes\n");
			for(int degreeCategory : getDegreeCategories()) {
				writer.write(degreeCategory + "\t" + getDegreeDistribution().get(degreeCategory) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * export network to file in tabular format
	 * @param file
	 */
	public void exportNetwork(File file) {
		try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
			for(NetEdge edge : getEdges()) {
				writer.write(edge.getNode1().getName() + "\t" + edge.getNode2().getName() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
