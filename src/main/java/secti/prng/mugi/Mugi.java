package secti.prng.mugi;

import java.math.BigInteger;

/**
 *
 * @author Juan Carlos García Torrecilla
 * @date January 2019
 */
public class Mugi {
    
    // Registers
    private String stateA;
    private String stateB;
    
    
    public Mugi(String key, String iv) {
        this.init(key,iv);
    }
    
    /**
     * Initializes the internal state
     * Firstly initialize buffer b with a secret key "k"
     * Secondly initialize state a with an initial vector "iv"
     * Finally, mix whole internal state
     * 
     * @param k
     * @param iv 
     */
    private void init(String k, String iv){
        // Round -49: Key to a
        long k0 = new BigInteger(k.substring(0, 16), 16).longValue();
        long k1 = new BigInteger(k.substring(16, 32), 16).longValue();
        StringBuilder sb = new StringBuilder();
        sb.append(k);
        k0 = (k0 << 7) | (k0 >>> Long.SIZE-7);      // k0 << 7
        k1 = (k1 << Long.SIZE-7) | (k1 >>> 7);      // k1 >>> 7
        sb.append(String.format("%016X", k0 ^ k1 ^ MugiFuncs.C0));
        this.stateA = sb.toString();
        
        // Rounds -48 to -33: Mixing by rho     b(15-i) = (rhoi+1(a,0))0
        String[] unitsOfB = new String[16];
        for(int i=0;i<16;i++){
            this.stateA = MugiFuncs.rho(this.stateA,
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "0000000000000000000000000000");
            unitsOfB[15-i] = this.stateA.substring(0, 16);
        }
        sb = new StringBuilder();
        for(int i=0; i<16; i++)
            sb.append(unitsOfB[i]);
        this.stateB = sb.toString();
        
        // Round -32: IV
        long a0 = new BigInteger(this.stateA.substring(0, 16), 16).longValue();
        long a1 = new BigInteger(this.stateA.substring(16, 32), 16).longValue();
        long a2 = new BigInteger(this.stateA.substring(32, 48), 16).longValue();
        long iv0 = new BigInteger(iv.substring(0, 16), 16).longValue();
        long iv1 = new BigInteger(iv.substring(16, 32), 16).longValue();
        
        a0 = a0 ^ iv0;                              // a0 = a0 xor IV0
        a1 = a1 ^ iv1;                              // a1 = a1 xor IV1
        iv0 = (iv0 << 7) | (iv0 >>> Long.SIZE-7);
        iv1 = (iv1 << Long.SIZE-7) | (iv1 >>> 7);
        a2 = a2 ^ iv0 ^ iv1 ^ MugiFuncs.C0;                   // a2 = a2 xor (IV0 << 7) xor (IV1 >>> 7) xor C0
        sb = new StringBuilder();
        sb.append(String.format("%016X", a0));
        sb.append(String.format("%016X", a1));
        sb.append(String.format("%016X", a2));
        this.stateA = sb.toString();
        
        // Rounds -31 to -16: Mixing by rho
        for(int i=0;i<16;i++)
            this.stateA = MugiFuncs.rho(this.stateA,
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "0000000000000000000000000000");
        
        // Round -15 to -1: Mixing by fUpdate
        for(int i=0;i<15;i++){
            this.fUpdate();
        }
    }
    
    
    /**
     * Updates state a and buffer b
     */
    private void fUpdate(){
        String tmpA = this.stateA;
        String tmpB = this.stateB;
        this.stateA = MugiFuncs.rho(tmpA, tmpB);
        this.stateB = MugiFuncs.lambda(tmpB, tmpA);
    }
    
    public String next() {
        this.fUpdate();
        return this.stateA.substring(32,48);
    }
    
    public void reset(String key, String iv) {
        init(key,iv);
    }
}
