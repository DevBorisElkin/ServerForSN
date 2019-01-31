package testbox;

import database.TimeManager;

public class Main {
    public static void main(String[] args) {
        String a="time$years:1$months:4$hours:1$minutes:40";
        System.out.println(TimeManager.parseString(a));
    }
}
