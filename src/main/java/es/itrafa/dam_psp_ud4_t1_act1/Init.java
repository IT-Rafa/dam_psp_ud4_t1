/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package es.itrafa.dam_psp_ud4_t1_act1;

import es.itrafa.dam_psp_ud4_t1_act1.client.TicketsClient;
import es.itrafa.dam_psp_ud4_t1_act1.server.TicketsServer;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author it-ra
 */
public class Init {

    private static final int NUMCLIENTS = 4;

    public int getNumClients() {
        return NUMCLIENTS;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new TicketsServer(NUMCLIENTS).start();
            Thread.sleep(300);

            for (int i = 1; i <= NUMCLIENTS; i++) {
                TicketAsk askTicket = new TicketAsk(
                        "Rafa",
                        LocalDate.now(),
                        TicketType.PENSIONISTAS,
                        i);

                new TicketsClient(askTicket).start();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
