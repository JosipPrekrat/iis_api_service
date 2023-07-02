package hr.algebra.iis_api_service.services;

import hr.algebra.iis_api_service.models.Photo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class MarshalService {

    MarshalService marshalService;
    public MarshalService(){
        this.marshalService = new MarshalService();
    }

    public void marshalPhoto(Photo photo) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Photo.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        mar.marshal(photo, new File("./photo.xml"));
    }



}
