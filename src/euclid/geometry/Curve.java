package euclid.geometry;

public abstract class Curve extends Element<Curve> {
	
	public boolean isLine() {
		return false;
	};
	
	public boolean isCircle() {
		return false;
	}

	public Line asLine() {
		throw new ClassCastException(this + " is not a line");
	}
	
	public Circle asCircle() {
		throw new ClassCastException(this + " is not a circle");
	}
}
