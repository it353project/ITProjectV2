/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Minu Sabu
 */
public class UserBean {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String confirmPassword;
    private String email;
    private String securityQuestion;
    private String securityAnswer;
    private String accountType;
    private String accountJustification;
    private String isLoggedIn = "NotLoggedIn";

    /**
     * Creates a new instance of SignUpBean
     */
    public UserBean() {
        
    }

    public UserBean(String firstName, String lastName, String userName, String password,
            String email, String securityQuestion, String securityAnswer, String accountType) {
        this.isLoggedIn = "NotLoggedIn";
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.accountType = accountType;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        if (firstName.contains("'")) {
            firstName = firstName.replace("'", "''");
        }
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        if (lastName.contains("'")) {
            lastName = lastName.replace("'", "''");
        }
        this.lastName = lastName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        if (userName.contains("'")) {
            userName = userName.replace("'", "''");
        }
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        if (password.contains("'")) {
            password = password.replace("'", "''");
        }
        this.password = password;
    }

    /**
     * @return the confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPassword the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        if (confirmPassword.contains("'")) {
            confirmPassword = confirmPassword.replace("'", "''");
        }
        this.confirmPassword = confirmPassword;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        if (email.contains("'")) {
            email = email.replace("'", "''");
        }
        this.email = email;
    }

    /**
     * @return the securityQuestion
     */
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * @param securityQuestion the securityQuestion to set
     */
    public void setSecurityQuestion(String securityQuestion) {
        if (securityQuestion.contains("'")) {
            securityQuestion = securityQuestion.replace("'", "''");
        }
        this.securityQuestion = securityQuestion;
    }

    /**
     * @return the securityAnswer
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * @param securityAnswer the securityAnswer to set
     */
    public void setSecurityAnswer(String securityAnswer) {
        if (securityAnswer.contains("'")) {
            securityAnswer = securityAnswer.replace("'", "''");
        }
        this.securityAnswer = securityAnswer;
    }

    /**
     * @return the accountType
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType) {
        if (accountType.contains("'")) {
            accountType = accountType.replace("'", "''");
        }
        this.accountType = accountType;
    }

    /**
     * @return the accountJustification
     */
    public String getAccountJustification() {
        return accountJustification;
    }

    /**
     * @param accountJustification the accountJustification to set
     */
    public void setAccountJustification(String accountJustification) {
        if (accountJustification.contains("'")) {
            accountJustification = accountJustification.replace("'", "''");
        }
        this.accountJustification = accountJustification;
    }

    /**
     * @return the isLoggedIn
     */
    public String getIsLoggedIn() {
        return isLoggedIn;
    }

    /**
     * @param isLoggedIn the isLoggedIn to set
     */
    public void setIsLoggedIn(String isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

}
