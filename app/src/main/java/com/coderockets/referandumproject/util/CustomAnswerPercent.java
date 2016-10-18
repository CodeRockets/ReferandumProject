package com.coderockets.referandumproject.util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.model.ModelFriend;
import com.coderockets.referandumproject.util.adapter.FriendAnswerListAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by aykutasil on 18.08.2016.
 */
public class CustomAnswerPercent extends View {

    private static final String TAG = CustomAnswerPercent.class.getSimpleName();
    Context mContext;
    Activity mActivity;
    Fragment mFragment;
    private static Canvas mCanvas;
    boolean ButtonClick = false;
    boolean isFinishAnimBarA = false;
    boolean isFinishAnimBarB = false;
    View mAlphaView = null;
    Paint mPaintBarA;
    Paint mPaintBarB;
    RectF mRectBarA;
    RectF mRectBarB;
    int mColorBarA;
    int mColorBarB;

    Paint mPaint;
    int mValueBarA;
    int mValueBarB;
    int mValueBarS;

    float animAValue;
    float animBValue;
    long animBarDuration;
    long animAlphaViewDuration;
    float alphaViewValue;

    int widthBarA;
    int widthBarB;

    List<ModelFriend> mListFriendsAnswer;
    final int FRIEND_COUNT = 3;
    int mFriendAnswerViewSize = 100;

    private enum ANOTHER_ICON_POSITION {
        LEFT,
        RIGHT
    }

//    Bundle stateBundle;
//
//    @DebugLog
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState savedState = new SavedState(superState);
//        savedState.bundle = stateBundle;
//        return savedState;
//    }
//
//    @DebugLog
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        SavedState ss = (SavedState) state;
//        super.onRestoreInstanceState(ss.getSuperState());
//        setStateBundle(ss.bundle);
//    }
//
//    static class SavedState extends BaseSavedState {
//
//        Bundle bundle;
//
//        SavedState(Parcelable parcelable) {
//            super(parcelable);
//        }
//
//        SavedState(Parcel source) {
//            super(source);
//            bundle = source.readBundle(getClass().getClassLoader());
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            super.writeToParcel(out, flags);
//            out.writeBundle(bundle);
//        }
//
//        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
//            @Override
//            public SavedState createFromParcel(Parcel source) {
//                return new SavedState(source);
//            }
//
//            @Override
//            public SavedState[] newArray(int size) {
//                return new SavedState[0];
//            }
//        };
//    }

    @DebugLog
    public CustomAnswerPercent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @DebugLog
    public void setStateBundle(Bundle bundle) {
        Logger.i("TRY STATE BUNDLE" + bundle.getString("STATE_FRIEND_NAME"));
    }

    @DebugLog
    private void init(Context context, AttributeSet attrs) {

        // Custom View lerde state kayıt etmek için manuel olarak aktif hale getirilmelilir. -> setSaveEnabled(true)
        //setSaveEnabled(true);

        this.mContext = context;
        //stateBundle = new Bundle();

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomAnswerPercent, 0, 0);
        this.widthBarA = ta.getInt(R.styleable.CustomAnswerPercent_barAWidth, 50);
        this.widthBarB = ta.getInt(R.styleable.CustomAnswerPercent_barBWidth, 50);
        this.mValueBarA = ta.getInt(R.styleable.CustomAnswerPercent_barAValue, 0);
        this.mValueBarB = ta.getInt(R.styleable.CustomAnswerPercent_barBValue, 0);
        this.mValueBarS = ta.getInt(R.styleable.CustomAnswerPercent_barSValue, 0);
        this.mColorBarA = ta.getColor(R.styleable.CustomAnswerPercent_barAColor, Color.RED);
        this.mColorBarB = ta.getColor(R.styleable.CustomAnswerPercent_barBColor, Color.BLUE);
        this.animBarDuration = ta.getInt(R.styleable.CustomAnswerPercent_animBarDuration, 500);
        this.animAlphaViewDuration = ta.getInt(R.styleable.CustomAnswerPercent_animAlphaViewDuration, 300);
        this.alphaViewValue = ta.getFloat(R.styleable.CustomAnswerPercent_alphaViewValue, 0.3f);
        ta.recycle();
    }

    public void addAlphaView(View hostView) {
        this.mAlphaView = hostView;
    }

    public void addActivity(Activity activity) {
        this.mActivity = activity;
    }

    public void addFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    public void setAValue(int val) {
        this.mValueBarA = val;
    }

    public void setWidthBarA(int val) {
        this.widthBarA = val;
    }

    public void setWidthBarB(int val) {
        this.widthBarB = val;
    }

    public void setBValue(int val) {
        this.mValueBarB = val;
    }

    public void setSValue(int val) {
        this.mValueBarS = val;
    }

    public void setAnimBarDuration(long duration) {
        this.animBarDuration = duration;
    }

    public void setAnimAlphaViewDuration(long duration) {
        this.animAlphaViewDuration = duration;
    }

    public void setAlphaViewValue(float val) {
        this.alphaViewValue = val;
    }

    public void setFriendAnswer(List<ModelFriend> friends) {
        this.mListFriendsAnswer = friends;

//        if (mListFriendsAnswer.size() > 0) {
//            ModelFriend fr = mListFriendsAnswer.get(0);
//            Logger.i("ModelFriend -> setFriendAnswer: " + fr.getName());
//            Logger.i("ModelFriend -> setFriendAnswer: " + fr.getOption());
//
//            mListFriendsAnswer.add(fr);
//            mListFriendsAnswer.add(fr);
//            mListFriendsAnswer.add(fr);
//            mListFriendsAnswer.add(fr);
//            mListFriendsAnswer.add(fr);
//
//            ModelFriend friend = new ModelFriend();
//            friend.setName("Osman");
//            friend.setOption("a");
//            friend.setFacebookId("345345345");
//            friend.setProfileImage("https://yt3.ggpht.com/-wh6msZtAzCU/AAAAAAAAAAI/AAAAAAAAAAA/ERwt1WSIOUA/s900-c-k-no-rj-c0xffffff/photo.jpg");
//            mListFriendsAnswer.add(friend);
//
//            friend = new ModelFriend();
//            friend.setName("Osmannn");
//            friend.setOption("b");
//            friend.setFacebookId("345345345");
//            friend.setProfileImage("https://avatars3.githubusercontent.com/u/3179872?v=3&s=400");
//            mListFriendsAnswer.add(friend);
//
//            friend = new ModelFriend();
//            friend.setName("Kerem");
//            friend.setOption("b");
//            friend.setFacebookId("34534345345");
//            friend.setProfileImage("https://yt3.ggpht.com/-wh6msZtAzCU/AAAAAAAAAAI/AAAAAAAAAAA/ERwt1WSIOUA/s900-c-k-no-rj-c0xffffff/photo.jpg");
//            mListFriendsAnswer.add(friend);
//
//            /*
//            friend = new ModelFriend();
//            friend.setName("Aykut Kerem");
//            friend.setOption("b");
//            friend.setFacebookId("34534343435345");
//            friend.setProfileImage("https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAZCAAAAJGM5ODIyMDI2LTVlMzctNDNhMC1hOWQzLTJlNThhYzBlOTczZA.jpg");
//            mListFriendsAnswer.add(friend);
//*/
//
//            for (ModelFriend fri : mListFriendsAnswer) {
//                Logger.i("ModelFriend options: " + fri.getOption());
//            }
//
//        }
    }

    public void setFriendAnswerViewSize(int size) {
        this.mFriendAnswerViewSize = size;
    }

    @DebugLog
    public void showResult() throws Exception {

        ButtonClick = true;
        isFinishAnimBarA = false;
        isFinishAnimBarB = false;

        if (mAlphaView == null) {
            Log.e(TAG, "alphaView is null");
            startBar();
        } else {
            ValueAnimator alphaViewValueAnimator = ValueAnimator.ofFloat(1f, alphaViewValue);
            alphaViewValueAnimator.setDuration(animAlphaViewDuration);
            alphaViewValueAnimator.addUpdateListener(animator ->
            {
                mAlphaView.setAlpha((float) animator.getAnimatedValue());
                if ((float) animator.getAnimatedValue() == alphaViewValue) {
                    startBar();
                }
            });
            alphaViewValueAnimator.start();
        }
    }

    private void startBar() {
        startBarA();
        startBarB();
    }

    private void startBarA() {
        // Bar A değerini %60 (Sadece 60) haline dönüştürüyoruz.
        int computePercentA = (int) (((float) mValueBarA / (float) (mValueBarA + mValueBarB)) * 100);

        // Bar ın gösterileceği yüksekliği belirlemek için toplam yükseklikten yazının yüksekliği değerini çıkartıyoruz (100)
        // ve bu değerin yüzdelik değerini buluyoruz.
        // Örnek : Host View yüksekliği = 720 , bar oranı = %50
        // - 100 = 620 -> en üstteki yazı için yer açıyoruz
        // * (50 / 100) = 310 -> bar ın gösterileceği yükseklik
        float computeBarAValue = ((getHeight() - 130) * computePercentA / 100);

        ValueAnimator animatorAHeight = ValueAnimator.ofFloat(0, computeBarAValue);
        animatorAHeight.setDuration(animBarDuration);

        animatorAHeight.addUpdateListener(valueAnimator -> {
            animAValue = (float) animatorAHeight.getAnimatedValue();
            if ((float) animatorAHeight.getAnimatedValue() == computeBarAValue) {
                this.isFinishAnimBarA = true;
            }
            invalidate();
        });
        animatorAHeight.start();

    }

    private void startBarB() {
        int computePercentB = (int) (((float) mValueBarB / (float) (mValueBarA + mValueBarB)) * 100);
        float computeBarBValue = ((getHeight() - 130) * computePercentB / 100);

        ValueAnimator animatorBHeight = ValueAnimator.ofFloat(0, computeBarBValue);
        animatorBHeight.setDuration(animBarDuration);

        animatorBHeight.addUpdateListener(valueAnimator -> {
            animBValue = (float) animatorBHeight.getAnimatedValue();
            if ((float) animatorBHeight.getAnimatedValue() == computeBarBValue) {
                this.isFinishAnimBarB = true;
            }
            invalidate();
        });
        animatorBHeight.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        try {
            drawBarA(mCanvas);
            drawBarB(mCanvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawBarA(Canvas canvas) throws IOException {
        //float center = getWidth() / 2;
        mPaintBarA = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBarA.setColor(mColorBarA);

        Resources resources = getResources();
        float scale = resources.getDisplayMetrics().density;

        float startPoint = (getWidth() / 4) - widthBarA;
        float endPoint = (getWidth() / 4) + widthBarA;

        // Eğer xml içerisinde bar değeri verilmişse animValue yi valueABar a eşitliyoruz.
        if (!ButtonClick) animAValue = mValueBarA;

        mRectBarA = new RectF(startPoint, (getHeight() - animAValue), endPoint, getHeight());
        canvas.drawRect(mRectBarA, mPaintBarA);

        if (isFinishAnimBarA) {

            int computePercentA = (int) (((float) mValueBarA / (float) (mValueBarA + mValueBarB)) * 100);
            float textStart = startPoint + (mRectBarA.width() / 2);
            float textEnd = getHeight() - mRectBarA.height() - (widthBarA / 2);

            Paint percentText = new Paint();
            percentText.setTextSize((int) (17 * scale));
            percentText.setColor(Color.BLACK);
            percentText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            percentText.setTextAlign(Paint.Align.CENTER);

            String barText = computePercentA + " %";
            Rect barTextRect = new Rect();
            percentText.getTextBounds(barText, 0, barText.length(), barTextRect);

            canvas.drawText(barText, textStart, textEnd, percentText);

            if (mListFriendsAnswer != null && mListFriendsAnswer.size() > 0) {
                drawFriendAnswerFalse();
            }
        }
    }

    private void drawBarB(Canvas canvas) {

        //float center = getWidth() / 2;
        mPaintBarB = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBarB.setColor(mColorBarB);

        Resources resources = getResources();
        float scale = resources.getDisplayMetrics().density;

        float startPoint = getWidth() - (getWidth() / 4) - widthBarB;
        float endPoint = getWidth() - (getWidth() / 4) + widthBarB;

        // Eğer xml içerisinde bar değeri verilmişse animValue yi valueABar a eşitliyoruz.
        if (!ButtonClick) animBValue = mValueBarB;
        mRectBarB = new RectF(startPoint, (getHeight() - animBValue), endPoint, getHeight());
        canvas.drawRect(mRectBarB, mPaintBarB);

        if (isFinishAnimBarB) {
            int computePercentB = (int) (((float) mValueBarB / (float) (mValueBarA + mValueBarB)) * 100);
            float textStart = startPoint + (mRectBarB.width() / 2);
            float textEnd = getHeight() - mRectBarB.height() - (widthBarB / 2);

            Paint percentText = new Paint();
            percentText.setTextSize((int) (17 * scale));
            percentText.setColor(Color.BLACK);
            percentText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            percentText.setTextAlign(Paint.Align.CENTER);

            String barText = computePercentB + " %";
            Rect barTextRect = new Rect();
            percentText.getTextBounds(barText, 0, barText.length(), barTextRect);

            canvas.drawText(barText, textStart, textEnd, percentText);


            if (mListFriendsAnswer != null && mListFriendsAnswer.size() > 0) {
                drawFriendAnswerTrue();
            }
            //isFinishAnimBarB = false;
        }
    }

    @DebugLog
    private void drawFriendAnswerFalse() {

        RelativeLayout relativeLayout = (RelativeLayout) getParent();

        Observable.from(mListFriendsAnswer)
                .filter(modelFriend -> modelFriend.getOption().equals("b"))
                .take(FRIEND_COUNT)
                .toList()
                .filter(modelFriends -> modelFriends.size() > 0)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    Logger.i("List Count : " + list.size());
                    int index = 0;
                    for (ModelFriend modelFriend : list) {

                        Logger.i("ModelFriend: " + modelFriend.getName());
                        //stateBundle.putString("STATE_FRIEND_NAME", modelFriend.getName());

                        ImageView imageView1 = new ImageView(relativeLayout.getContext());

                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.leftMargin = 30 - (index % 2 == 0 ? 10 : -10);
                        layoutParams.bottomMargin = index * mFriendAnswerViewSize / 2;

                        imageView1.setLayoutParams(layoutParams);

                        relativeLayout.addView(imageView1);

                        Logger.i("Friend Answer False, Name: " + modelFriend.getName());
                        Picasso.with(relativeLayout.getContext())
                                .load(modelFriend.getProfileImage())
                                .transform(new PicassoCircleTransform())
                                .resize(mFriendAnswerViewSize, mFriendAnswerViewSize)
                                .centerCrop()
                                .into(imageView1);

                        index++;
                    }
                    drawAnotherIcon(index, ANOTHER_ICON_POSITION.LEFT);
                }, error -> {
                    Logger.e(error, "HATA");
                    error.printStackTrace();
                });
    }

    @DebugLog
    private void drawFriendAnswerTrue() {

        RelativeLayout relativeLayout = (RelativeLayout) getParent();

        Observable.from(mListFriendsAnswer)
                .filter(modelFriend -> modelFriend.getOption().equals("a"))
                .take(FRIEND_COUNT)
                .toList()
                .filter(modelFriends -> modelFriends.size() > 0)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pairs -> {
                    int index = 0;
                    for (ModelFriend modelFriend : pairs) {

                        ImageView imageView1 = new ImageView(relativeLayout.getContext());
                        imageView1.setId(new Random().nextInt());

                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.rightMargin = 30 - (index % 2 == 0 ? 10 : -10);
                        layoutParams.bottomMargin = index * mFriendAnswerViewSize / 2;

                        imageView1.setLayoutParams(layoutParams);

                        relativeLayout.addView(imageView1);

                        Logger.i("Friend Answer True, Name: " + modelFriend.getName());
                        Picasso.with(relativeLayout.getContext())
                                .load(modelFriend.getProfileImage())
                                .transform(new PicassoCircleTransform())
                                .resize(mFriendAnswerViewSize, mFriendAnswerViewSize)
                                .centerCrop()
                                .into(imageView1);

                        index++;
                    }
                    drawAnotherIcon(index, ANOTHER_ICON_POSITION.RIGHT);
                }, error -> {
                    Logger.e(error, "HATA");
                    error.printStackTrace();
                });
    }

    @DebugLog
    private void drawAnotherIcon(int index, ANOTHER_ICON_POSITION position) {

        RelativeLayout relativeLayout = (RelativeLayout) getParent();

        ImageView imageView = new ImageView(mContext);

        imageView.setOnClickListener(v -> {

            List<ModelFriend> answerTrueFriendList = new ArrayList<>();
            rx.Observable.from(mListFriendsAnswer)
                    .filter(modelFriend -> modelFriend.getOption().equals(position == ANOTHER_ICON_POSITION.LEFT ? "b" : "a"))
                    .subscribe(answerTrueFriendList::add).unsubscribe();


            FriendAnswerListAdapter adapter = new FriendAnswerListAdapter(mContext, answerTrueFriendList);
            MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                    .title("Arkadaşlarınız")
                    .adapter(adapter, (mDialog, itemView, which, text) -> {
                        //MaterialSimpleListItem item1 = listTopluTeslimStateAdapter.getItem(which);
                        //showToast(item.getContent().toString());
                    }).build();
            dialog.show();
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(position == ANOTHER_ICON_POSITION.LEFT ? RelativeLayout.ALIGN_PARENT_START : RelativeLayout.ALIGN_PARENT_END);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.rightMargin = position == ANOTHER_ICON_POSITION.RIGHT ? 30 : 0;
        layoutParams.leftMargin = position == ANOTHER_ICON_POSITION.LEFT ? 30 : 0;
        layoutParams.bottomMargin = index * mFriendAnswerViewSize / 2;

        imageView.setLayoutParams(layoutParams);

        relativeLayout.addView(imageView);

        Picasso.with(mContext)
                .load(R.drawable.ic_add_circle_indigo_300_48dp)
                .resize(mFriendAnswerViewSize, mFriendAnswerViewSize)
                .into(imageView);
    }


//    public Bitmap getCroppedBitmap(Bitmap bitmap) {
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
//                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
//                bitmap.getWidth() / 2, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
//        //return _bmp;
//        return output;
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getParent() instanceof RelativeLayout) {
            //Logger.i("Parent is RelativeLayout");
        } else {
            Log.i(CustomAnswerPercent.class.getSimpleName(), "Parent is not RelativeLayout !");
            return;
        }

        //Logger.i("MeasureSpec.toString(width):" + MeasureSpec.toString(widthMeasureSpec));
        //Logger.i("MeasureSpec.toString(height):" + MeasureSpec.toString(heightMeasureSpec));

        // View in height ını parent ı kadar yapıyoruz.
        // Parent Relative Layout olmak zorunda
        //int specHeight = MeasureSpec.makeMeasureSpec(((RelativeLayout) getParent()).getHeight(), MeasureSpec.UNSPECIFIED);
        //int specWidth = MeasureSpec.makeMeasureSpec(((RelativeLayout) getParent()).getWidth(), MeasureSpec.UNSPECIFIED);
        //Logger.i("makeMeasureSpec.toString(width):" + MeasureSpec.toString(specWidth));
        //Logger.i("makeMeasureSpec.toString(height):" + MeasureSpec.toString(specHeight));
        //setMeasuredDimension(specWidth, specHeight);
        //setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private void drawBar(Canvas canvas) {
        /*
        String maxValueString = String.valueOf(maxValue);
        Rect maxValueRect = new Rect();
        maxValuePaint.getTextBounds(maxValueString, 0, maxValueString.length(), maxValueRect);
        float barLength = getWidth() - getPaddingRight() - getPaddingLeft() - circleRadius - maxValueRect.width() - spaceAfterBar;

        float barCenter = getBarCenter();

        float halfBarHeight = barHeight / 2;
        float top = barCenter - halfBarHeight;
        float bottom = barCenter + halfBarHeight;
        float left = getPaddingLeft();
        float right = getPaddingLeft() + barLength;
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, halfBarHeight, halfBarHeight, barBasePaint);


        float percentFilled = (float) valueToDraw / (float) maxValue;
        float fillLength = barLength * percentFilled;
        float fillPosition = left + fillLength;
        RectF fillRect = new RectF(left, top, fillPosition, bottom);
        canvas.drawRoundRect(fillRect, halfBarHeight, halfBarHeight, barFillPaint);

        canvas.drawCircle(fillPosition, barCenter, circleRadius, circlePaint);

        Rect bounds = new Rect();
        String valueString = String.valueOf(Math.round(valueToDraw));
        currentValuePaint.getTextBounds(valueString, 0, valueString.length(), bounds);
        float y = barCenter + (bounds.height() / 2);
        canvas.drawText(valueString, fillPosition, y, currentValuePaint);
        */
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawLine(0, 0, 200, 200, mPaint);
    }

    private void drawLabel(Canvas canvas) {
        /*
        float x = getPaddingLeft();
        //the y coordinate marks the bottom of the text, so we need to factor in the height
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        float y = getPaddingTop() + bounds.height();

        canvas.drawText(labelText, x, y, labelPaint);
        */
    }

    private void drawRect(Canvas canvas, Rect rect) {
        /*
        float x = getPaddingLeft();
        //the y coordinate marks the bottom of the text, so we need to factor in the height
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        float y = getPaddingTop() + bounds.height();

        canvas.drawText(labelText, x, y, labelPaint);
        */
        canvas.drawRect(rect, mPaint);
    }

    private int measureHeight(int measureSpec) {


        int size = getPaddingTop() + getPaddingBottom();
        //size += labelPaint.getFontSpacing();
        //float maxValueTextSpacing = maxValuePaint.getFontSpacing();
        //size += Math.max(maxValueTextSpacing, Math.max(barHeight, circleRadius * 2));
        size += CustomAnswerPercent.this.getRootView().getMeasuredHeight();
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int measureWidth(int measureSpec) {
        /*
        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        size += bounds.width();

        bounds = new Rect();
        String maxValueText = String.valueOf(maxValue);
        maxValuePaint.getTextBounds(maxValueText, 0, maxValueText.length(), bounds);
        size += bounds.width();
        */
        //Logger.i("getWidth(): " + getWidth());
        return resolveSizeAndState(getWidth(), measureSpec, 0);
    }
}
