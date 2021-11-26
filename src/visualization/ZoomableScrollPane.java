package visualization;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;


/**
 * ZoomableScrollPane.java
 * @author CarlosGallardo
 */

public class ZoomableScrollPane extends ScrollPane {
    
	Group zoomGroup;
    Scale scaleTransform;
    Node content;
    double scaleValue = 1.0;
    double delta = 0.1;
    double minScale = 0.1;
    double maxScale = 2.0;

    /**
     * constructor
     * @param content
     */
    public ZoomableScrollPane(Node content) {
        this.content = content;
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(content);
        setContent(contentGroup);
        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
        zoomGroup.getTransforms().add(scaleTransform);
        zoomGroup.setOnScroll(new EventHandler<ScrollEvent>() {
        	@Override
            public void handle(ScrollEvent scrollEvent) {
            	if (scrollEvent.getDeltaY() < 0) scaleValue -= delta;
            	else scaleValue += delta;
                if (scaleValue < minScale) scaleValue += delta;
                else if (scaleValue > maxScale) scaleValue -= delta;
                else {
                	zoomTo(scaleValue);
                	scrollEvent.consume();
                }
            }
        });
    }

    /**
     * retrieve zoom scaling
     * @return
     */
    public double getScaleValue() {
        return scaleValue;
    }

    /**
     * reset zoom scale
     */
    public void zoomToActual() {
        zoomTo(1.0);
    }

    /**
     * zoom to a given scale
     * @param scaleValue
     */
    public void zoomTo(double scaleValue) {
        this.scaleValue = scaleValue;
        scaleTransform.setX(scaleValue);
        scaleTransform.setY(scaleValue);
    }
}
