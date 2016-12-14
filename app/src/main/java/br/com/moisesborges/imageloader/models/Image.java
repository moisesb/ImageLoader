package br.com.moisesborges.imageloader.models;

/**
 * Created by moises.anjos on 12/12/2016.
 */

public class Image {
    private String title;
    private String contentUrl;
    private String thumbnailUrl;

    public Image() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public static Image fromHit(Hit hit) {
        Image image = new Image();
        image.setContentUrl(hit.getWebformatURL());
        image.setThumbnailUrl(hit.getPreviewURL());
        image.setTitle(hit.getId().toString());
        return image;
    }

    public static Image fromValue(Value value) {
        Image image = new Image();
        image.setContentUrl(value.getContentUrl());
        image.setThumbnailUrl(value.getThumbnailUrl());
        image.setTitle(value.getName());
        return image;
    }

}
