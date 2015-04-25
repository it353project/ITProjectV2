/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import model.SearchBean;
import model.ViewBean;

/**
 *
 * @author it3530219
 */
public interface SearchDAO {
    public ArrayList searchRequest(SearchBean aSearch);    
    public ViewBean detailsRequest(int thesisID);
    public ArrayList findSimilar(ViewBean aView);
}
