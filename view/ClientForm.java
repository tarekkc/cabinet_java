package com.yourcompany.clientmanagement.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import com.yourcompany.clientmanagement.controller.ClientController;
import com.yourcompany.clientmanagement.model.Client;

public class ClientForm extends JFrame {
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private boolean isDarkMode = false;
    private ClientController controller;
    private TableRowSorter<DefaultTableModel> sorter;

    public ClientForm() {
        // Set initial theme
        FlatLightLaf.setup();
        controller = new ClientController();

        initializeUI();
        setupTable();
        setupSearch();
        setupButtons();
        loadClientData();
    }

    private void initializeUI() {
        setTitle("Client Management");
        setSize(1200, 800);  // Increased height for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));  // Added gaps between components
    }

    private void setupTable() {
        // Table columns
        String[] columnNames = {
            "ID", "Nom", "Prénom", "Activité", "Année", "Agent", 
            "Forme Juridique", "Régime Fiscal", "Régime CNAS", 
            "Mode Paiement", "Indicateur", "Recette Impôts", 
            "Observation", "Source", "Honoraires/Mois", "Montant", 
            "Téléphone", "Email", "Company", "Adresse", "Type", 
            "Date Création", "Date Modification", "Premier Versement"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table non-editable
            }
        };

        clientTable = new JTable(tableModel);
        customizeTableAppearance();
        setupColumnWidths();
        
        JScrollPane scrollPane = new JScrollPane(clientTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void customizeTableAppearance() {
        Font tableFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 14);

        clientTable.setFillsViewportHeight(true);
        clientTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        clientTable.setFont(tableFont);
        clientTable.setRowHeight(30);  // Slightly reduced row height
        clientTable.getTableHeader().setFont(headerFont);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientTable.setShowGrid(true);
        clientTable.setGridColor(new Color(220, 220, 220));
        clientTable.setIntercellSpacing(new Dimension(0, 1));
        
        // Better selection colors
        clientTable.setSelectionBackground(new Color(52, 152, 219));
        clientTable.setSelectionForeground(Color.WHITE);
    }

    private void setupColumnWidths() {
        TableColumnModel columnModel = clientTable.getColumnModel();
        
        // Set specific widths for key columns
        columnModel.getColumn(0).setPreferredWidth(50);   // ID
        columnModel.getColumn(1).setPreferredWidth(150);  // Nom
        columnModel.getColumn(2).setPreferredWidth(150);  // Prénom
        columnModel.getColumn(3).setPreferredWidth(200);  // Activité
        columnModel.getColumn(4).setPreferredWidth(80);   // Année
        columnModel.getColumn(5).setPreferredWidth(120);  // Agent
        
        // Set default width for other columns
        for (int i = 6; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(120);
        }
    }

    private void setupSearch() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel searchLabel = new JLabel("🔍 Rechercher:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton toggleThemeButton = new JButton("🌓 Toggle Theme");
        toggleThemeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        toggleThemeButton.addActionListener(e -> toggleTheme());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(toggleThemeButton);
        add(searchPanel, BorderLayout.NORTH);

        // Initialize sorter
        sorter = new TableRowSorter<>(tableModel);
        clientTable.setRowSorter(sorter);

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText());
            }
        });
    }

    private void setupButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton addButton = createButton("Ajouter", "icons/add.png", e -> showAddDialog());
        JButton editButton = createButton("Modifier", "icons/edit.png", e -> showEditDialog());
        JButton deleteButton = createButton("Supprimer", "icons/delete.png", e -> deleteClient());
        JButton refreshButton = createButton("Actualiser", "icons/refresh.png", e -> refreshClientTable());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, String iconPath, ActionListener listener) {
        JButton button = new JButton(text, new ImageIcon(iconPath));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        button.addActionListener(listener);
        return button;
    }

    private void loadClientData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                List<Client> clients = controller.fetchAllClients();
                tableModel.setRowCount(0); // Clear table
                
                for (Client c : clients) {
                    tableModel.addRow(convertClientToRow(c));
                }
                return null;
            }
            
            @Override
            protected void done() {
                clientTable.repaint();
            }
        };
        worker.execute();
    }

    private Object[] convertClientToRow(Client c) {
        return new Object[] {
            c.getId(), c.getNom(), c.getPrenom(), c.getActivite(), c.getAnnee(),
            c.getAgentResponsable(), c.getFormeJuridique(), c.getRegimeFiscal(),
            c.getRegimeCnas(), c.getModePaiement(), c.getIndicateur(),
            c.getRecetteImpots(), c.getObservation(), c.getSource(),
            c.getHonorairesMois(), c.getMontant(), c.getPhone(), c.getEmail(),
            c.getCompany(), c.getAddress(), c.getType(), c.getCreatedAt(),
            c.getUpdatedAt(), c.getPremierVersement()
        };
    }

    private void filterTable(String query) {
        if (query.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
            } catch (Exception e) {
                // Invalid regex - ignore
            }
        }
    }

    private void refreshClientTable() {
        loadClientData();
    }

    private void showAddDialog() {
        ClientDialog dialog = new ClientDialog(this, "Ajouter Client", null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Client newClient = dialog.getClient();
            if (controller.addClient(newClient)) {
                refreshClientTable();
                JOptionPane.showMessageDialog(this, "Client ajouté avec succès!");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = clientTable.convertRowIndexToModel(selectedRow);
        int clientId = (Integer) tableModel.getValueAt(modelRow, 0);
        Client clientToEdit = controller.getClientById(clientId);

        if (clientToEdit != null) {
            ClientDialog dialog = new ClientDialog(this, "Modifier Client", clientToEdit);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                Client updatedClient = dialog.getClient();
                if (controller.updateClient(updatedClient)) {
                    refreshClientTable();
                    JOptionPane.showMessageDialog(this, "Client modifié avec succès!");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteClient() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Êtes-vous sûr de vouloir supprimer ce client?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int modelRow = clientTable.convertRowIndexToModel(selectedRow);
            int clientId = (Integer) tableModel.getValueAt(modelRow, 0);
            
            if (controller.deleteClient(clientId)) {
                tableModel.removeRow(modelRow);
                JOptionPane.showMessageDialog(this, "Client supprimé avec succès!");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void toggleTheme() {
        try {
            if (isDarkMode) {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } else {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
            SwingUtilities.updateComponentTreeUI(this);
            isDarkMode = !isDarkMode;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ClientForm().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}