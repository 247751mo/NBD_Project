package repositories;

import model.Renter;
import java.util.ArrayList;

public class RenterRepo {

    private final RedisRenterRepo redisRepo;
    private final MongoRenterRepo mongoRepo;

    public RenterRepo(RedisRenterRepo redisRepo, MongoRenterRepo mongoRepo) {
        this.redisRepo = redisRepo;
        this.mongoRepo = mongoRepo;
    }

    public Renter read(String id) {
        Renter renter = redisRepo.read(id);
        if (renter != null) {
            return renter;
        }
        renter = mongoRepo.read(id);
        if (renter != null) {
            redisRepo.create(renter);
        }
        return renter;
    }

    public ArrayList<Renter> readAll() {
        ArrayList<Renter> renters = mongoRepo.readAll();
        renters.forEach(renter -> {
            Renter cachedRenter = redisRepo.read(renter.getPersonalID());
            if (cachedRenter == null) {
                redisRepo.create(renter);
            }
        });
        return renters;
    }

    public void create(Renter renter) {
        mongoRepo.create(renter);
        redisRepo.create(renter);
    }

    public void delete(Renter renter) {
        mongoRepo.delete(renter);
        redisRepo.delete(renter);
    }

    public void update(Renter renter) {
        mongoRepo.update(renter);
        redisRepo.update(renter);
    }
}
