package DB.Types;

import DB.Types.Connect.Link;
import java.sql.*;

public class Public
{
	String Mother_name;
	String Father_name;
	String Address;
	String City;
	String State;
    String Country;

	Public()
	{
	    Mother_name = new String();
	    Father_name = new String();
	    Address = new String();
	    City = new String();
	    State = new String();
	    Country = new String();
	}
	
	public void setMotherName(String mn)
	{
	    Mother_name = mn;
	}
	public void setFatherName(String fn)
	{
	    Father_name = fn;
	}
	public void setAddress(String a)
	{
	    Address = a;
	}
	public void setCity(String c)
	{
	    City = c;
	}
	public void setState(String s)
	{
	    State = s;
	}
	
	public void setCountry(String c)
	{
	    Country = c;
	}
	
	boolean insert(long ID)
	{
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    PreparedStatement s = null;
        int res = 0;
        try
        {
            s = c.prepareStatement("INSERT INTO PUBLIC_INFO"
                                + " VALUES (?,?,?,?,?,?,?);");
            s.setLong(1,ID);
            
            if (Mother_name.length() != 0)
                s.setString(2,Mother_name);
            else
                s.setNull(2,Types.NULL);
            
            if (Father_name.length() != 0)
                s.setString(3,Father_name);
            else
                s.setNull(3,Types.NULL);
            
            s.setString(4,Address);
            
            s.setString(5,City);
            
            s.setString(6,State);

            s.setString(7,Country);
            
            res = s.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Cannot Insert Public Info");
            System.out.println("Error Code: "+e.getErrorCode());
            System.out.println("Error Info: "+e.getSQLState());
            System.out.println("Error Description: "+e.getMessage());
        } finally {
            if (s != null)
            try {
                s.close();
            } catch (SQLException e) {
                System.out.println("Error closing Statement!");
                System.out.println("Error Code: \t"+e.getErrorCode());
                System.out.println("Error Info: \t"+e.getSQLState());
	            System.out.println("Error Description: "+e.getMessage());
            }
            if (res == 0)
                return false;
            else
                return true;
	    }
	}
	
	public String getMotherName()
	{
	    return Mother_name;
	}
	public String getFatherName()
	{
	    return Father_name;
	}
	public String getAddress()
	{
	    return Address;
	}
	public String getCity()
	{
	    return City;
	}
	public String getState()
	{
	    return State;
	}
	
	public String getCountry()
	{
	    return Country;
	}
	
	boolean init(long ID)
	{
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    Statement s = null;
        try
        {
            s = c.createStatement();
            
            ResultSet rs = s.executeQuery("SELECT * FROM PUBLIC_INFO;");
            
            if (rs.first())
                do
                {
                    if (rs.getLong(1) == ID)
                    {
                        Mother_name = rs.getString(2);
                        Father_name = rs.getString(3);
                        Address = rs.getString(4);
                        City = rs.getString(5);
                        State = rs.getString(6);
                        Country = rs.getString(7);
                        break;
                    }
                }while(rs.next());
                rs.close();

        } catch (SQLException e) {
            System.out.println("Cannot Access Public Info");
            System.out.println("Error Code: "+e.getErrorCode());
            System.out.println("Error Info: "+e.getSQLState());
            System.out.println("Error Description: "+e.getMessage());
	        return false;
        } finally {
            if (s != null)
            try {
                s.close();
            } catch (SQLException e) {
                System.out.println("Error closing Statement!");
                System.out.println("Error Code: \t"+e.getErrorCode());
                System.out.println("Error Info: \t"+e.getSQLState());
	            System.out.println("Error Description: "+e.getMessage());
            }
            l.disconnect();
	    }
	    return true;
	}
}
