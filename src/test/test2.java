package src;

import java.io.FileNotFoundException;

public class test2 {
	public static void main(String [] args) {
		String q = "SELECT SUM(D.c0), SUM(D.c4), SUM(C.c1)\n" + 
				"FROM A, B, C, D\n" + 
				"WHERE A.c1 = B.c0 AND A.c3 = D.c0 AND C.c2 = D.c2\n" + 
				"AND D.c3 = -9496;";
		String q1 = "SELECT SUM(A.c49), SUM(A.c38), SUM(A.c22), SUM(A.c25)\n" + 
				"FROM A, K, E, D, H, C, G, B, O, M, P, L\n" + 
				"WHERE A.c10 = K.c0 AND A.c4 = E.c0 AND A.c3 = D.c0 AND A.c7 = H.c0 AND A.c2 = C.c0 AND A.c6 = G.c0 AND A.c1 = B.c0 AND K.c1 = O.c0 AND G.c1 = M.c0 AND C.c1 = P.c0 AND G.c1 = L.c0\n" + 
				"AND A.c23 = 5397 AND A.c49 = 8902 AND D.c3 > 0 AND O.c3 > 0;";
		Operation [] opts = parserTest(q1);
		for(Operation opt : opts) {
			if(opt!=null) {
				System.out.println(opt.toString());
			}
		}
	}
	
	public static Operation[] parserTest(String q) {
		Parser par = new Parser(q);
		par.process();
		return par.getOpts();
		
	}
}
//SELECT SUM(A.c32), SUM(A.c35), SUM(A.c25) FROM A, C, F, J, G, H, D WHERE A.c2 = C.c0 AND A.c5 = F.c0 AND A.c9 = J.c0 AND A.c6 = G.c0 AND A.c7 = H.c0 AND A.c3 = D.c0 AND A.c46 < -2101 AND J.c1 > 0 and F.c2 = 0;