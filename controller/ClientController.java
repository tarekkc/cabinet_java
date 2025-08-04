package com.yourcompany.clientmanagement.controller;

import com.yourcompany.clientmanagement.dao.ClientDAO;
import com.yourcompany.clientmanagement.model.Client;

import java.util.List;

public class ClientController {
    private ClientDAO clientDAO;

    public ClientController() {
        clientDAO = new ClientDAO();
    }

    // 🔄 1. Fetch all clients
    public List<Client> fetchAllClients() {
        return clientDAO.getAllClients();
    }

    // ➕ 2. Add a client
    public int addClient(Client client) {
        return clientDAO.insertClient(client);
    }

    // ✏️ 3. Update a client
    public boolean updateClient(Client client) {
        return clientDAO.updateClient(client);
    }

    // ❌ 4. Delete a client by ID
    public boolean deleteClient(int clientId) {
        return clientDAO.deleteClientById(clientId);
    }

    // 🔎 5. Optional: Search by name or other field
    public List<Client> searchClients(String keyword) {
        return clientDAO.searchClients(keyword);
    }
     public Client getClientById(int id) {
        return clientDAO.getClientById(id);
    }
}
