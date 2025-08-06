package com.tesseract.demo.Service;

import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesseract.demo.Model.Text;
import com.tesseract.demo.Repository.TextRepository;
import com.tesseract.demo.dto.TextDTO;
import com.tesseract.demo.dto.TextMapper;

@Service
public class TextService {
    
    @Autowired
    private TextRepository textRepository;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private JiebaService jiebaService;

    @Autowired
    private TextMapper textMapper;

    public TextDTO save(Text text){     //Este metodo es solo para inicializar los datos (toma Text directamente)
        if(textRepository.findByTitleEnglish(text.getTitleEnglish()).isPresent() || textRepository.findByTitleSpanish(text.getTitleSpanish()).isPresent()){
            return null;
        } else{
            return toDTO(textRepository.save(text));
        }
    }

    public TextDTO save(TextDTO text){  //Este metodo es el que se llama al enviar el formulario (con DTOs)
        Text newText = toDomain(text);
        newText.setCreationDate(LocalDate.now());
        if(textRepository.findByTitleEnglish(newText.getTitleEnglish()).isPresent() || textRepository.findByTitleSpanish(newText.getTitleSpanish()).isPresent()){
            return null;
        } else{
            return toDTO(textRepository.save(newText));
        }
    }

    public void createTextImage(long id, InputStream inputStream, long size) {
        Text text = textRepository.findById(id).orElseThrow();
        text.setImage(BlobProxy.generateProxy(inputStream, size));
        textRepository.save(text);
    }

    public TextDTO getText(long id){
        Optional<Text> text = this.textRepository.findById(id);
        if(text.isPresent()){
            return toDTO(text.get());
        } else {
            return null;
        }
    }

    public Text findText(long id){
        Optional<Text> text = this.textRepository.findById(id);
        if(text.isPresent()){
            return text.get();
        } else {
            return null;
        }
    }

    public List<TextDTO> getTextsOrderByCreationDateDesc(){
        return toDTOs(textRepository.findAllByOrderByCreationDateDesc());
    }

    public Resource getTextImage(Long textId) {
        Text text = textRepository.findById(textId).orElseThrow();
        try {
            return new InputStreamResource(text.getImage().getBinaryStream());
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user image", e);
        }
    }

    public String[][] getTextSpanish(TextDTO text){
        List<String> textSegmented = jiebaService.segment(text.text());
        List<String> words = dictionaryService.translateToSpanish(textSegmented);

        String[] chineseArray = textSegmented.toArray(new String[0]);
        String[] spanishArray = words.toArray(new String[0]);

        String[][] result = new String[2][];
        result[0] = chineseArray;
        result[1] = spanishArray;
        return result;
    }

    public String[][] getTextEnglish(TextDTO text){
        List<String> textSegmented = jiebaService.segment(text.text());
        List<String> words = dictionaryService.translateToEnglish(textSegmented);

        String[] chineseArray = textSegmented.toArray(new String[0]);
        String[] englishArray = words.toArray(new String[0]);

        String[][] result = new String[2][];
        result[0] = chineseArray;
        result[1] = englishArray;
        return result;
    }

    public boolean deleteText(long id) {
        if (textRepository.existsById(id)) {
            textRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public String processWithPaddleOCR(MultipartFile file) throws Exception{

        String url = "http://localhost:5000/ocr";

        RestTemplate restTemplate = new RestTemplate();

        // Crear recurso del archivo
        ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename(); // Asegura que el nombre del archivo se envíe
            }
        };

        // Crear cuerpo multipart
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileAsResource);

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Enviar POST
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response.getBody();

    }

    public String processWithDeepseek(String chineseText) throws Exception {
        String url = "http://localhost:5001/analyze";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("text", chineseText);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getBody();
    }

    public String[] getTitlesWithDeepseek(String chineseText) throws Exception {
        String url = "http://localhost:5001/getTitles";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("text", chineseText);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), String[].class);
    }

    public String[] getTranslationsWithDeepseek(String chineseText) throws Exception {
        // 1. URL de la API donde se solicitarán las traducciones
        String url = "http://localhost:5001/getTranslations";

        // 2. Crear un RestTemplate para hacer la solicitud HTTP
        RestTemplate restTemplate = new RestTemplate();
        
        // 3. Configurar los encabezados HTTP para indicar que enviamos y recibimos JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. Crear el cuerpo de la solicitud (JSON) con el texto chino
        Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("text", chineseText); // El texto en chino que queremos traducir

        // 5. Crear un HttpEntity que contiene el cuerpo de la solicitud y los encabezados
        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonBody, headers);

        // 6. Hacer la solicitud POST a la API y obtener la respuesta
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // 8. Convertir la respuesta JSON a un arreglo de String usando ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        String[] translations = mapper.readValue(response.getBody(), String[].class);

        // 10. Retornar el arreglo de traducciones
        return translations;
    }

    public String[] getDescriptionsWithDeepseek(String chineseText) throws Exception {
        String url = "http://localhost:5001/getDescriptions";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("text", chineseText);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), String[].class);
    }

    private TextDTO toDTO(Text text){
        return textMapper.toDTO(text);
    }

    private Text toDomain(TextDTO textDTO){
        return textMapper.toDomain(textDTO);
    }

    private List<TextDTO> toDTOs(List<Text> texts) {
        return textMapper.toDTO(texts);
    }

}
