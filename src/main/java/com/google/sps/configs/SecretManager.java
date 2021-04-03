package com.google.sps.configs;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;

public final class SecretManager {

  public String getSecret(String secretId) throws Exception {
    // Initialize client that will be used to send requests. This client only needs to be created
    // once, and can be reused for multiple requests. After completing all of your requests, call
    // the "close" method on the client to safely clean up any remaining background resources.
    try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
      // Access the secret version.
      AccessSecretVersionResponse response = client.accessSecretVersion(secretId);
      return response.getPayload().getData().toStringUtf8();
    }
  }
}
