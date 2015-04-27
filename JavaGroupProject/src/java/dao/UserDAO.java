/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import model.UserBean;

/**
 *
 * @author it3530219
 */
public interface UserDAO {
    public int createAccount(UserBean aSignUp);
    public int checkUserName(String userName);
    
    public int findAccount(UserBean aSignUp);
    public String[] findDetails(UserBean aLogin);
    public int findPendingAccount(UserBean aLogin);
    public int findDeniedAccount(UserBean aLogin);
    public String findUserAccountType(UserBean aLogin);
    
    public String retrievePassword(String userName);
    
    public ArrayList findByUserName(String aName);
    public int updateProfile(UserBean anUpdate);
    
}
