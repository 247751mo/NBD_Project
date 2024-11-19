package repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import model.*;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;

public abstract class AbstractMongoRepository implements AutoCloseable {

    private final ConnectionString connectionString = new ConnectionString("mongodb://mongodb1:27017,mongodb2:27017,mongodb3:27017/?replicaSet=replica_set_single");
    private final MongoCredential credential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(
                    PojoCodecProvider.builder()
                            .automatic(true)
                            .conventions(Conventions.DEFAULT_CONVENTIONS)
                            .register(Book.class,Card.class,Monthly.class, NoCard.class, Publication.class, Rent.class, Renter.class, RenterType.class,Volume.class,Weekly.class)
                            .build()
            )
    );

    private MongoClient mongoClient;
    private MongoDatabase database;

    public void  initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation (UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        CodecRegistries.fromCodecs(new VirtualMachineCodec()),
                        pojoCodecRegistry
                ))
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("rental");
        if (!getDatabase().listCollectionNames().into(new ArrayList<>()).contains("books")) {
            createBooksCollection();
        }
    }
//Miejsce na create collection
//


    public MongoClient getMongoClient() {
        if (mongoClient == null) {
            initDbConnection();
        }
        return mongoClient;
    }

    public MongoDatabase getDatabase() {
        if (database == null) {
            initDbConnection();
        }
        return database;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}