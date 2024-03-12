package ru.wwerlosh.vktestcase.albums;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.wwerlosh.vktestcase.handlers.albums.AlbumController;
import ru.wwerlosh.vktestcase.handlers.albums.AlbumDTO;
import ru.wwerlosh.vktestcase.handlers.albums.AlbumService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlbumController.class)
public class AlbumControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AlbumService albumService;
    
    @Test
    @WithMockUser
    public void getAllAlbums_withAuthentication_success() throws Exception {
        List<AlbumDTO> dtos = makeListOfAlbumDTO();

        Mockito.when(albumService.getAllAlbums()).thenReturn(dtos);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/albums")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].title", is("Mana")));
    }

    @Test
    @WithAnonymousUser
    public void getAllAlbums_anonymous_fail() throws Exception {
        List<AlbumDTO> dtos = makeListOfAlbumDTO();

        Mockito.when(albumService.getAllAlbums()).thenReturn(dtos);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void createNewAlbum_withAuthentication_success() throws Exception {
        AlbumDTO dto = new AlbumDTO(null, 1L, "Honor");

        Mockito.when(albumService.save(dto)).then(ans -> {
            dto.setId(1L);
            return dto;
        });

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/albums").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithAnonymousUser
    public void createNewAlbum_anonymous_success() throws Exception {
        AlbumDTO dto = new AlbumDTO(null, 1L, "Honor");

        Mockito.when(albumService.save(dto)).then(ans -> {
            dto.setId(1L);
            return dto;
        });

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/albums").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    private List<AlbumDTO> makeListOfAlbumDTO() {
        AlbumDTO dto_1 = new AlbumDTO(1L, 1L, "Logan");
        AlbumDTO dto_2 = new AlbumDTO(2L, 1L, "Mana");
        AlbumDTO dto_3 = new AlbumDTO(3L, 2L, "Chrono");
        return List.of(dto_1, dto_2, dto_3);
    }
}
