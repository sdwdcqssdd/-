import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class T extends JFrame {

    private static Connection con = null;
    private static PreparedStatement stmt = null;
    private static ResultSet search(String begin,String end) {
        ResultSet rs = null;
        Timestamp timestamp = Timestamp.valueOf(begin + " 00:00:00");
        Timestamp timestamp1 = Timestamp.valueOf(end + " 00:00:00");
        if (con != null) {
            try {
                stmt.setTimestamp(1,timestamp);
                stmt.setTimestamp(2,timestamp1);
                rs = stmt.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return rs;
    }

    public T(Connection c, PreparedStatement s) {
        this.setSize(300,300);
        this.setTitle("时间检索");
        this.setLocationRelativeTo(null);
        con = c;
        stmt = s;
        JTextField textField = new JTextField();
        JLabel format = new JLabel("格式: 年-月-日");
        JButton exe = new JButton("检索");
        textField.setBounds(70,60,150,25);
        JLabel start = new JLabel("开始时间");
        start.setBounds(20,50,60,40);
        JLabel end = new JLabel("结束时间");
        end.setBounds(20,90,60,40);
        JTextField field = new JTextField();
        field.setBounds(70,100,150,25);
        format.setBounds(90,150,150,40);
        exe.setBounds(100,200,70,30);
        try {
            stmt = con.prepareStatement("select * from post where post_time >= ? and post_time <= ?;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        exe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = textField.getText();
                String key2 = field.getText();
                ResultSet rs = search(key,key2);
                new TR(rs,con,stmt);
            }
        });
        JPanel panel = new JPanel();
        panel.add(textField);
        panel.add(field);
        panel.add(start);
        panel.add(end);
        panel.add(exe);
        panel.add(format);
        panel.setLayout(null);
        this.add(panel);
        this.setVisible(true);
    }

}
