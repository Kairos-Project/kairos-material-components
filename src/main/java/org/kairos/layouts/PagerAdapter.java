package org.kairos.layouts;

import javafx.scene.layout.Pane;

/**
 * Created by Felipe on 04/11/2015.
 */
public abstract class PagerAdapter {

    public abstract String getPageTitle(int position);
    public abstract int getCount();
    public abstract Object instantiateItem(Pane container,int position);

}
