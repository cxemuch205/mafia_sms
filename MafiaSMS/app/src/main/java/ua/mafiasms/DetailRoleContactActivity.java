package ua.mafiasms;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ua.mafiasms.constants.App;
import ua.mafiasms.helpers.GameHelper;
import ua.mafiasms.helpers.Tools;
import ua.mafiasms.models.Contact;


public class DetailRoleContactActivity extends ActionBarActivity {

    public static final String TAG = "DetailRoleContactActivity";

    private Contact mContact;
    private TextView tvName, tvRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_role_contact);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvRole = (TextView) findViewById(R.id.tv_role);

        tvName.setTypeface(Tools.getFont(this, App.MTypeface.ROBOTO_THIN));
        tvRole.setTypeface(Tools.getFont(this, App.MTypeface.ROBOTO_THIN));

        getSupportActionBar().hide();
        mContact = (Contact)getIntent().getExtras().getSerializable(App.IntentKeys.CONTACT_OBJ);

        tvName.setText(mContact.name.substring(0, 1) + mContact.name.substring(1, mContact.name.length()).toLowerCase());
        tvRole.setText(GameHelper.getNameRoleById(this, mContact.role));
    }
}
