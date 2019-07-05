package MyExample;

import java.io.*;
import java.net.Socket;

public class ServerMachine extends Thread {

    private Socket socket; // сокет через который клинт общается с сервером
    private BufferedReader reader; // читает инфу с сокета
    private BufferedWriter writer; // пишет инфу в сокет
    private BufferedReader adminReader; // админ пишет всем пользователсям

    public ServerMachine(Socket socket) throws IOException{
        this.socket = socket;

        adminReader = new BufferedReader(new InputStreamReader(System.in));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // При подключении отправляет клиенту все его сообщения
        Server.storage.printStorage(writer);

        // Запускаем наш поток
        start();

        // Запускаем поток Админа
        new AdminMessage().start();
    }

    // TODO: задокументировать все
    @Override
    public void run(){
        String word;

        try{
            // Объявляем никнейм
            word = reader.readLine();

            // Объявление на сервере клиента
            System.out.println("Клиент подключился : " + word.split(" ")[1]);

            try{
                writer.write(word + "\n");
                writer.flush();
            } catch (IOException e){}

            try{

                while(true){

                    word = reader.readLine();

                    if(word.equals("stop")){

                        this.downServer();
                        break;

                    }

                    System.out.println("Клиент : " + word);
                    Server.storage.addMessages(word);

                    for(ServerMachine serverMachine : Server.serverList){
                        serverMachine.send(word);
                    }

                }

            } catch(NullPointerException e){
                e.printStackTrace();
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // TODO: задокументировать все
    private void send(String message){
        try{
            writer.write(message + "\n");
            writer.flush();
        }catch(IOException e){}
    }

    // TODO: задокументировать все
    private void downServer(){
        try{
            if(!socket.isClosed()){
                socket.close();
                reader.close();
                writer.close();

                for(ServerMachine serverMachine : Server.serverList){
                    if(serverMachine.equals(this)) serverMachine.interrupt();
                    Server.serverList.remove(this);
                }
            }
        }catch (IOException e){}
    }

    // TODO: админ класс
    private class AdminMessage extends Thread{
        @Override
        public void run() {

            while(true){
                String adminMessage;

                try{
                    adminMessage = " (ADMIN) " + adminReader.readLine();
                    send(adminMessage);

                }catch(IOException e){

                }catch (NullPointerException e){

                }


            }

        }
    }
}
