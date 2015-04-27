/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dao.SearchDAO;
import dao.SearchDAOImpl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.internet.ParseException;
import model.SearchBean;
import model.ViewBean;

/**
 *
 * @author it3530219
 */
@ManagedBean
@SessionScoped
public class SearchController {

    private SearchBean theModel;
    private ViewBean finalSelection;
    private String authorName;
    private String courseID;
    private String keywords;
    private Date startDate;
    private Date endDate;
    private List searchList;

    public SearchController() {
        theModel = new SearchBean();
        finalSelection = new ViewBean();
    }

    public SearchBean getTheModel() {
        return theModel;
    }

    public void setTheModel(SearchBean theModel) {
        this.theModel = theModel;
    }

    public String performSearch(String accountType) {
        authorName = theModel.getAuthorName();
        courseID = theModel.getCourseNo();
        keywords = theModel.getKeywords();
        startDate = theModel.getStartDate();
        endDate = theModel.getEndDate();
        int validationFlag = 0;

        
        /* Validate that the form contains valid search criteria */
        /* Check to see that the user's entered at least one search critera. */
        if ( authorName.equals("") &&
                theModel.getCourseNo().equals("") &&
                theModel.getKeywords().equals("") && 
                    ( theModel.getStartDate() == null && 
                      theModel.getEndDate() == null
                    )
            ){
            validationFlag++;
        }

        
        
//        /* If the user doesn't give an end date, but gives a start date, assume today. */
//        if (theModel.getEndDate() == null && theModel.getStartDate() != null){
//            Calendar c = new GregorianCalendar();
//            c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
//            c.set(Calendar.MINUTE, 0);
//            c.set(Calendar.SECOND, 0);
//            theModel.setEndDate(c.getTime());
//        }
//        
        /* If the user doesn't give a start date, but give an end date, assume 1900
         if (theModel.getStartDate() == null && theModel.getEndDate() != null){
         theModel.setStartDate(new GregorianCalendar(1900,1,1).getTime());
         }*/
        
        /* Ensure that startDate comes before endDate */
        if(theModel.getStartDate() != null && theModel.getEndDate() != null){
            if (theModel.getStartDate().compareTo(theModel.getEndDate()) > 0){
                /* Status message showing that the date range isn't valid. */
                validationFlag++;
            }
        }
        /* Perform search and stores the resultset in the bean if valid. */
        if (validationFlag == 0){
            SearchDAO searchDAO = new SearchDAOImpl();
            theModel.setResults(searchDAO.searchRequest(theModel));
        }else{
            /* Or nothing, invalid search terms */
        }

        /* Direct the page to the resultsPage screen if valid */
        if (!theModel.getResults().isEmpty()) {
            searchList = theModel.getResults();
          if(accountType.equalsIgnoreCase("student"))
            {
                return "searchResult.xhtml";
            }
            else{
                    return "adminSearchResult.xhtml";
                    }

        } else {
            return null;
        }

    }

    public String getDetails(int thesisID){
        /* set the view */
        SearchDAO searchDAO = new SearchDAOImpl();
        finalSelection = searchDAO.detailsRequest(thesisID);
        searchDAO.incrementViewCount(thesisID);
        return "viewDetails.xhtml";
    }

    public String showSimilar(String accountType){
        SearchDAO searchDAO = new SearchDAOImpl();
        theModel.setResults(searchDAO.findSimilar(finalSelection));
        searchList = theModel.getResults();
        if(accountType.equalsIgnoreCase("student"))
            {
                return "searchResult.xhtml";
            }
            else{
                    return "adminSearchResult.xhtml";
                    }
    }

    /**
     * @return the theResult
     */
    public ViewBean getFinalSelection() {
        return finalSelection;
    }

    /**
     * @param theResult the theResult to set
     */
    public void setFinalSelection(ViewBean finalSelection) {
        this.finalSelection = finalSelection;
    }

    /**
     * @return the searchList
     */
    public List getSearchList() {
        return searchList;
    }

    /**
     * @param searchList the searchList to set
     */
    public void setSearchList(List searchList) {
        this.searchList = searchList;
    }
    
    public String performMarkUnmark(int thesisId, String markStatus){
        SearchDAO aSearch = new SearchDAOImpl();
        if(markStatus.equalsIgnoreCase("mark")){
            
            int markRowCount = aSearch.performMark(thesisId);
//            finalSelection = aSearch.detailsRequest(thesisId);
            theModel.setResults(aSearch.searchRequest(theModel));
                
//            finalSelection.setHighlightStatus("Unmark");
        }
        else //if(markStatus.equalsIgnoreCase("unmark"))
        {
            
            int markRowCount = aSearch.performUnmark(thesisId);
            theModel.setResults(aSearch.searchRequest(theModel));
//            finalSelection = aSearch.detailsRequest(thesisId);
//            finalSelection.setHighlightStatus("Mark");
        }
        if (!theModel.getResults().isEmpty()) {
            searchList = theModel.getResults();
        return "adminSearchResult.xhtml";
        }
        return null;
    }
}
