package DB.Types;

public class Meta
{
    Credentials Cr = new Credentials();   //The credentials of the user.
    Data Info = new Data();     //The info of the user, can be null for Admin.
    
    public Credentials getCredentials()
    {
        return Cr;
    }
    
    public Data getData()
    {
        return Info;
    }
    
    public boolean init()
    {
        return Cr.init() & Info.init(Cr.getID());
    }
    
    public boolean insert()
    {
        return Cr.insert();
    }
    
    public boolean isAdmin()
    {
        long ID = getID();
        return (1 < ID && ID < 6);
    }
    
    public Data[] readAll()
    {
        long ID = getID();
        if(0 < ID && ID < 6)
        {
            return Info.getAll();
        }
        else
        {
            return null;
        }
    }
    
    public long getID()
    {
        return Cr.getID();
    }
    
    public boolean delete()
    {
        return Cr.delete();
    }
}
