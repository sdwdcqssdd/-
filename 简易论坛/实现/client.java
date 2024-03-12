import jdk.javadoc.doclet.Taglet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;


public class client {
    private static Connection con = null;
    private static PreparedStatement stmt = null;
    public int idMin;
    public int idMax;

    private static void openDB(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
        try {
            con = DriverManager.getConnection(url, prop);
            if (con != null) {
                con.setAutoCommit(false);
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void checkName() {
        try {
            stmt = con.prepareStatement("select name from author where name = ? ;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkShare() {
        try {
            stmt = con.prepareStatement("select * from post p join author_shared a_s on p.id = a_s.post_id where a_s.author_id = ? ;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkFavorite() {
        try {
            stmt = con.prepareStatement("select * from post p join author_favour_post afp on p.id = afp.post_id where afp.author_id = ? ;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkLike() {
        try {
            stmt = con.prepareStatement("select * from post p join author_like_post alp on p.id = alp.post_id where alp.author_id = ? ;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static ResultSet searchName(String name) {
        ResultSet rs = null;
        if (con != null) {
            try {
                stmt.setString(1,name);
                rs = stmt.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return rs;
    }

    private static ResultSet searchName1(Timestamp name, Timestamp name1) {
        ResultSet rs = null;
        if (con != null) {
            try {
                stmt.setTimestamp(1,name);
                stmt.setTimestamp(2,name1);
                rs = stmt.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return rs;
    }

    private static ResultSet searchIDInt(int name) {
        ResultSet rs = null;
        if (con != null) {
            try {
                stmt.setInt(1,name);
                rs = stmt.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return rs;
    }

    private static ResultSet searchIDInt1(int name, int name1) {
        ResultSet rs = null;
        if (con != null) {
            try {
                stmt.setInt(1,name);
                stmt.setInt(2, name1);
                rs = stmt.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return rs;
    }

    private static void register() {
        try {
            stmt = con.prepareStatement("insert into author(id, name, registration_time, phone) " +
                    "VALUES (?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void userID() {
        try {
            stmt = con.prepareStatement("select id from author where name = ? ");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static ResultSet searchID(String name) {
        ResultSet rs = null;
        if (con != null) {
            try {
                stmt.setString(1,name);
                rs = stmt.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return rs;
    }

    private static void update(int post_id, String author_id) {
        if (con != null) {
            try {
                stmt.setInt(1, post_id);
                stmt.setString(2, author_id);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void followUpdate(String author_id, String followed_id) {
        if (con != null) {
            try {
                stmt.setString(1, author_id);
                stmt.setString(2, followed_id);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void postUpdate(int id, String author, String title, String content, Timestamp post_time, String post_city) {
        if (con != null) {
            try {
                stmt.setInt(1,id);
                stmt.setString(2, author);
                stmt.setString(3, title);
                stmt.setString(4,content);
                stmt.setTimestamp(5,post_time);
                stmt.setString(6,post_city);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void replyUpdate(int id, int post_id, String content, int star, String author_id) {
        if (con != null) {
            try {
                stmt.setInt(1,id);
                stmt.setInt(2, post_id);
                stmt.setString(3,content);
                stmt.setInt(4,star);
                stmt.setString(5,author_id);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void secondReplyUpdate(int id, int reply_id, String content, int star, String author_id) {
        if (con != null) {
            try {
                stmt.setInt(1, id);
                stmt.setInt(2, reply_id);
                stmt.setString(3,content);
                stmt.setInt(4,star);
                stmt.setString(5,author_id);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void favourite() {
        try {
            stmt = con.prepareStatement("insert into author_favour_post(post_id, author_id) " +
                    "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void like() {
        try {
            stmt = con.prepareStatement("insert into author_like_post(post_id, author_id) " +
                    "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void share() {
        try {
            stmt = con.prepareStatement("insert into author_share_post(post_id, author_id) " +
                    "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void post() {//新创建帖子
        try {
            stmt = con.prepareStatement("insert into post(id,author, title, content, post_time, post_city) " +
                    "VALUES (?,?,?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }


    private static void foundAuthorId(){
        try {
            stmt = con.prepareStatement("select * from author where author.name = ? ;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void follow() {//新创建帖子
        try {
            stmt = con.prepareStatement("insert into author_followed(author_id, followed_id) " +
                    "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkPost() {
        try {
            stmt = con.prepareStatement("select * from post p join author a on p.author = a.name where a.id = ? ;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkReply() {
        try {
            stmt = con.prepareStatement("select * from post join (select post_id from reply join author a on a.id = reply.author_id where a.id = ?)id_search on post.id = id_search.post_id;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkFollow() {
        try {
            stmt = con.prepareStatement("select * from author au join (select author_id from author_followed join author a on a.id = author_followed.followed_id where a.id = ?)id_search  on au.id = id_search.author_id ;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void searchPost() {
        try{
            stmt = con.prepareStatement("select * from post where title like ?;");
        }catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void searchPostdate() {
        try{
            stmt = con.prepareStatement("select * from post where post_time between ? and ?;");
        }catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void browsePost(){
        try {
            stmt = con.prepareStatement("select * from post where id between ? and ?");
        }catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void enterPost(){
        try {
            stmt = con.prepareStatement("select * from post where id = ?");
        }catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void quitFollow() {
        try {
            stmt = con.prepareStatement("delete from author_followed where author_followed.author_id = ? && followed_id = ?;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void reply() {//新创建回复
        try {
            stmt = con.prepareStatement("insert into reply(id,post_id, content, star, author_id) " +
                    "VALUES (?,?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void second_reply() {//新创建帖子
        try {
            stmt = con.prepareStatement("insert into second_reply(id,reply_id, content, star, author_id) " +
                    "VALUES (?,?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void insertAuthor(String ID,String name,Timestamp timestamp,String Phone) {
        if (con != null) {
            try {
                stmt.setString(1, ID);
                stmt.setString(2, name);
                stmt.setTimestamp(3, timestamp);
                stmt.setString(4, Phone);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void maxID() {
        try {
            stmt = con.prepareStatement("SELECT MAX(id) FROM author;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void maxIDPost() {
        try {
            stmt = con.prepareStatement("SELECT MAX(id) FROM post;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void maxIDReply() {
        try {
            stmt = con.prepareStatement("SELECT MAX(id) FROM reply;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void maxIDSecondReply() {
        try {
            stmt = con.prepareStatement("SELECT MAX(id) FROM second_reply;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static String generateID() {
        ResultSet rs;
        long id = 0;
        if (con != null) {
            try {
                rs = stmt.executeQuery();
                if (rs.next()) {
                    id = Long.parseLong(rs.getString(1)) + 1;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        String gid;
        gid = String.valueOf(id);
        return gid;
    }

    private static int generateIDInt() {
        ResultSet rs;
        int id = 0;
        if (con != null) {
            try {
                rs = stmt.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1) + 1;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return id;
    }

    public static void browsePostContinue(int idMin, int idMax){
        browsePost();
        ResultSet rs = searchIDInt1(idMin,idMax);
        try {
            int cnt = 0;
            while (rs.next()) {
                System.out.println(rs.getInt("id") +"  "+rs.getString("title"));
                System.out.println("");
                cnt++;
            }
            if (cnt == 0) {
                System.out.println("No share");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void closeDB() {
        if (con != null) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                con.close();
                con = null;
            } catch (Exception ignored) {
            }
        }
    }

    private static Properties loadDBUser() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream("D:\\learn\\learningProgram\\数据库原理\\lab_9\\data\\dbUser.properties")));
            return properties;
        } catch (IOException e) {
            System.err.println("can not find db user file");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Properties prop = loadDBUser();
        Scanner in = new Scanner(System.in);
        openDB(prop);
        while (true) {
            System.out.println("log or register");
            String operation = in.next();
            if (operation.equals("register")) {
                System.out.println("input your name");
                String name = in.next();
                try {
                    checkName();
                    ResultSet resultSet = searchName(name);
                    boolean nameUsed = false;
                    if (resultSet.next()) {
                        System.out.println("name is used");
                        nameUsed = true;
                    }
                    if (!nameUsed) {
                        System.out.println("input your phone");
                        String phone = in.next();
                        maxID();
                        String id = generateID();
                        System.out.println("Here is your id: " + id);
                        register();
                        Timestamp timestamp = new Timestamp(new Date().getTime());
                        insertAuthor(id,name,timestamp,phone);
                        try {
                            con.commit();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (operation.equals("log")) {
                System.out.println("Input your name");
                String userName;
                while (true) {
                    String name = in.next();
                    try {
                        checkName();
                        ResultSet rs = searchName(name);
                        if (!rs.next()) {
                            System.out.println("name doesn't exist, please try again");
                        } else {
                            userName = name;
                            System.out.println("log in successfully");
                            break;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                String userID = "";
                try {
                    userID();
                    ResultSet rs = searchID(userName);
                    if (rs.next()) {
                        userID = rs.getString("id");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                while (true) {
                    System.out.println("what do you want to do");
                    System.out.println("you can type 'browsePost' to browse these Post");
                    System.out.println("you can also type 'searchDate' or 'searchPost' to search your wanted posts ");
                    System.out.println("you can type 'favouritePost', 'likePost', 'sharePost' to do sth. without reading ");
                    System.out.println("you can type 'Post', 'follow', 'reply' and 'secondReply' to do sth. without reading");
                    System.out.println("type 'checkShare', 'checkFavourite', 'checkLike', 'checkPost', 'checkReply', 'checkFollow' to check your cycle ");
                    System.out.println("if you want to type 'quit', goodbye for meeting unexpectedly next time ");
                    String action = in.next();

                    if (action.equals("browsePost")){
                        browsePost();
                        int idMin = 0;
                        int idMax = 20;
                        ResultSet rs = searchIDInt1(idMin,idMax);
                        try {
                            int cnt = 0;
                            while (rs.next()) {
                                System.out.println(rs.getInt("id") +"  "+rs.getString("title"));
                                System.out.println("");
                                cnt++;
                            }
                            if (cnt == 0) {
                                System.out.println("No share");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        maxIDPost();
                        int PostIdMax = generateIDInt();
                        while (idMin<= PostIdMax && idMax >0) {
                            System.out.println("type the enter to browse or type quit to back,or you can type continue or back to keep browsing");
                            String action_1 = in.next();
                            if (action_1.equals("continue")) {
                                idMin += 20;
                                idMax += 20;
                                browsePostContinue(idMin, idMax);
                            } else if (action_1.equals("back")) {
                                idMin -= 20;
                                idMax -= 20;
                                browsePostContinue(idMin, idMax);
                            } else if (action_1.equals("enter")) {
                                enterPost();
                                int id = in.nextInt();
                                String author = null;
                                ResultSet rs1 = searchIDInt(id);
                                try {
                                    int cnt = 0;
                                    while (rs1.next()) {
                                        System.out.println(rs1.getString("title"));
                                        System.out.println(rs1.getString("author"));
                                        System.out.println(rs1.getString("content"));
                                        author = rs1.getString("author");
                                        System.out.println("");
                                        cnt++;
                                    }
                                    if (cnt == 0) {
                                        System.out.println("No share");
                                    }
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                for (int i = 0; i < 100; i++) {
                                    System.out.println("you can like, favourite, and share it. ");
                                    System.out.println("Meanwhile, you can reply and follow it");
                                    String action_2 = in.next();
                                    if (action_2.equals("like")) {
                                        like();
                                        update(id, userID);
                                    }
                                    else if (action_2.equals("favourite")) {
                                        favourite();
                                        update(id, userID);
                                    }
                                    else if (action_2.equals("share")) {
                                        share();
                                        update(id, userID);
                                    }
                                    else if (action_2.equals("follow")){
                                        String author_id = null;
                                        foundAuthorId();
                                        ResultSet rs2 = searchName(author);
                                        try {
                                            int cnt = 0;
                                            while (rs2.next()) {
                                                author_id = rs2.getString("id");
                                                cnt++;
                                            }
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        follow();
                                        followUpdate(author_id,userID);
                                    }
                                    else if (action_2.equals("reply")){
                                        System.out.println("please input the content and the star");
                                        String content = in.next();
                                        int star = in.nextInt();
                                        maxIDReply();
                                        int replyIdMax = generateIDInt();
                                        reply();
                                        replyUpdate(replyIdMax,id,content,star,userID);
                                    }
                                    else if (action_2.equals("quit")) {
                                        break;
                                    }
                                }
                            } else if (action_1.equals("quit")) {
                                break;
                            }
                        }
                    }
                    else if(action.equals("searchDate")){
                        searchPostdate();
                        String yearMin = in.next();
                        String monthMin = in.next();
                        String dateMin = in.next();
                        String yearMax = in.next();
                        String monthMax = in.next();
                        String dateMax = in.next();
                        Timestamp temp = Timestamp.valueOf(yearMin+"-"+monthMin+"-"+dateMin+" 00:00:00");

                        Timestamp temp1 = Timestamp.valueOf(yearMax+"-"+monthMax+"-"+dateMax+" 00:00:00");
                        ResultSet rs = searchName1(temp,temp1);
                        try {
                            int cnt = 0;
                            while (rs.next()) {
                                System.out.println(rs.getInt("id") +"  "+rs.getString("title"));
                                System.out.println("");
                                cnt++;
                            }
                            if (cnt == 0) {
                                System.out.println("No share");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("type the enter to browse or type quit to back");
                        String action_1 = in.next();
                        if(action_1.equals("enter")){
                            enterPost();
                            int id = in.nextInt();
                            String author = null;
                            ResultSet rs1 = searchIDInt(id);
                            try {
                                int cnt = 0;
                                while (rs1.next()) {
                                    System.out.println(rs1.getString("title"));
                                    System.out.println(rs1.getString("author"));
                                    System.out.println(rs1.getString("content"));
                                    author = rs1.getString("author");
                                    System.out.println("");
                                    cnt++;
                                }
                                if (cnt == 0) {
                                    System.out.println("No share");
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            for (int i = 0; i < 100; i++) {
                                System.out.println("you can like, favourite, and share it. ");
                                System.out.println("Meanwhile, you can reply and follow it");
                                String action_2 = in.next();
                                if (action_2.equals("like")) {
                                    like();
                                    update(id, userID);
                                }
                                else if (action_2.equals("favourite")) {
                                    favourite();
                                    update(id, userID);
                                }
                                else if (action_2.equals("share")) {
                                    share();
                                    update(id, userID);
                                }
                                else if (action_2.equals("follow")){
                                    String author_id = null;
                                    foundAuthorId();
                                    ResultSet rs2 = searchName(author);
                                    try {
                                        int cnt = 0;
                                        while (rs2.next()) {
                                            author_id = rs2.getString("id");
                                            cnt++;
                                        }
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    follow();
                                    followUpdate(author_id,userID);
                                }
                                else if (action_2.equals("reply")){
                                    System.out.println("please input the content and the star");
                                    String content = in.next();
                                    int star = in.nextInt();
                                    maxIDReply();
                                    int replyIdMax = generateIDInt();
                                    reply();
                                    replyUpdate(replyIdMax,id,content,star,userID);
                                }
                                else if (action_2.equals("quit")) {
                                    break;
                                }
                            }
                        }else if(action_1.equals("quit")){
                            continue;
                        }

                    }
                    else if(action.equals("searchPost")){
                        searchPost();
                        String titleLike = in.next();
                        String temp = "%"+titleLike+"%";
                        ResultSet rs = searchName(temp);
                        try {
                            int cnt = 0;
                            while (rs.next()) {
                                System.out.println(rs.getInt("id") +"  "+rs.getString("title"));
                                System.out.println("");
                                cnt++;
                            }
                            if (cnt == 0) {
                                System.out.println("No share");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("type the enter to browse or type quit to back");
                        String action_1 = in.next();
                        if(action_1.equals("enter")){
                            enterPost();
                            int id = in.nextInt();
                            String author = null;
                            ResultSet rs1 = searchIDInt(id);
                            try {
                                int cnt = 0;
                                while (rs1.next()) {
                                    System.out.println(rs1.getString("title"));
                                    System.out.println(rs1.getString("author"));
                                    System.out.println(rs1.getString("content"));
                                    author = rs1.getString("author");
                                    System.out.println("");
                                    cnt++;
                                }
                                if (cnt == 0) {
                                    System.out.println("No share");
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            for (int i = 0; i < 100; i++) {
                                System.out.println("you can like, favourite, and share it. ");
                                System.out.println("Meanwhile, you can reply and follow it");
                                String action_2 = in.next();
                                if (action_2.equals("like")) {
                                    like();
                                    update(id, userID);
                                }
                                else if (action_2.equals("favourite")) {
                                    favourite();
                                    update(id, userID);
                                }
                                else if (action_2.equals("share")) {
                                    share();
                                    update(id, userID);
                                }
                                else if (action_2.equals("follow")){
                                    String author_id = null;
                                    foundAuthorId();
                                    ResultSet rs2 = searchName(author);
                                    try {
                                        int cnt = 0;
                                        while (rs2.next()) {
                                            author_id = rs2.getString("id");
                                            cnt++;
                                        }
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    follow();
                                    followUpdate(author_id,userID);
                                }
                                else if (action_2.equals("reply")){
                                    System.out.println("please input the content and the star");
                                    String content = in.next();
                                    int star = in.nextInt();
                                    maxIDReply();
                                    int replyIdMax = generateIDInt();
                                    reply();
                                    replyUpdate(replyIdMax,id,content,star,userID);
                                }
                                else if (action_2.equals("quit")) {
                                    break;
                                }
                            }
                        } else if(action_1.equals("quit")){
                            continue;
                        }

                    }
                    else if (action.equals("favouritePost")) {
                        int postID = in.nextInt();
                        favourite();
                        update(postID,userID);
                    }
                    else if (action.equals("likePost")) {
                        int postID = in.nextInt();
                        like();
                        update(postID,userID);
                    }
                    else if (action.equals("sharePost")) {
                        int postID = in.nextInt();
                        share();
                        update(postID,userID);
                    }
                    else if (action.equals("post")){
                        System.out.println("please input the title, content and your city");
                        String title = in.next();
                        String content = in.next();
                        Timestamp post_time = new Timestamp(new Date().getTime());
                        String post_city = in.next();
                        maxIDPost();
                        int id = generateIDInt();
                        post();
                        postUpdate(id,userName,title,content,post_time,post_city);
                    }
                    else if (action.equals("follow")){
                        System.out.println("please input the author's id");
                        String author_id = in.next();
                        follow();
                        followUpdate(author_id,userID);
                    }
                    else if (action.equals("reply")){
                        int postID = in.nextInt();
                        System.out.println("please input the content and the star");
                        String content = in.next();
                        int star = in.nextInt();
                        maxIDReply();
                        int id = generateIDInt();
                        reply();
                        replyUpdate(id,postID,content,star,userID);
                    }
                    else if (action.equals("secondReply")){
                        int reply_id = in.nextInt();
                        System.out.println("please input the content and the star");
                        String content = in.next();
                        int star = in.nextInt();
                        maxIDSecondReply();
                        int id = generateIDInt();
                        second_reply();
                        secondReplyUpdate(id,reply_id,content,star,userID);
                    }

                    else if (action.equals("checkShare")) {
                        checkShare();
                        ResultSet rs = searchName(userID);
                        try {
                            int cnt = 0;
                            while (rs.next()) {
                                System.out.println(rs.getString("title"));
                                System.out.println(rs.getString("content"));
                                System.out.println("");
                                cnt++;
                            }
                            if (cnt == 0) {
                                System.out.println("No share");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if (action.equals("checkFavourite")) {
                        checkFavorite();
                        ResultSet rs = searchName(userID);
                        try {
                            int cnt = 0;
                            while (rs.next()) {
                                System.out.println(rs.getString("title"));
                                System.out.println(rs.getString("content"));
                                System.out.println("");
                                cnt++;
                            }
                            if (cnt == 0) {
                                System.out.println("No favourite");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if (action.equals("checkLike")) {
                        checkLike();
                        ResultSet rs = searchName(userID);
                        try {
                            int cnt = 0;
                            while (rs.next()) {
                                System.out.println(rs.getString("title"));
                                System.out.println(rs.getString("content"));
                                System.out.println("");
                                cnt++;
                            }
                            if (cnt == 0) {
                                System.out.println("No like");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(action.equals("checkPost")) {
                        checkPost();
                        ResultSet rs = searchName(userID);
                        try {
                            int cnt = 0;
                            while (rs.next()) {
                                System.out.println(rs.getString("title"));
                                System.out.println(rs.getString("content"));
                                System.out.println("");
                                cnt++;
                            }
                            if (cnt == 0) {
                                System.out.println("No Post");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(action.equals("checkReply")) {
                        checkReply();
                        ResultSet rs = searchName(userID);
                        try {
                            int cnt = 0;
                            while (rs.next()) {
                                System.out.println(rs.getString("title"));
                                System.out.println(rs.getString("content"));
                                System.out.println("");
                                cnt++;
                            }
                            if (cnt == 0) {
                                System.out.println("No Reply");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(action.equals("checkFollow")){
                        checkFollow();
                        ResultSet rs = searchName(userID);
                        try{
                            int cnt = 0;
                            while (rs.next()){
                                System.out.println(rs.getString("name"));
                                System.out.println(rs.getTimestamp("registration_time"));
                                System.out.println(rs.getString("phone"));
                                System.out.println("");
                                cnt++;
                            }
                            if (cnt == 0) {
                                System.out.println("No Follow");
                            }
                        }catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(action.equals("quitFollow")){
                        String author_id = in.next();
                        quitFollow();
                        followUpdate(author_id,userID);
                        System.out.println("quit follow success");
                    }

                    else if (action.equals("quit")) {
                        break;
                    }
                }
            } else if (operation.equals("close")) {
                break;
            }
        }
        try {
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeDB();
    }

}


