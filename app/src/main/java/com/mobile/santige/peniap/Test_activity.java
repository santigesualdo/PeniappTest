package com.mobile.santige.peniap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;


public class Test_activity extends Activity {

    Button myBut5, myBut6, myBut7, myBut8;

    public ChainTourGuide mTourGuideHandler;
    public Activity mActivity;
    private Animation mEnterAnimation, mExitAnimation;

    public static final int OVERLAY_METHOD = 1;
    public static final int OVERLAY_LISTENER_METHOD = 2;

    public static final String CONTINUE_METHOD = "continue_method";
    private int mChosenContinueMethod = OVERLAY_LISTENER_METHOD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activity);

        myBut5 = (Button) findViewById(R.id.button5);
        myBut6 = (Button) findViewById(R.id.button6);
        myBut7 = (Button) findViewById(R.id.button7);
        myBut8 = (Button) findViewById(R.id.button8);

                /* setup enter and exit animation */
        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(600);
        mEnterAnimation.setFillAfter(true);

        mExitAnimation = new AlphaAnimation(1f, 0f);
        mExitAnimation.setDuration(600);
        mExitAnimation.setFillAfter(true);

        runOverlay_ContinueMethod();
    }

    private void runOverlay_ContinueMethod() {
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        ChainTourGuide tourGuide1 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setTitle("ContinueMethod.OVERLAY")
                        .setDescription("When using this ContinueMethod, you can't specify the additional action before going to next TourGuide.")
                        .setGravity(Gravity.BOTTOM)
                )
                // note that there is no Overlay here, so the default one will be used
                .playLater(myBut5);

        ChainTourGuide tourGuide2 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setTitle("Tip")
                        .setDescription("Individual Overlay will be used when it's supplied.")
                        .setGravity(Gravity.BOTTOM | Gravity.LEFT)
                        .setBackgroundColor(Color.parseColor("#c0392b"))
                )
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .playLater(myBut6);

        ChainTourGuide tourGuide3 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setTitle("ContinueMethod.OVERLAY")
                        .setDescription("When using this ContinueMethod, you don't need to call tourGuide.next() explicitly, TourGuide will do it for you.")
                        .setGravity(Gravity.TOP)
                )
                // note that there is no Overlay here, so the default one will be used
                .playLater(myBut7);

        ChainTourGuide tourGuide4 = ChainTourGuide.init(this)
                .setToolTip(new ToolTip()
                        .setTitle("ContinueMethod.OVERLAY")
                        .setDescription("When using this ContinueMethod, you don't need to call tourGuide.next() explicitly, TourGuide will do it for you.")
                        .setGravity(Gravity.TOP)
                )
                // note that there is no Overlay here, so the default one will be used
                .playLater(myBut8);

        Sequence sequence = new Sequence.SequenceBuilder()
                .add(tourGuide1, tourGuide2, tourGuide3, tourGuide4)
                .setDefaultOverlay(new Overlay()
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                )
                .setDefaultPointer(null)
                .setContinueMethod(Sequence.ContinueMethod.Overlay)
                .build();


        mTourGuideHandler = ChainTourGuide.init(this).playInSequence(sequence);
    }

}
