package executors;
import handlers.ClientHandler;
import view.ClientView;

public class ClientExecute2 {
    public static void main(String[] args) {
        ClientHandler clientHandler = new ClientHandler(5656, "localhost");
        clientHandler.start();
    }
}
