package ua.mafiasms.models;

import ua.mafiasms.constants.Game;

/**
 * Created by daniil on 9/26/14.
 */
public class Contact {

    public String _id;
    public String name;
    public String phoneNumber;
    public int role = Game.Role.CITIZEN;
    public int status = Game.Role.CITIZEN;
    public boolean isSelect;
}
