/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dao.SubscriptionDAO;
import dao.SubscriptionDAOImpl;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.UserBean;


/**
 *
 * @author it3530219
 */
@ManagedBean
@SessionScoped
public class SubscriptionController {
    
    private UserBean theModel;
    private String userName;
    private List currentSubscriptions;
    private List availableSubscriptions;
    
    public SubscriptionController() {
        
    }
       
    public void populateCurrentSubscriptions(String ULID){
        SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();
        setCurrentSubscriptions(subscriptionDAO.getCurrentSubscriptions(ULID));
        
    }
    
    public void populateAvailableSubscriptions(String ULID){
        SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();
        setAvailableSubscriptions(subscriptionDAO.getAvailableSubscriptions(ULID));
    }
            
    public void addNewSubscription(String keyword, String ULID){
        SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();
        subscriptionDAO.addSubscription(ULID, keyword);
    }
    
    public void removeSubscription(String keyword, String ULID){
        SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();
        subscriptionDAO.removeSubscription(ULID, keyword);
    }

    /**
     * @return the currentSubscriptions
     */
    public List getCurrentSubscriptions() {
        return currentSubscriptions;
    }

    /**
     * @param currentSubscriptions the currentSubscriptions to set
     */
    public void setCurrentSubscriptions(List currentSubscriptions) {
        this.currentSubscriptions = currentSubscriptions;
    }

    /**
     * @return the availableSubscriptions
     */
    public List getAvailableSubscriptions() {
        return availableSubscriptions;
    }

    /**
     * @param availableSubscriptions the availableSubscriptions to set
     */
    public void setAvailableSubscriptions(List availableSubscriptions) {
        this.availableSubscriptions = availableSubscriptions;
    }
}
