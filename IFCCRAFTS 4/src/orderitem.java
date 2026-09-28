
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
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
    
public class orderitem extends javax.swing.JPanel {
   
    private Connection conn;
    private Integer[] originalIds;
    private Object[][] originalData;
    /**
     * Creates new form customer
     */
    public orderitem() {
        initComponents();
        customizeOrderItemTable();
        loadOrderItemTable();
        
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        for (int i = 0; i < orderitemtable.getColumnCount(); i++) {
            orderitemtable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
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
    
    private void centerAlignOrderItemTable() {
        javax.swing.table.DefaultTableCellRenderer centerRenderer =
                new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        for (int i = 0; i < orderitemtable.getColumnCount(); i++) {
            orderitemtable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    private void loadOrderItemTable() {
        try {
            String sql =
                "SELECT oi.order_id, " +
                "oi.product_id, " +
                "p.product_name, " +
                "oi.quantity, " +
                "oi.price_at_order " +
                "FROM Order_Items oi " +
                "JOIN Products p ON oi.product_id = p.product_id " +
                "ORDER BY oi.order_id";

            Connection conn = getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                
            @Override
            public boolean isCellEditable(int row, int column) {
            // only QUANTITY editable
                if (column == 0 || column == 1 || column == 2 || column == 4) {
                    return false;
                }
                    return true;
                }
            
            };

        model.setColumnIdentifiers(new String[]{
            "Order ID",
            "Product ID",
            "Product Name",
            "Quantity",
            "Price at Order"
        });

        java.util.List<Integer> idList = new java.util.ArrayList<>();
        java.util.List<Object[]> dataList = new java.util.ArrayList<>();

        while (rs.next()) {
            Object[] row = {
                rs.getInt("order_id"),
                rs.getInt("product_id"),
                rs.getString("product_name"),
                rs.getInt("quantity"),
                rs.getDouble("price_at_order")
            };

            model.addRow(row);

            idList.add(rs.getInt("order_id"));
            dataList.add(row);
        }

        originalIds = idList.toArray(new Integer[0]);
        originalData = dataList.toArray(new Object[0][]);

        orderitemtable.setModel(model);

        customizeOrderItemTable();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}
    
    private void loadOrderRecord(int orderId) {
        try {
            String sql =
                "SELECT oi.order_id, " +
                "oi.product_id, " +
                "p.product_name, " +
                "oi.quantity, " +
                "oi.price_at_order " +
                "FROM Order_Items oi " +
                "JOIN Products p ON oi.product_id = p.product_id " +
                "WHERE oi.order_id = ?";

            Connection conn = getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, orderId);
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                // Order ID, Product ID, Product Name and Price at Order NOT editable
                    if (column == 0 || column == 1 || column == 2 || column == 4) {
                        return false;
                    }   
                    return true;
                }   
            };

            model.setColumnIdentifiers(new String[]{
                "Order ID",
                "Product ID",
                "Product Name",
                "Quantity",
                "Price at Order"
            });

            java.util.List<Integer> idList = new java.util.ArrayList<>();
            java.util.List<Object[]> dataList = new java.util.ArrayList<>();

            boolean found = false;   
            while (rs.next()) {
                found = true;
                Object[] row = {
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price_at_order")
                };

                model.addRow(row);
                idList.add(rs.getInt("order_id"));
                dataList.add(row);
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Order ID not found.");
                return;
            }

            originalIds = idList.toArray(new Integer[0]);
            originalData = dataList.toArray(new Object[0][]);
            orderitemtable.setModel(model);
            customizeOrderItemTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    private void loadProductRecord(int productId) {
        try {
            String sql =
                "SELECT oi.order_id, " +
                "oi.product_id, " +
                "p.product_name, " +
                "oi.quantity, " +
                "oi.price_at_order " +
                "FROM Order_Items oi " +
                "JOIN Products p ON oi.product_id = p.product_id " +
                "WHERE oi.product_id = ?";

            Connection conn = getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, productId);
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // only QUANTITY editable
                    if (column == 0 || column == 1 || column == 2 || column == 4) {
                        return false;
                    }
                    return true;
                }
            };

            model.setColumnIdentifiers(new String[]{
                "Order ID",
                "Product ID",
                "Product Name",
                "Quantity",
                "Price at Order"
            });

            java.util.List<Integer> idList = new java.util.ArrayList<>();
            java.util.List<Object[]> dataList = new java.util.ArrayList<>();

            boolean found = false;   
            while (rs.next()) {
                found = true;
                Object[] row = {
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price_at_order")
                };

                model.addRow(row);
                idList.add(rs.getInt("product_id"));
                dataList.add(row);
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Product ID not found.");
                return;
            }

            originalIds = idList.toArray(new Integer[0]);
            originalData = dataList.toArray(new Object[0][]);
            orderitemtable.setModel(model);
            customizeOrderItemTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    private void customizeOrderItemTable() {
        
        orderitemtable.setBackground(java.awt.Color.WHITE);
        orderitemtable.setForeground(new java.awt.Color(25, 25, 25));
        orderitemtable.setFont(new java.awt.Font("Baskerville", java.awt.Font.PLAIN, 18));
        orderitemtable.setRowHeight(38);

        orderitemtable.setGridColor(new java.awt.Color(220, 220, 220));
        orderitemtable.setShowGrid(true);
        orderitemtable.setShowVerticalLines(false);

        orderitemtable.setSelectionBackground(new java.awt.Color(35, 35, 35));
        orderitemtable.setSelectionForeground(java.awt.Color.WHITE);

        orderitemtable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        orderitemtable.setFillsViewportHeight(true);
        
        orderitemtable.getColumnModel().getColumn(0).setMinWidth(80);
        orderitemtable.getColumnModel().getColumn(0).setPreferredWidth(100);
        orderitemtable.getColumnModel().getColumn(0).setMaxWidth(120);

        javax.swing.table.JTableHeader header = orderitemtable.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 42));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBackground(new java.awt.Color(28, 28, 28));
        headerRenderer.setForeground(java.awt.Color.WHITE);
        headerRenderer.setFont(new java.awt.Font("Big Caslon", java.awt.Font.BOLD, 18));
        headerRenderer.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(65, 65, 65)));

        for (int i = 0; i < orderitemtable.getColumnModel().getColumnCount(); i++) {
            orderitemtable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        centerAlignOrderItemTable();
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
        searchorderitemID = new javax.swing.JButton();
        refresh = new javax.swing.JButton();
        save = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderitemtable = new javax.swing.JTable();
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

        searchorderitemID.setBackground(new java.awt.Color(255, 255, 255));
        searchorderitemID.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        searchorderitemID.setForeground(new java.awt.Color(0, 0, 0));
        searchorderitemID.setText("Search Order ID or Product ID");
        searchorderitemID.addActionListener(this::searchorderitemIDActionPerformed);
        add(searchorderitemID, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 570, 40));

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

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 204));

        orderitemtable.setBackground(new java.awt.Color(255, 255, 255));
        orderitemtable.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        orderitemtable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(orderitemtable);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 1070, 580));

        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ifccrafts/assets/mainbg.png"))); // NOI18N
        bg.setText("jLabel5");
        add(bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1300, 850));
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        javax.swing.JFrame frame =
        (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

        frame.setContentPane(new orderpages());
        frame.revalidate();
        frame.repaint();
    }//GEN-LAST:event_backActionPerformed

    private void edittableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edittableActionPerformed
        javax.swing.JFrame frame =
        (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

        frame.setContentPane(new editorderitem());
        frame.revalidate();
        frame.repaint(); 
    }//GEN-LAST:event_edittableActionPerformed

    private void searchorderitemIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchorderitemIDActionPerformed
        String input = JOptionPane.showInputDialog(
        this,
        "Enter Order ID (3000-3999) or Product ID (2000-2999):"
    );

    if (input == null || input.trim().isEmpty()) {
        return;
    }

    input = input.trim();

    try {
        int id = Integer.parseInt(input);

        // ORDER ID
        if (input.startsWith("3")) {

            if (id < 3000 || id > 3999) {
                JOptionPane.showMessageDialog(this,
                    "Order ID must be between 3000-3999.");
                return;
            }

            loadOrderRecord(id);
        }

        // PRODUCT ID
        else if (input.startsWith("2")) {

            if (id < 2000 || id > 2999) {
                JOptionPane.showMessageDialog(this,
                    "Product ID must be between 2000-2999.");
                return;
            }

            loadProductRecord(id);
        }

        // INVALID PREFIX
        else {
            JOptionPane.showMessageDialog(this,
                "ID must start with 2 or 3.");
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "ID must be a valid number.");
    }
    }//GEN-LAST:event_searchorderitemIDActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        loadOrderItemTable();
        JOptionPane.showMessageDialog(this, "All records are now displayed.");
    }//GEN-LAST:event_refreshActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
DefaultTableModel model = (DefaultTableModel) orderitemtable.getModel();

        try {
            Connection conn = getConnection();
            
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean changed = false;
                
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object oldVal = originalData[i][j];
                    Object newVal = model.getValueAt(i, j);

                    if (oldVal == null && newVal != null ||
                        oldVal != null && !oldVal.toString().equals(newVal.toString())) {
                        changed = true;
                        break;
                    }
                }

                if (!changed) {
                    continue;
                }

                StringBuilder preview = new StringBuilder();
                
                preview.append("Current Information:\n\n");
                for (int j = 0; j < model.getColumnCount(); j++) {
                    preview.append(model.getColumnName(j))
                        .append(": ")
                        .append(originalData[i][j])
                        .append("\n");
                }

                preview.append("\nNew Information:\n\n");
                for (int j = 0; j < model.getColumnCount(); j++) {
                    preview.append(model.getColumnName(j))
                        .append(": ")
                        .append(model.getValueAt(i, j))
                        .append("\n");
                }

                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    preview.toString(),
                    "Confirm Save",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
                
                int orderId = Integer.parseInt(model.getValueAt(i, 0).toString());
                int productId = Integer.parseInt(model.getValueAt(i, 1).toString());

                // CHECK ORDER EXISTS
                String orderCheckSQL = "SELECT order_id FROM Orders WHERE order_id=?";
                PreparedStatement orderCheck = conn.prepareStatement(orderCheckSQL);
                orderCheck.setInt(1, orderId);
                ResultSet orderRS = orderCheck.executeQuery();

                if (!orderRS.next()) {
                    JOptionPane.showMessageDialog(this,"Order ID " + orderId + " does not exist.");
                    return;
                }

                // CHECK PRODUCT EXISTS
                String productCheckSQL = "SELECT product_id FROM Products WHERE product_id=?";
                PreparedStatement productCheck = conn.prepareStatement(productCheckSQL);
                productCheck.setInt(1, productId);
                ResultSet productRS = productCheck.executeQuery();

                if (!productRS.next()) {
                    JOptionPane.showMessageDialog(this,"Product ID " + productId + " does not exist.");
                    return;
                }
                
                String sql =
                    "UPDATE Order_Items SET "
                    + "quantity=? "
                    + "WHERE order_id=? "
                    + "AND product_id=?";

                PreparedStatement pst = conn.prepareStatement(sql);

                // quantity
                pst.setInt(1,
                    Integer.parseInt(model.getValueAt(i, 3).toString()));

                // WHERE order_id
                pst.setInt(2,
                    Integer.parseInt(originalData[i][0].toString()));

                // WHERE product_id
                pst.setInt(3,
                    Integer.parseInt(originalData[i][1].toString()));

                pst.executeUpdate();

            }
            
            JOptionPane.showMessageDialog(this,"Changes saved successfully!");
            loadOrderItemTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,e.getMessage());
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
    private javax.swing.JTable orderitemtable;
    private javax.swing.JButton refresh;
    private javax.swing.JButton save;
    private javax.swing.JButton searchorderitemID;
    // End of variables declaration//GEN-END:variables

}
