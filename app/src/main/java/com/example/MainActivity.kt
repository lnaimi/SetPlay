package com.example

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoSizeSelectLarge
import androidx.compose.material.icons.filled.PictureInPictureAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import com.example.ui.theme.tvFocusableControl
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.data.model.PlaylistEntity
import com.example.data.model.StreamEntity
import com.example.ui.favorites.FavoritesScreen
import com.example.ui.home.HomeScreen
import com.example.ui.livetv.LiveTvScreen
import com.example.ui.livetv.LiveTvViewModel
import com.example.ui.parental.ParentalControlsScreen
import com.example.ui.player.PlayerScreen
import com.example.ui.player.PlayerViewModel
import com.example.ui.playlists.PlaylistsScreen
import com.example.ui.search.GlobalSearchScreen
import com.example.ui.settings.SettingsScreen
import com.example.data.sync.SyncRole
import com.example.ui.sync.TvSyncCompanionBar
import com.example.ui.sync.TvSyncModalSheet
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ArenaFloodlightLime
import com.example.ui.theme.Champagne
import com.example.ui.theme.DarkGraphite
import com.example.ui.theme.FavoritesBotanicalBorder
import com.example.ui.theme.FavoritesEvergreenSurface
import com.example.ui.theme.FavoritesObsidianBase
import com.example.ui.theme.FavoritesPaleMint
import com.example.ui.theme.FavoritesSagePrimary
import com.example.ui.theme.KickoffCeruleanBlue
import com.example.ui.theme.KickoffCoralRed
import com.example.ui.theme.KickoffFloodlightVolt
import com.example.ui.theme.KickoffFloodlightWhite
import com.example.ui.theme.KickoffLiveCrimson
import com.example.ui.theme.KickoffOceanBase
import com.example.ui.theme.KickoffOceanSurface
import com.example.ui.theme.KickoffPeachCream
import com.example.ui.theme.KickoffPitchBlack
import com.example.ui.theme.KickoffPitchEmerald
import com.example.ui.theme.KickoffStadiumForest
import com.example.ui.theme.KickoffTrophyGold
import com.example.ui.theme.KickoffTurfSurface
import com.example.data.repository.MatchStatus
import com.example.ui.theme.KickoffTropicalTeal
import com.example.ui.theme.LiveCrimson
import com.example.ui.theme.LiveRed
import com.example.ui.theme.MetalCharcoal
import com.example.ui.theme.MetalCharcoalElevated
import com.example.ui.theme.ChampagneMuted
import com.example.ui.theme.ChampagneSubtleBorder
import com.example.ui.theme.MoviesAquamarine
import com.example.ui.theme.MoviesAtlanticTeal
import com.example.ui.theme.MoviesCyanTeal
import com.example.ui.theme.MoviesMintSpring
import com.example.ui.theme.MoviesSapphireNavy
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PaleGold
import com.example.ui.theme.SeriesEmerald
import com.example.ui.theme.SeriesForestSurface
import com.example.ui.theme.SeriesIceAqua
import com.example.ui.theme.SeriesMistTeal
import com.example.ui.theme.SeriesPineBase
import com.example.ui.theme.SeriesSeaJade
import com.example.ui.theme.TvBroadcastScarlet
import com.example.ui.theme.TvCrimsonRose
import com.example.ui.theme.TvMidnightSlate
import com.example.ui.theme.TvPlumMagenta
import com.example.ui.theme.TvTwilightIndigo
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.vod.VodScreen
import com.example.ui.vod.VodTab
import com.example.ui.vod.VodViewModel
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.util.SecurityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by viewModels()
    private val liveTvViewModel: LiveTvViewModel by viewModels()
    private val vodViewModel: VodViewModel by viewModels()

    private val isInSystemPipModeFlow = MutableStateFlow(false)

    @kotlin.OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isInSystemPip by isInSystemPipModeFlow.collectAsStateWithLifecycle()
            MyApplicationTheme {
                MainAppContent(
                    mainViewModel = mainViewModel,
                    playerViewModel = playerViewModel,
                    liveTvViewModel = liveTvViewModel,
                    vodViewModel = vodViewModel,
                    isInSystemPipMode = isInSystemPip
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.refreshSyncedTvStatusOnResume()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val hasActiveStream = playerViewModel.uiState.value.currentStream != null &&
            playerViewModel.uiState.value.isPlaying
        if (hasActiveStream && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val params = PictureInPictureParams.Builder()
                    .setAspectRatio(Rational(16, 9))
                    .build()
                enterPictureInPictureMode(params)
            } catch (_: Exception) {}
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isInSystemPipModeFlow.value = isInPictureInPictureMode
    }
}

@OptIn(UnstableApi::class)
@ExperimentalMaterial3Api
@Composable
fun MainAppContent(
    mainViewModel: MainViewModel,
    playerViewModel: PlayerViewModel,
    liveTvViewModel: LiveTvViewModel,
    vodViewModel: VodViewModel,
    isInSystemPipMode: Boolean = false
) {
    val currentScreen by mainViewModel.currentScreen.collectAsStateWithLifecycle()
    val activePlaylist by mainViewModel.activePlaylist.collectAsStateWithLifecycle()
    val isParentalUnlocked by mainViewModel.isParentalUnlockedThisSession.collectAsStateWithLifecycle()
    val playerUiState by playerViewModel.uiState.collectAsStateWithLifecycle()
    val activeGoalAlert by mainViewModel.activeGoalAlert.collectAsStateWithLifecycle()
    val tvSyncState by mainViewModel.tvSyncState.collectAsStateWithLifecycle()
    val tvSyncErrorMessage by mainViewModel.tvSyncErrorMessage.collectAsStateWithLifecycle()

    val selectedMatchDay by mainViewModel.selectedMatchDay.collectAsStateWithLifecycle()
    val matchesByDay by mainViewModel.matchesByDay.collectAsStateWithLifecycle()
    val todayMatches by mainViewModel.todayMatches.collectAsStateWithLifecycle()
    val favorites by mainViewModel.favorites.collectAsStateWithLifecycle()
    val recentlyWatched by mainViewModel.recentlyWatched.collectAsStateWithLifecycle()
    val liveCategories by liveTvViewModel.categories.collectAsStateWithLifecycle()
    val movieCategories by vodViewModel.movieCategories.collectAsStateWithLifecycle()
    val seriesCategories by vodViewModel.seriesCategories.collectAsStateWithLifecycle()

    val liveMatchesCount = remember(todayMatches) {
        todayMatches.count { it.status == MatchStatus.LIVE || it.status == MatchStatus.HALFTIME }
    }
    val activeDayMatches = remember(matchesByDay, selectedMatchDay) {
        matchesByDay[selectedMatchDay].orEmpty()
    }
    val activeDayLeaguesCount = remember(activeDayMatches) {
        activeDayMatches.map { it.tournamentId }.distinct().size
    }
    val topSectionDetailText = remember(
        currentScreen,
        selectedMatchDay,
        activeDayMatches.size,
        activeDayLeaguesCount,
        activePlaylist,
        liveCategories.size,
        movieCategories.size,
        seriesCategories.size,
        favorites.size
    ) {
        when (currentScreen) {
            AppScreen.HOME -> {
                val dayLabel = selectedMatchDay.getDynamicTitle()
                if (activeDayMatches.isNotEmpty()) {
                    "$dayLabel · ${activeDayMatches.size} games · $activeDayLeaguesCount leagues"
                } else {
                    "$dayLabel · no games yet"
                }
            }
            AppScreen.LIVE_TV -> {
                if (activePlaylist != null && activePlaylist!!.channelCount > 0) {
                    "%,d channels · ${liveCategories.size} categories".format(activePlaylist!!.channelCount)
                } else {
                    "Connect an account to load channels"
                }
            }
            AppScreen.MOVIES -> {
                if (activePlaylist != null && activePlaylist!!.vodCount > 0) {
                    "%,d movies · ${movieCategories.size} genres".format(activePlaylist!!.vodCount)
                } else {
                    "Connect an account to load movies"
                }
            }
            AppScreen.SERIES -> {
                if (activePlaylist != null && activePlaylist!!.seriesCount > 0) {
                    "%,d series · ${seriesCategories.size} genres".format(activePlaylist!!.seriesCount)
                } else {
                    "Connect an account to load series"
                }
            }
            AppScreen.FAVORITES -> {
                val liveFavs = favorites.count { it.streamType.equals("live", ignoreCase = true) }
                "${favorites.size} saved · $liveFavs live TV"
            }
            AppScreen.PLAYLISTS -> activePlaylist?.name ?: "No account connected"
            AppScreen.GLOBAL_SEARCH -> "Channels, movies, series and teams"
            AppScreen.PARENTAL_SETTINGS -> "PIN and locked categories"
            AppScreen.SETTINGS -> activePlaylist?.let { "Signed in to ${it.name}" } ?: "Account, lock, TV pairing, zone"
        }
    }
    val avatarInitial = remember(activePlaylist) {
        activePlaylist?.name?.trim()?.firstOrNull()?.uppercaseChar()?.toString() ?: "S"
    }
    val selectedCountryZone by mainViewModel.selectedCountryZone.collectAsStateWithLifecycle()

    var isFullScreenPlayer by remember { mutableStateOf(false) }
    // TV rail takes focus once at launch, never again on later re-compositions.
    var tvRailInitialFocusDone by remember { mutableStateOf(false) }
    var showTvSyncSheet by remember { mutableStateOf(false) }
    var openTvSyncSheetOnHostTab by remember { mutableStateOf(false) }
    var pendingStreamToPlay by remember { mutableStateOf<StreamEntity?>(null) }
    var pendingUnlockCallback by remember { mutableStateOf<(() -> Unit)?>(null) }
    var showPinDialog by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()

    val editionOverride by mainViewModel.editionOverride.collectAsStateWithLifecycle()
    val activeEdition = remember(editionOverride, context) {
        com.example.util.EditionResolver.resolveEdition(context)
    }

    var lastTvBackPressEpochMs by remember { androidx.compose.runtime.mutableLongStateOf(0L) }
    var showTvExitConfirmBanner by remember { mutableStateOf(false) }

    LaunchedEffect(showTvExitConfirmBanner) {
        if (showTvExitConfirmBanner) {
            kotlinx.coroutines.delay(2500L)
            showTvExitConfirmBanner = false
        }
    }

    LaunchedEffect(activeEdition) {
        if (activeEdition == com.example.util.SetPlayEdition.TV && tvSyncState.role != SyncRole.TV_HOST) {
            mainViewModel.startTvHostMode()
        }
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                mainViewModel.refreshTvSyncStatusOnResume()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(isFullScreenPlayer) {
        mainViewModel.isTvInFullScreenPlayback = isFullScreenPlayer
    }

    LaunchedEffect(
        playerUiState.currentStream?.name,
        playerUiState.currentStream?.streamId,
        playerUiState.currentStream?.streamType,
        playerUiState.currentPositionMs,
        playerUiState.isPlaying,
        playerUiState.selectedWyzieLanguageCode,
        playerUiState.lastSelectedSubtitleLanguageCode
    ) {
        mainViewModel.tvSyncManager.tvPlaybackStatusProvider = {
            val latestPlayerState = playerViewModel.uiState.value
            mainViewModel.tvSyncState.value.copy(
                tvNowPlayingTitle = latestPlayerState.currentStream?.name.orEmpty(),
                tvNowPlayingStreamId = latestPlayerState.currentStream?.streamId?.toString().orEmpty(),
                tvNowPlayingType = latestPlayerState.currentStream?.streamType.orEmpty(),
                tvNowPlayingPositionMs = latestPlayerState.currentPositionMs,
                tvIsPlaying = latestPlayerState.isPlaying,
                tvActiveSubtitleLang = (latestPlayerState.selectedWyzieLanguageCode ?: latestPlayerState.lastSelectedSubtitleLanguageCode).orEmpty()
            )
        }
    }

    LaunchedEffect(mainViewModel.tvSyncManager) {
        mainViewModel.tvSyncManager.onControlCommandReceived = { cmd ->
            if (mainViewModel.tvSyncState.value.role == SyncRole.TV_HOST) {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main.immediate) {
                    when (cmd.action.uppercase()) {
                        "TOGGLE_PLAY_PAUSE" -> playerViewModel.togglePlayPause()
                        "STOP" -> {
                            playerViewModel.stopAndClosePlayer()
                            isFullScreenPlayer = false
                        }
                        "ZAP_NEXT" -> playerViewModel.zapChannelOffset(1)
                        "ZAP_PREV" -> playerViewModel.zapChannelOffset(-1)
                        "SEEK_RELATIVE" -> {
                            if (cmd.seekSeconds != 0) {
                                playerViewModel.seekRelative(cmd.seekSeconds.toLong())
                            }
                        }
                        "SET_SUBTITLE_LANG" -> {
                            val lang = cmd.value.takeIf { !it.equals("off", ignoreCase = true) && it.isNotBlank() }
                            playerViewModel.chooseSubtitleLanguageFromPrompt(lang)
                        }
                    }
                }
            }
        }
        launch {
            mainViewModel.tvSyncManager.incomingPlayCommands.collect { cmd ->
                if (mainViewModel.tvSyncState.value.role == SyncRole.TV_HOST) {
                    val resolved = mainViewModel.resolveOrBuildStreamForTvCommand(cmd)
                    if (resolved != null) {
                        val (stream, playlist) = resolved
                        isFullScreenPlayer = true
                        playerViewModel.playStream(stream, playlist)
                        if (cmd.preferredSubtitleLang.isNotBlank()) {
                            playerViewModel.chooseSubtitleLanguageFromPrompt(cmd.preferredSubtitleLang)
                        }
                    }
                }
            }
        }
    }

    // If Android System PiP is active, show only the clean video surface
    if (isInSystemPipMode && playerUiState.currentStream != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = playerViewModel.player
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    }
                },
                update = { view ->
                    view.player = playerViewModel.player
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        return
    }

    // One Back rule everywhere:
    // 1. In the fullscreen player -> close full screen; on the phone playback continues in the now-playing bar
    // 2. On Accounts / Parental lock (opened from Settings) -> back to Settings
    // 3. On any other section -> back to KickOff
    // 4. On KickOff, TV edition -> "Press Back again to exit" (2.5 s window)
    BackHandler(
        enabled = isFullScreenPlayer ||
            currentScreen != AppScreen.HOME ||
            activeEdition == com.example.util.SetPlayEdition.TV
    ) {
        if (isFullScreenPlayer) {
            isFullScreenPlayer = false
        } else if (currentScreen == AppScreen.PLAYLISTS || currentScreen == AppScreen.PARENTAL_SETTINGS) {
            mainViewModel.setScreen(AppScreen.SETTINGS)
        } else if (currentScreen != AppScreen.HOME) {
            mainViewModel.setScreen(AppScreen.HOME)
        } else if (activeEdition == com.example.util.SetPlayEdition.TV) {
            val now = System.currentTimeMillis()
            if (now - lastTvBackPressEpochMs <= 2500L) {
                activity?.finish()
            } else {
                lastTvBackPressEpochMs = now
                showTvExitConfirmBanner = true
            }
        }
    }

    // Function to safely attempt playback with parental check & Single-Connection Safe TV Sync routing
    val requestPlayStream: (StreamEntity) -> Unit = { stream ->
        scope.launch {
            val isLocked = stream.isLocked || mainViewModel.repository.isCategoryLocked(stream.categoryId)
            if (isLocked && !isParentalUnlocked) {
                pendingUnlockCallback = null
                pendingStreamToPlay = stream
                showPinDialog = true
            } else if (tvSyncState.role == SyncRole.PHONE_CONTROLLER && tvSyncState.isSyncActive) {
                // Single-Connection Safe: stop local phone player and wait 300ms for socket release BEFORE delegating stream to TV Host
                val wasLocalPlayerActive = playerUiState.currentStream != null
                playerViewModel.stopAndClosePlayer()
                isFullScreenPlayer = false
                if (wasLocalPlayerActive) {
                    kotlinx.coroutines.delay(300L)
                }
                mainViewModel.playStreamOnSyncedTv(stream, activePlaylist)
            } else {
                isFullScreenPlayer = true
                activePlaylist?.let { playlist ->
                    playerViewModel.playStream(stream, playlist)
                }
            }
        }
    }

    // Function to gate browsing a locked category behind Parental PIN when Parental Lock is active
    val requestUnlockCategory: (String, () -> Unit) -> Unit = { _, onUnlocked ->
        if (!isParentalUnlocked) {
            pendingStreamToPlay = null
            pendingUnlockCallback = onUnlocked
            showPinDialog = true
        } else {
            onUnlocked()
        }
    }

    // Fullscreen Media3 Player Overlay
    if (isFullScreenPlayer && playerUiState.currentStream != null && activePlaylist != null) {
        PlayerScreen(
            viewModel = playerViewModel,
            playlist = activePlaylist!!,
            onBack = {
                isFullScreenPlayer = false
            },
            onEnterInAppPip = {
                isFullScreenPlayer = false
            },
            onToggleFavorite = { stream ->
                scope.launch {
                    mainViewModel.repository.toggleFavorite(stream.id, !stream.isFavorite)
                }
            },
            activeGoalAlert = activeGoalAlert,
            onWatchGoalMatch = { match ->
                mainViewModel.dismissGoalAlert()
                mainViewModel.onWatchMatchOnBeinClicked(
                    match = match,
                    onAutoPlaySingleStream = { stream ->
                        requestPlayStream(stream)
                    }
                )
            },
            onDismissGoalAlert = {
                mainViewModel.dismissGoalAlert()
            },
            canSendToTv = activeEdition == com.example.util.SetPlayEdition.PHONE &&
                (tvSyncState.isSyncActive || mainViewModel.tvSyncManager.hasSavedTvHost()),
            activeEdition = activeEdition,
            onSendToTv = {
                val streamToHandover = playerUiState.currentStream?.copy(
                    lastPositionMs = playerUiState.currentPositionMs
                )
                playerViewModel.stopAndClosePlayer()
                isFullScreenPlayer = false
                if (streamToHandover != null) {
                    scope.launch {
                        // Strict 300ms socket release mutex before TV connects to IPTV server
                        kotlinx.coroutines.delay(300L)
                        if (tvSyncState.role == SyncRole.PHONE_CONTROLLER && tvSyncState.isSyncActive) {
                            mainViewModel.playStreamOnSyncedTv(streamToHandover, activePlaylist)
                        } else if (mainViewModel.tvSyncManager.hasSavedTvHost()) {
                            mainViewModel.reconnectToLastTv {
                                mainViewModel.playStreamOnSyncedTv(streamToHandover, activePlaylist)
                            }
                        } else {
                            openTvSyncSheetOnHostTab = false
                            showTvSyncSheet = true
                        }
                    }
                }
            }
        )
    } else {
        // One Dark Graphite base for every section; the section palettes stay as accents only.
        val sectionBaseColor = DarkGraphite

        val renderMainScreenContent: @Composable (Modifier) -> Unit = { containerModifier ->
            BoxWithConstraints(
                modifier = containerModifier.fillMaxSize()
            ) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        val forward = targetState.ordinal >= initialState.ordinal
                        val enterSlide = slideInHorizontally(
                            animationSpec = tween(durationMillis = 260),
                            initialOffsetX = { fullWidth -> if (forward) fullWidth / 14 else -fullWidth / 14 }
                        ) + fadeIn(animationSpec = tween(durationMillis = 240))
                        val exitSlide = slideOutHorizontally(
                            animationSpec = tween(durationMillis = 190),
                            targetOffsetX = { fullWidth -> if (forward) -fullWidth / 18 else fullWidth / 18 }
                        ) + fadeOut(animationSpec = tween(durationMillis = 160))
                        enterSlide togetherWith exitSlide
                    },
                    label = "luxury_section_transition",
                    modifier = Modifier.fillMaxSize()
                ) { targetScreen ->
                    when (targetScreen) {
                        AppScreen.HOME -> HomeScreen(
                            mainViewModel = mainViewModel,
                            onStreamClick = requestPlayStream,
                            onNavigateScreen = { screen ->
                                mainViewModel.setScreen(screen)
                            }
                        )
                        AppScreen.LIVE_TV -> LiveTvScreen(
                            viewModel = liveTvViewModel,
                            activePlaylist = activePlaylist,
                            onChannelClick = requestPlayStream,
                            isParentalUnlocked = isParentalUnlocked,
                            onRequestUnlockCategory = { category, onUnlocked ->
                                requestUnlockCategory(category.categoryId, onUnlocked)
                            }
                        )
                        AppScreen.MOVIES -> VodScreen(
                            sectionTab = VodTab.MOVIES,
                            viewModel = vodViewModel,
                            activePlaylist = activePlaylist,
                            onItemClick = requestPlayStream,
                            isParentalUnlocked = isParentalUnlocked,
                            onRequestUnlockCategory = requestUnlockCategory,
                            recentlyWatched = recentlyWatched
                        )
                        AppScreen.SERIES -> VodScreen(
                            sectionTab = VodTab.SERIES,
                            viewModel = vodViewModel,
                            activePlaylist = activePlaylist,
                            onItemClick = requestPlayStream,
                            isParentalUnlocked = isParentalUnlocked,
                            onRequestUnlockCategory = requestUnlockCategory,
                            recentlyWatched = recentlyWatched,
                            onSeriesEpisodeQueueBuilt = { queue ->
                                playerViewModel.setSeriesEpisodeQueue(queue)
                            }
                        )
                        AppScreen.FAVORITES -> FavoritesScreen(
                            viewModel = mainViewModel,
                            activePlaylist = activePlaylist,
                            onStreamClick = requestPlayStream,
                            onToggleFavorite = { stream ->
                                scope.launch {
                                    mainViewModel.repository.toggleFavorite(stream.id, !stream.isFavorite)
                                }
                            }
                        )
                        AppScreen.GLOBAL_SEARCH -> GlobalSearchScreen(
                            viewModel = mainViewModel,
                            activePlaylist = activePlaylist,
                            onStreamClick = requestPlayStream
                        )
                        AppScreen.PLAYLISTS -> PlaylistsScreen(
                            viewModel = mainViewModel
                        )
                        AppScreen.PARENTAL_SETTINGS -> ParentalControlsScreen(
                            viewModel = mainViewModel,
                            activePlaylist = activePlaylist
                        )
                        AppScreen.SETTINGS -> SettingsScreen(
                            activePlaylist = activePlaylist,
                            tvSyncState = tvSyncState,
                            activeEdition = activeEdition,
                            selectedZoneLabel = selectedCountryZone.countryName,
                            onOpenAccounts = { mainViewModel.setScreen(AppScreen.PLAYLISTS) },
                            onOpenParentalLock = { mainViewModel.setScreen(AppScreen.PARENTAL_SETTINGS) },
                            onOpenTvPairing = {
                                openTvSyncSheetOnHostTab = activeEdition == com.example.util.SetPlayEdition.TV
                                showTvSyncSheet = true
                            },
                            onOpenBroadcastZone = {
                                mainViewModel.openCountryZoneModal()
                                mainViewModel.setScreen(AppScreen.HOME)
                            },
                            onSwitchEdition = { edition -> mainViewModel.setEditionOverride(edition) }
                        )
                    }
                }

                // 15-Minute Halftime Return Banner when browsing VOD/Live TV outside fullscreen player
                if (playerUiState.showHalftimeReturnPrompt && playerUiState.halftimeSavedStream != null) {
                    val savedChannelTitle = playerUiState.halftimeSavedStream?.name ?: "Match Channel"
                    Surface(
                        color = Color(0xF0101622),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, AmberGold),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                            .testTag("halftime_return_banner")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "⚽ 2nd Half Starting — Return to $savedChannelTitle",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Surface(
                                color = AmberGold,
                                shape = RoundedCornerShape(50),
                                modifier = Modifier
                                    .clickable {
                                        playerViewModel.returnToHalftimeChannel()
                                        isFullScreenPlayer = true
                                    }
                                    .testTag("halftime_return_confirm_button")
                            ) {
                                Text(
                                    text = "Return Now",
                                    color = Color.Black,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                            IconButton(
                                onClick = { playerViewModel.dismissHalftimeReturnPrompt() },
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // 2-Stage TV Back-Button Exit Guard Banner
                if (showTvExitConfirmBanner && activeEdition == com.example.util.SetPlayEdition.TV) {
                    Surface(
                        color = Color(0xF50B131E),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.2.dp, AmberGold),
                        shadowElevation = 12.dp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 14.dp)
                            .testTag("tv_exit_confirm_banner")
                    ) {
                        Text(
                            text = "Press Back again to exit SetPlay TV",
                            color = PaleGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                        )
                    }
                }

                // While a stream plays outside the full-screen player, the phone shows
                // PhoneNowPlayingBar above the tabs (see the Scaffold bottomBar).
            }
        }

        if (activeEdition == com.example.util.SetPlayEdition.TV) {
            // =========================================================================
            // ANDROID TV SHELL: collapsible icon rail + content
            // =========================================================================
            val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
            var moveFocusIntoContentTick by remember { androidx.compose.runtime.mutableIntStateOf(0) }
            LaunchedEffect(moveFocusIntoContentTick) {
                if (moveFocusIntoContentTick > 0) {
                    // Wait for the section transition, then hand focus from the rail to the content.
                    kotlinx.coroutines.delay(320L)
                    runCatching { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Right) }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(sectionBaseColor)
                    .testTag("main_scaffold")
            ) {
                TvLeftNavigationRail(
                    requestInitialFocus = !tvRailInitialFocusDone,
                    onInitialFocusRequested = { tvRailInitialFocusDone = true },
                    currentScreen = currentScreen,
                    liveMatchesCount = liveMatchesCount,
                    favoritesCount = favorites.size,
                    tvSyncState = tvSyncState,
                    onScreenSelected = { screen ->
                        mainViewModel.setScreen(screen)
                        moveFocusIntoContentTick++
                    },
                    onOpenTvSyncQr = {
                        mainViewModel.startTvHostMode()
                        openTvSyncSheetOnHostTab = true
                        showTvSyncSheet = true
                    }
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        // Coming back from the rail with → returns to the item last focused in this section.
                        .tvContentFocusRestorer()
                ) {
                    if (currentScreen.isCinema()) {
                        CinemaSegmentedToggle(
                            selected = if (currentScreen == AppScreen.SERIES) VodTab.SERIES else VodTab.MOVIES,
                            onSelect = { tab ->
                                mainViewModel.setScreen(if (tab == VodTab.SERIES) AppScreen.SERIES else AppScreen.MOVIES)
                            },
                            modifier = Modifier
                                .padding(start = 28.dp, top = 16.dp, end = 28.dp)
                                .width(260.dp)
                        )
                    }
                    renderMainScreenContent(Modifier.weight(1f))
                    TvRemoteHintBar(
                        backLabel = when (currentScreen) {
                            AppScreen.HOME -> "Exit"
                            AppScreen.PLAYLISTS, AppScreen.PARENTAL_SETTINGS -> "Settings"
                            else -> "KickOff"
                        }
                    )
                }
            }
        } else {
            // =========================================================================
            // PHONE & TABLET SHELL: title bar + content + five tabs
            // =========================================================================
            Scaffold(
                containerColor = sectionBaseColor,
                topBar = {
                    SectionTopBar(
                        currentScreen = currentScreen,
                        sectionDetailSummary = topSectionDetailText,
                        avatarInitial = avatarInitial,
                        isTvSyncActive = tvSyncState.isSyncActive,
                        halftimeRemainingSeconds = playerUiState.halftimeTimerRemainingSeconds,
                        onReturnToHalftimeMatch = {
                            playerViewModel.returnToHalftimeChannel()
                            isFullScreenPlayer = true
                        },
                        onTvSyncClick = {
                            openTvSyncSheetOnHostTab = false
                            showTvSyncSheet = true
                        },
                        onSettingsClick = { mainViewModel.setScreen(AppScreen.SETTINGS) },
                        onCinemaTabSelected = { tab ->
                            mainViewModel.setScreen(if (tab == VodTab.SERIES) AppScreen.SERIES else AppScreen.MOVIES)
                        }
                    )
                },
                bottomBar = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (tvSyncState.role == SyncRole.PHONE_CONTROLLER && tvSyncState.isSyncActive) {
                            TvSyncCompanionBar(
                                syncState = tvSyncState,
                                onTogglePlayPauseOnTv = {
                                    mainViewModel.sendControlToSyncedTv(action = "TOGGLE_PLAY_PAUSE")
                                },
                                onZapPrevOnTv = {
                                    mainViewModel.sendControlToSyncedTv(action = "ZAP_PREV")
                                },
                                onZapNextOnTv = {
                                    mainViewModel.sendControlToSyncedTv(action = "ZAP_NEXT")
                                },
                                onPullStreamToPhone = {
                                    mainViewModel.pullStreamFromTvToPhone { pulledStream, playlist ->
                                        isFullScreenPlayer = true
                                        playerViewModel.playStream(pulledStream, playlist)
                                    }
                                },
                                onLeaveSyncMode = {
                                    mainViewModel.leaveTvSyncMode()
                                },
                                onOpenSyncSheet = {
                                    openTvSyncSheetOnHostTab = false
                                    showTvSyncSheet = true
                                }
                            )
                        }
                        if (playerUiState.currentStream != null && !isFullScreenPlayer) {
                            PhoneNowPlayingBar(
                                playerViewModel = playerViewModel,
                                stream = playerUiState.currentStream!!,
                                isPlaying = playerUiState.isPlaying,
                                resolutionLabel = playerUiState.resolutionLabel,
                                onExpandFullScreen = { isFullScreenPlayer = true },
                                onTogglePlayPause = { playerViewModel.togglePlayPause() },
                                onStop = {
                                    playerViewModel.stopAndClosePlayer()
                                    isFullScreenPlayer = false
                                }
                            )
                        }
                        SectionBottomNav(
                            currentScreen = currentScreen,
                            liveMatchesCount = liveMatchesCount,
                            onScreenSelected = { screen ->
                                mainViewModel.setScreen(screen)
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("main_scaffold")
            ) { innerPadding ->
                renderMainScreenContent(Modifier.padding(innerPadding))
            }
        }
    }

    if (showTvSyncSheet) {
        TvSyncModalSheet(
            syncState = tvSyncState,
            qrPayload = mainViewModel.tvSyncManager.buildQrPairingPayload(),
            hasSavedTvHost = mainViewModel.tvSyncManager.hasSavedTvHost(),
            savedTvHostName = mainViewModel.tvSyncManager.getSavedTvHostName(),
            savedTvHostSummary = mainViewModel.tvSyncManager.getSavedTvHostSummary(),
            statusErrorMessage = tvSyncErrorMessage,
            activeEdition = activeEdition,
            defaultToTvHostTab = openTvSyncSheetOnHostTab,
            onStartTvHostMode = { mainViewModel.startTvHostMode() },
            onStartPhoneDiscovery = { mainViewModel.startPhoneSyncDiscovery() },
            onConnectToTv = { ip, port, pin ->
                mainViewModel.connectPhoneToTv(
                    ip = ip,
                    port = port,
                    pin = pin,
                    onSuccess = {
                        playerViewModel.stopAndClosePlayer()
                        isFullScreenPlayer = false
                        showTvSyncSheet = false
                    }
                )
            },
            onReconnectLastTv = {
                mainViewModel.reconnectToLastTv(
                    onSuccess = {
                        playerViewModel.stopAndClosePlayer()
                        isFullScreenPlayer = false
                        showTvSyncSheet = false
                    }
                )
            },
            onLeaveSyncMode = {
                mainViewModel.leaveTvSyncMode()
            },
            onDismiss = {
                showTvSyncSheet = false
            }
        )
    }

    // Salted SHA-256 Parental PIN Challenge Modal with Brute-Force Rate Limiting
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = {
                showPinDialog = false
                pendingStreamToPlay = null
                pendingUnlockCallback = null
                enteredPin = ""
                pinError = null
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = AmberGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Parental PIN Required", color = PaleGold, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        if (pendingStreamToPlay != null) {
                            "This stream belongs to a locked category. Enter your 4-digit PIN to watch (Default: 0000):"
                        } else {
                            "This category is protected by Parental Lock. Enter your 4-digit PIN to browse (Default: 0000):"
                        },
                        color = Champagne,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = { if (it.length <= 4 && it.all(Char::isDigit)) enteredPin = it },
                        placeholder = { Text("4 Digits", color = Champagne.copy(alpha = 0.6f)) },
                        singleLine = true,
                        isError = pinError != null,
                        supportingText = pinError?.let { { Text(it, color = LiveRed) } },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberGold,
                            unfocusedBorderColor = Champagne.copy(alpha = 0.4f),
                            focusedContainerColor = DarkGraphite,
                            unfocusedContainerColor = DarkGraphite,
                            focusedTextColor = PaleGold,
                            unfocusedTextColor = PaleGold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pin_input_field")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val lockoutSec = SecurityManager.getPinLockoutRemainingSeconds()
                            if (lockoutSec > 0) {
                                pinError = "Too many failed attempts. Try again in ${lockoutSec}s."
                                return@launch
                            }
                            if (mainViewModel.repository.verifyParentalPin(enteredPin)) {
                                mainViewModel.unlockParentalSession()
                                showPinDialog = false
                                pendingStreamToPlay?.let { stream ->
                                    if (tvSyncState.role == SyncRole.PHONE_CONTROLLER && tvSyncState.isSyncActive) {
                                        playerViewModel.stopAndClosePlayer()
                                        isFullScreenPlayer = false
                                        mainViewModel.playStreamOnSyncedTv(stream, activePlaylist)
                                    } else {
                                        isFullScreenPlayer = true
                                        activePlaylist?.let { playlist ->
                                            playerViewModel.playStream(stream, playlist)
                                        }
                                    }
                                }
                                pendingUnlockCallback?.invoke()
                                pendingStreamToPlay = null
                                pendingUnlockCallback = null
                                enteredPin = ""
                                pinError = null
                            } else {
                                val newLockout = SecurityManager.getPinLockoutRemainingSeconds()
                                pinError = if (newLockout > 0) {
                                    "PIN locked for ${newLockout}s due to repeated failures."
                                } else {
                                    "Incorrect PIN. Try again."
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGold, contentColor = DarkGraphite),
                    modifier = Modifier.testTag("pin_submit_button")
                ) {
                    Text(
                        if (pendingStreamToPlay != null) "Unlock & Play" else "Unlock Category",
                        color = DarkGraphite,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPinDialog = false
                    pendingStreamToPlay = null
                    pendingUnlockCallback = null
                    enteredPin = ""
                    pinError = null
                }) {
                    Text("Cancel", color = Champagne)
                }
            },
            containerColor = MetalCharcoal
        )
    }
}

/**
 * Phone "now playing" bar, docked above the tabs while a stream plays and the
 * player is not full screen. Replaces the draggable floating window: a small
 * live picture, the channel, pause/play and stop. Tapping the bar returns to
 * the full-screen player.
 */
@OptIn(UnstableApi::class)
@Composable
fun PhoneNowPlayingBar(
    playerViewModel: PlayerViewModel,
    stream: StreamEntity,
    isPlaying: Boolean,
    resolutionLabel: String,
    onExpandFullScreen: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onStop: () -> Unit
) {
    Surface(
        color = MetalCharcoalElevated,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, ChampagneSubtleBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onExpandFullScreen() }
            .testTag("mini_player_dock")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 6.dp, bottom = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 72.dp, height = 40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = playerViewModel.player
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        }
                    },
                    update = { playerView -> playerView.player = playerViewModel.player },
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stream.name,
                    color = PaleGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = listOf(
                        if (stream.streamType.equals("live", ignoreCase = true)) "Live" else "Playing",
                        resolutionLabel
                    ).filter { it.isNotBlank() }.joinToString(" · "),
                    color = ChampagneMuted,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AmberGold)
                    .clickable { onTogglePlayPause() }
                    .testTag("mini_player_play_pause")
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = DarkGraphite,
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(
                onClick = onStop,
                modifier = Modifier.testTag("mini_player_stop")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Stop",
                    tint = ChampagneMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Floating Draggable In-App Picture-in-Picture (PiP) Video Window.
 */
@OptIn(UnstableApi::class)
@Composable
fun FloatingInAppPipWindow(
    playerViewModel: PlayerViewModel,
    stream: StreamEntity,
    isPlaying: Boolean,
    resolutionLabel: String,
    maxContainerWidthPx: Float,
    maxContainerHeightPx: Float,
    onExpandFullScreen: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onEnterSystemPip: () -> Unit,
    onCloseAndStop: () -> Unit
) {
    val density = LocalDensity.current
    var isLargePip by remember { mutableStateOf(false) }

    val pipWidthDp = if (isLargePip) 268.dp else 216.dp
    val pipHeightDp = if (isLargePip) 168.dp else 138.dp

    val pipWidthPx = with(density) { pipWidthDp.toPx() }
    val pipHeightPx = with(density) { pipHeightDp.toPx() }
    val marginPx = with(density) { 12.dp.toPx() }

    val defaultX = (maxContainerWidthPx - pipWidthPx - marginPx).coerceAtLeast(marginPx)
    val defaultY = (maxContainerHeightPx - pipHeightPx - marginPx).coerceAtLeast(marginPx)

    var offsetX by remember(isLargePip) { mutableFloatStateOf(defaultX) }
    var offsetY by remember(isLargePip) { mutableFloatStateOf(defaultY) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MetalCharcoal),
        border = BorderStroke(1.5.dp, AmberGold),
        elevation = CardDefaults.cardElevation(defaultElevation = 14.dp),
        modifier = Modifier
            .offset {
                IntOffset(
                    x = offsetX.roundToInt().coerceIn(
                        0,
                        (maxContainerWidthPx - pipWidthPx).roundToInt().coerceAtLeast(0)
                    ),
                    y = offsetY.roundToInt().coerceIn(
                        0,
                        (maxContainerHeightPx - pipHeightPx).roundToInt().coerceAtLeast(0)
                    )
                )
            }
            .size(width = pipWidthDp, height = pipHeightDp)
            .pointerInput(maxContainerWidthPx, maxContainerHeightPx, isLargePip) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val maxX = (maxContainerWidthPx - pipWidthPx).coerceAtLeast(0f)
                    val maxY = (maxContainerHeightPx - pipHeightPx).coerceAtLeast(0f)
                    offsetX = (offsetX + dragAmount.x).coerceIn(0f, maxX)
                    offsetY = (offsetY + dragAmount.y).coerceIn(0f, maxY)
                }
            }
            .testTag("mini_player_dock")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black)
                    .clickable { onExpandFullScreen() }
            ) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = playerViewModel.player
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        }
                    },
                    update = { playerView ->
                        playerView.player = playerViewModel.player
                    },
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                ) {
                    Surface(
                        color = DarkGraphite.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(0.5.dp, AmberGold)
                    ) {
                        Text(
                            text = if (isPlaying) "PiP LIVE" else "PAUSED",
                            color = AmberGold,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                    if (resolutionLabel.isNotEmpty()) {
                        Surface(
                            color = PaleGold,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = resolutionLabel,
                                color = DarkGraphite,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onCloseAndStop,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(26.dp)
                        .background(DarkGraphite.copy(alpha = 0.85f), CircleShape)
                        .border(0.5.dp, Champagne.copy(alpha = 0.5f), CircleShape)
                        .testTag("pip_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Player",
                        tint = PaleGold,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .background(MetalCharcoal)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = com.example.util.StreamMetadataParser.cleanDisplayTitle(stream.name),
                    color = PaleGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onExpandFullScreen() }
                )

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onTogglePlayPause,
                    modifier = Modifier
                        .size(24.dp)
                        .background(AmberGold, CircleShape)
                        .testTag("pip_play_pause_button")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = DarkGraphite,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Spacer(modifier = Modifier.width(3.dp))

                IconButton(
                    onClick = { isLargePip = !isLargePip },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoSizeSelectLarge,
                        contentDescription = "Resize PiP",
                        tint = Champagne,
                        modifier = Modifier.size(14.dp)
                    )
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    IconButton(
                        onClick = onEnterSystemPip,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureInPictureAlt,
                            contentDescription = "System PiP",
                            tint = AmberGold,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onExpandFullScreen,
                    modifier = Modifier
                        .size(24.dp)
                        .testTag("pip_expand_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInFull,
                        contentDescription = "Expand Fullscreen",
                        tint = PaleGold,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
