package com.infamous.zod.songmgmt.controller;

import java.io.InputStream;
import java.util.List;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public interface SongManagementController {

    Response upload(FormDataBodyPart songInfoPart, InputStream file, FormDataContentDisposition metadataFile);

    Response multipleUpload(FormDataBodyPart songInfoParts, List<FormDataBodyPart> fileParts);

    Response getAll();
}
