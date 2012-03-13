package bunnies;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MoveCompleteCondition;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.TurnCompleteCondition;
import robocode.util.Utils;

public class SnuggleBunny extends AdvancedRobot {
	private static final int DEFAULT_DISTANCE = 500;
	int turnDirection = 1;
	int dist = 25;
	boolean movingForward;
	int count = 0; // Keeps track of how long we've
	// been searching for our target
	double gunTurnAmt; // How much to turn our gun when searching
	String trackName; // Name of the robot we're currently tracking

	double previousEnergy = 100;
	int movementDirection = 1;
	int gunDirection = 1;

	public void run() {
		// Set colors
		setBodyColor(new Color(0, 200, 0));
		setGunColor(new Color(0, 150, 50));
		setRadarColor(new Color(0, 100, 100));
		setBulletColor(new Color(255, 255, 100));
		setScanColor(new Color(255, 200, 200));

		// Prepare gun
		trackName = null; // Initialize to not tracking anyone
		setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
		gunTurnAmt = 10; // Initialize gunTurn to 10

		// Loop forever
		while (true) {
			// turn the Gun (looks for enemy)
			turnGunRight(gunTurnAmt);
			// Keep track of how long we've been looking
			count++;
			// If we've haven't seen our target for 2 turns, look left
			if (count > 2) {
				gunTurnAmt = -10;
			}
			// If we still haven't seen our target for 5 turns, look right
			if (count > 5) {
				gunTurnAmt = 10;
			}
			// If we *still* haven't seen our target after 10 turns, find
			// another target
			if (count > 11) {
				trackName = null;
			}
		}

	}

	public void onScannedRobot(ScannedRobotEvent e) {

		  // Stay at right angles to the opponent
	      setTurnRight(e.getBearing()+90-30*movementDirection);
	         
	     // If the bot has small energy drop,
	    // assume it fired
	    double changeInEnergy =
	      previousEnergy-e.getEnergy();
	    if (changeInEnergy>0 &&
	        changeInEnergy<=3) {
	         // Dodge!
	         movementDirection =-movementDirection;
	         setAhead((e.getDistance()/4+25)*movementDirection);
	     }
	    // When a bot is spotted,
	    // sweep the gun and radar
	    gunDirection = -gunDirection;
	    setTurnGunRight(99999*gunDirection);
	    
	    // Fire directly at target
	    fire ( 2 ) ;
	    
	    // Track the energy level
	    previousEnergy = e.getEnergy();
	}

	public void onHitRobot(HitRobotEvent e) {
		if (e.getBearing() >= 0) {
			turnDirection = 1;
		} else {
			turnDirection = -1;
		}
		turnRight(e.getBearing());

		// Determine a shot that won't kill the robot...
		// We want to ram him instead for bonus points
		if (e.getEnergy() > 16) {
			fire(3);
		} else if (e.getEnergy() > 10) {
			fire(2);
		} else if (e.getEnergy() > 4) {
			fire(1);
		} else if (e.getEnergy() > 2) {
			fire(.5);
		} else if (e.getEnergy() > .4) {
			fire(.1);
		}

	}

	public void onHitByBullet(HitByBulletEvent e) {
		turnLeft(90 - e.getBearing());
		reverseDirection();
		
	}

	public void onHitWall(HitWallEvent event) {
		setTurnLeft(30);
		reverseDirection();
	}

	public void reverseDirection() {
		if (movingForward) {
			setBack(DEFAULT_DISTANCE);
			movingForward = false;
		} else {
			setAhead(DEFAULT_DISTANCE);
			movingForward = true;
		}
	}

}
