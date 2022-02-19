
import java.net.*;
import java.io.*;
import java.util.*;

public class Server implements Runnable{

    Socket socket;

    public static Vector client = new Vector();

    public Server(Socket socket){
        try{
            this.socket = socket;
        }catch(Exception e){}
    }


    public void run(){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            client.add(writer);

            while(true){
                String data = reader.readLine().trim();


                for(int i = 0; i < client.size(); i++){
                    try{
                        BufferedWriter bw = (BufferedWriter)client.get(i);
                        bw.write(data);
                        bw.write("\r\n");
                        bw.flush();
                    }catch(Exception e){}
                }

            }
        }catch(Exception e){}

    }


    public static void main(String[] args) throws Exception{
        ServerSocket s = new ServerSocket(4444);
        System.out.println("Server is UP on port 4444");
        while(true){
            Socket socket = s.accept();
            System.out.println("We have new User");
            Server server = new Server(socket);
            Thread thread = new Thread(server);
            thread.start();
        }
    }
}
