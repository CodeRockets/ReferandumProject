package com.coderockets.referandumproject.util.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.facebook.login.widget.LoginButton;

import agency.tango.materialintroscreen.SlideFragment;
import hugo.weaving.DebugLog;
import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Alpha;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Loop;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Side;

public class LoginSlide extends SlideFragment {

    LoginButton mLoginButton;
    TextSurface textSurface;

    @DebugLog
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.slide_login_layout, container, false);

        mLoginButton = (LoginButton) view.findViewById(R.id.LoginButton);
        textSurface = (TextSurface) view.findViewById(R.id.text_surface);

        setLoginButton();

        return view;
    }

    @DebugLog
    @Override
    public int backgroundColor() {
        return R.color.slide6_background;
    }

    @DebugLog
    @Override
    public int buttonsColor() {
        return R.color.slide6_buttons;
    }

    @DebugLog
    @Override
    public boolean canMoveFurther() {
        return SuperHelper.checkUser();
    }

    @DebugLog
    @Override
    public String cantMoveFurtherErrorMessage() {
        return "Giriş yapmanız gerekmektedir.";
    }

    @DebugLog
    private void setLoginButton() {
        mLoginButton.setReadPermissions("public_profile", "email", "user_friends");
    }

    @DebugLog
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && textSurface != null) {
            textSurface.postDelayed(this::show, 100);
        }
    }


    private void show1() {


        textSurface.reset();


        //SurfaceTransSample
        /*
        Text textA = TextBuilder.create("TextA").setPosition(Align.SURFACE_CENTER).build();
        Text textB = TextBuilder.create("TextB").setPosition(Align.RIGHT_OF | Align.BOTTOM_OF, textA).build();
        Text textC = TextBuilder.create("TextC").setPosition(Align.LEFT_OF | Align.BOTTOM_OF, textB).build();
        Text textD = TextBuilder.create("TextD").setPosition(Align.RIGHT_OF | Align.BOTTOM_OF, textC).build();

        int duration = 500;

        textSurface.play(TYPE.SEQUENTIAL,
                Alpha.show(textA, duration),
                new AnimationsSet(TYPE.PARALLEL, Alpha.show(textB, duration), new TransSurface(duration, textB, Pivot.CENTER)),
                new AnimationsSet(TYPE.PARALLEL, Alpha.show(textC, duration), new TransSurface(duration, textC, Pivot.CENTER)),
                new AnimationsSet(TYPE.PARALLEL, Alpha.show(textD, duration), new TransSurface(duration, textD, Pivot.CENTER)),
                new TransSurface(duration, textC, Pivot.CENTER),
                new TransSurface(duration, textB, Pivot.CENTER),
                new TransSurface(duration, textA, Pivot.CENTER)
        );
        */

        //SurfaceScaleSample
        /*
        Text textA = TextBuilder.create("How are you?").setPosition(Align.SURFACE_CENTER).build();
        Text textB = TextBuilder.create("Would you mind?").setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textA).build();
        Text textC = TextBuilder.create("Yes!").setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textB).build();


        textSurface.play(TYPE.SEQUENTIAL,
                Alpha.show(textA, 500),
                new AnimationsSet(TYPE.PARALLEL,
                        new AnimationsSet(TYPE.PARALLEL, Alpha.show(textB, 500), Alpha.hide(textA, 500)),
                        new ScaleSurface(500, textB, Fit.WIDTH)
                ),
                Delay.duration(1000),
                new AnimationsSet(TYPE.PARALLEL,
                        new AnimationsSet(TYPE.PARALLEL, Alpha.show(textC, 500), Alpha.hide(textB, 500)),
                        new ScaleSurface(500, textC, Fit.WIDTH)
                )
        );
        */


        //SlideSample
        /*
        Text textA = TextBuilder.create(" How are you?").build();
        Text textB = TextBuilder.create("I'm fine! ").setPosition(Align.LEFT_OF, textA).build();
        Text textC = TextBuilder.create("Are you sure?").setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textB).build();
        Text textD = TextBuilder.create("Totally!").setPadding(10, 10, 10, 10).setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textC).build();
        int duration = 1250;

        textSurface.play(
                TYPE.SEQUENTIAL,
                new AnimationsSet(TYPE.PARALLEL,
                        new AnimationsSet(TYPE.SEQUENTIAL,
                                new AnimationsSet(TYPE.PARALLEL, Slide.showFrom(Side.LEFT, textA, duration), Slide.showFrom(Side.RIGHT, textB, duration)),
                                Slide.showFrom(Side.TOP, textC, duration),
                                Slide.showFrom(Side.BOTTOM, textD, duration)
                        ),
                        new TransSurface(duration * 3, textD, Pivot.CENTER)
                ),
                new AnimationsSet(TYPE.PARALLEL,
                        new AnimationsSet(TYPE.SEQUENTIAL,
                                new AnimationsSet(TYPE.PARALLEL, Slide.hideFrom(Side.LEFT, textD, duration), Slide.hideFrom(Side.RIGHT, textC, duration)),
                                Slide.hideFrom(Side.TOP, textB, duration),
                                Slide.hideFrom(Side.BOTTOM, textA, duration)
                        ),
                        new TransSurface(duration * 3, textA, Pivot.CENTER)
                )

        );
        */

        //ShapeRevealLoopSample
        /*
        Text textA = TextBuilder.create("Now why you loer en kyk gelyk?").setPosition(Align.SURFACE_CENTER).build();
        Text textB = TextBuilder.create("Is ek miskien van goud gemake?").setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textA).build();
        Text textC = TextBuilder.create("You want to fight, you come tonight.").setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textB).build();
        Text textD = TextBuilder.create("Ek moer jou sleg! So jy hardloop weg.").setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textC).build();

        final int flash = 1500;

        textSurface.play(
                new Loop(
                        Rotate3D.showFromCenter(textA, 500, Direction.CLOCK, Axis.X),
                        new AnimationsSet(TYPE.PARALLEL,
                                ShapeReveal.create(textA, flash, SideCut.hide(Side.LEFT), false),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(flash / 4), ShapeReveal.create(textA, flash, SideCut.show(Side.LEFT), false))
                        ),
                        new AnimationsSet(TYPE.PARALLEL,
                                Rotate3D.showFromSide(textB, 500, Pivot.TOP),
                                new TransSurface(500, textB, Pivot.CENTER)
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                Slide.showFrom(Side.TOP, textC, 500),
                                new TransSurface(1000, textC, Pivot.CENTER)
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                ShapeReveal.create(textD, 500, Circle.show(Side.CENTER, Direction.OUT), false),
                                new TransSurface(1500, textD, Pivot.CENTER)
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textD, 700), ShapeReveal.create(textD, 1000, SideCut.hide(Side.LEFT), true)),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(500), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textC, 700), ShapeReveal.create(textC, 1000, SideCut.hide(Side.LEFT), true))),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(1000), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textB, 700), ShapeReveal.create(textB, 1000, SideCut.hide(Side.LEFT), true))),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(1500), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textA, 700), ShapeReveal.create(textA, 1000, SideCut.hide(Side.LEFT), true))),
                                new TransSurface(4000, textA, Pivot.CENTER)
                        )
                )
        );
        */


        //ColorSample
        /*
        Text textA = TextBuilder
                .create("Now why you loer en kyk gelyk?")
                .setPosition(Align.SURFACE_CENTER)
                .setSize(30)
                .setAlpha(0)
                .build();

        textSurface.play(TYPE.SEQUENTIAL,
                Alpha.show(textA, 1000),
                ChangeColor.to(textA, 1000, Color.RED),
                ChangeColor.fromTo(textA, 1000, Color.BLUE, Color.CYAN),
                Alpha.hide(textA, 1000)
        );
        */


        // AlignSample
        /*
        Text textCenter = TextBuilder.create("Center")
                .setPosition(Align.SURFACE_CENTER)
                .setPadding(25, 25, 25, 25)
                .build();

        //

        Text textLeft = TextBuilder.create("L")
                .setPadding(20, 20, 20, 20)
                .setPosition(Align.LEFT_OF | Align.CENTER_OF, textCenter)
                .build();

        Text textRight = TextBuilder.create("R")
                .setPadding(20, 20, 20, 20)
                .setPosition(Align.RIGHT_OF | Align.CENTER_OF, textCenter)
                .build();

        Text textTop = TextBuilder.create("T")
                .setPosition(Align.TOP_OF | Align.CENTER_OF, textCenter)
                .build();

        Text textBottom = TextBuilder.create("B")
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textCenter)
                .build();

        //

        Text textBottomBottom = TextBuilder.create("BB")
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textBottom)
                .build();

        //

        Text textLeftTop = TextBuilder.create("LT")
                .setPosition(Align.LEFT_OF | Align.TOP_OF, textCenter)
                .build();

        Text textRightTop = TextBuilder.create("RT")
                .setPosition(Align.RIGHT_OF | Align.TOP_OF, textCenter)
                .build();

        Text textLeftBottom = TextBuilder.create("LB")
                .setPosition(Align.LEFT_OF | Align.BOTTOM_OF, textCenter)
                .build();

        Text textRightBottom = TextBuilder.create("RB")
                .setPosition(Align.BOTTOM_OF | Align.RIGHT_OF, textCenter)
                .build();

        final int duration = 125;

        textSurface.play(TYPE.SEQUENTIAL,
                Alpha.show(textCenter, duration),
                Alpha.show(textRight, duration),
                Alpha.show(textTop, duration),
                Alpha.show(textLeft, duration),
                Alpha.show(textBottom, duration),

                Alpha.show(textLeftTop, duration),
                Alpha.show(textLeftBottom, duration),
                Alpha.show(textRightBottom, duration),
                Alpha.show(textRightTop, duration),

                Alpha.show(textBottomBottom, duration)
        );
        */


        /*
        Text textA = TextBuilder.create("How are you?").setPosition(Align.SURFACE_CENTER).build();
        Text textB = TextBuilder.create("I'm fine! And you?").setPosition(Align.SURFACE_CENTER, textA).build();
        Text textC = TextBuilder.create("Haaay!").setPosition(Align.SURFACE_CENTER, textB).build();
        int duration = 2750;

        textSurface.play(TYPE.SEQUENTIAL,
                new AnimationsSet(TYPE.SEQUENTIAL,
                        Rotate3D.showFromCenter(textA, duration, Direction.CLOCK, Axis.X),
                        Rotate3D.hideFromCenter(textA, duration, Direction.CLOCK, Axis.Y)
                ),
                new AnimationsSet(TYPE.SEQUENTIAL,
                        Rotate3D.showFromSide(textB, duration, Pivot.LEFT),
                        Rotate3D.hideFromSide(textB, duration, Pivot.RIGHT)
                ),
                new AnimationsSet(TYPE.SEQUENTIAL,
                        Rotate3D.showFromSide(textC, duration, Pivot.TOP),
                        Rotate3D.hideFromSide(textC, duration, Pivot.BOTTOM)
                )
        );*/
    }

    @DebugLog
    private void show() {

        textSurface.reset();

        final Typeface robotoBlack = Typeface.createFromAsset(getContext().getAssets(), "fonts/Anton.ttf");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(robotoBlack);

        Text textReferandum = TextBuilder
                .create("REFERANDUM")
                .setPaint(paint)
                .setSize(64)
                .setAlpha(23)
                .setColor(Color.WHITE)
                .setPosition(Align.SURFACE_CENTER)
                .build();

        Text textCodeRockets = TextBuilder
                .create("CodeRockets")
                .setPaint(paint)
                .setSize(24)
                .setAlpha(1)
                .setColor(Color.CYAN)
                .setPosition(Align.TOP_OF, textReferandum)
                .build();

        textSurface.play(
                new Sequential(
                        new Loop(
                                //Alpha.show(textCodeRockets, 300),
                                //Delay.duration(500),
                                ShapeReveal.create(textReferandum, 2000, SideCut.show(Side.LEFT), false),
                                Delay.duration(2000),
                                Alpha.hide(textReferandum,1000)
                        )
                )
        );

//        textSurface.play(
//                new Sequential(
//                        ShapeReveal.create(textReferandum, 750, SideCut.show(Side.LEFT), false),
//                        new Parallel(ShapeReveal.create(textReferandum, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(300), ShapeReveal.create(textReferandum, 600, SideCut.show(Side.LEFT), false))),
//                        new Parallel(new TransSurface(500, textBraAnies, Pivot.CENTER), ShapeReveal.create(textBraAnies, 1300, SideCut.show(Side.LEFT), false)),
//                        Delay.duration(500),
//                        Delay.duration(2000),
//                        new Parallel(new TransSurface(750, textFokkenGamBra, Pivot.CENTER), Slide.showFrom(Side.LEFT, textFokkenGamBra, 750), ChangeColor.to(textFokkenGamBra, 750, Color.WHITE)),
//                        Delay.duration(500),
//                        new Parallel(TransSurface.toCenter(textHaai, 500), Rotate3D.showFromSide(textHaai, 750, Pivot.TOP)),
//                        new Parallel(TransSurface.toCenter(textDaaiAnies, 500), Slide.showFrom(Side.TOP, textDaaiAnies, 500)),
//                        new Parallel(TransSurface.toCenter(texThyLamInnie, 750), Slide.showFrom(Side.LEFT, texThyLamInnie, 500)),
//                        Delay.duration(500),
//                        new Parallel(
//                                new TransSurface(1500, textSignsInTheAir, Pivot.CENTER),
//                                new Sequential(
//                                        new Sequential(ShapeReveal.create(textThrowDamn, 500, Circle.show(Side.CENTER, Direction.OUT), false)),
//                                        new Sequential(ShapeReveal.create(textDevilishGang, 500, Circle.show(Side.CENTER, Direction.OUT), false)),
//                                        new Sequential(ShapeReveal.create(textSignsInTheAir, 500, Circle.show(Side.CENTER, Direction.OUT), false))
//                                )
//                        ),
//                        Delay.duration(200),
//                        new Parallel(
//                                ShapeReveal.create(textThrowDamn, 1500, SideCut.hide(Side.LEFT), true),
//                                new Sequential(Delay.duration(250), ShapeReveal.create(textDevilishGang, 1500, SideCut.hide(Side.LEFT), true)),
//                                new Sequential(Delay.duration(500), ShapeReveal.create(textSignsInTheAir, 1500, SideCut.hide(Side.LEFT), true)),
//                                Alpha.hide(texThyLamInnie, 1500),
//                                Alpha.hide(textDaaiAnies, 1500)
//                        )
//                )
//        );

        //CookieThumperSample.play(textSurface, getAssets());
    }

}