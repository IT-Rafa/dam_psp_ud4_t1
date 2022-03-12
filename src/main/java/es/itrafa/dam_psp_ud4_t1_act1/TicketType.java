/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package es.itrafa.dam_psp_ud4_t1_act1;


/**
 * List of ticket types with its prices.
 *
 * @author it-ra
 */
public enum TicketType {
    // Elements enum
    NORMAL("Normal", 10.0),
    MENORES("Niños", 3.0), 
    JOVENES("Carnet Joven", 5.0), 
    PENSIONISTAS("3º Edad", 4.0);
    
    // ATTRIBUTTES
    private final String desc;
    private final double price;
    
    // CONSTRUCTOR
    /**
     * Constructor for an element of type enum TicketType.
     * 
     * @param desc Description of ticket type
     * @param price Price of ticket type
     */
    private TicketType(String desc, double price) {
        this.desc = desc;
        this.price = price;
    }
    
    // GETTER & SETTER
    /**
     * Return the description of ticket type.
     * 
     * @return Description of ticket type
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Return the price of ticket type.
     * 
     * @return Price of ticket type
     */
    public double getPrice() {
        return price;
    }
}
