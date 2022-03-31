package send.nutez.model;

public class Person {
    public enum FITNESS_LEVEL {
        LOW(1.0), MEDIUM(1.5), HIGH(2.0);

        private double level;
        FITNESS_LEVEL(double level) {
            this.level = level;
        }
        public double getLevel() {
            return level;
        }
    }

    private int age = 24*12; // month
    private FITNESS_LEVEL fitness_level = FITNESS_LEVEL.MEDIUM;
    private int weight = 70; //kg
    private boolean female = true;

    public Person() {

    }

    public Person(int age, FITNESS_LEVEL fitness_level, int weight, boolean female) {
        this.age = age;
        this.fitness_level = fitness_level;
        this.weight = weight;
        this.female = female;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public FITNESS_LEVEL getFitness_level() {
        return fitness_level;
    }

    public void setFitness_level(FITNESS_LEVEL fitness_level) {
        this.fitness_level = fitness_level;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    public String getGender() {
        if(isFemale())
            return "Female";
        else
            return "Male";
    }
}
