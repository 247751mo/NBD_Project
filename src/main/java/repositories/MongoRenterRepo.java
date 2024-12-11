package repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.Renter;
import org.bson.conversions.Bson;
import java.util.ArrayList;

public class MongoRenterRepo extends AbstractMongoRepository {

    public Renter read(String id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<Renter> collection = getDatabase().getCollection("renters", Renter.class);
        FindIterable<Renter> renters = collection.find(filter);
        return renters.first();
    }

    public ArrayList<Renter> readAll() {
        MongoCollection<Renter> collection = getDatabase().getCollection("renters", Renter.class);
        return collection.find().into(new ArrayList<>());
    }

    public void create(Renter renter) {
        MongoCollection<Renter> collection = getDatabase().getCollection("renters", Renter.class);
        System.out.println("Inserting renter: " + renter);
        try {
            collection.insertOne(renter);
            System.out.println("Successfully inserted: " + renter);
        } catch (Exception e) {
            System.out.println("Error inserting renter: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void delete(Renter renter) {
        Bson filter = Filters.eq("_id", renter.getPersonalID());
        MongoCollection<Renter> collection = getDatabase().getCollection("renters", Renter.class);
        collection.findOneAndDelete(filter);
    }

    public void update(Renter renter) {
        Bson filter = Filters.eq("_id", renter.getPersonalID());
        MongoCollection<Renter> collection = getDatabase().getCollection("renters", Renter.class);
        Bson updates = Updates.combine(
                Updates.set("firstName", renter.getFirstName()),
                Updates.set("lastName", renter.getLastName()),
                Updates.set("personalID", renter.getPersonalID()),
                Updates.set("currentRentsNumber", renter.getCurrentRentsNumber()),
                Updates.set("archived", renter.isArchived())
        );
        collection.findOneAndUpdate(filter, updates);
    }
}