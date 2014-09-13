package com.christopherfebles.magic.form.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.christopherfebles.magic.dao.SearchDAO;
import com.christopherfebles.magic.dao.parameter.SearchParameter;
import com.christopherfebles.magic.dao.parameter.SearchParameter.FieldName;
import com.christopherfebles.magic.enums.Color;
import com.christopherfebles.magic.enums.Language;
import com.christopherfebles.magic.enums.Type;
import com.christopherfebles.magic.model.MagicCard;
import com.christopherfebles.magic.service.MagicCardSearchService;
import com.christopherfebles.magic.testsupport.DAOTester;
import com.christopherfebles.magic.testsupport.UnitTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = { "classpath:/applicationContext-test.xml" } )
@Category( UnitTest.class )
public class DisplayMagicCardTest extends DAOTester {

    private static final Logger LOG = LoggerFactory.getLogger( DisplayMagicCardTest.class );
    
    @Autowired
    private MagicCardSearchService service;

    @Autowired
    private SearchDAO searchDAO;
    
    @Test
    public void testHTMLTextGeneration() throws Exception {
        String text = "Fireball deals X damage divided evenly, rounded down, among any number of target creatures and/or players. Fireball costs {1} more to cast for each target beyond the first.";
        String expectedText = "Fireball deals X damage divided evenly, rounded down, among any number of target creatures and/or players. <br>Fireball costs <img src=\"images/1.gif\" class=\"manaImg\" /> more to cast for each target beyond the first.";
        
        String html = Whitebox.invokeMethod( 
                Whitebox.invokeConstructor( DisplayMagicCard.class ), 
                "convertToHTML", text );
     
        assertEquals( expectedText, html );
    }
    
    @Test
    public void testManaFilenameWithPhyrexianMana() {
        MagicCard actOfAggression = new MagicCard( 230076, "Act of Aggression", "3RPRP", Type.INSTANT.toString(), "New Phyrexia" );
        actOfAggression.setText( "({RP} can be paid with either {R} or 2 life.) Gain control of target creature an opponent controls until end of turn. Untap that creature. It gains haste until end of turn." );
        actOfAggression.setRarity( "Uncommon" );

        DisplayMagicCard card = new DisplayMagicCard( actOfAggression );
        List<String> manaFilenames = card.getManaFilename();
        
        for( String manaFilename : manaFilenames ) {
            assertTrue( "3.gif".equals( manaFilename ) || "RP.png".equals( manaFilename ) );
        }
    }
    
    @Test
    public void testParentMultiverseIDWithIsland() {
        SearchFormModel searchForm = new SearchFormModel();
        searchForm.setColor( Color.COLORLESS );
        searchForm.setCardName( "Island" );
        
        List<DisplayMagicCard> islandResults = service.searchDatabase( searchForm );
        for( DisplayMagicCard card : islandResults ) {
            assertEquals( card.getMultiverseId(), card.getParentMultiverseId() );
        }
    }
    
    @Test
    public void testConvertAllToDisplayMagicCards() {

        List<SearchParameter> searchParams = new ArrayList<>();
        searchParams.add( new SearchParameter( FieldName.NAME, "Island" ) );
        searchParams.add( new SearchParameter( FieldName.LANGUAGE, Language.ENGLISH ) );
        
        int totalCards = searchDAO.numberOfResultsWithSearchParameters( searchParams );
        assertTrue( totalCards > 0 );
        assertEquals( 360, totalCards ); //The current number of English Island cards in the database
        
        List<MagicCard> cardList = searchDAO.getAllByNameWithSearchParameters( searchParams );
        List<DisplayMagicCard> displayCards = DisplayMagicCard.convertAllToDisplayMagicCards( cardList );
        
        //Since we only searched for one card, Island, there should only be one DisplayCard
        assertEquals( 1, displayCards.size() );
        
        int cardCount = 0;
        for( DisplayMagicCard card : displayCards ) {
            cardCount++;
            cardCount += card.getAlternateVersions().size();
        }
        assertEquals( cardCount, totalCards );
    }
    
    @Test
    public void testParentMultiverseID() {
        DisplayMagicCard card = new DisplayMagicCard( this.setUpDemonicHordes() );
        
        DisplayMagicCard alternate1 = new DisplayMagicCard( this.setUpDemonicHordes() );
        alternate1.setMultiverseId( card.getMultiverseId() + 1 );
        card.addAlternateVersion( alternate1 );
        
        DisplayMagicCard alternate2 = new DisplayMagicCard( this.setUpDemonicHordes() );
        alternate2.setMultiverseId( card.getMultiverseId() + 2 );
        card.addAlternateVersion( alternate2 );
        
        assertEquals( card.getMultiverseId(), card.getParentMultiverseId() );
    }
    
    @Test
    public void testGenerateJSON() {
        DisplayMagicCard card = new DisplayMagicCard( this.setUpDemonicHordes() );
        
        DisplayMagicCard alternate1 = new DisplayMagicCard( this.setUpDemonicHordes() );
        alternate1.setMultiverseId( card.getMultiverseId() + 1 );
        card.addAlternateVersion( alternate1 );
        
        DisplayMagicCard alternate2 = new DisplayMagicCard( this.setUpDemonicHordes() );
        alternate2.setMultiverseId( card.getMultiverseId() + 2 );
        card.addAlternateVersion( alternate2 );
        
        String json = card.getJSONString();
        assertNotNull( json );
//        System.err.println(json);
    }
    
    @Test
    public void testGetHTMLText() {
        DisplayMagicCard card = new DisplayMagicCard( this.setUpDemonicHordes() );
        String htmlText = card.getText();
        
        assertNotNull( htmlText );
        assertTrue( htmlText.contains( "img" ));
    }
    
    @Test
    public void testCardNoLoyalty() {
        DisplayMagicCard noLoyalty = new DisplayMagicCard( this.setUpDemonicHordes() );
        assertNull( noLoyalty.getLoyalty() );
        assertEquals( "5", noLoyalty.getPower() );
    }
    
    @Test
    public void testCardLoyalty() {
        DisplayMagicCard loyalty = new DisplayMagicCard( this.setUpPlaneswalker() );
        assertNull( loyalty.getPower() );
        assertEquals( "5", loyalty.getLoyalty() );
    }
    
    private MagicCard setUpDemonicHordes() {
        MagicCard demonicHordes = new MagicCard( 59, "Demonic Hordes", "3BBB", Type.CREATURE.toString() + Type.TYPE_SEPARATOR_WITH_SPACES + "Demon", "Limited Edition Alpha" );
        demonicHordes.setText( "{T}: Destroy target land. At the beginning of your upkeep, unless you pay {B}{B}{B}, tap Demonic Hordes and sacrifice a land of an opponent's choice." );
        demonicHordes.setFlavorText( "Created to destroy Dominia, Demons can sometimes be bent to a more focused purpose." );
        demonicHordes.setRarity( "Rare" );
        demonicHordes.setPower( "5" );
        demonicHordes.setToughness( "5" );
        
        return demonicHordes;
    }
    
    private MagicCard setUpPlaneswalker() {
        MagicCard planeswalker = new MagicCard( 205957, "Ajani Goldmane", "2WW", Type.PLANESWALKER.toString() + Type.TYPE_SEPARATOR_WITH_SPACES + "Ajani", "Magic 2011" );
        planeswalker.setText( "+1: You gain 2 life. −1: Put a +1/+1 counter on each creature you control. Those creatures gain vigilance until end of turn. "
                + "−6: Put a white Avatar creature token onto the battlefield. It has \"This creature's power and toughness are each equal to your life total.\"" );
        planeswalker.setFlavorText( "Created to destroy Dominia, Demons can sometimes be bent to a more focused purpose." );
        planeswalker.setRarity( "Mythic Rare" );
        planeswalker.setPower( "5" );
        
        return planeswalker;
    }
    
    protected static boolean magicCardTesterInitializationComplete = false;
    @Override
    public void additionalSetUp() {
        
        if ( !magicCardTesterInitializationComplete ) {
            LOG.trace( "Initializing embedded database for MagicCard tests." );
            
            //Add other Islands
            super.addAllIslands();
            
            magicCardTesterInitializationComplete = true;
        }
    }
}
