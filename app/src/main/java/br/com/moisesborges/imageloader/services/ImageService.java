package br.com.moisesborges.imageloader.services;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import br.com.moisesborges.imageloader.models.BingImageResponse;
import br.com.moisesborges.imageloader.models.Hit;
import br.com.moisesborges.imageloader.models.Image;
import br.com.moisesborges.imageloader.models.PixabayImageResponse;
import br.com.moisesborges.imageloader.models.Value;
import br.com.moisesborges.imageloader.network.BingImageApi;
import br.com.moisesborges.imageloader.network.PixabayApi;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by moises.anjos on 14/12/2016.
 */

public class ImageService {

    private static final String PIXABAY_API_KEY = "3982032-889dcb372a9b53caf8e9dd92b";

    @Inject
    BingImageApi mBingImageApi;

    @Inject
    PixabayApi mPixabayApi;

    public ImageService(BingImageApi bingImageApi, PixabayApi pixabayApi) {
        mBingImageApi = bingImageApi;
        mPixabayApi = pixabayApi;
    }

    @NonNull
    public Observable<Image> searchImages(@NonNull String query) {
        return Observable.merge(pixabayServiceObservable(query), bingServiceObservable(query));
    }


    private Observable<Image> bingServiceObservable(String query) {
        Log.d("observable", "begin bingService");
        return mBingImageApi.searchImages(query)
                .filter(filterBingResponse)
                .flatMap(new Func1<BingImageResponse, Observable<Value>>() {
                    @Override
                    public Observable<Value> call(BingImageResponse bingImageResponse) {
                        return Observable.from(bingImageResponse.getValue());
                    }
                })
                .map(mapValueToImage);
    }

    final Func1<BingImageResponse, Boolean> filterBingResponse = new Func1<BingImageResponse, Boolean>() {
        @Override
        public Boolean call(BingImageResponse bingImageResponse) {
            return bingImageResponse.getValue().size() > 0;
        }
    };

    final Func1<Value, Image> mapValueToImage = new Func1<Value, Image>() {
        @Override
        public Image call(Value value) {
            Log.d("bing", "mapping value");
            return Image.fromValue(value);
        }
    };

    @NonNull
    private Observable<Image> pixabayServiceObservable(String query) {
        Log.d("observable", "begin pixabayService");
        return mPixabayApi.searchImages(query, PIXABAY_API_KEY)
                .filter(filterPixabayResponse)
                .flatMap(new Func1<PixabayImageResponse, Observable<Hit>>() {
                    @Override
                    public Observable<Hit> call(PixabayImageResponse pixabayImageResponse) {
                        return Observable.from(pixabayImageResponse.getHits());
                    }
                })
                .map(mapHitToImage);
    }

    final Func1<PixabayImageResponse, Boolean> filterPixabayResponse = new Func1<PixabayImageResponse, Boolean>() {
        @Override
        public Boolean call(PixabayImageResponse pixabayImageResponse) {
            return pixabayImageResponse.getHits().size() > 0;
        }
    };

    final Func1<Hit, Image> mapHitToImage = new Func1<Hit, Image>() {
        @Override
        public Image call(Hit hit) {
            Log.d("pixabay", "mapping hit");
            return Image.fromHit(hit);
        }
    };
}
