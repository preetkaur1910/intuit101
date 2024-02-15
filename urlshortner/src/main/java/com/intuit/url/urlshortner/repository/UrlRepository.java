package com.intuit.url.urlshortner.repository;

import com.intuit.url.urlshortner.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Url findByShortLink(String shortLink);
    Url findByOriginalUrl(String url);
}
