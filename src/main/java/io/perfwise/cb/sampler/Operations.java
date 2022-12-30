package io.perfwise.cb.sampler;

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Operations {

    private Scope scope = null;
    private Collection collection = null;
    private Cluster cluster = null;
    private static Logger LOGGER = LoggerFactory.getLogger(Operations.class);

    public Operations(Scope scope, Collection collection){
        this.scope=scope;
        this.collection=collection;
    }

    public Operations(Cluster cluster){
        this.cluster = cluster;
    }

    // Data Operations (Insert/Get/Upsert/Remove)
    public JsonObject dataOperations(String type, String data){

        switch (type.toLowerCase()){
            case "get":
                try{
//                    GetResult result = collection.get();
                }catch (DocumentNotFoundException ex){
                    LOGGER.info("Document not found");
                }
                break;

            case "insert":
                break;

            case "upsert":
                break;

            case "remove":
                break;

        }

        return null;
    }

    // Query Operations (Mostly N1ql)
    public void queryOperations(Cluster cluster, String data){

        try{
            final QueryResult result = cluster.query(data);
            List<JsonObject> test = result.rowsAsObject();
        }catch (CouchbaseException ce){
            LOGGER.info("Couchbase exception occurred while w=executing N1ql query ");
            ce.printStackTrace();
        }


    }
}
