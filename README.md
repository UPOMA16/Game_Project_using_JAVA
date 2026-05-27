# Flappy Bird (Java + JavaFX)

## Description  
A simple Flappy Bird Game built using Java and JavaFX. “Tap, survive but don’t hit the pipes.”

The bird continuously falls due to gravity and the player must press a key to make it flap upward. Pipes move from right to left with random gaps. The score increases as the bird successfully passes through pipes. Colliding with a pipe or the ground ends the game. Press R to restart.


## Controls  
- Space / Up Arrow → Flap (jump upward)  
- R → Restart game (after game over)

## Requirements  
- Java JDK 8+ (recommended JDK 11 or higher)  
- JavaFX SDK (if not bundled with JDK)  
- IntelliJ IDEA / Eclipse / VS Code  
- Windows / Linux / macOS  

## How to Run  
1. Install JDK and JavaFX SDK  
2. Set JavaFX library path in your IDE  
3. Add VM options (if required):  
   --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml  
4. Compile and run App.java  

## Project Structure  
FlappyBirdGame  
- src/  
  - App.java  
- resources/  
  - bird3.png  
  - bg.jpg  
- README.md  

## Features  
- Gravity-based movement  
- Random pipe generation  
- Collision detection  
- Score system  
- Game over + restart system  
- Smooth animation using AnimationTimer  

## Notes  
- Make sure all image assets are in the correct folder path or the game will not run properly.  
- If images don’t load, check file paths first.

## Author  
- Fatiha Tasnim Upoma(2023831012)
- Ariba Sharaf Chowdhury(2023831051)
-
