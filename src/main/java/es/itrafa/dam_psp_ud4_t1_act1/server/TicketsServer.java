package es.itrafa.dam_psp_ud4_t1_act1.server;

import es.itrafa.dam_psp_ud4_t1_act1.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Amusement Park Ticket Server.
 * <p>
 * Starts a server that receives requests from one client to buy a tickets using
 * TCP.
 * <p>
 * Use a TicketAsk objetct to contain the request data, and a Ticket object to
 * contain the data to return.
 *
 * @author it-ra
 */
public class TicketsServer extends Thread {

    // ATTRIBUTES
    /**
     * Port through which the connection will be established
     */
    final private int PORT = 2000;

    // MAIN METHOD
    /**
     * Wait for cient connection, if it is successful, exchanges data.
     */
    @Override
    public void run() {

        // Server creation indicating port (try-with-resources)
        try (ServerSocket connTCP = new ServerSocket(PORT)) {
            Logger.getLogger(TicketsServer.class.getName()).log(
                    Level.INFO, String.format(
                            "SERVER: Waiting a client request in port %d", PORT));

            // When happens, store the new connnection, by a new port,
            // to exchage of data with client
            Socket connCli = connTCP.accept();

            // A client request is received 
            Logger.getLogger(TicketsServer.class.getName()).log(
                    Level.INFO, String.format(
                            "SERVER: Client request received from ip %s ",
                            connCli.getRemoteSocketAddress().toString()));

            // Manage client request
            manageClientData(connCli);

            // Closing
            Logger.getLogger(TicketsServer.class.getName()).log(
                    Level.INFO, "SERVER: Closing Server");
            // Close client data connection
            connCli.close();
            // connTCP ya cerrado

            // socketExceptions
        } catch (BindException ex) {
            Logger.getLogger(TicketsServer.class.getName()).
                    log(Level.SEVERE, "SERVER: BindException: Probably, the port is in use");
        } catch (ConnectException ex) {
            Logger.getLogger(TicketsServer.class.getName()).
                    log(Level.SEVERE, "SERVER: ConnectException: Probably, no server listening");
        } catch (NoRouteToHostException ex) {
            Logger.getLogger(TicketsServer.class.getName()).
                    log(Level.SEVERE, "SERVER: NoRouteToHostException: Probably, a firewall is intervening or intermediate router is down");
        } catch (PortUnreachableException ex) {
            Logger.getLogger(TicketsServer.class.getName()).
                    log(Level.SEVERE, "SERVER: PortUnreachableException: an ICMP Port Unreachable message has been received on a connected datagram");

        } catch (IOException ex) {
            // Handles both the main() and makeRequest() methods
            Logger.getLogger(TicketsServer.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Controls the exchange of data between client and server from the server
     * side.
     *
     * @param connCli Connection to exchange data with client
     * @throws IOException
     */
    private static void manageClientData(Socket connCli) throws IOException {

        try (ObjectInputStream inputObject
                = new ObjectInputStream(connCli.getInputStream());
                ObjectOutputStream outObject
                = new ObjectOutputStream(connCli.getOutputStream())) {

            // Get data of client request
            TicketAsk dato = (TicketAsk) inputObject.readObject();
            Logger.getLogger(TicketsServer.class.getName()).log(
                    Level.INFO, String.format(
                            "SERVER: Ticket request from client received:\n** %s",
                            dato.toString()));

            // Prepare Ticket
            Ticket ticketToSend = new Ticket(dato);

            // Send Ticket
            outObject.writeObject(ticketToSend);
            Logger.getLogger(TicketsServer.class.getName()).log(
                    Level.INFO, String.format(
                            "SERVER: Ticket created and sent: \n** %s",
                            ticketToSend.toString()));

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TicketsServer.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger.getLogger(TicketsServer.class
                .getName()).
                log(Level.INFO, "INICIO SERVIDOR");
        new TicketsServer().start();
    }
}
