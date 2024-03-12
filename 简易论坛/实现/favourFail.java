import javax.swing.*;

public class favourFail extends JFrame {
    private JLabel label = new JLabel("收藏对象不存在");
    private JLabel label2 = new JLabel("id不能为空");
    public favourFail() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("收藏失败");

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        int labelWidth = 200;
        int labelHeight = 50;
        int x = (getWidth() - labelWidth) / 2;
        int y = (getHeight() - labelHeight) / 2;
        label.setBounds(x, y, labelWidth, labelHeight);
        label2.setBounds(x, y, labelWidth, labelHeight);
        this.setLayout(null);
        this.add(label);
        this.add(label2);
        label.setVisible(false);
        label2.setVisible(false);
        this.setVisible(true);
    }

    void setLabel() {
        label.setVisible(true);
    }

    void setLabel2() {
        label2.setVisible(true);
    }


}
