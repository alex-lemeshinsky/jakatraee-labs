package com.example.forum.rest;

import com.example.forum.dto.TopicCloseRequestDTO;
import com.example.forum.dto.TopicDTO;
import com.example.forum.model.Topic;
import com.example.forum.service.TopicClosureException;
import com.example.forum.service.TopicService;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/topics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TopicResource {

    @EJB
    private TopicService topicService;

    @GET()
    @Path("/all")
    public Response getAllTopics() {
        List<Topic> topics = topicService.getAllTopics();
        return Response.ok(topics).build();
    }

    @GET
    public Response getTopics(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("search") String search) {
        List<Topic> topics = topicService.getFilteredTopics(search, page, size);
        return Response.ok(topics).build();
    }

    @GET
    @Path("/{id}")
    public Response getOne(@PathParam("id") Long id) {
        Topic topic = topicService.getTopicById(id);
        if (topic == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(topic).build();
    }

    @POST
    public Response create(@Valid TopicDTO dto) {
        Topic topic = new Topic(null, dto.title, dto.description, dto.closed);
        topicService.createTopic(topic);
        return Response.status(Response.Status.CREATED).entity(topic).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid TopicDTO dto) {
        Topic existing = topicService.getTopicById(id);
        if (existing == null) return Response.status(Response.Status.NOT_FOUND).build();

        existing.setTitle(dto.title);
        existing.setDescription(dto.description);

        topicService.updateTopic(existing);
        return Response.ok(existing).build();
    }

    @POST
    @Path("/{id}/close")
    public Response closeTopic(@PathParam("id") Long id, TopicCloseRequestDTO dto) {
        try {
            Topic topic = topicService.closeTopic(id, dto != null && dto.simulateFailure);
            return Response.ok(topic).build();
        } catch (TopicClosureException e) {
            Response.Status status = "Тему не знайдено.".equals(e.getMessage())
                    ? Response.Status.NOT_FOUND
                    : Response.Status.CONFLICT;
            return Response.status(status).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        topicService.deleteTopic(id);
        return Response.noContent().build();
    }
}
