import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class secondReply extends JFrame {
    private JButton button = new JButton("发布");
    private JButton button1 = new JButton("匿名发布");
    private JTextField textField = new JTextField();
    private JLabel label = new JLabel("内容");
    private JTextField textField2 = new JTextField();
    private JLabel label2 = new JLabel("star");
    private JLabel la = new JLabel("reply id");
    private JTextField text = new JTextField();
    private Connection con = null;
    private PreparedStatement stmt = null;

    public secondReply(Connection c, PreparedStatement s, String name) {
        this.setSize(800, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("发布");
        con = c;
        stmt = s;
        textField.setBounds(100,50,600,25);
        text.setBounds(100,20,600,25);
        textField2.setBounds(100,80,600,25);
        label.setBounds(50,50,100,25);
        label.setFont(new Font("宋体",Font.PLAIN,15));
        la.setBounds(30,20,100,25);
        la.setFont(new Font("宋体",Font.PLAIN,15));
        label2.setBounds(50,80,100,25);
        label2.setFont(new Font("宋体",Font.PLAIN,15));
        button.setBounds(400,125,70,25);
        button1.setBounds(250,125,100,25);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.add(textField);
        panel.add(textField2);
        panel.add(label);
        panel.add(label2);
        panel.add(la);
        panel.add(text);
        panel.add(button);
        panel.add(button1);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((textField.getText()!= null && !textField.getText().trim().isEmpty()) && (text.getText()!= null && !text.getText().trim().isEmpty())) {
                    try {
                        stmt = con.prepareStatement("select * from second_reply where id = ?;" );
                        stmt.setInt(1,Integer.parseInt(text.getText()));
                        ResultSet rs = stmt.executeQuery();
                        if (!rs.next()) {
                            new replyFail().setLabel();
                        } else {
                            stmt = con.prepareStatement("select id from author where name = ?;");
                            stmt.setString(1,name);
                            ResultSet rs2 = stmt.executeQuery();
                            String idd = "";
                            if (rs2.next()) {
                                idd = rs2.getString("id");
                            }
                            if (textField2.getText()== null || textField2.getText().trim().isEmpty()) {
                                stmt = con.prepareStatement("insert into second_reply(reply_id, content, author_id) values (?,?,?);");
                                stmt.setInt(1, Integer.parseInt(text.getText()));
                                stmt.setString(2, textField.getText());
                                stmt.setString(3, idd);
                            } else {
                                stmt = con.prepareStatement("insert into second_reply(reply_id, content,star,author_id) values (?,?,?,?);");
                                stmt.setInt(1, Integer.parseInt(text.getText()));
                                stmt.setString(2, textField.getText());
                                stmt.setInt(3,Integer.parseInt(textField2.getText()));
                                stmt.setString(4, idd);
                            }
                            stmt.executeUpdate();
                            con.commit();
                            new PostSu();
                        }
                    } catch (SQLException er) {
                        throw new RuntimeException(er);
                    }
                } else {
                    new replyFail().setLabel2();
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((textField.getText()!= null && !textField.getText().trim().isEmpty()) && (text.getText()!= null && !text.getText().trim().isEmpty())) {
                    try {
                        stmt = con.prepareStatement("select * from post where id = ?;" );
                        stmt.setInt(1,Integer.parseInt(text.getText()));
                        ResultSet rs = stmt.executeQuery();
                        if (!rs.next()) {
                            new replyFail().setLabel();
                        } else {
                            if (textField2.getText()== null || textField2.getText().trim().isEmpty()) {
                                stmt = con.prepareStatement("insert into second_reply(reply_id, content) values (?,?);");
                                stmt.setInt(1, Integer.parseInt(text.getText()));
                                stmt.setString(2, textField.getText());
                            } else {
                                stmt = con.prepareStatement("insert into second_reply(reply_id, content,star) values (?,?,?);");
                                stmt.setInt(1, Integer.parseInt(text.getText()));
                                stmt.setString(2, textField.getText());
                                stmt.setInt(3,Integer.parseInt(textField2.getText()));
                            }
                            stmt.executeUpdate();
                            con.commit();
                            new PostSu();
                        }
                    } catch (SQLException er) {
                        throw new RuntimeException(er);
                    }
                } else {
                    new replyFail().setLabel2();
                }
            }
        });

        this.add(panel);
        this.setVisible(true);
    }


}
