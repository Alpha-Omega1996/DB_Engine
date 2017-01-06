package DB;

import DB.Types.*;

public class Record
{
    public static final int VALID = 0;
    public static final int INVALID = -1;
    public static final int INCORRECT = +1;
    Meta m;
    long ID;
    
    public Record()
    {
        m = new Meta();
        ID = 0L;
    }

    public Data[] readAll()
    {
        return m.readAll();
    }
    
    public Meta getMeta()
    {
        return m;
    }
    
    public boolean insert()
    {
        ID = m.getID();
        Data d = m.getData();
        boolean test = true;
        test &= m.insert();
        test &= d.insert(ID);
        return test;
    }
    
    public int read(String Username, String Password)
    {
        Credentials c = m.getCredentials();
        boolean test = true;
        int status = Record.INVALID;
        test &= c.setUsername(Username);
        if (!test)
        {
            status = Record.INCORRECT;
            test = c.setUsername(Username, true);
        }
        else
            return status;
        c.setPassword(Password);
        
        test &= m.init();
        if (!test)
            return Record.INVALID;
        else
        {
            c.login(Password);
            Data d = m.getData();
            d.init(m.getID());
            return Record.VALID;
        }
    }
    
    public boolean delete()
    {
        return m.delete();
    }
    
    public boolean update(boolean now)
    {
        if (now)
            return insert();
        else
            return delete();
    }
}
