package ua.mafiasms.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ua.mafiasms.DetailRoleContactActivity;
import ua.mafiasms.GetContactsActivity;
import ua.mafiasms.R;
import ua.mafiasms.adapters.GamersAdapter;
import ua.mafiasms.constants.App;
import ua.mafiasms.helpers.StaticDataStorage;
import ua.mafiasms.interfaces.OnSwitchTabListener;
import ua.mafiasms.models.Contact;

/**
 * Created by daniil on 9/26/14.
 */
public class ListGamersFragment extends Fragment {

    public static final String TAG = "ListGamersFragment";
    public static final int INDEX = 1;
    public static final int REQUEST_CHANGE_LIST = 112;

    private ListView lvGamers;
    private GamersAdapter adapterGamers;

    private static ListGamersFragment instance;

    public static ListGamersFragment getInstance(){
        if(instance == null)
            instance = new ListGamersFragment();
        return instance;
    }

    @SuppressLint("ValidFragment")
    private ListGamersFragment(){};

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View llRoot = inflater.inflate(R.layout.fragment_list_gamers, container, false);
        lvGamers = (ListView) llRoot.findViewById(R.id.lv_gamers);
        return llRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        lvGamers.setOnItemClickListener(itemGamersClickListener);
    }

    private AdapterView.OnItemClickListener itemGamersClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent showDetailRole = new Intent(getActivity(), DetailRoleContactActivity.class);
            showDetailRole.putExtra(App.IntentKeys.CONTACT_OBJ, adapterGamers.getItem(position));
            getActivity().startActivity(showDetailRole);
        }
    };

    public void initListData() {
        Log.d(TAG, "initListData()");
        if (adapterGamers == null) {
            ArrayList<Contact> data = new ArrayList<Contact>();
            data.addAll(StaticDataStorage.getListCurrentContacts());
            adapterGamers = new GamersAdapter(getActivity(), data);
            lvGamers.setAdapter(adapterGamers);
        } else {
            adapterGamers.clear();
            adapterGamers.addAll(StaticDataStorage.getListCurrentContacts());
            if (lvGamers.getAdapter() == null) {
                lvGamers.setAdapter(adapterGamers);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_gamers, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_list:
                Intent startListGetContacts = new Intent(getActivity(), GetContactsActivity.class);
                startListGetContacts.putExtra(GetContactsActivity.TypeRequestActivity.KEY,
                        GetContactsActivity.TypeRequestActivity.EDIT);
                startActivityForResult(startListGetContacts, REQUEST_CHANGE_LIST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, String.format("onActivityResult(%d, %d, data", requestCode, resultCode));
        if (requestCode == REQUEST_CHANGE_LIST) {
            if (resultCode == Activity.RESULT_OK) {
                initListData();
                switchTabListener.onSwitch(ControlPanelFragment.INDEX);
            }
        }
    }

    private OnSwitchTabListener switchTabListener;

    public void setOnSwitchTabListener(OnSwitchTabListener listener) {
        this.switchTabListener = listener;
    }
}
