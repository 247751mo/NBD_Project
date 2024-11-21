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

//public class RentRepo extends AbstractMongoRepository {
//
//    public Rent read(String id) {
//        Bson filter = Filters.eq("_id", id);
//        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
//        FindIterable<Rent> rents = collection.find(filter);
//        return rents.first();
//    }
//
//    public ArrayList<Rent> readAll() {
//        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
//        return collection.find().into(new ArrayList<>());}
//
//
//    public void create(Rent rent) {
//        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
//        collection.insertOne(rent);
//    }
//
//    // Delete Rent
//    public void delete(Rent rent) {
//        Bson filter = Filters.eq("_id", rent.getId());
//        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
//        collection.findOneAndDelete(filter);
//    }
//
//
//    public void update(Rent rent) {
//        Bson filter = Filters.eq("_id", rent.getId());
//        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
//        Bson updates = Updates.combine(
//                Updates.set("renter", rent.getRenter()),
//                Updates.set("volume", rent.getVolume()),
//                Updates.set("beginTime", rent.getBeginTime()),
//                Updates.set("endTime", rent.getEndTime())
//        );
//        collection.findOneAndUpdate(filter, updates);
//    }
//
//
//    public void bookVolume(Renter renter, Volume volume, LocalDateTime beginTime) {
//        try (ClientSession clientSession = getMongoClient().startSession()) {
//            clientSession.startTransaction();
//
//            MongoCollection<Volume> volumeCollection = getDatabase().getCollection("volumes", Volume.class);
//            MongoCollection<Renter> renterCollection = getDatabase().getCollection("renters", Renter.class);
//            MongoCollection<Rent> rentCollection = getDatabase().getCollection("rents", Rent.class);
//
//            Bson volumeFilter = Filters.eq("_id", volume.getVolumeId());
//            Bson renterFilter = Filters.eq("_id", renter.getPersonalID());
//
//            Volume existingVolume = volumeCollection.find(volumeFilter).first();
//            if (existingVolume == null || existingVolume.isRented()) {
//                throw new IllegalStateException("Booking failed: Volume does not exist or is already unavailable.");
//            }
//
//            // Update `isRented` directly in the database
//            existingVolume.setRented(true);
//
//            // Increment rent count for the renter
//            renterCollection.findOneAndUpdate(clientSession, renterFilter, Updates.inc("rents", 1));
//
//            Renter updatedRenter = renterCollection.find(renterFilter).first();
//            Volume updatedVolume = volumeCollection.find(volumeFilter).first();
//
//            // Insert the rent record
//            Rent rent = new Rent(updatedRenter, updatedVolume, beginTime);
//            rentCollection.insertOne(clientSession, rent);
//
//            clientSession.commitTransaction();
//        } catch (Exception e) {
//            throw new RuntimeException("Booking volume failed", e);
//        }
//    }
//
//
//
//
//    public void returnVolume(Rent rent, LocalDateTime endTime) {
//        try (ClientSession clientSession = getMongoClient().startSession()) {
//            clientSession.startTransaction();
//
//            MongoCollection<Volume> volumeCollection = getDatabase().getCollection("volumes", Volume.class);
//            MongoCollection<Renter> renterCollection = getDatabase().getCollection("renters", Renter.class);
//            MongoCollection<Rent> rentCollection = getDatabase().getCollection("rents", Rent.class);
//
//
//            Bson volumeFilter = Filters.eq("_id", rent.getVolume().getVolumeId());
//            Bson renterFilter = Filters.eq("_id", rent.getRenter().getPersonalID());
//
//            Volume existingVolume = volumeCollection.find(volumeFilter).first();
//            if (existingVolume == null || !existingVolume.isRented()) {
//                throw new IllegalStateException("Returning failed: Volume does not exist or is already available.");
//            }
//            existingVolume.setRented(false);
//            volumeCollection.replaceOne(clientSession, volumeFilter, existingVolume);
//
//            renterCollection.findOneAndUpdate(clientSession, renterFilter, Updates.inc("rents", -1));
//
//            Renter updatedRenter = renterCollection.find(renterFilter).first();
//            Volume updatedVolume = volumeCollection.find(volumeFilter).first();
//
//            rent.endRent();
//
//            Rent updatedRent = new Rent(rent.getId(), updatedRenter, updatedVolume, rent.getBeginTime());
//            updatedRent.endRent();
//
//            Bson rentFilter = Filters.eq("_id", rent.getId());
//            Bson updates = Updates.combine(
//                    Updates.set("renter", updatedRenter),
//                    Updates.set("volume", updatedVolume),
//                    Updates.set("rentEnd", updatedRent.getEndTime())
//            );
//            rentCollection.findOneAndUpdate(clientSession, rentFilter, updates);
//
//            clientSession.commitTransaction();
//        } catch (Exception e) {
//            throw new RuntimeException("Returning volume failed", e);
//        }
//    }
//}


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
            Bson renterUpdates = Updates.inc("currentRentsNumber", 1);
            rentersCollection.updateOne(clientSession, renterFilter, renterUpdates);
            Renter updatedRenter = rentersCollection.find(renterFilter).first();
            rent.getRenter().setCurrentRentsNumber(updatedRenter.getCurrentRentsNumber());
            getDatabase().getCollection("renters").find(filter).forEach(doc ->System.out.println(doc.toJson()));

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