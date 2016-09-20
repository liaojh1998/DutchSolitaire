import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class DutchSolitaire extends java.applet.Applet implements MouseListener{

    private Card[][] defaultcards = new Card[4][14];
    private boolean[][] scramble = new boolean[4][14];
    private Card[][] cards = new Card[4][14];
    private Graphics goff;
    private Graphics g;
    private Image offscreen;
    private int moves;
    private int lastx, lasty;
    private boolean clicked;
    private boolean acemsg;
    private boolean spacemsg;
    private boolean swapmsg;
    private boolean completed;
    
    public void init(){
        for(int i = 0; i <= 3; i++){
        	for(int j = 0; j <= 13; j++){
        		String suit;
        		switch(i){
        			case 0: suit = "c"; break;
        			case 1: suit = "d"; break;
        			case 2: suit = "h"; break;
        			case 3: suit = "s"; break;
        			default: suit = ""; break;
        		}
        		defaultcards[i][j] = new Card(suit, j+2);
        	}
        }
        for(int i = 0; i <= 3; i++){
        	for(int j = 0; j <= 13; j++){
        		cards[i][j] = getCard();
        	}
        }
        setA();
        setA();
        setA();
       	goff();
        addMouseListener(this);
        g = this.getGraphics();
        paint(g);
    }
    
    public void goff(){
    	offscreen = createImage(1220, 401);
    	goff = offscreen.getGraphics();
        for(int i = 0; i <= 13; i++){
        	goff.drawLine(i*75, 0, i*75, 400);
        }
        for(int j = 0; j <= 3; j++){
        	goff.drawLine(0, j*100, 1050, j*100);
        }
        goff.drawRect(0,0,1050,400);
        for(int i = 0; i <= 3; i++){
        	for(int j = 0; j <= 13; j++){
        		goff.drawImage(cards[i][j].getImage(), j*75+1, i*100+1, null);
        	}
        }
        goff.setFont(new Font("Magneto", Font.PLAIN, 35));
        goff.setColor(Color.BLACK);
        goff.drawString("Moves: ", 1070, 100);
        goff.setFont(new Font("Magneto", Font.PLAIN, 30));
        goff.drawString(String.valueOf(moves), 1080, 150);
    }
    
    public void paint(Graphics g) {
        g.drawImage(offscreen, 0, 0, this);
    }
    
	public Card getCard()
	{
		Card c = null;
		boolean Done = false;
		Random rnd = new Random();
		while(!Done)
		{
			int xrndNum = rnd.nextInt(14);
			int yrndNum = rnd.nextInt(4);
			if (!scramble[yrndNum][xrndNum])
			{
				c = defaultcards[yrndNum][xrndNum];
				scramble[yrndNum][xrndNum] = true;
				Done = true;
			}
		}
		return c;
	}
	
	public void swap(int x1, int y1, int x2, int y2){
		Card temp = cards[x1][y1];
		cards[x1][y1] = cards[x2][y2];
		cards[x2][y2] = temp;
	}
	
	public void setA(){
		for(int i = 0; i <= 3; i++){
			for(int j = 0; j <= 13; j++){
				if(cards[i][j].getNum() == 14){
					if(cards[i][j].getSuit().equals("c")){
						swap(0, 13, i, j);
					}
					else if(cards[i][j].getSuit().equals("d")){
						swap(1, 13, i, j);
					}
					else if(cards[i][j].getSuit().equals("h")){
						swap(2, 13, i, j);
					}
					else if(cards[i][j].getSuit().equals("s")){
						swap(3, 13, i, j);
					}
				}
			}
		}
	}
	
	public void mouseClicked(MouseEvent e){/* Wow Tom, you clicked the mouse! */}
	public void mousePressed(MouseEvent e){this.selectCard(e.getX(), e.getY());}
	public void mouseReleased(MouseEvent e){/* Let it gooo~~ Let it gooooo~~~ */}
	public void mouseExited(MouseEvent e){/* Nooooo Tom, why?! */}
	public void mouseEntered(MouseEvent e){/* Go Tom, go catch that mouse! */}
    
    public void swapCards(int j, int i){
    	if(cards[lastx][lasty].getNum() == 2 && j == 0 && cards[lastx][lasty].getSuit().equals(cards[i][13].getSuit())){
    		swap(i, j, lastx, lasty);
    		moves++;
    		goff();
    		lastx = lasty = -1;
    	}else if(j == 0){
    		if((cards[i][j+1].getNum() == 15) || (cards[i][j+1].getNum()-cards[lastx][lasty].getNum() == 1 && cards[lastx][lasty].getSuit().equals(cards[i][j+1].getSuit()))){
    	 		swap(i, j, lastx, lasty);
    			moves++;
    			goff();
    			lastx = lasty = -1;
    		}else{
				if(!swapmsg){
        			JOptionPane.showConfirmDialog(new JFrame("Cannot swap!"), "Sorry, but you must place a card in this space that is either 1 less than and has the same suit of the card on the right or 1 more than and has the \nsame suit of the card on the left! (Exception in spaces or cards numbered 2 with same suit of the ace on the far right)", "Cannot place card!", JOptionPane.DEFAULT_OPTION);
        			swapmsg = true;
    			}
    			clicked = true;
    		}
    	}else if(j > 0 && j < 13){
    		if(cards[i][j-1].getNum() == 15 && cards[i][j+1].getNum() == 15){
    	 		swap(i, j, lastx, lasty);
    			moves++;
    			goff();
    			lastx = lasty = -1;
    		}else if((cards[i][j+1].getNum()-cards[lastx][lasty].getNum() == 1 && cards[lastx][lasty].getSuit().equals(cards[i][j+1].getSuit())) || (cards[i][j-1].getNum()-cards[lastx][lasty].getNum() == -1 && cards[lastx][lasty].getSuit().equals(cards[i][j-1].getSuit()))){
    	 		swap(i, j, lastx, lasty);
    			moves++;
    			goff();
    			lastx = lasty = -1;
    		}else{
				if(!swapmsg){
        			JOptionPane.showConfirmDialog(new JFrame("Cannot swap!"), "Sorry, but you must place a card in this space that is either 1 less than and has the same suit of the card on the right or 1 more than and has the \nsame suit of the card on the left! (Exception in spaces or cards numbered 2 with same suit of the ace on the far right)", "Cannot place card!", JOptionPane.DEFAULT_OPTION);
        			swapmsg = true;
    			}
    			clicked = true;
    		}
    	}
    }
    
    public void swapSpace(int j, int i){
    	if(lasty == 0 && cards[i][j].getNum() == 2 && cards[lastx][13].getSuit().equals(cards[i][j].getSuit())){
    		swap(i, j, lastx, lasty);
    		moves++;
    		goff();
    		lastx = lasty = -1;
    	}else if(lasty == 0){
    		if((cards[lastx][lasty+1].getNum() == 15) || (cards[lastx][lasty+1].getNum()-cards[i][j].getNum() == 1 && cards[i][j].getSuit().equals(cards[lastx][lasty+1].getSuit()))){
    	 		swap(i, j, lastx, lasty);
    			moves++;
    			goff();
    			lastx = lasty = -1;
    		}else{
				if(!swapmsg){
        			JOptionPane.showConfirmDialog(new JFrame("Cannot swap!"), "Sorry, but you must place a card in this space that is either 1 less than and has the same suit of the card on the right or 1 more than and has the \nsame suit of the card on the left! (Exception in spaces or cards numbered 2 with same suit of the ace on the far right)", "Cannot place card!", JOptionPane.DEFAULT_OPTION);
        			swapmsg = true;
    			}
    			clicked = true;
    		}
    	}else if(lasty > 0 && lasty < 13){
    		if(cards[lastx][lasty-1].getNum() == 15 && cards[lastx][lasty+1].getNum() == 15){
    	 		swap(i, j, lastx, lasty);
    			moves++;
    			goff();
    			lastx = lasty = -1;
    		}else if((cards[lastx][lasty+1].getNum()-cards[i][j].getNum() == 1 && cards[i][j].getSuit().equals(cards[lastx][lasty+1].getSuit())) || (cards[lastx][lasty-1].getNum()-cards[i][j].getNum() == -1 && cards[i][j].getSuit().equals(cards[lastx][lasty-1].getSuit()))){
    	 		swap(i, j, lastx, lasty);
    			moves++;
    			goff();
    			lastx = lasty = -1;
    		}else{
				if(!swapmsg){
        			JOptionPane.showConfirmDialog(new JFrame("Cannot swap!"), "Sorry, but you must place a card in this space that is either 1 less than and has the same suit of the card on the right or 1 more than and has the \nsame suit of the card on the left! (Exception in spaces or cards numbered 2 with same suit of the ace on the far right)", "Cannot place card!", JOptionPane.DEFAULT_OPTION);
        			swapmsg = true;
    			}
    			clicked = true;
    		}
    	}
    }
    
    public void selectCard(int x, int y){
		for(int i = 0; i <= 3; i++){
        	for(int j = 0; j <= 13; j++){
        		if(x >= j*75+1 && x <= j*75+74 && y >= i*100+1 && y <= i*100+98){
        			if(cards[i][j].getNum() == 14){
        				if(!acemsg){
        					JOptionPane.showConfirmDialog(new JFrame("Cannot click on Aces!"), "Sorry, but you cannot swap Aces!", "Cannot swap Aces!", JOptionPane.DEFAULT_OPTION);
        					acemsg = true;
        				}
        			}else{
        				clicked = !clicked;
        				if(lastx == i && lasty == j){
        					goff();
        					lastx = lasty = -1;
        				}else{
        					if(clicked){
        						cards[i][j].drawRed(goff, j, i);
        						lastx = i;
        						lasty = j;
        					}else{
        						if(cards[lastx][lasty].getNum() != 15 && cards[i][j].getNum() != 15 && cards[lastx][lasty] != cards[i][j]){
        							if(!spacemsg){
										JOptionPane.showConfirmDialog(new JFrame("Must click on yellow cards"), "Sorry, but you can only put cards in spaces!", "Must put in spaces!", JOptionPane.DEFAULT_OPTION);
        								spacemsg = true;
        							}
        							clicked = true;
        						}else if(cards[lastx][lasty].getNum() == 15 && cards[i][j].getNum() == 15 && cards[lastx][lasty].getSuit() != cards[i][j].getSuit()){
        							clicked = true;
        						}else if(cards[lastx][lasty].getNum() == 15){
									swapSpace(j, i);
        						}else{
        							swapCards(j, i);
        						}
        					}
        				}
        			}
        		break;
        		}
        	}
        }
        update(g);
    }
}

class Card{
	private BufferedImage image;
	private int x, y;
	private String suit;
	private int num;
	
	public Card(String s, int n){
		suit = s;
		num = n;
		try{image = ImageIO.read(new File(num+suit+".gif"));}catch(IOException e){/* (Pokemon successfully caught music) Whoa, "Exception" was caught, would you like to name it? */}
	}
	public BufferedImage getImage(){
		return image;
	}
	public int getNum(){
		return num;
	}
	public String getSuit(){
		return suit;
	}
	public void drawRed(Graphics g, int j, int i){
		x = j;
		y = i;
		Graphics2D g2 = (Graphics2D) g;
    	g2.setColor(Color.RED);
       	g2.setStroke(new BasicStroke(2));
        g2.drawRect(x*75, y*100, 75, 100);
	}
}