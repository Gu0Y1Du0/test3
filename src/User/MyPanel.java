package User;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    public MyPanel() {
        setLayout(new BorderLayout());
        JLabel myTitle = new JLabel("我的信息", SwingConstants.CENTER);
        add(myTitle, BorderLayout.NORTH);
        JTextArea myInfo = new JTextArea("欢迎来到您的个人中心！");
        myInfo.setEditable(false);
        add(new JScrollPane(myInfo), BorderLayout.CENTER);
    }
}