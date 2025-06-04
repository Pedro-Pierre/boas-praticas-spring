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
class UserTest {

@Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void T000_UserTest() {
        User user = new User();
        user.setId((long) 10);
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

    @Test
    void T004_deleteUserById() throws Exception {
        // Primeiro cria o usuário para depois deletar
        User user = new User();
        user.setName("Usuário Para Deletar");
        user.setEmail("deletar@email.com");

        String content = mapper.writeValueAsString(user);

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        JsonNode createdUserJson = getJson(createResult.getResponse().getContentAsString());
        Long userId = createdUserJson.get("id").asLong();

        // Realiza o DELETE
        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/users/" + userId))
                .andReturn();

        int status = deleteResult.getResponse().getStatus();
        Assert.assertEquals(204, status); // NO CONTENT
    }

    @Test
    void T005_deleteUserById_inexistente() throws Exception {
        Long idInexistente = 9999L;

        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/users/" + idInexistente))
                .andReturn();

        Assert.assertEquals(404, deleteResult.getResponse().getStatus());
    }

    @Test
    void T005_findByName() throws Exception {

        User user = new User();
        user.setName("Pedro Buscar");
        user.setEmail("buscar@email.com");

        String content = mapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/users/")
                .param("name", "Pedro Buscar"))
                .andReturn();

        String jsonString = result.getResponse().getContentAsString();
        JsonNode resultJSON = getJson(jsonString);

        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertEquals("Pedro Buscar", resultJSON.get(0).get("name").asText());
    }

        @Test
    void T007_findByName_vazio() throws Exception {
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/users/")
                .param("name", ""))
                .andReturn();

        Assert.assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void T006_updateUser() throws Exception {

        User user = new User();
        user.setName("Original");
        user.setEmail("original@email.com");

        String content = mapper.writeValueAsString(user);

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        JsonNode createdUser = getJson(createResult.getResponse().getContentAsString());
        Long userId = createdUser.get("id").asLong();

        User updatedUser = new User();
        updatedUser.setName("Atualizado");
        updatedUser.setEmail("atualizado@email.com");

        String updatedContent = mapper.writeValueAsString(updatedUser);

        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/users/" + userId)
                .content(updatedContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = updateResult.getResponse().getStatus();
        Assert.assertEquals(201, status); 

        JsonNode updatedJson = getJson(updateResult.getResponse().getContentAsString());
        Assert.assertEquals("Atualizado", updatedJson.get("name").asText());
        Assert.assertEquals("atualizado@email.com", updatedJson.get("email").asText());
    }

    @Test
    void T007_updateUser_nameEmpty() throws Exception {

        User user = new User();
        user.setName("Original");
        user.setEmail("original@email.com");

        String content = mapper.writeValueAsString(user);

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        JsonNode createdUser = getJson(createResult.getResponse().getContentAsString());
        Long userId = createdUser.get("id").asLong();

        User updatedUser = new User();
        updatedUser.setName("");
        updatedUser.setEmail("atualizado@email.com");

        String updatedContent = mapper.writeValueAsString(updatedUser);

        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/users/" + userId)
                .content(updatedContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = updateResult.getResponse().getStatus();
        Assert.assertEquals(400, status); 
    }

    @Test
    void T008_updateUser_idInexistente() throws Exception {
        Long idInvalido = 9999L; 

        User user = new User();
        user.setName("Pedro");
        user.setEmail("pedro@teste.com");

        String content = mapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .put("/users/" + idInvalido)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andReturn();

        int status = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();

        Assert.assertEquals(404, status);
        Assert.assertTrue(responseBody.contains("Id não encontrado"));
    }

    private JsonNode getJson(String jsonString) throws JsonProcessingException {
        return mapper.readTree(jsonString);
    }


}
