package euclid.geometry;

public interface Curve extends Element<Curve> {
	
	default public boolean isLine() {
		return false;
	};
	
	default public boolean isCircle() {
		return false;
	}

	default public Line asLine() {
		throw new ClassCastException(this + " is not a line");
	}
	
	default public Circle asCircle() {
		throw new ClassCastException(this + " is not a circle");
	}
}
