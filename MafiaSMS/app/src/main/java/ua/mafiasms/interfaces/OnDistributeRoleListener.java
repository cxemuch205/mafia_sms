package ua.mafiasms.interfaces;

import android.app.Activity;

import java.util.ArrayList;

import ua.mafiasms.constants.Game;
import ua.mafiasms.models.Contact;

/**
 * Created by daniil on 9/26/14.
 */
public interface OnDistributeRoleListener {
    void onProgressDistributing(Activity activity, String msgStage);
    void onStartDistribute(Activity activity);
    void onEndDistribute(Activity activity, ArrayList<Contact> listContactWithRole);
}
