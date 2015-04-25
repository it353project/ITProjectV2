/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.Date;

/**
 *
 * @author it3530229
 */
public class ViewBean {
    private int thesisID;
    private String thesisName;
    private String authorName;
    private String courseNo;
    private String[] keywords;
    private String abstractCLOB;
    private String attachmentLink;
    private String screencastLink;
    private String liveLink;
    private String uploadDate;

    /**
     * @return the thesisID
     */
    public int getThesisID() {
        return thesisID;
    }

    /**
     * @param thesisID the thesisID to set
     */
    public void setThesisID(int thesisID) {
        this.thesisID = thesisID;
    }

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
        this.authorName = authorName;
    }

    /**
     * @return the courseID
     */
    public String getCourseNo() {
        return courseNo;
    }

    /**
     * @param courseID the courseID to set
     */
    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    /**
     * @return the keywords
     */
    public String[] getKeywords() {
        return keywords;
    }
    
public String getKeywordString() {
        String keywordString = "";
            for(int i = 0; i < keywords.length; i++){
                
                if (keywords.length == 1){
                    keywordString = keywords[i];
                }else{
                    if(i == keywords.length - 1){
                        keywordString = keywordString + keywords[i];
                    }else{
                        keywordString = keywordString + keywords[i] + ", ";
                    }
                }
            }
        
        return keywordString;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    /**
     * @return the abstractCLOB
     */
    public String getAbstractCLOB() {
        return abstractCLOB;
    }

    /**
     * @param abstractCLOB the abstractCLOB to set
     */
    public void setAbstractCLOB(String abstractCLOB) {
        this.abstractCLOB = abstractCLOB;
    }

    /**
     * @return the attachmentLink
     */
    public String getAttachmentLink() {
        return attachmentLink;
    }

    /**
     * @param attachmentLink the attachmentLink to set
     */
    public void setAttachmentLink(String attachmentLink) {
        this.attachmentLink = attachmentLink;
    }

    /**
     * @return the screencastLink
     */
    public String getScreencastLink() {
        return screencastLink;
    }

    /**
     * @param screencastLink the screencastLink to set
     */
    public void setScreencastLink(String screencastLink) {
        this.screencastLink = screencastLink;
    }

    /**
     * @return the liveLink
     */
    public String getLiveLink() {
        return liveLink;
    }

    /**
     * @param liveLink the liveLink to set
     */
    public void setLiveLink(String liveLink) {
        this.liveLink = liveLink;
    }

    /**
     * @return the uploadDate
     */
    public String getUploadDate() {
        return uploadDate;
    }

    /**
     * @param uploadDate the uploadDate to set
     */
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
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


    
    
}
