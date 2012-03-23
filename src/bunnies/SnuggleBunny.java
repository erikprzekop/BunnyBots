package bunnies;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.Color;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

public class SnuggleBunny extends TeamRobot {
	private static final int DEFAULT_DISTANCE = 650;
	int turnDirection = 1;
	boolean movingForward;
	int numberOfSearches = 0; 
	double howMuchToTurnGunWhenSearching;  
	String robotCurrentlyBeingTracked; 

	double previousEnergy = 100;
	int movementDirection = 1;
	int gunDirection = 1;

	public void run() {
		setBodyColor(Color.pink);
		setGunColor(Color.white);
		setRadarColor(Color.black);
		setBulletColor(Color.pink);
		setScanColor(Color.pink);

		robotCurrentlyBeingTracked = null;
		setAdjustGunForRobotTurn(true); 
		howMuchToTurnGunWhenSearching = 10;

		while (true) {
			turnGunRight(howMuchToTurnGunWhenSearching);
			numberOfSearches++;
			if (numberOfSearches > 2) {
				howMuchToTurnGunWhenSearching = -10;
			}
			if (numberOfSearches > 5) {
				howMuchToTurnGunWhenSearching = 10;
			}
			if (numberOfSearches > 11) {
				robotCurrentlyBeingTracked = null;
			}
		}

	}

	public void onScannedRobot(ScannedRobotEvent e) {
		if (isTeammate(e.getName())) {
			return;
		}

		// If the bot has small energy drop, assume it fired
		double changeInEnergy = previousEnergy - e.getEnergy();
		if (changeInEnergy > 0 && changeInEnergy <= 3) {
			// Dodge!
			movementDirection = -movementDirection - 10;
			setAhead(((e.getDistance() / 2) + 25) * movementDirection);
		}

		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing
				- getGunHeading());
		if (Math.abs(bearingFromGun) <= 3 && e.getDistance() < 400) {
			setTurnGunRight(bearingFromGun);
			if (getGunHeat() == 0) {
				setFire(Math
						.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
			}
		} 
		else {
			setTurnGunRight(bearingFromGun);
		}
		if (bearingFromGun == 0) {
			scan();
		}
		setFire(2);

		previousEnergy = e.getEnergy();
	}

	public void onHitRobot(HitRobotEvent e) {
		if (e.getBearing() >= 0) {
			turnDirection = 1;
		} else {
			turnDirection = -1;
		}
		turnRight(e.getBearing());

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
