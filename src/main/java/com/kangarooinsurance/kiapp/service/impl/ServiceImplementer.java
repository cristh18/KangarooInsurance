package com.kangarooinsurance.kiapp.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kangarooinsurance.kiapp.model.Brand;
import com.kangarooinsurance.kiapp.model.DefaultResponse;
import com.kangarooinsurance.kiapp.service.AppService;
import io.netty.buffer.ByteBuf;
import io.netty.handler.logging.LogLevel;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.File;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceImplementer implements AppService {

    private final static String HOST = "segurocanguro.com";
    private final static Integer PORT = 443;
    private final static String FIELDS = "fields";
    private final static String VALUES = "values";
    private final static String PROXY_WIRE_LOGGING = "proxy-client";
    private final static String HTTP_SECURE_WIRE_LOGGING = "http-secure-default-client";
    private final static String API_LEADS_PAGE_0 = "/api/leads/page/0";
    private static Logger logger;

    public static List<Brand> brandList;

    public static List<DefaultResponse> yearList;

    public static List<DefaultResponse> referenceList;


    // Service impl
    @Override
    public void getHomeService() {
        logger = LoggerFactory.getLogger(getClass());

        Observable.just(true)
                .doOnNext(aBoolean -> readEndPoint())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(aBoolean -> {
                }, Throwable::printStackTrace);
    }

    @Override
    public void getBrandYears(String brand) {
        String apiYears = "/api/leads/choices/year/".concat(brand);
        readEndPoint(apiYears, false);
    }

    @Override
    public void getReference(String brand, String year) {
        String apiReference = "/api/leads/choices/model/".concat(brand).concat(File.separator).concat(year);
        readEndPoint(apiReference, true);
    }


    // Mapper

    private void readEndPoint() {


        SSLEngine sslEngine = null;
        try {
            sslEngine = defaultSSLEngineForClient();
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("Failed to create SSLEngine.", nsae);
            System.exit(-1);
        }

        HttpClient.newClient(HOST, PORT)
                .enableWireLogging(PROXY_WIRE_LOGGING, LogLevel.DEBUG)
                .secure(sslEngine)
                .createGet(API_LEADS_PAGE_0)
                .doOnNext(resp -> logger.info(resp.toString()))
                .flatMap(this::mapResponse)
                .toBlocking()
                .forEach(this::getBrands);
    }

    private void readEndPoint(String apiUrl, boolean reference) {


        SSLEngine sslEngine = null;
        try {
            sslEngine = defaultSSLEngineForClient();
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("Failed to create SSLEngine.", nsae);
            System.exit(-1);
        }

        HttpClient.newClient(HOST, PORT)
                .enableWireLogging(PROXY_WIRE_LOGGING, LogLevel.DEBUG)
                .secure(sslEngine)
                .createGet(apiUrl)
                .doOnNext(resp -> logger.info(resp.toString()))
                .flatMap(this::mapResponse)
                .toBlocking()
                .forEach(o -> getValues(o, reference));
    }

    private Observable<? extends String> mapResponse(HttpClientResponse<ByteBuf> resp) {
        System.out.println(resp.getStatus());
        return resp.getContent()
                .map(bb -> bb.toString(Charset.defaultCharset()));
    }

    private void getBrands(String s) {
        logger.info(s);
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(s).getAsJsonObject();

        JsonArray jsonArray = o.getAsJsonArray(FIELDS);

        if (jsonArray != null && jsonArray.size() > 0) {
            JsonObject brandJsonObject = jsonArray.get(2).getAsJsonObject();
            JsonArray brands = brandJsonObject.getAsJsonArray(VALUES);
            logger.info(brands.toString());

            brandList = new ArrayList<>(brands.size());
            for (int i = 0; i < brands.size(); i++) {
                brandList.add(new Brand(brands.get(i).getAsJsonObject()));
            }
        }
    }

    private void getValues(String s, boolean reference) {
        logger.info(s);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(s).getAsJsonArray();

        int size = jsonArray.size();

        List<DefaultResponse> responses = new ArrayList<>(size);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                responses.add(new DefaultResponse(jsonArray.get(i).getAsJsonObject()));
            }
        }

        if (reference) {
            referenceList = new ArrayList<>(responses);
        } else {
            yearList = new ArrayList<>(responses);
        }
    }

    private SSLEngine defaultSSLEngineForClient() throws NoSuchAlgorithmException {
        SSLContext sslCtx = SSLContext.getDefault();
        SSLEngine sslEngine = sslCtx.createSSLEngine(HOST, PORT);
        sslEngine.setUseClientMode(true);
        return sslEngine;
    }
}