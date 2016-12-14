package br.com.moisesborges.imageloader.di;

import javax.inject.Singleton;

import br.com.moisesborges.imageloader.network.BingImageApi;
import br.com.moisesborges.imageloader.network.PixabayApi;
import br.com.moisesborges.imageloader.services.ImageService;
import dagger.Module;
import dagger.Provides;

/**
 * Created by moises.anjos on 14/12/2016.
 */
@Module
public class ServiceModule {

    @Provides
    @Singleton
    public ImageService providesImageService(BingImageApi bingImageApi, PixabayApi pixabayImageService) {
        return new ImageService(bingImageApi, pixabayImageService);
    }
}
