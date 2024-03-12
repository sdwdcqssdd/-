import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class checkFollow extends JFrame {
    private JTextArea area = new JTextArea();
    public checkFollow(ResultSet rs, Connection con, PreparedStatement stmt) {
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setTitle("已关注");
        area.setFont(new Font("宋体", Font.PLAIN, 18));
        area.setText("");
        try {
            while (rs.next()) {
                area.append("\n" + "id: "+rs.getString("id"));
                area.append("\n" + "用户名: " +rs.getString("name"));
                area.append("\n"+ "注册时间: " +rs.getString("registration_time"));
                area.append("\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setSize(400,400);
        this.add(scrollPane);
        this.setVisible(true);
    }

}
