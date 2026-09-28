
import javax.swing.JOptionPane;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.Component;
import com.toedter.calendar.JDateChooser;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */


/**
 *
 * @author hezekiahmagalona
 */
public class transactions extends javax.swing.JPanel {
    private Integer[] originalIds;
    private Object[][] originalData;
    private Object orderIDValue;
    
    /**
     * Creates new form customer
     */
    public transactions() {
        initComponents();
        customizeTransactionTable();
        loadTransactionTable();
        
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        for (int i = 0; i < transactiontable.getColumnCount(); i++) {
            transactiontable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
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
    
    private void centerAlignTransactionTable() {
        javax.swing.table.DefaultTableCellRenderer centerRenderer =
                new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        for (int i = 0; i < transactiontable.getColumnCount(); i++) {
            transactiontable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    private void loadTransactionTable() {
        try {
            String sql = "SELECT * FROM Payments ORDER BY payment_id";

            Connection conn = getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                
            @Override
            public boolean isCellEditable(int row, int column) {
            // Payment ID, Order ID, Payment Status, and Date Paid column NOT editable
                if (column == 0 || column == 1 || column == 2) {
                    return false;
                }
                    return true;
                }
            
            };

        model.setColumnIdentifiers(new String[]{
            "Payment ID",
            "Order ID",
            "Payment Status",
            "Date Paid",
            "Payment Method",
            "Amount"
        });

        java.util.List<Integer> idList = new java.util.ArrayList<>();
        java.util.List<Object[]> dataList = new java.util.ArrayList<>();

        while (rs.next()) {
            Object[] row = {

                rs.getInt("payment_id"),
                rs.getInt("order_id"),
                rs.getString("payment_status"),
                rs.getDate("date_paid"),
                rs.getString("payment_method"),
                rs.getDouble("amount")
            };

            model.addRow(row);

            idList.add(rs.getInt("payment_id"));
            dataList.add(row);
        }

        originalIds = idList.toArray(new Integer[0]);
        originalData = dataList.toArray(new Object[0][]);

        transactiontable.setModel(model);

        customizeTransactionTable();
        
        // Payment Method COMBOBOX
        String[] pay_method = {
            "Cash",
            "Gcash",
            "Others"
        };

        // DATE EDITOR
        transactiontable.getColumnModel().getColumn(3)
                .setCellEditor(new DateCellEditor());
        
        JComboBox<String> comboBox = new JComboBox<>(pay_method);

        transactiontable.getColumnModel().getColumn(4)
                .setCellEditor(new DefaultCellEditor(comboBox));

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}
    
    private void loadPaymentRecord(int orderId) {
        try {
            String sql = "SELECT * FROM Payments WHERE payment_id = ?";

            Connection conn = getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, orderId);
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                // Payment ID is NOT editable
                    if (column == 0 || column == 1 || column == 2) {
                        return false;
                    }   
                    return true;
                }   
            };

            model.setColumnIdentifiers(new String[]{
            "Payment ID",
            "Order ID",
            "Payment Status",
            "Date Paid",
            "Payment Method",
            "Amount"
            });

            java.util.List<Integer> idList = new java.util.ArrayList<>();
            java.util.List<Object[]> dataList = new java.util.ArrayList<>();

            boolean found = false;   
            while (rs.next()) {
                found = true;
                Object[] row = {
                    rs.getInt("payment_id"),
                    rs.getInt("order_id"),
                    rs.getString("payment_status"),
                    rs.getDate("date_paid"),
                    rs.getString("payment_method"),
                    rs.getDouble("amount")
            };

                model.addRow(row);
                idList.add(rs.getInt("payment_id"));
                dataList.add(row);
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Payment ID not found.");
                return;
            }

            originalIds = idList.toArray(new Integer[0]);
            originalData = dataList.toArray(new Object[0][]);
            transactiontable.setModel(model);
            customizeTransactionTable();
            
            // Payment Method COMBOBOX
            String[] pay_method = {
                "Cash",
                "Gcash",
                "Others"
            };
            
            // DATE EDITOR
        transactiontable.getColumnModel().getColumn(3)
                .setCellEditor(new DateCellEditor());
            
            JComboBox<String> comboBox = new JComboBox<>(pay_method);

            transactiontable.getColumnModel().getColumn(4)
                    .setCellEditor(new DefaultCellEditor(comboBox));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
   
    
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

    private void customizeTransactionTable() {
    transactiontable.setBackground(java.awt.Color.WHITE);
    transactiontable.setForeground(new java.awt.Color(25, 25, 25));
    transactiontable.setFont(new java.awt.Font("Baskerville", java.awt.Font.PLAIN, 18));
    transactiontable.setRowHeight(38);

    transactiontable.setGridColor(new java.awt.Color(220, 220, 220));
    transactiontable.setShowGrid(true);
    transactiontable.setShowVerticalLines(false);

    transactiontable.setSelectionBackground(new java.awt.Color(35, 35, 35));
    transactiontable.setSelectionForeground(java.awt.Color.WHITE);

    transactiontable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    transactiontable.setFillsViewportHeight(true);

    transactiontable.getColumnModel().getColumn(0).setMinWidth(80);
    transactiontable.getColumnModel().getColumn(0).setPreferredWidth(100);
    transactiontable.getColumnModel().getColumn(0).setMaxWidth(120);

    javax.swing.table.JTableHeader header = transactiontable.getTableHeader();
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

    for (int i = 0; i < transactiontable.getColumnModel().getColumnCount(); i++) {
        transactiontable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
    }

    if (transactiontable.getColumnCount() >= 6) {
        String[] statuses = {"Paid", "Unpaid", "Partial"};
        javax.swing.JComboBox<String> statusCombo =
                new javax.swing.JComboBox<>(statuses);

        transactiontable.getColumnModel().getColumn(2)
                .setCellEditor(new javax.swing.DefaultCellEditor(statusCombo));

        transactiontable.getColumnModel().getColumn(3)
                .setCellEditor(new DateCellEditor());

        String[] methods = {"Cash", "Gcash", "Others"};
        javax.swing.JComboBox<String> methodCombo =
                new javax.swing.JComboBox<>(methods);

        transactiontable.getColumnModel().getColumn(4)
                .setCellEditor(new javax.swing.DefaultCellEditor(methodCombo));
    }

    centerAlignTransactionTable();
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
        searchpaymentID = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        transactiontable = new javax.swing.JTable();
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
        edittable.setText("Edit Table");
        edittable.addActionListener(this::edittableActionPerformed);
        add(edittable, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 100, 120, 40));

        searchpaymentID.setBackground(new java.awt.Color(255, 255, 255));
        searchpaymentID.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        searchpaymentID.setForeground(new java.awt.Color(0, 0, 0));
        searchpaymentID.setText("Search Payment ID");
        searchpaymentID.setToolTipText("");
        searchpaymentID.addActionListener(this::searchpaymentIDActionPerformed);
        add(searchpaymentID, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 560, 40));

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 204));

        transactiontable.setBackground(new java.awt.Color(255, 255, 255));
        transactiontable.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        transactiontable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(transactiontable);

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

        frame.setContentPane(new edittransaction());
        frame.revalidate();
        frame.repaint(); 
    }//GEN-LAST:event_edittableActionPerformed

    private void searchpaymentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchpaymentIDActionPerformed
        String inputPaymentID = JOptionPane.showInputDialog(this,"Enter Payment ID to search:");

        if (inputPaymentID == null
            || inputPaymentID.trim().isEmpty()) {

            loadTransactionTable();
            return;
        }

        try {
            int paymentID = Integer.parseInt(inputPaymentID);

            if (paymentID < 4000) {
                JOptionPane.showMessageDialog(this,"Payment ID must be 4000 or above.");
                return;
            }   

            loadPaymentRecord(paymentID);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,"Payment ID must be a valid number.");
        }
    }//GEN-LAST:event_searchpaymentIDActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        loadTransactionTable();
        JOptionPane.showMessageDialog(this,"All records are now displayed.");

    }//GEN-LAST:event_refreshActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
    DefaultTableModel model = (DefaultTableModel) transactiontable.getModel();

           try {
               Connection conn = getConnection();

               for (int i = 0; i < model.getRowCount(); i++) {
                   boolean changed = false;
                   for (int j = 1; j < model.getColumnCount(); j++) {
                       Object oldVal = originalData[i][j];
                       Object newVal = model.getValueAt(i, j);

                       if ((oldVal == null && newVal != null)
                           || (oldVal != null && !oldVal.toString().equals(String.valueOf(newVal)))) {
                                   changed = true;
                                   break;
                       }
                   }

                   if (!changed) {
                       continue;
                   }

                   // VALUES
                   int paymentID = Integer.parseInt(model.getValueAt(i, 0).toString());
                   int orderID = Integer.parseInt(model.getValueAt(i, 1).toString());
                   String paymentStatus = String.valueOf(model.getValueAt(i, 2));
                   Object dateObj = model.getValueAt(i, 3);
                   String paymentMethod = String.valueOf(model.getValueAt(i, 4));
                   double amount = Double.parseDouble(
                       model.getValueAt(i, 5).toString());

                   // VALIDATIONS

                   if (paymentID < 4000) {
                       JOptionPane.showMessageDialog(this,"Payment ID must be 4000 or above.");
                       return;
                   }
                   if (orderID < 3000) {
                       JOptionPane.showMessageDialog(this,"Order ID must be 3000 or above.");
                       return;
                   }   
                   if (amount < 0) {
                       JOptionPane.showMessageDialog(this,"Amount cannot be negative.");
                       return;
                   }

                   // CHECK ORDER EXISTS
                   String checkOrderSQL = "SELECT total_amount " + "FROM Orders " + "WHERE order_id = ?";
                   PreparedStatement orderCheck = conn.prepareStatement(checkOrderSQL);
                   orderCheck.setInt(1, orderID);
                   ResultSet rs = orderCheck.executeQuery();

                   if (!rs.next()) {
                       JOptionPane.showMessageDialog(this, "Order ID does not exist.");
                       return;
                   }

                   double totalAmount = rs.getDouble("total_amount");
                   java.sql.Date sqlDate = null;

                   // RULE: UNPAID
                   if (amount == 0) {

                       paymentStatus = "Unpaid";

                       JOptionPane.showMessageDialog(
                           this,
                           """
                           RULE ENFORCED:

                           If Amount = 0:
                           • Payment Status must be UNPAID
                           • Date Paid must be EMPTY

                           The system corrected the values automatically.
                           """
                       );

                       sqlDate = null;
                   }

                   // RULE: PARTIAL
                   else if (amount > 0 && amount < totalAmount) {

                       paymentStatus = "Partial";

                       // REQUIRE DATE
                       if (dateObj == null
                       || dateObj.toString().trim().isEmpty()) {

                           JOptionPane.showMessageDialog(
                               this,
                               """
                               RULE ENFORCED:

                               Partial payments require a Date Paid.

                               Please input a valid payment date.
                               """
                           );

                           return;
                       }

                       if (dateObj instanceof java.sql.Date) {

                           sqlDate = (java.sql.Date) dateObj;

                       } else if (dateObj instanceof java.util.Date) {

                           sqlDate = new java.sql.Date(
                               ((java.util.Date) dateObj).getTime()
                           );

                       } else {

                           sqlDate = java.sql.Date.valueOf(dateObj.toString());
                       }
                   }

                   // RULE: PAID
                   else {

                       paymentStatus = "Paid";

                       // REQUIRE DATE
                       if (dateObj == null
                       || dateObj.toString().trim().isEmpty()) {

                           JOptionPane.showMessageDialog(
                               this,
                               """
                               RULE ENFORCED:

                               Paid payments require a Date Paid.

                               Please input a valid payment date.
                               """
                           );

                           return;
                       }

                       if (dateObj instanceof java.sql.Date) {

                           sqlDate = (java.sql.Date) dateObj;

                       } else if (dateObj instanceof java.util.Date) {

                           sqlDate = new java.sql.Date(
                               ((java.util.Date) dateObj).getTime()
                           );

                       } else {

                           sqlDate = java.sql.Date.valueOf(dateObj.toString());
                       }
                   }

                   // PREVIEW
                   String preview = "Current Information:\n\n"
                       + "Payment ID: "
                       + originalData[i][0] + "\n"
                       + "Order ID: "
                       + originalData[i][1] + "\n"
                       + "Payment Status: "
                       + originalData[i][2] + "\n"
                       + "Date Paid: "
                       + (sqlDate == null ? "EMPTY" : sqlDate) + "\n"
                       + "Payment Method: "
                       + originalData[i][4] + "\n"
                       + "Amount: "
                       + originalData[i][5]

                       + "\n\nNew Information:\n\n"
                       + "Payment ID: "
                       + paymentID + "\n"
                       + "Order ID: "
                       + orderID + "\n"
                       + "Payment Status: "
                       + paymentStatus + "\n"
                       + "Date Paid: "
                       + sqlDate + "\n"
                       + "Payment Method: "
                       + paymentMethod + "\n"
                       + "Amount: "
                       + amount;

                   int confirm =JOptionPane.showConfirmDialog(
                       this,
                       preview,
                       "Confirm Save",
                       JOptionPane.YES_NO_OPTION
                   );

                   if (confirm != JOptionPane.YES_OPTION) {
                       continue;
                   }

                   // UPDATE
                   String sql =
                       "UPDATE Payments SET "
                       + "order_id = ?, "
                       + "payment_status = ?, "
                       + "date_paid = ?, "
                       + "payment_method = ?, "
                       + "amount = ? "
                       + "WHERE payment_id = ?";

                   PreparedStatement pst = conn.prepareStatement(sql);

                   pst.setInt(1, orderID);
                   pst.setString(2, paymentStatus);

                   if (sqlDate != null) {
                       pst.setDate(3, sqlDate);
                   } else {
                       pst.setNull(3, java.sql.Types.DATE);
                   }

                   pst.setString(4, paymentMethod);
                   pst.setDouble(5, amount);
                   pst.setInt(6, paymentID);
                   pst.executeUpdate();
               }
               
               transactiontable.clearSelection();

               JOptionPane.showMessageDialog(this,"Changes saved successfully!");
               loadTransactionTable();
               conn.close();
           } catch (Exception e) {
               JOptionPane.showMessageDialog(this,"Error: " + e.getMessage());
               e.printStackTrace();
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
    private javax.swing.JButton searchpaymentID;
    private javax.swing.JTable transactiontable;
    // End of variables declaration//GEN-END:variables

}
