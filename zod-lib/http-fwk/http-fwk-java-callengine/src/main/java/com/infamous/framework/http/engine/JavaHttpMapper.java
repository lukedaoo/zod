package com.infamous.framework.http.engine;

import com.infamous.framework.http.core.BodyAsByteArray;
import com.infamous.framework.http.core.BodyAsString;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.RequestBody;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Optional;

class JavaHttpMapper {


    static java.net.http.HttpRequest.Builder mapBody(java.net.http.HttpRequest.Builder builder, HttpRequest request) {
        String httpMethod = request.getMethod().name();
        Optional<RequestBody> body = request.getBody();
        return body
            .map(bodyEntity -> mapEntity(builder, httpMethod, bodyEntity))
            .orElse(builder);
    }

    private static java.net.http.HttpRequest.Builder mapEntity(java.net.http.HttpRequest.Builder builder,
                                                               String httpMethod, RequestBody bodyEntity) {
        if (bodyEntity.isBodyEntity()) {
            BodyPart part = bodyEntity.entity();
            if (bodyEntity.isEmpty()) {
                return builder.method(httpMethod, BodyPublishers.noBody());
            }
            if (part instanceof BodyAsString) {
                return builder.method(httpMethod,
                    BodyPublishers.ofString(((BodyAsString) part).getValue()));
            } else if (part instanceof BodyAsByteArray) {
                return builder.method(httpMethod,
                    BodyPublishers.ofByteArray(((BodyAsByteArray) part).getValue()));
            }
        } else if (bodyEntity.isMultiPart()) {
            // TODO
            return builder;
        }
        return builder;
    }

}
