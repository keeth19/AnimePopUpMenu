package com.idesk.anime_popup_menu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.idesk.anime_popup_menu.Animation.AnimationManager;
import com.idesk.anime_popup_menu.Animation.Ease;
import com.idesk.anime_popup_menu.Animation.EaseEnum;
import com.idesk.anime_popup_menu.Animation.HideRgbEvaluator;
import com.idesk.anime_popup_menu.Animation.MenuEnum;
import com.idesk.anime_popup_menu.Animation.OrderEnum;
import com.idesk.anime_popup_menu.Animation.Rotate3DAnimation;
import com.idesk.anime_popup_menu.Animation.ShareLinesView;
import com.idesk.anime_popup_menu.Animation.ShowRgbEvaluator;
import com.idesk.anime_popup_menu.Piece.MenuPiece;
import com.idesk.anime_popup_menu.Piece.PiecePlaceEnum;
import com.idesk.anime_popup_menu.Piece.PiecePlaceManager;
import com.idesk.anime_popup_menu.PopUpButtons.ButtonPlaceAlignmentEnum;
import com.idesk.anime_popup_menu.PopUpButtons.ButtonPlaceEnum;
import com.idesk.anime_popup_menu.PopUpButtons.ButtonPlaceManager;
import com.idesk.anime_popup_menu.PopUpButtons.HamButton;
import com.idesk.anime_popup_menu.PopUpButtons.InnerOnMenuButtonClickListener;
import com.idesk.anime_popup_menu.PopUpButtons.MenuButton;
import com.idesk.anime_popup_menu.PopUpButtons.MenuButtonBuilder;
import com.idesk.anime_popup_menu.PopUpButtons.SimpleCircleButton;
import com.idesk.anime_popup_menu.PopUpButtons.TextInsideCircleButton;
import com.idesk.anime_popup_menu.PopUpButtons.TextOutsideCircleButton;

import java.util.ArrayList;

import static com.idesk.anime_popup_menu.Piece.PiecePlaceEnum.Share;

@SuppressWarnings("unused")
// Todo Landscape
public class PoupupMenuButton extends FrameLayout implements InnerOnMenuButtonClickListener {

    protected static final String TAG = "PopUpMenuButton";

    // Basic
    private Context context;
    private boolean needToLayout = true;
    private boolean cacheOptimization;
    private boolean popUpInWholeScreen;
    private boolean inList;
    private boolean inFragment;
    private boolean isBackPressListened = true;
    private Runnable layoutJobsRunnable;

    // Shadow
    private boolean shadowEffect;
    private int shadowOffsetX;
    private int shadowOffsetY;
    private int shadowRadius;
    private int shadowColor;
    private BMBShadow shadow;

    // Button
    private int buttonRadius;
    private ButtonEnum buttonEnum = ButtonEnum.Unknown;
    private boolean backgroundEffect;
    private boolean rippleEffect;
    private int normalColor;
    private int highlightedColor;
    private int unableColor;
    private boolean draggable;
    private float startPositionX;
    private float startPositionY;
    private boolean ableToStartDragging = false;
    private boolean isDragging = false;
    private float lastMotionX = -1;
    private float lastMotionY = -1;
    private Rect edgeInsetsInParentView;
    private FrameLayout button;

    // Piece
    private ArrayList<MenuPiece> pieces;
    private ArrayList<RectF> piecePositions;
    private float dotRadius;
    private float hamWidth;
    private float hamHeight;
    private float pieceCornerRadius = -1;
    private float pieceHorizontalMargin;
    private float pieceVerticalMargin;
    private float pieceInclinedMargin;
    private float shareLineLength;
    private int shareLine1Color;
    private int shareLine2Color;
    private float shareLineWidth;
    private ShareLinesView shareLinesView;
    private PiecePlaceEnum piecePlaceEnum = PiecePlaceEnum.Unknown;
    private ArrayList<PointF> customPiecePlacePositions = new ArrayList<>();

    // Animation
    private int animatingViewNumber = 0;
    private OnMenuListener onMenuListener;
    private int dimColor;
    private long showDuration;
    private long showDelay;
    private long hideDuration;
    private long hideDelay;
    private boolean cancelable;
    private boolean autoHide;
    private OrderEnum orderEnum;
    private int frames;
    private MenuEnum menuEnum;
    private EaseEnum showMoveEaseEnum;
    private EaseEnum showScaleEaseEnum;
    private EaseEnum showRotateEaseEnum;
    private EaseEnum hideMoveEaseEnum;
    private EaseEnum hideScaleEaseEnum;
    private EaseEnum hideRotateEaseEnum;
    private int rotateDegree;
    private boolean use3DTransformAnimation;
    private boolean autoPopUp;
    private boolean autoPopUpImmediately;
    private MenuStateEnum popUpStateEnum = MenuStateEnum.DidRePopUp;

    // Background
    private BackgroundView background;

    // PopUp Buttons
    private ArrayList<MenuButton> menuButtons = new ArrayList<>();
    private ArrayList<MenuButtonBuilder> menuButtonBuilders = new ArrayList<>();
    private float simpleCircleButtonRadius;
    private float textInsideCircleButtonRadius;
    private float textOutsideCircleButtonWidth;
    private float textOutsideCircleButtonHeight;
    private float hamButtonWidth;
    private float hamButtonHeight;
    private ButtonPlaceEnum buttonPlaceEnum = ButtonPlaceEnum.Unknown;
    private ArrayList<PointF> customButtonPlacePositions = new ArrayList<>();
    private ButtonPlaceAlignmentEnum buttonPlaceAlignmentEnum;
    private float buttonHorizontalMargin;
    private float buttonVerticalMargin;
    private float buttonInclinedMargin;
    private float buttonTopMargin;
    private float buttonBottomMargin;
    private float buttonLeftMargin;
    private float buttonRightMargin;
    private ArrayList<PointF> startPositions;
    private ArrayList<PointF> endPositions;
    private float bottomHamButtonTopMargin;
    private boolean needToCreateButtons = true;
    private boolean needToCalculateStartPositions = true;
    private int lastRePopUpIndex = -1;

    // Orientation
    private boolean isOrientationAdaptable;
    private int lastOrientation = OrientationEventListener.ORIENTATION_UNKNOWN;
    private boolean isOrientationChanged = false;
    private OrientationEventListener orientationEventListener;

    private void ____________________________Initialization() {}
    //region Constructor and Initializer

    public PoupupMenuButton(Context context) {
        super(context);
        init(context, null);
    }

    public PoupupMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PoupupMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.bmb, this, true);
        initAttrs(context, attrs);
        initShadow();
        initButton();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.PoupupMenuButton, 0, 0);
        if (typedArray == null) return;
        try {
            // Basic
            cacheOptimization = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_cacheOptimization, R.bool.default_bmb_cacheOptimization);
            popUpInWholeScreen = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_PopUpInWholeScreen, R.bool.default_bmb_PopUpInWholeScreen);
            inList = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_inList, R.bool.default_bmb_inList);
            inFragment = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_inFragment, R.bool.default_bmb_inFragment);
            isBackPressListened = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_backPressListened, R.bool.default_bmb_backPressListened);
            isOrientationAdaptable = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_orientationAdaptable, R.bool.default_bmb_orientationAdaptable);

            // Shadow
            shadowEffect = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_shadowEffect, R.bool.default_bmb_shadow_effect);
            shadowRadius = Util.getDimenSize(typedArray, R.styleable.PoupupMenuButton_bmb_shadowRadius, R.dimen.default_bmb_shadow_radius);
            shadowOffsetX = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_shadowOffsetX, R.dimen.default_bmb_shadow_offset_x);
            shadowOffsetY = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_shadowOffsetY, R.dimen.default_bmb_shadow_offset_y);
            shadowColor = Util.getColor(typedArray, R.styleable.PoupupMenuButton_bmb_shadowColor, R.color.default_bmb_shadow_color);

            // Button
            buttonRadius = Util.getDimenSize(typedArray, R.styleable.PoupupMenuButton_bmb_buttonRadius, R.dimen.default_bmb_button_radius);
            buttonEnum = ButtonEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_buttonEnum, R.integer.default_bmb_button_enum));
            backgroundEffect = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_backgroundEffect, R.bool.default_bmb_background_effect);
            rippleEffect = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_rippleEffect, R.bool.default_bmb_ripple_effect);
            normalColor = Util.getColor(typedArray, R.styleable.PoupupMenuButton_bmb_normalColor, R.color.default_bmb_normal_color);
            highlightedColor = Util.getColor(typedArray, R.styleable.PoupupMenuButton_bmb_highlightedColor, R.color.default_bmb_highlighted_color);
            if (highlightedColor == Color.TRANSPARENT) highlightedColor = Util.getDarkerColor(normalColor);
            unableColor = Util.getColor(typedArray, R.styleable.PoupupMenuButton_bmb_unableColor, R.color.default_bmb_unable_color);
            if (unableColor == Color.TRANSPARENT) unableColor = Util.getLighterColor(normalColor);
            draggable = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_draggable, R.bool.default_bmb_draggable);
            edgeInsetsInParentView = new Rect(0, 0, 0, 0);
            edgeInsetsInParentView.left = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_edgeInsetsLeft, R.dimen.default_bmb_edgeInsetsLeft);
            edgeInsetsInParentView.top = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_edgeInsetsTop, R.dimen.default_bmb_edgeInsetsTop);
            edgeInsetsInParentView.right = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_edgeInsetsRight, R.dimen.default_bmb_edgeInsetsRight);
            edgeInsetsInParentView.bottom = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_edgeInsetsBottom, R.dimen.default_bmb_edgeInsetsBottom);

            // Piece
            dotRadius = Util.getDimenSize(typedArray, R.styleable.PoupupMenuButton_bmb_dotRadius, R.dimen.default_bmb_dotRadius);
            hamWidth = Util.getDimenSize(typedArray, R.styleable.PoupupMenuButton_bmb_hamWidth, R.dimen.default_bmb_hamWidth);
            hamHeight = Util.getDimenSize(typedArray, R.styleable.PoupupMenuButton_bmb_hamHeight, R.dimen.default_bmb_hamHeight);
            pieceCornerRadius = Util.getDimenSize(typedArray, R.styleable.PoupupMenuButton_bmb_pieceCornerRadius, R.dimen.default_bmb_pieceCornerRadius);
            pieceHorizontalMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_pieceHorizontalMargin, R.dimen.default_bmb_pieceHorizontalMargin);
            pieceVerticalMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_pieceVerticalMargin, R.dimen.default_bmb_pieceVerticalMargin);
            pieceInclinedMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_pieceInclinedMargin, R.dimen.default_bmb_pieceInclinedMargin);
            shareLineLength = Util.getDimenSize(typedArray, R.styleable.PoupupMenuButton_bmb_sharedLineLength, R.dimen.default_bmb_sharedLineLength);
            shareLine1Color = Util.getColor(typedArray, R.styleable.PoupupMenuButton_bmb_shareLine1Color, R.color.default_bmb_shareLine1Color);
            shareLine2Color = Util.getColor(typedArray, R.styleable.PoupupMenuButton_bmb_shareLine2Color, R.color.default_bmb_shareLine2Color);
            shareLineWidth = Util.getDimenSize(typedArray, R.styleable.PoupupMenuButton_bmb_shareLineWidth, R.dimen.default_bmb_shareLineWidth);
            piecePlaceEnum = PiecePlaceEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_piecePlaceEnum, R.integer.default_bmb_pieceEnum));

            // Animation
            dimColor = Util.getColor(typedArray, R.styleable.PoupupMenuButton_bmb_dimColor, R.color.default_bmb_dimColor);
            showDuration = Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_showDuration, R.integer.default_bmb_showDuration);
            showDelay = Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_showDelay, R.integer.default_bmb_showDelay);
            hideDuration = Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_hideDuration, R.integer.default_bmb_hideDuration);
            hideDelay = Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_hideDelay, R.integer.default_bmb_hideDelay);
            cancelable = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_cancelable, R.bool.default_bmb_cancelable);
            autoHide = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_autoHide, R.bool.default_bmb_autoHide);
            orderEnum = OrderEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_orderEnum, R.integer.default_bmb_orderEnum));
            frames = Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_frames, R.integer.default_bmb_frames);
            menuEnum = MenuEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_PopUpEnum, R.integer.default_bmb_PopUpEnum));
            showMoveEaseEnum = EaseEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_showMoveEaseEnum, R.integer.default_bmb_showMoveEaseEnum));
            showScaleEaseEnum = EaseEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_showScaleEaseEnum, R.integer.default_bmb_showScaleEaseEnum));
            showRotateEaseEnum = EaseEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_showRotateEaseEnum, R.integer.default_bmb_showRotateEaseEnum));
            hideMoveEaseEnum = EaseEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_hideMoveEaseEnum, R.integer.default_bmb_hideMoveEaseEnum));
            hideScaleEaseEnum = EaseEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_hideScaleEaseEnum, R.integer.default_bmb_hideScaleEaseEnum));
            hideRotateEaseEnum = EaseEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_hideRotateEaseEnum, R.integer.default_bmb_hideRotateEaseEnum));
            rotateDegree = Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_rotateDegree, R.integer.default_bmb_rotateDegree);
            use3DTransformAnimation = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_use3DTransformAnimation, R.bool.default_bmb_use3DTransformAnimation);
            autoPopUp = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_autoPopUp, R.bool.default_bmb_autoPopUp);
            autoPopUpImmediately = Util.getBoolean(typedArray, R.styleable.PoupupMenuButton_bmb_autoPopUpImmediately, R.bool.default_bmb_autoPopUpImmediately);

            // PopUp buttons
            buttonPlaceEnum = ButtonPlaceEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_buttonPlaceEnum, R.integer.default_bmb_buttonPlaceEnum));
            buttonPlaceAlignmentEnum = ButtonPlaceAlignmentEnum.getEnum(Util.getInt(typedArray, R.styleable.PoupupMenuButton_bmb_buttonPlaceAlignmentEnum, R.integer.default_bmb_buttonPlaceAlignmentEnum));
            buttonHorizontalMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_buttonHorizontalMargin, R.dimen.default_bmb_buttonHorizontalMargin);
            buttonVerticalMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_buttonVerticalMargin, R.dimen.default_bmb_buttonVerticalMargin);
            buttonInclinedMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_buttonInclinedMargin, R.dimen.default_bmb_buttonInclinedMargin);
            buttonTopMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_buttonTopMargin, R.dimen.default_bmb_buttonTopMargin);
            buttonBottomMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_buttonBottomMargin, R.dimen.default_bmb_buttonBottomMargin);
            buttonLeftMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_buttonLeftMargin, R.dimen.default_bmb_buttonLeftMargin);
            buttonRightMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_buttonRightMargin, R.dimen.default_bmb_buttonRightMargin);
            bottomHamButtonTopMargin = Util.getDimenOffset(typedArray, R.styleable.PoupupMenuButton_bmb_bottomHamButtonTopMargin, R.dimen.default_bmb_bottomHamButtonTopMargin);
        } finally {
            typedArray.recycle();
        }
    }

    private void initShadow() {
        if (shadow == null) shadow = (BMBShadow) findViewById(R.id.shadow);
        boolean hasShadow = shadowEffect && backgroundEffect && !inList;
        shadow.setShadowEffect(hasShadow);
        if (hasShadow) {
            shadow.setShadowOffsetX(shadowOffsetX);
            shadow.setShadowOffsetY(shadowOffsetY);
            shadow.setShadowColor(shadowColor);
            shadow.setShadowRadius(shadowRadius);
            shadow.setShadowCornerRadius(shadowRadius + buttonRadius);
        } else {
            shadow.clearShadow();
        }
    }

    private void initButton() {
        if (button == null) button = (FrameLayout) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUp();
            }
        });
        initDraggableTouchListener();

        setButtonSize();
        setButtonBackground();
    }

    private void setButtonSize() {
        LayoutParams params = (LayoutParams) button.getLayoutParams();
        params.width = buttonRadius * 2;
        params.height = buttonRadius * 2;
        button.setLayoutParams(params);
    }

    private void setButtonBackground() {
        if (backgroundEffect && !inList) {
            if (rippleEffect && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RippleDrawable rippleDrawable = new RippleDrawable(
                        ColorStateList.valueOf(highlightedColor),
                        Util.getOvalDrawable(button, normalColor),
                        null);
                Util.setDrawable(button, rippleDrawable);
            } else {
                StateListDrawable stateListDrawable = Util.getOvalStateListBitmapDrawable(
                        button,
                        buttonRadius,
                        normalColor,
                        highlightedColor,
                        unableColor);
                Util.setDrawable(button, stateListDrawable);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Util.setDrawable(button, Util.getSystemDrawable(context, android.R.attr.selectableItemBackgroundBorderless));
            } else {
                Util.setDrawable(button, Util.getSystemDrawable(context, android.R.attr.selectableItemBackground));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode
                && isBackPressListened
                && (popUpStateEnum == MenuStateEnum.WillPopUp || popUpStateEnum == MenuStateEnum.DidPopUp)) {
            rePopUp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //endregion

    private void ____________________________Place_Pieces() {}

    //region Place Pieces

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isOrientationChanged) {
            reLayoutAfterOrientationChanged();
        }
        if (needToLayout) {
            if (inList) delayToDoLayoutJobs();
            else doLayoutJobs();
        }
        needToLayout = false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        checkAutoPopUp();
    }

    private void doLayoutJobs() {
        if (uninitializedPopUpButtons()) return;
        clearPieces();
        createPieces();
        placeShareLinesView();
        placePieces();
        placePiecesAtPositions();
        calculateStartPositions(false);
        setShareLinesViewData();
    }

    private void clearPieces() {
        if (pieces != null) for (MenuPiece piece : pieces) button.removeView(piece);
        if (pieces != null) pieces.clear();
    }

    private void createPieces() {
        calculatePiecePositions();
        int pieceNumber = pieceNumber();
        pieces = new ArrayList<>(pieceNumber);
        for (int i = 0; i < pieceNumber; i++)
            pieces.add(PiecePlaceManager.createPiece(this, menuButtonBuilders.get(i)));
    }

    private void placePieces() {
        ArrayList<Integer> indexes;
        if (piecePlaceEnum == Share)
            indexes = AnimationManager.getOrderIndex(OrderEnum.DEFAULT, pieces.size());
        else
            indexes = AnimationManager.getOrderIndex(orderEnum, pieces.size());
        // Reverse to keep the former pieces are above than the latter(z-axis)
        // So the early-animating pieces are above than the later ones
        for (int i = indexes.size() - 1; i >= 0; i--) button.addView(pieces.get(indexes.get(i)));
    }

    private void placePiecesAtPositions() {
        int pieceNumber = pieceNumber();
        for (int i = 0; i < pieceNumber; i++) pieces.get(i).place(piecePositions.get(i));
    }

    private void calculatePiecePositions() {
        switch (buttonEnum) {
            case SimpleCircle:
            case TextInsideCircle:
            case TextOutsideCircle:
                if (piecePlaceEnum == Share)
                    piecePositions = PiecePlaceManager.getShareDotPositions(this,
                            new Point(button.getWidth(), button.getHeight()),
                            menuButtonBuilders.size());
                else
                    piecePositions = PiecePlaceManager.getDotPositions(this,
                            new Point(button.getWidth(), button.getHeight()));
                break;
            case Ham:
                piecePositions = PiecePlaceManager.getHamPositions(this,
                        new Point(button.getWidth(), button.getHeight()));
                break;
            case Unknown:
                throw new RuntimeException("The button-enum is unknown!");
        }
    }

    //endregion

    private void ____________________________Touch() {}

    private void initDraggableTouchListener() {
        if (button == null) return;
        if (!draggable) button.setOnTouchListener(null);
        else {
            button.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            if (draggable) {
                                startPositionX = getX() - event.getRawX();
                                startPositionY = getY() - event.getRawY();
                                lastMotionX = event.getRawX();
                                lastMotionY = event.getRawY();
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (Math.abs(lastMotionX - event.getRawX()) > 10
                                    || Math.abs(lastMotionY - event.getRawY()) > 10)
                                ableToStartDragging = true;
                            if (draggable && ableToStartDragging) {
                                isDragging = true;
                                if (shadow != null) {
                                    setX(event.getRawX() + startPositionX);
                                    setY(event.getRawY() + startPositionY);
                                }
                            } else {
                                ableToStartDragging = false;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (isDragging) {
                                ableToStartDragging = false;
                                isDragging = false;
                                needToCalculateStartPositions = true;
                                preventDragOutside();
                                button.setPressed(false);
                                return true;
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            if (isDragging) {
                                ableToStartDragging = false;
                                isDragging = false;
                                needToCalculateStartPositions = true;
                                preventDragOutside();
                                return true;
                            }
                            break;
                    }
                    return false;
                }
            });
        }
    }

    //endregion

    private void ____________________________Animation() {}
    //region Animation

    /**
     * Whether BMB is animating.
     *
     * @return Is animating.
     */
    public boolean isAnimating() { return animatingViewNumber != 0; }

    /**
     * Whether the BMB has finished PopUpping.
     *
     * @return whether the BMB has finished PopUpping
     */
    public boolean isPopUpd() {
        return popUpStateEnum == MenuStateEnum.DidPopUp;
    }

    /**
     * Whether the BMB has finished RePopUpping.
     *
     * @return whether the BMB has finished RePopUpping
     */
    public boolean isRePopUpd() { return popUpStateEnum == MenuStateEnum.DidRePopUp; }

    /**
     * PopUp the BMB!
     */
    public void PopUp() { innerPopUp(false); }

    /**
     * PopUp the BMB with duration 0!
     */
    public void popUpImmediately() {
        innerPopUp(true);
    }

    private void innerPopUp(boolean immediately) {
        if (isAnimating() || popUpStateEnum != MenuStateEnum.DidRePopUp) return;
        ExceptionManager.judge(this, menuButtonBuilders);
        popUpStateEnum = MenuStateEnum.WillPopUp;
        if (onMenuListener != null) onMenuListener.onPopUpWillShow();
        calculateStartPositions(false);
        createButtons();
        dimBackground(immediately);
        startPopUpAnimations(immediately);
        startPopUpAnimationForFadeViews(immediately);
        if (isBackPressListened) {
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
        }
    }

    /**
     * Re-PopUp the BMB!
     */
    public void rePopUp() { innerRePopUp(false); }

    /**
     * Re-PopUp the BMB with duration 0!
     */
    public void rePopUpImmediately() {
        innerRePopUp(true);
    }

    private void innerRePopUp(boolean immediately) {
        if (isAnimating() || popUpStateEnum != MenuStateEnum.DidPopUp) return;
        popUpStateEnum = MenuStateEnum.WillRePopUp;
        if (onMenuListener != null) onMenuListener.onPopUpWillHide();
        lightBackground(immediately);
        startRePopUpAnimations(immediately);
        startRePopUpAnimationForFadeViews(immediately);
        if (isBackPressListened) {
            setFocusable(false);
            setFocusableInTouchMode(false);
        }
    }

    private void dimBackground(boolean immediately) {
        createBackground();
        Util.setVisibility(VISIBLE, background);
        long duration = immediately ? 1 : showDuration + showDelay * (pieces.size() - 1);
        background.dim(duration, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                popUpStateEnum = MenuStateEnum.DidPopUp;
                if (onMenuListener != null) onMenuListener.onPopUpDidShow();
            }
        });
        if (piecePlaceEnum == Share) {
            AnimationManager.animate(shareLinesView, "showProcess", 0, duration,
                    Ease.getInstance(EaseEnum.Linear), 0f, 1f);
        }
    }

    private void lightBackground(boolean immediately) {
        createBackground();
        long duration = immediately ? 1 : hideDuration + hideDelay * (pieces.size() - 1);
        background.light(duration, null);
        if (piecePlaceEnum == Share) {
            AnimationManager.animate( shareLinesView, "hideProcess", 0, duration,
                    Ease.getInstance(EaseEnum.Linear), 0f, 1f);
        }
    }

    private void finishRePopUpAnimations() {
        if (isAnimating()) {
            return;
        }
        popUpStateEnum = MenuStateEnum.DidRePopUp;
        if (onMenuListener != null) onMenuListener.onPopUpDidHide();
        background.setVisibility(GONE);
        clearViews(false);
    }

    private void startPopUpAnimations(boolean immediately) {
        if (background != null) background.removeAllViews();
        calculateEndPositions();
        ArrayList<Integer> indexes;
        if (piecePlaceEnum == Share)
            indexes = AnimationManager.getOrderIndex(OrderEnum.DEFAULT, pieces.size());
        else indexes = AnimationManager.getOrderIndex(orderEnum, pieces.size());
        // Todo Fix the following bug
        // There is a strange bug when use3DTransformAnimation is true.
        // The last PopUp-button that rePopUps has a strange behavior with a duplicate shadow.
        // So we need to recreate it.
        if (lastRePopUpIndex != -1 && use3DTransformAnimation)
            menuButtons.set(lastRePopUpIndex, menuButtonBuilders.get(lastRePopUpIndex)
                    .innerListener(this).index(lastRePopUpIndex).build(context));
        // Reverse to keep the former PopUp-buttons are above than the latter(z-axis)
        // So the early-animating PopUp-buttons are above than the later ones
        for (int i = indexes.size() - 1; i >= 0; i--) {
            int index = indexes.get(i);
            MenuButton menuButton = menuButtons.get(index);
            PointF startPosition = new PointF(
                    startPositions.get(index).x - menuButton.centerPoint.x,
                    startPositions.get(index).y - menuButton.centerPoint.y);
            putPopUpButtonInBackground(menuButton, startPosition);
            startEachPopUpAnimation(pieces.get(index), menuButton, startPosition,
                    endPositions.get(index), i, immediately);
        }
    }

    private void startRePopUpAnimations(boolean immediately) {
        ArrayList<Integer> indexes;
        if (piecePlaceEnum == Share)
            indexes = AnimationManager.getOrderIndex(OrderEnum.REVERSE, pieces.size());
        else indexes = AnimationManager.getOrderIndex(orderEnum, pieces.size());
        lastRePopUpIndex = indexes.get(indexes.size() - 1);
        for (Integer index : indexes) menuButtons.get(index).bringToFront();
        for (int i = 0; i < indexes.size(); i++) {
            int index = indexes.get(i);
            MenuButton menuButton = menuButtons.get(index);
            PointF startPosition = new PointF(
                    startPositions.get(index).x - menuButton.centerPoint.x,
                    startPositions.get(index).y - menuButton.centerPoint.y);
            startEachRePopUpAnimation( pieces.get(index), menuButton,
                    endPositions.get(index), startPosition, i, immediately);
        }
    }

    private void startEachPopUpAnimation(final MenuPiece piece,
                                         final MenuButton menuButton,
                                         final PointF startPosition,
                                         final PointF endPosition,
                                         final int delayFactor,
                                         final boolean immediately) {
        if (isBatterySaveModeTurnOn()) {
            post(new Runnable() {
                @Override
                public void run() {
                    innerStartEachPouUpAnimation(piece, menuButton, startPosition, endPosition, delayFactor, immediately);
                }
            });
        } else {
            innerStartEachPouUpAnimation(piece, menuButton, startPosition, endPosition, delayFactor, immediately);
        }
    }

    private void innerStartEachPouUpAnimation(final MenuPiece piece,
                                              final MenuButton menuButton,
                                              PointF startPosition,
                                              PointF endPosition,
                                              int delayFactor,
                                              boolean immediately) {
        animatingViewNumber++;
        float[] xs = new float[frames + 1];
        float[] ys = new float[frames + 1];
        float scaleX = piece.getWidth() * 1.0f / menuButton.contentWidth();
        float scaleY = piece.getHeight() * 1.0f / menuButton.contentHeight();
        long delay = immediately ? 1 : showDelay * delayFactor;
        long duration = immediately ? 1 : showDuration;
        menuButton.setSelfScaleAnchorPoints();
        menuButton.setScaleX(scaleX);
        menuButton.setScaleY(scaleY);
        menuButton.hideAllGoneViews();
        AnimationManager.calculateShowXY(menuEnum,
                new PointF(background.getLayoutParams().width, background.getLayoutParams().height),
                Ease.getInstance(showMoveEaseEnum), frames, startPosition, endPosition, xs, ys);
        if (menuButton.isNeededColorAnimation()) {
            if (menuButton.prepareColorTransformAnimation())
                AnimationManager.animate(menuButton, "rippleButtonColor", delay, duration, ShowRgbEvaluator.getInstance(), menuButton.pieceColor(), menuButton.buttonColor());
            else
                AnimationManager.animate(menuButton, "nonRippleButtonColor", delay, duration, ShowRgbEvaluator.getInstance(), menuButton.pieceColor(), menuButton.buttonColor());
        }
        AnimationManager.animate(menuButton, "x", delay, duration, new LinearInterpolator(), xs);
        AnimationManager.animate(menuButton, "y", delay, duration, new LinearInterpolator(), ys);
        AnimationManager.rotate(menuButton, delay, duration, Ease.getInstance(showRotateEaseEnum), 0, rotateDegree);
        AnimationManager.animate("alpha", delay, duration, new float[]{0, 1}, Ease.getInstance(EaseEnum.Linear), menuButton.goneViews());
        AnimationManager.animate(menuButton, "scaleX", delay, duration, Ease.getInstance(showScaleEaseEnum), scaleX, 1);
        AnimationManager.animate(menuButton, "scaleY", delay, duration, Ease.getInstance(showScaleEaseEnum),
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        Util.setVisibility(INVISIBLE, piece);
                        Util.setVisibility(VISIBLE, menuButton);
                        menuButton.willShow();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        menuButton.didShow();
                        animatingViewNumber--;
                    }
                }, scaleY, 1);

        if (use3DTransformAnimation) {
            Rotate3DAnimation rotate3DAnimation = AnimationManager.getRotate3DAnimation(xs, ys,
                    delay, duration, menuButton);
            rotate3DAnimation.set(menuButton, startPosition.x, startPosition.y);
            menuButton.setCameraDistance(0);
            menuButton.startAnimation(rotate3DAnimation);
        }
    }

    private void startEachRePopUpAnimation(final MenuPiece piece,
                                           final MenuButton menuButton,
                                           PointF startPosition,
                                           PointF endPosition,
                                           final int delayFactor,
                                           boolean immediately) {
        animatingViewNumber++;
        float[] xs = new float[frames + 1];
        float[] ys = new float[frames + 1];
        float scaleX = piece.getWidth() * 1.0f / menuButton.contentWidth();
        float scaleY = piece.getHeight() * 1.0f / menuButton.contentHeight();
        long delay = immediately ? 1 : hideDelay * delayFactor;
        long duration = immediately ? 1 : hideDuration;
        AnimationManager.calculateHideXY(
                menuEnum,
                new PointF(background.getLayoutParams().width, background.getLayoutParams().height),
                Ease.getInstance(hideMoveEaseEnum), frames, startPosition, endPosition, xs, ys);
        if (menuButton.isNeededColorAnimation()) {
            if (menuButton.prepareColorTransformAnimation())
                AnimationManager.animate(menuButton, "rippleButtonColor", delay, duration, HideRgbEvaluator.getInstance(), menuButton.buttonColor(), menuButton.pieceColor());
            else
                AnimationManager.animate(menuButton, "nonRippleButtonColor", delay, duration, HideRgbEvaluator.getInstance(), menuButton.buttonColor(), menuButton.pieceColor());
        }
        AnimationManager.animate(menuButton, "x", delay, duration, new LinearInterpolator(), xs);
        AnimationManager.animate(menuButton, "y", delay, duration, new LinearInterpolator(), ys);
        AnimationManager.rotate(menuButton, delay, duration, Ease.getInstance(hideRotateEaseEnum), 0, -rotateDegree);
        AnimationManager.animate("alpha", delay, duration, new float[]{1, 0}, Ease.getInstance(EaseEnum.Linear), menuButton.goneViews());
        AnimationManager.animate(menuButton, "scaleX", delay, duration, Ease.getInstance(hideScaleEaseEnum), 1, scaleX);
        AnimationManager.animate(menuButton, "scaleY", delay, duration, Ease.getInstance(hideScaleEaseEnum),
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        menuButton.willHide();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Util.setVisibility(VISIBLE, piece);
                        Util.setVisibility(INVISIBLE, menuButton);
                        menuButton.didHide();
                        animatingViewNumber--;
                        finishRePopUpAnimations();
                    }
                }, 1, scaleY);
        if (use3DTransformAnimation) {
            Rotate3DAnimation rotate3DAnimation = AnimationManager.getRotate3DAnimation(xs, ys,
                    delay, duration, menuButton);
            rotate3DAnimation.set(menuButton, endPosition.x, endPosition.y);
            menuButton.setCameraDistance(0);
            menuButton.startAnimation(rotate3DAnimation);
        }
    }

    //endregion

    private void ____________________________Support_Methods() {}
    //region Support Methods

    private void createBackground() {
        if (background == null) {
            background = new BackgroundView(context, this);
        }
    }

    protected ViewGroup getParentView() {
        if (popUpInWholeScreen) {
            Activity activity = Util.scanForActivity(context);
            if (activity == null) {
                return (ViewGroup) getParent();
            } else {
                return (ViewGroup) activity.getWindow().getDecorView();
            }
        } else {
            return (ViewGroup) getParent();
        }
    }

    private void clearBackground() {
        Util.setVisibility(GONE, background);
        if (!cacheOptimization || inList || inFragment) {
            background.removeAllViews();
            ((ViewGroup)background.getParent()).removeView(background);
            background = null;
        }
    }

    private void createButtons() {
        if (!needToCreateButtons) return;
        needToCreateButtons = false;
        menuButtons = new ArrayList<>(pieces.size());
        int buttonNumber = pieces.size();
        for (int i = 0; i < menuButtonBuilders.size(); i++)
            menuButtons.add(menuButtonBuilders.get(i).innerListener(this).index(i).build(context));
        switch (buttonEnum) {
            case SimpleCircle:
                simpleCircleButtonRadius = ((SimpleCircleButton.Builder)
                        menuButtonBuilders.get(0)).getButtonRadius();
                break;
            case TextInsideCircle:
                textInsideCircleButtonRadius = ((TextInsideCircleButton.Builder)
                        menuButtonBuilders.get(0)).getButtonRadius();
                break;
            case TextOutsideCircle:
                textOutsideCircleButtonWidth = ((TextOutsideCircleButton.Builder)
                        menuButtonBuilders.get(0)).getButtonContentWidth();
                textOutsideCircleButtonHeight = ((TextOutsideCircleButton.Builder)
                        menuButtonBuilders.get(0)).getButtonContentHeight();
                break;
            case Ham:
                hamButtonWidth = ((HamButton.Builder)
                        menuButtonBuilders.get(0)).getButtonWidth();
                hamButtonHeight = ((HamButton.Builder)
                        menuButtonBuilders.get(0)).getButtonHeight();
                break;
        }
    }

    protected void onBackgroundClicked() {
        if (isAnimating()) return;
        if (onMenuListener != null) onMenuListener.onBackgroundClick();
        if (cancelable) rePopUp();
    }

    private void calculateStartPositions(boolean force) {
        if (!(force || needToCalculateStartPositions || inList || inFragment)) return;
        if (!force) needToCalculateStartPositions = false;
        startPositions = new ArrayList<>(pieceNumber());
        ViewGroup rootView = getParentView();
        int[] rootViewLocation = new int[2];
        rootView.getLocationOnScreen(rootViewLocation);
        for (int i = 0; i < pieces.size(); i++) {
            PointF pieceCenterInRootView = new PointF();
            int[] buttonLocation = new int[2];
            button.getLocationOnScreen(buttonLocation);
            pieceCenterInRootView.x = buttonLocation[0] + piecePositions.get(i).left
                    - rootViewLocation[0] + pieces.get(i).getLayoutParams().width / 2;
            pieceCenterInRootView.y = buttonLocation[1] + piecePositions.get(i).top
                    - rootViewLocation[1] + pieces.get(i).getLayoutParams().height / 2;
            startPositions.add(pieceCenterInRootView);
        }
    }

    private void calculateEndPositions() {
        Point parentSize = new Point(background.getLayoutParams().width,
                background.getLayoutParams().height);
        switch (buttonEnum) {
            case SimpleCircle:
                endPositions = ButtonPlaceManager.getPositions(parentSize,
                        simpleCircleButtonRadius, menuButtonBuilders.size(), this);
                break;
            case TextInsideCircle:
                endPositions = ButtonPlaceManager.getPositions(parentSize,
                        textInsideCircleButtonRadius, menuButtonBuilders.size(), this);
                break;
            case TextOutsideCircle:
                endPositions = ButtonPlaceManager.getPositions(parentSize,
                        textOutsideCircleButtonWidth, textOutsideCircleButtonHeight,
                        menuButtonBuilders.size(), this);
                break;
            case Ham:
                endPositions = ButtonPlaceManager.getPositions(parentSize,
                        hamButtonWidth, hamButtonHeight,
                        menuButtonBuilders.size(), this);
                break;
        }
        for (int i = 0; i < menuButtons.size(); i++)
            endPositions.get(i).offset(-menuButtons.get(i).centerPoint.x,
                    -menuButtons.get(i).centerPoint.y);
    }

    private MenuButton putPopUpButtonInBackground(MenuButton menuButton, PointF position) {
        createBackground();
        menuButton.place(
                (int) position.x,
                (int) position.y,
                menuButton.trueWidth(),
                menuButton.trueHeight());
        menuButton.setVisibility(INVISIBLE);
        background.addView(menuButton);
        return menuButton;
    }

    private void clearViews(boolean force) {
        if (force || !cacheOptimization || inList || inFragment) {
            clearButtons();
            clearBackground();
        }
    }

    private void clearButtons() {
        needToCreateButtons = true;
        if (background != null) for (MenuButton menuButton : menuButtons) background.removeView(menuButton);
        menuButtons.clear();
    }

    private float buttonMaxHeight() {
        switch (buttonEnum) {
            case SimpleCircle: return simpleCircleButtonRadius * 2;
            case TextInsideCircle: return textInsideCircleButtonRadius * 2;
            case TextOutsideCircle: return textOutsideCircleButtonHeight;
            case Ham: return hamButtonHeight;
        }
        return 0;
    }

    private void preventDragOutside() {
        boolean needToAdjustXY = false;
        float newX = getX();
        float newY = getY();
        ViewGroup parentView = (ViewGroup) getParent();

        if (newX < edgeInsetsInParentView.left) {
            newX = edgeInsetsInParentView.left;
            needToAdjustXY = true;
        }

        if (newY < edgeInsetsInParentView.top) {
            newY = edgeInsetsInParentView.top;
            needToAdjustXY = true;
        }

        if (newX > parentView.getWidth() - edgeInsetsInParentView.right - getWidth()) {
            newX = parentView.getWidth() - edgeInsetsInParentView.right - getWidth();
            needToAdjustXY = true;
        }

        if (newY > parentView.getHeight() - edgeInsetsInParentView.bottom - getHeight()) {
            newY = parentView.getHeight() - edgeInsetsInParentView.bottom - getHeight();
            needToAdjustXY = true;
        }

        if (needToAdjustXY) {
            AnimationManager.animate(this, "x", 0, 300, Ease.getInstance(EaseEnum.EaseOutBack), getX(), newX);
            AnimationManager.animate(this, "y", 0, 300, Ease.getInstance(EaseEnum.EaseOutBack), getY(), newY);
        }
    }

    private void toLayout() {
        if (needToLayout) return;
        needToLayout = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (!isInLayout()) requestLayout();
        } else {
            requestLayout();
        }
    }

    private void delayToDoLayoutJobs() {
        if (layoutJobsRunnable == null) {
            layoutJobsRunnable = new Runnable() {
                @Override
                public void run() {
                    doLayoutJobs();
                }
            };
        }
        post(layoutJobsRunnable);
    }

    private int pieceNumber() {
        if (piecePlaceEnum.equals(PiecePlaceEnum.Unknown)) return 0;
        else if (piecePlaceEnum.equals(Share)) return menuButtonBuilders.size();
        else if (piecePlaceEnum.equals(PiecePlaceEnum.Custom))
            return customPiecePlacePositions.size();
        else return piecePlaceEnum.pieceNumber();
    }

    @Override
    public void onButtonClick(int index, MenuButton menuButton) {
        if (isAnimating()) return;
        if (onMenuListener != null) onMenuListener.onClicked(index, menuButton);
        if (autoHide) rePopUp();
    }

    private void placeShareLinesView() {
        if (piecePlaceEnum == Share) {
            if (shareLinesView != null) button.removeView(shareLinesView);
            shareLinesView = new ShareLinesView(context);
            shareLinesView.setLine1Color(shareLine1Color);
            shareLinesView.setLine2Color(shareLine2Color);
            shareLinesView.setLineWidth(shareLineWidth);
            button.addView(shareLinesView);
            shareLinesView.place(0, 0, button.getWidth(), button.getHeight());
        } else {
            if (shareLinesView != null) button.removeView(shareLinesView);
        }
    }

    private void setShareLinesViewData() {
        if (piecePlaceEnum == Share) shareLinesView.setData(piecePositions, this);
    }

    private boolean uninitializedPopUpButtons() {
        return buttonEnum.equals(ButtonEnum.Unknown)
                || piecePlaceEnum.equals(PiecePlaceEnum.Unknown)
                || buttonPlaceEnum.equals(ButtonPlaceEnum.Unknown);
    }

    private boolean isBatterySaveModeTurnOn() {
        PowerManager powerManager = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && powerManager.isPowerSaveMode();
    }

    private void checkAutoPopUp() {
        if (autoPopUpImmediately) popUpImmediately();
        else if (autoPopUp) PopUp();
        autoPopUpImmediately = autoPopUp = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isOrientationAdaptable) initOrientationListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (orientationEventListener != null) orientationEventListener.disable();
    }

    private void initOrientationListener() {
        if (orientationEventListener == null) {
            orientationEventListener = new OrientationEventListener(context) {
                @Override
                public void onOrientationChanged(int orientation) {
                    if (orientation != lastOrientation && lastOrientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
                        isOrientationChanged = true;
                    }
                    lastOrientation = orientation;
                }
            };
        }
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }
    }

    private void reLayoutAfterOrientationChanged() {
        post(new Runnable() {
            @Override
            public void run() {
                if (background != null) background.reLayout(PoupupMenuButton.this);
                calculateStartPositions(true);
                calculateEndPositions();
                switch (popUpStateEnum) {
                    case DidRePopUp:
                        break;
                    case DidPopUp:
                        placeButtons();
                        break;
                    case WillPopUp:
                    case WillRePopUp:
                        stopAllAnimations(popUpStateEnum == MenuStateEnum.WillPopUp);
                        placeButtons();
                        break;
                }
            }
        });
    }

    private void placeButtons() {
        for (int i = 0; i < menuButtons.size(); i++) {
            MenuButton menuButton = menuButtons.get(i);
            PointF pointF = endPositions.get(i);
            menuButton.setX(pointF.x);
            menuButton.setY(pointF.y);
        }
    }

    private void stopAllAnimations(boolean isPopUpAnimation) {
        if (background != null) background.clearAnimation();
        for (MenuButton menuButton : menuButtons) {
            menuButton.clearAnimation();
        }
    }

    //endregion

    private void ____________________________Builders_and_Buttons() {}

    //region Builders

    /**
     * Add a builder to bmb, notice that @needToLayout will be called.
     *
     * @param builder builder
     */
    public void addBuilder(MenuButtonBuilder builder) {
        menuButtonBuilders.add(builder);
        toLayout();
    }

    /**
     * Set a builder at index, notice that @needToLayout will be called.
     *
     * @param index index
     * @param builder builder
     */
    public void setBuilder(int index, MenuButtonBuilder builder) {
        menuButtonBuilders.set(index, builder);
        toLayout();
    }

    /**
     * Set builders array, notice that @needToLayout will be called.
     *
     * @param builders builders
     */
    public void setBuilders(ArrayList<MenuButtonBuilder> builders) {
        menuButtonBuilders = builders;
        toLayout();
    }

    /**
     * Get a builder at index.
     *
     * @param index index
     * @return the builder at the index
     */
    public MenuButtonBuilder getBuilder(int index) {
        if (menuButtonBuilders == null || index < 0 || index >= menuButtonBuilders.size()) return null;
        else return menuButtonBuilders.get(index);
    }

    /**
     * Remove a builder, notice that @needToLayout will be called.
     *
     * @param builder builder
     */
    public void removeBuilder(MenuButtonBuilder builder) {
        menuButtonBuilders.remove(builder);
        toLayout();
    }

    /**
     * Remove a builder at index, notice that @needToLayout will be called.
     *
     * @param index index
     */
    public void removeBuilder(int index) {
        menuButtonBuilders.remove(index);
        toLayout();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        button.setEnabled(enabled);
        setButtonBackground();
    }

    /**
     * Set enable attribute of the PopUp-button at index.
     *
     * @param index index of the PopUp-button
     * @param enable whether the PopUp-button should be enable
     */
    public void setEnable(int index, boolean enable) {
        if (index < 0) return;
        if (menuButtonBuilders != null && index < menuButtonBuilders.size()) {
            menuButtonBuilders.get(index).setUnable(!enable);
        }
        if (menuButtons != null && index < menuButtons.size()) {
            menuButtons.get(index).setEnabled(enable);
        }
    }

    /**
     * Remove all builders, notice that @needToLayout will NOT be called.
     */
    public void clearBuilders() {
        menuButtonBuilders.clear();
    }

    /**
     * Get the array of builders.
     *
     * @return array of builders
     */
    public ArrayList<MenuButtonBuilder> getBuilders() {
        return menuButtonBuilders;
    }

    /**
     * Get a PopUp button at index.
     * Notice that the PopUp button may be null,
     * because PopUp buttons are cleared in some situation(in list, in fragment, etc.)
     *
     * @param index index
     * @return PopUp button
     */
    public MenuButton getPopUpButton(int index) {
        if (menuButtons != null && 0 <= index && index < menuButtons.size()) return menuButtons.get(index);
        return null;
    }

    /**
     * Get PopUp buttons.
     * Notice that the PopUp button may be null,
     * because PopUp buttons are cleared in some situation(in list, in fragment, etc.)
     *
     * @return PopUp buttons
     */
    public ArrayList<MenuButton> getMenuButtons() {
        return menuButtons;
    }

    //endregion

    private void ____________________________Fade_Views() {}

    //region Fade Views

    private void startPopUpAnimationForFadeViews(boolean immediately) {
        long duration = immediately ? 1 : showDuration + showDelay * (pieces.size() - 1);
        AnimationManager.animate("alpha", 0, duration, new float[]{1, 0},
                new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
                        return Math.min(input * 2, 1);
                    }
                }, getFadeViews());
    }

    private void startRePopUpAnimationForFadeViews(boolean immediately) {
        long duration = immediately ? 1 : hideDuration + hideDelay * (pieces.size() - 1);
        AnimationManager.animate("alpha", 0, duration, new float[]{0, 1},
                new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
                        if (input <= 0.5) return 0;
                        else return Math.min((input - 0.5f) * 2, 1);
                    }
                }, getFadeViews());
    }

    private ArrayList<View> getFadeViews() {
        ArrayList<View> fadeViews = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View subView = getChildAt(i);
            if (!(subView == shadow
                    || subView == button
                    || subView == shareLinesView)) fadeViews.add(subView);
        }
        return fadeViews;
    }

    //endregion

    private void ____________________________Getters_and_Setters() {}

    //region Getter and Setter


    public boolean isCacheOptimization() {
        return cacheOptimization;
    }

    /**
     * Whether use cache optimization to avoid multi-creating PopUp-buttons.
     *
     * @param cacheOptimization cache optimization
     */
    public void setCacheOptimization(boolean cacheOptimization) {
        this.cacheOptimization = cacheOptimization;
    }

    public boolean isPopUpInWholeScreen() {
        return popUpInWholeScreen;
    }

    /**
     * Whether the BMB should PopUp in the whole screen.
     * If this value is false, then BMB will PopUp its PopUp-buttons to its parent-view.
     *
     * @param popUpInWholeScreen PopUp in the whole screen
     */
    public void setPopUpInWholeScreen(boolean popUpInWholeScreen) {
        this.popUpInWholeScreen = popUpInWholeScreen;
    }

    public boolean isInList() {
        return inList;
    }

    /**
     * When BMB is used in list-view, it must be setInList(true).
     *
     * @param inList use BMB in list-view
     */
    public void setInList(boolean inList) {
        this.inList = inList;
    }

    public boolean isInFragment() {
        return inFragment;
    }

    /**
     * When BMB is used in fragment, it must be setInFragment(true).
     *
     * @param inFragment use BMB in fragment
     */
    public void setInFragment(boolean inFragment) {
        this.inFragment = inFragment;
    }

    public boolean isBackPressListened() {
        return isBackPressListened;
    }

    /**
     * Whether BMB will rePopUp when the back-key is pressed.
     *
     * @param backPressListened whether BMB will rePopUp when the back-key is pressed
     */
    public void setBackPressListened(boolean backPressListened) {
        isBackPressListened = backPressListened;
    }

    public boolean isShadowEffect() {
        return shadowEffect;
    }

    /**
     * Whether BMB should have a shadow-effect.
     * Notice that when you set @backgroundEffect to false, this value will set to false too.
     *
     * @param shadowEffect shadow-effect
     */
    public void setShadowEffect(boolean shadowEffect) {
        if (this.shadowEffect == shadowEffect) return;
        this.shadowEffect = shadowEffect;
        initShadow();
    }

    public int getShadowOffsetX() {
        return shadowOffsetX;
    }

    /**
     * Set the BMB's shadow offset in the x-axis.
     *
     * @param shadowOffsetX x-axis shadow offset
     */
    public void setShadowOffsetX(int shadowOffsetX) {
        if (this.shadowOffsetX == shadowOffsetX) return;
        this.shadowOffsetX = shadowOffsetX;
        initShadow();
    }

    public int getShadowOffsetY() {
        return shadowOffsetY;
    }

    /**
     * Set the BMB's shadow offset in the y-axis.
     *
     * @param shadowOffsetY y-axis shadow offset
     */
    public void setShadowOffsetY(int shadowOffsetY) {
        if (this.shadowOffsetY == shadowOffsetY) return;
        this.shadowOffsetY = shadowOffsetY;
        initShadow();
    }

    public int getShadowRadius() {
        return shadowRadius;
    }

    /**
     * Set the shadow-radius of BMB, please notice that the "radius" here means the extra
     * radius of BMB.
     * For example, if the radius of BMB is 30dp and the shadow-radius is 5dp, then the
     * radius of shadow-circle behind the BMB if 35dp.
     *
     * @param shadowRadius extra shadow radius
     */
    public void setShadowRadius(int shadowRadius) {
        if (this.shadowRadius == shadowRadius) return;
        this.shadowRadius = shadowRadius;
        initShadow();
    }

    public int getShadowColor() {
        return shadowColor;
    }

    /**
     * Set the color of shadow.
     *
     * @param shadowColor color of shadow
     */
    public void setShadowColor(int shadowColor) {
        if (this.shadowColor == shadowColor) return;
        this.shadowColor = shadowColor;
        initShadow();
    }

    public int getButtonRadius() {
        return buttonRadius;
    }

    /**
     * Set the radius of BMB, if you use this method to set the size of BMB,
     * then you should set the width and height of BMB in .xml file to "wrap-content".
     *
     * @param buttonRadius radius of BMB
     */
    public void setButtonRadius(int buttonRadius) {
        if (this.buttonRadius == buttonRadius) return;
        this.buttonRadius = buttonRadius;
        initButton();
        toLayout();
    }

    public ButtonEnum getButtonEnum() {
        return buttonEnum;
    }

    /**
     * Set the button-enum for bmb, notice that methods {@link #toLayout()}, {@link #clearPieces()},
     * {@link #clearBuilders()}, and {@link #clearButtons()} will be called.
     *
     * @param buttonEnum button-enum
     */
    public void setButtonEnum(ButtonEnum buttonEnum) {
        if (this.buttonEnum.equals(buttonEnum)) return;
        this.buttonEnum = buttonEnum;
        clearPieces();
        clearBuilders();
        clearButtons();
        toLayout();
    }

    public boolean isBackgroundEffect() {
        return backgroundEffect;
    }

    /**
     * Whether the BMB should have a background effect. Use this when you don't want the
     * circle background of BMB.
     * It can be useful when you want to use BMB in actionbar of in list-view.
     * Please notice that, when BMB does not have a background effect, it does not
     * have shadow effect, either.
     *
     * @param backgroundEffect background effect
     */
    public void setBackgroundEffect(boolean backgroundEffect) {
        if (this.backgroundEffect == backgroundEffect) return;
        this.backgroundEffect = backgroundEffect;
        setButtonBackground();
        toLayout();
    }

    public boolean isRippleEffect() {
        return rippleEffect;
    }

    /**
     * Whether the BMB should have a ripple-effect.
     * The ripple effect is disable below LOLLIPOP.
     *
     * @param rippleEffect ripple effect
     */
    public void setRippleEffect(boolean rippleEffect) {
        if (this.rippleEffect == rippleEffect) return;
        this.rippleEffect = rippleEffect;
        setButtonBackground();
        toLayout();
    }

    public int getNormalColor() {
        return normalColor;
    }

    /**
     * Set the color of BMB at normal-state.
     *
     * @param normalColor the color of BMB at normal-state
     */
    public void setNormalColor(int normalColor) {
        if (this.normalColor == normalColor) return;
        this.normalColor = normalColor;
        setButtonBackground();
        toLayout();
    }

    public int getHighlightedColor() {
        return highlightedColor;
    }

    /**
     * Set the color of BMB at highlighted-state.
     *
     * @param highlightedColor the color of BMB at highlighted-state
     */
    public void setHighlightedColor(int highlightedColor) {
        if (this.highlightedColor == highlightedColor) return;
        this.highlightedColor = highlightedColor;
        setButtonBackground();
        toLayout();
    }

    public int getUnableColor() {
        return unableColor;
    }

    /**
     * Set the color of BMB at unable-state.
     *
     * @param unableColor the color of BMB at unable-state
     */
    public void setUnableColor(int unableColor) {
        if (this.unableColor == unableColor) return;
        this.unableColor = unableColor;
        setButtonBackground();
        toLayout();
    }

    public boolean isDraggable() {
        return draggable;
    }

    /**
     * Make BMB draggable or not.
     *
     * @param draggable draggable or not.
     */
    public void setDraggable(boolean draggable) {
        if (this.draggable == draggable) return;
        this.draggable = draggable;
        initDraggableTouchListener();
    }

    public Rect getEdgeInsetsInParentView() {
        return edgeInsetsInParentView;
    }

    /**
     * Set the top, left, bottom and right margins in BMB's parent view when BMB is draggable.
     * @param edgeInsetsInParentView the top, left, bottom and right margins
     */
    public void setEdgeInsetsInParentView(Rect edgeInsetsInParentView) {
        if (this.edgeInsetsInParentView.equals(edgeInsetsInParentView)) return;
        this.edgeInsetsInParentView = edgeInsetsInParentView;
        preventDragOutside();
    }

    public float getDotRadius() {
        return dotRadius;
    }

    /**
     * Set the radius of dots in BMB.
     *
     * @param dotRadius radius of dot
     */
    public void setDotRadius(float dotRadius) {
        if (this.dotRadius == dotRadius) return;
        this.dotRadius = dotRadius;
        toLayout();
    }

    public float getHamWidth() {
        return hamWidth;
    }

    /**
     * Set the width of hams in BMB.
     *
     * @param hamWidth width of ham
     */
    public void setHamWidth(float hamWidth) {
        if (this.hamWidth == hamWidth) return;
        this.hamWidth = hamWidth;
        toLayout();
    }

    public float getHamHeight() {
        return hamHeight;
    }

    /**
     * Set the height of hams in BMB.
     *
     * @param hamHeight height of ham
     */
    public void setHamHeight(int hamHeight) {
        if (this.hamHeight == hamHeight) return;
        this.hamHeight = hamHeight;
        toLayout();
    }

    public float getPieceCornerRadius() {
        return pieceCornerRadius;
    }

    /**
     * Set the corner radius of pieces.
     *
     * @param pieceCornerRadius corner radius of pieces
     */
    public void setPieceCornerRadius(float pieceCornerRadius) {
        if (this.pieceCornerRadius == pieceCornerRadius) return;
        this.pieceCornerRadius = pieceCornerRadius;
        toLayout();
    }

    public float getPieceHorizontalMargin() {
        return pieceHorizontalMargin;
    }

    /**
     * Set the horizontal margin between pieces(dots, blocks or hams) in BMB.
     *
     * @param pieceHorizontalMargin horizontal margin between pieces
     */
    public void setPieceHorizontalMargin(float pieceHorizontalMargin) {
        if (this.pieceHorizontalMargin == pieceHorizontalMargin) return;
        this.pieceHorizontalMargin = pieceHorizontalMargin;
        toLayout();
    }

    public float getPieceVerticalMargin() {
        return pieceVerticalMargin;
    }

    /**
     * Set the vertical margin between pieces(dots, blocks or hams) in BMB.
     *
     * @param pieceVerticalMargin vertical margin between pieces
     */
    public void setPieceVerticalMargin(float pieceVerticalMargin) {
        if (this.pieceVerticalMargin == pieceVerticalMargin) return;
        this.pieceVerticalMargin = pieceVerticalMargin;
        toLayout();
    }

    public float getPieceInclinedMargin() {
        return pieceInclinedMargin;
    }

    /**
     * Set the inclined margin between pieces(dots, blocks or hams) in BMB.
     *
     * @param pieceInclinedMargin inclined margin between pieces
     */
    public void setPieceInclinedMargin(float pieceInclinedMargin) {
        if (this.pieceInclinedMargin == pieceInclinedMargin) return;
        this.pieceInclinedMargin = pieceInclinedMargin;
        toLayout();
    }

    public float getShareLineLength() {
        return shareLineLength;
    }

    /**
     * Set the length of share-lines in BMB, only works when piece-place-enum is Share.
     *
     * @param shareLineLength length of share-lines, in pixel
     */
    public void setShareLineLength(float shareLineLength) {
        if (this.shareLineLength == shareLineLength) return;
        this.shareLineLength = shareLineLength;
        toLayout();
    }

    public int getShareLine1Color() {
        return shareLine1Color;
    }

    /**
     * Set the color of share-line 1(the above), only works when piece-place-enum is Share.
     *
     * @param shareLine1Color color of share-line 1
     */
    public void setShareLine1Color(int shareLine1Color) {
        if (this.shareLine1Color == shareLine1Color) return;
        this.shareLine1Color = shareLine1Color;
        if (shareLinesView != null) {
            shareLinesView.setLine1Color(shareLine1Color);
            shareLinesView.invalidate();
        }
    }

    public int getShareLine2Color() {
        return shareLine2Color;
    }

    /**
     * Set the color of share-line 2(the below), only works when piece-place-enum is Share.
     *
     * @param shareLine2Color color of share-line 2
     */
    public void setShareLine2Color(int shareLine2Color) {
        if (this.shareLine2Color == shareLine2Color) return;
        this.shareLine2Color = shareLine2Color;
        if (shareLinesView != null) {
            shareLinesView.setLine2Color(shareLine2Color);
            shareLinesView.invalidate();
        }
    }

    public float getShareLineWidth() {
        return shareLineWidth;
    }

    /**
     * Set the width of share-lines in BMB, only works when piece-place-enum is Share.
     *
     * @param shareLineWidth width of share-lines, in pixel
     */
    public void setShareLineWidth(float shareLineWidth) {
        if (this.shareLineWidth == shareLineWidth) return;
        this.shareLineWidth = shareLineWidth;
        if (shareLinesView != null) {
            shareLinesView.setLineWidth(shareLineWidth);
            shareLinesView.invalidate();
        }
    }

    public PiecePlaceEnum getPiecePlaceEnum() {
        return piecePlaceEnum;
    }

    /**
     * Set the piece-place-enum, notice that @requestLayout() will be called.
     *
     * @param piecePlaceEnum piece-place-enum
     */
    public void setPiecePlaceEnum(PiecePlaceEnum piecePlaceEnum) {
        this.piecePlaceEnum = piecePlaceEnum;
        clearPieces();
        toLayout();
    }

    public ArrayList<PointF> getCustomPiecePlacePositions() {
        return customPiecePlacePositions;
    }

    /**
     * The customized positions of pieces. Only works when the piece-place-enum is **custom**.
     * The elements in positions-array must be Point.
     *
     * @param customPiecePlacePositions customized positions
     */
    public void setCustomPiecePlacePositions(ArrayList<PointF> customPiecePlacePositions) {
        if (this.customPiecePlacePositions.equals(customPiecePlacePositions)) return;
        this.customPiecePlacePositions = customPiecePlacePositions;
        clearPieces();
        toLayout();
    }

    public OnMenuListener getOnMenuListener() {
        return onMenuListener;
    }

    /**
     * Set the @OnMenuListener of BMB.
     *
     * @param onMenuListener OnMenuListener
     */
    public void setOnMenuListener(OnMenuListener onMenuListener) {
        this.onMenuListener = onMenuListener;
    }

    public int getDimColor() {
        return dimColor;
    }

    /**
     * Set the dim-color of the background when the BMB PopUps.
     *
     * @param dimColor dim-color of background
     */
    public void setDimColor(int dimColor) {
        if (this.dimColor == dimColor) return;
        this.dimColor = dimColor;
        if (popUpStateEnum == MenuStateEnum.DidPopUp && background != null) background.setBackgroundColor(dimColor);
    }

    public void setDelay(long delay) {
        setShowDelay(delay);
        setHideDelay(delay);
    }

    public void setDuration(long duration) {
        setShowDuration(duration);
        setHideDuration(duration);
    }

    public long getShowDelay() {
        return showDelay;
    }

    /**
     * Delay of every PopUp-button.
     *
     * @param showDelay delay of every PopUp-button
     */
    public void setShowDelay(long showDelay) {
        this.showDelay = showDelay;
        setShareLinesViewData();
    }

    public long getShowDuration() {
        return showDuration;
    }

    /**
     * Set the duration of every PopUp-button when PopUpping.
     * Notice that this is not the total duration of the PopUp animation.
     * For example, if there are 5 PopUp-buttons, delay of PopUp animation is 100ms
     * and duration of every PopUp animation is 1000ms. Then the total duration of
     * all PopUp animations is 1000ms + 4 * 100ms = 1400ms.
     *
     * @param showDuration duration of every PopUp-button when PopUpping
     */
    public void setShowDuration(long showDuration) {
        if (this.showDuration == showDuration) return;
        this.showDuration = Math.max(1, showDuration);
        setShareLinesViewData();
    }

    public long getHideDelay() {
        return hideDelay;
    }

    /**
     * Delay of every PopUp-button.
     *
     * @param hideDelay delay of every PopUp-button
     */
    public void setHideDelay(long hideDelay) {
        this.hideDelay = hideDelay;
        setShareLinesViewData();
    }

    public long getHideDuration() {
        return hideDuration;
    }
    
    /**
     * Set the duration of every PopUp-button when re-PopUpping.
     * Notice that this is not the total duration of the re-PopUp animation.
     * For example, if there are 5 PopUp-buttons, delay of re-PopUp animation is 100ms
     * and duration of every re-PopUp animation is 1000ms. Then the total duration of
     * all re-PopUp animations is 1000ms + 4 * 100ms = 1400ms.
     *
     * @param hideDuration duration of every PopUp-button when PopUpping
     */
    public void setHideDuration(long hideDuration) {
        if (this.hideDuration == hideDuration) return;
        this.hideDuration = Math.max(1, hideDuration);
        setShareLinesViewData();
    }

    public boolean isCancelable() {
        return cancelable;
    }

    /**
     * Whether the BMB is cancelable. If the BMB is cancelable, then when after the PopUp
     * animation of PopUpping, you can re-PopUp BMB by clicking the background.
     *
     * @param cancelable whether the BMB is cancelable
     */
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean isAutoHide() {
        return autoHide;
    }

    /**
     * Whether the BMB is auto-hide. If the BMB is auto-hide, then when after the PopUp
     * animation of PopUpping, you can click one of the PopUp-buttons to re-PopUp BMB.
     *
     * @param autoHide whether the BMB is auto-hide
     */
    public void setAutoHide(boolean autoHide) {
        this.autoHide = autoHide;
    }

    public OrderEnum getOrderEnum() {
        return orderEnum;
    }

    /**
     * Set the order-enum for BMB.
     *
     * @param orderEnum order-enum
     */
    public void setOrderEnum(OrderEnum orderEnum) {
        this.orderEnum = orderEnum;
    }

    public int getFrames() {
        return frames;
    }

    /**
     * Set the animation-frames for every PopUp-button animation. The higher, the animations
     * more continue.
     *
     * @param frames frames for every PopUp-button animation
     */
    public void setFrames(int frames) {
        this.frames = frames;
    }

    public MenuEnum getMenuEnum() {
        return menuEnum;
    }

    /**
     * Set the PopUp-enum for BMB.
     *
     * @param menuEnum PopUp-enum
     */
    public void setMenuEnum(MenuEnum menuEnum) {
        this.menuEnum = menuEnum;
    }

    public void setShowEaseEnum(EaseEnum showEaseEnum) {
        setShowMoveEaseEnum(showEaseEnum);
        setShowScaleEaseEnum(showEaseEnum);
        setShowRotateEaseEnum(showEaseEnum);
    }

    public EaseEnum getShowMoveEaseEnum() {
        return showMoveEaseEnum;
    }

    /**
     * Set the ease type of movement when every PopUp-button is PopUpping.
     *
     * @param showMoveEaseEnum ease type of movement when PopUpping
     */
    public void setShowMoveEaseEnum(EaseEnum showMoveEaseEnum) {
        this.showMoveEaseEnum = showMoveEaseEnum;
    }

    public EaseEnum getShowScaleEaseEnum() {
        return showScaleEaseEnum;
    }

    /**
     * Set the ease type of scale-animation when every PopUp-button is PopUpping.
     *
     * @param showScaleEaseEnum ease type of scale-animation when PopUpping
     */
    public void setShowScaleEaseEnum(EaseEnum showScaleEaseEnum) {
        this.showScaleEaseEnum = showScaleEaseEnum;
    }

    public EaseEnum getShowRotateEaseEnum() {
        return showRotateEaseEnum;
    }

    /**
     * Set the ease type of rotate-animation when every PopUp-button is PopUpping.
     *
     * @param showRotateEaseEnum ease type of rotate-animation when PopUpping
     */
    public void setShowRotateEaseEnum(EaseEnum showRotateEaseEnum) {
        this.showRotateEaseEnum = showRotateEaseEnum;
    }

    public void setHideEaseEnum(EaseEnum hideEaseEnum) {
        setHideMoveEaseEnum(hideEaseEnum);
        setHideScaleEaseEnum(hideEaseEnum);
        setHideRotateEaseEnum(hideEaseEnum);
    }

    public EaseEnum getHideMoveEaseEnum() {
        return hideMoveEaseEnum;
    }

    /**
     * Set the ease type of movement when every PopUp-button is re-PopUpping.
     *
     * @param hideMoveEaseEnum ease type of movement when re-PopUpping
     */
    public void setHideMoveEaseEnum(EaseEnum hideMoveEaseEnum) {
        this.hideMoveEaseEnum = hideMoveEaseEnum;
    }

    public EaseEnum getHideScaleEaseEnum() {
        return hideScaleEaseEnum;
    }

    /**
     * Set the ease type of scale-animation when every PopUp-button is re-PopUpping.
     *
     * @param hideScaleEaseEnum ease type of scale-animation when re-PopUpping
     */
    public void setHideScaleEaseEnum(EaseEnum hideScaleEaseEnum) {
        this.hideScaleEaseEnum = hideScaleEaseEnum;
    }

    public EaseEnum getHideRotateEaseEnum() {
        return hideRotateEaseEnum;
    }

    /**
     * Set the ease type of rotate-animation when every PopUp-button is re-PopUpping.
     *
     * @param hideRotateEaseEnum ease type of rotate-animation when re-PopUpping
     */
    public void setHideRotateEaseEnum(EaseEnum hideRotateEaseEnum) {
        this.hideRotateEaseEnum = hideRotateEaseEnum;
    }

    public int getRotateDegree() {
        return rotateDegree;
    }

    /**
     * Whether there are 3d animations for PopUp-buttons.
     *
     * @param use3DTransformAnimation Whether there are 3d animations for PopUp-buttons.
     */
    public void setUse3DTransformAnimation(boolean use3DTransformAnimation) {
        this.use3DTransformAnimation = use3DTransformAnimation;
    }

    public boolean isUse3DTransformAnimation() {
        return use3DTransformAnimation;
    }

    /**
     * Whether BMB will PopUp automatically when it's shown.
     * This property can be useful if the BMB is supposed to PopUp when its activity appears.
     *
     * @param autoPopUp true/false
     */
    public void setAutoPopUp(boolean autoPopUp) {
        this.autoPopUp = autoPopUp;
    }

    public boolean isAutoPopUp() {
        return autoPopUp;
    }

    /**
     * Whether BMB will PopUp automatically without animations when it's shown.
     * This property can be useful if the BMB is supposed to PopUp when its activity appears.
     *
     * @param autoPopUpImmediately true/false
     */
    public void setAutoPopUpImmediately(boolean autoPopUpImmediately) {
        this.autoPopUpImmediately = autoPopUpImmediately;
    }

    public boolean isAutoPopUpImmediately() {
        return autoPopUpImmediately;
    }

    /**
     * Set the rotate degree of rotate-animation of every PopUp-button.
     *
     * @param rotateDegree rotate degree
     */
    public void setRotateDegree(int rotateDegree) {
        this.rotateDegree = rotateDegree;
    }

    public ButtonPlaceEnum getButtonPlaceEnum() {
        return buttonPlaceEnum;
    }

    /**
     * Set the button-place-enum.
     *
     * @param buttonPlaceEnum button-place-enum
     */
    public void setButtonPlaceEnum(ButtonPlaceEnum buttonPlaceEnum) {
        this.buttonPlaceEnum = buttonPlaceEnum;
        clearButtons();
        needToCalculateStartPositions = true;
    }

    public ArrayList<PointF> getCustomButtonPlacePositions() {
        return customButtonPlacePositions;
    }

    public void setCustomButtonPlacePositions(ArrayList<PointF> customButtonPlacePositions) {
        this.customButtonPlacePositions = customButtonPlacePositions;
        clearButtons();
        needToCalculateStartPositions = true;
    }

    public ButtonPlaceAlignmentEnum getButtonPlaceAlignmentEnum() {
        return buttonPlaceAlignmentEnum;
    }

    /**
     * Set the button-place-alignment-enum.
     *
     * @param buttonPlaceAlignmentEnum button-place-alignment-enum
     */
    public void setButtonPlaceAlignmentEnum(ButtonPlaceAlignmentEnum buttonPlaceAlignmentEnum) {
        this.buttonPlaceAlignmentEnum = buttonPlaceAlignmentEnum;
    }

    public float getButtonHorizontalMargin() {
        return buttonHorizontalMargin;
    }

    /**
     * Set the horizontal-margin between buttons.
     *
     * @param buttonHorizontalMargin horizontal-margin
     */
    public void setButtonHorizontalMargin(float buttonHorizontalMargin) {
        this.buttonHorizontalMargin = buttonHorizontalMargin;
    }

    public float getButtonVerticalMargin() {
        return buttonVerticalMargin;
    }

    /**
     * Set the vertical-margin between buttons.
     *
     * @param buttonVerticalMargin vertical-margin
     */
    public void setButtonVerticalMargin(float buttonVerticalMargin) {
        this.buttonVerticalMargin = buttonVerticalMargin;
    }

    public float getButtonInclinedMargin() {
        return buttonInclinedMargin;
    }

    /**
     * Set the inclined-margin between buttons.
     *
     * @param buttonInclinedMargin Inclined-margin
     */
    public void setButtonInclinedMargin(float buttonInclinedMargin) {
        this.buttonInclinedMargin = buttonInclinedMargin;
    }

    public float getButtonTopMargin() {
        return buttonTopMargin;
    }

    /**
     * Set the top-alignment margin between screen and buttons.
     *
     * @param buttonTopMargin Top-alignment margin
     */
    public void setButtonTopMargin(float buttonTopMargin) {
        this.buttonTopMargin = buttonTopMargin;
    }

    public float getButtonBottomMargin() {
        return buttonBottomMargin;
    }

    /**
     * Set the bottom-alignment margin between screen and buttons.
     *
     * @param buttonBottomMargin Bottom-alignment margin
     */
    public void setButtonBottomMargin(float buttonBottomMargin) {
        this.buttonBottomMargin = buttonBottomMargin;
    }

    public float getButtonLeftMargin() {
        return buttonLeftMargin;
    }

    /**
     * Set the left-alignment margin between screen and buttons.
     *
     * @param buttonLeftMargin Left-alignment margin
     */
    public void setButtonLeftMargin(float buttonLeftMargin) {
        this.buttonLeftMargin = buttonLeftMargin;
    }

    public float getButtonRightMargin() {
        return buttonRightMargin;
    }

    /**
     * Set the right-alignment margin between screen and buttons.
     *
     * @param buttonRightMargin Right-alignment margin
     */
    public void setButtonRightMargin(float buttonRightMargin) {
        this.buttonRightMargin = buttonRightMargin;
    }

    public float getBottomHamButtonTopMargin() {
        return bottomHamButtonTopMargin;
    }

    /**
     * Set the top-margin of bottom ham-PopUp-button. This method is used when the bottom
     * ham-PopUp-button has different meaning compared with others. For example, the bottom
     * ham-PopUp-button maybe used as a "cancel" selection.
     *
     * @param bottomHamButtonTopMargin top-margin of bottom ham-PopUp-button
     */
    public void setBottomHamButtonTopMargin(float bottomHamButtonTopMargin) {
        this.bottomHamButtonTopMargin = bottomHamButtonTopMargin;
    }

    public boolean isOrientationAdaptable() {
        return isOrientationAdaptable;
    }

    /**
     * Usually, you don't need to worry about the positions of sub-buttons of BMB when the screen
     * orientation is changed. Because the activity will be destroyed and re-created. But if the
     * "configChanges" property of activity is set, then you need to set orientation-adaptable to
     * true, then BMB will change the positions of sub-buttons when orientation of screen is
     * changed.
     *
     * @param orientationAdaptable orientationAdaptable
     */
    public void setOrientationAdaptable(boolean orientationAdaptable) {
        isOrientationAdaptable = orientationAdaptable;
        if (isOrientationAdaptable) {
            initOrientationListener();
        }
    }

    //endregion
}
