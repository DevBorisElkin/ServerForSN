package support;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    public String id, nick, date, message;

    public Message(String id, String nick, String date, String message) {
        this.id = id;
        this.nick = nick;
        this.date = date;
        this.message = message;
    }
    public Message(){}

    public String toString(){
        return "@"+id+"@"+nick+"@"+date+"@"+message;
    }
}
