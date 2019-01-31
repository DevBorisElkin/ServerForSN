package support;

public class UserData {
    public String id, login, password, nickname, avatar, description, status, last_online;

    public UserData(String id, String login, String password, String nickname, String avatar, String description, String status, String last_online){
        this.id=id;this.login=login;this.password=password;this.nickname=nickname;this.avatar =avatar;this.description=description;this.status=status; this.last_online=last_online;
    }
    public String toStr(){
        StringBuilder result=new StringBuilder("");
        result.append(id+" "+login+" "+password+" "+nickname+" "+ avatar +" "+description+" "+status+" "+last_online);
        return result.toString();
    }
}
