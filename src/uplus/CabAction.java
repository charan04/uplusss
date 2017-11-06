//$Id$
package uplus;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpRetryException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CabAction extends HttpServlet{

	
	public void doGet(HttpServletRequest request,  HttpServletResponse response)   throws ServletException, IOException
	{
			
		Connection conn=null;
		try
		{
		PrintWriter out = response.getWriter();
		/*	  out.println("<HTML>");
			  out.println("<HEAD>");
			  out.println("<TITLE>Servlet Testing</TITLE>");
			  out.println("</HEAD>");
			  out.println("<BODY>");
			  out.println("Welcome to the Servlet Testing Center");
			  out.println("</BODY>");
			  out.println("</HTML>");*/
			  
			  //creating the db connection
			// conn =JDBCMySQLConnection.getConnection();
			//  dbutil.getEmployee(1);
			  
			// String view= (String) request.getAttribute("view");
			 String typeofrequest= (String) request.getParameter("type");
			 
			 System.out.println("typeofrequest "+typeofrequest);
			 if(typeofrequest!=null )
			 {
				 //Integer id=  Integer.valueOf((String) request.getParameter("id"));
				 String id= (String) request.getParameter("id");
				 if(typeofrequest.equalsIgnoreCase("cabrequest"))
				 {
					 System.out.println(" The typeofrequest is"+typeofrequest+" and the id is "+id );
					if(id!=null&&!id.isEmpty())
					{ 
						dbutil.addNewRideRequest(Integer.valueOf(id),12);
						out.print("Cab Request successfully made for the customerid "+id);
					}
					else
					{
						out.print("Please provide an customer id for booking the cab");
					}
				 }
				 else if(typeofrequest.equals("admindashboard"))
				 {
					 //out.print("Admins dashboard");
					 JSONArray finalobj=dbutil.getAdminDashboardData();
					 
				
					 
					 out.print(finalobj);
				 }
				 else if(typeofrequest.equals("driverdashboard"))
				 {
					 //out.print("Admins dashboard");
					 if(id!=null&&!id.isEmpty())
					 {
						 JSONArray finalobj=dbutil.getDriverDashboardData(Integer.valueOf(id));
						 if(!finalobj.isEmpty())
						 {
							 out.println(finalobj);
						 }
						 else
						 {
							 out.print("Please input proper driver id (1,2,3,4,5 ");
						 }
					 }
					 else
					 {
						 out.println("Please input proper driver id (1,2,3,4,5)");
					 }
					
					
				 }
				 else if(typeofrequest.equals("selectride"))
				 {
					 String reqid= (String) request.getParameter("reqid");
					 String driverid= (String) request.getParameter("driverid");
					 String custid= (String) request.getParameter("custid");
					 if(reqid!=null&&driverid!=null)
					 {
						 String finalobj=dbutil.chooseRideForDriver(Integer.valueOf(driverid), Integer.valueOf(reqid),Integer.valueOf(custid));
						 out.print(finalobj);
					 }
					 else
					 {
						 out.println("Please input proper driver id (1,2,3,4,5)");
					 }
					
					
				 }
			 }
			 else
			 {
				 out.print("Invalid API request,, please check the format");
			 }
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("cabaction");
		}
		finally
		{
			
			System.out.println("cabaction is completed---final block");
		}
			  
		 }
}
