package com.intuit.url.urlshortner.service;

import com.intuit.url.urlshortner.model.Url;
import com.intuit.url.urlshortner.model.UrlRequestDto;
import com.intuit.url.urlshortner.repository.UrlRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Component
public class CreateShortUrlImpl implements iCreateShortUrl {

    @Autowired
    private UrlRepository urlRepository;

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    @Override
    public Url createShortUrl(UrlRequestDto request) {
        System.out.println("Given Long Url Request = " + request);
        String tinyUrl = "";
        String localhostString = "localhost:8080/";
        if(StringUtils.isNotEmpty(request.getUrl()) && !checkIfOriginalAlreadyExists(request.getUrl()))
        {
            if(StringUtils.isBlank(request.getCustomTinyUrl()))
            {
                System.out.println("tiny Url is blank");
                tinyUrl = encodeUrl(request.getUrl());
            }
            else
            {
                if(checkIfCustomUrlAlreadyExists(localhostString.concat(request.getCustomTinyUrl())))
                {
                    System.out.println("Url already exists");
                    return null;
                }
                else
                {
                    tinyUrl = request.getCustomTinyUrl();
                }
            }
            String finalUrl = localhostString.concat(tinyUrl);

            Url urlToPersist = new Url();
            setValuesForPersistUrl(urlToPersist,request, finalUrl);
            return persistShortUrl(urlToPersist);
        }
        return null;
    }

    private String encodeUrl(String longUrl) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Generate the hash for the long URL
            byte[] hashBytes = md.digest(longUrl.getBytes());
            BigInteger bigInt = new BigInteger(1, hashBytes);
            // Convert the big integer to a base 62 string
            StringBuilder base62String = new StringBuilder();
            while (bigInt.compareTo(BigInteger.ZERO) > 0) {
                int remainder = bigInt.mod(BigInteger.valueOf(62)).intValue();
                base62String.insert(0, BASE62.charAt(remainder));
                bigInt = bigInt.divide(BigInteger.valueOf(62));
            }
            System.out.println("Long Url = " + longUrl + " Short url = " + base62String.toString());
            return base62String.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    boolean checkIfCustomUrlAlreadyExists(String tinyUrl)
    {
        Url urlResp = urlRepository.findByShortLink(tinyUrl);
        if(urlResp != null)
            return true;
        else
            return false;
    }

    boolean checkIfOriginalAlreadyExists(String originalUrl)
    {
        Url urlResp = urlRepository.findByOriginalUrl(originalUrl);
        if(urlResp != null)
            return true;
        else
            return false;
    }

    private void setValuesForPersistUrl(Url url, UrlRequestDto request, String finalUrl) {
        url.setCreationDate(LocalDateTime.now());
        url.setOriginalUrl(request.getUrl());
        url.setShortLink(finalUrl);
        url.setExpirationDate(getExpirationDate(request.getExpirationDate(),url.getCreationDate()));
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
        if(StringUtils.isBlank(expirationDate))
        {
            return creationDate.plusYears(5);
        }
        LocalDateTime expirationDateToReturn;
        try {
            expirationDateToReturn = LocalDateTime.parse(expirationDate);
        }
        catch (Exception E)
        {
            throw E;
        }

        return expirationDateToReturn;
    }

    Url persistShortUrl(Url urlToPersist) {
        return urlRepository.save(urlToPersist);
    }
}
