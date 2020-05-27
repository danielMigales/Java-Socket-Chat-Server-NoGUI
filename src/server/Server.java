package server;

/**
 *
 * @author daniel migales puertas
 *
 */
import dataPaquete.DataPaquete;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {

    private final int SERVER_PORT = 40000;
    private final int CLIENT_PORT = 50000;

    String userName;
    String ipAddress;
    String message;
    String destinatario;

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
                    userName = inputData.getNombreUsuario();
                    ipAddress = inputData.getDireccionIP();
                    message = inputData.getMensaje();
                    destinatario = inputData.getDestinatarioIP();

                    // visualizar el mensaje en el area del mensaje en la interfaz
                    System.out.println("Mensaje encriptado recibido:");
                    var concatenatedMessage = userName + "/" + ipAddress + " dice: \t" + message + " para: " + destinatario + "\n";
                    System.out.println(concatenatedMessage);

                    //reenviar de nuevo el mensaje
                    enviarAlDestinatario();

                    //desencriptado del mensaje recibido
                    Cryptography cryptoMessage = new Cryptography();
                    try {
                        String decryptedMessage = cryptoMessage.decrypt(message);
                        System.out.println("Mensaje Desencriptado: " + decryptedMessage + "\n");
                    } catch (Exception ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

    public void enviarAlDestinatario() {

        //se envia encriptado
        DataPaquete outputData = new DataPaquete(userName, ipAddress, message);
        //System.out.println(mensaje);

        try {

            //flujo de informacion y se asocia al socket
            try ( //crear el socket (conector con el servidor)
                    Socket socket = new Socket(destinatario, CLIENT_PORT); //flujo de informacion y se asocia al socket
                    java.io.ObjectOutputStream outFlow = new ObjectOutputStream(socket.getOutputStream())) {
                //enviar al dato
                outFlow.writeObject(outputData);
            }

        } catch (IOException ex) {

            System.out.println("No se pudo crear el socket para conectar con  la direccion " + destinatario);
        }

    }
}
