# Arena Fighter

## What is "Arena Fighter"?
Arena Fighter is a game developed in Java where players create their own custom character
who will fight battle after battle in order to become champion of the arena. Players will be able
to choose their character's:

- Name
- Race (*Human, dwarf or elf*)
- Class (*Warrior, rouge or merchant*)

As an avid fan of video games and D&D, I chose to create a game that would implement features 
from D&D to create fun gameplay for anyone to enjoy. In between rounds of combat at the arena, players
are able to spend their hard-earned gold in various ways, like:

- Buying new equipment for their inventory
- Training to increase their stats
- Drinking potions to heal their health

Each time the player picks up the game, the experience will always be different due to how character
stats control the flow of combat. Fast characters always attack first, while strong characters will
do the most damage. Players have complete control over their play style, and how they make their fighter.

Do you have what it takes to become **champion of the arena**?

## User Stories
This project was developed in four phases, with each stage having their own user stories
being implemented. The stories implemented in each stage can be seen as follows:

### Phase 1 - Console Based Application
User Story | Completed 
--- | --- 
As a user, I want to be able to add weapons to my inventory | Yes 
As a user, I want items to modify my characters current stats | Yes 
As a user, I want to be able to buy potions to restore my health| Yes 
As a user, I want to be able to create a character | Yes 
As a user, I want to be able to choose a race for my character | Yes 
As a user, I want to be able choose a class for my character | Yes 
As a user, I want to be able to choose whether to fight or shop | Yes 
As a user, I want to be able to shop | Yes 
As a user, I want to be able to fight | Yes 
As a user, I want to be able to fight different enemies | Yes  
As a user, I want to be able to level up after winning a fight | Yes 
As a user, I want to my stats to change when I level up | Yes 
As a user, I want to be able to win the game | Yes 

### Phase 2 - Data Persistence
User Story | Completed 
--- | --- 
As a user, I want to be able to save my progress | Yes
As a user, I want to be able to load a saved game | Yes 
As a user, I want to be able to start a new game | Yes 
As a user, I want to be able to quit the game | Yes 
As a user, I want to be reminded to save the game before I quit | Yes 
As a user, I want to be greeted with a Main Menu on start | Yes 

### Phase 3 - Java Swing GUI
User Story | Completed 
--- | --- 
As a user, I want to be able to play the game with a GUI | Yes
As a user, I want to be greeted with a main menu when starting the game | Yes 
As a user, I want to see an image on the main menu | Yes 
As a user, I want to be able to start a new game with a button | Yes 
As a user, I want to be able to quit the game with a button | Yes 
As a user, I want to be reminded to save the game before I quit | Yes 
As a user, I want to be able to load a previous game with a button | Yes 
As a user, I want to be able to see the stats of my character on screen | Yes 
As a user, I want to be able to see the equipment added to my inventory on screen | Yes
As a user, I want to be able to see the detailed stats of my character modified by my inventory on screen | Yes
As a user, I want the game to close when I lose | Yes
As a user, I want the game to close when I win | Yes

### Phase 4 - Design
This phase did not include adding additional features, but instead on modifying the design and 
structure of the code. It also involved reflecting on the design choices made during the project
and seeing where any improvements could be made.
#### Phase 4: Task 2
For this phase, I chose to make the Inventory class more robust. To do this, I created a new Exception
called InvalidEquipmentException, and implemented it in two methods in the Inventory class. These methods
are the getEquipment() and removeEquipment() methods.
#### Phase 4: Task 3
It is clear to see that there is a lot of improvement that could be done on my project design if I had more time.
The Game class is not very cohesive, and has many composition relationships with the Handler classes which is not 
an ideal design pattern. I would have liked to refactor Game so that some of its responsibilities are handles by 
separate classes. I also wanted to implement an enumeration to handle some game logic that Game and ConsoleGame 
currently handles, like enemies and healing. Additionally, I think it would also be a good idea to create a Shop 
class which could handle the store rather than making Game and ConsoleGame manage it themselves, 
and the class could have extended Inventory. 

There are clearly many things that I would refactor if I had more time on this project, but I will carry these
ideas into any projects I make in the future. 


 

