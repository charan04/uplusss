//$Id$
package uplus;
import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class dbutil {
	
	public static String chooseRideForDriver(int driverid,int requestid,int customerid)
	{

		ResultSet rs = null;
        Connection connection = null;
        Statement statement = null; 
         JSONArray finalObj=new JSONArray();
         try
        {    
        	 connection = JDBCMySQLConnection.getConnection(); 
        	 String query = "insert into rides (driverid,customerid,ridestatus,ridestarttime,requestid) values (?,?,?,?,?) where ((select count(*) from rides where driverid =? and ridestatus= ? )=0)";
             PreparedStatement preparedStmt = connection.prepareStatement(query);
             preparedStmt.setInt  (1, driverid);
             preparedStmt.setInt  (2, customerid);
             preparedStmt.setString  (3, "ongoing");
             preparedStmt.setLong  (4, System.currentTimeMillis());
             preparedStmt.setInt  (5, requestid);
             preparedStmt.setInt  (6, driverid);
             preparedStmt.setString  (7, "ongoing");
             preparedStmt.execute();
            return "success";
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            if(e.getMessage().contains("constraint"))
            {
            	//requestId shoudl be unqiue in order to serve a request only by one driver
            	return "oops! request is already been taken by other driver";
            			
            }
        }
        try
        {
        /*	 String query2 = "update riderequests set rideid= ? where requestid=? ";//update in riderequest table
        	 PreparedStatement preparedStmt = connection.prepareStatement(query2);
             preparedStmt.setInt  (1, driverid);
             preparedStmt.setInt  (2, customerid);
             preparedStmt.setString  (3, "ongoing");
             preparedStmt.setLong  (4, System.currentTimeMillis());
             preparedStmt.setInt  (5, requestid);
             preparedStmt.executeUpdate();*/
        }
        catch(Exception e)
        {
        	 e.printStackTrace();
        }
         
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
        }

         return "failure";
	}
	public static void updateStatusOfFinsihedRides()
	{

		ResultSet rs = null;
        Connection connection = null;
        Statement statement = null; 
         JSONArray finalObj=new JSONArray();
         long endtime=System.currentTimeMillis()-300000;// 
       // String query = "update rides set ridestatus='completed' where ridestarttime<"+endtime+" and ridestatus='ongoing' ";
         try
        {    
        	 connection = JDBCMySQLConnection.getConnection();
             
        	 String query = "update rides set ridestatus= ? and finishtime = ? where ridestarttime < ? and ridestatus= ? ";
             PreparedStatement preparedStmt = connection.prepareStatement(query);
             preparedStmt.setString  (1, "completed");
             preparedStmt.setLong  (2, System.currentTimeMillis());
             preparedStmt.setLong  (3, endtime);
             preparedStmt.setString  (4, "ongoing");
             preparedStmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

	
	}
	public static JSONArray getAdminDashboardData()
	{
		ResultSet rs = null;
        Connection connection = null;
        Statement statement = null; 
         JSONArray finalObj=new JSONArray();
        String query = "select a.requestid, a.customerid, a.requestedtime, b.ridestatus, b.driverid from riderequests as a left join rides as b on a.requestid=b.requestid;";
       // String query2 = "select a.driverid,b.customerid,b.ridestatus,b.ridestarttime,c.requestedtime from driverdetails as a inner join rides as b inner join riderequests as c on a.driverid=b.driverid and b.rideid = c.rideid  where b.ridestatus in ('ongoing','completed') ;";
        try
        {           
            connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            long currentTime= System.currentTimeMillis();
            while (rs.next())
            {	
            	System.out.println("rows are "+rs.getInt(1)+"-"+rs.getString(2)+"-"+rs.getString(4));
            	long time=rs.getLong(3);
            	long timegap=(currentTime-time)/60000;//in mins
            	String tgap=String.valueOf(timegap).concat(" minutes ago ");
            	
            	JSONObject temp=new JSONObject();
            	temp.put("requestid", rs.getInt(1));
            	//temp.put("reqid", rs.getInt(1));
            	temp.put("customerid", rs.getInt(2));
            	temp.put("timeelapsed", tgap);
            	
            	String ridestatus= rs.getString(4);
            	if(ridestatus!=null&&!ridestatus.isEmpty())
            	{
            		temp.put("status", ridestatus);
            	}
            	else
            	{
            		temp.put("status", "Waitingforaride");
            	}
            	int driverid=rs.getInt(5);
            	if(driverid>0)
            	{
            		temp.put("driver",driverid);
            	}
            	else
            	{
            		temp.put("driver","No");
            		
            		
            	}
            	
            	finalObj.add(temp);
            	
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return finalObj;
	}
	public static JSONArray getDriverDashboardData(int id)
	{    
		// reqstatus- (waiting,ongoing,complete), requestid,customerid, requestedtime, pickdup, completedtime
        ResultSet rs = null;
        Connection connection = null;
        Statement statement = null; 
         JSONArray finalObj=new JSONArray();
        String query = "SELECT a.requestid,a.customerid,a.requestedtime,b.rideid FROM riderequests as a left join rides as b on a.requestid=b.requestid";
        String query2 = "select a.driverid,b.customerid,b.ridestatus,b.ridestarttime,c.requestedtime from driverdetails as a inner join rides as b inner join riderequests as c on a.driverid=b.driverid and b.rideid = c.rideid and a.driverid="+id+" where b.ridestatus in ('ongoing','completed') and a.driverid="+id;
        try
        {           
            connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            long currentTime= System.currentTimeMillis();
            while (rs.next())
            {	
            	System.out.println("rows are "+rs.getInt(1)+"-"+rs.getString(2)+"-"+rs.getString(3)+" rideid "+rs.getInt(4));
            	long time=rs.getLong(3);
            	long timegap=(currentTime-time)/60000;//in mins
            	String tgap=String.valueOf(timegap).concat(" minutes ago ");
            	
            	int rideid=rs.getInt(4);
            	if(rideid==0)
            	{
	            	JSONObject temp=new JSONObject();
	            	temp.put("reqstatus", "waiting");
	            	temp.put("reqid", rs.getInt(1));
	            	temp.put("customerid", rs.getInt(2));
	            	temp.put("requesttime", tgap);
	            	
	            	finalObj.add(temp);
            	}
            	
            }
            rs = statement.executeQuery(query2);
            while (rs.next())
            {	
            	System.out.println("rows are "+rs.getInt(1)+"-"+rs.getString(2)+"-"+rs.getString(3));
            	long time=rs.getLong(5);
            	long timegap=(currentTime-time)/60000;//in mins
            	String tgap="";//String.valueOf(timegap).concat(" minutes ago ");
            	String ridestatus= rs.getString(3);
            	JSONObject tempobj=new JSONObject();
            	
            	if(!ridestatus.isEmpty())
            	{
            		long starttime=rs.getLong(4);
        			long temp=(currentTime-time)/60000;//in mins
            		if(ridestatus.equalsIgnoreCase("ongoing"))
            		{
            			tgap=String.valueOf(temp).concat(" minutes ago ");//As a ride exactly takes 5 minutes
            			tempobj.put("pickuptime", tgap);
            		}
            		else if(ridestatus.equalsIgnoreCase("completed"))
            		{
            			tgap=String.valueOf(temp+5).concat(" minutes ago ");//As a ride exactly takes 5 minutes
            			tempobj.put("completedtime", tgap);
            		}
            		
            	}
            	String reqTimeGap=String.valueOf(timegap).concat(" minutes ago ");
            	
            	tempobj.put("reqstatus",ridestatus);
            	tempobj.put("reqid", rs.getInt(1));
            	tempobj.put("customerid", rs.getInt(2));
            	tempobj.put("requesttime", reqTimeGap);
            	
            	finalObj.add(tempobj);
            	
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return finalObj;
    }
	
	public static void getEmployee(int employeeId)  {      
        ResultSet rs = null;
        Connection connection = null;
        Statement statement = null; 
         
      //  Employee employee = null;
        String query = "SELECT * FROM customer";// WHERE emp_id=" + employeeId;
        try {           
            connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
             
            if (rs.next()) {
            	
            	System.out.println("rows are "+rs.getInt(1)+"-"+rs.getString(2)+"-"+rs.getString(3));
          /*      employee = new Employee();
                employee.setEmpId(rs.getInt("emp_id"));
                employee.setEmpName(rs.getString("emp_name"));
                employee.setDob(rs.getDate("dob"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setDeptId((rs.getInt("dept_id")));*/
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
     //   return employee;
    }
	public static void addNewRideRequest(int customerid,int location)
	{
		 Connection connection = null;
	        Statement statement = null; 
	        
		try
		{
			 connection = JDBCMySQLConnection.getConnection();
	            statement = connection.createStatement();
	           
			String query = " insert into riderequests (customerid,rideid,requestedtime)"
			        + " values (?, ?,?)";

			      // create the mysql insert preparedstatement
			      PreparedStatement preparedStmt = connection.prepareStatement(query);
			      preparedStmt.setInt (1, customerid);//unique id generator
			      preparedStmt.setInt (2, 0);// indicates--no ride
			      preparedStmt.setLong (3, System.currentTimeMillis());
			      preparedStmt.execute();
			      
			      connection.close();
			      
		}
		catch(Exception e)
		{
			System.out.println("Exception while adding a new request "+e.getMessage());
		}
		finally
		{
			/*if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }*/
		}
	}

}
