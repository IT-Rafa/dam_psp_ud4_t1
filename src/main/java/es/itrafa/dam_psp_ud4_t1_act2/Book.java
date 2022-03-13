/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud4_t1_act2;

/**
 *
 * @author Juan Morillo Fernandez
 */
import java.io.Serializable;

public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String isbn;
    private double cost;

    public Book(String isbn) {
        this.isbn = isbn;
    }

    public Book(String title, String isbn, double cost) {
        this.title = title;
        this.isbn = isbn;
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public double getCost() {
        return cost;
    }

    public String toString() {
        return "> " + this.title + " ($" + this.cost + ")";
    }

}