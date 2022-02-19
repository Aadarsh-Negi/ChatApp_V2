import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.*;

public class Client implements ActionListener, Runnable, KeyListener{
    JPanel top_area;
    JTextField temp_msg;
    JButton send;
    static  JFrame screen;
    static JTextArea all_msg;
    String UserName;
    BufferedWriter writer;
    BufferedReader reader;

    Client(String s,String ip,int port){
        UserName = "[" + s + "] : ";
        screen = new JFrame();
        top_area = new JPanel();
        top_area.setLayout(null);
        top_area.setBackground(new Color(7, 94, 84));
        top_area.setBounds(0, 0, 450, 70);
        screen.add(top_area);



//
//        JLabel l3 = new JLabel("Mirzapur");
//        l3.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
//        l3.setForeground(Color.WHITE);
//        l3.setBounds(110, 15, 100, 18);
//        top_area.add(l3);
//
//
//        JLabel l4 = new JLabel("Kaaleen, Guddu, Bablu, Sweety, IG Dubey, Shukla");
//        l4.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
//        l4.setForeground(Color.WHITE);
//        l4.setBounds(110, 35, 160, 20);
//        top_area.add(l4);


        all_msg = new JTextArea();
        all_msg.setBounds(5, 75, 440, 570);
        all_msg.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        all_msg.setEditable(false);
        all_msg.setLineWrap(true);
        all_msg.setWrapStyleWord(true);
        screen.add(all_msg);


        temp_msg = new JTextField();
        temp_msg.setBounds(5, 655, 310, 40);
        temp_msg.addActionListener(this);
        temp_msg.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        screen.add(temp_msg);

        send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        send.addActionListener(this);
        screen.add(send);

        screen.getContentPane().setBackground(Color.WHITE);
        screen.setLayout(null);
        screen.setSize(460, 740);
        screen.setLocation(300, 50);
//        setUndecorated(true);
        screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        screen.setVisible(true);

        try{

            Socket socketClient = new Socket(ip, port);
            writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        }catch(Exception e){}


    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER){
            String str = UserName+ temp_msg.getText();
            if(str.length() == UserName.length()) return;
            try{
                writer.write(str);
                writer.write("\r\n");
                writer.flush();
            }catch(Exception e2){}
            temp_msg.setText("");

        }
    }

    @Override
    public void keyReleased(KeyEvent arg) {}

    @Override
    public void keyTyped(KeyEvent arg) {}

    public void actionPerformed(ActionEvent ae){
        String str = UserName+ temp_msg.getText();
        if(str.length() == UserName.length()) return;
        try{
            writer.write(str);
            writer.write("\r\n");
            writer.flush();
        }catch(Exception e){}
        temp_msg.setText("");
    }

    public void run(){
        try{
            String msg = "";
            while((msg = reader.readLine()) != null){
                all_msg.append(msg + "\n");
            }
        }catch(Exception e){}
    }
}
