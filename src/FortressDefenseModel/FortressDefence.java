package FortressDefenseModel;

import GameLogic.Logic;

import java.util.Scanner;

/**
 * Main class for running FortressDefense
 */
public class FortressDefence {

    public static final int DEFAULT_TANK_NUM = 5;

    public static void main(String[] args)
    {
        //start of game
        Scanner scanner = new Scanner(System.in);
        int tankNum = DEFAULT_TANK_NUM;
        if(scanner.hasNextInt())
        {
            tankNum = scanner.nextInt();
        }
        String cheat = scanner.nextLine();
        boolean isCheat = false;
        if(cheat.trim().toLowerCase().equals("--cheat"))
            isCheat = true;

        UserInterface ui = new UserInterface();

        Logic logic = new Logic(tankNum, 5);
        //check if board is initialized properly
        if(logic.isBoardEmpty())
            return;

        //introduction
        ui.printIntro(tankNum, isCheat, logic.getTankBoardState(), logic.getFortressHealth());
        ui.printGameState(logic.getBoardState(), logic.getFortressHealth());

        //repeat turns till game is over
        while(!logic.isGameOver())
        {
            ui.printMovePrompt();
//            String userInput = ui.getUserInput();


            int result = logic.shootCoordinate(ui.getUserInput());
            //input error
            while(result == -1)
            {
//                System.out.println("Invalid target. Please enter a coordinate such as D10.");
                ui.printMoveResult(result, logic.getTanksDamageForUI());
                ui.printMovePrompt();
                result = logic.shootCoordinate(ui.getUserInput());
            }
            ui.printMoveResult(result, logic.getTanksDamageForUI());
            logic.shootFortress();
            ui.printGameState(logic.getBoardState(), logic.getFortressHealth());

        }

        //Game Over
        ui.printEndGameResult(logic.isVictory());
        ui.printRevealed(logic.getTankBoardState(),logic.getFortressHealth());

    }
}
