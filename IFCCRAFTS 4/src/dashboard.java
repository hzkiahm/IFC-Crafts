import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontFormatException;
import java.awt.Insets;
import java.io.IOException;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */


/**
 *
 * @author hezekiahmagalona
 */
public class dashboard extends javax.swing.JPanel {
   
    /**
     * Creates new form customer
     */
    private boolean useDateFilter = false;
    private Connection conn;
    private java.awt.Font customFont;
    
    public dashboard() {
        initComponents();
        try {
            conn = getConnection();
            loadFonts();
            
            best.setFont(customFont.deriveFont(java.awt.Font.BOLD, 28f));
            best1.setFont(customFont.deriveFont(java.awt.Font.BOLD, 28f));
            best2.setFont(customFont.deriveFont(java.awt.Font.BOLD, 28f));
            
            // Month
            jMonthChooser.setBackground(Color.WHITE);
            jMonthChooser.setForeground(Color.BLACK);

            JComboBox<?> monthCombo =
                    (JComboBox<?>) jMonthChooser.getComboBox();
            monthCombo.setUI(new BasicComboBoxUI());
            monthCombo.setBackground(Color.WHITE);
            monthCombo.setForeground(Color.BLACK);
            
            ((JLabel) monthCombo.getRenderer())
                    .setHorizontalAlignment(SwingConstants.CENTER);

            Component comp = jYearChooser.getComponent(0);
            
            monthCombo.setBorder(BorderFactory.createEmptyBorder());
            monthCombo.setPreferredSize(jMonthChooser.getPreferredSize());

           // JYearChooser
            jYearChooser.setBackground(Color.WHITE);
            jYearChooser.setForeground(Color.BLACK);

            if (comp instanceof JTextField) {
            JTextField yearField = (JTextField) comp;

            yearField.setHorizontalAlignment(JTextField.CENTER);
            yearField.setBorder(BorderFactory.createEmptyBorder());

            yearField.setBackground(Color.WHITE);
            yearField.setForeground(Color.BLACK);

            // make field use full width
            yearField.setColumns(4);
            yearField.setMargin(new Insets(0, 0, 0, 0));
        }
        
            useDateFilter = false;

            addPieChart();
            loadSales();
            loadProfit();
            

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dashboard failed to load: " + e.getMessage());
        }
    }
    
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
    
    private Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://localhost:1433;"
                + "databaseName=ifcdb;"
                + "encrypt=true;"
                + "trustServerCertificate=true";

        String user = "sa";
        String password = "Anolagam28";

        return java.sql.DriverManager.getConnection(url, user, password);
    }

    private void addPieChart() {
        // JavaFX embedded container
        JFXPanel fxPanel = new JFXPanel();

        bestsellingitems_container.setLayout(new BorderLayout());
        bestsellingitems_container.removeAll();
        bestsellingitems_container.add(fxPanel, BorderLayout.CENTER);
        bestsellingitems_container.revalidate();
        bestsellingitems_container.repaint();

        Platform.runLater(() -> {
            PieChart pieChart = new PieChart();
            pieChart.setTitle(null);
            pieChart.setLabelsVisible(true);
            
            pieChart.setLabelLineLength(20);
            pieChart.setClockwise(true);


            try {
                String sql =
                    "SELECT p.product_name, SUM(oi.quantity) AS total_qty\n" +
                    "FROM Order_Items oi\n" +
                    "JOIN Products p ON oi.product_id = p.product_id\n" +
                    "GROUP BY p.product_name\n" +
                    "ORDER BY total_qty DESC";

                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();


                while (rs.next()) {

                    String name = rs.getString("product_name");
                    int qty = rs.getInt("total_qty");

                    pieChart.getData().add(
                        new PieChart.Data(name, qty)
                    );
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            VBox chartCard = new VBox(pieChart);
            chartCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-padding: 20;" +
                "-fx-border-color: #000000;" +
                "-fx-border-width: 3;"
            );

            fxPanel.setScene(new Scene(chartCard));
        });
        
    }
    
    private void showDailyProfitChart(int month, int year) {

    JFXPanel fxPanel = new JFXPanel();

    profit_container.removeAll();
    profit_container.setLayout(new BorderLayout());
    profit_container.add(fxPanel, BorderLayout.CENTER);

    Platform.runLater(() -> {

        javafx.scene.chart.CategoryAxis xAxis =
                new javafx.scene.chart.CategoryAxis();

        javafx.scene.chart.NumberAxis yAxis =
                new javafx.scene.chart.NumberAxis();

        javafx.scene.chart.BarChart<String, Number> chart =
                new javafx.scene.chart.BarChart<>(xAxis, yAxis);

        chart.setLegendVisible(false);

        javafx.scene.chart.XYChart.Series<String, Number> series =
                new javafx.scene.chart.XYChart.Series<>();

        try {

            String sql =
                "SELECT DAY(date_paid) AS day, " +
                "SUM(amount) AS total " +
                "FROM Payments " +
                "WHERE payment_status='Paid' " +
                "AND MONTH(date_paid)=? " +
                "AND YEAR(date_paid)=? " +
                "GROUP BY DAY(date_paid) " +
                "ORDER BY DAY(date_paid)";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, month);
            pst.setInt(2, year);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                int day = rs.getInt("day");
                double total = rs.getDouble("total");

                series.getData().add(
                        new javafx.scene.chart.XYChart.Data<>(
                                String.valueOf(day),
                                total
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        chart.getData().add(series);
        fxPanel.setScene(new Scene(chart));
    });

    profit_container.revalidate();
    profit_container.repaint();
}
    
    private void showProfitBarChart(int month, int year) {

        JFXPanel fxPanel = new JFXPanel();

        profit_container.removeAll();
        profit_container.setLayout(new BorderLayout());
        profit_container.add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {

            javafx.scene.chart.CategoryAxis xAxis = new javafx.scene.chart.CategoryAxis();
            javafx.scene.chart.NumberAxis yAxis = new javafx.scene.chart.NumberAxis();

            javafx.scene.chart.BarChart<String, Number> barChart =
                    new javafx.scene.chart.BarChart<>(xAxis, yAxis);


            javafx.scene.chart.XYChart.Series<String, Number> series =
                    new javafx.scene.chart.XYChart.Series<>();

            try {
                double sales = 0;
                double expense = 0;

                // SALES
                String salesSQL =
                    "SELECT SUM(amount) AS total " +
                    "FROM Payments " +
                    "WHERE payment_status='Paid' " +
                    "AND EXTRACT(MONTH FROM date_paid)=? " +
                    "AND EXTRACT(YEAR FROM date_paid)=?";

                PreparedStatement pst1 = conn.prepareStatement(salesSQL);
                pst1.setInt(1, month);
                pst1.setInt(2, year);
                ResultSet rs1 = pst1.executeQuery();

                if (rs1.next()) {
                    sales = rs1.getDouble("total");
                }

                // EXPENSE
                String expSQL = "SELECT SUM(material_cost) AS totalExpense FROM Inventory";
                PreparedStatement pst2 = conn.prepareStatement(expSQL);
                ResultSet rs2 = pst2.executeQuery();

                if (rs2.next()) {
                    expense = rs2.getDouble("totalExpense");
                }

                double profit = sales - expense;

                // BAR GRAPH DATA
                series.getData().add(new javafx.scene.chart.XYChart.Data<>("Profit", profit));

            } catch (Exception e) {
                e.printStackTrace();
            }

            barChart.getData().add(series);
            fxPanel.setScene(new Scene(barChart));
        });

        profit_container.revalidate();
        profit_container.repaint();
    }
    
    private void loadSales() {
        try {

            double totalSales = 0;

            String salesSQL =
                "SELECT SUM(amount) AS totalSales " +
                "FROM Payments " +
                "WHERE payment_status='Paid'";

            PreparedStatement pst = conn.prepareStatement(salesSQL);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                totalSales = rs.getDouble("totalSales");
            }

            JLabel label = new JLabel("₱ " + totalSales);
            label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
            label.setHorizontalAlignment(JLabel.CENTER);

            sales_container.removeAll();
            sales_container.setLayout(new BorderLayout());
            sales_container.add(label, BorderLayout.CENTER);
            sales_container.revalidate();
            sales_container.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    
    private void besterReload() {
        bestsellingitems_container.removeAll();
        addPieChart();
    }
    
    private void loadProfit() {
        try {

            double totalSales = 0;
            double totalExpense = 0;

            // SALES
            String salesSQL =
                "SELECT SUM(amount) AS totalSales " +
                "FROM Payments WHERE payment_status='Paid'";

            PreparedStatement pst1 = conn.prepareStatement(salesSQL);
            ResultSet rs1 = pst1.executeQuery();

            if (rs1.next()) {
                totalSales = rs1.getDouble("totalSales");
            }

            // COST (Inventory)
            String expSQL =
                "SELECT SUM(material_cost) AS totalExpense FROM Inventory";

            PreparedStatement pst2 = conn.prepareStatement(expSQL);
            ResultSet rs2 = pst2.executeQuery();

            if (rs2.next()) {
                totalExpense = rs2.getDouble("totalExpense");
            }

            double profit = totalSales - totalExpense;

            JLabel label = new JLabel("₱ " + profit);
            label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
            label.setHorizontalAlignment(JLabel.CENTER);

            profit_container.removeAll();
            profit_container.setLayout(new BorderLayout());
            profit_container.add(label, BorderLayout.CENTER);
            profit_container.revalidate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
        private void bestsellerReload() {
            bestsellingitems_container.removeAll();
            addPieChart();
        }
    
    private void loadSalesByDate(int month, int year) {
        try {
            String sql =
                "SELECT SUM(amount) AS totalSales " +
                "FROM Payments " +
                "WHERE payment_status='Paid' " +
                "AND MONTH(date_paid) = ?" +
                "AND YEAR(date_paid) = ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, month);
            pst.setInt(2, year);

            ResultSet rs = pst.executeQuery();

            double sales = 0;
            if (rs.next()) {
                sales = rs.getDouble("totalSales");
            }

            JLabel label = new JLabel("₱ " + sales);
            label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
            label.setHorizontalAlignment(JLabel.CENTER);

            sales_container.removeAll();
            sales_container.setLayout(new BorderLayout());
            sales_container.add(label, BorderLayout.CENTER);
            sales_container.revalidate();
            sales_container.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadProfitByDate(int month, int year) {
        try {
            // SALES BY DATE
            String salesSQL =
                "SELECT SUM(amount) AS totalSales " +
                "FROM Payments " +
                "WHERE payment_status='Paid' " +
                "AND MONTH(date_paid)=?" +
                "AND YEAR(date_paid)=?";

            PreparedStatement pst1 = conn.prepareStatement(salesSQL);
            pst1.setInt(1, month);
            pst1.setInt(2, year);
            ResultSet rs1 = pst1.executeQuery();

            double sales = 0;
            if (rs1.next()) {
                sales = rs1.getDouble("totalSales");
            }

            // EXPENSE
            String expSQL = "SELECT SUM(material_cost) AS totalExpense FROM Inventory";
            PreparedStatement pst2 = conn.prepareStatement(expSQL);
            ResultSet rs2 = pst2.executeQuery();

            double expense = 0;
            if (rs2.next()) {
                expense = rs2.getDouble("totalExpense");
            }

            double profit = sales - expense;

            JLabel label = new JLabel("₱ " + profit);
            label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
            label.setHorizontalAlignment(JLabel.CENTER);

            profit_container.removeAll();
            profit_container.setLayout(new BorderLayout());
            profit_container.add(label, BorderLayout.CENTER);
            profit_container.revalidate();
            profit_container.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     private void loadBestSellingByDate(int month, int year) {

    bestsellingitems_container.removeAll();

    JFXPanel fxPanel = new JFXPanel();

    bestsellingitems_container.setLayout(new BorderLayout());
    bestsellingitems_container.add(fxPanel, BorderLayout.CENTER);

    Platform.runLater(() -> {

        PieChart chart = new PieChart();
        chart.setTitle("Best Sellers");

        try {

            String sql =
                "SELECT p.product_name, SUM(oi.quantity) AS total_qty " +
                "FROM Order_Items oi " +
                "JOIN Orders o ON oi.order_id = o.order_id " +
                "JOIN Products p ON oi.product_id = p.product_id " +
                "WHERE MONTH(o.order_date)=? " +
                "AND YEAR(o.order_date)=? " +
                "GROUP BY p.product_name " +
                "ORDER BY total_qty DESC";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, month);
            pst.setInt(2, year);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                chart.getData().add(
                        new PieChart.Data(
                                rs.getString("product_name"),
                                rs.getInt("total_qty")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox box = new VBox(chart);

        box.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 20;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 3;"
        );

        fxPanel.setScene(new Scene(box));
    });

    bestsellingitems_container.revalidate();
    bestsellingitems_container.repaint();
}
   
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        best2 = new javax.swing.JLabel();
        best1 = new javax.swing.JLabel();
        best = new javax.swing.JLabel();
        back = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        searchdate = new javax.swing.JButton();
        bestsellingitems_container = new javax.swing.JPanel();
        sales_container = new javax.swing.JPanel();
        profit_container = new javax.swing.JPanel();
        refresh = new javax.swing.JButton();
        jMonthChooser = new com.toedter.calendar.JMonthChooser();
        jYearChooser = new com.toedter.calendar.JYearChooser();
        bg = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        best2.setFont(new java.awt.Font("Big Caslon", 0, 24)); // NOI18N
        best2.setForeground(new java.awt.Color(255, 255, 255));
        best2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        best2.setText("Profit / Orders");
        add(best2, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 470, 360, 50));

        best1.setFont(new java.awt.Font("Big Caslon", 0, 24)); // NOI18N
        best1.setForeground(new java.awt.Color(255, 255, 255));
        best1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        best1.setText("Sales");
        add(best1, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 190, 210, 50));

        best.setFont(new java.awt.Font("Big Caslon", 0, 24)); // NOI18N
        best.setForeground(new java.awt.Color(255, 255, 255));
        best.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        best.setText("Best Selling Items");
        add(best, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, 390, 50));

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

        searchdate.setBackground(new java.awt.Color(255, 255, 255));
        searchdate.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        searchdate.setForeground(new java.awt.Color(0, 0, 0));
        searchdate.setText("Search Date");
        searchdate.addActionListener(this::searchdateActionPerformed);
        add(searchdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 90, 150, 40));

        bestsellingitems_container.setLayout(new java.awt.BorderLayout());
        add(bestsellingitems_container, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, 490, 460));

        sales_container.setLayout(new java.awt.BorderLayout());
        add(sales_container, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 250, 430, 190));

        profit_container.setLayout(new java.awt.BorderLayout());
        add(profit_container, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 520, 430, 190));

        refresh.setBackground(new java.awt.Color(255, 255, 255));
        refresh.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        refresh.setForeground(new java.awt.Color(0, 0, 0));
        refresh.setText("Refresh");
        refresh.addActionListener(this::refreshActionPerformed);
        add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 90, 120, 40));

        jMonthChooser.setBackground(new java.awt.Color(255, 255, 255));
        jMonthChooser.setForeground(new java.awt.Color(0, 0, 0));
        jMonthChooser.setFont(new java.awt.Font("Kokonor", 0, 14)); // NOI18N
        jMonthChooser.setMinimumSize(new java.awt.Dimension(142, 45));
        jMonthChooser.setMonth(0);
        jMonthChooser.setOpaque(false);
        jMonthChooser.setPreferredSize(new java.awt.Dimension(126, 38));
        jMonthChooser.setYearChooser(null);
        add(jMonthChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, 140, 40));

        jYearChooser.setBackground(new java.awt.Color(255, 255, 255));
        jYearChooser.setForeground(new java.awt.Color(0, 0, 0));
        jYearChooser.setFont(new java.awt.Font("Kokonor", 0, 18)); // NOI18N
        jYearChooser.setMinimumSize(new java.awt.Dimension(93, 38));
        jYearChooser.setOpaque(false);
        jYearChooser.setPreferredSize(new java.awt.Dimension(80, 30));
        add(jYearChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 90, 90, 40));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        bg.setForeground(new java.awt.Color(0, 0, 0));
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

    private void searchdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchdateActionPerformed
    useDateFilter = true;

    int month = jMonthChooser.getMonth() + 1;
    int year = jYearChooser.getYear();

    loadSalesByDate(month, year);
    loadProfitByDate(month, year);
    loadBestSellingByDate(month, year);
    showDailyProfitChart(month, year);
    }//GEN-LAST:event_searchdateActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
    useDateFilter = false;

    addPieChart();
    loadSales();
    loadProfit();
    JOptionPane.showMessageDialog(this, "Dashboard refreshed!");

    }//GEN-LAST:event_refreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JLabel best;
    private javax.swing.JLabel best1;
    private javax.swing.JLabel best2;
    private javax.swing.JPanel bestsellingitems_container;
    private javax.swing.JLabel bg;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private com.toedter.calendar.JMonthChooser jMonthChooser;
    private com.toedter.calendar.JYearChooser jYearChooser;
    private javax.swing.JPanel profit_container;
    private javax.swing.JButton refresh;
    private javax.swing.JPanel sales_container;
    private javax.swing.JButton searchdate;
    // End of variables declaration//GEN-END:variables

 
}

