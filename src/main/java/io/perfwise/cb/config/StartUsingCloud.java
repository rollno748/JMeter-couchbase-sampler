package io.perfwise.cb.config;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.*;
import com.couchbase.client.java.json.*;
import com.couchbase.client.java.query.*;

import java.time.Duration;


public class StartUsingCloud {
    // Update these variables to point to your Couchbase Capella instance and credentials.
    static String connectionString = "cb.o72tsvg3eyaiouj.cloud.couchbase.com";
    static String username = "perfwise";
    static String password = "Perfwise@123";
    static String bucketName = "travel-sample";

    public static void main(String... args) {
        // Simple connection.
//        Cluster cluster = Cluster.connect("couchbases://" + connectionString, username, password);
//        cluster.disconnect();
//
        // Custom environment connection.
        Cluster cluster = Cluster.connect(
                "couchbases://" + connectionString,
                ClusterOptions.clusterOptions(username, password).environment(env -> {
                    // Sets a pre-configured profile called "wan-development" to help avoid latency issues
                    // when accessing Capella from a different Wide Area Network
                    // or Availability Zone (e.g. your laptop).
                    env.applyProfile("wan-development");
                })
        );

        // Get a bucket reference
        Bucket bucket = cluster.bucket(bucketName);
        bucket.waitUntilReady(Duration.ofSeconds(120));

        // Get a user defined collection reference
        Scope scope = bucket.scope("tenant_agent_00");
        Collection collection = scope.collection("users");

        // Upsert Document
//        MutationResult upsertResult = collection.upsert(
//                "my-document",
//                JsonObject.create().put("name", "mike")
//        );

        // Get Document
        GetResult getResult = collection.get("0");
        String name = getResult.contentAsObject().getString("name");
        System.out.println(name); // name == "mike"

        // Call the query() method on the scope object and store the result.
        Scope inventoryScope = bucket.scope("inventory");
        QueryResult result = inventoryScope.query("SELECT * FROM airline WHERE id = 10;");

        // Return the result rows with the rowsAsObject() method and print to the terminal.
        System.out.println(result.rowsAsObject());
    }
}