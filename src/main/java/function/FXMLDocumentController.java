package function;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

public class FXMLDocumentController implements Initializable{
	
	@FXML
    private BarChart<?, ?> barChart;

    @FXML
    private AnchorPane main_form;

    private Connection connect=null;
    private PreparedStatement prepare;
    private ResultSet result;
    
    public Connection connectDb()
    {
    	try {
    		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		
			String url = "jdbc:sqlserver://LAPTOP-VH86VFT4\\SQLEXPRESS01:1433;databaseName=STUDENT_MANAGEMENT;encrypt=true;trustServerCertificate=true";
    		String username = "sa";
			String password = "123";
    		Connection con=DriverManager.getConnection(url, username, password);

    		System.out.println("Connect successfully");
    		return con;
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	return null;
    }
    public void chart()
    {
//    	String chartSql= "SELECT date, SUM(total) FROM STUDENT GROUP BY DATE ORDER BY TIMESTAMP (DATE) ASC LIMIT 8" ;
    	
//    	String chartSql= "SELECT MATH, SUM(MATH) FROM STUDENT GROUP BY MATH ORDER BY DATE ASC LIMIT 8" ;
    	String chartSql= "SELECT TOP 8 MATH, SUM(MATH) FROM STUDENT GROUP BY MATH ORDER BY MATH ASC";
    	connect = connectDb();
    	
    	try {
			XYChart.Series chartData = new XYChart.Series();
			
//			prepare = connect.prepareStatement(chartSql);
			prepare = connect.prepareCall(chartSql);
			result= prepare.executeQuery();
			
			while (result.next())
			{
				chartData.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
			}
				
			barChart.getData().add(chartData);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	chart();
	}
}