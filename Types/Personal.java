package DB.Types;

import DB.Types.Connect.Link;
import java.io.*;
import java.sql.*;

public class Personal
{
	String Fname;
	String Lname;
	String Mname;
	int Age;
	public static enum Gender
	{
	   MALE,FEMALE
	}
	Gender gender;
	float height;
	float weight;
	String Photo_file;
	int Marital_Status;
	String Mobile;
	String Driving_License;
	boolean Handicapped;
	int Vehicles;

	Personal()
	{
	    Fname = new String();
	    Lname = new String();
	    Mname = new String();
	    Age = Marital_Status = Vehicles = 0;
	    height = weight = 0.0f;
	    Photo_file = new String();
	    Mobile = new String();
	    Driving_License = new String();
	    Handicapped = false;
	}
	
	public void setFname(String fn)
	{
	    Fname = fn;
	}
	public void setLname(String ln)
	{
	    Lname = ln;
	}
	public void setMname(String mn)
	{
	    Mname = mn;
	}
	public void setAge(int a)
	{
	    Age = a;
	}
	public void setGender(Personal.Gender g)
	{
	    gender = g;
	}
	public boolean setHeight(float h)
	{
	    int ft = (int)h;
	    int inch = (int)((h - ft)*100);
	    if ( (0 < ft) && !(inch < 0 || inch > 11) )
	    {
	        height = h;
	        return true;
	    }
	    else
	        return false;
	}
	public boolean setWeight(float w)
	{
	    int kg = (int)w;
	    int gm = (int)((w - kg)*1000);
	    if ( (0 < kg) && !(gm < 0 || gm > 1000) )
	    {
	        weight = w;
	        return true;
	    }
	    else
	        return false;
	}
	public void setPhoto(String pfile)
	{
	    Photo_file = pfile;
	}
	public boolean setMaritalStatus(int ms)
	{
	    switch(ms)
	    {
	        case 1:
	        case 2:
	        case 3:
	        case 4:
	        case 5:
	            Marital_Status = ms;
	            return true;
	        default:
	            return false;
	    }
	}
	public void setMobileNo(String mob)
	{
	    Mobile = mob;
	}
	public void setDrivingLicenseNo(String dln)
	{
	    Driving_License = dln;
	}
	public void setHandicapped(boolean h)
	{
	    Handicapped = h;
	}
	public boolean setVehicles(int v)
	{
	    if (v > 0)
	    {
	        Vehicles = v;
	        return true;
	    }
	    else
	        return false;
	}
	
	boolean insert(long ID)
	{
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    PreparedStatement s = null;
        int res = 0;
        try
        {
            s = c.prepareStatement("INSERT INTO PERSONAL_INFO"
                                + " VALUES (?,?,?,?,?,?,?,?);");
            s.setLong(1,ID);
            
            s.setString(2,Fname);
            
            s.setString(3,Lname);
            
            if (Mname != null && Mname.length() != 0)
                s.setString(4,Mname);
            else
                s.setNull(4,Types.NULL);
            
            s.setInt(5,Age);
            
            if (gender == Personal.Gender.MALE)
                s.setString(6,"M");
            else
                s.setString(6,"F");
            
            if (height != 0)
                s.setFloat(7,height);
            else
                s.setNull(7,Types.NULL);
            
            if (weight != 0)
                s.setFloat(8,weight);
            else
                s.setNull(8,Types.NULL);
            
            res = s.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Cannot Insert Personal Info");
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
            s = c.prepareStatement("INSERT INTO OTHER_INFO"
                                + " VALUES (?,?,?,?,?,?,?);");
            s.setLong(1,ID);
            
            if (Photo_file.length() == 0)
                s.setNull(2,Types.NULL);
            else
                try {
                    FileInputStream fin = new FileInputStream
                                         ( new File(Photo_file) );
                    s.setBinaryStream(2,fin,fin.available());
                } catch(IOException e) {
                    s.setNull(2,Types.NULL);
                }

            
            s.setInt(3,Marital_Status);
            
            if (Mobile.length() != 0)
                s.setString(4,Mobile);
            else
                s.setNull(4,Types.NULL);
            
            if (Driving_License.length() != 0)
                s.setString(5,Driving_License);
            else
                s.setNull(5,Types.NULL);
            
            if (Handicapped)
                s.setString(6,"Y");
            else
                s.setString(6,"N");
            
            s.setInt(7,Vehicles);
            
            res = s.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Cannot Insert Other Info");
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
    
    public String getFname()
	{
	    return Fname;
	}
	public String getLname()
	{
	    return Lname;
	}
	public String getMname()
	{
	    if (Mname == null || Mname.length() == 0)
	        return null;
	    else
	        return Mname;
	}
	public int getAge()
	{
	    return Age;
	}
	public String getGender()
	{
	    if (gender == Personal.Gender.MALE)
	        return "M";
	    else
	        return "F";
	}
	public float getHeight()
	{
	    return height;
	}
	public float getWeight()
	{
	    return weight;
	}
	public String getPhoto()
	{
	    return Photo_file;
	}
	public int getMaritalStatus()
	{
	    return Marital_Status;
	}
	public String getMobileNo()
	{
	    return Mobile;
	}
	public String getDrivingLicenseNo()
	{
	    return Driving_License;
	}
	public boolean isHandicapped()
	{
	    return Handicapped;
	}
	public int getVehicles()
	{
	    return Vehicles;
	}
	
	boolean init(long ID)
	{
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    Statement s = null;
        try
        {
            s = c.createStatement();
            
            ResultSet rs = s.executeQuery("SELECT * FROM PERSONAL_INFO;");
            
            if (rs.first())
                do
                {
                    if (rs.getLong(1) == ID)
                    {
                        Fname = rs.getString(2);
                        Lname = rs.getString(3);
                        Mname = rs.getString(4);
                        Age = rs.getInt(5);
                        gender = (rs.getString(6).charAt(0) == 'M') ?
                                    Personal.Gender.MALE : 
                                    Personal.Gender.FEMALE ;
                        height = rs.getFloat(7);
                        weight = rs.getFloat(8);
                        break;
                    }
                }while(rs.next());
                rs.close();

        } catch (SQLException e) {
            System.out.println("Cannot Access Personal Info");
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
	    }
	    
	    s = null;
        try
        {
            s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM OTHER_INFO;");
            
            if (rs.first())
                do
                {
                    if (rs.getLong(1) == ID)
                    {
                        InputStream fin = rs.getBinaryStream(2);
                        if (fin != null)
                        {
                            try
                            {
                                OutputStream fout = new FileOutputStream( new File(ID+".jpg") );
                                int i = 0;
                                while ( (i = fin.read() ) > -1) fout.write(i);
                                fin.close();
                                fout.close();
                            } catch(IOException e) {
                                ;//Not expecting this error
                            }
                            Photo_file = ID+".jpg";
                        }
                        else
                            Photo_file = null;
                        
                        Marital_Status = rs.getInt(3);
                        Mobile = rs.getString(4);
                        Driving_License = rs.getString(5);
                        Handicapped = (rs.getString(6).charAt(0) == 'Y') ? true : false;
                        Vehicles = rs.getInt(7);
                        break;
                    }
                }while(rs.next());
                rs.close();

        } catch (SQLException e) {
            System.out.println("Cannot Access Other Info");
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
