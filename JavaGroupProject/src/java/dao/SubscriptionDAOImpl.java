/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author it3530219
 */
public class SubscriptionDAOImpl implements SubscriptionDAO {
    /* Performs a search against the database, using the searchBean's variables. */
    @Override
    public ArrayList getCurrentSubscriptions(String ULID){
        int accountID = findAccountIDByULID(ULID);
        String sqlSelect = "SELECT IT353.KEYWORD.KEYWORD "
                + "FROM IT353.KEYWORD "
                + "JOIN IT353.SUBSCRIPTION ON IT353.SUBSCRIPTION.KEYWORDID = IT353.KEYWORD.KEYWORDID "
                + "JOIN IT353.ACCOUNT ON IT353.ACCOUNT.ACCOUNTID = IT353.SUBSCRIPTION.ACCOUNTID "
                + "WHERE IT353.ACCOUNT.ACCOUNTID = " + accountID;
        
        return getSubscriptionList(sqlSelect);
    }
    
    public ArrayList getAvailableSubscriptions(String ULID){
        int accountID = findAccountIDByULID(ULID);
        
        String sqlSelect = "SELECT IT353.KEYWORD.KEYWORD "
                + "FROM IT353.KEYWORD "
                + "JOIN IT353.SUBSCRIPTION ON IT353.SUBSCRIPTION.KEYWORDID = IT353.KEYWORD.KEYWORDID "
                + "JOIN IT353.ACCOUNT ON IT353.ACCOUNT.ACCOUNTID = IT353.SUBSCRIPTION.ACCOUNTID "
                + "WHERE IT353.ACCOUNT.ACCOUNTID <> " + accountID;
        
        return getSubscriptionList(sqlSelect);
    }  
    
    public void addSubscription(String ULID, String keyword){
        int accountID = findAccountIDByULID(ULID);
        String insertStatement = "INSERT INTO IT353.SUBSCRIPTION VALUES";
        
        int statusCode = modifySubscriptions(insertStatement);
    }
    
    public void removeSubscription(String ULID, String keyword){
        int accountID = findAccountIDByULID(ULID);
        String deleteStatement = "DELETE FROM IT353.SUBSCRIPTION "
                + "JOIN IT353.KEYWORD ON IT353.KEYWORD.KEYWORDID = IT353.SUBSCRIPTION.KEYWORDID "
                + "WHERE IT353.SUBSCRIPTION.ACCOUNTID = " + accountID + " AND IT353.KEYWORD.KEYWORD = '" + keyword + "'";
        int statusCode = modifySubscriptions(deleteStatement);
    }  
    
    public String[] getSubscriberEmails(String keyword){
        String sqlSelect = "SELECT IT353.ACCOUNT.EMAIL FROM IT353.ACCOUNT WHERE IT353.KEYWORD.KEYWORD = " + keyword;
        return emailSearch(sqlSelect);
    }
    
    private String[] emailSearch(String query){
        String emails[] = null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        
        try {         
            String myDB = "jdbc:derby://localhost:1527/IT353";  
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
            Statement stmt = DBConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);
            
            int counter = 0;
            
            while(rs.next()){
                emails[counter] = rs.getString("EMAIL");
                counter++;
            }
            
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        return emails;
    }
    
    private ArrayList getSubscriptionList(String query){
        ArrayList keywords = new ArrayList();
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        
        try {         
            String myDB = "jdbc:derby://localhost:1527/IT353";  
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
            Statement stmt = DBConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);
                      
            while(rs.next()){
                String keyword = new String();
                keyword = rs.getString("KEYWORD");
                keywords.add(keyword);
            }
            
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return keywords;
    }
    
    public int findAccountIDByULID(String ULID){
        String query = "SELECT ACCOUNTID FROM IT353.ACCOUNT ";
        query += "WHERE IT353.ACCOUNT.ULID = '" + ULID + "'";

        int accoundIT = getAccountIDByULID(query);
        return accoundIT;
    }
    
    private int getAccountIDByULID(String query){
        Connection DBConn = null;
        int accountID = 0;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            String myDB = "jdbc:derby://localhost:1527/IT353";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");

            Statement stmt = DBConn.createStatement();
            
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next())
                accountID = rs.getInt("ACCOUNTID");
            
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
        return accountID;
    }
    
    private int modifySubscriptions(String query){
        Connection DBConn = null;
        int statusCode = 0;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            String myDB = "jdbc:derby://localhost:1527/IT353";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");

            Statement stmt = DBConn.createStatement();
            
            statusCode = stmt.executeUpdate(query);

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
        return statusCode;
    }

}
