package com.ichsanudinstore.loka.api;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.util.SharedPreferencesUtil;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author Ichsanudin_Chairin
 * @since Thursday 8/22/2019 10:19 PM
 */
public class RestManager {
    private static final transient boolean ALLOW_INSECURE = true;
    private static List<Cookie> COOKIES = new ArrayList<>();

    private static Retrofit RETROFIT;
    private static OkHttpClient OK_HTTP_CLIENT;

    @SuppressLint("BadHostnameVerifier")
    private static HostnameVerifier GET_HOSTNAME_VERIFIER() {
        return (s, sslSession) -> true;
    }

    @SuppressLint("TrustAllX509TrustManager")
    private static X509TrustManager GET_X509_TRUST_MANAGER() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private static SSLSocketFactory GET_SSL_SOCKET_FACTORY() {
        try {
            SSLContext mSSLContext = SSLContext.getInstance("TLS");

            mSSLContext.init(null, new TrustManager[]{GET_X509_TRUST_MANAGER()}, null);

            return mSSLContext.getSocketFactory();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    static OkHttpClient GET_OK_HTTP_CLIENT() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(5, MINUTES)
                .readTimeout(5, MINUTES)
                .writeTimeout(5, MINUTES);

        if (ALLOW_INSECURE) {
            builder.sslSocketFactory(GET_SSL_SOCKET_FACTORY(), GET_X509_TRUST_MANAGER());
            builder.hostnameVerifier(GET_HOSTNAME_VERIFIER());
        }

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(httpLoggingInterceptor);

        builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
                COOKIES = cookies;
            }

            @Override
            public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                return COOKIES;
            }
        });

        return builder.build();
    }

    public static Retrofit GET_RETROFIT(byte type) {
        if (OK_HTTP_CLIENT == null) {
            OK_HTTP_CLIENT = GET_OK_HTTP_CLIENT();
        }

        if (type == 0) {
            RETROFIT = new Retrofit.Builder()
                    .baseUrl(Constant.Application.PRODUCTION ? Constant.URL.BASE_PRODUCTION : SharedPreferencesUtil.get(Constant.SharedPreferenceKey.BASE_URL, String.class, Constant.URL.BASE_DEVELOPMENT))
                    .client(OK_HTTP_CLIENT)
                    .addConverterFactory(GsonConverterFactory.create(
                            new GsonBuilder()
                                    .create()
                            )
                    )
                    .build();
        } else {
            if (RETROFIT == null) {
                RETROFIT = new Retrofit.Builder()
                        .baseUrl(Constant.Application.PRODUCTION ? Constant.URL.BASE_PRODUCTION : SharedPreferencesUtil.get(Constant.SharedPreferenceKey.BASE_URL, String.class, Constant.URL.BASE_DEVELOPMENT))
                        .client(OK_HTTP_CLIENT)
                        .addConverterFactory(GsonConverterFactory.create(
                                new GsonBuilder()
                                        .create()
                                )
                        )
                        .build();
            }
        }

        return RETROFIT;
    }
}