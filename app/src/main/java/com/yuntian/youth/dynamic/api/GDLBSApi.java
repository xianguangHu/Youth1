package com.yuntian.youth.dynamic.api;

import com.google.gson.JsonObject;
import com.socks.library.KLog;
import com.yuntian.youth.dynamic.model.CreateResults;
import com.yuntian.youth.dynamic.service.GDLBSService;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by huxianguang on 2017/4/25.
 */

public class GDLBSApi {
        public static Observable<CreateResults> add(String key, String tableid, int type, JsonObject jsonObject){

        OkHttpClient client =getheader().build();
        Retrofit retrofit=new Retrofit.Builder()
                .client(client)
                .baseUrl("http://yuntuapi.amap.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GDLBSService service=retrofit.create(GDLBSService.class);
        return service.addLocation(key,tableid,type,jsonObject);
    }

    public static OkHttpClient.Builder getheader(){
        OkHttpClient.Builder httpClient=new OkHttpClient.Builder()
        .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Content-Type","application/x-www-form-urlencoded")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        }).addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request request = chain.request();
                        final Response response = chain.proceed(request);

                        final ResponseBody responseBody = response.body();
                        final long contentLength = responseBody.contentLength();

                        BufferedSource source = responseBody.source();
                        source.request(Long.MAX_VALUE); // Buffer the entire body.
                        Buffer buffer = source.buffer();

                        Charset charset = Charset.forName("UTF-8");
                        MediaType contentType = responseBody.contentType();
                        if (contentType != null) {
                            try {
                                charset = contentType.charset(charset);
                            } catch (UnsupportedCharsetException e) {
                                KLog.e("");
                                KLog.e("Couldn't decode the response body; charset is likely malformed.");
                                return response;
                            }
                        }

                        if (contentLength != 0) {
                            KLog.v("--------------------------------------------开始打印返回数据----------------------------------------------------");
                            KLog.json(buffer.clone().readString(charset));
                            KLog.v("--------------------------------------------结束打印返回数据----------------------------------------------------");
                        }

                        return response;
                    }
                }).retryOnConnectionFailure(true);
        return httpClient;
    }
}
