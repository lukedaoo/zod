package com.infamous.zod.storage;

import com.infamous.framework.factory.JacksonConverterFactory;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.engine.JavaHttpEngine;
import com.infamous.framework.http.factory.ZodHttpClientFactory;
import com.infamous.zod.storage.model.StorageFileVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class StorageRestClientDemo {

    private static final String BASE_URL = "http://localhost:8080/storage/v1";

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        StorageRestClient m_client;

        ZodHttpClientFactory clientFactory =
            ZodHttpClientFactory.builder()
                .baseUrl(BASE_URL)
                .converterFactory(JacksonConverterFactory.create())
                .callEngine(JavaHttpEngine.getInstance())
                .config(new HttpConfig())
                .build();

        m_client = clientFactory.create(StorageRestClient.class);

//        testDownload(m_client);
//        testMultipleDownload(m_client);
//        testUpload(m_client);
//        testMultipleUpload(m_client);
//        testInfo(m_client);
//        testAllInfos(m_client);
    }

    private static void testAllInfos(StorageRestClient m_client) throws InterruptedException, ExecutionException {
        List<StorageFileVO> infos = m_client.info().get();

        System.out.println(infos);
    }

    private static void testInfo(StorageRestClient m_client) throws InterruptedException, ExecutionException {
        StorageFileVO info = m_client.info("021d65fe-5c61-42e6-b508-c540871abc8d").get();
        System.out.println(info);
    }

    private static void testMultipleUpload(StorageRestClient m_client) throws IOException {
        Path path = Path
            .of("zod-fileserver/zod-storage-service/storage-service-restclient/src/test/resources/theme.mp3");

        InputStream is = new FileInputStream(path.toFile());
        String contentType = Files.probeContentType(path);
        List<BodyPart<InputStream>> bodyParts = new ArrayList<>();
        bodyParts.add(create(path, contentType, "file1.mp3"));
        bodyParts.add(create(path, contentType, "file2.mp3"));
        bodyParts.add(create(path, contentType, "file3.mp3"));
        bodyParts.add(create(path, contentType, "file4.mp3"));

        var f = m_client.upload(bodyParts);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            var x = f.join();
            System.out.println(x);
            executor.shutdown();
        });
    }

    private static void testUpload(StorageRestClient m_client) throws IOException {
        Path path = Path
            .of("zod-fileserver/zod-storage-service/storage-service-restclient/src/test/resources/theme.mp3");

        InputStream is = new FileInputStream(path.toFile());
        String contentType = Files.probeContentType(path);

        var uploadRes = m_client.upload(new BodyPart<>("file", is, contentType) {
            @Override
            public boolean isFile() {

                return true;
            }

            @Override
            public String getFileName() {
                return "theme.mp3";
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            var x = uploadRes.join();
            System.out.println(x);

            executor.shutdown();
        });
    }

    private static void testMultipleDownload(StorageRestClient m_client) {
        var multipleDownloadRes = m_client
            .multipleDownload(List.of("6b05e317-4f4a-4702-82f4-4ded478db559", "5705560e-636d-408d-b96e-dbd0fab35855"));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                var res = multipleDownloadRes.join();

                File x = new File("x.zip");
                try (FileOutputStream outputStream = new FileOutputStream(x, false)) {
                    int read;
                    byte[] bytes = new byte[1024];
                    while ((read = res.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            executor.shutdown();
        });
    }

    private static void testDownload(StorageRestClient m_client) {
        var downloadRes = m_client.download("aae6e770-2727-40bf-850d-d9a6bedc3047");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            InputStream res = downloadRes.join();

            File x = new File("x.flac");
            try (FileOutputStream outputStream = new FileOutputStream(x, false)) {
                int read;
                byte[] bytes = new byte[1024];
                while ((read = res.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            executor.shutdown();
        });
    }

    private static BodyPart<InputStream> create(Path path, String contentType, String fileName)
        throws FileNotFoundException {
        return new BodyPart<InputStream>("files", new FileInputStream(path.toFile()), contentType) {
            @Override
            public boolean isFile() {

                return true;
            }

            @Override
            public String getFileName() {
                return fileName;
            }
        };
    }
}