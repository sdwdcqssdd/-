
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FirstPage extends JFrame {
    private Image backgroundImage;
    public JButton register = new JButton("注册");
    public JButton log = new JButton("登录");
    Font lll = new Font("楷体", 2, 30);


    public FirstPage() {
        this.setSize(1000, 800);
        this.setDefaultCloseOperation(FirstPage.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("开始界面");
        this.setVisible(true);
        init();
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\汤嘉阳\\IdeaProjects\\project_2\\resource\\背景.jpg")); // 根据文件路径读取图片
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null); // 绘制图片
            }
        };

        //将所有的组件添加到panel面板中
        panel.add(register);
        panel.add(log);
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new register().setVisible(true);
            }
        });
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new log().setVisible(true);
            }
        });
        panel.setLayout(null);
        add(panel);
    }

    private void init() {
        register.setSize(200, 60);
        register.setLocation(400, 200);
        log.setSize(200, 60);
        log.setLocation(400, 400);
        log.setBorder(BorderFactory.createLoweredBevelBorder());
        register.setFont(lll);
        log.setFont(lll);
    }

    public static void main(String[] args) {
        new FirstPage().setVisible(true);
    }

}










