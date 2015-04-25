/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dao.ThesisDAO;
import dao.ThesisDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import java.util.ArrayList;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import model.ThesisBean;
import model.UserBean;

/**
 *
 * @author it3530219
 */
@ManagedBean
@SessionScoped
public class UpdateController {
    
    private UserBean theUserModel;
    private ThesisBean theThesisModel;
    private String updateStatus;
    
    public UpdateController() {
        theUserModel = new UserBean();
        theThesisModel = new ThesisBean();
        theUserModel.setIsLoggedIn("NotLoggedIn");
    }
    
    public UserBean getTheUserModel() {
        return theUserModel;
    }

    public void setTheUserModel(UserBean theUserModel) {
        this.theUserModel = theUserModel;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }
    
    public String retrieveThesis(String userName, String courseNo) {
        System.out.println("in retrieveThesis method");
        System.out.println("userName ="+userName);
        System.err.println("courseNo = "+courseNo);
        ThesisDAO aSubmissionDAO = new ThesisDAOImpl();
        ArrayList result = aSubmissionDAO.findByStudentIDCourseID(userName, courseNo);
        theThesisModel = (ThesisBean) result.get(0);
        if (theThesisModel != null) 
        {
            theUserModel.setIsLoggedIn("LoggedIn");
            return "updateThesis.xhtml";
        }
        else
        {
            theUserModel.setIsLoggedIn("NotLoggedIn");
            return "error.xhtml";
        }
    }
    
    public void updateThis(String username) {
        
//        String first = theUserModel.getFirstName();
//        String last = theUserModel.getLastName();
//        String pwd = theUserModel.getPassword();
//        String confPwd = theUserModel.getConfirmPassword();
//        String email = theUserModel.getEmail();
//        String secQn = theUserModel.getSecurityQuestion();
//        String secAns = theUserModel.getSecurityAnswer();
        
        String courseNo = theThesisModel.getCourseID();
        String semester = theThesisModel.getSemesterName();
        String keywords = theThesisModel.getKeywords();
        String liveLink = theThesisModel.getLiveLink();
        String screenCastLink = theThesisModel.getScreencastLink();
        String committeeChair = theThesisModel.getCommitteeChair();
        String committeeMember1 = theThesisModel.getCommitteMember1();
        String committeeMember2 = theThesisModel.getCommitteMember2();
        String committeeMember3 = theThesisModel.getCommitteMember3();
        String projectAbstract = theThesisModel.getProjectAbstract();
        String deliverableLink = theThesisModel.getDeliverableLink();
        
        ThesisDAO anUpdateDAO = new ThesisDAOImpl();
        
        
        if (courseNo.length() == 0) {
            setUpdateStatus("Please enter your course number");
        } else if (semester.length() == 0) {
            setUpdateStatus("Please enter the current semester");
        } else if (keywords.length() == 0) {
            setUpdateStatus("Please enter a few keywords separated by comma");
        } else if (liveLink.length() == 0) {
            setUpdateStatus("Please enter the live link for the work");
        } else if (screenCastLink.length() == 0) {
            setUpdateStatus("Please enter a the link to your screencast");
        } else if (committeeChair.length() == 0) {
            setUpdateStatus("Please enter the name of the assigned committe chair");
        } else if (committeeMember1.length() == 0 || committeeMember2.length() == 0 || committeeMember3.length() == 0) {
            setUpdateStatus("Please enter names of your committee members");
        } else if (projectAbstract.length() == 0) {
            setUpdateStatus("Please paste your abstract in the space provided");
        } //        else if(deliverableLink.length()==0){
        //            setFormValidationMessage("Please upload your deliverables"); 
        //        }
        else{
            
        int status = anUpdateDAO.updateThesis(theThesisModel, username); 
        if (status != 0) {
            updateStatus = "Your submission has been updated successfully!";
        } else {
            updateStatus = "Could not update your submission. Please check your updates.";
        }
        }
        
    }
    
    public String isLoggedInCheck(ComponentSystemEvent event) {
        String navi = null;

        if (!theUserModel.getIsLoggedIn().equals("LoggedIn")) {

            FacesContext fc = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            nav.performNavigation("login?faces-redirect=true");
         
        }

        return navi;
    }

    /**
     * @return the theThesisModel
     */
    public ThesisBean getTheThesisModel() {
        return theThesisModel;
    }

    /**
     * @param theThesisModel the theThesisModel to set
     */
    public void setTheThesisModel(ThesisBean theThesisModel) {
        this.theThesisModel = theThesisModel;
    }
}
