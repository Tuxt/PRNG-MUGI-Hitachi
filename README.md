# PRNG MUGI (Hitachi)
A software implementation of MUGI (Hitachi Pseudorandom Number Generator)

This repository contains a Java implementation of the MUGI pseudo-random number generator, made as a practice for a subject of the university.

Specification Ver. 1.2
http://www.hitachi.com/rd/yrl/crypto/mugi/mugi_spe.pdf


Use:

    String key = "0BA78F9D00719CDEBBC79A6CE99EBCE2";  // 128-bit (16 bytes) key as a hexadecimal String
    String iv = "7744BEC800183BD6AAC729C8FF029EE21";  // 128-bit (16 bytes) initial vector as a hexadecimal String
    Mugi prng = new Mugi(key,iv);
    prng.next();                                      // Returns the next 64-bit (8 bytes) pseudo-random number as a hexadecimal String


