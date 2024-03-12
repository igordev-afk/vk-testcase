package ru.wwerlosh.vktestcase.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.wwerlosh.vktestcase.handlers.posts.PostController;
import ru.wwerlosh.vktestcase.handlers.posts.PostDTO;
import ru.wwerlosh.vktestcase.handlers.posts.PostService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @Test
    @WithMockUser
    public void getAllPosts_withAuthentication_success() throws Exception {
        List<PostDTO> dtos = makeListOfPostDTO();

        Mockito.when(postService.getAllPosts()).thenReturn(dtos);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].title", is("Second Title")));
    }

    @Test
    @WithAnonymousUser
    public void getAllPosts_anonymous_fail() throws Exception {
        List<PostDTO> dtos = makeListOfPostDTO();

        Mockito.when(postService.getAllPosts()).thenReturn(dtos);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void createNewPost_withAuthentication_success() throws Exception {
        PostDTO dto = new PostDTO();
        dto.setUserId(1L);
        dto.setTitle("First Title");
        dto.setBody("First Body");

        Mockito.when(postService.save(dto)).then(ans -> {
            dto.setId(1L);
            return dto;
        });

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/posts").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithAnonymousUser
    public void createNewPost_anonymous_success() throws Exception {
        PostDTO dto = new PostDTO();
        dto.setUserId(1L);
        dto.setTitle("First Title");
        dto.setBody("First Body");

        Mockito.when(postService.save(dto)).then(ans -> {
            dto.setId(1L);
            return dto;
        });

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/posts").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    private static List<PostDTO> makeListOfPostDTO() {
        List<PostDTO> postDTOs = new ArrayList<>();

        // Создаем первый объект PostDTO
        PostDTO dto1 = new PostDTO();
        dto1.setId(1L);
        dto1.setUserId(1L);
        dto1.setTitle("First Title");
        dto1.setBody("First Body");
        postDTOs.add(dto1);

        // Создаем второй объект PostDTO
        PostDTO dto2 = new PostDTO();
        dto2.setId(2L);
        dto2.setUserId(2L);
        dto2.setTitle("Second Title");
        dto2.setBody("Second Body");
        postDTOs.add(dto2);

        // Создаем третий объект PostDTO
        PostDTO dto3 = new PostDTO();
        dto3.setId(3L);
        dto3.setUserId(1L);
        dto3.setTitle("Third Title");
        dto3.setBody("Third Body");
        postDTOs.add(dto3);

        return postDTOs;
    }
}
