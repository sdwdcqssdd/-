import com.alibaba.fastjson.JSON;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.sql.*;


public class LoadPost {
    private static Connection con = null;
    private static PreparedStatement stmt = null;

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
                System.out.println("Successfully connected to the database "
                        + prop.getProperty("database") + " as " + prop.getProperty("user"));
                con.setAutoCommit(false);
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void setPrepareStatement_1() {
        try {
            stmt = con.prepareStatement("insert into author(id, name, registration_time, phone) " +
                    "VALUES (?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_2() {
        try {
            stmt = con.prepareStatement("insert into post(id, author, title, content, post_time, post_city) " +
                    "VALUES (?,?,?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_3() {
        try {
            stmt = con.prepareStatement("insert into post_category(post_id, category) " +
                    "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
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
            properties.load(new InputStreamReader(new FileInputStream("resource\\dbUser.properties")));
            return properties;
        } catch (IOException e) {
            System.err.println("can not find db user file");
            throw new RuntimeException(e);
        }
    }

    private static void loadAuthor(String authorID,String Author,String authorRegistrationTime,String authoPhone) {
        if (con != null) {
            try {
                stmt.setString(1, authorID);
                stmt.setString(2, Author);
                stmt.setTimestamp(3, Timestamp.valueOf(authorRegistrationTime));
                stmt.setString(4, authoPhone);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void loadPost(int id,String author,String title,String content,String post_time,String post_city) {
        if (con != null) {
            try {
                stmt.setInt(1, id);
                stmt.setString(2, author);
                stmt.setString(3, title);
                stmt.setString(4,content);
                stmt.setTimestamp(5, Timestamp.valueOf(post_time));
                stmt.setString(6,post_city);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void loadCategory(int post_id, String category) {
        if (con != null) {
            try {
                stmt.setInt(1, post_id);
                stmt.setString(2, category);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public static void main(String[] args) {
        Properties prop = loadDBUser();
        int cnt = 0;
        long start = System.currentTimeMillis();
        openDB(prop);
        try {
            String jsonStrings = Files.readString(Path.of("resource\\posts.json"));
            List<Post> posts = JSON.parseArray(jsonStrings,Post.class);
            for (Post post : posts) {
                int postID = post.getPostID();
                String title = post.getTitle();
                List<String> category = post.getCategory();
                String content = post.getContent();
                String postingTime = post.getPostingTime();
                String postingDestination = post.getPostingCity();
                int index = postingDestination.indexOf(", ");
                String postingCity = postingDestination.substring(0,index);
                String Author = post.getAuthor();
                String authorRegistrationTime = post.getAuthorRegistrationTime();
                String authorID = post.getAuthorID();
                String authoPhone = post.getAuthoPhone();
                setPrepareStatement_1();
                loadAuthor(authorID,Author,authorRegistrationTime,authoPhone);
                cnt++;
                setPrepareStatement_2();
                loadPost(postID,Author,title,content,postingTime,postingCity);
                cnt++;
                setPrepareStatement_3();
                for (String categories : category) {
                    loadCategory(postID,categories);
                    cnt++;
                }
            }
            try {
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        closeDB();
        long end = System.currentTimeMillis();
        System.out.println(cnt + " records successfully loaded");
        System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");
    }
}


