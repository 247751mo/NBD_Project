package repositories;

import model.Rent;
import java.util.ArrayList;
import java.util.List;

public class RentRepo {

    // Lista przechowująca wypożyczenia
    private final List<Rent> rents;

    // Konstruktor inicjujący pustą listę wypożyczeń
    public RentRepo() {
        this.rents = new ArrayList<>();
    }

    // Dodanie wypożyczenia do listy
    public void addRent(Rent rent, List<Rent> rents) {
        if (rent != null) {
            this.rents.add(rent);
        }
    }

    // Usunięcie wypożyczenia z listy, jeśli istnieje
    public void removeRent(Rent rent) {
        rents.removeIf(existingRent -> existingRent.equals(rent));
    }

    // Pobranie wszystkich wypożyczeń
    public List<Rent> getAllRents() {
        return new ArrayList<>(rents); // Zwracamy nową listę, aby zapobiec modyfikacji oryginału
    }
}
