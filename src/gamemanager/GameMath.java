package gamemanager;


/**
 * Helps with math calculations in the game.
 *
 * @author Jackie Chan Apr 20, 2016
 */
public class GameMath {        
    
    
    /**
     * Calculates the distance between two points by using the distance formula.
     * 
     * @param x1    The x-coordinate of the first point.
     * @param y1    The y-coordinate of the first point.
     * @param x2    The x-coordinate of the second point.
     * @param y2    The y-coordinate of the second point.
     * 
     * @return  The distance between (x1,y1) and (x2,y2).
     */
    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2,2));
    }     
    
    
    /**
     * Calculates the slope between two points. Uses a proportion to scale the 
     * slope so every bullet moves at the same pace. To do the that, use the
     * following sequence:
     * 
     *      First find the rise (y2-y1) and the run (x2-x1) and recored if they 
     *      are negative.
     * 
     *      Find the bigger number; this will be the rise or the run.
     *      For this example, it will be rise. 
     * 
     *      If the rise was the bigger number, then do the following.
     *          run = Settings.SCALE_MAX * run) / rise;
     *      
     *      Set the rise to the value stored in Settings.SCALE_MAX.
     *          rise = Settings.SCALE_MAX;
     * 
     *      If the run was the bigger number, then do the following:
     *          rise = Settings.SCALE_MAX * rise) / run;
     *          run = Settings.SCALE_MAX;
     * 
     *      In both instances, the bigger value will be set to Settings.SCALE_MAX.
     * 
     * @param x1    The x-coordinate of the first point.
     * @param x2    The x-coordinate of the second point.
     * @param y1    The y-coordinate of the first point.
     * @param y2    The y-coordinate of the second point.
     * 
     * @return  An array containing the slope's rise and run.
     */
    public static double[] calculateSlope(double x1, double x2, double y1, double y2) {       
                       
        double rise = y2 - y1, run = x2 - x1;
        boolean nRise = false, nRun = false;
        
        if (rise < 0) {
            rise *= -1;
            nRise = true;
        }
        
        if (run < 0) {
            run *= -1;
            nRun = true;
        }
        
        if (Math.max(rise, run) == rise) {
            run = (Settings.SCALE_MAX * run) / rise;
            rise = Settings.SCALE_MAX;
        } else {
            rise = (Settings.SCALE_MAX * rise) / run;
            run = Settings.SCALE_MAX;
        }
        
        if (nRise) rise *= -1;
        if (nRun) run *= -1;
        
        return new double[]{rise,run};
    }   
    
    
    /**
     * Calculates the angle that two points form. The third point is the point
     * the forms a right triangle with the two points that were given. Calculate
     * the angle by using the following sequence:
     * 
     *      Find the opposite side's length:
     *          Calculate: y2 - y1. 
     * 
     *      Find the distance between (x1,y1) and (x2,y2) for hypotenuse:
     *          Calculate: call calculateDistance(x1,y1,x2,y2)
     * 
     *      Find the inverse sin of opp/hyp:
     *          Math.asin((y2-y1)/calculateDistance(x1,y1,x2,y2)
     * 
     *      Convert the result from radians to degrees:
     *          Math.toDegrees(result);
     * 
     * @param x1    The x-coordinate of the first point.
     * @param y1    The y-coordinate of the first point.
     * @param x2    The x-coordinate of the second point.
     * @param y2    The y-coordinate of the second point.
     * 
     * @return      Angle created by those points. 
     */
    public static double calculateAngle(double x1, double y1, double x2, 
                                        double y2) {                
        
        double angle = Math.toDegrees(Math.asin((y1-y2)/calculateDistance(x1,y1,x2,y2)));
        
        if (x1 < x2) {angle = 180 - angle;}
        if (angle < 0) {angle += 360;}                                                   
        
        return angle;
    } 
        
}
