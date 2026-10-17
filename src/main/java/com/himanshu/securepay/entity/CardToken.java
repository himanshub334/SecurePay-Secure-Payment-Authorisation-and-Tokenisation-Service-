package com.himanshu.securepay.entity;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="card_tokens")
public class CardToken {
 @Id @Column(length=80) private String token;
 @Column(name="encrypted_pan", nullable=false, length=512) private String encryptedPan;
 @Column(name="last_four", nullable=false, length=4) private String lastFour;
 @Column(name="created_at", nullable=false) private Instant createdAt=Instant.now();
 protected CardToken(){} public CardToken(String token,String encryptedPan,String lastFour){this.token=token;this.encryptedPan=encryptedPan;this.lastFour=lastFour;this.createdAt=Instant.now();}
 public String getToken(){return token;} public String getEncryptedPan(){return encryptedPan;} public String getLastFour(){return lastFour;} public Instant getCreatedAt(){return createdAt;}
}
