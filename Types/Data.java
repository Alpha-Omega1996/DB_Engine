package DB.Types;

import DB.Types.Connect.Link;
import java.sql.*;
import java.util.*;

public class Data
{
    Personal Pe = new Personal();  //The Personal info of the user.
    Public Pu = new Public();    //The Public info of the user.
    Medical Me = new Medical();     //The Medical info of the user.
    Financial Fi = new Financial();   //The Financial info of the user.
    Criminal Cr = new Criminal();    //The Criminal History of the user.
    Social So = new Social();      //The Social info of the user.
    
    public Personal getPersonalInfo()
    {
        return Pe;
    }
    public Public getPublicInfo()
    {
        return Pu;
    }
    public Medical getMedicalInfo()
    {
        return Me;
    }
    public Financial getFinancialInfo()
    {
        return Fi;
    }
    public Criminal getCriminalInfo()
    {
        return Cr;
    }
    public Social getSocialInfo()
    {
        return So;
    }
    
    public boolean insert(long ID)
    {
        boolean test = true;
        test &= Pe.insert(ID);
        test &= Pu.insert(ID);
        test &= Me.insert(ID);
        test &= Cr.insert(ID);
        test &= Fi.insert(ID);
        test &= So.insert(ID);
        return test;
    }
    
    public boolean init(long ID)
    {
        boolean test = true;
        test &= Pe.init(ID);
        test &= Pu.init(ID);
        test &= Me.init(ID);
        test &= Cr.init(ID);
        test &= Fi.init(ID);
        test &= So.init(ID);
        return test;
    }
    
    Data[] getAll()
    {
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    Statement s = null;
	    ArrayList<Long> IDs = new ArrayList<Long>();
	    try
	    {
	        s = c.createStatement();
	        ResultSet rs = s.executeQuery("SELECT INFO_ID FROM LOGIN_INFO WHERE INFO_ID > 5;");
	        
	        if (rs.first())
	            do
                {
	                IDs.add(Long.valueOf(rs.getLong(1)));
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
            }
	    }
        
        Long[] list = (Long[])IDs.toArray();
        Data[] d = new Data[list.length];
        for (int i = 0; i < list.length; i++)
        {
            if( !( d[i].init( list[i] ) ) )
                return null;
        }
        return d;
    }
}
