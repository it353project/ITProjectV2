/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import java.util.List;
import model.SearchBean;
import model.ViewBean;

/**
 *
 * @author it3530219
 */
public interface SubscriptionDAO {
    public List getCurrentSubscriptions(String userID);
    public List getAvailableSubscriptions(String userID);
    public String[] getSubscriberEmails(String keyword);
    public void addSubscription(String userID, String keyword);
    public void removeSubscription(String userID, String keyword);
    public int findAccountIDByULID(String ULID);
}
