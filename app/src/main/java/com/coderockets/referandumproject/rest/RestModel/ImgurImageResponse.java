package com.coderockets.referandumproject.rest.RestModel;

/**
 * Created by aykutasil on 11.11.2016.
 */

public class ImgurImageResponse {
    public boolean success;
    public int status;
    public UploadedImage data;

    public static class UploadedImage {
        public String id;
        public String title;
        public String description;
        public String type;
        public boolean animated;
        public int width;
        public int height;
        public int size;
        public int views;
        public int bandwidth;
        public String vote;
        public boolean favorite;
        public String account_url;
        public String deletehash;
        public String name;
        public String link;

        @Override
        public String toString() {
            return "UploadedImage{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", type='" + type + '\'' +
                    ", animated=" + animated +
                    ", width=" + width +
                    ", height=" + height +
                    ", size=" + size +
                    ", views=" + views +
                    ", bandwidth=" + bandwidth +
                    ", vote='" + vote + '\'' +
                    ", favorite=" + favorite +
                    ", account_url='" + account_url + '\'' +
                    ", deletehash='" + deletehash + '\'' +
                    ", name='" + name + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ImgurImageResponse{" +
                "success=" + success +
                ", status=" + status +
                ", data=" + data.toString() +
                '}';
    }
}