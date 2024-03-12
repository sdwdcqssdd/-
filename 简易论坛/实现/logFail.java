import javax.swing.*;

public class logFail extends JFrame {
    private JLabel label = new JLabel("用户名不存在，请重试");
    private JLabel label2 = new JLabel("密码错误");
    public logFail() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("登录失败");

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        int labelWidth = 200;
        int labelHeight = 50;
        int x = (getWidth() - labelWidth) / 2;
        int y = (getHeight() - labelHeight) / 2;
        label.setBounds(x, y, labelWidth, labelHeight);
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setVerticalAlignment(SwingConstants.CENTER);
        label2.setBounds(x, y, labelWidth, labelHeight);

        this.setLayout(null);
        this.add(label);
        label.setVisible(false);
        this.add(label2);
        label2.setVisible(false);
        this.setVisible(true);
    }

    public void set() {
        label.setVisible(true);
    }
    public void set2() {
        label2.setVisible(true);
    }

}