package org.kairos.layouts;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.IOException;

/**
 * Created by felipe on 20/09/15.
 */
public class RecyclerView<S> extends ListView<S> {

    public void setAdapter(Adapter adapter){
        getStyleClass().clear();
        getStyleClass().add("recycler-view");
        setCellFactory(adapter);
    }

    public Adapter getAdapter(){
        return (Adapter) getCellFactory();
    }


    static abstract public class Adapter<T> implements Callback<ListView,ViewRow>{

        @Override
        public ViewRow call(ListView param) {
            return new ViewRow<T>(this);
        }

        public abstract T onCreateViewHolder(FXMLLoader loader);
        public abstract void onBindViewHolder(T holder,Object item);

    }
    static  public class ViewRow<T> extends ListCell{
        private T holder;
        private Adapter<T> adapter;
        public ViewRow(Adapter<T> adapter){
            this.adapter=adapter;
            getStyleClass().clear();
            setMinWidth(USE_PREF_SIZE);
            setPrefWidth(0);
        }
        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                if(holder==null){
                    holder=adapter.onCreateViewHolder(new FXMLLoader());
                }
                if(holder instanceof ViewHolder){
                    adapter.onBindViewHolder(holder, item);
                    setGraphic(((ViewHolder)holder).view);
                }
            }else{
                setText(null);
                setGraphic(null);
            }
        }
    }
    static abstract public class ViewHolder{

        protected Node view;
        public  ViewHolder(FXMLLoader loader){
            loader.setController(this);
            try {
                loader.load();
                view=loader.getRoot();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Node getView() {
            return view;
        }
    }
}
