package com.kangarooinsurance.kiapp.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.handler.logging.LogLevel;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

@Controller
public class HomeController {

    private final static String HOST = "github.com";
    private final static String HOST2 = "52.44.43.67";
    private final static Integer PORT = 443;
    private static Logger logger;

    @RequestMapping("/kiapp")
    public String home() {

//        Observable.just(true)
//                .doOnNext(aBoolean -> init())
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread())
//                .subscribe(aBoolean -> {}, Throwable::printStackTrace);

        init();

        return "index";
    }

    private void init() {
        buildLogger();
        readEndPoint();
    }

    void init2() {
//        RxNetty.cre
    }

    private void buildLogger() {
        logger = LoggerFactory.getLogger(getClass());
    }

    private static void readEndPoint() {


        SSLEngine sslEngine = null;
        try {
            sslEngine = defaultSSLEngineForClient();
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("Failed to create SSLEngine.", nsae);
            System.exit(-1);
        }

        HttpClient.newClient(HOST2, PORT)
//                .enableWireLogging("http-secure-default-client", LogLevel.DEBUG)
                .enableWireLogging("proxy-client", LogLevel.DEBUG)
                .secure(sslEngine)
                .createGet("/api/leads/page/0")
//                .createGet("/api/leads/choices/brand")
                .doOnNext(resp -> logger.info(resp.toString()))
                .flatMap(resp -> {
                    System.out.println(resp.getStatus());
                    return resp.getContent()
                            .map(bb -> bb.toString(Charset.defaultCharset()));
                })
                .toBlocking()
//                .forEach(logger::info);
                .forEach(s -> {
                    System.out.println("----init----");
                    System.out.println(s);

                    JsonParser parser = new JsonParser();
                    JsonObject o = parser.parse(s).getAsJsonObject();

                    JsonArray jsonArray = o.getAsJsonArray("fields");

                    if (jsonArray != null && jsonArray.size() > 0) {
//                        for (int i = 0; i < jsonArray.size(); i++) {
//                            JsonArray childJsonArray = jsonArray.getJs(i);
//                            if (childJsonArray != null && childJsonArray.length() > 0) {
//                                for (int j = 0; j < childJsonArray.length(); j++) {
//                                    System.out.println(childJsonArray.optString(j));
//                                }
//                            }
//                        }

                        JsonObject brandJsonObject = jsonArray.get(2).getAsJsonObject();
                        System.out.println(brandJsonObject.toString());

                        System.out.println("----brands----");
                        JsonArray brands = brandJsonObject.getAsJsonArray("values");
                        System.out.println(brands.toString());
                    }
                });
    }

    private static SSLEngine defaultSSLEngineForClient() throws NoSuchAlgorithmException {
        SSLContext sslCtx = SSLContext.getDefault();
        SSLEngine sslEngine = sslCtx.createSSLEngine(HOST, PORT);
        sslEngine.setUseClientMode(true);
        return sslEngine;
    }
}
