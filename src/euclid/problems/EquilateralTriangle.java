package euclid.problems;

import static euclid.model.Sugar.*;

import java.util.Optional;

import euclid.alg.CurveBasedSearch;
import euclid.model.*;

public class EquilateralTriangle {

	public static void main(String[] args) {
		
		// equilateral triangle from two points
		double sqrt3 = Math.sqrt(3);
		Point p0=p(-1,0), p1=p(1,0), p2=p(0,sqrt3);
		CurveSet triangle = CurveSet.of(l(p1,p2), l(p2,p0), l(p0,p1));

		Board init = Board.withPoints(p0,p1).andCurves();
		Board req = Board.withPoints().andCurves(triangle);
		
		Optional<Board> solution = new CurveBasedSearch(init, req, 5).findFirst();
		if(solution.isPresent())
			solution.get().print();
		else
			System.out.println("No Solution");		
	}
	
}
