
import java.net.*;
import java.io.*;
import java.util.*;

public class Server implements Runnable{

    Socket socket;

    static Vector<BufferedWriter> client_writer = new Vector<>();
    static Vector<DataOutputStream> client_dout = new Vector<>();

    public Server(Socket socket){
        try{
            this.socket = socket;
        }catch(Exception e){}
    }


    public void run(){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            DataInputStream din = new DataInputStream(socket.getInputStream());
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

            client_writer.add(writer);
            client_dout.add(dout);

            while(true){
                int msg = Integer.parseInt(String.valueOf(reader.read()));
                if(msg==1){
                    System.out.println("msg ok");
                    String data = reader.readLine().trim();
                    System.out.println("msg : - " +data);
                    for (BufferedWriter BW : client_writer) {
                        try {
                            BW.write(data + "\r\n");
                            BW.flush();
                        } catch (Exception e) {}
                    }
                }else {
                    System.out.println("file recieved");
                    int fileContentlen = din.readInt();
                    System.out.println("len :"+ fileContentlen);
                    byte[] fb = new byte[fileContentlen];
                    System.out.println("done 1");
                    din.readFully(fb,0,fileContentlen);
                    System.out.println("done 2");
//                    File downlaod = new File("new");
                    try {
                        for (DataOutputStream dt : client_dout) {
                                dt.writeInt(fileContentlen);
                                dt.write(fb);
                        }
//                        FileOutputStream fout  = new FileOutputStream(downlaod);
//                        fout.write(fb);
//                        fout.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("looks good");
                }
//                    reader.readLine()
                System.out.println("outt");
            }
        }catch(Exception e){}

    }


    public static void main(String[] args) throws Exception{
        ServerSocket s = new ServerSocket(4444);
        System.out.println("Server is UP on port 4444");
        while(true){
            Socket socket = s.accept();
            System.out.println("We have a new User");
            Server server = new Server(socket);
            Thread thread = new Thread(server);
            thread.start();
        }
    }
}
