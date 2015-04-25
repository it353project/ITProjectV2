/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *
 * @author it3530229
 */
public class ThesisBean {
    
    private String topic;
    private String courseID;
    private String semesterName;
    private String keywords;
    private String liveLink;
    private String screencastLink;
    private String committeeChair;
    private String committeMember1;
    private String committeMember2;
    private String committeMember3;
    private String projectAbstract;
    private String deliverableLink;
    private String uploadDate;
    private int numberOfViews;
    private int downloadsCount;
    private String submissionStatus;
    private byte[] uploadedFile;

    
    public ThesisBean(){
        
    }

    public ThesisBean(String topic, String courseID, String semesterName, String keywords, String liveLink, String screencastLink,
            String committeeChair, String committeMember1, String committeMember2, String committeMember3, String projectAbstract,
            String deliverableLink){
        this.topic = topic;
        this.courseID = courseID;
        this.semesterName = semesterName;
        this.keywords = keywords;
        this.liveLink = liveLink;
        this.screencastLink = screencastLink;
        this.committeeChair = committeeChair;
        this.committeMember1 = committeMember1;
        this.committeMember2 = committeMember2;
        this.committeMember3 = committeMember3;
        this.projectAbstract = projectAbstract;
        this.deliverableLink = deliverableLink;
    }

    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        if (topic.contains("'")) {
            topic = topic.replace("'", "''");
        }
        this.topic = topic;
    }

    /**
     * @return the courseID
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * @param courseID the courseID to set
     */
    public void setCourseID(String courseID) {
        if (courseID.contains("'")) {
            courseID = courseID.replace("'", "''");
        }
        this.courseID = courseID;
    }

    /**
     * @return the semesterName
     */
    public String getSemesterName() {
        return semesterName;
    }

    /**
     * @param semesterName the semesterName to set
     */
    public void setSemesterName(String semesterName) {
        if (semesterName.contains("'")) {
            semesterName = semesterName.replace("'", "''");
        }
        this.semesterName = semesterName;
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
        if (keywords.contains("'")) {
            keywords = keywords.replace("'", "''");
        }
        this.keywords = keywords;
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
        if (liveLink.contains("'")) {
            liveLink = liveLink.replace("'", "''");
        }
        this.liveLink = liveLink;
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
        if (screencastLink.contains("'")) {
            screencastLink = screencastLink.replace("'", "''");
        }
        this.screencastLink = screencastLink;
    }

    /**
     * @return the committeeChair
     */
    public String getCommitteeChair() {
        return committeeChair;
    }

    /**
     * @param committeeChair the committeeChair to set
     */
    public void setCommitteeChair(String committeeChair) {
        if (committeeChair.contains("'")) {
            committeeChair = committeeChair.replace("'", "''");
        }
        this.committeeChair = committeeChair;
    }

    /**
     * @return the committeMember1
     */
    public String getCommitteMember1() {
        return committeMember1;
    }

    /**
     * @param committeMember1 the committeMember1 to set
     */
    public void setCommitteMember1(String committeMember1) {
        if (committeMember1.contains("'")) {
            committeMember1 = committeMember1.replace("'", "''");
        }
        this.committeMember1 = committeMember1;
    }

    /**
     * @return the committeMember2
     */
    public String getCommitteMember2() {
        return committeMember2;
    }

    /**
     * @param committeMember2 the committeMember2 to set
     */
    public void setCommitteMember2(String committeMember2) {
        if (committeMember2.contains("'")) {
            committeMember2 = committeMember2.replace("'", "''");
        }
        this.committeMember2 = committeMember2;
    }

    /**
     * @return the committeMember3
     */
    public String getCommitteMember3() {
        return committeMember3;
    }

    /**
     * @param committeMember3 the committeMember3 to set
     */
    public void setCommitteMember3(String committeMember3) {
        if (committeMember3.contains("'")) {
            committeMember3 = committeMember3.replace("'", "''");
        }
        this.committeMember3 = committeMember3;
    }

    /**
     * @return the projectAbstract
     */
    public String getProjectAbstract() {
        return projectAbstract;
    }

    /**
     * @param projectAbstract the projectAbstract to set
     */
    public void setProjectAbstract(String projectAbstract) {
        if (projectAbstract.contains("'")) {
            projectAbstract = projectAbstract.replace("'", "''");
        }
        this.projectAbstract = projectAbstract;
    }

    /**
     * @return the deliverableLink
     */
    public String getDeliverableLink() {
        return deliverableLink;
    }

    /**
     * @param deliverableLink the deliverableLink to set
     */
    public void setDeliverableLink(String deliverableLink) {
        if (deliverableLink.contains("'")) {
            deliverableLink = deliverableLink.replace("'", "''");
        }
        this.deliverableLink = deliverableLink;
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
        if (uploadDate.contains("'")) {
            uploadDate = uploadDate.replace("'", "''");
        }
        this.uploadDate = uploadDate;
    }

    /**
     * @return the numberOfViews
     */
    public int getNumberOfViews() {
        return numberOfViews;
    }

    /**
     * @param numberOfViews the numberOfViews to set
     */
    public void setNumberOfViews(int numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    /**
     * @return the downloadsCount
     */
    public int getDownloadsCount() {
        return downloadsCount;
    }

    /**
     * @param downloadsCount the downloadsCount to set
     */
    public void setDownloadsCount(int downloadsCount) {
        this.downloadsCount = downloadsCount;
    }

    /**
     * @return the submissionStatus
     */
    public String getSubmissionStatus() {
        return submissionStatus;
    }

    /**
     * @param submissionStatus the submissionStatus to set
     */
    public void setSubmissionStatus(String submissionStatus) {
        if (submissionStatus.contains("'")) {
            submissionStatus = submissionStatus.replace("'", "''");
        }
        this.submissionStatus = submissionStatus;
    }

    /**
     * @return the uploadedFile
     */
    public byte[] getUploadedFile() {
        return uploadedFile;
    }

    /**
     * @param uploadedFile the uploadedFile to set
     */
    public void setUploadedFile(byte[] uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    
    
    

    
    
}
