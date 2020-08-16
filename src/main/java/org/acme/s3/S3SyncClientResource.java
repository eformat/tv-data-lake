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
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class S3SyncClientResource {

    private final Logger log = LoggerFactory.getLogger(S3SyncClientResource.class);

    @Inject
    S3Client s3;

    @ConfigProperty(name = "r1.bucket.name")
    String r1bucketName;

    @ConfigProperty(name = "r5.bucket.name")
    String r5bucketName;

    @ConfigProperty(name = "t1.bucket.name")
    String t1bucketName;

    @ConfigProperty(name = "t5.bucket.name")
    String t5bucketName;

    @Incoming("tv-in-r1")
    public void storeInS3R1(JsonObject aggregation) {
        String cacheKey = String.format("%s-%s-%s", "r1", aggregation.getString("key"), aggregation.getString("instant"));
        PutObjectResponse putResponse = s3.putObject(buildPutRequest(r1bucketName, cacheKey, aggregation), RequestBody.fromString(aggregation.toString()));
        if (putResponse != null) {
            // ok
            log.debug("::storeInS3R1 ok: " + putResponse);
        } else {
            log.warn("::storeInS3R1 error: " + aggregation);
        }
    }

    @Incoming("tv-in-r5")
    public void storeInS3R5(JsonObject aggregation) {
        String cacheKey = String.format("%s-%s-%s", "r5", aggregation.getString("key"), aggregation.getString("instant"));
        PutObjectResponse putResponse = s3.putObject(buildPutRequest(r1bucketName, cacheKey, aggregation), RequestBody.fromString(aggregation.toString()));
        if (putResponse != null) {
            // ok
            log.debug("::storeInS3R5 ok: " + putResponse);
        } else {
            log.warn("::storeInS3R5 error: " + aggregation);
        }
    }

    @Incoming("tv-in-t1")
    public void storeInS3T1(JsonObject aggregation) {
        String cacheKey = String.format("%s-%s-%s", "t1", aggregation.getString("key"), aggregation.getString("instant"));
        PutObjectResponse putResponse = s3.putObject(buildPutRequest(r1bucketName, cacheKey, aggregation), RequestBody.fromString(aggregation.toString()));
        if (putResponse != null) {
            // ok
            log.debug("::storeInS3T1 ok: " + putResponse);
        } else {
            log.warn("::storeInS3T1 error: " + aggregation);
        }
    }

    @Incoming("tv-in-t5")
    public void storeInS3T5(JsonObject aggregation) {
        String cacheKey = String.format("%s-%s-%s", "t5", aggregation.getString("key"), aggregation.getString("instant"));
        PutObjectResponse putResponse = s3.putObject(buildPutRequest(r1bucketName, cacheKey, aggregation), RequestBody.fromString(aggregation.toString()));
        if (putResponse != null) {
            // ok
            log.debug("::storeInS3T5 ok: " + putResponse);
        } else {
            log.warn("::storeInS3T5 error: " + aggregation);
        }
    }

    protected PutObjectRequest buildPutRequest(String bucketName, String key, JsonObject data) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(MediaType.APPLICATION_JSON_TYPE.getSubtype())
                .build();
    }
}
