package com.blandware.android.atleap.loader;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.List;

/**
 * Adapter which takes a list of beans and convert them into List of Maps.
 */
public class BeanAdapter<T> extends SimpleAdapter {


    /**
     * Constructor
     *
     * @param context The context where the View associated with this SimpleAdapter is running
     * @param data A List of Beans. Each entry in the List corresponds to one row in the list. The
     *        Bean contain the data for each row, and should include all the entries specified in
     *        "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *        item. The layout file should include at least those named views defined in "to"
     * @param from A list of column names that will be added to the Map associated with each
     *        item.
     * @param to The views that should display column in the "from" parameter. These should all be
     *        TextViews. The first N views in this list are given the values of the first N columns
     *        in the from parameter.
     */
    public BeanAdapter(Context context, List<T> data,
                       int resource, String[] from, int[] to) {
        super(context, CursorUtil.listOfBeansToListOfMaps(data, from), resource, from, to);
    }
}
