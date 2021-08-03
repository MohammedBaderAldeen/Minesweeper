package settings;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    public static final String WINDOW_TITLE = "Minesweeper";
    public static final int DISPLAY_SCORE_THRESHOLD = 10;

    public static int CELL_SIZE = 20;
    public static int X_CELLS = 30;
    public static int Y_CELLS = 15;
    public static int WIDTH = CELL_SIZE * X_CELLS;
    public static int HEIGHT = CELL_SIZE * Y_CELLS;

    public static int PLAYERS_COUNT = 1;
    public static int BOMBS_COUNT = 50;
    public static int SHIELDS_COUNT = 5;
    public static int SHIELDS_INIT = 0;
    public static int PLAY_TIME = 10;
    public static int SUPER_SHIELDS_COUNT = 1;
    public static boolean computerShield = true;
	public static boolean SAVE_REPLAY = false; 
	public static String SAVEPath = "game.result"; 
	public static String PlayerNames = "player1;player2";
	public static boolean AutoSave = false; 
	public static String AUTOSAVEPath="";
	public static String RecentSAVEPath="";

    public static boolean isValidCoordinates(int x, int y) {
        return ((x >= 0 && x < X_CELLS) && (y >= 0 && y < Y_CELLS));
    }

    public static void update(int xCells, int yCells, int playersCount, int bombsCount, int shieldscount,int shieldInit,int playTime,int superCount,boolean cmSH,boolean saveReplay,String names,boolean c ) {
        X_CELLS = xCells;
        Y_CELLS = yCells;
        WIDTH = CELL_SIZE * X_CELLS;
        HEIGHT = CELL_SIZE * Y_CELLS;
        PLAYERS_COUNT = playersCount;
        BOMBS_COUNT = bombsCount;
        SHIELDS_COUNT = shieldscount;
        SHIELDS_INIT=shieldInit;
        PLAY_TIME=playTime;
        SUPER_SHIELDS_COUNT=superCount;
        computerShield=cmSH;
        SAVE_REPLAY=saveReplay;
        PlayerNames=names;
        AutoSave =c;
        
    }

    public static class ImageService {
        private static final String RESOURCES_PATH = "file:resources";
        private static final String IMAGES_PATH = RESOURCES_PATH + "/images";

        private static Map<Status, ImagePattern> imagesMap = new HashMap<>();

        static {
            initImages();
        }

        public static ImagePattern getImage(Status status) {
            return imagesMap.get(status);
        }

        private static void initImages()  {
            imagesMap.put(Status.BOMBED, new ImagePattern(new Image(IMAGES_PATH + "/bomb.png")));
            imagesMap.put(Status.GRAY_BOMBED, new ImagePattern(new Image(IMAGES_PATH + "/gray_bomb.png")));
            imagesMap.put(Status.FLAGGED, new ImagePattern(new Image(IMAGES_PATH + "/flag.png")));
            imagesMap.put(Status.BLANK, new ImagePattern(new Image(IMAGES_PATH + "/blank.png")));
            imagesMap.put(Status.COVERED, new ImagePattern(new Image(IMAGES_PATH + "/covered.png")));
            imagesMap.put(Status.ONE, new ImagePattern(new Image(IMAGES_PATH + "/1.png")));
            imagesMap.put(Status.TWO, new ImagePattern(new Image(IMAGES_PATH + "/2.png")));
            imagesMap.put(Status.THREE, new ImagePattern(new Image(IMAGES_PATH + "/3.png")));
            imagesMap.put(Status.FOUR, new ImagePattern(new Image(IMAGES_PATH + "/4.png")));
            imagesMap.put(Status.FIVE, new ImagePattern(new Image(IMAGES_PATH + "/5.png")));
            imagesMap.put(Status.SIX, new ImagePattern(new Image(IMAGES_PATH + "/6.png")));
            imagesMap.put(Status.SEVEN, new ImagePattern(new Image(IMAGES_PATH + "/7.png")));
            imagesMap.put(Status.EIGHT, new ImagePattern(new Image(IMAGES_PATH + "/8.png")));
            imagesMap.put(Status.DISPLAY_ZERO, new ImagePattern(new Image(IMAGES_PATH + "/displayzero.png")));
            imagesMap.put(Status.DISPLAY_ONE, new ImagePattern(new Image(IMAGES_PATH + "/displayone.png")));
            imagesMap.put(Status.DISPLAY_TWO, new ImagePattern(new Image(IMAGES_PATH + "/displaytwo.png")));
            imagesMap.put(Status.DISPLAY_THREE, new ImagePattern(new Image(IMAGES_PATH + "/displaythree.png")));
            imagesMap.put(Status.DISPLAY_FOUR, new ImagePattern(new Image(IMAGES_PATH + "/displayfour.png")));
            imagesMap.put(Status.DISPLAY_FIVE, new ImagePattern(new Image(IMAGES_PATH + "/displayfive.png")));
            imagesMap.put(Status.DISPLAY_SIX, new ImagePattern(new Image(IMAGES_PATH + "/displaysix.png")));
            imagesMap.put(Status.DISPLAY_SEVEN, new ImagePattern(new Image(IMAGES_PATH + "/displayseven.png")));
            imagesMap.put(Status.DISPLAY_EIGHT, new ImagePattern(new Image(IMAGES_PATH + "/displayeight.png")));
            imagesMap.put(Status.DISPLAY_NINE, new ImagePattern(new Image(IMAGES_PATH + "/displaynine.png")));
            imagesMap.put(Status.SMILEY, new ImagePattern(new Image(IMAGES_PATH + "/smiley.png")));
            imagesMap.put(Status.DEAD_SMILEY, new ImagePattern(new Image(IMAGES_PATH + "/deadsmiley.png")));
            imagesMap.put(Status.SUN_GLASSES, new ImagePattern(new Image(IMAGES_PATH + "/sunglasses.png")));
            imagesMap.put(Status.SHIELD, new ImagePattern(new Image(IMAGES_PATH + "/shield.png")));
            imagesMap.put(Status.SUPER_SHIELD, new ImagePattern(new Image(IMAGES_PATH + "/flyShield.png")));
        }
    }
}
