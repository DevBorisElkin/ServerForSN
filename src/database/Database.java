package database;

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
            String query = "INSERT INTO users (login, password, nickname, av_color, description) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.setString(3, nick);
            ps.setInt(4, new Random().nextInt(10)+1);
            ps.setString(5,"-");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getCompleteData(String nick){
        List<String> data = new ArrayList<>();
        try {
            rs = stmt.executeQuery("SELECT id, login, password, nickname, av_color, description, status FROM users WHERE nickname = '" + nick + "'");
            while (rs.next()) {
                String id=new StringBuilder().append(rs.getInt(1)).toString();    data.add(id);
                String login = rs.getString(2);                                   data.add(login);
                String pass = rs.getString(3);                                    data.add(pass);
                String nickname = rs.getString(4);                                data.add(nickname);
                String color=rs.getString(5);                                     data.add(color);
                String description = rs.getString(6);                             data.add(description);
                String status=rs.getString(7);                                    data.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getNickByLoginAndPass(String login, String pass) {
        try {
            rs = stmt.executeQuery("SELECT login, password FROM users WHERE login = '" + login + "'");

            while (rs.next()) {
                String nick = rs.getString(1);
                String dbPass = rs.getString(2);
                if (pass.equals(dbPass)) {
                    return nick;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

