package es.itrafa.dam_psp_ud4_t1_act1;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Contains data to create a Ticket. Designed to send using a socket
 *
 * @author it-ra
 */
public class TicketAsk implements Serializable {

    // ATTRIBUTES
    private String nameUser;
    private LocalDate dateToUse;
    private TicketType type;
    private int cant;

    // CONSTRUCTORS
    /**
     * Class constructor that asks for all the necessary data to create a ticket
     * 
     * @param nameUser
     * @param dateToUse
     * @param type
     * @param cant 
     */
    public TicketAsk(String nameUser, LocalDate dateToUse, TicketType type, int cant) {
        this.nameUser = nameUser;
        this.dateToUse = dateToUse;
        this.type = type;
        this.cant = cant;
    }

    // GETTER & SETTERS
    /**
     * Return the name of the user who makes the purchase
     *
     * @return Name of the user who makes the purchase
     */
    public String getNameUser() {
        return nameUser;
    }
    
    /**
     * Modify the name of the user who makes the purchase
     *
     * @param nameUser Name of the user who makes the purchase
     */
    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }


    /**
     * Return the day for which the tickets will be valid
     *
     * @return The day for which the tickets will be valid
     */
    public LocalDate getDateToUse() {
        return dateToUse;
    }

    /**
     * Modify the day for which the tickets will be valid
     *
     * @param dateToUse The day for which the tickets will be valid
     */
    public void setDateToUse(LocalDate dateToUse) {
        this.dateToUse = dateToUse;
    }

    /**
     * Return cant of chosen type of tickets to buy
     *
     * @return Cant of chosen type of tickets to buy
     */
    public int getCant() {
        return cant;
    }

    /**
     * Modify cant of chosen type of tickets to buy
     *
     * @param cant cant of chosen type of tickets to buy
     */
    public void setCant(int cant) {
        this.cant = cant;
    }

    /**
     * Return type of ticket to buy
     *
     * @return Type of ticket to buy
     */
    public TicketType getType() {
        return type;
    }

    /**
     * Modify type of ticket to buy
     * 
     * @param type
     */
    public void setType(TicketType type) {
        this.type = type;
    }
    /**
     * Show TicketAsk as String
     */
    @Override
    public String toString() {
        final DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                        new Locale("es", "ES"));

        String msg = String.format(
                "Petición de ticket por usuario %s de %d entradas para %s para el día %s",
                this.nameUser,
                this.cant,
                this.type.getDesc(),
                this.dateToUse.format(formatter));

        return msg;
    }

}
