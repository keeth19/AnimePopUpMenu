package com.idesk.anime_popup_menu;


import com.idesk.anime_popup_menu.PopUpButtons.MenuButton;

public class OnMenuListenerAdapter implements OnMenuListener {

    /**
     * When one of the popUp-button is clicked.
     *
     * @param index index of the clicked popUp-button
     * @param menuButton the clicked popUp-button
     */
    @Override
    public void onClicked(int index, MenuButton menuButton) {

    }

    /**
     * When the background of popUp-buttons is clicked.
     */
    @Override
    public void onBackgroundClick() {

    }

    /**
     * When the BMB is going to hide its popUp-buttons.
     */
    @Override
    public void onPopUpWillHide() {

    }

    /**
     * When the BMB finishes hide animations.
     */
    @Override
    public void onPopUpDidHide() {

    }

    /**
     * When the BMB is going to show its popUp-buttons.
     */
    @Override
    public void onPopUpWillShow() {

    }

    /**
     * When the BMB finished popUp animations.
     */
    @Override
    public void onPopUpDidShow() {

    }

}
