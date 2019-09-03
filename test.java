package parkinglot;
import java.util.*;

public class test {
	    // classic Rush Hour parameters
	    static final int N = 6;
	    static final int M = 6;
	    static final int GOAL_R = 2;
	    static final int GOAL_C = 5;

	    static final String INITIAL =   "2222B." +
	                                    "22B.BC" +
	                                    "C.BXXC" +
	                                    "C333.C" +
	                                    "C..B22" +
	                                    ".22B22";

	    static final String HORZS = "23X";  // horizontal-sliding cars
	    static final String VERTS = "BC";   // vertical-sliding cars
	    static final String LONGS = "3C";   // length 3 cars
	    static final String SHORTS = "2BX"; // length 2 cars
	    static final char GOAL_CAR = 'X';
	    static final char EMPTY = '.';      // empty space, movable into
	    static final char VOID = '@';       // represents everything out of bound

	    // breaks a string into lines of length N using regex
	    static String prettify(String state) {
	        String EVERY_NTH = "(?<=\\G.{N})".replace("N", String.valueOf(N));
	        return state.replaceAll(EVERY_NTH, "\n");
	    }

	    // conventional row major 2D-1D index transformation
	    static int rc2i(int r, int c) {
	        return r * N + c;
	    }

	    // checks if an entity is of a given type
	    static boolean isType(char entity, String type) {
	        return type.indexOf(entity) != -1;
	    }

	    // finds the length of a car
	    static int length(char car) {
	        return
	            isType(car, LONGS) ? 3 :
	            isType(car, SHORTS) ? 2 :
	            0/0; // a nasty shortcut for throwing IllegalArgumentException
	    }

	    // in given state, returns the entity at a given coordinate, possibly out of bound
	    static char at(String state, int r, int c) {
	        return (inBound(r, M) && inBound(c, N)) ? state.charAt(rc2i(r, c)) : VOID;
	    }
	    static boolean inBound(int v, int max) {
	        return (v >= 0) && (v < max);
	    }

	    // checks if a given state is a goal state
	    static boolean isGoal(String state) {
	        return at(state, GOAL_R, GOAL_C) == GOAL_CAR;
	    }

	    // in a given state, starting from given coordinate, toward the given direction,
	    // counts how many empty spaces there are (origin inclusive)
	    static int countSpaces(String state, int r, int c, int dr, int dc) {
	        int k = 0;
	        while (at(state, r + k * dr, c + k * dc) == EMPTY) {
	            k++;
	        }
	        return k;
	    }

	    // the predecessor map, maps currentState => previousState
	    static Map<String,String> pred = new HashMap<String,String>();
	    // the breadth first search queue
	    static Queue<String> queue = new LinkedList<String>();
	    // the breadth first search proposal method: if we haven't reached it yet,
	    // (i.e. it has no predecessor), we map the given state and add to queue
	    static void propose(String next, String prev) {
	        if (!pred.containsKey(next)) {
	            pred.put(next, prev);
	            queue.add(next);
	        }
	    }

	    static int trace(String current) {
	        String prev = pred.get(current);
	        int step = (prev == null) ? 0 : trace(prev) + 1;
	        System.out.println(step);
	        System.out.println(prettify(current));
	        return step;
	    }

	    static void slide(String current, int r, int c, String type, int distance, int dr, int dc, int n) {
	        r += distance * dr;
	        c += distance * dc;
	        char car = at(current, r, c);
	        if (!isType(car, type)) return;
	        final int L = length(car);
	        StringBuilder sb = new StringBuilder(current);
	        for (int i = 0; i < n; i++) {
	            r -= dr;
	            c -= dc;
	            sb.setCharAt(rc2i(r, c), car);
	            sb.setCharAt(rc2i(r + L * dr, c + L * dc), EMPTY);
	            propose(sb.toString(), current);
	            //current = sb.toString(); // comment to combo as one step
	        }
	    }

	    static void explore(String current) {
	        for (int r = 0; r < M; r++) {
	            for (int c = 0; c < N; c++) {
	                if (at(current, r, c) != EMPTY) continue;
	                int nU = countSpaces(current, r, c, -1, 0);
	                int nD = countSpaces(current, r, c, +1, 0);
	                int nL = countSpaces(current, r, c, 0, -1);
	                int nR = countSpaces(current, r, c, 0, +1);
	                slide(current, r, c, VERTS, nU, -1, 0, nU + nD - 1);
	                slide(current, r, c, VERTS, nD, +1, 0, nU + nD - 1);
	                slide(current, r, c, HORZS, nL, 0, -1, nL + nR - 1);
	                slide(current, r, c, HORZS, nR, 0, +1, nL + nR - 1);
	            }
	        }
	    }
	    public static void main(String[] args) {
	    	int count = 1;
	    	for(int i = 0; i < t.length;i++) {
	    		String temp = t[i];
		        // typical queue-based breadth first search implementation
		        propose(temp, null);
		        boolean solved = false;
		        HashSet<String> set = new HashSet<>();
		        while (!queue.isEmpty()) {
		            String current = queue.remove();
		            /*
		            if(set.contains(current))
		            	continue;
		            else
		            	set.add(current);
		            */	
		            if (isGoal(current) && !solved) {
		                solved = true;
		                trace(current);
		                break; // comment to continue exploring entire space
		            }
		            explore(current);
		        }
		        System.out.println("MAP "+ count++ + " " + pred.size() + " explored");
		        pred.clear();
	    	}
	    }
	    static final String[] t = {
	    		//"2222B.22B.B.C.BXX.C333..C..B22.22B22",
	    		//"C22.B.CBB.BCCBBXXC333B.C..BB2222B22.",//51
	    		//"2222B.22B.BCC.BXXCC333.CC..B22.22B22",
	    		//"33322.22.B..XXBB.B22B22BB22.BBB22.BB",
	    		//"......22B...BBBXXCBB22BC.22BBC333B22",
	    		//"2222BBB...BBB..XXCC22B.CCBBB.CCBB333",
	    		//"33322B22B22BXXBC.B22.C.BB22C..B.2222",
	    		//"2222BC22B.BC..BXXCC333..C..B22C22B22",
	    		//"333C2222.CBBXXBCBBB.B22CB22..C333..C",
	    		//"333C..22.CBBXX.CBBB.2222B.B33322B...",
	    		//"333C22..BC22XXBCCBB.22CBB.22CB333..B",
	    		//"33322.22B.BBXXBCBBB22C22B22C.B22...B",
	    		//"22CC.B22CCBBXXCCBCB..22CB....C22....",
	    		//"22CC2222CC..XXCC..B..22CB...BC22..BC",
	    		//"22CC..22CC.CXXCC.CB..22CB...B.22..B.",
	    		//"33322B22.22BXXBC..22BC.BB22C.BB.2222",
	    		//"22CC.C22CC.CXXCCBCB.22B.B.....22....",
	    		//"33322.22.B..XXBB.B22B22BB22.BBB22.BB",
	    		//"2222B..BB.BC.BBXXCC333.CC..B22C..B22",
	    		//"2222B.22B.BC.BBXXCCB333CC..B22C..B22",
	    		//"22CC....CC..XXCC.B22..BBB22.BBB2222B",
	    		//"22CC..22CC.BXXCCBBB.22BCB....C22...C",
	    		//"333C22.22CBBXX.CBBB.B22CB.B22C333..C",
	    		//"33322.22.B.BXXBB.B22B22BB22.BBB22.B.",
	    		//"33322B22.22BXXB...22BC.BB22C.BB22C22",
	    		//"22CC..22CCB.XXCCBCB..22CB....C22....",
	    		//"33322B22B.BBXXB.BBB2222BB22B..22.B..",
	    		//"......22B..C.BBXXCBB22BCB22BB.333B22",
	    		//"333C.B.22CBBXX.CB.B.2222B.B33322B...",
	    		//"33322C.333.CXXBB.C..BB22B.22B.B333B.",
	    		//"333C..22BCB.XXBCBBB22..BB.B22.22B...",
	    		};

}
