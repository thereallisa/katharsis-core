package io.katharsis.dispatcher.controller.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.katharsis.dispatcher.controller.BaseControllerTest;
import io.katharsis.dispatcher.controller.HttpMethod;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.request.Request;
import io.katharsis.request.dto.DataBody;
import io.katharsis.request.dto.RequestBody;
import io.katharsis.request.dto.ResourceRelationships;
import io.katharsis.request.path.JsonPath;
import io.katharsis.resource.mock.models.Project;
import io.katharsis.resource.mock.models.Task;
import io.katharsis.resource.mock.models.User;
import io.katharsis.resource.mock.repository.TaskToProjectRepository;
import io.katharsis.resource.mock.repository.UserToProjectRepository;
import io.katharsis.resource.registry.ResourceRegistry;
import io.katharsis.response.BaseResponseContext;
import io.katharsis.response.HttpStatus;
import io.katharsis.response.ResourceResponseContext;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class RelationshipsResourceDeleteTest extends BaseControllerTest {

    private static final String REQUEST_TYPE = HttpMethod.DELETE.name();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final QueryParams REQUEST_PARAMS = new QueryParams();

    @Test
    public void onValidRequestShouldAcceptIt()  {
        // GIVEN
        Request request = new Request("http://domain.local/tasks/1/relationships/project", REQUEST_TYPE, null, parameterProvider);

        ResourceRegistry resourceRegistry = mock(ResourceRegistry.class);
        RelationshipsResourceDelete sut = new RelationshipsResourceDelete(resourceRegistry, typeParser, queryParamsBuilder);

        // WHEN
        boolean result = sut.isAcceptable(request);

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    public void onNonRelationRequestShouldDenyIt()  {
        // GIVEN
        Request request = new Request("http://domain.local/tasks", REQUEST_TYPE, null, parameterProvider);

        ResourceRegistry resourceRegistry = mock(ResourceRegistry.class);
        RelationshipsResourceDelete sut = new RelationshipsResourceDelete(resourceRegistry, typeParser, queryParamsBuilder);

        // WHEN
        boolean result = sut.isAcceptable(request);

        // THEN
        assertThat(result).isFalse();
    }

//    @Test
//    public void onExistingToOneRelationshipShouldRemoveIt() throws Exception {
//        // GIVEN
//
//        RequestBody newTaskBody = new RequestBody();
//        DataBody data = new DataBody();
//        newTaskBody.setData(data);
//        data.setType("tasks");
//        data.setAttributes(OBJECT_MAPPER.createObjectNode().put("name", "sample task"));
//        data.setRelationships(new ResourceRelationships());
//
//        byte[] body = new byte[]{};
//        URL url = URI.create("/tasks").toURL();
//        Request request = new Request(url, REQUEST_TYPE, new ByteArrayInputStream(body), parameterProvider);
//
//        ResourcePost resourcePost = new ResourcePost(resourceRegistry, typeParser, OBJECT_MAPPER, queryParamsBuilder);
//
//        // WHEN -- adding a task
//        BaseResponseContext taskResponse = resourcePost.handle(request);
//
//        // THEN
//        assertThat(taskResponse.getResponse().getEntity()).isExactlyInstanceOf(Task.class);
//        Long taskId = ((Task) (taskResponse.getResponse().getEntity())).getId();
//        assertThat(taskId).isNotNull();
//
//        /* ------- */
//
//        // GIVEN
//        RequestBody newProjectBody = new RequestBody();
//        data = new DataBody();
//        newProjectBody.setData(data);
//        data.setType("projects");
//        data.setAttributes(OBJECT_MAPPER.createObjectNode().put("name", "sample project"));
//
//
//
//        // WHEN -- adding a project
//        ResourceResponseContext projectResponse = resourcePost.handle(request);
//
//        // THEN
//        assertThat(projectResponse.getResponse().getEntity()).isExactlyInstanceOf(Project.class);
//        assertThat(((Project) (projectResponse.getResponse().getEntity())).getId()).isNotNull();
//        assertThat(((Project) (projectResponse.getResponse().getEntity())).getName()).isEqualTo("sample project");
//        Long projectId = ((Project) (projectResponse.getResponse().getEntity())).getId();
//        assertThat(projectId).isNotNull();
//
//        /* ------- */
//
//        // GIVEN
//        RequestBody newTaskToProjectBody = new RequestBody();
//        data = new DataBody();
//        newTaskToProjectBody.setData(data);
//        data.setType("projects");
//        data.setId(projectId.toString());
//
//        JsonPath savedTaskPath = pathBuilder.buildPath("/tasks/" + taskId + "/relationships/project");
//        RelationshipsResourcePost relationshipsResourcePost =
//                new RelationshipsResourcePost(resourceRegistry, typeParser, queryParamsBuilder);
//
//        // WHEN -- adding a relation between task and project
//        BaseResponseContext projectRelationshipResponse =
//                relationshipsResourcePost.handle(savedTaskPath, new QueryParams(), newTaskToProjectBody);
//        assertThat(projectRelationshipResponse).isNotNull();
//
//        // THEN
//        TaskToProjectRepository taskToProjectRepository = new TaskToProjectRepository();
//        Project project = taskToProjectRepository.findOneTarget(taskId, "project", REQUEST_PARAMS);
//        assertThat(project.getId()).isEqualTo(projectId);
//
//        /* ------- */
//
//        // GIVEN
//        RelationshipsResourceDelete sut = new RelationshipsResourceDelete(resourceRegistry, typeParser, queryParamsBuilder);
//
//        // WHEN -- removing a relation between task and project
//        BaseResponseContext result = sut.handle(savedTaskPath, new QueryParams(), newTaskToProjectBody);
//        assertThat(result).isNotNull();
//
//        // THEN
//        assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.NO_CONTENT_204);
//        Project nullProject = taskToProjectRepository.findOneTarget(taskId, "project", REQUEST_PARAMS);
//        assertThat(nullProject).isNull();
//    }
//
//    @Test
//    public void onExistingToManyRelationshipShouldRemoveIt() throws Exception {
//        // GIVEN
//        RequestBody newUserBody = new RequestBody();
//        DataBody data = new DataBody();
//        newUserBody.setData(data);
//        data.setType("users");
//        data.setAttributes(OBJECT_MAPPER.createObjectNode().put("name", "sample task"));
//        data.setRelationships(new ResourceRelationships());
//
//        JsonPath taskPath = pathBuilder.buildPath("/users");
//        ResourcePost resourcePost = new ResourcePost(resourceRegistry, typeParser, OBJECT_MAPPER, queryParamsBuilder);
//
//        // WHEN -- adding a user
//        BaseResponseContext taskResponse = resourcePost.handle(taskPath, new QueryParams(), newUserBody);
//
//        // THEN
//        assertThat(taskResponse.getResponse().getEntity()).isExactlyInstanceOf(User.class);
//        Long userId = ((User) (taskResponse.getResponse().getEntity())).getId();
//        assertThat(userId).isNotNull();
//
//        /* ------- */
//
//        // GIVEN
//        RequestBody newProjectBody = new RequestBody();
//        data = new DataBody();
//        newProjectBody.setData(data);
//        data.setType("projects");
//        data.setAttributes(OBJECT_MAPPER.createObjectNode().put("name", "sample project"));
//
//        JsonPath projectPath = pathBuilder.buildPath("/projects");
//
//        // WHEN -- adding a project
//        ResourceResponseContext projectResponse = resourcePost.handle(projectPath, new QueryParams(), newProjectBody);
//
//        // THEN
//        assertThat(projectResponse.getResponse().getEntity()).isExactlyInstanceOf(Project.class);
//        assertThat(((Project) (projectResponse.getResponse().getEntity())).getId()).isNotNull();
//        assertThat(((Project) (projectResponse.getResponse().getEntity())).getName()).isEqualTo("sample project");
//        Long projectId = ((Project) (projectResponse.getResponse().getEntity())).getId();
//        assertThat(projectId).isNotNull();
//
//        /* ------- */
//
//        // GIVEN
//        RequestBody newTaskToProjectBody = new RequestBody();
//        data = new DataBody();
//        newTaskToProjectBody.setData(Collections.singletonList(data));
//        data.setType("projects");
//        data.setId(projectId.toString());
//
//        JsonPath savedTaskPath = pathBuilder.buildPath("/users/" + userId + "/relationships/assignedProjects");
//        RelationshipsResourcePost relationshipsResourcePost =
//                new RelationshipsResourcePost(resourceRegistry, typeParser, queryParamsBuilder);
//
//        // WHEN -- adding a relation between user and project
//        BaseResponseContext projectRelationshipResponse =
//                relationshipsResourcePost.handle(savedTaskPath, new QueryParams(), newTaskToProjectBody);
//        assertThat(projectRelationshipResponse).isNotNull();
//
//        // THEN
//        UserToProjectRepository userToProjectRepository = new UserToProjectRepository();
//        Project project = userToProjectRepository.findOneTarget(userId, "assignedProjects", REQUEST_PARAMS);
//        assertThat(project.getId()).isEqualTo(projectId);
//
//        /* ------- */
//
//        // GIVEN
//        RelationshipsResourceDelete sut = new RelationshipsResourceDelete(resourceRegistry, typeParser, queryParamsBuilder);
//
//        // WHEN -- removing a relation between task and project
//        BaseResponseContext result = sut.handle(savedTaskPath, new QueryParams(), newTaskToProjectBody);
//        assertThat(result).isNotNull();
//
//        // THEN
//        Project nullProject = userToProjectRepository.findOneTarget(userId, "assignedProjects", REQUEST_PARAMS);
//        assertThat(nullProject).isNull();
//    }
}
