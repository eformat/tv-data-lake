package org.acme.s3;

import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class S3SyncClientResource {

    private final Logger log = LoggerFactory.getLogger(S3SyncClientResource.class);

    @Inject
    S3Client s3;

    @ConfigProperty(name = "bucket.name", defaultValue = "default-bucket")
    String bucketName;

    @Incoming("tv-in-r1")
    public void storeInS3R1(JsonObject aggregation) {
        String cacheKey = String.format("%s-%s-%s", "r1", aggregation.getString("key"), Instant.now());
        PutObjectResponse putResponse = s3.putObject(buildPutRequest(cacheKey, aggregation), RequestBody.fromString(aggregation.toString()));
        if (putResponse != null) {
            // ok
            log.debug("::storeInS3R1 ok: " + putResponse);
        } else {
            log.warn("::storeInS3R1 error: " + aggregation);
        }
    }

    protected PutObjectRequest buildPutRequest(String key, JsonObject data) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(MediaType.APPLICATION_JSON_TYPE.getSubtype())
                .build();
    }
}
