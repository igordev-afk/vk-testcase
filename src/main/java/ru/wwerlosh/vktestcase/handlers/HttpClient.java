package ru.wwerlosh.vktestcase.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClient<T> {

    private final CloseableHttpClient httpClient;
    private final String URL;
    private final ObjectMapper objectMapper;
    private final Class<T> aClass;

    public HttpClient(String URL, ObjectMapper objectMapper, Class<T> aClass) {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = objectMapper;
        this.URL = URL;
        this.aClass = aClass;
    }

    public List<T> getAll() {
        HttpGet request = new HttpGet(URL);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String json = EntityUtils.toString(response.getEntity());
                return objectMapper.readValue(json, new TypeReference<List<T>>() {});
            } else {
                throw new RuntimeException("Failed to get data from endpoint " + URL + ". Status code: " + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to get data from endpoint " + URL + " due to IO exception", e);
        }
    }

    public T getById(Long id) {
        HttpGet request = new HttpGet(URL + "/" + id);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String json = EntityUtils.toString(response.getEntity());
                return objectMapper.readValue(json, aClass);
            } else {
                throw new RuntimeException("Failed to get data from endpoint " + URL + " by id " + id + ". Status code: " + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to get data from endpoint " + URL + " by id " + id + " due to IO exception", e);
        }
    }

    public T save(T dto) {
        HttpPost request = new HttpPost(URL);
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert DTO to JSON", e);
        }
        StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200 || statusCode == 201) {
                String json = EntityUtils.toString(response.getEntity());
                return objectMapper.readValue(json, aClass);
            } else {
                throw new RuntimeException("Failed to save data from endpoint " + URL + ". Status code: " + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data from endpoint " + URL + " due to IO exception", e);
        }
    }

    public Long deleteById(Long id) {
        HttpDelete request = new HttpDelete(URL + "/" + id);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 204 || statusCode == 200) {
                return id;
            } else {
                throw new RuntimeException("Failed to delete data from endpoint " + URL + " by id " + id + ". Status code: " + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete data from endpoint " + URL + " by id " + id + " due to IO exception", e);
        }
    }

    public T updateById(Long id, T dto) {
        HttpPut request = new HttpPut(URL + "/" + id);
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert DTO to JSON", e);
        }
        StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200 || statusCode == 204) {
                String json = EntityUtils.toString(response.getEntity());
                return objectMapper.readValue(json, aClass);
            } else {
                throw new RuntimeException("Failed to update data from endpoint " + URL + " by id " + id + ". Status code: " + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to update data from endpoint " + URL + " by id " + id + " due to IO exception", e);
        }
    }

    public T updateFieldsById(Long id, T dto) {
        HttpPatch request = new HttpPatch(URL + "/" + id);
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert DTO to JSON", e);
        }
        StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200 || statusCode == 204) {
                String json = EntityUtils.toString(response.getEntity());
                return objectMapper.readValue(json, aClass);
            } else {
                throw new RuntimeException("Failed to update fields from endpoint " + URL + " by id " + id + ". Status code: " + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to update fields from endpoint " + URL + " by id " + id + " due to IO exception", e);
        }
    }

}
