package com.pedro.avaliacaospringapi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.avaliacaospringapi.models.Category;
import com.pedro.avaliacaospringapi.models.Product;
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
class ProductTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void T000_ProductTest() {
        Category category = new Category();
        category.setId(1L);
        category.setDescription("Eletrônicos");

        Product product = new Product();
        product.setId(10L);
        product.setDescription("Notebook");
        product.setPrice(4500.0);
        product.setCategory(category);

        assertEquals(10L, product.getId());
        assertEquals("Notebook", product.getDescription());
        assertEquals(4500.0, product.getPrice());
        assertEquals(category, product.getCategory());
    }

    @Test
    void T001_saveProduct() throws Exception {
       
        Category category = new Category();
        category.setDescription("Eletrônicos");

        String categoryContent = mapper.writeValueAsString(category);

        MvcResult categoryResult = mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .content(categoryContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String categoryJson = categoryResult.getResponse().getContentAsString();
        JsonNode categoryNode = getJson(categoryJson);
        Long categoryId = categoryNode.get("id").asLong();

        Product product = new Product();
        product.setDescription("Mouse Gamer");
        product.setPrice(250.0);
        product.setCategory(new Category(categoryId, null));

        String productContent = mapper.writeValueAsString(product);

        MvcResult productResult = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .content(productContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonString = productResult.getResponse().getContentAsString();
        JsonNode resultJSON = getJson(jsonString);

        Assert.assertEquals("Mouse Gamer", resultJSON.get("description").asText());
        Assert.assertEquals(250.0, resultJSON.get("price").asDouble(), 0.01);
        assertEquals(categoryId, resultJSON.get("category").get("id").asLong());
    }

    @Test
    void T002_saveProduct_descriptionEmpty() throws Exception {
       
        Category category = new Category();
        category.setDescription("Periféricos");

        String categoryContent = mapper.writeValueAsString(category);

        MvcResult categoryResult = mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .content(categoryContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String categoryJson = categoryResult.getResponse().getContentAsString();
        JsonNode categoryNode = getJson(categoryJson);
        Long categoryId = categoryNode.get("id").asLong();

        Product product = new Product();
        product.setDescription(""); 
        product.setPrice(150.0);
        product.setCategory(new Category(categoryId, null));

        String content = mapper.writeValueAsString(product);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        Assert.assertEquals(400, status);
    }

    @Test
    void T003_getAllProducts() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andReturn();

        String jsonString = result.getResponse().getContentAsString();
        JsonNode resultJSON = getJson(jsonString);

        Assert.assertTrue(resultJSON.isArray());
    }

    @Test
    void T004_deleteProductById() throws Exception {

        Category category = new Category();
        category.setId(1L); // ou use o ID de uma categoria válida já salva no banco

        Product product = new Product();
        product.setDescription("Notebook");
        product.setPrice(2500.0);
        product.setCategory(category);

        String content = mapper.writeValueAsString(product);

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andReturn();

        JsonNode createdJson = getJson(createResult.getResponse().getContentAsString());
        Long productId = createdJson.get("id").asLong();

        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/products/" + productId))
                .andReturn();

        Assert.assertEquals(204, deleteResult.getResponse().getStatus());
    }

    @Test
    void T005_deleteProductById_inexistente() throws Exception {
        Long idInexistente = 9999L;

        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/products/" + idInexistente))
                .andReturn();

        Assert.assertEquals(404, deleteResult.getResponse().getStatus());
    }

    @Test
    void T006_findByDescription() throws Exception {
        Category category = new Category();
        category.setId(1L); // 

        Product product = new Product();
        product.setDescription("Notebook");
        product.setPrice(2500.0);
        product.setCategory(category);

        String content = mapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/products/")
                .param("description", "Notebook"))
                .andReturn();

        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void T007_findByDescription_vazio() throws Exception {
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/products/")
                .param("description", ""))
                .andReturn();

        Assert.assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void T008_updateProduct_sucesso() throws Exception {
        Category category = new Category();
        category.setId(1L); 
        
        Product product = new Product();
        product.setDescription("Notebook");
        product.setPrice(2500.0);
        product.setCategory(category);


        String content = mapper.writeValueAsString(product);

        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andReturn();

        JsonNode createdJson = getJson(createResult.getResponse().getContentAsString());
        Long productId = createdJson.get("id").asLong();

        product.setDescription("Produto Atualizado");
        product.setPrice(25.0);
        String updateContent = mapper.writeValueAsString(product);

        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateContent))
                .andReturn();

        Assert.assertEquals(201, updateResult.getResponse().getStatus());
        JsonNode updatedJson = getJson(updateResult.getResponse().getContentAsString());
        Assert.assertEquals("Produto Atualizado", updatedJson.get("description").asText());
        assertEquals(25.0, updatedJson.get("price").asDouble());
    }

    @Test
    void T009_updateProduct_idInexistente() throws Exception {
        Long idInexistente = 9999L;

        Product product = new Product();
        product.setDescription("Produto Qualquer");
        product.setPrice(30.0);

        String content = mapper.writeValueAsString(product);

        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/products/" + idInexistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andReturn();

        Assert.assertEquals(404, updateResult.getResponse().getStatus());
    }

@Test
    void T010_updateProduct_descricaoVazia() throws Exception {
        // Criação de categoria válida
        Category category = new Category();
        category.setId(1L);

        Product product = new Product();
        product.setDescription("Notebook");
        product.setPrice(2500.0);
        product.setCategory(category);

        String validProductJson = mapper.writeValueAsString(product);

  
        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validProductJson))
                .andReturn();

        String createResponseContent = createResult.getResponse().getContentAsString();
        Long productId = getJson(createResponseContent).get("id").asLong();

        Product updatedProduct = new Product();
        updatedProduct.setDescription(""); 
        updatedProduct.setPrice(2500.0);
        updatedProduct.setCategory(category);

        String invalidUpdateJson = mapper.writeValueAsString(updatedProduct);

        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUpdateJson))
                .andReturn();

        Assert.assertEquals(400, updateResult.getResponse().getStatus());
    }

    private JsonNode getJson(String jsonString) throws JsonProcessingException {
        return mapper.readTree(jsonString);
    }

}
