package DB.Types;

import DB.Types.Connect.Link;
import java.sql.*;

public class Social
{
	String[] Ans;
	Social()
	{
	    Ans = new String[5];
	}
	
	public boolean setAnswers(String[] a)
	{
	    boolean test = true;
	    for(int i = 0; i < 4; i++)
	        test &= (a[i] != null && a[i].contains("YyNn") && a[i].length() == 1);
	    if (test)
	        Ans = a;
	    return test;
	}
	
	boolean insert(long ID)
	{
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    PreparedStatement s = null;
	    int res = 0;
	    try
	    {
	        s = c.prepareStatement("INSERT INTO MISC_INFO"
	                            + " VALUES (?,?,?,?,?,?);");
	        
	        s.setLong(1,ID);
	        if (Ans == null)
	            for (int i = 0; i < 5; i++)
	                s.setNull(i+2,Types.NULL);
	        else
	        {
	            for (int i = 0; i < 4; i++)
	                if (Ans[i] != null)
    	                s.setString(i+2,Ans[i].charAt(0)+"");
    	            else
    	                s.setNull(i+2,Types.NULL);
	            if (Ans[4] != null && Ans[4].length() != 0)
        	        s.setString(6,Ans[4]);
        	    else
        	        s.setNull(6,Types.NULL);
        	}
	        
	        res = s.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println("Cannot Insert Social Info for "+ID);
	        System.out.println("Error Code: "+e.getErrorCode());
	        System.out.println("Error Info: "+e.getSQLState());
	        System.out.println("Error Description: "+e.getMessage());
	        res = 0;
	    } finally {
	        try
	        {
	            s.close();
	        } catch (SQLException e) {
	            System.err.println("Error closing Statement!");
                System.err.println("Error Code: \t"+e.getErrorCode());
                System.err.println("Error Info: \t"+e.getSQLState());
	            System.err.println("Error Description: "+e.getMessage());
	            l.disconnect();
	            if (res == 0)
    	            return false;
	        }
	    }
	    return true;        
	}
	
	public String[] getAnswers()
	{
	    return Ans;
	}
	
	boolean init(long ID)
	{
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    Statement s = null;
	    try
	    {
	        s = c.createStatement();
	        ResultSet rs = s.executeQuery("SELECT * FROM MISC_INFO;");
	        
	        if (rs.first())
	            do
	            {
	                if (rs.getLong(1) == ID)
	                {
	                    for (int i = 0; i < 5; i++)
	                        Ans[i] = rs.getString(i+2);
	                }
	            }while(rs.next());
	        rs.close();
	    } catch (SQLException e) {
            System.out.println("Cannot Access Social Info");
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
                return false;
            }
            l.disconnect();
	    }
	    return true;
	}
}
