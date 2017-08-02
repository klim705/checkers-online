/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.*;


/**
 * Created by AnthonySangrigoli on 7/27/17.
 */

public class Checkers extends Board implements ActionListener  {

    Board board = null;
    JFrame frame = new JFrame();
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private JPanel boardPanel = new JPanel();
    private JButton[][] boardButtons = new JButton[8][8];
    private JPanel hudPanel = new JPanel();
    private JPanel newGamePanel = new JPanel();
    private JButton newGameButton = new JButton("New Game");
    private JPanel playerTurnPanel = new JPanel();
    private JLabel playerTurnLabel = new JLabel("Player 1's Turn");

    private ImageIcon emptyIcon = new ImageIcon("resources/EvenEmpty.png");

    private ImageIcon checkOddBlank = new ImageIcon("resources/OddEmpty.png");
    private ImageIcon checkEvenBlank = new ImageIcon("resources/EvenEmpty.png");
    private ImageIcon checkLightMan = new ImageIcon("resources/RedMan.png");
    private ImageIcon checkDarkMan = new ImageIcon("resources/BlackMan.png");
    private ImageIcon checkLightKing = new ImageIcon("resources/RedKing.png");
    private ImageIcon checkDarkKing = new ImageIcon("resources/BlackKing.png");
    private ImageIcon checkLightManSel = new ImageIcon("resources/SelectedRedMan.png");
    private ImageIcon checkDarkManSel = new ImageIcon("resources/SelectedBlackMan.png");
    private ImageIcon checkLightKingSel = new ImageIcon("resources/SelectedRedKing.png");
    private ImageIcon checkDarkKingSel = new ImageIcon("resources/SelectedBlackKing.png");


    public static void main(String[] args) {
        new Checkers();
    }


    public Checkers()
    {
        frame.setSize(400,500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Checkers");


        setupGUI();


        //this.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void setupGUI()
    {
        Container cP = frame.getContentPane();
        cP.add(boardPanel, BorderLayout.CENTER);
        playerTurnPanel.add(playerTurnLabel);
        newGamePanel.add(newGameButton);
        hudPanel.add(newGamePanel, BorderLayout.CENTER);
        cP.add(playerTurnPanel, BorderLayout.NORTH);
        cP.add(hudPanel, BorderLayout.SOUTH);
        boardPanel.setLayout(new GridLayout(8, 8));

        for (int i=0; i<8; i++)
        {
            for (int j=0; j<8; j++)
            {
                boardButtons[i][j] = new JButton("");
                boardButtons[i][j].addActionListener(this);
                boardButtons[i][j].setIcon(emptyIcon);
                boardButtons[i][j].setActionCommand("" + i + "" + j);
                boardPanel.add(boardButtons[i][j]);
            }
        }

        board = new Board();

        updateBoard();
    }

    public void updateBoard()
    {
        for(int i=0; i<8; i++)
        {
            for(int j=0; j<8; j++)
            {
                //System.out.println(cells[i][j]);
                if(cells[i][j] == Board.pType.BLANK)
                {
                    if((i+j)%2 == 0)
                    {
                        boardButtons[i][j].setIcon(checkEvenBlank);
                    }
                    else
                    {
                        boardButtons[i][j].setIcon(checkOddBlank);
                    }
                }
                else if(cells[i][j] == Board.pType.REDMAN)
                {
                    boardButtons[i][j].setIcon(checkLightMan);
                }
                else if(cells[i][j] == Board.pType.BLACKMAN)
                {
                    boardButtons[i][j].setIcon(checkDarkMan);
                }
                else if(cells[i][j] == Board.pType.REDKING)
                {
                    boardButtons[i][j].setIcon(checkLightKing);
                }
                else if(cells[i][j] == Board.pType.BLACKKING)
                {
                    boardButtons[i][j].setIcon(checkDarkKing);
                }
            }
        }
    }


    public void actionPerformed(ActionEvent e)
    {
        updateBoard();

        String command = e.getActionCommand();
        int row = Integer.parseInt("" + command.charAt(0));
        int col = Integer.parseInt("" + command.charAt(1));
        Dimension dim = new Dimension(row,col);

        setPieceSelected(row, col);

        System.out.println(dim);
        System.out.println(Board.getPieceSelected());

        if(Board.getPieceSelected() != Board.pType.BLANK)
        {
            PieceType cell = cells[row][col];
            if(cell == Board.pType.REDMAN)
            {
                boardButtons[row][col].setIcon(checkLightManSel);
            }
            else if(cell == Board.pType.BLACKMAN)
            {
                boardButtons[row][col].setIcon(checkDarkManSel);
            }
            else if(cell == Board.pType.REDKING)
            {
                boardButtons[row][col].setIcon(checkLightKingSel);
            }
            else if(cell == Board.pType.BLACKKING)
            {
                boardButtons[row][col].setIcon(checkDarkKingSel);
            }
        }
    }
}
