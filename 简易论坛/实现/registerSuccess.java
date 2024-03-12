import javax.swing.*;
import java.awt.*;

public class registerSuccess extends JFrame {
    private JLabel label = new JLabel("注册成功");


    public registerSuccess() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("注册成功");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        int labelWidth = 200;
        int labelHeight = 50;
        int x = (getWidth() - labelWidth) / 2;
        int y = (getHeight() - labelHeight) / 2;
        label.setBounds(100, 20, 200, 100);
        label.setFont(new Font("宋体",Font.PLAIN,15));
        this.setLayout(null);
        this.add(label);
        this.setVisible(true);
    }


}
