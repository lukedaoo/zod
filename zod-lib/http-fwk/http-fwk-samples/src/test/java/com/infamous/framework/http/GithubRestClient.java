package com.infamous.framework.http;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestClient(category = "githubRestClient")
public interface GithubRestClient {


    @Rest(method = HttpMethod.GET, url = "/users")
    List<GithubUserVo> listUsers();

    @Async
    @Rest(method = HttpMethod.GET, url = "/users")
    CompletableFuture<List<GithubUserVo>> listUsersAsync();

    @Rest(method = HttpMethod.GET, url = "/users/{userId}")
    GithubUserVo user(@PathParam("userId") String userId);

    @Async
    @Rest(method = HttpMethod.GET, url = "/search/repositories")
    CompletableFuture<GithubRepositoriesVo> findRepo(@QueryParam("q") String query);

}
