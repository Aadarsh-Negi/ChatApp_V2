import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.*;

public class Client implements ActionListener, Runnable, KeyListener{
    JPanel top_area;
    JTextField temp_msg;
    JButton send;
    JButton sendFile;
    File file;
    static  JFrame screen;
    static JTextArea all_msg;
    String UserName;
    BufferedWriter writer;
    BufferedReader reader;
    Socket socketClient;
    Client(String s,String ip,int port){
        try{
            socketClient = new Socket(ip, port);
            writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Unable to connect to Server. Try again.");
//            System.exit(0) ;
        }

        UserName = "[" + s + "] : ";
        screen = new JFrame("ChatApp");
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
//        screen.add(all_msg);


        temp_msg = new JTextField();
        temp_msg.setBounds(5, 655, 310, 40);
        temp_msg.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String str = UserName + temp_msg.getText();
                    if (str.length() == UserName.length()) return;
                    try {
                        writer.write(1);
                        writer.write(str + "\r\n");
                        writer.flush();
                    } catch (Exception e2) {}
                    temp_msg.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        temp_msg.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        screen.add(temp_msg);

        sendFile = new JButton("Send File");
        sendFile.setBounds(320, 700, 123, 40);
        sendFile.setBackground(new Color(7, 94, 84));
        sendFile.setForeground(Color.WHITE);
        sendFile.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        sendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser send_file = new JFileChooser();
                send_file.setDialogTitle("Choose a file to send");
                if(send_file.showOpenDialog(null)==send_file.APPROVE_OPTION){
                    file = send_file.getSelectedFile();
                    try {
                        FileInputStream fileInputStream  = new FileInputStream(file.getAbsolutePath());
                         DataOutputStream dout = new DataOutputStream(socketClient.getOutputStream());
                        byte[] fileContentbytes = new byte[(int) file.length()];
                        fileInputStream.read(fileContentbytes);
                        writer.write(2);
                        writer.flush();
                        System.out.println("ck1");
                         dout.writeInt(fileContentbytes.length);
                        System.out.println("ck2");
                         dout.write(fileContentbytes);
                        System.out.println("ck3");
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });

        screen.add(sendFile);

        send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = UserName+ temp_msg.getText();
                if(str.length() == UserName.length()) return;
                try{
                    writer.write(1);
                    writer.write(str + "\r\n");
                    writer.flush();
                }catch(Exception e2){}
                temp_msg.setText("");
            }
        });
        screen.add(send);

        screen.getContentPane().setBackground(Color.WHITE);
        screen.setLayout(null);
        screen.setSize(460, 800);
        screen.setLocation(300, 50);
//        setUndecorated(true);
        screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        screen.setVisible(true);



    }



    public void run(){
        try{
            String msg = "";
            while((msg = reader.readLine()) != null){
                all_msg.append(msg + "\n");
            }
        }catch(Exception e){}
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String []arg){
        new Client("ad","localhost",4444);
    }
}
