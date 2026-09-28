
import com.toedter.calendar.JDateChooser;
import java.awt.FontFormatException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */


/**
 *
 * @author hezekiahmagalona
 */
public class editshipment extends javax.swing.JPanel {
    
    private java.awt.Font customFont;
    private Connection conn;


    /**
     * Creates new form customer
     */
     public editshipment(String passedOrderIdStr) {
        initComponents();
        initializePanel();

        if (passedOrderIdStr != null && !passedOrderIdStr.trim().isEmpty()) {
            try {
                orderID.setText(passedOrderIdStr.trim());
                orderID.setEditable(true);
                
                int ordId = Integer.parseInt(passedOrderIdStr.trim());
                checkForExistingShipment(ordId);
            } catch (NumberFormatException e) {
                generateShipmentId();
            }
        } else {
            generateShipmentId();
        }
    }
    
     public editshipment() {
        initComponents();
        initializePanel();
        
        orderID.setText("");
        orderID.setEditable(true);
        generateShipmentId();
    }
     
     private void initializePanel() {
        try {
            conn = getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        }
        
        shipmentstatus.setUI(new javax.swing.plaf.basic.BasicComboBoxUI());

        shipmentstatus.setBackground(java.awt.Color.WHITE);
        courier.setBackground(java.awt.Color.WHITE);
        shipmentstatus.setForeground(java.awt.Color.BLACK);
        courier.setForeground(java.awt.Color.BLACK);

        loadFonts();

        if (customFont != null) {
            pID.setFont(customFont.deriveFont(java.awt.Font.BOLD, 28f));
            otid.setFont(customFont.deriveFont(java.awt.Font.BOLD, 28f));
            shipdate.setFont(customFont.deriveFont(java.awt.Font.BOLD, 28f));
            devmode.setFont(customFont.deriveFont(java.awt.Font.BOLD, 28f));
            cour.setFont(customFont.deriveFont(java.awt.Font.BOLD, 28f));
            shipstat.setFont(customFont.deriveFont(java.awt.Font.BOLD, 28f));
        }
    }
     
    private Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://localhost:1433;"
                + "databaseName=ifcdb;"
                + "encrypt=true;"
                + "trustServerCertificate=true";

        String user = "sa";
        String password = "Anolagam28";

        return java.sql.DriverManager.getConnection(url, user, password);
    }
    
    private void generateShipmentId() {
        String sql = "SELECT ISNULL(MAX(shipment_id), 4999) + 1 AS next_id FROM dbo.Shipments";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                shipmentID.setText(String.valueOf(rs.getInt("next_id")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error generating Shipment ID: " + ex.getMessage());
        }
    }
    
    private void checkForExistingShipment(int orderId) {
        String sql = "SELECT shipment_id, shipment_date, delivery_mode, courier, shipment_status " +
                     "FROM dbo.Shipments WHERE order_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, orderId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    shipmentID.setText(String.valueOf(rs.getInt("shipment_id")));
                    shipmentdate.setDate(rs.getDate("shipment_date"));
                    courier.setText(rs.getString("courier"));
                    shipmentstatus.setSelectedItem(rs.getString("shipment_status"));
                    
                    String mode = rs.getString("delivery_mode");
                    if ("Delivery".equalsIgnoreCase(mode)) delivery.setSelected(true);
                    else if ("Pick Up".equalsIgnoreCase(mode)) pickup.setSelected(true);
                    else if ("Meet up".equalsIgnoreCase(mode)) meetup.setSelected(true);
                } else {
                    generateShipmentId();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error scanning shipment catalog: " + ex.getMessage());
        }
    }
    
    private void clearFieldsWithoutPrompt() {
        if (!orderID.isEditable()) {
            shipmentdate.setDate(null);
            deliverymode.clearSelection();
            courier.setText("");
            shipmentstatus.setSelectedIndex(-1);
            try {
                int ordId = Integer.parseInt(orderID.getText().trim());
                checkForExistingShipment(ordId);
            } catch (Exception e) {
                generateShipmentId();
            }
        } else {
            orderID.setText("");
            shipmentdate.setDate(null);
            deliverymode.clearSelection();
            courier.setText("");
            shipmentstatus.setSelectedIndex(-1);
            generateShipmentId();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        deliverymode = new javax.swing.ButtonGroup();
        shipmentdate = new com.toedter.calendar.JDateChooser();
        shipmentstatus = new javax.swing.JComboBox<>();
        courier = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        meetup = new javax.swing.JRadioButton();
        delivery = new javax.swing.JRadioButton();
        pickup = new javax.swing.JRadioButton();
        orderID = new javax.swing.JTextField();
        amount = new javax.swing.JTextField();
        shipmentID = new javax.swing.JTextField();
        clear = new javax.swing.JButton();
        save = new javax.swing.JButton();
        backtotable = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pel4 = new javax.swing.JLabel();
        otid = new javax.swing.JLabel();
        shipdate = new javax.swing.JLabel();
        cour = new javax.swing.JLabel();
        shipstat = new javax.swing.JLabel();
        devmode = new javax.swing.JLabel();
        pID = new javax.swing.JLabel();
        mainbg = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        shipmentdate.setBackground(new java.awt.Color(255, 255, 255));
        shipmentdate.setForeground(new java.awt.Color(0, 0, 0));
        shipmentdate.setFont(new java.awt.Font("Kokonor", 0, 24)); // NOI18N
        shipmentdate.setOpaque(false);
        add(shipmentdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 320, 370, 50));

        shipmentstatus.setBackground(new java.awt.Color(255, 255, 255));
        shipmentstatus.setFont(new java.awt.Font("Kokonor", 0, 24)); // NOI18N
        shipmentstatus.setForeground(new java.awt.Color(0, 0, 0));
        shipmentstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "In Transit", "Delivered", "Cancelled", "Failed" }));
        shipmentstatus.setSelectedIndex(-1);
        shipmentstatus.setPreferredSize(new java.awt.Dimension(133, 65));
        shipmentstatus.addActionListener(this::shipmentstatusActionPerformed);
        add(shipmentstatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 560, 380, 50));

        courier.setBackground(new java.awt.Color(255, 255, 255));
        courier.setFont(new java.awt.Font("Baskerville", 0, 24)); // NOI18N
        courier.setForeground(new java.awt.Color(0, 0, 0));
        courier.setBorder(null);
        courier.addActionListener(this::courierActionPerformed);
        add(courier, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 480, 370, 50));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ifccrafts/assets/tfield.png"))); // NOI18N
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 470, -1, 70));

        deliverymode.add(meetup);
        meetup.setFont(new java.awt.Font("Kokonor", 0, 24)); // NOI18N
        meetup.setForeground(new java.awt.Color(255, 255, 255));
        meetup.setText("Meet up");
        meetup.addActionListener(this::meetupActionPerformed);
        add(meetup, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 390, -1, 60));

        deliverymode.add(delivery);
        delivery.setFont(new java.awt.Font("Kokonor", 0, 24)); // NOI18N
        delivery.setForeground(new java.awt.Color(255, 255, 255));
        delivery.setText("Delivery");
        delivery.addActionListener(this::deliveryActionPerformed);
        add(delivery, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 390, -1, 60));

        deliverymode.add(pickup);
        pickup.setFont(new java.awt.Font("Kokonor", 0, 24)); // NOI18N
        pickup.setForeground(new java.awt.Color(255, 255, 255));
        pickup.setText("Pick Up");
        pickup.addActionListener(this::pickupActionPerformed);
        add(pickup, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 390, -1, 60));

        orderID.setBackground(new java.awt.Color(255, 255, 255));
        orderID.setFont(new java.awt.Font("Baskerville", 0, 24)); // NOI18N
        orderID.setForeground(new java.awt.Color(0, 0, 0));
        orderID.setBorder(null);
        orderID.addActionListener(this::orderIDActionPerformed);
        add(orderID, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 240, 370, 50));

        amount.setBackground(new java.awt.Color(255, 255, 255));
        amount.setFont(new java.awt.Font("Baskerville", 0, 24)); // NOI18N
        amount.setForeground(new java.awt.Color(0, 0, 0));
        amount.setBorder(null);
        add(amount, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 560, 370, 50));

        shipmentID.setBackground(new java.awt.Color(255, 255, 255));
        shipmentID.setFont(new java.awt.Font("Baskerville", 0, 24)); // NOI18N
        shipmentID.setForeground(new java.awt.Color(0, 0, 0));
        shipmentID.setBorder(null);
        add(shipmentID, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 160, 370, 50));

        clear.setBackground(new java.awt.Color(255, 255, 255));
        clear.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        clear.setForeground(new java.awt.Color(0, 0, 0));
        clear.setText("Clear");
        clear.addActionListener(this::clearActionPerformed);
        add(clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 670, 120, 40));

        save.setBackground(new java.awt.Color(255, 255, 255));
        save.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        save.setForeground(new java.awt.Color(0, 0, 0));
        save.setText("Save");
        save.addActionListener(this::saveActionPerformed);
        add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 670, 120, 40));

        backtotable.setBackground(new java.awt.Color(255, 255, 255));
        backtotable.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        backtotable.setForeground(new java.awt.Color(0, 0, 0));
        backtotable.setText("Back to Table");
        backtotable.setToolTipText("");
        backtotable.addActionListener(this::backtotableActionPerformed);
        add(backtotable, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 670, 150, 40));

        delete.setBackground(new java.awt.Color(255, 255, 255));
        delete.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        delete.setForeground(new java.awt.Color(0, 0, 0));
        delete.setText("Delete");
        delete.addActionListener(this::deleteActionPerformed);
        add(delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 670, 120, 40));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ifccrafts/assets/tfield.png"))); // NOI18N
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 310, -1, 70));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ifccrafts/assets/tfield.png"))); // NOI18N
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 230, -1, 70));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ifccrafts/assets/tfield.png"))); // NOI18N
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 550, -1, 70));

        pel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ifccrafts/assets/tfield.png"))); // NOI18N
        add(pel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, -1, 70));

        otid.setFont(new java.awt.Font("Big Caslon", 0, 24)); // NOI18N
        otid.setForeground(new java.awt.Color(255, 255, 255));
        otid.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        otid.setText("Order ID:");
        add(otid, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 240, 180, 50));

        shipdate.setFont(new java.awt.Font("Big Caslon", 0, 24)); // NOI18N
        shipdate.setForeground(new java.awt.Color(255, 255, 255));
        shipdate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        shipdate.setText("Shipment Date:");
        add(shipdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 290, 50));

        cour.setFont(new java.awt.Font("Big Caslon", 0, 24)); // NOI18N
        cour.setForeground(new java.awt.Color(255, 255, 255));
        cour.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cour.setText("Courier:");
        add(cour, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 480, 240, 50));

        shipstat.setFont(new java.awt.Font("Big Caslon", 0, 24)); // NOI18N
        shipstat.setForeground(new java.awt.Color(255, 255, 255));
        shipstat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        shipstat.setText("Shipment Status");
        add(shipstat, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 560, 240, 50));

        devmode.setFont(new java.awt.Font("Big Caslon", 0, 24)); // NOI18N
        devmode.setForeground(new java.awt.Color(255, 255, 255));
        devmode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        devmode.setText("Delivery Mode:");
        add(devmode, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 400, 240, 50));

        pID.setFont(new java.awt.Font("Big Caslon", 0, 24)); // NOI18N
        pID.setForeground(new java.awt.Color(255, 255, 255));
        pID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pID.setText("Shipment ID:");
        add(pID, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, 210, 50));

        mainbg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ifccrafts/assets/mainbg.png"))); // NOI18N
        add(mainbg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1300, -1));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ifccrafts/assets/tfield.png"))); // NOI18N
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 230, -1, 70));
    }// </editor-fold>//GEN-END:initComponents

    private void orderIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_orderIDActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
String idText = shipmentID.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Shipment ID first.");
            return;
        }

        String fetchSql = "SELECT order_id, shipment_date, delivery_mode, courier, shipment_status " +
                          "FROM dbo.Shipments WHERE shipment_id = ?";

        try (PreparedStatement fetchPst = conn.prepareStatement(fetchSql)) {
            fetchPst.setInt(1, Integer.parseInt(idText));
            
            try (ResultSet rs = fetchPst.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Shipment ID not found.");
                    return;
                }

                String preview = "Are you sure you want to delete this shipment?\n\n" +
                                 "Shipment ID: " + idText + "\n" +
                                 "Order ID: " + rs.getInt("order_id") + "\n" +
                                 "Shipment Date: " + rs.getDate("shipment_date") + "\n" +
                                 "Delivery Mode: " + rs.getString("delivery_mode") + "\n" +
                                 "Courier: " + rs.getString("courier") + "\n" +
                                 "Status: " + rs.getString("shipment_status") + "\n";

                int confirm = JOptionPane.showConfirmDialog(this, preview, "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
            }

            String deleteSql = "DELETE FROM dbo.Shipments WHERE shipment_id = ?";
            try (PreparedStatement deletePst = conn.prepareStatement(deleteSql)) {
                deletePst.setInt(1, Integer.parseInt(idText));
                deletePst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Shipment deleted successfully.");
                clearFieldsWithoutPrompt();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_deleteActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        int response =
                JOptionPane.showConfirmDialog(
                        this,
                        "This will clear all your inputs. Continue?",
                        "Confirm Clear",
                        JOptionPane.YES_NO_OPTION
                );

        if (response == JOptionPane.YES_OPTION) {

            orderID.setText("");

            shipmentdate.setDate(null);

            deliverymode.clearSelection();

            courier.setText("");

            shipmentstatus.setSelectedIndex(-1);

            generateShipmentId();

            JOptionPane.showMessageDialog(
                    this,
                    "Fields cleared successfully."
            );
        }
    }//GEN-LAST:event_clearActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        String shipmentIDText = shipmentID.getText().trim();
        String orderIDText = orderID.getText().trim();
        java.util.Date utilDate = shipmentdate.getDate();
        String courierText = courier.getText().trim();
        String shipmentStatusText = (shipmentstatus.getSelectedIndex() == -1)
                ? null : shipmentstatus.getSelectedItem().toString();

        String deliveryModeText = null;
        java.util.Enumeration<javax.swing.AbstractButton> buttons = deliverymode.getElements();
        while (buttons.hasMoreElements()) {
            javax.swing.AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                deliveryModeText = button.getText();
                break;
            }
        }

        if (shipmentIDText.isEmpty() || orderIDText.isEmpty() || utilDate == null 
                || deliveryModeText == null || shipmentStatusText == null || courierText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        try {
            int shipmentId = Integer.parseInt(shipmentIDText);
            int orderId = Integer.parseInt(orderIDText);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            String checkSql = "SELECT COUNT(*) FROM dbo.Shipments WHERE shipment_id = ?";
            boolean exists = false;
            try (PreparedStatement checkPst = conn.prepareStatement(checkSql)) {
                checkPst.setInt(1, shipmentId);
                try (ResultSet checkRs = checkPst.executeQuery()) {
                    if (checkRs.next() && checkRs.getInt(1) > 0) {
                        exists = true;
                    }
                }
            }

            String preview = "Confirm Shipment Details:\n\n" +
                             "Action: " + (exists ? "UPDATE" : "INSERT") + "\n" +
                             "Shipment ID: " + (exists ? shipmentId : "AUTO-GENERATED") + "\n" +
                             "Order ID: " + orderId + "\n" +
                             "Shipment Date: " + sqlDate + "\n" +
                             "Delivery Mode: " + deliveryModeText + "\n" +
                             "Courier: " + courierText + "\n" +
                             "Status: " + shipmentStatusText + "\n";

            int confirm = JOptionPane.showConfirmDialog(this, preview, "Confirm Save", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            String querySql;
            if (exists) {
                querySql = "UPDATE dbo.Shipments SET order_id = ?, shipment_date = ?, "
                         + "delivery_mode = ?, courier = ?, shipment_status = ? WHERE shipment_id = ?";
            } else {
                querySql = "INSERT INTO dbo.Shipments (order_id, shipment_date, delivery_mode, courier, shipment_status) "
                         + "VALUES (?, ?, ?, ?, ?)";
            }

            try (PreparedStatement pst = conn.prepareStatement(querySql)) {
                pst.setInt(1, orderId);
                pst.setDate(2, sqlDate);
                pst.setString(3, deliveryModeText);
                pst.setString(4, courierText);
                pst.setString(5, shipmentStatusText);

                if (exists) {
                    pst.setInt(6, shipmentId); // Only needed for the UPDATE clause
                }

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Shipment saved successfully.");
                clearFieldsWithoutPrompt();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "IDs must be valid whole numbers.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_saveActionPerformed

    private void backtotableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backtotableActionPerformed
        javax.swing.JFrame frame =
        (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

        frame.setContentPane(new shipments());
        frame.revalidate();
        frame.repaint();            
    }//GEN-LAST:event_backtotableActionPerformed

    private void shipmentstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shipmentstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_shipmentstatusActionPerformed

    private void pickupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pickupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pickupActionPerformed

    private void deliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deliveryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deliveryActionPerformed

    private void meetupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meetupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_meetupActionPerformed

    private void courierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_courierActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amount;
    private javax.swing.JButton backtotable;
    private javax.swing.JButton clear;
    private javax.swing.JLabel cour;
    private javax.swing.JTextField courier;
    private javax.swing.JButton delete;
    private javax.swing.JRadioButton delivery;
    private javax.swing.ButtonGroup deliverymode;
    private javax.swing.JLabel devmode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel mainbg;
    private javax.swing.JRadioButton meetup;
    private javax.swing.JTextField orderID;
    private javax.swing.JLabel otid;
    private javax.swing.JLabel pID;
    private javax.swing.JLabel pel4;
    private javax.swing.JRadioButton pickup;
    private javax.swing.JButton save;
    private javax.swing.JLabel shipdate;
    private javax.swing.JTextField shipmentID;
    private com.toedter.calendar.JDateChooser shipmentdate;
    private javax.swing.JComboBox<String> shipmentstatus;
    private javax.swing.JLabel shipstat;
    // End of variables declaration//GEN-END:variables
    
    private void loadFonts() {
        try {
            customFont = java.awt.Font.createFont(
                    java.awt.Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/ifccrafts/assets/MyFont.ttf")
            );
        } catch (FontFormatException | IOException e) {
            customFont = new java.awt.Font("Serif", java.awt.Font.PLAIN, 18);
        }
    }
}
