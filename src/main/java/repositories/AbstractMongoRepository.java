package repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationOptions;
import model.*;
import org.bson.BsonType;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMongoRepository implements AutoCloseable {

    private ConnectionString connectionString = new ConnectionString("mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single");
    private MongoCredential credential = MongoCredential.createCredential("nbd", "admin", "nbdpassword".toCharArray());

    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(
                    PojoCodecProvider.builder()
                            .automatic(true)
                            .conventions(Conventions.DEFAULT_CONVENTIONS)
                            .register(Book.class,Card.class,Monthly.class, NoCard.class, Publication.class, Rent.class, Renter.class, RenterType.class,Volume.class,Weekly.class)
                            .build()
            ),
            CodecRegistries.fromCodecs(new RenterTypeCodec())
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
                        CodecRegistries.fromCodecs(new VolumeCodec()),
                        pojoCodecRegistry
                ))
                .build();
        System.out.println("PojoCodecProvider registered");

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("rental");
        if (!getDatabase().listCollectionNames().into(new ArrayList<>()).contains("volumes")) {
            createVolumesCollection();
        }
        if (!getDatabase().listCollectionNames().into(new ArrayList<>()).contains("renters")) {
            createRentersCollection();
        }
    }
    private void createRentersCollection() {
        try {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Filters.jsonSchema(
                            new Document("bsonType", "object")
                                    .append("required", List.of("_id", "firstName", "lastName", "renterType", "rents"))
                                    .append("properties", new Document()
                                            .append("_id", new Document("bsonType", "string"))
                                            .append("firstName", new Document("bsonType", "string"))
                                            .append("lastName", new Document("bsonType", "string"))
                                            .append("renterType", new Document("bsonType", "object")
                                                    .append("required", List.of("_type"))
                                                    .append("properties", new Document()
                                                            .append("_type", new Document("bsonType", "string"))))
                                            .append("isArchived", new Document("bsonType", "boolean"))
                                            .append("rents", new Document("bsonType", "int")
                                                    .append("minimum", 0)
                                                    .append("maximum", 10))
                                    )
                    )
            ).validationAction(ValidationAction.ERROR);

            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                    .validationOptions(validationOptions);

            getDatabase().createCollection("renters", createCollectionOptions);
        } catch (Exception e) {
            System.err.println("Error creating 'renters' collection: " + e.getMessage());
        }
    }

    private void createVolumesCollection() {
        Bson isRentedType = Filters.type("isRented", BsonType.BOOLEAN);
        Bson isRentedYes = Filters.eq("isRented", true);
        Bson isRentedNo = Filters.eq("isRented", false);

        ValidationOptions validationOptions = new ValidationOptions()
                .validator(Filters.and(isRentedType, isRentedYes, isRentedNo));

        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);
        getDatabase().createCollection("volumes", createCollectionOptions);
    }



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