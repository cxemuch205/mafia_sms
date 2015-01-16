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
import ua.mafiasms.constants.App;
import ua.mafiasms.helpers.Tools;
import ua.mafiasms.models.Info;

/**
 * Created by daniil on 9/26/14.
 */
public class InfoAdapter extends ArrayAdapter<Info> {

    private Context context;
    private ArrayList<Info> data;

    public InfoAdapter(Context context, ArrayList<Info> data){
        super(context, R.layout.item_info, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_info, parent, false);

            holder = new ViewHolder();
            holder.tvData = (TextView) view.findViewById(R.id.tv_data);
            holder.tvData.setTypeface(Tools.getFont(context, App.MTypeface.ROBOTO_REGULAR));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Info item = data.get(position);
        holder.tvData.setText(item.data);

        return view;
    }

    public void add(int index, Info info) {
        data.add(index, info);
        notifyDataSetChanged();
    }

    static class ViewHolder{
        TextView tvData;
    }
}
