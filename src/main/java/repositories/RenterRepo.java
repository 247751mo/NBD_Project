package repositories;

import model.Renter;

import java.util.ArrayList;

public class RenterRepo {

    private final RedisRenterRepo redisRenterRepo;
    private final MongoRenterRepo mongoRenterRepo;

    public RenterRepo(RedisRenterRepo redisRenterRepo, MongoRenterRepo mongoRenterRepo) {
        this.redisRenterRepo = redisRenterRepo;
        this.mongoRenterRepo = mongoRenterRepo;
    }

    public Renter findById(String id) {
        Renter cachedRenter = redisRenterRepo.findById(id);
        if (cachedRenter != null) {
            return cachedRenter;
        } else {
            Renter renter = mongoRenterRepo.read(id);
            if (renter != null) {
                redisRenterRepo.add(renter);
            }
            return renter;
        }
    }

    public ArrayList<Renter> findAll() {
        ArrayList<Renter> renters = mongoRenterRepo.readAll();
        renters.forEach(renter -> {
            Renter cachedRenter = redisRenterRepo.findById(renter.getPersonalID());
            if (cachedRenter == null) {
                redisRenterRepo.add(renter);
            }
        });
        return renters;
    }

    public void add(Renter renter) {
        mongoRenterRepo.create(renter);
        redisRenterRepo.add(renter);
    }

    public void update(Renter renter) {
        mongoRenterRepo.update(renter);
        redisRenterRepo.update(renter);
    }

    public void delete(Renter renter) {
        mongoRenterRepo.delete(renter);
        redisRenterRepo.delete(renter);
    }
}
