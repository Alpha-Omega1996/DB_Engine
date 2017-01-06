package DB.Types;

import DB.Types.Connect.Link;
import java.sql.*;

public class Financial
{
	int Earning_Method;
	String[] Accounts;

	Financial()
	{
	    Earning_Method = 0;
	    Accounts = new String[1];
	}
	
	public boolean setEarningMethod(int e)
	{
	    switch (e)
	    {
	        case 1:
	        case 2:
	        case 3:
	        case 4:
	            Earning_Method = e;
	            break;
	        default:
	            return false;
	    }
	    return true;
	}
	
	public void setAccounts(String[] a)
	{
	    Accounts = a;
	}
	
	boolean insert(long ID)
	{
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    PreparedStatement s = null;
	    int res = 0;
	    try
	    {
	        s = c.prepareStatement("INSERT INTO EARNINGS VALUES(?,?);");
	        
	        s.setLong(1,ID);
	        
	        s.setInt(2,Earning_Method);
	        
	        res = s.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println("Cannot Insert Financial Info for "+ID);
	        System.out.println("Error code: "+e.getErrorCode());
	        System.out.println("Error info: "+e.getSQLState());
	        System.out.println("Error Description: "+e.getMessage());
	        res = 0;
	    } finally {
	        if (s != null)
	            try {
	                s.close();
	            } catch (SQLException e) {
	                System.out.println("Error closing statement!");
	                System.out.println("Error code: "+e.getErrorCode());
	                System.out.println("Error info: "+e.getSQLState());
	                System.out.println("Error Description: "+e.getMessage());
	            }
	        if (res == 0)
	            return false;
	    }
	    
	    s = null;
	    res = 0;
	    try
	    {
	        s = c.prepareStatement("INSERT INTO ACCOUNTS"
                                + "(ID,BANK_ID,OTHER) "
                                + " VALUES(?,?,?);");
            
            s.setLong(1,ID);
            
            Statement s2 = c.createStatement();

            boolean set = false;
            if (Accounts == null)
            {
                s.setInt(2,0);
                s.setNull(3,Types.NULL);
                res = s.executeUpdate();
            }
            else
                for (String A : Accounts)
                {
                    ResultSet rs = s2.executeQuery("SELECT * FROM"
                                                  + " BANK_TYPES;");
                    if (rs.first())
                        do
                        {
                            if (rs.getString(2).compareTo(A) == 0)
                            {
                                s.setInt(2,rs.getInt(1));
                                s.setNull(3,Types.NULL);
                                set = true;
                                break;
                            }
                        }while(rs.next());
                    if (!set)
                    {
                        s.setInt(2,0);
                        s.setString(3,A);
                    }
                    res = s.executeUpdate();
                    s.setLong(1,ID);
                    rs.close();
                }
            s2.close();
	    } catch (SQLException e) {
            System.out.println("Cannot Insert Financial Info");
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
            l.disconnect();
            if (res == 0)
                return false;
	    }
	    return true;	    
	}
	
	public int getEarningMethod()
	{
	    return Earning_Method;
	}
	
	public String[] getAccounts()
	{
	    return Accounts;
	}
	
	boolean init(long ID)
	{
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    Statement s = null;
	    try
	    {
	        s = c.createStatement();
	        ResultSet rs = s.executeQuery("SELECT * FROM EARNINGS");
	        if (rs.first())
	            do
	            {
	                if (rs.getLong(1) == ID)
	                {
	                    Earning_Method = rs.getInt(2);
	                    break;
	                }   
	            }while(rs.next());
	        rs = s.executeQuery("SELECT A.ID, A.OTHER,B.BANK_NAME"
                            + " FROM ACCOUNTS A,"
                            + " BANK_TYPES B WHERE"
                            + " A.BANK_ID = B.BANK_ID;");
	        StringBuffer sb = new StringBuffer();
	        String p = "";
	        if(rs.first())
	            do
	            {
	                if(rs.getLong(1) == ID)
	                {
	                    sb.append(p);
	                    p = ",";
	                    if (rs.getString(2) == null)
    	                    sb.append(rs.getString(3));
    	                else
    	                    sb.append(rs.getString(2));
	                }
	            }while(rs.next());
	        Accounts = sb.toString().split(",");
	    } catch (SQLException e) {
            System.out.println("Cannot Access Financial Info");
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
