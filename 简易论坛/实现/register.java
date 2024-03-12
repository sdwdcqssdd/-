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
import java.util.Date;
import java.util.Properties;

public class register extends JFrame {
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

    private static void insertAuthor(String ID,String name,Timestamp timestamp,String Phone,String code) {
        if (con != null) {
            try {
                stmt.setString(1, ID);
                stmt.setString(2, name);
                stmt.setTimestamp(3, timestamp);
                stmt.setString(4, Phone);
                stmt.setString(5,code);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void checkName() {
        try {
            stmt = con.prepareStatement("select name from author where name = ? ;");
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

    private static void register() {
        try {
            stmt = con.prepareStatement("insert into author(id, name, registration_time, phone,code) " +
                    "VALUES (?,?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void maxID() {
        try {
            stmt = con.prepareStatement("SELECT MAX(id) FROM author;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static String generateID() {
        ResultSet rs;
        long id = 0;
        if (con != null) {
            try {
                rs = stmt.executeQuery();
                if (rs.next()) {
                    id = Long.parseLong(rs.getString(1)) + 1;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        String gid;
        gid = String.valueOf(id);
        return gid;
    }

    private Image backgroundImage;
    private JTextField textField = new JTextField(20);
    private JTextField textField2 = new JTextField(20);
    private JTextField textField3 = new JTextField(20);
    private JLabel label1 = new JLabel("用户名");
    private JLabel label2 = new JLabel("手机");
    private JLabel label3 = new JLabel("密码");
    private JButton button = new JButton("确认");
    String name;
    public register() {
        Properties prop = loadDBUser();
        openDB(prop);
        this.setSize(300, 300);
        //  this.setDefaultCloseOperation(FirstPage.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("注册");
        textField.setBounds(100,50,100,25);
        textField2.setBounds(100,100,100,25);
        textField3.setBounds(100,150,100,25);
        label1.setBounds(50,50,100,25);
        label1.setFont(new Font("宋体",Font.PLAIN,15));
        label2.setBounds(50,100,100,25);
        label2.setFont(new Font("宋体",Font.PLAIN,15));
        label3.setBounds(50,150,100,25);
        label3.setFont(new Font("宋体",Font.PLAIN,15));
        button.setBounds(110,200,70,25);

        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\汤嘉阳\\IdeaProjects\\project_2\\resource\\注册背景.jpg")); // 根据文件路径读取图片
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
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = textField.getText();
                try {
                    checkName();
                    ResultSet rs = searchName(name);
                    if (rs.next()) {
                        registerFail r = new registerFail();
                        r.setLabel();
                    } else {
                        String phone = textField2.getText();
                        maxID();
                        String id = generateID();
                        register();
                        Timestamp timestamp = new Timestamp(new Date().getTime());
                        if (textField3.getText().equals("")) {
                            registerFail r = new registerFail();
                            r.setLabel2();
                        } else {
                            insertAuthor(id,name,timestamp,phone,textField3.getText());
                            con.commit();
                            new registerSuccess();
                        }
                    }
                } catch (SQLException error) {
                    throw new RuntimeException(error);
                }
            }
        });
        panel.setLayout(null);
        panel.add(textField);
        panel.add(textField2);
        panel.add(textField3);
        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(button);
        textField2.requestFocus();
        textField3.requestFocus();
        textField.requestFocus();
        add(panel);
    }


}
