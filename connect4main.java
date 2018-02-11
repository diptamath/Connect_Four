import java.util.Scanner;
public class Konnect4New
{ 
    //private Board b;
    private Scanner scan;
    private int nextMoveLocation=-1;
    private int maxDepth = 8;
    byte[][] board = new byte[4][8];
    public Konnect4New()
    {
        for (int i=0; i<4; i++)
        {
            for (int j=0; j<8; j++)
            {
                board[i][j]=0;
            }
        }
        scan = new Scanner(System.in);
    }
    
    public boolean isLegalMove(int column)
    {
        if (column<0 || column>7) return false;
        return board[0][column]==0;
    }
    
    //Placing a Move on the board
    public boolean placeMove(int column, int player)
    { 
        if(!isLegalMove(column)) {System.out.println("Illegal move!"); return false;}
        for(int i=3;i>=0;--i){
            if(board[i][column] == 0) {
                board[i][column] = (byte)player;
                return true;
            }
        }
        return false;
    }
    
    public void undoMove(int column)
    {
        for(int i=0;i<=3;++i){
            if(board[i][column] != 0) {
                board[i][column] = 0;
                break;
            }
        }        
    }
    //Printing the board
    public void displayBoard()
    {
        System.out.println();
        for(int i=0;i<=3;++i){
            for(int j=0;j<=7;++j){
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    
    //Opponent's turn
    
    
    public void letOpponentMove(int move)
    {
        //System.out.println("Your move (1-7): ");
        //int move = scan.nextInt();
        //while(move<1 || move > 7 || !isLegalMove(move-1)){
        //    System.out.println("Invalid move.\n\nYour move (1-7): "); 
         //   move = scan.nextInt();
        //
        
        //Assume 2 is the opponent
        placeMove(move-1, (byte)2); 
    }
    
    
    
    //Game Result
    public int gameResult()
    {
        int aiScore = 0, humanScore = 0;
        for(int i=3;i>=0;--i){
            for(int j=0;j<=7;++j){
                if(board[i][j]==0) continue;
                
                //Checking cells to the right
                if(j<=4){
                    for(int k=0;k<4;++k){ 
                            if(board[i][j+k]==1) aiScore++;
                            else if(board[i][j+k]==2) humanScore++;
                            else break; 
                    }
                    if(aiScore==4)return 1; else if (humanScore==4)return 2;
                    aiScore = 0; humanScore = 0;
                } 
                
                //Checking cells up
                if(i>=3){
                    for(int k=0;k<4;++k){
                            if(board[i-k][j]==1) aiScore++;
                            else if(board[i-k][j]==2) humanScore++;
                            else break;
                    }
                    if(aiScore==4)return 1; else if (humanScore==4)return 2;
                    aiScore = 0; humanScore = 0;
                } 
                
                //Checking diagonal up-right
                if(j<=4 && i>= 3){
                    for(int k=0;k<4;++k){
                        if(board[i-k][j+k]==1) aiScore++;
                        else if(board[i-k][j+k]==2) humanScore++;
                        else break;
                    }
                    if(aiScore==4)return 1; else if (humanScore==4)return 2;
                    aiScore = 0; humanScore = 0;
                }
                
                //Checking diagonal up-left
                if(j>=4 && i>=3){
                    for(int k=0;k<4;++k){
                        if(board[i-k][j-k]==1) aiScore++;
                        else if(board[i-k][j-k]==2) humanScore++;
                        else break;
                    } 
                    if(aiScore==4)return 1; else if (humanScore==4)return 2;
                    aiScore = 0; humanScore = 0;
                }  
            }
        }
        
        for(int j=0;j<7;++j){
            //Game has not ended yet
            if(board[0][j]==0)return -1;
        }
        //Game draw!
        return 0;
    }
    
    int calculateScore(int aiScore, int moreMoves)
    {   
        int moveScore = 4 - moreMoves;
        if(aiScore==0)return 0;
        else if(aiScore==1)return 1*moveScore;
        else if(aiScore==2)return 10*moveScore;
        else if(aiScore==3)return 100*moveScore;
        else return 1000;
    }
    
    //Evaluate board favorableness for AI
    public int evaluateBoard()
    {
      
        int aiScore=1;
        int score=0;
        int blanks = 0;
        int k=0, moreMoves=0;
        for(int i=3;i>=0;--i){
            for(int j=0;j<=7;++j){
                
                if(board[i][j]==0 || board[i][j]==2) continue; 
                
                if(j<=4){ 
                    for(k=1;k<4;++k){
                        if(board[i][j+k]==1)aiScore++;
                        else if(board[i][j+k]==2){aiScore=0;blanks = 0;break;}
                        else blanks++;
                    }
                     
                    moreMoves = 0; 
                    if(blanks>0) 
                        for(int c=1;c<4;++c){
                            int column = j+c;
                            for(int m=i; m<= 3;m++){
                             if(board[m][column]==0)moreMoves++;
                                else break;
                            } 
                        } 
                    
                    if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
                    aiScore=1;   
                    blanks = 0;
                } 
                
                if(i>=3){
                    for(k=1;k<4;++k){
                        if(board[i-k][j]==1)aiScore++;
                        else if(board[i-k][j]==2){aiScore=0;break;} 
                    } 
                    moreMoves = 0; 
                    
                    if(aiScore>0){
                        int column = j;
                        for(int m=i-k+1; m<=i-1;m++){
                         if(board[m][column]==0)moreMoves++;
                            else break;
                        }  
                    }
                    if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
                    aiScore=1;  
                    blanks = 0;
                }
                 
                if(j>=4){
                    for(k=1;k<4;++k){
                        if(board[i][j-k]==1)aiScore++;
                        else if(board[i][j-k]==2){aiScore=0; blanks=0;break;}
                        else blanks++;
                    }
                    moreMoves=0;
                    if(blanks>0) 
                        for(int c=1;c<4;++c){
                            int column = j- c;
                            for(int m=i; m<= 3;m++){
                             if(board[m][column]==0)moreMoves++;
                                else break;
                            } 
                        } 
                    
                    if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
                    aiScore=1; 
                    blanks = 0;
                }
                 
                if(j<=4 && i>=3){
                    for(k=1;k<4;++k){
                        if(board[i-k][j+k]==1)aiScore++;
                        else if(board[i-k][j+k]==2){aiScore=0;blanks=0;break;}
                        else blanks++;                        
                    }
                    moreMoves=0;
                    if(blanks>0){
                        for(int c=1;c<4;++c){
                            int column = j+c, row = i-c;
                            for(int m=row;m<=3;++m){
                                if(board[m][column]==0)moreMoves++;
                                else if(board[m][column]==1);
                                else break;
                            }
                        } 
                        if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
                        aiScore=1;
                        blanks = 0;
                    }
                }
                 
                if(i>=3 && j>=4){
                    for(k=1;k<4;++k){
                        if(board[i-k][j-k]==1)aiScore++;
                        else if(board[i-k][j-k]==2){aiScore=0;blanks=0;break;}
                        else blanks++;                        
                    }
                    moreMoves=0;
                    if(blanks>0){
                        for(int c=1;c<4;++c){
                            int column = j-c, row = i-c;
                            for(int m=row;m<=3;++m){
                                if(board[m][column]==0)moreMoves++;
                                else if(board[m][column]==1);
                                else break;
                            }
                        } 
                        if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
                        aiScore=1;
                        blanks = 0;
                    }
                } 
            }
        }
        return score;
    } 
    
    public int minimax(int depth, int turn)
    {
        int gameResult = gameResult();
        if(gameResult==1)return Integer.MAX_VALUE;
        else if(gameResult==2)return Integer.MIN_VALUE;
        else if(gameResult==0)return 0;
        
        if(depth==maxDepth)return evaluateBoard();
        
        int maxScore=Integer.MIN_VALUE, minScore = Integer.MAX_VALUE;
        for(int j=0;j<=7;++j){
            if(!isLegalMove(j)) continue;
                
            if(turn==1){
                    placeMove(j, 1);
                    int currentScore = minimax(depth+1, 2);
                    maxScore = Math.max(currentScore, maxScore);
                    if(depth==0){
                        System.out.println("Score for location "+j+" = "+currentScore);
                        if(maxScore==currentScore) nextMoveLocation = j;
                    }
            }else if(turn==2){
                    placeMove(j, 2);
                    int currentScore = minimax(depth+1, 1);
                    minScore = Math.min(currentScore, minScore);
            }
            undoMove(j);
        }
        return turn==1?maxScore:minScore;
    }
    
    public int getAIMove()
    {
        nextMoveLocation = -1;
        minimax(0, 1);
        return nextMoveLocation;
    }
    public int checkTrivialWin()
    {
        int rowCount=0, colCount=0, diagCount=0;
        for (int i=0; i<=7; i++)
        {
            colCount=0;
            for (int k=4-1; k>=0; k--)
            {
                if (board[k][i]==1) colCount++;
                else colCount=0;
                if (colCount==3 && isLegalMove(i))
                {
                    if (board[k-1][i]==0) return i;
                }
            }    
        }
        for (int i=0; i<4; i++)
        {
            rowCount=0;
            for (int j=0; j<8; j++)
            {
                if (board[i][j]==1) rowCount++;
                else rowCount=0;
                if (rowCount==3 && (isLegalMove(j+1)||isLegalMove(j-1)))
                {
                    boolean flag=true;
                    for (int k=0; k<=i; k++)
                    if (board[k][j+1]!=0) flag=false;
                    if (flag) return j+1;
                    flag=true;
                    for (int k=0; k<=i; k++)
                    if (board[k][j-1]!=0) flag=false;
                    if (flag) return j-1;
                }
            }
        }
        return -1;            
    }   
    
    public int playAgainstAIConsole(int move)
    {
        int humanMove=-1;
        
        
        if(move==0)
        {
            displayBoard();
            placeMove(3, 1);
            displayBoard();
            return 3;
        }
        
         
            letOpponentMove(move);
            displayBoard();
            
            int gameResult = gameResult();
            if(gameResult==1){System.out.println("AI Wins!");return -1;}
            else if(gameResult==2){System.out.println("You Win!");return -1;}
            else if(gameResult==0){System.out.println("Draw!");return -1;}
            int pos1=checkTrivialWin();
            if (pos1!=-1)
            {
                placeMove(pos1,1);
                displayBoard();
                gameResult = gameResult();
                if(gameResult==1){System.out.println("AI Wins!");return -1;}
                else if(gameResult==2){System.out.println("You Win!");return -1;}
                else if(gameResult==0){System.out.println("Draw!");return -1;}
                return pos1;
            }
            int pos=getAIMove();
             System.out.println (pos1);
            placeMove(pos,1);
            System.out.println(pos+1);
            displayBoard();
            gameResult = gameResult();
            if(gameResult==1){System.out.println("AI Wins!");return -1;}
            else if(gameResult==2){System.out.println("You Win!");return -1;}
            else if(gameResult==0){System.out.println("Draw!");return -1;}
        
        return (pos+1);
    }
    
    public static void main(String[] args)
    {
        //Board b = new Board();
        Konnect4New ai = new Konnect4New();  
        Scanner sc=new Scanner (System.in);
        while (sc.hasNextInt())
        {
            int move=sc.nextInt();
            int win=ai.playAgainstAIConsole(move);
            System.out.printf("%d",win);
            if (win==-1) break;
        }
   }
}


