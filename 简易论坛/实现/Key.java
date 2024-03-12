import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Key extends JFrame {

    private static Connection con = null;
    private static PreparedStatement stmt = null;
    private static ResultSet search(String key) {
        ResultSet rs = null;
        if (con != null) {
            try {
                stmt.setString(1,key);
                rs = stmt.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return rs;
    }

    public Key(Connection c, PreparedStatement s) {
        this.setSize(300,300);
        this.setTitle("关键词检索");
        this.setLocationRelativeTo(null);
        con = c;
        stmt = s;
        JTextField textField = new JTextField();
        textField.setBounds(60,100,150,25);
        try {
            stmt = con.prepareStatement("select * from post where title like ?;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = textField.getText();
                key = "%"+key+"%";
                ResultSet rs = search(key);
                new KeyR(rs,con,stmt);
            }
        });
        JPanel panel = new JPanel();
        panel.add(textField);
        panel.setLayout(null);
        this.add(panel);
        this.setVisible(true);
    }
}
