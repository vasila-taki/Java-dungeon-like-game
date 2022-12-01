import java.util.*;
import java.lang.*;
import java.awt.*; 
import java.awt.image.BufferedImage;
import javax.swing.*;


enum ItemType{
    USABLE, EQUIPPABLE
}


enum Equippables{
    SWORD, AXE, STAFF, WAND, SHIELD, RING, NONE
}

enum Usables{
    HEALTH_POTION, MANA_POTION, RESTORATION_POTION
}

enum SlotType {
    MAIN_HAND, OFF_HAND, FINGER
}

enum EffectType {
    INT_BONUS, STR_BONUS, MP_BONUS,
    HP_BONUS, HP_REPLENISH, MP_REPLENISH, DEFENSE
}

enum PossibleActions {
    W, A, S, D, H, M, B, R, C, V
}

enum TileState{
    VISIBLE, UNKNOWN, FOGGED
}

enum TileType{
    FLOOR, WALL, START, EXIT, FINAL_EXIT, TRAP
}

interface Item {
    ItemType getItemType();
    ArrayList<ItemEffect> getEffects();
    void setItemsTile(MapTile tile);
    MapTile getItemsTile();
    String getName();
    String getDescription();
    int getPositionX();
    int getPositionY();
}


interface Equippable extends Item {
    SlotType getSlotType();
    Equippables getEquippableType();
}

interface Usable extends Item {
    Usables getUsableType();
    void addUses();
    int getUses();
    void use();
}

class Dice {
    int diceNum;
    int pleuresNum;
    Random rand;

    public Dice(int n, int s){
        this.rand = new Random();
        this.pleuresNum = s;
        this.diceNum = n;
    }

    public int roll(){
        int ret = 0;

        for(int i = 0; i < diceNum; i++){
            ret = ret + rand.nextInt(pleuresNum) + 1;
        }
        return ret;
    }
}

class MapTile{

    TileType tt;
    Position pos;
    TileState ts;
    boolean isOccupied, hasItem, isTrap;
    Item tilesItem;
    Enemy tilesEnemy;

    public MapTile(TileType type, Position p, TileState state){
        this.tt = type;
        this.pos = p;
        this.ts = state;
        this.isOccupied = false;
        this.hasItem = false;
        this.isTrap = false;
    }

    public Position getPosition() { return pos; }

    public void setTileType(TileType type){
        this.tt = type;
    }

    public TileType getTileType(){
        return this.tt;
    }

    public void setTileState(TileState state){
        this.ts = state;
    }

    public TileState getTileState(){
        return this.ts;
    }

    public boolean isTrap(){return this.isTrap;}

    public void setIsTrap(){this.isTrap = true;}

    public void unsetIsTrap(){this.isTrap = false;}

    public boolean isOccupied(){return this.isOccupied;}

    public void setOccupied(){this.isOccupied = true;}

    public void unsetOccupied(){this.isOccupied = false;}

    public boolean hasItem(){return this.hasItem;}

    public void setHasItem(){this.hasItem = true;}

    public void unsetHasItem(){this.hasItem = false;}

    public void setTilesItem(Item item){ this.tilesItem = item; }

    public Item getTilesItem(){return this.tilesItem;}

    public void setTilesEnemy(Enemy enemy){ this.tilesEnemy = enemy; }

    public Enemy getTilesEnemy(){return this.tilesEnemy;}
}

class ItemEffect {
    EffectType et;
    int amount;

    public ItemEffect(EffectType type, int amnt){
        this.et = type;
        this.amount = amnt;
    }

    public int getAmount() {return this.amount;}
    
    public EffectType getEffectType() {return this.et;}
}

class HealthPotion implements Usable{
    String name, description;
    ItemType it;
    Usables ut;
    int uses;
    ArrayList<ItemEffect> ie;
    MapTile itemsTile;
    Random rand;

    public HealthPotion(){
        this.rand = new Random();
        this.name = "Health Potion";
        this.description = "Very healthy";
        this.it = ItemType.USABLE;
        this.ut = Usables.HEALTH_POTION;
        this.uses = 0;

        this.ie = new ArrayList<ItemEffect>();
        this.ie.add(new ItemEffect(EffectType.HP_REPLENISH, 25));
        this.ie.add(new ItemEffect(EffectType.HP_REPLENISH, -5));
    }

    public String getName(){ return this.name;}

    public String getDescription(){ return this.description;}

    public ItemType getItemType(){ return this.it; }

    public ArrayList<ItemEffect> getEffects(){
        double rng = this.rand.nextDouble();
        ArrayList<ItemEffect> temp = new ArrayList<ItemEffect>();

        if(rng <= 0.05){
            temp.add(this.ie.get(1));
        }else{
            temp.add(this.ie.get(0));
        }
        return temp;
    }

    public Usables getUsableType(){ return this.ut;}

    public void setItemsTile(MapTile tile){this.itemsTile = tile;}

    public MapTile getItemsTile(){return this.itemsTile;}

    public int getPositionX(){return this.itemsTile.pos.getX(); }

    public int getPositionY(){return this.itemsTile.pos.getY();}

    public int getUses(){return this.uses;}

    public void addUses(){this.uses = this.uses + 2;}

    public void use(){this.uses = this.uses - 1;}
}

class ManaPotion implements Usable{

    String name, description;
    Usables ut;
    ItemType it;
    int uses;
    ArrayList<ItemEffect> ie;
    MapTile itemsTile;

    public  ManaPotion(){
        this.name = "Mana Potion";
        this.description = "If u no wizard :(";
        this.it = ItemType.USABLE;
        this.ut = Usables.MANA_POTION;
        this.uses = 0;

        this.ie = new ArrayList<ItemEffect>();
        this.ie.add(new ItemEffect(EffectType.MP_REPLENISH, 20));
    }

    public String getName(){return this.name;}

    public String getDescription() { return this.description; }

    public ItemType getItemType(){return this.it;}

    public ArrayList<ItemEffect> getEffects(){return this.ie;}

    public Usables getUsableType(){ return this.ut;}

    public void setItemsTile(MapTile tile){this.itemsTile = tile;}

    public MapTile getItemsTile(){return this.itemsTile;}

    public int getPositionX(){return this.itemsTile.pos.getX();}

    public int getPositionY(){return this.itemsTile.pos.getY();}

    public int getUses(){return this.uses;}

    public void addUses(){this.uses = this.uses + 2;}

    public void use(){this.uses = this.uses - 1;}
}

class RestorationPotion implements Usable{
    String name, description;
    ItemType it;
    Usables ut;
    ArrayList<ItemEffect> ie;
    MapTile itemsTile;
    int uses;

    public RestorationPotion(){
        this.name = "Restoration Potion";
        this.description = "Delicious";
        this.it = ItemType.USABLE;
        this.ut = Usables.RESTORATION_POTION;

        this.ie = new ArrayList<ItemEffect>();
        this.ie.add(new ItemEffect(EffectType.HP_REPLENISH, 25));
        this.ie.add(new ItemEffect(EffectType.MP_REPLENISH, 20));

        this.uses = 0;
    }

    public String getName(){return this.name;}

    public String getDescription(){ return this.description;}

    public ItemType getItemType(){ return this.it; }

    public ArrayList<ItemEffect> getEffects(){ return this.ie; }

    public Usables getUsableType(){return this.ut;}

    public void setItemsTile(MapTile tile){this.itemsTile = tile;}

    public MapTile getItemsTile(){return this.itemsTile;}

    public int getPositionX(){return this.itemsTile.pos.getX(); }

    public int getPositionY(){return this.itemsTile.pos.getY();}

    public int getUses(){return this.uses;}

    public void addUses(){this.uses = this.uses + 2;}

    public void use(){this.uses = this.uses - 1;}
}

abstract class Individual{
    String name;
    int HP;
    MapTile itsTile;
    boolean isDead;
    
    public Individual(String name, int hitPoints){
        this.name = name;
        this.HP = hitPoints;
        this.isDead = false;
    }

    public abstract void hits(Individual other);

    public String getName(){return this.name; }

    public int getHP(){ return this.HP; }

    public abstract int calculateDmgTaken(Weapon weapon, int strORintl);

    public abstract void getDamage(int damage);

    public void setItsTile(MapTile tile){this.itsTile = tile;}

    public MapTile getItsTile(){return this.itsTile;}

    public int getPositionX(){return this.itsTile.pos.getX(); }

    public int getPositionY(){return this.itsTile.pos.getY();}

    public void setIsDead(){this.isDead = true;}

    public boolean isDead(){return this.isDead;}

    public abstract Weapon getItsWeapon();
}

abstract class Player extends Individual{

    int MP, STR, INT, EXP;

    int visibilityRadius;

    ArrayList<Usable> inventory;

    ArrayList<Equippable> equipment;
    Map<SlotType, Equippables> isEquipped;

    ArrayList<MapTile> visibleTiles;

    public Player(String name){
        super(name, 0);
        this.EXP = 0;
        this.HP = getBaseHP(this.getLevel());
        this.MP = getBaseMP(this.getLevel());
        this.STR = getBaseSTR(this.getLevel());
        this.INT = getBaseINT(this.getLevel());

        this.inventory = new ArrayList<Usable>();
        this.inventory.add(new HealthPotion());
        this.inventory.add(new ManaPotion());
        this.inventory.add(new RestorationPotion());

        this.visibleTiles = new ArrayList<MapTile>();

        this.equipment = new ArrayList<Equippable>();
        this.isEquipped = new HashMap<SlotType, Equippables>();
    }

    public String getName(){return this.name;}

    public int getVisibilityRadius(){return this.visibilityRadius;}

    public abstract int getBaseHP(int lvl);
    public abstract int getBaseMP(int lvl);
    public abstract int getBaseSTR(int lvl);
    public abstract int getBaseINT(int lvl);

    public int getLevel(){
        if(this.EXP <= 299){
            return 1;
        }
        else if(this.EXP <= 899){
            return 2;
        }
        else if(this.EXP <= 2699){
            return 3;
        }
        else if(this.EXP <= 6499){
            return 4;
        }
        else if(this.EXP <= 13999){
            return 5;
        }
        else{
            return 6;
        }
    }

    public void changeLevel(){
       int temp = this.getLevel();

        this.HP = this.HP + (this.getBaseHP(temp) - this.getBaseHP((temp - 1)));
        this.MP = this.MP + (this.getBaseMP(temp) - this.getBaseMP((temp - 1)));
        this.STR = this.STR + (this.getBaseSTR(temp) - this.getBaseSTR((temp - 1)));
        this.INT = this.INT + (this.getBaseINT(temp) - this.getBaseINT((temp - 1)));

        System.out.println("LEVEL: " + this.getLevel() + "!");
    }

    public void setEXP(int xp){
        int tempOld, tempNew;
        tempOld = this.getLevel();

        this.EXP = this.EXP + xp;

        tempNew = this.getLevel();

        if(tempOld != tempNew){
            this.changeLevel();
        }
    }

    public void setHPReplenish(int i){
        if(this.HP < this.getBaseHP(this.getLevel())){
            this.HP = this.HP + i;

            if(this.HP > getBaseHP(this.getLevel())){
                this.HP = getBaseHP(this.getLevel());
            }
        }
    }

    public void setMPReplenish(int i){
        if(this.MP < this.getBaseMP(this.getLevel())){
            this.MP = this.MP + i;

            if(this.MP > getBaseMP(this.getLevel())){
                this.MP = getBaseMP(this.getLevel());
            }
        }
    }

    public abstract void rest();

    public int getEquippableHPBonus(){
        int bonus = 0;

        for(int k = 0; k < this.equipment.size(); k++){
            ArrayList<ItemEffect> temp = this.equipment.get(k).getEffects();

            for(int i = 0; i < temp.size(); i++) {
                if(temp.get(i).getEffectType() == EffectType.HP_BONUS){
                    bonus = bonus + temp.get(i).getAmount();
                }
            }
        }
        return bonus;
    }

    public int getEquippableMPBonus(){
        int bonus = 0;

        if(this.getClass() == Wizard.class) {
            for (int k = 0; k < this.equipment.size(); k++) {
                ArrayList<ItemEffect> temp = this.equipment.get(k).getEffects();

                for (int i = 0; i < temp.size(); i++) {
                    if (temp.get(i).getEffectType() == EffectType.MP_BONUS) {
                        bonus = bonus + temp.get(i).getAmount();
                    }
                }
            }
        }
        return bonus;
    }

    public int getEquippableSTRBonus(){
        int bonus = 0;

        for(int k = 0; k < this.equipment.size(); k++){
            ArrayList<ItemEffect> temp = this.equipment.get(k).getEffects();

            for(int i = 0; i < temp.size(); i++) {
                if(temp.get(i).getEffectType() == EffectType.STR_BONUS){
                    bonus = bonus + temp.get(i).getAmount();
                }
            }
        }
        return bonus;
    }

    public int getEquippableINTBonus() {
        int bonus = 0;

        for (int k = 0; k < this.equipment.size(); k++) {
            ArrayList<ItemEffect> temp = this.equipment.get(k).getEffects();

            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getEffectType() == EffectType.INT_BONUS) {
                    bonus = bonus + temp.get(i).getAmount();
                }
            }
        }
        return bonus;
    }

    public int getEquippableDEFBonus() {
        int bonus = 0;

        for (int k = 0; k < this.equipment.size(); k++) {
            ArrayList<ItemEffect> temp = this.equipment.get(k).getEffects();

            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getEffectType() == EffectType.DEFENSE) {
                    bonus = bonus + temp.get(i).getAmount();
                }
            }
        }
        return bonus;
    }

    public Weapon getItsWeapon(){
        for(int i = 0; i < this.equipment.size(); i++){
            if(this.equipment.get(i).getSlotType() == SlotType.MAIN_HAND){
                return (Weapon) this.equipment.get(i);
            }
        }
        return null;
    }

    //kanei equip ton paikth me to item pou dexetai
    //an mporei na to kanei (px oxi wand se warrior)
    //an uparxei hdh item equipped ekei to epistrefei
    //alliws epistrefei null
    public Equippable equip(Equippable item) {
        Equippable oldEquippable = null;

        //se periptwsh pou den thelw kapoio slot (px to WAND) diathesimo
        //tou anathetw to Equippables.NONE ston antisoixo constructor tou player
        if(this.isEquipped.get(item.getSlotType()) != Equippables.NONE) {
            //se periptwsh pou to zhtoumeno slot exei hdh equippable
            if(this.isEquipped.get(item.getSlotType()) != null) {
                for (int k = 0; k < this.equipment.size(); k++) {
                    if (this.equipment.get(k).getSlotType() == item.getSlotType()) {
                        oldEquippable = this.equipment.get(k);
                        this.equipment.remove(k);
                        this.isEquipped.remove(item.getSlotType());
                        this.removeEquippableBonus(oldEquippable);

                        System.out.println("REMOVED: " + oldEquippable.getName());
                    }
                }
            }
            this.isEquipped.put(item.getSlotType(), item.getEquippableType());
            this.equipment.add(item);
            this.addEquippableBonus(item);

            System.out.println("EQUIPPED WITH: " + item.getName());

            return oldEquippable;
        }
        return item;
    }

    public void addEquippableBonus(Equippable item){
        ArrayList<ItemEffect> temp = item.getEffects();

        for(int i = 0; i < temp.size(); i++) {
            if(temp.get(i).getEffectType() == EffectType.HP_BONUS){
                this.HP = this.HP + temp.get(i).getAmount();
            }else if((temp.get(i).getEffectType() == EffectType.MP_BONUS) && (this.getClass() == Wizard.class)){
                this.MP = this.MP + temp.get(i).getAmount();
            }else if(temp.get(i).getEffectType() == EffectType.STR_BONUS){
                this.STR = this.STR + temp.get(i).getAmount();
            }else if(temp.get(i).getEffectType() == EffectType.INT_BONUS){
                this.INT = this.INT + temp.get(i).getAmount();
            }
        }
    }

    public void removeEquippableBonus(Equippable item){
        ArrayList<ItemEffect> temp = item.getEffects();

        for(int i = 0; i < temp.size(); i++) {
            if(temp.get(i).getEffectType() == EffectType.HP_BONUS){
                if(this.HP > this.getBaseHP(this.getLevel()) + this.getEquippableHPBonus()){
                    this.HP = this.getBaseHP(this.getLevel()) + this.getEquippableHPBonus();
                }
            }else if((temp.get(i).getEffectType() == EffectType.MP_BONUS) && (this.getClass() == Wizard.class)){
                if(this.MP > this.getBaseMP(this.getLevel()) + this.getEquippableMPBonus()){
                    this.MP = this.getBaseMP(this.getLevel()) + this.getEquippableMPBonus();
                }
            }else if(temp.get(i).getEffectType() == EffectType.STR_BONUS){
                if(this.STR > this.getBaseSTR(this.getLevel()) + this.getEquippableSTRBonus()){
                    this.STR = this.getBaseSTR(this.getLevel()) + this.getEquippableSTRBonus();
                }
            }else if(temp.get(i).getEffectType() == EffectType.INT_BONUS){
                if(this.INT > this.getBaseINT(this.getLevel()) + this.getEquippableINTBonus()){
                    this.INT = this.getBaseINT(this.getLevel()) + this.getEquippableINTBonus();
                }
            }
        }
    }

    public void addToInventory(Usable potion){
        for(int i = 0; i < this.inventory.size(); i++){
            if(potion.getUsableType() == this.inventory.get(i).getUsableType()){
                this.inventory.get(i).addUses();
            }
        }
    }

    public void removeUsedFromInventory(Usable potion){
        for(int i = 0; i < this.inventory.size(); i++){
            if(potion.getUsableType() == this.inventory.get(i).getUsableType()){
                this.inventory.get(i).use();
            }
        }
    }

    public void usePotion(Usable potion){

        ArrayList<ItemEffect> temp = potion.getEffects();
        int oldHP = this.getHP();

        for (int k = 0; k < temp.size(); k++) {
            if((temp.get(k).getEffectType() == EffectType.HP_REPLENISH) && (temp.get(k).getAmount() < 0)){
                this.getDamage(temp.get(k).getAmount());
                System.out.println("iouuu! spoiled Health Potion!");
                System.out.println("-" + (oldHP - this.getHP()) + " HP!");
            }
            else if(temp.get(k).getEffectType() == EffectType.HP_REPLENISH){
                this.setHPReplenish(temp.get(k).getAmount());
            }
            else if(temp.get(k).getEffectType() == EffectType.MP_REPLENISH){
                this.setMPReplenish(temp.get(k).getAmount());
            }
        }

        removeUsedFromInventory(potion);
    }

    public void setHP(int i){
        this.HP = this.HP - i;

        if(this.HP < 0){
            this.HP = 0;
        }
    }

    public int getHP(){return this.HP;}

    public int getMP(){return this.MP;}

    public int getSTR(){return this.STR;}

    public int getINT(){return this.INT;}

    public int getEXP(){return this.EXP;}

    public abstract void hits(Individual other);

    public int calculateDmgTaken(Weapon weapon, int STRorINT){
    	int dmg = (weapon.hit() + STRorINT) - this.getEquippableDEFBonus();
    	if(dmg < 0){dmg = 0;}
        return dmg;
    }

    public void getDamage(int damage){
        this.setHP(damage);

        if(this.getHP() > 0){
            System.out.println(this.getName() + " GETS DAMAGE: " + damage + "!");
            System.out.println(this.getName() +"'s HEALTH: " + this.getHP());
        }
        else{
            System.out.println(this.getName() + " GETS DAMAGE: " + damage + "!");
            System.out.println(this.getName() +" IS DEAD!");
            this.setIsDead();
        }
    }

    public abstract void findVisibleTiles(Room room);

    public ArrayList<MapTile> getVisibleTiles(){return this.visibleTiles;}
}

class Warrior extends Player{

    public Warrior(String name){
        super(name);
        //this.isEquipped.put(SlotType.WAND, Equippables.NONE);
        //an eixa kai SlotType.WAND to opoio den hthela diathesimo gia ton warrior

        System.out.println("new player: " + this.getName());
        this.visibilityRadius = 4;
    }

    public int getBaseHP(int lvl){
        int temp = this.getLevel();

        if(temp == 1){
            return 30;
        }
        else if(temp == 2){
            return 40;
        }
        else if(temp == 3){
            return 50;
        }
        else if(temp == 4){
            return 60;
        }
        else if(temp == 5){
            return 80;
        }
        else{
            return 100;
        }
    }

    public int getBaseMP(int lvl){
        int temp = this.getLevel();

        if(temp == 1){
            return 0;
        }
        else if(temp == 2){
            return 0;
        }
        else if(temp == 3){
            return 0;
        }
        else if(temp == 4){
            return 0;
        }
        else if(temp == 5){
            return 0;
        }
        else{
            return 0;
        }
    }

    public int getBaseSTR(int lvl){
        int temp = this.getLevel();

        if(temp == 1){
            return 10;
        }
        else if(temp == 2){
            return 15;
        }
        else if(temp == 3){
            return 25;
        }
        else if(temp == 4){
            return 30;
        }
        else if(temp == 5){
            return 40;
        }
        else{
            return 50;
        }
    }

    public int getBaseINT(int lvl){
        int temp = this.getLevel();

        if(temp == 1){
            return 0;
        }
        else if(temp == 2){
            return 0;
        }
        else if(temp == 3){
            return 0;
        }
        else if(temp == 4){
            return 0;
        }
        else if(temp == 5){
            return 0;
        }
        else{
            return 0;
        }
    }

    public void rest(){
        this.setHPReplenish(5);
        this.setMPReplenish(0);
    }

    public void findVisibleTiles(Room room) {
        MapTile playersTile = this.getItsTile();

        for (int i = (playersTile.getPosition().getY() - this.visibilityRadius); i == (playersTile.getPosition().getY() + this.visibilityRadius); i++) {
            for (int j = (playersTile.getPosition().getX() - this.visibilityRadius); j == (playersTile.getPosition().getX() + this.visibilityRadius); j++) {
                if ((i >= 0) && (j >= 0) && (i < room.getWidth()) && (j < room.getHeight())) {
                    this.visibleTiles.add(room.getRoomTiles()[j][i]);
                }
            }
        }
    }

    public void hits(Individual other){
        Equippable playersWeapon = (PlayerWeapon) this.getItsWeapon();

        int damage = other.calculateDmgTaken((Weapon) playersWeapon, this.getSTR());
        other.getDamage(damage);
    }
}

class Wizard extends Player{

    public Wizard(String name){
        super(name);

        System.out.println("new player: " + this.getName());
        this.visibilityRadius = 8;
    }

    public int getBaseHP(int lvl){
        int temp = this.getLevel();

        if(temp == 1){
            return 15;
        }
        else if(temp == 2){
            return 20;
        }
        else if(temp == 3){
            return 25;
        }
        else if(temp == 4){
            return 30;
        }
        else if(temp == 5){
            return 35;
        }
        else{
            return 50;
        }
    }

    public int getBaseMP(int lvl){
        int temp = this.getLevel();

        if(temp == 1){
            return 40;
        }
        else if(temp == 2){
            return 50;
        }
        else if(temp == 3){
            return 60;
        }
        else if(temp == 4){
            return 80;
        }
        else if(temp == 5){
            return 100;
        }
        else{
            return 120;
        }
    }

    public int getBaseSTR(int lvl){
        int temp = this.getLevel();

        if(temp == 1){
            return 0;
        }
        else if(temp == 2){
            return 0;
        }
        else if(temp == 3){
            return 0;
        }
        else if(temp == 4){
            return 0;
        }
        else if(temp == 5){
            return 0;
        }
        else{
            return 0;
        }
    }

    public int getBaseINT(int lvl){
        int temp = this.getLevel();

        if(temp == 1){
            return 20;
        }
        else if(temp == 2){
            return 25;
        }
        else if(temp == 3){
            return 30;
        }
        else if(temp == 4){
            return 40;
        }
        else if(temp == 5){
            return 55;
        }
        else{
            return 80;
        }
    }

    public void rest(){
        this.setHPReplenish(5);
        this.setMPReplenish(5);
    }

    public void setMP(int i){
        this.MP = this.MP - i;

        if(this.MP < 0){
            this.MP = 0;
        }
    }

    public void hits(Individual other){
        Equippable playersWeapon = (PlayerWeapon) this.getItsWeapon();

        int damage = other.calculateDmgTaken((Weapon) playersWeapon, this.getINT());
        other.getDamage(damage);

        int temp = this.getLevel();

        if(temp == 1){
            this.setMP(5);
        }
        else if(temp == 2){
           this.setMP(5);
        }
        else if(temp == 3){
           this.setMP(8);
        }
        else if(temp == 4){
           this.setMP(8);
        }
        else if(temp == 5){
           this.setMP(10);
        }
        else{
           this.setMP(12);
        }
    }

    public boolean canCastSpell(){
        int temp = this.getLevel();
        boolean b = true;

        if((temp == 1) && (this.getMP() < 5)){
            b = false;
        }
        else if((temp == 2) && (this.getMP() < 5)){
           b = false;
        }
        else if((temp == 3) && (this.getMP() < 8)){
           b = false;
        }
        else if((temp == 4) && (this.getMP() < 8)){
           b = false;
        }
        else if((temp == 5) && (this.getMP() < 10)){
           b = false;
        }
        else if((temp == 6) && (this.getMP() < 12)){
           b = false;
        }
        
        return b;
    }

    public void findVisibleTiles(Room room){
        int playerX = this.getItsTile().getPosition().getX();
        int playerY = this.getItsTile().getPosition().getY();

        for (int i = (playerY - this.visibilityRadius); i <= (playerY + this.visibilityRadius); i++) {
            for (int j = (playerX - this.visibilityRadius); j <= (playerX + this.visibilityRadius); j++) {
                if ((i >= 0) && (j >= 0) && (i < room.getWidth()) && (j < room.getHeight())) {  
                    this.visibleTiles.add(room.getRoomTiles()[j][i]);
                }
            }
        }
    }
}


abstract class Enemy extends Individual {
    int exp;
    Weapon enemyWeapon;

    public Enemy(String name, int hitPoints, int experiencePoints, int n, int s, int m){
        super(name, hitPoints);
        this.exp = experiencePoints;
        this.enemyWeapon = new EnemyWeapon(n, s, m);

        System.out.println("A " + this.getName() + " appears!");
    }

    public int getExp(){return this.exp;}

    public void hits(Individual other){
        int damage = other.calculateDmgTaken(this.getItsWeapon(), 0);
        other.getDamage(damage);
    }

    public int calculateDmgTaken(Weapon weapon, int STRorINT){
        return (STRorINT + weapon.hit());
    }

    public void getDamage(int damage){

        this.HP = this.HP - damage;

        if(this.HP > 0){
            System.out.println(this.getName() + " GETS DAMAGE: " + damage + "!");
            System.out.println(this.getName() +"'s HEALTH: " + this.HP);
        }
        else{
            System.out.println(this.getName() + " GETS DAMAGE: " + damage + "!");
            System.out.println(this.getName() + " IS DEAD!");
            this.setIsDead();
        }
    }

    public Weapon getItsWeapon(){return this.enemyWeapon;}
}

class GiantRat extends Enemy{
    public GiantRat(){ super("Giant Rat", 15, 30, 1, 6, 1);}
}

class MadGuard extends Enemy {
    public MadGuard() {super("Mad Guard", 25, 30,2, 6, 2);}
}

class NamelessOne extends Enemy {
    public NamelessOne() {super("Nameless One", 10000, 10000, 1, 6, 1000);}
}

class Shade extends Enemy{
    public Shade(){ super("Shade", 60, 150, 4, 6,6);}
}

class Skeleton extends Enemy{
    public Skeleton(){super("Skeleton", 30, 80,2, 6, 4);}
}


class SkeletonLord extends Enemy{
    public SkeletonLord(){super("Skeleton Lord", 50, 120, 3, 6, 4);}
}

class Vampire extends Enemy{
    public Vampire(){super("Eric Northman", 50, 120, 3, 6, 2);}

    @Override
    public void hits(Individual other){
        int damage = other.calculateDmgTaken(this.getItsWeapon(), 0);
        other.getDamage(damage);
        this.HP = this.HP + ((int) damage / 3);
    }
}

abstract class Weapon{
    String name;
    int bonus;
    Dice weaponDice;

    public Weapon(String name, int n, int s, int m){
        this.name = name;
        this.bonus = m;
        this.weaponDice = new Dice(n,s);
    }

    public String getName(){return this.name;}

    public int hit(){
        return this.weaponDice.roll() + this.bonus;
    }
}


class PlayerWeapon extends Weapon implements Equippable{
    Equippables et;
    SlotType st;
    ItemType it;
    ArrayList<ItemEffect> ie;
    MapTile itemsTile;
    String desc;

    public PlayerWeapon(String name, String description, int n, int s, int m){
        super(name, n, s, m);
        
        this.desc = description;
        this.ie = new ArrayList<ItemEffect>();
        this.it = ItemType.EQUIPPABLE;
        this.et = Equippables.valueOf(getName());
        this.st = SlotType.MAIN_HAND;
    }

    public String getDescription(){return this.desc;}
    
    public ItemType getItemType(){return this.it;}

    public Equippables getEquippableType(){return this.et;}

    public SlotType getSlotType(){return this.st;}

    public ArrayList<ItemEffect> getEffects(){ return this.ie; }

    public void addItemEffect(ItemEffect ie){ this.ie.add(ie);}

    public void setItemsTile(MapTile tile){this.itemsTile = tile; }

    public MapTile getItemsTile(){return this.itemsTile;}

    public int getPositionX(){return this.itemsTile.pos.getX(); }

    public int getPositionY(){return this.itemsTile.pos.getY();}
}

class EnemyWeapon extends Weapon{
    public EnemyWeapon(int n, int s, int m){
        super("Enemy's weapon", n, s, m);
    }
}

class Shield implements Equippable{

    String name, desc;
    ItemType it;
    Equippables et;
    SlotType st;
    ArrayList<ItemEffect> ie;
    MapTile itemsTile;

    public Shield(){
        this.name = "Shield";
        this.desc = "I will always protect you <3";
        this.it = ItemType.EQUIPPABLE;
        this.et = Equippables.SHIELD;
        this.st = SlotType.OFF_HAND;

        this.ie = new ArrayList<ItemEffect>();
        this.ie.add(new ItemEffect(EffectType.HP_BONUS, 10));
        this.ie.add(new ItemEffect(EffectType.STR_BONUS, 10));
        this.ie.add(new ItemEffect(EffectType.INT_BONUS, 10));
        this.ie.add(new ItemEffect(EffectType.MP_BONUS, 10));
        this.ie.add(new ItemEffect(EffectType.DEFENSE, 4));
    }

    public String getName(){return this.name;}

    public String getDescription(){return this.desc;}

    public ItemType getItemType(){return this.it;}

    public ArrayList<ItemEffect> getEffects(){return this.ie;}

    public SlotType getSlotType(){return this.st;}

    public Equippables getEquippableType(){return this.et;}

    public void setItemsTile(MapTile tile){this.itemsTile = tile;}

    public MapTile getItemsTile(){return this.itemsTile;}

    public int getPositionX(){return this.itemsTile.pos.getX(); }

    public int getPositionY(){return this.itemsTile.pos.getY();}
}

class Ring implements Equippable{

    String name, desc;
    ItemType it;
    Equippables et;
    SlotType st;
    ArrayList<ItemEffect> ie;
    MapTile itemsTile;

    public Ring(){
        this.name = "Ring";
        this.desc = "Very precious!";
        this.it = ItemType.EQUIPPABLE;
        this.et = Equippables.RING;
        this.st = SlotType.FINGER;

        this.ie = new ArrayList<ItemEffect>();
        this.ie.add(new ItemEffect(EffectType.HP_BONUS, 11));
        this.ie.add(new ItemEffect(EffectType.STR_BONUS, 7));
        this.ie.add(new ItemEffect(EffectType.INT_BONUS, 6));
        this.ie.add(new ItemEffect(EffectType.MP_BONUS, 12));
        this.ie.add(new ItemEffect(EffectType.DEFENSE, 3));
    }

    public String getName(){return this.name;}

    public String getDescription(){return this.desc;}

    public ItemType getItemType(){return this.it;}

    public ArrayList<ItemEffect> getEffects(){return this.ie;}

    public SlotType getSlotType(){return this.st;}

    public Equippables getEquippableType(){return this.et;}

    public void setItemsTile(MapTile tile){this.itemsTile = tile;}

    public MapTile getItemsTile(){return this.itemsTile;}

    public int getPositionX(){return this.itemsTile.pos.getX(); }

    public int getPositionY(){return this.itemsTile.pos.getY();}
}

class Labyrinth{
    public Room[] rooms;

    public Labyrinth() {
        // create rooms
        this.rooms = new Room[9];
        this.rooms[0] = new Room("The Pit", "A dark pit, where the voices of the damned echo", 0.4, false);
        this.rooms[1] = new Room("The Room of Bones", "Bones rattle in the darkness", 0.4, false);
        this.rooms[2] = new Room("The Undertomb", "The Nameless Ones writhe in their sleep", 0.7, false);
        this.rooms[3] = new Room("The Room of Chains", "Chains rustle all around you", 0.4, false);
        this.rooms[4] = new Room("The Painted Room", "A faint light reveals walls painted with winged creatures", 0.4, false);
        this.rooms[5] = new Room("The Treasure Room", "Little lights sparkle in the darkness", 0.4, false);
        this.rooms[6] = new Room("The Red Rock Room", "Noone has crossed the Red Rock Door, ever", 0.6, false);
        this.rooms[7] = new Room("The underground garden","A Garden of Wanders in a place so dark", 0.6, false);
        this.rooms[8] = new Room("The Hall of Throne", "The Great Hall of Throne", 0.7, true);

        // create passages
        Passage temp;
        temp = new Passage("Passage from The Pit to Room of Bones");
        temp.connect(rooms[0], rooms[1]);
        temp = new Passage("Passage from Room of Bones to The Pit");
        temp.connect(rooms[1], rooms[0]);
        temp = new Passage("Passage from The Pit to Room of Chains");
        temp.connect(rooms[0], rooms[3]);
        temp = new Passage("Passage from Room of Chains to Room of Bones");
        temp.connect(rooms[3], rooms[1]);
        temp = new Passage("Passage from Treasure Room to The Pit");
        temp.connect(rooms[5], rooms[0]);
        temp = new Passage("Passage from Undertomb to The Pit");
        temp.connect(rooms[2], rooms[0]);
        temp = new Passage("Passage from Red Rock Room to The Pit");
        temp.connect(rooms[6], rooms[0]);
        temp = new Passage("Passage from Room of Bones to Painted Room");
        temp.connect(rooms[1], rooms[4]);
        temp = new Passage("Passage from Painted Room to Treasure Room");
        temp.connect(rooms[4], rooms[5]);
        temp = new Passage("Passage from Treasure Room to Undertomb");
        temp.connect(rooms[5], rooms[2]);
        temp = new Passage("Passage from Undertomb to Painted Room");
        temp.connect(rooms[2], rooms[4]);
        temp = new Passage("Passage from Undertomb to Red Rock Room");
        temp.connect(rooms[2], rooms[6]);
        temp = new Passage("Passage from Undertomb to Underground Garden");
        temp.connect(rooms[2], rooms[7]);
        temp = new Passage("Passage from Underground Garden to Undertomb");
        temp.connect(rooms[7], rooms[2]);
        temp = new Passage("Passage from Underground Garden to Hall of Throne");
        temp.connect(rooms[7], rooms[8]);

        System.out.println("ftiaxame kai ton lavurintho!");
    }

    public int getRoomsNumber(){return this.rooms.length;}

    public Room getRoom(int i){
        if((i >= 0) && (i < this.rooms.length)){
            return (this.rooms[i]);
        }else{
            return null;
        }
    }
}

class Room{

    String name, description;
    boolean isFinal;
    static int numRooms = 0;
    Random rand;
    int WIDTH, HEIGHT;
    MapTile startTile;
    ArrayList<MapTile> availableTiles;//oxi to startTile, oute ta exit tiles
    MapTile[][] roomTiles;
    int room_id;
    ArrayList<Passage> exits;
    double fillRate;
    int fillCount, tilesBound;
    Map<MapTile, Passage> mapExit;


    public Room(String name, String desc, double flrt, boolean isFinal){
        this.name = name;
        this.description = desc;
        this.fillRate = flrt;
        this.isFinal = isFinal;
        this.room_id = Room.numRooms;
        this.mapExit = new HashMap<MapTile, Passage>();
        numRooms++;

        int startX, startY;

        this.rand = new Random();
        this.WIDTH = rand.nextInt(41) + 20;
        this.HEIGHT = rand.nextInt(31) + 20;

        System.out.println("dwmatio" + this.getRoomNumber() + ": " + this.getName());
        System.out.println("DIASTASEIS: height: " + this.HEIGHT + ", width: " + this.WIDTH);

        startX = this.rand.nextInt(HEIGHT - 2) +1;
        startY = this.rand.nextInt(WIDTH - 2) + 1;

        System.out.println("START: x= " + startX + "y= " + startY);

        this.availableTiles = new ArrayList<MapTile>();
        this.roomTiles = new MapTile[HEIGHT][WIDTH];
        this.exits = new ArrayList<Passage>();
        this.fillCount = 1;
        this.tilesBound = (int) (this.fillRate * (HEIGHT - 2) * (WIDTH - 2));

        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < WIDTH; j++) {

                this.roomTiles[i][j] = new MapTile(TileType.WALL, new Position(i, j), TileState.UNKNOWN);
            }
        }

        Position current = new Position(startX, startY);

        this.roomTiles[current.getX()][current.getY()].setTileType(TileType.START);
        this.startTile = this.roomTiles[current.getX()][current.getY()];

        ArrayList<MapTile> geitones = new ArrayList<MapTile>();

        while(this.fillCount < this.tilesBound) {

            for(int i = 0; i < 4; i++){
                Position tempPosition = current.getNewPosition(i);

                if((tempPosition.getX() >= this.HEIGHT - 2) || (tempPosition.getX() < 1) || (tempPosition.getY() >= this.WIDTH - 2) || (tempPosition.getY() < 1)) {
                    continue;
                }

                boolean contains = geitones.contains(this.roomTiles[tempPosition.getX()][tempPosition.getY()]);
                if((this.roomTiles[tempPosition.getX()][tempPosition.getY()].getTileType() == TileType.WALL) && (contains == false)){
                    geitones.add(this.roomTiles[tempPosition.getX()][tempPosition.getY()]);
                }
            }

            if(geitones.size() > 0){
                int i = this.rand.nextInt(geitones.size());
                MapTile newTile = geitones.get(i);

                geitones.remove(i);
               
                newTile.setTileType(TileType.FLOOR);
                this.fillCount = this.fillCount + 1;
                this.availableTiles.add(newTile);
                newTile.unsetOccupied();
                newTile.unsetHasItem();

                current = newTile.getPosition();
            }
        }

        if(this.isFinal()){
            this.addExit(null);
        }

        System.out.println("availiable tiles: " + this.availableTiles.size());
    }

    public void addExit(Passage newExit){

        if (this.exits.size() >= 8) {
            System.out.println("yoyoyo san polla na ginan ta exits!");
            return;
        }

        if(this.availableTiles.size() > 0) {
            int pos = this.rand.nextInt(this.availableTiles.size());

            if(this.isFinal() == false){
                this.availableTiles.get(pos).setTileType(TileType.EXIT);
                this.mapExit.put(this.availableTiles.get(pos), newExit);
                this.exits.add(newExit);
            }else{
                this.availableTiles.get(pos).setTileType(TileType.FINAL_EXIT);
            }
            this.availableTiles.remove(pos);
        }
    }

    public String getName(){return this.name;}

    public String getDescription(){return this.description;}

    public int getNumExits(){return this.exits.size();}

    public boolean isFinal(){ return this.isFinal; }

    public ArrayList<MapTile> getAvailableTiles(){return this.availableTiles;}

    public MapTile getStartTile(){return this.startTile;}

    public int getRoomNumber(){return this.room_id;}

    public MapTile[][] getRoomTiles(){return this.roomTiles;}

    public int getHeight(){return this.HEIGHT;}

    public int getWidth(){return this.WIDTH;}
}

class Passage{
    String name;
    Room destination;

    public Passage(String name){
        this.name = name;

        System.out.println("ftiaxame " + this.getName());
    }

    public void connect(Room fromRoom, Room toRoom){
        this.destination = toRoom;

        Passage passage = this;
        fromRoom.addExit(passage);
    }

    public Room getDestination(){return this.destination;}

    public String getName(){ return this.name;}
}

class Position{
    int i,j;

    public Position(int k, int l){
        this.i = k;
        this.j = l;
    }

    //0:left, 1:right, 2:down, 3:up
    public Position getNewPosition(int dir){

        if(dir == 0){
            return new Position((this.getX()), (this.getY() - 1));
        }
        else if(dir == 1){
            return new Position((this.getX()), (this.getY() + 1));
        }
        else if(dir == 2){
            return new Position((this.getX() + 1), (this.getY()));
        }
        else{
            return new Position((this.getX() - 1), (this.getY()));
        }
    }

    public int getX(){return this.i;}

    public int getY(){return this.j;}
}



class MyFrame extends JFrame{
        JLabel playerStatus;  
        JTextArea gameLog;   
        JComponent plotPanel;   
        Image img;

        public MyFrame(){
            JPanel mainPanel = new JPanel();
            BorderLayout b = new BorderLayout();
            mainPanel.setLayout(b);

            JPanel ctrlPanel = new JPanel();
            ctrlPanel.setLayout(new GridLayout(1, 2));
            ctrlPanel.setMaximumSize(new Dimension(10, 10));

            this.playerStatus = new JLabel("PLAYERS STATE:");

            this.gameLog = new JTextArea(8, 80);
            this.gameLog.setMaximumSize(new Dimension(10, 5));
            JScrollPane scrollPane = new JScrollPane(this.gameLog);
            
            ctrlPanel.add(playerStatus);
            // ctrlPanel.add(gameLog);
            ctrlPanel.add(scrollPane);

            this.plotPanel = new JComponent() {
                @Override
                public void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.drawImage(img, 0, 0, null);
                }
            };
            this.plotPanel.setPreferredSize(new Dimension(900, 900));    

            // mainPanel.add(scrollPane, BorderLayout.SOUTH);
            mainPanel.add(ctrlPanel, BorderLayout.SOUTH);
            mainPanel.add(plotPanel, BorderLayout.NORTH);

            add(mainPanel);
            pack();
        }

        public void setPlayerStatusLabel(String text){this.playerStatus.setText(text);}

        public void appendToGameLog(String text){
            this.gameLog.append(text);
            this.gameLog.append(System.getProperty("line.separator"));
        }
}


public class Game {

    Random rand;
    Labyrinth env;
    ArrayList<Enemy> roomEnemies;
    ArrayList<Item> roomItems;
    ArrayList<MapTile> roomTraps;
    int maxEnemies;
    Player player;
    ArrayList<String> nextPossibleActions;
    Room currentRoom;
    MyFrame frame;

    public Game(int maxRoomEnemies, String playerClass, String playerName){
        this.rand = new Random();
        this.env = new Labyrinth();
        this.roomEnemies = new ArrayList<Enemy>();
        this.roomItems = new ArrayList<Item>();
        this.roomTraps = new ArrayList<MapTile>();
        this.maxEnemies = maxRoomEnemies;
        this.nextPossibleActions = new ArrayList<String>();
        this.currentRoom = this.env.getRoom(0);

        if(playerClass.equals("wizard")){
            this.player = new Wizard(playerName);
        }else if(playerClass.equals("warrior")){
            this.player = new Warrior(playerName);
        }
        
        this.frame = new MyFrame();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
        
        System.out.println("To sthsame to game!");
    }

    public Room getCurrentRoom(){return this.currentRoom;}

    public ArrayList<String> getNextPossibleActions(){return this.nextPossibleActions;}

    //vazei ton player sto start tile tou room
    //vriskei ta visible tiles tou ekei
    public void putPlayerIntoRoom(){
        System.out.println("put player into room jekinaw!");

        if (this.player == null)
            throw new RuntimeException("1");
        if (this.getCurrentRoom() == null)       
            throw new RuntimeException("2");
        if (this.getCurrentRoom().getStartTile() == null)        
            throw new RuntimeException("3");
        this.player.setItsTile(this.getCurrentRoom().getStartTile());
        changeVisibleTiles();
        this.getCurrentRoom().getStartTile().setOccupied();
        System.out.println( this.player.getName() + " just got into room: " + this.getCurrentRoom().getName());
    }

    //adeiazei to arraylist visibletiles (an gemato) kanontas ta fogged,
    //to gemizei me ta nea tiles, ta opoia kanei visible
    public void changeVisibleTiles(){
        ArrayList<MapTile> temp = this.player.getVisibleTiles();
        System.out.println("change visible tiles jekinaw!");

        if(!temp.isEmpty()) {
            for (int i = 0; i < temp.size(); i++) {
                temp.get(i).setTileState(TileState.FOGGED);
            }
            temp.clear();
        }

        this.player.findVisibleTiles(this.getCurrentRoom());
        
        for(int i = 0; i < temp.size(); i++){
            temp.get(i).setTileState(TileState.VISIBLE);
        }
    }

    //gemizei ena dwmatio me items: usables + equippables
    //vazei kai ta trap tiles
    public void fillRoom(){
        System.out.println("fill room with items jekinaw!");

        int equipNum = this.rand.nextInt(5) + 1;
        int potionNum = this.rand.nextInt(10) + 5;
        int trapNum = this.rand.nextInt(10) + 5;

        System.out.println("room: " + this.getCurrentRoom().getName() + " has: " + potionNum + " usables");

        for(int j = 0; j < potionNum; j++){
            boolean b = true;
            while(b) {
                int pos = this.rand.nextInt(this.getCurrentRoom().getAvailableTiles().size());
                MapTile tempTile = this.getCurrentRoom().availableTiles.get(pos);

                if(!tempTile.hasItem()) {
                    Item tempItem = makeRandomPotion();
                    tempItem.setItemsTile(tempTile);
                    tempTile.setHasItem();
                    tempTile.setTilesItem(tempItem);
                    this.roomItems.add(tempItem);
                    b = false;
                }
            }
        }

        System.out.println("room: " + this.getCurrentRoom().getName() + " has: " + equipNum + " equippables");

        for(int k = 0; k < equipNum; k++){
            boolean b = true;
            while(b) {
                int pos = this.rand.nextInt(this.getCurrentRoom().getAvailableTiles().size());
                MapTile tempTile = this.getCurrentRoom().availableTiles.get(pos);

                if(!tempTile.hasItem()){
                    double tempDouble = this.rand.nextDouble();
                    Item tempItem;

                    if(tempDouble <= 0.2){
                        tempItem = new Shield();
                        System.out.println("new " + tempItem.getName());
                    }else if(tempDouble <= 0.4){
                        tempItem = new Ring();
                        System.out.println("new " + tempItem.getName());
                    }else{
                        tempItem = makeRandomWeapon(getItemLevel());
                    }

                    tempItem.setItemsTile(tempTile);
                    tempTile.setHasItem();
                    tempTile.setTilesItem(tempItem);
                    this.roomItems.add(tempItem);
                    b = false;
                }
            }
        }

        System.out.println("room: " + this.getCurrentRoom().getName() + " has: " + trapNum + " traps");

        for(int i = 0; i < trapNum; i++){
            boolean b = true;
            while(b) {
                int pos = this.rand.nextInt(this.getCurrentRoom().getAvailableTiles().size());
                MapTile tempTile = this.getCurrentRoom().availableTiles.get(pos);

                if(!tempTile.isTrap()) {
                    tempTile.setIsTrap();
                    this.roomTraps.add(tempTile);
                    b = false;
                }
            }
        }
    }

    public Usable makeRandomPotion(){
        System.out.println("make random potion jekinaw!");

        double temp = this.rand.nextDouble();

        if(temp <= 0.2){
            System.out.println("new restoration potion");
            return new RestorationPotion();
        }
        else if(temp <= 0.6){
            System.out.println("new health potion");
            return new HealthPotion();
        }
        else{
            System.out.println("new mana potion");
            return new ManaPotion();
        }
    }

    public int getItemLevel(){
        return (5 * (this.getCurrentRoom().getRoomNumber() + 1) + 5);
    }

    public PlayerWeapon makeRandomWeapon(int itemLevel){
        System.out.println("make random weapon jekinaw!");

        String[] adjectives = {"Fierce", "Stubborn", "Pure", "Ancient"};

        Map<Class, java.util.List<String>> playerWeapons =
                Map.of(Warrior.class, java.util.List.of("AXE", "SWORD"),
                        Wizard.class, java.util.List.of("STAFF", "WAND"));

        String[] surnames = {"of the Champion", "of Erreth-Akbe", "of Rok"};

        StringBuilder sb = new StringBuilder(adjectives[this.rand.nextInt(adjectives.length)]).append(" ");

        String name;

        if(this.player.getClass() == Wizard.class) {
            java.util.List<String> possibleWeapons = playerWeapons.get(Wizard.class);
            name = possibleWeapons.get(this.rand.nextInt(playerWeapons.get(Wizard.class).size()));
            sb.append(name).append(" ");
        } else {
            java.util.List<String> possibleWeapons = playerWeapons.get(Warrior.class);
            name = possibleWeapons.get(this.rand.nextInt(playerWeapons.get(Warrior.class).size()));
            sb.append(name).append(" ");
        }
        sb.append(surnames[this.rand.nextInt(surnames.length)]);

        String description = sb.toString();

        EffectType mainEffect;
        EffectType[] otherEffects;

        if(this.player.getClass() == Wizard.class) {
            mainEffect = EffectType.INT_BONUS;
            otherEffects = new EffectType[]{EffectType.HP_BONUS, EffectType.MP_BONUS, EffectType.DEFENSE};
        }else{
            mainEffect = EffectType.STR_BONUS;
            otherEffects = new EffectType[] {EffectType.HP_BONUS, EffectType.DEFENSE};
        }

        java.util.List<ItemEffect> effects = new LinkedList<>();

        int stat = this.rand.nextInt(itemLevel) + 1;
        effects.add(new ItemEffect(mainEffect, stat));

        itemLevel = itemLevel - stat;

        EffectType secondStat = otherEffects[this.rand.nextInt(otherEffects.length)];
        effects.add(new ItemEffect(secondStat, itemLevel));

        int diceNum = this.rand.nextInt(6) + 1;
        int pleuresNum = this.rand.nextInt(20) + 1;
        int bonus = this.rand.nextInt(10) + 1;

        PlayerWeapon weapon = new PlayerWeapon(name, description, diceNum, pleuresNum, bonus);

        for(int i = 0; i < effects.size(); i++){
            weapon.addItemEffect(effects.get(i));
        }

        System.out.println("new " + weapon.getDescription());
        return weapon;
    }

    //dedomenou to tile pou vrisketai o paikths
    //vriskei ola ta pithana next action pou mporei na parei autos
    public void setNextPossibleActions(){
        System.out.println("get next possible actions jekinaw!");

        if(!this.nextPossibleActions.isEmpty()) {
            this.nextPossibleActions.clear();
        }

        //1) change equippable: C
        if(this.player.getItsTile().hasItem()){
            if(this.player.getItsTile().getTilesItem().getItemType() == ItemType.EQUIPPABLE){
                this.nextPossibleActions.add(PossibleActions.C.toString());
            }
        }
        //2) rest: R
        this.nextPossibleActions.add(PossibleActions.R.toString());

        //3,4,5) use Health, Mana or Restoration potion: H, M, B
        for(int i = 0; i < this.player.inventory.size(); i++){
            if((this.player.inventory.get(i).getUsableType() == Usables.HEALTH_POTION) && (this.player.inventory.get(i).getUses() > 0)){
                this.nextPossibleActions.add(PossibleActions.H.toString());
            }else if((this.player.inventory.get(i).getUsableType() == Usables.MANA_POTION) && (this.player.inventory.get(i).getUses() > 0)){
                this.nextPossibleActions.add(PossibleActions.M.toString());
            }else if((this.player.inventory.get(i).getUsableType() == Usables.RESTORATION_POTION) && (this.player.inventory.get(i).getUses() > 0)){
                this.nextPossibleActions.add(PossibleActions.B.toString());
            }
        }
        //6, 7, 8, 9) move up, down, left, right: W, S, A, D
        //0:left, 1:right, 2:down, 3:up
        boolean c = false;
        for(int i = 0; i < 4; i++){
            boolean b = false;
            Position tempPosition = this.player.getItsTile().getPosition().getNewPosition(i);
            MapTile tempT = this.getCurrentRoom().roomTiles[tempPosition.getX()][tempPosition.getY()];

            if(tempT.isOccupied()){
                b = true;
                c = true;
            }

            if((i == 0) && (tempT.getTileType() != TileType.WALL) && (!b)){
                this.nextPossibleActions.add(PossibleActions.A.toString());
            }
            if((i == 1) && (tempT.getTileType() != TileType.WALL) && (!b)){
                this.nextPossibleActions.add(PossibleActions.D.toString());
            }
            if((i == 2) && (tempT.getTileType() != TileType.WALL) && (!b)){
                this.nextPossibleActions.add(PossibleActions.S.toString());
            }
            if((i == 3) && (tempT.getTileType() != TileType.WALL) && (!b)){
                this.nextPossibleActions.add(PossibleActions.W.toString());
            }
        }

        //10) hit an enemy: V
        if(this.player.getClass() == Warrior.class){
            if((c) && (this.player.getItsWeapon() != null)){this.nextPossibleActions.add(PossibleActions.V.toString());}
        }else{
            Wizard wizard = (Wizard) this.player;
            boolean d = false;
            int playerX = wizard.getItsTile().getPosition().getX();
            int playerY = wizard.getItsTile().getPosition().getY();

            for (int i = (playerX - wizard.getVisibilityRadius()); i <= (playerX + wizard.getVisibilityRadius()); i++) {
                for (int j = (playerY - wizard.getVisibilityRadius()); j <= (playerY + wizard.getVisibilityRadius()); j++) {
                    if ((i >= 0) && (j >= 0) && (j < this.getCurrentRoom().getWidth()) && (i < this.getCurrentRoom().getHeight())){    
                        if((this.getCurrentRoom().getRoomTiles()[i][j] != wizard.getItsTile()) && (this.getCurrentRoom().getRoomTiles()[i][j].isOccupied())){                        
                            d = true;
                        }
                    }
                }
            }
            if((d) && (wizard.getItsWeapon() != null) && (wizard.canCastSpell())){this.nextPossibleActions.add(PossibleActions.V.toString());}
        }
    }

    public void executeAction(String choice){
        System.out.println("execute action jekinaw!");

        if(choice.equals("W")){
            movePlayerToNextTile(3);
        }else if(choice.equals("A")){
            movePlayerToNextTile(0);
        }else if(choice.equals("S")){
            movePlayerToNextTile(2);
        }else if(choice.equals("D")){
            movePlayerToNextTile(1);
        }else if(choice.equals("R")){
            int beforeHP = this.player.getHP();
            int beforeMP = this.player.getMP();
            this.player.rest();
            this.frame.appendToGameLog(this.player.getName() + " RESTS: +" + (this.player.getHP() - beforeHP) + " HP!,  +" + (this.player.getMP() - beforeMP) + " MP!");
        }else if(choice.equals("H")){
            int beforeHP = this.player.getHP();
            this.player.usePotion(this.player.inventory.get(0));
            if(this.player.getHP() > beforeHP){
            	this.frame.appendToGameLog(this.player.inventory.get(0).getName() + ": " + this.player.inventory.get(0).getDescription() + "!");
            	this.frame.appendToGameLog(this.player.getName() + " GETS: +" + (this.player.getHP() - beforeHP) + " HP!");
           	}else{
           		this.frame.appendToGameLog("IOUUU! Spoiled Health Potion!");
				this.frame.appendToGameLog(this.player.getName() + " GETS: -" + (beforeHP - this.player.getHP()) + " HP!");
           	}
        }else if(choice.equals("M")){
            int beforeMP = this.player.getMP();
            this.player.usePotion(this.player.inventory.get(1));
            this.frame.appendToGameLog(this.player.inventory.get(1).getName() + ": " + this.player.inventory.get(1).getDescription() + "!");
            this.frame.appendToGameLog(this.player.getName() + " GETS:  +" + (this.player.getMP() - beforeMP) + " MP");
        }else if(choice.equals("B")){
            int beforeHP = this.player.getHP();
            int beforeMP = this.player.getMP();
            this.player.usePotion(this.player.inventory.get(2));
            this.frame.appendToGameLog(this.player.inventory.get(2).getName() + ": " + this.player.inventory.get(2).getDescription() + "!");
            this.frame.appendToGameLog(this.player.getName() + " GETS:  +" + (this.player.getHP() - beforeHP) + " HP!,  +" + (this.player.getMP() - beforeMP) + " MP!");
        }else if(choice.equals("C")){
            Equippable oldEquip = this.player.equip((Equippable) this.player.getItsTile().getTilesItem());
            if((oldEquip != null) && (oldEquip != this.player.getItsTile().getTilesItem())){
                this.frame.appendToGameLog(this.player.getName() + " DROPPED: " + oldEquip.getName());
            }

            if(oldEquip == this.player.getItsTile().getTilesItem()){
                this.frame.appendToGameLog("YOU CAN'T GET EQUIPPED WITH: " + oldEquip.getName() + "!");
            }else{
            	this.frame.appendToGameLog(this.player.getName() + " EQUIPPED WITH: " + this.player.getItsTile().getTilesItem().getName());
                switchTileItems(oldEquip);
            }
        }else if(choice.equals("V")){
            Enemy temp = findEnemyWithLeastHP();
            int oldHP = temp.getHP();
            
            System.out.println(this.player.getName() + " ATTACKS A " + temp.getName() + "!");
            this.frame.appendToGameLog(this.player.getName() + " ATTACKS A " + temp.getName() + "!");
            this.player.hits(temp);

            this.frame.appendToGameLog(temp.getName() + " GETS: -" + (oldHP - temp.getHP()) + " HP!");
            this.enemyIsDead(temp);
        }
    }

    public void movePlayerToNextTile(int dir){
        System.out.println("move player to next tile jekinaw!");

        Position tempPosition = this.player.getItsTile().getPosition().getNewPosition(dir);

        MapTile newTile = this.getCurrentRoom().roomTiles[tempPosition.getX()][tempPosition.getY()];
        MapTile oldTile = this.player.getItsTile();

        if(newTile.getTileType() == TileType.EXIT){
            oldTile.unsetOccupied();

            this.changeRoom(newTile);
            return;
        }

        this.player.setItsTile(newTile);
        changeVisibleTiles();

        newTile.setOccupied();
        oldTile.unsetOccupied();

        changedTile();
    }

    public void changeRoom(MapTile exitTile){
        System.out.println("change room jekinaw!");

        Room oldRoom = this.getCurrentRoom();

        MapTile tempTile = exitTile;
        Passage tempPassage = oldRoom.mapExit.get(exitTile);
        Room newRoom = tempPassage.getDestination();

        this.refreshRoomGraphics(newRoom);
        this.clearPreviousRoom(oldRoom);
        this.setCurrentRoom(newRoom);
        this.fillRoom();
        this.putPlayerIntoRoom();
        this.changeVisibleTiles();
        this.changedTile();

        System.out.println("You are in Room:" + this.getCurrentRoom().getName());
        System.out.println(this.getCurrentRoom().getDescription());
        this.frame.appendToGameLog("EXIT TO ROOM " + this.getCurrentRoom().getName() + ": ");
        this.frame.appendToGameLog(this.getCurrentRoom().getDescription());
    }

    public void clearPreviousRoom(Room room){
        System.out.println("clear previous room jekinaw!");

        ArrayList<MapTile> temp = room.getAvailableTiles();

        for(int i=0; i < temp.size(); i++){
            temp.get(i).unsetOccupied();
            temp.get(i).setTilesEnemy(null);
            temp.get(i).unsetHasItem();
            temp.get(i).setTilesItem(null);
            temp.get(i).unsetIsTrap();
        }

        this.roomEnemies.clear();
        this.roomItems.clear();
        this.roomTraps.clear();
    }

    public void setCurrentRoom(Room room){this.currentRoom = room;}

    public void changedTile(){
        System.out.println("changed tile jekinaw!");

        if(this.player.getItsTile().hasItem()){
            Item tempItem = this.player.getItsTile().getTilesItem();

            if(tempItem.getItemType() == ItemType.USABLE){
                System.out.println(tempItem.getName());
                System.out.println(((Usable) tempItem).getDescription());
                this.frame.appendToGameLog(tempItem.getName() + " ADDED TO INVENTORY!");
                

                this.player.addToInventory((Usable) tempItem);

                for(int i = 0; i < this.roomItems.size(); i++){
                    if(this.roomItems.get(i).getItemsTile() == this.player.getItsTile()){
                        this.roomItems.remove(i);
                        this.player.getItsTile().unsetHasItem();
                        this.player.getItsTile().setTilesItem(null);
                    }
                }
            }else if(tempItem.getItemType() == ItemType.EQUIPPABLE){
                System.out.println(tempItem.getName());
                this.frame.appendToGameLog("YOU FOUND A " + tempItem.getName() + ": " + tempItem.getDescription());
            }
        }

        if(this.player.getItsTile().isTrap()){
        	this.steppedOnATrap();
        }
    }

    public void steppedOnATrap(){
    	System.out.println("You stepped on a trap!");
        this.frame.appendToGameLog("You stepped on a trap!");

       	int damage = (int)(this.player.getHP() / 2);
        if(damage <= 0){damage = 1;}
        this.player.getDamage(damage);
        this.player.getItsTile().unsetIsTrap();

        System.out.println(this.player.getName() + " GETS: -" + damage + "HP!");
        this.frame.appendToGameLog(this.player.getName() + " GETS: -" + damage + "HP!");
    }

    public void switchTileItems(Equippable item){
        System.out.println("switch tile items jekinaw!");

        for(int i = 0; i < this.roomItems.size(); i++){
            if(this.roomItems.get(i).getItemsTile() == this.player.getItsTile()){
            	this.roomItems.get(i).setItemsTile(null);
                this.roomItems.remove(i);
                this.player.getItsTile().unsetHasItem();
                this.player.getItsTile().setTilesItem(null);
                
                if(item != null){
                    this.roomItems.add(item);
                    item.setItemsTile(this.player.getItsTile());
                    this.player.getItsTile().setHasItem();
                    this.player.getItsTile().setTilesItem(item);
                }
            }
        }
    }

    public Enemy findEnemyWithLeastHP(){
        System.out.println("find enemy with least hp jekinaw!");

        Enemy tempEnemy = null;

        if(this.player.getClass() == Warrior.class){
            for(int i = 0; i < 4; i++){
                Position tempPosition = this.player.getItsTile().getPosition().getNewPosition(i);
                MapTile tempTile = this.getCurrentRoom().roomTiles[tempPosition.getX()][tempPosition.getY()];

                if(tempTile.isOccupied()){
                    if((tempEnemy == null) || (tempEnemy.getHP() > tempTile.getTilesEnemy().getHP())){
                        tempEnemy = tempTile.getTilesEnemy();
                    }
                }
            }
        }else{
            int playerX = this.player.getItsTile().getPosition().getX();
            int playerY = this.player.getItsTile().getPosition().getY();

            for(int i = (playerX - this.player.getVisibilityRadius()); i <= (playerX + this.player.getVisibilityRadius()); i++) {
                for(int j = (playerY - this.player.getVisibilityRadius()); j <= (playerY + this.player.getVisibilityRadius()); j++) {
                    if((i >= 0) && (j >= 0) && (j < this.getCurrentRoom().getWidth()) && (i < this.getCurrentRoom().getHeight())){ 
                        MapTile tempTile = this.getCurrentRoom().roomTiles[i][j];

                        if((this.getCurrentRoom().getRoomTiles()[i][j] != this.player.getItsTile()) && (this.getCurrentRoom().getRoomTiles()[i][j].isOccupied())){  
                            if((tempEnemy == null) || (tempEnemy.getHP() > tempTile.getTilesEnemy().getHP())){
                                tempEnemy = tempTile.getTilesEnemy();
                            }
                        }
                    }
                }
            }
        }

        return tempEnemy;
    }

    public void enemyIsDead(Enemy enemy){
        System.out.println("enemy is dead jekinaw!");
        double temp = this.rand.nextDouble();

        if(enemy.isDead()){
            this.frame.appendToGameLog(enemy.getName() + " IS DEAD!");
            this.frame.appendToGameLog("+" + enemy.getExp() + "EXP");
            
            for(int i = 0; i < this.roomEnemies.size(); i++){
                if(this.roomEnemies.get(i).getItsTile() == enemy.getItsTile()){
                    MapTile tempTile = enemy.getItsTile();
                    
                    this.roomEnemies.remove(i);
                    tempTile.unsetOccupied();
                    tempTile.setTilesEnemy(null);
                    enemy.setItsTile(null);
                    this.player.setEXP(enemy.getExp());
            
                    if((temp <= 0.25) && (!tempTile.hasItem())){
                        Item tempItem = makeRandomPotion();
                        tempItem.setItemsTile(tempTile);
                        tempTile.setHasItem();
                        tempTile.setTilesItem(tempItem);
                        this.roomItems.add(tempItem);
                       
                    }
                }
            }
        }
    }

    //gia kathenan apo tous enemies tou room
    //an autos oxi geitonas kineitai ena Maptile pros ton player
    //an geitonas, sullegetai kai apo autous epilegetai tyxaia enas
    //na kanei attack ston player
    public void gameMoves(){
        System.out.println("game moves jekinaw!");

        this.addEnemy();

        Enemy tempEnemy = null;
        ArrayList<Enemy> geitonesEnemies = new ArrayList<Enemy>();

        for(int i = 0; i < 4; i++){
            Position tempPosition = this.player.getItsTile().getPosition().getNewPosition(i);
            MapTile tempTile = this.getCurrentRoom().roomTiles[tempPosition.getX()][tempPosition.getY()];

            if(tempTile.isOccupied()){
                tempEnemy = tempTile.getTilesEnemy();
                geitonesEnemies.add(tempEnemy);
            }
        }

        for(int i = 0; i < this.roomEnemies.size(); i++){
            tempEnemy = this.roomEnemies.get(i);
            boolean contains = geitonesEnemies.contains(tempEnemy);
            TileState before, after;

            if(!contains){
                before = tempEnemy.getItsTile().getTileState();
                moveEnemyTowardsPlayer(tempEnemy);
                after = tempEnemy.getItsTile().getTileState();

                if((before != after) && (after == TileState.VISIBLE)){
                    this.frame.appendToGameLog("A " + tempEnemy.getName() + " APPEARS!");              
                }
            }
        }

        if(geitonesEnemies.isEmpty() == false){
            int i = this.rand.nextInt(geitonesEnemies.size());
            tempEnemy = geitonesEnemies.get(i);
            int oldHP = this.player.getHP();

            System.out.println("A " + tempEnemy.getName() + " ATTACKS!");
            this.frame.appendToGameLog("A " + tempEnemy.getName() + " ATTACKS!");
            tempEnemy.hits(this.player);
            this.frame.appendToGameLog(this.player.getName() + " GETS: -" + (oldHP - this.player.getHP()) + "HP!");
        }
        this.setNextPossibleActions();
    }

    //adds an enemy, if room is not full
    public void addEnemy(){
        System.out.println("add enemy jekinaw!");

        Map<Integer, java.util.List<Class>> levelEnemyMap =
                Map.of(1, java.util.List.of(GiantRat.class),
                        2, java.util.List.of(GiantRat.class, MadGuard.class),
                        3, java.util.List.of(GiantRat.class, MadGuard.class, Skeleton.class, Vampire.class),
                        4, java.util.List.of(MadGuard.class, Skeleton.class, SkeletonLord.class, Vampire.class),
                        5, java.util.List.of(Skeleton.class, SkeletonLord.class, Vampire.class),
                        6, java.util.List.of(Skeleton.class, SkeletonLord.class, Shade.class, Vampire.class));
        
        //tha mporousa edw na valw pithanothta emfanishs neou enemy
        //sunarthsei tou hp tou paikth 
        if(this.roomEnemies.size() < this.maxEnemies){
            double temp = this.rand.nextDouble();

            if(temp <= 0.2){
                java.util.List<Class> possibleEnemies = levelEnemyMap.get(this.player.getLevel());

                while(true) {
                    Class enemyClass = possibleEnemies.get(this.rand.nextInt(possibleEnemies.size()));
                    try {
                        Enemy enemy = (Enemy) enemyClass.getDeclaredConstructor().newInstance();

                        boolean b = true;
                        while(b) {
                            int pos = this.rand.nextInt(this.getCurrentRoom().getAvailableTiles().size());
                            MapTile tempTile = this.getCurrentRoom().availableTiles.get(pos);
                            if(!tempTile.isOccupied()){
                                enemy.setItsTile(tempTile);
                                tempTile.setOccupied();
                                tempTile.setTilesEnemy(enemy);
                                this.roomEnemies.add(enemy);
                                b = false;
                                
                                if(enemy.getItsTile().getTileState() == TileState.VISIBLE){
                                    this.frame.appendToGameLog("A " + enemy.getName() + " APPEARS!");
                                }
                                System.out.println("NEW enemy" + enemy.getName());
                            }
                        }
                        break;
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    //gia enan enemy, oxi geitona tou player
    // vriskei to pio kontino pros ton player, geitoniko tou MaptTile
    //kai an auto elethero ton metakinei ekei, alliws menei sthn idia thesh
    //0:left, 1:right, 2:down, 3:up
    public void moveEnemyTowardsPlayer(Enemy enemy){
        System.out.println("move enemy towards player jekinaw!");

        int tempDX, tempDY, dir;
        tempDX = Math.abs(this.player.getPositionX() - enemy.getPositionX());
        tempDY = Math.abs(this.player.getPositionY() - enemy.getPositionY());

        if((tempDX == 0) || ((tempDY != 0) && (tempDX >= tempDY))){
            //kinhsh dejia
            if(this.player.getPositionY() > enemy.getPositionY()){
                dir = 1;
            //kinhsh aristera
            }else{
                dir = 0;
            }
        }else{
            //kinhsh down
            if(this.player.getPositionX() > enemy.getPositionX()){
                dir = 2;
            //kinhsh up
            }else{
                dir = 3;
            }
        }

        Position tempPosition = enemy.getItsTile().getPosition().getNewPosition(dir);
        MapTile newTile = this.getCurrentRoom().roomTiles[tempPosition.getX()][tempPosition.getY()];

        if((!newTile.isOccupied()) && (newTile.getTileType() == TileType.FLOOR)){
            MapTile oldTile = enemy.getItsTile();

            enemy.setItsTile(newTile);
            newTile.setOccupied();
            newTile.setTilesEnemy(enemy);

            oldTile.unsetOccupied();
            oldTile.setTilesEnemy(null);
        }
    }


    public String getPlayerStatusText(){
        StringBuilder pstate = new StringBuilder("<html>");
        pstate.append("Room: ").append(this.getCurrentRoom().getName() + ": ").append(this.getCurrentRoom().getDescription()).append("<br/>");
        pstate.append(player.getName() + ", ").append(" level: ").append(+ player.getLevel()).append("<br/>");
        pstate.append("EXP: ").append(+ player.getEXP()).append("<br/>");
        pstate.append("HP: ").append(+ player.getHP()).append("<br/>");
        pstate.append("MP: ").append(+ player.getMP()).append("<br/>");
        pstate.append("STR: ").append(+ player.getSTR()).append("<br/>");
        pstate.append("INT: ").append(+ player.getINT()).append("<br/>");
        pstate.append("DEF: ").append(+ player.getEquippableDEFBonus()).append("<br/>");
        pstate.append("<br/>");
        pstate.append("EQUIPPABLES:");
        for(int i = 0; i < player.equipment.size(); i++){
            pstate.append(player.equipment.get(i).getSlotType().toString() + ": ").append(player.equipment.get(i).getName()).append("<br/>");
        }
        pstate.append("<br/>");
        pstate.append("USABLES:").append("<br/>");
        for(int i = 0; i < player.inventory.size(); i++){
            if(player.inventory.get(i).getUses() > 0){
                pstate.append(player.inventory.get(i).getUsableType().toString() + ": ").append(+ player.inventory.get(i).getUses()).append("<br/>");
            }        
        }
        pstate.append("</html>");

        return pstate.toString();
    }

    public void refreshRoomGraphics(Room room){
        MapTile[][] temp = room.getRoomTiles();
        int TILESIZE = 12;

        BufferedImage new_img = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) new_img.getGraphics();
        g.setColor(Color.BLACK);
        g.setBackground(Color.BLACK);  
              
        for(int i = 0; i < room.getHeight(); i++){
            for(int j = 0; j < room.getWidth(); j++){
                g.setColor(new Color(100, 60, 40));
                g.fillRect(j * TILESIZE, i * TILESIZE, TILESIZE, TILESIZE);

                if(temp[i][j].getTileState() == TileState.UNKNOWN){
                    g.setColor(new Color(155, 10, 25));
                    g.fillRect(j * TILESIZE, i * TILESIZE, (TILESIZE - 1), (TILESIZE - 1));
                }else{
                    if(temp[i][j].getTileState() == TileState.FOGGED){
                        g.setColor(new Color(178, 19, 85));
                        g.fillRect(j * TILESIZE, i * TILESIZE, (TILESIZE - 1), (TILESIZE - 1));
                    }else if(temp[i][j].getTileState() == TileState.VISIBLE){
                        g.setColor(new Color(180, 100, 100));
                        g.fillRect(j * TILESIZE, i * TILESIZE, (TILESIZE - 1), (TILESIZE - 1));

                        if(temp[i][j] == this.player.getItsTile()){
                            g.setColor(new Color(30, 200, 200));
                            g.fillArc(j * TILESIZE + 1, i * TILESIZE + 1, TILESIZE - 3, TILESIZE - 3, 0, 360);
                        }else if(temp[i][j].isOccupied()){
                            g.setColor(new Color(156, 233, 20));
                            g.fillArc(j * TILESIZE + 1, i * TILESIZE + 1, TILESIZE - 3, TILESIZE - 3, 0, 360);
                        }
                    }

                    if(temp[i][j].hasItem()){
                        if(temp[i][j].getTilesItem().getItemType() == ItemType.USABLE){
                            g.setColor(new Color(30, 250, 30));
                            g.fillRect(j * TILESIZE + TILESIZE - 3, i * TILESIZE, 2, 2);
                        }else{
                            g.setColor(new Color(45, 35, 125));
                            g.fillRect(j * TILESIZE + TILESIZE - 3, i * TILESIZE, 2, 2);
                        }
                    }
                    
                    if(temp[i][j].getTileType() == TileType.EXIT){
                        g.setColor(new Color(34, 105, 208));
                        g.fillRect(j * TILESIZE, i * TILESIZE, (TILESIZE - 1), (TILESIZE - 1));
                    }else if(temp[i][j].getTileType() == TileType.WALL){
                        g.setColor(new Color(170, 160, 120));
                        g.fillRect(j * TILESIZE, i * TILESIZE, (TILESIZE - 1), (TILESIZE - 1));
                    }

                    MapTile dat = temp[i][j];
                    boolean contains = this.roomTraps.contains(dat);
					if((contains) && (!temp[i][j].isTrap())){
                        g.setColor(new Color(50, 50, 50));
                    	g.fillRect(j * TILESIZE, i * TILESIZE, (TILESIZE - 1), (TILESIZE - 1));                
                    }
                }
                if(temp[i][j].isOccupied()){
                    g.setColor(new Color(10, 10, 10));
                    g.fillRect(j * TILESIZE, i * TILESIZE, (TILESIZE - 1), (TILESIZE - 1));                
                }
            }
        }

        this.frame.img = new_img;
        this.frame.plotPanel.repaint();
    }

    public static void main(String[] args){
        // Player class is in args[0]
        // Player name is in args[1]
        if(args.length < 2) {
            System.out.println("Missing arguments");
            System.out.println("Please run as: java Game " + " player-class player-name");
            System.out.println("where player-class is either " + "\"wizard\" or \"warrior\"");
            System.out.println("and player-name is the " + "character name");
            System.exit(0);
        }

        Scanner sc = new Scanner(System.in);
        String playerStatus, gameLog;

        //Initialize Game: make Labyrinth, Player
        //put Player on start tile of the first room (The Pit)
        //fill first room with items
        Game game = new Game(10, args[0], args[1]);
        Player player = game.player;
        game.putPlayerIntoRoom();
        ArrayList<String> nextActionsAre = new ArrayList<String>();
        game.setNextPossibleActions();
        nextActionsAre = game.getNextPossibleActions();
        game.changeVisibleTiles();
        game.fillRoom();
        game.refreshRoomGraphics(game.getCurrentRoom());
        game.frame.appendToGameLog("up: W, down: S, left: A, right: D");
        game.frame.appendToGameLog("health potion: H, mana potion: M, restoration potion: B");
        game.frame.appendToGameLog("switch equippable: C");
        game.frame.appendToGameLog("rest: R");
        game.frame.appendToGameLog("attack an enemy: V");

        System.out.println("You are in Room:" + game.getCurrentRoom().getName());
        System.out.println(game.getCurrentRoom().getDescription());

        while((!player.isDead()) && (player.getItsTile().getTileType() != TileType.FINAL_EXIT)){
            Room temp1 = game.getCurrentRoom();
            int level1 = player.getLevel();

            String choice = "Q";
            boolean contains = nextActionsAre.contains(choice);
            while(!contains){
                System.out.println("Your next possible actions are:");
                for(int i = 0; i < nextActionsAre.size(); i++){
                    System.out.println(nextActionsAre.get(i));
                }
                System.out.println("Pick one!");

                choice = sc.nextLine();
                contains = nextActionsAre.contains(choice);
            }
            //take next action
            game.executeAction(choice);
            game.frame.setPlayerStatusLabel(game.getPlayerStatusText());
            game.refreshRoomGraphics(game.getCurrentRoom());

            //elegxos an sto endiameso allajame dwmatio h vrhkame to FINAL_EXIT h an o player pethane
            if((temp1 != game.getCurrentRoom()) || (game.player.getItsTile().getTileType() == TileType.FINAL_EXIT) || (player.isDead())){
                if(temp1 != game.getCurrentRoom()){
                	System.out.println("CHANGED ROOM: from " + temp1.getName() + " to " + game.getCurrentRoom().getName());
            	}
                continue;
            }

            //elegxos an sto endiameso allajame level
            if(level1 != player.getLevel()){
                game.frame.appendToGameLog("LEVEL: " +  player.getLevel() + "!");
            }

            //game continues after players action
            game.gameMoves();
            game.frame.setPlayerStatusLabel(game.getPlayerStatusText());
            game.refreshRoomGraphics(game.getCurrentRoom());
        }

        if(game.player.isDead() == false){
            System.out.println("GAME OVER!");
            System.out.println("YOU WIN!");
        }
        else{
            System.out.println("GAME OVER!");
            System.out.println("YOU LOST!");
        }

        sc.close();
        return;
    }
}




