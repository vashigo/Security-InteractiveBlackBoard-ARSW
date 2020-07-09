/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.escuelaing.interactivebalckboardlife.repositories;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * singleton maquina virtual
 * 
 * @author Vashi
 */
public class TicketRepository {
    
    //singleton
    private static TicketRepository _instance = new TicketRepository();
    
    private List<String> listaTickets; // poner y manejar la lista en redis
    
    private int ticketNumber;
    
    private TicketRepository(){
        listaTickets = new CopyOnWriteArrayList<>();
        ticketNumber = 0; //generar un numero random
    }
    
    public static TicketRepository getInstance(){
        
        return _instance;
    }

    public synchronized int getTicketNumber() {
        Integer a = ticketNumber++;
        listaTickets.add(a.toString());
        return a;
    }
    public boolean checkTicket(String ticket){
        boolean isValid = false;
        if(listaTickets.contains(ticket)){
            listaTickets.remove(ticket);
            isValid = true;
        }
        return isValid;
    }

    public List<String> getListaTickets() {
        return listaTickets;
    }
 
    private void eviction(){
        // delete ticket after timout or include this functionality in cheklist
    }
    
}
