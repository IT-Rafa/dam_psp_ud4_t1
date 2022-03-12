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
import java.time.LocalDate;
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

    // MAIN METHOD
    /**
     * It tries to connect to the server and, if successful, exchanges data.
     */
    @Override
    public void run() {
        Ticket ticketReceived;
        try {
            // Start communication with Ticket server (try-with-resources)
            try (Socket ServerConn = new Socket(SERVERHOST, SERVERPORT)) {
                Logger.getLogger(TicketsClient.class.getName()).log(
                        Level.INFO, String.format(
                                "CLIENT %s: Start communication with server %s:%d",
                                ServerConn.getLocalSocketAddress(),
                                ServerConn.getInetAddress(), ServerConn.getPort()));

                // Manage data exchange. Sshould produce a Ticket object
                ticketReceived = makeRequest(ServerConn);

                if (ticketReceived != null) {
                    // Ticket recibido
                    Logger.getLogger(TicketsClient.class.getName()).log(
                            Level.INFO, String.format(
                                    "CLIENT %s: Ticket received successfully: \n** %s",
                                    ServerConn.getLocalSocketAddress(),
                                    ServerConn.getLocalSocketAddress(),
                                    ticketReceived.toString()));

                } else {
                    // Failed data exchange
                    Logger.getLogger(TicketsClient.class.getName()).log(
                            Level.SEVERE, String.format(
                                    "CLIENT %s: Ticket not received",
                                    ServerConn.getLocalSocketAddress()
                            ));

                    Logger.getLogger(TicketsClient.class.getName()).log(
                            Level.INFO, String.format(
                                    "CLIENT %s: End communication with server %s:%d",
                                    ServerConn.getLocalSocketAddress(),
                                    ServerConn.getInetAddress(),
                                    ServerConn.getPort()
                            )
                    );
                }// (try-with-resources)

                // socketExceptions
            } catch (BindException ex) {
                                            Logger.getLogger(TicketsClient.class.getName()).log(
                            Level.SEVERE, String.format(
                                    "CLIENT %s: End communication with server %s:%d",
                                    "BindException",
                                    ServerConn.getLocalSocketAddress(),
                                    ": Probably, the port is in use"
                            )
                    );
                
                
                        log(Level.SEVERE, "CLIENT %s: %s");
            } catch (ConnectException ex) {
                Logger.getLogger(TicketsClient.class.getName()).
                        log(Level.SEVERE, "CLIENT %s: ConnectException: Probably, no server listening");
            } catch (NoRouteToHostException ex) {
                Logger.getLogger(TicketsClient.class.getName()).
                        log(Level.SEVERE, "CLIENT %s: NoRouteToHostException: Probably, a firewall is intervening or intermediate router is down");
            } catch (PortUnreachableException ex) {
                Logger.getLogger(TicketsClient.class.getName()).
                        log(Level.SEVERE, "CLIENT %s: PortUnreachableException: an ICMP Port Unreachable message has been received on a connected datagram");

            } catch (IOException ex) {
                // Handles both the main() and makeRequest() methods
                Logger.getLogger(TicketsClient.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

        }
        // OTHER METHODS
        /**
         * Controls the exchange of data between client and server from the
         * client side.
         *
         * @param ServerConn
         * @return ticket with purchase information
         *
         * @throws IOException
         */
    private static Ticket makeRequest(Socket ServerConn) throws IOException {
        // Var to capture Ticket object
        Ticket ticketReceived = null;

        // Creamos petición mediante objeto TicketAsk
        TicketAsk askTicket = new TicketAsk(
                "Rafa",
                LocalDate.now(),
                TicketType.PENSIONISTAS,
                3);

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
                                    "CLIENT: Ticket Request sent to Server :\n** %s ",
                                    askTicket.toString()));

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger.getLogger(TicketsClient.class
                .getName()).
                log(Level.INFO, "INICIO CLIENTES");
        new TicketsClient().start();
    }
}
