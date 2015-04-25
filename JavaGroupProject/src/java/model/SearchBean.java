/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author it3530229
 */
public class SearchBean {
    private String thesisID;
    private String authorName;
    private String thesisName;
    private String courseNo;
    private String keywords;
    private Date startDate;
    private Date endDate;
    private List results;

    /**
     * @return the authorName
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * @param authorName the authorName to set
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName.trim();
    }

    /**
     * @return the courseNo
     */
    public String getCourseNo() {
        return courseNo;
    }

    /**
     * @param courseNo the courseNo to set
     */
    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo.trim();
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the keywords
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords.trim();
    }

    /**
     * @return the results
     */
    public List getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(ArrayList results) {
        this.results = results;
    }

    /**
     * @return the thesisName
     */
    public String getThesisName() {
        return thesisName;
    }

    /**
     * @param thesisName the thesisName to set
     */
    public void setThesisName(String thesisName) {
        this.thesisName = thesisName;
    }

    /**
     * @return the thesisID
     */
    public String getThesisID() {
        return thesisID;
    }

    /**
     * @param thesisID the thesisID to set
     */
    public void setThesisID(String thesisID) {
        this.thesisID = thesisID;
    }
            
    
}
