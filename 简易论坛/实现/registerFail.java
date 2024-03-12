import javax.swing.*;

public class registerFail extends JFrame {
    public JLabel label = new JLabel("用户名已存在，请重试");
    public JLabel label2 = new JLabel("输入密码");
    public registerFail() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("注册失败");

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

    public void setLabel() {
        label.setVisible(true);
    }

    public void setLabel2() {
        label2.setVisible(true);
    }


}
