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

    /**
     * limited mode only accept a limited cant of clients (cantClients)
     *
     */
    private final boolean serverLimited;

    /**
     * Cant of clients connections allowed in limited mode
     *
     */
    private int cantClients;

    /**
     * Start server, wait for cient connection and process data exchange
     */
    public TicketsServer() {
        this.serverLimited = false;
        this.cantClients = 0;
    }

    public TicketsServer(int cantClients) {
        this.serverLimited = true;
        this.cantClients = cantClients;
    }

    public void run() {

        // Server creation indicating port (try-with-resources)
        ServerSocket connTCP;
        try {
            connTCP = new ServerSocket(PORT);
            String clientId;
            Logger.getLogger(TicketsServer.class.getName()).log(
                    Level.INFO, String.format(
                            "SERVER: Waiting a client request in port %d", PORT));

            if (serverLimited) {
                Logger.getLogger(TicketsServer.class.getName()).log(
                        Level.WARNING, String.format(
                                "SERVER: Only accept %d clients (limited mode)", cantClients));
            }
            // When happens, store the new connnection, by a new port,
            // to exchage of data with client

            while (!serverLimited || (cantClients-- > 0)) {
                Socket connCli = connTCP.accept();
                clientId = connCli.getRemoteSocketAddress().toString();
                // A client request is received 
                Logger.getLogger(TicketsServer.class.getName()).log(
                        Level.INFO, String.format(
                                "SERVER: Client %s request received", clientId));

                // Manage client request
                new ManageDataClient(connCli, clientId).start();

            }

            // Closing
            Thread.sleep(300);
            Logger.getLogger(TicketsServer.class.getName()).log(
                    Level.INFO, "SERVER: Closing Server (limited mode)");
            connTCP.close();
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
        } catch (InterruptedException ex) {
            Logger.getLogger(TicketsServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Controls the exchange of data between client and server from the server
     * side.
     *
     * @param connCli Connection to exchange data with client
     * @throws IOException
     */
    private static void manageClientData(Socket connCli, String clientId) throws IOException {

        try (ObjectInputStream inputObject
                = new ObjectInputStream(connCli.getInputStream());
                ObjectOutputStream outObject
                = new ObjectOutputStream(connCli.getOutputStream())) {

            // Get data of client request
            TicketAsk dato = (TicketAsk) inputObject.readObject();
            Logger.getLogger(TicketsServer.class.getName()).log(
                    Level.INFO, String.format(
                            "SERVER: Ticket request from client %s is ok:\n** %s",
                            clientId,
                            dato.toString()));

            // Prepare Ticket
            Ticket ticketToSend = new Ticket(dato);

            // Send Ticket
            outObject.writeObject(ticketToSend);
            Logger.getLogger(TicketsServer.class.getName()).log(
                    Level.INFO, String.format(
                            "SERVER: Ticket to client %s created and sent: \n** %s",
                            clientId,
                            ticketToSend.toString()));

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TicketsServer.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

}
