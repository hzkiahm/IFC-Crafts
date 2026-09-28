import javax.swing.JOptionPane;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author hezekiahmagalona
 */
public class shipments extends javax.swing.JPanel {
    private Connection conn;
    private String[] originalIds;
    private Object[][] originalData;
    
    /**
     * Creates new form customer
     */
    public shipments() {
    initComponents();

    try {
        conn = getConnection();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Database connection failed: " + e.getMessage());
        return;
    }

    loadShipmentTable();
    customizeShipmentTable();
    centerAlignShipmentTable();
}
    
    
    private void centerAlignShipmentTable() {
        javax.swing.table.DefaultTableCellRenderer centerRenderer =
                new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        for (int i = 0; i < shipmenttable.getColumnCount(); i++) {
            shipmenttable.getColumnModel().getColumn(i)
                    .setCellRenderer(centerRenderer);
        }
    }
    
    private Connection getConnection() throws Exception {
        // Make sure "true" is lowercase and has no extra spaces

        String url = "jdbc:sqlserver://localhost:1433;databaseName=ifcdb;trustServerCertificate=true;";

        String user = "sa";
        String password = "Anolagam28";

        return java.sql.DriverManager.getConnection(url, user, password);
    }
    
    public class DateCellEditor
            extends AbstractCellEditor
            implements TableCellEditor {

        private JDateChooser dateChooser;

        public DateCellEditor() {

            dateChooser = new JDateChooser();
            dateChooser.setDateFormatString(
                    "yyyy-MM-dd");
        }
        @Override
        public Object getCellEditorValue() {

            return dateChooser.getDate();
        }
        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {
            if (value instanceof java.util.Date) {

                dateChooser.setDate(
                        (java.util.Date) value);
            } else {
                dateChooser.setDate(null);
            }
            return dateChooser;
        }
    }
    
    private void loadShipmentTable() {
try {

            String sql =
                    "SELECT shipment_id, order_id, "
                    + "shipment_date, delivery_mode, "
                    + "courier, shipment_status "
                    + "FROM dbo.Shipments";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    pst.executeQuery();

            DefaultTableModel model =
                    new DefaultTableModel() {

                @Override
                public boolean isCellEditable(
                        int row,
                        int column) {

                    return column != 0 && column != 1;
                }
            };

            model.setColumnIdentifiers(new String[]{
                "Shipment ID",
                "Order ID",
                "Shipment Date",
                "Delivery Mode",
                "Courier",
                "Status"
            });

            java.util.List<String> idList =
                    new java.util.ArrayList<>();

            java.util.List<Object[]> dataList =
                    new java.util.ArrayList<>();

            while (rs.next()) {

                String shipmentId =
                        rs.getString("shipment_id");

                Object[] row = new Object[]{
                    shipmentId,
                    rs.getInt("order_id"),
                    rs.getDate("shipment_date"),
                    rs.getString("delivery_mode"),
                    rs.getString("courier"),
                    rs.getString("shipment_status")
                };

                model.addRow(row);

                idList.add(shipmentId);
                dataList.add(row);
            }

            originalIds =
                    idList.toArray(new String[0]);

            originalData =
                    dataList.toArray(new Object[0][]);

            shipmenttable.setModel(model);

            customizeShipmentTable();

            shipmenttable.getColumnModel()
                    .getColumn(2)
                    .setCellEditor(
                            new DateCellEditor());
            
            String[] deliverymodes = {
                "Pick-up",
                "Delivery",
                "Meet-up"
            };
            
            JComboBox<String> deliveryCombo =
                    new JComboBox<>(deliverymodes);

            shipmenttable.getColumnModel()
                    .getColumn(3)
                    .setCellEditor(
                            new DefaultCellEditor(deliveryCombo));            

            String[] statuses = {
                "Pending",
                "In Transit",
                "Delivered",
                "Failed",
                "Cancelled"
            };

            JComboBox<String> comboBox =
                    new JComboBox<>(statuses);

            shipmenttable.getColumnModel()
                    .getColumn(5)
                    .setCellEditor(
                            new DefaultCellEditor(comboBox));

            rs.close();
            pst.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading shipments: "
                    + e.getMessage());
        }    
    }
        
        private void customizeShipmentTable() {

        shipmenttable.setBackground(java.awt.Color.WHITE);

        shipmenttable.setForeground(
                new java.awt.Color(25, 25, 25));

        shipmenttable.setFont(
                new java.awt.Font(
                        "Baskerville",
                        java.awt.Font.PLAIN,
                        18));

        shipmenttable.setRowHeight(38);

        shipmenttable.setGridColor(
                new java.awt.Color(220, 220, 220));

        shipmenttable.setShowGrid(true);
        shipmenttable.setShowVerticalLines(false);

        shipmenttable.setSelectionBackground(
                new java.awt.Color(35, 35, 35));

        shipmenttable.setSelectionForeground(
                java.awt.Color.WHITE);

        shipmenttable.setAutoResizeMode(
                javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);

        shipmenttable.setFillsViewportHeight(true);

        shipmenttable.getColumnModel()
                .getColumn(0)
                .setMinWidth(100);

        shipmenttable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(120);

        shipmenttable.getColumnModel()
                .getColumn(0)
                .setMaxWidth(140);

        javax.swing.table.JTableHeader header =
                shipmenttable.getTableHeader();

        header.setPreferredSize(
                new java.awt.Dimension(
                        header.getWidth(),
                        42));

        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        javax.swing.table.DefaultTableCellRenderer headerRenderer =
                new javax.swing.table.DefaultTableCellRenderer();

        headerRenderer.setHorizontalAlignment(
                javax.swing.JLabel.CENTER);

        headerRenderer.setBackground(
                new java.awt.Color(28, 28, 28));

        headerRenderer.setForeground(
                java.awt.Color.WHITE);

        headerRenderer.setFont(
                new java.awt.Font(
                        "Big Caslon",
                        java.awt.Font.BOLD,
                        18));

        headerRenderer.setBorder(
                javax.swing.BorderFactory
                        .createMatteBorder(
                                0,
                                0,
                                1,
                                1,
                                new java.awt.Color(65, 65, 65)));

        for (int i = 0;
             i < shipmenttable.getColumnModel().getColumnCount();
             i++) {

            shipmenttable.getColumnModel()
                    .getColumn(i)
                    .setHeaderRenderer(headerRenderer);
        }

        centerAlignShipmentTable();
    }
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        back = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        edittable = new javax.swing.JButton();
        searchshipmentID = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        shipmenttable = new javax.swing.JTable();
        refresh = new javax.swing.JButton();
        save = new javax.swing.JButton();
        bg = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ifccrafts/assets/back.png"))); // NOI18N
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        back.addActionListener(this::backActionPerformed);
        add(back, new org.netbeans.lib.awtextra.AbsoluteConstraints(98, 75, -1, 77));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 280, -1, 70));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 380, -1, 70));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 480, -1, 70));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 180, -1, 70));

        edittable.setBackground(new java.awt.Color(255, 255, 255));
        edittable.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        edittable.setForeground(new java.awt.Color(0, 0, 0));
        edittable.setText("Add/Delete");
        edittable.addActionListener(this::edittableActionPerformed);
        add(edittable, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 100, 120, 40));

        searchshipmentID.setBackground(new java.awt.Color(255, 255, 255));
        searchshipmentID.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        searchshipmentID.setForeground(new java.awt.Color(0, 0, 0));
        searchshipmentID.setText("Search Shipment ID");
        searchshipmentID.addActionListener(this::searchshipmentIDActionPerformed);
        add(searchshipmentID, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 570, 40));

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 204));

        shipmenttable.setBackground(new java.awt.Color(255, 255, 255));
        shipmenttable.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        shipmenttable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(shipmenttable);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 1070, 580));

        refresh.setBackground(new java.awt.Color(255, 255, 255));
        refresh.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        refresh.setForeground(new java.awt.Color(0, 0, 0));
        refresh.setText("Refresh");
        refresh.addActionListener(this::refreshActionPerformed);
        add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 100, 120, 40));

        save.setBackground(new java.awt.Color(255, 255, 255));
        save.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        save.setForeground(new java.awt.Color(0, 0, 0));
        save.setText("Save");
        save.addActionListener(this::saveActionPerformed);
        add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 100, 120, 40));

        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ifccrafts/assets/mainbg.png"))); // NOI18N
        bg.setText("jLabel5");
        add(bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1300, 850));
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        javax.swing.JFrame frame =
        (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

        frame.setContentPane(new homepage());
        frame.revalidate();
        frame.repaint();
    }//GEN-LAST:event_backActionPerformed

    private void edittableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edittableActionPerformed
        javax.swing.JFrame frame =
        (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

        frame.setContentPane(new editshipment());
        frame.revalidate();
        frame.repaint(); 
    }//GEN-LAST:event_edittableActionPerformed

    private void searchshipmentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchshipmentIDActionPerformed
        String inputShipmentID =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Shipment ID:");

        if (inputShipmentID == null
                || inputShipmentID.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter an ID.");

            return;
        }

        try {

            String sql =
                    "SELECT shipment_id, order_id, "
                    + "shipment_date, delivery_mode, "
                    + "courier, shipment_status "
                    + "FROM dbo.Shipments "
                    + "WHERE CAST(shipment_id AS VARCHAR(30)) LIKE ?";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setString(
                    1,
                    "%" + inputShipmentID.trim() + "%");

            ResultSet rs =
                    pst.executeQuery();

            DefaultTableModel model =
                    new DefaultTableModel() {

                @Override
                public boolean isCellEditable(
                        int row,
                        int column) {

                    return column != 0 && column != 1;
                }
            };

            model.setColumnIdentifiers(new String[]{
                "Shipment ID",
                "Order ID",
                "Shipment Date",
                "Delivery Mode",
                "Courier",
                "Status"
            });

            java.util.List<String> idList =
                    new java.util.ArrayList<>();

            java.util.List<Object[]> dataList =
                    new java.util.ArrayList<>();

            boolean found = false;

            while (rs.next()) {

                found = true;

                String shipmentId =
                        rs.getString("shipment_id");

                Object[] row = new Object[]{
                    shipmentId,
                    rs.getInt("order_id"),
                    rs.getDate("shipment_date"),
                    rs.getString("delivery_mode"),
                    rs.getString("courier"),
                    rs.getString("shipment_status")
                };

                model.addRow(row);

                idList.add(shipmentId);
                dataList.add(row);
            }

            if (!found) {

                JOptionPane.showMessageDialog(
                        this,
                        "No matching record found.");

                rs.close();
                pst.close();

                return;
            }

            originalIds =
                    idList.toArray(new String[0]);

            originalData =
                    dataList.toArray(new Object[0][]);

            shipmenttable.setModel(model);

            customizeShipmentTable();

            shipmenttable.getColumnModel()
                    .getColumn(2)
                    .setCellEditor(
                            new DateCellEditor());

            String[] statuses = {
                "Pending",
                "Shipped",
                "Delivered",
                "Cancelled"
            };

            JComboBox<String> comboBox =
                    new JComboBox<>(statuses);

            shipmenttable.getColumnModel()
                    .getColumn(5)
                    .setCellEditor(
                            new DefaultCellEditor(comboBox));

            rs.close();
            pst.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search error: "
                    + e.getMessage());
        }
    }//GEN-LAST:event_searchshipmentIDActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        loadShipmentTable();
        JOptionPane.showMessageDialog(this, "All records are now displayed.");
    }//GEN-LAST:event_refreshActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        DefaultTableModel model = (DefaultTableModel) shipmenttable.getModel();

    if (originalData == null || originalData.length != model.getRowCount()) {
        JOptionPane.showMessageDialog(
                this,
                "Table state is out of sync. Please refresh first.");
        return;
    }

    // Flag to track if ANY row in the table has been modified
    boolean anyChangesDetected = false;

    // First, scan the table to see if anything changed at all
    for (int i = 0; i < model.getRowCount(); i++) {
        for (int j = 0; j < model.getColumnCount(); j++) {
            Object oldVal = originalData[i][j];
            Object newVal = model.getValueAt(i, j);

            if ((oldVal == null && newVal != null)
                    || (oldVal != null && !oldVal.toString().equals(newVal == null ? "" : newVal.toString()))) {
                anyChangesDetected = true;
                break;
            }
        }
        if (anyChangesDetected) {
            break; 
        }
    }

    // If absolutely nothing was changed, alert the user and stop processing
    if (!anyChangesDetected) {
        JOptionPane.showMessageDialog(this, "No changes detected.");
        return;
    }

    // Proceed to database connection only if changes exist
    try (Connection conn = getConnection()) {

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean changed = false;

            for (int j = 0; j < model.getColumnCount(); j++) {
                Object oldVal = originalData[i][j];
                Object newVal = model.getValueAt(i, j);

                if ((oldVal == null && newVal != null)
                        || (oldVal != null && !oldVal.toString().equals(newVal == null ? "" : newVal.toString()))) {
                    changed = true;
                    break;
                }
            }

            if (!changed) {
                continue;
            }

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            StringBuilder preview = new StringBuilder();

            preview.append("Current Information:\n\n");
            preview.append("Shipment ID: ").append(originalData[i][0]).append("\n");
            preview.append("Order ID: ").append(originalData[i][1]).append("\n");
            preview.append("Shipment Date: ").append(originalData[i][2] != null ? sdf.format((java.util.Date) originalData[i][2]) : "").append("\n");
            preview.append("Delivery Mode: ").append(originalData[i][3]).append("\n");
            preview.append("Courier: ").append(originalData[i][4]).append("\n");
            preview.append("Status: ").append(originalData[i][5]).append("\n\n");

            preview.append("New Information:\n\n");
            preview.append("Shipment ID: ").append(model.getValueAt(i, 0)).append("\n");
            preview.append("Order ID: ").append(model.getValueAt(i, 1)).append("\n");

            Object dateObj = model.getValueAt(i, 2);
            if (dateObj instanceof java.util.Date) {
                preview.append("Shipment Date: ").append(sdf.format((java.util.Date) dateObj)).append("\n");
            } else {
                preview.append("Shipment Date: ").append(dateObj != null ? dateObj.toString() : "").append("\n");
            }

            preview.append("Delivery Mode: ").append(model.getValueAt(i, 3)).append("\n");
            preview.append("Courier: ").append(model.getValueAt(i, 4)).append("\n");
            preview.append("Status: ").append(model.getValueAt(i, 5)).append("\n");

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    preview.toString(),
                    "Confirm Save",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return; // User canceled the process
            }

            String sql = "UPDATE dbo.Shipments "
                    + "SET shipment_date = ?, "
                    + "delivery_mode = ?, "
                    + "courier = ?, "
                    + "shipment_status = ? "
                    + "WHERE shipment_id = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                Object dateValue = model.getValueAt(i, 2);

                if (dateValue instanceof java.util.Date) {
                    pst.setDate(1, new java.sql.Date(((java.util.Date) dateValue).getTime()));
                } else if (dateValue != null && !dateValue.toString().trim().isEmpty()) {
                    pst.setDate(1, java.sql.Date.valueOf(dateValue.toString()));
                } else {
                    pst.setNull(1, java.sql.Types.DATE);
                }

                Object modeVal = model.getValueAt(i, 3);
                Object courierVal = model.getValueAt(i, 4);
                Object statusVal = model.getValueAt(i, 5);
                Object idVal = model.getValueAt(i, 0);

                pst.setString(2, (modeVal != null) ? modeVal.toString() : "");
                pst.setString(3, (courierVal != null) ? courierVal.toString() : "");
                pst.setString(4, (statusVal != null) ? statusVal.toString() : "");
                pst.setString(5, (idVal != null) ? idVal.toString() : "");

                pst.executeUpdate();
            }
        }

        JOptionPane.showMessageDialog(this, "Changes saved successfully!");
        loadShipmentTable();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Save Processing Aborted: " + e.getMessage());
    }
    }//GEN-LAST:event_saveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JLabel bg;
    private javax.swing.JButton edittable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton refresh;
    private javax.swing.JButton save;
    private javax.swing.JButton searchshipmentID;
    private javax.swing.JTable shipmenttable;
    // End of variables declaration//GEN-END:variables

}
