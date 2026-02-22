package com.example.forum.rest;

import com.example.forum.dto.PostDTO;
import com.example.forum.model.Post;
import com.example.forum.model.User;
import com.example.forum.service.PostService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    @Inject
    private PostService postService;

    @GET
    public Response getPosts(
            @QueryParam("topicId") Long topicId,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        if (topicId == null) return Response.status(400).entity("topicId is required").build();

        List<Post> results = postService.getFilteredPosts(topicId, page, size);
        return Response.ok(results).build();
    }

    @POST
    public Response create(@Valid PostDTO dto) {
        // Заглушка автора (в реальності беремо з контексту безпеки)
        User author = new User(dto.authorName != null ? dto.authorName : "Guest", "user");

        Post post = new Post(null, dto.content, author, dto.topicId);
        postService.createPost(post);

        return Response.status(Response.Status.CREATED).entity(post).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid PostDTO dto) {
        Post existing = postService.getPostById(id);
        if (existing == null) return Response.status(404).build();

        existing.setContent(dto.content);
        postService.updatePost(existing);

        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        postService.deletePost(id);
        return Response.noContent().build();
    }
}