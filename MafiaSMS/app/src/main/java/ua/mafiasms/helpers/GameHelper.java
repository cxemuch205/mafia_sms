package ua.mafiasms.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ua.mafiasms.R;
import ua.mafiasms.constants.App;
import ua.mafiasms.constants.Game;
import ua.mafiasms.interfaces.OnDistributeRoleListener;
import ua.mafiasms.interfaces.OnSendingMessageListener;
import ua.mafiasms.models.Contact;

/**
 * Created by daniil on 9/26/14.
 */
public class GameHelper {

    public static final String TAG = "GameHelper";

    public static void runDistributeRoles(final Activity activity, final ArrayList<Contact> listContacts, final OnDistributeRoleListener listener, final int maxMafia) {
        if (listener != null) {
            listener.onProgressDistributing(activity, activity.getString(R.string.progress_start_distributing));
            listener.onStartDistribute(activity);
        }
        final SharedPreferences pref = activity.getSharedPreferences(App.Pref.NAME_PREF, 0);
        final boolean useDon = pref.getBoolean(App.Pref.WITH_DON, true);
        final boolean useDoctor = pref.getBoolean(App.Pref.WITH_DOCTOR, true);
        final boolean useSniper = pref.getBoolean(App.Pref.WITH_SNIPER, true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onProgressDistributing(activity, activity.getString(R.string.progress_get_user_ids));
                    }
                });
                final ArrayList<Contact> listContactWithRole = new ArrayList<Contact>();
                Random random = new Random();
                ArrayList<Integer> ids = new ArrayList<Integer>();
                for (int i = 0; i < listContacts.size(); i++) {
                    boolean isUniq = true;
                    int newValue = random.nextInt(listContacts.size());
                    for (int j = 0; j < ids.size(); j++) {
                        if(ids.contains(newValue)){
                            isUniq = false;
                            break;
                        }
                    }
                    if(isUniq)
                        ids.add(newValue);
                    else
                        i--;
                }
                for (Integer i : ids) {
                    Log.d(TAG, "ids: " + i);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onProgressDistributing(activity, activity.getString(R.string.progress_apply_role));
                    }
                });
                int keyRole = Game.Role.MAFIA;
                if(useDon)
                    keyRole = Game.Role.DON_MAFIA;
                int countMafia = 0;
                for (int i = 0; i < listContacts.size(); i++) {
                    Contact item = listContacts.get(ids.get(i));

                    if (countMafia > 0 && countMafia < maxMafia) {
                        keyRole = Game.Role.MAFIA;
                    } else if (countMafia == maxMafia) {
                        keyRole = Game.Role.SHERIFF;
                    } else if (countMafia == (maxMafia+1) && useDoctor) {
                        keyRole = Game.Role.DOC;
                    } else if (countMafia >= (maxMafia+2) && countMafia < (listContacts.size() - 1)) {
                        keyRole = Game.Role.CITIZEN;
                    } else if (countMafia == (listContacts.size() - 1) && (listContacts.size() >= (Game.MIN_GAMERS + 1) || !useDoctor)
                            && useSniper) {
                        keyRole = Game.Role.SNIPER;
                    }

                    item.role = keyRole;
                    keyRole = Game.Role.CITIZEN;
                    countMafia++;
                    listContactWithRole.add(item);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onProgressDistributing(activity, activity.getString(R.string.ending));
                        listener.onEndDistribute(activity, listContactWithRole);
                    }
                });
            }
        }).start();
    }

    public static void sendMessages(final SmsManager smsManager, final Activity activity, final ArrayList<Contact> listContactWithRole, final OnSendingMessageListener listener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onStartSending();
                listener.onProgressMessage(activity.getString(R.string.progress_sending_msg));
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < listContactWithRole.size(); i++) {
                    Contact contact = listContactWithRole.get(i);
                    final String msgSendData = activity.getString(R.string.send_msg_to)
                            + " " + contact.name
                            + activity.getString(R.string.next_line_role) + " "
                            + getNameRoleById(activity, contact.role);
                    String message = "MAFIA\n" + activity.getString(R.string.next_line_role) + " "
                            + getNameRoleById(activity, contact.role);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onProgressMessage(msgSendData);
                        }
                    });
                    sendMessageToContact(activity.getSharedPreferences(App.Pref.NAME_PREF, 0), smsManager, contact, message);
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, msgSendData);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResultSending(Game.SendingResult.SENDING_SUCCESS);
                    }
                });
            }
        }).start();
    }

    public static void sendMessageToContact(SharedPreferences pref, SmsManager smsManager, Contact contact, String message) {
        if (pref.getBoolean(App.Pref.ENABLE_SENDING, false)) {
            Log.d(TAG, "SEND ENABLED");
            smsManager.sendTextMessage(
                    contact.phoneNumber,
                    null,
                    message,
                    null,
                    null
            );
        }
    }

    public static int getRecommendedNumberOfMafia(int countGames){
        return (int)(countGames / Game.K_MAFIA);
    }

    public static String getNameRoleById(Context context, int role) {
        switch (role) {
            case Game.Role.DON_MAFIA:
                return context.getString(R.string.role_don_mafia);
            case Game.Role.MAFIA:
                return context.getString(R.string.role_mafia);
            case Game.Role.SHERIFF:
                return context.getString(R.string.role_sheriff);
            case Game.Role.DOC:
                return context.getString(R.string.role_doctor);
            case Game.Role.SNIPER:
                return context.getString(R.string.role_sniper);
            default:
                return context.getString(R.string.role_citizen);
        }
    }
}
