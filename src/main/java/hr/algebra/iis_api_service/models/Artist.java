package hr.algebra.iis_api_service.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "artist")
@XmlAccessorType (XmlAccessType.FIELD)
public class Artist {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String username;
    private String phone_number;
    private Long studio_id;
    private String studio_address;
    private String city;
    private String studio_name;

    public Artist() {
    }

    public Artist(Long id, String first_name, String last_name, String email, String username, String phone_number, Long studio_id, String studio_address, String city, String studio_name) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.username = username;
        this.phone_number = phone_number;
        this.studio_id = studio_id;
        this.studio_address = studio_address;
        this.city = city;
        this.studio_name = studio_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Long getStudio_id() {
        return studio_id;
    }

    public void setStudio_id(Long studio_id) {
        this.studio_id = studio_id;
    }

    public String getStudio_address() {
        return studio_address;
    }

    public void setStudio_address(String studio_address) {
        this.studio_address = studio_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStudio_name() {
        return studio_name;
    }

    public void setStudio_name(String studio_name) {
        this.studio_name = studio_name;
    }
}
