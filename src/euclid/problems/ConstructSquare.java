package euclid.problems;

import static euclid.model.Sugar.*;

import java.util.Optional;

import euclid.alg.CurveBasedSearch;
import euclid.model.*;

public class ConstructSquare {

	public static void main(String[] args) {
		
		// center and corners of a square
		Point c=p(0,0), p1=p(1,0), p2=p(0,1), p3=p(-1,0), p4=p(0,-1);
		CurveSet square = CurveSet.of(l(p1,p2), l(p2,p3), l(p3,p4), l(p4,p1));

		Board init = Board.withPoints(c,p1).andCurves(c(c,p1));
		Board req = Board.withPoints().andCurves(l(p1,p2));
		
		Optional<Board> solution = new CurveBasedSearch(init, req, 5).findFirst();
		if(solution.isPresent())
			solution.get().print();
		else
			System.out.println("No Solution");		
	}
	
}
