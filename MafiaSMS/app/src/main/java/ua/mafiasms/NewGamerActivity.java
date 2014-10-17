package ua.mafiasms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import ua.mafiasms.constants.App;
import ua.mafiasms.helpers.Tools;
import ua.mafiasms.models.Contact;


public class NewGamerActivity extends Activity {

    public static final String TAG = "NewGamerActivity";
    public static final String CUSTOM = "custom";

    private EditText etName, etPhoneNumber;
    private Button btnAdd;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gamer);
        etName = (EditText) findViewById(R.id.et_name);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        btnAdd = (Button) findViewById(R.id.btn_add);

        etName.setTypeface(Tools.getFont(this, App.MTypeface.COMFORTA_LIGHT));
        etPhoneNumber.setTypeface(Tools.getFont(this, App.MTypeface.COMFORTA_LIGHT));
        btnAdd.setTypeface(Tools.getFont(this, App.MTypeface.COMFORTA_LIGHT));

        btnAdd.setOnClickListener(clickAddListener);
        random = new Random();
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
                contact._id = CUSTOM+"_"+random.nextInt();

                Intent result = new Intent();
                result.putExtra(App.IntentKeys.CONTACT_OBJ, contact);
                setResult(RESULT_OK, result);
                finish();
            } else {
                Toast toast = Toast.makeText(NewGamerActivity.this, getString(R.string.enter_name), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0 , 0);
                toast.getView().setBackgroundColor(Color.parseColor("#bbFF0000"));
                toast.show();
            }
        }
    };
}
