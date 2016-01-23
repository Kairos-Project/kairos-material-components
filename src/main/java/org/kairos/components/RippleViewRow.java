package org.kairos.components;

import com.sun.javafx.scene.control.skin.ListCellSkin;
import javafx.scene.control.Skin;
import org.kairos.layouts.RecyclerView;

/**
 * Created by Felipe on 03/11/2015.
 */
public class RippleViewRow extends RecyclerView.ViewRow {

    public RippleViewRow(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        ListCellSkin listCellSkin=new ListCellSkin(this);
        RippleSkinFactory.getRippleEffect(listCellSkin, this);
        return listCellSkin;
    }
}
