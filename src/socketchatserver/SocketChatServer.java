package socketchatserver;

/**
 *
 * @author daniel migales puertas
 *
 */
public class SocketChatServer {

    public static void main(String[] args) {

        var server = new Thread(new Server());
        server.start();

    }

}
