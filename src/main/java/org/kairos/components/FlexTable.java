package org.kairos.components;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by felipe on 13/09/15.
 */
public class FlexTable<S> extends TableView<S> {
    protected double layoutWeight= 12;
    private  double headerHeight;

    public FlexTable() {
        getColumns().addListener(new ListChangeListener<TableColumn<S, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<S, ?>> c) {
                if (c.next()) {
                    for (TableColumn column : c.getAddedSubList()) {
                        column.setResizable(false);
                        double per = ((FlexColumn) column).getWeight() / layoutWeight;
                        column.prefWidthProperty().bind(widthProperty().subtract(16).multiply(per));
                    }
                }
            }
        });
        setFixedCellSize(46);
        itemsProperty().addListener(new ChangeListener<ObservableList<S>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<S>> observable, ObservableList<S> oldValue, ObservableList<S> newValue) {
                setPrefHeight((46 * newValue.size()) + 62);
            }
        });


    }

    public boolean getFixSize() {
        return getFixedCellSize() == 0 ? false : true;
    }


}
