/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.itrafa.dam_psp_ud4_t1_act1.server;

import es.itrafa.dam_psp_ud4_t1_act1.Ticket;
import es.itrafa.dam_psp_ud4_t1_act1.TicketAsk;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author it-ra
 */
public class ManageDataClient extends Thread {

    private final Socket connCli;
    private final String clientId;

    public ManageDataClient(Socket connCli, String clientId) {
        this.connCli = connCli;
        this.clientId = clientId;
    }

    @Override
    public void run() {

        // try-with-resources (close streams and socket)
        try (ObjectInputStream inputObject
                = new ObjectInputStream(connCli.getInputStream());
                ObjectOutputStream outObject
                = new ObjectOutputStream(connCli.getOutputStream()); connCli) {

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

            // end try-with-resources (close streams and socket)
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TicketsServer.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManageDataClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
