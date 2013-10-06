package isolation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Board {
	public static int size = 8;
	public static int my =1;
	public static int opponent =0;
	public static int Toprow = 1;
	public static int Leftcol = 1;
	public static int Bottomrow = size;
	public static int Rightcol = size;
	public static char[] symbol={'X','O'};	
	public static int infinite = 10000;
	public static double startTime;
	public static int ENDdepth;
	public int[] bestMove;
	public int allmovecount=0;
	public long timelimit;
	Board(long time){
		timelimit = time+1;
	}
	//initialize the board according to the role
	public char[][] ini_board(String role1, int[][] position){
		char[][] status = new char[size][size];
		for	(int row=0;row<size;row++){
			for(int col = 0; col < size; col++){
				status[row][col]='-';	
			}	
		}
		if(role1.equals("X")){
			my = 0;
			opponent = 1;
		}
		else{
			symbol[0] = 'O';
			symbol[1] = 'X';
			
		}
		position[my][0]=Toprow;
		position[my][1]=Leftcol;
		status[Toprow-1][Leftcol-1] = 'X' ;
		position[opponent][0]=Bottomrow;
		position[opponent][1]=Rightcol;
		status[Bottomrow-1][Rightcol-1] = 'O';
		return status;
	}
	/**
	 * print the board
	 */
	public void print(char[][] status){

		System.out.println();
		for(int i = 0;i < size;i++){
			for(int j = 0;j < size; j++){
				System.out.print(status[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	/**
	 * 
	 */
	public char[][] myturn(int[][] position, char[][] status){
		
		startTime=System.currentTimeMillis();
		allmovecount++;
		int player = 0;
		status = alpha_beta(status,position);
		if(moveavailable(status, position, 1-player)==0){
			System.out.println("I win");
		}
		else if(moveavailable(status, position,player)==0){
			System.out.println("I lose");
		}
		return status;
	}
	/**
	 * Algorithm
	 */
	public char[][] alpha_beta(char[][] status,int[][] position){
		
		ENDdepth = 4;
		int player = 0;
		int start = 0;
		int depth = 0;
		int[] bestmove = null;
		//List<int[]> choice = new ArrayList();
		//choice = find_movelist(position, status, player);
	//	Iterator<int[]> it = choice.iterator();
		//if(it.hasNext()){
			//System.out.println("choice"+Arrays.toString(it.next()));
		//}
		//Iterator it = choice.iterator();
		//int[] bestMove = (int[]) it.next();
		//int[] bestmove = bestMove;
		//bestmove = bestMove.clone();
		//bestmove[0] =bestMove[0];
		//bestmove[1] =bestMove[1];
	//	find_movelist(position, status, depth);
		do{
			
			MAX_VALUE(position, status, -infinite, infinite, start);
			if(((double)System.currentTimeMillis()) - startTime < timelimit){
			//	System.out.println("BestMove1:"+Arrays.toString(bestMove));
			if(bestMove!=null){
				bestmove = bestMove;
			depth = ENDdepth;
			ENDdepth += 2;
			}
		}
		}while(((double)System.currentTimeMillis()) - startTime < timelimit&&ENDdepth<62-allmovecount);
		//System.out.println(depth);
		System.out.println(Arrays.toString(bestmove));
		if(bestmove!=null){
			move(position, status, player, bestmove[0], bestmove[1]);
		}
	//	if(bestMove!=null){
		//	move(position, status, player, bestMove[0], bestMove[1]);
		//}
		return status;
	}
	/**
	 * opponent's choice
	 */
	public int opturn(int[][] position, char[][] status, int[] newposition){
		allmovecount++;
		int player = 1;
		if(canmove(position,status,player,newposition[0],newposition[1])){
			move(position, status, player, newposition[0], newposition[1]);
			return 1;
		}
		else{
			System.out.println(Arrays.toString(newposition));
			System.out.println("your choice is not legal");
			System.out.println("Are you sure this is your result(y/n) ");
			String string = " ";
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try{
				string = br.readLine();
			}catch(IOException e){
				System.out.println("Input wrong");
			}
			if (string.equals( "y")){
				System.out.println("You lose!");
				return 2;
			}
			return 0;
		}
	}
	/**
	 * see if it can still be moved
	 */
	public int moveavailable(char[][] status, int[][] position, int player){
		int currow = position[player][0];
		int curcol = position[player][1];
		int count = 0;
		for(int i = 1; i<=size;i++){
			
			if(canmove(position, status, player, currow, i)){
				count++;
			}
			if(canmove(position, status, player, i, curcol)){
					count++;	
			}
			if(currow+i <= size&&curcol+i <= size){
				if(canmove(position, status, player, currow+i, curcol+i)){
				count++;
			}}
			if(currow-i >=1 && curcol - i >=1 ){
				if(canmove(position, status, player, currow-i, curcol-i)){
					count++;
				}	
			}
			if(currow+i <= size && curcol - i >= 1){
				if(canmove(position, status, player, currow+i, curcol-i)){
					count++;
				}
			}
			if(currow-i >= 1 && curcol + i <= size){
				if(canmove(position, status, player, currow-i, curcol+i)){
					count++;
				}
			}
		}
			return count;
	}
	public boolean canmove(int[][] position, char[][] status, int player, int goalrow, int goalcol){
		int currow = position[player][0];
		int curcol = position[player][1];
		if(currow == goalrow){
			if(curcol == goalcol){
				return false;
			}
			else if(curcol < goalcol){
			for(int col = curcol+1;col <= goalcol;col++){
				if(status[currow-1][col-1] != '-'){
					return false;
				}
			}
			return true;
			}
			else{
				for(int col = curcol-1;col >= goalcol;col--){
					if(status[currow-1][col-1] != '-'){
						return false;
					}
				}
				return true;
			}
		}
		
		else if(curcol == goalcol){
			if(currow == goalrow){
				return false;
			}
			if(currow < goalrow){
				for(int row = currow+1;row <= goalrow;row++){
					if(status[row-1][curcol-1] != '-'){
						return false;
				}
			}
				return true;
			}
			else{
				for(int row = currow-1;row >= goalrow;row--){
					if(status[row-1][curcol-1] != '-'){
						return false;
			}
		}return true;
			}
		}
		
		else if(Math.abs(goalcol-curcol) == Math.abs(goalrow-currow)){
			 if(currow < goalrow && curcol < goalcol){
				 int col = curcol+1;
				 for(int row = currow+1;row<=goalrow; row++,col++){
					 if(status[row-1][col-1] != '-'){
						 return false;
					 }
				 }
				 return true;
			 }
			 else if(currow < goalrow && curcol > goalcol){
				 int col = curcol-1;
				 for( int row = currow+1;row <= goalrow; row++,col--){
					 if(status[row-1][col-1] != '-'){
						 return false;
					 }
				 }return true;
			 }
			 else if(currow > goalrow && curcol > goalcol){
				 int col = curcol-1;
				 for(int row = currow-1;row>=goalrow; row--,col--){
					 if(status[row-1][col-1] != '-'){
						 return false;
					 }
				 } return true;
			 }
			 else{
				 int col = curcol+1;
				 for(int row = currow-1;row>=goalrow; row--,col++){
					 if(status[row-1][col-1] != '-'){
						 return false;
					 }
				 }
				 return true; 
			 }		 
		}
		
		else{
		return false;
		}
	}
	/**
	 * move the board to a new status
	 */
	public void move(int[][] position, char[][] status, int player, int row, int col ){
		status[position[player][0]-1][position[player][1]-1] = '*';
		position[player][0] = row;
		position[player][1] = col;
		status[row-1][col-1] = symbol[player];
	}
	/**
	 * find movelist
	 */
	public List find_movelist(int[][] position, char[][] status, int player){
		List<int[]> movelist = new ArrayList<int[]>();
		int currow = position[player][0];
		int curcol = position[player][1];
		for(int i = 1; i<=size;i++){
			
			if(canmove(position, status, player, currow, i)){
				movelist.add(new int[]{currow,i});

			}
			if(canmove(position, status, player, i, curcol)){
				movelist.add(new int[]{i,curcol});

			}
			if(currow+i <= size&&curcol+i <= size){
				if(canmove(position, status, player, currow+i, curcol+i)){
				movelist.add(new int[]{currow+i,curcol+i});

			}}
			if(currow-i >=1 && curcol - i >=1 ){
				if(canmove(position, status, player, currow-i, curcol-i)){
					movelist.add(new int[]{currow-i,curcol-i});

				}	
			}
			if(currow+i <= size && curcol - i >= 1){
				if(canmove(position, status, player, currow+i, curcol-i)){
					movelist.add(new int[]{currow+i,curcol-i});
	
				}
			}
			if(currow-i >= 1 && curcol + i <= size){
				if(canmove(position, status, player, currow-i, curcol+i)){
					movelist.add(new int[]{currow-i,curcol+i});

				}
			}
		}
		return movelist;
	}
	
	/**
	 * MAX-VALUE
	 */
	public double MAX_VALUE(int[][] position, char[][] status, double alpha, double beta, long start){
		//Terminal test
		if(((double)System.currentTimeMillis()) - startTime > timelimit){
			return 0;
		}
		double v = - infinite;
		int player = 0;
		if(start>ENDdepth){
			return Estimate(position, status);
		}
		List<int[]> choice = new ArrayList();
		choice = find_movelist(position, status, player);
		//Iterator<int[]> it = choice.iterator();
		//if(it.hasNext()){
			//System.out.println("choice"+Arrays.toString(it.next()));
		//}
	//	Iterator it = choice.iterator();
	//	bestMove = (int[]) it.next();
		
		for(int[] c:choice){
					int[][] copyposition = position;
					int oldrow = position[player][0];
					int oldcol = position[player][1];
					move(copyposition, status, player, c[0],c[1]);
					double minvalue = MIN_VALUE(copyposition, status, alpha, beta, start+1);
					unmove(copyposition,status,player,oldrow, oldcol);
					if(minvalue > v){
						v =  minvalue;
						if(start == 0){
							bestMove=c;
							//System.out.println("c is:"+Arrays.toString(bestMove));
					}
					}
					if(v>=beta){
						return v;
					}
					alpha = Math.max(alpha, v);
				}
		return v;
	}
	public char[][] unmove(int[][] copyposition, char[][] status, int player,int oldrow, int oldcol) {
	// TODO Auto-generated method stub
		 status[oldrow-1][oldcol-1] = symbol[player];
		 status[copyposition[player][0]-1][copyposition[player][1]-1] = '-';
		 copyposition[player][0] = oldrow;
		 copyposition[player][1] = oldcol;
		 return status;
	}
	/**
	 * MIN-VALUE
	 */
	public double MIN_VALUE(int[][] position, char[][] status, double alpha, double beta, long start){
		if(((double)System.currentTimeMillis()) - startTime > timelimit-1){
			return 0;
		}
		double v = infinite;
		int player = 1;
		if(start > ENDdepth){
			return Estimate(position, status);
		}
		List<int[]> minchoice  = find_movelist(position, status, player);
		
				for(int[] c:minchoice){
					int[][] copyposition = position;
					int oldrow = position[player][0];
					int oldcol = position[player][1];
					move(copyposition, status, player, c[0],c[1]);
					double maxvalue = MAX_VALUE(copyposition, status, alpha, beta, start+1);
					unmove(copyposition, status, player, oldrow, oldcol);
					v = Math.min(v, maxvalue);
					if(v<=alpha){
						return v;
					}
					beta = Math.min(beta, v);
				}
		return v;
	}
	/**
	 * Estimate
	 */
	public int Estimate(int[][] position, char[][] status){
		if(allmovecount < 10){
			return netcanMove(position, status);
		}
		else{
			int areaDif = areaDifference(status, position);
			if(areaDif != 0){
				return areaDif * 20;
			}
			else{
				return netcanMove(position,status);
			}
		}
		
	}
	public int netcanMove(int[][] position, char[][] status){
		return moveavailable(status, position,0) - moveavailable(status, position,1);
	}
	
	public int areaDifference(char[][] status , int[][] position){
		return areaSize(status, position, 0) - areaSize(status, position, 1);
	}

	/**
	 * Counts the Size of an area available to a player
	 */
	public int areaSize(char[][] status, int[][] position, int player){
		int row = position[player][0];
		int col = position[player][1];
		int counter = 0;
		char[][] board = new char[8][8];
		for(int j = 0; j<size; j++){
			for(int k = 0; k < size; k++){
				board[j][k]=status[j][k];
			}
		}
		
	//	char[][] board = status.clone();
		
		//char[][] copy1 = status;
		board = makeArea(board, row, col);
		//status = copy1;
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				if (board[i][j] == 'S'){
					counter++;
				}
			}
		}
		return counter;
	}

	/**
	 * Recursive Area maker to check how much area a player can reach from his current position
	 */
	public char[][] makeArea(char[][] board, int row, int col){
		//Down
		if (row + 1 <= size){
			if (board[row ][col-1] == '-'){
				board[row ][col-1] = 'S';
				makeArea(board, row + 1, col);
			}
		}
		//Up
		if (row - 1 >= 1){
			if (board[row - 2][col-1] == '-'){
				board[row - 2][col-1] = 'S';
				makeArea(board, row - 1, col);
			}
		}
		//Right
		if (col + 1 <= size){
			if (board[row-1][col ] == '-'){
				board[row-1][col ] = 'S';
				makeArea(board, row, col + 1);
			}
		}
		//Left
		if (col - 1 >= 1){
			if (board[row-1][col - 2] == '-'){
				board[row-1][col - 2] = 'S';
				makeArea(board, row, col - 1);
			}
		}
		//Down Right
		if (col + 1 <= size && row + 1 <= size){
			if (board[row][col] == '-'){
				board[row ][col] = 'S';
				makeArea(board, row + 1, col + 1);
			}
		}
		//Down Left
		if (col - 1 >= 1 && row + 1 <= size){
			if (board[row ][col - 2] == '-'){
				board[row ][col - 2] = 'S';
				makeArea(board, row + 1, col - 1);
			}
		}
		//Up Right 
		if (col + 1 <= size && row - 1 >= 1){
			if (board[row - 2][col ] == '-'){
				board[row - 2][col ] = 'S';
				makeArea(board, row - 1, col + 1);
			}
		}
		//Up Left
		if (col - 1 >= 1 && row - 1 >= 1){
			if (board[row - 2][col - 2] == '-'){
				board[row - 2][col - 2] = 'S';
				makeArea(board, row - 1, col - 1);
			}
		}
		return board;
	}

}
