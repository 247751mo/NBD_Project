package repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import model.Volume;
import java.util.ArrayList;
import java.util.UUID;

public class VolumeRepo extends AbstractMongoRepository {

    public void create(Volume volume) {
        MongoCollection<Volume> collection = getDatabase().getCollection("volumes", Volume.class);
        collection.insertOne(volume);
    }

    public Volume read(String id) {
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
                Updates.set("rentedStatus", volume.isRented()),
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
