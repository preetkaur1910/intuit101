package com.intuit.url.urlshortner.service;


import com.intuit.url.urlshortner.model.Url;
import org.springframework.stereotype.Service;

@Service
public interface iDeleteTinyUrl {

    public void deleteTinyUrl(Url url);
}
