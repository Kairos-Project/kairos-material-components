package org.kairos.components;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 * Created by felipe on 12/08/15.
 */
public class RippleSkinFactory {
    static public void getRippleEffect(SkinBase skinBase, Region node, Shape clip) {

        node.setClip(clip);
        Circle ripple = new Circle();
        ripple.radiusProperty().bind(node.widthProperty().divide(2));
        ripple.getStyleClass().add("ripple");

        skinBase.getChildren().add(ripple);
        skinBase.getChildren().addListener((ListChangeListener.Change c) -> {
            ObservableList observableList = c.getList();
            if (observableList.indexOf(ripple) == -1) {
                observableList.add(ripple);
            }
        });

        ripple.setScaleX(0);
        ripple.setScaleY(0);
        ripple.setOpacity(0.5);
        FadeTransition fade = new FadeTransition(new Duration(500), ripple);
        fade.setToValue(0);
        ScaleTransition scale = new ScaleTransition(new Duration(250), ripple);
        scale.setToX(1);
        scale.setToY(1);
        ParallelTransition rippleEffect = new ParallelTransition(fade, scale);
        rippleEffect.setInterpolator(Interpolator.EASE_OUT);
        rippleEffect.setOnFinished((ActionEvent event) -> {
            ripple.setOpacity(0.5);
            ripple.setScaleX(0);
            ripple.setScaleY(0);
        });

        node.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {

            ripple.setCenterX(event.getX());
            ripple.setCenterY(event.getY());
            rippleEffect.play();
        });

    }

    static public void getRippleEffect(SkinBase skinBase, Region node) {
        Rectangle rippleClip = new Rectangle();
        rippleClip.widthProperty().bind(node.widthProperty());
        rippleClip.heightProperty().bind(node.heightProperty());
        getRippleEffect(skinBase, node, rippleClip);

    }
}
