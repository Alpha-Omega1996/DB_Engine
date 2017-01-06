package DB.Types.Connect;

import java.sql.*;

public final class Link
{
    private String ip, port, usr, pwd;
    Connection c;
    public Link()
    {
    	this("127.0.0.1","root","root","3306");
    }
    public Link(String server_ip)
    {
    	this(server_ip,"root","root","3306");
    }
    public Link(String user, String passkey)
    {
        this("127.0.0.1",user,passkey,"3306");
    }
    public Link(String server_ip,String user,String passkey)
    {
    	this(server_ip,user,passkey,"3306");
    }
    public Link(String server_ip,String user,String passkey,String port)
    {
	    ip = server_ip;
	    this.port = port;
	    usr = user;
	    pwd = passkey;
	    connect();
    }
    public Connection getConnection()
    {
    	return this.c;
    }
    public void disconnect()
    {
	    ip = port = usr = pwd = null;
	    try {
	
	        c.close();
    	
	    } catch (SQLException e) {
	        System.err.println("Error while closing Connection.");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
            System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
	    }
    }
    private void connect()
    {
        Statement s = null;
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            
        } catch (Exception e) {
            System.err.println("Drastic Bug!! Did you forget using the classpath switch?");
            System.exit(1);
        }
        try {
        	c = DriverManager.getConnection("jdbc:mysql://"
        	                                + "address=(protocol=tcp)"
        	                                + "(host=" + ip + ")"
        	                                + "(port=" + port+")/"
						                    + "?autoReconnect=true&useSSL=no",
        	                                usr,
        	                                pwd);
        } catch (SQLException e) {
            System.err.println("Error gaining access to database. Please check the connection details!");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
            System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        }
        try {
    	
    	    s = c.createStatement();
    	
    	} catch (SQLException e) {
            System.err.println("Error while accessing Database: Connection is not open.");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
            System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        }
        try {
        
    	    s.executeUpdate("CREATE DATABASE IF NOT EXISTS T2B_APP_DB;");
    	    s.close();
    	    c.setCatalog("T2B_APP_DB");
    	    
    	} catch (SQLException e) {
            System.err.println("Error accessing Database!");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
            System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        }
        try {
        
    	    s = c.createStatement();
    	    
    	} catch (SQLException e) {
            System.err.println("Error while accessing Database: Connection is not open.");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
            System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        }
        buildTest(s);
	    try {

	        s.close();

	    } catch (SQLException e) {
            System.err.println("Error closing Statement!");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
	        System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        }
    }
    private void buildTest(Statement s)
    {
        try {
                            //Login Info.
	        s.addBatch("CREATE TABLE IF NOT EXISTS LOGIN_INFO"
		            + " (INFO_ID BIGINT NOT NULL PRIMARY KEY,"
		            + " USERNAME TEXT NOT NULL,"
		            + " PASSKEY TEXT NOT NULL,"
		            + " QUESTION TEXT,"
		            + " ANSWER TEXT"
		            + " )ENGINE=INNODB;");
		            
	        s.executeBatch();
	        s.clearBatch();
	    } catch (SQLException e) {
            System.err.println("Error accessing Database while testing Login Info!");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
	        System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        }		    

        try {
                        //Basic + Extended Info.
	        s.addBatch("CREATE TABLE IF NOT EXISTS PERSONAL_INFO"
		            + " (ID BIGINT NOT NULL PRIMARY KEY,"
		            + " FNAME TEXT NOT NULL,"
		            + " LNAME TEXT NOT NULL,"
		            + " MNAME TEXT,"
		            + " AGE INT(3) NOT NULL,"
		            + " GENDER CHAR(1) NOT NULL,"
		            + " HEIGHT FLOAT,"
		            + " WEIGHT FLOAT,"
		            + " FOREIGN KEY(ID)"
		            + " REFERENCES LOGIN_INFO(INFO_ID)"
		            + " ON DELETE CASCADE"
		            + " ON UPDATE CASCADE"
		            + " )ENGINE=INNODB;");
		            
	        s.addBatch("CREATE TABLE IF NOT EXISTS OTHER_INFO"
		            + " (ID BIGINT NOT NULL PRIMARY KEY,"
		            + " PHOTO BLOB,"
		            + " MARITAL_STATUS INT(1) NOT NULL,"
		            + " MOBILE_NO TEXT,"
		            + " DRIVING_LIC_NO TEXT,"
		            + " HANDICAPPED CHAR(1) NOT NULL,"
		            + " VEHICLES INT NOT NULL,"
		            + " FOREIGN KEY(ID)"
		            + " REFERENCES PERSONAL_INFO(ID)"
		            + " ON DELETE CASCADE"
		            + " ON UPDATE CASCADE"
		            + " )ENGINE=INNODB;");
		            
	        s.addBatch("CREATE TABLE IF NOT EXISTS PUBLIC_INFO"
		            + " (ID BIGINT NOT NULL PRIMARY KEY,"
		            + " MOTHER_NAME TEXT,"
		            + " FATHER_NAME TEXT,"
		            + " ADDRESS TEXT NOT NULL,"
		            + " CITY TEXT NOT NULL,"
		            + " STATE TEXT NOT NULL,"
		            + " COUNTRY TEXT NOT NULL,"
		            + " FOREIGN KEY(ID)"
		            + " REFERENCES OTHER_INFO(ID)"
		            + " ON DELETE CASCADE"
		            + " ON UPDATE CASCADE"
		            + " )ENGINE=INNODB;");
		            
	        s.executeBatch();
	        s.clearBatch();
	    } catch (SQLException e) {
            System.err.println("Error accessing Database while testing Basic + Extended Info!");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
	        System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        }		    
	    try {
		            //Medical Info
	        s.addBatch("CREATE TABLE IF NOT EXISTS MEDICAL_INFO"
		            + " (ID BIGINT NOT NULL PRIMARY KEY,"
	                + " BLOOD_GRP VARCHAR(3) NOT NULL,"
	                + " INSURANCE_NO TEXT,"
	                + " FOREIGN KEY(ID)"
		            + " REFERENCES PERSONAL_INFO(ID)"
		            + " ON DELETE CASCADE"
		            + " ON UPDATE CASCADE"
		            + " )ENGINE=INNODB;");

	        s.addBatch("CREATE TABLE IF NOT EXISTS ILLNESS_TYPES"
		            + " (DISEASE_ID INT NOT NULL PRIMARY KEY,"
		            + " DISEASE_NAME TEXT NOT NULL"
		            + " )ENGINE=INNODB;");
		        
	        s.addBatch("CREATE TABLE IF NOT EXISTS ILLNESS_RELATION"
		            + " (R_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
		            + " ID BIGINT NOT NULL,"
		            + " DISEASE_ID INT NOT NULL,"
		            + " OTHER TEXT,"
	                + " FOREIGN KEY(ID)"
		            + " REFERENCES MEDICAL_INFO(ID)"
	                + " ON DELETE CASCADE"
	                + " ON UPDATE CASCADE"
		            + " )ENGINE=INNODB;");
		        
	        s.executeBatch();
	        s.clearBatch();
	    } catch (SQLException e) {
            System.err.println("Error accessing Database while testing Medical Info!");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
	        System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        } try {
		        //Criminal History
	        s.addBatch("CREATE TABLE IF NOT EXISTS CRIMINAL_RECORD"
        		    + " (ID BIGINT NOT NULL PRIMARY KEY,"
        		    + " FIR_FILED CHAR(1) NOT NULL,"
        		    + " IPC_SECTIONS TEXT,"
        		    + " FINGERPRINT BLOB,"
        		    + " FOREIGN KEY(ID)"
        		    + " REFERENCES PERSONAL_INFO(ID)"
        		    + " ON DELETE CASCADE"
        		    + " ON UPDATE CASCADE"
        		    + " )ENGINE=INNODB;");
        		
        	    s.executeBatch();
        	    s.clearBatch();
        } catch (SQLException e) {
            System.err.println("Error accessing Database while testing Criminal History Info!");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
	        System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        } try {	
        		        //Bank Details
        	s.addBatch("CREATE TABLE IF NOT EXISTS EARNINGS"
        		    + " (ID BIGINT NOT NULL PRIMARY KEY,"
        		    + " EARNING_METHOD INT(1) NOT NULL,"
        		    + " FOREIGN KEY(ID)"
        		    + " REFERENCES PERSONAL_INFO(ID)"
        		    + " ON DELETE CASCADE"
        		    + " ON UPDATE CASCADE"
        		    + " )ENGINE=INNODB;");

        	s.addBatch("CREATE TABLE IF NOT EXISTS BANK_TYPES"
        		    + " (BANK_ID INT NOT NULL PRIMARY KEY,"
        		    + " BANK_NAME TEXT NOT NULL"
        		    + " )ENGINE=INNODB;");

        	s.addBatch("CREATE TABLE IF NOT EXISTS ACCOUNTS"
        		    + " (R_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
        		    + " ID BIGINT NOT NULL,"
        		    + " BANK_ID INT NOT NULL,"
        		    + " OTHER TEXT,"
        		    + " FOREIGN KEY(ID)"
        		    + " REFERENCES PERSONAL_INFO(ID)"
        		    + " ON DELETE CASCADE"
        		    + " ON UPDATE CASCADE"
        		    + " )ENGINE=INNODB;");

        	s.executeBatch();
        	s.clearBatch();
        } catch (SQLException e) {
            System.err.println("Error accessing Database while testing Earnings Info!");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
	        System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        }
	    try {
        		        //Social Contributions
        	s.addBatch("CREATE TABLE IF NOT EXISTS MISC_INFO"
        		    + " (ID BIGINT NOT NULL PRIMARY KEY,"
        		    + " Q1 CHAR(1),"
        		    + " Q2 CHAR(1),"
        		    + " Q3 CHAR(1),"
        		    + " Q4 CHAR(1),"
        		    + " OTHER TEXT,"
        		    + " FOREIGN KEY(ID)"
        		    + " REFERENCES PERSONAL_INFO(ID)"
        		    + " ON DELETE CASCADE"
        		    + " ON UPDATE CASCADE"
        		    + " )ENGINE=INNODB;");
        		
        	s.executeBatch();
        	s.clearBatch();
    		
        } catch (SQLException e) {
            System.err.println("Error accessing Database while testing Misc. Info!");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
	        System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        }
	    try {
        		        //Social Contributions
        	s.addBatch("INSERT INTO BANK_TYPES VALUES(0,'Other')"
        	        + " ON DUPLICATE KEY UPDATE BANK_ID = BANK_ID+0;");
        	s.addBatch("INSERT INTO ILLNESS_TYPES VALUES(0,'Other')"
        	        + " ON DUPLICATE KEY UPDATE DISEASE_ID = DISEASE_ID+0;");
        	s.executeBatch();
        	s.clearBatch();
    		
        } catch (SQLException e) {
            System.err.println("Error configuring Bank Types and Illness Types!");
            System.err.println("Error Code: \t"+e.getErrorCode());
            System.err.println("Error Info: \t"+e.getSQLState());
	        System.err.println("Error Description: "+e.getMessage());
            System.exit(1);
        }

    }
}
