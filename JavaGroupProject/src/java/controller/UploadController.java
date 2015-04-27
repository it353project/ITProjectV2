/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.SearchDAO;
import dao.SearchDAOImpl;
import dao.ThesisDAO;
import dao.ThesisDAOImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import model.ThesisBean;
import model.UserBean;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author it3530219
 */
@ManagedBean
@SessionScoped
public class UploadController {

    private ThesisBean theThesisModel;

    private Part file1;
    private String documentUploadResult;
    private String uploadLink;
    private String uploadFileName;
    private String formValidationMessage;
    private UserBean theUserModel;
    private boolean workUploaded;

    public UploadController() {
        documentUploadResult = "";

        theThesisModel = new ThesisBean();

    }

    public UploadController(UserBean theUserModel) {
        super();
        this.theUserModel = theUserModel;
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

    /**
     *
     * @return the file1
     */
    public Part getFile1() {
        return file1;
    }

    /**
     *
     * @param file1 the file1 to set
     */
    public void setFile1(Part file1) {
        this.file1 = file1;
    }

    /**
     * @return the documentUploadResult
     */
    public String getDocumentUploadResult() {
        return documentUploadResult;
    }

    /**
     * @param documentUploadResult the documentUploadResult to set
     */
    public void setDocumentUploadResult(String documentUploadResult) {
        this.documentUploadResult = documentUploadResult;
    }

    /**
     * @return the uploadLink
     */
    public String getUploadLink() {
        return uploadLink;
    }

    /**
     * @param uploadLink the uploadLink to set
     */
    public void setUploadLink(String uploadLink) {
        this.uploadLink = uploadLink;
    }

    /**
     * @return the uploadFileName
     */
    public String getUploadFileName() {
        return uploadFileName;
    }

    /**
     * @param uploadFileName the uploadFileName to set
     */
    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String authenticateSubmission(String username) {
        String uploadValidationMessage = null;

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

        if (courseNo.length() == 0) {
            setFormValidationMessage("Please enter your course number");
        } else if (semester.length() == 0) {
            setFormValidationMessage("Please enter the current semester");
        } else if (keywords.length() == 0) {
            setFormValidationMessage("Please enter a few keywords separated by comma");
        } else if (liveLink.length() == 0) {
            setFormValidationMessage("Please enter the live link for the work");
        } else if (screenCastLink.length() == 0) {
            setFormValidationMessage("Please enter a the link to your screencast");
        } else if (committeeChair.length() == 0) {
            setFormValidationMessage("Please enter the name of the assigned committe chair");
        } else if (committeeMember1.length() == 0 || committeeMember2.length() == 0 || committeeMember3.length() == 0) {
            setFormValidationMessage("Please enter names of your committee members");
        } else if (projectAbstract.length() == 0) {
            setFormValidationMessage("Please paste your abstract in the space provided");
        } //        else if(deliverableLink.length()==0){
        //            setFormValidationMessage("Please upload your deliverables"); 
        //        }
        else {
            setFormValidationMessage("");
            uploadValidationMessage = saveSubmission(username);

        }

        return uploadValidationMessage;
    }

    public String saveSubmission(String username) {
        int memberCount = 0;
        String[] committeeMembersEmail = new String[4];
        int rowCount = 0;
        ThesisDAO aSubmissionDAO = new ThesisDAOImpl();

        String StudentName = aSubmissionDAO.getFullName(aSubmissionDAO.findAccountIDFromUserName(username));

        committeeMembersEmail[memberCount] = aSubmissionDAO.findEmailFromName(theThesisModel.getCommitteeChair());
        memberCount++;
        committeeMembersEmail[memberCount] = aSubmissionDAO.findEmailFromName(theThesisModel.getCommitteMember1());
        memberCount++;
        committeeMembersEmail[memberCount] = aSubmissionDAO.findEmailFromName(theThesisModel.getCommitteMember2());
        memberCount++;
        committeeMembersEmail[memberCount] = aSubmissionDAO.findEmailFromName(theThesisModel.getCommitteMember3());
        memberCount++;

        rowCount = aSubmissionDAO.saveSubmission(theThesisModel, username);
        if (rowCount > 0) {
            setFormValidationMessage("Thesis submitted successfully.");
            //send email to committee
            for (int i = 0; i < memberCount; i++) {
                sendEmailForApproval(committeeMembersEmail[i], StudentName);
            }
            sendEmailToSubscribers();
            setWorkUploaded(true);
        } else {
            setFormValidationMessage("Couldn't save submission. Please check the details.");
            setWorkUploaded(false);
        }

        return null;
    }

    public void upload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        //creating the folder structure
        String relativeWebPath = "\\resources\\uploads\\msabu";
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
        File file = new File(absoluteDiskPath);
        // Defining Directory/Folder Name   
        if (!file.exists()) {  // Checks that Directory/Folder Doesn't Exists!   
            boolean result = file.mkdirs();
            if (result) {
                System.out.println("New sub folder created!");
            }
        }
    // Do what you want with the file        
    
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream(), absoluteDiskPath);
    }
    catch (IOException e) {
            e.printStackTrace();
    }

}

    public void copyFile(String fileName, InputStream in, String absoluteDiskPath) {
        try {

//            String relativeWebPath = "\\resources\\uploads\\msabu";
//            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//            String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
            String absoluteFileName = absoluteDiskPath + "\\" + fileName;

            System.out.println("absoluteFileName=" + absoluteFileName);
            setUploadFileName(fileName);
            theThesisModel.setDeliverableLink(absoluteFileName);

            File file = new File(absoluteFileName);

            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(file);
            
            int read = 0;
            byte[] bytes = new byte[1024];
            for (int length = 0; (length = in.read(bytes)) > 0;) {
                out.write(bytes, 0, length);
            }
            in.close();
            out.flush();
            out.close();

            System.out.println("New file created at:" + file.getAbsolutePath());
            setUploadLink(file.getAbsolutePath());
            setDocumentUploadResult(" Uploaded Successfully...");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendEmailForApproval(String to, String studentName) {
// Recipient's email ID needs to be mentioned.
//        String to = "msabu@ilstu.edu"; //have to query db to get the email of admin
        // Sender's email ID needs to be mentioned
        String from = "mdgrome@ilstu.edu";
//        String accountJustification = theModel.getAccountJustification();
        // Assuming you are sending email from this host
        String host = "smtp.ilstu.edu";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.user", "mdgrome"); // if needed
        properties.setProperty("mail.password", "Z1rconium"); // if needed
        // Get the default Session object.
        Session session = Session.getInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            // Set Subject: header field
            message.setSubject("ISU Thesis Tracker: New Student Submission");
            String messageBody = "Hi,<br><br>A new project/thesis has been submitted by <studentname> in the "
                    + "ISU thesis tracker system and the student has listed you as part of the project/thesis committee. "
                    + " Below are the details of the submission.<br><br>"
                    + "Student Name: " + studentName
                    + "Thesis/Project Title: " + theThesisModel.getTopic() + "<br>"
                    + "Course Number: " + theThesisModel.getCourseID() + "<br>"
                    + "Committe Chair: " + theThesisModel.getCommitteeChair() + "<br>"
                    + "Committee Members: " + theThesisModel.getCommitteMember1() + ", " + theThesisModel.getCommitteMember2() + ", " + theThesisModel.getCommitteMember3() + "<br><br>"
                    + "Please login to the Thesis Tracker System and review this submission and do the needful."
                    + "<br><br>Best,<br>Thesis Tracker Team<br>";
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
    
    public void sendEmailToSubscribers(){
        String keywords = theThesisModel.getKeywords();
        String keywordHolder[] = keywords.split(",");
        for(int i = 0; i < keywordHolder.length; i++){
            String subscriberEmails[] = getSubscriberEmails(keywordHolder[i]);
            for(int j = 0; j < subscriberEmails.length; j++){
                sendSubscriberEmail(subscriberEmails[j], keywordHolder[i]);
            }
        } 
        
        
        
    }

    public String[] getSubscriberEmails(String keyword){
        String emailArray[] = null;
        SearchDAO aSearchDAO = new SearchDAOImpl();
        aSearchDAO.getSubscriberEmails(keyword);
        
        return emailArray;
    }
    
    public void sendSubscriberEmail(String email, String keyword){
        // Recipient's email ID needs to be mentioned.
        //        String to = "msabu@ilstu.edu"; //have to query db to get the email of admin
        // Sender's email ID needs to be mentioned
        String from = "mdgrome@ilstu.edu";
        //        String accountJustification = theModel.getAccountJustification();
        // Assuming you are sending email from this host
        String host = "smtp.ilstu.edu";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.user", "mdgrome"); // if needed
        properties.setProperty("mail.password", "Z1rconium"); // if needed
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email));
            // Set Subject: header field
            message.setSubject("ISU Thesis Tracker: Thesis Uploaded - " + keyword);
            String messageBody = "Hi,<br><br>A new project/thesis has been submitted by <studentname> in the "
                    + "ISU thesis tracker system with the keyword " + keyword + ", which you have subscribed to."
                    + " Below are the details of the submission.<br><br>"
                    + "Thesis/Project Title: " + theThesisModel.getTopic() + "<br>"
                    + "Course Number: " + theThesisModel.getCourseID() + "<br>"
                    + "<br><br>Best,<br>Thesis Tracker Team<br>";
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
//    public String upload() throws IOException {
//        InputStream input = file1.getInputStream();
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        byte[] buffer = new byte[10240];
//        for (int length = 0; (length = input.read(buffer)) > 0;) {
//            output.write(buffer, 0, length);
//        }
//        theThesisModel.setUploadedFile(output.toByteArray());
//        setUploadFileName(getFilename(file1));
//        setDocumentUploadResult(getUploadFileName() + " Uploaded Successfully.");
////        <h:commandLink value="#{uploadController.uploadFileName}" action="#{uploadController.uploadLink}"/>
//    upload button old                                               
//    <h:inputFile value="#{uploadController.file1}" /> 
//                                                <h:commandButton value="Upload Document" action="#{uploadController.upload()}"/>
//
//        file1.write(getFilename(file1));
////        setDocumentUploadResult("File Uploaded Successfully.");
//        uploadLink = "C:\\java\\glassfish-4.0\\glassfish\\domains\\domain1\\generated\\jsp\\JavaGroupProject\\" + getFilename(file1);
//        theThesisModel.setDeliverableLink(uploadLink);
////        setUploadFileName(getFilename(file1));
//
////        file1.write("C:\\data\\"+getFilename(file1));
//        InputStream inputStream = file1.getInputStream();          
//        FileOutputStream outputStream = new FileOutputStream(getFilename(file1));  
//          
//        byte[] buffer = new byte[4096];          
//        int bytesRead = 0;  
//        while(true) {                          
//            bytesRead = inputStream.read(buffer);  
//            if(bytesRead > 0) {  
//                ?outputStream.write(buffer, 0, bytesRead);
//            }else {
//                documentUploadResult = "Error uploading file. Please try again.";
//                break;  
//            }                         
//        }  
//        outputStream.close();  
//        inputStream.close(); 
//        return "";
//    }
//
//    public static String getFilename(Part part) {
//        for (String cd : part.getHeader("content-disposition").split(";")) {
//            if (cd.trim().startsWith("filename")) {
//                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
//                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
//            }
//        }
//        return null;
//    }
    /**
     * @return the formValidationMessage
     */
    public String getFormValidationMessage() {
        return formValidationMessage;
    }

    /**
     * @param formValidationMessage the formValidationMessage to set
     */
    public void setFormValidationMessage(String formValidationMessage) {
        this.formValidationMessage = formValidationMessage;
    }

    /**
     * @return the theUserModel
     */
    public UserBean getTheUserModel() {
        return theUserModel;
    }

    /**
     * @param theUserModel the theUserModel to set
     */
    public void setTheUserModel(UserBean theUserModel) {
        this.theUserModel = theUserModel;
    }

    /**
     * @return the WorkUploaded
     */
    public boolean isWorkUploaded() {
        return workUploaded;
    }

    /**
     * @param WorkUploaded the WorkUploaded to set
     */
    public void setWorkUploaded(boolean workUploaded) {
        this.workUploaded = workUploaded;
    }

}
