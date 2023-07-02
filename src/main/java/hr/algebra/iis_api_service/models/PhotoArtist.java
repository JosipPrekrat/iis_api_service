package hr.algebra.iis_api_service.models;

public class PhotoArtist {

    private String url;
    private String description;
    private Integer likes;
    private String dateTime;
    private Long artist_id;
    private String artist_username;

    public PhotoArtist(String url, String description, Integer likes, String dateTime, Long artist_id, String artist_username) {
        this.url = url;
        this.description = description;
        this.likes = likes;
        this.dateTime = dateTime;
        this.artist_id = artist_id;
        this.artist_username = artist_username;
    }

    public PhotoArtist(){
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setArtist_id(Long artist_id) {
        this.artist_id = artist_id;
    }

    public void setArtist_username(String artist_username) {
        this.artist_username = artist_username;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLikes() {
        return likes;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Long getArtist_id() {
        return artist_id;
    }

    public String getArtist_username() {
        return artist_username;
    }
}
