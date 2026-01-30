import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Prey extends Animals{ // error with the deadAnimals() method
    ArrayList<Boolean> shouldFlee = new ArrayList<>();

    // initialization

    @Override
    void spawning(int initialSpawnCap) { // complete
        for (int i = 0; i < initialSpawnCap; i++) {
            shouldFlee.add(Boolean.FALSE);
        }
        super.spawning(initialSpawnCap);
    }

    public Prey() { // complete
        this.spawning(50);
    }

    void Methods(ArrayList<Point> predatorPopulation) {
        improvedLifeCycle();
        if (!predatorPopulation.isEmpty()) {
            findClosestPredator(predatorPopulation);
            fleeOrBreed();
        }
        move();
        stayOnScreen();
        if (animalArray.size() > 1) breed();
    }

    // movement logic

    void move() { // complete
        for (Point animal: animalArray) super.move(animal);
    }

    // breeding logic

    @Override
    void createBaby(int xCoordinate, int yCoordinate) { // complete
        super.createBaby(xCoordinate, yCoordinate);
        shouldFlee.add(Boolean.FALSE);
    }

    // path finding logic
    int size = 5;
    int lowerBound = 1;
    int upperBound = 599;

    void moveAwayFromNeighbour(Point prey) { // complete
        if (closestDistanceX > 0 && closestDistanceY == 0) {
            direction = Direction.West;
            animalDirection.set(animalArray.indexOf(prey), direction);
        }
        else if (closestDistanceX < 0 && closestDistanceY == 0) {
            direction = Direction.East;
            animalDirection.set(animalArray.indexOf(prey), direction);
        }
        else if (closestDistanceX == 0 && closestDistanceY > 0) {
            direction = Direction.North;
            animalDirection.set(animalArray.indexOf(prey), direction);
        }
        else if (closestDistanceX == 0 && closestDistanceY < 0) {
            direction = Direction.South;
            animalDirection.set(animalArray.indexOf(prey), direction);
        }
        else if (closestDistanceX > 0 && closestDistanceY < 0) {
            direction = Direction.SouthWest;
            animalDirection.set(animalArray.indexOf(prey), direction);
        }
        else if (closestDistanceX > 0 && closestDistanceY > 0) {
            direction = Direction.NorthWest;
            animalDirection.set(animalArray.indexOf(prey), direction);
        }
        else if (closestDistanceX < 0 && closestDistanceY < 0) {
            direction = Direction.SouthEast;
            animalDirection.set(animalArray.indexOf(prey), direction);
        }
        else if (closestDistanceX < 0 && closestDistanceY > 0) {
            direction = Direction.NorthEast;
            animalDirection.set(animalArray.indexOf(prey), direction);
        }
    }

    void findClosestPredator(ArrayList<Point> predatorPopulation) { // complete
        for (Point prey: animalArray) {
            for (Point predator: predatorPopulation) {
                if (predator.x - prey.x < 24 && predator.x - prey.x > -24 && predator.y - prey.y < 24 && predator.y - prey.y > -24) {
                    distanceX.add(prey.x - predator.x);
                    distanceY.add(prey.y - predator.y);
                }
            }
            if (!distanceX.isEmpty() && !distanceY.isEmpty()) { // positive neighbour prey was found
                Collections.sort(distanceX);
                Collections.sort(distanceY);
                closestDistanceX = distanceX.get(0);
                closestDistanceY = distanceY.get(0);
                shouldFlee.set(animalArray.indexOf(prey), Boolean.TRUE);
            }
            else shouldFlee.set(animalArray.indexOf(prey), Boolean.FALSE); // negative prey was not found
            distanceX.clear();
            distanceY.clear();
        }
    }

    void fleeOrBreed() { // complete
        for (Point prey: animalArray) {
            if (shouldFlee.get(animalArray.indexOf(prey)) && prey.x > lowerBound && prey.x + size < upperBound && prey.y > lowerBound && prey.y + size < upperBound) moveAwayFromNeighbour(prey);
            else if (!shouldFlee.get(animalArray.indexOf(prey))) findClosestNeighbour(prey);
        }
    }

    // life cycle logic

    @Override
    void deadAnimals(int deadAnimal) { // tested out-of-bounds error when deleting animals
        shouldFlee.remove(deadAnimal);
        super.deadAnimals(deadAnimal);
    }
}
