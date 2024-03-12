import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class 主界面 extends JFrame {
    private static Connection con = null;
    private static PreparedStatement stmt = null;
    private Image background;
    private JButton checkPost = new JButton("查看发布");
    private JButton checkLike = new JButton("查看点赞");
    private JButton checkFavour = new JButton("查看收藏");
    private JButton checkShare = new JButton("查看转发");
    private JButton checkFollow = new JButton("查看关注");
    private JButton Follow = new JButton("关注");
    private JButton quitFollow = new JButton("取消关注");
    private JButton post = new JButton("发帖");
    private JButton reply = new JButton("回复帖子");
    private JButton secondReply = new JButton("回复评论");
    private JButton Reply = new JButton("已回复");
    private JButton like = new JButton("点赞");
    private JButton Favour = new JButton("收藏");
    private JButton Share = new JButton("分享");
    private JButton hot = new JButton("热搜");
    private JButton search = new JButton("搜索");
    private JButton browse = new JButton("浏览");

    public static void checkPost() {
        try {
            stmt = con.prepareStatement("select * from post p join author a on p.author = a.name where a.name = ? ;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkAll() {
        try {
            stmt = con.prepareStatement("select * from post p order by id;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkHot() {
        try {
            stmt = con.prepareStatement("select * from post p order by search_time desc limit 10;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkReply() {
        try {
            stmt = con.prepareStatement("select * from post p join reply r on p.id = r.post_id join author a on a.id = r.author_id where a.name = ? ;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkLike() {
        try {
            stmt = con.prepareStatement("select * from post p join author_like_post alp on p.id = alp.post_id join author a on a.id = alp.author_id where a.name = ?;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkShare() {
        try {
            stmt = con.prepareStatement("select * from post p join author_shared a_s on p.id = a_s.post_id join author a on a.id = a_s.author_id where a.name = ?;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkFavor() {
        try {
            stmt = con.prepareStatement("select * from post p join author_favour_post afp on p.id = afp.post_id join author a on a.id = afp.author_id where a.name = ?;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void checkFollow() {
        try {
            stmt = con.prepareStatement("select * from author a join author_followed af on a.id = af.author_id join author a2 on a2.id = af.followed_id where a2.name = ?;");
        } catch (SQLException e) {
            System.err.println("failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
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

    private static ResultSet all() {
        ResultSet rs = null;
        if (con != null) {
            try {
                rs = stmt.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return rs;
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

    public 主界面(Connection con, PreparedStatement stmt, String name) {
        this.con = con;
        this.stmt = stmt;
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setTitle("主界面");
        this.setVisible(true);
        try {
            background = ImageIO.read(new File("C:\\Users\\汤嘉阳\\IdeaProjects\\project_2\\resource\\主界面背景.jpg")); // 根据文件路径读取图片
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), null); // 绘制图片
            }
        };
        init();
        panel.add(checkPost);
        panel.add(checkLike);
        panel.add(checkFavour);
        panel.add(checkShare);
        panel.add(checkFollow);
        panel.add(post);
        panel.add(Follow);
        panel.add(quitFollow);
        panel.add(reply);
        panel.add(browse);
        panel.add(secondReply);
        panel.add(Reply);
        panel.add(hot);
        panel.add(Favour);
        panel.add(like);
        panel.add(Share);
        checkPost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkPost();
                ResultSet rs = searchName(name);
                new checkPost(rs,con,stmt);
            }
        });

        checkLike.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLike();
                ResultSet rs = searchName(name);
                new checkLike(rs,con,stmt);
            }
        });

        checkShare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkShare();
                ResultSet rs = searchName(name);
                new checkShare(rs,con,stmt);
            }
        });

        checkFavour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkFavor();
                ResultSet rs = searchName(name);
                new checkFavour(rs,con,stmt);
            }
        });

        checkFollow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkFollow();
                ResultSet rs = searchName(name);
                new checkFollow(rs,con,stmt);
            }
        });

        post.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 new Post(con,stmt,name);
            }
        });

        Follow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 new Follow(con,stmt,name);
            }
        });

        quitFollow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new quitFollow(con,stmt,name);
            }
        });

        reply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new reply(con,stmt,name);
            }
        });

        secondReply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new secondReply(con,stmt,name);
            }
        });



        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new searchChose(con,stmt);
            }
        });

        hot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkHot();
                ResultSet rs = all();
                new hot(rs,con,stmt);
            }
        });

        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAll();
                ResultSet rs = all();
                new browse(rs,con,stmt);
            }
        });

        like.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Update(con,stmt,name);
            }
        });

        Favour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Favour(con,stmt,name);
            }
        });

        Reply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkReply();
                ResultSet rs = searchName(name);
                new Rep(rs,con,stmt);
            }
        });

        Share.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Share(con,stmt,name);
            }
        });

        panel.add(search);
        panel.setLayout(null);
        add(panel);
    }

    private  void init() {
        checkPost.setSize(100, 30);
        checkPost.setLocation(150, 200);
        checkShare.setSize(100, 30);
        checkShare.setLocation(150, 300);
        checkLike.setSize(100, 30);
        checkLike.setLocation(150, 400);
        checkFavour.setSize(100,30);
        checkFavour.setLocation(400,200);
        checkFollow.setSize(100,30);
        checkFollow.setLocation(400,300);
        Follow.setBounds(150,500,100,30);
        quitFollow.setBounds(150,600,100,30);
        reply.setBounds(400,500,100,30);
        secondReply.setBounds(400,600,100,30);
        Reply.setBounds(600,200,100,30);
        post.setSize(100,30);
        post.setLocation(400,400);
        like.setBounds(600,300,100,30);
        Share.setBounds(600,400,100,30);
        Favour.setBounds(600,500,100,30);
        search.setBounds(500,50,90,20);
        browse.setBounds(330,50,90,20);
        hot.setBounds(670,50,90,20);
    }

    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        new 主界面(con,stmt,"fdfe").setVisible(true);
    }
}
