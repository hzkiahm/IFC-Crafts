import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */


/**
 *
 * @author hezekiahmagalona
 */
public class order extends javax.swing.JPanel {
    private Connection conn;
    private Integer[] originalIds;
    private Object[][] originalData;
    private String currentView = "Orders";
   
   
    /**
     * Creates new form customer
     */
    public order() {
        initComponents();
        
        try {
        conn = getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Database connection failed: " + e.getMessage());
            return;
        }
        
        customizeOrderTable();
        loadOrderTable();
        
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        for (int i = 0; i < ordertable.getColumnCount(); i++) {
            ordertable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    private void centerAlignOrderTable() {
        javax.swing.table.DefaultTableCellRenderer centerRenderer =
                new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        for (int i = 0; i < ordertable.getColumnCount(); i++) {
            ordertable.getColumnModel().getColumn(i)
                    .setCellRenderer(centerRenderer);
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
        
      private void loadOrderTable() {
        currentView = "Orders";

        try {

            String sql = "SELECT * FROM Orders ORDER BY order_id";

            Connection conn = getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                
            @Override
            public boolean isCellEditable(int row, int column) {
            // Order ID, Customer ID, & Total Amount column NOT editable
                if (column == 0 || column == 4 || column == 6) {
                    return false;
                }
                    return true;
                }
            
            };

        model.setColumnIdentifiers(new String[]{
            "Order ID",
            "Order Date",
            "Due Date",
            "Order Status",
            "Customer ID",
            "Affiliate ID",
            "Total Amount"
        });

        java.util.List<Integer> idList = new java.util.ArrayList<>();
        java.util.List<Object[]> dataList = new java.util.ArrayList<>();

        while (rs.next()) {
            Object[] row = {

                rs.getInt("order_id"),
                rs.getDate("order_date"),
                rs.getDate("due_date"),
                rs.getString("order_status"),
                rs.getInt("customer_id"),
                rs.getString("affiliate_id"),
                rs.getDouble("total_amount")
            };

            model.addRow(row);

            idList.add(rs.getInt("order_id"));
            dataList.add(row);
        }

        originalIds = idList.toArray(new Integer[0]);
        originalData = dataList.toArray(new Object[0][]);

        ordertable.setModel(model);

        customizeOrderTable();

        // DATE EDITORS
        ordertable.getColumnModel().getColumn(1)
                .setCellEditor(new DateCellEditor());

        ordertable.getColumnModel().getColumn(2)
                .setCellEditor(new DateCellEditor());

        // STATUS COMBOBOX
        String[] statuses = {
            "Not Started",
            "In Progress",
            "Done"
        };

        JComboBox<String> comboBox = new JComboBox<>(statuses);

        ordertable.getColumnModel().getColumn(3)
                .setCellEditor(new DefaultCellEditor(comboBox));

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
    }
      
      private void loadOrderRecord(int orderId) {
        currentView = "Orders";

        try {

            String sql = "SELECT * FROM Orders WHERE order_id = ?";

            Connection conn = getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, orderId);
            ResultSet rs = pst.executeQuery();
            
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                // Order ID and Total Amount not editable
                    if (column == 0 || column == 4 || column == 6) {
                        return false;
                    }
                    return true;
                }
            };

            model.setColumnIdentifiers(new String[]{
                "Order ID",
                "Order Date",
                "Due Date",
                "Order Status",
                "Customer ID",
                "Affiliate ID",
                "Total Amount"
            });

            java.util.List<Integer> idList = new java.util.ArrayList<>();
            java.util.List<Object[]> dataList = new java.util.ArrayList<>();

            boolean found = false;
            while (rs.next()) {
                found = true;

                Object[] row = {
                    rs.getInt("order_id"),
                    rs.getDate("order_date"),
                    rs.getDate("due_date"),
                    rs.getString("order_status"),
                    rs.getInt("customer_id"),
                    rs.getString("affiliate_id"),
                    rs.getDouble("total_amount")
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
            ordertable.setModel(model);
            customizeOrderTable();
            // DATE EDITORS
            ordertable.getColumnModel().getColumn(1)
                    .setCellEditor(new DateCellEditor());

            ordertable.getColumnModel().getColumn(2)
                    .setCellEditor(new DateCellEditor());

            // STATUS COMBOBOX
            String[] statuses = {
                "Not Started",
                "In Progress",
                "Done"
            };

            JComboBox<String> comboBox = new JComboBox<>(statuses);

            ordertable.getColumnModel().getColumn(3)
                    .setCellEditor(new DefaultCellEditor(comboBox));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
      
     private void loadOrderItemRecord(int orderId) {
       currentView = "Order_Items";
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
            ordertable.setModel(model);
            customizeOrderTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
  // PARA MAEDIT 'YUNG DATE SA MISMONG TABLE 
    public class DateCellEditor extends AbstractCellEditor implements TableCellEditor { 
        private JDateChooser dateChooser;

        public DateCellEditor() {
            dateChooser = new JDateChooser();
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
                dateChooser.setDate((java.util.Date) value);
            } else {
                dateChooser.setDate(null);
            }

            return dateChooser;
        }
    }
        
        private void customizeOrderTable() {
        ordertable.setBackground(java.awt.Color.WHITE);
        ordertable.setForeground(new java.awt.Color(25, 25, 25));
        ordertable.setFont(new java.awt.Font("Baskerville", java.awt.Font.PLAIN, 18));
        ordertable.setRowHeight(38);

        ordertable.setGridColor(new java.awt.Color(220, 220, 220));
        ordertable.setShowGrid(true);
        ordertable.setShowVerticalLines(false);

        ordertable.setSelectionBackground(new java.awt.Color(35, 35, 35));
        ordertable.setSelectionForeground(java.awt.Color.WHITE);

        ordertable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        ordertable.setFillsViewportHeight(true);
        
        ordertable.getColumnModel().getColumn(0).setMinWidth(45);
        ordertable.getColumnModel().getColumn(0).setPreferredWidth(55);
        ordertable.getColumnModel().getColumn(0).setMaxWidth(65);

        javax.swing.table.JTableHeader header = ordertable.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 42));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer();

        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBackground(new java.awt.Color(28, 28, 28));
        headerRenderer.setForeground(java.awt.Color.WHITE);
        headerRenderer.setFont(new java.awt.Font("Big Caslon", java.awt.Font.BOLD, 18));
        headerRenderer.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 1, new java.awt.Color(65, 65, 65)
        ));

        for (int i = 0; i < ordertable.getColumnModel().getColumnCount(); i++) {
            ordertable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        centerAlignOrderTable();
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
        searchorderID = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ordertable = new javax.swing.JTable();
        save = new javax.swing.JButton();
        refresh = new javax.swing.JButton();
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

        searchorderID.setBackground(new java.awt.Color(255, 255, 255));
        searchorderID.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        searchorderID.setForeground(new java.awt.Color(0, 0, 0));
        searchorderID.setText("Search Order ID");
        searchorderID.addActionListener(this::searchorderIDActionPerformed);
        add(searchorderID, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 540, 40));

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 204));

        ordertable.setBackground(new java.awt.Color(255, 255, 255));
        ordertable.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        ordertable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(ordertable);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 1070, 580));

        save.setBackground(new java.awt.Color(255, 255, 255));
        save.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        save.setForeground(new java.awt.Color(0, 0, 0));
        save.setText("Save");
        save.addActionListener(this::saveActionPerformed);
        add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 100, 120, 40));

        refresh.setBackground(new java.awt.Color(255, 255, 255));
        refresh.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        refresh.setForeground(new java.awt.Color(0, 0, 0));
        refresh.setText("Refresh");
        refresh.addActionListener(this::refreshActionPerformed);
        add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 100, 120, 40));

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
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        frame.setContentPane(new editorder());
        frame.revalidate();
        frame.repaint(); 
    }//GEN-LAST:event_edittableActionPerformed

    private void searchorderIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchorderIDActionPerformed
       String input = JOptionPane.showInputDialog(this, "Enter Order ID:");

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            int orderId = Integer.parseInt(input);

            String[] options = {
                "View Orders",
                "View Order Items"
            };

            int choice = JOptionPane.showOptionDialog(
                this,
                "Choose what to view:",
                "Search",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice == 0) {
                loadOrderRecord(orderId);
            } else if (choice == 1) {
                loadOrderItemRecord(orderId);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Order ID.");
        }
    }//GEN-LAST:event_searchorderIDActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        if (ordertable.isEditing()) {
            ordertable.getCellEditor().stopCellEditing();
        }
        DefaultTableModel model = (DefaultTableModel) ordertable.getModel();

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

                if (currentView.equals("Orders")) {
                    String sql =
                        "UPDATE Orders SET "
                        + "order_date=?, "
                        + "due_date=?, "
                        + "order_status=?, "
                        + "customer_id=?, "
                        + "affiliate_id=?, "
                        + "total_amount=? "
                        + "WHERE order_id=?";

                        PreparedStatement pst = conn.prepareStatement(sql);

                        // Get values safely
                        Object orderDateObj = model.getValueAt(i, 1);
                        Object dueDateObj = model.getValueAt(i, 2);
                        Object statusObj = model.getValueAt(i, 3);
                        Object customerObj = model.getValueAt(i, 4);
                        Object affiliateObj = model.getValueAt(i, 5);
                        Object totalObj = model.getValueAt(i, 6);

                        // Validation
                        if (orderDateObj == null ||
                            dueDateObj == null ||
                            statusObj == null ||
                            customerObj == null ||
                            totalObj == null) {

                            JOptionPane.showMessageDialog(this,"Required fields cannot be empty.");
                            return;
                            }

                            // Dates
                            pst.setDate(1,
                                new java.sql.Date(
                                    ((java.util.Date) orderDateObj).getTime()));

                            pst.setDate(2,
                                new java.sql.Date(
                                    ((java.util.Date) dueDateObj).getTime()));

                            // Strings
                            pst.setString(3, statusObj.toString());

                            // Customer ID
                            pst.setInt(4,
                                Integer.parseInt(customerObj.toString()));

                            // Affiliate ID (nullable)
                            if (affiliateObj == null ||
                                affiliateObj.toString().trim().isEmpty()) {

                                pst.setNull(5, java.sql.Types.VARCHAR);

                            } else {
                                pst.setString(5, affiliateObj.toString());
                            }

                            // Total amount
                            pst.setDouble(6,
                                Double.parseDouble(totalObj.toString()));

                            // Order ID
                            pst.setInt(7,
                                Integer.parseInt(model.getValueAt(i, 0).toString()));

                            pst.executeUpdate();
                } else if (currentView.equals("Order_Items")) {
                    String sql =
                        "UPDATE Order_Items SET "
                        + "quantity=?, "
                        + "price_at_order=? "
                        + "WHERE order_id=? "
                        + "AND product_id=?";
                    
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1,
                        Integer.parseInt(model.getValueAt(i, 3).toString()));
                    pst.setDouble(2,
                        Double.parseDouble(model.getValueAt(i, 4).toString()));
                    pst.setInt(3,
                        Integer.parseInt(model.getValueAt(i, 0).toString()));
                    pst.setInt(4,
                        Integer.parseInt(model.getValueAt(i, 1).toString()));
                    pst.executeUpdate();
                }
            }
            
            JOptionPane.showMessageDialog(this,"Changes saved successfully!");
            loadOrderTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }//GEN-LAST:event_saveActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        loadOrderTable();
        JOptionPane.showMessageDialog(this, "All records are now displayed.");
    }//GEN-LAST:event_refreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JLabel bg;
    private javax.swing.JButton edittable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable ordertable;
    private javax.swing.JButton refresh;
    private javax.swing.JButton save;
    private javax.swing.JButton searchorderID;
    // End of variables declaration//GEN-END:variables

}
