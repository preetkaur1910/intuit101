package com.intuit.url.urlshortner.service;


import com.intuit.url.urlshortner.model.Url;
import com.intuit.url.urlshortner.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetOriginalUrlImpl implements iGetOriginalUrl {

    @Autowired
    UrlRepository urlRepository;

    @Override
    public Url getOriginalUrl(String tinyUrl) {
        return urlRepository.findByShortLink(tinyUrl);
    }
}