import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FirstScreen implements ActionListener {

    static JFrame screen;
    JTextField UserName;
    JTextField Port;
    JTextField Ip;
    JLabel txt;
    JLabel txt2;
    JLabel txt3;
    JButton connect;
    FirstScreen(){
        screen = new JFrame("ChatApp");

        txt = new JLabel("Enter your name");
        txt.setBounds(20,20,100,20);
        screen.add(txt);

        UserName = new JTextField();
        UserName.setBounds(20,50,200,30);
        screen.add(UserName);

        txt2 = new JLabel("Enter Server Ip address");
        txt2.setBounds(20,90,150,20);
        screen.add(txt2);

        Ip = new JTextField();
        Ip.setBounds(20,110,200,30);
        screen.add(Ip);

        txt3 = new JLabel("Enter port number");
        txt3.setBounds(20,150,120,20);
        screen.add(txt3);


        Port = new JTextField();
        Port.setBounds(20,170,200,30);
        screen.add(Port);


        connect = new JButton("Connect to chat Room");
        connect.setBounds(40,220,170,50);
//        send.setBackground(new Color(7,71,7));
        connect.addActionListener(this);
        screen.add(connect);



        screen.setLayout(null);
        screen.setSize(260,350);
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
            String p = Port.getText();
            String ip = Ip.getText();
            if(Name.length()==0) JOptionPane.showMessageDialog(screen, "UserName missing.");
            else if(ip.length() == 0) JOptionPane.showMessageDialog(screen, "Server Ip address missing.");
            else if(p.length() == 0) JOptionPane.showMessageDialog(screen, "Port number missing.");
            else{
                screen.setVisible(false);
                int pp = Integer.parseInt(p);
                Client one = new Client(Name,ip,pp);
                Thread t1 = new Thread(one);
                t1.start();
            }
    }
}
