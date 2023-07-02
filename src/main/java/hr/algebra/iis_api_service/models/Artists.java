package hr.algebra.iis_api_service.models;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlType(namespace = "http://www.w3.org/2001/artists")
@XmlRootElement(name = "artists")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Artist.class)
public class Artists implements Serializable {

    @XmlElement(name="artist", type=Artist.class)
    private List<Artist> artists;

    public Artists(List<Artist> artists) {
        this.artists = artists;
    }

    @XmlElement
    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
