package com.himanshu.securepay.service;
import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import javax.crypto.Cipher; import javax.crypto.spec.GCMParameterSpec; import javax.crypto.spec.SecretKeySpec; import java.nio.charset.StandardCharsets; import java.security.SecureRandom; import java.util.Base64;
@Service public class PanCryptoService {
 private final byte[] key; private final SecureRandom random=new SecureRandom();
 public PanCryptoService(@Value("${securepay.encryption-key:}") String configuredKey){
  if(configuredKey==null || configuredKey.isBlank()) throw new IllegalStateException("Set SECUREPAY_ENCRYPTION_KEY to a Base64-encoded 32-byte key");
  try { key=Base64.getDecoder().decode(configuredKey); } catch(IllegalArgumentException e){throw new IllegalStateException("SECUREPAY_ENCRYPTION_KEY must be Base64",e);}
  if(key.length!=32) throw new IllegalStateException("SECUREPAY_ENCRYPTION_KEY must decode to 32 bytes");
 }
 public String encrypt(String pan){try{byte[] iv=new byte[12];random.nextBytes(iv);Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key,"AES"),new GCMParameterSpec(128,iv));byte[] encrypted=c.doFinal(pan.getBytes(StandardCharsets.UTF_8));byte[] combined=new byte[iv.length+encrypted.length];System.arraycopy(iv,0,combined,0,iv.length);System.arraycopy(encrypted,0,combined,iv.length,encrypted.length);return Base64.getEncoder().encodeToString(combined);}catch(Exception e){throw new IllegalStateException("PAN encryption failed",e);}}
}
