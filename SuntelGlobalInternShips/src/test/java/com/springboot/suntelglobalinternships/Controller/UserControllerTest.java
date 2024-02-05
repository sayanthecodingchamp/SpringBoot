package com.springboot.suntelglobalinternships.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.springboot.suntelglobalinternships.Model.Role;
import com.springboot.suntelglobalinternships.Model.User;
import com.springboot.suntelglobalinternships.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;
    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer(); // JSON -> <- String

    @Mock
    private UserRepository userRepository; // Mocking the UserRepo

    @InjectMocks
    private UserController userController;
    UUID uuid=UUID.randomUUID();
    User user_record=new User(uuid,"Sayan","Ghosh","sayanG@gmail.com","ghosh","9876564567","Bangalore",new Role("ROLE_USER"));
    User user2_record=new User("Debasish","Mandal","mandal@gmail.com","abc","7656789089","Delhi",new Role("ROLE_USER"));
    @BeforeEach
    public void setUp(){
//        MockitoAnnotations.openMocks(this);
        this.mockMvc= MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers() throws Exception {
        List<User> users=new ArrayList<>(Arrays.asList(user_record,user2_record));
        Mockito.when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].firstName",is("Sayan")))
                .andExpect(jsonPath("$[0].lastName",is("Ghosh")))
                .andExpect(jsonPath("$[0].email",is("sayanG@gmail.com")))
                .andExpect(jsonPath("$[0].phone",is("9876564567")))
                .andExpect(jsonPath("$[0].uId",is(uuid.toString())))
                .andExpect(jsonPath("$[1].firstName",is("Debasish")))
                .andExpect(jsonPath("$[1].lastName",is("Mandal")));
    }

    @Test
    void getUserbyId() throws Exception {
        Mockito.when(userRepository.findById(user_record.getuId())).thenReturn(Optional.of(user_record));
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}",uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.firstName",is("Sayan")))
                .andExpect(jsonPath("$.lastName",is("Ghosh")))
                .andExpect(jsonPath("$.email",is("sayanG@gmail.com")))
                .andExpect(jsonPath("$.phone",is("9876564567")))
                .andExpect(jsonPath("$.uId",is(uuid.toString())));

    }

    @Test
    void createUser() throws Exception {

//        UUID uuid=UUID.randomUUID();
        User user3 =new User("Sushant","Singh","singh@gmail.com","singh","9996789089","Vizag",new Role("ROLE_USER"));

        Mockito.when(userRepository.save(user3)).thenReturn(user3);

        lenient().when(userRepository.save(user3)).thenReturn(user3);

        String content= objectWriter.writeValueAsString(user3); // POJO -> JSON
        System.out.println(content);
        MockHttpServletRequestBuilder mockReq=MockMvcRequestBuilders.post("/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockReq)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.firstName",is("Sushant")));

//        verify(userController).createUser(any(User.class));

    }



    @Test
    void updateUser() {

    }

    @Test
    void deleteUser() {
    }


}