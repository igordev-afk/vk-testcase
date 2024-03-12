package ru.wwerlosh.vktestcase.handlers.albums;

import java.util.List;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.wwerlosh.vktestcase.handlers.HttpClient;

@Service
@CacheConfig(cacheNames = "albumsCache")
public class AlbumService {

    private final HttpClient<AlbumDTO> httpClient;

    public AlbumService(HttpClient<AlbumDTO> httpClient) {
        this.httpClient = httpClient;
    }

    public List<AlbumDTO> getAllAlbums() {
        return httpClient.getAll();
    }

    @Cacheable
    public AlbumDTO getAlbumById(Long id) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return httpClient.getById(id);
    }

    @Cacheable
    public AlbumDTO save(AlbumDTO albumDTO) {
        return httpClient.save(albumDTO);
    }

    @CacheEvict
    public Long deleteAlbumById(Long id) {
        return httpClient.deleteById(id);
    }

    @CachePut
    public AlbumDTO updateAlbumById(Long id, AlbumDTO albumDTO) {
        return httpClient.updateById(id, albumDTO);
    }

    @CachePut
    public AlbumDTO updateAlbumFieldsById(Long id, AlbumDTO albumDTO) {
        return httpClient.updateFieldsById(id, albumDTO);
    }
}
