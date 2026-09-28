
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
public class affiliate extends javax.swing.JPanel {
   
    /**
     * Creates new form customer
     */    
    private Connection conn;
    private String[] originalIds;
    private Object[][] originalData;
    public affiliate() {
        initComponents();
        
        try {
            conn = getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            return;
        }
        
        loadAffiliateTable();
        customizeAffiliateTable(); 
        
        javax.swing.table.DefaultTableCellRenderer centerRenderer =
                new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        for (int i = 0; i < affiliatetable.getColumnCount(); i++) {
            affiliatetable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
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
    
    private void centerAlignAffiliateTable() {
        javax.swing.table.DefaultTableCellRenderer centerRenderer =
                new javax.swing.table.DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        for (int i = 0; i < affiliatetable.getColumnCount(); i++) {
            affiliatetable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    private void loadAffiliateTable() {
        try {
            String sql = "SELECT * FROM Affiliates";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return true;
                }
            };

            model.setColumnIdentifiers(new String[]{
                "ID", "Name", "Start Date", "End Date", "Status", "Social Media"
            });

            java.util.List<String> idList = new java.util.ArrayList<>();
            java.util.List<Object[]> dataList = new java.util.ArrayList<>();
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

            while (rs.next()) {
                String id = rs.getString("affiliate_id");
                java.sql.Date startDate = rs.getDate("start_date");
                java.sql.Date endDate = rs.getDate("end_date");
                String status = rs.getString("aff_status");

                // UPDATED: Expire if past end date, otherwise default to "Active"
                if (endDate != null) {
                    if (endDate.getTime() < today.getTime()) {
                        status = "Expired";
                    } else {
                        status = "Active";
                    }
                }

                Object[] row = new Object[]{
                    id,
                    rs.getString("aff_name"),
                    startDate,
                    endDate,
                    status,
                    rs.getString("soc_med_acc")
                };

                model.addRow(row);
                idList.add(id);
                dataList.add(row); 
            }

            originalIds = idList.toArray(new String[0]);
            originalData = dataList.toArray(new Object[0][]);

            affiliatetable.setModel(model);
            customizeAffiliateTable();
            
            // START DATE EDIT
            affiliatetable.getColumnModel().getColumn(2)
                .setCellEditor(new DateCellEditor());

            // END DATE EDIT
            affiliatetable.getColumnModel().getColumn(3)
                .setCellEditor(new DateCellEditor());

            // COMBO BOX EDIT
            String[] statuses = {"Active", "Inactive", "Expired"};
            JComboBox<String> comboBox = new JComboBox<>(statuses);

            affiliatetable.getColumnModel().getColumn(4)
                    .setCellEditor(new DefaultCellEditor(comboBox));
            
            rs.close();
            pst.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        centerAlignAffiliateTable();
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
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (value instanceof java.util.Date) {
                dateChooser.setDate((java.util.Date) value);
            }
            return dateChooser;
        }
    }
    
    
   private void loadAffiliateRecord(String affiliateId) {
        String sql = "SELECT * FROM Affiliates WHERE affiliate_id = ?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, affiliateId);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Name");
            model.addColumn("Start Date");
            model.addColumn("End Date");
            model.addColumn("Status");
            model.addColumn("Social Media");

            boolean found = false;
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

            while (rs.next()) {
                found = true;
                java.sql.Date endDate = rs.getDate("end_date");
                String status = rs.getString("aff_status");

                // UPDATED: Expire if past end date, otherwise default to "Active"
                if (endDate != null) {
                    if (endDate.getTime() < today.getTime()) {
                        status = "Expired";
                    } else {
                        status = "Active";
                    }
                }

                model.addRow(new Object[]{
                    rs.getString("affiliate_id"),
                    rs.getString("aff_name"),
                    rs.getDate("start_date"),
                    endDate,
                    status,
                    rs.getString("soc_med_acc")
                });
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Affiliate ID not found.");
                return;
            }

            affiliatetable.setModel(model); 
            centerAlignAffiliateTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    
   private void customizeAffiliateTable() {
        affiliatetable.setBackground(java.awt.Color.WHITE);
        affiliatetable.setForeground(new java.awt.Color(25, 25, 25));
        affiliatetable.setFont(new java.awt.Font("Baskerville", java.awt.Font.PLAIN, 18));
        affiliatetable.setRowHeight(38);

        affiliatetable.setGridColor(new java.awt.Color(220, 220, 220));
        affiliatetable.setShowGrid(true);
        affiliatetable.setShowVerticalLines(false);

        affiliatetable.setSelectionBackground(new java.awt.Color(35, 35, 35));
        affiliatetable.setSelectionForeground(java.awt.Color.WHITE);

        affiliatetable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        affiliatetable.setFillsViewportHeight(true);

        affiliatetable.getColumnModel().getColumn(0).setMinWidth(80);
        affiliatetable.getColumnModel().getColumn(0).setPreferredWidth(100);
        affiliatetable.getColumnModel().getColumn(0).setMaxWidth(120);

        javax.swing.table.JTableHeader header = affiliatetable.getTableHeader();
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

        for (int i = 0; i < affiliatetable.getColumnModel().getColumnCount(); i++) {
            affiliatetable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
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
        edittable = new javax.swing.JButton();
        searchaffiliateID = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        affiliatetable = new javax.swing.JTable();
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

        edittable.setBackground(new java.awt.Color(255, 255, 255));
        edittable.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        edittable.setForeground(new java.awt.Color(0, 0, 0));
        edittable.setText("Add/Delete");
        edittable.addActionListener(this::edittableActionPerformed);
        add(edittable, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 100, 120, 40));

        searchaffiliateID.setBackground(new java.awt.Color(255, 255, 255));
        searchaffiliateID.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        searchaffiliateID.setForeground(new java.awt.Color(0, 0, 0));
        searchaffiliateID.setText("Search Affiliate ID");
        searchaffiliateID.setToolTipText("");
        searchaffiliateID.addActionListener(this::searchaffiliateIDActionPerformed);
        add(searchaffiliateID, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 550, 40));

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 204));

        affiliatetable.setBackground(new java.awt.Color(255, 255, 255));
        affiliatetable.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        affiliatetable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(affiliatetable);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 1080, 580));

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

        frame.setContentPane(new editaffiliate());
        frame.revalidate();
        frame.repaint(); 
    }//GEN-LAST:event_edittableActionPerformed

    private void searchaffiliateIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchaffiliateIDActionPerformed
        String id = JOptionPane.showInputDialog(this, "Enter Affiliate ID:");

        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID.");
            return;
        }

        String sql = "SELECT * FROM Affiliates WHERE affiliate_id LIKE ?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + id.trim() + "%");
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{
                "ID", "Name", "Start Date", "End Date", "Status", "Social Media"
            });

            boolean found = false;
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

            while (rs.next()) {
                found = true;
                java.sql.Date endDate = rs.getDate("end_date");
                String status = rs.getString("aff_status");

                // UPDATED: Expire if past end date, otherwise default to "Active"
                if (endDate != null) {
                    if (endDate.getTime() < today.getTime()) {
                        status = "Expired";
                    } else {
                        status = "Active";
                    }
                }

                model.addRow(new Object[]{
                    rs.getString("affiliate_id"),
                    rs.getString("aff_name"),
                    rs.getDate("start_date"),
                    endDate,
                    status,
                    rs.getString("soc_med_acc")
                });
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "No matching record found.");
                return;
            }

            affiliatetable.setModel(model); 
            customizeAffiliateTable();

            String[] statuses = {"Active", "Inactive", "Expired"};
            JComboBox<String> comboBox = new JComboBox<>(statuses);

            affiliatetable.getColumnModel().getColumn(4)
                    .setCellEditor(new DefaultCellEditor(comboBox));

            rs.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        
        centerAlignAffiliateTable();
    }//GEN-LAST:event_searchaffiliateIDActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        if (affiliatetable.isEditing()) {
            affiliatetable.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) affiliatetable.getModel();
        StringBuilder preview = new StringBuilder();
        boolean hasChanges = false;

        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean rowChanged = false;
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object newVal = model.getValueAt(i, j);
                    Object oldVal = originalData[i][j];

                    if ((newVal == null && oldVal != null) ||
                        (newVal != null && !newVal.toString().equals(oldVal.toString()))) {
                        rowChanged = true;
                        break;
                    }
                }

                if (rowChanged) {
                    hasChanges = true;
                    preview.append("ID: ").append(model.getValueAt(i, 0)).append("\n")
                           .append("Name: ").append(model.getValueAt(i, 1)).append("\n")
                           .append("Start Date: ").append(model.getValueAt(i, 2)).append("\n")
                           .append("End Date: ").append(model.getValueAt(i, 3)).append("\n")
                           .append("Status: ").append(model.getValueAt(i, 4)).append("\n")
                           .append("Social: ").append(model.getValueAt(i, 5)).append("\n\n");
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

            String sql = "UPDATE Affiliates SET "
                    + "affiliate_id=?, aff_name=?, start_date=?, end_date=?, aff_status=?, soc_med_acc=? "
                    + "WHERE affiliate_id=?";

            for (int i = 0; i < model.getRowCount(); i++) {
                boolean rowChanged = false;
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object newVal = model.getValueAt(i, j);
                    Object oldVal = originalData[i][j];

                    if ((newVal == null && oldVal != null) ||
                        (newVal != null && !newVal.toString().equals(oldVal.toString()))) {
                        rowChanged = true;
                        break;
                    }
                }

                if (!rowChanged) continue;

                String OG_Id = originalIds[i];
                String new_Id = model.getValueAt(i, 0).toString();
                String name = model.getValueAt(i, 1).toString();

                java.sql.Date startDate = null;
                java.sql.Date endDate = null;

                if (model.getValueAt(i, 2) instanceof java.util.Date) {
                    startDate = new java.sql.Date(((java.util.Date) model.getValueAt(i, 2)).getTime());
                }

                if (model.getValueAt(i, 3) instanceof java.util.Date) {
                    endDate = new java.sql.Date(((java.util.Date) model.getValueAt(i, 3)).getTime());
                }

                String status = model.getValueAt(i, 4).toString();
                
                // UPDATED: Expire if past end date, otherwise default to "Active"
                java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                if (endDate != null) {
                    if (endDate.getTime() < today.getTime()) {
                        status = "Expired";
                        model.setValueAt("Expired", i, 4); 
                    } else {
                        status = "Active";
                        model.setValueAt("Active", i, 4);
                    }
                }

                String social = model.getValueAt(i, 5) != null ? model.getValueAt(i, 5).toString() : "";

                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setString(1, new_Id);
                    pst.setString(2, name);
                    pst.setDate(3, startDate);
                    pst.setDate(4, endDate);
                    pst.setString(5, status);
                    pst.setString(6, social);
                    pst.setString(7, OG_Id);

                    pst.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(this, "Changes saved successfully!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        loadAffiliateTable();
    }//GEN-LAST:event_saveActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        loadAffiliateTable();
        javax.swing.JOptionPane.showMessageDialog(this, "All records are now displayed.");

    }//GEN-LAST:event_refreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable affiliatetable;
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
    private javax.swing.JButton searchaffiliateID;
    // End of variables declaration//GEN-END:variables

}
