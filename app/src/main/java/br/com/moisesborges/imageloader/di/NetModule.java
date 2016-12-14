package br.com.moisesborges.imageloader.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import br.com.moisesborges.imageloader.network.BingImageApi;
import br.com.moisesborges.imageloader.network.PixabayApi;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by moises.anjos on 12/12/2016.
 */
@Module
public class NetModule {

    private final String pixabayBaseUrl;
    private final String bingBaseUrl;
    private final Context mContext;

    public NetModule(@NonNull String pixabayBaseUrl,
                     @NonNull String bingBaseUrl,
                     @NonNull Context context) {
        this.pixabayBaseUrl = pixabayBaseUrl;
        this.bingBaseUrl = bingBaseUrl;
        this.mContext = context;
    }

    @Provides
    @Singleton
    public Gson providesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @Provides
    @Singleton
    public OkHttp3Downloader providesOkHttp3Downloader(OkHttpClient client) {
        return new OkHttp3Downloader(client);
    }


    @Provides
    @Singleton
    public Picasso providesPicasso(OkHttp3Downloader okHttp3Downloader) {
        return new Picasso.Builder(mContext)
                .downloader(okHttp3Downloader)
                .executor(Executors.newSingleThreadExecutor())
                .build();
    }

    @Provides
    @Singleton
    public PixabayApi providesPixabay(Gson gson, OkHttpClient okHttpClient) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(pixabayBaseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        return retrofit.create(PixabayApi.class);
    }

    @Provides
    @Singleton
    public BingImageApi providesBing(Gson gson, OkHttpClient okHttpClient) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(bingBaseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        return retrofit.create(BingImageApi.class);
    }
}
