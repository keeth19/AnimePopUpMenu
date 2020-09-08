package com.idesk.anime_popup_menu;

import com.idesk.anime_popup_menu.Animation.MenuEnum;
import com.idesk.anime_popup_menu.Piece.PiecePlaceEnum;
import com.idesk.anime_popup_menu.PopUpButtons.ButtonPlaceEnum;
import com.idesk.anime_popup_menu.PopUpButtons.MenuButtonBuilder;

import java.util.ArrayList;

class ExceptionManager {

    static void judge(PoupupMenuButton bmb, ArrayList<MenuButtonBuilder> builders) {
        if (bmb.getButtonEnum() == null || bmb.getButtonEnum().equals(ButtonEnum.Unknown))
            throw new RuntimeException("Unknown button-enum!");
        if (bmb.getPiecePlaceEnum() == null || bmb.getPiecePlaceEnum().equals(PiecePlaceEnum.Unknown))
            throw new RuntimeException("Unknown piece-place-enum!");
        if (bmb.getButtonPlaceEnum() == null || bmb.getButtonPlaceEnum().equals(ButtonPlaceEnum.Unknown))
            throw new RuntimeException("Unknown button-place-enum!");
        if (bmb.getMenuEnum() == null || bmb.getMenuEnum().equals(MenuEnum.Unknown))
            throw new RuntimeException("Unknown popUp-enum!");
        if (builders == null || builders.size() == 0)
            throw new RuntimeException("Empty builders!");

        int pieceNumber = bmb.getPiecePlaceEnum().pieceNumber();
        int minPieceNumber = bmb.getPiecePlaceEnum().minPieceNumber();
        int maxPieceNumber = bmb.getPiecePlaceEnum().maxPieceNumber();
        int customPiecePositionsNumber = bmb.getCustomPiecePlacePositions().size();

        int buttonNumber = bmb.getButtonPlaceEnum().buttonNumber();
        int minButtonNumber = bmb.getButtonPlaceEnum().minButtonNumber();
        int maxButtonNumber = bmb.getButtonPlaceEnum().maxButtonNumber();
        int customButtonPositionsNumber = bmb.getCustomButtonPlacePositions().size();

        int builderNumber = builders.size();

        if (pieceNumber == -1) {
            // The piece number is in a range
            if (buttonNumber != -1 && !(minPieceNumber <= buttonNumber && buttonNumber <= maxPieceNumber)) {
                // The button-place-enum has a certain number of buttons, then it must be in the range
                throw new RuntimeException("The number(" + buttonNumber + ") of buttons of " +
                        "button-place-enum(" + bmb.getButtonPlaceEnum() + ") is not in the " +
                        "range([" + minPieceNumber + ", " + maxPieceNumber + "]) of the " +
                        "piece-place-enum(" + bmb.getPiecePlaceEnum() + ")");
            }
            if (!(minPieceNumber <= builderNumber && builderNumber <= maxPieceNumber)) {
                // The number of builders must be in the range
                throw new RuntimeException("The number of builders(" + builderNumber + ") is not " +
                        "in the range([" + minPieceNumber + ", " + maxPieceNumber + "]) of the " +
                        "piece-place-enum(" + bmb.getPiecePlaceEnum() + ")");
            }
        } else {
            if (buttonNumber != -1) {
                // The piece-place-enum and button-place-enum both have a certain number of pieces and buttons. They must be the same
                if (pieceNumber != buttonNumber) {
                    throw new RuntimeException("The number of piece(" + pieceNumber + ") is not " +
                            "equal to buttons'(" + buttonNumber + ")");
                }
                if (pieceNumber != builderNumber) {
                    throw new RuntimeException("The number of piece(" + pieceNumber + ") is not " +
                            "equal to builders'(" + builderNumber + ")");
                }
            }
        }

        if (bmb.getPiecePlaceEnum().equals(PiecePlaceEnum.Custom)) {
            if (customPiecePositionsNumber <= 0) {
                throw new RuntimeException("When the positions of pieces are customized, the " +
                        "custom-piece-place-positions array is empty");
            }
            if (buttonNumber == -1) {
                // The button number is in a range
                if (!(minButtonNumber <= customPiecePositionsNumber && customPiecePositionsNumber <= maxButtonNumber)) {
                    throw new RuntimeException("When the positions of pieces is customized, the " +
                            "length(" + customPiecePositionsNumber + ") of " +
                            "custom-piece-place-positions array is not in the range([" +
                            minButtonNumber + ", " + maxButtonNumber + "])");
                }
            } else {
                if (customPiecePositionsNumber != buttonNumber) {
                    throw new RuntimeException("The number of piece(" + customPiecePositionsNumber +
                            ") is not equal to buttons'(" + buttonNumber + ")");
                }
            }
            if (customPiecePositionsNumber != builderNumber) {
                throw new RuntimeException("The number of piece(" + customPiecePositionsNumber +
                        ") is not equal to builders'(" + builderNumber + ")");
            }
        }

        if (bmb.getButtonPlaceEnum().equals(ButtonPlaceEnum.Custom)) {
            if (customButtonPositionsNumber <= 0) {
                throw new RuntimeException("When the positions of buttons are customized, the " +
                        "custom-button-place-positions array is empty");
            }
            if (pieceNumber == -1) {
                // The piece number is in a range
                if (!(minPieceNumber <= customButtonPositionsNumber && customButtonPositionsNumber <= maxPieceNumber)) {
                    throw new RuntimeException("When the positions of buttons is customized, the " +
                            "length(" + customButtonPositionsNumber + ") of " +
                            "custom-button-place-positions array is not in the range([" +
                            minPieceNumber + ", " + maxPieceNumber + "])");
                }
            } else {
                if (customButtonPositionsNumber != pieceNumber) {
                    throw new RuntimeException("The number of button(" + customButtonPositionsNumber
                            + ") is not equal to pieces'(" + pieceNumber + ")");
                }
            }
            if (customButtonPositionsNumber != builderNumber) {
                throw new RuntimeException("The number of button(" + customButtonPositionsNumber +
                        ") is not equal to builders'(" + builderNumber + ")");
            }
        }
    }
}
