package MyExample;

public class Client2 {
    public static String ipAddress = "localhost";
    public static int port = 8080;

    public static void main(String[] args) {
        new ClientMachine(ipAddress , port);
    }
}
