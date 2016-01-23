/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kairos.components;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import javafx.animation.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 *
 * @author Felipe
 */
public class MaterialButton extends Button {

    private static final Duration RIPPLE_DURATION = Duration.millis(250);
    private static final Duration SHADOW_DURATION = Duration.millis(350);
    private static final Color RIPPLE_COLOR = Color.web("#FFF", 0.3);

    public MaterialButton() {

        textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!oldValue.endsWith(newValue.toUpperCase())) {
                textProperty().set(newValue.toUpperCase());
            }
        });
        setPrefHeight(36);

    }

    @Override
    public Skin<?> createDefaultSkin() {
        ButtonSkin buttonSkin = (ButtonSkin) getSkin();
        if (buttonSkin == null) {
            buttonSkin = new ButtonSkin(this);
            Circle circleRipple = new Circle(0.1, RIPPLE_COLOR);
            buttonSkin.getChildren().add(0, circleRipple);
            setSkin(buttonSkin);

            createRippleEffect(circleRipple);
            getStyleClass().add("ripple-button");
        }
        return buttonSkin;
    }

    public ButtonSkin getButtonSkin() {
        return (ButtonSkin) getSkin();
    }

    public void setFlated(boolean flated) {
        if (flated) {
            getStyleClass().add("flat");
        } else {
            getStyleClass().remove("flat");
        }
    }

    public boolean getFlated() {
        return getStyleClass().indexOf("flat") != -1;
    }

    public void toggled(boolean toggled) {
        if (toggled) {
            getStyleClass().add("toggle");
        } else {
            getStyleClass().remove("toggle");
        }
    }

    public boolean getToggled() {
        return getStyleClass().indexOf("toggle") != -1;
    }

    private void createRippleEffect(Circle circleRipple) {
        Rectangle rippleClip = new Rectangle();
        rippleClip.widthProperty().bind(widthProperty());
        rippleClip.heightProperty().bind(heightProperty());
        
        circleRipple.setClip(rippleClip);
        circleRipple.setOpacity(0.0);
        /*Fade Transition*/
        FadeTransition fadeTransition = new FadeTransition(RIPPLE_DURATION, circleRipple);
        fadeTransition.setInterpolator(Interpolator.EASE_OUT);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        /*ScaleTransition*/
        final Timeline scaleRippleTimeline = new Timeline();
        DoubleBinding circleRippleRadius = new DoubleBinding() {
            {
                bind(heightProperty(), widthProperty());
            }

            @Override
            protected double computeValue() {
                return Math.max(heightProperty().get(), widthProperty().get() * 0.45);
            }
        };
        circleRippleRadius.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            KeyValue scaleValue = new KeyValue(circleRipple.radiusProperty(), newValue, Interpolator.EASE_OUT);
            KeyFrame scaleFrame = new KeyFrame(RIPPLE_DURATION, scaleValue);
            scaleRippleTimeline.getKeyFrames().add(scaleFrame);
        });
        /*ShadowTransition*/
        Animation animation = new Transition() {
            {
                setCycleDuration(SHADOW_DURATION);
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.30), 5 + (10 * frac), 0.10 + ((3 * frac) / 10), 0, 2 + (4 * frac)));

            }
        };
        animation.setCycleCount(2);
        animation.setAutoReverse(true);

        final SequentialTransition rippleTransition = new SequentialTransition();
        rippleTransition.getChildren().addAll(
                scaleRippleTimeline,
                fadeTransition
        );

        final ParallelTransition parallelTransition = new ParallelTransition();

        getStyleClass().addListener((ListChangeListener.Change<? extends String> c) -> {
            if (c.getList().indexOf("flat") == -1 && c.getList().indexOf("toggle") == -1) {
                setMinWidth(88);
                setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.30), 5, 0.10, 0, 2));
                parallelTransition.getChildren().addAll(rippleTransition, animation);
            } else {

                parallelTransition.getChildren().addAll(rippleTransition);
                setMinWidth(USE_COMPUTED_SIZE);
                setEffect(null);
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            parallelTransition.stop();
            circleRipple.setOpacity(0.0);
            circleRipple.setRadius(0.1);
            circleRipple.setCenterX(event.getX());
            circleRipple.setCenterY(event.getY());
            parallelTransition.playFromStart();

        });
    }

    public void setRippleColor(Color color) {
        ((Shape) ((SkinBase) getSkin()).getChildren().get(0)).setFill(color);
    }

}
