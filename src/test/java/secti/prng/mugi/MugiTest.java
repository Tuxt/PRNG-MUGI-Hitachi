package secti.prng.mugi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Juan Carlos García Torrecilla
 * @date January 2019
 */
public class MugiTest {
    
    @Test
    public void testSpecificationTestVector1() {
        System.out.println("TEST1: Specification Test Vector 1");
        Mugi instance = new Mugi("00000000000000000000000000000000", "00000000000000000000000000000000");
        String[] expResult = {"C76E14E70836E6B6", "CB0E9C5A0BF03E1E",
            "0ACF9AF49EBE6D67", "D5726E374B1397AC",
            "DAC3838528C1E592", "8A132730EF2BB752",
            "BD6229599F6D9AC2", "7C04760502F1E182"};
        for (String expRes : expResult)
            assertEquals(expRes, instance.next());
    }
    
    @Test
    public void testSpecificationTestVector2() {
        System.out.println("TEST2: Specification Test Vector 2");
        Mugi instance = new Mugi("000102030405060708090A0B0C0D0E0F", "F0E0D0C0B0A090807060504030201000");
        String[] expResult = {"BC62430614B79B71", "71A66681C35542DE",
            "7ABA5B4FB80E82D7", "0B96982890B6E143",
            "4930B5D033157F46", "B96ED8499A282645",
            "DBEB1EF16D329B15", "34A9192C4DDCF34E"};
        for (String expRes : expResult)
            assertEquals(expRes, instance.next());
    }
    
    @Test
    public void testReset() {
        System.out.println("TEST3: Reset");
        Mugi instance = new Mugi("00000000000000000000000000000000", "00000000000000000000000000000000");
        String[] expResult1 = {"C76E14E70836E6B6", "CB0E9C5A0BF03E1E",
            "0ACF9AF49EBE6D67", "D5726E374B1397AC",
            "DAC3838528C1E592", "8A132730EF2BB752",
            "BD6229599F6D9AC2", "7C04760502F1E182"};
        String[] expResult2 = {"BC62430614B79B71", "71A66681C35542DE",
            "7ABA5B4FB80E82D7", "0B96982890B6E143",
            "4930B5D033157F46", "B96ED8499A282645",
            "DBEB1EF16D329B15", "34A9192C4DDCF34E"};
        for (String expRes : expResult1)
            assertEquals(expRes, instance.next());
        instance.reset("000102030405060708090A0B0C0D0E0F", "F0E0D0C0B0A090807060504030201000");
        for (String expRes : expResult2)
            assertEquals(expRes, instance.next());
    }
    
}
