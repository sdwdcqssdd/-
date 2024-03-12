import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;

public class Follow extends JFrame {

    private JTextField textField = new JTextField();
    private JLabel label = new JLabel("用户名");

    private Connection con = null;
    private PreparedStatement stmt = null;


    public Follow(Connection c, PreparedStatement s,String name) {
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("关注");
        con = c;
        stmt = s;
        textField.setBounds(100,50,100,25);

        label.setBounds(50,50,100,25);
        label.setFont(new Font("宋体",Font.PLAIN,15));
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stmt = con.prepareStatement("select id from author where name = ?");
                    stmt.setString(1,textField.getText());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        String id = rs.getString("id");
                        stmt = con.prepareStatement("select * from author_followed join author on author.id = author_followed.followed_id where author_id = ? and author.name = ?;");
                        stmt.setString(1,id);
                        stmt.setString(2,name);
                        ResultSet rs2 = stmt.executeQuery();
                        if (rs2.next()) {
                            new FollowFail().set1();
                        } else {
                            stmt = con.prepareStatement("select id from author where name = ?;");
                            stmt.setString(1,name);
                            ResultSet r3 = stmt.executeQuery();
                            String idd = "";
                            if (r3.next()) {
                                idd = r3.getString("id");
                            }
                            stmt = con.prepareStatement("insert into author_followed(author_id,followed_id) values(?,?); ");
                            stmt.setString(1,id);
                            stmt.setString(2,idd);
                            stmt.executeUpdate();
                            new FollowS();
                            con.commit();
                        }
                    } else {
                        new FollowFail().setL();
                    }
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.add(textField);

        panel.add(label);


        this.add(panel);
        this.setVisible(true);
    }


}
