package repositories;

import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.Rent;
import model.Renter;
import model.Volume;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RentRepo extends AbstractMongoRepository {

    public Rent get(String id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        FindIterable<Rent> rents = collection.find(filter);
        return rents.first();
    }

    public ArrayList<Rent> getAll() {
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        return collection.find().into(new ArrayList<>());}


    public void add(Rent rent) {
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        collection.insertOne(rent);
    }

    // Delete Rent
    public void delete(Rent rent) {
        Bson filter = Filters.eq("_id", rent.getId());
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        collection.findOneAndDelete(filter);
    }


    public void update(Rent rent) {
        Bson filter = Filters.eq("_id", rent.getId());
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        Bson updates = Updates.combine(
                Updates.set("renter", rent.getRenter()),
                Updates.set("volume", rent.getVolume()),
                Updates.set("beginTime", rent.getBeginTime()),
                Updates.set("endTime", rent.getEndTime())
        );
        collection.findOneAndUpdate(filter, updates);
    }


    public void bookVolume(Renter renter, Volume volume, LocalDateTime beginTime) {
        try (ClientSession clientSession = getMongoClient().startSession()) {
            clientSession.startTransaction();

            MongoCollection<Volume> volumeCollection = getDatabase().getCollection("volumes", Volume.class);
            MongoCollection<Renter> renterCollection = getDatabase().getCollection("renters", Renter.class);
            MongoCollection<Rent> rentCollection = getDatabase().getCollection("rents", Rent.class);

            Bson volumeFilter = Filters.eq("_id", volume.getVolumeId());
            Bson renterFilter = Filters.eq("_id", renter.getPersonalID());

            Volume existingVolume = volumeCollection.find(volumeFilter).first();
            if (existingVolume == null || existingVolume.isRented()) {
                throw new IllegalStateException("Booking failed: Volume does not exist or is already unavailable.");
            }
            existingVolume.setRented(true);
            volumeCollection.replaceOne(clientSession, volumeFilter, existingVolume);

            renterCollection.findOneAndUpdate(clientSession, renterFilter, Updates.inc("rents", 1));

            Renter updatedRenter = renterCollection.find(renterFilter).first();
            Volume updatedVolume = volumeCollection.find(volumeFilter).first();

            Rent rent = new Rent(updatedRenter, updatedVolume, beginTime);
            rentCollection.insertOne(clientSession, rent);

            clientSession.commitTransaction();
        } catch (Exception e) {
            throw new RuntimeException("Booking volume failed", e);
        }
    }



    public void returnVolume(Rent rent, LocalDateTime endTime) {
        try (ClientSession clientSession = getMongoClient().startSession()) {
            clientSession.startTransaction();

            MongoCollection<Volume> volumeCollection = getDatabase().getCollection("volumes", Volume.class);
            MongoCollection<Renter> renterCollection = getDatabase().getCollection("renters", Renter.class);
            MongoCollection<Rent> rentCollection = getDatabase().getCollection("rents", Rent.class);


            Bson volumeFilter = Filters.eq("_id", rent.getVolume().getVolumeId());
            Bson renterFilter = Filters.eq("_id", rent.getRenter().getPersonalID());

            Volume existingVolume = volumeCollection.find(volumeFilter).first();
            if (existingVolume == null || !existingVolume.isRented()) {
                throw new IllegalStateException("Returning failed: Volume does not exist or is already available.");
            }
            existingVolume.setRented(false);
            volumeCollection.replaceOne(clientSession, volumeFilter, existingVolume);

            renterCollection.findOneAndUpdate(clientSession, renterFilter, Updates.inc("rents", -1));

            Renter updatedRenter = renterCollection.find(renterFilter).first();
            Volume updatedVolume = volumeCollection.find(volumeFilter).first();

            rent.endRent();

            Rent updatedRent = new Rent(rent.getId(), updatedRenter, updatedVolume, rent.getBeginTime());
            updatedRent.endRent();

            Bson rentFilter = Filters.eq("_id", rent.getId());
            Bson updates = Updates.combine(
                    Updates.set("renter", updatedRenter),
                    Updates.set("volume", updatedVolume),
                    Updates.set("rentEnd", updatedRent.getEndTime())
            );
            rentCollection.findOneAndUpdate(clientSession, rentFilter, updates);

            clientSession.commitTransaction();
        } catch (Exception e) {
            throw new RuntimeException("Returning volume failed", e);
        }
    }
}
