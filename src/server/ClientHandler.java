package server;

import database.Database;
import support.UserData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String nick;
    String str;

    List<String> blackList;

    public String getNick() {
        return nick;
    }

    public ClientHandler(Server server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.blackList = new ArrayList<>();
            new Thread(() -> {
                try {
                    while (true) {
                        str = in.readUTF();
                        if(str.startsWith("/check")) {
                            sendMsg("/online");
                        }else if (str.startsWith("/auth")) { // /auth login72 pass72
                                String[] tokens = str.split(" ");
                                String newNick = Database.getNickByLoginAndPass(tokens[1], tokens[2]);
                                if (newNick != null) {
                                    if (!server.isNickBusy(newNick)) {
                                        sendMsg("/auth_ok "+newNick);
                                        System.out.println("Пользователь "+newNick+" авторизовался.");
                                        nick = newNick;
                                        server.subscribe(this);
                                        Database.updateUserStatus(newNick,"online");
                                        break;

                                    } else {
                                        sendMsg("Учетная запись уже используется");
                                    }
                                } else {
                                    sendMsg("/wrong login/password");
                                }

                        }else if(str.startsWith("/add_user")){
                            String[] tokens = str.split(" ");
                            Database.addUser(tokens[1],tokens[2],tokens[3]);
                            sendMsg("user_was_added");
                            break;
                            //TODO: доделать регистрацию
                        }else{
                            sendMsg("/unknown command");
                        }
                    }
                    while (true) {
                        str = in.readUTF();
                        if (str.startsWith("/")) {
                            if(str.startsWith("/check")) {
                                sendMsg("/online");
                            }else if (str.equals("/end")) {
                                out.writeUTF("/serverclosed");
                                break;
                            }
                            if(str.startsWith("/get_main_info")){
                                String[] tokens = str.split(" ");
                                List<String>data=Database.getCompleteData(tokens[1]);
                                StringBuilder output= new StringBuilder("/compl_data");
                                for(String e: data) output.append(e).append(" ");
                                out.writeUTF(output.toString());
                            }
                            if (str.startsWith("/w ")) { // /w nick3 lsdfhldf sdkfjhsdf wkerhwr
                                String[] tokens = str.split(" ", 3);
                                String m = str.substring(tokens[1].length() + 4);
                                server.sendPersonalMsg(this, tokens[1], tokens[2]);
                            }
                            if (str.startsWith("/blacklist ")) { // /blacklist nick3
                                String[] tokens = str.split(" ");
                                blackList.add(tokens[1]);
                                sendMsg("Вы добавили пользователя " + tokens[1] + " в черный список");
                            }
                            if(str.startsWith("/get_all_users")){
                                List<UserData>list=Database.getAllUsersData();
                                StringBuilder builder=new StringBuilder("/all_users_data@");
                                for(UserData a:list){ builder.append(a.toStr()+"@"); }
                                //builder.append(list.get(0).toStr()+"@");  // replace this line with line above
                                sendMsg(builder.toString());
                            }
                            if(str.startsWith("/online_push ")){
                                String[] tokens = str.split(" ");
                                Database.updateUserStatus(tokens[1], "online");
                            }
                            if(str.startsWith("/update_user ")){
                                String[] tokens = str.split(" ");
                                Database.updateUserData(tokens[1],tokens[2]);
                            }

                        } else {
                            server.broadcastMsg(this, nick + ": " + str);
                        }
                        //System.out.println("Client: " + str);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                } finally {
                    Database.updateUserStatus(nick, "offline");
                    if(nick!=null){
                        System.out.println("Пользователь "+nick+" отключился");
                    }
                    System.out.println("Клиент отключился");
                    sendMsg("offline");
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    server.unsubscribe(this);
                }
            }).start();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public boolean checkBlackList(String nick) {
        return blackList.contains(nick);
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void sendAllData(){
        List<UserData>list=Database.getAllUsersData();
        StringBuilder builder=new StringBuilder("/all_users_data@");
        for(UserData a:list){ builder.append(a.toStr()+"@"); }
        sendMsg(builder.toString());
    }


}
