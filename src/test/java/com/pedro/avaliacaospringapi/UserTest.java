package com.pedro.avaliacaospringapi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.avaliacaospringapi.models.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void T000_UserTest() {
        User user = new User();
        user.setId(10L);
        user.setName("Pedro");
        user.setEmail("pedro@email.com");

        assertEquals(10L, user.getId());      
        assertEquals("Pedro", user.getName());
        assertEquals("pedro@email.com", user.getEmail());
    }

    @Test
    void T001_saveUser() throws Exception {
        User user = new User();
        user.setName("Pedro Test");
        user.setEmail("pedro@email.com");

        String content = mapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonString = result.getResponse().getContentAsString();
        JsonNode resultJSON = getJson(jsonString);

        Assert.assertEquals("Pedro Test", resultJSON.get("name").asText());
        Assert.assertEquals("pedro@email.com", resultJSON.get("email").asText());
    }

    @Test
    void T002_saveUser_nameEmpty() throws Exception {
        User user = new User();
        user.setName(""); // inválido
        user.setEmail("pedro@email.com");

        String content = mapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        Assert.assertEquals(400, status);
    }

    @Test
    void T003_getAllUsers() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andReturn();

        String jsonString = result.getResponse().getContentAsString();
        JsonNode resultJSON = getJson(jsonString);

        Assert.assertTrue(resultJSON.isArray());
    }

    private JsonNode getJson(String jsonString) throws JsonProcessingException {
        return mapper.readTree(jsonString);
    }
}
