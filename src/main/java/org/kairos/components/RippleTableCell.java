/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kairos.components;

import com.sun.javafx.scene.control.skin.TableCellSkin;
import javafx.animation.FadeTransition;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.Duration;


public  class RippleTableCell<S, T> extends TableCell<S, T> {

    private final FadeTransition fade;
    final private double DEFAULT_DURATION = 350;

    public RippleTableCell() {
        fade = new FadeTransition(new Duration(DEFAULT_DURATION), this);
        fade.setFromValue(0);
        fade.setToValue(1);
        disableProperty().bind(emptyProperty());
    }

    static public Callback<TableColumn<Object, String>, TableCell<Object, String>> getRippleTableCell() {
        return param -> new RippleTableCell<>();
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if(!empty){
            if(item instanceof String){
                setText(item.toString());
            }
        }else{
            setText(null);
            setGraphic(null);
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    @Override
    protected Skin<?> createDefaultSkin() {

        TableCellSkin tableCellSkin = new TableCellSkin(this);
        RippleSkinFactory.getRippleEffect(tableCellSkin, this);
        return tableCellSkin;
      }

}
