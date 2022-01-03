package handlers;

import javafx.beans.property.SimpleIntegerProperty;


/**
 * TableInstance.java
 * @author CarlosGallardo
 */

public class TableInstance {

	private SimpleIntegerProperty degreeCategory = null;
    private SimpleIntegerProperty nodeCounts = null;
    
    /**
     * constructor
     * @param degreeCategory
     * @param nodeCounts
     */
    public TableInstance(Integer degreeCategory, Integer nodeCounts) {
        this.degreeCategory = new SimpleIntegerProperty(degreeCategory);
        this.nodeCounts = new SimpleIntegerProperty(nodeCounts);
    }
    
    /**
     * retrieve degree category
     * @return
     */
    public Integer getDegreeCategory() {
        return degreeCategory.get();
    }

    /**
     * set degree category
     * @param degreeCategory
     */
    public void setDegreeCategory(Integer degreeCategory) {
        this.degreeCategory.set(degreeCategory);
    }

    /**
     * retrieve node counts
     * @return
     */
    public Integer getNodeCounts() {
        return nodeCounts.get();
    }

    /**
     * set node counts
     * @param nodeCounts
     */
    public void setNodeCounts(Integer nodeCounts) {
        this.nodeCounts.set(nodeCounts);
    }
    
    /**
     * retrieve degree category property
     * @return
     */
    public SimpleIntegerProperty degreeCategoryProperty() {
        return degreeCategory;
    }
    
    /**
     * retrieve node count property
     * @return
     */
    public SimpleIntegerProperty nodeCountsProperty() {
        return nodeCounts;
    }
}
