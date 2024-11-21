package repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import model.Volume;
import java.util.ArrayList;


public class VolumeRepo extends AbstractMongoRepository {

    public void create(Volume volume) {
        try {
            MongoCollection<Volume> collection = getDatabase().getCollection("volumes", Volume.class);
            collection.insertOne(volume);
            System.out.println("Inserted volume with ID: " + volume.getVolumeId());
        } catch (Exception e) {
            throw new RuntimeException("Error inserting volume: " + e.getMessage(), e);
        }
    }

    public Volume read(Object id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<Volume> collection = getDatabase().getCollection("volumes", Volume.class);
        FindIterable<Volume> volumes = collection.find(filter);
        return volumes.first();
    }

    public ArrayList<Volume> readAll() {
        MongoCollection<Volume> collection = getDatabase().getCollection("volumes", Volume.class);
        return collection.find().into(new ArrayList<>());
    }

    public void update(Volume volume) {
        Bson filter = Filters.eq("_id", volume.getVolumeId());
        MongoCollection<Volume> collection = getDatabase().getCollection("volumes", Volume.class);
        Bson updates = Updates.combine(
                Updates.set("title", volume.getTitle()),
                Updates.set("genre", volume.getGenre()),
                Updates.set("rentedStatus", volume.getIsRented()),
                Updates.set("archivedStatus", volume.isArchive())
        );
        collection.findOneAndUpdate(filter, updates);
    }

    public void delete(Volume volume) {
        Bson filter = Filters.eq("_id", volume.getVolumeId());
        MongoCollection<Volume> collection = getDatabase().getCollection("volumes", Volume.class);
        collection.findOneAndDelete(filter);
    }
}
