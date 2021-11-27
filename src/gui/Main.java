package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import core.NetEdge;
import core.NetNode;
import core.Network;
import handlers.Controller;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import visualization.NetNodeShape;
import visualization.Visualization;


/**
 * Main.java
 * MAIN CLASS OF THE APPLICATION
 * @author CarlosGallardo
 */

public class Main extends Application{
	
	Visualization visualization;
	Network network;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			BorderPane root = new BorderPane();
			
			// create menu bar and items
			MenuBar menuBar = new MenuBar();
			Menu menuFile = new Menu("File");
			MenuItem menuitemLoadNetwork = new MenuItem("Load network");
			MenuItem menuitemSaveNetwork = new MenuItem("Save network");
			MenuItem menuitemExportNetworkImage = new MenuItem("Export network image");
			
			// create elements for the editing panel
			VBox editingPanel = new VBox();
			editingPanel.setPadding(new Insets(10, 10, 10, 10));
			editingPanel.setSpacing(10);

			Text title = new Text("Editing Pad");
			title.setFont(Font.font("Arial", FontWeight.BOLD, 14));

			Text nodeSubtitle = new Text("Node");
			nodeSubtitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			VBox.setMargin(nodeSubtitle, new Insets(10, 0, 0, 0));

			GridPane nodeGrid1 = new GridPane();
			nodeGrid1.setVgap(10);
			nodeGrid1.setHgap(10);	
			TextField nodeSelectionTxt = new TextField();
			Button searchNode = new Button("Search");
			GridPane.setConstraints(nodeSelectionTxt, 0, 0);
			GridPane.setConstraints(searchNode, 1, 0);

			GridPane nodeGrid2 = new GridPane();
			nodeGrid2.setVgap(10);
			nodeGrid2.setHgap(10);
			Label nodeSizeLbl = new Label("Size:");
			TextField nodeSizeTxt = new TextField();
			Label nodeShapeLbl = new Label("Shape:");
			ComboBox<NetNodeShape> nodeShapeComboBox = new ComboBox<NetNodeShape>();
			nodeShapeComboBox.getItems().setAll(NetNodeShape.values());
			nodeShapeComboBox.setValue(NetNodeShape.CIRCLE);
			Label nodeColorLbl = new Label("Colour:");
			ColorPicker nodeColorPicker = new ColorPicker();
			Button updateNode = new Button("Update");
			GridPane.setConstraints(nodeSizeLbl, 0, 0);
			GridPane.setConstraints(nodeSizeTxt, 1, 0);
			GridPane.setConstraints(nodeShapeLbl, 0, 1);
			GridPane.setConstraints(nodeShapeComboBox, 1, 1);
			GridPane.setConstraints(nodeColorLbl, 0, 2);
			GridPane.setConstraints(nodeColorPicker, 1, 2);
			GridPane.setConstraints(updateNode, 0, 3);

			Text edgeSubtitle = new Text("Edge");
			edgeSubtitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			VBox.setMargin(edgeSubtitle, new Insets(10, 0, 0, 0));

			GridPane edgeGrid = new GridPane();
			edgeGrid.setVgap(10);
			edgeGrid.setHgap(10);
			Label node1Lbl = new Label("Node 1:");
			TextField node1Txt = new TextField();
			Label node2Lbl = new Label("Node 2:");
			TextField node2Txt = new TextField();
			Button addEdge = new Button("Add");
			Button removeEdge = new Button("Remove");
			GridPane.setConstraints(node1Lbl, 0, 0);
			GridPane.setConstraints(node1Txt, 1, 0);
			GridPane.setConstraints(node2Lbl, 0, 1);
			GridPane.setConstraints(node2Txt, 1, 1);
			GridPane.setConstraints(addEdge, 0, 2);
			GridPane.setConstraints(removeEdge, 1, 2);
			
			// create bottom status bar
			AnchorPane statusBar = new AnchorPane();
			Label networkInfoLbl = new Label("0 nodes, 0 edges");
			networkInfoLbl.setPadding(new Insets(2,0,2,0));
			Label statusLbl = new Label();
			statusLbl.setPadding(new Insets(2,0,2,0));
			AnchorPane.setLeftAnchor(networkInfoLbl, 10.0);
			AnchorPane.setRightAnchor(statusLbl, 10.0);
			
			// set action events
			menuitemLoadNetwork.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					File file = locateFile(primaryStage);
					loadFile(file);
					networkInfoLbl.setText(getNetworkInfoLbl());
				}
			});
			menuitemSaveNetwork.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					Network network = visualization.getNetwork();
					if(!network.getNodes().isEmpty()) {
						File file = setSavingFile(primaryStage);
						if(file != null) network.exportNetwork(file);
					} else Controller.AlertBox.display(AlertType.WARNING, "Please, load a network.");
				}
			});
			menuitemExportNetworkImage.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					Network network = visualization.getNetwork();
					if(!network.getNodes().isEmpty()) {
						File file = setSavingImg(primaryStage);
						if(file != null) {
							WritableImage writableImage = visualization.getNodeLayer().snapshot(new SnapshotParameters(), null);
							BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
							try {
								ImageIO.write(bufferedImage, "png", file);
							} catch (IOException e) {
								System.err.println(e.getMessage());
							}
						}
					} else Controller.AlertBox.display(AlertType.WARNING, "Please, load a network.");
				}
			});
			searchNode.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if(nodeSelectionTxt.getText().matches("[\\w]+")) {
						NetNode node = new NetNode(nodeSelectionTxt.getText());
						Network network = visualization.getNetwork();
						if(network.getNodes().contains(node)) {
							nodeSizeTxt.setText(Integer.toString(network.getNode(node).size));
							nodeShapeComboBox.getSelectionModel().select(network.getNode(node).shape);
							nodeColorPicker.setValue(network.getNode(node).color);
						} else {
							Controller.AlertBox.display(AlertType.WARNING, "Given node is not present.");
						}
					} else {
						Controller.AlertBox.display(AlertType.WARNING, "Only word characters allowed.");
					}
				}
			});
			updateNode.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if(nodeSizeTxt.getText().matches("[1-9][\\d]")) {
						NetNode node = new NetNode(nodeSelectionTxt.getText());
						Network network = visualization.getNetwork();
						if(network.getNodes().contains(node)) {
							network.getNode(node).updateView(nodeShapeComboBox.getValue(), Integer.parseInt(nodeSizeTxt.getText()), nodeColorPicker.getValue());
							for(NetEdge edge : network.getEdges()) {
								if(edge.hasNode(node)) edge.updateInteractionView(network.getNode(node));
							}
						} else {
							Controller.AlertBox.display(AlertType.WARNING, "Given node is not present.");
						}
					} else {
						Controller.AlertBox.display(AlertType.WARNING, "Use a valid size (10-99).");
					}
				}
			});
			addEdge.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if(node1Txt.getText().matches("[\\w]+") && node2Txt.getText().matches("[\\w]+")) {
						addNewEdge(node1Txt.getText(), node2Txt.getText());
					}
					else Controller.AlertBox.display(AlertType.WARNING, "Only word characters allowed.");
				}
			});
			removeEdge.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if(node1Txt.getText().matches("[\\w]+") && node2Txt.getText().matches("[\\w]+")) {
						removeExistingEdge(node1Txt.getText(), node2Txt.getText());
					}
					else Controller.AlertBox.display(AlertType.WARNING, "Only word characters allowed.");
				}
			});
			
			// add elements to root pane
			menuFile.getItems().addAll(menuitemLoadNetwork, menuitemSaveNetwork, menuitemExportNetworkImage);
			menuBar.getMenus().addAll(menuFile);
			root.setTop(menuBar);
			visualization = new Visualization();
			root.setCenter(visualization.getScrollPane());
			nodeGrid1.getChildren().addAll(nodeSelectionTxt, searchNode);
			nodeGrid2.getChildren().addAll(nodeSizeLbl, nodeSizeTxt, nodeShapeLbl, nodeShapeComboBox, nodeColorLbl, nodeColorPicker, updateNode);
			edgeGrid.getChildren().addAll(node1Lbl, node1Txt, node2Lbl, node2Txt, addEdge, removeEdge);
			editingPanel.getChildren().addAll(title, nodeSubtitle, nodeGrid1, nodeGrid2, edgeSubtitle, edgeGrid);
			root.setRight(editingPanel);
			statusBar.getChildren().addAll(networkInfoLbl, statusLbl);
			root.setBottom(statusBar);
			
			// create, set scene and show stage
			Scene scene = new Scene(root,800,600);
			primaryStage.setTitle("NetStudio");		
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("NetStudio.png")));
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * prompts file explorer for loading file selection
	 * @return
	 */
	public File locateFile(Stage stage) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Load File");
		chooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Network files", "*.txt", "*.tab"),
				new FileChooser.ExtensionFilter("All files", "*.*"));
		return chooser.showOpenDialog(stage);
	}

	/**
	 * loads network from a given file
	 * @param file
	 */
	public void loadFile(File file) {
		if(file != null){
			Network network = visualization.getNetwork();
			visualization.clear();
			network.clear();
			network.loadFromFile(file);        	
			visualization.update();
		}
	}
	
	/**
	 * adds new edge to network
     * @param nodeName1
     * @param nodeName2
     */
	public void addNewEdge(String nodeName1, String nodeName2) {
		NetNode node1 = new NetNode(nodeName1);
		NetNode node2 = new NetNode(nodeName2);
		Network network = visualization.getNetwork();
		if(!network.getNodes().contains(node1)) network.addNode(node1.getName());
        if(!network.getNodes().contains(node2) && !node2.equals(node1)) network.addNode(node2.getName());
        if(!network.getEdges().contains(new NetEdge(node1, node2))) network.addEdge(node1.getName(), node2.getName());
        else Controller.AlertBox.display(AlertType.WARNING, "Given edge is already present.");
        visualization.update();
	}
	
	/**
	 * removes edge from network
     * @param nodeName1
     * @param nodeName2
     */
	public void removeExistingEdge(String nodeName1, String nodeName2) {
		NetNode node1 = new NetNode(nodeName1);
		NetNode node2 = new NetNode(nodeName2);
		Network network = visualization.getNetwork();
		if(network.getEdges().contains(new NetEdge(node1, node2))) network.removeEdge(node1.getName(), node2.getName());
        else Controller.AlertBox.display(AlertType.WARNING, "Given edge is not present.");
		if(network.getNodes().contains(node1) && network.getNode(node1).getLinkedNodes().isEmpty()) network.removeNode(network.getNode(node1));
		if(network.getNodes().contains(node2) && network.getNode(node2).getLinkedNodes().isEmpty()) network.removeNode(network.getNode(node2));
		visualization.update();
	}
	
	/**
	 * displays number of nodes and edges in network
     * @return
     */
	public String getNetworkInfoLbl() {
		Network network = visualization.getNetwork();
		String nodeNumber = Integer.toString(network.getNodes().size());
		String edgeNumber = Integer.toString(network.getEdges().size());
		return nodeNumber + " nodes, " + edgeNumber + " edges";
	}
	
	/**
	 * prompts file explorer for saving file
     * @return
     */
	public static File setSavingFile(Stage stage) {
		FileChooser chooser = new FileChooser();
	    chooser.setTitle("Save File");
        chooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("Network files", "*.txt", "*.tab"),
        		new FileChooser.ExtensionFilter("All files", "*.*"));
        return chooser.showSaveDialog(stage);
	}
	
	/**
	 * prompts file explorer for saving image
     * @return@
     */
	public File setSavingImg(Stage stage) {
		FileChooser chooser = new FileChooser();
	    chooser.setTitle("Save Image");
        chooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("Network image", "*.png"),
        		new FileChooser.ExtensionFilter("All files", "*.*"));
        return chooser.showSaveDialog(stage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
