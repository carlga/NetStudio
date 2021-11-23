package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{
	
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
			TextField nodeShapeBox = new TextField();		// shape options for node
			Label nodeColorLbl = new Label("Colour:");
			ColorPicker nodeColorPicker = new ColorPicker();
			Button updateNode = new Button("Update");
			GridPane.setConstraints(nodeSizeLbl, 0, 0);
			GridPane.setConstraints(nodeSizeTxt, 1, 0);
			GridPane.setConstraints(nodeShapeLbl, 0, 1);
			GridPane.setConstraints(nodeShapeBox, 1, 1);
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
			
			// add elements to root pane
			menuFile.getItems().addAll(menuitemLoadNetwork, menuitemSaveNetwork, menuitemExportNetworkImage);
			menuBar.getMenus().addAll(menuFile);
			root.setTop(menuBar);
			// visualization pane to be added
			nodeGrid1.getChildren().addAll(nodeSelectionTxt, searchNode);
			nodeGrid2.getChildren().addAll(nodeSizeLbl, nodeSizeTxt, nodeShapeLbl, nodeShapeBox, nodeColorLbl, nodeColorPicker, updateNode);
			edgeGrid.getChildren().addAll(node1Lbl, node1Txt, node2Lbl, node2Txt, addEdge, removeEdge);
			editingPanel.getChildren().addAll(title, nodeSubtitle, nodeGrid1, nodeGrid2, edgeSubtitle, edgeGrid);
			root.setRight(editingPanel);
			statusBar.getChildren().addAll(networkInfoLbl, statusLbl);
			root.setBottom(statusBar);
			
			// create, set scene and show stage
			Scene scene = new Scene(root,800,600);
			primaryStage.setTitle("NetStudio");			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
