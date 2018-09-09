package com.a2944100.reversi;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by silentshad on 20/03/17.
 */

/**
 * class responsible for handling the board logic
 */
 class Board {
    int size = 8;
    private Discs[][] board;

    /**
     * Constructor for the class make a call to the init function
     */
    Board() {

    init();
    }

    /**
     * initialize the board with the four starting discs at the center
     */
    void init() {
    board =new Discs[size][size];

    for(int i = 0;i<(size*size);i++)
    {
        board[i / size][i % size] = new Discs();
    }
        board[size/2 -1][size/2-1].color = 0;
        board[size/2][size/2].color = 0;
        board[size/2-1][size/2].color = 1;
        board[size/2][size/2-1].color = 1;

    }

    /**
     * get a specific disk in the board, the parameter must be valid coordinate
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the discs present in the board at position [x,y]
     */
    Discs get (int x, int y)
    {
        return board[x][y];
    }

    /**
     * check if a pair of coordinates is valid
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if valid , false if not
     */
    boolean isvalid(int x, int y)
    {
        return ( x<size && x>= 0 && y<size  && y >=0);
    }


    /**
     *  given a position and a player say if the player can turn anything in the 8 direction,
     *  whether the given position is empty must be check before calling this function
     * @param curr_player current player: 0 for black player , 1 for white player
     * @param origin_x  the x coordinate
     * @param origin_y  the y coordinate
     * @return a list of all discs that could be turn if the player play on the original position
     */
    ArrayList<Discs> full_check(int curr_player,int origin_x , int origin_y)
    {
        ArrayList<Discs> to_turnList = new ArrayList<>();
        ArrayList<Discs> temp;

        temp = check_line(curr_player,origin_x,origin_y,1,0);
        if(temp != null)
            to_turnList.addAll(temp);

        temp = check_line(curr_player,origin_x,origin_y,1,-1);
        if(temp != null)
            to_turnList.addAll(temp);

        temp = check_line(curr_player,origin_x,origin_y,0,-1);
        if(temp != null)
            to_turnList.addAll(temp);

        temp = check_line(curr_player,origin_x,origin_y,-1,-1);
        if(temp != null)
            to_turnList.addAll(temp);

        temp = check_line(curr_player,origin_x,origin_y,-1,0);
        if(temp != null)
            to_turnList.addAll(temp);

        temp = check_line(curr_player,origin_x,origin_y,-1,1);
        if(temp != null)
            to_turnList.addAll(temp);

        temp = check_line(curr_player,origin_x,origin_y,0,1);
        if(temp != null)
            to_turnList.addAll(temp);

        temp = check_line(curr_player,origin_x,origin_y,1,1);
        if(temp != null)
            to_turnList.addAll(temp);


        return to_turnList;
    }

    /**
     * given a list of discs and a player put all the discs color to the player's one
     * used to turn reverse chain
     * @param curr_player current player: 0 for black player , 1 for white player
     * @param to_turnList list contaning the discs that should be turn
     */
    public void turn(int curr_player, ArrayList<Discs> to_turnList)
    {
        for (Discs d :to_turnList) {
            d.color = curr_player;
        }
    }

    /**
     * get the score of a particular player
     * @param player current player: 0 for black player , 1 for white player
     * @return return the score
     */
    public int getScore(int player)
    {
        int score =0;
        for(int i = 0;i<(size*size);i++)
        {
            if(board[i / size][i % size].color == player)
                score++;
        }
        return score;
    }

    /**
     * given a player say there is a empty position on the board that could get him some point
     * @param player current player: 0 for black player , 1 for white player
     * @return true if the player can play false else
     */
    public boolean can_play(int player)
    {
        for(int i = 0;i<size;i++)
        {
            for(int j = 0; j<size;j++)
            {
                if(get(i,j).color==-1 && full_check(player,i,j).size()>0 )
                    return true;
            }
        }
        return false;
    }

    /**
     *given a player a starting position and a pair of directional indication check if a reverse chain
     * can be maid return null if not else return the discs composing the reverse chain
     * @param curr_player current player: 0 for black player , 1 for white player
     * @param origin_x starting x position
     * @param origin_y starting y position
     * @param off_x directional indication on x goes to the left if -1 , to the right if+1 do not move if 0
     * @param off_y directional indication on y goes to the top if -1 , to the bottom if+1 do not move if 0
     * @return null if no reverse chain can be form else the list of each discs composing the reverse chain
     */
    public ArrayList<Discs> check_line(int curr_player,int origin_x , int origin_y, int off_x, int off_y ) {
        ArrayList<Discs> returnList = new ArrayList<>();
        int i = 1;
        if (isvalid(origin_x + i * off_x, origin_y + i * off_y))
        {
            Discs current = get(origin_x + i * off_x, origin_y + i * off_y);
            while (isvalid(origin_x + i * off_x, origin_y + i * off_y) && current.color >= 0 && curr_player != current.color)
            {
                current = get(origin_x + i * off_x, origin_y + i * off_y);
                returnList.add(current);
                i++;

            }
            if (isvalid(origin_x + i * off_x, origin_y + i * off_y) && curr_player == current.color)
                return returnList;
            else
                return null;
        }
        return null;
    }


}
