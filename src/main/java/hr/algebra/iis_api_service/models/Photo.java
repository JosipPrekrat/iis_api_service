package hr.algebra.iis_api_service.models;
import java.util.Date;

public class Photo {
    private String url;
    private String description;
    private Long artist_id;

    public Photo(String url, String description, Long artist_id) {
        this.url = url;
        this.description = description;
        this.artist_id = artist_id;
    }

    public Photo(){
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setArtist_id(Long artist_id) {
        this.artist_id = artist_id;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public Long getArtist_id() {
        return artist_id;
    }

}
