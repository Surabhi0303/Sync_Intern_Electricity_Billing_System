import java.sql.*;

public class Bill_Generation {

    /* Current Rates in Maharashtra
    0-100-Rs 4.67
    100-300 units -Rs 6.58
    300-500-8.57
    500 above -Rs 10

     */

    static Connection con;

    public static void main(String[] args) {

    }


    public static double  generate_bill(String id,String meter_no,int units) throws SQLException {

        double bill=0;


        if(units>0 && units<=100)

        {
            bill=units*4.67;
        }

        else if(units>100 && units<=300)

        {
            bill=units*6.58;
        }

        else if(units>300 && units<=500)

        {
            bill=units*8.57;
        }

        else if(units>500)
        {
            bill=units*10;
        }


        Connection c = Connection();
        String query = "UPDATE electricity_bill SET units=?, bill=? WHERE customer_id=?";
        PreparedStatement st = c.prepareStatement(query);
        st.setInt(1, units);
        st.setDouble(2, bill);
        st.setString(3, id);
        st.executeUpdate();




        return bill;




    }

    public static Connection Connection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/electricity_bill";
        String user = "root";
        String pwd = "sheru123#";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        con = DriverManager.getConnection(url, user, pwd);

        return con;

    }



}
