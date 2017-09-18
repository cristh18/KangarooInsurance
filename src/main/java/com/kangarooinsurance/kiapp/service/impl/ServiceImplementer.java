package com.kangarooinsurance.kiapp.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kangarooinsurance.kiapp.model.Brand;
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

    private SSLEngine defaultSSLEngineForClient() throws NoSuchAlgorithmException {
        SSLContext sslCtx = SSLContext.getDefault();
        SSLEngine sslEngine = sslCtx.createSSLEngine(HOST, PORT);
        sslEngine.setUseClientMode(true);
        return sslEngine;
    }
}