package ru.kishko.taskmanager.taskmanagerservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.kishko.taskmanager.taskmanagerservice.dtos.TaskDTO;
import ru.kishko.taskmanager.taskmanagerservice.entites.Comment;
import ru.kishko.taskmanager.taskmanagerservice.entites.Task;
import ru.kishko.taskmanager.taskmanagerservice.services.TaskService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TaskService taskService;
    private TaskDTO taskDTO;
    private TaskDTO taskDTO2;
    private TaskDTO taskDTO3;
    private TaskDTO taskDTO4;
    private TaskDTO taskDTO5;
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHVzZXIuY29tIiwiaWF0IjoxNzAyMzIxNTc0LCJleHAiOjE3MDIzMjQyNTJ9.2NV3rHiz0znpiK_a1zrnK1D3YhysQsIZNO4ky4niUxI";

    @BeforeEach
    void setUp() {

        taskDTO = TaskDTO.builder()
                .id(1L)
                .name("Task")
                .description("Create task")
                .status("WAITING")
                .priority("HIGH")
                .authorId(1L)
                .producerId(2L)
                .comments(new ArrayList<>())
                .users(new ArrayList<>())
                .build();

        taskDTO2 = TaskDTO.builder()
                .id(1L)
                .name("Task2")
                .description("Create task2")
                .status("WAITING")
                .priority("HIGH")
                .authorId(1L)
                .producerId(2L)
                .comments(new ArrayList<>())
                .users(new ArrayList<>())
                .build();

        taskDTO3 = TaskDTO.builder()
                .id(1L)
                .name("Task2")
                .description("Create task2")
                .status("WAITING")
                .priority("HIGH")
                .authorId(1L)
                .producerId(2L)
                .comments(List.of(Comment.builder().id(1L).task(new Task()).text("Hello").build()))
                .users(new ArrayList<>())
                .build();

        taskDTO4 = TaskDTO.builder()
                .id(1L)
                .name("Task2")
                .description("Create task2")
                .status("WAITING")
                .priority("HIGH")
                .authorId(1L)
                .producerId(10L)
                .comments(List.of(Comment.builder().id(1L).task(new Task()).text("Hello").build()))
                .users(new ArrayList<>())
                .build();

        taskDTO5 = TaskDTO.builder()
                .id(1L)
                .name("Task2")
                .description("Create task2")
                .status("WAITING")
                .priority("HIGH")
                .authorId(1L)
                .producerId(null)
                .comments(List.of(Comment.builder().id(1L).task(new Task()).text("Hello").build()))
                .users(new ArrayList<>())
                .build();

    }

    @Test
    void createTask() throws Exception {

        given(taskService.createTask(taskDTO)).willReturn(taskDTO);

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated());

    }

    @Test
    @Disabled
    void getAllTasks() throws Exception {

        List<TaskDTO> entities = Collections.singletonList(taskDTO);
        given(taskService.getAllTasks()).willReturn(entities);

        // Act and Assert
        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    void getTaskById() throws Exception {

        Long id = 1L;

        when(taskService.getTaskById(id)).thenReturn(taskDTO);

        ResultActions response = mockMvc.perform(get("/tasks/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(taskDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(taskDTO.getDescription())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void updateTaskById() throws Exception {

        Long id = 1L;

        when(taskService.updateTaskById(id, taskDTO2)).thenReturn(taskDTO2);

        ResultActions response = mockMvc.perform(put("/tasks/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO2)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(taskDTO2.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(taskDTO2.getDescription())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void addCommentToTask() throws Exception {

        Long id = 1L;

        when(taskService.addCommentToTask(id, "Hello")).thenReturn(taskDTO3);

        ResultActions response = mockMvc.perform(post("/tasks/add-comment/1?commentText=" + "Hello")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO2)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(taskDTO3.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(taskDTO3.getDescription())))
                .andExpect(jsonPath("$.comments", hasSize(1)))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void deleteTaskById() throws Exception {

        Long id = 1L;

        when(taskService.deleteTaskById(id)).thenReturn("successful deleted");

        ResultActions response = mockMvc.perform(delete("/tasks/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void getTasksByUserId() throws Exception {

        Long id = 1L;

        when(taskService.getTasksByUserId(id)).thenReturn(List.of(taskDTO, taskDTO2));

        ResultActions response = mockMvc.perform(get("/tasks/user/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void setProducerToTask() throws Exception {

        Long id = 1L;

        when(taskService.setProducerToTask(id, 10L)).thenReturn(taskDTO4);

        ResultActions response = mockMvc.perform(put("/tasks/" + id + "/producer/" + 10L)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO3)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(taskDTO4.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(taskDTO4.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.producerId", CoreMatchers.is(taskDTO4.getProducerId().intValue())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void deleteProducerFromTask() throws Exception {

        Long id = 1L;

        when(taskService.deleteProducerFromTask(id)).thenReturn(taskDTO5);

        ResultActions response = mockMvc.perform(delete("/tasks/" + id + "/delete-producer")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO4)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.producerId", CoreMatchers.is(taskDTO5.getProducerId())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @Disabled
    void changeStatusByTaskId() {
    }
}