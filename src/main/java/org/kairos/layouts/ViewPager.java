package org.kairos.layouts;

import javafx.scene.layout.StackPane;

/**
 * Created by Felipe on 04/11/2015.
 */
public class ViewPager extends StackPane {
    private PagerAdapter adapter;
    protected String id;
    private OnPageChangeListener onPageChangeListener;

    public ViewPager(){
        id="view-pager";
        setId(id);
    }
    public void setAdapter(PagerAdapter adapter) {
        this.adapter = adapter;
    }

    public PagerAdapter getAdapter() {
        return adapter;
    }

    public void setCurrentItem(int position){
        adapter.instantiateItem(this,position);
        if(onPageChangeListener!=null) {
            onPageChangeListener.onPageSelected(position);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public static interface OnPageChangeListener{
        public abstract void onPageSelected(int position);
    }
}
