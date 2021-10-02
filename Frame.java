/*
*Author: Tan,Rachel
*Personal Project: Snake Game
*Brief purpose: The objective is to eat as many apples as possible. 
*Each time the snake eats an apple its body grows.
*There will be a time limit of 5 minutes per game.
*The snake must avoid the walls and its own body in order to continue the game.
*/

import javax.swing.JFrame;

public class Frame extends JFrame {

	Frame() {

		this.add(new Board());
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
