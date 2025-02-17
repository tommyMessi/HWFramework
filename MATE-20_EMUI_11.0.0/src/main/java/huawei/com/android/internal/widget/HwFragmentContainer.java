package huawei.com.android.internal.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class HwFragmentContainer {
    protected static final int ANIMATION_DURATION = 300;
    private static final int ANIMATION_TYPE_LEFT_IN = 2;
    private static final int ANIMATION_TYPE_LEFT_OUT = 1;
    private static final int ANIMATION_TYPE_RIGHT_IN = 0;
    private static final int ANIMATION_TYPE_RIGHT_OUT = 3;
    protected static final float BLUR_LAYER_FORTY_PERCENT = 0.4f;
    protected static final float BLUR_LAYER_TRANSPARENT = 0.0f;
    public static final int COLUMN_NUMBER_ONE = 1;
    public static final int COLUMN_NUMBER_TWO = 2;
    public static final int CONTAINER_BOTH = 2;
    public static final int CONTAINER_LEFT = 0;
    public static final int CONTAINER_RIGHT = 1;
    public static final float DISPLAY_RATE_FIFTY_PERCENT = 0.5f;
    public static final float DISPLAY_RATE_FORTY_PERCENT = 0.4f;
    public static final float DISPLAY_RATE_SIXTY_PERCENT = 0.6f;
    private static final float FLOAT_COMPARE_VALUE = 0.001f;
    protected static final int FRAGMENT_BACKGROUND_COLORCOLOR = -197380;
    private static final int FRAGMENT_LAYOUT_ID = 655360;
    public static final int FRAGMENT_LEVEL_1 = 1;
    public static final int FRAGMENT_LEVEL_2 = 2;
    public static final int FRAGMENT_LEVEL_3 = 3;
    public static final int FRAGMENT_LEVEL_NONE = 0;
    private static final float HALF_WIDTH = 2.0f;
    private static final int ILLEGAL_VALUE = -1;
    protected static final PathInterpolator INTERPOLATOR_20_90 = new PathInterpolator(0.3f, 0.15f, 0.1f, 0.85f);
    protected static final PathInterpolator INTERPOLATOR_33_33 = new PathInterpolator(0.2f, 0.5f, 0.8f, 0.5f);
    private static final boolean IS_DEBUG = false;
    private static final int MAX_STACK_COUNT = 2;
    public static final int SPLITE_MODE_ALL_SEPARATE = 3;
    public static final int SPLITE_MODE_DEFAULT_SEPARATE = 0;
    public static final int SPLITE_MODE_LAND_SEPARATE = 2;
    public static final int SPLITE_MODE_NONE_SEPARATE = 1;
    private static final String TAG = "FragmentContainer";
    private static final String TAG_LEFT_CONTAINER = "left_container";
    private static final String TAG_RIGHT_CONTAINER = "right_container";
    public static final int TRANSITION_FADE = 1;
    public static final int TRANSITION_SLIDE_HORIZONTAL = 2;
    private static final String TRANSLATION_X = "translationX";
    private static final int TRANSLATION_Z_VALUE = -1;
    private int mAnimatorWidth;
    private ObjectAnimator mBlurLayerAnimator;
    private HwFragmentLayout mFragmentLayout;
    private FragmentManager mFragmentManager;
    private boolean mIsFragmentAnimEmpty;
    private boolean mIsPopMultiple;
    private int mPopCount;

    public HwFragmentContainer(Context context, FragmentManager fragmentManager) {
        this(context, 0.4f, fragmentManager);
    }

    public HwFragmentContainer(Context context, float displayRate, FragmentManager fragmentManager) {
        this(context, displayRate, false, fragmentManager);
    }

    public HwFragmentContainer(Context context, float displayRate, boolean canMove, FragmentManager fragmentManager) {
        this.mAnimatorWidth = 0;
        this.mIsPopMultiple = false;
        this.mPopCount = 0;
        this.mIsFragmentAnimEmpty = false;
        this.mFragmentLayout = new HwFragmentLayout(context, displayRate, canMove);
        if (this.mFragmentLayout.getId() == -1) {
            this.mFragmentLayout.setId(FRAGMENT_LAYOUT_ID);
        }
        this.mFragmentManager = fragmentManager;
    }

    public HwFragmentContainer(Context context, HwFragmentLayout fragmentLayout, FragmentManager fragmentManager) {
        this.mAnimatorWidth = 0;
        this.mIsPopMultiple = false;
        this.mPopCount = 0;
        this.mIsFragmentAnimEmpty = false;
        this.mFragmentLayout = fragmentLayout;
        this.mFragmentManager = fragmentManager;
    }

    public View getFragmentLayout() {
        return this.mFragmentLayout.getFragmentLayout();
    }

    public FrameLayout getLeftLayout() {
        return this.mFragmentLayout.getLeftLayout();
    }

    public FrameLayout getRightLayout() {
        return this.mFragmentLayout.getRightLayout();
    }

    public ImageView getSplitLine() {
        return this.mFragmentLayout.getSplitLine();
    }

    public ImageView getSplitBtn() {
        return this.mFragmentLayout.getSplitBtn();
    }

    private int getLeftContentId() {
        return this.mFragmentLayout.getLeftContentID();
    }

    private int getRightContentId() {
        return this.mFragmentLayout.getRightContentID();
    }

    public ImageView getLeftBlurLayer() {
        return this.mFragmentLayout.getLeftBlurLayer();
    }

    public ImageView getRightBlurLayer() {
        return this.mFragmentLayout.getRightBlurLayer();
    }

    public void setDisplayRate(float displayRate) {
        this.mFragmentLayout.setDisplayRate(displayRate);
    }

    public void setCanMove(boolean canMove) {
        this.mFragmentLayout.setCanMove(canMove);
    }

    public void setSplitMode(int splitMode) {
        this.mFragmentLayout.setSplitMode(splitMode);
    }

    public int getColumnsNumber() {
        return this.mFragmentLayout.calculateColumnsNumber();
    }

    public int getColumnsNumber(int orientation, int appWidth) {
        return this.mFragmentLayout.calculateColumnsNumber(orientation, appWidth);
    }

    public void setSeparateDeviceSize(double landSeparateSize, double portSeparateSize) {
        this.mFragmentLayout.setSeparateDeviceSize(landSeparateSize, portSeparateSize);
    }

    public void openLeftClearStack(Fragment fragment) {
        if (fragment != null) {
            setSelectedAndAnimation(0);
            popPrepare();
            this.mFragmentManager.popBackStackImmediate(TAG_LEFT_CONTAINER, 1);
            popFinish();
            FragmentTransaction transaction = this.mFragmentManager.beginTransaction();
            transaction.setTransition(4097);
            transaction.replace(getLeftContentId(), fragment, fragment.getClass().getName());
            transaction.addToBackStack(TAG_LEFT_CONTAINER);
            transaction.commitAllowingStateLoss();
            refreshFragmentLayout();
        }
    }

    public void openRightClearStack(Fragment fragment) {
        openRightClearStack(fragment, false);
    }

    private void openRightClearStack(Fragment fragment, boolean isInitial) {
        if (fragment != null) {
            setSelectedAndAnimation(!isInitial ? 1 : 0);
            popPrepare();
            this.mFragmentManager.popBackStackImmediate(TAG_LEFT_CONTAINER, 0);
            popFinish();
            FragmentTransaction transaction = this.mFragmentManager.beginTransaction();
            transaction.setTransition(4097);
            transaction.replace(getRightContentId(), fragment, fragment.getClass().getName());
            transaction.addToBackStack(TAG_RIGHT_CONTAINER);
            transaction.commitAllowingStateLoss();
            this.mFragmentManager.executePendingTransactions();
            if (!isInitial) {
                refreshFragmentLayout();
            }
        }
    }

    public void initRightContainer(Fragment fragment) {
        openRightClearStack(fragment, true);
    }

    public void changeRightAddToStack(Fragment nextFragment, Fragment currentFragment) {
        if (nextFragment != null) {
            setSelectedAndAnimation(1);
            FragmentTransaction transaction = this.mFragmentManager.beginTransaction();
            transaction.setTransition(4097);
            if (currentFragment == null) {
                transaction.replace(getRightContentId(), nextFragment, nextFragment.getClass().getName());
            } else if (!nextFragment.isAdded()) {
                transaction.hide(currentFragment);
                transaction.add(getRightContentId(), nextFragment, nextFragment.getClass().getName());
            } else {
                transaction.hide(currentFragment);
                transaction.show(nextFragment);
            }
            transaction.addToBackStack(TAG_RIGHT_CONTAINER);
            transaction.commitAllowingStateLoss();
            this.mFragmentManager.executePendingTransactions();
            refreshFragmentLayout();
        }
    }

    public void setSelectedContainer(int selectedContainer) {
        this.mFragmentLayout.setSelectedContainer(selectedContainer);
    }

    private void setSelectedAndAnimation(int selectedContainer) {
        if (getColumnsNumber() != 1 || selectedContainer == getSelectedContainer()) {
            this.mIsFragmentAnimEmpty = false;
        } else {
            this.mIsFragmentAnimEmpty = true;
        }
        setSelectedContainer(selectedContainer);
    }

    public int getSelectedContainer() {
        return this.mFragmentLayout.getSelectedContainer();
    }

    public void refreshFragmentLayout() {
        this.mFragmentLayout.refreshFragmentLayout();
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x0041: APUT  
      (r0v1 'stackCounts' int[] A[D('stackCounts' int[])])
      (0 ??[int, short, byte, char])
      (r1v1 'leftStackCount' int A[D('leftStackCount' int)])
     */
    public int[] getLeftRightBackStackCount() {
        int[] stackCounts = new int[2];
        int leftStackCount = 0;
        int rightStackCount = 0;
        int backStackCount = this.mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            FragmentManager.BackStackEntry backStack = this.mFragmentManager.getBackStackEntryAt(i);
            if (backStack.getName() != null && backStack.getName().indexOf(TAG_LEFT_CONTAINER) >= 0) {
                leftStackCount++;
            } else if (backStack.getName() != null && backStack.getName().indexOf(TAG_RIGHT_CONTAINER) >= 0) {
                rightStackCount++;
            }
        }
        stackCounts[0] = leftStackCount;
        stackCounts[1] = rightStackCount;
        return stackCounts;
    }

    public boolean isBackPressed() {
        int rightStackCount = getLeftRightBackStackCount()[1];
        int columnNumber = getColumnsNumber();
        if (rightStackCount > 1) {
            this.mFragmentManager.popBackStackImmediate();
            this.mFragmentLayout.setSelectedContainer(1);
            return false;
        }
        if (columnNumber == 1) {
            if (rightStackCount != 1 || getRightLayout().getVisibility() != 0) {
                return true;
            }
            this.mFragmentLayout.setSelectedContainer(0);
            refreshFragmentLayout();
            return false;
        } else if (columnNumber == 2 && rightStackCount == 1) {
            return true;
        }
        return true;
    }

    public void setSelectContainerByTouch(boolean isEnabled) {
        this.mFragmentLayout.setSelectContainerByTouch(isEnabled);
    }

    public void logBackStacksInfo() {
        int leftStackCount = 0;
        int rightStackCount = 0;
        int rightOpenStackCount = 0;
        int backStackCount = this.mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            FragmentManager.BackStackEntry backStack = this.mFragmentManager.getBackStackEntryAt(i);
            if (backStack.getName() != null && backStack.getName().indexOf(TAG_LEFT_CONTAINER) >= 0) {
                leftStackCount++;
            } else if (backStack.getName() != null && backStack.getName().indexOf(TAG_RIGHT_CONTAINER) >= 0) {
                rightStackCount++;
            }
            if (TAG_RIGHT_CONTAINER.equals(backStack.getName())) {
                rightOpenStackCount++;
            }
            Log.d(TAG, "ID = " + backStack.getId() + ", name = " + backStack.getName());
        }
        Log.d(TAG, "BackStackCount = " + backStackCount + ", leftStackCount = " + leftStackCount + ", rightStackCount = " + rightStackCount + ", rightOpenStackCount = " + rightOpenStackCount);
    }

    private void popPrepare() {
        this.mIsPopMultiple = true;
        this.mPopCount = 0;
    }

    private void popFinish() {
        this.mIsPopMultiple = false;
        this.mPopCount = 0;
    }

    public Animator getAnimator(View view, int transit, boolean isEntered) {
        this.mFragmentLayout.displayAnimation();
        if (view == null || this.mIsFragmentAnimEmpty || getSelectedContainer() != 1) {
            return null;
        }
        int width = view.getWidth();
        this.mAnimatorWidth = width != 0 ? width : this.mAnimatorWidth;
        if (this.mAnimatorWidth == 0) {
            return null;
        }
        AnimatorSet animatorSet = null;
        if (transit == 4097) {
            if (isEntered) {
                return createAnimator(view, 0);
            }
            return createAnimator(view, 1);
        } else if (transit != 8194) {
            return null;
        } else {
            if (this.mIsPopMultiple) {
                if (view.getVisibility() == 0 && this.mPopCount == 0) {
                    animatorSet = createAnimator(view, 1);
                }
                this.mPopCount++;
                return animatorSet;
            } else if (isEntered) {
                return createAnimator(view, 2);
            } else {
                return createAnimator(view, 3);
            }
        }
    }

    private AnimatorSet createAnimator(View view, int type) {
        if (type == 0) {
            view.setTranslationZ(1.0f);
            ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(view, TRANSLATION_X, (float) this.mAnimatorWidth, 0.0f);
            translateAnimator.setInterpolator(INTERPOLATOR_20_90);
            translateAnimator.setDuration(300L);
            TranslateAnimatorListener animatorListener = new TranslateAnimatorListener();
            animatorListener.setView(view);
            translateAnimator.addListener(animatorListener);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(translateAnimator);
            return animatorSet;
        } else if (type == 1) {
            view.setTranslationZ(-1.0f);
            ObjectAnimator translateAnimator2 = ObjectAnimator.ofFloat(view, TRANSLATION_X, 0.0f, ((float) (-this.mAnimatorWidth)) / 2.0f);
            translateAnimator2.setInterpolator(INTERPOLATOR_33_33);
            translateAnimator2.setDuration(300L);
            createBlurLayerAnimator(view, 0.0f, 0.4f);
            AnimatorSet animatorSet2 = new AnimatorSet();
            animatorSet2.playTogether(translateAnimator2, this.mBlurLayerAnimator);
            return animatorSet2;
        } else if (type == 2) {
            view.setTranslationZ(-1.0f);
            ObjectAnimator translateAnimator3 = ObjectAnimator.ofFloat(view, TRANSLATION_X, ((float) (-this.mAnimatorWidth)) / 2.0f, 0.0f);
            translateAnimator3.setInterpolator(INTERPOLATOR_20_90);
            translateAnimator3.setDuration(300L);
            createBlurLayerAnimator(view, 0.4f, 0.0f);
            AnimatorSet animatorSet3 = new AnimatorSet();
            animatorSet3.playTogether(translateAnimator3, this.mBlurLayerAnimator);
            return animatorSet3;
        } else if (type != 3) {
            return null;
        } else {
            view.setTranslationZ(1.0f);
            ObjectAnimator translateAnimator4 = ObjectAnimator.ofFloat(view, TRANSLATION_X, 0.0f, (float) this.mAnimatorWidth);
            translateAnimator4.setInterpolator(INTERPOLATOR_20_90);
            translateAnimator4.setDuration(300L);
            TranslateAnimatorListener animatorListener2 = new TranslateAnimatorListener();
            animatorListener2.setView(view);
            translateAnimator4.addListener(animatorListener2);
            AnimatorSet animatorSet4 = new AnimatorSet();
            animatorSet4.play(translateAnimator4);
            return animatorSet4;
        }
    }

    /* access modifiers changed from: private */
    public static class TranslateAnimatorListener implements Animator.AnimatorListener {
        boolean hasSetBackground;
        private View mView;

        private TranslateAnimatorListener() {
            this.hasSetBackground = false;
            this.mView = null;
        }

        /* access modifiers changed from: protected */
        public void setView(View view) {
            this.mView = view;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animation) {
            View view = this.mView;
            if (view != null && view.getBackground() == null) {
                this.hasSetBackground = true;
                this.mView.setBackgroundColor(HwFragmentContainer.FRAGMENT_BACKGROUND_COLORCOLOR);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animation) {
            View view = this.mView;
            if (view != null && this.hasSetBackground) {
                view.setBackground(null);
                this.mView = null;
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animation) {
            View view = this.mView;
            if (view != null && this.hasSetBackground) {
                view.setBackground(null);
                this.mView = null;
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animation) {
        }
    }

    private void createBlurLayerAnimator(View view, float startAlpha, final float endAlpha) {
        ObjectAnimator objectAnimator = this.mBlurLayerAnimator;
        if (objectAnimator != null) {
            objectAnimator.removeAllUpdateListeners();
        }
        this.mBlurLayerAnimator = ObjectAnimator.ofFloat(view, "blurlayer", startAlpha, endAlpha);
        this.mBlurLayerAnimator.setInterpolator(INTERPOLATOR_20_90);
        this.mBlurLayerAnimator.setDuration(300L);
        this.mFragmentLayout.getRightBlurLayer().setTranslationZ(0.0f);
        this.mFragmentLayout.getRightBlurLayer().setVisibility(0);
        this.mFragmentLayout.getRightBlurLayer().setLayerType(2, null);
        this.mBlurLayerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            /* class huawei.com.android.internal.widget.HwFragmentContainer.AnonymousClass1 */

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation != null) {
                    Object animatedValue = animation.getAnimatedValue();
                    if (animatedValue instanceof Float) {
                        float alphaValue = ((Float) animatedValue).floatValue();
                        animation.getCurrentPlayTime();
                        HwFragmentContainer.this.mFragmentLayout.getRightBlurLayer().setAlpha(alphaValue);
                        if (Math.abs(endAlpha - alphaValue) < 0.001f) {
                            HwFragmentContainer.this.mFragmentLayout.getRightBlurLayer().setVisibility(8);
                            HwFragmentContainer.this.mFragmentLayout.getRightBlurLayer().setLayerType(0, null);
                        }
                    }
                }
            }
        });
    }
}
