package com.intuit.url.urlshortner.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Url {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private String originalUrl;
    @Column
    private String shortLink;
    @Column
    private LocalDateTime creationDate;
    @Column
    private LocalDateTime expirationDate;

//    @Override
//    public String toString() {
//        return "Url{" +
//                "id=" + id +
//                ", originalUrl='" + originalUrl + '\'' +
//                ", shortLink='" + shortLink + '\'' +
//                ", creationDate=" + creationDate +
//                ", expirationDate=" + expirationDate +
//                '}';
//    }
}
