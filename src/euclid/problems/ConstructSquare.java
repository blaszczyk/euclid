package euclid.problems;

import static euclid.model.Sugar.*;

import java.util.Optional;

import euclid.alg.CurvedBasedSearch;
import euclid.alg.Search;
import euclid.model.*;

public class ConstructSquare {

	public static void main(String[] args) {

		final Search search = new CurvedBasedSearch();
		
		// center and corners of a square
		final Point c=p(0,0), p1=p(1,0), p2=p(0,1), p3=p(-1,0), p4=p(0,-1);
		final CurveSet square = CurveSet.of(l(p1,p2), l(p2,p3), l(p3,p4), l(p4,p1));

		final Board init = Board.withPoints(c,p1).andCurves(c(c,p1));
		final Board req = Board.withPoints().andCurves(square);
		
		final Optional<Board> solution = search.findFirst(init, req, 7);
		if(solution.isPresent())
			solution.get().print();
		else
			System.out.println("No Solution");		
	}
	
}
