package repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import jakarta.persistence.EntityManager;
import model.Renter;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class RenterRepo extends AbstractMongoRepository {

    private final EntityManager em;

    public RenterRepo(EntityManager entityManager) {
        this.em = entityManager;
    }

    public Renter get(String id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<Renter> collection = getDatabase().getCollection("renters", Renter.class);
        FindIterable<Renter> renters = collection.find(filter);
        return renters.first();
    }

    public ArrayList<Renter> getAll() {
        MongoCollection<Renter> collection = getDatabase().getCollection("renters", Renter.class);
        return collection.find().into(new ArrayList<>());
    }

    public void add(Renter renter) {
        MongoCollection<Renter> collection = getDatabase().getCollection("renters", Renter.class);
        collection.insertOne(renter);
    }

    public void delete(Renter renter) {
        Bson filter = Filters.eq("_id", renter.getId());
        MongoCollection<Renter> collection = getDatabase().getCollection("renters", Renter.class);
        collection.findOneAndDelete(filter);
    }

    public void update(Renter renter) {
        Bson filter = Filters.eq("_id", renter.getId());
        MongoCollection<Renter> collection = getDatabase().getCollection("renters", Renter.class);
        Bson updates = Updates.combine(
                Updates.set("firstName", renter.getFirstName()),
                Updates.set("lastName", renter.getLastName()),
                Updates.set("personalID", renter.getPersonalID()),
                Updates.set("renterType", renter.getRenterType()),
                Updates.set("archived", renter.isArchived())
        );
        collection.findOneAndUpdate(filter, updates);
    }
}