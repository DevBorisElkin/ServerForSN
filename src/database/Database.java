package database;

import support.TimeManager;
import support.UserData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Database {
    private static Connection connection;
    private static Statement stmt;
    private static ResultSet rs;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(String login, String pass, String nick) {
        try {
            String query = "INSERT INTO users (login, password, nickname, avatar, description, status, last_online) VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.setString(3, nick);
            ps.setInt(4, new Random().nextInt(15)+1);
            ps.setString(5,"-");
            ps.setString(6,"offline");
            ps.setString(7,""+System.currentTimeMillis());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getCompleteData(String nick){
        List<String> data = new ArrayList<>();
        try {
            rs = stmt.executeQuery("SELECT id, login, password, nickname, avatar, description, status, last_online FROM users WHERE nickname = '" + nick + "'");
            while (rs.next()) {
                String id=new StringBuilder().append(rs.getInt(1)).toString();    data.add(id);
                String login = rs.getString(2);                                   data.add(login);
                String pass = rs.getString(3);                                    data.add(pass);
                String nickname = rs.getString(4);                                data.add(nickname);
                String color=rs.getString(5);                                     data.add(color);
                String description = rs.getString(6);                             data.add(description);
                String status=rs.getString(7);                                    data.add(status);
                String last_online= TimeManager.calcTimeSince(Long.parseLong(rs.getString(8)),System.currentTimeMillis());  data.add(last_online);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static  List<UserData> getAllUsersData(){
        List<UserData> users = new ArrayList<>();
        try {
            rs = stmt.executeQuery("SELECT id, login, password, nickname, avatar, description, status, last_online FROM users");
            while (rs.next()) {
                String id=new StringBuilder().append(rs.getInt(1)).toString();
                String login = rs.getString(2);
                String pass = rs.getString(3);
                String nickname = rs.getString(4);
                String color=rs.getString(5);
                String description = rs.getString(6);
                String newDescription=description.replaceAll(" ","&");
                String status=rs.getString(7);
                String last_online=TimeManager.calcTimeSince(Long.parseLong(rs.getString(8)),System.currentTimeMillis());
                users.add(new UserData(id,login,pass,nickname,color,newDescription,status, last_online));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return users;
    }

    public static String getNickByLoginAndPass(String login_, String pass) {
        try {
            rs = stmt.executeQuery("SELECT login, password, nickname FROM users WHERE login = '" + login_ + "'");

            while (rs.next()) {
                String login = rs.getString(1);
                String dbPass = rs.getString(2);
                String nick=rs.getString(3);
                if (pass.equals(dbPass)&login.equals(login_)) {
                    return nick;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void checkAllOnline(){
        String nick, last_online;
        try {
            rs = stmt.executeQuery("SELECT nickname, last_online FROM users WHERE status = 'online'");
            while (rs.next()) {
                nick=rs.getString(1);
                last_online=rs.getString(2);
                long minuteDifference=TimeManager.getSecDifference(Long.parseLong(last_online),System.currentTimeMillis());
                if (minuteDifference>=2){
                    PreparedStatement st = connection.prepareStatement("UPDATE users SET status = 'offline' WHERE nickname = '"+nick+"'");
                    st.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUserStatus(String nick, String online_offline_str){
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE users SET status = ?, last_online = ? WHERE nickname = '"+nick+"'");
            st.setString(1,online_offline_str);
            st.setString(2,""+System.currentTimeMillis());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

