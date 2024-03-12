import com.alibaba.fastjson.JSON;
import java.util.Date;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.sql.*;


public class LoadOthers {
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


    public static void setPrepareStatement_4() {
        try {
            stmt = con.prepareStatement("insert into author_favour_post(post_id, author_id) " +
                    "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_5() {
        try {
            stmt = con.prepareStatement("insert into author_like_post(post_id, author_id) " +
                    "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_6() {
        try {
            stmt = con.prepareStatement("insert into author_followed(author_id, followed_id) " +
                    "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_7() {
        try {
            stmt = con.prepareStatement("insert into author_shared(post_id, author_id) " +
                    "VALUES (?,?);");
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

    private static void loadAuthorFavor(int post_id, String id) {
        if (con != null) {
            try {
                stmt.setInt(1, post_id);
                stmt.setString(2, id);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void loadAuthorLike(int post_id, String author) {
        if (con != null) {
            try {
                stmt.setInt(1, post_id);
                stmt.setString(2, author);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void loadAuthorFollowed(String author_id, String follow_id) {
        if (con != null) {
            try {
                stmt.setString(1, author_id);
                stmt.setString(2, follow_id);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void loadAuthorShared(int post_id, String author) {
        if (con != null) {
            try {
                stmt.setInt(1, post_id);
                stmt.setString(2, author);
                stmt.executeUpdate();
            } catch (SQLException ex) {
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
                String authorID = post.getAuthorID();
                List<String> authorFollowedBy = post.getAuthorFollowedBy();
                List<String> authorFavorite = post.getAuthorFavorite();
                List<String> authorShared = post.getAuthorShared();
                List<String> authorLiked = post.getAuthorLiked();
                for (String authorFollowed : authorFollowedBy) {
                    try {
                        setPrepareStatement_9();
                        ResultSet rs = searchID(authorFollowed);
                        if (rs.next()) {
                            String followedID = rs.getString("id");
                            setPrepareStatement_6();
                            loadAuthorFollowed(authorID,followedID);
                            cnt++;
                        } else {
                            setPrepareStatement_10();
                            String generateID = generateID();
                            setPrepareStatement_8();
                            Timestamp timestamp = new Timestamp(new Date().getTime());
                            loadNewAuthor(generateID,authorFollowed,timestamp);
                            setPrepareStatement_6();
                            loadAuthorFollowed(authorID,generateID);
                            cnt+=2;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                for (String author : authorFavorite) {
                    try {
                        setPrepareStatement_9();
                        ResultSet rs = searchID(author);
                        if (rs.next()) {
                            String id = rs.getString("id");
                            setPrepareStatement_4();
                            loadAuthorFavor(postID,id);
                            cnt++;
                        } else {
                            setPrepareStatement_10();
                            String generateID = generateID();
                            setPrepareStatement_8();
                            Timestamp timestamp = new Timestamp(new Date().getTime());
                            loadNewAuthor(generateID,author,timestamp);
                            setPrepareStatement_4();
                            loadAuthorFavor(postID,generateID);
                            cnt+=2;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                for (String author : authorShared) {
                    try {
                        setPrepareStatement_9();
                        ResultSet rs = searchID(author);
                        if (rs.next()) {
                            String id = rs.getString("id");
                            setPrepareStatement_7();
                            loadAuthorShared(postID,id);
                            cnt++;
                        } else {
                            setPrepareStatement_10();
                            String generateID = generateID();
                            setPrepareStatement_8();
                            Timestamp timestamp = new Timestamp(new Date().getTime());
                            loadNewAuthor(generateID,author,timestamp);
                            setPrepareStatement_7();
                            loadAuthorShared(postID,generateID);
                            cnt+=2;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                for (String author : authorLiked) {
                    try {
                        setPrepareStatement_9();
                        ResultSet rs = searchID(author);
                        if (rs.next()) {
                            String id = rs.getString("id");
                            setPrepareStatement_5();
                            loadAuthorLike(postID,id);
                            cnt++;
                        } else {
                            setPrepareStatement_10();
                            String generateID = generateID();
                            setPrepareStatement_8();
                            Timestamp timestamp = new Timestamp(new Date().getTime());
                            loadNewAuthor(generateID,author,timestamp);
                            setPrepareStatement_5();
                            loadAuthorLike(postID,generateID);
                            cnt+=2;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
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



