import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;

public class Post extends JFrame {
    private JButton button = new JButton("发布");
    private JButton button2 = new JButton("匿名发布");
    private JTextField textField = new JTextField();
    private JLabel label = new JLabel("标题");
    private JTextField textField2 = new JTextField();
    private JLabel label2 = new JLabel("正文");
    private Connection con = null;
    private PreparedStatement stmt = null;

    public void maxID() {
        try {
            stmt = con.prepareStatement("SELECT MAX(id) FROM post;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public Post(Connection c, PreparedStatement s,String name) {
        this.setSize(800, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("发布");
        con = c;
        stmt = s;
        textField.setBounds(100,50,600,25);
        textField2.setBounds(100,80,600,25);
        label.setBounds(50,50,100,25);
        label.setFont(new Font("宋体",Font.PLAIN,15));
        label2.setBounds(50,80,100,25);
        label2.setFont(new Font("宋体",Font.PLAIN,15));
        button.setBounds(400,125,70,25);
        button2.setBounds(230,125,100,25);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.add(textField);
        panel.add(textField2);
        panel.add(label);
        panel.add(label2);
        panel.add(button2);
        panel.add(button);

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField.getText()!= null && !textField.getText().trim().isEmpty() && textField2.getText()!= null && !textField2.getText().trim().isEmpty()) {
                    try {
                        maxID();
                        ResultSet rs = stmt.executeQuery();
                        int id = 0;
                        while (rs.next()) {
                            id = rs.getInt("max");
                        }
                        id++;
                        stmt = con.prepareStatement("insert into post(id,title, content, post_time,search_time) values (?,?,?,?,?);");
                        stmt.setInt(1,id);
                        stmt.setString(2,textField.getText());
                        stmt.setString(3,textField2.getText());
                        stmt.setTimestamp(4,new Timestamp(new Date().getTime()));
                        stmt.setInt(5,0);
                        stmt.executeUpdate();
                        con.commit();
                        new PostSu();
                    } catch (SQLException er) {
                        throw new RuntimeException(er);
                    }
                } else {
                    new PostFail();
                }
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField.getText()!= null && !textField.getText().trim().isEmpty() && textField2.getText()!= null && !textField2.getText().trim().isEmpty()) {
                    try {
                        maxID();
                        ResultSet rs = stmt.executeQuery();
                        int id = 0;
                        while (rs.next()) {
                            id = rs.getInt("max");
                        }
                        id++;
                        stmt = con.prepareStatement("insert into post(id, author, title, content, post_time,search_time) values (?,?,?,?,?,?);");
                        stmt.setInt(1,id);
                        stmt.setString(2,name);
                        stmt.setString(3,textField.getText());
                        stmt.setString(4,textField2.getText());
                        stmt.setTimestamp(5,new Timestamp(new Date().getTime()));
                        stmt.setInt(6,0);
                        stmt.executeUpdate();
                        con.commit();
                        new PostSu();
                    } catch (SQLException er) {
                        throw new RuntimeException(er);
                    }
                } else {
                    new PostFail();
                }
            }
        });

        this.add(panel);
        this.setVisible(true);
    }
}
