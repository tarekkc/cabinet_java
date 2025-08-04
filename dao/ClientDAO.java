package com.yourcompany.clientmanagement.dao;

import com.yourcompany.clientmanagement.model.Client;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    // Get all clients
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY nom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching clients: " + e.getMessage());
            e.printStackTrace();
        }

        return clients;
    }

    // Insert new client and return the generated ID
    public int insertClient(Client client) {
        String sql = "INSERT INTO clients (nom, prenom, activite, annee, agent_responsable, "
                + "forme_juridique, regime_fiscal, regime_cnas, mode_paiement, indicateur, "
                + "recette_impots, observation, source, honoraires_mois, montant, phone, "
                + "email, company, address, type, premier_versement) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set all parameters
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getActivite());
            stmt.setString(4, client.getAnnee());
            stmt.setString(5, client.getAgentResponsable());
            stmt.setString(6, client.getFormeJuridique());
            stmt.setString(7, client.getRegimeFiscal());
            stmt.setString(8, client.getRegimeCnas());
            stmt.setString(9, client.getModePaiement());
            stmt.setString(10, client.getIndicateur());
            stmt.setString(11, client.getRecetteImpots());
            stmt.setString(12, client.getObservation());
            if (client.getSource() != null) {
                stmt.setInt(13, client.getSource());
            } else {
                stmt.setNull(13, Types.INTEGER);
            }
            stmt.setString(14, client.getHonorairesMois());
            if (client.getMontant() != null) {
                stmt.setBigDecimal(15, BigDecimal.valueOf(client.getMontant()));
            } else {
                stmt.setNull(15, Types.DECIMAL);
            }
            stmt.setString(16, client.getPhone());
            stmt.setString(17, client.getEmail());
            stmt.setString(18, client.getCompany());
            stmt.setString(19, client.getAddress());
            stmt.setString(20, client.getType());
            stmt.setString(21, client.getPremierVersement());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating client failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated ID
                } else {
                    throw new SQLException("Creating client failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserting client: " + e.getMessage());
            e.printStackTrace();
            return -1; // Return -1 to indicate failure
        }
    }

    // Update client
    public boolean updateClient(Client client) {
        String sql = "UPDATE clients SET nom=?, prenom=?, activite=?, annee=?, agent_responsable=?, "
                + "forme_juridique=?, regime_fiscal=?, regime_cnas=?, mode_paiement=?, indicateur=?, "
                + "recette_impots=?, observation=?, source=?, honoraires_mois=?, montant=?, phone=?, "
                + "email=?, company=?, address=?, type=?, premier_versement=?, updated_at=CURRENT_TIMESTAMP "
                + "WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getActivite());
            stmt.setString(4, client.getAnnee());
            stmt.setString(5, client.getAgentResponsable());
            stmt.setString(6, client.getFormeJuridique());
            stmt.setString(7, client.getRegimeFiscal());
            stmt.setString(8, client.getRegimeCnas());
            stmt.setString(9, client.getModePaiement());
            stmt.setString(10, client.getIndicateur());
            stmt.setString(11, client.getRecetteImpots());
            stmt.setString(12, client.getObservation());
            if (client.getSource() != null) {
                stmt.setInt(13, client.getSource());
            } else {
                stmt.setNull(13, Types.INTEGER);
            }
            stmt.setString(14, client.getHonorairesMois());
            if (client.getMontant() != null) {
                stmt.setBigDecimal(15, BigDecimal.valueOf(client.getMontant()));
            } else {
                stmt.setNull(15, Types.DECIMAL);
            }
            stmt.setString(16, client.getPhone());
            stmt.setString(17, client.getEmail());
            stmt.setString(18, client.getCompany());
            stmt.setString(19, client.getAddress());
            stmt.setString(20, client.getType());
            stmt.setString(21, client.getPremierVersement());
            stmt.setInt(22, client.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating client: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete client
    public boolean deleteClientById(int id) {
        String sql = "DELETE FROM clients WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting client: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get client by ID
    public Client getClientById(int id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching client by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    // Search clients
    public List<Client> searchClients(String keyword) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE nom LIKE ? OR activite LIKE ? OR company LIKE ? ORDER BY nom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword + "%";
            stmt.setString(1, likeKeyword);
            stmt.setString(2, likeKeyword);
            stmt.setString(3, likeKeyword);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapResultSetToClient(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error searching clients: " + e.getMessage());
            e.printStackTrace();
        }

        return clients;
    }

    // Map ResultSet to Client object
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setActivite(rs.getString("activite"));
        client.setAnnee(rs.getString("annee"));
        client.setAgentResponsable(rs.getString("agent_responsable"));
        client.setFormeJuridique(rs.getString("forme_juridique"));
        client.setRegimeFiscal(rs.getString("regime_fiscal"));
        client.setRegimeCnas(rs.getString("regime_cnas"));
        client.setModePaiement(rs.getString("mode_paiement"));
        client.setIndicateur(rs.getString("indicateur"));
        client.setRecetteImpots(rs.getString("recette_impots"));
        client.setObservation(rs.getString("observation"));
        
        // Handle source field properly
        int sourceValue = rs.getInt("source");
        if (!rs.wasNull()) {
            client.setSource(sourceValue);
        } else {
            client.setSource(null);
        }
        
        client.setHonorairesMois(rs.getString("honoraires_mois"));
        
        // Handle montant field properly
        BigDecimal montantBD = rs.getBigDecimal("montant");
        if (montantBD != null) {
            client.setMontant(montantBD.doubleValue());
        } else {
            client.setMontant(null);
        }
        
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        client.setCompany(rs.getString("company"));
        client.setAddress(rs.getString("address"));
        client.setType(rs.getString("type"));
        client.setPremierVersement(rs.getString("premier_versement"));
        
        // Handle timestamp fields
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            client.setCreatedAt(createdAt.toString());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            client.setUpdatedAt(updatedAt.toString());
        }
        
        return client;
    }
}