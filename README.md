Connect3 is android java multiplayer game that integrates Firebase Authentication and Realtime Database to deliver both local and online gameplay experience. The game is built around a 2D grid where players drop tokens into columns, aiming to match three in a row—horizontally, vertically, or diagonally—to win.

Technologies Used
    Java (Android SDK)
    Firebase Authentication
    Firebase Realtime Database

Authentication
    Login Screen (MainActivity):
        Users sign in using Firebase signInWithEmailAndPassword.
        Option to create a new account via the registration screen.

Registration Screen (RegistrationActivity):
        Users register with email and password using Firebase createUserWithEmailAndPassword.
        Confirmation message displayed upon successful account creation.


Main Menu (MainMenuActivity)
    After login, users are directed to the main menu with four options:
    	Play with Computer
    	Play with Partner (Same Device)
    	Play Online
    	Log Out


Play with Computer (ComputerActivity)
    User competes against a basic AI.
    Restart button resets the grid.
    Quit button returns to the main menu.
    Dialog displayed when the game ends in a win or draw.


Play with Partner (PartnerActivity)
    Two users take turns on the same device.
    Includes Restart and Quit buttons.
    Dialog shown when either player wins.


Log Out
    Signs out the user via Firebase and returns to the login screen.


Play Online (LobbyActivity & OnlineGameplayActivity)

LobbyActivity
    	Connects players via Firebase Realtime Database.
    	If no active game exists, a new session is created.
    	If a session is found, the user joins and the game status updates to "match ready".
    	Polling mechanism checks for player availability every 3 seconds.
    	Once both players are connected, the Start button launches the game.

OnlineGameplayActivity
    	Real-time multiplayer gameplay using Firebase listeners.

Player roles:
        First user is Player 1
        Second user is Player 2

Game logic:
        	Each move updates the Firebase database (player1Move, player2Move, turn, winner).
        	Listener checks whose turn it is and updates the grid accordingly.
        	Grid is a 2D array with visual updates using redstar (Player 1) and bluestar (Player 2).

Game outcomes:
        Win, draw, or opponent quit triggers appropriate dialogs.

Exit options:
        New Game and Quit Game detach listeners and return to the main menu.
