package bunnies;

import robocode.*;
import robocode.util.Utils;
import java.awt.geom.*;     // for Point2D's
import java.util.ArrayList; // for collection of waves
 
public class WaveSurfingBunny extends TeamRobot {
//    public static int BINS = 47;
//    public static double _surfStats[] = new double[BINS];
//    public Point2D.Double _myLocation;     // our bot's location
//    public Point2D.Double _enemyLocation;  // enemy bot's location
// 
//    public ArrayList<EnemyWave> _enemyWaves;
//    public ArrayList<Integer> _surfDirections;
//    public ArrayList<Double> _surfAbsBearings;
// 
//    public static double _oppEnergy = 100.0;
// 
//   /** This is a rectangle that represents an 800x600 battle field,
//    * used for a simple, iterative WallSmoothing method (by PEZ).
//    * If you're not familiar with WallSmoothing, the wall stick indicates
//    * the amount of space we try to always have on either end of the tank
//    * (extending straight out the front or back) before touching a wall.
//    */
//    public static Rectangle2D.Double _fieldRect
//        = new java.awt.geom.Rectangle2D.Double(18, 18, 764, 564);
//    public static double WALL_STICK = 160;
// 
//    public void run() {
//        _enemyWaves = new ArrayList<EnemyWave>();
//        _surfDirections = new ArrayList<Integer>();
//        _surfAbsBearings = new ArrayList<Double>();
// 
//        setAdjustGunForRobotTurn(true);
//        setAdjustRadarForGunTurn(true);
// 
//        do {
//            turnRadarRightRadians(Double.POSITIVE_INFINITY);
//        } while (true);
//    }
//
//    public void onScannedRobot(ScannedRobotEvent e) {
//        _myLocation = new Point2D.Double(getX(), getY());
// 
//        double lateralVelocity = getVelocity()*Math.sin(e.getBearingRadians());
//        double absBearing = e.getBearingRadians() + getHeadingRadians();
// 
//        setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing
//            - getRadarHeadingRadians()) * 2);
// 
//        _surfDirections.add(0,
//            new Integer((lateralVelocity >= 0) ? 1 : -1));
//        _surfAbsBearings.add(0, new Double(absBearing + Math.PI));
//  
//        double bulletPower = _oppEnergy - e.getEnergy();
//        if (bulletPower < 3.01 && bulletPower > 0.09
//            && _surfDirections.size() > 2) {
//            EnemyWave ew = new EnemyWave();
//            ew.fireTime = getTime() - 1;
//            ew.bulletVelocity = bulletVelocity(bulletPower);
//            ew.distanceTraveled = bulletVelocity(bulletPower);
//            ew.direction = ((Integer)_surfDirections.get(2)).intValue();
//            ew.directAngle = ((Double)_surfAbsBearings.get(2)).doubleValue();
//            ew.fireLocation = (Point2D.Double)_enemyLocation.clone(); // last tick
// 
//            _enemyWaves.add(ew);
//        }
// 
//        _oppEnergy = e.getEnergy();
// 
//        // update after EnemyWave detection, because that needs the previous
//        // enemy location as the source of the wave
//        _enemyLocation = project(_myLocation, absBearing, e.getDistance());
// 
//        updateWaves();
//        doSurfing();
// 
//        // gun code would go here...
//    }
//
//    public void updateWaves() {
//        for (int x = 0; x < _enemyWaves.size(); x++) {
//            EnemyWave ew = (EnemyWave)_enemyWaves.get(x);
// 
//            ew.distanceTraveled = (getTime() - ew.fireTime) * ew.bulletVelocity;
//            if (ew.distanceTraveled >
//                _myLocation.distance(ew.fireLocation) + 50) {
//                _enemyWaves.remove(x);
//                x--;
//            }
//        }
//    }
//    
  
}
class EnemyWave {
    Point2D.Double fireLocation;
    long fireTime;
    double bulletVelocity, directAngle, distanceTraveled;
    int direction;

    public EnemyWave() { }
}
