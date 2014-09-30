package ua.mafiasms.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ua.mafiasms.NavigationDrawerFragment;
import ua.mafiasms.R;
import ua.mafiasms.adapters.InfoAdapter;
import ua.mafiasms.constants.Game;
import ua.mafiasms.helpers.GameHelper;
import ua.mafiasms.helpers.StaticDataStorage;
import ua.mafiasms.interfaces.OnDistributeRoleListener;
import ua.mafiasms.interfaces.OnSendingMessageListener;
import ua.mafiasms.models.Contact;
import ua.mafiasms.models.Info;

/**
 * Created by daniil on 9/26/14.
 */
public class ControlPanelFragment extends Fragment {

    public static final String TAG = "ControlPanelFragment";
    public static final int INDEX = 0;

    private Button btnDistrRole, btnSend;
    private ProgressDialog pd;
    private EditText etMaxMafia;
    private InfoAdapter adapter;
    private ListView lvInfo;
    private int maxMafia;
    private SmsManager smsManager;
    private static ArrayList<Info> listInfo = new ArrayList<Info>();

    private static ControlPanelFragment instance;

    public static ControlPanelFragment getInstance(){
        if(instance == null)
            instance = new ControlPanelFragment();
        return instance;
    }

    @SuppressLint("ValidFragment")
    private ControlPanelFragment(){};

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);
        smsManager = SmsManager.getDefault();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View llRoot = inflater.inflate(R.layout.fragment_controll, container, false);
        btnDistrRole = (Button) llRoot.findViewById(R.id.btn_distribute_role);
        btnSend = (Button) llRoot.findViewById(R.id.btn_send_msgs);
        etMaxMafia = (EditText) llRoot.findViewById(R.id.et_max_mafia);
        lvInfo = (ListView) llRoot.findViewById(R.id.lv_info);
        return llRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnDistrRole.setOnClickListener(clickBtnListener);
        btnSend.setOnClickListener(clickBtnListener);
        initProgressDialog();

        enableBtnSend(StaticDataStorage.withRole);
        adapter = new InfoAdapter(getActivity(), listInfo);
        if(!StaticDataStorage.withRole){
            Info infoCountGamers = new Info();
            infoCountGamers.data = "Added contacts to game: " + String.valueOf(StaticDataStorage.listCurrentContacts.size());
            adapter.add(0, infoCountGamers);
        }
        lvInfo.setAdapter(adapter);
        maxMafia = GameHelper.getRecommendedNumberOfMafia(StaticDataStorage.listCurrentContacts.size());
        etMaxMafia.setHint(String.format(getString(R.string.recommended_number__of_maia), maxMafia));
    }

    private void initProgressDialog() {
        pd = ProgressDialog.show(getActivity(), "", "");
        pd.setTitle(null);
        pd.dismiss();
    }

    private void enableBtnSend(boolean enable) {
        if (btnSend != null) {
            btnSend.setEnabled(enable);
        }
    }

    private View.OnClickListener clickBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_distribute_role:
                    if(etMaxMafia.getText().toString().length() > 0){
                        maxMafia = Integer.parseInt(etMaxMafia.getText().toString());
                    }
                    GameHelper.runDistributeRoles(getActivity(), StaticDataStorage.getListCurrentContacts(), distributeRoleListener, maxMafia);
                    break;
                case R.id.btn_send_msgs:
                    GameHelper.sendMessages(smsManager, getActivity(), StaticDataStorage.getListCurrentContacts(), sendingMessageListener);
                    break;
            }
        }
    };

    private OnDistributeRoleListener distributeRoleListener = new OnDistributeRoleListener() {
        @Override
        public void onProgressDistributing(Activity activity, final String msgStage) {
            if (pd != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.setMessage(msgStage);
                    }
                });
            }
        }

        @Override
        public void onStartDistribute(Activity activity) {
            if (pd != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.show();
                    }
                });
            }
        }

        @Override
        public void onEndDistribute(Activity activity, ArrayList<Contact> listContactWithRole) {
            StaticDataStorage.addAllContactsInCurrentList(listContactWithRole, true);
            if (pd != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        adapter.add(0, new Info("Distribute role to contact!"));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btnSend.performClick();
                            }
                        }, 700);
                    }
                });
            }
            enableBtnSend(true);
        }
    };

    private OnSendingMessageListener sendingMessageListener = new OnSendingMessageListener() {
        @Override
        public void onStartSending() {
            if (pd != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.show();
                    }
                });
            }
        }

        @Override
        public void onResultSending(int result) {
            switch (result) {
                case Game.SendingResult.ERROR_CAN_NOT_SENDING:
                    Log.e(TAG, "onResultSending(): ERROR_CAN_NOT_SENDING");
                    break;
                case Game.SendingResult.SENDING_SUCCESS:
                    Log.d(TAG, "onResultSending(): SENDING_SUCCESS");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.add(0, new Info("Sending messages is SUCCESS!"));
                            Toast toast = Toast.makeText(getActivity(), "SENDING SUCCESS", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                    NavigationDrawerFragment.getInstance().selectItem(ListGamersFragment.INDEX);
                    break;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pd != null) {
                        pd.dismiss();
                    }
                }
            });
        }

        @Override
        public void onProgressMessage(final String msg) {
            if (pd != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.setMessage(msg);
                    }
                });
            }
        }
    };
}
