package com.christopherfebles.magic.service;

import java.util.List;

import com.christopherfebles.magic.form.model.DisplayMagicCard;
import com.christopherfebles.magic.form.model.SearchFormModel;

public interface MagicCardSearchService {

    /**
     * Search the database with the search criteria specified by the given form
     * 
     * @param searchForm    Search criteria
     * @return    A list of cards loaded from the database
     */
    List<DisplayMagicCard> searchDatabase( SearchFormModel searchForm );
    
    /**
     * Search the database with the search criteria specified by the given form one page at a time
     * 
     * @param searchForm    Search criteria
     * @param pageNumber    The page of results to load from the database
     * @return    A list of cards loaded from the database
     */
    List<DisplayMagicCard> searchDatabaseByPage( SearchFormModel searchForm, int pageNumber );
    
    /**
     * The number of pages that will be returned by the given search criteria
     * 
     * @param     searchForm    Search criteria
     * @return    The total number of pages that will be returned
     */
    int numberOfPagesForSearch( SearchFormModel searchForm );

    /**
     * The number of DisplayMagicCard that will be returned by the given search criteria
     * 
     * @param     searchForm    Search criteria
     * @return    The total number of pages that will be returned
     */
    int numberOfResultsForSearch( SearchFormModel searchForm );
}
