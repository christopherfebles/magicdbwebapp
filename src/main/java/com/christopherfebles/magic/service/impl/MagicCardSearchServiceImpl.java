package com.christopherfebles.magic.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.christopherfebles.magic.dao.SearchDAO;
import com.christopherfebles.magic.dao.parameter.SearchParameter;
import com.christopherfebles.magic.dao.parameter.SearchParameter.FieldName;
import com.christopherfebles.magic.enums.Language;
import com.christopherfebles.magic.form.model.DisplayMagicCard;
import com.christopherfebles.magic.form.model.SearchFormModel;
import com.christopherfebles.magic.model.MagicCard;
import com.christopherfebles.magic.service.MagicCardSearchService;

@Service
public class MagicCardSearchServiceImpl implements MagicCardSearchService {
    
    private static final Logger LOG = LoggerFactory.getLogger( MagicCardSearchServiceImpl.class );
    private static final int DEFAULT_PAGE_SIZE = 9;

    @Autowired
    private SearchDAO searchDAO;

    @Override
    public List<DisplayMagicCard> searchDatabaseByPage( SearchFormModel searchForm, int pageNumber ) {

        LOG.debug( "Searching with SearchFormModel object {}", searchForm );
        StopWatch timer = new StopWatch();
        timer.start();
        
        List<SearchParameter> searchParms = this.generateSearchParameters( searchForm );
        
        List<MagicCard> searchResults = searchDAO.getPageByNameWithSearchParametersAndPageSize( pageNumber, DEFAULT_PAGE_SIZE, searchParms );
        
        List<DisplayMagicCard> results = DisplayMagicCard.convertAllToDisplayMagicCards( searchResults );
        
        Collections.sort( results );
        timer.stop();
        this.logElapsedTime( "Database Search for page " + pageNumber, timer.getTime() );
        
        return results;
    }

    @Override
    public int numberOfPagesForSearch( SearchFormModel searchForm ) {
        return (int)Math.ceil( (double)this.numberOfResultsForSearch( searchForm ) / DEFAULT_PAGE_SIZE );
    }

    @Override
    public int numberOfResultsForSearch( SearchFormModel searchForm ) {
        
        LOG.debug( "Counting results for SearchFormModel object {}", searchForm );
        StopWatch timer = new StopWatch();
        timer.start();
        
        List<SearchParameter> searchParms = this.generateSearchParameters( searchForm );
        
        int numResults = searchDAO.numberOfUniqueNamesWithSearchParameters( searchParms );
        
        timer.stop();
        this.logElapsedTime( "Database Count ", timer.getTime() );
        
        return numResults;
    }
    
    @Override
    public List<DisplayMagicCard> searchDatabase( SearchFormModel searchForm ) {

        LOG.debug( "Searching with SearchFormModel object {}", searchForm );
        StopWatch timer = new StopWatch();
        timer.start();
        
        List<SearchParameter> searchParms = this.generateSearchParameters( searchForm );
        
        List<MagicCard> searchResults = searchDAO.getAllByNameWithSearchParameters( searchParms );
        
        List<DisplayMagicCard> results = DisplayMagicCard.convertAllToDisplayMagicCards( searchResults );
        Collections.sort( results );
        
        timer.stop();
        this.logElapsedTime( "Database Search All", timer.getTime() );
        
        return results;
    }

    /**
     * Generate a list of SearchParameters based on the given object's search criteria
     * 
     * @param    searchForm    The object to convert
     * @return    A list of SearchParameter objects that reflect the given object, or empty list if nothing set
     */
    private List<SearchParameter> generateSearchParameters( SearchFormModel searchForm ) {
        
        List<SearchParameter> retVal = new ArrayList<>();
        SearchParameter parameter = null;
        
        //Search by Name
        //Default StartsWith search
        String nameSearchText = searchForm.getCardName();
        if ( StringUtils.isNotEmpty( nameSearchText ) ) {
            if ( searchForm.isNameSearchContains() ) {
                nameSearchText = "%" + nameSearchText + "%";
            } else if ( searchForm.isNameSearchStartsWith() ) {
                //StartsWith
                nameSearchText = nameSearchText + "%";
            }
            //Else: exact match 
            parameter = new SearchParameter( FieldName.NAME, nameSearchText );
            retVal.add( parameter );
        }
        
        //Search by Color
        if ( searchForm.getColor() != null ) {
            parameter = new SearchParameter( FieldName.COLOR, searchForm.getColor(), searchForm.isAndColor() );
            retVal.add( parameter );
        }
        
        //Search by Type
        if ( searchForm.getType() != null ) {
            parameter = new SearchParameter( FieldName.TYPE, searchForm.getType(), searchForm.isAndType() );
            retVal.add( parameter );
        }
        
        //Search by SubType
        if ( searchForm.getSubType() != null ) {
            parameter = new SearchParameter( FieldName.SUBTYPE, searchForm.getSubType(), searchForm.isAndSubType() );
            retVal.add( parameter );
        }
        
        //Search by Expansion
        if ( StringUtils.isNotEmpty( searchForm.getExpansion() ) ) {
            parameter = new SearchParameter( FieldName.EXPANSION, searchForm.getExpansion(), searchForm.isAndExpansion() );
            retVal.add( parameter );
        }
        
        //Search by Owned Cards
        if ( searchForm.isOwnedCardsOnly() ) {
            parameter = new SearchParameter( FieldName.OWNED, "", true );
            retVal.add( parameter );
        }
        
        //Hardcode English search for now
        parameter = new SearchParameter( FieldName.LANGUAGE, Language.ENGLISH );
        retVal.add( parameter );
        
        return retVal;
    }
    
    /**
     * Log the given elapsed time to the DEBUG log
     * 
     * @see     StopWatch#getTime()
     * @param prefix    The log message to print before the elapsed time
     * @param elapsedTime   The elapsed time in milliseconds
     */
    private void logElapsedTime( String prefix, long elapsedTime ) {
        
        long elapsedTimeMillis = elapsedTime;
        
        long hr = TimeUnit.MILLISECONDS.toHours( elapsedTimeMillis );
        long min = TimeUnit.MILLISECONDS.toMinutes( elapsedTimeMillis - TimeUnit.HOURS.toMillis( hr ) );
        long sec = TimeUnit.MILLISECONDS.toSeconds( elapsedTimeMillis - TimeUnit.HOURS.toMillis( hr ) - TimeUnit.MINUTES.toMillis( min ) );
        long ms = TimeUnit.MILLISECONDS.toMillis( elapsedTimeMillis - TimeUnit.HOURS.toMillis( hr ) - TimeUnit.MINUTES.toMillis( min )
                - TimeUnit.SECONDS.toMillis( sec ) );
        
        String timestamp = String.format( "%d:%d:%d.%d", hr, min, sec, ms );
        
        LOG.debug( "{} Elapsed Time: {}", prefix, timestamp );
    }
}
