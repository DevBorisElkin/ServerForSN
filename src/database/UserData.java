package database;

public class UserData {
    String id, login, password, nickname, av_color, description, status;

    public UserData(String id, String login, String password, String nickname, String av_color, String description, String status){
        this.id=id;this.login=login;this.password=password;this.nickname=nickname;this.av_color=av_color;this.description=description;this.status=status;
    }
    public String toStr(){
        StringBuilder result=new StringBuilder("");
        result.append(id+" "+login+" "+password+" "+nickname+" "+av_color+" "+description+" "+status);
        return result.toString();
    }
}
