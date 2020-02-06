package io.github.nul00000000.ball;

import org.opencv.core.Point;

public class Ball {
	
	private Point point;
	private double radius;
	
	public Ball(Point point, double perimeter) {
		this.point = point;
		this.radius = perimeter / 6.283;
	}
	
	public double distanceSquared(Ball ball) {
		return (ball.point.x - point.x) * (ball.point.x - point.x) + (ball.point.y - point.y) * (ball.point.y - point.y);
	}
	
	public double distanceSquared(Point point) {
		return (this.point.x - point.x) * (this.point.x - point.x) + (this.point.y - point.y) * (this.point.y - point.y);
	}
	
	public boolean overlaps(Ball other) {
		return distanceSquared(other) < (radius + other.radius) * (radius + other.radius);
	}
	
	public double getRadius() {
		return radius;
	}
	
	public Point getCenter() {
		return point;
	}

}
