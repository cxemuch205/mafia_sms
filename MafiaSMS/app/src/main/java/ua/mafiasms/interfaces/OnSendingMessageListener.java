package ua.mafiasms.interfaces;

/**
 * Created by daniil on 9/29/14.
 */
public interface OnSendingMessageListener {
    void onStartSending();
    void onResultSending(int result);
    void onProgressMessage(String msg);
}
