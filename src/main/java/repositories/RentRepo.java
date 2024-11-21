package repositories;

import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import model.Rent;
import model.Renter;
import model.Volume;
import org.bson.conversions.Bson;
import java.util.ArrayList;

public class RentRepo extends AbstractMongoRepository {
    public Rent read(Object id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        FindIterable<Rent> rents = collection.find(filter);
        return rents.first();
    }

    public ArrayList<Rent> readAll() {
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        return collection.find().into(new ArrayList<>());
    }


    public void create(Rent rent) {
        if (rent == null) {
            throw new NullPointerException("Rent is null");
        } else if (rent.getRenter() == null) {
            throw new NullPointerException("Renter is null");
        } else if (rent.getVolume() == null) {
            throw new NullPointerException("Volume is null");
        }
        ClientSession clientSession = getMongoClient().startSession();
        try {
            clientSession.startTransaction();

            MongoCollection<Rent> rentsCollection = getDatabase().getCollection("rents", Rent.class);
            rentsCollection.insertOne(clientSession, rent);

            MongoCollection<Volume> volumeCollection = getDatabase().getCollection("volumes", Volume.class);
            Bson filter = Filters.eq("_id", rent.getVolume().getVolumeId());
            Bson updates = Updates.inc("isRented", 1);
            volumeCollection.findOneAndUpdate(clientSession, filter, updates);
            getDatabase().getCollection("volumes").find(filter).forEach(doc ->System.out.println(doc.toJson()));

            MongoCollection<Renter> rentersCollection = getDatabase().getCollection("renters", Renter.class);
            Bson renterFilter = Filters.eq("_id", rent.getRenter().getPersonalID());
            Bson renterUpdate = Updates.inc("currentRentsNumber",1);
            UpdateResult result = rentersCollection.updateOne(clientSession,renterFilter, renterUpdate);
            System.out.println("Update result: " + result.getModifiedCount());

            Renter renterAfter = rentersCollection.find(renterFilter).first();
            System.out.println("After update: " + renterAfter.getCurrentRentsNumber());

            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
            throw e;
        } finally {
            clientSession.close();
        }
    }


    public void update(Rent rent) {
        ClientSession clientSession = getMongoClient().startSession();
        try {
            clientSession.startTransaction();

            Bson rentFilter = Filters.eq("_id", rent.getId());
            MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
            Bson rentUpdates = Updates.combine(
                    Updates.set("renter", rent.getRenter()),
                    Updates.set("volume", rent.getVolume()),
                    Updates.set("beginTime", rent.getBeginTime()),
                    Updates.set("endTime", rent.getEndTime())
            );
            collection.findOneAndUpdate(clientSession, rentFilter, rentUpdates);

            if (rent.getEndTime() != null) {
                MongoCollection<Volume> volumeCollection = getDatabase().getCollection("volumes", Volume.class);
                Bson volumeFilter = Filters.eq("_id", rent.getVolume().getVolumeId());
                Bson volumeUpdates = Updates.inc("isRented", -1);
                volumeCollection.updateOne(clientSession, volumeFilter, volumeUpdates);

                MongoCollection<Renter> rentersCollection = getDatabase().getCollection("renters", Renter.class);
                Bson renterFilter = Filters.eq("_id", rent.getRenter().getPersonalID());
                Bson renterUpdates = Updates.inc("currentRentsNumber", -1);
                rentersCollection.updateOne(clientSession, renterFilter, renterUpdates);
            }

            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
            throw e;
        } finally {
            clientSession.close();
        }
    }


    public void delete(Rent rent) {
        ClientSession clientSession = getMongoClient().startSession();
        try {
            clientSession.startTransaction();

            Bson rentFilter = Filters.eq("_id", rent.getId());
            MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
            collection.findOneAndDelete(clientSession, rentFilter);

            if (rent.getEndTime() != null) {
                MongoCollection<Volume> volumeCollection = getDatabase().getCollection("volumes", Volume.class);
                Bson vmFilter = Filters.eq("_id", rent.getVolume().getVolumeId());
                Bson updates = Updates.inc("isRented", 1);
                volumeCollection.updateOne(clientSession, vmFilter, updates);

                MongoCollection<Renter> rentersCollection = getDatabase().getCollection("renters", Renter.class);
                Bson renterFilter = Filters.eq("_id", rent.getRenter().getPersonalID());
                Bson renterUpdates = Updates.inc("currentRentsNumber", -1);
                rentersCollection.updateOne(clientSession, renterFilter, renterUpdates);
            }

            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
            throw e;
        } finally {
            clientSession.close();
        }
    }

}