package euclid.model;

public interface Curve extends Element<Curve> {
	
	public boolean isLine();
	
	default public boolean isCircle() {
		return !isLine();
	}

	default public Line asLine() {
		throw new ClassCastException(this + " is not a line");
	}
	
	default public Circle asCircle() {
		throw new ClassCastException(this + " is not a circle");
	}
}
