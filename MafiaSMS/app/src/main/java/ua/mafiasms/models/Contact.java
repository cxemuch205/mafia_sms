package ua.mafiasms.models;

import java.io.Serializable;

import ua.mafiasms.constants.Game;

/**
 * Created by daniil on 9/26/14.
 */
public class Contact implements Serializable{

    public String _id;
    public String name;
    public String phoneNumber;
    public int role = Game.Role.CITIZEN;
    public boolean isSelect;
    public int rowIdInDB;
    public String listFavoriteName;
}
