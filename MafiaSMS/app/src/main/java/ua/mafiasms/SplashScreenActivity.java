package ua.mafiasms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ua.mafiasms.constants.App;
import ua.mafiasms.helpers.Tools;


public class SplashScreenActivity extends Activity {

    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        tvInfo.setTypeface(Tools.getFont(this, App.MTypeface.ROBOTO_REGULAR));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent startListGetContacts = new Intent(SplashScreenActivity.this, GetContactsActivity.class);
                startListGetContacts.putExtra(GetContactsActivity.TypeRequestActivity.KEY,
                        GetContactsActivity.TypeRequestActivity.CREATE);
                startActivity(startListGetContacts);
            }
        }, 2000);
    }
}
