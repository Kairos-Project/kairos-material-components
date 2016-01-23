package org.kairos.components;

import com.sun.javafx.scene.control.skin.RadioButtonSkin;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Skin;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;


/**
 * Created by felipe on 17/09/15.
 */
public class MaterialRadioButton<S> extends RadioButton {

    private S value;

    public MaterialRadioButton() {
        getStyleClass().add("material");

    }

    public S getValue() {
        return value;
    }

    public void setValue(S value) {
        this.value = value;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MaterialRadioButtonSkin(this);

    }


    static private class MaterialRadioButtonSkin extends RadioButtonSkin {
        static final int DURATION_ANIMATION = 200;
        private final StackPane radio;
        private final Node dot;
        private final Circle ripple = new Circle();
        private boolean isAction = false;

        public MaterialRadioButtonSkin(RadioButton radioButton) {
            super(radioButton);
            radio = (StackPane) getChildren().get(getChildren().size() - 1);
            dot = radio.lookup(".dot");
            if (!radioButton.isSelected()) {
                dot.setScaleX(0);
                dot.setScaleY(0);
            }
            getChildren().add(ripple);
            ripple.setRadius(24);
            ripple.setOpacity(0);
            ripple.centerXProperty().bind(radio.layoutXProperty().add(radio.widthProperty().divide(2)));
            ripple.centerYProperty().bind(radio.layoutYProperty().add(radio.heightProperty().divide(2)));
            ripple.getStyleClass().add("ripple");
            ripple.setMouseTransparent(true);
            radioButton.addEventFilter(ActionEvent.ACTION, event -> {
                isAction = true;
                animation(radioButton);
            });
            radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (!isAction) {
                    animation(radioButton);
                }
            });
        }


        private void animation(RadioButton radioButton) {
            ScaleTransition scaleTransition;
            if (radioButton.isSelected()) {
                scaleTransition = new ScaleTransition(Duration.millis(DURATION_ANIMATION), dot);
                scaleTransition.setToX(1);
                scaleTransition.setToY(1);

            } else {
                scaleTransition = new ScaleTransition(Duration.millis(DURATION_ANIMATION), dot);
                scaleTransition.setToX(0);
                scaleTransition.setToY(0);

            }
            if (isAction) {
                FadeTransition fadeInRipple = new FadeTransition(Duration.millis(DURATION_ANIMATION), ripple);
                fadeInRipple.setToValue(0.3);
                FadeTransition fadeOutRipple = new FadeTransition(Duration.millis(DURATION_ANIMATION), ripple);
                fadeOutRipple.setToValue(0);
                ParallelTransition focusEffect = new ParallelTransition(scaleTransition, fadeOutRipple);
                SequentialTransition animation = new SequentialTransition(fadeInRipple, focusEffect);
                animation.play();
                isAction = false;
            } else {
                scaleTransition.play();
            }
        }

    }
}
