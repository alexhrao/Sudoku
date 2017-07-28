all:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/bin/javapackager -deploy -title SudokuOnline -name SudokuOnline -appclass main.java.logic.Sudoku -native dmg -outdir deploy -outfile SudokuOnline -srcfiles ../EXE/SudokuPlayer.jar
	cp ./deploy/bundles/SudokuOnline-1.0.dmg .
	rm -R deploy