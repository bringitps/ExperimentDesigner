package com.bringit.experiment.util;

import org.apache.commons.codec.binary.Base64;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class M5Encryption {
	
	private static byte[] sharedvector = {
		    0x01, 0x02, 0x03, 0x05, 0x07, 0x0B, 0x0D, 0x11
		    };
	
	public static String encrypt(String dataToEncrypt)
	{
	        String EncText = "";
	        byte[] keyArray = new byte[24];
	        byte[] temporaryKey;
	        String key = new Config().getProperty("encryptionkey");
	        byte[] toEncryptArray = null;
	  
	        try
	        {
	 
	            toEncryptArray =  dataToEncrypt.getBytes("UTF-8");        
	            MessageDigest m = MessageDigest.getInstance("MD5");
	            temporaryKey = m.digest(key.getBytes("UTF-8"));
	 
	            if(temporaryKey.length < 24) // DESede require 24 byte length key
	            {
	                int index = 0;
	                for(int i=temporaryKey.length;i< 24;i++)
	                {                   
	                    keyArray[i] =  temporaryKey[index];
	                }
	            }        
	 
	            Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");            
	            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));            
	            byte[] encrypted = c.doFinal(toEncryptArray);            
	            EncText = Base64.encodeBase64String(encrypted);
	 
	        }
	        catch(NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException NoEx)
	        {
	            System.err.println("Problem encrypting the data");
	            NoEx.printStackTrace();
	        }
	 
	        return EncText;        
	}
	
	 public static String decrypt(String dataToDecrypt)
	 {
		 	
	        String RawText = "";
	        byte[] keyArray = new byte[24];
	        byte[] temporaryKey;
	        String key = new Config().getProperty("encryptionkey");
	        
	        try
	        {
	            MessageDigest m = MessageDigest.getInstance("MD5");
	            temporaryKey = m.digest(key.getBytes("UTF-8"));           
	 
	            if(temporaryKey.length < 24) // DESede require 24 byte length key
	            {
	                int index = 0;
	                for(int i=temporaryKey.length;i< 24;i++)
	                {                  
	                    keyArray[i] =  temporaryKey[index];
	                }
	            }
	            
	            Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
	            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
	            byte[] decrypted = c.doFinal(Base64.decodeBase64(dataToDecrypt));   
	 
	            RawText = new String(decrypted, "UTF-8");                    
	        }
	        catch(NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException NoEx)
	        {
	            System.err.println("Problem decrypting the data");
	            NoEx.printStackTrace();
	        }      
	 
	        return RawText; 
	 
	}	 
}
