import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */


/**
 *
 * @author hezekiahmagalona
 */
public class inventory extends javax.swing.JPanel {
   
    /**
     * Creates new form customer
     */
    
    private Connection conn;
    private String[] originalIds;
    private Object[][] originalData;

  
    public inventory() {
        initComponents();

        try {
            conn = getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            return;
        }

        loadInventoryTable();
        customizeInventoryTable();
        centerAlignInventoryTable();
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
    
    private void centerAlignInventoryTable() {

        javax.swing.table.DefaultTableCellRenderer centerRenderer =
                new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                javax.swing.JLabel.CENTER);

        for (int i = 0;
             i < inventorytable.getColumnCount();
             i++) {

            inventorytable.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centerRenderer);
        }
    }
    
     private void loadInventoryTable() {
 String sql = "SELECT inventory_id, material, stock_quantity, material_cost, "
                   + "(stock_quantity * material_cost) AS total_cost "
                   + "FROM dbo.Inventory";

        try {

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

                    return column != 0 && column != 4;
                }
            };

            model.setColumnIdentifiers(new String[]{
                "Inventory ID",
                "Material",
                "Stock Quantity",
                "Material Cost",
                "Total Cost"
            });

            java.util.List<String> idList =
                    new java.util.ArrayList<>();

            java.util.List<Object[]> dataList =
                    new java.util.ArrayList<>();

            while (rs.next()) {

                String id =
                        String.valueOf(
                                rs.getInt("inventory_id"));

                Object[] row = new Object[]{
                    id,
                    rs.getString("material"),
                    rs.getInt("stock_quantity"),
                    rs.getDouble("material_cost"),
                    rs.getDouble("total_cost")
                };

                model.addRow(row);

                idList.add(id);
                dataList.add(row);
            }

            originalIds =
                    idList.toArray(new String[0]);

            originalData =
                    dataList.toArray(new Object[0][]);

            inventorytable.setModel(model);

            customizeInventoryTable();

            rs.close();
            pst.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading inventory: "
                    + e.getMessage());
        }
     }
     
     private void customizeInventoryTable() {

        inventorytable.setBackground(
                java.awt.Color.WHITE);

        inventorytable.setForeground(
                new java.awt.Color(25, 25, 25));

        inventorytable.setFont(
                new java.awt.Font(
                        "Baskerville",
                        java.awt.Font.PLAIN,
                        18));

        inventorytable.setRowHeight(38);

        inventorytable.setGridColor(
                new java.awt.Color(220, 220, 220));

        inventorytable.setShowGrid(true);

        inventorytable.setShowVerticalLines(false);

        inventorytable.setSelectionBackground(
                new java.awt.Color(35, 35, 35));

        inventorytable.setSelectionForeground(
                java.awt.Color.WHITE);

        inventorytable.setAutoResizeMode(
                javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);

        inventorytable.setFillsViewportHeight(true);

        inventorytable.getColumnModel()
                .getColumn(0)
                .setMinWidth(100);

        inventorytable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(120);

        inventorytable.getColumnModel()
                .getColumn(0)
                .setMaxWidth(140);

        javax.swing.table.JTableHeader header =
                inventorytable.getTableHeader();

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
             i < inventorytable.getColumnModel().getColumnCount();
             i++) {

            inventorytable.getColumnModel()
                    .getColumn(i)
                    .setHeaderRenderer(headerRenderer);
        }

        centerAlignInventoryTable();
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
        searchinventoryID = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventorytable = new javax.swing.JTable();
        edittable = new javax.swing.JButton();
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

        searchinventoryID.setBackground(new java.awt.Color(255, 255, 255));
        searchinventoryID.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        searchinventoryID.setForeground(new java.awt.Color(0, 0, 0));
        searchinventoryID.setText("Search Inventory ID");
        searchinventoryID.addActionListener(this::searchinventoryIDActionPerformed);
        add(searchinventoryID, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 570, 40));

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 204));

        inventorytable.setBackground(new java.awt.Color(255, 255, 255));
        inventorytable.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        inventorytable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(inventorytable);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 1070, 580));

        edittable.setBackground(new java.awt.Color(255, 255, 255));
        edittable.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        edittable.setForeground(new java.awt.Color(0, 0, 0));
        edittable.setText("Add/Delete");
        edittable.addActionListener(this::edittableActionPerformed);
        add(edittable, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 100, 120, 40));

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

    private void searchinventoryIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchinventoryIDActionPerformed
         String inputInventoryID =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Inventory ID:");

        if (inputInventoryID == null
                || inputInventoryID.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter an ID.");

            return;
        }

        try {
            // FIXED: Added comma after material_cost and space before FROM keyword
            String sql = "SELECT inventory_id, material, stock_quantity, material_cost, "
                       + "(stock_quantity * material_cost) AS total_cost "
                       + "FROM dbo.Inventory "
                       + "WHERE CAST(inventory_id AS VARCHAR(30)) LIKE ?";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setString(
                    1,
                    "%" + inputInventoryID.trim() + "%");

            ResultSet rs =
                    pst.executeQuery();

            DefaultTableModel model =
                    new DefaultTableModel() {

                @Override
                public boolean isCellEditable(
                        int row,
                        int column) {

                    return column != 0 && column != 4;
                }
            };

            model.setColumnIdentifiers(new String[]{
                "Inventory ID",
                "Material",
                "Stock Quantity",
                "Material Cost",
                "Total Cost"
            });

            java.util.List<String> idList =
                    new java.util.ArrayList<>();

            java.util.List<Object[]> dataList =
                    new java.util.ArrayList<>();

            boolean found = false;

            while (rs.next()) {

                found = true;

                String inventoryId =
                        rs.getString("inventory_id");

                Object[] row = new Object[]{
                    inventoryId,
                    rs.getString("material"),
                    rs.getInt("stock_quantity"),
                    rs.getDouble("material_cost"),
                    rs.getDouble("total_cost")
                };

                model.addRow(row);

                idList.add(inventoryId);
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

            inventorytable.setModel(model);

            customizeInventoryTable();

            rs.close();
            pst.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search error: "
                    + e.getMessage());
        }
    }//GEN-LAST:event_searchinventoryIDActionPerformed

    private void edittableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edittableActionPerformed
        javax.swing.JFrame frame =
        (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

        frame.setContentPane(new editinventory());
        frame.revalidate();
        frame.repaint(); 
    }//GEN-LAST:event_edittableActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        loadInventoryTable();
        JOptionPane.showMessageDialog(this, "All records are now displayed.");
    }//GEN-LAST:event_refreshActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        if (inventorytable.isEditing()) {
            inventorytable.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model =
                (DefaultTableModel) inventorytable.getModel();

        if (originalData == null || originalData.length != model.getRowCount()) {
            JOptionPane.showMessageDialog(this, "Table state is out of sync. Please refresh first.");
            return;
        }

        StringBuilder preview = new StringBuilder();
        boolean hasChanges = false;

        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean rowChanged = false;

                for (int j = 0; j < 4; j++) {
                    Object newVal = model.getValueAt(i, j);
                    Object oldVal = originalData[i][j];

                    if ((newVal == null && oldVal != null)
                            || (newVal != null && !newVal.toString().equals(oldVal.toString()))) {
                        rowChanged = true;
                        break;
                    }
                }

                if (rowChanged) {
                    hasChanges = true;

                    preview.append("Inventory ID: ")
                            .append(model.getValueAt(i, 0))
                            .append("\n")
                            .append("Material: ")
                            .append(model.getValueAt(i, 1))
                            .append("\n")
                            .append("Stock Quantity: ")
                            .append(model.getValueAt(i, 2))
                            .append("\n")
                            .append("Material Cost: ")
                            .append(model.getValueAt(i, 3))
                            .append("\n")
                            .append("Total Cost: ")
                            .append(model.getValueAt(i, 4))
                            .append("\n\n");
                }
            }

            if (!hasChanges) {
                JOptionPane.showMessageDialog(this, "No changes detected.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "SAVE THESE CHANGES ONLY:\n\n" + preview,
                    "Confirm Save",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                boolean rowChanged = false;

                for (int j = 0; j < 4; j++) {
                    Object newVal = model.getValueAt(i, j);
                    Object oldVal = originalData[i][j];

                    if ((newVal == null && oldVal != null)
                            || (newVal != null && !newVal.toString().equals(oldVal.toString()))) {
                        rowChanged = true;
                        break;
                    }
                }

                if (!rowChanged) {
                    continue;
                }

                int inventoryId = Integer.parseInt(originalIds[i]);

                String material = model.getValueAt(i, 1).toString().trim();

                int stockQuantity = Integer.parseInt(
                        model.getValueAt(i, 2).toString()
                );

                double materialCost = Double.parseDouble(
                        model.getValueAt(i, 3).toString()
                );
                
                double totalCost = 
                        stockQuantity * materialCost;

                if (material.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Material cannot be empty.");
                    return;
                }

                if (stockQuantity < 0 || materialCost < 0) {
                    JOptionPane.showMessageDialog(this, "Stock and cost must be non-negative.");
                    return;
                }

                String sql =
                        "UPDATE dbo.Inventory SET "
                        + "material = ?, "
                        + "stock_quantity = ?, "
                        + "material_cost = ?, "
                        + "total_cost = ? "  
                        + "WHERE inventory_id = ?";

                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setString(1, material);
                    pst.setInt(2, stockQuantity);
                    pst.setDouble(3, materialCost);
                    pst.setDouble(4, totalCost);
                    pst.setInt(5, inventoryId);

                    pst.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(this, "Changes saved successfully!");
            loadInventoryTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock and cost must be numbers.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving changes: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage());
        }
    }//GEN-LAST:event_saveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JLabel bg;
    private javax.swing.JButton edittable;
    private javax.swing.JTable inventorytable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton refresh;
    private javax.swing.JButton save;
    private javax.swing.JButton searchinventoryID;
    // End of variables declaration//GEN-END:variables

}
