package io.perfwise.cb.sampler;

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.couchbase.client.java.query.QueryOptions.queryOptions;

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
    public MutationResult dataOperations(int type, String data){
        JsonObject content = JsonObject.fromJson(data);
        MutationResult result = null;

        switch (type){
            case CBSamplerBeanInfo.INSERT:
                try{
//                    result = collection.insert(key, json);
                }catch (DocumentNotFoundException ex){
                    LOGGER.info("Document not found");
                }
                break;

            case CBSamplerBeanInfo.GET:
                GetResult getResult = collection.get(content.getString("key"));
//                result = getResult.contentAsObject();
                break;

            case CBSamplerBeanInfo.UPSERT:
//                result = collection.upsert(key, json);
                break;

            case CBSamplerBeanInfo.REMOVE:
                try{
                    collection.remove(content.getString("key"));
                }catch (DocumentNotFoundException dnfe){
                    LOGGER.error("Document not found exception " + dnfe);
                }

                break;

            default:
                LOGGER.info("Invalid operation Selected - Please check the sampler operation selection");
                LOGGER.info("Aborting Test..");
//                testEnded();
        }

        return null;
    }

    // Query Operations (Mostly N1ql)
    public QueryResult queryOperations(String data){
        QueryResult result = null;

        try{
            result = cluster.query(data, queryOptions().metrics(true));
        }catch (CouchbaseException ce){
            LOGGER.info("Couchbase exception occurred while w=executing N1ql query ");
            ce.printStackTrace();
        }
        return result;
    }
}
