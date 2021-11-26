package handlers;

import visualization.Visualization;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;


/**
 * Controller.java
 * @author CarlosGallardo
 */

public class Controller {
	
	public static class AlertBox {
		
		/**
		 * prompt alert window
		 * @param alertType
		 * @param message
		 */
		public static void display(AlertType alertType, String message) {
			Alert alert = new Alert(alertType);
			alert.setContentText(message);
			alert.showAndWait();
		}
	}

	public static class MouseGestures {

		Visualization visualization;
	    DragContext dragContext = new DragContext();
	    
	    /**
		 * constructor
		 * @param visualization
		 */
	    public MouseGestures( Visualization visualization) {
	        this.visualization = visualization;
	    }
	    
	    class DragContext {
	        double x;
	        double y;
	    }
	    
	    /**
		 * enable node dragging
		 * @param node
		 */
	    public void makeDraggable(Node node) {
	        node.setOnMousePressed(new EventHandler<MouseEvent>() {
	        	@Override
		        public void handle(MouseEvent event) {
		            Node node = (Node) event.getSource();
		            double scale = visualization.getScale();
		            dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
		            dragContext.y = node.getBoundsInParent().getMinY()  * scale - event.getScreenY();
		        }
	        });
	        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        	@Override
		        public void handle(MouseEvent event) {
		            Node node = (Node) event.getSource();
		            double offsetX = event.getScreenX() + dragContext.x;
		            double offsetY = event.getScreenY() + dragContext.y;

		            // set offset according to scaling factor
		            double scale = visualization.getScale();
		            offsetX /= scale;
		            offsetY /= scale;
		            
		            // relocate node
		            node.relocate(offsetX, offsetY);
		        }
	        });
	    }
	}
}