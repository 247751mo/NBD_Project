package repositories;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import model.Renter;

import java.util.ArrayList;


public class RedisRenterRepo extends org.example.repositories.AbstractRedisRepository {

    private static final String HASH_PREFIX = "renter:";
    private final Jsonb jsonb = JsonbBuilder.create();

    public Renter findById(String id) {
        String jsonRenter = getPool().jsonGet(HASH_PREFIX + id);
        if (jsonRenter != null) {
            return jsonb.fromJson(jsonRenter, Renter.class);
        }
        return null;
    }

    public ArrayList<Renter> findAll() {
        ArrayList<Renter> renters = new ArrayList<>();
        getPool().keys(HASH_PREFIX + "*").forEach(key -> {
            String jsonRenter = getPool().jsonGet(key);
            if (jsonRenter != null) {
                renters.add(jsonb.fromJson(jsonRenter, Renter.class));
            }
        });
        return renters;
    }

    public void add(Renter renter) {
        String jsonRenter = jsonb.toJson(renter);
        getPool().jsonSet(HASH_PREFIX + renter.getPersonalID(), jsonRenter);
        getPool().expire(HASH_PREFIX + renter.getPersonalID(), 3600); // TTL 1 godzina
    }

    public void update(Renter renter) {
        String jsonRenter = jsonb.toJson(renter);
        getPool().jsonSet(HASH_PREFIX + renter.getPersonalID(), jsonRenter);
    }

    public void delete(Renter renter) {
        getPool().del(HASH_PREFIX + renter.getPersonalID());
    }
}
