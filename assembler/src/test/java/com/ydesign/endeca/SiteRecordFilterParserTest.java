package com.ydesign.endeca;

import com.ydg.endeca.utils.SiteRecordFilterParser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for  SiteRecordFilterParser.
 */
public class SiteRecordFilterParserTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SiteRecordFilterParserTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SiteRecordFilterParserTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        String storeId = SiteRecordFilterParser.getStoreIdFromRecordFilter("AND(Store_id:yv,blah:news)");
        assertNotNull(storeId);
        assertEquals("yv",storeId);

        storeId = SiteRecordFilterParser.getStoreIdFromRecordFilter("Store_id:yv");
        assertNotNull(storeId);
        assertEquals("yv",storeId);
        
        storeId = SiteRecordFilterParser.getStoreIdFromRecordFilter("AND(blah:news)");
        assertNull(storeId);
        
        
        storeId = SiteRecordFilterParser.getStoreIdFromRecordFilter("Store_id:yb");
        assertNotNull(storeId);
        assertEquals("yb",storeId);
        
    }
}
