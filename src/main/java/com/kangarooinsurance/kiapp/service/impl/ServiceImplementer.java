package com.kangarooinsurance.kiapp.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kangarooinsurance.kiapp.model.Brand;
import com.kangarooinsurance.kiapp.model.DefaultResponse;
import com.kangarooinsurance.kiapp.model.VehicleRequest;
import com.kangarooinsurance.kiapp.service.AppService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
import java.io.*;
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
    private final static String KI_API_PREFIX = "/api/leads";
    private final static String API_LEADS_PAGE_0 = KI_API_PREFIX.concat("/page/0");
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
        String apiYears = KI_API_PREFIX.concat("/choices/year/").concat(brand);
        readEndPoint(apiYears, false);
    }

    @Override
    public void getReference(String brand, String year) {
        String apiReference = KI_API_PREFIX.concat("/choices/model/").concat(brand).concat(File.separator).concat(year);
        readEndPoint(apiReference, true);
    }

    @Override
    public void sendVehicleData(VehicleRequest vehicleRequest) {

        logger = LoggerFactory.getLogger(getClass());

//        Observable.just(true)
//                .doOnNext(aBoolean -> sendDataToEndPoint(API_LEADS_PAGE_0, vehicleRequest))
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.computation())
//                .subscribe(aBoolean -> {
//                }, Throwable::printStackTrace);

        sendDataToEndPoint(API_LEADS_PAGE_0, vehicleRequest);
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
            JsonObject brandJsonObject = jsonArray.get(0).getAsJsonObject();
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

    private void sendDataToEndPoint(String api, VehicleRequest vehicleRequest) {

//        Observable<ByteBuf> body = Bytes.from(new File("/Users/frank/git/reactive-servlet/rxjava-extras-0.8.0.8.jar"))
//                .doOnNext(e -> System.err.println("Read some data"))
//                .doOnCompleted(() -> System.err.println("All data complete"))
//                .observeOn(Schedulers.io())
//                .map(b -> Unpooled.copiedBuffer(b));

        HttpClient.newClient(HOST, PORT)
                .enableWireLogging("TMP", LogLevel.INFO)
                .createPost(api)
                .writeContent(Observable.just(getByteBuf(vehicleRequest)))
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(this::mapResponse)
                .doOnNext(resp -> logger.info(resp.toString()))
                .subscribe(o -> {
                    logger.info(o);
                    logger.info("PAPITAS");
                }, Throwable::printStackTrace);
    }

    private ByteBuf getByteBuf(VehicleRequest vehicleRequest) {
        return Unpooled.copiedBuffer(getBytes(vehicleRequest));
    }

    private byte[] getBytes(VehicleRequest vehicleRequest) {
        byte[] vehicleRequestBytes = new byte[0];
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(vehicleRequest);
            out.flush();
            vehicleRequestBytes = bos.toByteArray();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vehicleRequestBytes;
    }
}