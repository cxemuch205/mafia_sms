package ua.mafiasms.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ua.mafiasms.R;
import ua.mafiasms.constants.App;
import ua.mafiasms.helpers.Tools;
import ua.mafiasms.models.Contact;

/**
 * Created by daniil on 9/26/14.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private ArrayList<Contact> data, backup;

    public ContactsAdapter(Context context, ArrayList<Contact> data){
        super(context, R.layout.item_contact, data);
        this.context = context;
        this.data = data;
        backup = new ArrayList<Contact>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if(view == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_contact, null);

            holder = new ViewHolder();
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.tvNumberPhone = (TextView) view.findViewById(R.id.tv_phone_number);
            holder.cbSelect = (CheckBox) view.findViewById(R.id.cb_select);
            holder.tvName.setTypeface(Tools.getFont(context, App.MTypeface.ROBOTO_LIGHT));
            holder.tvNumberPhone.setTypeface(Tools.getFont(context, App.MTypeface.ROBOTO_THIN));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Contact item = data.get(position);
        holder.tvName.setText(item.name);
        holder.tvNumberPhone.setText(item.phoneNumber);

        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.isSelect = isChecked;
            }
        });

        holder.cbSelect.setChecked(item.isSelect);

        return view;
    }

    public void itemClickUpdate(int position) {
        if (position > -1 && position < data.size()) {
            Contact contact = data.get(position);
            if (contact != null) {
                contact.isSelect = !contact.isSelect;
                notifyDataSetChanged();
            }
        }
    }

    public void addAll(Collection<? extends Contact> collection, boolean isFilter) {
        super.addAll(collection);
        if (!isFilter) {
            backup.clear();
            backup.addAll(collection);
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results){
                if(results != null && results.count > 0){
                    clear();
                    addAll((ArrayList<Contact>)results.values, true);
                    notifyDataSetChanged();
                } else {
                    if(results == null || (results != null && results.count == 0)){
                        updateDefault();
                    }
                    notifyDataSetInvalidated();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new Filter.FilterResults();
                List<Contact> results = new ArrayList<Contact>();
                filterResults.values = results;
                if(constraint != null){
                    if(data.size() > 0){
                        for (Contact item : data) {
                            String request = constraint.subSequence(0, 1).toString().toUpperCase()
                                    + constraint.subSequence(1, constraint.length());
                            if(item.name.contains(constraint)
                                    || item.phoneNumber.contains(constraint)
                                    || item.name.contains(request)){
                                results.add(item);
                            }
                        }
                    }
                    filterResults.count = results.size();
                    filterResults.values = results;
                }
                return filterResults;
            }
        };
        return filter;
    }

    public void updateDefault(){
        clear();
        addAll(backup);
        notifyDataSetChanged();
    }

    public ArrayList<Contact> getListSelectedContacts(){
        ArrayList<Contact> result = new ArrayList<Contact>();
        for (Contact item : backup){
            if(item.isSelect)
                result.add(item);
        }
        return result;
    }

    public void add(int index, Contact contact) {
        data.add(index, contact);
        backup.add(index, contact);
        notifyDataSetChanged();
    }

    static class ViewHolder{
        TextView tvName;
        TextView tvNumberPhone;
        CheckBox cbSelect;
    }
}
