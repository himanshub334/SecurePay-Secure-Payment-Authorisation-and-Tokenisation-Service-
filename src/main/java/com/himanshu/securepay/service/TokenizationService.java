package com.himanshu.securepay.service;
import com.himanshu.securepay.dto.*; import com.himanshu.securepay.entity.CardToken; import com.himanshu.securepay.repository.CardTokenRepository; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.UUID;
@Service public class TokenizationService {
 private final CardTokenRepository repo; private final PanCryptoService crypto;
 public TokenizationService(CardTokenRepository repo,PanCryptoService crypto){this.repo=repo;this.crypto=crypto;}
 @Transactional public TokenizeResponse tokenize(String pan){if(!luhnValid(pan)) throw new IllegalArgumentException("PAN failed checksum validation");String token="tok_"+UUID.randomUUID().toString().replace("-","");CardToken saved=repo.save(new CardToken(token,crypto.encrypt(pan),pan.substring(pan.length()-4)));return new TokenizeResponse(saved.getToken(),saved.getLastFour(),saved.getCreatedAt());}
 private boolean luhnValid(String pan){int sum=0;boolean doubleDigit=false;for(int i=pan.length()-1;i>=0;i--){int n=pan.charAt(i)-'0';if(doubleDigit){n*=2;if(n>9)n-=9;}sum+=n;doubleDigit=!doubleDigit;}return sum%10==0;}
}
