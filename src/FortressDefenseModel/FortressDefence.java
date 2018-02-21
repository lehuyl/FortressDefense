package FortressDefenseModel;

import GameLogic.Logic;

/**
 * Main class for running FortressDefense
 */
public class FortressDefence {

    private static final int DEFAULT_TANK_NUM = 5;

    public static void main(String[] args)
    {
        //start of game
        int tankNum = DEFAULT_TANK_NUM;

        boolean isCheat = false;
        if(args.length == 1){
            tankNum = Integer.parseInt(args[0]);
        }
        if(args.length == 2 && args[1].trim().toLowerCase().equals("--cheat"))
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


            int result = logic.shootCoordinate(ui.getUserInput());
            //input error
            while(result == -1)
            {
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
