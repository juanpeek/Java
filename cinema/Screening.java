/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

/**
 *
 * @author IES TRASSIERRA
 */
public class Screening {
    private int idRoom;
    private int ticketssold;

    public Screening(int idRoom, int ticketssold) {
        this.idRoom = idRoom;
        this.ticketssold = ticketssold;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public int getTicketssold() {
        return ticketssold;
    }

    public void setTicketssold(int ticketssold) {
        this.ticketssold = ticketssold;
    }

    @Override
    public String toString() {
        return "Screening{" + "idRoom=" + idRoom + ", ticketssold=" + ticketssold + '}';
    }
    
    
}
