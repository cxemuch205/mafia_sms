package ua.mafiasms;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import ua.mafiasms.adapters.ContactsAdapter;
import ua.mafiasms.constants.App;
import ua.mafiasms.constants.Game;
import ua.mafiasms.helpers.StaticDataStorage;
import ua.mafiasms.helpers.Tools;
import ua.mafiasms.models.Contact;


public class GetContactsActivity extends Activity {

    public static final String TAG = "GetContactsActivity";

    private ContactsAdapter adapter;
    private ListView lvContacts;
    private EditText etFilter;
    private ProgressBar pb;
    private int TYPE_REQUEST;

    public interface TypeRequestActivity {
        public static final String KEY = "type_opened";
        public static final int CREATE = 0;
        public static final int EDIT = 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contacts);
        lvContacts = (ListView) findViewById(R.id.lv_contacts);
        pb = (ProgressBar) findViewById(R.id.pb_load);
        etFilter = (EditText) findViewById(R.id.et_filter);

        adapter = new ContactsAdapter(this, new ArrayList<Contact>());

        lvContacts.setAdapter(adapter);
        lvContacts.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lvContacts.setOnItemClickListener(itemContactClickListener);

        etFilter.addTextChangedListener(watchETListener);
        etFilter.setTypeface(Tools.getFont(this, App.MTypeface.COMFORTA_LIGHT));

        TYPE_REQUEST = getIntent().getIntExtra(TypeRequestActivity.KEY, TypeRequestActivity.CREATE);

        if (TYPE_REQUEST == TypeRequestActivity.CREATE) {
            getActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private TextWatcher watchETListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(count > 0)
                adapter.getFilter().filter(s.toString());
            else
                adapter.updateDefault();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void enablePB(final boolean enable){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(enable) {
                    etFilter.setEnabled(false);
                    pb.setVisibility(ProgressBar.VISIBLE);
                    lvContacts.setVisibility(ListView.GONE);
                    } else {
                    itemMenuOk.setVisible(true);
                    etFilter.setEnabled(true);
                    lvContacts.setVisibility(ListView.VISIBLE);
                    pb.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }

    private void performGetDataContacts() {
        enablePB(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);

                final ArrayList<Contact> list = new ArrayList<Contact>();
                while (phones.moveToNext())
                {
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    Contact item = new Contact();
                    item._id = id;
                    item.name = name;
                    item.phoneNumber = phoneNumber;
                    boolean isUniq = true;
                    for (int i = 0; i < list.size(); i++){
                        Contact it = list.get(i);
                        if ((it.name.contentEquals(item.name)
                                && it.phoneNumber.contentEquals(item.phoneNumber))
                                || !Patterns.PHONE.matcher(item.phoneNumber).matches()
                                || item.phoneNumber.contains(".")
                                || item.phoneNumber.contains("+1")
                                || item.phoneNumber.contains("*")) {
                            isUniq = false;
                            break;
                        }
                    }
                    if(isUniq){
                        list.add(item);
                    }
                }
                Collections.sort(list, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact lhs, Contact rhs) {
                        return lhs.name.compareTo(rhs.name);
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enablePB(false);
                        for (Contact contact : list) {
                            for (Contact c : StaticDataStorage.getListCurrentContacts()) {
                                if (contact._id.contentEquals(c._id)) {
                                    contact.isSelect = true;
                                    break;
                                }
                            }
                        }
                        adapter.addAll(list, false);
                    }
                });
            }
        }).start();
    }

    private AdapterView.OnItemClickListener itemContactClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    private MenuItem itemMenuOk;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.get_contacts, menu);
        itemMenuOk = menu.findItem(R.id.action_ok);
        itemMenuOk.setVisible(false);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        performGetDataContacts();
        Tools.hideKeyboard(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_ok && pb.getVisibility() != ProgressBar.VISIBLE) {
            ArrayList<Contact> list = adapter.getListSelectedContacts();
            if (list.size() >= Game.MIN_GAMERS) {
                StaticDataStorage.addAllContactsInCurrentList(list, false);

                if (TYPE_REQUEST == TypeRequestActivity.CREATE) {
                    Intent startGamePanel = new Intent(this, ProcessActivity.class);//GameProcessActivity.class);
                    startActivity(startGamePanel);
                    finish();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                String text = getString(R.string.select_items)+ ", you select %d";
                Toast toast = Toast.makeText(this, String.format(text,
                        Game.MIN_GAMERS, list.size()), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.getView().setBackgroundColor(Color.parseColor("#bbFF0000"));
                toast.show();
            }
            return true;
        } else if (id == android.R.id.home) {
            finish();
            setResult(RESULT_CANCELED);
        }
        return super.onOptionsItemSelected(item);
    }
}
