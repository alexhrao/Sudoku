# Sudoku Online
***An online multiplayer Sudoku game***

## Features
 - Has all the classic Sudoku tools - Notes, answers, hints, etc.
 - Play online with as many players as you wish
 - Drop-In/Drop-Out play style means players can join and leave when they wish
 - Let the app generate the board _or_ generate your own and upload it!
 - Chat as you play, and even after the game is over
 - Works well on any screen size

## Installation

### macOS
Simply download the _SudokuOnline.dmg_ from the _installers/DMG_ folder, and install!

### Windows
Simply download the _InstallSudokuOnline.exe_ from the _installers/EXE/Output_ folder, and install!

### All Other OS
You'll need to have the latest version of The Java&trade; Runtime Environment (JRE&trade;). As of this writing, this version is 1.8, revision 131. You can download it [here][jre].
Then, simply download the jar file from _out/artifacts/client_ folder, and run it as normally.

## Development
Sudoku is being actively developed. If you would like to help develop, simply send a pull request with your changes! The repository is located [here][rep] Please note the branch structure:
- **Development**
    - This branch is for active development. The installers may or may not be recent, and same goes for the jar files. This branch is for _active development_, and no guaruntees are made for it's stability.
- **Testing**
    - This branch is for Quality Assurance. The installers ***must*** be recent, and the jars are also current. It is not production, so it might not be stable, but that's the goal here.
- **Production**
    - This branch is for final products. The installers and jar files are ***guarunteed*** to be up to date, and the code has been tested. This branch is also, incidentally, protected - only the owner can push to this branch.

All pull requests should go to *Development*, while any bug reports should be filed under *Testing*. From there, an issue will be made, and someone assigned.

As stated above, this project is written in Java, using the IntelliJ&trade; IDE from JetBrains&trade;. This IDE is free, and the community edition can be bought [here][ide]. Note that this repository has the IntelliJ&trade; metadata tied to it.

You will need to have the most recent Java&trade; Developers Kit (JDK&trade;); as of this writing, this project uses JDK&trade; version 1.8, revision 131. It can be downloaded from [here][jdk].

To clone the repository, simply type the following in a terminal of your choice:
```
git clone https://github.com/alexhrao/Sudoku.git
```

## Acknowledgements
This project is a labor of love, but I could not have done it alone. The following users have been instrumental in the creation and testing of Sudoku Online:
- [Uday Rao][ukr]
- [Raeedah Choudhury][rac]
- Marcy Rao
- Emily Rao

I invite you to follow me on [GitHub][ahr] and look at my other projects!

Alexander Rao

   [jre]: <http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html>
   [jdk]: <http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>
   [ide]: <https://www.jetbrains.com/idea/download/>
   [ukr]: <https://www.github.com/udaykrao>
   [rac]: <https://www.github.com/Raeedah>
   [ahr]: <https://www.github.com/alexhrao>
   [rep]: <https://www.github.com/alexhrao/Sudoku>
