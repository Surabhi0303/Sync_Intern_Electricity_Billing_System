import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


public class Login extends JDialog {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField4;
    private JButton button1;

    public Login(JFrame p) {
        super(p);
        setTitle("Login");

        // XML Content Pane (loginpanel)
        JPanel loginpanel = new JPanel();
        loginpanel.setLayout(new GridLayout(2, 1, 20, 20));
        loginpanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel welcomeLabel = new JLabel("Welcome to ");
        welcomeLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 18));
        welcomeLabel.setForeground(new Color(-15724528));
        topPanel.add(welcomeLabel);

        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Electricity Bill System");
        middlePanel.setBackground(new Color(-13989452));
        titleLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 20));
        titleLabel.setForeground(new Color(-15724528));
        middlePanel.add(titleLabel);

        loginpanel.add(topPanel);
        loginpanel.add(middlePanel);

        // XML Panel_1l
        JPanel Panel_1l = new JPanel(new GridLayout(5, 5, 10, 10));
        Panel_1l.setBorder(BorderFactory.createEmptyBorder(20, 15, 0, 0));

        JLabel customerIDLabel = new JLabel("Customer ID");
        JLabel meterNoLabel = new JLabel("Meter No.");
        JLabel unitsLabel = new JLabel("Units");

        textField1 = new JTextField(20);
        textField2 = new JTextField(15);
        textField4 = new JTextField(15);



        button1 = new JButton("Calculate Bill");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textField1.getText();
                String meter_no = textField2.getText();
                String units = textField4.getText();

                int unit = Integer.parseInt(units);

                try {
                    double bill = Bill_Generation.generate_bill(id, meter_no, unit);

                    showBillInfo(id,meter_no,unit,bill);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });



        Panel_1l.add(customerIDLabel);
        Panel_1l.add(textField1);
        Panel_1l.add(new JLabel());

        Panel_1l.add(meterNoLabel);
        Panel_1l.add(textField2);
        Panel_1l.add(new JLabel());

        Panel_1l.add(unitsLabel);
        Panel_1l.add(textField4);
        Panel_1l.add(new JLabel());

        Panel_1l.add(new JLabel());


        Panel_1l.add(button1);







        // Main Content Pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(loginpanel, BorderLayout.NORTH);
        contentPane.add(Panel_1l,BorderLayout.CENTER);



        setSize(new Dimension(450, 400));
        setLocationRelativeTo(p);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Login l = new Login(null);
            }
        });
    }

    private void showBillInfo(final String id, final String meter_no, final int units, final double bill) throws SQLException {
        JFrame billFrame = new JFrame("Bill Information");
        billFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        billFrame.setSize(300, 300);
        billFrame.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel idLabel = new JLabel("Customer ID:");
        JLabel meterNoLabel = new JLabel("Meter No.:");
        JLabel nameNoLabel = new JLabel("Name:");
        JLabel unitsLabel = new JLabel("Units:");
        JLabel billLabel = new JLabel("Bill Amount:");

        JLabel idValue = new JLabel(id);
        JLabel meterNoValue = new JLabel(meter_no);
        JLabel unitsValue = new JLabel(Integer.toString(units));
        JLabel billValue = new JLabel(Double.toString(bill));
        final String name=retrieveName(id);

        JLabel name1Label=new JLabel(name);








        billFrame.add(idLabel);
        billFrame.add(idValue);


        billFrame.add(meterNoLabel);
        billFrame.add(meterNoValue);
        billFrame.add(nameNoLabel);
        billFrame.add(name1Label);
        billFrame.add(unitsLabel);
        billFrame.add(unitsValue);
        billFrame.add(billLabel);
        billFrame.add(billValue);

        JButton printButton = new JButton("Print");
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    // Generate the PDF and save it to a file
                    generatePDF(id, name, meter_no, units, bill);
                    System.out.println("PDF generated and saved.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(printButton);

        // Add the button panel to the billFrame
        billFrame.add(buttonPanel);

        billFrame.setVisible(true);



        billFrame.setVisible(true);
    }



    private static String retrieveName(String id) throws SQLException {

        Connection c=Bill_Generation.Connection();
        String query = "SELECT name FROM electricity_bill WHERE customer_id=?";
        PreparedStatement st = c.prepareStatement(query);
        st.setString(1, id);
        ResultSet rs = st.executeQuery();

        String name = "";
        if (rs.next()) {
            name = rs.getString("name");
        }

        return name;
    }


    private void generatePDF(String id, String name, String meter_no, int units, double bill) throws Exception {
        String fileName = "Bill_" + id + ".pdf"; // File name for the PDF

        // Create a new PDF document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(fileName));
        Document doc = new Document(pdfDoc);

        // Add bill information to the PDF


        Paragraph centeredText = new Paragraph("Bill Information");
        centeredText.setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER);
        doc.add(centeredText);

        // Create a table with 2 columns
        Table table = new Table(2);

        // Add rows to the table
        table.addCell(new Cell().add("Customer Name:"));
        table.addCell(new Cell().add(name));
        table.addCell(new Cell().add("Customer ID:"));
        table.addCell(new Cell().add(id));
        table.addCell(new Cell().add("Meter No.:"));
        table.addCell(new Cell().add(meter_no));
        table.addCell(new Cell().add("Units:"));
        table.addCell(new Cell().add(Integer.toString(units)));
        table.addCell(new Cell().add("Bill Amount:"));
        table.addCell(new Cell().add("Rs."+(bill)));

        // Add the table to the document
        doc.add(table);

        // Close the PDF document

        doc.close();
    }
}
