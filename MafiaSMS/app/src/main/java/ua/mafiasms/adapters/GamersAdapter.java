package ua.mafiasms.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ua.mafiasms.R;
import ua.mafiasms.helpers.GameHelper;
import ua.mafiasms.models.Contact;

/**
 * Created by daniil on 9/29/14.
 */
public class GamersAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private ArrayList<Contact> data;

    public GamersAdapter(Context context, ArrayList<Contact> data) {
        super(context, R.layout.item_gamers, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_gamers, null);

            holder = new ViewHolder();
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.tvRole = (TextView) view.findViewById(R.id.tv_role);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Contact item = data.get(position);
        holder.tvName.setText(item.name);
        holder.tvRole.setText(GameHelper.getNameRoleById(context, item.role));

        return view;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvRole;
    }
}
