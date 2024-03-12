import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TR extends JFrame {
    private JTextArea area = new JTextArea();
    public TR(ResultSet rs, Connection con, PreparedStatement stmt) {
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setTitle("结果");
        area.setFont(new Font("宋体", Font.PLAIN, 18));
        area.setText("");
        try {
            while (rs.next()) {
                stmt = con.prepareStatement("select search_time from post where id = " + rs.getInt("id") + ";");
                ResultSet i = stmt.executeQuery();
                int s = 0;
                if (i.next()) {
                    s = i.getInt("search_time");
                }
                s++;
                stmt = con.prepareStatement("update post set search_time = " + s + " where id = " + rs.getInt("id") + ";");
                stmt.executeUpdate();
                con.commit();
                area.append("\n" + "post id: "+rs.getInt("id"));
                area.append("\n"+ "标题: " +rs.getString("title"));
                area.append("\n" + "内容: " +rs.getString("content"));
                area.append("\n" + "发布时间: " +rs.getString("post_time"));
                area.append("\n" + "发布地点: " +rs.getString("post_city"));
                area.append("\n");
                stmt = con.prepareStatement("select r.id,r.content,r.star,p.title,a2.name from post p join author a on a.name = p.author join reply r on p.id = r.post_id join author a2 on a2.id = r.author_id where p.title = ?;");
                stmt.setString(1,rs.getString("title"));
                ResultSet reply = stmt.executeQuery();
                while (reply.next()) {
                    area.append("\nreply");
                    area.append("\n"+ "reply id: " + reply.getInt("id"));
                    area.append("\n" + "内容: " +reply.getString("content"));
                    area.append("\n" + "star: " +reply.getString("star"));
                    area.append("\n" + "用户: " +reply.getString("name"));
                    area.append("\n");
                    stmt = con.prepareStatement("select sr.id,sr.content,sr.star,a.name from second_reply sr join reply r on r.id = sr.reply_id join author a on a.id = sr.author_id where sr.reply_id = ?;");
                    stmt.setInt(1,reply.getInt("id"));
                    ResultSet secondReply = stmt.executeQuery();
                    while (secondReply.next()) {
                        area.append("\n" +"second reply");
                        area.append("\n"+ "second reply id: " +  secondReply.getInt("id"));
                        area.append("\n" +"内容: "+ secondReply.getString("content"));
                        area.append("\n"+"star: "  + secondReply.getString("star"));
                        area.append("\n" +"用户: " + secondReply.getString("name"));
                    }
                    area.append("\n");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setSize(400,400);
        this.add(scrollPane);
        this.setVisible(true);
        ResultSet reply = null;
    }

}
