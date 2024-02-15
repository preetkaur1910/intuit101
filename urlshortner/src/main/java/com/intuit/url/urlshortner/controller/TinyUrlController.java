package com.intuit.url.urlshortner.controller;

import com.intuit.url.urlshortner.model.Url;
import com.intuit.url.urlshortner.model.UrlErrorResponseDto;
import com.intuit.url.urlshortner.model.UrlRequestDto;
import com.intuit.url.urlshortner.model.UrlResponseDto;
import com.intuit.url.urlshortner.repository.UrlRepository;
import com.intuit.url.urlshortner.service.iCreateShortUrl;
import com.intuit.url.urlshortner.service.iGetOriginalUrl;
import com.intuit.url.urlshortner.service.iDeleteTinyUrl;
import io.micrometer.common.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TinyUrlController {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    iCreateShortUrl iCreateShortUrl;

    @Autowired
    iGetOriginalUrl iGetOriginalUrl;

    @Autowired
    iDeleteTinyUrl iDeleteTinyUrl;

    @GetMapping(value = "/getAllShortUrl")
    public List<Url> getAllUrl() {
        return urlRepository.findAll();
    }

    @PostMapping(value = "/saveShortUrl")
    public String saveShortUrl(Url url) {
        urlRepository.save(url);
        return "Saved url = " + url;
    }

    @ApiOperation(value = "Convert new url", notes = "Converts long url to short url")
    @PostMapping("/generateShortUrl")
    public ResponseEntity<?> convertToShortUrl(@RequestBody UrlRequestDto request) {
        Url urlResp = iCreateShortUrl.createShortUrl(request);
        if(urlResp!= null )
        {
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setExpirationDate(urlResp.getExpirationDate());
            urlResponseDto.setOriginalLink(urlResp.getOriginalUrl());
            urlResponseDto.setShortLink(urlResp.getShortLink());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
        }
        if(urlResp == null)
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("I am sorry my friend, the url generation is not possible.");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.BAD_REQUEST);
        }
        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setError("I am sorry my friend, the url generation is not possible.");
        urlErrorResponseDto.setStatus("400");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Convert new urls", notes = "Converts long urls to short urls")
    @PostMapping("/generateShortUrls")
    public ResponseEntity<?> convertToShortUrls(@RequestBody UrlRequestDto[] requests) {
        ArrayList<Object> result = new ArrayList<>();
        for(UrlRequestDto request: requests) {
            Url urlResp = iCreateShortUrl.createShortUrl(request);
            if(urlResp!= null )
            {
                UrlResponseDto urlResponseDto = new UrlResponseDto();
                urlResponseDto.setExpirationDate(urlResp.getExpirationDate());
                urlResponseDto.setOriginalLink(urlResp.getOriginalUrl());
                urlResponseDto.setShortLink(urlResp.getShortLink());
                result.add(urlResponseDto);
            }
            else if(urlResp == null)
            {
                UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
                urlErrorResponseDto.setError("The custom url already");
                urlErrorResponseDto.setStatus("420");
                result.add(urlErrorResponseDto);
            } else {
                UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
                urlErrorResponseDto.setError("I am sorry my friend, the url generation is not possible.");
                urlErrorResponseDto.setStatus("404");
                result.add(urlErrorResponseDto);
            }
        }
        return new ResponseEntity<ArrayList<Object>>(result,HttpStatus.OK);
    }

    @ApiOperation(value = "Redirect", notes = "Finds original url from short url and redirects")
    @GetMapping(value = "/{tinyUrl}")
    public ResponseEntity<?> getAndRedirect(@PathVariable String tinyUrl, HttpServletResponse response) throws IOException
    {
        String localhostString = "localhost:8080/";
        if(StringUtils.isEmpty(tinyUrl))
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Invalid Url");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }

        Url originalUrlObject = iGetOriginalUrl.getOriginalUrl(localhostString.concat(tinyUrl));

        if(originalUrlObject == null)
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url does not exist or it might have expired!");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }

        if(originalUrlObject.getExpirationDate().isBefore(LocalDateTime.now()))
        {
            iDeleteTinyUrl.deleteTinyUrl(originalUrlObject);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url Expired. Please try generating a fresh one.");
            urlErrorResponseDto.setStatus("200");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrlObject.getOriginalUrl()))
                .build();
    }

    @DeleteMapping(value = "/deleteUrl")
    public String deleteUrl(Url url) {
        System.out.println("Url to delete = " + url);
        iDeleteTinyUrl.deleteTinyUrl(url);
        return "Deleted url = " + url + " from DB";
    }
}
