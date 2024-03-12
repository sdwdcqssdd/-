import com.alibaba.fastjson.JSON;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.sql.*;

public class LoadReply {
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

    public static void setPrepareStatement() {
        try {
            stmt = con.prepareStatement("insert into reply(post_id, content, star, author_id) " +
                    "VALUES (?,?,?,?) on conflict do nothing;");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_1() {
        try {
            stmt = con.prepareStatement("insert into second_reply(content, star, author_id) " +
                    "VALUES (?,?,?) on conflict do nothing;");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_8() {
        try {
            stmt = con.prepareStatement("insert into author(id, name, registration_time) " +
                    "values (?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_9() {
        try {
            stmt = con.prepareStatement("select id from author where name = ? ");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_10() {
        try {
            stmt = con.prepareStatement("SELECT MAX(id) FROM author;");
        } catch (SQLException e) {
            System.err.println("failed");
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

    private static void loadData(int postID, String replyContent, int replyStars, String replyAuthor) {
        if (con != null) {
            try {
                stmt.setInt(1, postID);
                stmt.setString(2, replyContent);
                stmt.setInt(3, replyStars);
                stmt.setString(4, replyAuthor);
                stmt.executeUpdate();
            } catch (SQLException ex)  {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void loadSecond(String replyContent, int replyStars, String replyAuthor) {
        if (con != null) {
            try {
                stmt.setString(1, replyContent);
                stmt.setInt(2, replyStars);
                stmt.setString(3, replyAuthor);
                stmt.executeUpdate();
            } catch (SQLException ex)  {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void loadNewAuthor(String id,String name,Timestamp timestamp) {
        if (con != null) {
            try {
                stmt.setString(1, id);
                stmt.setString(2, name);
                stmt.setTimestamp(3,timestamp);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
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

    public static void main(String[] args) {

        Properties prop = loadDBUser();
        openDB(prop);
        int cnt = 0;
        long start = System.currentTimeMillis();
        try {
            String jsonStrings = Files.readString(Path.of("resource\\replies.json"));
            List<Replies> replies = JSON.parseArray(jsonStrings,Replies.class);
            for (Replies reply : replies) {
                int postID = reply.getPostID();
                String replyContent = reply.getReplyContent();
                int replyStars = reply.getReplyStars();
                String replyAuthor = reply.getReplyAuthor();
                String secondaryReplyContent = reply.getSecondaryReplyContent();
                int secondaryReplyStars = reply.getSecondaryReplyStars();
                String secondaryReplyAuthor = reply.getSecondaryReplyAuthor();
                try {
                    setPrepareStatement_9();
                    ResultSet result = searchID(replyAuthor);
                    if (result.next()) {
                        String ID = result.getString("id");
                        setPrepareStatement();
                        loadData(postID,replyContent,replyStars,ID);
                        cnt++;
                    } else {
                        setPrepareStatement_10();
                        String generateID = generateID();
                        setPrepareStatement_8();
                        Timestamp timestamp = new Timestamp(new Date().getTime());
                        loadNewAuthor(generateID,replyAuthor,timestamp);
                        setPrepareStatement();
                        loadData(postID,replyContent,replyStars,generateID);
                        cnt+=2;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    setPrepareStatement_9();
                    ResultSet result = searchID(secondaryReplyAuthor);
                    if (result.next()) {
                        String ID = result.getString("id");
                        setPrepareStatement_1();
                        loadSecond(secondaryReplyContent,secondaryReplyStars,ID);
                        cnt++;
                    } else {
                        setPrepareStatement_10();
                        String generateID = generateID();
                        setPrepareStatement_8();
                        Timestamp timestamp = new Timestamp(new Date().getTime());
                        loadNewAuthor(generateID,secondaryReplyAuthor,timestamp);
                        setPrepareStatement_1();
                        loadSecond(secondaryReplyContent,secondaryReplyStars,generateID);
                        cnt+=2;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
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


