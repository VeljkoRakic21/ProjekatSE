package com.example.cs230;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MainServer {
    public static void main(String[] args) {
        try {
            // Kreiramo nekoliko skladišnih servera
            StorageServer server1 = new StorageServer("server1");
            StorageServer server2 = new StorageServer("server2");
            StorageServer server3 = new StorageServer("server3");

            // Kreiramo bazu podataka
            Database database = new Database();

            // Kreiramo orkestrator i povezujemo ga sa skladišnim serverima
            Orchestrator orchestrator = new Orchestrator(List.of(server1, server2, server3), database);

            // Ispisujemo status servera
            System.out.println("Storage servers and orchestrator are up and running.");
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }
}

