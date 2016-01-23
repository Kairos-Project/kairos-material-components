package org.kairos.components;

import javafx.scene.control.TableColumn;


/**
 * Created by felipe on 13/09/15.
 */
public class FlexColumn<S, T> extends TableColumn<S, T> {

    private double weight;

    public FlexColumn() {
        this.getStyleClass().add("flex-column");
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {

        this.weight = weight;
    }
}
