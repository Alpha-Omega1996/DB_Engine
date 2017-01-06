package DB.Types;

import DB.Types.Connect.Link;
import java.sql.*;

public class Medical
{
    public static enum BloodGroup
	{
	    A,B,AB,O,A$,B$,AB$,O$
	}
	BloodGroup bg;
	String Insurance;
	String[] Diseases;

	Medical()
	{
	    Insurance = new String();
	    Diseases = new String[1];
	}
	
	public void setBloodGroup(Medical.BloodGroup b)
	{
	    bg = b;
	}
	
	public void setInsurance(String i)
	{
	    Insurance = i;
	}
	
	public void setDiseases(String[] d)
	{
	    Diseases = d;
	}
	
	boolean insert(long ID)
	{
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    PreparedStatement s = null;
        int res = 0;
        try
        {
            s = c.prepareStatement("INSERT INTO MEDICAL_INFO"
                                + " VALUES (?,?,?);");
            s.setLong(1,ID);
            
            switch (bg)
            {
                case A:
                    s.setString(2,"A+");
                    break;
                case A$:
                    s.setString(2,"A-");
                    break;
                case B:
                    s.setString(2,"B+");
                    break;
                case B$:
                    s.setString(2,"B-");
                    break;
                case AB:
                    s.setString(2,"AB+");
                    break;
                case AB$:
                    s.setString(2,"AB-");
                    break;
                case O:
                    s.setString(2,"O+");
                    break;
                case O$:
                    s.setString(2,"O-");
                    break;
            }
            
            if (Insurance.length() != 0)
                s.setString(3,Insurance);
            else
                s.setNull(3,Types.NULL);
            
            res = s.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Cannot Insert Medical Info");
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
	    }
	    s = null;
        res = 0;
        try
        {
            s = c.prepareStatement("INSERT INTO ILLNESS_RELATION"
                                + "(ID, DISEASE_ID, OTHER)"
                                + " VALUES (?,?,?);");
            s.setLong(1,ID);
            
            Statement s2 = c.createStatement();
            boolean set = false;
            if (Diseases == null)
            {
                s.setInt(2,0);
                s.setNull(3,Types.NULL);
                res = s.executeUpdate();
            }
            else
                for (String d : Diseases)
                {
                    ResultSet rs = s2.executeQuery("SELECT * FROM"
                                        + " ILLNESS_TYPES;");
                    if (rs.first())
                        do
                        {
                            if (rs.getString(2).compareTo(d) == 0)
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
                        s.setString(3,d);
                        set = false;
                    }
                    res = s.executeUpdate();
                    s.setLong(1,ID);
                    rs.close();
                }
            s2.close();
        } catch (SQLException e) {
            System.out.println("Cannot Insert Medical Details");
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
	    }
	    return true;
	}

	public Medical.BloodGroup getBloodGroup()
	{
	    return bg;
	}
	
	public String getInsuranceNo()
	{
	    return Insurance;
	}
	
	public String[] getDiseases()
	{
	    return Diseases;
	}

    boolean init(long ID)
    {
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    Statement s = null;
        try
        {
            s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM"
                                        + " MEDICAL_INFO;");
            
            if (rs.first())
                do
                {
                    if (rs.getLong(1) == ID)
                    {
                        switch(rs.getString(2))
                        {
                            case "A+":
                                bg = Medical.BloodGroup.A;
                                break;
                            case "A-":
                                bg = Medical.BloodGroup.A$;
                                break;
                            case "B+":
                                bg = Medical.BloodGroup.B;
                                break;
                            case "B-":
                                bg = Medical.BloodGroup.B$;
                                break;
                            case "AB+":
                                bg = Medical.BloodGroup.AB;
                                break;
                            case "AB-":
                                bg = Medical.BloodGroup.AB$;
                                break;
                            case "O+":
                                bg = Medical.BloodGroup.O;
                                break;
                            case "O-":
                                bg = Medical.BloodGroup.O$;
                                break;
                        }
                        
                        Insurance = rs.getString(3);
                        break;
                    }
                }while(rs.next());
            rs = s.executeQuery("SELECT A.ID,A.OTHER,B.DISEASE_NAME"
                            + " FROM ILLNESS_RELATION A,"
                            + " ILLNESS_TYPES B WHERE"
                            + " A.DISEASE_ID = B.DISEASE_ID;");
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
            Diseases = sb.toString().split(",");
        } catch (SQLException e) {
            System.out.println("Cannot Access Medical Info");
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
