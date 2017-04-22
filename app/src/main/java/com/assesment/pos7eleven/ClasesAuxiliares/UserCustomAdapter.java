package com.assesment.pos7eleven.ClasesAuxiliares;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.assesment.pos7eleven.R;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Created by marco on 17/04/2017.
 */
public class UserCustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ArrayList<String>> users;

    public UserCustomAdapter(Context context, ArrayList<ArrayList<String>> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(users.get(position).get(4)) ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View customView = View.inflate(context, R.layout.list_user, null);

        TextView nameText = (TextView) customView.findViewById(R.id.nameTVLU);
        TextView lastnameText = (TextView) customView.findViewById(R.id.lastnameTVLU);
        TextView positionText = (TextView) customView.findViewById(R.id.positionTVLU);
        TextView storeText = (TextView) customView.findViewById(R.id.storeTVLU);

        nameText.setText(users.get(position).get(0));
        lastnameText.setText(users.get(position).get(1));
        positionText.setText(users.get(position).get(2));
        storeText.setText(users.get(position).get(3));

        return customView;
    }
}
