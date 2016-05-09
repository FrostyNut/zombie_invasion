package gamemanager;

/**
 * This class contains and manages the runtime settings during game play.
 *
 * @author Jackie Chan
 * May 2, 2016
 */
public class RuntimeSettings {
    
    private static int gameFieldWidth;
    private static int gameFieldHeight;
    
    private static int maxPlayerSpawnX;
    private static int maxPlayerSpawnY;
    
    private static int maxBulletPositionX;
    private static int maxBulletPositionY;    
    
    private RuntimeSettings(){}
    
    public static void loadRuntimeSettings(int w, int h) {
        gameFieldWidth  = w;
        gameFieldHeight = h;
        
        maxPlayerSpawnX = w - Settings.PLAYER_WIDTH;
        maxPlayerSpawnY = h - Settings.PLAYER_HEIGHT;
        
        maxBulletPositionX = w - Settings.EXPLOSION_WIDTH;
        maxBulletPositionY = h - Settings.EXPLOSION_HEIGHT;        
    }   
    
    public static int getWidth() {return gameFieldWidth;}
    public static int getHeight() {return gameFieldHeight;}
    public static int getMaxBulletX() {return maxBulletPositionX;}
    public static int getMaxBulletY() {return maxBulletPositionY;}
    public static int getMaxPlayerSpawnX() {return maxPlayerSpawnX;}
    public static int getMaxPlayerSpawnY() {return maxPlayerSpawnY;}    
}
