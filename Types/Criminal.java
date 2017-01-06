package DB.Types;

import DB.Types.Connect.Link;
import java.sql.*;
import java.io.*;

public class Criminal
{
    boolean FIR;
    String[] IPC;
	String Fingerprint_file;

	Criminal()
	{
	    IPC = new String[1];
	    Fingerprint_file = new String();
	}
	
	public void setFIR(boolean f)
	{
	    FIR = f;
	}
	
	public void setIPC(String[] ipc)
	{
	    IPC = ipc;
	}
	
	public void setFingerprint(String ff)
	{
	    Fingerprint_file = ff;
	}
	
	boolean insert(long ID)
	{
	    Link l = new Link("AO","A.O's~DB2");
	    Connection c = l.getConnection();
	    PreparedStatement s = null;
        int res = 0;
        try
        {
            s = c.prepareStatement("INSERT INTO CRIMINAL_RECORD"
                                + " VALUES (?,?,?,?);");
            s.setLong(1,ID);
            
            s.setString(2,(FIR)?"Y":"N");
            
            if (IPC.length != 0)
            {
                StringBuffer sb = new StringBuffer();
                String p = "";
                for (String str : IPC)
                {
                    sb.append(p);
                    p = ",";
                    sb.append(str);
                }
                s.setString(3,sb.toString());
            }
            else
                s.setNull(3,Types.NULL);
            
            if (Fingerprint_file.length() == 0)
                s.setNull(4,Types.NULL);
            else
                try {
                    FileInputStream fin = new FileInputStream
                                         (new File(Fingerprint_file));
                    s.setBinaryStream(2,fin,fin.available());
                } catch(IOException e) {
                    s.setNull(2,Types.NULL);
                }
            
            res = s.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Cannot Insert Criminal History");
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

	public boolean FIRfiled()
	{
	    return FIR;
	}
	
	public String[] getIPC()
	{
	    return IPC;
	}
	
	public String getFingerprint()
	{
	    return Fingerprint_file;
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
                                        + " CRIMINAL_RECORD;");
            
            if(rs.first())
                do
                {
                    if (rs.getLong(1) == ID)
                    {
                        FIR = (rs.getString(2).charAt(0)) == 'Y';
                        IPC = rs.getString(3).split(",");
                        InputStream fin = rs.getBinaryStream(4);
                        if (fin != null)
                        {
                            try
                            {
                                OutputStream fout = new FileOutputStream( new File("FP"+ID+".jpg") );
                                int i = 0;
                                while ( (i = fin.read() ) > -1) fout.write(i);
                                fin.close();
                                fout.close();
                            } catch(IOException e) {
                                ;//Not expecting this error
                            }
                            Fingerprint_file = "FP"+ID+".jpg";
                        }
                        else
                            Fingerprint_file = null;
                        break;
                    }
                }while(rs.next());
        } catch (SQLException e) {
            System.out.println("Cannot Access Criminal History");
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
