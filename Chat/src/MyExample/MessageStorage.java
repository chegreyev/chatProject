package MyExample;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class MessageStorage {
    // Архив наших сообщений
    private LinkedList<String> storage = new LinkedList<>();

    /*
      Добавляем каждое сообщение в некий архив который покажется при
      повторном подключении клинтом к серверу
     */
    public void addMessages(String message){
        if(storage.size() >= 15){
            storage.removeFirst();
            storage.add(message);
        }else{
            storage.add(message);
        }
    }

    /*
        Показываем весь архив сообщений при повтором входе
     */
    public void printStorage(BufferedWriter writer) {

        if(storage.size() > 0){
            try {
                writer.write(" Архив ваших сообщений :" + "\n");

                for (String message : storage) {
                    writer.write(message + "\n");
                }
                writer.write("-----" + "\n" );
                writer.flush(); // Чистит оставший кэш в BufferedWriter

            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
