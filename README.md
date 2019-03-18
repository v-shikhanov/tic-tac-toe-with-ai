# Tic-Tac-Toe game

It's a tic-tac-toe game implemented on java with different field sizes and computer opponent behaviour. 

### Field sizes
* Classic 3*3
* Big 4*4
* Extra big 5*5 
* Crazy 6*6

### Computer opponent modes:
* Easy - Computer makes random move to any empty cell
* Medium - Computer finds combination which could finish game and blocks this, or finishes it. 
* Hard - Computer analyses every available move for it and selects the move which highest probability of win and lowest probability of lose (MiniMax algorithm) - working good for classic field size, in other cases too much moves for analyse.
* Self-learning algorithm - Computer using own experience to win the game.

You also can check the efficiency of every mode and made all players computer, or set all players types "humans" and play game with your friend. 

UI made with Swing application framework
