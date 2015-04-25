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
import model.ThesisBean;
import model.UserBean;

/**
 *
 * @author it3530219
 */
public class ThesisDAOImpl implements ThesisDAO {

    @Override
    public int saveSubmission(ThesisBean aSubmissionBean, String username) {

        int rowCount1, rowCount2 = 0, rowCount3 = 0, rowCount4 = 0, rowCount5 = 0, rowCount6 = 0, rowCount7 = 0;
        String courseNo = aSubmissionBean.getCourseID();
        String semester = aSubmissionBean.getSemesterName();
        String topic = aSubmissionBean.getTopic();
        String liveLink = aSubmissionBean.getLiveLink();
        String screenCast = aSubmissionBean.getScreencastLink();
        String committeeChair = aSubmissionBean.getCommitteeChair();
        String committeMember1 = aSubmissionBean.getCommitteMember1();
        String committeMember2 = aSubmissionBean.getCommitteMember2();
        String committeMember3 = aSubmissionBean.getCommitteMember3();
        String abstractText = aSubmissionBean.getProjectAbstract();
        String deliverableLink = aSubmissionBean.getDeliverableLink();

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
//
//            String studentName = aUserBean.getFirstName() + " " + aUserBean.getLastName();
            int accountId = findAccountIDFromUserName(username);
            System.out.println("Student id = " + accountId);

            int nextThesisID = findNextSequenceValue("THESIS_ID_SEQ");
            String startingInsertString = "INSERT INTO IT353.THESIS VALUES (" + nextThesisID
                    + ", " + accountId + ", '"
                    + topic + "', "
                    + "'" + abstractText + "', "
                    + "'" + deliverableLink + "', "
                    + "'" + semester + "', "
                    + "'" + screenCast + "', "
                    + "'" + liveLink + "',"
                    + "CAST(CURRENT_TIMESTAMP AS DATE), default, default, "
                    + "'" + courseNo + "', "
                    + "default)";

            rowCount1 = stmt.executeUpdate(startingInsertString);
            System.out.println("insert string =" + startingInsertString);
            System.out.println(rowCount1 + " row(s) inserted");
            System.out.println("Thesis insert completed");

            //populating the keyword and keyAssign tables
            String rawKeywords = aSubmissionBean.getKeywords();
            System.out.println(rawKeywords);
            String[] splitKeywords = rawKeywords.split(", ");
            int keywordCount = splitKeywords.length;
            System.out.println("no of keywords = " + keywordCount);
            for (int i = 0; i < keywordCount; i++) {
                System.out.println(splitKeywords[i]);
                int dbIdCheck = checkKeywordInDB(splitKeywords[i]);
                int nextKeywordID;
                if (dbIdCheck <= 0) {
                    nextKeywordID = findNextSequenceValue("KEYWORD_ID_SEQ");
                    String keywordsInsert = "INSERT INTO IT353.KEYWORD VALUES(" + nextKeywordID + ", '"
                            + splitKeywords[i].trim() + "')";
                    rowCount2 = stmt.executeUpdate(keywordsInsert);
                    System.out.println("insert string =" + keywordsInsert);
                    System.out.println(rowCount2 + " row(s) inserted");
                } else {
                    nextKeywordID = dbIdCheck;
                }

                int nextKeyAssignID = findNextSequenceValue("KEYASSIGN_ID_SEQ");
                //populating the keyAssign table
                String keyAssignInsert = "INSERT INTO IT353.KEYASSIGN VALUES(" + nextKeyAssignID + ", "
                        + nextKeywordID + ", "
                        + nextThesisID + ")";
                rowCount3 = stmt.executeUpdate(keyAssignInsert);
                System.out.println("insert string =" + keyAssignInsert);
                System.out.println(rowCount3 + " row(s) inserted");

            }
            System.out.println("Keyword/Keyassign insert completed");

            //populate the committee table
            //find the accountid for committee chair
            int committeeChairID = findAccountIDFromName(committeeChair);
            int nextCommitteeID = findNextSequenceValue("COMMITTEE_ID_SEQ");
            String committeeInsert = "INSERT INTO IT353.COMMITTEE VALUES (" + nextCommitteeID + ", "
                    + nextThesisID + ", "
                    + committeeChairID + ")";
            rowCount4 = stmt.executeUpdate(committeeInsert);
            System.out.println("insert string =" + committeeInsert);
            System.out.println(rowCount4 + " row(s) inserted");
            System.out.println("committee insert completed");

            //populate the Appointment table
            int member1ID = findAccountIDFromName(committeMember1);
            int member2ID = findAccountIDFromName(committeMember2);
            int member3ID = findAccountIDFromName(committeMember3);

            int nextAppointmentID = findNextSequenceValue("APPOINTMENT_ID_SEQ");
            String appointmentInsert1 = "INSERT INTO IT353.APPOINTMENT VALUES (" + nextAppointmentID + ", "
                    + member1ID + ", " + nextCommitteeID + ")";
            rowCount5 = stmt.executeUpdate(appointmentInsert1);
            System.out.println("insert string =" + appointmentInsert1);
            System.out.println(rowCount5 + " row(s) inserted");

            nextAppointmentID = findNextSequenceValue("APPOINTMENT_ID_SEQ");
            String appointmentInsert2 = "INSERT INTO IT353.APPOINTMENT VALUES (" + nextAppointmentID + ", "
                    + member2ID + ", " + nextCommitteeID + ")";
            rowCount6 = stmt.executeUpdate(appointmentInsert2);
            System.out.println("insert string =" + appointmentInsert2);
            System.out.println(rowCount6 + " row(s) inserted");

            nextAppointmentID = findNextSequenceValue("APPOINTMENT_ID_SEQ");
            String appointmentInsert3 = "INSERT INTO IT353.APPOINTMENT VALUES (" + nextAppointmentID + ", "
                    + member3ID + ", " + nextCommitteeID + ")";
            rowCount7 = stmt.executeUpdate(appointmentInsert3);
            System.out.println("insert string =" + appointmentInsert3);
            System.out.println(rowCount7 + " row(s) inserted");
            System.out.println("appointment insert completed");
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert in saveSubmission()");
            System.err.println(e.getMessage());
        }
        return rowCount7;
    }

    @Override
    public int findAccountIDFromName(String givenFullName) {

        String fullName = givenFullName.trim();
        String firstName = fullName.substring(0, fullName.indexOf(" "));
        String lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
        System.out.println("firstname = " + firstName);
        System.out.println("lastname = " + lastName);
        int accountID = 0;

        String retrieveIDQuery = "SELECT ACCOUNTID FROM IT353.ACCOUNT WHERE FIRSTNAME = '"
                + firstName + "' AND LASTNAME = '" + lastName + "'";

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
                accountID = Integer.parseInt(rs.getString("ACCOUNTID"));
            }
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccountIDFromName()");
            System.err.println(e.getMessage());
        }
        return accountID;
    }
    
    @Override
    public String findEmailFromName(String givenFullName) {

        String fullName = givenFullName.trim();
        String firstName = fullName.substring(0, fullName.indexOf(" "));
        String lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
        System.out.println("firstname = " + firstName);
        System.out.println("lastname = " + lastName);
        String email=null;

        String retrieveIDQuery = "SELECT EMAIL FROM IT353.ACCOUNT WHERE FIRSTNAME = '"
                + firstName + "' AND LASTNAME = '" + lastName + "'";

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
                email = rs.getString("EMAIL");
            }
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccountIDFromName()");
            System.err.println(e.getMessage());
        }
        return email;
    }
    

    @Override
    public int findAccountIDFromUserName(String userName) {

        userName = userName.trim();
        int accountID = 0;

        String retrieveIDQuery = "SELECT ACCOUNTID FROM IT353.ACCOUNT WHERE ULID = '"
                + userName + "'";

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
                accountID = Integer.parseInt(rs.getString("ACCOUNTID"));
            }
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccountIDFromName()");
            System.err.println(e.getMessage());
        }
        return accountID;
    }

    @Override
    public int findNextSequenceValue(String sequenceName) {

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

    @Override
    public int checkKeywordInDB(String keyword) {

        System.out.println("keyword checked = " + keyword);

        int keyworId = 0;

        String retrieveIDQuery = "SELECT KEYWORDID FROM IT353.KEYWORD WHERE KEYWORD = '"
                + keyword + "'";

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
                keyworId = Integer.parseInt(rs.getString("KEYWORDID"));
            }
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in checkKeywordInDB()");
            System.err.println(e.getMessage());
        }
        return keyworId;
    }

    @Override
    public ArrayList findByStudentIDCourseID(String userName, String courseID) {
        // if interested in matching wild cards, use: LIKE and '%" + aName + "%'";
        int accountID = findAccountIDFromUserName(userName);
        String query = "SELECT * FROM IT353.THESIS ";
        query += "WHERE ACCOUNTID = " + accountID + " AND COURSENO = '" + courseID + "'";

        ArrayList aUserData = selectThesisFromDB(query);
        return aUserData;
    }

    private ArrayList selectThesisFromDB(String query) {
        ArrayList aThesisBeanCollection = new ArrayList();
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

            String thesisName = null, abstractText = null, attachment = null, semester = null, screencast = null, livelink = null, courseNo = null;
            int thesisid = 0, accountid = 0;
            ThesisBean aSubmission;

            while (rs.next()) {
                // 1. if a float (say PRICE) is to be retrieved, use rs.getFloat("PRICE");
                // 2. Instead of using column name, can alternatively use: rs.getString(1); // not 0
                thesisid = Integer.parseInt(rs.getString("THESISID"));
                accountid = Integer.parseInt(rs.getString("ACCOUNTID"));
//                committeeid = Integer.parseInt(rs.getString("COMMITTEEID"));
                thesisName = rs.getString("THESISNAME");//
                semester = rs.getString("SEMESTERNAME");//
                abstractText = rs.getString("ABSTRACT");//
                screencast = rs.getString("SCREENCASTLINK");//
                livelink = rs.getString("LIVELINK");//
                courseNo = rs.getString("COURSENO");
                attachment = rs.getString("ATTACHMENTLINK");//
            }
            System.out.println("thesisid = " + thesisid);
            System.out.println("accountid = " + accountid);
            System.out.println("thesisName = " + thesisName);
            System.out.println("semester = " + semester);
            System.out.println("abstractText = " + abstractText);
            System.out.println("screencast = " + screencast);
            System.out.println("livelink = " + livelink);
            System.out.println("courseNo = " + courseNo);
            System.out.println("attachment = " + attachment);
            rs.close();

            //get details about the committee head and committee members
            int committeHeadId = 0;
            int committeID = 0;
            int[] committeeDetails = findCommitteeDetails(thesisid);
            committeHeadId = committeeDetails[0];
            committeID = committeeDetails[1];
            String committeeHeadName = getFullName(committeHeadId);
            System.out.println("committeeHeadName = " + committeeHeadName);

            String[] memberNames = findMemberNames(committeID);
//            System.out.println(memberNames[0]);
//            System.out.println(memberNames[1]);
//            System.out.println(memberNames[2]);

            //get all the keywords for the thesis
            int[] keywordId = getKeyWords(thesisid);
            int keywordCount = keywordId.length;

            String keyword = null;
            String keywordString = "";
            for (int i = 0; i < keywordCount; i++) {
                keyword = findKeyword(keywordId[i]);
                keywordString += keyword + ", ";
            }

            int nullposition = keywordString.indexOf("null");
            keywordString = keywordString.substring(0, nullposition);
            keywordString = keywordString.substring(0, keywordString.lastIndexOf(','));
            System.out.println("keywordString = " + keywordString);

            // make a ThesisBean object out of the values
            aSubmission = new ThesisBean(thesisName, courseNo, semester, keywordString, livelink, screencast,
                    committeeHeadName, memberNames[0], memberNames[1], memberNames[2], abstractText,
                    attachment);
            // add the newly created object to the collection
            aThesisBeanCollection.add(aSubmission);
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert/select/update in selectThesisFromDB()");
            System.err.println(e.getMessage());
        }
        return aThesisBeanCollection;
    }

    private String findKeyword(int keywordId) {
        String keywordQuery = "SELECT KEYWORD FROM IT353.KEYWORD WHERE KEYWORDID=" + keywordId;
        String keyword = null;
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

            ResultSet rs = stmt.executeQuery(keywordQuery);

            while (rs.next()) {
                keyword = rs.getString("KEYWORD");

            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert/select/update in findCommitteeDetails()");
            System.err.println(e.getMessage());
        }

        return keyword;
    }

//    @Override
    public int[] getKeyWords(int thesisId) {

        String query = "SELECT KEYWORDID FROM IT353.KEYASSIGN WHERE THESISID=" + thesisId;
        int keywordCount = 0;
        int[] keywordId = new int[50];
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
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                keywordId[keywordCount] = Integer.parseInt(rs.getString("KEYWORDID"));
                keywordCount++;
            }
            rs.close();

            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in checkKeywordInDB()");
            System.err.println(e.getMessage());
        }

        return keywordId;
    }

    private String[] findMemberNames(int committeeID) {
        String appointmentQuery = "SELECT ACCOUNTID FROM IT353.APPOINTMENT WHERE COMMITTEEID = " + committeeID;
        String[] memberNames = new String[10];
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
            ResultSet rs = stmt.executeQuery(appointmentQuery);
            int memberCount = 0;
            int[] memeberId = new int[10];
            while (rs.next()) {
                memeberId[memberCount] = Integer.parseInt(rs.getString("ACCOUNTID"));
                memberCount++;
            }

            for (int i = 0; i < memberCount; i++) {
                memberNames[i] = getFullName(memeberId[i]);
                System.out.println("memberNames = " + memberNames[i]);

            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert/select/update in findCommitteeDetails()");
            System.err.println(e.getMessage());
        }
        return memberNames;
    }

    private int[] findCommitteeDetails(int thesisid) {
        String committeeHeadIDQuery = "SELECT COMMITTEEHEAD, COMMITTEEID FROM IT353.COMMITTEE "
                + "WHERE THESISID = " + thesisid;
        System.out.println(committeeHeadIDQuery);
        int committeHeadId = 0;
        int committeID = 0;
        int[] returnArray = new int[5];

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

            ResultSet rs = stmt.executeQuery(committeeHeadIDQuery);
            while (rs.next()) {
                committeHeadId = Integer.parseInt(rs.getString("COMMITTEEHEAD"));
                committeID = Integer.parseInt(rs.getString("COMMITTEEID"));
            }
            System.out.println("committeHeadId = " + committeHeadId);
            System.out.println("committeID = " + committeID);
//            String committeeHeadName = getFullName(committeHeadId);
//            System.out.println("committeeHeadName = "+committeeHeadName);

            returnArray[0] = committeHeadId;
            returnArray[1] = committeID;

            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert/select/update in findCommitteeDetails()");
            System.err.println(e.getMessage());
        }

        return returnArray;
    }

    @Override
    public String getFullName(int accountID) {
        String fullName = null;
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

            String nameQuery = "SELECT FIRSTNAME || ' ' || LASTNAME AS FULLNAME FROM IT353.ACCOUNT WHERE ACCOUNTID="
                    + accountID;

            ResultSet rs = stmt.executeQuery(nameQuery);
            while (rs.next()) {
                fullName = rs.getString("FULLNAME");

            }

            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert/select/update in UpdateThesis()");
            System.err.println(e.getMessage());
        }

        return fullName;
    }

    @Override
    public int updateThesis(ThesisBean anUpdate, String username) {

        String courseNo = anUpdate.getCourseID();
        String semester = anUpdate.getSemesterName();
        String topic = anUpdate.getTopic();
        String liveLink = anUpdate.getLiveLink();
        String screenCast = anUpdate.getScreencastLink();
        String committeeChair = anUpdate.getCommitteeChair();
        String committeMember1 = anUpdate.getCommitteMember1();
        String committeMember2 = anUpdate.getCommitteMember2();
        String committeMember3 = anUpdate.getCommitteMember3();
        String abstractText = anUpdate.getProjectAbstract();
        String deliverableLink = anUpdate.getDeliverableLink();
        String rawKeywords = anUpdate.getKeywords();
        int accountID = findAccountIDFromUserName(username);
        int thesisId = 0;
        int rowCount = 0, rowCount1 = 0, rowCount2 = 0, rowCount3 = 0, rowCount4 = 0, rowCount5 = 0, rowCount6 = 0, rowCount7 = 0, rowCount8 = 0;

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

            //first get the thesisid for the submission being updated
            String thesisIdQuery = "SELECT THESISID FROM IT353.THESIS WHERE ACCOUNTID = " + accountID
                    + " AND COURSENO = '" + courseNo + "'";

            ResultSet rs = stmt.executeQuery(thesisIdQuery);
            while (rs.next()) {
                thesisId = Integer.parseInt(rs.getString("THESISID"));
            }
            rs.close();
            //update the thesis table first
            String thesisUpdateQuery = "UPDATE IT353.THESIS SET "
                    + "THESISNAME = '" + topic
                    + "', SEMESTERNAME = '" + semester
                    + "', ABSTRACT = '" + abstractText
                    + "', SCREENCASTLINK = '" + screenCast
                    + "', LIVELINK = '" + liveLink
                    + "', UPLOADDATE = CAST(CURRENT_TIMESTAMP AS DATE), "
                    + "ATTACHMENTLINK = '" + deliverableLink
                    + "' WHERE ACCOUNTID =" + accountID + " AND COURSENO = '" + courseNo + "'";

            rowCount = stmt.executeUpdate(thesisUpdateQuery);
            System.out.println("updateString =" + thesisUpdateQuery);
            System.out.println(rowCount + "row updated");

            System.out.println("Thesis table update completed");

            //update the Keyword and keyAssign tables
            //first delete all the entries in the keyAssign table
            String deleteKeyassign = "DELETE FROM IT353.KEYASSIGN WHERE THESISID =" + thesisId;
            rowCount1 = stmt.executeUpdate(deleteKeyassign);
            System.out.println("DeleteString =" + deleteKeyassign);
            System.out.println(rowCount1 + "row deleted");

            //update/insert the keywords and keyassign table
            System.out.println(rawKeywords);
            String[] splitKeywords = rawKeywords.split(", ");
            int keywordCount = splitKeywords.length;
            System.out.println("no of new keywords = " + keywordCount);
            for (int i = 0; i < keywordCount; i++) {
                System.out.println(splitKeywords[i]);
                int dbIdCheck = checkKeywordInDB(splitKeywords[i]);
                int nextKeywordID;
                if (dbIdCheck <= 0) {
                    nextKeywordID = findNextSequenceValue("KEYWORD_ID_SEQ");
                    String keywordsInsert = "INSERT INTO IT353.KEYWORD VALUES(" + nextKeywordID + ", '"
                            + splitKeywords[i].trim() + "')";
                    rowCount2 = stmt.executeUpdate(keywordsInsert);
                    System.out.println("insert string =" + keywordsInsert);
                    System.out.println(rowCount2 + " row(s) inserted");
                } else {
                    nextKeywordID = dbIdCheck;
                }

                int nextKeyAssignID = findNextSequenceValue("KEYASSIGN_ID_SEQ");
                //populating the keyAssign table
                String keyAssignInsert = "INSERT INTO IT353.KEYASSIGN VALUES(" + nextKeyAssignID + ", "
                        + nextKeywordID + ", "
                        + thesisId + ")";
                rowCount3 = stmt.executeUpdate(keyAssignInsert);
                System.out.println("insert string =" + keyAssignInsert);
                System.out.println(rowCount3 + " row(s) inserted");

            }
            System.out.println("Keyword/Keyassign insert/update completed");

            //now update the committee and appointment tables
            //updating the committe table first
            //for that we ned to get the id of the committe head 
            int committeeHeadId = findAccountIDFromName(committeeChair);
            //update the committe table with the new committee head id
            String updateCommitteString = "UPDATE IT353.COMMITTEE "
                    + "SET COMMITTEEHEAD = " + committeeHeadId
                    + " WHERE THESISID = " + thesisId;

            rowCount4 = stmt.executeUpdate(updateCommitteString);
            System.out.println("updateString =" + updateCommitteString);
            System.out.println(rowCount4 + "row updated");
            System.out.println("committee table update completed");

            //now get the committe id for the current thesis to update the Appointment table
            int committeId = 0;
            String findCommitteeIdQuery = "SELECT COMMITTEEID FROM IT353.COMMITTEE WHERE THESISID = " + thesisId;
            ResultSet rs1 = stmt.executeQuery(findCommitteeIdQuery);
            while (rs1.next()) {
                committeId = Integer.parseInt(rs1.getString("COMMITTEEID"));
            }
            rs1.close();
            System.out.println("Committe id returned from query = " + committeId);

            //now delete all the previous entries in the appointment table pertaining to the given committee id
            String deleteAppointmentQuery = "DELETE FROM IT353.APPOINTMENT WHERE COMMITTEEID =" + committeId;
            rowCount5 = stmt.executeUpdate(deleteAppointmentQuery);
            System.out.println("DeleteString =" + deleteAppointmentQuery);
            System.out.println(rowCount5 + "row deleted");

            //now add a new set of entries into the appointment tables
            int member1ID = findAccountIDFromName(committeMember1);
            int member2ID = findAccountIDFromName(committeMember2);
            int member3ID = findAccountIDFromName(committeMember3);

            int nextAppointmentID = findNextSequenceValue("APPOINTMENT_ID_SEQ");
            String appointmentInsert1 = "INSERT INTO IT353.APPOINTMENT VALUES (" + nextAppointmentID + ", "
                    + member1ID + ", " + committeId + ")";
            rowCount6 = stmt.executeUpdate(appointmentInsert1);
            System.out.println("insert string =" + appointmentInsert1);
            System.out.println(rowCount6 + " row(s) inserted");

            nextAppointmentID = findNextSequenceValue("APPOINTMENT_ID_SEQ");
            String appointmentInsert2 = "INSERT INTO IT353.APPOINTMENT VALUES (" + nextAppointmentID + ", "
                    + member2ID + ", " + committeId + ")";
            rowCount7 = stmt.executeUpdate(appointmentInsert2);
            System.out.println("insert string =" + appointmentInsert2);
            System.out.println(rowCount7 + " row(s) inserted");

            nextAppointmentID = findNextSequenceValue("APPOINTMENT_ID_SEQ");
            String appointmentInsert3 = "INSERT INTO IT353.APPOINTMENT VALUES (" + nextAppointmentID + ", "
                    + member3ID + ", " + committeId + ")";
            rowCount8 = stmt.executeUpdate(appointmentInsert3);
            System.out.println("insert string =" + appointmentInsert3);
            System.out.println(rowCount8 + " row(s) inserted");
            System.out.println("appointment insert completed");

            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert/select/update in UpdateThesis()");
            System.err.println(e.getMessage());
        }
        // if insert is successful, rowCount will be set to 1 (1 row inserted successfully). Else, insert failed.
        return rowCount8;
    }

}
