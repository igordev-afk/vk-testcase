package ru.wwerlosh.vktestcase.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.wwerlosh.vktestcase.handlers.albums.AlbumDTO;
import ru.wwerlosh.vktestcase.handlers.posts.PostDTO;
import ru.wwerlosh.vktestcase.handlers.users.dto.UserDTO;

@Configuration
public class HttpClientConfiguration {

    @Bean
    public HttpClient<PostDTO> postDTOHttpClient(@Value("${spring.api.posts.url}") String URL,
                                                 ObjectMapper objectMapper) {
        return new HttpClient<>(URL, objectMapper, PostDTO.class);
    }

    @Bean
    public HttpClient<UserDTO> userDTOHttpClient(@Value("${spring.api.users.url}") String URL,
                                                 ObjectMapper objectMapper) {
        return new HttpClient<>(URL, objectMapper, UserDTO.class);
    }

    @Bean
    public HttpClient<AlbumDTO> albumDTOHttpClient(@Value("${spring.api.albums.url}") String URL,
                                                 ObjectMapper objectMapper) {
        return new HttpClient<>(URL, objectMapper, AlbumDTO.class);
    }
}
