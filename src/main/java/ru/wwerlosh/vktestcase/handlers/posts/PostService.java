package ru.wwerlosh.vktestcase.handlers.posts;

import java.util.List;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.wwerlosh.vktestcase.handlers.HttpClient;

@Service
@CacheConfig(cacheNames = "postsCache")
public class PostService {

    private final HttpClient<PostDTO> httpClient;

    public PostService(HttpClient<PostDTO> httpClient) {
        this.httpClient = httpClient;
    }

    public List<PostDTO> getAllPosts() {
        return httpClient.getAll();
    }

    @Cacheable
    public PostDTO getPostById(Long id) {
        return httpClient.getById(id);
    }

    @CacheEvict
    public Long deletePostById(Long id) {
        return httpClient.deleteById(id);
    }

    @CachePut
    public PostDTO updatePostById(Long id, PostDTO postDTO) {
        return httpClient.updateFieldsById(id, postDTO);
    }

    @CachePut
    public PostDTO updatePostFieldsById(Long id, PostDTO postDTO) {
        return httpClient.updateFieldsById(id, postDTO);
    }

    @Cacheable
    public PostDTO save(PostDTO postDTO) {
        return httpClient.save(postDTO);
    }
}
