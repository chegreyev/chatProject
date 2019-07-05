package MyExample;

import com.sun.xml.internal.ws.resources.ClientMessages;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientMachine {

    private Socket socket;
    private BufferedReader reader; // поток чтения из сокета
    private BufferedWriter writer; // поток чтения в сокет
    private BufferedReader inputUser; // поток чтения с консоли
    private String address; // ip адрес клиента
    private int port; // порт соединения
    private String nickname; // имя клиента
    private Date time;
    private String dateTime;
    private SimpleDateFormat dateFormat;

    // TODO: задокументировать все
    public ClientMachine(String address , int port){
        this.address = address;
        this.port = port;

        try{
            this.socket = new Socket(address , port);
        } catch (IOException e) {}

        try{
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            this.setNickname();
            new ReadMessage().start();
            new WriteMessage().start();

        }catch (IOException e){
            e.printStackTrace();

            ClientMachine.this.downServer();
        }

    }

    private void setNickname(){
        System.out.print("Set your nickname: ");
        try{
            nickname = inputUser.readLine();
            writer.write("Hello " + nickname + "\n");

            writer.flush();

        }catch(IOException e){}
    }

    private void downServer(){

        try{
            if(!socket.isClosed()){
                socket.close();
                reader.close();
                writer.close();
            }
        }catch(IOException e){}

    }

    private class ReadMessage extends Thread{
        @Override
        public void run(){

            String message;

            try{

                while(true){
                    message = reader.readLine();
                    if(message.equals("stop")){
                        ClientMachine.this.downServer();
                        break;
                    }
                    System.out.println(message);
                }

            } catch (IOException e){}

        }
    }

    private class WriteMessage extends Thread{
        @Override
        public void run(){

            while(true){

                String userMessage;

                try{
                    time = new Date();
                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    dateTime = dateFormat.format(time);

                    userMessage = inputUser.readLine();

                    if(userMessage.equals("stop")){
                        writer.write("stop" + "\n");
                        ClientMachine.this.downServer();
                        break;
                    } else {
                        writer.write(" (" + dateTime + ") " + nickname + " : " + userMessage + "\n");
                    }
                    writer.flush();

                }catch (IOException e){
                    ClientMachine.this.downServer();
                }

            }

        }
    }

}
