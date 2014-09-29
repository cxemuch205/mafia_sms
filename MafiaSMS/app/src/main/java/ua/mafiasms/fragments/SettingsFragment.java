package ua.mafiasms.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import ua.mafiasms.R;
import ua.mafiasms.constants.App;

/**
 * Created by daniil on 9/26/14.
 */
public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

    private CheckBox cbUseDoctor, cbUseSniper, cbUseDonMafia;
    private Switch sSendingEnable;
    private SharedPreferences pref;

    private static SettingsFragment instance;

    public static SettingsFragment getInstance(){
        if(instance == null)
            instance = new SettingsFragment();
        return instance;
    }

    @SuppressLint("ValidFragment")
    private SettingsFragment(){};

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);
        pref = activity.getSharedPreferences(App.Pref.NAME_PREF, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View llRoot = inflater.inflate(R.layout.fragment_settings, container, false);
        cbUseDoctor = (CheckBox) llRoot.findViewById(R.id.cb_use_doctor);
        cbUseSniper = (CheckBox) llRoot.findViewById(R.id.cb_use_sniper);
        cbUseDonMafia = (CheckBox) llRoot.findViewById(R.id.cb_use_don);
        sSendingEnable = (Switch) llRoot.findViewById(R.id.s_enable_sending);
        return llRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        boolean useDoctor = pref.getBoolean(App.Pref.WITH_DOCTOR, true);
        boolean useSniper = pref.getBoolean(App.Pref.WITH_SNIPER, true);
        boolean useDon = pref.getBoolean(App.Pref.WITH_DON, true);
        boolean enableSending = pref.getBoolean(App.Pref.ENABLE_SENDING, false);

        cbUseDoctor.setChecked(useDoctor);
        cbUseSniper.setChecked(useSniper);
        cbUseDonMafia.setChecked(useDon);
        sSendingEnable.setChecked(enableSending);

        cbUseDoctor.setOnCheckedChangeListener(checkedChangeListener);
        cbUseSniper.setOnCheckedChangeListener(checkedChangeListener);
        cbUseDonMafia.setOnCheckedChangeListener(checkedChangeListener);
        sSendingEnable.setOnCheckedChangeListener(checkedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences.Editor editor = pref.edit();
            switch (buttonView.getId()) {
                case R.id.cb_use_doctor:
                    editor.putBoolean(App.Pref.WITH_DOCTOR, isChecked).commit();
                    break;
                case R.id.cb_use_sniper:
                    editor.putBoolean(App.Pref.WITH_SNIPER, isChecked).commit();
                    break;
                case R.id.cb_use_don:
                    editor.putBoolean(App.Pref.WITH_DON, isChecked).commit();
                    break;
                case R.id.s_enable_sending:
                    editor.putBoolean(App.Pref.ENABLE_SENDING, isChecked).commit();
                    break;
            }
        }
    };
}
