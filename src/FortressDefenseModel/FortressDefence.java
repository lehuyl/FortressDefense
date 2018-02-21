package FortressDefenseModel;

import GameLogic.Logic;

import java.util.Scanner;

public class FortressDefence {
    public static void main(String[] args)
    {
        //start of game
        Scanner scanner = new Scanner(System.in);
        int tankNum = scanner.nextInt();
        String cheat = scanner.nextLine();
        boolean isCheat = false;
        if(cheat.equals(" --cheat"))
            isCheat = true;

        UserInterface ui = new UserInterface();

        Logic logic = new Logic(tankNum, 5);
        ui.printIntro(tankNum, isCheat, logic.getTankBoardState(), logic.getFortressHealth());
        ui.printGameState(logic.getBoardState(), logic.getFortressHealth());

        //repeat turns till game is over
        while(!logic.isGameOver())
        {
            ui.printMovePrompt();
            String userInput = ui.getUserInput();

            //input error
            while(logic.shootCoordinate(userInput) == -1)
            {
                System.out.println("Invalid target. Please enter a coordinate such as D10.");
                userInput = ui.getUserInput();
            }
            ui.printMoveResult(logic.shootCoordinate(userInput), logic.getTanksDamageForUI());
            logic.shootFortress();
            ui.printGameState(logic.getBoardState(), logic.getFortressHealth());

        }

        //Game Over
        ui.printEndGameResult(logic.isVictory());
        ui.printRevealed(logic.getTankBoardState(),logic.getFortressHealth());

    }
}
