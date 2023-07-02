package hr.algebra.iis_api_service.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import hr.algebra.iis_api_service.models.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.util.List;

@RestController
@CrossOrigin
public class AuthController {

    public static final String API_URL = "http://localhost:8081/";

    public XmlMapper xmlMapper = new XmlMapper();

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/getPhotos")
    public ResponseEntity<String> get(@RequestHeader HttpHeaders h) throws JsonProcessingException
    {
        List<String> getToken = h.get("x-access-token");
        String token =  getToken.get(0);
        String check = token;

        HttpHeaders headers = getJwtHeaders(token);

        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> res = restTemplate.exchange(API_URL + "photos", HttpMethod.GET, entity, String.class);
        String result = res.getBody().toString();

        PhotoArtist[] photos = objectMapper.readValue(result, PhotoArtist[].class);

        XmlMapper xmlMapper = new XmlMapper();
        String photoXml = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(photos);
        HttpHeaders resHeaders = new HttpHeaders();
        ResponseEntity<String> res_ = new ResponseEntity<>(photoXml, resHeaders, HttpStatus.OK);
        return res_;
    }

    @GetMapping("/artists")
    public ResponseEntity<String> getArtists() throws IOException
    {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> res = restTemplate.getForEntity(API_URL + "artists", String.class);
        String result = res.getBody().toString();

        Artist[] artists = objectMapper.readValue(result, Artist[].class);

        //SAVING AN OBJECTS TO XML
        ArtistList al = new ArtistList();
        al.setArtists(List.of(artists));
        jaxbObjectToXML(al);

        //VALIDATION OF XML

        Reader fileReader = new FileReader("artistList.xml");
        BufferedReader bufReader = new BufferedReader(fileReader);
        StringBuilder sb = new StringBuilder();
        String line;
        while( ( line = bufReader.readLine()) != null){
            sb.append(line);
            System.out.println(line);
        }

        String artistsXml = sb.toString();

        Boolean b = validateArtists(artistsXml);

        ResponseEntity<String> response = new ResponseEntity<>(artistsXml, HttpStatus.OK);
        return response;
    }

    private static void jaxbObjectToXML(ArtistList artistList)
    {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(ArtistList.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(artistList, System.out);
            File file = new File("artistList.xml");
            marshaller.marshal(artistList, file);
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    public Boolean validateArtists(String xmlArtists){
        try{
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            File file = new File(new ClassPathResource("/src/main/resources/artistsSchema.xsd").getPath());
            Schema schema = factory.newSchema(file);

            StringReader reader = new StringReader(xmlArtists);
            schema.newValidator().validate(new StreamSource(reader));
            return true;
        }
        catch (Exception e){
            System.out.println("Wrong data");
            return false;
        }
    }
    public HttpHeaders getJwtHeaders(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-access-token", token);
        return headers;
    }



    @PostMapping("/photo")
    public ResponseEntity<String> postPhoto(@RequestBody Photo photo, @RequestHeader HttpHeaders h) throws JsonProcessingException{
        XmlMapper xmlMapper = new XmlMapper();
        String photoXml = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(photo);
        Boolean valid = validatePhoto(photoXml);
        List<String> getToken = h.get("x-access-token");
        String token =  getToken.get(0);

        if (valid.equals(true)){

            String photoJson = objectMapper.writeValueAsString(photo);

            RestTemplate restTemplate = new RestTemplate();


            HttpHeaders headers = getJwtHeaders(token);
            HttpEntity<Object> entity = new HttpEntity<>(photoJson, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(API_URL + "photo", entity, String.class);
            String jsonResponse = response.getBody();

            PhotoArtist photoArtist = objectMapper.readValue(jsonResponse, PhotoArtist.class);

            XmlMapper xmlMapper1 = new XmlMapper();
            String photoArtistBody = xmlMapper1.writerWithDefaultPrettyPrinter().writeValueAsString(photoArtist);

            // sneding response to client
            ResponseEntity<String> okResponse = new ResponseEntity(photoArtistBody, HttpStatus.OK);
            return okResponse;
        }else {
            ResponseEntity<String> wrongDataResponse = new ResponseEntity("Invalid data", HttpStatus.BAD_REQUEST);
            return wrongDataResponse;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> postLogin(@RequestBody Login login) throws JsonProcessingException {
        System.out.println(login);
        String loginJson = objectMapper.writeValueAsString(login);
        System.out.println(loginJson);

        String resonseJson = postToApi(loginJson, "login");

        Jwt userWithJwt = objectMapper.readValue(resonseJson, Jwt.class);

        String userBody = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userWithJwt);

        ResponseEntity<String> okResponse = new ResponseEntity(userBody, HttpStatus.OK);
        return okResponse;
    }

    public String postToApi(String jsonObject, String apiRoute){
        RestTemplate restTemplate;
        restTemplate = new RestTemplate();
        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(jsonObject, headers);
        // post req to server
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL + apiRoute, entity, String.class);
        String jsonResponse = response.getBody();

        return  jsonResponse;
    }

    public String jwtPostToApi(String jsonObject, String apiRoute, String token){
        RestTemplate restTemplate1;
        restTemplate1 = new RestTemplate();
        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.set("Authorization", "Token "+ token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(jsonObject, headers);
        // post req to server
        ResponseEntity<String> response = restTemplate1.postForEntity(API_URL + apiRoute, entity, String.class);
        String jsonResponse = response.getBody();

        return  jsonResponse;
    }

    @PostMapping("/auth")
    public ResponseEntity<String> post(@RequestBody User user) throws JsonProcessingException {
        XmlMapper xmlMapper1 = new XmlMapper();
        String userXml = xmlMapper1.writerWithDefaultPrettyPrinter().writeValueAsString(user);
        Boolean valid = validateUser(userXml);

        if (valid.equals(true)){
            // object to json
            String userJson = objectMapper.writeValueAsString(user);

            // creating restTemplate for request
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(userJson, headers);

            // post req to server
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL + "registration", entity, String.class);
            String jsonResponse = response.getBody();

            // json to Object
            Jwt userWithJwt = objectMapper.readValue(jsonResponse, Jwt.class);

            // object to Xml
            String userBody = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userWithJwt);

            // sneding response to client
            ResponseEntity<String> okResponse = new ResponseEntity(userBody, HttpStatus.OK);
            return okResponse;
        }
        else{
            ResponseEntity<String> wrongDataResponse = new ResponseEntity("Invalid data", HttpStatus.BAD_REQUEST);
            return wrongDataResponse;
        }
    }

    public Boolean validateUser(String xmlUser){
        try{
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            File file = new File(new ClassPathResource("/src/main/resources/userSchema.xsd").getPath());
            Schema schema = factory.newSchema(file);

            StringReader reader = new StringReader(xmlUser);
            schema.newValidator().validate(new StreamSource(reader));
            return true;
        }
        catch (Exception e){
            System.out.println("Wrong data");
            return false;
        }
    }

    public Boolean validatePhoto(String xmlPhoto){
        try{
            System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory");
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);

            File file = new File(new ClassPathResource("/src/main/resources/photoSchema.rng").getPath());
            Schema schema = factory.newSchema(file);

            StringReader reader = new StringReader(xmlPhoto);
            schema.newValidator().validate(new StreamSource(reader));
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }



}
