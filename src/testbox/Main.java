package testbox;

import support.Message;
import support.TimeManager;

public class Main {
    public static void main(String[] args) {
        System.out.println(TimeManager.getDateWithTime(System.currentTimeMillis()));
        System.out.println(TimeManager.getDate(System.currentTimeMillis()));
    }
}
