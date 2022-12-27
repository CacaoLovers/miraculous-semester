package executors;
import handlers.ClientHandler;
import view.ClientView;

public class ClientExecute{
    public static void main(String[] args) {
        ClientHandler clientHandler = new ClientHandler(5656, "localhost");
        clientHandler.start();
    }
}
