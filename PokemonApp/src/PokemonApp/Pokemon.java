package PokemonApp;

public class Pokemon {
    private String name;
    private String type;
    private int hp;
    private int attackPower;
    private String move;
    private String moveType;
    private int maxHp;
    private int grade;

    public Pokemon(String name, String type, int hp, int attackPower, String move, String moveType, int maxHp, int grade) {
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.attackPower = attackPower;
        this.move = move;
        this.moveType = moveType;
        this.maxHp = maxHp;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttackPower() {
        return attackPower;
    }
    
    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
    
    public String getMoveType() {
    	return moveType;
    }
    
    public void setMoveType(String moveType) {
    	this.moveType = moveType;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getGrade() {
        return grade;
    }

    public boolean isDefeated() {
        return hp <= 0;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", hp=" + hp +
                ", attackPower=" + attackPower +
                ", move='" + move + '\'' +
                ", moveType='" + moveType + '\'' +
                ", maxHp=" + maxHp +
                ", grade=" + grade +
                '}';
    }
}