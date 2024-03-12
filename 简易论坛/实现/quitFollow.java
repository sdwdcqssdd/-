import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;

public class quitFollow extends JFrame {

    private JTextField textField = new JTextField();
    private JLabel label = new JLabel("用户名");

    private Connection con = null;
    private PreparedStatement stmt = null;


    public quitFollow(Connection c, PreparedStatement s,String name) {
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("取关");
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
                            stmt = con.prepareStatement("select id from author where name = ?;");
                            stmt.setString(1,name);
                            ResultSet r3 = stmt.executeQuery();
                            String idd = "";
                            if (r3.next()) {
                                idd = r3.getString("id");
                            }
                            stmt = con.prepareStatement("delete from author_followed where author_id = ? and followed_id = ?; ");
                            stmt.setString(1,id);
                            stmt.setString(2,idd);
                            stmt.executeUpdate();
                            new quitFollowS();
                            con.commit();
                        } else {
                            new quitFollowFail().set1();
                        }
                    } else {
                        new quitFollowFail().setL();
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
