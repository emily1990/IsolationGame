package isolation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Input {
		public static int size = 8;
		public static double timelimit;
		public static int myoldrow ;
		public static int myoldcol ;
		public static int hisoldrow ;
		public static int hisoldcol;
		Input() {
		}

		public static void main(String args[]){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Ya Cai");
		System.out.println("Please give the time limit in seconds");
		String time = " ";
		try{
			time = br.readLine();
			
		}catch(IOException e){
			System.out.println("Input wrong");
		}
		long setTime = Long.parseLong(time);
		 String sure = null;
		 String	role = null;
		do{
			System.out.println("Am I a X or O");
			try{
				role = br.readLine();
				}catch(IOException e){
					System.out.println("Input wrong");
				}
			System.out.println("are you sure?(y/n)");
			try{
				sure = br.readLine();
			
				}catch(IOException e){
					System.out.println("Input wrong");
					}
		}while(!sure.equals("y") );
		int[][] position = new int[2][2];
		char[][] status = new char[size][size];
		Board board = new Board(setTime*1000);
		status = board.ini_board(role, position);
		board.print(status);
		String rolecopy = role;
		while(true){
			if(role.equals("X")){
				int player = 0;
				System.out.println("Let me compute");
				status = board.myturn( position,status);
				board.print(status);
				role = "O";
				
			}
			
			int[] newposition={0,0};
			String string2;
			do{
				System.out.println("opponent's choice:");
			try{
				int player=0;
				if(role == "O"){player=1;}
				int i=0;
				 String string = br.readLine();
				 Pattern p = Pattern.compile("\\D+");
				 String[] numbers = p.split(string);
				 for (String number :numbers){
					 if(null!=number && !"".equals(number)&&i<2)
						 newposition[i++]=Integer.parseInt(number);
				 }
				}catch(IOException e){
					System.out.println("Input wrong");
				}
			 string2 = null;
			System.out.println("Are you sure?(y/n)");
			try{
				 string2 = br.readLine();
				 
				}catch(IOException e){
					System.out.println("Input wrong");
				}
			}while(!string2.equals( "y"));
			
			int[][] copyposition = position;
			if(newposition[0]==9&&newposition[1]==9){
				int player1 = 0;
				board.unmove(copyposition, status, player1, myoldrow, myoldcol);
				player1 = 1-player1;
				board.unmove(copyposition, status, player1, hisoldrow, hisoldcol);
				System.out.println(Arrays.toString(position[0]));
				board.print(status);
			}
			else{
				hisoldrow = position[1][0];
				hisoldcol = position[1][1];
				int m =board.opturn(position,status, newposition);
				 if(m==1){
					
					myoldrow = position[0][0];
					myoldcol = position[0][1];
					status = board.myturn(position, status);
					board.print(status);
				}
				 else if(m==2){
					 break;
				 }

				}
			}

		}
}
