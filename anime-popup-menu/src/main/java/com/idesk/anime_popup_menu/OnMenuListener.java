package com.idesk.anime_popup_menu;


import com.idesk.anime_popup_menu.PopUpButtons.MenuButton;

public interface OnMenuListener {

    /**
     * When one of the PopUp-button is clicked.
     *
     * @param index index of the clicked PopUp-button
     * @param menuButton the clicked PopUp-button
     */
    void onClicked(int index, MenuButton menuButton);

    /**
     * When the background of PopUp-buttons is clicked.
     */
    void onBackgroundClick();

    /**
     * When the BMB is going to hide its PopUp-buttons.
     */
    void onPopUpWillHide();

    /**
     * When the BMB finishes hide animations.
     */
    void onPopUpDidHide();

    /**
     * When the BMB is going to show its PopUp-buttons.
     */
    void onPopUpWillShow();

    /**
     * When the BMB finished PopUp animations.
     */
    void onPopUpDidShow();

}
