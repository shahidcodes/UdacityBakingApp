package ml.shahidkamal.udacitybakingapp.fragments;


import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.shahidkamal.udacitybakingapp.R;
import ml.shahidkamal.udacitybakingapp.StepsActivity;
import ml.shahidkamal.udacitybakingapp.model.Steps;
import ml.shahidkamal.udacitybakingapp.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String TAG = StepsFragment.class.getSimpleName();
    List<Steps> stepsList;
    int position;
    @BindView(R.id.tv_step_description)
    TextView tvStepDesc;
    @BindView(R.id.iv_step_image)
    ImageView ivStepThumb;
    @BindView(R.id.exo_step_video)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.btn_next_recipe)
    Button nextRecipeBtn;
    @BindView(R.id.btn_prev_recipe)
    Button prevRecipeBtn;

    private PlaybackStateCompat.Builder playbackBuilder;
    SimpleExoPlayer simpleExoPlayer;
    MediaSessionCompat mediaSessionCompat;
    boolean mTwoPane;
    Steps steps;

    long positionPlayer;
    boolean playWhenReady;

    public StepsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            String stepsJSON = bundle.getString(Constants.INTENT_KEY_STEPS_FRAGMENT);
            position = bundle.getInt(Constants.INTENT_KEY_STEPS_POSITION);
            mTwoPane = bundle.getBoolean(Constants.INTENT_KEY_PANE);
            Gson gson = new Gson();
            stepsList = gson.fromJson(stepsJSON, new TypeToken<List<Steps>>(){}.getType());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, view);
        if(mTwoPane){
            prevRecipeBtn.setVisibility(View.GONE);
            nextRecipeBtn.setVisibility(View.GONE);
        }
        if(position == 0) disablePrevBtn();
        if(position == stepsList.size()-1) disableNextBtn();
        prevRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepsActivity stepsActivity = (StepsActivity)getActivity();
                if(position > 0) {
                    stepsActivity.prevRecipe(position);
                }else{
                    disablePrevBtn();
                }
            }
        });
        nextRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepsActivity stepsActivity = (StepsActivity)getActivity();
                if(position <= stepsList.size()-1){
                    stepsActivity.nextRecipe(position);
                }else{
                    disableNextBtn();
                }
            }
        });
        if (savedInstanceState != null) {
            int placeHolderVisibility = savedInstanceState.getInt(Constants.KEY_VISIBILITY_PLACEHOLDER);
            Log.d(TAG, "Visiblity: " + placeHolderVisibility);
            ivStepThumb.setVisibility(placeHolderVisibility);
            int visibilityExo = savedInstanceState.getInt(Constants.KEY_VISIBILITY_EXO_PLAYER);
            simpleExoPlayerView.setVisibility(visibilityExo);
            playWhenReady = savedInstanceState.getBoolean(Constants.KEY_PLAY_WHEN_READY);
        }
        Log.d(TAG, "Orientation: " + getResources().getConfiguration().orientation);
        steps = stepsList.get(position);
//        Log.d(TAG, "Step:" + steps.getDescription()  + ", " + steps.getThumbnailURL() + ", " + steps.getVideoURL());
        Log.d(TAG, "Adapter postition: "+ position);
        if(!steps.getVideoURL().equals("")){
            Log.d(TAG, "Has VideoView ");
            if (savedInstanceState != null) {
                positionPlayer = savedInstanceState.getLong(Constants.MEDIA_POS);
            }
            initMedia();
            initializePlayer(Uri.parse(steps.getVideoURL()));
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT && !mTwoPane)  {
                hideUI();
                simpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                simpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }else if(!steps.getThumbnailURL().equals("")){
            Log.d(TAG, "Has image ");
            simpleExoPlayerView.setVisibility(View.GONE);
            if(!steps.getThumbnailURL().isEmpty()) {
                ivStepThumb.setVisibility(View.VISIBLE);
                Picasso.get().load(steps.getThumbnailURL())
                        .into(ivStepThumb);
            }
        }
        tvStepDesc.setText(steps.getDescription());
        return view;
    }

    private void disableNextBtn() {
        nextRecipeBtn.setEnabled(false);
        nextRecipeBtn.setBackgroundColor(Color.GRAY);
    }

    private void disablePrevBtn() {
        prevRecipeBtn.setEnabled(false);
        prevRecipeBtn.setBackgroundColor(Color.GRAY);
    }


    private void initMedia() {
        mediaSessionCompat = new MediaSessionCompat(getActivity(), TAG);
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSessionCompat.setMediaButtonReceiver(null);
        playbackBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSessionCompat.setPlaybackState(playbackBuilder.build());
        mediaSessionCompat.setCallback(new SessionCallBacks());
        mediaSessionCompat.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance
                    (getActivity(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getActivity(),
                    getActivity().getString(R.string.application_name_exo_player));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            playbackBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            playbackBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        }
        mediaSessionCompat.setPlaybackState(playbackBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void hideUI() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.KEY_VISIBILITY_EXO_PLAYER, simpleExoPlayerView.getVisibility());
        outState.putInt(Constants.KEY_VISIBILITY_PLACEHOLDER, ivStepThumb.getVisibility());
        //Saving current Position before rotation
        outState.putLong(Constants.MEDIA_POS, positionPlayer);
        //for preserving state of exoplayer
        outState.putBoolean(Constants.KEY_PLAY_WHEN_READY, playWhenReady);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mediaSessionCompat != null) {
            mediaSessionCompat.setActive(false);
        }
    }

    @Override
    public void onPause() {
        //releasing in Pause and saving current position for resuming
        super.onPause();
        if (simpleExoPlayer != null) {
            positionPlayer = simpleExoPlayer.getCurrentPosition();
            //getting play when ready so that player can be properly store state on rotation
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (simpleExoPlayer != null) {
            //resuming properly
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
            simpleExoPlayer.seekTo(positionPlayer);
        } else {
            //Correctly initialize and play properly fromm seekTo function
            initMedia();
            initializePlayer(Uri.parse(steps.getVideoURL()));
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
            simpleExoPlayer.seekTo(positionPlayer);
        }
    }


    private class SessionCallBacks extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
            simpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            simpleExoPlayer.seekTo(0);
        }
    }

}
