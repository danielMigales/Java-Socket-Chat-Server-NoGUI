package socketchatserver;

/**
 *
 * @author daniel migales puertas
 *
 *
 */
import dataPaquete.DataPaquete;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {

    private final int SERVER_PORT = 40000;

    @Override
    public void run() {

        System.out.println("Hilo del servidor iniciado");

        try {
            //crear el servidor
            var server = new ServerSocket(SERVER_PORT);

            //bucle 
            while (true) {

                //crear el flujo de entrada asociado al socket
                try ( //crear el socket para aceptar la comunicacion
                        Socket socket = server.accept(); //crear el flujo de entrada asociado al socket
                        java.io.ObjectInputStream inputFlow = new ObjectInputStream(socket.getInputStream())) {
                    //extraer el mensaje
                    DataPaquete inputData = (DataPaquete) inputFlow.readObject();
                    var userName = inputData.getNombreUsuario();
                    var ipAddress = inputData.getDireccionIP();
                    var message = inputData.getMensaje();
                    // visualizar el mensaje en el area del mensaje en la interfaz
                    var concatenatedMessage = userName + "/" + ipAddress + " dice: \t" + message + "\n";
                    System.out.println(concatenatedMessage);

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No se puede crear el socket en el servidor");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No se encuentra la clase DataPaquete");
        }

    }
}
