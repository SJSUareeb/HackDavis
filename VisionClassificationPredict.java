package com.example.fishapp;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Storage;
import com.google.cloud.automl.v1.AnnotationPayload;
import com.google.cloud.automl.v1.ExamplePayload;
import com.google.cloud.automl.v1.Image;
import com.google.cloud.automl.v1.ModelName;
import com.google.cloud.automl.v1.PredictRequest;
import com.google.cloud.automl.v1.PredictResponse;
import com.google.cloud.automl.v1.PredictionServiceClient;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageOptions;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class VisionClassificationPredict {


    public static void main(String[] args) throws IOException {
        // TODO(developer): Replace these variables before running the sample.
        String projectId = "YOUR_PROJECT_ID";
        String modelId = "YOUR_MODEL_ID";
        String filePath = "path_to_local_file.jpg";
        //predict(projectId, modelId, filePath);
    }

    public String predict(String projectId, String modelId, String filePath) throws IOException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        String result="";
        try (PredictionServiceClient client = PredictionServiceClient.create()) {
            // Get the full path of the model.
            ModelName name = ModelName.of(projectId, "us-central1", modelId);
            ByteString content = ByteString.copyFrom(Files.readAllBytes(Paths.get(filePath)));
            Image image = Image.newBuilder().setImageBytes(content).build();
            ExamplePayload payload = ExamplePayload.newBuilder().setImage(image).build();
            PredictRequest predictRequest =
                    PredictRequest.newBuilder()
                            .setName(name.toString())
                            .setPayload(payload)
                            .putParams(
                                    "score_threshold", "0.8") // [0.0-1.0] Only produce results higher than this value
                            .build();

            PredictResponse response = client.predict(predictRequest);
//
//            for (AnnotationPayload annotationPayload : response.getPayloadList()) {
//                Toast.makeText(VisionClassificationPredict.this, "Configuration change", Toast.LENGTH_SHORT).show();
//
//                System.out.format("Predicted class name: %s\n", annotationPayload.getDisplayName());
//                System.out.format(
//                        "Predicted class score: %.2f\n", annotationPayload.getClassification().getScore());
//            }
            response.getPayloadList().get(0).getDisplayName();
        }
        return result;
    }

    static void authImplicit() {
        // If you don't specify credentials when constructing the client, the client library will
        // look for credentials via the environment variable GOOGLE_APPLICATION_CREDENTIALS.
        Storage storage = StorageOptions.getDefaultInstance().getService();

        System.out.println("Buckets:");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
    }
}
