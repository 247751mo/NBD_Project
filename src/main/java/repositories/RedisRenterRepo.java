package repositories;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import model.Renter;
import java.util.ArrayList;

public class RedisRenterRepo extends AbstractRedisRepository {

    private static final String HASH_PREFIX = "renter:";
    private final Jsonb jsonb = JsonbBuilder.create();

    public Renter read(Object id) {
        String jsonRenter = jsonb.toJson(getPool().jsonGet(HASH_PREFIX + id));
        return jsonb.fromJson(jsonRenter, Renter.class);
    }

    public ArrayList<Renter> readAll() {
        ArrayList<Renter> renters = new ArrayList<>();
        getPool().keys(HASH_PREFIX + "*").forEach(key -> {
            String jsonRenter = jsonb.toJson(getPool().jsonGet(key));
            if (jsonRenter != null) {
                renters.add(jsonb.fromJson(jsonRenter, Renter.class));
            }
        });
        return renters;
    }

    public void create(Renter renter) {
        String jsonRenter = jsonb.toJson(renter);
        try {
            getPool().jsonSet(HASH_PREFIX + renter.getPersonalID(), jsonRenter);
            getPool().expire(HASH_PREFIX + renter.getPersonalID(), 600);
        } catch (Exception e) {
            System.out.println("Error creating renter: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(Renter renter) {
        try {
            getPool().del(HASH_PREFIX + renter.getPersonalID());
        } catch (Exception e) {
            System.out.println("Error deleting renter: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(Renter renter) {
        String jsonRenter = jsonb.toJson(renter);
        try {
            getPool().jsonSet(HASH_PREFIX + renter.getPersonalID(), jsonRenter);
        } catch (Exception e) {
            System.out.println("Error updating renter: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
