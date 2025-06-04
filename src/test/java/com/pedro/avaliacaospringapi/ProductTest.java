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
public class ProductTest {

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
        // criando a categoria primeiro
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

        // criando o produto
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
        // criando a categoria primeiro
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

        // criando o produto inválido
        Product product = new Product();
        product.setDescription(""); // inválido
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

    private JsonNode getJson(String jsonString) throws JsonProcessingException {
        return mapper.readTree(jsonString);
    }
}
