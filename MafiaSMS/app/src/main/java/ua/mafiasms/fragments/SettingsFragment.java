package ua.mafiasms.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.mafiasms.R;

/**
 * Created by daniil on 9/26/14.
 */
public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View llRoot = inflater.inflate(R.layout.fragment_settings, container, false);

        return llRoot;
    }
}
