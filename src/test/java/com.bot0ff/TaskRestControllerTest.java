package com.bot0ff;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TaskRestControllerTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    TaskRestController taskRestController;

    @Test
    void handleGetAllTask_ReturnsValidResponseEntity() {
        //given
        var tasks = List.of(new Task(UUID.randomUUID(),"Первая задача", false),
                new Task(UUID.randomUUID(), "Вторая задача", false));
        Mockito.doReturn(tasks).when(this.taskRepository).findAll();

        //when
        var responseEntity = this.taskRestController.handleGetAllTasks();

        //then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        Assertions.assertEquals(tasks, responseEntity.getBody());
    }

    @Test
    void handleCreateNewTask_PayloadIsValid_ReturnsValidResponseEntity() {
        //given
        var details = "Третья задача";

        //when
        var responseEntity = this.taskRestController.handleCreateNewTask(new NewTaskPayload(details),
                UriComponentsBuilder.fromUriString("http://localhost:8082"), Locale.ENGLISH);

        //then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        if(responseEntity.getBody() instanceof Task task) {
            Assertions.assertNotNull(task.id());
            Assertions.assertEquals(details, task.details());
            Assertions.assertFalse(task.completed());

            Assertions.assertEquals(URI.create("http://localhost:8082/api/tasks/" + task.id()),
                    responseEntity.getHeaders().getLocation());

            Mockito.verify(this.taskRepository).save(task);
        }
        else {
            Assertions.assertInstanceOf(Task.class, responseEntity.getBody());
        }

        Mockito.verifyNoMoreInteractions(this.taskRepository);
    }

    @Test
    void handleCreateNewTask_PayloadInvalid_ReturnsValidResponseEntity() {
        //given
        var details  = "    ";
        var locale = Locale.US;
        var errorMessage = "Details is empty";

        Mockito.doReturn(errorMessage).when(this.messageSource)
                .getMessage("tasks.create.details.errors", new Object[0], locale);

        //when
        var responseEntity = this.taskRestController.handleCreateNewTask(new NewTaskPayload(details),
                UriComponentsBuilder.fromUriString("http://localhost:8082"), locale);

        //then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        Assertions.assertEquals(new ErrorsPresentation(List.of(errorMessage)), responseEntity.getBody());

        Mockito.verifyNoInteractions(taskRepository);
    }
}
