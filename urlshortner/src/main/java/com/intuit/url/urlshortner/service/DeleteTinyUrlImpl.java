package com.intuit.url.urlshortner.service;

import com.intuit.url.urlshortner.model.Url;
import com.intuit.url.urlshortner.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteTinyUrlImpl implements iDeleteTinyUrl {

    @Autowired
    UrlRepository urlRepository;

    @Override
    public void deleteTinyUrl(Url url) {
        Url urlToDelete = urlRepository.findByShortLink(url.getShortLink());
        System.out.println("delete handler: got object from db" + urlToDelete);
        urlRepository.deleteById(urlToDelete.getId());
        System.out.println("post delete handler");
    }
}
