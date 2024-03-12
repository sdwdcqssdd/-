
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class searchChose extends JFrame {
    public JButton keyWd = new JButton("关键词检索");
    public JButton time = new JButton("时间检索");


    public searchChose(Connection con, PreparedStatement stmt) {
        this.setSize(300, 300);
        this.setLocationRelativeTo(null);
        this.setTitle("搜索");
        this.setVisible(true);
        init();
        JPanel panel = new JPanel();
        panel.add(keyWd);
        panel.add(time);
        keyWd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Key(con,stmt);
            }
        });
        time.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new T(con,stmt);
            }
        });
        panel.setLayout(null);
        add(panel);
    }

    private void init() {
        keyWd.setSize(100, 20);
        keyWd.setLocation(100, 100);
        time.setSize(100, 20);
        time.setLocation(100, 150);
    }


}
