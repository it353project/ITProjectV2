/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import model.UserBean;

/**
 *
 * @author it3530219
 */

@ManagedBean
@SessionScoped
public class SignUpController {
    private String response;
    private String signUpValidaton;
    private UserBean theModel;

    /**
     * Creates a new instance of ProfileController
     */
    public SignUpController() {
        theModel = new UserBean();
    }
    
    /**
     * @return the theModel
     */
    public UserBean getTheModel() {
        return theModel;
    }

    /**
     * @param theModel the theModel to set
     */
    public void setTheModel(UserBean theModel) {
        this.theModel = theModel;
    }
    
    /**
     * @return the signUpValidaton
     */
    public String getSignUpValidaton() {
        return signUpValidaton;
    }

    /**
     * @param signUpValidaton the signUpValidaton to set
     */
    public void setSignUpValidaton(String signUpValidaton) {
        this.signUpValidaton = signUpValidaton;
    }
    
    /**
     * @return the response
     */
    public String getResponse() {
        
        String first = theModel.getFirstName();
        String last = theModel.getLastName();
        
        if(first.contains("''")){
            first = first.replace("''", "'");
        }
        else if(last.contains("''")){
            last = last.replace("''", "'");
        }
        
        String resultStr = "";
        resultStr += "<br/>Hello " + first + " " + last + "," + "<br/><br/>";
        resultStr += "Thank you for signing up with the ISU Thesis Tracker System. <br/>";
        resultStr += "An email has been sent to the System Admin for "
                + "review. Admin will review your request and will get get back to you via email within 2 business days. "
                + "We really appreciate your patience and co-operation. Follow the instructions in the email that you receive for "
                + "using the thesis tracker system.<br/> "
                + "Please check the junk folder in your email as well to ensure that you do not miss any emails from the Admin. <br/><br/>";
        resultStr += "Cheers, <br/> Thesis Tracker Team.";
        response = resultStr;
        
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }
    
    public String authenticatePage1(){
        String validationMessage = null;
        
        String first = theModel.getFirstName();
        String last = theModel.getLastName();
        String uid = theModel.getUserName();
        String pwd = theModel.getPassword();
//        String confPwd = theModel.getConfirmPassword();
        String email = theModel.getEmail();
        
        String emailName = email.substring(0, email.indexOf('@'));

        String type = theModel.getAccountType();
        
        UserDAO aSignUpDAO = new UserDAOImpl();
        int uidCount = aSignUpDAO.checkUserName(uid);
         
        if(first.length()==0){
           signUpValidaton = "The first name field cannot be left blank. Please enter your first name"; 
        }
        else if(first.length()<2 || first.length()>25){
            signUpValidaton = "Your first name has to be between 2 and 25 letters long";
        }
        else if(last.length()==0){
           signUpValidaton = "The last name field cannot be left blank. Please enter your last name"; 
        }
        else if(last.length()<2 || last.length()>25){
            signUpValidaton = "Your last name has to be between 2 and 25 letters long";
        }
        else if(uid.length()==0){
           signUpValidaton = "Your ULID is required to create an account. Please enter your ULID"; 
        }
        else if(uid.length()<2 || uid.length()>7){
           signUpValidaton = "Invalid ULID. Please enter your ULID again."; 
        }
        else if(uidCount >=1){
               signUpValidaton = "The ULID you provided is already registered in this system. Try recovering your password."; 
        }
        else if(pwd.length()==0){
           signUpValidaton = "A password is required to create an account. Please enter a password for your account"; 
        }
        else if(pwd.length()<8){
           signUpValidaton = "The password should have at least 8 characters"; 
        }
////        else if(confPwd.length()==0){
////           signUpValidaton = "Please confirm the password you have entered"; 
////        }
////        else if(!pwd.equals(confPwd)){
////            signUpValidaton = "The password and the confirmation password doesn't match";
////        }
        else if(email.length()==0){
           signUpValidaton = "A valid email address is required to create an account. Please enter your email id"; 
        }
//        else if(!emailName.equalsIgnoreCase(uid)){
//            signUpValidaton = "The ULID and the email address doesn't match. Please check your entries";
//        }
        else if(type.length()==0){
            signUpValidaton = "Please select a type of account your wish to sign up for.";
        }
        else{
            signUpValidaton = "";
            validationMessage = "signUpPage2.xhtml";
        }
        return validationMessage;
    }
    
    public String authenticatePage2(){
        String validationMessage = null;
        
        String secQn = theModel.getSecurityQuestion();
        String secAns = theModel.getSecurityAnswer();
        String justification = theModel.getAccountJustification();
        
    
        if(secQn.length()==0){
           signUpValidaton = "Please select a security question for account recovery processes."; 
        }
        else if(secAns.length()==0){
           signUpValidaton = "Please give an answer for the security question you chose."; 
        }
        else if(justification.length()==0){
            signUpValidaton = "Please provide a justificationas to why you are requesting this account.";
        }
        else{
            signUpValidaton = "";
//            validationMessage = "signUpConfirmation.xhtml";
            validationMessage = createAccount();
//            sendEmailForApproval();
        }
        return validationMessage;
        
    }
    
        public String createAccount() {
        UserDAO aSignUpDAO = new UserDAOImpl();    // Creating a new object each time.
        int rowCount = aSignUpDAO.createAccount(theModel); // Doing anything with the object after this?
        if (rowCount == 1)
        {
            theModel.setIsLoggedIn("NotLoggedIn");
            sendEmailForApproval();
            return "signUpConfirmation.xhtml"; // navigate to "signUpConfirmation.xhtml"
        }
        else
        {
            theModel.setIsLoggedIn("NotLoggedIn");
            return "error.xhml"; 
        }
    }

    public void sendEmailForApproval(){
        // Recipient's email ID needs to be mentioned.
        String to = "msabu@ilstu.edu"; //have to query db to get the email of admin
        // Sender's email ID needs to be mentioned
        String from = "msabu@ilstu.edu";
        String accountJustification = theModel.getAccountJustification();
        // Assuming you are sending email from this host
        String host = "smtp.ilstu.edu";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.user", "yourID"); // if needed
        properties.setProperty("mail.password", "yourPassword"); // if needed
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            // Set Subject: header field
            message.setSubject("ISU Thesis Tracker: New Account Request");
            String messageBody = "Hi,<br><br>A new account for the ISU thesis tracker system has been requested"
                    + " by a student. Please see below the reason for account provided by the student.<br><br>"
                    + accountJustification + "<br><br>" 
                    + "Please login to the Thesis Tracker System and review this request and do the needful."
                    + "<br><br>Best Regards,<br>Thesis Tracker Team<br>";
            // Send the actual HTML message, as big as you like
            message.setContent(messageBody,
                    "text/html");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
             throw new RuntimeException(mex);
        }
    }
}