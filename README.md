# CS-Final-Project-Fall-2015
Risk (Terminal) - Joel Ye and William Xiang
Note for Mr. Brown: to-do and devlog can be found in ProjectManagement folder. A bunch of features were ditched. Like AI. Unfortunately.
To launch the program, make sure you are in the same directory as Risk.java.
<br>
Bugs? Make an issue about it! Thanks for coming by.
Want out of the game? Just press ctrl+c (C+c)! Guaranteed quit mechanism.
<br>
Type the following commands:
<br>$ javac Risk.java
<br>$ java Risk
<br><br>
The program should then run, and you will be guided from there. There will 
be a short rules rundown within the game itself, but for detailed rules, read
the Rules.txt file. The map displays territory borders as '#' and confusing continent borders as '+'. If you're still not too sure about continent borders, check Board.jpg in /ProjectManagement.

Control Overview
- Type country name (Full, w/ capitalization) when prompted to select country. Type integers when prompted
- Type 'help' at (most) any time to get a grasp of the generic commands.
- When in doubt, 'cancel' will save the day.

Risk - A Game of Domination
For 2-6 players
Try to conquer the world with your armies - each turn, one player gains 
reinforcements. From armies on countries, they can attack adjacent countries.
(See Rules.txt for attack mechanism). At the end of every turn, they can
fortify their countries with ONE troop transfer. If any countries were 
conquered that turn, the player will be given a card. (See Rules.txt for card
usage). The game is over once a player wons all 42 territories.  
<br><br>
Project Features
- Zoom into any country (territory) via name! (No continents, though).
- More or less fully functional Risk game. (Er... yeah).
- Can pan the map to get a grasp of the situation.
