/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.escuelaing.interactivebalckboardlife.controllers;

import co.edu.escuelaing.interactivebalckboardlife.repositories.TicketRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DrawingServiceController {
    
    @GetMapping("/status")
    public String status() {
        return "{\"status\":\"Greetings from Spring Boot. " +
                java.time.LocalDate.now() + ", " +
                java.time.LocalTime.now() +
                ". " + "The server is Runnig!\"}";
    }
    
    @GetMapping("/getticket")
    public String getTicket() {
        return "{\"ticket\":\""+
                TicketRepository.getInstance().getTicketNumber() + "\"}";
    }
}