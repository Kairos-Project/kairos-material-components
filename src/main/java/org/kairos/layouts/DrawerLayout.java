package org.kairos.layouts;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Created by Felipe on 02/11/2015.
 */
public class DrawerLayout extends AnchorPane {
    private Pane content, nav;
    private final SimpleBooleanProperty tableScreen = new SimpleBooleanProperty();
    private static final double DEFAULT_WIDTH_NAV = 256;
    private DrawerListener drawerListener;
    private final Pane toggleLayer = new Pane();

    private final static Duration DEFAULT_TIME_ANIM = new Duration(200);

    private boolean drawerOpened = false;

    public boolean isDrawerOpened() {
        return drawerOpened;
    }

    public DrawerLayout() {
        AnchorPane.setTopAnchor(toggleLayer, 0d);
        AnchorPane.setRightAnchor(toggleLayer, 0d);
        AnchorPane.setBottomAnchor(toggleLayer, 0d);
        AnchorPane.setLeftAnchor(toggleLayer, 0d);
        toggleLayer.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0d), new Insets(0))));
        toggleLayer.setOpacity(0);
        toggleLayer.setVisible(false);

        toggleLayer.setOnMouseClicked(evt -> {
            if (evt.getButton().equals(MouseButton.PRIMARY)) {
                if (drawerOpened) {
                    closeDrawer();
                }
            }
        });

        tableScreen.bind(widthProperty().lessThan(800));
        tableScreen.addListener((observable, oldValue, newValue) -> {
            responsiveBehavior(newValue);
        });

    }

    private void responsiveBehavior(boolean tabletScreen) {
        if (content != null && nav != null) {
            if (tabletScreen) {
                AnchorPane.setLeftAnchor(content, 0d);
                nav.setTranslateX(-DEFAULT_WIDTH_NAV);
            } else {
                AnchorPane.setLeftAnchor(content, DEFAULT_WIDTH_NAV);
                nav.setTranslateX(0);
                toggleLayer.setVisible(false);
                drawerOpened=false;
            }
        }
    }

    public Pane getContent() {
        return content;
    }

    public void setContent(Pane content) {
        this.content = content;
        AnchorPane.setTopAnchor(content, 0d);
        AnchorPane.setRightAnchor(content, 0d);
        AnchorPane.setBottomAnchor(content, 0d);
        AnchorPane.setLeftAnchor(content, DEFAULT_WIDTH_NAV);
        getChildren().add(content);
        getChildren().add(toggleLayer);

    }

    public Pane getNav() {
        return nav;
    }

    public void setNav(Pane nav) {
        this.nav = nav;
        nav.setPrefWidth(DEFAULT_WIDTH_NAV);
        AnchorPane.setTopAnchor(nav, 0d);
        AnchorPane.setBottomAnchor(nav, 0d);
        getChildren().add(nav);
    }

    public void setDrawerListener(DrawerListener drawerListener) {
        this.drawerListener = drawerListener;
    }

    public void openDrawer() {
        if (tableScreen.getValue()) {
            drawerOpened = true;
            drawerAnimation();
            if (drawerListener != null) {
                drawerListener.onDrawerOpened(nav);
            }
        }
    }

    public void closeDrawer() {
        if (tableScreen.getValue()) {
            drawerOpened = false;
            drawerAnimation();
            if (drawerListener != null) {
                drawerListener.onDrawerClosed(nav);
            }
        }
    }

    private void drawerAnimation() {
        Timeline animation;
        if (drawerOpened) {
            toggleLayer.setVisible(true);
            animation = new Timeline(new KeyFrame(DEFAULT_TIME_ANIM,
                    new KeyValue(nav.translateXProperty(), 0, Interpolator.EASE_OUT),
                    new KeyValue(toggleLayer.opacityProperty(), 0.3)
            ));
        } else {
            animation = new Timeline(new KeyFrame(DEFAULT_TIME_ANIM,
                    new KeyValue(nav.translateXProperty(), -DEFAULT_WIDTH_NAV,Interpolator.EASE_IN),
                    new KeyValue(toggleLayer.opacityProperty(), 0)
            ));
            animation.setOnFinished(evt -> {
                toggleLayer.setOpacity(0);
                toggleLayer.setVisible(false);
            });
        }
        animation.play();
    }

    public static interface DrawerListener {
        public abstract void onDrawerClosed(Node node);

        public abstract void onDrawerOpened(Node node);

    }
}
