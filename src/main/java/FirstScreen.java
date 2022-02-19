import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FirstScreen implements ActionListener {

    static JFrame screen;
    JTextField UserName;
    JLabel txt;
    JButton connect;
    FirstScreen(){
        screen = new JFrame();

        txt = new JLabel("Enter your name");
        txt.setBounds(20,20,100,20);
        screen.add(txt);

        UserName = new JTextField();
        UserName.setBounds(20,50,200,30);
        screen.add(UserName);

        connect = new JButton("Connect to chat Room");
        connect.setBounds(40,130,170,50);
//        send.setBackground(new Color(7,71,7));
        connect.addActionListener(this);
        screen.add(connect);



        screen.setLayout(null);
        screen.setSize(300,300);
        screen.setLocation(500,200);
        screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        screen.setVisible(true);
    }

    public static void main(String[] arg){
        new FirstScreen();


    }


    @Override
    public void actionPerformed(ActionEvent e) {
            String Name = UserName.getText();

            if(Name.length()==0){
                JOptionPane.showMessageDialog(screen, "Enter your Name");
            }else{
                screen.setVisible(false);
                Client one = new Client(Name);
                Thread t1 = new Thread(one);
                t1.start();
            }
    }
}
