/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;


import java.awt.Dimension;
import java.io.Serializable;


public class Board implements Serializable
{

    public enum PieceType {
        BLANK, REDMAN, REDKING, BLACKMAN, BLACKKING}

    public static PieceType pType;
    public static PieceType cells[][];

    public Board()
    {
        cells=new PieceType[8][8];
        pieceSelected=pType.BLANK;
        selection=new Dimension();
        resetBoard();
    }


    protected static PieceType pieceSelected;
    public static void setPieceSelected(int row, int col)
    {
        pieceSelected = cells[row][col];
    }
    public static PieceType getPieceSelected()
    {
        return pieceSelected;
    }

    protected static Dimension selection;
    public static Dimension getSelection()
    {
        return selection;
    }


    public static PieceType getCellType(int row, int col)
    {   return cells[row][col];
    }

    public void resetBoard()
    {
        for(int i=0; i<8; i++)
        {
            for(int j=0; j<8; j++)
            {
                cells[i][j] = pType.BLANK;
            }
        }
        for(int j=0; j<8; j+=2)
        {
            cells[7][j] = pType.REDMAN;
            cells[6][j + 1] = pType.REDMAN;
            cells[5][j] = pType.REDMAN;
            cells[0][j + 1] = pType.BLACKMAN;
            cells[1][j] = pType.BLACKMAN;
            cells[2][j + 1] = pType.BLACKMAN;
        }
    }
}
