package ru.wwerlosh.vktestcase.handlers.users;

import java.util.List;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.wwerlosh.vktestcase.handlers.HttpClient;
import ru.wwerlosh.vktestcase.handlers.users.dto.UserDTO;

@Service
@CacheConfig(cacheNames = "usersCache")
public class UserService {

    private final HttpClient<UserDTO> httpClient;

    public UserService(HttpClient<UserDTO> httpClient) {
        this.httpClient = httpClient;
    }

    public List<UserDTO> getAllUsers() {
        return httpClient.getAll();
    }

    @Cacheable
    public UserDTO getUserById(Long id) {
        return httpClient.getById(id);
    }

    @CacheEvict
    public Long deleteUserById(Long id) {
        return httpClient.deleteById(id);
    }

    @CachePut
    public UserDTO updateUserById(Long id, UserDTO userDTO) {
        return httpClient.updateById(id, userDTO);
    }

    @CachePut
    public UserDTO updateUserFieldsById(Long id, UserDTO userDTO) {
        return httpClient.updateFieldsById(id, userDTO);
    }

    @CachePut
    public UserDTO save(UserDTO userDTO) {
        return httpClient.save(userDTO);
    }
}
