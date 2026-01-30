import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Predator extends Animals{ // need to make a method where is a predator is hungry for too long they will die and there is an issue when deleting dead animals
    ArrayList<Boolean> isHungry = new ArrayList<>();
    ArrayList<Boolean> inRange = new ArrayList<>();

    boolean range; // this will be used to make the predators faster than the prey so they can catch and eat them
    boolean hungry = true; // this will be used to make the predators eat prey only when they are hungry

    // initialization

    @Override
    void spawning(int initialSpawnCap) { // complete
        for (int i = 0; i < initialSpawnCap; i++) {
            isHungry.add(hungry);
            inRange.add(range);
        }
        super.spawning(initialSpawnCap);
    }

    public Predator() { // complete
        this.spawning(20);
    }

    void Methods(ArrayList<Point> preyPopulation, List<Integer> preyDeadAnimalIndex) {
        improvedLifeCycle();
        stayOnScreen();
        if (!preyPopulation.isEmpty()) {
            sprintOrWalk();
            eatOrBreed(preyPopulation);
            eat(preyPopulation, preyDeadAnimalIndex);
        }
        if (animalArray.size() > 1) breed();
    }

    // movement logic

    void sprinting(Point predator) { // complete
        switch (direction) {
            case North -> predator.y-=2;
            case South -> predator.y+=2;
            case East -> predator.x+=2;
            case West -> predator.x-=2;
            case NorthEast -> {
                predator.x+=2; predator.y-=2;
            }
            case NorthWest -> {
                predator.x-=2; predator.y-=2;
            }
            case SouthEast -> {
                predator.x+=2; predator.y+=2;
            }
            case SouthWest -> {
                predator.x-=2; predator.y+=2;
            }
        }
    }

    void sprintOrWalk() { // complete
        for (Point predator: animalArray) {
            if (inRange.get(animalArray.indexOf(predator)) && isHungry.get(animalArray.indexOf(predator))) {
                direction = animalDirection.get(animalArray.indexOf(predator));
                sprinting(predator);
            }
            else move(predator);
        }
    }

    // Path finding logic

    void findClosestPrey(ArrayList<Point> preyPopulation, Point predator) { // complete
            for (Point prey: preyPopulation) {
                if (prey.x - predator.x < 24 && prey.x - predator.x > -24 && prey.y - predator.y < 24 && prey.y - predator.y > -24) {
                    distanceX.add(prey.x - predator.x);
                    distanceY.add(prey.y - predator.y);
                }
            }
            if (!distanceX.isEmpty() && !distanceY.isEmpty()) { // positive neighbour prey was found
                Collections.sort(distanceX);
                Collections.sort(distanceY);
                closestDistanceX = distanceX.get(0);
                closestDistanceY = distanceY.get(0);
                moveToNeighbour(predator);
                inRange.set(animalArray.indexOf(predator), Boolean.TRUE);
            }
            else inRange.set(animalArray.indexOf(predator), Boolean.FALSE); // negative prey was not found
            distanceX.clear();
            distanceY.clear();
    }

    void eatOrBreed(ArrayList<Point> preyPopulation) { // complete
        for (Point predator: animalArray) {
            if (!isHungry.get(animalArray.indexOf(predator))) findClosestNeighbour(predator);
            else if (isHungry.get(animalArray.indexOf(predator))) findClosestPrey(preyPopulation, predator);
        }
    }

    // eating logic

    void eat(ArrayList<Point> preyPopulation, List<Integer> preyDeadAnimalIndex) { // complete
        for (Point predator: animalArray) {
            if (isHungry.get(animalArray.indexOf(predator)) && inRange.get(animalArray.indexOf(predator))) {
                for (Point prey: preyPopulation) {
                    if (predator.x <= prey.x + 5 && predator.x >= prey.x && predator.y <= prey.y + 5 && predator.y >= prey.y) {
                        isHungry.set(animalArray.indexOf(predator), Boolean.FALSE);
                        preyDeadAnimalIndex.add(preyPopulation.indexOf(prey));
                    }
                }
            }
        }
    }

    // breeding logic

    @Override
    void createBaby(int xCoordinate, int yCoordinate) { // complete
        super.createBaby(xCoordinate, yCoordinate);
        inRange.add(Boolean.FALSE);
        isHungry.add(Boolean.FALSE);
    }

    // life cycle logic
    HashMap<Integer, Integer> hungryQueue = new HashMap<>();
    HashMap<Integer, Integer> starveQueue = new HashMap<>();
    Integer hungryCooldown = 110;

    @Override
    void deadAnimals(int deadAnimal) { // complete
        isHungry.remove(deadAnimal);
        inRange.remove(deadAnimal);
        super.deadAnimals(deadAnimal);
        hungryQueue.remove(deadAnimal);
        starveQueue.remove(deadAnimal);
    }

    void setHungryCooldown() { // complete
        if (isHungry.contains(Boolean.FALSE)) {
            for (Point predator: animalArray) {
                if (!isHungry.get(animalArray.indexOf(predator)) && !hungryQueue.containsKey(animalArray.indexOf(predator))) {
                    hungryQueue.put(animalArray.indexOf(predator), hungryCooldown);
                }
            }
        }
    }

    void setStarvation() { // complete
        Integer starveTime = 60;
        if (isHungry.contains(Boolean.TRUE)) {
            for (Point predator: animalArray) {
                if (isHungry.get(animalArray.indexOf(predator)) && !starveQueue.containsKey(animalArray.indexOf(predator))) {
                    starveQueue.put(animalArray.indexOf(predator), starveTime);
                }
            }
        }
    }

    void decrementHunger() { // complete
        for (Point predator: animalArray) {
            if (hungryQueue.containsKey(animalArray.indexOf(predator))) {
                Integer cooldown = hungryQueue.get(animalArray.indexOf(predator));
                if (cooldown <= 0) {
                    isHungry.set(animalArray.indexOf(predator), Boolean.TRUE);
                    hungryQueue.remove(animalArray.indexOf(predator));
                }
                else {
                    cooldown--;
                    hungryQueue.replace(animalArray.indexOf(predator), cooldown);
                }
            }
        }
    }

    void decrementStarvation() { // complete
        for (Point predator: animalArray) {
            if (starveQueue.containsKey(animalArray.indexOf(predator))) {
                Integer cooldown = starveQueue.get(animalArray.indexOf(predator));
                if (cooldown <= 0) {
                    starveQueue.remove(animalArray.indexOf(predator));
                    deadAnimalIndex.add(animalArray.indexOf(predator));
                }
                else if (!isHungry.get(animalArray.indexOf(predator)) && starveQueue.containsKey(animalArray.indexOf(predator))) {
                    starveQueue.remove(animalArray.indexOf(predator));
                    hungryQueue.put(animalArray.indexOf(predator), hungryCooldown);
                }
                else {
                    cooldown--;
                    starveQueue.replace(animalArray.indexOf(predator), cooldown);
                }
            }
        }
    }

    @Override
    void decrementLife() { // error due to the super method
        decrementHunger();
        decrementStarvation();
        super.decrementLife();
    }

    @Override
    void improvedLifeCycle() { // tested error due to deadAnimals() method
        setHungryCooldown();
        setStarvation();
        super.improvedLifeCycle();
    }
}
