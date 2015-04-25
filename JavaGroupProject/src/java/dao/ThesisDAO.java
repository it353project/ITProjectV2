/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import model.ThesisBean;
import model.UserBean;

/**
 *
 * @author it3530219
 */
public interface ThesisDAO {
    public int saveSubmission(ThesisBean aSubmissionBean, String username);
    public int findAccountIDFromName(String givenFullName) ;
    public int findAccountIDFromUserName(String userName);
    public int findNextSequenceValue(String sequenceName);
    public int checkKeywordInDB(String keyword);
    public ArrayList findByStudentIDCourseID(String userName, String courseID);
    public int updateThesis(ThesisBean anUpdate, String username);
    public String findEmailFromName(String givenFullName);
    public String getFullName(int accountID);
    
}
