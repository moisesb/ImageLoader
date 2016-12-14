package br.com.moisesborges.imageloader.di;

import javax.inject.Singleton;

import br.com.moisesborges.imageloader.imagesearch.ImageSearchActivity;
import dagger.Component;


/**
 * Created by moises.anjos on 12/12/2016.
 */

@Singleton
@Component(modules = {NetModule.class, ServiceModule.class})
public interface ServiceComponent {
    void inject(ImageSearchActivity activity);
}
