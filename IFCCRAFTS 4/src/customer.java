
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */


/**
 *
 * @author hezekiahmagalona
 */

public class customer extends javax.swing.JPanel {
    
    private Connection conn;
    private String[] originalIds;
    private Object[][] originalData;
   
    /**
     * Creates new form customer
     */
    public customer() {
        initComponents();

        try {
            conn = getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            return;
        }

        loadCustomerTable();
        customizeCustomerTable();
    }
    
    private void centerAlignCustomerTable() {
        javax.swing.table.DefaultTableCellRenderer centerRenderer =
                new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        for (int i = 0; i < customertable.getColumnCount(); i++) {
            customertable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
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
   
     
    private void customizeCustomerTable() {
        
        customertable.setBackground(java.awt.Color.WHITE);
        customertable.setForeground(new java.awt.Color(25, 25, 25));
        customertable.setFont(new java.awt.Font("Baskerville", java.awt.Font.PLAIN, 18));
        customertable.setRowHeight(38);

        customertable.setGridColor(new java.awt.Color(220, 220, 220));
        customertable.setShowGrid(true);
        customertable.setShowVerticalLines(false);

        customertable.setSelectionBackground(new java.awt.Color(35, 35, 35));
        customertable.setSelectionForeground(java.awt.Color.WHITE);

        customertable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        customertable.setFillsViewportHeight(true);
        
        customertable.getColumnModel().getColumn(0).setMinWidth(80);
        customertable.getColumnModel().getColumn(0).setPreferredWidth(100);
        customertable.getColumnModel().getColumn(0).setMaxWidth(120);   

        javax.swing.table.JTableHeader header = customertable.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 42));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        javax.swing.table.DefaultTableCellRenderer headerRenderer =
                new javax.swing.table.DefaultTableCellRenderer();

        headerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        headerRenderer.setBackground(new java.awt.Color(28, 28, 28));
        headerRenderer.setForeground(java.awt.Color.WHITE);
        headerRenderer.setFont(new java.awt.Font("Big Caslon", java.awt.Font.BOLD, 18));
        headerRenderer.setBorder(javax.swing.BorderFactory.createMatteBorder(
                0, 0, 1, 1, new java.awt.Color(65, 65, 65)
        ));

        for (int i = 0; i < customertable.getColumnModel().getColumnCount(); i++) {
            customertable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        centerAlignCustomerTable();
    }
    
    
    private void loadCustomerTable() {
        String sql = "SELECT customer_id, customer_name, customer_address, contact_num FROM dbo.Customers";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column != 0;
}
            };

            model.setColumnIdentifiers(new String[]{
                "Customer ID", "Name", "Address", "Contact Number"
            });

            java.util.List<String> idList = new java.util.ArrayList<>();
            java.util.List<Object[]> dataList = new java.util.ArrayList<>();

            while (rs.next()) {
                String id = String.valueOf(rs.getInt("customer_id"));

                Object[] row = new Object[]{
                    id,
                    rs.getString("customer_name"),
                    rs.getString("customer_address"),
                    rs.getString("contact_num")
                };

                model.addRow(row);
                idList.add(id);
                dataList.add(row);
            }

            originalIds = idList.toArray(new String[0]);
            originalData = dataList.toArray(new Object[0][]);

            customertable.setModel(model);
            customizeCustomerTable();

            rs.close();
            pst.close();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage());
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

        refresh = new javax.swing.JButton();
        save = new javax.swing.JButton();
        back = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        searchcustomerID = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        customertable = new javax.swing.JTable();
        edittable = new javax.swing.JButton();
        bg = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        searchcustomerID.setBackground(new java.awt.Color(255, 255, 255));
        searchcustomerID.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        searchcustomerID.setForeground(new java.awt.Color(0, 0, 0));
        searchcustomerID.setText("Search Customer ID");
        searchcustomerID.addActionListener(this::searchcustomerIDActionPerformed);
        add(searchcustomerID, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 560, 40));

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 204));

        customertable.setBackground(new java.awt.Color(255, 255, 255));
        customertable.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        customertable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(customertable);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 1070, 580));

        edittable.setBackground(new java.awt.Color(255, 255, 255));
        edittable.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        edittable.setForeground(new java.awt.Color(0, 0, 0));
        edittable.setText("Add/Delete");
        edittable.addActionListener(this::edittableActionPerformed);
        add(edittable, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 100, 120, 40));

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

    private void searchcustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchcustomerIDActionPerformed
        String inputCustomerID = JOptionPane.showInputDialog(this, "Enter Customer ID:");

    if (inputCustomerID == null || inputCustomerID.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter an ID.");
        return;
    }

    try {
        String sql = "SELECT customer_id, customer_name, customer_address, contact_num "
                + "FROM dbo.Customers "
                + "WHERE CAST(customer_id AS VARCHAR(30)) LIKE ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, "%" + inputCustomerID.trim() + "%");

        ResultSet rs = pst.executeQuery();

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
            "Customer ID", "Name", "Address", "Contact Number"
        });

        java.util.List<String> idList = new java.util.ArrayList<>();
        java.util.List<Object[]> dataList = new java.util.ArrayList<>();

        boolean found = false;

        while (rs.next()) {
            found = true;

            String customerId = String.valueOf(rs.getInt("customer_id"));

            Object[] row = new Object[]{
                customerId,
                rs.getString("customer_name"),
                rs.getString("customer_address"),
                rs.getString("contact_num")
            };

            model.addRow(row);
            idList.add(customerId);
            dataList.add(row);
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "No matching record found.");
            rs.close();
            pst.close();
            return;
        }

        originalIds = idList.toArray(new String[0]);
        originalData = dataList.toArray(new Object[0][]);

        customertable.setModel(model);
        customizeCustomerTable();

        rs.close();
        pst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
    }//GEN-LAST:event_searchcustomerIDActionPerformed

    private void edittableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edittableActionPerformed
        javax.swing.JFrame frame =
        (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

        frame.setContentPane(new editcustomer());
        frame.revalidate();
        frame.repaint(); 
    }//GEN-LAST:event_edittableActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        loadCustomerTable();
        javax.swing.JOptionPane.showMessageDialog(this, "All records are now displayed.");
    }//GEN-LAST:event_refreshActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        if (customertable.isEditing()) {
        customertable.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) customertable.getModel();
        StringBuilder preview = new StringBuilder();
        boolean hasChanges = false;

    try {
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean rowChanged = false;

            for (int j = 0; j < model.getColumnCount(); j++) {
                Object newVal = model.getValueAt(i, j);
                Object oldVal = originalData[i][j];

                if (newVal == null && oldVal != null ||
                    newVal != null && !newVal.toString().equals(oldVal.toString())) {

                    rowChanged = true;
                    break;
                }
            }

            if (rowChanged) {
                hasChanges = true;

                preview.append("Customer ID: ").append(model.getValueAt(i, 0)).append("\n")
                       .append("Name: ").append(model.getValueAt(i, 1)).append("\n")
                       .append("Address: ").append(model.getValueAt(i, 2)).append("\n")
                       .append("Contact Number: ").append(model.getValueAt(i, 3)).append("\n\n");
            }
        }

        if (!hasChanges) {
            JOptionPane.showMessageDialog(this, "No changes detected.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "SAVE THESE CHANGES ONLY:\n\n" + preview.toString(),
                "Confirm Save",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean rowChanged = false;

            for (int j = 0; j < model.getColumnCount(); j++) {
                Object newVal = model.getValueAt(i, j);
                Object oldVal = originalData[i][j];

                if (newVal == null && oldVal != null ||
                    newVal != null && !newVal.toString().equals(oldVal.toString())) {

                    rowChanged = true;
                    break;
                }
            }

            if (!rowChanged) continue;

            int originalCustomerId = Integer.parseInt(originalIds[i]);
            String name = model.getValueAt(i, 1).toString();
            String address = model.getValueAt(i, 2).toString();
            String contactNumber = model.getValueAt(i, 3).toString();

            String sql = "UPDATE dbo.Customers SET "
                    + "customer_name=?, customer_address=?, contact_num=? "
                    + "WHERE customer_id=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, name);
            pst.setString(2, address);
            pst.setString(3, contactNumber);
            pst.setInt(4, originalCustomerId);

                        pst.executeUpdate();
                        pst.close();
                    }

        JOptionPane.showMessageDialog(this, "Changes saved successfully!");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }

    loadCustomerTable();
    }//GEN-LAST:event_saveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JLabel bg;
    private javax.swing.JTable customertable;
    private javax.swing.JButton edittable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton refresh;
    private javax.swing.JButton save;
    private javax.swing.JButton searchcustomerID;
    // End of variables declaration//GEN-END:variables

}