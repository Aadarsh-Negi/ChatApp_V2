import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import static javax.swing.BorderFactory.createEmptyBorder;

public class Client implements ActionListener, Runnable, KeyListener{
    JPanel top_area;
    JTextField temp_msg;
    JButton send;
    JButton sendFile;
    File file;
    static  JFrame screen;
    static JTextArea all_msg;
    String UserName;
    DataOutputStream writer;
    DataInputStream reader;
    Socket socketClient;
    JLabel l3;
    JLabel active_count;
    Client(String s,String ip,int port){
        try{
            socketClient = new Socket(ip, port);
            writer = new DataOutputStream(new DataOutputStream(socketClient.getOutputStream()));
            reader = new DataInputStream(new DataInputStream(socketClient.getInputStream()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Unable to connect to Server. Try again.");
            System.exit(0) ;
        }

        UserName = "[" + s + "] : ";
        screen = new JFrame("ChatApp");
        top_area = new JPanel();
        top_area.setLayout(null);
        top_area.setBackground(new Color(7, 94, 84));
        top_area.setBounds(0, 0, 450, 70);
        screen.add(top_area);


        l3 = new JLabel("Chat Room");
        l3.setFont(new Font("SAN_SERIF", Font.BOLD, 22));
        l3.setForeground(Color.WHITE);
        l3.setBounds(165, 15, 120, 18);
        top_area.add(l3);

        active_count = new JLabel("Active User : 0");
        active_count.setFont(new Font("SAN_SERIF",Font.BOLD,12));
        active_count.setForeground(Color.WHITE);
        active_count.setBounds(180, 40, 100, 18);
        top_area.add(active_count);

        all_msg = new JTextArea();
        all_msg.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        all_msg.setEditable(false);
        all_msg.setLineWrap(true);
        all_msg.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(all_msg,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBounds(5, 75, 440, 570);
        sp.setBorder(createEmptyBorder());
        sp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
        screen.add(sp);



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
                        writer.writeInt(1);
                        writer.writeUTF(str);
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
        sendFile.setBounds(320, 675, 123, 30);
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
                        byte[] fileContentbytes = new byte[(int) file.length()];
                        fileInputStream.read(fileContentbytes);
                        writer.writeInt(2);
                        writer.writeUTF(file.getName());
                         writer.writeInt(fileContentbytes.length);

                         writer.write(fileContentbytes);

                        writer.flush();
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
        send.setBounds(320, 650, 123, 30);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = UserName+ temp_msg.getText();
                if(str.length() == UserName.length()) return;
                try{
                    writer.writeInt(1);
                    writer.writeUTF(str);
                    writer.flush();
                }catch(Exception e2){}
                temp_msg.setText("");
            }
        });
        screen.add(send);

        screen.getContentPane().setBackground(Color.WHITE);
        screen.setLayout(null);
        screen.setSize(460, 750);
        screen.setLocation(300, 50);
        screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        screen.setResizable(false);
        screen.setVisible(true);

    }



    public void run() {
        while (true) {
            int flag = 0;
            try {
                flag = reader.readInt();
                if (flag == 2) {

                    String file_name = reader.readUTF();

                    int fileContentlen = 0;
                    fileContentlen = reader.readInt();

                    byte[] fb = new byte[fileContentlen];

                        reader.readFully(fb, 0, fileContentlen);
                        File downlaod = new File(file_name);
                        FileOutputStream fout = new FileOutputStream(downlaod);
                        fout.write(fb);
                        JOptionPane.showMessageDialog(screen, "New file recieved");
                        fout.close();
                }else{
                        String msg = "";
                        msg = reader.readUTF();
                        all_msg.append(msg + "\n");

                    }
                active_count.setText("Active User : " + reader.readInt());
            }catch (Exception ee){}
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}
