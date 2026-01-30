import java.awt.*;
import java.util.*;
import java.util.List;

public class Animals { // error with the deadAnimals() method and the improvedLifeCycle() method

    // Initialisation

    ArrayList<Point> animalArray = new ArrayList<>();
    ArrayList<Direction> animalDirection = new ArrayList<>();
    ArrayList<Boolean> setBreeding = new ArrayList<>();
    ArrayList<Integer> lifeSpan = new ArrayList<>();

    int year = 4; // this is to offset the life span decrease caused by the timer so animals can last in seconds

    void spawning(int initialSpawnCap) { // complete
        int x; // the x co-ordinate of the point
        int y; // the y co-ordinate of the point
        Random randomX = new Random();
        Random randomY = new Random();

        boolean setSpawning = true;
        Integer life = 80 * year; // constant for the life span of spawned in animals

        do {
            x = randomX.nextInt(0, 600);
            y = randomY.nextInt(0, 600);

            animalArray.add(new Point(x, y));
            setBreeding.add(Boolean.TRUE);
            lifeSpan.add(life); // the idea is they will have a lifespan of 100 units and 80 for spawned in units
            defineInitialMovement();

            if (animalArray.size() == initialSpawnCap) {

                setSpawning = false;
            }
        } while (setSpawning);
    }

    // Movement Logic

    Direction direction;
    int d; // the random direction assigned to a new animal from birth
    Random randomD = new Random();

    void defineInitialMovement() { // complete
        d = randomD.nextInt(0, 7);
        switch (d) {
            case 0 -> {
                direction = Direction.North;
                animalDirection.add(direction);
            }
            case 1 -> {
                direction = Direction.East;
                animalDirection.add(direction);
            }
            case 2 -> {
                direction = Direction.West;
                animalDirection.add(direction);
            }
            case 3 -> {
                direction = Direction.South;
                animalDirection.add(direction);
            }
            case 4 -> {
                direction = Direction.NorthEast;
                animalDirection.add(direction);
            }
            case 5 -> {
                direction = Direction.NorthWest;
                animalDirection.add(direction);
            }
            case 6 -> {
                direction = Direction.SouthEast;
                animalDirection.add(direction);
            }
            case 7 -> {
                direction = Direction.SouthWest;
                animalDirection.add(direction);
            }
        }
    }

    void directionCase(Point animal) { // complete
        switch (direction) {
            case North -> animal.y--;
            case South -> animal.y++;
            case East -> animal.x++;
            case West -> animal.x--;
            case NorthEast -> {
                animal.x++; animal.y--;
            }
            case NorthWest -> {
                animal.x--; animal.y--;
            }
            case SouthEast -> {
                animal.x++; animal.y++;
            }
            case SouthWest -> {
                animal.x--; animal.y++;
            }
        }
    }

    void move(Point animal) { // complete
        direction = animalDirection.get(animalArray.indexOf(animal));
        directionCase(animal);
    }

    // Breeding Logic

    List<Point> babies = new ArrayList<>();
    int life = 100; // life span for bred animals

    void createBaby(int xCoordinate, int yCoordinate) { // complete
            babies.add(new Point(xCoordinate, yCoordinate));
            setBreeding.add(Boolean.FALSE);
            lifeSpan.add(life * year);
            defineInitialMovement();
    }

    void breed() { // complete
        for (Point animal: animalArray) {
            if (setBreeding.get(animalArray.indexOf(animal))) {
                for (Point neighbour: animalArray) {
                    if (animal.x >= neighbour.x && animal.x <= neighbour.x + 5 && animal.y >= neighbour.y && animal.y <= neighbour.y + 5 && setBreeding.get(animalArray.indexOf(neighbour)) && animalArray.indexOf(animal) != animalArray.indexOf(neighbour)) {
                        setBreeding.set(animalArray.indexOf(animal), Boolean.FALSE);
                        setBreeding.set(animalArray.indexOf(neighbour), Boolean.FALSE);
                        createBaby(animal.x, neighbour.y);
                    }
                }
            }
        }
        if (!babies.isEmpty()) {
            animalArray.addAll(babies);
            babies.clear();
        }
    }

    // Life Cycle Logic

    List<Integer> deadAnimalIndex = new ArrayList<>(); // this to delete animals
    HashMap<Integer, Integer> breedQueue = new HashMap<>();
    Integer breedCooldown = 50;

    void deadAnimals(int deadAnimal) { // complete
        animalArray.remove(deadAnimal);
        setBreeding.remove(deadAnimal);
        lifeSpan.remove(deadAnimal);
        animalDirection.remove(deadAnimal);
        breedQueue.remove(deadAnimal);
    }

    void setBreedCooldown() { // complete
        if (setBreeding.contains(Boolean.FALSE)) {
            for (Point animal: animalArray) {
                if (!setBreeding.get(animalArray.indexOf(animal)) && !breedQueue.containsKey(animalArray.indexOf(animal))) {
                    breedQueue.put(animalArray.indexOf(animal), breedCooldown);
                }
            }
        }
    }

    void decrementLife() { // concurrent modification exception
        for (Point animal: animalArray) {
            Integer life = lifeSpan.get(animalArray.indexOf(animal)) - 1;
            lifeSpan.set(animalArray.indexOf(animal), life);

            if (breedQueue.containsKey(animalArray.indexOf(animal))) {
                Integer cooldown = breedQueue.get(animalArray.indexOf(animal));
                if (cooldown <= 0) {
                    setBreeding.set(animalArray.indexOf(animal), Boolean.TRUE);
                    breedQueue.remove(animalArray.indexOf(animal));
                }
                else {
                    cooldown--;
                    breedQueue.put(animalArray.indexOf(animal), cooldown);
                }
            }
        }
    }

    void improvedLifeCycle() { // complete
        setBreedCooldown();
        decrementLife();
        for (Integer life: lifeSpan) {
            if (life == 0) {
                deadAnimalIndex.add(lifeSpan.indexOf(life));
            }
        }
        if (!deadAnimalIndex.isEmpty()) {
            Collections.sort(deadAnimalIndex);
            deadAnimalIndex.sort(Collections.reverseOrder());
            for (int deadAnimal: deadAnimalIndex) {
                deadAnimals(deadAnimal);
            }
            deadAnimalIndex.clear();
        }
    }

    @Deprecated
    void lifeCycle() { // complete, might want to make it more complex
        List<Integer> decrementFix = new ArrayList<>(); // this to fix the decrement of life issue

        for (Integer life : lifeSpan) {
            switch (life) {
                case 350, 300, 250, 200, 150, 100, 50 -> { // they will be able to breed again every 20 life span units
                    setBreeding.set(lifeSpan.indexOf(life), Boolean.TRUE);
                }
                case  0 -> { // death
                    deadAnimalIndex.add(lifeSpan.indexOf(life));
                }
            }
            life--;
            decrementFix.add(life);
        }
        lifeSpan.clear();
        lifeSpan.addAll(decrementFix);
        decrementFix.clear();

        if (!deadAnimalIndex.isEmpty()) {
            for (int deadAnimal: deadAnimalIndex) {
                deadAnimals(deadAnimal);
            }
        }
    }

    // Path Finding Logic

    List<Integer> distanceX = new ArrayList<>();
    List<Integer> distanceY = new ArrayList<>();
    Integer closestDistanceX;
    Integer closestDistanceY;
    int size = 5;
    int lowerBound = 1;
    int upperBound = 599;

    void findClosestNeighbour(Point animal) { // complete
        for (Point neighbour: animalArray) { // this checks for closest breedable neighbour
            if (animalArray.indexOf(animal) == animalArray.indexOf(neighbour)) continue;
            else if (neighbour.x - animal.x < 32 && neighbour.x - animal.x > -32 && neighbour.y - animal.y < 32 && neighbour.y - animal.y > -32 && setBreeding.get(animalArray.indexOf(animal)) && setBreeding.get(animalArray.indexOf(neighbour))){
                distanceX.add(neighbour.x - animal.x);
                distanceY.add(neighbour.y - animal.y);
            }
        }
        if (!distanceX.isEmpty() && !distanceY.isEmpty()) {
            Collections.sort(distanceX);
            Collections.sort(distanceY);
            closestDistanceX = distanceX.get(0);
            closestDistanceY = distanceY.get(0);
            moveToNeighbour(animal);
        }
        distanceX.clear();
        distanceY.clear();
    }

    void moveToNeighbour(Point animal) { // complete

        if (closestDistanceX > 0 && closestDistanceY == 0) {
            direction = Direction.East;
            animalDirection.set(animalArray.indexOf(animal), direction);
        }
        else if (closestDistanceX < 0 && closestDistanceY == 0) {
            direction = Direction.West;
            animalDirection.set(animalArray.indexOf(animal), direction);
        }
        else if (closestDistanceX == 0 && closestDistanceY > 0) {
            direction = Direction.South;
            animalDirection.set(animalArray.indexOf(animal), direction);
        }
        else if (closestDistanceX == 0 && closestDistanceY < 0) {
            direction = Direction.North;
            animalDirection.set(animalArray.indexOf(animal), direction);
        }
        else if (closestDistanceX > 0 && closestDistanceY < 0) {
            direction = Direction.NorthEast;
            animalDirection.set(animalArray.indexOf(animal), direction);
        }
        else if (closestDistanceX > 0 && closestDistanceY > 0) {
            direction = Direction.SouthEast;
            animalDirection.set(animalArray.indexOf(animal), direction);
        }
        else if (closestDistanceX < 0 && closestDistanceY < 0) {
            direction = Direction.NorthWest;
            animalDirection.set(animalArray.indexOf(animal), direction);
        }
        else if (closestDistanceX < 0 && closestDistanceY > 0) {
            direction = Direction.SouthWest;
            animalDirection.set(animalArray.indexOf(animal), direction);
        }
    }
    void stayOnScreen() { // complete
        // using 1 for the origin and 559 because the animals have a size of 5 pixels & I don't want them too close to the border of the frame
        d = randomD.nextInt(0, 2);
        for (Point animal: animalArray) {
            if (animal.x == lowerBound) {
                stayOnScreenCleanUp(Direction.East, Direction.SouthEast, Direction.NorthEast);
                animalDirection.set(animalArray.indexOf(animal), direction);
            }
            else if (animal.x + size == upperBound) {
                stayOnScreenCleanUp(Direction.West, Direction.NorthWest, Direction.SouthWest);
                animalDirection.set(animalArray.indexOf(animal), direction);
            }
            else if (animal.y == lowerBound) {
                stayOnScreenCleanUp(Direction.South, Direction.SouthEast, Direction.SouthWest);
                animalDirection.set(animalArray.indexOf(animal), direction);
            }
            else if (animal.y + size == upperBound) {
                stayOnScreenCleanUp(Direction.North, Direction.NorthEast, Direction.NorthWest);
                animalDirection.set(animalArray.indexOf(animal), direction);
            }
        }
    }
    void stayOnScreenCleanUp(Direction direction1, Direction direction2, Direction direction3) { // complete
        switch (d) {
            case 0 -> direction = direction1;
            case 1 -> direction = direction2;
            case 2 -> direction = direction3;
        }
    }
}
