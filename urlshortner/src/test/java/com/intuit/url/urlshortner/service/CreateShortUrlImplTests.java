package com.intuit.url.urlshortner.service;

import com.intuit.url.urlshortner.model.Url;
import com.intuit.url.urlshortner.model.UrlRequestDto;
import com.intuit.url.urlshortner.repository.UrlRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class CreateShortUrlImplTests {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private CreateShortUrlImpl createShortUrlImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testCreateShortUrl_ValidLongUrl_NoCustomUrl() {
//        UrlRequestDto request = new UrlRequestDto("https://www.example.com", null,null);
//
//        Url result = createShortUrlImpl.createShortUrl(request);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals("localhost:8080/hMpQW", result.getShortLink());
//    }

//    @Test
//    public void testCreateShortUrl_ValidLongUrl_WithCustomUrl() {
//        UrlRequestDto request = new UrlRequestDto("https://www.example.com", "custom", null);
//        Url url = new Url();
//        url.setShortLink("localhost:8080/custom");
//
//        Mockito.when(createShortUrlImpl.checkIfOriginalAlreadyExists(request.getUrl())).thenReturn(false);
//        Mockito.when(createShortUrlImpl.checkIfCustomUrlAlreadyExists(request.getCustomTinyUrl())).thenReturn(false);
//        Mockito.when(urlRepository.findByShortLink(Mockito.anyString())).thenReturn(null);
//        Mockito.when(createShortUrlImpl.persistShortUrl(any())).thenReturn(url);
//
//        Url result = createShortUrlImpl.createShortUrl(request);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals("localhost:8080/custom", result.getShortLink());
//    }

    @Test
    public void testCreateShortUrl_EmptyLongUrl() {
        UrlRequestDto request = new UrlRequestDto("", null, null);

        Url result = createShortUrlImpl.createShortUrl(request);

        Assertions.assertNull(result);
    }

//    @Test
//    public void testCreateShortUrl_EmptyCustomUrl() {
//        UrlRequestDto request = new UrlRequestDto("https://www.example.com", "", null);
//
//        Url result = createShortUrlImpl.createShortUrl(request);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertNotEquals("localhost:8080/", result.getShortLink());
//    }

    // Additional test cases can be added to cover other scenarios...
}
