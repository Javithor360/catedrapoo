package com.tickets.util;

import com.tickets.model.TicketManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutor {

    public static void checkTicketDate() {
        // Creamos un ScheduledExecutorService con un solo hilo
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        // Ejecutar el método una vez después de un retraso inicial de 1 segundo
        executor.scheduleAtFixedRate(TicketManager::checkTickets, 0, 10, TimeUnit.SECONDS);
    }
}
