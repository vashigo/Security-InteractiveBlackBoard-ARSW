/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.escuelaing.interactivebalckboardlife.endpoints;

import co.edu.escuelaing.interactivebalckboardlife.repositories.TicketRepository;
import java.io.IOException;
import java.util.logging.Level;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/bbService")
public class BBEndpoint {

    private static final Logger logger = Logger.getLogger(BBEndpoint.class.getName());
    /* Queue for all open WebSocket sessions */
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();

    Session ownSession = null;
    
    String ownTicket = null;

    private boolean accepted = false;

    TicketRepository ticketRepo = TicketRepository.getInstance();

    /* Call this method to send a message to all clients */
    public void send(String msg) {
        try {
            /* Send updates to all open WebSocket sessions */
            for (Session session : queue) {
                if (!session.equals(this.ownSession)) {
                    session.getBasicRemote().sendText(msg);
                }
                logger.log(Level.INFO, "Sent: {0}", msg);
            }
        } catch (IOException e) {
            logger.log(Level.INFO, e.toString());
        }
    }

    @OnMessage
    public void processPoint(String message, Session session) {
        if (accepted) {
            logger.log(Level.INFO, "Point received:" + message + ". From session: " + session);
            this.send(message);
        }else{
            if (!accepted && ticketRepo.checkTicket(message)) {
                this.accepted = true;
                this.ownTicket = message; //almaceno el ticket
            }else{
                //Cerrar la conexion del socket
                this.closedConnection(session);
            }
        }
    }

    @OnOpen
    public void openConnection(Session session) {
        /* Register this connection in the queue */
        queue.add(session);
        ownSession = session;
        logger.log(Level.INFO, "Connection opened.");
        try {
            session.getBasicRemote().sendText("Connection established.");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @OnClose
    public void closedConnection(Session session) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, "Connection closed for session " + session + "with ticket: "+ this.ownTicket);
    }

    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, t.toString());
        logger.log(Level.INFO, "Connection error.");
    }
}
