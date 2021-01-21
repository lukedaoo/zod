package com.infamous.framework.http;

import com.infamous.framework.factory.JacksonConverterFactory;
import com.infamous.framework.http.engine.JavaHttpEngine;
import com.infamous.framework.http.factory.ZodHttpClientFactory;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Demo {


    public static void main(String[] args) {
        ZodHttpClientFactory clientFactory =
            ZodHttpClientFactory.builder()
                .baseUrl("http://localhost:8080/storage/v1")
                .converterFactory(JacksonConverterFactory.create())
                .callEngine(new JavaHttpEngine())
                .config(new HttpConfig())
                .build();

        StorageFileClient client = clientFactory.create(StorageFileClient.class);

        var obj = client
            .upload("https://api.github.com/users/infamouSs", "fileId", new File("src/resources/test.txt"));

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                System.out.println(obj.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}

@RestClient(category = "storageFileRestClient")
interface StorageFileClient {

    @Rest(method = HttpMethod.GET, url = "/info")
    List<StorageFileVO> info();

    @Rest(method = HttpMethod.GET, url = "/info/{fileId}")
    List<StorageFileVO> info(@PathParam("fileId") String fileId);

    @Async
    @MultiPart
    @Rest(method = HttpMethod.POST, url = "/upload")
    CompletableFuture<GitHubUserVO> upload(@Url(fullUrl = true) String url, @Part("fileId") String fileId,
                                     @Part("file") File file);

    @Headers({
        "Static-Header-Name: Static-Header-Value"
    })
    @Rest(method = HttpMethod.POST, url = "/upload")
    Object upload(@Body StorageFileVO storageFileVO);
}

class GitHubUserVO {
    private String m_login;
    private String m_id;

    public String getLogin() {
        return m_login;
    }

    public GitHubUserVO setLogin(String login) {
        m_login = login;
        return this;
    }

    public String getId() {
        return m_id;
    }

    public GitHubUserVO setId(String id) {
        m_id = id;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GitHubUserVO{");
        sb.append("m_login='").append(m_login).append('\'');
        sb.append(", m_id='").append(m_id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

class StorageFileVO {

    private String m_id;
    private String m_fileName;
    private long m_fileSize;
    private String m_extension;
    private boolean m_enabled = true;
    private InputStream m_content;

    public String getId() {
        return m_id;
    }

    public StorageFileVO setId(String id) {
        m_id = id;
        return this;
    }

    public String getFileName() {
        return m_fileName;
    }

    public StorageFileVO setFileName(String fileName) {
        m_fileName = fileName;
        return this;
    }

    public long getFileSize() {
        return m_fileSize;
    }

    public StorageFileVO setFileSize(long fileSize) {
        m_fileSize = fileSize;
        return this;
    }

    public String getExtension() {
        return m_extension;
    }

    public StorageFileVO setExtension(String extension) {
        m_extension = extension;
        return this;
    }

    public boolean isEnabled() {
        return m_enabled;
    }

    public StorageFileVO setEnabled(boolean enabled) {
        m_enabled = enabled;
        return this;
    }

    public InputStream getContent() {
        return m_content;
    }

    public StorageFileVO setContent(InputStream content) {
        m_content = content;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StorageFileVO that = (StorageFileVO) o;
        return Objects.equals(m_id, that.m_id) &&
            Objects.equals(m_fileName, that.m_fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_id, m_fileName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StorageFileVO{");
        sb.append("m_id='").append(m_id).append('\'');
        sb.append(", m_fileName='").append(m_fileName).append('\'');
        sb.append(", m_fileSize=").append(m_fileSize);
        sb.append(", m_extension='").append(m_extension).append('\'');
        sb.append(", m_enabled=").append(m_enabled);
        sb.append(", m_content=").append(m_content);
        sb.append('}');
        return sb.toString();
    }
}
