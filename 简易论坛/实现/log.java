import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class log extends JFrame {
    private static Connection con = null;
    private static PreparedStatement stmt = null;
    private static void openDB(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
        try {
            con = DriverManager.getConnection(url, prop);
            if (con != null) {
                con.setAutoCommit(false);
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void checkName() {
        try {
            stmt = con.prepareStatement("select name,code from author where name = ? ;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void closeDB() {
        if (con != null) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                con.close();
                con = null;
            } catch (Exception ignored) {
            }
        }
    }

    private static ResultSet searchName(String name) {
        ResultSet rs = null;
        if (con != null) {
            try {
                stmt.setString(1,name);
                rs = stmt.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return rs;
    }

    private static Properties loadDBUser() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream("C:\\Users\\汤嘉阳\\IdeaProjects\\project_2\\resource\\dbUser.properties")));
            return properties;
        } catch (IOException e) {
            System.err.println("can not find db user file");
            throw new RuntimeException(e);
        }
    }

    private Image backgroundImage;
    private JTextField textField = new JTextField(20);
    private JLabel label = new JLabel("用户名");
    private JTextField textField2 = new JTextField(20);
    private JLabel label2 = new JLabel("密码");
    private JButton button = new JButton("确认");
    String name;
    public log() {
        Properties prop = loadDBUser();
        openDB(prop);
        this.setSize(300, 300);
      //  this.setDefaultCloseOperation(FirstPage.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("登录");
        textField.setBounds(100,50,100,25);
        textField2.setBounds(100,100,100,25);
        label.setBounds(50,50,100,25);
        label.setFont(new Font("宋体",Font.PLAIN,15));
        label2.setBounds(50,100,100,25);
        label2.setFont(new Font("宋体",Font.PLAIN,15));
        button.setBounds(110,200,70,25);
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\汤嘉阳\\IdeaProjects\\project_2\\resource\\登录背景.jpg")); // 根据文件路径读取图片
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
        panel.setLayout(null);
        panel.add(textField);
        panel.add(label);
        panel.add(textField2);
        panel.add(label2);
        panel.add(button);
        button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    name = textField.getText();
                    try {
                        checkName();
                        ResultSet rs = searchName(name);
                        if (!rs.next()) {
                            new logFail().set();
                        } else {
                            String code = rs.getString("code");
                            if (code.equals(textField2.getText())) {
                                new 主界面(con,stmt,name).setVisible(true);
                            } else {
                                new logFail().set2();
                            }
                        }
                    } catch (SQLException error) {
                        throw new RuntimeException(error);
                    }
                }
            }
        );
        textField.requestFocus();
        textField2.requestFocus();
        add(panel);
    }

    public static void main(String[] args) {
        new log().setVisible(true);
    }
}
