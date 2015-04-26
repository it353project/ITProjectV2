/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controller.SignUpController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.UserBean;

/**
 *
 * @author it3530219
 */
public class UserDAOImpl implements UserDAO {

    //used in signUp Controller, for adding new account details to db

    @Override
    public int createAccount(UserBean aSignUp) {

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        int rowCount = 0;
        int rowCountLogin = 0;
        try {
            String myDB = "jdbc:derby://localhost:1527/IT353";
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");

            String accountInsertString;
            Statement stmt = DBConn.createStatement();
            
            int nextAccountID = findNextSequenceValue("ACCOUNT_ID_SEQ");
            accountInsertString = "INSERT INTO IT353.ACCOUNT VALUES (" + nextAccountID + ", '"
                    + aSignUp.getFirstName()
                    + "','" + aSignUp.getLastName()
                    + "','" + aSignUp.getPassword()
                    + "','" + aSignUp.getAccountType()
                    + "', default, '"
                    + aSignUp.getEmail()
                    + "','" + aSignUp.getSecurityQuestion()
                    + "','" + aSignUp.getSecurityAnswer()
                    + "','" + aSignUp.getUserName()
                    + "')";

            rowCount = stmt.executeUpdate(accountInsertString);
            System.out.println("insert string =" + accountInsertString);
            System.out.println(rowCount + " row(s) inserted");
            System.out.println("Account Table insert completed");
            
            int nextRequestID = findNextSequenceValue("REQUEST_ID_SEQ");
            String requestInsertString = "INSERT INTO IT353.REQUEST VALUES (" + nextRequestID + ", "
                    + nextAccountID
                    + ",'" + aSignUp.getAccountJustification()
                    + "')";

            rowCount = stmt.executeUpdate(requestInsertString);
            System.out.println("insert string =" + requestInsertString);
            System.out.println(rowCount + " row(s) inserted");
            System.out.println("Request Table insert completed");
            
            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert in createAccount()");
            System.err.println(e.getMessage());
        }
        return rowCount;
    }
    
    private int findNextSequenceValue(String sequenceName) {

        sequenceName = sequenceName.trim().toUpperCase();
        System.out.println("sequence name = " + sequenceName);

        int nextValue = 0;

        String retrieveIDQuery = "VALUES NEXT VALUE FOR " + sequenceName;

        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
//            DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String myDB = "jdbc:derby://localhost:1527/IT353";
//            String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();

            ResultSet rs = stmt.executeQuery(retrieveIDQuery);
            while (rs.next()) {
                nextValue = rs.getInt(1);
            }
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findNextSequenceValue()");
            System.err.println(e.getMessage());
        }
        System.out.println("next value = " + nextValue);
        return nextValue;
    }

    //used in SignUpController for checking if the user name is already registered in the system.
    //also used in loginController to check if the username is already in the system for account recovery
    @Override
    public int checkUserName(String userName) {
        String query = "SELECT COUNT(*) AS USERCOUNT FROM IT353.ACCOUNT ";
        query += "WHERE ULID  = '" + userName + "'";

        int existingUserCount = 0;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                existingUserCount = Integer.parseInt(rs.getString("USERCOUNT"));
            }

            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in checkUserName()");
            System.err.println(e.getMessage());
        }

        return existingUserCount;
    }

    //used in LoginController for validating the user while logging in
    @Override
    public int findAccount(UserBean aLogin) {
        String userName = aLogin.getUserName();
        String password = aLogin.getPassword();

        String query = "SELECT COUNT(*) AS USERCOUNT FROM IT353.ACCOUNT ";
        query += "WHERE ulid  = '" + userName + "' AND password = '" + password +
                "' AND accountstatus = 'approved'";

        int accountsCount = 0;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                accountsCount = Integer.parseInt(rs.getString("USERCOUNT"));
            }
            System.out.println("approved account count=" + accountsCount);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccount()");
            System.err.println(e.getMessage());
        }
        return accountsCount;
    }

    //used in LoginController for validating the user while logging in
    @Override
    public int findPendingAccount(UserBean aLogin) {
        String userName = aLogin.getUserName();
        String password = aLogin.getPassword();

        String query = "SELECT COUNT(*) AS PENDINGUSERCOUNT FROM IT353.ACCOUNT ";
        query += "WHERE ulid  = '" + userName + "' AND password = '" + password + "' AND accountstatus = 'PENDING'";

        int pendingAccountsCount = 0;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                pendingAccountsCount = Integer.parseInt(rs.getString("PENDINGUSERCOUNT"));
            }
            System.out.println("pending user count =" + pendingAccountsCount);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccount()");
            System.err.println(e.getMessage());
        }
        return pendingAccountsCount;
    }
    
        //used in LoginController for validating the user while logging in
    @Override
    public int findDeniedAccount(UserBean aLogin) {
        String userName = aLogin.getUserName();
        String password = aLogin.getPassword();

        String query = "SELECT COUNT(*) AS DENIEDUSERCOUNT FROM IT353.ACCOUNT ";
        query += "WHERE ulid  = '" + userName + "' AND password = '" + password + "' AND accountstatus = 'DENIED'";

        int deniedAccountsCount = 0;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                deniedAccountsCount = Integer.parseInt(rs.getString("DENIEDUSERCOUNT"));
            }
            System.out.println("denied user count =" + deniedAccountsCount);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccount()");
            System.err.println(e.getMessage());
        }
        return deniedAccountsCount;
    }

    //used in LoginController for finding the role of the user while logging in. 
    @Override
    public String findUserAccountType(UserBean aLogin) {
        String userName = aLogin.getUserName();
        String password = aLogin.getPassword();

        String query = "SELECT ACCOUNTTYPE FROM IT353.ACCOUNT ";
        query += "WHERE ulid  = '" + userName + "' AND password = '" + password + "' AND accountstatus = 'approved'";

        String accountType = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                accountType = rs.getString("ACCOUNTTYPE");
            }
            System.out.println("Account type= " + accountType);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccount()");
            System.err.println(e.getMessage());
        }
        return accountType;
    }

    //used in loginController to retrieve the lost password
    @Override//not yet used
    public String retrievePassword(String userName) {
        String query = "SELECT PASSWORD FROM IT353.ACCOUNT ";
        query += "WHERE ulid  = '" + userName + "'";

        String password = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                password = rs.getString("PASSWORD");
            }
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in retrieveAccount()");
            System.err.println(e.getMessage());
        }
        return password;
    }

    //for updaterpofile?
    @Override
    public ArrayList findByUserName(String uName) {
        // if interested in matching wild cards, use: LIKE and '%" + aName + "%'";
        String query = "SELECT * FROM IT353.Users ";
        query += "WHERE userid = '" + uName + "'";

        ArrayList aUserData = selectProfilesFromDB(query);
        return aUserData;
    }

    private ArrayList selectProfilesFromDB(String query) {
        ArrayList aProjectBeanCollection = new ArrayList();
        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");

            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            String firstName, lastName, userName, password, email, securityQuestion, securityAnswer, reasonForAccount;
            UserBean aProjectBean;
            while (rs.next()) {
                // 1. if a float (say PRICE) is to be retrieved, use rs.getFloat("PRICE");
                // 2. Instead of using column name, can alternatively use: rs.getString(1); // not 0
                firstName = rs.getString("firstname");
                lastName = rs.getString("lastname");
                userName = rs.getString("userid");
                password = rs.getString("password");
                email = rs.getString("email");
                securityQuestion = rs.getString("security_qn");
                securityAnswer = rs.getString("security_ans");
                reasonForAccount = rs.getString("account_reason");

                // make a ProfileBean object out of the values
                aProjectBean = new UserBean(firstName, lastName, userName, password, email, securityQuestion, securityAnswer, reasonForAccount);
                // add the newly created object to the collection
                aProjectBeanCollection.add(aProjectBean);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("ERROR: Problems with SQL select");
            e.printStackTrace();
        }
        try {
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return aProjectBeanCollection;
    }

    @Override
    public int updateProfile(UserBean anUpdate) {

        Connection DBConn = null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        int rowCount = 0;
        int rowCount2 = 0;
        try {
            String myDB = "jdbc:derby://localhost:1527/IT353";
            DBConn = DriverManager.getConnection(myDB, "itkstu", "student");

            String updateString, updateString2;
            Statement stmt = DBConn.createStatement();

            String fname = anUpdate.getFirstName();
            String lname = anUpdate.getLastName();
            String uid = anUpdate.getUserName();
            String pwd = anUpdate.getPassword();
            String email = anUpdate.getEmail();
            String secQn = anUpdate.getSecurityQuestion();
            String secAns = anUpdate.getSecurityAnswer();

            // SQL UPDATE Syntax [http://www.w3schools.com]:
            // UPDATE table_name
            // SET column1=value, column2=value2,...
            // WHERE some_column=some_value
            // Note: Notice the WHERE clause in the UPDATE syntax. The WHERE clause specifies which record or records that should be updated. If you omit the WHERE clause, all records will be updated!
            updateString = "UPDATE IT353.Users SET "
                    + "firstname = '" + fname + "', "
                    + "lastname = '" + lname + "', "
                    + "password = '" + pwd + "', "
                    + "email = '" + email + "', "
                    + "security_qn = '" + secQn + "', "
                    + "security_ans = '" + secAns + "' "
                    + "WHERE userid = '" + uid + "'";
            rowCount = stmt.executeUpdate(updateString);
            System.out.println("updateString =" + updateString);

            updateString2 = "UPDATE IT353.LOGIN SET "
                    + "lastname = '" + pwd + "' "
                    + "WHERE userid = '" + uid + "'";
            rowCount2 = stmt.executeUpdate(updateString2);
            System.out.println("updateString2 =" + updateString2);

            DBConn.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        // if insert is successful, rowCount will be set to 1 (1 row inserted successfully). Else, insert failed.
        return rowCount;
    }
    
}
