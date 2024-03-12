package ru.wwerlosh.vktestcase.users;

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
import ru.wwerlosh.vktestcase.handlers.users.UserController;
import ru.wwerlosh.vktestcase.handlers.users.UserService;
import ru.wwerlosh.vktestcase.handlers.users.dto.Address;
import ru.wwerlosh.vktestcase.handlers.users.dto.Company;
import ru.wwerlosh.vktestcase.handlers.users.dto.Geo;
import ru.wwerlosh.vktestcase.handlers.users.dto.UserDTO;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(username = "admin", password = "test")
    public void getAllUsers_withAuthentication_success() throws Exception {
        List<UserDTO> dtos = makeListOfUserDTO();

        Mockito.when(userService.getAllUsers()).thenReturn(dtos);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));
    }

    @Test
    @WithAnonymousUser
    public void getAllUsers_anonymous_fail() throws Exception {
        List<UserDTO> dtos = makeListOfUserDTO();

        Mockito.when(userService.getAllUsers()).thenReturn(dtos);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    public static List<UserDTO> makeListOfUserDTO() {
        List<UserDTO> userDTOs = new ArrayList<>();

        UserDTO dto1 = new UserDTO();
        dto1.setId(1L);
        dto1.setName("John Doe");
        dto1.setUsername("john.doe");
        dto1.setEmail("john.doe@example.com");
        dto1.setAddress(makeAddress());
        dto1.setPhone("123456789");
        dto1.setWebsite("www.example.com");
        dto1.setCompany(makeCompany());
        userDTOs.add(dto1);

        UserDTO dto2 = new UserDTO();
        dto2.setId(2L);
        dto2.setName("Jane Smith");
        dto2.setUsername("jane.smith");
        dto2.setEmail("jane.smith@example.com");
        dto2.setAddress(makeAddress());
        dto2.setPhone("987654321");
        dto2.setWebsite("www.example.org");
        dto2.setCompany(makeCompany());
        userDTOs.add(dto2);

        UserDTO dto3 = new UserDTO();
        dto3.setId(3L);
        dto3.setName("Bob Johnson");
        dto3.setUsername("bob.johnson");
        dto3.setEmail("bob.johnson@example.com");
        dto3.setAddress(makeAddress());
        dto3.setPhone("555555555");
        dto3.setWebsite("www.example.net");
        dto3.setCompany(makeCompany());
        userDTOs.add(dto3);

        return userDTOs;
    }

    private static Address makeAddress() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setSuite("Apt 101");
        address.setCity("New York");
        address.setZipcode("10001");
        address.setGeo(makeGeo());
        return address;
    }

    private static Geo makeGeo() {
        Geo geo = new Geo();
        geo.setLat("40.7128");
        geo.setLng("-74.0060");
        return geo;
    }

    private static Company makeCompany() {
        Company company = new Company();
        company.setName("Example Company");
        company.setCatchPhrase("Lorem ipsum dolor sit amet");
        company.setBs("Et harum quidem rerum facilis est et expedita distinctio");
        return company;
    }
}
