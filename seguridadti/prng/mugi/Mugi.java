package seguridadti.prng.mugi;

import java.math.BigInteger;

/**
 * 
 * @author Juan Carlos Garcia Torrecilla
 * @date November 2014
 * 
 * PRNG MUGI software implementation
 * 
 */
public class Mugi {
    
    // Registers
    static String a;    // State a
    static String b;    // Buffer b
    
    // sBox
    static final int[] sBox = {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
                               0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
                               0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
                               0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
                               0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
                               0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
                               0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
                               0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
                               0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
                               0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
                               0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
                               0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
                               0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
                               0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
                               0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
                               0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16};
    
    // Mul 2 in GF(2^8) for MDS Matrix
    static final int[] mul2 = {0x00, 0x02, 0x04, 0x06, 0x08, 0x0a, 0x0c, 0x0e, 0x10, 0x12, 0x14, 0x16, 0x18, 0x1a, 0x1c, 0x1e,
                               0x20, 0x22, 0x24, 0x26, 0x28, 0x2a, 0x2c, 0x2e, 0x30, 0x32, 0x34, 0x36, 0x38, 0x3a, 0x3c, 0x3e,
                               0x40, 0x42, 0x44, 0x46, 0x48, 0x4a, 0x4c, 0x4e, 0x50, 0x52, 0x54, 0x56, 0x58, 0x5a, 0x5c, 0x5e,
                               0x60, 0x62, 0x64, 0x66, 0x68, 0x6a, 0x6c, 0x6e, 0x70, 0x72, 0x74, 0x76, 0x78, 0x7a, 0x7c, 0x7e,
                               0x80, 0x82, 0x84, 0x86, 0x88, 0x8a, 0x8c, 0x8e, 0x90, 0x92, 0x94, 0x96, 0x98, 0x9a, 0x9c, 0x9e,
                               0xa0, 0xa2, 0xa4, 0xa6, 0xa8, 0xaa, 0xac, 0xae, 0xb0, 0xb2, 0xb4, 0xb6, 0xb8, 0xba, 0xbc, 0xbe,
                               0xc0, 0xc2, 0xc4, 0xc6, 0xc8, 0xca, 0xcc, 0xce, 0xd0, 0xd2, 0xd4, 0xd6, 0xd8, 0xda, 0xdc, 0xde,
                               0xe0, 0xe2, 0xe4, 0xe6, 0xe8, 0xea, 0xec, 0xee, 0xf0, 0xf2, 0xf4, 0xf6, 0xf8, 0xfa, 0xfc, 0xfe,
                               0x1b, 0x19, 0x1f, 0x1d, 0x13, 0x11, 0x17, 0x15, 0x0b, 0x09, 0x0f, 0x0d, 0x03, 0x01, 0x07, 0x05,
                               0x3b, 0x39, 0x3f, 0x3d, 0x33, 0x31, 0x37, 0x35, 0x2b, 0x29, 0x2f, 0x2d, 0x23, 0x21, 0x27, 0x25,
                               0x5b, 0x59, 0x5f, 0x5d, 0x53, 0x51, 0x57, 0x55, 0x4b, 0x49, 0x4f, 0x4d, 0x43, 0x41, 0x47, 0x45,
                               0x7b, 0x79, 0x7f, 0x7d, 0x73, 0x71, 0x77, 0x75, 0x6b, 0x69, 0x6f, 0x6d, 0x63, 0x61, 0x67, 0x65,
                               0x9b, 0x99, 0x9f, 0x9d, 0x93, 0x91, 0x97, 0x95, 0x8b, 0x89, 0x8f, 0x8d, 0x83, 0x81, 0x87, 0x85,
                               0xbb, 0xb9, 0xbf, 0xbd, 0xb3, 0xb1, 0xb7, 0xb5, 0xab, 0xa9, 0xaf, 0xad, 0xa3, 0xa1, 0xa7, 0xa5,
                               0xdb, 0xd9, 0xdf, 0xdd, 0xd3, 0xd1, 0xd7, 0xd5, 0xcb, 0xc9, 0xcf, 0xcd, 0xc3, 0xc1, 0xc7, 0xc5,
                               0xfb, 0xf9, 0xff, 0xfd, 0xf3, 0xf1, 0xf7, 0xf5, 0xeb, 0xe9, 0xef, 0xed, 0xe3, 0xe1, 0xe7, 0xe5};
    
    // Constants
    static final long C0 = 0x6A09E667F3BCC908L;
    static final long C1 = 0xBB67AE8584CAA73BL;
    static final long C2 = 0x3C6EF372FE94F82BL;
    
    /**
     * @param args[0] K Hexadecimal Secret Key (128 bits)
     * @param args[1] I Hexadecimal Initial Vector (128 bits)
     * @param args[2] n Number of units
    */
    public static void main(String[] args){
        init(args[0], args[1]);
        long inicio = System.currentTimeMillis();
        int n = Integer.valueOf(args[2]);
        for(int i=1; i<=n; i++){
            fUpdate();
            System.out.println(a.substring(32,48));
        }
        long fin = System.currentTimeMillis();
        System.out.println(n+" NUMEROS: "+(fin-inicio)+" ms\t"+n/(fin-inicio));
    }
    
    
    /**
     * Rijndael (AES) substitution Box
     * SubBytes AES function
     * 
     * @param val
     * @return The value of the box
     */
    static int box(int val){
        return sBox[val];
    }
    
    /**
     * Rijndael (AES) MDS Matrix
     * MixColumns AES function
     * 
     * @param i1
     * @param i2
     * @param i3
     * @param i4
     * @return Multiply the initial vector in GF(2^8) by an MDS matrix
     */
    static int[] M(int i1, int i2, int i3, int i4){
        int[] ret = new int[4];
        ret[0] = mul2[i1] ^ (mul2[i2] ^ i2) ^ i3 ^ i4;
        ret[1] = i1 ^ mul2[i2] ^ (mul2[i3] ^ i3) ^ i4;
        ret[2] = i1 ^ i2 ^ mul2[i3] ^ (mul2[i4] ^ i4);
        ret[3] = (mul2[i1] ^ i1) ^ i2 ^ i3 ^ mul2[i4];
        return ret;
    }
    
    /**
     * The F-function is composition of:
     * a key addition (the data addition from buffer),
     * a non-linear transformation using the S-box,
     * a linear transformation using MDS matrix M
     * and byte shuffling.
     * 
     * @param in1
     * @param in2
     * @return The result of F-function over in1 and in2
     */
    static long fFunction(long in1, long in2){
        int[] a = new int[8];
        int[] b = new int[8];
        String tmp1 = String.format("%016X", in1);
        String tmp2 = String.format("%016X", in2);
        
        for(int i=0;i<8;i++){
            a[i] = Integer.parseInt(tmp1.substring(i*2, i*2+2), 16);
            b[i] = Integer.parseInt(tmp2.substring(i*2, i*2+2), 16);
            
            // XOR
            a[i] = a[i] ^ b[i];
            
            // sBox
            a[i] = box(a[i]);
        }
        
        // MDS Matrix
        int[] m1 = M(a[0], a[1], a[2], a[3]);
        int[] m2 = M(a[4], a[5], a[6], a[7]);
        
        // Concat
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02X", m2[0]));
        sb.append(String.format("%02X", m2[1]));
        sb.append(String.format("%02X", m1[2]));
        sb.append(String.format("%02X", m1[3]));
        sb.append(String.format("%02X", m1[0]));
        sb.append(String.format("%02X", m1[1]));
        sb.append(String.format("%02X", m2[2]));
        sb.append(String.format("%02X", m2[3]));
        return new BigInteger(sb.toString(), 16).longValue();
    }
    
    /**
     * Rho is the update function of state a.
     * It is a kind of target heavy Feistel structure with two F-functions and
     * uses buffer b as a parameter.
     * 
     * @param in1
     * @param in2
     * @return The next value of state a
     */
    static String rho(String in1, String in2){
        // Inputs to long
        long[] a = new long[3];
        long[] b = new long[2];                                         // Only use b4 and b10
        a[0] = new BigInteger(in1.substring(0, 16), 16).longValue();    // Long.parseLong(str, 16) not valid for 0x8000000000000000 or above
        a[1] = new BigInteger(in1.substring(16,32), 16).longValue();
        a[2] = new BigInteger(in1.substring(32,48), 16).longValue();
        
        b[0] = new BigInteger(in2.substring(4*16, 5*16), 16).longValue();
        b[1] = (new BigInteger(in2.substring(10*16,11*16), 16).longValue() << 17) | (new BigInteger(in2.substring(10*16,11*16), 16).longValue() >>> Long.SIZE-17);
        
        // Output to String
        StringBuilder output = new StringBuilder();
        output.append(in1.substring(16,32));                                            // a1
        output.append(String.format("%016X", C1 ^ (a[2] ^ fFunction(a[1],b[0]))));      // C1 xor (a2 xor F(a1,b4))
        output.append(String.format("%016X", C2 ^ (a[0] ^ fFunction(a[1],b[1]))));      // C2 xor (a0 xor F(a1,b10 <<17))
        return output.toString();
    }
    
    /**
     * Lambda is the update function of buffer b.
     * It is a linear transformation of b.
     * 
     * @param b
     * @param a
     * @return The next value of buffer b
     */
    static String lambda(String b, String a){
        String tmpB = b;
        String tmpA = a;
        StringBuilder sb = new StringBuilder();
        long tmp1;
        long tmp2;
        for(int i=0;i<16;i++){
            if(i==0){
                tmp1 = new BigInteger(tmpB.substring(15*16, 16*16), 16).longValue();
                tmp2 = new BigInteger(tmpA.substring(0,16), 16).longValue();
                sb.append(String.format("%016X", tmp1^tmp2));
            }else if(i==4){
                tmp1 = new BigInteger(tmpB.substring(3*16, 4*16), 16).longValue();
                tmp2 = new BigInteger(tmpB.substring(7*16, 8*16), 16).longValue();
                sb.append(String.format("%016X", tmp1^tmp2));
            }else if(i==10){
                tmp1 = new BigInteger(tmpB.substring(9*16, 10*16), 16).longValue();
                tmp2 = new BigInteger(tmpB.substring(13*16, 14*16), 16).longValue();
                tmp2 = (tmp2 << 32) | (tmp2 >>> 32);
                sb.append(String.format("%016X", tmp1^tmp2));
            }else{
                sb.append(tmpB.substring((i-1)*16 ,i*16));
            }
        }
        return sb.toString();
    }
    
    /**
     * Updates state a and buffer b
     */
    static void fUpdate(){
        String tmpA = a;
        String tmpB = b;
        a = rho(tmpA, tmpB);
        b = lambda(tmpB, tmpA);
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
    static void init(String k, String iv){
        // Round -49: Key to a
        long k0 = new BigInteger(k.substring(0, 16), 16).longValue();
        long k1 = new BigInteger(k.substring(16, 32), 16).longValue();
        StringBuilder sb = new StringBuilder();
        sb.append(k);
        k0 = (k0 << 7) | (k0 >>> Long.SIZE-7);      // k0 << 7
        k1 = (k1 << Long.SIZE-7) | (k1 >>> 7);      // k1 >>> 7
        sb.append(String.format("%016X", k0 ^ k1 ^C0));
        a = sb.toString();
        
        // Rounds -48 to -33: Mixing by rho     b(15-i) = (rhoi+1(a,0))0
        String[] unitsOfB = new String[16];
        for(int i=0;i<16;i++){
            a = rho(a,"0000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000");
            unitsOfB[15-i] = a.substring(0, 16);
        }
        sb = new StringBuilder();
        for(int i=0; i<16; i++)
            sb.append(unitsOfB[i]);
        b = sb.toString();
        
        // Round -32: IV
        long a0 = new BigInteger(a.substring(0, 16), 16).longValue();
        long a1 = new BigInteger(a.substring(16, 32), 16).longValue();
        long a2 = new BigInteger(a.substring(32, 48), 16).longValue();
        long iv0 = new BigInteger(iv.substring(0, 16), 16).longValue();
        long iv1 = new BigInteger(iv.substring(16, 32), 16).longValue();
        
        a0 = a0 ^ iv0;                              // a0 = a0 xor IV0
        a1 = a1 ^ iv1;                              // a1 = a1 xor IV1
        iv0 = (iv0 << 7) | (iv0 >>> Long.SIZE-7);
        iv1 = (iv1 << Long.SIZE-7) | (iv1 >>> 7);
        a2 = a2 ^ iv0 ^ iv1 ^ C0;                   // a2 = a2 xor (IV0 << 7) xor (IV1 >>> 7) xor C0
        sb = new StringBuilder();
        sb.append(String.format("%016X", a0));
        sb.append(String.format("%016X", a1));
        sb.append(String.format("%016X", a2));
        a = sb.toString();
        
        // Rounds -31 to -16: Mixing by rho
        for(int i=0;i<16;i++)
            a = rho(a,"0000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000000000000000000000000000000"+
                    "000000000000000000000000000000");
        
        // Round -15 to -1: Mixing by fUpdate
        for(int i=0;i<15;i++){
            fUpdate();
        }
    }
}
