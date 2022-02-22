import java.net.*;
import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class Server implements Runnable{

    Socket socket;
    static Vector<DataOutputStream> client_dout = new Vector<>();

    public Server(Socket socket){
            this.socket = socket;
    }


    public void run(){
        try{
            DataInputStream din = new DataInputStream(socket.getInputStream());
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

            client_dout.add(dout);

            while(true){
                int msg = din.readInt();
                Vector<Integer> to_remove = new Vector<>();
                int index_to_remove=0;
                if(msg==1){
                    String data = din.readUTF();
                    for (DataOutputStream BW : client_dout) {
                        try{
                            BW.writeInt(1);
                            BW.writeUTF(data);
                            BW.flush();
                        }catch (IOException ee){
                            to_remove.add(index_to_remove);
                        }
                        index_to_remove++;
                    }
                }else {



                    int fileContentlen = din.readInt();
                    byte[] fb = new byte[fileContentlen];
                    din.readFully(fb,0,fileContentlen);

                        for (DataOutputStream dt : client_dout) {
                            try{
                                   dt.writeInt(2);
                                   dt.writeInt(fileContentlen);
                                   dt.write(fb);
                            }catch (IOException ee){
                                     to_remove.add(index_to_remove);
                              }
                              index_to_remove++;
                        }
                }
                System.out.println(to_remove.size() + " users left");
                for(int i:to_remove) client_dout.remove(i);
                for(DataOutputStream dt : client_dout){
                        dt.writeInt(client_dout.size());
                }
            }
        }catch (IOException e){}

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
