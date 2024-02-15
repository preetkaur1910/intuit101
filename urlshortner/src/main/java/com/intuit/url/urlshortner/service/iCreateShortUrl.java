package com.intuit.url.urlshortner.service;

import com.intuit.url.urlshortner.model.Url;
import com.intuit.url.urlshortner.model.UrlRequestDto;

public interface iCreateShortUrl {
    Url createShortUrl(UrlRequestDto request);
}
