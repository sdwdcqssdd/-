import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Update extends JFrame {
    private JButton button = new JButton("点赞");
    private JTextField textField = new JTextField();
    private JLabel la = new JLabel("post id");
    private Connection con = null;
    private PreparedStatement stmt = null;
    public Update(Connection c, PreparedStatement s,String name) {
        this.setSize(800, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("点赞");
        con = c;
        stmt = s;
        textField.setBounds(100,50,600,25);

        la.setBounds(30,50,100,25);
        la.setFont(new Font("宋体",Font.PLAIN,15));
        button.setBounds(400,125,70,25);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.add(textField);
        panel.add(la);
        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((textField.getText()!= null && !textField.getText().trim().isEmpty())) {
                    try {
                        stmt = con.prepareStatement("select * from post where id = ?;" );
                        stmt.setInt(1,Integer.parseInt(textField.getText()));
                        ResultSet rs = stmt.executeQuery();
                        if (!rs.next()) {
                            new likeFail().setLabel();
                        } else {
                            stmt = con.prepareStatement("select id from author where name = ?;");
                            stmt.setString(1,name);
                            ResultSet rs2 = stmt.executeQuery();
                            String idd = "";
                            if (rs2.next()) {
                                idd = rs2.getString("id");
                            }
                            stmt = con.prepareStatement("insert into author_like_post(post_id,author_id) values (?,?);");
                            stmt.setInt(1, Integer.parseInt(textField.getText()));
                            stmt.setString(2, idd);
                            stmt.executeUpdate();
                            con.commit();
                            new likeSu();
                        }
                    } catch (SQLException er) {
                        throw new RuntimeException(er);
                    }
                } else {
                    new likeFail().setLabel2();
                }
            }
        });
        this.add(panel);
        this.setVisible(true);
    }
}
