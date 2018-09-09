package com.a2944100.reversi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by silentshad on 20/03/17.
 */

/**
 * class responsible of the display pf the actual game and handling the touch interaction
 */
public class BoardView extends View {

    Board board;
    int curr_player;
    int size;
    RectF oval;
    Rect block;
    Paint color_frame;
    Paint color_discs;
    boolean pause;


    /**
     * constructor for the class , initialize the board and player( player 0 the black player start)
     * @param context the context of the view
     * @param attrs the attribute set
     */
    public BoardView(Context context , AttributeSet attrs) {

        super(context, attrs);
        board = new Board();
        curr_player = 0;
        block = new Rect();
        color_frame = new Paint();
        color_discs = new Paint();
        oval = new RectF();
        pause = false;
        curr_player = 0;
    }

    /**
     * restart a new game
     */
    public void restart()
    {
        board.init();
        curr_player =0;
        pause=false;
        invalidate();
    }

    /**
     * onDraw function , invalidate is done each time the user touch the board or after retarting a game
     * @param canvas the canvas where we draw
     */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!pause) {
            if (!board.can_play(1) && !board.can_play(0)) {
                pause = true;
                int b_score = board.getScore(0);
                int w_score = board.getScore(1);
                if (b_score > w_score)
                    onWin("Black");
                else if (w_score > b_score)
                    onWin("White");
                else
                    onWin("Nobody");
            }
            if (curr_player == 0 && !board.can_play(0) && board.can_play(1)) {
                Toast t = Toast.makeText(getContext(), "black cant play, white turn", Toast.LENGTH_LONG);
                t.show();
                curr_player = (curr_player + 1) % 2;
            }
            if (curr_player == 1 && !board.can_play(1) && board.can_play(0)) {
                Toast t = Toast.makeText(getContext(), "white cant play, black turn", Toast.LENGTH_LONG);
                t.show();
                curr_player = (curr_player + 1) % 2;
            }

            float block_s = size / (float) board.size;

            block.left = 0;
            block.top = 0;
            block.bottom = block.top + size;
            block.right = block.left + size;


            color_frame.setARGB(255, 22, 22, 22);
            color_frame.setStyle(Paint.Style.STROKE);
            color_frame.setStrokeWidth(10);

            canvas.drawRect(block, color_frame);


            LinearLayout hud = (LinearLayout) (((View) getParent()).findViewById(R.id.hud));
            hud.findViewById(R.id.curr_player_color).setBackgroundColor(Color.rgb(curr_player * 255, curr_player * 255, curr_player * 255));
            ((TextView) (hud.findViewById(R.id.white_score))).setText("" + board.getScore(0));
            ((TextView) (hud.findViewById(R.id.black_score))).setText("" + board.getScore(1));


            for (int i = 0; i < board.size; i++) {
                for (int j = 0; j < board.size; j++) {
                    Discs current = board.get(i, j);


                    block.left = (int) (i * block_s);
                    block.top = (int) (block_s * j);
                    block.bottom = block.top + (int) block_s;
                    block.right = block.left + (int) block_s;

                    float oval_size = block_s * 0.85f;
                    oval.left = block.left + (block_s - oval_size) / 2;
                    oval.top = block.top + (block_s - oval_size) / 2;
                    oval.bottom = oval.top + oval_size;
                    oval.right = oval.left + oval_size;

                    color_frame.setARGB(255, 42, 42, 42);
                    color_frame.setStyle(Paint.Style.STROKE);
                    color_frame.setStrokeWidth(5);

                    canvas.drawRect(block, color_frame);

                    if (current.color >= 0) {
                        int player_color = current.color;
                        color_discs.setARGB(255, player_color * 255, player_color * 255, player_color * 255);
                        canvas.drawOval(oval, color_discs);
                    }
                }
            }

        }
    }

    /**+
     * function called when the user touch board
     * @param event the motion event
     * @return return true if the touch was in the board (which is always the case has the ontouch listener is only on the boardview)
     */
    public boolean onTouchEvent( MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        int discs_touchedX = x / (size / board.size);
        int discs_touchedY = y / (size / board.size);


        discs_touchedX = (discs_touchedX>=board.size ? board.size-1 : discs_touchedX);
        discs_touchedY = (discs_touchedY>=board.size ? board.size-1 : discs_touchedY);

        if(board.get(discs_touchedX,discs_touchedY).color <0) {
            ArrayList<Discs> possible_turn = board.full_check(curr_player, discs_touchedX, discs_touchedY);
            invalidate();
            if (possible_turn.size() > 0) {
                board.turn(curr_player, possible_turn);
                board.get(discs_touchedX,discs_touchedY).color = curr_player;
                curr_player = (curr_player + 1) % 2;
            }

        }
            return true;
    }

    /**
     * called when there are no move possible create and show an AlertDialog which tell who has won
     * @param winner the name of the winner
     * can be either black, white or nobody in case of an equality
     */
    private void onWin(String winner)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle( winner +" has won!!");
        dialog.show();
    }

    /**
     * function called when creating the board , set the view size to the maximal size possible while
     * the view is a square so that the width and heigt are set to the minimum between maxHeight and mawWidth
     * @param widthMeasureSpec width send by android
     * @param heightMeasureSpec height send by android
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int desiredWidth =  widthSize ;
        int desiredHeight = heightSize;


        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        size = Math.min(width,height);
        setMeasuredDimension(size, size);

    }
}
