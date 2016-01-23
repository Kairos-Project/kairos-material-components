package org.kairos.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.util.Duration;


/**
 * Created by Felipe on 20/10/2015.
 */
public class TextInputLayout extends StackPane implements ChangeListener<Boolean> {

    private static PseudoClass NOT_ANIMATED = PseudoClass.getPseudoClass("not-animated");
    private static PseudoClass BAR_PLACED = PseudoClass.getPseudoClass("bar-placed");
    private static Duration ANIM_DURATION = new Duration(200);

    private final Scale labelScale;
    private final Line bar;
    private final Label label;

    private boolean animated;
    private boolean floatedLabel;

    private TextInputControl input;

    public TextInputLayout() {

        bar = new Line(0, 0, 0, 0);
        bar.getStyleClass().add("bar");

        labelScale = new Scale(1, 1, 0, 0);
        label = new Label();
        label.getTransforms().add(labelScale);
        label.setMouseTransparent(true);
        StackPane.setAlignment(label, Pos.TOP_LEFT);
        label.setTranslateY(10);

        getStyleClass().add("paper-input");
        setPrefWidth(150);
        setMinWidth(USE_PREF_SIZE);


        getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> c) {
                if (c.getList().size() == 1) {
                    if (c.getList().get(0) instanceof TextInputControl) {
                        placeTextInput((TextInputControl) c.getList().get(0));
                    }
                } else if (c.getList().size() > 1) {
                    getChildrenUnmodifiable().removeListener(this);
                }
            }
        });

    }

    public void setAnimated(boolean animation) {
        this.animated = animation;
        animate();
    }

    public boolean getAnimated() {
        return this.animated;
    }

    public void setFloatedLabel(boolean floatedLabel) {
        this.floatedLabel = floatedLabel;
        floatedConf();
    }

    public boolean getFloatedLabel() {
        return this.floatedLabel;
    }

    private void floatedConf() {
        if (floatedLabel) {
            this.getChildren().add(label);
            label.setText(input.getPromptText());
            input.setPromptText("");
            input.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue != null) {
                        if (newValue.length() > 0) {
                            label.setTranslateY(-12);
                            labelScale.setX(0.75);
                            labelScale.setY(0.75);
                            input.textProperty().removeListener(this);
                        }
                    }
                }
            });
            if (input.getText().length() > 0) {
                label.setTranslateY(-12);
                labelScale.setX(0.75);
                labelScale.setY(0.75);
            } else if (input.getText().isEmpty() && !input.isFocused()) {
                label.setTranslateY(12);
                labelScale.setX(1);
                labelScale.setY(1);
            }
        } else {
            this.getChildren().remove(label);
            input.setPromptText(label.getText());
        }
    }

    private void animate() {

        input.pseudoClassStateChanged(NOT_ANIMATED, !animated);
        if (animated) {
            input.focusedProperty().addListener(this);
            this.getChildren().add(bar);
        } else {
            input.focusedProperty().removeListener(this);
            this.getChildren().remove(bar);
        }
        setFloatedLabel(animated);


    }

    private void placeTextInput(TextInputControl input) {
        this.input = input;
        input.getStyleClass().add("paper-input");
        bar.setStrokeWidth(2);
        bar.setVisible(false);
        StackPane.setAlignment(bar, Pos.BOTTOM_CENTER);

        setAnimated(true);

    }

    /**
     * Method called when the input has or less the focus
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        Timeline timeline;
        this.pseudoClassStateChanged(PseudoClass.getPseudoClass("focused"), newValue);
        bar.setVisible(true);
        if (floatedLabel) {
            timeline = getNormalAnimation(newValue);
        } else {
            timeline = getNotFloatedAnimation(newValue);
        }
        timeline.setOnFinished(evt ->
                {
                    bar.setVisible(false);
                    this.pseudoClassStateChanged(BAR_PLACED, newValue);
                    label.setTextFill(newValue ? bar.getStroke() : Color.BLACK);
                }

        );

        timeline.play();
    }


    private Timeline getNormalAnimation(boolean sense) {
        Timeline timeline;
        if (sense) {

            timeline = new Timeline(
                    new KeyFrame(ANIM_DURATION,
                            new KeyValue(bar.endXProperty(), input.getWidth()),
                            new KeyValue(labelScale.xProperty(), 0.75),
                            new KeyValue(labelScale.yProperty(), 0.75),
                            new KeyValue(label.translateYProperty(), -12)
                    )
            );

        } else {

            timeline = new Timeline(
                    new KeyFrame(ANIM_DURATION,
                            new KeyValue(bar.endXProperty(), 0),
                            new KeyValue(labelScale.xProperty(), input.getText() == null || input.getText().isEmpty() ? 1 : 0.75),
                            new KeyValue(labelScale.yProperty(), input.getText() == null || input.getText().isEmpty() ? 1 : 0.75),
                            new KeyValue(label.translateYProperty(),input.getText() == null || input.getText().isEmpty() ? 12 : -12)
                    )
            );

        }
        return timeline;
    }

    private Timeline getNotFloatedAnimation(boolean sense) {
        Timeline timeline;
        if (sense) {

            timeline = new Timeline(
                    new KeyFrame(ANIM_DURATION,
                            new KeyValue(bar.endXProperty(), input.getWidth())
                    )
            );

        } else {

            timeline = new Timeline(
                    new KeyFrame(ANIM_DURATION,
                            new KeyValue(bar.endXProperty(), 0))
            );

        }
        return timeline;
    }
}
