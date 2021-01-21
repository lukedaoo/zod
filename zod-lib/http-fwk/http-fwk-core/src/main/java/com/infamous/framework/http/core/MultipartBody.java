package com.infamous.framework.http.core;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

interface MultipartBody extends HttpRequest<MultipartBody>, RequestBody {

    MultipartBody part(String name, String value, String contentType);

    MultipartBody part(String name, File file, String contentType);

    MultipartBody part(String name, InputStream stream, String contentType, String fileName);

    MultipartBody part(String name, byte[] bytes, String contentType, String fileName);

    MultipartBody part(BodyPart multiPartInfo);

    MultipartBody charset(Charset charset);
}
