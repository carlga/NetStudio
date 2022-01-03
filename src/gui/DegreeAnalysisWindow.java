package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import core.NetNode;
import core.Network;
import handlers.Controller;
import handlers.TableInstance;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * DegreeAnalysisWindow.java
 * @author CarlosGallardo
 */

public class DegreeAnalysisWindow {
	
	/**
     * displays degree analysis stage for network
     * @param network
     */
	public static void display(Network network) {
		Stage degreeAnalysisWindow = new Stage();
		degreeAnalysisWindow.initModality(Modality.APPLICATION_MODAL);
		degreeAnalysisWindow.setTitle("Degree analysis");
		
		BorderPane degreeAnalysisBox = new BorderPane();
		
		// create menu bar and items
		MenuBar menuBar = new MenuBar();
		Menu menuExport = new Menu("Export");
		MenuItem menuitemDistributionTable = new MenuItem("Degree distribution table");
		
		// create elements for the degree distribution visualization
		TabPane distributionTab = new TabPane();
		distributionTab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
        Tab tab1 = new Tab("Distribution Chart");
        CategoryAxis xAxis = new CategoryAxis(); 
        xAxis.setLabel("Degree");  
        NumberAxis yAxis = new NumberAxis();            
        yAxis.setLabel("Nodes");
        BarChart<String, Number> distributionChart = new BarChart<String,Number>(xAxis,yAxis);
        distributionChart.setTitle("Degree Distribution");
        distributionChart.setLegendVisible(false);
        XYChart.Series<String,Number> series = new XYChart.Series<String, Number>();  
        for(int degreeCategory : network.getDegreeCategories()) {
        	series.getData().add(new XYChart.Data<String, Number>(Integer.toString(degreeCategory),
        			network.getDegreeDistribution().get(degreeCategory)));
		}
        distributionChart.getData().add(series);
        
        Tab tab2 = new Tab("Distribution Table");
        TableView<TableInstance> distributionTable = new TableView<TableInstance>();
        distributionTable.setEditable(false);
        TableColumn<TableInstance, Integer> degreeCol = new TableColumn<TableInstance, Integer>("Degree");
        degreeCol.setCellValueFactory(new PropertyValueFactory<>("degreeCategory"));
        TableColumn<TableInstance, Integer> nodeCountCol = new TableColumn<TableInstance, Integer>("Nodes");
        nodeCountCol.setCellValueFactory(new PropertyValueFactory<>("nodeCounts"));
        distributionTable.getColumns().add(degreeCol);
        distributionTable.getColumns().add(nodeCountCol);
        for(int degreeCategory : network.getDegreeCategories()) {
        	TableInstance instance = new TableInstance(degreeCategory, network.getDegreeDistribution().get(degreeCategory));
        	distributionTable.getItems().add(instance);
        }
        
        // create elements for the information panel
        VBox degreeInfoPanel = new VBox();
        degreeInfoPanel.setPadding(new Insets(10, 10, 10, 10));
        degreeInfoPanel.setSpacing(10);
		
        HBox hbox1 = new HBox();
        hbox1.setSpacing(10);
		Text avgDegreeTxt = new Text("Average degree:");
		avgDegreeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		Label avgDegreeLbl = new Label();
		avgDegreeLbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		avgDegreeLbl.setText(String.format("%.4f", network.getAverageDegree()));
		
		Text netHubsTxt = new Text("Network Hubs:");
		netHubsTxt.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		TextArea netHubsTxt2 = new TextArea();
		netHubsTxt2.setEditable(false);
		netHubsTxt2.setPrefHeight(100);
		netHubsTxt2.setPrefWidth(100);
		String netHubs = "Name\tDegree\n";
    	for(NetNode node : network.getHubNodes()) {
    		netHubs += String.format("%s\t%d\n", node.getName(), node.getLinkedNodes().size());
    	}
    	netHubsTxt2.setText(netHubs);
		
		HBox hbox2 = new HBox();
		hbox2.setSpacing(10);
		VBox.setMargin(hbox2, new Insets(20, 0, 0, 0));
		Text nodeTxt = new Text("Node:");
		nodeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		TextField nodeTxt2 = new TextField();
		nodeTxt2.setPrefWidth(75);
		Button searchNode = new Button("Search");
		
		HBox hbox3 = new HBox();
		hbox3.setSpacing(10);
		Text nodeDegreeTxt = new Text("Degree:");
		nodeDegreeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		Label nodeDegreeLbl = new Label("0");
		nodeDegreeLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		
		Text linkedNodesTxt = new Text("Linked Nodes:");
		linkedNodesTxt.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		TextArea linkedNodesTxt2 = new TextArea();
		linkedNodesTxt2.setEditable(false);
		linkedNodesTxt2.setPrefHeight(100);
		linkedNodesTxt2.setPrefWidth(100);
		
		// set action events
		menuitemDistributionTable.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				File file = Main.setSavingFile(degreeAnalysisWindow);
				if(file != null) network.exportDegreeDistribution(file);
			}
		});
		searchNode.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(network.getNodes().contains(new NetNode(nodeTxt2.getText()))) {
					nodeDegreeLbl.setText(Integer.toString(network.getDegree(new NetNode(nodeTxt2.getText()))));
					
					String linkedNodes = "";
					List<NetNode>linkedNodesList = new ArrayList<NetNode>();
					linkedNodesList = network.getNode(new NetNode(nodeTxt2.getText())).getLinkedNodes().stream().collect(
							Collectors.toSet()).stream().collect(Collectors.toList());
			    	for(NetNode node : linkedNodesList) {
			    		linkedNodes += node.getName() + "\n";
			    	}
			    	linkedNodesTxt2.setText(linkedNodes);
				} else Controller.AlertBox.display(AlertType.WARNING, "Given node is not present.");
			}
		});
		
		// add elements to pane
		menuExport.getItems().addAll(menuitemDistributionTable);
		menuBar.getMenus().addAll(menuExport);
		degreeAnalysisBox.setTop(menuBar);
        tab1.setContent(distributionChart);
        tab2.setContent(distributionTable);
        distributionTab.getTabs().addAll(tab1, tab2);
        degreeAnalysisBox.setCenter(distributionTab);
		hbox1.getChildren().addAll(avgDegreeTxt, avgDegreeLbl);
		hbox2.getChildren().addAll(nodeTxt, nodeTxt2, searchNode);
		hbox3.getChildren().addAll(nodeDegreeTxt, nodeDegreeLbl);
		degreeInfoPanel.getChildren().addAll(hbox1, netHubsTxt,
				netHubsTxt2, hbox2, hbox3, linkedNodesTxt, linkedNodesTxt2);
		degreeAnalysisBox.setRight(degreeInfoPanel);
		
		// create, set scene and show stage
		Scene scene = new Scene(degreeAnalysisBox,600,400);
		degreeAnalysisWindow.setScene(scene);
		degreeAnalysisWindow.showAndWait();
	}
}
