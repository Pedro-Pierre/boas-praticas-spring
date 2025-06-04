package com.pedro.avaliacaospringapi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.avaliacaospringapi.models.Category;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void T000_CategoryTest() {
        Category category = new Category();
        category.setDescription("Produtos de limpeza");

        assertEquals("Produtos de limpeza", category.getDescription());
    
    }

    @Test 
    void T001_saveCategory () throws Exception {

        Category category = new Category();
        category.setDescription("Acessórios para carro");

        String categoryContent = mapper.writeValueAsString(category);

        MvcResult categoryResult = mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .content(categoryContent)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();
        
        String jsonString = categoryResult.getResponse().getContentAsString();
        JsonNode resultJson = getJson(jsonString);
        int status = categoryResult.getResponse().getStatus();
        
        Assert.assertEquals("Acessórios para carro", resultJson.get("description").asText());
        Assert.assertEquals(200, status);
    }

    @Test 
    void T002_saveCategory_descriptionEmpty() throws Exception{

        Category category = new Category();
        category.setDescription("");

        String categoryContent = mapper.writeValueAsString(category);

        MvcResult categoryResult = mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .content(categoryContent)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();
        
        int status = categoryResult.getResponse().getStatus();
        
        Assert.assertEquals(400, status);
    }

    @Test
    void T003_getAllCategories() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andReturn();

        String jsonString = result.getResponse().getContentAsString();
        JsonNode resultJSON = getJson(jsonString);

        Assert.assertTrue(resultJSON.isArray());
    }

    @Test
    void T004_deleteCategoryById() throws Exception {

        Category category = new Category();
        category.setDescription("Livros");
        
        String content = mapper.writeValueAsString(category);

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andReturn();

        JsonNode createdJson = getJson(createResult.getResponse().getContentAsString());
        Long categorytId = createdJson.get("id").asLong();

        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/categories/" + categorytId))
                .andReturn();

        Assert.assertEquals(204, deleteResult.getResponse().getStatus());
    } 

    @Test
    void T005_deleteCategoryById_inexistente() throws Exception {
        Long idInexistente = 9999L;

        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/categories/" + idInexistente))
                .andReturn();

        Assert.assertEquals(404, deleteResult.getResponse().getStatus());
    }

    @Test
    void T006_findByDescription() throws Exception {
        Category category = new Category();
        category.setDescription("Livros");

        String content = mapper.writeValueAsString(category);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/categories/")
                .param("description", "Livros"))
                .andReturn();

        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void T007_findByDescription_vazio() throws Exception {
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/categories/")
                .param("description", ""))
                .andReturn();

        Assert.assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void T008_updateCategory_sucesso() throws Exception {
        Category category = new Category();
        category.setDescription("Livros"); 

        String content = mapper.writeValueAsString(category);

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andReturn();

        JsonNode createdJson = getJson(createResult.getResponse().getContentAsString());
        Long categoryId = createdJson.get("id").asLong();

        category.setDescription("Cadernos");
        String updateContent = mapper.writeValueAsString(category);

        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateContent))
                .andReturn();

        Assert.assertEquals(201, updateResult.getResponse().getStatus());
        JsonNode updatedJson = getJson(updateResult.getResponse().getContentAsString());
        Assert.assertEquals("Cadernos", updatedJson.get("description").asText());
    }



   
    private JsonNode getJson(String jsonString) throws JsonProcessingException {
        return mapper.readTree(jsonString);
    }
}
