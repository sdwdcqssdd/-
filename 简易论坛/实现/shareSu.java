import javax.swing.*;

public class shareSu extends JFrame {
    private JLabel label = new JLabel("已转发");
    public shareSu() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setTitle("转发成功");

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