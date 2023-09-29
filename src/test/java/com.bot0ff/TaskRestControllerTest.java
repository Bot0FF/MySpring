package com.bot0ff;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class TaskRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void handleGetAllTask_ReturnsValidResponseEntity() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/api/tasks")
                .with(httpBasic("user1", "password1"));

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id":"f8d2e61a-bd8b-4647-a917-828d804be519",
                                        "details":"Первая задача",
                                        "completed":false
                                    },
                                    {
                                        "id":"394c1076-a771-4ad2-bc24-058088108f3d",
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
                .with(httpBasic("user2", "password2"))
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
                        status().isCreated(),
                        header().exists(HttpHeaders.LOCATION),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                
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
                .with(httpBasic("user1", "password1"))
                .content("""
                          {
                              "details": null
                          }
                          """);

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isBadRequest(),
                        header().doesNotExist(HttpHeaders.LOCATION),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                
                                    {
                                        "errors": ["Описание задачи должно быть указано"]
                                    }
                                
                                """, true
                        )
                );
    }
}
