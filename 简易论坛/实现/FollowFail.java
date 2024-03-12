import javax.swing.*;

public class FollowFail extends JFrame {
    private JLabel label = new JLabel("已经关注了");
    private JLabel l = new JLabel("用户不存在");
    public FollowFail() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("关注失败");

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setVerticalAlignment(SwingConstants.CENTER);
        int labelWidth = 200;
        int labelHeight = 50;
        int x = (getWidth() - labelWidth) / 2;
        int y = (getHeight() - labelHeight) / 2;
        label.setBounds(x, y, labelWidth, labelHeight);
        label.setVisible(false);
        l.setBounds(x, y, labelWidth, labelHeight);
        l.setVisible(false);
        this.setLayout(null);
        this.add(label);
        this.add(l);
        this.setVisible(true);
    }

    void set1() {
        label.setVisible(true);
    }

    void setL() {
        l.setVisible(true);
    }



}
