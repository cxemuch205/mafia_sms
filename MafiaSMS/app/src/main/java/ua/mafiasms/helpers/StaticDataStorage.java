package ua.mafiasms.helpers;

import java.util.ArrayList;

import ua.mafiasms.models.Contact;

/**
 * Created by daniil on 9/26/14.
 */
public class StaticDataStorage {

    public static ArrayList<Contact> listCurrentContacts = new ArrayList<Contact>();

    public static boolean withRole = false;

    public static void addAllContactsInCurrentList(ArrayList<Contact> listSelectedContacts, boolean wRole) {
        listCurrentContacts.clear();
        listCurrentContacts.addAll(listSelectedContacts);
        withRole = wRole;
    }

    public static ArrayList<Contact> getListCurrentContacts() {
        return listCurrentContacts;
    }
}
