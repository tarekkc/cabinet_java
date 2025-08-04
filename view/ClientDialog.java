package com.yourcompany.clientmanagement.view;

import com.yourcompany.clientmanagement.model.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClientDialog extends JDialog {
    private JTextField nomField, prenomField, activiteField, anneeField, agentField;
    private JTextField formeJuridiqueField, regimeFiscalField, regimeCnasField;
    private JTextField modePaiementField, indicateurField, recetteImpotsField;
    private JTextField observationField, sourceField, honorairesMoisField;
    private JTextField montantField, phoneField, emailField, companyField;
    private JTextField addressField, typeField, premierVersementField;
    private boolean confirmed = false;
    private Client client;

    public ClientDialog(JFrame parent, String title, Client client) {
        super(parent, title, true);
        this.client = client;
        initializeUI();
        populateFields();
    }

    private void initializeUI() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        // Main form panel with scroll
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Personal Information
        addFormField(formPanel, "Nom*:", nomField = new JTextField());
        addFormField(formPanel, "Prénom:", prenomField = new JTextField());
        addFormField(formPanel, "Téléphone:", phoneField = new JTextField());
        addFormField(formPanel, "Email:", emailField = new JTextField());
        
        // Professional Information
        addFormField(formPanel, "Activité*:", activiteField = new JTextField());
        addFormField(formPanel, "Année:", anneeField = new JTextField());
        addFormField(formPanel, "Agent Responsable:", agentField = new JTextField());
        addFormField(formPanel, "Forme Juridique:", formeJuridiqueField = new JTextField());
        
        // Financial Information
        addFormField(formPanel, "Régime Fiscal:", regimeFiscalField = new JTextField());
        addFormField(formPanel, "Régime CNAS:", regimeCnasField = new JTextField());
        addFormField(formPanel, "Mode Paiement:", modePaiementField = new JTextField());
        addFormField(formPanel, "Indicateur:", indicateurField = new JTextField());
        
        // Additional Information
        addFormField(formPanel, "Recette Impôts:", recetteImpotsField = new JTextField());
        addFormField(formPanel, "Observation:", observationField = new JTextField());
        addFormField(formPanel, "Source:", sourceField = new JTextField());
        addFormField(formPanel, "Honoraires/Mois:", honorairesMoisField = new JTextField());
        
        // Company Information
        addFormField(formPanel, "Montant:", montantField = new JTextField());
        addNumericValidation(montantField);
        addFormField(formPanel, "Company:", companyField = new JTextField());
        addFormField(formPanel, "Adresse:", addressField = new JTextField());
        addFormField(formPanel, "Type:", typeField = new JTextField());
        addFormField(formPanel, "Premier Versement:", premierVersementField = new JTextField());

        JScrollPane scrollPane = new JScrollPane(formPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton okButton = new JButton("Enregistrer");
        okButton.addActionListener(e -> validateAndClose());
        okButton.setPreferredSize(new Dimension(120, 30));

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dispose());
        cancelButton.setPreferredSize(new Dimension(120, 30));

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set default button for Enter key
        getRootPane().setDefaultButton(okButton);
    }

    private void addFormField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    private void addNumericValidation(JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == '.')) {
                    e.consume();
                }
            }
        });
    }

    private void populateFields() {
        if (client == null) return;

        nomField.setText(client.getNom());
        prenomField.setText(client.getPrenom());
        activiteField.setText(client.getActivite());
        anneeField.setText(client.getAnnee());
        agentField.setText(client.getAgentResponsable());
        formeJuridiqueField.setText(client.getFormeJuridique());
        regimeFiscalField.setText(client.getRegimeFiscal());
        regimeCnasField.setText(client.getRegimeCnas());
        modePaiementField.setText(client.getModePaiement());
        indicateurField.setText(client.getIndicateur());
        recetteImpotsField.setText(client.getRecetteImpots());
        observationField.setText(client.getObservation());
        sourceField.setText(client.getSource() != null ? client.getSource().toString() : "");
        honorairesMoisField.setText(client.getHonorairesMois());
        montantField.setText(client.getMontant() != null ? String.valueOf(client.getMontant()) : "");
        phoneField.setText(client.getPhone());
        emailField.setText(client.getEmail());
        companyField.setText(client.getCompany());
        addressField.setText(client.getAddress());
        typeField.setText(client.getType());
        premierVersementField.setText(client.getPremierVersement());
    }

    private void validateAndClose() {
        if (nomField.getText().trim().isEmpty() || activiteField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Les champs 'Nom' et 'Activité' sont obligatoires", 
                "Validation", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Client getClient() {
        if (!confirmed) return null;

        Client c = client != null ? client : new Client();
        c.setNom(nomField.getText());
        c.setPrenom(prenomField.getText());
        c.setActivite(activiteField.getText());
        c.setAnnee(anneeField.getText());
        c.setAgentResponsable(agentField.getText());
        c.setFormeJuridique(formeJuridiqueField.getText());
        c.setRegimeFiscal(regimeFiscalField.getText());
        c.setRegimeCnas(regimeCnasField.getText());
        c.setModePaiement(modePaiementField.getText());
        c.setIndicateur(indicateurField.getText());
        c.setRecetteImpots(recetteImpotsField.getText());
        c.setObservation(observationField.getText());
        
        try {
            c.setSource(sourceField.getText().isEmpty() ? null : Integer.parseInt(sourceField.getText()));
        } catch (NumberFormatException e) {
            c.setSource(null);
        }
        
        c.setHonorairesMois(honorairesMoisField.getText());
        
        try {
            c.setMontant(montantField.getText().isEmpty() ? null : Double.parseDouble(montantField.getText()));
        } catch (NumberFormatException e) {
            c.setMontant(null);
        }
        
        c.setPhone(phoneField.getText());
        c.setEmail(emailField.getText());
        c.setCompany(companyField.getText());
        c.setAddress(addressField.getText());
        c.setType(typeField.getText());
        c.setPremierVersement(premierVersementField.getText());
        
        // Set current date for new clients
        if (client == null) {
            String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            c.setCreatedAt(currentDate);
            c.setUpdatedAt(currentDate);
        } else {
            c.setUpdatedAt(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }

        return c;
    }
}