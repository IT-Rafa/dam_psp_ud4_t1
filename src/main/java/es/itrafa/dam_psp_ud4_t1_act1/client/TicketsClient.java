package es.itrafa.dam_psp_ud4_t1_act1.client;

import es.itrafa.dam_psp_ud4_t1_act1.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Amusement Park Ticket Client.
 * <p>
 * Establishes communication with a Ticket Server for an amusement park, sends
 * the ticket request and receives the ticket.
 * <p>
 * Extends Thread to avoid BindException when test
 *
 * @author it-ra
 */
public class TicketsClient extends Thread {

    // ATTRIBUTES
    /**
     * Listening Port through which the connection will be established
     */
    final private int SERVERPORT = 2000;
    /**
     * Server IP
     */
    final private String SERVERHOST = "localhost";
    /**
     * petición de ticket que hará el cliente
     */
    final private TicketAsk askTicket;

    // CONSTRUCTORS
    public TicketsClient(TicketAsk askTicket) {
        this.askTicket = askTicket;
    }

    //  METHODS
    /**
     * It tries to connect to the server and, if successful, exchanges data.
     */
    @Override
    public void run() {
        Socket ssocket;
        String clientId = "None";
        Ticket ticketReceived;
        try {
            // Start communication with Ticket server
            ssocket = new Socket(SERVERHOST, SERVERPORT);
            clientId = ssocket.getLocalSocketAddress().toString();
            Logger.getLogger(TicketsClient.class.getName()).log(
                    Level.INFO, String.format(
                            "CLIENT %s: Start communication with server %s:%d",
                            clientId, ssocket.getInetAddress(), ssocket.getPort()
                    )
            );

            // Manage data exchange. Sshould produce a Ticket object
            ticketReceived = makeRequest(ssocket, askTicket, clientId);

            if (ticketReceived != null) {
                // Ticket recibido
                Logger.getLogger(TicketsClient.class.getName()).log(
                        Level.INFO, String.format(
                                "CLIENT %s: Ticket received successfully: \n** %s",
                                clientId, ticketReceived.toString()
                        )
                );
                ssocket.close();
            } else {
                // Failed data exchange
                Logger.getLogger(TicketsClient.class.getName()).log(
                        Level.SEVERE, String.format(
                                "CLIENT %s: Ticket not received", clientId
                        ));

            }
            Logger.getLogger(TicketsClient.class.getName()).log(
                    Level.INFO, String.format(
                            "CLIENT %s: End communication with server %s:%d",
                            clientId,
                            ssocket.getInetAddress(),
                            ssocket.getPort()
                    )
            );

            // socketExceptions
        } catch (BindException ex) {
            Logger.getLogger(TicketsClient.class.getName()).log(
                    Level.SEVERE, String.format(
                            "CLIENT %s: %s: %s",
                            clientId,
                            "BindException",
                            "Probably, the port is in use"
                    )
            );

        } catch (ConnectException ex) {
            Logger.getLogger(TicketsClient.class.getName()).
                    log(Level.SEVERE, "CLIENT %s: ConnectException: Probably, no server listening");

            Logger.getLogger(TicketsClient.class.getName()).log(
                    Level.SEVERE, String.format(
                            "CLIENT %s: %s: %s",
                            clientId,
                            "ConnectException",
                            "Probably, no server listening"
                    )
            );
        } catch (NoRouteToHostException ex) {
            Logger.getLogger(TicketsClient.class.getName()).log(
                    Level.SEVERE, String.format(
                            "CLIENT %s: %s: %s",
                            clientId,
                            "NoRouteToHostException",
                            "Probably, a firewall is intervening or intermediate router is down"
                    )
            );

        } catch (PortUnreachableException ex) {
            Logger.getLogger(TicketsClient.class.getName()).log(
                    Level.SEVERE, String.format(
                            "CLIENT %s: %s: %s",
                            clientId,
                            "PortUnreachableException",
                            "an ICMP Port Unreachable message has been received on a connected datagram"
                    )
            );

        } catch (IOException ex) {
            // Handles both the main() and makeRequest() methods
            Logger.getLogger(TicketsClient.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

    }
    // OTHER METHODS

    /**
     * Controls the exchange of data between client and server from the client
     * side.
     *
     * @param ServerConn
     * @return ticket with purchase information
     *
     * @throws IOException
     */
    private static Ticket makeRequest(Socket ServerConn, TicketAsk askTicket, String clientId) throws IOException {
        // Var to capture Ticket object
        Ticket ticketReceived = null;

        // Prepare input and output streams (try-with resources)
        try (
                ObjectOutputStream outObject
                = new ObjectOutputStream(ServerConn.getOutputStream());
                ObjectInputStream inputObject
                = new ObjectInputStream(ServerConn.getInputStream());) {

            // Send request to Ticket Server
            outObject.writeObject(askTicket);
            Logger.getLogger(TicketsClient.class
                    .getName()).log(
                            Level.INFO, String.format(
                                    "CLIENT %s: Ticket Request sent to Server :\n** %s ",
                                    clientId, askTicket.toString()));

            // Get Ticket from Ticket Server
            ticketReceived = (Ticket) inputObject.readObject();
            // streams closed

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TicketsClient.class
                    .getName()).
                    log(Level.SEVERE, null, ex);
        }
        return ticketReceived;
    }

}
