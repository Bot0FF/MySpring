package com.bot0ff;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class TaskRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void handleGetAllTask_ReturnsValidResponseEntity() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/api/tasks");

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                [
                                    {
                                        "id":"5fd1a7b4-5d61-11ee-8c99-0242ac120002",
                                        "details":"Первая задача",
                                        "completed":false
                                    },
                                    {
                                        "id":"6c82123c-5d61-11ee-8c99-0242ac120002",
                                        "details":"Вторая задача",
                                        "completed":true
                                    }
                                ]
                                """
                        )

                );
    }

    @Test
    void handleCreateNewTask_PayloadIsValid_ReturnsValidResponseEntity() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         
                          {
                              "details": "Третья задача"
                          }
                          """);

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                
                                    {
                                        "details": "Третья задача",
                                        "completed": false
                                    }
                                
                                """
                        ),
                        MockMvcResultMatchers.jsonPath("$.id").exists()
                );
    }

    @Test
    void handleCreateNewTask_PayloadIsInvalid_ReturnsValidResponseEntity() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                          {
                              "details": null
                          }
                          """);

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.header().doesNotExist(HttpHeaders.LOCATION),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                
                                    {
                                        "errors": ["Описание задачи должно быть указано"]
                                    }
                                
                                """, true
                        )
                );
    }
}
