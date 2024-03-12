import javax.swing.*;

public class PostFail extends JFrame {
    private JLabel label = new JLabel("标题正文不得为空");
    public PostFail() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("发布失败");

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        int labelWidth = 200;
        int labelHeight = 50;
        int x = (getWidth() - labelWidth) / 2;
        int y = (getHeight() - labelHeight) / 2;
        label.setBounds(x, y, labelWidth, labelHeight);
        this.setLayout(null);
        this.add(label);
        this.setVisible(true);
    }


}
