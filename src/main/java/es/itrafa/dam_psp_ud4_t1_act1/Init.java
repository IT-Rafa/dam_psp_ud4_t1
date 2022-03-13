package es.itrafa.dam_psp_ud4_t1_act1;

import es.itrafa.dam_psp_ud4_t1_act1.client.TicketsClient;
import es.itrafa.dam_psp_ud4_t1_act1.server.TicketsServer;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para probar que clientes y servidor de tickets funciona correctamente.
 * Logs de ambos programas salen, sin orden.
 * 
 * @author it-ra
 */
public class Init {
//  ATTRIBUTES
    /**
     * Client requests that the server will accept
     */
    private static final int NUMCLIENTS = 4;

    // GETTER & SETTERS
    public int getNumClients() {
        return NUMCLIENTS;
    }

    /**
     * Init servers and clients that will make the communications
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Activamos servidor de Tickets
            // Definimos límite para que todo el proceso finalize (incluido servidor)
            // Para usar sin limite de clientes (funcionamiento normal de servidor) 
            // quitar parámetro NUMCLIENTS
            new TicketsServer(NUMCLIENTS).start();
            
            // Damos margen para que mensaje log del servidor salga antes
            Thread.sleep(300);

            // Preparamos petición 1 cantidad de tickets por cliente
            
            for (int i = 1; i <= NUMCLIENTS; i++) {
                // Creamos objeto con datos para pedir el ticket
                //(indice = cant tickets pedidos; Resto igual)
                TicketAsk askTicket = new TicketAsk(
                        "Rafa",
                        LocalDate.now(),
                        TicketType.PENSIONISTAS,
                        i);

                // Iniciciamos petición cliente
                // Usamos hilo para hacer todas las peticiones y 
                // usar distintos puertos para intercambio datos
                new TicketsClient(askTicket).start();
            }

            //excepciones
        } catch (InterruptedException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
