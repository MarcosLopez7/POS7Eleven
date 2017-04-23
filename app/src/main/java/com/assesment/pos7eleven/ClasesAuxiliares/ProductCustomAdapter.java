package com.assesment.pos7eleven.ClasesAuxiliares;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.assesment.pos7eleven.R;

import java.util.ArrayList;

/**
 * Created by marco on 22/04/2017.
 */

public class ProductCustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ArrayList<String>> products;

    public ProductCustomAdapter(Context context, ArrayList<ArrayList<String>> products){
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(products.get(position).get(3)) ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = View.inflate(context, R.layout.list_user, null);

        TextView productNameText = (TextView) customView.findViewById(R.id.productTVLU);
        TextView priceText = (TextView) customView.findViewById(R.id.priceTVLU);
        TextView quantityText = (TextView) customView.findViewById(R.id.quantityTVLU);
        Button deleteButton = (Button) customView.findViewById(R.id.deleteProductButton);

        productNameText.setText(products.get(position).get(0));
        priceText.setText(products.get(position).get(1));
        quantityText.setText(products.get(position).get(2));

        return customView;
    }
}
