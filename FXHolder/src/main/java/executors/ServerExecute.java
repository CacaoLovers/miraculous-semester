package executors;

import handlers.ServerHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerExecute {

    private final static int MAX_PLAYERS = 2;

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_PLAYERS);
        ServerHandler server = ServerHandler.create(5656, executor);

        while (true){
            executor.execute(server);
            if (executor.getActiveCount() > MAX_PLAYERS){
                executor.wait();
            }
        }

    }
}
