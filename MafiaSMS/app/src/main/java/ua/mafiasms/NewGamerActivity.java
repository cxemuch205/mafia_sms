package ua.mafiasms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ua.mafiasms.constants.App;
import ua.mafiasms.models.Contact;


public class NewGamerActivity extends Activity {

    public static final String TAG = "NewGamerActivity";

    private EditText etName, etPhoneNumber;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gamer);
        etName = (EditText) findViewById(R.id.et_name);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        btnAdd = (Button) findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(clickAddListener);
    }

    private View.OnClickListener clickAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = etName.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            if (name != null && name.length() > 0) {
                Contact contact = new Contact();
                contact.name = name;
                contact.phoneNumber = phoneNumber;
                contact.isSelect = true;

                Intent result = new Intent();
                result.putExtra(App.IntentKeys.CONTACT_OBJ, contact);
                setResult(RESULT_OK, result);
                finish();
            } else {
                Toast toast = Toast.makeText(NewGamerActivity.this, getString(R.string.enter_name), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0 , 0);
                toast.show();
            }
        }
    };
}
