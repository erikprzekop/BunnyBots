package bunnies;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.Color;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

public class SnuggleBunny extends TeamRobot {
	private static final int DEFAULT_DISTANCE = 500;
	int turnDirection = 1;
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
		// Don't fire on teammates
		if (isTeammate(e.getName())) {
			return;
		}

		// If the bot has small energy drop,
		// assume it fired
		double changeInEnergy = previousEnergy - e.getEnergy();
		if (changeInEnergy > 0 && changeInEnergy <= 3) {
			// Dodge!
			movementDirection = -movementDirection - 10;
			setAhead(((e.getDistance() / 2) + 25) * movementDirection);
		}

		// Calculate exact location of the robot
		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing
				- getGunHeading());
		// If it's close enough, fire!
		if (Math.abs(bearingFromGun) <= 3 && e.getDistance() < 400) {
			setTurnGunRight(bearingFromGun);
			// We check gun heat here, because calling fire()
			// uses a turn, which could cause us to lose track
			// of the other robot.
			if (getGunHeat() == 0) {
				setFire(Math
						.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
			}
		} // otherwise just set the gun to turn.
			// Note: This will have no effect until we call scan()
		else {
			setTurnGunRight(bearingFromGun);
		}
		// Generates another scan event if we see a robot.
		// We only need to call this if the gun (and therefore radar)
		// are not turning. Otherwise, scan is called automatically.
		if (bearingFromGun == 0) {
			scan();
		}

		// Fire directly at target
		setFire(2);

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
			setFire(3);
		} else if (e.getEnergy() > 10) {
			setFire(2);
		} else if (e.getEnergy() > 4) {
			setFire(1);
		} else if (e.getEnergy() > 2) {
			setFire(.5);
		} else if (e.getEnergy() > .4) {
			setFire(.1);
		}

	}

	public void onHitByBullet(HitByBulletEvent e) {
		setTurnLeft(80 - e.getBearing());
		reverseDirection();

	}

	public void onHitWall(HitWallEvent event) {
		setTurnLeft(45);
		reverseDirection();
	}

	public void reverseDirection() {
		double rand = Math.random();
		if (movingForward) {
			setBack(DEFAULT_DISTANCE * rand);
			movingForward = false;
		} else {
			setAhead(DEFAULT_DISTANCE * rand);
			movingForward = true;
		}
	}

}
