package MyExample;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ServerMachine> serverList = new LinkedList<>();
    public static MessageStorage storage;

    public static void main(String[] args) throws IOException {
        // Создаем сокет сервер куда отсылаются все сообщения
        ServerSocket server = new ServerSocket(PORT);
        // Создаем историю наших сообщений клиента
        //TODO:
        storage = new MessageStorage();
        System.out.println("Сервер запущен");

        try{
            while(true){
                // Ждем клинтов для подключения
                Socket socket = server.accept();
                try{
                    // Новый клиент , создаем для него свой сокет
                    // для прослушки / записи
                    serverList.add(new ServerMachine(socket));
                } catch (IOException e ){
                    // Если не получится соединится то закрывает соединение с сервером и с сокетом
                    socket.close();
                }
            }
        } finally {
            server.close();
        }

    }
}
