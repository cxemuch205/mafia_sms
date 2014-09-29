package ua.mafiasms.constants;

import ua.mafiasms.R;

/**
 * Created by daniil on 9/26/14.
 */
public class Game {

    public static final int MIN_GAMERS = 6;
    public static final float K_MAFIA = 3f;

    public interface Role{
        public static final int DON_MAFIA = 0;
        public static final int MAFIA = 1;
        public static final int DOC = 2;
        public static final int SHERIFF = 3;
        public static final int CITIZEN = 4;
        public static final int SNIPER = 5;
    }

    public interface SendingResult{
        public static final int ERROR_CAN_NOT_SENDING = 0;
        public static final int SENDING_SUCCESS = 1;
    }
}
