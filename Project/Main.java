import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;















import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.application.Application.launch;

class CommandProcessor {
    String pattern = new String();
    Pattern compiledPattern;
    Matcher matcher;
    MenuStack menuStack = new MenuStack();
    Game game;

    public CommandProcessor(Game game) {
        this.game = game;
        game.setMenuStack(menuStack);
    }

    public void commander(String input) {
        if(menuStack.isEmpty()) {
            String[] inputParts = input.split(" ");
        /* whereAmI */
            pattern = "WhereAmI\\s*";
            compiledPattern = Pattern.compile(pattern);
            matcher = compiledPattern.matcher(input);
            if (matcher.matches()) {
                game.player.whereAmI();
                return;
            }
        /* goTo */
            pattern = "GoTo \\S+\\s*";
            compiledPattern = Pattern.compile(pattern);
            matcher = compiledPattern.matcher(input);
            if (matcher.matches()) {
                game.player.checkHealthAndEnergy();

                if (game.player.getHealth().getCurrentAmount() <= 0){
                    game.player.looseConscious(game);
                }
                if (game.player.getEnergy().getCurrentAmount() >= 11){
                    game.player.getEnergy().setCurrentAmount(game.player.getEnergy().getCurrentAmount()-10);
                }
                else{
                    game.player.switchDay(game, true);
                    game.switchDay();
                }
                game.player.changeLocation(inputParts[1]);
                game.player.getLocation().entrance(game);
                return;
            }
        }
        /* inspect */
        pattern = "Inspect \\S+(\\s+\\S+)*\\s*";
        compiledPattern = Pattern.compile(pattern);
        matcher = compiledPattern.matcher(input);
        if(matcher.matches()){
            String inspectingItemName = input.substring(8);
            MenuHaving temp = game.player.inspect(inspectingItemName);
            if(temp != null) {
                menuStack.push(temp);
                System.out.println(menuStack.getFirst().toString(0));
                menuStack.getFirst().setMyMenus(game);
                menuStack.getFirst().myMenus.get(0).printMenu();
            }
            return;
        }
        /* backpack */
        pattern = "Backpack\\s*";
        compiledPattern = Pattern.compile(pattern);
        matcher = compiledPattern.matcher(input);
        if(matcher.matches()){
            menuStack.push(game.player.backpack);
            System.out.println(menuStack.getFirst().toString(0));
            menuStack.getFirst().setMyMenus(game);
            menuStack.getFirst().myMenus.get(0).printMenu();
            if(menuStack.getFirst().myMenus.get(0).options.size() == 0)
                System.out.println("Backpack is empty");
            return;
        }
        /* stats */
        pattern = "Stats\\s*";
        compiledPattern = Pattern.compile(pattern);
        matcher = compiledPattern.matcher(input);
        if(matcher.matches()){
            game.player.showStats();
            return;
        }
        if(!menuStack.isEmpty()){
        /* whereAmI */
            pattern = "WhereAmI\\s*";
            compiledPattern = Pattern.compile(pattern);
            matcher = compiledPattern.matcher(input);
            if (matcher.matches()) {
                menuStack.whereAmI();
                return;
            }
        /* back */
            pattern = "Back\\s*";
            compiledPattern = Pattern.compile(pattern);
            matcher = compiledPattern.matcher(input);
            if(matcher.matches()){
                if(menuStack.getFirst().involvedMenuNum == 0) {
                    menuStack.pop();
                    if (!menuStack.isEmpty()) {
                        System.out.println(menuStack.getFirst().toString(menuStack.getFirst().involvedMenuNum));
                        menuStack.getFirst().myMenus.get(menuStack.getFirst().involvedMenuNum).printMenu();
                    }
                }
                else if(menuStack.getFirst().involvedMenuNum > 0){
                    menuStack.getFirst().reduceInvolvedMenuNum();
                    if (!menuStack.isEmpty()) {
                        System.out.println(menuStack.getFirst().toString(menuStack.getFirst().involvedMenuNum));
                        menuStack.getFirst().myMenus.get(menuStack.getFirst().involvedMenuNum).printMenu();
                    }
                }
                return;
            }
        /* choosing from menu */
            pattern = "\\d+\\s*";
            compiledPattern = Pattern.compile(pattern);
            matcher = compiledPattern.matcher(input);
            if(matcher.matches()){
                MenuHaving temp = menuStack.getFirst();
                temp.chooseFromMenu(Integer.parseInt(input) , temp.involvedMenuNum , game);
                if(temp.backAfterChoose){
                    if(temp.involvedMenuNum == 0)
                        menuStack.pop();
                    if(temp.involvedMenuNum > 0){
                        temp.reduceInvolvedMenuNum();
                    }
                }
                if(!menuStack.isEmpty()){
                    menuStack.getFirst().setMyMenus(game);
                    System.out.println(menuStack.getFirst().toString(menuStack.getFirst().involvedMenuNum));
                    menuStack.getFirst().myMenus.get(menuStack.getFirst().involvedMenuNum).printMenu();
                }
                return;
            }
        }
        System.out.println("Invalid Command");
    }
}

class MyMenu{
    ArrayList<String> options = new ArrayList<String>();
    public void printMenu(){
        for(int i = 0 ; i < options.size() ; i++){
            System.out.println((i + 1) + ". " + options.get(i));
        }
        if(options.size() > 0)
            System.out.println();
    }
}

class MenuStack{
    ArrayList<MenuHaving> menuHavings = new ArrayList<MenuHaving>();
    public MenuHaving pop(){
        if(!menuHavings.isEmpty()) {
            MenuHaving outputingMenu = menuHavings.get(0);
            menuHavings.remove(0);
            return outputingMenu;
        }
        return null;
    }
    public boolean isEmpty() {
        return menuHavings.size() > 0 ? false : true;
    }
    public void push(MenuHaving menu){
        menuHavings.add(0 , menu);
    }
    public void whereAmI(){
        if(!menuHavings.isEmpty()){
            System.out.println(menuHavings.get(0).toString(menuHavings.get(0).involvedMenuNum));
            menuHavings.get(0).myMenus.get(menuHavings.get(0).involvedMenuNum).printMenu();
        }
    }
    public MenuHaving getFirst(){
        return menuHavings.get(0);
    }
}

class Game{
    Farm farm;
    Player player;
    Village village;
    Jungle jungle;
    MenuStack menuStack;
    int day;
    CommandProcessor commandProcessor = new CommandProcessor(this);

    public void setMenuStack(MenuStack menuStack) {
        this.menuStack = menuStack;
    }

    Season season;

    void starter()
    {

    }

    void switchDay()
    {
        farm.switchDay(this);
        day++;
        TypesOfSeason typesOfSeason = new TypesOfSeason();


        if (day%120 > 30){
            if(season.getId() == 4){

                season = typesOfSeason.spring;
            }
            if (typesOfSeason.spring.getId() == season.getId() + 1)
            {
                season = typesOfSeason.spring;
            }
            else if (typesOfSeason.summer.getId() == season.getId() + 1)
            {
                season = typesOfSeason.summer;
            }
            else if (typesOfSeason.fall.getId() == season.getId() + 1)
            {
                season = typesOfSeason.fall;
            }
            else if (typesOfSeason.winter.getId() == season.getId() + 1)
            {
                season = typesOfSeason.winter;
            }
        }

        System.out.println("Day : " + day + "\nSeason : " + season.getName() + " Location : " + player.getLocation().getName());

    }

    void switchSeason(){

    }

    Game(){
        new TypesOfDrug().setTypes();
        new TypesOfSkinProduct().setTypes();
        new TypesOfStone().setTypes();
        new TypesOfWood().setTypes();
        new TypesOfThread().setTypes();
        new TypesOfMachine().setTypes();
        new TypesOfTree().setTypes();
        new TypesOfExploreTool().setTypes();
        new TypesOfFish().setTypes();
        new TypesOfFishingTool().setTypes();
        new TypesOfFruit().setTypes();
        new TypesOfKitchenTool().setTypes();
        new TypesOfMeat().setTypes();
        new TypesOfNonMeat().setTypes();
        new TypesOfAnimalFood().setTypes();
        new TypesOfPlant().setTypes();
        new TypesOfRecipes().setTypes();
        new TypesOfSeed().setTypes();
        new TypesOfVegetable().setTypes();
        new TypesOfWateringTool().setTypes();
        new TypesOfTool().setTypes();


        this.farm = new Farm(this);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Your Name And Age By Order :");
        if (scanner.hasNext())
        {
            String name = scanner.next();
            if (scanner.hasNextInt())
            {
                int age = scanner.nextInt();
                this.player = new Player(name, age, this);
            }
        }
        this.village = new Village(this);
//        this.locations = new ArrayList<Location>();
        this.jungle = new Jungle(this);


        ArrayList<Location> goTo = new ArrayList<Location>();
        goTo.add(village);
        goTo.add(jungle);
        farm.completeGoToLocations(goTo);

        goTo = new ArrayList<Location>();
        goTo.add(jungle);
        village.completeGoToLocations(goTo);


        System.out.println("Choose The Season You Want To Start With :\n 1 : Spring\n 2 : Summer\n 3 : Fall\n 4 : Winter");
        if(scanner.hasNextInt())
        {
            int seasonID = scanner.nextInt();
            TypesOfSeason typesOfSeason = new TypesOfSeason();

            switch (seasonID){
                case 1:
                    this.season = typesOfSeason.spring;
                    break;
                case 2:
                    this.season = typesOfSeason.summer;
                    break;
                case 3:
                    this.season = typesOfSeason.fall;
                    break;
                case 4:
                    this.season = typesOfSeason.winter;
                    break;
            }
        }
    }
}

abstract class MenuHaving {
    protected String name;

    public boolean inspectable = true;
    public ArrayList<MyMenu> myMenus = new ArrayList<MyMenu>();
    public int involvedMenuNum = 0;
    public boolean backAfterChoose = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void reduceInvolvedMenuNum(){
        involvedMenuNum--;
    }

    public abstract void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game);
    public abstract String toString(int i);
    public abstract void setMyMenus(Game game);
    // TODO class backpack extend MenuHaving
}

class Farm extends Location{

    Field field;
    FruitGarden fruitGarden;
    Pond pond;
    House house;
    GreenHouse greenHouse;
    Barn barn;

    Farm(Game game){
        this.field = new Field(10);
        this.pond = new Pond();
        this.fruitGarden = new FruitGarden();
        this.house = new House(this);
        this.greenHouse = new GreenHouse(6, 1000, 200, "Spring", this);
        this.barn = new Barn(this);

        this.goToLocations.add(this.house);
        this.goToLocations.add(this.greenHouse);
        this.goToLocations.add(this.barn);

        this.menuHavings.add(this.pond);
        this.menuHavings.add(this.fruitGarden);
        this.menuHavings.add(this.field);

        setName("Farm");
    }

    public void switchDay(Game game){
        field.switchDay(game, game.season.getName());
        greenHouse.field.switchDay(game, greenHouse.getCurrentSeason());
        fruitGarden.switchDay(game);
        barn.switchDays(game);
    }
}

class Field extends MenuHaving {
    ArrayList<CultivablePlaces> cultivablePlaces;
    private int capacity;

    public void switchDay(Game game, String season){
        for (int i = 0; i < cultivablePlaces.size(); i++){
            cultivablePlaces.get(i).switchDay(game, season);
        }
    }

    Field(int capacity) {
        setName("Field");
        backAfterChoose = false;
        setCapacity(capacity);
        cultivablePlaces = new ArrayList<>();
        for (int i = 0; i < capacity; i++)
        {
            CultivablePlaces temp = new CultivablePlaces();
            cultivablePlaces.add(temp);
        }
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();
        myMenus.clear();
        for (int i = 0; i < cultivablePlaces.size(); i++)
        {
            myMenu.options.add(cultivablePlaces.get(i).getName() + " Field");
        }
        myMenus.add(myMenu);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        backAfterChoose = false;
        if (numOfMenu == 0) {
            if (numThatChosenFromMenu <= cultivablePlaces.size()){
                cultivablePlaces.get(numThatChosenFromMenu - 1).setMyMenus(game);
                game.menuStack.push(cultivablePlaces.get(numThatChosenFromMenu - 1));
            }

        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(int i) {
        switch (i){
            case 0:
                return "Field :";
        }
        return null;
    }
}

class CultivablePlaces extends MenuHaving{
    //    private ArrayList<Plant> plants = new ArrayList();
    private Plant plant = new Plant("Empty", "", 0, 0, 0,"", false);
    private boolean isJunk;
    private boolean isPlowed;

    public void switchDay(Game game, String season){
        if (plant.getName().equals("Empty")){
            plant.setWateredToday(false);
            return;
        }

        if (plant.getHarvestedTimes() == plant.getPossibleHarvestTimes()) {
            plant = new Plant("Empty", "", 0,0,0,  "", false);
            return;
        }

        if (plant.isFullGrowth()) {
            plant.setDaysTakingTospoilage(plant.getDaysTakingTospoilage() - 1);
            if (plant.getDaysTakingTospoilage() == 0) {
                isJunk = true;
                plant.setFullGrowth(false);
            }
            return;
        }
        if (plant.isWateredToday()) {
            plant.setDaysTakingToFullGrowth(plant.getDaysTakingToFullGrowth() - 1);
            if (plant.getDaysTakingToFullGrowth() == 0) {
//                if (plant.getPossibleHarvestTimes() >= plant.getHarvestedTimes()) {
                plant.setFullGrowth(true);
                plant.setDaysTakingTospoilage(1);
//                }
            }
            plant.setWateredToday(false);
        }
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        backAfterChoose = false;
        if (numOfMenu == 0)
//        involvedMenuNum++;
        {
            if (numThatChosenFromMenu == 1)
            {
                showStatus();
            }
            else if (numThatChosenFromMenu == 2 && getName().equals("Empty"))
            {
                plow(game);
            }
            else if (numThatChosenFromMenu == 2 && !getName().equals("Empty"))
            {
                water(game);
            }
            else if (numThatChosenFromMenu == 3 && getName().equals("Empty"))
            {
                water(game);
            }
            else if (numThatChosenFromMenu == 3 && !getName().equals("Empty"))
            {
                harvest(game);
            }
            else if (numThatChosenFromMenu == 4 && getName().equals("Empty"))
            {
                plant(game);
            }
            else if (numThatChosenFromMenu == 4 && !getName().equals("Empty"))
            {
                destroy(game);
            }
        }
    }

    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();
        if (getName().equals("Empty")){
            MyMenu myMenu = new MyMenu();
            myMenu.options.add("Status");
            myMenu.options.add("Plow this Field");
            myMenu.options.add("Water this Field");
            myMenu.options.add("Plant Seeds");

            myMenus.add(myMenu);
        }
        else{
            MyMenu myMenu = new MyMenu();
            myMenu.options.add("Status");
            myMenu.options.add("Water this Field");
            myMenu.options.add("Harvest crops");
            myMenu.options.add("Destroy crops");

            myMenus.add(myMenu);
        }
    }

    @Override
    public String toString(int i) {
        if (i == 0)
            return getName()+ " Field :";
        return "";
    }

    public String getName(){
        return plant.getName();
    }
    public void water(Game game){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select A Watering Can From BackPack!");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getClass().equals(WateringTool.class))
        {
            System.out.println("Not A Watering Can");
            return;
        }
        else {
            WateringTool wateringTool = WateringTool.class.cast(item.item);
            if (wateringTool.isBroken())
            {
                System.out.println("The Watering Can is broken.");
                return;
            }
            else if (wateringTool.getCurrentWater() > 0)
            {
                item.item.use();
                plant.setWateredToday(true);
                game.player.use(game, wateringTool.getLoosingEnergy());
                wateringTool.breakIt(20, wateringTool.getName());
                System.out.println("Watered !");
            }
            else
            {
                System.out.println("Watering Can is Empty !!!");
            }
        }
    }

    public void harvest(Game game){
        if (isJunk){
            if (game.player.backpack.put(new Junk(), 9))
            {
                isJunk = false;
                plant.setHarvestedTimes(plant.getHarvestedTimes() + 1);
                return;
            }
        }

        if (plant.isFullGrowth() && !isJunk){
            {
                TypesOfFruit typesOfFruit = new TypesOfFruit();
                typesOfFruit.setTypes();
                TypesOfVegetable  typesOfVegetable = new TypesOfVegetable();
                typesOfVegetable.setTypes();
                for (int i = 0; i < typesOfFruit.types.size(); i++)
                {
                    if (plant.getName().equals(typesOfFruit.types.get(i).getName()))
                    {
                        if(game.player.backpack.put(typesOfFruit.types.get(i), 9)){
                            plant.setFullGrowth(false);
                            plant.setHarvestedTimes(plant.getHarvestedTimes()+1);
                            checkIfHarvestedFully();
                        }
                        else
                        {
                            System.out.println("Backpack is Full !");
                        }
                    }
                }

                for (int i = 0; i < typesOfVegetable.types.size(); i++)
                {
                    if (plant.getName().equals(typesOfVegetable.types.get(i).getName()))
                    {
                        if(game.player.backpack.put(typesOfVegetable.types.get(i), 9)){
                            plant.setFullGrowth(false);
                            plant.setHarvestedTimes(plant.getHarvestedTimes()+1);
                            return;
//                            checkIfHarvestedFully();
                        }
                        else
                        {
                            System.out.println("Backpack is Full !");
                            return;
                        }
                    }
                }
            }
        }

        if (!isJunk && !plant.isFullGrowth())
        {
            System.out.println("Not Full Growth Plant");
        }
    }
    public void checkIfHarvestedFully(){
        if (plant.getHarvestedTimes() == plant.getPossibleHarvestTimes())
        {
            plant = new Plant("Empty", "", 0, 0, 0,"", false);
        }
    }
    public void destroy(Game game){
        isPlowed = false;
        plant = new Plant("Empty", "", 0, 0, 0,"", false);
    }
    public void plant(Game game){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select A Seed From BackPack");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getClass().equals(Seed.class))
        {
            System.out.println("Not A Seed");
            return;
        }
        else {
            Seed seed = Seed.class.cast(item.item);
            if (isPlowed)
            {
                game.player.backpack.take(seed, 1);
                TypesOfPlant typesOfPlant = new TypesOfPlant();
                typesOfPlant.setTypes();
                for (int i = 0; i < typesOfPlant.types.size(); i++)
                {
                    if (seed.getName().equals(typesOfPlant.types.get(i).getName()))
                    {
                        plant = typesOfPlant.types.get(i);
                        break;
                    }
                }

                System.out.println(plant.getName() + " Seed Planted!");

            }
            else {
                System.out.println("Not Plowed!");
                return;
            }
        }

    }

    public void plow(Game game){
        if (!isPlowed) {
            System.out.println("Select A Shovel From BackPack");
            Scanner scanner = new Scanner(System.in);

            game.player.backpack.showItems();
            SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
            if (item == null)
                return;
            if (!item.item.getClass().equals(ExploreTool.class)) {
                System.out.println("Not A Shovel**");
                return;
            } else {
                ExploreTool shovel = ExploreTool.class.cast(item.item);
                if (!shovel.getKind().equals("Shovel")) {
                    System.out.println("Not A Shovel");
                    return;
                }
                if (shovel.isBroken()) {
                    System.out.println("The Shovel is broken.");
                    return;
                }

                shovel.breakIt(20, shovel.getName());
                game.player.use(game, shovel.getLoosingEnergy());
                isPlowed = true;
            }
        }
        else
        {
            System.out.println("***\nThe Field is Already Plowed !\n***");
        }
    }

    public void showStatus(){
        if (getName().equals("Empty"))
        {
            System.out.println("***\nStatus");
            System.out.println("Empty Field");
            System.out.println("Plowed? : " + isPlowed);
            System.out.println("Watered : " + plant.isWateredToday() + "\n***\n");
        }
        else
        {
            System.out.println("***\nStatus");
            System.out.println("Crop Type : " + plant.getName());
            System.out.println("Crop Season : " + plant.getSeasonOfGrowth());
            System.out.println("Days Until Full Growth : " + plant.getDaysTakingToFullGrowth());
            System.out.println("Watered Today? : " + plant.isWateredToday());
            System.out.println("Days Until Spoilage : " + plant.getDaysTakingTospoilage());
            System.out.println("Crop Harvest Times Left : " + (plant.getPossibleHarvestTimes() - plant.getHarvestedTimes()) + "\n***\n");
        }
    }
}
class Junk extends Item{
    private String name = new String("Junk");
    Junk(){
        setBackpackable(true);
    }
}
class Plant{
    private boolean finish;
    private String name;
    private String description;
    private int possibleHarvestTimes;
    private int daysTakingToFullGrowth;
    private int daysTakingTospoilage;
    private int daysToSpoilage;
    private int harvestedTimes;
    private boolean isWateredToday;
    private boolean isFullGrowth;
    private boolean isRoot;
    private String seasonOfGrowth;
    private boolean isJunk;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPossibleHarvestTimes() {
        return possibleHarvestTimes;
    }

    public void setPossibleHarvestTimes(int possibleHarvestTimes) {
        this.possibleHarvestTimes = possibleHarvestTimes;
    }

    public int getDaysTakingToFullGrowth() {
        return daysTakingToFullGrowth;
    }

    public void setDaysTakingToFullGrowth(int daysTakingToFullGrowth) {
        this.daysTakingToFullGrowth = daysTakingToFullGrowth;
    }

    public int getDaysToSpoilage() {
        return daysToSpoilage;
    }

    public void setDaysToSpoilage(int daysToSpoilage) {
        this.daysToSpoilage = daysToSpoilage;
    }

    public int getHarvestedTimes() {
        return harvestedTimes;
    }

    public void setHarvestedTimes(int harvestedTimes) {
        this.harvestedTimes = harvestedTimes;
    }

    public boolean isWateredToday() {
        return isWateredToday;
    }

    public void setWateredToday(boolean wateredToday) {
        isWateredToday = wateredToday;
    }

    public boolean isFullGrowth() {
        return isFullGrowth;
    }

    public void setFullGrowth(boolean fullGrowth) {
        isFullGrowth = fullGrowth;
    }

    public void changeIsFullGrowth(){

    }
    public void showStatus(){

    }

    Plant(String name, String description, int possibleHarvestTimes, int daysTakingToFullGrowth, int daysTakingToSpoilage, String seasonOfGrowth, boolean isRoot)
    {
        setName(name);
        setSeasonOfGrowth(seasonOfGrowth);
        setPossibleHarvestTimes(possibleHarvestTimes);
        setDaysTakingToFullGrowth(daysTakingToFullGrowth);
        setDaysTakingTospoilage(daysTakingToSpoilage);
        setDescription(description);
        setRoot(isRoot);
    }

    public int getDaysTakingTospoilage() {
        return daysTakingTospoilage;
    }

    public void setDaysTakingTospoilage(int daysTakingTospoilage) {
        this.daysTakingTospoilage = daysTakingTospoilage;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public String getSeasonOfGrowth() {
        return seasonOfGrowth;
    }

    public void setSeasonOfGrowth(String seasonOfGrowth) {
        this.seasonOfGrowth = seasonOfGrowth;
    }

    public boolean isJunk() {
        return isJunk;
    }

    public void setJunk(boolean junk) {
        isJunk = junk;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}

class Pond extends MenuHaving {
    Pond(){
        setName("Pond");
    }
    public void fullWater(){
    }
    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game){
        backAfterChoose = false;
        if (numOfMenu == 0)
        {
            if (numThatChosenFromMenu == 1)
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Select A Watering Can From BackPack!");

                game.player.backpack.showItems();
                SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
                if (item == null)
                    return;
                if (!item.item.getClass().equals(WateringTool.class))
                {
                    System.out.println("Not A Watering Can");
                    return;
                }
                else {
                    WateringTool wateringTool = WateringTool.class.cast(item.item);
                    if (wateringTool.isBroken())
                    {
                        System.out.println("The Watering Can is broken.");
                        return;
                    }
                    else
                    {
                        wateringTool.setCurrentWater(wateringTool.getCapacity());
                    }
                }


            }
        }
    }

    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();
        MyMenu newMyMenu = new MyMenu();
        newMyMenu.options.add("Fill a Watering Can");
        myMenus.add(newMyMenu);
    }

    @Override
    public String toString(int i) {
        return i == 0 ? "Pond :" : null;
    }
}
class FruitGarden extends MenuHaving {
    ArrayList<Tree> trees;

    FruitGarden(){
        setName("FruitGarden");
        TypesOfTree typesOfTree = new TypesOfTree();
        typesOfTree.setTypes();
        trees = typesOfTree.types;

    }
    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game){
        if (numOfMenu == 0) {
            backAfterChoose = false;
            if (numThatChosenFromMenu < trees.size()) {
                trees.get(numThatChosenFromMenu - 1).setMyMenus(game);
                game.menuStack.push(trees.get(numThatChosenFromMenu - 1));
            }
        }
    }

    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();
        MyMenu myMenu = new MyMenu();
//        System.out.println(trees.size());
        for (int i = 0; i < trees.size(); i++)
        {
            myMenu.options.add((trees.get(i).isBought()?"":"Buy ")+  trees.get(i).getName());
        }
        myMenus.add(myMenu);
        // TODO myMenus.options.add() tree Status!
    }

    private void showStatus(){

    }

    public void waterOneTree(int indexOfTree){

    }

    private void buyOneTree(int indexOfTree){

    }

    private void collectFruitsOfOneTree(int indexOfTree){

    }

    public void switchDay(Game game){
        for (int i = 0; i < trees.size(); i++){
            trees.get(i).switchDays(game);
        }
    }

    @Override
    public String toString(int num) {
        switch (num){
            case 0:
                return "Fruit Garden :";
        }
        return null;
    }
}

class Tree extends MenuHaving{
    private String name;
    private String seasonOfGrowth = new String();
    private int possibleHarvestTimes;
    private int harvestedTimes;
    private int price;
    private boolean isBought;
    private boolean isWateredToday;
    private boolean isFruitAvailable;

    public void switchDays(Game game){
        if (!isBought())
            return;
        else {
            if (isWateredToday()){
                if (seasonOfGrowth.equals(game.season.getName())){
                    isFruitAvailable = true;
                }
            }
            else {
                isFruitAvailable = false;
            }
        }
    }
    @Override
    public String toString(int i) {
        return getName() + " Tree : ";
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {

        if (numOfMenu == 0)
        {
            backAfterChoose = false;
            if (isBought)
            {
                if (numThatChosenFromMenu == 1)
                {
                    System.out.println("***\nStatus");
                    System.out.println("Season Of Growth :" + getSeasonOfGrowth());
                    System.out.println("Watered? : " + isWateredToday());
                    System.out.println("Fruits Available? : " + isFruitAvailable + "\n***");
                }
                else if(numThatChosenFromMenu == 2)
                {
                    water(game);
                }
                else if (numThatChosenFromMenu == 3)
                {
                    collectFruits(game);
                }

            }
            else
            {
                if (numThatChosenFromMenu == 1)
                {
                    System.out.println("Do you want To buy? Y/N");
                    Scanner scanner = new Scanner(System.in);
                    if (scanner.next().equals("Y"))
                    {
                        if (game.player.buy(price))
                        {
                            isBought = true;
                        }
                        else
                        {
                            System.out.println("Not Enough Money");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();
        if (!isBought){
            MyMenu myMenu = new MyMenu();
            myMenu.options.add("Buy");
            myMenus.add(myMenu);
        }
        else {
            MyMenu myMenu = new MyMenu();
            myMenu.options.add("Status");
            myMenu.options.add("Water This Tree");
            myMenu.options.add("Collect Fruits");
            myMenus.add(myMenu);
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPossibleHarvestTimes() {
        return possibleHarvestTimes;
    }

    public void setPossibleHarvestTimes(int possibleHarvestTimes) {
        this.possibleHarvestTimes = possibleHarvestTimes;
    }

    public int getHarvestedTimes() {
        return harvestedTimes;
    }

    public void setHarvestedTimes(int harvestedTimes) {
        this.harvestedTimes = harvestedTimes;
    }

    public int getPrice() {
        return price;
    }

    public void collectFruits(Game game) {
        if (isFruitAvailable)
        {
            TypesOfFruit typesOfFruit = new TypesOfFruit();
            typesOfFruit.setTypes();
            for (int i = 0; i < typesOfFruit.types.size(); i++)
            {
                if (getName().equals(typesOfFruit.types.get(i).getName()))
                {
                    if(game.player.backpack.put(typesOfFruit.types.get(i), 3)){
                        isFruitAvailable = false;
                    }
                    else
                    {
                        System.out.println("Backpack is Full !");
                    }
                }
            }
        }
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public void water(Game game)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select A Watering Can From BackPack!");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getClass().equals(WateringTool.class))
        {
            System.out.println("Not A Watering Can");
            return;
        }
        else {
            WateringTool wateringTool = WateringTool.class.cast(item.item);
            if (wateringTool.isBroken())
            {
                System.out.println("The Watering Can is broken.");
                return;
            }
            else if (wateringTool.getCurrentWater() > 0)
            {
                game.player.use(game, wateringTool.getLoosingEnergy());
                wateringTool.breakIt(20, wateringTool.getName());
                setWateredToday(true);
                System.out.println("Watered !");
            }
            else
            {
                System.out.println("Watering Can is Empty !!!");
            }
        }


    }

    Tree(String name, int possibleHarvestTimes, int price, String seasonOfGrowth)
    {
        setName(name);
        setPossibleHarvestTimes(possibleHarvestTimes);
        setPrice(price);
        setSeasonOfGrowth(seasonOfGrowth);
    }

    public String getSeasonOfGrowth() {
        return seasonOfGrowth;
    }

    public void setSeasonOfGrowth(String seasonOfGrowth) {
        this.seasonOfGrowth = seasonOfGrowth;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public boolean isWateredToday() {
        return isWateredToday;
    }

    public void setWateredToday(boolean wateredToday) {
        isWateredToday = wateredToday;
    }

    public boolean isFruitAvailable() {
        return isFruitAvailable;
    }

    public void setFruitAvailable(boolean fruitAvailable) {
        isFruitAvailable = fruitAvailable;
    }
}

class WeatherMachine extends MenuHaving {
    boolean isRepaired = false;
    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game){
        if (numOfMenu == 0)
        {
            if (isRepaired)
            {
                backAfterChoose = false;
                switch (numThatChosenFromMenu){
                    case 1:
                        game.farm.greenHouse.setCurrentSeason("Spring");
                        break;
                    case 2:
                        game.farm.greenHouse.setCurrentSeason("Summer");
                        break;
                    case 3:
                        game.farm.greenHouse.setCurrentSeason("Fall");
                        break;
                    case 4:
                        game.farm.greenHouse.setCurrentSeason("Winter");
                        break;
                    case 5:
                        game.farm.greenHouse.setCurrentSeason("Tropical");
                        break;
                }
            }
        }
    }
    WeatherMachine(){
        setName("WeatherMachine");
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        if (isRepaired)
        {
            myMenu.options.add("Spring");
            myMenu.options.add("Summer");
            myMenu.options.add("Fall");
            myMenu.options.add("Winter");
            myMenu.options.add("Tropical");

        }
        else
        {
            myMenu.options.add("You Have To first Repair");
        }
        myMenus.add(myMenu);
    }

    @Override
    public String toString(int i) {
        return i == 0 ? "WeatherMachine :" + (isRepaired ? "\nChoose the Greenhouse's weather :" + "": "It's not repaired")
                : null;
    }
}
class GreenHouse extends Location{
    WeatherMachine weatherMachine = new WeatherMachine();
    Field field;
    //    @Deprecated
    ArrayList<DemandingRawMaterial> demandingRawMaterialsToRepair = new ArrayList<>();
    private String currentSeason;
    private int capacity;
    private int demandingMoneyToRepair;
    private int demandingMoneyToExtendPerCapacity;
    private boolean isRepaired = false;

    GreenHouse(int capacity, int demandingMoneyToRepair, int demandingMoneyToExtendPerCapacity, String currentSeason, Farm farm)
    {
        setCapacity(capacity);
        setCurrentSeason(currentSeason);
        setDemandingMoneyToExtendPerCapacity(demandingMoneyToExtendPerCapacity);
        setDemandingMoneyToRepair(demandingMoneyToRepair);
        goToLocations.add(farm);
        this.field = new Field(capacity);

        demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Iron", 20, 2,"Stone"));
        demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Old Lumber", 20, 2, "Wood"));
        demandingMoneyToRepair = 5000;

        setName("GreenHouse");
    }

    @Override
    public void entrance(Game game) {
        involvedMenuNum = 0;
        if (!isRepaired){
            setMyMenus(game);
            game.menuStack.push(this);
            System.out.println(game.menuStack.getFirst().toString(0));
            game.menuStack.getFirst().myMenus.get(game.menuStack.getFirst().involvedMenuNum).printMenu();
        }
        else {
            menuHavings.add(this.field);
            menuHavings.add(this.weatherMachine);
        }
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int num, Game game){
        if (num == 0)
        {
            if (numThatChosenFromMenu == 1)
            {
                for (int i = 0; i < demandingRawMaterialsToRepair.size(); i++)
                {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Select " +demandingRawMaterialsToRepair.get(i).getNumber()+ demandingRawMaterialsToRepair.get(i).getName() +" From BackPack");

                    game.player.backpack.showItems();
                    SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
                    if (item == null)
                        return;
                    if (item.item.getName().equals(demandingRawMaterialsToRepair.get(i).getName())) {
                        if (game.player.backpack.take(item.item, demandingRawMaterialsToRepair.get(i).getNumber())){
                            continue;
                        }
                        else {
                            System.out.println("Not Enough Number!");
                            return;
                        }
                    }
                    else {
                        System.out.println("Wrong Item!");
                        return;
                    }
                }

                isRepaired = true;
                entrance(game);
            }
        }
    }

    @Override
    public void setMyMenus(Game game){
        myMenus.clear();
        MyMenu myMenu = new MyMenu();
        if (isRepaired) {
            return;
        }
        else {

            myMenu.options.add("Repair");
        }
        myMenus.add(myMenu);
    }

    public String toString(int i){
        return "";
    }

    public void chooseFromMenu(int num){

    }

    public String getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(String currentSeason) {
        this.currentSeason = currentSeason;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getDemandingMoneyToRepair() {
        return demandingMoneyToRepair;
    }

    public void setDemandingMoneyToRepair(int demandingMoneyToRepair) {
        this.demandingMoneyToRepair = demandingMoneyToRepair;
    }

    public int getDemandingMoneyToExtendPerCapacity() {
        return demandingMoneyToExtendPerCapacity;
    }

    public void setDemandingMoneyToExtendPerCapacity(int demandingMoneyToExtendPerCapacity) {
        this.demandingMoneyToExtendPerCapacity = demandingMoneyToExtendPerCapacity;
    }

    public boolean isRepaired() {
        return isRepaired;
    }

    public void setRepaired(boolean repaired) {
        isRepaired = repaired;
    }

    public void repair(){
        isRepaired = true;
        menuHavings.add(this.weatherMachine);
    }

    public void water(Game game){

    }

    public void extend(int increasingCapacity){

    }
}

class House extends Location{
    private Bed bed;
    private Kitchen kitchen;
    private StorageBox storageBox;

    House(Farm farm){
        this.bed = new Bed();
        this.kitchen = new Kitchen();
        this.storageBox = new StorageBox();

        goToLocations.add(farm);
        menuHavings.add(bed);
        menuHavings.add(kitchen);
        menuHavings.add(storageBox);

        setName("House");
    }
}

class Bed extends MenuHaving {

    Bed(){
        setName("Bed");
    }
    public void sleepWithoutSaving(Game game){
        game.switchDay();
        game.player.switchDay(game, false);
    }

    public void sleepAndSave(Game game){
        game.switchDay();
        game.player.switchDay(game, false);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int num, Game game){
        if(num != 0) return; // because with have only one menu for this havingMenu Object so our only choose is menu num 0
        switch (numThatChosenFromMenu) {
            case 1:
                this.sleepAndSave(game);
                break;
            case 2:
                this.sleepWithoutSaving(game);
                break;
        }
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();
        myMenu.options.add("Sleep and save game");
        myMenu.options.add("Sleep without saving");
        myMenus.add(myMenu);
    }

    // TODO Saving menu

    @Override
    public String toString(int i) {
        return i == 0 ? "Bed :" : null;
    }
}

class Kitchen extends MenuHaving {
    ArrayList<Recipe> recipes;
    private ToolShelf toolShelf;
    private boolean cook;

    Kitchen(){
        TypesOfRecipes typesOfRecipes = new TypesOfRecipes();
        typesOfRecipes.setTypes();
        recipes = typesOfRecipes.types;
        toolShelf = new ToolShelf();

        setName("Kitchen");
    }
    public void checkToolShelf(){

    }

    public void cookMeal(){

    }

    public void showRecipes(){

    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Cook a meal");
        myMenu.options.add("Check Tool Shelf");
        myMenu.options.add("Check recipes");

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        for (int i = 0; i < recipes.size(); i++)
        {
            myMenu.options.add(recipes.get(i).getName());
        }
        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenus, Game game){
        if (numOfMenus == 0)
        {
            switch (numThatChosenFromMenu){
                case 1:
                {
                    backAfterChoose = false;
                    cook = true;
                    this.involvedMenuNum++;
                    break;
                }

                case 2:
                {
                    backAfterChoose = false;
                    toolShelf.setMyMenus(game);
                    game.menuStack.push(toolShelf);
                    break;
                }
                case 3:
                {
                    backAfterChoose = false;
                    cook = false;
                    this.involvedMenuNum++;
                    break;
                }
            }
        }
        else if (numOfMenus == 1){
            recipes.get(numThatChosenFromMenu - 1).cook(toolShelf, game, cook);
//            game.menuStack.push(recipes.get(numThatChosenFromMenu));
        }
    }

    @Override
    public String toString(int i) {
        if (i == 0)
            return "Kitchen : ";
        else if(i == 1)
            return "Recipes : ";
        else
            return "";
    }
}
class Ingredients {
    String name = new String();
    int number;

    Ingredients(String name, int number){
        this.name = name;
        this.number = number;
    }
}
class Recipe extends MenuHaving{
    ArrayList<String> demandingTools = new ArrayList<>();
    ArrayList<Ingredients> ingredients = new ArrayList<>();
    private String name;
    private int price;
    private int effectOnHealth;
    private int effectOnEnergy;
    private int effectOnHealthMax;

    public void cook(ToolShelf toolShelf, Game game, boolean cook){
        System.out.println(name + " : ");
        for (int i = 0; i < demandingTools.size(); i++)
        {
            System.out.println(demandingTools.get(i));
        }

        System.out.println();
        for (int i = 0; i < ingredients.size(); i++)
        {
            System.out.println(ingredients.get(i).name + " x" + ingredients.get(i).number);
        }

        if (!cook)
        {
            return;
        }
        System.out.println("Do You Want To Cook? Y/N");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();

        if (answer.equals("N"))
        {
            return;
        }

        else if (answer.equals("Y"))
        {
            startCooking(toolShelf, game);
        }
    }

    public void startCooking(ToolShelf toolShelf, Game game){
        boolean notPossible = false;
        boolean noIngredient = false;
        ArrayList<KitchenTool> usedTools = new ArrayList<>();
        if (!game.player.backpack.put(new Food(getName(), getEffectOnHealthMax(), getEffectOnHealth(), getEffectOnEnergy()), 1)){
            System.out.println("No Room In backPack For That!");
            return;
        }
        else {
            game.player.backpack.take(new Food(getName(), getEffectOnHealthMax(), getEffectOnHealth(), getEffectOnEnergy()), 1);
        }

        for (int i = 0; i < demandingTools.size(); i++)
        {
            for (int k = 0; k < toolShelf.kitchenTools.size(); k++)
            {
                if (demandingTools.get(i).equals(toolShelf.kitchenTools.get(k).getName()))
                {
                    if (toolShelf.kitchenTools.get(k).isBought())
                    {
                        if (!toolShelf.kitchenTools.get(k).isBroken()) {
                            usedTools.add(toolShelf.kitchenTools.get(k));
                            continue;
                        }
                        else
                        {
                            System.out.println(toolShelf.kitchenTools.get(k).getName() + " is Broken!");
                            notPossible = true;
                        }
                    }
                    else
                    {
                        System.out.println("There is no " + toolShelf.kitchenTools.get(k).getName());
                        notPossible = true;
                    }
                }
            }
        }

        if (!notPossible)
        {
            Scanner scanner = new Scanner(System.in);
            ArrayList<Integer> addedIngredientsNumber = new ArrayList<>();
            ArrayList<Item> addedIngredientItem = new ArrayList<>();

            for (int i = 0; i < ingredients.size(); i++)
            {
                if (noIngredient)
                {
                    break;
                }
                System.out.println(ingredients.get(i).name + " x" + ingredients.get(i).number);
                game.player.backpack.showItems();
                SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
                if (item == null)
                    return;
                if (!item.item.getName().equals(ingredients.get(i).name))
                {
                    System.out.println("Not A " + ingredients.get(i).name);
                    return;
                }
                else {
                    if(game.player.backpack.take(item.item, ingredients.get(i).number))
                    {
                        addedIngredientItem.add(item.item);
                        addedIngredientsNumber.add(i);
                        continue;
                    }
                    else {
                        for (int j = 0; j < addedIngredientItem.size(); j++)
                        {
                            game.player.backpack.put(addedIngredientItem.get(j), addedIngredientsNumber.get(j));
                        }
                        System.out.println("Not Enough Of " + ingredients.get(i).name);
                        noIngredient = true;
                    }
                }
            }
        }

        if (!notPossible && !noIngredient){
            for (int i = 0; i < usedTools.size(); i++){
                usedTools.get(i).breakIt(10, usedTools.get(i).getName());
            }
            Food food = new Food(getName(), getEffectOnHealthMax(), getEffectOnHealth(), getEffectOnEnergy());
            game.player.backpack.put(food, 1);
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void show(){

    }

    public void setMyMenus(Game game){

    }

    @Override
    public String toString(int i) {
        return null;
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getEffectOnHealth() {
        return effectOnHealth;
    }

    public void setEffectOnHealth(int effectOnHealth) {
        this.effectOnHealth = effectOnHealth;
    }

    public int getEffectOnEnergy() {
        return effectOnEnergy;
    }

    public void setEffectOnEnergy(int effectOnEnergy) {
        this.effectOnEnergy = effectOnEnergy;
    }

    public int getEffectOnHealthMax() {
        return effectOnHealthMax;
    }

    public void setEffectOnHealthMax(int effectOnHealthMax) {
        this.effectOnHealthMax = effectOnHealthMax;
    }
}

class ToolShelf extends MenuHaving{
    ArrayList<KitchenTool> kitchenTools;
    ToolShelf(){
        TypesOfKitchenTool typesOfKitchenTool = new TypesOfKitchenTool();
        typesOfKitchenTool.setTypes();

        for (int i = 0; i < typesOfKitchenTool.types.size(); i++){

            typesOfKitchenTool.types.get(i).setBought(true);
        }

        kitchenTools = typesOfKitchenTool.types;
    }

    public void putTool(KitchenTool tool){

    }
    public void takeTool(KitchenTool tool){

    }
    public void selectTool(int indexOfTool){

    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        for (int i = 0; i < kitchenTools.size(); i++)
        {
            myMenu.options.add(kitchenTools.get(i).getName() + " Existence : " + kitchenTools.get(i).isBought());
        }
        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        if (numOfMenu == 0){
            backAfterChoose = false;
            kitchenTools.get(numThatChosenFromMenu - 1).setMyMenus(game);
            game.menuStack.push(kitchenTools.get(numThatChosenFromMenu - 1));
        }
    }

    @Override
    public String toString(int i) {
        return "Tool Shelf :";
    }
}

class StorageBox extends MenuHaving {
    StorageBox() {
        setName("Storage Box");
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Put in item");
        myMenu.options.add("Take out item");

        myMenus.add(myMenu);
    }

    private ArrayList<SavingItem> toolsInIt = new ArrayList<>();

    public void put(Game game) {
        int number = 0;
        System.out.println("Choose From BackPack");
        Scanner scanner = new Scanner(System.in);

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (item.item.isPackable())
        {
            number = scanner.nextInt();
            while (number > item.getNumber()) {
                System.out.println("Wrong Number !");
                if (!scanner.hasNextInt())
                    return;
                number = scanner.nextInt();
            }

            for (int i =0; i < toolsInIt.size(); i++)
            {
                if (toolsInIt.get(i).item.getName().equals(item.item.getName()))
                {
                    toolsInIt.get(i).setNumber(toolsInIt.get(i).getNumber() + number);
                    game.player.backpack.take(item.item, number);
                    return;
                }
            }
            toolsInIt.add(item);
            return;
        }

        else {
            toolsInIt.add(item);
        }
    }

    public void take(Game game){
        if (toolsInIt.size() == 0)
        {
            System.out.println("Empty");
            return;
        }
        showItems();
        int index;
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt())
            index = scanner.nextInt();
        else
            return;
        if (index <= toolsInIt.size())
        {
            if (toolsInIt.get(index - 1).item.isPackable())
            {
                System.out.println("Enter The Number You Want To Take : ");
                int number = scanner.nextInt();
                if (number > toolsInIt.get(index - 1).getNumber())
                {
                    System.out.println("Not That Number !");
                    return;
                }
                if (game.player.backpack.put(toolsInIt.get(index - 1).item, number))
                {
                    check();
                }
                else
                {
                    System.out.println("No Room in Backpack !");
                }
            }
            else {
                if (game.player.backpack.put(toolsInIt.get(index - 1).item, 1))
                {
                    check();
                }
                else {
                    System.out.println("No Room in Backpack !");
                }
            }
        }
    }

    public void check(){
        ArrayList<SavingItem> deletingOnes = new ArrayList<>();

        if (toolsInIt.size() == 0)
            return;

        for (int i = 0; i < toolsInIt.size(); i++)
        {
            if (toolsInIt.get(i).getNumber() == 0){
                deletingOnes.add(toolsInIt.get(i));
            }
        }
        for (int i = 0; i < deletingOnes.size(); i++)
        {
            toolsInIt.remove(deletingOnes.get(i));
        }
    }

    public void showItems(){
        for (int i = 0; i < toolsInIt.size(); i++)
        {
            if (toolsInIt.get(i).item.isPackable())
            {
                System.out.println((i+1) + toolsInIt.get(i).item.getName() + " x" + toolsInIt.get(i).getNumber());
            }
            else {
                System.out.println((i+1) + toolsInIt.get(i).item.getName());
            }
        }
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game){
        if (numOfMenu == 0)
        {
            backAfterChoose = false;
            if (numThatChosenFromMenu == 1)
            {
                put(game);
            }
            else if (numThatChosenFromMenu ==2)
            {
                take(game);
            }
        }
    }

    @Override
    public String toString(int i) {
        return i == 0 ? "Storage Box :" : null;
    }
}

class Barn extends Location{
    CowsPlace cowsPlace;
    HensPlace hensPlace;
    ShepPlace shepPlace;
    MachinePart machinePart;


    Barn(Farm farm){
        this.cowsPlace = new CowsPlace("Cows");
        this.hensPlace = new HensPlace("Hens");
        this.shepPlace = new ShepPlace("Shep");
        this.machinePart = new MachinePart();

        this.goToLocations.add(farm);

        this.menuHavings.add(this.cowsPlace);
        this.menuHavings.add(this.hensPlace);
        this.menuHavings.add(this.shepPlace);
        this.menuHavings.add(this.machinePart);

        setName("Barn");
    }

    public void switchDays(Game game){
        cowsPlace.switchDays(game);
        hensPlace.switchDays(game);
        shepPlace.switchDays(game);
    }
}

class AnimalPlace extends MenuHaving{
    public void showAnimals(){

    }

    public void selectAnAnimal(int indexOfAnimal){

    }


    @Override
    public String toString(int i) {
        return null;
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {

    }

    @Override
    public void setMyMenus(Game game) {

    }
}

class CowsPlace extends AnimalPlace{
    //    String name = new String("Cows");
    public ArrayList<Cow> cows = new ArrayList<Cow>();
    public int maxCow = 5;

    public void switchDays(Game game){
        if (cows.size() == 0)
            return;
        for (int i = 0; i < cows.size(); i++){
            cows.get(i).switchDays(game);
        }
    }

    CowsPlace(String name){
        setName(name);
//        cows.add(new Cow());
    }

    @Override
    public void showAnimals() {
        for(int i = 0 ; i < cows.size() ; i++) {
            System.out.println((i + 1) + " .Cow " + getName());
        }
    }

    @Override
    public String toString(int i) {
        switch (i){
            case 0:
                return "Cows :";
        }
        return null;
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        if (numOfMenu == 0){
            if (numThatChosenFromMenu <= cows.size()){
                backAfterChoose = false;
                cows.get(numThatChosenFromMenu - 1).setMyMenus(game);
                game.menuStack.push(cows.get(numThatChosenFromMenu - 1));
            }
        }
    }

    /*  menu 0:
    1.Cow No.2
    2.Cow ...

        menu 1:
    1.Status
    2.Feed this Cow
    3.Heal this Cow
    4.Milk this Cow
     */
    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();
        myMenus.clear();
        if (cows.size() != 0){
            for (int i = 0; i < cows.size(); i++){
                myMenu.options.add("Cow " + cows.get(i).getName());

            }
        }
        else {
            myMenu.options.add("\b\b\bThere's No Cow , First Buy From Ranch !");
            System.out.println("Empty");
        }
        myMenus.add(myMenu);
    }

    public void setName(String name){
        this.name = name;
    }
}

class HensPlace extends AnimalPlace{
    public ArrayList<Hen> hens = new ArrayList<>();
    public int maxHen = 10;
    HensPlace(String name){
        setName(name);
    }


    public void switchDays(Game game){
        if (hens.size() == 0)
            return;
        for (int i = 0; i < hens.size(); i++){
            hens.get(i).switchDays(game);
        }
    }

    @Override
    public String toString(int i) {
        switch (i){
            case 0:
                return "Hens :";
        }
        return null;
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        if (numOfMenu == 0){
            backAfterChoose = false;
            if (numThatChosenFromMenu <= hens.size()){
                hens.get(numThatChosenFromMenu - 1).setMyMenus(game);
                game.menuStack.push(hens.get(numThatChosenFromMenu - 1));
            }
        }
    }

    @Override
    public void showAnimals() {
        for(int i = 0 ; i < hens.size() ; i++){
            System.out.println((i + 1) + ". Hen " + hens.get(i).getName());
        }
    }

    /*  menu 0:
        1.Hen No.2
        2.Hen ...

            menu 1:
        1.Status
        2.Feed this Hen
        3.Heal this Hen
        4.Milk this Hen
         */
    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();
        myMenus.clear();
        if (hens.size() != 0){
            for (int i = 0; i < hens.size(); i++){
                myMenu.options.add("Hen " + hens.get(i).getName());

            }
        }
        else {
            myMenu.options.add("\b\b\bThere's No Hen , First Buy From Ranch !");
            System.out.println("Empty");
        }
        myMenus.add(myMenu);
    }

    public void setName(String name){
        this.name = name;
    }
}

class ShepPlace extends AnimalPlace{
    public ArrayList<Sheep> shep = new ArrayList<>();
    public int maxShep = 5;

    ShepPlace(String name){
        setName(name);
    }
    @Override
    public String toString(int i) {
        switch (i){
            case 0:
                return "Shep :";
        }
        return null;
    }


    public void switchDays(Game game){
        if (shep.size() == 0)
            return;
        for (int i = 0; i < shep.size(); i++){
            shep.get(i).switchDays(game);
        }
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        if (numOfMenu == 0){
            backAfterChoose = false;
            if (numThatChosenFromMenu <= shep.size()){
                shep.get(numThatChosenFromMenu - 1).setMyMenus(game);
                game.menuStack.push(shep.get(numThatChosenFromMenu - 1));
            }
        }
    }

    /*  menu 0:
    1.Cow No.2
    2.Cow ...

        menu 1:
    1.Status
    2.Feed this Cow
    3.Heal this Cow
    4.Milk this Cow
     */
    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();
        myMenus.clear();
        if (shep.size() != 0){
            for (int i = 0; i < shep.size(); i++){
                myMenu.options.add("Sheep " + shep.get(i).getName());

            }
        }
        else {
            myMenu.options.add("\b\b\bThere's No Sheep , First Buy From Ranch !");
            System.out.println("Empty");
        }
        myMenus.add(myMenu);
    }

    @Override
    public void showAnimals() {
        for(int i = 0 ; i < shep.size() ; i++){
            System.out.println((i + 1) + ". Sheep " + getName());
        }
    }

    public void setName(String name){
        this.name = name;
    }
}

class MachinePart extends MenuHaving{
    ArrayList<Machine> machines = new ArrayList<>();

    MachinePart(){
        this.name = "Machines";
//        TypesOfMachine typesOfMachine = new TypesOfMachine();
//        typesOfMachine.setTypes();
//        machines.add(typesOfMachine.types.get(0));
    }
    @Override
    public String toString(int i) {
        return "Machines :";
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        if (numOfMenu == 0){
            backAfterChoose = false;
            if (numThatChosenFromMenu <= machines.size()){
                machines.get(numThatChosenFromMenu - 1).setMyMenus(game);
                game.menuStack.push(machines.get(numThatChosenFromMenu - 1));
            }
        }
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenus.clear();
        if (machines.size() != 0){
            for (int i = 0; i < machines.size(); i++){
                myMenu.options.add(machines.get(i).getName() + "Machine");
            }
        }
        else {
            myMenu.options.add("\b\b\bThere's No Machine , First Buy From Lab !");
            System.out.println("Empty");
        }
        myMenus.add(myMenu);
    }
}

class AnimalMedicine extends Item{
    private String name = new String();
    private int buyingCost;

    AnimalMedicine(String name, int price){
        setName(name);
        setBuyingCost(price);
    }

    @Override
    public String toString(int i) {
        return name;
    }

    public int getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(int price) {
        this.buyingCost = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void showDescription(){
        System.out.println("name : " + name);
        System.out.println("Price : " + buyingCost);
    }
}

abstract class Animal extends MenuHaving{
    private int health;
    private int satiety;
    private boolean isEatenToday;
    private String name;
    private String kind;

    abstract void feed(Game game);

    void heal(Game game){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select An Animal Medicine From BackPack");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getClass().equals(AnimalMedicine.class))
        {
            System.out.println("Not An AnimalMedicine");
            return;
        }
        else {
            game.player.backpack.take(item.item, 1);
            setHealth(100);
            System.out.println("Full Health !");
        }

    }

    abstract public void showStatus();
    abstract public void showDescription();

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSatiety() {
        return satiety;
    }

    public void setSatiety(int satiety) {
        this.satiety = satiety;
    }

    public boolean isEatenToday() {
        return isEatenToday;
    }

    public void setEatenToday(boolean eatenToday) {
        isEatenToday = eatenToday;
    }
}

class Cow extends Animal{
    private boolean canBeMilked = false;
    private static int buyingCost = 4000;
    private static int sellingCost = 3000;

    public void switchDays(Game game){
        if (getSatiety() >= 30){
            canBeMilked = true;
        }
        if (getSatiety() >= 20){
            setSatiety(getSatiety() - 20);
        }
        else{
            setSatiety(0);
        }

//        if (getHealth() <= 0){
//            game.farm.barn.cowsPlace.cows.remove(this);
//            System.out.println("One Cow died Of hunger !");
//        }
    }
    public static int getBuyingCost() {
        return buyingCost;
    }

    public static int getSellingCost() {
        return sellingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }

    public void setSellingCost(int sellingCost) {
        this.sellingCost = sellingCost;
    }

    @Override
    public void feed(Game game){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select A Cow Food From BackPack");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getClass().equals(AnimalFood.class))
        {
            System.out.println("Not An AnimalFood");
            return;
        }
        else {
            AnimalFood animalFood = AnimalFood.class.cast(item.item);
            if (animalFood.animalsWhichCanEatIt.contains("Cow")){
                game.player.backpack.take(item.item, 1);
                setEatenToday(true);
                if (getSatiety() <= 50){
                    setSatiety(getSatiety() + 50);
                }
                else
                    setSatiety(100);
                System.out.println("Eaten !");
            }
            else {
                System.out.println("Not A Good Food For A Cow!");
            }
        }
    }
    public void milk(Game game){
        if (!canBeMilked){
            System.out.println("No Milk !");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select A Milker From BackPack");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getName().equals("Milker"))
        {
            System.out.println("Not A Milker");
            return;
        }
        else {
            if (item.item.isBroken()){
                System.out.println("It is Broken !");
                return;
            }
            TypesOfNonMeat typesOfNonMeat = new TypesOfNonMeat();
            typesOfNonMeat.setTypes();
            for (int i = 0; i < typesOfNonMeat.types.size(); i++){
                if (typesOfNonMeat.types.get(i).getName().equals("Cow Milk")){
                    if (game.player.backpack.put(typesOfNonMeat.types.get(i), 1)){
                        Tool.class.cast(item.item).breakIt(20, item.item.getName());
                        canBeMilked = false;
                        System.out.println("Milked");
                        break;
                    }
                    else {
                        System.out.println("No Room In Backpack!");
                        return;
                    }
                }
            }
        }
    }
    public void showStatus(){
        System.out.println("Status : ");
        System.out.println("Health : " + getHealth());
        System.out.println("Satiety : " + getSatiety());
        System.out.println("isEatenToday? : " + isEatenToday());
    }

    @Override
    public void showDescription() {
        System.out.println("Cow :");
        System.out.println("Benefits : 1.milk\n2.meat\n3.Wool");
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Status");
        myMenu.options.add("Feed this Cow");
        myMenu.options.add("Heal this Cow");
        myMenu.options.add("Milk this Cow");

        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        backAfterChoose = false;
        if (numOfMenu ==0){
            if (numThatChosenFromMenu == 1){
                showStatus();
            }
            else if (numThatChosenFromMenu == 2){
                feed(game);
            }
            else if (numThatChosenFromMenu == 3){
                heal(game);
            }
            else if (numThatChosenFromMenu == 4){
                milk(game);
            }
        }
    }

    @Override
    public String toString(int i) {
        return "Cow : ";
    }
}

class Hen extends Animal{
    private boolean canGiveEgg;
    private int eggsPerTime = 2;
    private static int buyingCost = 4000;
    private static int sellingCost = 3000;

    public void switchDays(Game game){
        if (getSatiety() >= 30){
            canGiveEgg = true;
//            setHealth(getHealth() - 1);
        }
        if (getSatiety() >= 20){
            setSatiety(getSatiety() - 20);
        }
        else{
            setSatiety(0);
        }
//        setHealth(getHealth() - 3);

//        if (getHealth() <= 0){
//            game.farm.barn.cowsPlace.cows.remove(this);
//            System.out.println("One Hen died Of hunger !");
//        }
    }

    public static int getBuyingCost() {
        return buyingCost;
    }

    public static int getSellingCost() {
        return sellingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }

    public void setSellingCost(int sellingCost) {
        this.sellingCost = sellingCost;
    }

    @Override
    public void feed(Game game){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select A Hen Food From BackPack");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getClass().equals(AnimalFood.class))
        {
            System.out.println("Not An AnimalFood");
            return;
        }
        else {
            AnimalFood animalFood = AnimalFood.class.cast(item.item);
            if (animalFood.animalsWhichCanEatIt.contains("Hen")){
                game.player.backpack.take(item.item, 1);
                setEatenToday(true);
                if (getSatiety() <= 50){
                    setSatiety(getSatiety() + 50);
                }
                else
                    setSatiety(100);
                System.out.println("Eaten !");
            }
            else {
                System.out.println("Not A Good Food For A Hen!");
            }
        }
    }

    @Override
    public void
    showDescription() {
        System.out.println("Hen :");
        System.out.println("Benefits : 1.egg\n2.meat");
    }

    public void getEgg(Game game){
        if (canGiveEgg)
        {
            NonMeat egg = new NonMeat("", 0, 0, 0, true);
            TypesOfNonMeat typesOfNonMeat = new TypesOfNonMeat();
            typesOfNonMeat.setTypes();
            for (int i = 0; i < typesOfNonMeat.types.size(); i++)
            {
                if (typesOfNonMeat.types.get(i).getName().equals("Egg")){
                    canGiveEgg = false;
                    egg = typesOfNonMeat.types.get(i);
                    if (game.player.backpack.put(egg, getEggsPerTime())){
                        System.out.println("Egged!");
                    }
                    else
                        System.out.println("No Room In Backpack!");
                    break;
                }
            }

        }
        else {
            System.out.println("Can't Give Any Egg !!");
            return;
        }

    }


    public void showStatus(){
        System.out.println("Status : ");
        System.out.println("Health : " + getHealth());
        System.out.println("Satiety : " + getSatiety());
        System.out.println("isEatenToday? : " + isEatenToday());
    }

    @Override
    public String toString(int i) {
        return "Hen : ";
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Status");
        myMenu.options.add("Feed this Hen");
        myMenu.options.add("Heal this Hen");
        myMenu.options.add("Egg this Hen");

        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        backAfterChoose = false;
        if (numOfMenu ==0){
            if (numThatChosenFromMenu == 1){
                showStatus();
            }
            else if (numThatChosenFromMenu == 2){
                feed(game);
            }
            else if (numThatChosenFromMenu == 3){
                heal(game);
            }
            else if (numThatChosenFromMenu == 4){
                getEgg(game);
            }
        }
    }

    public int getEggsPerTime() {
        return eggsPerTime;
    }

    public void setEggsPerTime(int eggsPerTime) {
        this.eggsPerTime = eggsPerTime;
    }
}

class Sheep extends Animal{
    boolean canBeSheered;
    private int daysToGetSheered = 2;
    private int daysAfterBeenSheered;
    private static int buyingCost = 4000;
    private static int sellingCost = 3000;

    public void switchDays(Game game){
        if (getSatiety() >= 30){
            if (daysToGetSheered != 0){
                daysToGetSheered--;
                canBeSheered = false;
            }
            else {
                canBeSheered = true;
                daysToGetSheered = 3;
            }
//            setHealth(getHealth() - 1);
        }
        if (getSatiety() >= 20){
            setSatiety(getSatiety() - 20);
        }
        else{
            setSatiety(0);
        }
//        setHealth(getHealth() - 3);

//        if (getHealth() <= 0){
//            game.farm.barn.shepPlace.shep.remove(this);
//            System.out.println("One Sheep died Of hunger !");
//        }
    }
    public static int getBuyingCost() {
        return buyingCost;
    }

    public static int getSellingCost() {
        return sellingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }

    public void setSellingCost(int sellingCost) {
        this.sellingCost = sellingCost;
    }

    @Override
    public void showStatus(){
        System.out.println("Status : ");
        System.out.println("Health : " + getHealth());
        System.out.println("Satiety : " + getSatiety());
        System.out.println("isEatenToday? : " + isEatenToday());
        System.out.println("Days To Get Sheered Again : " + getDaysToGetSheered());
    }
    @Override
    void feed(Game game){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select A Sheep Food From BackPack");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getClass().equals(AnimalFood.class))
        {
            System.out.println("Not An AnimalFood");
            return;
        }
        else {
            AnimalFood animalFood = AnimalFood.class.cast(item.item);
            if (animalFood.animalsWhichCanEatIt.contains("Sheep")){
                game.player.backpack.take(item.item, 1);
                setEatenToday(true);
                if (getSatiety() <= 50){
                    setSatiety(getSatiety() + 50);
                }
                else
                    setSatiety(100);
                System.out.println("Eaten !");
            }
            else {
                System.out.println("Not A Good Food For A Sheep!");
            }
        }

    }

    @Override
    public void showDescription() {
        System.out.println("Sheep :");
        System.out.println("Benefits : 1.egg 2.meat");
    }

    public void sheer(Game game){
        if (!canBeSheered){
            System.out.println("No Sheer !");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select A Scissors From BackPack");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getName().equals("Scissors"))
        {
            System.out.println("Not A Scissors");
            return;
        }
        else {

            if (item.item.isBroken()){
                System.out.println("It is Broken !");
                return;
            }
            TypesOfSkinProduct typesOfSkinProduct = new TypesOfSkinProduct();
            typesOfSkinProduct.setTypes();
            for (int i = 0; i < typesOfSkinProduct.types.size(); i++){
                if (typesOfSkinProduct.types.get(i).getName().equals("Wool")){
                    if (game.player.backpack.put(typesOfSkinProduct.types.get(i), 1)){
                        canBeSheered = false;
                        daysToGetSheered = 3;
                        break;
                    }
                    else {
                        System.out.println("No Room In Backpack!");
                        return;
                    }
                }
            }
        }
    }

    @Override
    public String toString(int i) {
        return "Sheep : ";
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Status");
        myMenu.options.add("Feed this Sheep");
        myMenu.options.add("Heal this Sheep");
        myMenu.options.add("Sheer this Sheep");

        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        backAfterChoose = false;
        if (numOfMenu ==0){
            if (numThatChosenFromMenu == 1){
                showStatus();
            }
            else if (numThatChosenFromMenu == 2){
                feed(game);
            }
            else if (numThatChosenFromMenu == 3){
                heal(game);
            }
            else if (numThatChosenFromMenu == 4){
                sheer(game);
            }
        }
    }

    public int getDaysToGetSheered() {
        return daysToGetSheered;
    }

    public void setDaysToGetSheered(int daysToGetSheered) {
        this.daysToGetSheered = daysToGetSheered;
    }
}
class Village extends Location{
    Market market;
    Laboratory laboratory;
    Clinic clinic;
    Gym gym;
    Ranch ranch;
    Cafe cafe;
    Workshop workshop;

    Village(Game game){
        market = new Market(this);
        laboratory = new Laboratory(this);
        clinic = new Clinic(this);
        gym = new Gym(this);
        ranch = new Ranch(this);
        cafe = new Cafe(this);
        workshop = new Workshop(this);

        goToLocations.add(game.farm);
//        goToLocations.add(game.jungle);
        goToLocations.add(market);
        goToLocations.add(laboratory);
        goToLocations.add(clinic);
        goToLocations.add(gym);
        goToLocations.add(cafe);
        goToLocations.add(workshop);
        goToLocations.add(ranch);

        name = "Village";
    }

}

class Workshop extends Location{
    private int openingTime;
    private int closingTime;
    private int chosenAction;
    ArrayList<Tool> types;

    Workshop(Village village){
        name = "Workshop";
        goToLocations.add(village);
        inspectable = false;
    }
    public void makeTool(Tool tool){

    }

    public void repairTool(Tool tool){

    }

    public void disassembleTool(Tool tool){

    }

    public void showStatus(){

    }

    public int getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(int openingTime) {
        this.openingTime = openingTime;
    }

    public int getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(int closingTime) {
        this.closingTime = closingTime;
    }

    @Override
    public void entrance(Game game) {
        involvedMenuNum = 0;
        setMyMenus(game);
        game.menuStack.push(this);
        System.out.println(game.menuStack.getFirst().toString(0));
        game.menuStack.getFirst().myMenus.get(game.menuStack.getFirst().involvedMenuNum).printMenu();
    }

    /* menu 0:
    1.Check this shop
    2.Make a tool
    3.Repair a tool
    4.Disassemble a tool
       menu 1:
    1.Item 1
    2.Item 2
     */
    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();

        TypesOfExploreTool exploreToolLib = new TypesOfExploreTool();
        exploreToolLib.setTypes();
        TypesOfKitchenTool kitchenToolLib = new TypesOfKitchenTool();
        kitchenToolLib.setTypes();
        TypesOfFishingTool fishingToolLib = new TypesOfFishingTool();
        fishingToolLib.setTypes();
        TypesOfWateringTool wateringToolLib = new TypesOfWateringTool();
        wateringToolLib.setTypes();
        TypesOfTool typesOfTool = new TypesOfTool();
        typesOfTool.setTypes();

        types = new ArrayList<Tool>();

        for(int i = 0 ; i < exploreToolLib.types.size() ; i++){
            types.add(exploreToolLib.types.get(i));
        }
        for(int i = 0 ; i < kitchenToolLib.types.size() ; i++){
            types.add(kitchenToolLib.types.get(i));
        }
        for(int i = 0 ; i < fishingToolLib.types.size() ; i++){
            types.add(kitchenToolLib.types.get(i));
        }
        for(int i = 0 ; i < wateringToolLib.types.size() ; i++){
            types.add(wateringToolLib.types.get(i));
        }
        for (int i = 0; i < typesOfTool.types.size(); i++){
            types.add(typesOfTool.types.get(i));
        }

        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Check this shop");
        myMenu.options.add("Make a tool");
        myMenu.options.add("Repair a tool");
        myMenu.options.add("Disassemble a tool");

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        for(int i = 0 ; i < types.size() ; i++){
            myMenu.options.add(types.get(i).getName() +  " " +
                    " " + types.get(i).getKind() +
                    " " + (types.get(i) instanceof WateringTool ? "Watering Tool":""));
        }

        myMenus.add(myMenu);


    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        Scanner scanner = new Scanner(System.in);
        String response = new String();
        switch (numOfMenu) {
            case 0:
                switch (numThatChosenFromMenu) {
                    case 1:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 1;
                        break;
                    case 2:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 2;
                        break;
                    case 3: {
                        backAfterChoose = true;
                        System.out.println("Choose am item from backpack!");
                        game.player.backpack.showItems();
                        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
                        if (item == null)
                            return;
                        if (!item.item.getClass().equals(Tool.class) && !item.item.getClass().equals(ExploreTool.class) && !item.item.getClass().equals((WateringTool.class)) && !item.item.getClass().equals(KitchenTool.class)) {
                            System.out.println("Not A Tool");
                            return;
                        } else {
                            if (item.item.isBroken()) {
                                Tool tool = Tool.class.cast(item.item);
                                System.out.println("Demanding Money :");
                                System.out.println(tool.getDemandingMoneyToRepair());
                                System.out.println("Demanding Items :");
                                for (int i = 0; i < tool.getDemandingRawMaterialsToRepair().size(); i++) {
                                    System.out.println((i + 1) + ". " + tool.getDemandingRawMaterialsToRepair().get(i).getName()
                                            + " x" + item.getNumber());
                                }
                                System.out.println("Would you repair? (Y/N)");
                                response = scanner.next();
                                while (!(response.equals("Y") || response.equals("N"))) {
                                    System.out.println("Invalid Response");
                                    response = scanner.next();
                                }
                                if (response.equals("Y")) {
                                    ArrayList<SavingItem> demandingItems = new ArrayList<>();
                                    ArrayList<Integer> demandingNumber = new ArrayList<>();
                                    boolean havingAllNeedings = true;
                                    if (game.player.getMoney() >= tool.getDemandingMoneyToRepair()) {
                                        for (int i = 0; i < tool.getDemandingRawMaterialsToRepair().size(); i++) {
                                            System.out.println("Choose " + tool.getDemandingRawMaterialsToRepair().get(i).getName()
                                                    + " from backpack!");
                                            game.player.backpack.showItems();
                                            SavingItem choosingItem = game.player.backpack.selectAnItem(scanner.nextInt());
                                            if (choosingItem == null) {
                                                return;
                                            }
                                            if (true) {
                                                if (checkQuality(choosingItem , tool.getDemandingRawMaterialsToRepair().get(i)) >= 1) {
                                                    demandingItems.add(choosingItem);
                                                    demandingNumber.add(tool.getDemandingRawMaterialsToRepair().get(i).getNumber() * checkQuality(choosingItem , tool.getDemandingRawMaterialsToRepair().get(i)));
                                                    System.out.println("Ok");
                                                } else {
                                                    System.out.println("You don't have enough " +
                                                            tool.getDemandingRawMaterialsToRepair().get(i).getName());
                                                    involvedMenuNum--;
                                                    havingAllNeedings = false;
                                                    break;
                                                }
                                            }
                                        }
                                        if (havingAllNeedings) {
                                            for (int i = 0; i < demandingItems.size(); i++) {
                                                game.player.backpack.take(demandingItems.get(i).item, demandingNumber.get(i));
                                            }
                                            item.item.setBroken(false);
                                            System.out.println("It's repaired");
                                        }
                                    } else {
                                        System.out.println("You don't have enough money!");
                                    }
                                }
                            } else {
                                System.out.println("It is't broken!!");
                            }
                        }
                        break;
                    }
                    case 4: {
                        backAfterChoose = true;
                        System.out.println("Choose am item from backpack!");
                        game.player.backpack.showItems();
                        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
                        ArrayList<RawMaterial> rawMaterialTypes = new ArrayList<>();

                        TypesOfSkinProduct typesOfSkinProduct = new TypesOfSkinProduct();
                        typesOfSkinProduct.setTypes();
                        for (int i = 0; i < typesOfSkinProduct.types.size(); i++) {
                            rawMaterialTypes.add(typesOfSkinProduct.types.get(i));
                        }

                        TypesOfThread typesOfThread = new TypesOfThread();
                        typesOfThread.setTypes();
                        for (int i = 0; i < typesOfThread.types.size(); i++) {
                            rawMaterialTypes.add(typesOfThread.types.get(i));
                        }

                        TypesOfWood typesOfWood = new TypesOfWood();
                        typesOfWood.setTypes();
                        for (int i = 0; i < typesOfWood.types.size(); i++) {
                            rawMaterialTypes.add(typesOfWood.types.get(i));
                        }

                        TypesOfStone typesOfStone = new TypesOfStone();
                        typesOfStone.setTypes();
                        for (int i = 0; i < typesOfStone.types.size(); i++) {
                            rawMaterialTypes.add(typesOfStone.types.get(i));
                        }

                        if (item == null)
                            return;
                        if (!item.item.getClass().equals(Tool.class) && !item.item.getClass().equals(ExploreTool.class) && !item.item.getClass().equals((WateringTool.class)) && !item.item.getClass().equals(KitchenTool.class)) {
                            System.out.println("Not A Tool");
                        } else {
                            Tool tool = Tool.class.cast(item.item);
                            System.out.println("Demanding Money :");
                            System.out.println(tool.getDemandingMoneyToRepair());
                            System.out.println("Would you disassemble? (Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                if (tool.getDemandingMoneyToRepair() <= game.player.getMoney()) {
                                    boolean haveSpace = true;
                                    if (tool.isBroken()) {
                                        for (int i = 0; i < tool.demandingRawMaterialsToDisassemble.size(); i++) {
                                            for (int j = 0; j < rawMaterialTypes.size(); j++) {
                                                if (rawMaterialTypes.get(j).getName().equals
                                                        (tool.demandingRawMaterialsToDisassemble.get(i).getName())) {
                                                    haveSpace &= game.player.backpack.checkToPut(rawMaterialTypes.get(j),
                                                            tool.demandingRawMaterialsToDisassemble.get(i).getNumber());
                                                    break;
                                                }
                                            }
                                        }
                                        if(haveSpace){
                                            for (int i = 0; i < tool.demandingRawMaterialsToDisassemble.size(); i++) {
                                                for (int j = 0; j < rawMaterialTypes.size(); j++) {
                                                    if (rawMaterialTypes.get(j).getName().equals
                                                            (tool.demandingRawMaterialsToDisassemble.get(i).getName())) {
                                                        game.player.backpack.put(rawMaterialTypes.get(j),
                                                                tool.demandingRawMaterialsToDisassemble.get(i).getNumber());
                                                        break;
                                                    }
                                                }
                                            }
                                            game.player.backpack.take(tool, 1);
                                            System.out.println("Disassembled");
                                        }
                                        else {
                                            System.out.println("Backpack is full");
                                        }
                                    } else if (!tool.isBroken()) {
                                        for (int i = 0; i < tool.demandingRawMaterialsToBuild.size(); i++) {
                                            for (int j = 0; j < rawMaterialTypes.size(); j++) {
                                                if (rawMaterialTypes.get(j).getName().equals
                                                        (tool.demandingRawMaterialsToBuild.get(i).getName())) {
                                                    haveSpace &= game.player.backpack.checkToPut(rawMaterialTypes.get(j),
                                                            tool.demandingRawMaterialsToBuild.get(i).getNumber());
                                                    break;
                                                }
                                            }

                                        }
                                        if(haveSpace){
                                            for (int i = 0; i < tool.demandingRawMaterialsToBuild.size(); i++) {
                                                for (int j = 0; j < rawMaterialTypes.size(); j++) {
                                                    if (rawMaterialTypes.get(j).getName().equals
                                                            (tool.demandingRawMaterialsToBuild.get(i).getName())) {
                                                        game.player.backpack.put(rawMaterialTypes.get(j),
                                                                tool.demandingRawMaterialsToBuild.get(i).getNumber());
                                                        break;
                                                    }
                                                }
                                            }
                                            game.player.backpack.take(tool, 1);
                                            System.out.println("Disassembled");
                                        }
                                        else {
                                            System.out.println("Backpack is full");
                                        }
                                    }
                                } else {
                                    System.out.println("You Don't have enough money");
                                }
                            }
                        }
                    }
                }
                if (numThatChosenFromMenu > 4)
                    System.out.println("Invalid Choice");
                break;
            case 1:
                if (numThatChosenFromMenu <= types.size()){
                    switch (chosenAction) {
                        case 1:
                            backAfterChoose = true;
                            types.get(numThatChosenFromMenu - 1).showStatus();
                            break;
                        case 2:
                            backAfterChoose = false;
////                            System.out.println("Choose am item from backpack!");
////                            game.player.backpack.showItems();
////                            SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
////                            if (item == null)
////                                return;
//                            if (!item.item.getClass().equals(Tool.class)) {
//                                System.out.println("Not A Tool");
//                                return;
//                            } else {
//                                if (item.item.isBroken()) {
                            Tool tool = types.get(numThatChosenFromMenu - 1);
                            System.out.println("Demanding Money :");
                            System.out.println(tool.getDemandingMoneyToBuild());
                            System.out.println("Demanding Items :");
                            for (int i = 0; i < tool.getDemandingRawMaterialToBuild().size(); i++) {
                                System.out.println((i + 1) + ". " + tool.getDemandingRawMaterialToBuild().get(i).getName()
                                        + " x" + tool.demandingRawMaterialsToBuild.get(i).getNumber());
                            }
                            System.out.println("Would you buy? (Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                ArrayList<SavingItem> demandingItems = new ArrayList<>();
                                ArrayList<Integer> demandingNumber = new ArrayList<>();
                                boolean havingAllNeedings = true;
                                if (game.player.getMoney() >= tool.getDemandingMoneyToBuild()) {
                                    for (int i = 0; i < tool.getDemandingRawMaterialToBuild().size(); i++) {
                                        System.out.println("Choose " + tool.getDemandingRawMaterialToBuild().get(i).getName()
                                                + " from backpack!");
                                        game.player.backpack.showItems();
                                        SavingItem choosingItem = game.player.backpack.selectAnItem(scanner.nextInt());
                                        if (choosingItem == null) {
                                            return;
                                        }
//                                        System.out.println(choosingItem.item.getKind() +"%%%"+ tool.getDemandingRawMaterialToBuild().get(i).getKind());
                                        if (choosingItem.item.getKind().equals(tool.getDemandingRawMaterialToBuild().get(i).getKind())) {
//                                            System.out.println("Kind Match");
                                            if (checkQuality(choosingItem , tool.getDemandingRawMaterialToBuild().get(i)) >= 1) {
                                                demandingItems.add(choosingItem);
                                                demandingNumber.add(tool.getDemandingRawMaterialsToRepair().get(i).getNumber() * checkQuality(choosingItem , tool.getDemandingRawMaterialToBuild().get(i)));
                                                System.out.println("Ok");
                                            } else {
                                                System.out.println("You don't have enough " +
                                                        tool.getDemandingRawMaterialToBuild().get(i).getName());
                                                involvedMenuNum--;
                                                havingAllNeedings = false;
                                                break;
                                            }
                                        } else {
                                            System.out.println("You don't have enough " +
                                                    tool.getDemandingRawMaterialToBuild().get(i).getName());
                                            involvedMenuNum--;
                                            havingAllNeedings = false;
                                            break;
                                        }
                                    }
                                    if (havingAllNeedings) {
                                        if (game.player.backpack.put(tool, 1)) {
                                            for (int i = 0; i < demandingItems.size(); i++) {
                                                game.player.backpack.take(demandingItems.get(i).item, demandingNumber.get(i));
                                            }
                                        } else {
                                            System.out.println("Backpack is full!");
                                        }
                                    }
                                } else {
                                    System.out.println("You don't have enough money!");
                                }

                            }
                    }
                    break;
                }
                else {
                    System.out.println("Invalid Choice");
                }

        }
    }

    @Override
    public String toString(int i) {
        switch (i){
            case 0:
                return "Workshop :";
            case 1:
                return "Which item dou you want?";
        }
        return null;
    }

    public int checkQuality(SavingItem savingItem , DemandingRawMaterial demandingRawMaterial){
//        TypesOfWood typesOfWood = new TypesOfWood();
//        TypesOfSkinProduct typesOfSkinProduct = new TypesOfSkinProduct();
//        TypesOfStone typesOfStone = new TypesOfStone();
//        TypesOfThread typesOfThread = new TypesOfThread();
//
//        typesOfSkinProduct.setTypes();
//        typesOfStone.setTypes();
//        typesOfThread.setTypes();
//        typesOfWood.setTypes();

//        for (int i = 0; i < typesOfSkinProduct.types.size(); i++){
//            if (typesOfSkinProduct.types.get(i).getName().equals(demandingRawMaterial.getName())){
//                found = true;
//                rawMaterial = typesOfSkinProduct.types.get(i);
//            }
//        }
//
//        if (rawMaterial.getKind())
        RawMaterial item = RawMaterial.class.cast(savingItem.item);
        if (item.getQuality() >= demandingRawMaterial.getQuality()){
            int n = item.getQuality() - demandingRawMaterial.getQuality();
            double m = Math.pow(3., n);
            n = m == 0 ? 1:(int) m;
            if (n <= savingItem.getNumber())
                return n;
            return -1;
        }
        else
            return  -1;
    }
}
class Laboratory extends Location{
    int openingTime;
    int closingTime;

    ArrayList<Machine> allMachines = new ArrayList<>();
    ArrayList<Machine> boughtableMachines = new ArrayList<>();

    Laboratory(Village village){
        name = "Laboratory";
        goToLocations.add(village);
        TypesOfMachine tyepsOfMachine = new TypesOfMachine();
        tyepsOfMachine.setTypes();

        allMachines = tyepsOfMachine.types;
    }


    public void showStatus(){

    }

    public void buildMachine(Machine machine){

    }

    public void checkMachines(){

    }

    @Override
    public void entrance(Game game) {
        involvedMenuNum = 0;
        setMyMenus(game);
        game.menuStack.push(this);
        System.out.println(game.menuStack.getFirst().toString(0));
        game.menuStack.getFirst().myMenus.get(game.menuStack.getFirst().involvedMenuNum).printMenu();
    }

    @Override
    public void setMyMenus(Game game) {
        boughtableMachines.clear();
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Check machines");
        myMenu.options.add("Build a machine");

        myMenus.add(myMenu);

        myMenu = new MyMenu();
        for (int i = 0; i < allMachines.size(); i++){
            if (!allMachines.get(i).isBought()){
                myMenu.options.add(allMachines.get(i).getName());
                boughtableMachines.add(allMachines.get(i));
            }
        }
        myMenus.add(myMenu);

        myMenu = new MyMenu();
        boughtableMachines.clear();
        for (int i = 0; i < allMachines.size(); i++){
            if (!allMachines.get(i).isBought()){
                myMenu.options.add(allMachines.get(i).getName());
                boughtableMachines.add(allMachines.get(i));
            }
        }
        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int num, Game game) {
        if (num == 0){
            if (numThatChosenFromMenu == 1){
                backAfterChoose = false;
                involvedMenuNum++;
            }
            else if (numThatChosenFromMenu == 2){
                backAfterChoose = false;
                involvedMenuNum += 2;
            }
        }

        else if (num == 1){
            if (numThatChosenFromMenu <= boughtableMachines.size())
            {
                backAfterChoose = false;
                boughtableMachines.get(numThatChosenFromMenu - 1).showStatus();

//                backAfterChoose = false;
//                boughtableMachines.get(numThatChosenFromMenu - 1).setMyMenus();
//                game.menuStack.push(boughtableMachines.get(numThatChosenFromMenu - 1));
            }

        }

        else if (num == 2){
            System.out.println(boughtableMachines.size());
            if (numThatChosenFromMenu <= boughtableMachines.size()){
                backAfterChoose = false;
                boughtableMachines.get(numThatChosenFromMenu - 1).buy(game);
            }
//                backAfterChoose = false;
//                boughtableMachines.get(numThatChosenFromMenu - 1).setMyMenus();
//                game.menuStack.push(boughtableMachines.get(numThatChosenFromMenu - 1));
//            }
        }

    }

    @Override
    public void reduceInvolvedMenuNum() {
        if (involvedMenuNum == 1){
            involvedMenuNum--;
        }
        else if (involvedMenuNum == 2){
            involvedMenuNum -= 2;
        }
    }

    @Override
    public String toString(int i) {
        if (i == 0)
            return "Lab : ";
        else
            return "Machines : ";
    }
}

class Clinic extends Location{
    private int openingTime;
    private int closingTime;
    private int chosenAction;
    private int healUpCost = 500;
    TypesOfDrug drugLib = new TypesOfDrug();

    Clinic(Village village){
        name = "Clinic";
        drugLib.setTypes();
        goToLocations.add(village);
    }

    public void showStatus(){

    }
    public void buy(Drug drug){

    }
    public void healUp(int increasingAmount){

    }

    @Override
    public void entrance(Game game) {
        involvedMenuNum = 0;
        setMyMenus(game);
        game.menuStack.push(this);
        System.out.println(game.menuStack.getFirst().toString(0));
        game.menuStack.getFirst().myMenus.get(game.menuStack.getFirst().involvedMenuNum).printMenu();
    }

    @Override
    public String toString(int i) {
        if(i == 0) return "Clinic :";
        if(i == 1) return "Which item do you want?";
        return null;
    }

    /*
      menu 0:
    1.Check this shop
    2.Buy an item
    3.Sell an item

      menu 1:
    1.drug 1
    2.drug 2
    */

    @Override
    public void setMyMenus(Game game) {
        TypesOfDrug drugLib = new TypesOfDrug();
        drugLib.setTypes();

        myMenus.clear();

        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Check this shop");
        myMenu.options.add("Buy an item");
        myMenu.options.add("Heal Up");

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        for(int i = 0 ; i < drugLib.types.size() ; i++){
            myMenu.options.add(drugLib.types.get(i).getName() + " Drug");
        }

        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        Scanner scanner = new Scanner(System.in);
        int num = 0;
        String response = new String();
        switch (numOfMenu){
            case 0:
                switch (numThatChosenFromMenu){
                    case 1:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 1;
                        break;
                    case 2:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 2;
                        break;
                    case 3:
                        backAfterChoose = false;
                        System.out.println("You Will Heal up for " + healUpCost + " Gill. Is this okay? (Y/N)");
                        response = scanner.next();
                        while(!( response.equals("Y") || response.equals("N"))){
                            System.out.println("Invalid Response");
                            response = scanner.next();
                        }
                        if(response.equals("Y")) {
                            if(game.player.getMoney() >= healUpCost)
                                game.player.getHealth().increaseCurrentAmount(500);
                            else
                                System.out.println("You don't have enough money!");
                        }
                        break;
                }
                if(numThatChosenFromMenu > 3)
                    System.out.println("Invalid Choice");
                break;
            case 1:
                if(numThatChosenFromMenu <= drugLib.types.size()) {
                    switch(chosenAction){
                        case 1:
                            drugLib.types.get(numThatChosenFromMenu - 1).showDescription();
                            break;
                        case 2:
                            System.out.println("How many drugs do you want to buy?");
                            num = scanner.nextInt();
                            System.out.println("You will buy " + drugLib.types.get(numThatChosenFromMenu - 1).getName()
                                    + " x" + num + " for " + num * drugLib.types.get(numThatChosenFromMenu - 1).getBuyingCost()
                                    + " Gil. Is this okay? (Y/N)");
                            response = scanner.next(); // there is a little problem!
                            while(!( response.equals("Y") || response.equals("N"))){
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if(response.equals("Y")) {
                                if(game.player.getMoney() >= num * drugLib.types.get(numThatChosenFromMenu - 1).getBuyingCost()) {
                                    if (game.player.backpack.put(drugLib.types.get(numThatChosenFromMenu - 1), num) && true) {
                                        game.player.decreseMoney(num * drugLib.types.get(numThatChosenFromMenu - 1).getBuyingCost());
                                    } else {
                                        System.out.println("Backpack is Full !");
                                    }
                                }
                                else {
                                    System.out.println("You don't have enough money");
                                }
                            }
                            break;
                    }
                }
                else {
                    System.out.println("Invalid Choice");
                }
        }
    }
}


class Gym extends Location{
    private int energyCostHealthRefill;
    private int energyCostEnergyRefill;
    private int energyCostHealthConsumption;
    private int energyCostEnergyConsumption;
    private int energyCostHealthMax;
    private int energyCostEnergyMax;
    private int costForHealthRefill = 500;
    private int costForEnergyRefill = 500;
    private int costForHealthConsumption = 500;
    private int costForEnergyConsumption = 500;
    private int costForHealthMax = 500;
    private int costForEnergyMax = 500;
    private int increasingHealthRefillAmount = 50;
    private int increasingHealthConsumptionAmount = 50;
    private int increasingHealthMaxAmount = 50;
    private int increasingEnergyRefillAmount = 50;
    private int increasingEnergyConsumptionAmount = 50;
    private int increasingEnergyMaxAmount = 50;
    private int chosenAbility;
    private int chosenProperty;

    Gym(Village village){
        name = "Gym";
        goToLocations.add(village);

    }

    @Override
    public void entrance(Game game) {
        involvedMenuNum = 0;
        setMyMenus(game);
        game.menuStack.push(this);
        System.out.println(game.menuStack.getFirst().toString(0));
        game.menuStack.getFirst().myMenus.get(game.menuStack.getFirst().involvedMenuNum).printMenu();
    }

    /*
    menu 0:
        1.Health
        2.Energy
    menu 1:
        1.Consumption rate
        2.ReFill rate
        3.Max amount
    menu 2:
        1.Status
        2.Train
     */
    @Override
    public String toString(int i) {
        switch (i){
            case 0:
                return "Gym : Which ability would you like to improve?";
            case 1:
                return "Gym : Which property would you like to improve?";
            case 2:
                return "Gym : ";
        }
        return null;
    }

    public void train(Game game){

        switch (chosenAbility) {
            case 1:
                switch (chosenProperty) {
                    case 1:
                        if (lastQuestion(costForHealthConsumption, energyCostHealthConsumption , game)) {
                            if (game.player.getHealth().getConsumptionRate() > 50) {
                                game.player.decreseMoney(costForHealthConsumption);
                                game.player.getHealth().decreaseConsumptionRate(increasingHealthConsumptionAmount);
                                game.player.getEnergy().decreaseCurrentAmount(game, energyCostHealthConsumption);
                            }
                        }
                        break;
                    case 2:
                        if (lastQuestion(costForHealthRefill, energyCostHealthRefill , game)) {
                            if (game.player.getHealth().getRefillRate() < 200) {
                                game.player.decreseMoney(costForHealthRefill);
                                game.player.getHealth().increaseRefillRate(increasingHealthRefillAmount);
                                game.player.getEnergy().decreaseCurrentAmount(game, energyCostHealthRefill);
                            }
                        }
                        break;
                    case 3:
                        if (lastQuestion(costForHealthMax, energyCostHealthMax , game)) {
                            if (game.player.getHealth().getMaxAmount() < 1000) {
                                game.player.decreseMoney(costForHealthMax);
                                game.player.getHealth().increaseMaxAmount(increasingHealthMaxAmount);
                                game.player.getEnergy().decreaseCurrentAmount(game, energyCostHealthMax);
                            }
                        }
                        break;
                }
                break;
            case 2:
                switch (chosenProperty){
                    case 1:
                        if (lastQuestion(costForEnergyConsumption, energyCostEnergyConsumption, game)) {
                            if (game.player.getEnergy().getConsumptionRate() > 50) {
                                game.player.decreseMoney(costForEnergyConsumption);
                                game.player.getEnergy().decreaseConsumptionRate(increasingEnergyConsumptionAmount);
                                game.player.getEnergy().decreaseCurrentAmount(game, energyCostEnergyConsumption);
                            }
                        }
                        break;
                    case 2:
                        if (lastQuestion(costForEnergyRefill, energyCostEnergyRefill ,game)) {
                            if (game.player.getEnergy().getRefillRate() < 200) {
                                game.player.decreseMoney(costForEnergyRefill);
                                game.player.getEnergy().increaseRefillRate(increasingEnergyRefillAmount);
                                game.player.getEnergy().decreaseCurrentAmount(game, energyCostHealthRefill);
                            }
                        }
                        break;
                    case 3:
                        if (lastQuestion(costForEnergyMax, energyCostEnergyMax , game)) {
                            if (game.player.getEnergy().getMaxAmount() < 1000) {
                                game.player.decreseMoney(costForEnergyMax);
                                game.player.getEnergy().increaseMaxAmount(increasingEnergyMaxAmount);
                                game.player.getEnergy().decreaseCurrentAmount(game, energyCostEnergyMax);
                            }
                        }
                        break;
                }
                break;
        }
    }

    public boolean lastQuestion(int cost , int energyCost , Game game){
        System.out.println("This will cost you " + cost + " Gil. Is This okay? (Y/N)");

        Scanner scanner = new Scanner(System.in);
        String response = scanner.next();

        while(!( response.equals("Y") || response.equals("N"))){
            System.out.println("Invalid Response");
            response = scanner.next();
        }

        if(response.equals("Y") && (game.player.getMoney() >= cost)  &&
                (game.player.getEnergy().getCurrentAmount() >= energyCost )){
            return true;
        }
        else {
            return false;
        }
    }

    public void status(Game game){
        switch (chosenAbility) {
            case 1:
                switch (chosenProperty) {
                    case 1:
                        System.out.println("Health Consumption Rate :");
                        System.out.println("Amount : " + game.player.getHealth().getConsumptionRate());
                        System.out.println("IncreasingAmount : "  + increasingHealthConsumptionAmount);
                        System.out.println("EnergyCost : " + energyCostHealthConsumption);
                        System.out.println("Cost : " + costForHealthConsumption);
                        System.out.println();
                        break;
                    case 2:
                        System.out.println("Health Refill Rate :");
                        System.out.println("Amount : " + game.player.getHealth().getRefillRate());
                        System.out.println("IncreasingAmount : " + increasingHealthRefillAmount);
                        System.out.println("EnergyCost : " + energyCostHealthRefill);
                        System.out.println("Cost : " + costForHealthRefill);
                        System.out.println();
                        break;
                    case 3:
                        System.out.println("Health Max Amount :");
                        System.out.println("Amount : " + game.player.getHealth().getMaxAmount());
                        System.out.println("IncreasingAmount : " + increasingHealthMaxAmount);
                        System.out.println("EnergyCost : " + energyCostHealthMax);
                        System.out.println("Cost : " + costForHealthMax);
                        System.out.println();
                        break;
                }
                break;
            case 2:
                switch (chosenProperty){
                    case 1:
                        System.out.println("Energy Consumption Rate :");
                        System.out.println("Amount : " + game.player.getEnergy().getConsumptionRate());
                        System.out.println("IncreasingAmount : " + increasingEnergyConsumptionAmount);
                        System.out.println("EnergyCost : " + energyCostEnergyConsumption);
                        System.out.println("Cost : " + costForEnergyConsumption);
                        System.out.println();
                        break;
                    case 2:
                        System.out.println("Energy Refill Rate :");
                        System.out.println("Amount : " + game.player.getEnergy().getRefillRate());
                        System.out.println("IncreasingAmount : " + increasingEnergyRefillAmount);
                        System.out.println("EnergyCost : " + energyCostEnergyRefill);
                        System.out.println("Cost : " + costForEnergyRefill);
                        System.out.println();
                        break;
                    case 3:
                        System.out.println("Energy MAx Amount :");
                        System.out.println("Amount : " + game.player.getEnergy().getMaxAmount());
                        System.out.println("IncreasingAmount : " + increasingEnergyMaxAmount);
                        System.out.println("EnergyCost : " + energyCostEnergyMax);
                        System.out.println("Cost : " + costForEnergyMax);
                        System.out.println();
                        break;
                }
                break;
        }
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        switch (numOfMenu){
            case 0:
                backAfterChoose = false;
                this.involvedMenuNum++;
//                System.out.println(toString(involvedMenuNum));
//                myMenus.get(involvedMenuNum).printMenu();
                chosenAbility = numThatChosenFromMenu;
                break;
            case 1:
                backAfterChoose = false;
                this.involvedMenuNum++;
//                System.out.println(toString(involvedMenuNum));
//                myMenus.get(involvedMenuNum).printMenu();
                chosenProperty = numThatChosenFromMenu;
                break;
            case 2:
                backAfterChoose = true;
                this.involvedMenuNum++;
                switch (numThatChosenFromMenu){
                    case 1:
                        status(game);
                        break;
                    case 2:
                        train(game);
                        break;
                }
                break;
        }
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Health");
        myMenu.options.add("Energy");

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        myMenu.options.add("Consumption rate");
        myMenu.options.add("Refill rate");
        myMenu.options.add("Max amount");

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        myMenu.options.add("Status");
        myMenu.options.add("Train");

        myMenus.add(myMenu);
    }
}

class Ranch extends Location{
    TypesOfAnimalFood foodLib;
    TypesOfAnimalMedicine medicineLib;
    private int chosenAction; // 1 -> check this shop -- 2 -> buy an item
    Ranch(Village village){
        name = "Ranch";
        goToLocations.add(village);
    }

    @Override
    public String toString(int i) {
        switch (i){
            case 0:
                return "Ranch :";
            case 1:
                return "Which item do you want?";
            case 2:
                return "Which animal do you want to buy?";
        }
        return null;
    }

    /*  menu 0:
    1.Check this shop
    2.Buy an item
    3.Buy an animal
        menu 1:
    1.item 1
    2.item 2
    ...
        menu 2:
    1.animal 1
    2.animal 2
    ...
     */

    @Override
    public void entrance(Game game) {
        involvedMenuNum = 0;
        setMyMenus(game);
        game.menuStack.push(this);
        System.out.println(game.menuStack.getFirst().toString(0));
        game.menuStack.getFirst().myMenus.get(game.menuStack.getFirst().involvedMenuNum).printMenu();
    }

    @Override
    public void setMyMenus(Game game) {
        foodLib = new TypesOfAnimalFood();
        foodLib.setTypes();
        medicineLib = new TypesOfAnimalMedicine();
        medicineLib.setTypes();

        myMenus.clear();

        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Check this shop");
        myMenu.options.add("Buy an item");
        myMenu.options.add("Buy an animal");

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        for(int i = 0 ; i < foodLib.types.size() ; i++) {
            myMenu.options.add(foodLib.types.get(i).getName());
        }

        for(int i = 0 ; i < medicineLib.types.size() ; i++) {
            myMenu.options.add(medicineLib.types.get(i).getName());
        }

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        myMenu.options.add("Cow");
        myMenu.options.add("Sheep");
        myMenu.options.add("Hen");

        myMenus.add(myMenu);
    }

    @Override
    public void reduceInvolvedMenuNum() {
        if(involvedMenuNum == 1 || involvedMenuNum == 2)
            involvedMenuNum = 0;
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu , Game game) {
        int num;
        String response = new String();
        Scanner scanner = new Scanner(System.in);
        switch (numOfMenu){
            case 0:
                switch (numThatChosenFromMenu){
                    case 1:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 1;
                        break;
                    case 2:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 2;
                        break;
                    case 3:
                        backAfterChoose = false;
                        involvedMenuNum += 2;
                        break;
                    default:
                        System.out.println("Invalid Choice");
                }
                backAfterChoose = false;
                break;
            case 1:
                if(numThatChosenFromMenu <= foodLib.types.size() + medicineLib.types.size()) {
                    switch(chosenAction) {
                        case 1:
                            if(numThatChosenFromMenu <= foodLib.types.size())
                                foodLib.types.get(numThatChosenFromMenu - 1).showDescription();
                            else {
                                medicineLib.types.get(numThatChosenFromMenu - foodLib.types.size() - 1).showDescription();
                            }
                            break;
                        case 2:
                            System.out.println("How many items do you want to buy?");
                            num = scanner.nextInt();
                            if(numThatChosenFromMenu <= foodLib.types.size()) {
                                System.out.println("You will buy " + foodLib.types.get(numThatChosenFromMenu - 1).getName()
                                        + " x" + num + " for " + num * foodLib.types.get(numThatChosenFromMenu - 1).getBuyingCost()
                                        + " Gil. Is this okay? (Y/N)");
                                response = scanner.next(); // there is a little problem!
                                while (!(response.equals("Y") || response.equals("N"))) {
                                    System.out.println("Invalid Response");
                                    response = scanner.next();
                                }
                                if (response.equals("Y")) {
                                    if (game.player.getMoney() >= num * foodLib.types.get(numThatChosenFromMenu - 1).getBuyingCost()) {
                                        if (true && game.player.backpack.put(foodLib.types.get(numThatChosenFromMenu - 1), num)) {
                                            game.player.decreseMoney(num * foodLib.types.get(numThatChosenFromMenu - 1).getBuyingCost());
                                        } else {
                                            System.out.println("Backpack is Full !");
                                        }
                                    } else {
                                        System.out.println("You don't have enough money");
                                    }
                                }
                            }
                            else {
                                numThatChosenFromMenu = numThatChosenFromMenu - foodLib.types.size();
                                System.out.println("You will buy " + medicineLib.types.get(numThatChosenFromMenu - 1).getName()
                                        + " x" + num + " for " + num * medicineLib.types.get(numThatChosenFromMenu - 1).getBuyingCost()
                                        + " Gil. Is this okay? (Y/N)");
                                response = scanner.next(); // there is a little problem!
                                while (!(response.equals("Y") || response.equals("N"))) {
                                    System.out.println("Invalid Response");
                                    response = scanner.next();
                                }
                                if (response.equals("Y")) {
                                    if (game.player.getMoney() >= num * medicineLib.types.get(numThatChosenFromMenu - 1).getBuyingCost()) {
                                        if (game.player.backpack.put(medicineLib.types.get(numThatChosenFromMenu - 1), num)) {
                                            game.player.decreseMoney(num * medicineLib.types.get(numThatChosenFromMenu - 1).getBuyingCost());
                                        } else {
                                            System.out.println("Backpack is Full !");
                                        }
                                    } else {
                                        System.out.println("You don't have enough money");
                                    }
                                }
                            }
                            break;
                    }
                }
                else {
                    System.out.println("Invalid Choice");
                }
                backAfterChoose = true;
                break;
            case 2:
                backAfterChoose = true;
                switch (numThatChosenFromMenu){
                    case 1:
                        if(game.farm.barn.cowsPlace.cows.size() < game.farm.barn.cowsPlace.maxCow) {
                            Cow buyingCow = new Cow();
                            System.out.println("You will buy a Cow for " + Cow.getBuyingCost() + " Gill, is this okay? (Y/N)");
                            response = scanner.next(); // there is a little problem!
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                if(game.player.getMoney() >= Cow.getBuyingCost()) {
                                    game.player.decreseMoney(Cow.getBuyingCost());
                                    System.out.println("So what's cow's name?");
                                    buyingCow.setName(scanner.next());
                                    game.farm.barn.cowsPlace.cows.add(buyingCow);
                                } else {
                                    System.out.println("You don't have enough money");
                                }
                            }
                        }
                        else {
                            System.out.println("There is no room for anymore cow!");
                        }
                        break;
                    case 2:
                        if(game.farm.barn.shepPlace.shep.size() < game.farm.barn.shepPlace.maxShep) {
                            Sheep buyingSheep = new Sheep();
                            System.out.println("You will buy a Sheep for " + Sheep.getBuyingCost() + " Gill, is this okay? (Y/N)");
                            response = scanner.next(); // there is a little problem!
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                if(game.player.getMoney() >= Sheep.getBuyingCost()) {
                                    game.player.decreseMoney(Sheep.getBuyingCost());
                                    System.out.println("So what's sheep's name?");
                                    buyingSheep.setName(scanner.next());
                                    game.farm.barn.shepPlace.shep.add(buyingSheep);
                                } else {
                                    System.out.println("You don't have enough money");
                                }
                            }
                        }
                        else {
                            System.out.println("There is no room for anymore sheep!");
                        }
                        break;
                    case 3:
                        if(game.farm.barn.hensPlace.hens.size() < game.farm.barn.hensPlace.maxHen) {
                            Hen buyingHen = new Hen();
                            System.out.println("You will buy a Hen for " + Hen.getBuyingCost() + " Gill, is this okay? (Y/N)");
                            response = scanner.next(); // there is a little problem!
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                if(game.player.getMoney() >= Hen.getBuyingCost()) {
                                    game.player.decreseMoney(Hen.getBuyingCost());
                                    System.out.println("So what's sheep's name?");
                                    buyingHen.setName(scanner.next());
                                    game.farm.barn.hensPlace.hens.add(buyingHen);
                                } else {
                                    System.out.println("You don't have enough money");
                                }
                            }
                        }
                        else {
                            System.out.println("There is no room for anymore hen!");
                        }
                        break;
                }
                break;
        }
    }
}

class Cafe extends Location{
    MissionBoard misiionBoard = new MissionBoard();
    DiningTable diningTable = new DiningTable();

    Cafe(Village village){
        name = "Cafe";
        goToLocations.add(village);
        menuHavings.add(misiionBoard);
        menuHavings.add(diningTable);
    }
}

class Player{
    private String name;
    private int money;
    private int age;
    private Health health;
    private Energy energy;
    private Location location;
    Backpack backpack;

    public void healthDecrease(int amount){
        if (amount <= health.getCurrentAmount()){
            health.setCurrentAmount(health.getCurrentAmount() - amount);
            if (health.getCurrentAmount() > health.getMaxAmount()){
                health.setCurrentAmount(health.getMaxAmount());
            }
        }
        else health.setCurrentAmount(0);

        checkHealthAndEnergy();
    }
    public void checkHealthAndEnergy(){
        energy.setMaxAmount((energy.getMaxAmount()/2) + (energy.getMaxAmount()*health.getCurrentAmount())/(2*1000));
    }
    public void looseConscious(Game game){
        System.out.println("You Lost Yor Consciousness!");
        health.setCurrentAmount(health.getMaxAmount()/2);
        energy.setCurrentAmount(energy.getMaxAmount()/2);
        for (int i = 0; i < 7; i++){
            game.switchDay();
        }
    }

    public void switchDay(Game game, boolean badMood){
        if (!badMood){
            energy.setCurrentAmount(energy.getMaxAmount()/2 + energy.getMaxAmount()*health.getCurrentAmount()/1000);
        }
        else {
            energy.setCurrentAmount(energy.getMaxAmount() * 3/ 4);
            health.setCurrentAmount(health.getCurrentAmount() * 3 /4);
            System.out.println("You Were Made to Sleep An Entire Day !!");
        }
    }

    public boolean use(Game game, int loosingEnergy){
        checkHealthAndEnergy();
        if (energy.getCurrentAmount() >= loosingEnergy){
            energy.decreaseCurrentAmount(game, loosingEnergy);
            if (energy.getCurrentAmount() > energy.getMaxAmount())
                energy.setCurrentAmount(energy.getMaxAmount());
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void decreseMoney(int amount){
        money -= amount;
    }

    public void increeseMoney(int amount){
        money += money;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Health getHealth() {
        return health;
    }

    public void setHealth(Health health) {
        this.health = health;
    }

    public Energy getEnergy() {
        return energy;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public Location getLocation() {
        return location;
    }

    public boolean buy(int price)
    {
        if (price <= money)
        {
            money -= price;
            return true;
        }
        return false;
    }

    public void setLocation(Location location) {
        this.location = location;

//        this.location.showGoToLocations();
    }

    public void increaseMoney(int money) {

    }

    public void changeAge(){

    }

    public void goTo(){

    }

    public void showStats(){
        System.out.println("  " + name + " :");
        System.out.println("Money : " + money);
        System.out.println("Health : " + health.getCurrentAmount());
        System.out.println("Energy : " + energy.getCurrentAmount());
    }

    public void whereAmI(){
        location.showThisPlace();
        location.showGoToLocations();
        location.showInspectables();
    }

    public void changeLocation(String destinationName){
        boolean locationChanging = false;
        for(int i = 0 ; i < location.goToLocations.size() ; i++){
            if(location.goToLocations.get(i).getName().equals(destinationName)){
                location = location.goToLocations.get(i);
                locationChanging = true;
                System.out.println("You Have Entered " + location.name);
                break;
            }
        }
        if(!locationChanging)
            System.out.println("Invalid Target");
    }

    public MenuHaving inspect(String objectName){
        for(int i = 0; i < location.menuHavings.size() ; i++){
            if(location.menuHavings.get(i).getName().equals(objectName)){
//                location.menuHavings.get(i).inspect();
                return location.menuHavings.get(i);
            }
        }
        System.out.println("Invalid Command");
        return null;

    }

    Player(String name, int age, Game game){
        this.name = new String();
        setName(name);
        setAge(age);
        backpack = new Backpack(6);

        this.health = new Health(1000, 1000, 1,0);
        this.energy = new Energy(1000, 1000, 0, 1);
        setMoney(20000);
        setLocation(game.farm.house);
    }
}

abstract class Location extends MenuHaving{
    ArrayList<Location> goToLocations = new ArrayList<>();
    ArrayList<MenuHaving> menuHavings = new ArrayList<>();
    protected String name;

    public void showGoToLocations(){
        System.out.println("Places You Can Go :");
        for (int i = 0; i < goToLocations.size(); i++) {
            System.out.println((i + 1) + " : " + goToLocations.get(i).getName());
        }
    }

    public void showInspectables(){
        System.out.println("Objects You Can Inspect : ");
        for(int i = 0; i < menuHavings.size() ; i++){
            System.out.println((i + 1) + " : " + menuHavings.get(i).getName());
        }
    }

    @Override
    public void setMyMenus(Game game) {
    }

    @Override
    public String toString(int i) {
        return "mistake in location toString";
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int num, Game game) {
    }

    public void entrance(Game game){
    }

    public void showThisPlace(){
        System.out.println("You Are In " + name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void completeGoToLocations(ArrayList<Location> goToLocations) {
        for(int i = 0 ; i < goToLocations.size() ; i++){
            this.goToLocations.add(goToLocations.get(i));
        }
    }

//    public abstract void setGoToLocations(ArrayList<Location> goToLocations);
}

class Health{
    private int maxAmount = 1000;
    private int currentAmount = 1000;
    private int consumptionRate = 1;
    private int refillRate = 1;

    public void increaseMaxAmount(int amount){
        maxAmount += amount;
    }

    public void decreaseMaxAmount(int amount){
        maxAmount -= amount;
    }

    public void increaseCurrentAmount(int amount){
        currentAmount += amount;
        if(currentAmount > maxAmount)
            currentAmount = maxAmount;
    }

    public boolean decreaseCurrentAmount(int amount){
        if(currentAmount >= amount) {
            currentAmount -= amount;
            return true;
        }
        return false;
    }

    public void increaseConsumptionRate(int amount){
        consumptionRate += amount;
    }

    public void decreaseConsumptionRate(int amount){
        consumptionRate -= amount;
    }

    public void increaseRefillRate(int amount){
        refillRate += amount;
    }

    public void decreaseRefillRate(int amount){
        refillRate -= amount;
    }
    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int getConsumptionRate() {
        return consumptionRate;
    }

    public void setConsumptionRate(int consumptionRate) {
        this.consumptionRate = consumptionRate;
    }

    public int getRefillRate() {
        return refillRate;
    }

    public void setRefillRate(int refillRate) {
        this.refillRate = refillRate;
    }

    Health(int maxAmount, int currentAmount, int refillRate, int consumptionRate)
    {
        this.maxAmount = maxAmount;
        this.currentAmount = currentAmount;
        this.consumptionRate = consumptionRate;
        this.refillRate = refillRate;
    }
}

class Energy{
    private int maxAmount = 1000;
    private int currentAmount = 1000;
    private int consumptionRate = 1;
    private int refillRate = 1;

    public void increaseMaxAmount(int amount){
        maxAmount += amount;
    }

    public void decreaseMaxAmount(int amount){
        maxAmount -= amount;
    }

    public void increaseCurrentAmount(Game game, int amount){
        game.player.checkHealthAndEnergy();
        currentAmount += amount;
        if(currentAmount > maxAmount){
            currentAmount = maxAmount;
        }
    }

    public boolean decreaseCurrentAmount(Game game, int amount){
        game.player.checkHealthAndEnergy();
        if(currentAmount >= amount) {
            currentAmount -= amount;
            return true;
        }
        return false;
    }

    public void increaseConsumptionRate(int amount){
        consumptionRate += amount;
    }

    public void decreaseConsumptionRate(int amount){
        consumptionRate -= amount;
    }

    public void increaseRefillRate(int amount){
        refillRate += amount;
    }

    public void decreaseRefillRate(int amount){
        refillRate -= amount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int getConsumptionRate() {
        return consumptionRate;
    }

    public void setConsumptionRate(int consumptionRate) {
        this.consumptionRate = consumptionRate;
    }

    public int getRefillRate() {
        return refillRate;
    }

    public void setRefillRate(int refillRate) {
        this.refillRate = refillRate;
    }

    Energy(int maxAmount, int currentAmount, int consumptionRate, int refillRate){
        this.maxAmount = maxAmount;
        this.consumptionRate = consumptionRate;
        this.currentAmount = currentAmount;
        this.refillRate = refillRate;
    }

}

class AnimalFood extends Item{
    private String name;
    private int effectOnHealth;
    private int effectOnEnergy;
    ArrayList<String> animalsWhichCanEatIt = new ArrayList<>();
    private int buyingCost;
    private int sellingCost;

    public void showStatus(){
        System.out.println("Food for : ");
        for (int i = 0; i < animalsWhichCanEatIt.size(); i++){
            System.out.println(animalsWhichCanEatIt.get(i));
        }
    }
    public int getSellingCost() {
        return sellingCost;
    }

    public void setSellingCost(int sellingCost) {
        this.sellingCost = sellingCost;
    }

    @Override
    public String toString(int i) {
        return name;
    }

    public int getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }

    public int getEffectOnEnergy() {
        return effectOnEnergy;
    }

    public void setEffectOnEnergy(int effectOnEnergy) {
        this.effectOnEnergy = effectOnEnergy;
    }

    public int getEffectOnHealth() {
        return effectOnHealth;
    }

    public void setEffectOnHealth(int effectOnHealth) {
        this.effectOnHealth = effectOnHealth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    AnimalFood(String name, int effectOnHealth, int effectOnEnergy, int buyingCost){
        setName(name);
        setBuyingCost(buyingCost);
        setEffectOnEnergy(effectOnEnergy);
        setEffectOnHealth(effectOnHealth);
//        @Deprecated ArrayList;
    }

    public void showDescription(){
        System.out.println("  " + name + " :");
        String output = new String();
        for(int i = 0 ; i < animalsWhichCanEatIt.size() ; i++){
            output = output + animalsWhichCanEatIt.get(i) + " ";
        }
        System.out.println("Related Animals : " + output);
        System.out.println("Cost : " + buyingCost);
    }
}

interface Breakable{

    public void breakIt();
    public void repairIt();
    public void decreaseEfficiency();
}

class Market extends Location{
    Butchery butchery = new Butchery();
    GeneralStore generalStore = new GeneralStore();
    GroceriesStore groceriesStore = new GroceriesStore();

    Market(Village village){
        name = "Market";
        butchery = new Butchery();
        generalStore = new GeneralStore();
        groceriesStore = new GroceriesStore();
        goToLocations.add(village);

        menuHavings.add(this.butchery);
        menuHavings.add(this.generalStore);
        menuHavings.add(this.groceriesStore);
    }
}

class Butchery extends MenuHaving {
    int openingTime;
    int closingTime;
    TypesOfMeat typesOfMeat;
    private int chosenAction;

    Butchery(){
        setName("Butchery");
    }

    public void showStatus(){

    }

    public void buy(Meat meat){

    }

    public void sell(Animal animal){

    }


    @Override
    public String toString(int i) {
        switch (i){
            case 0:
                return "Butchery :";
            case 1:
                return "Which item do you want?";
            case 2:
                return "Which animal do you want to sell?";
        }
        return null;
    }
    /*  menu 0:
    1.Check this shop
    2.Buy an item
    3.Sell an animal
        menu 1:
    1.item 1
    2.item 2
    ...
        menu 2:
    1.animal 1
    2.animal 2
    ...
     */
    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();
        typesOfMeat = new TypesOfMeat();
        typesOfMeat.setTypes();

        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Check this shop");
        myMenu.options.add("Buy an item");
        myMenu.options.add("Sell an animal");

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        for(int i = 0 ; i < typesOfMeat.types.size() ; i++) {
            myMenu.options.add(typesOfMeat.types.get(i).getName());
        }

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        myMenu.options.add("Cow");
        myMenu.options.add("Sheep");
        myMenu.options.add("Hen");

        myMenus.add(myMenu);
    }

    @Override
    public void reduceInvolvedMenuNum() {
        switch (involvedMenuNum){
            case 1:
                involvedMenuNum--;
                break;
            case 2:
                involvedMenuNum -= 2;
                break;
        }
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu , Game game) {
        Scanner scanner = new Scanner(System.in);
        int num = 0;
        String response = new String();
        switch (numOfMenu){
            case 0:
                switch (numThatChosenFromMenu){
                    case 1:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 1;
                        break;
                    case 2:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 2;
                        break;
                    case 3:
                        backAfterChoose = false;
                        involvedMenuNum += 2;
                        break;
                }
                if(numThatChosenFromMenu > 3)
                    System.out.println("Invalid Choice");
                break;
            case 1:
                if(numThatChosenFromMenu <= typesOfMeat.types.size()) {
                    switch(chosenAction){
                        case 1:
                            backAfterChoose = true;
                            typesOfMeat.types.get(numThatChosenFromMenu - 1).showDescription();
                            break;
                        case 2:
                            backAfterChoose = true;
                            System.out.println("How many meat do you want to buy?");
                            num = scanner.nextInt();
                            System.out.println("You will buy " + typesOfMeat.types.get(numThatChosenFromMenu - 1).getName()
                                    + " x" + num + " for " + num * typesOfMeat.types.get(numThatChosenFromMenu - 1).getBuyingCost()
                                    + " Gil. Is this okay? (Y/N)");
                            response = scanner.next(); // there is a little problem!
                            while(!( response.equals("Y") || response.equals("N"))){
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if(response.equals("Y")) {
                                if(game.player.getMoney() >= num * typesOfMeat.types.get(numThatChosenFromMenu - 1).getBuyingCost()) {
                                    if (game.player.backpack.put(typesOfMeat.types.get(numThatChosenFromMenu - 1), num)) {
                                        game.player.decreseMoney(num * typesOfMeat.types.get(numThatChosenFromMenu - 1).getBuyingCost());
                                    } else {
                                        System.out.println("Backpack is Full !");
                                    }
                                }
                                else {
                                    System.out.println("You don't have enough money");
                                }
                            }
                            break;
                    }
                }
                else {
                    System.out.println("Invalid Choice");
                }
                break;
            case 2:
                int choice = 0;
                switch (numThatChosenFromMenu){
                    case 1:
                        backAfterChoose = true;
                        System.out.println("Choose a Cow:");
                        System.out.println("Cows :");
                        game.farm.barn.cowsPlace.showAnimals();
                        choice = scanner.nextInt();
                        if(choice <= game.farm.barn.cowsPlace.cows.size())
                            game.player.increaseMoney(game.farm.barn.cowsPlace.cows.get(choice - 1).getSellingCost());
                        else
                            System.out.println("Invalid Choice");
                        break;
                    case 2:
                        backAfterChoose = true;
                        System.out.println("Choose a Sheep:");
                        System.out.println("Shep :");
                        game.farm.barn.shepPlace.showAnimals();
                        choice = scanner.nextInt();
                        if(choice <= game.farm.barn.shepPlace.shep.size())
                            game.player.increaseMoney(game.farm.barn.shepPlace.shep.get(choice - 1).getSellingCost());
                        else
                            System.out.println("Invalid Choice");
                        break;
                    case 3:
                        backAfterChoose = true;
                        System.out.println("Choose a Hen:");
                        System.out.println("Hens :");
                        game.farm.barn.hensPlace.showAnimals();
                        choice = scanner.nextInt();
                        if(choice <= game.farm.barn.hensPlace.hens.size())
                            game.player.increaseMoney(game.farm.barn.hensPlace.hens.get(choice - 1).getSellingCost());
                        else
                            System.out.println("Invalid Choice");
                        break;
                }
                break;
        }
    }
}

class Meat extends NonPlantFood{
    Meat(String name, int buyingCost, boolean isChangingNecessary){
        setName(name);
        setBuyingCost(buyingCost);
        setChangingNecessary(isChangingNecessary);
        setBackpackable(true);
    }

    public void showDescription(){

    }
}

class GeneralStore extends MenuHaving {
    int openingTime;
    int closingTime;
    ArrayList<NonMeat> types = new ArrayList<>();
    ArrayList<Seed> seeds = new ArrayList<>();
    private int chosenAction;
    GeneralStore(){
        setName("General Store");
    }
    public void showStatus(){

    }

    public void buy(Item item){

    }
    public void sell(Item item){

    }
    @Override
    public String toString(int i) {
        switch (i){
            case 0:
                return "Groceries Store :";
            case 1:
                return "Which item do you want to buy?";
            case 2:
                return "Which item do you want to sell?";
        }
        return null;
    }
    /*  menu 0:
    1.Check this shop
    2.Buy an item
    3.Sell an item
        menu 1:
    1.item 1
    2.item 2
    ...
     */
    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();
        types.clear();

        TypesOfNonMeat typesOfNonMeat = new TypesOfNonMeat();
        typesOfNonMeat.setTypes();

        for(int i = 0; i < typesOfNonMeat.types.size() ; i++){
            types.add(typesOfNonMeat.types.get(i));
        }

        TypesOfSeed typesOfSeed = new TypesOfSeed();
        typesOfSeed.setTypes();

        for(int i = 0 ; i < typesOfSeed.types.size() ; i++){
            seeds.add(typesOfSeed.types.get(i));
        }

        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Check this shop");
        myMenu.options.add("Buy an item");
        myMenu.options.add("Sell an item");

        myMenus.add(myMenu);

        myMenu = new MyMenu();


        for (int i = 0; i < types.size(); i++) {
            myMenu.options.add(types.get(i).getName());
        }
        for(int i = 0 ; i < seeds.size() ; i++){
            myMenu.options.add(seeds.get(i).getName() + " Seed");
        }

        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        Scanner scanner = new Scanner(System.in);
        String response = new String();
        int num;
        switch (numOfMenu){
            case 0:
                switch (numThatChosenFromMenu){
                    case 1:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 1;
                        break;
                    case 2:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 2;
                        break;
                    case 3:
                        backAfterChoose = true;
                        System.out.println("Choose am item from backpack!");
                        game.player.backpack.showItems();
                        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
                        if (item == null)
                            return;
                        if (!(item.item.getClass().equals(NonMeat.class) || item.item.getClass().equals(Seed.class)) ){
                            System.out.println("Not in this shop");
                            return;
                        }
                        else if(item.item.getClass().equals(NonMeat.class)){
                            NonMeat nonMeat = NonMeat.class.cast(item.item);
                            System.out.println("How many do you want?");
                            int sellingNumber = 0;
                            if (scanner.hasNextInt())
                                sellingNumber = scanner.nextInt();
                            if (sellingNumber <= item.getNumber() && sellingNumber != 0) {

                            } else {
                                System.out.println("Invalid Number");
                                return;
                            }
                            int finalPrice = nonMeat.getBuyingCost();
                            System.out.println("Do you want to sell " + sellingNumber + " " + nonMeat.getName() + " for " + finalPrice + "? (Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                game.player.backpack.take(item.item, sellingNumber);
                                game.player.buy(-1 * finalPrice);
                            }
                        }
                        else if(item.item.getClass().equals(Seed.class)){
                            Seed seed = Seed.class.cast(item.item);
                            System.out.println("How many do you want?");
                            int sellingNumber = 0;
                            if (scanner.hasNextInt())
                                sellingNumber = scanner.nextInt();
                            if (sellingNumber <= item.getNumber() && sellingNumber != 0) {

                            } else {
                                System.out.println("Invalid Number");
                                return;
                            }
                            int finalPrice = seed.getBuyingCost();
                            System.out.println("Do you want to sell " + sellingNumber + " " + seed.getName() + " for " + finalPrice + "? (Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                game.player.backpack.take(item.item, sellingNumber);
                                game.player.buy(-1 * finalPrice);
                            }
                        }
                        break;
                }
                break;
            case 1:
                switch (chosenAction){
                    case 1:
                        backAfterChoose = true;
                        if(numThatChosenFromMenu <= types.size()){
                            types.get(numThatChosenFromMenu - 1).showDescription();
                        }
                        break;
                    case 2:
                        backAfterChoose = true;
                        if(numThatChosenFromMenu <= types.size()) {
                            System.out.println("How many items do you want to buy?");
                            num = scanner.nextInt();
                            int finalPrice = types.get(numThatChosenFromMenu - 1).getBuyingCost();
                            System.out.println("You will buy " + types.get(numThatChosenFromMenu - 1).getName()
                                    + " x" + num + " for " + finalPrice + " Gil. Is this okay? (Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                if (game.player.getMoney() >= finalPrice) {
                                    if (game.player.backpack.put(types.get(numThatChosenFromMenu - 1), num)) {
                                        game.player.decreseMoney(finalPrice);
                                    } else {
                                        System.out.println("Backpack is Full !");
                                    }
                                } else {
                                    System.out.println("You don't have enough money");
                                }
                            }
                        }
                        else if(numThatChosenFromMenu - types.size() <= seeds.size()){
                            System.out.println("How many items do you want to buy?");
                            num = scanner.nextInt();
                            int finalPrice = seeds.get(numThatChosenFromMenu - types.size() - 1).getBuyingCost();
                            System.out.println("You will buy " + seeds.get(numThatChosenFromMenu - types.size()- 1).getName()
                                    + " x" + num + " for " + finalPrice + " Gil. Is this okay? (Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                if (game.player.getMoney() >= finalPrice) {
                                    if (game.player.backpack.put(seeds.get(numThatChosenFromMenu - types.size() - 1), num)) {
                                        game.player.decreseMoney(finalPrice);
                                    } else {
                                        System.out.println("Backpack is Full !");
                                    }
                                } else {
                                    System.out.println("You don't have enough money");
                                }
                            }
                        }
                        else {
                            System.out.println("Invalid Choice!");
                        }
                        break;
                }
        }
    }

}

class GroceriesStore extends MenuHaving {
    int openingTime;
    int closingTime;
    ArrayList<FruitAndVegetable> types = new ArrayList<>();
    TypesOfSeed typesOfSeed = new TypesOfSeed();
    private int chosenAction;

    public void showStatus(){
    }

    public void buy(FruitAndVegetable item){

    }
    public void sell(FruitAndVegetable item){

    }

    public GroceriesStore() {
        setName("Groceries Store");
    }

    @Override
    public String toString(int i) {
        switch (i){
            case 0:
                return "Groceries Store :";
            case 1:
                return "Which item do you want to buy?";
            case 2:
                return "Which item do you want to sell?";
        }
        return null;
    }
    /*  menu 0:
    1.Check this shop
    2.Buy an item
    3.Sell an item
        menu 1:
    1.item 1
    2.item 2
    ...
     */
    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();
        types.clear();

        TypesOfVegetable typesOfVegetable = new TypesOfVegetable();
        typesOfVegetable.setTypes();

        TypesOfFruit typesOfFruit = new TypesOfFruit();
        typesOfFruit.setTypes();

//        typesOfSeed = new TypesOfSeed();
//        typesOfSeed.setTypes();

        for(int i = 0 ; i < typesOfFruit.types.size() ; i++){
            types.add(typesOfFruit.types.get(i));
        }
//        for(int i = 0; i < typesOfVegetable.types.size() ; i++){
//            types.add(typesOfVegetable.types.get(i));
//        }

        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Check this shop");
        myMenu.options.add("Buy an item");
        myMenu.options.add("Sell an item");

        myMenus.add(myMenu);

        myMenu = new MyMenu();


        for (int i = 0; i < types.size(); i++) {
            myMenu.options.add(types.get(i).getName());
        }
        for(int i = 0 ; i < typesOfSeed.types.size() ; i++){
            myMenu.options.add(typesOfSeed.types.get(i).getName());
        }

        myMenus.add(myMenu);
    }


    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        Scanner scanner = new Scanner(System.in);
        String response = new String();
        int num;
        switch (numOfMenu){
            case 0:
                switch (numThatChosenFromMenu){
                    case 1:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 1;
                        break;
                    case 2:
                        backAfterChoose = false;
                        involvedMenuNum++;
                        chosenAction = 2;
                        break;
                    case 3:
                        backAfterChoose = true;
                        System.out.println("Choose am item from backpack!");
                        game.player.backpack.showItems();
                        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
                        if (item == null)
                            return;
                        if (!(item.item.getClass().equals(Seed.class) || item.item.getClass().equals(Fruit.class)
                                || item.item.getClass().equals(Vegetable.class))) {
                            System.out.println("Not A Fruit, Vegetable or Seed as a result of that you can't sell here!");
//                            reduceInvolvedMenuNum();
                            return;
                        }
                        else{
                            FruitAndVegetable fruitAndVegetable = FruitAndVegetable.class.cast(item.item);
                            System.out.println("How many do you want to buy?");
                            int sellingNumber = 0;
                            if (scanner.hasNextInt())
                                sellingNumber = scanner.nextInt();
                            if (sellingNumber <= item.getNumber() && sellingNumber != 0) {

                            } else {
                                System.out.println("Invalid Number");
//                                reduceInvolvedMenuNum();
                                return;
                            }

                            int finalPrice = sellingNumber * FruitAndVegetable.rateSelling *
                                    (fruitAndVegetable.getPrice() *
                                            (game.season.getName().equals(fruitAndVegetable.getSeason()) ? 100 : FruitAndVegetable.rate )) / (100*100);
                            System.out.println("Do you want to sell " + sellingNumber + " " + fruitAndVegetable.getName() + " for " + finalPrice + "? (Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                game.player.backpack.take(item.item, sellingNumber);
                                game.player.buy(-1 * finalPrice);
                                System.out.println("Sold !");
//                                reduceInvolvedMenuNum();
//                                return;
                            }
                        }
                        break;
                }
                break;
            case 1:
                switch (chosenAction){
                    case 1:
                        backAfterChoose = true;
                        if(numThatChosenFromMenu <= types.size()){
                            types.get(numThatChosenFromMenu - 1).showDescription();
                        }
                        else if(numThatChosenFromMenu - types.size() <= typesOfSeed.types.size()){
                            typesOfSeed.types.get(numThatChosenFromMenu - types.size() - 1).showStatus();
                        }
                        else {
                            System.out.println("Invalid Choice!");
                        }
                        break;
                    case 2:
                        backAfterChoose = true;
                        if(numThatChosenFromMenu <= types.size()) {
                            System.out.println("How many items do you want to buy?");
                            num = scanner.nextInt();
                            int finalPrice = num * (types.get(numThatChosenFromMenu - 1).getPrice() *
                                    (game.season.getName().equals(types.get(numThatChosenFromMenu - 1).getSeason()) ? 100 : FruitAndVegetable.rate)) / 100;
                            System.out.println("You will buy " + types.get(numThatChosenFromMenu - 1).getName()
                                    + " x" + num + " for " + finalPrice + " Gil. Is this okay? (Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                if (game.player.getMoney() >= finalPrice) {
                                    if (game.player.backpack.put(types.get(numThatChosenFromMenu - 1), num)) {
                                        game.player.decreseMoney(finalPrice);
                                    } else {
                                        System.out.println("Backpack is Full !");
                                    }
                                } else {
                                    System.out.println("You don't have enough money");
                                }
                            }
                        }
                        else if(numThatChosenFromMenu - types.size() <= typesOfSeed.types.size()){
                            System.out.println("How many items do you want to buy?");
                            num = scanner.nextInt();
                            int finalPrice = num * (typesOfSeed.types.get(numThatChosenFromMenu - types.size() - 1).getSellingCost() *
                                    (game.season.getName().equals(typesOfSeed.types.get(numThatChosenFromMenu - types.size() - 1).getSeason()) ? 100 : FruitAndVegetable.rate)) / 100;
                            System.out.println("You will buy " + types.get(numThatChosenFromMenu - types.size() - 1).getName()
                                    + " x" + num + " for " + finalPrice + " Gil. Is this okay? (Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (response.equals("Y")) {
                                if (game.player.getMoney() >= finalPrice) {
                                    if (game.player.backpack.put(typesOfSeed.types.get(numThatChosenFromMenu - types.size() - 1), num)) {
                                        game.player.decreseMoney(finalPrice);
                                    } else {
                                        System.out.println("Backpack is Full !");
                                    }
                                } else {
                                    System.out.println("You don't have enough money");
                                }
                            }
                        }
                        else {
                            System.out.println("Invalid Choice!");
                        }
                        break;
                }
        }
    }

}

class Trader{
    ArrayList<DemandingTradeable> availableInputTradeable;
    ArrayList<DemandingTradeable> AvailableOutputTradeable;
    private int incomeMoney;
    private int outcomeMoney;

    public Trader(ArrayList<DemandingTradeable> availableInputTradeable
            , ArrayList<DemandingTradeable> availableOutputTradeable, int incomeMoney, int outcomeMoney) {
        this.availableInputTradeable = availableInputTradeable;
        AvailableOutputTradeable = availableOutputTradeable;
        this.incomeMoney = incomeMoney;
        this.outcomeMoney = outcomeMoney;
    }

    public void trade(int incomeMoney, int outcomeMoney, ArrayList<DemandingTradeable> inputItems, ArrayList<DemandingTradeable> outputItems){

    }

    public int getIncomeMoney() {
        return incomeMoney;
    }

    public void setIncomeMoney(int incomeMoney) {
        this.incomeMoney = incomeMoney;
    }

    public int getOutcomeMoney() {
        return outcomeMoney;
    }

    public void setOutcomeMoney(int outcomeMoney) {
        this.outcomeMoney = outcomeMoney;
    }
}

class DemandingRawMaterial{
    private String name = new String();
    private String kind = new String();
    private int quality;
    private int number;

    DemandingRawMaterial(String name, int number , int quality , String kind){
        this.name = name;
        this.number = number;
        this.quality = quality;
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

//    public void check

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getQuality() {
        return quality;
    }
}
class Tool extends Item{
    private String name = new String();
    //    private String kind = new String();
    private int buyingCost;
    private int sellingCost;
    private int efficiency;
    private int demandingMoneyToBuild;
    private int demandingMoneyToRepair;
    ArrayList<DemandingRawMaterial> demandingRawMaterialsToBuild = new ArrayList<>();
    ArrayList<DemandingRawMaterial> demandingRawMaterialsToRepair = new ArrayList<>();
    ArrayList<DemandingRawMaterial> demandingRawMaterialsToDisassemble = new ArrayList<>();//TODO Fill
    //    ArrayList<DemandingRawMaterial> getDemandingRawMaterialsToDisassembleRepaired = new ArrayList<>(); //TODO Fill
    private int decreasingEfficiencyPerUse;
    private int repairingCost; //TODO Fill


    public void breakIt(int probablityOfBreak, String name){
        Random rand = new Random();
        if(rand.nextInt(100) < probablityOfBreak) {
            setBroken(true);
            System.out.println(name + " Broken");
        }

    }



    public int getRepairingCost() {
        return repairingCost;
    }

    public void repair(){

    }

    public int getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }

    public int getSellingCost() {
        return sellingCost;
    }

    public void setSellingCost(int sellingCost) {
        this.sellingCost = sellingCost;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }

    public int getDecreasingEfficiencyPerUse() {
        return decreasingEfficiencyPerUse;
    }

    public void setDecreasingEfficiencyPerUse(int decreasingEfficiencyPerUse) {
        this.decreasingEfficiencyPerUse = decreasingEfficiencyPerUse;
    }

    public int getDemandingMoneyToRepair() {
        return demandingMoneyToRepair;
    }

    public void setDemandingMoneyToRepair(int demandingMoneyToRepair) {
        this.demandingMoneyToRepair = demandingMoneyToRepair;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getKind() {
//        return kind;
//    }
//
//    public void setKind(String kind) {
//        this.kind = kind;
//    }

    public int getDemandingMoneyToBuild() {
        return demandingMoneyToBuild;
    }

    public void setDemandingMoneyToBuild(int demandingMoneyToBuild) {
        this.demandingMoneyToBuild = demandingMoneyToBuild;
    }

    public ArrayList<DemandingRawMaterial> getDemandingRawMaterialToBuild() {
        return demandingRawMaterialsToBuild;
    }

    public ArrayList<DemandingRawMaterial> getDemandingRawMaterialsToRepair() {
        return demandingRawMaterialsToRepair;
    }
}

class ExploreTool extends Tool{
    private int loosingEnergy;
    private int quality;

    ArrayList<RawMaterial> materialsCanBeExplored;

    ExploreTool(String kind, String name, int quality, int loosingEnergy, int demandingMoneyTobuild, int demandingMoneyToRepair){
        setKind(kind);
        setName(name);
        setQuality(quality);
        setLoosingEnergy(loosingEnergy);
        setDemandingMoneyToRepair(demandingMoneyToRepair);
        setDemandingMoneyToBuild(demandingMoneyTobuild);
    }

    @Override
    public void showStatus() {
        System.out.println("A " + getName() + " " + getKind() + " " +(getKind().equals("Shovel")?"\n Energy Use :" + loosingEnergy:"")
                + " " + " isBroken? :" + isBroken());
    }

    public int getLoosingEnergy() {
        return loosingEnergy;
    }

    public void setLoosingEnergy(int loosingEnergy) {
        this.loosingEnergy = loosingEnergy;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        return getName() + " " + getKind();
    }
}

class FishingTool extends Tool{
    private int probablityOfCatching;
    private int loosingEnergy;
    private int probablityOfBreak;


    public FishingTool(String name , int buyingCost, int demandingMoneyToRepair, int loosingEnergy, int probablityOfCatching, int probablityOfBreak, String kind) {
        this.loosingEnergy = loosingEnergy;
        this.probablityOfCatching = probablityOfCatching;
        setBuyingCost(buyingCost);
        setName(name);
        setDemandingMoneyToRepair(demandingMoneyToRepair);
        this.probablityOfBreak = probablityOfBreak;
        setKind(kind);
    }

    @Override
    public void showStatus() {
        System.out.println("A " + getName() + getKind() + " FishingTool" + " Probability O CAtching : " + probablityOfCatching + " isBroken ? : "+ isBroken());
    }

    public int getProbablityOfCatching() {
        return probablityOfCatching;
    }

    public void setProbablityOfCatching(int probablityOfCatching) {
        this.probablityOfCatching = probablityOfCatching;
    }

    public int getProbablityOfBreak() {
        return probablityOfBreak;
    }

    public void setProbablityOfBreak(int probablityOfBreak) {
        this.probablityOfBreak = probablityOfBreak;
    }

    @Override
    public String toString() {
        return getKind() + "FishingTool";
    }
}

class KitchenTool extends Tool{
    private int probablityOfBreak;
    private boolean isBought;

    public void setProbablityOfBreak(int probablityOfBreak){
        this.probablityOfBreak = probablityOfBreak;
    }

    public int getProbablityOfBreak(){
        return probablityOfBreak;
    }
    @Override
    public void setMyMenus(Game game){
        MyMenu myMenu = new MyMenu();
        if (isBought()){
            myMenu.options.add("Status");
            myMenu.options.add("Replace This Tool");
            myMenu.options.add("Remove This Tool");
        }
        else {
            myMenu.options.add("Put This Tool in Shelf");
        }
        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        if (numOfMenu == 0)
        {
            if (isBought())
            {
                switch (numThatChosenFromMenu){
                    case 1:
                    {
                        showStatus();
                    }
                    case 2:
                    {
                        replace(game);
                    }
                    case 3:
                    {
                        remove(game);
                    }
                }
            }
            else {
                if (numThatChosenFromMenu == 1)
                {
                    put(game);
                }
            }
        }
    }

    public void showStatus(){
        System.out.println("Status : ");
        System.out.println("Probability Of Break : " + probablityOfBreak);
        System.out.println("Is Broken? : " + isBroken());
    }

    public void replace(Game game){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select A " + getName() + " From BackPack");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getName().equals(getName()))
        {
            System.out.println("Not A " + getName());
            return;
        }
        else {
            KitchenTool temp = new KitchenTool("",0);
            clone(temp, this);
            clone(this, KitchenTool.class.cast(item.item));
            clone(KitchenTool.class.cast(item.item), temp);
        }


    }

    public void remove(Game game){
        if (game.player.backpack.getCapacity() > game.player.backpack.savingItems.size())
        {
            game.player.backpack.put(this, 1);
        }
        else
        {
            System.out.println("No Room In BackPack!");
        }
    }

    public void put(Game game){
        replace(game);
    }

    public void clone(KitchenTool to, KitchenTool from) {
        to.setName(from.getName());
        to.setPackable(from.isPackable());
        to.setDemandingMoneyToRepair(from.getDemandingMoneyToRepair());
        to.setBuyingCost(from.getBuyingCost());
        to.setBought(from.isBought());
        to.setBroken(from.isBroken());
        to.setKind(from.getKind());
        to.demandingRawMaterialsToBuild = from.demandingRawMaterialsToBuild;

        to.setProbablityOfBreak(from.getProbablityOfBreak());
    }
//
//            game.player.backpack.showItems();
//        if (game.player.backpack.take(this, 1))
//        {
//
//        }


    @Override

    public String toString(int i) {
        if (i == 0)
            return getName() + " : ";
        return "";
    }

    public KitchenTool(String name ,int probablityOfBreak) {
        this.probablityOfBreak = probablityOfBreak;
        setName(name);
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }
}

class WateringTool extends Tool{
    private String kind = new String();
    private int loosingEnergy;
    private int capacity;
    private int quality;
    private int currentWater;
    private int perUseCapacity;


    public WateringTool(String kind, String name ,int quality, int loosingEnergy, int buyingCost, int demandingMoneyToRepair, int capacity, int perUseCapacity) {
        this.loosingEnergy = loosingEnergy;
        this.capacity = capacity;
        this.kind = kind;
        setName(name);
        this.quality = quality;
        setBuyingCost(buyingCost);
        setDemandingMoneyToRepair(demandingMoneyToRepair);
        this.perUseCapacity = perUseCapacity;
    }

    @Override
    public void showStatus() {
        System.out.println("A "+ getName() + " " + getKind() + " WateringTool" + " Capacity : " + capacity + " is broken? :" + isBroken());
    }

    public void use(Game game ) {
        game.player.getEnergy().setCurrentAmount(game.player.getEnergy().getCurrentAmount() - getLoosingEnergy());
        currentWater--;
    }
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getLoosingEnergy() {
        return loosingEnergy;
    }

    public void setLoosingEnergy(int loosingEnergy) {
        this.loosingEnergy = loosingEnergy;
    }

    public int getCurrentWater() {
        return currentWater;
    }

    public void setCurrentWater(int currentWater) {
        this.currentWater = currentWater;
    }

    @Override
    public String toString() {
        return getKind() + "WateringTool";
    }
}

class MachineItem{
    private String classOfItem = new String();
    private String nameOfItem = new String();
    private int number;

    MachineItem(String classOfItem, String nameOfItem, int number){
        setClassOfItem(classOfItem);
        setNameOfItem(nameOfItem);
        setNumber(number);
    }

    public String getClassOfItem() {
        return classOfItem;
    }

    public void setClassOfItem(String classOfItem) {
        this.classOfItem = classOfItem;
    }

    public String getNameOfItem() {
        return nameOfItem;
    }

    public void setNameOfItem(String namOfItem) {
        this.nameOfItem = namOfItem;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

class Machine extends MenuHaving implements Breakable{
    private String name = new String();
    private int loosingEfficiencyPerUse;
    private int efficiency;
    private int buyingCost;
    private int sellingCost;
    private boolean isBought;
    ArrayList<MachineItem> inputClasses = new ArrayList<>();
    ArrayList<MachineItem> outputClasses = new ArrayList<>();

    @Override
    public String toString(int i) {
        return name + " : ";
    }

    @Override
    public void setMyMenus(Game game) {

        MyMenu myMenu = new MyMenu();
        myMenu.options.add("Status");
        myMenu.options.add("Use this Machine");

        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        if (numOfMenu == 0){
            backAfterChoose = false;
            if (numThatChosenFromMenu == 1){
                showStatus();
            }
            else if (numThatChosenFromMenu == 2){
                use(game);
            }
        }
    }

    public void breakIt(){

    }
    public void repairIt(){

    }
    public void buy(Game game){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to buy this machine ? : " + name);
        String command = new String(scanner.next());
        while (!command.equals("Y") && !command.equals("N")){
            System.out.println("Invalid");
            command = scanner.next();
        }
        if (command.equals("N"))
            return;
        else if (command.equals("Y")) {
            if (game.player.buy(buyingCost)){
                game.farm.barn.machinePart.machines.add(this);
                game.village.laboratory.boughtableMachines.remove(this);
                setBought(true);
                System.out.println("Bought !");
            }
            else
                System.out.println("Not Enough Money!");
        }
    }
    public void decreaseEfficiency(){

    }
    public void use(Game game){
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < inputClasses.size(); i++) {
            if (inputClasses.get(i).getNameOfItem().equals(""))
            {
                System.out.println("Select Fruit x2 Or Vegetable x2 From BackPack");

                game.player.backpack.showItems();
                SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
                if (item == null)
                    return;

                if (!item.item.getClass().equals(Fruit.class) && !item.item.getClass().equals(Vegetable.class) )
                {
                    System.out.println("Not A Fruit Or Vegetable");
                    return;
                }
                else if (item.item.getClass().equals(Fruit.class) && !item.item.getClass().equals(Vegetable.class)){
                    if (!game.player.backpack.take(item.item, inputClasses.get(i).getNumber()))
                    {
                        System.out.println("Not Enough Number");
                        return;
                    }
                    Fruit fruit = Fruit.class.cast(item.item);
                    Juice juice = new Juice(fruit);

                    if (game.player.backpack.put(juice, 1)){
                        System.out.println("Enjoy!");

                    }
                    else
                    {
                        System.out.println("No Room In BackPack!");
                        return;
                    }

                }
                else if (!item.item.getClass().equals(Fruit.class) && item.item.getClass().equals(Vegetable.class)){
                    if (item.getNumber() < inputClasses.get(i).getNumber())
                    {
                        System.out.println("Not Enough Number");
                        return;
                    }
                    Vegetable vegetable = Vegetable.class.cast(item.item);

                    Juice juice = new Juice(vegetable);


                    if (game.player.backpack.put(juice, 1)){
                        System.out.println("Enjoy!");
                        break;

                    }
                    else
                    {
                        System.out.println("No Room In BackPack!");
                        return;
                    }
                }

            }
            else{
                System.out.println("Select " + inputClasses.get(i).getNameOfItem() + " x" + inputClasses.get(i).getNumber() +" From BackPack");

                game.player.backpack.showItems();
                SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
                if (item == null)
                    return;
                else if (item.item.getName().equals(inputClasses.get(i).getNameOfItem())){
                    if (!game.player.backpack.take(item.item, inputClasses.get(i).getNumber())){
                        return;
                    }
                    else{
                        TypesOfNonMeat typesOfNonMeat = new TypesOfNonMeat();
                        typesOfNonMeat.setTypes();
                        for (int j = 0; j < typesOfNonMeat.types.size(); j++){
                            if (typesOfNonMeat.types.get(j).getName().equals(inputClasses.get(i).getNameOfItem())){
                                if (game.player.backpack.put(typesOfNonMeat.types.get(j), 1)){
                                    System.out.println("Enjoy !");
                                    return;
                                }
                                else{
                                    System.out.println("No Room In Backpack!");
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public void showStatus(){
        System.out.println("Input : ");
        for (int i = 0; i < inputClasses.size(); i++) {
            if (inputClasses.get(i).getNameOfItem().equals(""))
            {
                System.out.println(inputClasses.get(i).getClassOfItem() + " x" + inputClasses.get(i).getNumber());
            }
            else{
                System.out.println(inputClasses.get(i).getNameOfItem() + " x" + inputClasses.get(i).getNumber());
            }
        }
        System.out.println("Output : ");
        for (int i = 0; i < outputClasses.size(); i++){
            if (inputClasses.get(i).getNameOfItem().equals(""))
            {
                System.out.println(outputClasses.get(i).getClassOfItem() + " x" + inputClasses.get(i).getNumber());
            }
            else{
                System.out.println(outputClasses.get(i).getNameOfItem());
            }
        }

        if (!isBought())
            System.out.println("Price : " + this.buyingCost);
    }

    public int getLoosingEfficiencyPerUse() {
        return loosingEfficiencyPerUse;
    }

    public void setLoosingEfficiencyPerUse(int loosingEfficiencyPerUse) {
        this.loosingEfficiencyPerUse = loosingEfficiencyPerUse;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }

    public int getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }

    public int getSellingCost() {
        return sellingCost;
    }

    public void setSellingCost(int sellingCost) {
        this.sellingCost = sellingCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }
}

abstract class Item extends MenuHaving{
    private String name = new String();
    private String kind = new String();
    private boolean isPackable;
    private boolean isBroken;
    private boolean havingUseInBackpackMenu = false;
    private String description;
    private int occupyingCapacity;

    public boolean isHavingUseInBackpackMenu() {
        return havingUseInBackpackMenu;
    }

    public String getDescription() {
        return description;
    }

    public void setHavingUseInBackpackMenu(boolean havingUseInBackpackMenu) {
        this.havingUseInBackpackMenu = havingUseInBackpackMenu;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOccupyingCapacity(int occupyingCapacity) {
        this.occupyingCapacity = occupyingCapacity;
    }

    public int getOccupyingCapacity() {
        return occupyingCapacity;
    }

    public String getKind(){
        return kind;
    }

    public void setKind(String kind){
        this.kind = kind;
    }

    public void setBroken(boolean isBroken){
        this.isBroken = isBroken;
    }
    public void setBackpackable(boolean backpackable){
        isPackable = backpackable;
    }
    public void showStatus(){
//        System.out.println(description);
    }

    public boolean isPackable() {
        return isPackable;
    }

    public void setPackable(boolean packable) {
        isPackable = packable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean useInBackpack(Game game){
        return isHavingUseInBackpackMenu(); // output indicates that after using this item drop it or not
    }

    public boolean isBroken() {
        return isBroken;
    }


    public void use(){}

    @Override
    public void setMyMenus(Game game) {}

    @Override
    public String toString(int i) {
        return null;
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {}
}

class Drug extends Item{
    //    private String kind;
    private String name;
    private String description;
    private int effectOnHealth;
    //    private int effectOnEnergy;
    private int buyingCost;
    private int sellingCost;

    Drug(String name, int effectOnHealth, int buyingCost, String kind) {
        setEffectOnHealth(effectOnHealth);
        setBackpackable(true);
        setName(name);
//        setEffectOnEnergy(effectOnEnergy);
        setBuyingCost(buyingCost);
//        setDescription(description);
        setPackable(true);
        setKind(kind);
        setHavingUseInBackpackMenu(true);
    }

    @Override
    public void showStatus() {
        System.out.println("    " + name + " " + getKind() + " : ");
        System.out.println("BuyingCost : " + buyingCost);
        System.out.println("EffectOnHealth : " + effectOnHealth);
        System.out.println();
    }

    public void showDescription() {
        System.out.println("    " + name + " " + getKind() + " : ");
        System.out.println("BuyingCost : " + buyingCost);
        System.out.println("EffectOnHealth : " + effectOnHealth);
        System.out.println();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean useInBackpack(Game game) {
        game.player.getHealth().increaseCurrentAmount(effectOnHealth);
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEffectOnHealth() {
        return effectOnHealth;
    }

    public void setEffectOnHealth(int effectOnHealth) {
        this.effectOnHealth = effectOnHealth;
    }

//    public int getEffectOnEnergy() {
//        return effectOnEnergy;
//    }

//    public void setEffectOnEnergy(int effectOnEnergy) {
//        this.effectOnEnergy = effectOnEnergy;
//    }

    public int getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }

    public int getSellingCost() {
        return sellingCost;
    }

    public void setSellingCost(int sellingCost) {
        this.sellingCost = sellingCost;
    }
}

class SapplingAndSeed extends Item {
    private String name;
    private int buyingCost;
    private int sellingCost;
    private String season;

    SapplingAndSeed(String name, int buyingCost, String growingSeason, String kind){
        setName(name);
        setBuyingCost(buyingCost);
        setSeason(growingSeason);
        setKind(kind);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }

    public int getSellingCost() {
        return sellingCost;
    }

    public void setSellingCost(int sellingCost) {
        this.sellingCost = sellingCost;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String growingSeason) {
        this.season = growingSeason;
    }
}

class Seed extends SapplingAndSeed {
    Seed(String name, int buyingCost, String growthSeason, String kind){
        super(name, buyingCost, growthSeason, kind);
        setBackpackable(true);
    }

    @Override
    public void showStatus() {
        System.out.println("A " + getName() + " Seed. Can be raised in" + getSeason() + " in field or greenhouse");
    }
}

//class Sappling extends SapplingAndSeed {
//    private int age;
//
//    Sappling(String name, int age){
//        this.setName(name);
//        this.setAge(age);
////    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//}

class CafeFood{
    private String name;
    private int price;
    private int effectOnHealth;
    private int effectOnEnergy;

    CafeFood(String name, int price, int effectOnHealth, int effectOnEnergy){
        setName(name);
        setPrice(price);
        setEffectOnHealth(effectOnHealth);
        setEffectOnEnergy(effectOnEnergy);
    }
    public void showStatus(){
        System.out.println("Name : " + name);
        System.out.println("Price : " + price);
        System.out.println("Health :" + effectOnHealth);
        System.out.println("Energy : " + effectOnEnergy);
    }

    public void buyAndEat(Game game){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do You Want To buy for " + price + " ? (Y/N)");
        String command = new String();
        command = scanner.next();
        while (!command.equals("Y")&& !command.equals("N"))
        {
            System.out.println("Invalid");
            command = scanner.next();
        }
        if (game.player.buy(price)){
            game.player.getEnergy().increaseCurrentAmount(game, effectOnEnergy);
//            game.player.use(-effectOnEnergy);
//            game.player.getEnergy().setCurrentAmount(game.player.getEnergy().getCurrentAmount() - effectOnEnergy);
            game.player.healthDecrease(-effectOnHealth);
            System.out.println("Delicious!");
        }
        else
            System.out.println("Not Enough Money!");
    }

    public int getEffectOnEnergy() {
        return effectOnEnergy;
    }

    public void setEffectOnEnergy(int effectOnEnergy) {
        this.effectOnEnergy = effectOnEnergy;
    }

    public int getEffectOnHealth() {
        return effectOnHealth;
    }

    public void setEffectOnHealth(int effectOnHealth) {
        this.effectOnHealth = effectOnHealth;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean eat(){
        return true;
    }
}

//class DemandingIngredients{
//    ArrayList<String> ingredientOfCookingItems;
//
//    DemandingIngredients(String name){
//        ingredientOfCookingItems.add(name);
//    }
//}


class DemandingItem{
    Item Item;
    private int demandingNumber;
    private int takenNumber;

    public int getDemandingNumber() {
        return demandingNumber;
    }

    public void setDemandingNumber(int demandingNumber) {
        this.demandingNumber = demandingNumber;
    }

    public int getTakenNumber() {
        return takenNumber;
    }

    public void setTakenNumber(int takenNumber) {
        this.takenNumber = takenNumber;
    }

    @Override
    public String toString() {
        return Item.getName() + " x" + Item;
    }
}

class DemandingTradeable{
    Tradeable tradeable;
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

interface Tradeable{
}


class DiningTable extends MenuHaving {
    ArrayList<CafeFood> cafeFoods = new ArrayList<>();

    public void showStatus(){

    }

    DiningTable(){
        name = "Dining Table";
        TypesOfCafeFood typesOfCafeFood = new TypesOfCafeFood();
        typesOfCafeFood.setTypes();
        cafeFoods = typesOfCafeFood.types;
    }
    public void buy(int indexOfFood){

    }

    public void select(int indexOfFood){

    }

    @Override
    public String toString(int i) {
        return "Dining Table";
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Check this menu");
        myMenu.options.add("Buy a meal");
        myMenus.add(myMenu);

        myMenu = new MyMenu();
        for (int i = 0; i < cafeFoods.size(); i++){
            myMenu.options.add(cafeFoods.get(i).getName());
        }
        myMenus.add(myMenu);

        myMenu = new MyMenu();
        for (int i = 0; i < cafeFoods.size(); i++){
            myMenu.options.add(cafeFoods.get(i).getName());
        }
        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfmenu, Game game) {
        if (numOfmenu == 0){
            if (numThatChosenFromMenu == 1){
                backAfterChoose = false;
                involvedMenuNum++;
            }
            else if (numThatChosenFromMenu == 2){
                backAfterChoose = false;
                involvedMenuNum += 2;
            }
        }
        else if (numOfmenu == 1){
            backAfterChoose = false;
            if (numThatChosenFromMenu <= cafeFoods.size()){
                cafeFoods.get(numThatChosenFromMenu - 1).showStatus();
            }
        }
        else if (numOfmenu == 2){
            backAfterChoose = false;
            if (numThatChosenFromMenu <= cafeFoods.size()){
                cafeFoods.get(numThatChosenFromMenu - 1).buyAndEat(game);
            }
        }
    }

    @Override
    public void reduceInvolvedMenuNum() {
        if (involvedMenuNum == 1) {
            involvedMenuNum--;
        }
        else if (involvedMenuNum == 2) {
            involvedMenuNum -= 2;
        }
    }

}

class FruitAndVegetable extends Item{
    static int rate = 125;
    static int rateSelling = 75;
    private String name;
    private int effectOnHealth;
    private int effectOnEnergy;
    private int effectOnEnergyMax;
    private int effectOnHealthMax;
    private int price;
    private String Season = new String();

    @Override
    public void showStatus() {
        System.out.println("A " + getName() + " can be used while cooking" + " effect On health " + effectOnHealth
                + " effectOnEnergy : " + effectOnEnergy + " effectOn Health Maximum : "+ effectOnHealthMax + " effectOn Energy Maximum" + effectOnEnergyMax);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEffectOnHealth() {
        return effectOnHealth;
    }

    public void setEffectOnHealth(int effectOnHealth) {
        this.effectOnHealth = effectOnHealth;
    }

    public int getEffectOnEnergy() {
        return effectOnEnergy;
    }

    public void setEffectOnEnergy(int effectOnEnergy) {
        this.effectOnEnergy = effectOnEnergy;
    }

    public int getEffectOnEnergyMax() {
        return effectOnEnergyMax;
    }

    public void setEffectOnEnergyMax(int effectOnEnergyMax) {
        this.effectOnEnergyMax = effectOnEnergyMax;
    }

    public int getEffectOnHealthMax() {
        return effectOnHealthMax;
    }

    public void setEffectOnHealthMax(int effectOnHealthMax) {
        this.effectOnHealthMax = effectOnHealthMax;
    }

    public void showDescription(){
    }

    FruitAndVegetable(String name, int effectOnHealthMax, int effectOnEnergyMax, int effectOnHealth, int effectOnEnergy, int price, String season){
        setName(name);
        setEffectOnHealthMax(effectOnHealthMax);
        setEffectOnEnergyMax(effectOnEnergyMax);
        setEffectOnHealth(effectOnHealth);
        setEffectOnEnergy(effectOnEnergy);
        setPrice(price);
        setSeason(season);
        setBackpackable(true);
        setHavingUseInBackpackMenu(true);
    }


    @Override
    public boolean useInBackpack(Game game) {
        game.player.getHealth().increaseCurrentAmount(getEffectOnHealth());
        game.player.getHealth().increaseMaxAmount(getEffectOnHealthMax());
        game.player.getEnergy().increaseCurrentAmount(game , getEffectOnEnergy());
        game.player.getEnergy().increaseMaxAmount(getEffectOnEnergyMax());
        return true;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSeason() {
        return Season;
    }

    public void setSeason(String season) {
        Season = season;
    }
}

class Jungle extends Location{
    private Woods woods = new Woods();
    private Rocks rocks = new Rocks();
    private Cave cave = new Cave(this);
    private River river =new River();

    Jungle(Game game){
        menuHavings.add(woods);
        menuHavings.add(rocks);
        menuHavings.add(river);

        goToLocations.add(game.farm);
        goToLocations.add(game.village);
        goToLocations.add(cave);
        setName("Jungle");

    }

    public void inspectRiver(){

    }

    public void inspectRock(){

    }

    public void inspectWoods(){

    }
}

class Cave extends Location {
    private RockInCave rockInCave = new RockInCave();
    Cave(Jungle jungle){
        goToLocations.add(jungle);

        menuHavings.add(rockInCave);

        setName("Cave");
    }
}

class DemandingUse{
    String kind;
    String name;
    private int demandingUses;
    private int numberOfUses;

    public int getNumberOfUses() {
        return numberOfUses;
    }

    public void setNumberOfUses(int numberOfUses) {
        this.numberOfUses = numberOfUses;
    }

    public int getDemandingUses() {
        return demandingUses;
    }

    public void setDemandingUses(int demandingUses) {
        this.demandingUses = demandingUses;
    }
}

class DemandingItemInMission{
    int numOfPurpose;
    int numThatReached;
    Item item;

    public boolean equals(Item item) {
        return (this.item.getKind().equals(item.getKind()) && this.item.getName().equals((item.getKind()))) ? true : false;
    }
}

class Mission extends MenuHaving{
    private boolean isActive = false;
    private int prizeFee;
    private int contractFee;
    private int deadline;
    private int maxDeadline = 20;
    private int maxDemandingItemTypeInMission = 10;
    private int maxNumOfDemandingItemInMission = 10;
    ArrayList<DemandingUse> purposeUses = new ArrayList<>();
    ArrayList<DemandingItemInMission> demandingItems = new ArrayList<>();
    ArrayList<Objects> purposeWorks = new ArrayList<>();
    ArrayList<Item> types = new ArrayList<Item>();

    TypesOfSeed typesOfSeed = new TypesOfSeed();
    TypesOfNonMeat typesOfNonMeat = new TypesOfNonMeat();
    TypesOfFruit typesOfFruit = new TypesOfFruit();
    TypesOfMeat typesOfMeat = new TypesOfMeat();
    TypesOfThread typesOfThread = new TypesOfThread();
    TypesOfSkinProduct typesOfSkinProduct = new TypesOfSkinProduct();
    TypesOfFish typesOfFish = new TypesOfFish();
    TypesOfWood typesOfWood = new TypesOfWood();
    TypesOfStone typesOfStone = new TypesOfStone();
    TypesOfAnimalFood typesOfAnimalFood = new TypesOfAnimalFood();
    TypesOfExploreTool typesOfExploreTool = new TypesOfExploreTool();
    TypesOfFishingTool typesOfFishingTool = new TypesOfFishingTool();
    TypesOfWateringTool typesOfWateringTool = new TypesOfWateringTool();
    TypesOfKitchenTool typesOfKitchenTool = new TypesOfKitchenTool();

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPrizeFee(int prizeFee) {
        this.prizeFee = prizeFee;
    }

    public void setContractFee(int contractFee) {
        this.contractFee = contractFee;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public void setMaxDeadline(int maxDeadline) {
        this.maxDeadline = maxDeadline;
    }

    public void setMaxDemandingItemTypeInMission(int maxDemandingItemTypeInMission) {
        this.maxDemandingItemTypeInMission = maxDemandingItemTypeInMission;
    }

    public void setMaxNumOfDemandingItemInMission(int maxNumOfDemandingItemInMission) {
        this.maxNumOfDemandingItemInMission = maxNumOfDemandingItemInMission;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getPrizeFee() {
        return prizeFee;
    }

    public int getContractFee() {
        return contractFee;
    }

    public int getDeadline() {
        return deadline;
    }

    public int getMaxDeadline() {
        return maxDeadline;
    }

    public int getMaxDemandingItemTypeInMission() {
        return maxDemandingItemTypeInMission;
    }

    public int getMaxNumOfDemandingItemInMission() {
        return maxNumOfDemandingItemInMission;
    }

    public void accept(){
        isActive = true;
    }

    public Mission(Game game){
        typesOfSeed.setTypes();
        typesOfNonMeat.setTypes();
        typesOfFruit.setTypes();
        typesOfMeat.setTypes();
        typesOfThread.setTypes();
        typesOfSkinProduct.setTypes();
        typesOfFish.setTypes();
        typesOfWood.setTypes();
        typesOfStone.setTypes();
        typesOfAnimalFood.setTypes();
        typesOfExploreTool.setTypes();
        typesOfFishingTool.setTypes();
        typesOfWateringTool.setTypes();
        typesOfKitchenTool.setTypes();

        for(int i = 0 ; i < typesOfNonMeat.types.size() ; i++){
            types.add(typesOfNonMeat.types.get(i));
        }
        for(int i = 0 ; i < typesOfFruit.types.size() ; i++){
            types.add(typesOfFruit.types.get(i));
        }
        for(int i = 0 ; i < typesOfMeat.types.size() ; i++){
            types.add(typesOfMeat.types.get(i));
        }
        for(int i = 0 ; i < typesOfThread.types.size() ; i++){
            types.add(typesOfThread.types.get(i));
        }
        for(int i = 0 ; i < typesOfSkinProduct.types.size() ; i++){
            types.add(typesOfSkinProduct.types.get(i));
        }
        for(int i = 0 ; i < typesOfFish.types.size() ; i++){
            types.add(typesOfFish.types.get(i));
        }
        for(int i = 0 ; i < typesOfWood.types.size() ; i++){
            types.add(typesOfWood.types.get(i));
        }
        for(int i = 0 ; i < typesOfStone.types.size() ; i++){
            types.add(typesOfStone.types.get(i));
        }
        for(int i = 0 ; i < typesOfAnimalFood.types.size() ; i++){
            types.add(typesOfAnimalFood.types.get(i));
        }
        for(int i = 0 ; i < typesOfExploreTool.types.size() ; i++){
            types.add(typesOfExploreTool.types.get(i));
        }
        for(int i = 0 ; i < typesOfFishingTool.types.size() ; i++){
            types.add(typesOfFishingTool.types.get(i));
        }
        for(int i = 0 ; i < typesOfWateringTool.types.size() ; i++){
            types.add(typesOfWateringTool.types.get(i));
        }
        for(int i = 0 ; i < typesOfKitchenTool.types.size() ; i++){
            types.add(typesOfKitchenTool.types.get(i));
        }


        Random random = new Random();
        deadline = random.nextInt(maxDeadline) + game.day;
        int demandingItemsNumber = random.nextInt(maxDemandingItemTypeInMission) + 1;

        A : for(int i = 0 ; i < demandingItemsNumber ; i++){
            int numOfItemType = random.nextInt(types.size() - 1);
            int numOfItemNeeding = random.nextInt(maxNumOfDemandingItemInMission) + 1;
            DemandingItemInMission demandingItemInMission = new DemandingItemInMission();
            demandingItemInMission.item = types.get(numOfItemType);
            demandingItemInMission.numOfPurpose = numOfItemNeeding;
//            System.out.println(demandingItemInMission.item.getName());
            if (demandingItems.size() > 0 && demandingItemInMission != null && demandingItemInMission.item != null && demandingItemInMission.item.getName() != null){
                for(int j = 0 ; j < demandingItems.size() ; j++) {
//                System.out.println(demandingItems.get(j).item.getName());
                    if (demandingItemInMission.item.getName().equals(demandingItems.get(j).item.getName())) {
                        i--;
                        continue A;
                    }
                }
            }
//
            demandingItems.add(demandingItemInMission);
        }

        contractFee = random.nextInt(1000);
        prizeFee = random.nextInt(100000) + contractFee;
    }

    public void showStatus(){

    }

    @Override
    public String toString(int i) {
        if(i == 0) return "Mission :";
        if(i == 1) return "Mission Progress :";
        return "Mission :";
    }

    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();

        MyMenu myMenu = new MyMenu();
        if(!isActive){
            myMenu.options.add("Mission Briefing");
            myMenu.options.add("Accept this mission");
        }
        else {
            myMenu.options.add("Mission Briefing");
            myMenu.options.add("Remove this mission");
            myMenu.options.add("Mission Progress");
        }

        myMenus.add(myMenu);

        /* Mission Progress */
        myMenu = new MyMenu();
        for(int i = 0 ; i < demandingItems.size() ; i++){
            myMenu.options.add(demandingItems.get(i).item.getName() + demandingItems.get(i).item.getKind() + (demandingItems.get(i).item instanceof WateringTool ? "Watering Tool": "")+" (" + demandingItems.get(i).numThatReached + " /" +
                    demandingItems.get(i).numOfPurpose + ")");
        }
        myMenu.options.add("Done!");

        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        Scanner scanner = new Scanner(System.in);
        String response = new String();
        backAfterChoose = false;
        switch (numOfMenu) {
            case 0:
                if (!isActive) {
                    switch (numThatChosenFromMenu) {
                        case 1:
//                            backAfterChoose = true;
                            showDescription();
                            break;
                        case 2:
//                            backAfterChoose = true;
                            System.out.println("You Will accept this mission for " + contractFee + " Gill . Is this okay?(Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if (game.player.getMoney() >= contractFee) {
                                game.village.cafe.misiionBoard.activeMissions.add(this);
                                game.village.cafe.misiionBoard.setMyMenus(game);
                                game.village.cafe.misiionBoard.availableMissions.remove(this);
                                game.player.decreseMoney(contractFee);
                                isActive = true;
                            } else {
                                System.out.println("You don't have enough money!");
                            }
                            break;
                    }
                    if (numThatChosenFromMenu > 2)
                        System.out.println("Invalid Choice");
                } else {
                    switch (numThatChosenFromMenu) {
                        case 1:
                            backAfterChoose = true;
                            showDescription();
                            break;
                        case 2:
                            backAfterChoose = true;
                            System.out.println("You Will remove this mission that cost " + contractFee + " Gill . Is this okay?(Y/N)");
                            response = scanner.next();
                            while (!(response.equals("Y") || response.equals("N"))) {
                                System.out.println("Invalid Response");
                                response = scanner.next();
                            }
                            if(response.equals("Y")) {
                                for (int i = 0; i < demandingItems.size() ; i++){
                                    game.player.backpack.put(demandingItems.get(i).item , demandingItems.get(i).numThatReached);
                                }
                                game.village.cafe.misiionBoard.activeMissions.remove(this);
                                isActive = false;
                            }
                            break;
                        case 3:
                            involvedMenuNum++;
                            backAfterChoose = false;
                    }
                    if(numThatChosenFromMenu > 3)
                        System.out.println("Invalid Choice!");
                }
                break;
            case 1:
                backAfterChoose = true;
                if (numThatChosenFromMenu <= demandingItems.size()) {
                    System.out.println("Choose am item from backpack!");
                    game.player.backpack.showItems();
                    SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
                    if (item == null)
                        return;
                    if (item.item.getClass().equals(demandingItems.get(numThatChosenFromMenu - 1).item.getClass())) {
                        if (game.player.backpack.take(demandingItems.get(numThatChosenFromMenu - 1).item
                                , demandingItems.get(numThatChosenFromMenu - 1).numOfPurpose
                                        - demandingItems.get(numThatChosenFromMenu - 1).numThatReached)) {
                            demandingItems.get(numThatChosenFromMenu - 1).numThatReached = demandingItems.get(numThatChosenFromMenu - 1).numOfPurpose;
                        }
                        else{
                            demandingItems.get(numThatChosenFromMenu - 1).numThatReached += item.getNumber();
                            game.player.backpack.take(item.item , item.getNumber());
                        }
                    }
                }
                if (numThatChosenFromMenu == demandingItems.size() + 1 && check()) {
                    game.village.cafe.misiionBoard.activeMissions.remove(this);
                    game.player.increaseMoney(this.prizeFee);
                }
                else{
                    System.out.println("Invalid Choice!");
                }
                break;
        }
    }

    public boolean check(){
        for(int i = 0 ; i < demandingItems.size() ; i++){
            if(demandingItems.get(i).numOfPurpose > demandingItems.get(i).numThatReached){
                return false;
            }
        }
        return true;
    }

    public void showDescription(){
        System.out.println("Wanting :");
        for(int i = 0 ; i < demandingItems.size() ; i++){
            System.out.println(demandingItems.get(i).item.getName() +" (" + demandingItems.get(i).numThatReached + "/" +
                    demandingItems.get(i).numOfPurpose +  ")");
        }
        System.out.println("Contract Fee = " + contractFee);
        System.out.println("Prize Fee = " + prizeFee);
        System.out.println("Deadline = " + deadline);
    }
}

class MissionBoard extends MenuHaving {
    ArrayList<Mission> availableMissions;
    ArrayList<Mission> activeMissions = new ArrayList<Mission>();
    int maxAvailableMission;

    MissionBoard(){
        name = "Mission Board";
    }
    public void checkAvailableMissions(){

    }

    public void missionStart(int indexOfMission){

    }
    public void selectAnAvailableMission(int indexOfMission){

    }
    public void manageActiveMissions(int indexOfMission){

    }
    public void selectAnActiveMission(int indexOfMission){

    }
    public void cancelAMission(int indexOfMission){

    }

    @Override
    public String toString(int i) {
        return "Mission Board :";
    }

    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Check available missions");
        myMenu.options.add("Manage active missions");

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        availableMissions = new ArrayList<Mission>();
        Random random = new Random();

        int numOfAvailableMission = random.nextInt(10) + 1;

        for(int i = 0 ; i < numOfAvailableMission ; i++){
            Mission mission = new Mission(game);
            availableMissions.add(mission);
            myMenu.options.add("Mission " + (i + 1));
        }

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        for(int i = 0 ; i < activeMissions.size() ; i++){
            if(activeMissions.get(i).getMaxDeadline() < game.day){
                activeMissions.remove(i);
                i--;
            }
        }

        for(int i = 0 ; i < activeMissions.size() ; i++){
            myMenu.options.add("Mission " + (i + 1));
        }

        myMenus.add(myMenu);
    }

    @Override
    public void reduceInvolvedMenuNum() {
        switch (involvedMenuNum){
            case 1:
                involvedMenuNum--;
                break;
            case 2:
                involvedMenuNum -=2;
                break;
        }
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        switch (numOfMenu){
            case 0:
                switch (numThatChosenFromMenu){
                    case 1:
                        backAfterChoose = false;
                        involvedMenuNum++;
//                        backAfterChoose = false;
                        break;
                    case 2:
                        backAfterChoose = false;
                        involvedMenuNum += 2;
//                        setMyMenus(game);
                        break;
                }
                break;
            case 1:
                backAfterChoose = false;
                availableMissions.get(numThatChosenFromMenu - 1).setMyMenus(game);
                game.menuStack.push(availableMissions.get(numThatChosenFromMenu - 1));
                break;
            case 2:
//                myMenus.get(2).printMenu();
                backAfterChoose = false;
                activeMissions.get(numThatChosenFromMenu - 1).setMyMenus(game);
                game.menuStack.push(activeMissions.get(numThatChosenFromMenu - 1));
                break;
        }
    }
}

class NonPlantFood extends Item{
    private String name;
    private boolean isChangingNecessary;
    private boolean isChanged;
    private int sellingCost;
    private int buyingCost;
    private int effectOnHealthWithoutChanging;
    private int effectOnEnergyWithoutChanging;
    ArrayList<String> animalsWhichCanBeEarnedFrom = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChangingNecessary() {
        return isChangingNecessary;
    }

    public void setChangingNecessary(boolean changingNecessary) {
        isChangingNecessary = changingNecessary;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public int getSellingCost() {
        return sellingCost;
    }

    public void setSellingCost(int sellingCost) {
        this.sellingCost = sellingCost;
    }

    public int getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }

    public int getEffectOnHealthWithoutChanging() {
        return effectOnHealthWithoutChanging;
    }

    public void setEffectOnHealthWithoutChanging(int effectOnHealthWithoutChanging) {
        this.effectOnHealthWithoutChanging = effectOnHealthWithoutChanging;
    }

    public int getEffectOnEnergyWithoutChanging() {
        return effectOnEnergyWithoutChanging;
    }

    public void setEffectOnEnergyWithoutChanging(int effectOnEnergyWithoutChanging) {
        this.effectOnEnergyWithoutChanging = effectOnEnergyWithoutChanging;
    }
}

class SavingItem{
    private int number;
    Item item;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void decreaseNumber(int i){
        number -= i;
    }
}

class Backpack extends MenuHaving {
    ArrayList<SavingItem> savingItems = new ArrayList<>();
    private int capacity;
    private int chosenItem;

    public int getCapacity(){
        return capacity;
    }

    Backpack(int capacity){
        this.capacity = capacity;

        SavingItem savingItem = new SavingItem();
        savingItem.item = new Vegetable("Potato", 0, 0, 0, 0,0, "");
        savingItem.setNumber(3);
        savingItems.add(savingItem);
        savingItem = new SavingItem();

        savingItem = new SavingItem();
        savingItem.item = new NonMeat("Oil", 0, 0, 00, true);
        savingItem.setNumber(3);
        savingItems.add(savingItem);

        savingItem = new SavingItem();
        savingItem.item = new NonMeat("Salt", 0, 0, 00, true);
        savingItem.setNumber(3);
        savingItems.add(savingItem);

        savingItem = new SavingItem();
        savingItem.item = new KitchenTool("Knife", 10);
        savingItem.setNumber(1);
        savingItems.add(savingItem);

        savingItem = new SavingItem();
        savingItem.item = new KitchenTool("Pan", 10);
        savingItem.setNumber(1);
        savingItems.add(savingItem);



    }

    public boolean put(Item item, int number) {
        if (number == 0)
            return false;
        if (item.isPackable()) {
            for (int i = 0; i < savingItems.size(); i++) {
                if (item.getName().equals(savingItems.get(i).item.getName())) {
                    savingItems.get(i).setNumber(savingItems.get(i).getNumber() + number);
                    return true;
                }
            }
        }
        if (savingItems.size() < capacity) {
            SavingItem temp = new SavingItem();
            temp.item = item;
            temp.setNumber(number);
            savingItems.add(temp);
            return true;
        }
        else
            return false;
    }

    public boolean checkToPut(Item item, int number) {
        if (item.isPackable()) {
            for (int i = 0; i < savingItems.size(); i++) {
                if (item.getName().equals(savingItems.get(i).item.getName())) {
//                    savingItems.get(i).setNumber(savingItems.get(i).getNumber() + number);
                    return true;
                }
            }
        }
        if (savingItems.size() < capacity) {
//            SavingItem temp = new SavingItem();
//            temp.item = item;
//            temp.setNumber(number);
//            savingItems.add(temp);
            return true;
        }
        else
            return false;
    }

    public boolean checkToPutAll(ArrayList<DemandingItem> demandingItems){
        boolean output = true;
        for(int i = 0 ; i < demandingItems.size() ; i++){
            output &= checkToPut(demandingItems.get(i).Item , demandingItems.get(i).getDemandingNumber());
        }
        return output;
    }

    public boolean take(Item item, int number){
        for (int i = 0; i < savingItems.size(); i++) {
            if (item.getName().equals(savingItems.get(i).item.getName())) {
                if (number <= savingItems.get(i).getNumber()) {
                    savingItems.get(i).setNumber(savingItems.get(i).getNumber() - number);
                    check();
                    return true;
                }
            }
        }
        return false;
    }

    public void check(){
        ArrayList<SavingItem> deletingOnes = new ArrayList<>();

        for (int j = 0; j < savingItems.size(); j++) {
            if (savingItems.get(j).getNumber() == 0) {
                deletingOnes.add(savingItems.get(j));
            }
        }
        for (int i = 0; i < deletingOnes.size(); i++) {
            savingItems.remove(deletingOnes.get(i));
        }
    }
    public void showItems(){
        for (int i = 0; i < capacity; i++) {
            if (i < savingItems.size() && savingItems.get(i) != null) {
                System.out.print((i+1) + ". " + savingItems.get(i).item.getName() + " " + savingItems.get(i).item.getKind() +
                        "" + (savingItems.get(i).item instanceof WateringTool ? "Watering Tool":""));
                if (savingItems.get(i).item.isPackable()) {
                    System.out.println("  x" + savingItems.get(i).getNumber());
                }
                else
                    System.out.println();
            }
            else if (i >= savingItems.size())
                System.out.println("Empty");
        }
    }

    public SavingItem selectAnItem(int indexOfItem){
        if (indexOfItem - 1 >= savingItems.size()){
            System.out.println("It's Empty You Mad Man !!");
            return null;
        }
        return savingItems.get(indexOfItem - 1);
    }

    @Override
    public String toString(int i) {
        return i == 0 ? "Backpack :" : "";
    }

    @Override
    public void reduceInvolvedMenuNum() {
        switch (involvedMenuNum){
            case 1:
                involvedMenuNum--;
                break;
            case 2:
                involvedMenuNum -= 2;
                break;
        }
    }

    /*
       menu 0:
    1.item 1
    2.item 2
    ...
       menu 1:
    1.status
    2.drop

       menu2:
    1.status
    2.use
    3.drop
    */

    @Override
    public void setMyMenus(Game game) {
        myMenus.clear();

        MyMenu myMenu = new MyMenu();

        for (int i = 0; i < savingItems.size(); i++) {
            myMenu.options.add(savingItems.get(i).item.getName() + " " + savingItems.get(i).item.getKind() +
                    " " + (savingItems.get(i).item instanceof WateringTool ? "Watering Tool":"" + (savingItems.get(i).item.isPackable() ? " x" +
                    savingItems.get(i).getNumber() : "") ));
        }

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        myMenu.options.add("Status");
        myMenu.options.add("Drop this item");

        myMenus.add(myMenu);

        myMenu = new MyMenu();

        myMenu.options.add("Status");
        myMenu.options.add("Use");
        myMenu.options.add("Drop this item");

        myMenus.add(myMenu);
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        switch (numOfMenu){
            case 0:
                if (numThatChosenFromMenu <= savingItems.size()) {
                    if(savingItems.get(numThatChosenFromMenu - 1).item.isHavingUseInBackpackMenu())
                        involvedMenuNum += 2;
                    else
                        involvedMenuNum++;
//                    game.menuStack.push(this);
                    chosenItem = numThatChosenFromMenu - 1;
                }
                else {
                    System.out.println("Invalid Choice");
                }
                backAfterChoose = false;
                break;
            case 1:
                backAfterChoose = true;
//                involvedMenuNum++;
                switch (numThatChosenFromMenu) {
                    case 1:
                        savingItems.get(chosenItem).item.showStatus();
                        break;
                    case 2:
                        if(savingItems.size() >= chosenItem) {
                            savingItems.remove(chosenItem);
                        }
                        else{
                            System.out.println("No Item !!");
                        }
                        break;
                }
                if(numThatChosenFromMenu > 2){
                    System.out.println("Invalid Choice");
                }
                break;
            case 2:
                backAfterChoose = true;
//                involvedMenuNum++;
                if(chosenItem <= savingItems.size()) {
                    switch (numThatChosenFromMenu) {
                        case 1:
                            savingItems.get(chosenItem).item.showStatus();
                            break;
                        case 2:
                            boolean dropAfterUse = savingItems.get(chosenItem).item.useInBackpack(game);
//                            savingItems.get(chosenItem).item.use();
                            if (dropAfterUse) {
                                if (savingItems.get(chosenItem).getNumber() == 1)
                                    savingItems.remove(chosenItem);
                                else
                                    savingItems.get(chosenItem).decreaseNumber(1);
                            }
                            break;
                        case 3:
                            savingItems.remove(chosenItem);
                            break;
                    }
                }
                else {
                    System.out.println("Invalid Choice");
                }
                break;
        }
    }
}

class RawMaterial extends Item{
    private String name;
    //    private String kind;
    private int quality;
    private static int qualityFactor = 3;

//    public void setKind(String kind) {
//        this.kind = kind;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}

class Fish extends NonPlantFood{
    Fish(){

    }
}

class River extends MenuHaving {
    ArrayList<Fish> fish;
    private String name;

    River(){
        setName("River");
    }
    public void fishing(){

    }

    @Override
    public String toString(int i) {
        return i == 0 ? "River :" : null;
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        backAfterChoose = false;
        if (numOfMenu == 0){
            if (numThatChosenFromMenu == 1){
                startFishing(game);
            }
        }
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Start fishing");

        myMenus.add(myMenu);
    }

    public void startFishing(Game game){
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select A Fishing Tool From BackPack!");

        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getClass().equals(FishingTool.class))
        {
            System.out.println("Not A Fishing Tool");
            return;
        }
        else {
            FishingTool fishingTool = FishingTool.class.cast(item.item);
            if (fishingTool.isBroken())
            {
                System.out.println("The Fishing Tool is broken.");
                return;
            }
            else
            {
                int n = getPossibleNumber(fishingTool.getProbablityOfCatching()/20 - 1, 0);

                if (game.player.backpack.put(fish.get(0), n)){
                    int loosingEnergy = getLoosingEnergy(fishingTool.getProbablityOfCatching()/20 - 1, 0);
                    if (game.player.use(game, loosingEnergy)){

                        int chance = random.nextInt(100) + 1;
                        if (chance <= fishingTool.getProbablityOfCatching())
                        {
                            fishingTool.breakIt(fishingTool.getProbablityOfBreak(), fishingTool.getName());
                            int doIt = 1;
                            System.out.println(n + " Fish Catched" + loosingEnergy + " Energy decreased.");
                        }
                    }
                    else {
                        game.player.backpack.take(fish.get(0), n);
                        System.out.println("No Energy");
                        return;
                    }
                }
                else {
                    System.out.println("No Room In Backpack");
                    return;
                }


            }
        }
    }

    public int getLoosingEnergy(int qualityOfTool, int qualityOfMaterial){
        double n = (30 * Math.pow(2., qualityOfMaterial))/Math.pow(1.6, qualityOfTool);

        return (int) n;
    }
    public int getPossibleNumber(int qualityOfTool, int qualityOfMaterial){
        double n = (2.5 * Math.pow(2., qualityOfTool + 1))/Math.pow(2., qualityOfMaterial);
        Random rand = new Random();
        return rand.nextInt((int) Math.floor(n)) + 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}

class RockInCave extends MenuHaving {

    RockInCave(){
        setName("Rocks");
    }
    public void showRocks(){

    }

    public void selectType(){

    }

    @Override
    public String toString(int i) {
        return i == 0 ? "RockInCave :" : null;
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {

        backAfterChoose = false;
        if (numOfMenu == 0){
            if (numThatChosenFromMenu == 1){
                collect(game, 1);
            }
            else if (numThatChosenFromMenu == 2){
                collect(game, 2);
            }
            else if (numThatChosenFromMenu == 3){
                collect(game, 3);
            }
            else if (numThatChosenFromMenu == 4){
                collect(game, 4);
            }
        }
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Collect Stones");
        myMenu.options.add("Collect Iron");
        myMenu.options.add("Collect Silver");
        myMenu.options.add("Collect Adamantium");

        myMenus.add(myMenu);
    }


    public void collect(Game game, int quality){
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        int n;
        TypesOfStone typesOfStone = new TypesOfStone();
        typesOfStone.setTypes();

        if (quality == 1){
            n = getPossibleNumber(0, 0);
            if (game.player.backpack.put(typesOfStone.types.get(0), n)){
                System.out.println(n + typesOfStone.types.get(0).getName() + " were collected.");
            }
            return;
        }


        else if (quality == 2) {
            System.out.println("Select A Pick (StoneMade Or Higher Quality) From BackPack!");
        }
        else if (quality == 3) {
            System.out.println("Select A Pick (IronMade Or Higher Quality) From BackPack!");

        }
        else if (quality == 4) {
            System.out.println("Select A Pick (AdamantiumMade Or Higher Quality) From BackPack!");

        }
        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getClass().equals(ExploreTool.class)) {
            System.out.println("Not A Pick");
            return;
        }
        else if (!ExploreTool.class.cast(item.item).getKind().equals("Pick")){
            System.out.println("Not An Pick");
            return;
        }
        else {
            ExploreTool exploreTool = ExploreTool.class.cast(item.item);
            if (exploreTool.isBroken()) {
                System.out.println("The pick is broken.");
                return;
            }
            else if (exploreTool.getQuality() >= quality - 1) {
                n = getPossibleNumber(exploreTool.getQuality(), quality);

                if (game.player.backpack.put(typesOfStone.types.get(quality - 1), n)) {
                    int loosingEnergy = getLoosingEnergy(exploreTool.getQuality(), quality);
                    if (game.player.use(game, loosingEnergy)){
                        System.out.println(n + typesOfStone.types.get(quality - 1).getName() + " were collected." + loosingEnergy + " Energy decreased.");

                    }
                    else {
                        System.out.println("No Energy!");
                    }
                }
                else {
                    System.out.println("No Room in Backpack!");
                }
            }
            else {
                System.out.println("Quality doesn't match");
                return;
            }
        }
    }
    public int getLoosingEnergy(int qualityOfTool, int qualityOfMaterial){
        double n = (30 * Math.pow(2., qualityOfMaterial))/Math.pow(1.6, qualityOfTool);

        return (int) n;
    }
    public int getPossibleNumber(int qualityOfTool, int qualityOfMaterial){
        double n = (2.5 * Math.pow(2., qualityOfTool + 1))/Math.pow(2., qualityOfMaterial);
        Random rand = new Random();
        return rand.nextInt((int) Math.floor(n)) + 0;
    }

}

class Rocks extends MenuHaving {
    ArrayList<Stone> stones;

    Rocks(){
        setName("Rocks");
    }
    public void showRocks(){

    }

    public int getPossibleNumber(int qualityOfTool, int qualityOfMaterial){
        double n = (2.5 * Math.pow(2., qualityOfTool + 1))/Math.pow(2., qualityOfMaterial);
        Random rand = new Random();
        return rand.nextInt((int) Math.floor(n)) + 0;
    }

    @Override
    public String toString(int i) {
        return i == 0 ? "Rocks :" : null;
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        if (numOfMenu == 0){
            backAfterChoose = false;
            if (numThatChosenFromMenu == 1){
                TypesOfStone typesOfStone = new TypesOfStone();
                typesOfStone.setTypes();

                int n = getPossibleNumber(0,0);
                if (game.player.backpack.put(typesOfStone.types.get(0),n)){
                    System.out.println(n + " Stones collected");
                }
                else if (n != 0){
                    System.out.println("No Room In backpack");
                    return;
                }
            }
        }
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Collect Stones");

        myMenus.add(myMenu);
    }
}

class Vegetable extends FruitAndVegetable{

    Vegetable(String name, int effecOnHealthMax, int effectOnEnergyMax, int effectOnHealth, int effectOnEnergy, int price, String season){
        super(name, effecOnHealthMax, effectOnEnergyMax, effectOnHealth, effectOnEnergy, price, season);
    }
}

class Fruit extends FruitAndVegetable{
    Fruit(String name, int effectOnHealthMax, int effectOnEnergyMax, int effectOnHealth, int effectOnEnergy, int price, String season) {
        super(name, effectOnHealthMax, effectOnEnergyMax, effectOnHealth, effectOnEnergy, price, season);
    }
}

class NonMeat extends NonPlantFood{

    NonMeat(String name, int effectOnHealth, int effectOnEnergy, int buyingCost, boolean isChangingNecessary){
        setName(name);
        setBuyingCost(buyingCost);
        setEffectOnEnergyWithoutChanging(effectOnEnergy);
        setEffectOnHealthWithoutChanging(effectOnHealth);
        setChangingNecessary(isChangingNecessary);
        setBackpackable(true);
    }

    @Override
    public void showStatus() {
        System.out.println("can be used while cooking certain fooods. effecOnEnergy" +getEffectOnEnergyWithoutChanging() + " effectOnHealth "+ getEffectOnHealthWithoutChanging());
    }

    public  void showDescription(){

    }

}

//class IngredientsOfCookingItem extends Item{
//
//}

class SkinProduct extends RawMaterial{
    private String description;
    private int buyingCost;
    ArrayList<String> animalsCanBeEarnedFrom;

    SkinProduct(String name, String description, int buyingCost, ArrayList<String> animalsCanBeEarnedFrom , String kind){
        setName(name);
        setDescription(description);
        setQuality(0);
        setBuyingCost(buyingCost);
        this.animalsCanBeEarnedFrom = animalsCanBeEarnedFrom;
        setBackpackable(true);
        super.setKind(kind);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }
    public void showStatus(){
        System.out.println("Sheep Wool . By using a spinning wheel you can turn it into thread.");
    }
}

class Stone extends RawMaterial{
    private String description;
    private int buyingCost;
    private boolean canBeEarnedFromJungle;

    Stone(String name, String description, int quality, int buyingCost, boolean canBeEarnedFromJungle , String kind){
        setName(name);
        setDescription(description);
        setQuality(quality);
        setBuyingCost(buyingCost);
        setCanBeEarnedFromJungle(canBeEarnedFromJungle);
        setBackpackable(true);
        setKind(kind);
    }
    public int getBuyingCost() {
        return buyingCost;
    }

    public void showStatus(){
        if (getName().equals("Stone")){
            System.out.println("Stone");
            System.out.println("Status : Bunch Of Stones collected from the ground. Can Be used to create specific items at the workshop");
        }
        else {
            System.out.print("Status : ");
            if (getQuality() == 2){
                System.out.print("Normal ");
            }
            else if (getQuality() == 3){
                System.out.print("Strong ");
            }
            else if (getQuality() == 4){
                System.out.print("Very Strong ");
            }
            System.out.print("type Of metal. Can Be used to create specific items at the workshop");
        }
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCanBeEarnedFromJungle() {
        return canBeEarnedFromJungle;
    }

    public void setCanBeEarnedFromJungle(boolean canBeEarnedFromJungle) {
        this.canBeEarnedFromJungle = canBeEarnedFromJungle;
    }
}

class Thread extends RawMaterial{
    private int buyingCost;

    Thread(String name, int buyingCost, int quality , String kind){
        setName(name);
        setBuyingCost(buyingCost);
        setQuality(quality);
        setBackpackable(true);
        setKind(kind);
    }
    public void showStatus(){
        System.out.println(" Can Be used to create specific items at the workshop");
    }
    public int getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(int buyingCost) {
        this.buyingCost = buyingCost;
    }
}

class Juice extends NonPlantFood{
    private String name =new String();
    //    private String name;
    private int effectOnHealth;
    private int effectOnEnergy;
    private int effectOnEnergyMax;
    private int effectOnHealthMax;
    private static int perentageOfEffect = 125;
    private static int percentageOfPrice = 120;
    private int price;

    Juice(FruitAndVegetable raw){
        this.name = raw.getName() + " Juice";
        this.effectOnEnergy = raw.getEffectOnEnergy() * ((perentageOfEffect)/100);
        this.effectOnEnergyMax = raw.getEffectOnEnergyMax() * ((perentageOfEffect)/100);
        this.effectOnHealthMax = raw.getEffectOnHealthMax() * ((perentageOfEffect)/100);
        this.effectOnHealth = raw.getEffectOnHealth() * ((perentageOfEffect)/100);
        this.price = raw.getPrice() * ((percentageOfPrice)/100);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getEffectOnHealth() {
        return effectOnHealth;
    }

    public void setEffectOnHealth(int effectOnHealth) {
        this.effectOnHealth = effectOnHealth;
    }

    public int getEffectOnEnergy() {
        return effectOnEnergy;
    }

    public void setEffectOnEnergy(int effectOnEnergy) {
        this.effectOnEnergy = effectOnEnergy;
    }

    public int getEffectOnEnergyMax() {
        return effectOnEnergyMax;
    }

    public void setEffectOnEnergyMax(int effectOnEnergyMax) {
        this.effectOnEnergyMax = effectOnEnergyMax;
    }

    public int getEffectOnHealthMax() {
        return effectOnHealthMax;
    }

    public void setEffectOnHealthMax(int effectOnHealthMax) {
        this.effectOnHealthMax = effectOnHealthMax;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

class Wood extends RawMaterial{
    private String description;
    private boolean jungleFindable;
    private int price;
//    private String kind;

    Wood(String name, String description, int quality, int price, boolean jungleFindable , String kind){
        setName(name);
        setDescription(description);
        setQuality(quality);
        setPrice(price);
        setJungleFindable(jungleFindable);
        setBackpackable(true);
//        System.out.println(kind);
        setKind(kind);
//        System.out.println(getKind());
    }
//    public String getKind() {
//        return description;
//    }

    public void showStatus(){
        if (getName().equals("Branch")){
            System.out.println("Branch");
            System.out.println("Status : Weak tree branch. Can Be used to create specific items at the workshop");
        }
        else {
            System.out.print("Status : ");
            if (getQuality() == 2){
                System.out.print("Normal ");
            }
            else if (getQuality() == 3){
                System.out.print("Strong ");
            }
            else if (getQuality() == 4){
                System.out.print("Very Strong ");
            }
            System.out.print("wood gotten from " + getName() + "\b\b\b\b\b\b" + " tree. Can Be used to create specific items at the workshop");
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isJungleFindable() {
        return jungleFindable;
    }

    public void setJungleFindable(boolean jungleFindable) {
        this.jungleFindable = jungleFindable;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

class Food extends Item{
    private String name = new String();
    private int effectOnHealthMax;
    private int effectOnEnergyMax;
    private int effectOnHealth;
    private int effectOnEnergy;

    Food(String name, int effectOnHealthMax, int effectOnHealth, int effectOnEnergy) {
        setName(name);
        setEffectOnEnergy(effectOnEnergy);
        setEffectOnHealth(effectOnHealth);
        setEffectOnHealthMax(effectOnHealthMax);
        setPackable(true);
        setHavingUseInBackpackMenu(true);
    }

    @Override
    public void showStatus() {
        System.out.println("effect On Health : " + effectOnHealth + " effectOnEnergy" + effectOnEnergy + " effectOn Health Max:" + effectOnHealthMax);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getEffectOnHealthMax() {
        return effectOnHealthMax;
    }

    public void setEffectOnHealthMax(int effectOnHealthMax) {
        this.effectOnHealthMax = effectOnHealthMax;
    }

    public int getEffectOnEnergyMax() {
        return effectOnEnergyMax;
    }

    public void setEffectOnEnergyMax(int effectOnEnergyMax) {
        this.effectOnEnergyMax = effectOnEnergyMax;
    }

    public int getEffectOnHealth() {
        return effectOnHealth;
    }

    public void setEffectOnHealth(int effectOnHealth) {
        this.effectOnHealth = effectOnHealth;
    }

    public int getEffectOnEnergy() {
        return effectOnEnergy;
    }

    public void setEffectOnEnergy(int effectOnEnergy) {
        this.effectOnEnergy = effectOnEnergy;
    }

    @Override
    public boolean useInBackpack(Game game) {
        game.player.getHealth().increaseCurrentAmount(getEffectOnHealth());
        game.player.getHealth().increaseMaxAmount(getEffectOnHealthMax());
        game.player.getEnergy().increaseCurrentAmount(game , getEffectOnEnergy());
        game.player.getEnergy().increaseMaxAmount(getEffectOnEnergyMax());
        return true;
    }
}
class Woods extends MenuHaving {

    Woods(){
        setName("Woods");
    }

    public void showWoods(){

    }

    public void selectType(int indexOfWood){

    }

    @Override
    public String toString(int i) {
        return i == 0 ? "Woods :" : null;
    }

    @Override
    public void chooseFromMenu(int numThatChosenFromMenu, int numOfMenu, Game game) {
        if (numOfMenu == 0){
            backAfterChoose = false;
            if (numThatChosenFromMenu == 1){
                collect(game, 1);
            }
            else if (numThatChosenFromMenu == 2){
                collect(game, 2);
            }
            else if (numThatChosenFromMenu == 3){
                collect(game, 3);
            }
            else if (numThatChosenFromMenu == 4){
                collect(game, 4);
            }
        }
    }

    @Override
    public void setMyMenus(Game game) {
        MyMenu myMenu = new MyMenu();

        myMenu.options.add("Collect Branch");
        myMenu.options.add("Collect Old Lumber");
        myMenu.options.add("Collect Pine Lumber");
        myMenu.options.add("Collect Oak Lumber");

        myMenus.add(myMenu);
    }

    public void collect(Game game, int quality){
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        int n;
        TypesOfWood typesOfWood = new TypesOfWood();
        typesOfWood.setTypes();

        if (quality == 1){
            n = getPossibleNumber(0, 0);
            if (game.player.backpack.put(typesOfWood.types.get(0), n)){
//                System.out.println(typesOfWood.types.get(0).getKind());
                System.out.println(n + typesOfWood.types.get(0).getName() + " were collected.");
            }
            return;
        }


        else if (quality == 2) {
            System.out.println("Select A Axe (StoneMade Or Higher Quality) From BackPack!");
        }
        else if (quality == 3) {
            System.out.println("Select A Axe (IronMade Or Higher Quality) From BackPack!");

        }
        else if (quality == 4) {
            System.out.println("Select A Axe (SilverMade Or Higher Quality) From BackPack!");

        }
        game.player.backpack.showItems();
        SavingItem item = game.player.backpack.selectAnItem(scanner.nextInt());
        if (item == null)
            return;
        if (!item.item.getClass().equals(ExploreTool.class)) {
            System.out.println("Not An Axe");
            return;
        }
        else if (!ExploreTool.class.cast(item.item).getKind().equals("Axe")){
            System.out.println("Not An Axe");
            return;
        }
        else {
            ExploreTool exploreTool = ExploreTool.class.cast(item.item);
            if (exploreTool.isBroken()) {
                System.out.println("The Axe is broken.");
                return;
            }
            else if (exploreTool.getQuality() >= quality - 1) {
                n = getPossibleNumber(exploreTool.getQuality(), quality);

                if (game.player.backpack.put(typesOfWood.types.get(quality - 1), n)) {
                    int loosingEnergy = getLoosingEnergy(exploreTool.getQuality(), quality);
                    if (game.player.use(game, loosingEnergy)){
                        exploreTool.breakIt(10, exploreTool.getName());
                        System.out.println(n + typesOfWood.types.get(quality - 1).getName() + " were collected." + loosingEnergy + "Energy decreased.");

                    }
                    else {
                        System.out.println("No Energy!");
                    }
                }
                else {
                    System.out.println("No Room in Backpack!");
                }
            }
            else {
                System.out.println("Quality doesn't match");
                return;
            }
        }
    }

    public int getLoosingEnergy(int qualityOfTool, int qualityOfMaterial){
        double n = (30 * Math.pow(2., qualityOfMaterial))/Math.pow(1.6, qualityOfTool);

        return (int) n;
    }
    public int getPossibleNumber(int qualityOfTool, int qualityOfMaterial){
        double n = (2.5 * Math.pow(2., qualityOfTool + 1))/Math.pow(2., qualityOfMaterial);
        Random rand = new Random();
        return rand.nextInt((int) Math.floor(n)) + 0;
    }
}

class TypesOfTool{
    ArrayList<Tool> types = new ArrayList<>();

    public void setTypes(){
        Tool temp = new Tool();
        temp.setName("Milker");
        temp.setDemandingMoneyToBuild(100);
        temp.setBackpackable(false);
        temp.setDemandingMoneyToRepair(20);
        temp.setBuyingCost(100);
        types.add(temp);

        temp = new Tool();
        temp.setName("Scissors");
        temp.setDemandingMoneyToBuild(100);
        temp.setBackpackable(false);
        temp.setDemandingMoneyToRepair(20);
        temp.setBuyingCost(100);
        types.add(temp);

    }
}
class TypesOfDrug{
    ArrayList<Drug> types = new ArrayList<>();

    public void setTypes(){
        Drug temp = new Drug("Normal" , 100 , 150 , "Drug");
        types.add(temp);

        temp = new Drug("Strong" , 250 , 350 , "Drug");
        types.add(temp);

        temp = new Drug("Super" , 500 , 600 , "Drug");
        types.add(temp);
    }

}

class TypesOfExploreTool{
    ArrayList<ExploreTool> types = new ArrayList<>();

    public void setTypes(){
        ExploreTool temp = new ExploreTool("Shovel", "StoneMade", 1,150, 100, 10);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Branch", 5 , 1 , "Wood"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Stone", 5 , 1 , "Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Branch", 2 , 1 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Stone", 2 , 1 , "Stone"));
        types.add(temp);

        temp = new ExploreTool("Shovel", "IronMade", 2, 80, 500, 100);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Iron", 4 , 2 , "Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Old Lumber", 4 , 2 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Iron", 2 , 2 , "Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Old Lumber", 2 , 2 ,"Wood"));
        types.add(temp);

        temp = new ExploreTool("Shovel", "SilverMade", 3, 40, 1000, 100);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Silver", 3 , 3 ,"Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Pine Lumber", 3 , 3 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Silver", 1 , 3 , "Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Pine Lumber", 1 , 3 , "Wood"));
        types.add(temp);

        temp = new ExploreTool("Shovel","AdamnatiumMade", 4, 20, 4000, 400);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Adamantium", 2 , 4 , "Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Acorn Lumber", 2 , 4 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Adamantium", 1 , 4 , "Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Acorn Lumber", 1 , 4 , "Wood"));
        types.add(temp);

        temp = new ExploreTool("Pick","StoneMade", 1, 0, 200, 20);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Stone", 10 , 1 , "Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Branch", 5 , 1 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Stone", 5 , 1 ,"Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Branch", 2 , 1 ,"Wood"));
        types.add(temp);

        temp = new ExploreTool("Pick","IronMade", 2, 0, 800, 80);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Iron", 8 , 2 , "Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Old Lumber", 4 , 2 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Iron", 4 , 2 ,"Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Old Lumber", 2 , 2 ,"Wood"));
        types.add(temp);

        temp = new ExploreTool("Pick","SilverMade", 3, 0, 2000, 200);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Silver", 6 , 3 , "Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Pine Lumber", 3 , 3 ,"Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Silver", 3 , 3 , "Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Pine Lumber", 1 , 3 , "Wood"));
        types.add(temp);

        temp = new ExploreTool("Pick","AdamantiumMade", 4, 0, 7000, 700);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Adamantium", 4 , 4 ,"Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Acorn Lumber", 2 , 4 ,"Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Adamantium", 2 , 4 , "Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Acorn Lumber", 1 , 4 ,"Wood"));
        types.add(temp);

        temp = new ExploreTool("Axe","StoneMade", 1, 0, 200, 20);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Stone", 5 , 1 ,"Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Branch", 10 , 1 ,"Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Stone", 2 , 1 , "Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Branch", 5 , 1 , "Wood"));
        types.add(temp);

        temp = new ExploreTool("Axe","IronMade", 2, 0, 800, 80);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Iron", 4 , 2 , "Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Old Lumber", 8 , 2 ,"wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Iron", 2 , 2 , "Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Old Lumber", 4 , 2 ,"Wood"));
        types.add(temp);

        temp = new ExploreTool("Axe","SilverMade", 3, 0, 2000, 200);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Silver", 3 , 3 , "Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Pine Lumber", 6 , 3 ,"Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Silver", 1 , 3 , "Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Pine Lumber", 3 , 3 , "Wood"));
        types.add(temp);

        temp = new ExploreTool("Axe","AdamantiumMade", 4, 0, 7000, 700);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Adamantium", 2 , 4 ,"Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Acorn Lumber", 4 , 4 ,"Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Adamantium", 1 , 4 ,"Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Acorn Lumber", 2 , 4 ,"Wood"));
        types.add(temp);
    }

}

class TypesOfAnimalMedicine{
    ArrayList<AnimalMedicine> types = new ArrayList<>();

    public void setTypes(){
        AnimalMedicine temp = new AnimalMedicine("AnimalMedicine", 150);
        types.add(temp);
    }
}

class TypesOfFish{
    ArrayList<Fish> types = new ArrayList<Fish>();

    public  void setTypes(){
        Fish temp = new Fish();
        types.add(temp);
    }
}

class TypesOfFishingTool{
    ArrayList<FishingTool> types = new ArrayList<>();

    public void setTypes(){
        FishingTool temp = new FishingTool("Small", 100, 10, 0, 40, 20, "Fishing Tool");
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Branch", 15 , 1 ,"Wood"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Thread", 2 , 1 , "Thread"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Branch", 5 , 1 ,"Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Thread", 1 , 1 ," Wood"));
        types.add(temp);

        temp = new FishingTool("Old", 300, 30, 0, 60, 15, "Fishing Tool");
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Old Lumber", 10 , 2 ,"Wood"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Thread", 3 , 1 ,"Thread"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Old Lumber", 4 , 2 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Thread", 2 , 1 ,"Thread"));
        types.add(temp);

        temp = new FishingTool("Pine", 800, 80, 0, 80, 10, "Fishing Tool");
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Pine Lumber", 6 , 3 , "Wood"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Thread", 4 , 1 , "Thread"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Pine Lumber", 3 , 3 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Thread", 1 , 1 ,"Thread"));
        types.add(temp);


        temp = new FishingTool("Acorn", 2000, 200, 0, 100, 5, "Fishing Tool");
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Acorn Lumber", 3 , 3 ,"Wood"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Thread", 5 , 1 ,"Thread"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Acorn Lumber", 2 , 3 ,"Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Thread", 1 , 1 , "Thread"));
        types.add(temp);
    }
}

class TypesOfFruit{
    ArrayList<Fruit> types = new ArrayList<>();
    public void setTypes(){
        types.add(new Fruit("Peach", 0, 0, 15, 0, 15, "Spring"));
        types.add(new Fruit("Pear", 0, 0, 0, 20, 15, "Spring"));
        types.add(new Fruit("Lemon", 0, 0, 0, 20, 15, "Summer"));
        types.add(new Fruit("Pomegranate", 0, 5, 15, 15, 25, "Summer"));
        types.add(new Fruit("Cucumber", 0, 0, 0, 10, 10, "Summer"));
        types.add(new Fruit("Watermelon", 0, 10, 10, 50, 80, "Summer"));
        types.add(new Fruit("Apple", 5, 0, 0, 10, 20, "'Fall"));
        types.add(new Fruit("Orange", 0, 5, 10, 0, 20, "Fall"));

        types.add(new Fruit("Melon", 0, 5, 10, 40, 60, "Fall"));
        types.add(new Fruit("Pineapple", 15, 15, 15, 15, 150, "Tropical"));
        types.add(new Fruit("Strawberry", 5, 5, 10, 10, 50, "Tropical"));
    }
}

class TypesOfKitchenTool{
    ArrayList<KitchenTool> types = new ArrayList<>();

    public void setTypes(){
        KitchenTool temp = new KitchenTool("Pan", 10);
        temp.demandingRawMaterialsToBuild.add( new DemandingRawMaterial("Iron", 7 , 2 ,"Stone"));
        types.add(temp);

        temp = new KitchenTool("Knife", 20);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Iron", 3 , 2 , "Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Old Lumber", 2 , 2 ,"Wood"));
        types.add(temp);

        temp = new KitchenTool("Blazer", 10);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Silver", 5 , 3 , "Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Pine Lumber", 3 , 3 ,"Wood"));
        types.add(temp);

        temp = new KitchenTool("Deeg", 20);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Stone", 10 , 1 , "Stone"));
        types.add(temp);

        temp = new KitchenTool("Oven", 5);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Adamantium", 3 , 4 ,"Stone"));
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Acorn", 3 , 4, "Wood"));
        types.add(temp);

    }
}

class TypesOfMeat{
    ArrayList<Meat> types = new ArrayList<>();

    public void setTypes(){
        Meat temp = new Meat("Cow Meat", 100, true);
        temp.animalsWhichCanBeEarnedFrom.add("Cow");
        types.add(temp);

        temp = new Meat("Sheep Meat", 150, true);
        temp.animalsWhichCanBeEarnedFrom.add("Sheep");
        types.add(temp);

        temp = new Meat("Hen Meat", 70, true);
        temp.animalsWhichCanBeEarnedFrom.add("Hen");
        types.add(temp);

        temp = new Meat("Fish Meat", 200, true);
        temp.animalsWhichCanBeEarnedFrom.add("Fish");
        types.add(temp);
    }
}

class TypesOfNonMeat{
    ArrayList<NonMeat> types = new ArrayList<>();

    public void setTypes(){
        NonMeat temp = new NonMeat("Cow Milk", 20, 20, 60, false);
        types.add(temp);
        temp = new NonMeat("Bread", 5, 10, 20, false);
        types.add(temp);
        temp = new NonMeat("Cheese", 30, 30, 150, false);
        types.add(temp);
        temp = new NonMeat("Oil", 0,0,30, true);
        types.add(temp);
        temp = new NonMeat("Flavor", 0, 0, 20, true);
        types.add(temp);
        temp = new NonMeat("Sugar", 0, 0, 10, true);
        types.add(temp);
        temp = new NonMeat("Salt", 0, 0, 10, true);
        types.add(temp);
        temp = new NonMeat("Egg", 0, 0, 30, true);
        types.add(temp);
    }

}

class TypesOfPlant{
    ArrayList<Plant> types = new ArrayList<>();

    public void setTypes(){
        types.add(new Plant("Garlic", "", 1, 2, 1, "Spring", true));
        types.add(new Plant("Pea", "", 3, 2, 1, "Spring", false));
        types.add(new Plant("Lettuce", "", 1, 2, 1, "Spring", true));
        types.add(new Plant("Eggplant", "", 3, 2, 1, "Spring", false));


        types.add(new Plant("Cucumber", "", 3, 2, 1, "Summer", false));
        types.add(new Plant("Turnip", "", 1, 2, 1, "Summer", true));
        types.add(new Plant("Watermelon", "", 3, 2, 1, "Summer", false));
        types.add(new Plant("Onion", "", 1, 2, 1, "Summer", true));


        types.add(new Plant("Potato", "", 1, 2, 1, "Fall", true));
        types.add(new Plant("Carrot", "", 1, 2, 1, "Fall", true));
        types.add(new Plant("Tomato", "", 3, 2, 1, "Fall", false));
        types.add(new Plant("Melon", "", 3, 2, 1, "Fall", false));

        types.add(new Plant("Pineapple", "", 3, 2, 1, "Tropical", false));
        types.add(new Plant("Strawberry", "", 3, 2, 1, "Tropical", false));
        types.add(new Plant("Chili", "", 3, 2, 1, "Tropical", false));

    }
}


class TypesOfSeed{
    ArrayList<Seed> types = new ArrayList<>();

    public void setTypes(){
        types.add(new Seed("Garlic", 75, "Spring", "Seed"));
        types.add(new Seed("Pea", 50, "Spring", "Seed"));
        types.add(new Seed("Lettuce", 50, "Spring", "Seed"));
        types.add(new Seed("Eggplant", 100, "Spring", "Seed"));
        types.add(new Seed("Cucumber", 50, "Summer", "Seed"));
        types.add(new Seed("Watermelon", 400, "Summer", "Seed"));
        types.add(new Seed("Onion", 75, "Summer", "Seed"));
        types.add(new Seed("Turnip", 575, "Summer", "Seed"));
        types.add(new Seed("Potato", 125, "Fall", "Seed"));
        types.add(new Seed("Carrot", 125, "Fall", "Seed"));
        types.add(new Seed("Tomato", 50, "Fall", "Seed"));
        types.add(new Seed("Melon", 300, "Fall", "Seed"));
        types.add(new Seed("Pineapple", 750, "Tropical", "Seed"));
        types.add(new Seed("Strawberry", 150, "Tropical", "Seed"));
        types.add(new Seed("Chili", 125, "Tropical", "Seed"));
    }
}

class TypesOfSkinProduct{
    ArrayList<SkinProduct> types = new ArrayList<>();

    public void setTypes(){
        ArrayList<String> animalsWhichCanBeEarnedFrom = new ArrayList<>();
        animalsWhichCanBeEarnedFrom.add("Sheep");
        types.add(new SkinProduct("Wool", "Sheep Wool By Using a spinning wheel you can turn it into a thread."
                , 200, animalsWhichCanBeEarnedFrom , "SkinProduct"));
    }
}

class TypesOfStone{
    ArrayList<Stone> types = new ArrayList<>();

    public void setTypes(){
        Stone temp = new Stone("Stone" , "" , 1 , 20 , true , "Stone");
        types.add(temp);

        temp = new Stone("Iron" , "" , 2 , 80 , false , "Stone");
        types.add(temp);

        temp = new Stone("Silver" , "" , 3 , 250 , false , "Stone");
        types.add(temp);

        temp = new Stone("Adamantium" , "" , 4 , 1000 , false , "Stone");
        types.add(temp);
    }
}

class TypesOfThread{
    ArrayList<Thread> types = new ArrayList<>();

    public void setTypes(){
        types.add(new Thread("Thread", 300, 1 , "Thread"));
    }
}

class TypesOfTree{
    ArrayList<Tree> types = new ArrayList<>();

    public void setTypes(){

        Tree temp = new Tree("Peach", 3, 15, "Spring");
        types.add(temp);

        temp = new Tree("Pear", 3, 15, "Spring");
        types.add(temp);

        temp = new Tree("Lemon", 3, 15, "Summer");
        types.add(temp);

        temp = new Tree("Pomegranate", 3, 25, "Summer");
        types.add(temp);

        temp = new Tree("Apple", 3, 20, "Fall");
        types.add(temp);

        temp = new Tree("Orange", 3, 20, "Fall");
        types.add(temp);

    }
}

class TypesOfVegetable{
    ArrayList<Vegetable> types = new ArrayList<>();

    public void setTypes(){
        types.add(new Vegetable("Garlic", 2, 0, 0, 0, 15, "Spring"));
        types.add(new Vegetable("Pea", 0, 2, 0, 0, 10, "Spring"));
        types.add(new Vegetable("Lettuce", 0, 0, 10, 0, 10, "Spring"));
        types.add(new Vegetable("EggPlant", 0, 0, 10, 0, 20, "Spring"));
        types.add(new Vegetable("Onion", 5, 0, 0, 0, 15, "Summer"));
        types.add(new Vegetable("Turnip", 3, 3, 0, 0, 15, "Summer"));
        types.add(new Vegetable("Potato", 0, 0, -5, 10, 25, "Fall"));
        types.add(new Vegetable("Carrot", 10, 0, 0, 0, 25, "Fall"));
        types.add(new Vegetable("Tomato", 0, 0, 5, 5, 10, "Fall"));
        types.add(new Vegetable("Chili", 5, 0, 0, 10, 25, "Tropical"));

    }
}


class TypesOfWateringTool{
    ArrayList<WateringTool> types = new ArrayList<>();

    public void setTypes(){
        WateringTool temp = new WateringTool("WoodMade", "Small", 1, 40, 50, 5, 1, 1);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Branch", 5 , 1 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Branch", 2 , 1 ,"Wood"));
        types.add(temp);

        temp = new WateringTool("WoodMade", "OldMade", 1, 30, 200, 20, 2, 2);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Old Lumber", 4 , 2 ,"Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Old Lumber", 2 , 2 ,"Wood"));
        types.add(temp);

        temp = new WateringTool("WoodMade", "PineMade", 1, 20, 500, 50, 4, 3);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Pine Lumber", 3 , 3 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Pine Lumber", 1 , 3 ,"Wood"));
        types.add(temp);

        temp = new WateringTool("WoodMade", "OakMade", 1, 10, 2000, 200, 8, 9);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Oak Lumber", 3 , 4 , "Wood"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Oak Lumber", 1 , 4 ,"wood"));
        types.add(temp);

        temp = new WateringTool("MetalMade", "StoneMade", 2, 80, 50, 5, 2, 1);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Stone", 5 , 1 ,"Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Stone", 2 , 1 ,"Stone"));
        types.add(temp);

        temp = new WateringTool("MetalMade", "IronMade", 2, 60, 200, 20, 4, 2);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Iron", 4 , 2 , "Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Iron", 2 , 2 ,"Stone"));
        types.add(temp);

        temp = new WateringTool("MetalMade", "SilverMade", 2, 40, 500, 50, 8, 3);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Silver", 3 , 3 ,"Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Silver", 1 , 3 , "Stone"));
        types.add(temp);

        temp = new WateringTool("MetalMade", "AdamantiumMade", 2, 20, 2000, 200, 16, 9);
        temp.demandingRawMaterialsToBuild.add(new DemandingRawMaterial("Adamantium", 2 , 4 ,"Stone"));
        temp.demandingRawMaterialsToRepair.add(new DemandingRawMaterial("Adamantium", 1 , 4 , "Stone"));
        types.add(temp);
    }
}

class TypesOfCafeFood{
    ArrayList<CafeFood> types = new ArrayList<>();

    public void setTypes(){
        CafeFood temp = new CafeFood("Falafel",10, -10, 100);
        types.add(temp);

        temp = new CafeFood("Bandari", 20, -20, 150);
        types.add(temp);

        temp = new CafeFood("Chese Burger", 50, -30, 200);
        types.add(temp);
    }
}

class TypesOfWood{
    ArrayList<Wood> types = new ArrayList<>();

    public void setTypes(){
        Wood temp = new Wood("Branch" , "" , 1 , 20 , true , "Wood");
        types.add(temp);

        temp = new Wood("Old Lumber" , "" , 2 , 80 , false , "Wood");
        types.add(temp);

        temp = new Wood("Pine Lumber" , "" , 3 , 250 , false , "Wood");
        types.add(temp);

        temp = new Wood("Oak Lumber" , "" , 4 , 1000 , false , "Wood");
        types.add(temp);
    }
}

class TypesOfMachine{
    ArrayList<Machine> types = new ArrayList<>();

    public void setTypes(){
        Machine machine = new Machine();
        machine.setName("Juicer");
        machine.inputClasses.add(new MachineItem("Fruit", "", 2));
        machine.inputClasses.add(new MachineItem("Vegetable", "", 2));
        machine.outputClasses.add(new MachineItem("Juice", "", 1));
        machine.setBuyingCost(1000);
        types.add(machine);

        machine = new Machine();
        machine.setName("Cheese Maker");
        machine.inputClasses.add(new MachineItem("NonMeat", "Milk", 2));
        machine.outputClasses.add(new MachineItem("NonMeat", "Cheese", 1));
        machine.setBuyingCost(3000);
        types.add(machine);

        machine = new Machine();
        machine.setName("Spinner");
        machine.inputClasses.add(new MachineItem("SkinProduct", "Wool", 2));
        machine.outputClasses.add(new MachineItem("SkinProduct", "Thread", 1));
        machine.setBuyingCost(2000);
        types.add(machine);

    }
}

class TypesOfMission{
    ArrayList<Mission> types;

    public static void setTypes(){

    }
}

class TypesOfRecipes{
    ArrayList<Recipe> types = new ArrayList<>();

    public void setTypes() {
        Recipe tempRecipe = new Recipe();
        tempRecipe.setName("French Fries");
        tempRecipe.ingredients.add(new Ingredients("Potato", 1));
        tempRecipe.ingredients.add(new Ingredients("Oil", 1));
        tempRecipe.ingredients.add(new Ingredients("Salt", 1));
        tempRecipe.demandingTools.add("Pan");
        tempRecipe.demandingTools.add("Knife");
        tempRecipe.setEffectOnEnergy(100);
        tempRecipe.setEffectOnHealthMax(-15);
        types.add(tempRecipe);

        tempRecipe = new Recipe();
        tempRecipe.setName("Shirazian Salad");
        tempRecipe.ingredients.add(new Ingredients("Cocumber", 1));
        tempRecipe.ingredients.add(new Ingredients("Tomato", 1));
        tempRecipe.ingredients.add(new Ingredients("Lemon Juice", 1));
        tempRecipe.ingredients.add(new Ingredients("Onion", 1));
        tempRecipe.demandingTools.add("Knife");
        tempRecipe.setEffectOnEnergy(140);
        tempRecipe.setEffectOnHealth(60);
        tempRecipe.setEffectOnHealthMax(10);
        types.add(tempRecipe);

        tempRecipe = new Recipe();
        tempRecipe.setName("Cheese Cake");
        tempRecipe.ingredients.add(new Ingredients("Milk", 1));
        tempRecipe.ingredients.add(new Ingredients("Cheese", 1));
        tempRecipe.ingredients.add(new Ingredients("Egg", 1));
        tempRecipe.ingredients.add(new Ingredients("Sugar", 1));
        tempRecipe.demandingTools.add("Deeg");
        tempRecipe.demandingTools.add("Blazer");
        tempRecipe.demandingTools.add("Oven");
        tempRecipe.setEffectOnEnergy(80);
        types.add(tempRecipe);

        tempRecipe = new Recipe();
        tempRecipe.setName("Mirza Ghasemi");
        tempRecipe.ingredients.add(new Ingredients("Oil", 1));
        tempRecipe.ingredients.add(new Ingredients("Garlic", 1));
        tempRecipe.ingredients.add(new Ingredients("Egg", 1));
        tempRecipe.ingredients.add(new Ingredients("Eggplant", 1));
        tempRecipe.ingredients.add(new Ingredients("Tomato", 1));
        tempRecipe.ingredients.add(new Ingredients("Salt", 1));
        tempRecipe.demandingTools.add("Knife");
        tempRecipe.demandingTools.add("Pan");
        tempRecipe.setEffectOnEnergy(80);
        types.add(tempRecipe);
    }
}

class TypesOfAnimalFood{
    ArrayList<AnimalFood> types = new ArrayList<>();

    public void setTypes(){
        AnimalFood temp = new AnimalFood("Alfalfa", 1,1,20);
        temp.animalsWhichCanEatIt.add("Cow");
        temp.animalsWhichCanEatIt.add("Sheep");
        temp.setPackable(true);
        types.add(temp);

        temp = new AnimalFood("Chickweed", 1,1, 10);
        temp.animalsWhichCanEatIt.add("Hen");
        temp.setPackable(true);
        types.add(temp);
    }
}

class Season{
    private String name;
    private int Id;

    Season(String name, int Id)
    {
        this.name = name;
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}

class TypesOfSeason{
    Season spring = new Season("Spring", 1);
    Season summer = new Season("Summer", 2);
    Season fall = new Season("Fall", 3);
    Season winter = new Season("Winter", 4);
    Season tropical = new Season("Tropical", -1);
}

//public class Main {

//    public static void main(String[] args) {
//        Game game = new Game();
//        game.starter();
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()){
//            game.commandProcessor.commander(scanner.nextLine());
//        }
//    }
//}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = new Pane();


        root.setBackground(new Background(new BackgroundImage(new Image("pics/background.jpg"), null, null, null, null)));

        Accordion startingMenu = new Accordion();
        Button singlePlayer = new Button("       Ready ?       ");
        Button multiPlayer = new Button(" Ready Friends ? ");
        Button customize = new Button("    Exciting :))     ");
        Button settings = new Button("   Make it Own   ");
        Button exit = new Button("        Sure :(        ");

        TitledPane singlePlayerPane = new TitledPane("Single Player", singlePlayer);
        TitledPane multiPlayerPane = new TitledPane("Multi Player", multiPlayer);
        TitledPane customizePane = new TitledPane("Customize", customize);
        TitledPane settingsPane = new TitledPane("Settings", settings);
        TitledPane exitPane = new TitledPane("Exit", exit);

        startingMenu.getPanes().addAll(singlePlayerPane, multiPlayerPane, customizePane, settingsPane, exitPane);

        startingMenu.setLayoutY(300);
        startingMenu.setLayoutX(420);
        singlePlayer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });

        multiPlayer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startMultiPlayer();
            }
        });

        customize.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setCustomize();
            }
        });

        settings.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setSettings();
            }
        });

        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Platform.exit();
            }
        });

        Scene mainScene = new Scene(root, 960, 720);
        primaryStage.setTitle("Game :)");
        primaryStage.setScene(mainScene);

        root.getChildren().add(startingMenu);
        primaryStage.show();

    }

    void startSinglePlayer(){

    }

    void startMultiPlayer(){

    }

    void setCustomize(){

    }

    void setSettings(){

    }

    public static void main(String[] args) {
        launch(args);
    }
}
