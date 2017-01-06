package DB.Types;

import DB.Types.Connect.Link;
import java.util.Calendar;
import java.sql.*;

public class Credentials
{
    public static final int ADMIN = -1;
    public static final int USER = +1;    
    
    private int ID;             //ID inside this table only
    private long Info_ID;       //ID to Personal Info table. If between 1 and 5 (inclusive), account type is Admin
    
    //Basic Credentials
    String Username;
    String Passkey;
    
    //If User forgets his/her Password
    String Question;        //Security Question
    String Answer;          //Answer to it
    
    //Blocks access to password
    private boolean lock = false;
    private boolean logged_in = false;
    
    Credentials()
    {
	    Info_ID = ID = -1;
	    Username = new String();
	    Passkey = new String();
	    Question = new String();
	    Answer = new String();
	    lock = false;
	    logged_in = true;
    }
    
    public boolean setUserType(int t)
    {
        switch(t)
        {
            case Credentials.ADMIN:
                Info_ID = -1;
                break;
            case Credentials.USER:
                Info_ID = 0;
                break;
            default:
                return false;
        }
        
        if (Info_ID == 0)
        {
            Calendar c = Calendar.getInstance();
            Info_ID = c.get(Calendar.YEAR);
            Info_ID = 100*Info_ID + c.get(Calendar.MONTH)+1;
            Info_ID = 100*Info_ID + c.get(Calendar.DAY_OF_MONTH);
            Info_ID = 100*Info_ID + c.get(Calendar.HOUR_OF_DAY);
            Info_ID = 100*Info_ID + c.get(Calendar.MINUTE);
            Info_ID = 100*Info_ID + c.get(Calendar.SECOND);
            return true;
        }
        else
        {
            Link l = new Link("AO","A.O's~DB2");
            Connection c = l.getConnection();
            long i = -1;
            int count = 0;
            try
            {
                Statement s = c.createStatement();
                ResultSet r = s.executeQuery("SELECT INFO_ID FROM LOGIN_INFO;");
                
                if (r.first())                  //If there is something in the database.
                    do
                    {
                        i = r.getLong(1);
                        if (i < 6 && i > 0)
                            count++;
                    }while(r.next());
                    
            } catch (SQLException e) {
                System.out.println("Cannot set "+Username+"as Admin.");
                System.out.println("Error Code: "+e.getErrorCode());
                System.out.println("Error Info: "+e.getSQLState());
                System.out.println("Error Description: "+e.getMessage());
                i = count = -1;
            }
            if (count == 0 || count < 6)    //If all 5 admins have been not added or there are no admins.
                i = (int)(Info_ID = (count==0)?1:count);
            else                            //If there are 5 admins already.
                i = -1;
            l.disconnect();
            
            return (i == -1)?false:true;
        }
    }
    
    
    public boolean setUsername (String u)
    {
        return this.setUsername(u,false);
    }
    
    public boolean setUsername(String u, boolean direct)
    {
        if (!direct)
        {
            Link l = new Link("AO","A.O's~DB2");
            Connection c = l.getConnection();
            if (logged_in)
            {
                try
                {
                    Statement s = c.createStatement();
                    ResultSet rs = s.executeQuery("SELECT USERNAME FROM LOGIN_INFO;");
                    
                    if (rs.first())
                        do
                        {
                            if (rs.getString(1).compareTo(u) == 0)
                            {
                                l.disconnect();
                                return false;
                            }
                        }while(rs.next());
                } catch (SQLException e) {
                    System.out.println("Connection to DB has failed.");
                    System.out.println("Error Code: "+e.getErrorCode());
                    System.out.println("Error Info: "+e.getSQLState());
                    System.out.println("Error Description: "+e.getMessage());
                }
                Username = u;
                l.disconnect();
                return true;
            }
            l.disconnect();
            return false;
        }
        else
        {
            Username = u;
            return true;
        }
    }
    
    public void setPassword(String p)
    {
        if (logged_in)
        {
            lock = true;
            Passkey = p;
        }
    }
    
    public void setQuestion(String q)
    {
        if (logged_in)
            Question = q;
    }
    
    public void setAnswer(String a)
    {
        if (logged_in)
            Answer = a;
    }

    

    boolean insert()
    {
        Link l = new Link("AO","A.O's~DB2");
        Connection c = l.getConnection();
        PreparedStatement s = null;
        int res = 0;
        try
        {
            s = c.prepareStatement("INSERT INTO LOGIN_INFO"
                                + " VALUES (?,?,?,?,?);");
            s.setLong(1,Info_ID);
            s.setString(2,Username);
            s.setString(3,Passkey);
            if (Question.length() != 0)
                s.setString(4,Question);
            else
                s.setNull(4,Types.NULL);
            if (Answer.length() != 0)
                s.setString(5,Answer);
            else
                s.setNull(5,Types.NULL);
            res = s.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Cannot Insert Account of "+Username);
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
            if (res != 0)
            {
                lock = true;
                logged_in = false;
                return true;
            }
            else
                return false;
        }
    }
    
    public String getUsername()
    {
        return Username;
    }
    
    String getPassword()
    {
        if (!lock)
            lock = true;
        else
            return null;
        return Passkey;
    }
    
    public String getQuestion()
    {
        return Question;
    }
    
    String getAnswer()
    {
        return Answer;
    }
    
    public boolean login(String p)
    {
        if (p.compareTo(Passkey) == 0)
            return logged_in = true;
        else
            return logged_in = false;
    }
    
    public boolean verify(String a)
    {
        if (a.compareTo(Answer) == 0)
            return lock = false;
        else
            return false;
    }
    
    long getID()
    {
        return Info_ID;
    }
    
    void unlock (boolean v)
    {
        logged_in = true;
    }
    
    public boolean unlock(Credentials t)
    {
        if (this.Info_ID > 0 && this.Info_ID < 6)
            t.unlock(true);
        else
            return false;
        return true;
    }
    
    boolean init()
    {
        Link l = new Link("AO","A.O's~DB2");
        Connection c = l.getConnection();
        boolean set = false;
        Statement s = null;
        try
        {
            s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM LOGIN_INFO;");
            if(rs.first())
                do
                {
                    if (( rs.getString(2).compareTo(Username) == 0)
                        && (rs.getString(3).compareTo(Passkey) == 0))
                    {
                        Info_ID = rs.getLong(1);
                        Question = rs.getString(4);
                        Answer = rs.getString(5);
                        set = true;
                        break;
                    }
                }while(rs.next());
            else if (!set)
                return false;
            else
                return false;
        } catch (SQLException e) {
            System.out.println("Cannot Access Credentials from DB");
            System.out.println("Error Code: "+e.getErrorCode());
            System.out.println("Error Info: "+e.getSQLState());
            System.out.println("Error Description: "+e.getMessage());
            return false;
        } finally {
            try
            {
                if(s != null)
                    s.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement!");
                System.out.println("Error Code: "+e.getErrorCode());
                System.out.println("Error Info: "+e.getSQLState());
                System.out.println("Error Description: "+e.getMessage());
                return false;
            }
            l.disconnect();
        }
        return true;
    }
    
    public boolean delete()
    {
        Link l = new Link();
        Connection c = l.getConnection();
        Statement s = null;
        try
        {
            s = c.createStatement();
            int res = s.executeUpdate("DELETE FROM LOGIN_INFO WHERE INFO_ID = "+Info_ID);
        } catch (SQLException e) {
            System.out.println("Cannot Access Credentials from DB");
            System.out.println("Error Code: "+e.getErrorCode());
            System.out.println("Error Info: "+e.getSQLState());
            System.out.println("Error Description: "+e.getMessage());
            return false;
        } finally {
            try
            {
                if(s != null)
                    s.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement!");
                System.out.println("Error Code: "+e.getErrorCode());
                System.out.println("Error Info: "+e.getSQLState());
                System.out.println("Error Description: "+e.getMessage());
                return false;
            }
            l.disconnect();
        }
        return true;
    }
}
