package support;

public class UserData {
    public String id, login, password, nickname, avatar, description, status, last_online, messages, painted, joined, paint_delay;

    public UserData(String id, String login, String password, String nickname, String avatar, String description, String status, String last_online){ this.id=id;this.login=login;this.password=password;this.nickname=nickname;this.avatar =avatar;this.description=description;this.status=status; this.last_online=last_online; }

    public UserData(String id, String login, String password, String nickname, String avatar, String description, String status, String last_online, String messages, String painted, String joined, String paint_delay) { this.id = id;this.login = login;this.password = password;this.nickname = nickname;this.avatar = avatar;this.description = description;this.status = status;this.last_online = last_online;this.messages = messages;this.painted = painted;this.joined = joined;this.paint_delay = paint_delay; }

    public String toStr(){
        StringBuilder result=new StringBuilder("");
        result.append(id+" "+login+" "+password+" "+nickname+" "+ avatar +" "+description+" "+status+" "+last_online+" "+messages+" "+painted+" "+joined+" "+paint_delay);
        return result.toString();
    }
}
