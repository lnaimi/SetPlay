package com.example

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.sync.TvSyncSessionState
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CatchupGreen
import com.example.ui.theme.Champagne
import com.example.ui.theme.ChampagneMuted
import com.example.ui.theme.ChampagneSubtleBorder
import com.example.ui.theme.DarkGraphite
import com.example.ui.theme.FavoritesSagePrimary
import com.example.ui.theme.KickoffPitchEmerald
import com.example.ui.theme.LiveRed
import com.example.ui.theme.MetalCharcoal
import com.example.ui.theme.MetalCharcoalElevated
import com.example.ui.theme.MoviesCyanTeal
import com.example.ui.theme.PaleGold
import com.example.ui.theme.TvBroadcastScarlet
import com.example.ui.theme.tvFocusableControl
import com.example.ui.viewmodel.AppScreen
import com.example.ui.vod.VodTab

// =============================================================================
// SetPlay navigation shell — shared by the phone and Android TV editions.
//
// One set of five sections on both devices:
//   KickOff · Live TV · Cinema (Movies | Series) · Favorites · Search
// plus Settings (accounts, parental lock, TV pairing, edition) reached from the
// avatar on phone and from the end of the rail on TV.
//
// Theme values come from the Royal Gold scheme (Color.kt). Focus and touch
// behaviour comes from TvFocusModifiers.kt. The section accents are used only on
// the count line and badges; the base stays Dark Graphite everywhere.
// =============================================================================

/** Movies and Series are two states of one "Cinema" section. */
fun AppScreen.isCinema(): Boolean = this == AppScreen.MOVIES || this == AppScreen.SERIES

/** The five sections shown as tabs, in order. */
private val PRIMARY_SECTIONS = listOf(
    AppScreen.HOME,
    AppScreen.LIVE_TV,
    AppScreen.MOVIES,
    AppScreen.FAVORITES,
    AppScreen.GLOBAL_SEARCH
)

fun sectionTitle(screen: AppScreen): String = when (screen) {
    AppScreen.HOME -> "KickOff"
    AppScreen.LIVE_TV -> "Live TV"
    AppScreen.MOVIES, AppScreen.SERIES -> "Cinema"
    AppScreen.FAVORITES -> "Favorites"
    AppScreen.GLOBAL_SEARCH -> "Search"
    AppScreen.PLAYLISTS -> "Accounts & servers"
    AppScreen.PARENTAL_SETTINGS -> "Parental lock"
    AppScreen.SETTINGS -> "Settings"
}

fun sectionAccent(screen: AppScreen): Color = when (screen) {
    AppScreen.HOME -> KickoffPitchEmerald
    AppScreen.LIVE_TV -> TvBroadcastScarlet
    AppScreen.MOVIES, AppScreen.SERIES -> MoviesCyanTeal
    AppScreen.FAVORITES -> FavoritesSagePrimary
    else -> AmberGold
}

private fun sectionIcon(screen: AppScreen, selected: Boolean): ImageVector = when (screen) {
    AppScreen.HOME -> if (selected) Icons.Filled.SportsSoccer else Icons.Outlined.SportsSoccer
    AppScreen.LIVE_TV -> if (selected) Icons.Filled.LiveTv else Icons.Outlined.LiveTv
    AppScreen.MOVIES, AppScreen.SERIES -> if (selected) Icons.Filled.Movie else Icons.Outlined.Movie
    AppScreen.FAVORITES -> if (selected) Icons.Filled.Star else Icons.Outlined.StarOutline
    AppScreen.GLOBAL_SEARCH -> if (selected) Icons.Filled.Search else Icons.Outlined.Search
    else -> Icons.Filled.Settings
}

private fun sectionTestTag(screen: AppScreen): String = when (screen) {
    AppScreen.HOME -> "nav_home"
    AppScreen.LIVE_TV -> "nav_live_tv"
    AppScreen.MOVIES, AppScreen.SERIES -> "nav_movies"
    AppScreen.FAVORITES -> "nav_favorites"
    AppScreen.GLOBAL_SEARCH -> "top_search_btn"
    else -> "nav_settings"
}

/** Whether [tab] is the active tab for [current] (Cinema covers both Movies and Series). */
private fun isTabSelected(tab: AppScreen, current: AppScreen): Boolean =
    if (tab.isCinema()) current.isCinema() else tab == current

// -----------------------------------------------------------------------------
// Phone: section title bar
// -----------------------------------------------------------------------------

/**
 * Title bar for the phone edition: the section name at 26sp, a one-line count
 * underneath, the Cinema switch when on Movies/Series, and on the right the
 * halftime return badge (when counting), a "TV" chip while paired, and the
 * account avatar that opens Settings.
 */
@Composable
fun SectionTopBar(
    currentScreen: AppScreen,
    sectionDetailSummary: String,
    avatarInitial: String,
    isTvSyncActive: Boolean,
    halftimeRemainingSeconds: Int?,
    onReturnToHalftimeMatch: () -> Unit,
    onTvSyncClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onCinemaTabSelected: (VodTab) -> Unit
) {
    val accent = sectionAccent(currentScreen)
    Surface(color = DarkGraphite, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sectionTitle(currentScreen),
                        color = PaleGold,
                        fontSize = 26.sp,
                        lineHeight = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.3).sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("section_title")
                    )
                    if (sectionDetailSummary.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(accent)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = sectionDetailSummary,
                                color = Champagne,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    if (halftimeRemainingSeconds != null && halftimeRemainingSeconds > 0) {
                        val mm = halftimeRemainingSeconds / 60
                        val ss = halftimeRemainingSeconds % 60
                        Surface(
                            color = AmberGold.copy(alpha = 0.20f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, AmberGold),
                            modifier = Modifier
                                .heightIn(min = 36.dp)
                                .tvFocusableControl(onClick = onReturnToHalftimeMatch)
                                .testTag("top_bar_halftime_countdown_badge")
                        ) {
                            Text(
                                text = String.format(java.util.Locale.US, "HT %02d:%02d", mm, ss),
                                color = AmberGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            )
                        }
                    }

                    if (isTvSyncActive) {
                        Surface(
                            color = MetalCharcoal,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, ChampagneSubtleBorder),
                            modifier = Modifier
                                .heightIn(min = 36.dp)
                                .tvFocusableControl(onClick = onTvSyncClick)
                                .testTag("top_tv_sync_btn")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(CatchupGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "TV",
                                    color = PaleGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(AmberGold, PaleGold)))
                            .tvFocusableControl(shape = CircleShape, onClick = onSettingsClick)
                            .testTag("top_settings_btn")
                    ) {
                        Text(
                            text = avatarInitial,
                            color = DarkGraphite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            if (currentScreen.isCinema()) {
                Spacer(modifier = Modifier.height(10.dp))
                CinemaSegmentedToggle(
                    selected = if (currentScreen == AppScreen.SERIES) VodTab.SERIES else VodTab.MOVIES,
                    onSelect = onCinemaTabSelected,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/** Movies | Series switch for the Cinema section. */
@Composable
fun CinemaSegmentedToggle(
    selected: VodTab,
    onSelect: (VodTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MetalCharcoal,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, ChampagneSubtleBorder),
        modifier = modifier.height(40.dp)
    ) {
        Row(modifier = Modifier.padding(3.dp)) {
            VodTab.entries.forEach { tab ->
                val isOn = tab == selected
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(if (isOn) MetalCharcoalElevated else Color.Transparent)
                        .tvFocusableControl(onClick = { onSelect(tab) })
                        .testTag(if (tab == VodTab.MOVIES) "cinema_tab_movies" else "cinema_tab_series")
                ) {
                    Text(
                        text = if (tab == VodTab.MOVIES) "Movies" else "Series",
                        color = if (isOn) PaleGold else Champagne,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// Phone: bottom tabs
// -----------------------------------------------------------------------------

/**
 * Five flat tabs with a single gold active state and one red badge for live
 * matches. The bar stays Metal Charcoal whatever the section.
 */
@Composable
fun SectionBottomNav(
    currentScreen: AppScreen,
    liveMatchesCount: Int,
    onScreenSelected: (AppScreen) -> Unit
) {
    Surface(color = MetalCharcoal, modifier = Modifier.fillMaxWidth()) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ChampagneSubtleBorder)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 4.dp, vertical = 6.dp)
                    .testTag("bottom_nav"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PRIMARY_SECTIONS.forEach { tab ->
                    val selected = isTabSelected(tab, currentScreen)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .tvFocusableControl(
                                shape = RoundedCornerShape(12.dp),
                                focusedScale = 1.0f,
                                onClick = { onScreenSelected(tab) }
                            )
                            .padding(vertical = 6.dp)
                            .testTag(sectionTestTag(tab))
                    ) {
                        Box {
                            Icon(
                                imageVector = sectionIcon(tab, selected),
                                contentDescription = sectionTitle(tab),
                                tint = if (selected) AmberGold else ChampagneMuted,
                                modifier = Modifier.size(24.dp)
                            )
                            if (tab == AppScreen.HOME && liveMatchesCount > 0) {
                                LiveCountBadge(
                                    count = liveMatchesCount,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(start = 14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = sectionTitle(tab),
                            color = if (selected) AmberGold else ChampagneMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveCountBadge(count: Int, modifier: Modifier = Modifier) {
    Surface(
        color = LiveRed,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Text(
            text = count.toString(),
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
        )
    }
}

// -----------------------------------------------------------------------------
// Android TV: section title bar
// -----------------------------------------------------------------------------

/**
 * Title bar for the TV edition: the section name at 28sp with its count line,
 * and an optional control on the right (the Movies | Series switch on Cinema).
 */
@Composable
fun TvSectionHeader(
    currentScreen: AppScreen,
    sectionDetailSummary: String,
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit = {}
) {
    val accent = sectionAccent(currentScreen)
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 28.dp, end = 28.dp, top = 20.dp, bottom = 8.dp)
            .testTag("tv_section_header")
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = sectionTitle(currentScreen),
                color = PaleGold,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.3).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (sectionDetailSummary.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(accent)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = sectionDetailSummary,
                        color = Champagne,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        trailing()
    }
}

// -----------------------------------------------------------------------------
// Android TV: remembered focus and the remote hint bar
// -----------------------------------------------------------------------------

/**
 * Makes the TV content area one focus group that remembers its last focused
 * child: pressing → from the rail lands back on the card you left, not on the
 * first item of the screen.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.tvContentFocusRestorer(): Modifier = this
    .focusRestorer()
    .focusGroup()

/**
 * Bottom strip on the TV edition naming what the remote buttons do here:
 * OK selects, ← opens the menu, Back returns to KickOff (or exits from KickOff).
 */
@Composable
fun TvRemoteHintBar(
    backLabel: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(22.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(MetalCharcoal)
            .padding(horizontal = 28.dp, vertical = 8.dp)
            .testTag("tv_remote_hint_bar")
    ) {
        RemoteHint(key = "OK", action = "Select")
        RemoteHint(key = "←", action = "Menu")
        RemoteHint(key = "Back", action = backLabel)
    }
}

@Composable
private fun RemoteHint(key: String, action: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            color = MetalCharcoalElevated,
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, ChampagneSubtleBorder)
        ) {
            Text(
                text = key,
                color = PaleGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = action, color = Champagne, fontSize = 12.sp)
    }
}

// -----------------------------------------------------------------------------
// Android TV: collapsible left rail
// -----------------------------------------------------------------------------

private val RAIL_COLLAPSED_WIDTH = 72.dp
private val RAIL_EXPANDED_WIDTH = 216.dp

/**
 * Left navigation rail for the TV edition (`testTag("tv_left_navigation_rail")`).
 *
 * - 72dp of icons by default; widens to 216dp with labels only while a rail item
 *   holds D-pad focus, so the content keeps the width.
 * - Focus is requested on the active item once, at first composition, never on
 *   every screen change: the remote keeps its place in the content.
 * - Sections: KickOff, Live TV, Cinema, Favorites, Search, Settings, and a
 *   pairing pod at the bottom (`tv_rail_sync_status_pod`) showing the PIN or the
 *   connected phone.
 */
@Composable
fun TvLeftNavigationRail(
    currentScreen: AppScreen,
    liveMatchesCount: Int,
    favoritesCount: Int,
    tvSyncState: TvSyncSessionState,
    onScreenSelected: (AppScreen) -> Unit,
    onOpenTvSyncQr: () -> Unit,
    modifier: Modifier = Modifier,
    requestInitialFocus: Boolean = true,
    onInitialFocusRequested: () -> Unit = {}
) {
    var railHasFocus by remember { mutableStateOf(false) }
    val railWidth by animateDpAsState(
        targetValue = if (railHasFocus) RAIL_EXPANDED_WIDTH else RAIL_COLLAPSED_WIDTH,
        animationSpec = tween(durationMillis = 180),
        label = "tv_rail_width"
    )
    val expanded = railWidth > (RAIL_COLLAPSED_WIDTH + RAIL_EXPANDED_WIDTH) / 2
    val initialFocusRequester = remember { FocusRequester() }

    // Only on the first launch: when the rail re-composes later (closing the player,
    // a sheet), focus stays where the remote left it.
    LaunchedEffect(Unit) {
        if (requestInitialFocus) {
            runCatching { initialFocusRequester.requestFocus() }
            onInitialFocusRequested()
        }
    }

    val entries = PRIMARY_SECTIONS + AppScreen.SETTINGS
    val isPhonePaired = tvSyncState.connectedPeerName.isNotBlank()

    Column(
        modifier = modifier
            .width(railWidth)
            .fillMaxHeight()
            .clipToBounds()
            .background(MetalCharcoal)
            .focusGroup()
            .onFocusChanged { railHasFocus = it.hasFocus }
            .padding(vertical = 14.dp)
            .testTag("tv_left_navigation_rail")
    ) {
        // Brand mark
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(46.dp)
                .padding(start = 20.dp, bottom = 10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(Brush.linearGradient(listOf(AmberGold, PaleGold)))
            ) {
                Text(text = "▶", color = DarkGraphite, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
            }
            if (expanded) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Set ▶ Play",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            entries.forEachIndexed { index, screen ->
                val selected = isTabSelected(screen, currentScreen)
                val isInitialTarget = selected || (index == 0 && entries.none { isTabSelected(it, currentScreen) })
                val badge = when {
                    screen == AppScreen.HOME && liveMatchesCount > 0 -> liveMatchesCount
                    screen == AppScreen.FAVORITES && favoritesCount > 0 -> favoritesCount
                    else -> null
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selected) MetalCharcoalElevated else Color.Transparent)
                        .then(if (isInitialTarget) Modifier.focusRequester(initialFocusRequester) else Modifier)
                        .tvFocusableControl(
                            shape = RoundedCornerShape(12.dp),
                            focusedScale = 1.0f,
                            onClick = { onScreenSelected(screen) }
                        )
                        .padding(vertical = 9.dp)
                        .padding(start = 16.dp)
                        .testTag(sectionTestTag(screen))
                ) {
                    Box {
                        Icon(
                            imageVector = sectionIcon(screen, selected),
                            contentDescription = sectionTitle(screen),
                            tint = if (selected) PaleGold else ChampagneMuted,
                            modifier = Modifier.size(22.dp)
                        )
                        if (badge != null) {
                            LiveCountBadge(
                                count = badge,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(start = 12.dp)
                            )
                        }
                    }
                    if (expanded) {
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = sectionTitle(screen),
                            color = if (selected) PaleGold else ChampagneMuted,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Pairing pod
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MetalCharcoalElevated)
                .tvFocusableControl(
                    shape = RoundedCornerShape(12.dp),
                    focusedScale = 1.0f,
                    onClick = onOpenTvSyncQr
                )
                .padding(vertical = 10.dp)
                .padding(start = 16.dp)
                .testTag("tv_rail_sync_status_pod")
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(MetalCharcoal)
            ) {
                Icon(
                    imageVector = Icons.Filled.Tv,
                    contentDescription = "Phone ↔ TV",
                    tint = if (isPhonePaired) CatchupGreen else Champagne,
                    modifier = Modifier.size(14.dp)
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = if (isPhonePaired) "Phone ↔ TV · connected" else "Phone ↔ TV",
                        color = Champagne,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (isPhonePaired) {
                            tvSyncState.connectedPeerName
                        } else {
                            tvSyncState.pairingPin.ifBlank { "Ready" }
                        },
                        color = PaleGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = if (isPhonePaired) 0.sp else 2.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
