package com.example.ui.livetv

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.model.CategoryWithCount
import com.example.data.model.PlaylistEntity
import com.example.data.model.StreamEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CatchupGreen
import com.example.ui.theme.Champagne
import com.example.ui.theme.DarkGraphite
import com.example.ui.theme.LiveRed
import com.example.ui.theme.MetalCharcoal
import com.example.ui.theme.MetalCharcoalElevated
import com.example.ui.theme.PaleGold
import com.example.ui.theme.TvBroadcastScarlet
import com.example.ui.theme.TvCrimsonRose
import com.example.ui.theme.TvDeepMaroon
import com.example.ui.theme.TvMidnightSlate
import com.example.ui.theme.TvPlumMagenta
import com.example.ui.theme.TvTwilightIndigo
import com.example.ui.theme.calculateLiveTvGridColumns
import com.example.ui.theme.luxuryFocusableCard
import com.example.ui.theme.tvFocusablePill
import com.example.util.StreamMetadataParser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTvScreen(
    viewModel: LiveTvViewModel,
    activePlaylist: PlaylistEntity?,
    onChannelClick: (StreamEntity) -> Unit,
    isParentalUnlocked: Boolean = false,
    onRequestUnlockCategory: ((CategoryWithCount, () -> Unit) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (activePlaylist == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(DarkGraphite)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.LiveTv,
                    contentDescription = null,
                    tint = TvBroadcastScarlet,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No account connected",
                    color = PaleGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Open Settings → Accounts & servers to connect your Xtream account.",
                    color = Champagne,
                    fontSize = 13.sp
                )
            }
        }
        return
    }

    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val categorySearchQuery by viewModel.categorySearchQuery.collectAsStateWithLifecycle()
    val channelFilterQuery by viewModel.channelFilterQuery.collectAsStateWithLifecycle()
    val channels by viewModel.channels.collectAsStateWithLifecycle()
    val currentLimit by viewModel.currentLimit.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isLoadingChannels by viewModel.isLoadingChannels.collectAsStateWithLifecycle()

    // Pulsing broadcast signal animation for live channel cards
    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
    val livePulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "live_dot_alpha"
    )

    var showAllCategoriesSheet by remember { mutableStateOf(false) }

    val allCategory = remember(activePlaylist.channelCount) {
        CategoryWithCount(
            categoryId = LiveTvViewModel.ALL_CHANNELS_CATEGORY_ID,
            categoryName = "All",
            type = "LIVE",
            isLocked = false,
            streamCount = activePlaylist.channelCount
        )
    }
    val isAllSelected = selectedCategory == null || selectedCategory?.categoryId == LiveTvViewModel.ALL_CHANNELS_CATEGORY_ID

    // Selecting a category: locked ones go through the parental PIN first.
    val chooseCategory: (CategoryWithCount?) -> Unit = { category ->
        if (category == null || category.categoryId == LiveTvViewModel.ALL_CHANNELS_CATEGORY_ID) {
            viewModel.selectCategory(null)
        } else if (category.isLocked && !isParentalUnlocked) {
            onRequestUnlockCategory?.invoke(category) { viewModel.selectCategory(category) }
        } else {
            viewModel.selectCategory(category)
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(DarkGraphite)
            .testTag("live_tv_screen")
    ) {
        val isWide = maxWidth >= 840.dp
        val columnCount = calculateLiveTvGridColumns(maxWidth.value.toInt())

        Column(modifier = Modifier.fillMaxSize()) {
            // Search across the selected category (or all channels)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = channelFilterQuery,
                    onValueChange = { viewModel.onChannelFilterQueryChanged(it) },
                    placeholder = {
                        Text(
                            text = if (isAllSelected) {
                                "Search %,d channels".format(activePlaylist.channelCount)
                            } else {
                                "Search in ${selectedCategory?.categoryName.orEmpty()}"
                            },
                            color = Champagne.copy(alpha = 0.65f),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search channels", tint = Champagne)
                    },
                    trailingIcon = {
                        if (channelFilterQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onChannelFilterQueryChanged("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Champagne)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmberGold,
                        unfocusedBorderColor = Champagne.copy(alpha = 0.3f),
                        focusedContainerColor = MetalCharcoal,
                        unfocusedContainerColor = MetalCharcoal,
                        focusedTextColor = PaleGold,
                        unfocusedTextColor = PaleGold
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("channel_search_input")
                )

                Surface(
                    color = MetalCharcoal,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Champagne.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .tvFocusablePill(
                            shape = RoundedCornerShape(12.dp),
                            onClick = { viewModel.refreshLiveTv(activePlaylist) }
                        )
                        .testTag("section_refresh_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isRefreshing) {
                            CircularProgressIndicator(color = AmberGold, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh channels", tint = Champagne, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            if (!isWide) {
                // Phone: category chips in one row, with "All categories" opening the searchable sheet
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("live_category_chips")
                ) {
                    item(key = "__all__") {
                        CategoryChip(
                            label = "All",
                            selected = isAllSelected,
                            onClick = { chooseCategory(null) }
                        )
                    }
                    items(categories.take(MAX_CATEGORY_CHIPS), key = { it.categoryId }) { category ->
                        CategoryChip(
                            label = category.categoryName,
                            selected = selectedCategory?.categoryId == category.categoryId,
                            locked = category.isLocked && !isParentalUnlocked,
                            onClick = { chooseCategory(category) }
                        )
                    }
                    if (categories.size > MAX_CATEGORY_CHIPS) {
                        item(key = "__more__") {
                            CategoryChip(
                                label = "All categories",
                                selected = false,
                                trailingArrow = true,
                                onClick = { showAllCategoriesSheet = true }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            Row(modifier = Modifier.fillMaxSize()) {
                if (isWide) {
                    // TV / wide: category column beside the grid (Up/Down to browse, Right into channels)
                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 8.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .width(200.dp)
                            .fillMaxHeight()
                            .testTag("live_category_column")
                    ) {
                        item(key = "__all__") {
                            CategoryListRow(
                                category = allCategory,
                                selected = isAllSelected,
                                locked = false,
                                onClick = { chooseCategory(null) }
                            )
                        }
                        items(categories, key = { it.categoryId }) { category ->
                            CategoryListRow(
                                category = category,
                                selected = selectedCategory?.categoryId == category.categoryId,
                                locked = category.isLocked && !isParentalUnlocked,
                                onClick = { chooseCategory(category) }
                            )
                        }
                    }
                }

                when {
                    activePlaylist.channelCount == 0 -> EmptyLiveState(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        title = "No channels yet",
                        body = "Open Settings → Accounts & servers and sync your account to load channels."
                    )
                    channels.isEmpty() && channelFilterQuery.isNotBlank() -> EmptyLiveState(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        title = "No channels match \"$channelFilterQuery\"",
                        body = if (isAllSelected) "Try a shorter name." else "Try a shorter name, or search in All."
                    )
                    channels.isEmpty() && (isLoadingChannels || isRefreshing) -> Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.weight(1f).fillMaxHeight().testTag("live_channels_loading")
                    ) {
                        CircularProgressIndicator(color = AmberGold, strokeWidth = 2.dp, modifier = Modifier.size(28.dp))
                    }
                    channels.isEmpty() -> EmptyLiveState(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        title = "Nothing in this category",
                        body = "Pick another category or search all channels."
                    )
                    else -> {
                        val gridState = rememberLazyGridState()
                        val shouldLoadMore by remember(channels.size, currentLimit) {
                            derivedStateOf {
                                val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                channels.size >= currentLimit && lastVisible >= (channels.size - 24).coerceAtLeast(0)
                            }
                        }
                        LaunchedEffect(shouldLoadMore) {
                            if (shouldLoadMore) {
                                viewModel.loadMore()
                            }
                        }
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Fixed(if (isWide) (columnCount - 1).coerceAtLeast(2) else columnCount),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .testTag("live_channels_list"),
                            contentPadding = PaddingValues(start = if (isWide) 8.dp else 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(channels, key = { it.id }) { channel ->
                                ChannelCard(
                                    channel = channel,
                                    livePulseAlpha = livePulseAlpha,
                                    onClick = { onChannelClick(channel) },
                                    onToggleFavorite = { viewModel.toggleFavorite(channel.id, channel.isFavorite) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAllCategoriesSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAllCategoriesSheet = false },
            containerColor = MetalCharcoal
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Categories",
                    color = PaleGold,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = categorySearchQuery,
                    onValueChange = { viewModel.onCategorySearchQueryChanged(it) },
                    placeholder = { Text("Search categories", color = Champagne.copy(alpha = 0.65f), fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Champagne) },
                    trailingIcon = {
                        if (categorySearchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onCategorySearchQueryChanged("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Champagne)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmberGold,
                        unfocusedBorderColor = Champagne.copy(alpha = 0.3f),
                        focusedContainerColor = DarkGraphite,
                        unfocusedContainerColor = DarkGraphite,
                        focusedTextColor = PaleGold,
                        unfocusedTextColor = PaleGold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("category_search_input")
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(480.dp)
                        .testTag("live_categories_directory")
                ) {
                    if (categorySearchQuery.isBlank()) {
                        item(key = "__all_channels__", span = { GridItemSpan(2) }) {
                            LiveCategoryDirectoryCard(
                                category = allCategory.copy(categoryName = "All channels"),
                                isAllChannelsCard = true,
                                onClick = {
                                    chooseCategory(null)
                                    showAllCategoriesSheet = false
                                }
                            )
                        }
                    }
                    items(categories, key = { it.categoryId }) { category ->
                        LiveCategoryDirectoryCard(
                            category = category,
                            isAllChannelsCard = false,
                            onClick = {
                                chooseCategory(category)
                                showAllCategoriesSheet = false
                            }
                        )
                    }
                }
            }
        }
    }
}

private const val MAX_CATEGORY_CHIPS = 14

@Composable
private fun CategoryChip(
    label: String,
    selected: Boolean,
    locked: Boolean = false,
    trailingArrow: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        color = if (selected) AmberGold else Color.Transparent,
        shape = RoundedCornerShape(50),
        border = if (selected) null else BorderStroke(1.dp, Champagne.copy(alpha = 0.3f)),
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(50))
            .tvFocusablePill(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            if (locked) {
                Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Champagne, modifier = Modifier.size(12.dp))
            }
            Text(
                text = label,
                color = if (selected) DarkGraphite else Champagne,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (trailingArrow) {
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Champagne, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
private fun CategoryListRow(
    category: CategoryWithCount,
    selected: Boolean,
    locked: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) MetalCharcoalElevated else Color.Transparent)
            .tvFocusablePill(shape = RoundedCornerShape(10.dp), focusedScale = 1.0f, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        Text(
            text = category.categoryName,
            color = if (selected) PaleGold else Champagne,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (locked) {
            Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Champagne, modifier = Modifier.size(12.dp))
        } else if (category.streamCount > 0) {
            Text(
                text = "%,d".format(category.streamCount),
                color = Champagne.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun EmptyLiveState(title: String, body: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.LiveTv,
                contentDescription = null,
                tint = Champagne,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, color = PaleGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = body, color = Champagne, fontSize = 13.sp)
        }
    }
}

@Composable
fun LiveCategoryDirectoryCard(
    category: CategoryWithCount,
    isAllChannelsCard: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAllChannelsCard) TvDeepMaroon else TvTwilightIndigo
        ),
        border = BorderStroke(
            width = 1.dp,
            brush = if (isAllChannelsCard) {
                Brush.horizontalGradient(
                    listOf(
                        TvBroadcastScarlet.copy(alpha = 0.75f),
                        AmberGold.copy(alpha = 0.45f)
                    )
                )
            } else {
                Brush.horizontalGradient(
                    listOf(
                        TvCrimsonRose.copy(alpha = 0.28f),
                        Color.White.copy(alpha = 0.07f)
                    )
                )
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .luxuryFocusableCard(
                accentColor = if (isAllChannelsCard) AmberGold else TvBroadcastScarlet,
                shape = RoundedCornerShape(16.dp),
                onClick = onClick
            )
            .testTag("category_card_${category.categoryId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            if (isAllChannelsCard) TvDeepMaroon else TvTwilightIndigo,
                            TvMidnightSlate.copy(alpha = 0.94f)
                        )
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Broadcast Studio Spine
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(64.dp)
                    .background(
                        Brush.verticalGradient(
                            if (isAllChannelsCard) {
                                listOf(AmberGold, TvBroadcastScarlet)
                            } else {
                                listOf(TvBroadcastScarlet, TvCrimsonRose.copy(alpha = 0.55f))
                            }
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 13.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isAllChannelsCard) {
                                Brush.linearGradient(listOf(TvBroadcastScarlet, TvDeepMaroon))
                            } else {
                                Brush.linearGradient(
                                    listOf(
                                        TvBroadcastScarlet.copy(alpha = 0.18f),
                                        Color.White.copy(alpha = 0.05f)
                                    )
                                )
                            }
                        )
                        .border(
                            0.8.dp,
                            if (isAllChannelsCard) AmberGold.copy(alpha = 0.55f) else TvCrimsonRose.copy(alpha = 0.35f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isAllChannelsCard) Icons.Default.LiveTv else Icons.Default.Sensors,
                        contentDescription = null,
                        tint = if (isAllChannelsCard) Color.White else TvCrimsonRose,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                val cleanedCatName = remember(category.categoryName, isAllChannelsCard) {
                    if (isAllChannelsCard) category.categoryName
                    else StreamMetadataParser.cleanDisplayTitle(category.categoryName)
                }
                val catRegionBadge = remember(category.categoryName, isAllChannelsCard) {
                    if (isAllChannelsCard) null
                    else StreamMetadataParser.extractRegionOrLangBadge(category.categoryName)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (catRegionBadge != null) {
                            Surface(
                                color = TvBroadcastScarlet.copy(alpha = 0.22f),
                                shape = RoundedCornerShape(5.dp),
                                border = BorderStroke(0.6.dp, TvCrimsonRose.copy(alpha = 0.55f))
                            ) {
                                Text(
                                    text = catRegionBadge,
                                    color = PaleGold,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 5.5.dp, vertical = 1.5.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = cleanedCatName,
                            color = Color.White,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (category.isLocked) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked Category",
                                tint = LiveRed,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isAllChannelsCard) {
                            "Full Live Broadcast Lineup • Instant Zap"
                        } else {
                            "Live Broadcast Bouquet • Direct Stream"
                        },
                        color = Champagne.copy(alpha = 0.72f),
                        fontSize = 11.sp
                    )
                }

                if (category.streamCount > 0) {
                    Surface(
                        color = if (isAllChannelsCard) TvBroadcastScarlet else Color(0xFF0A0D18),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(
                            0.7.dp,
                            if (isAllChannelsCard) AmberGold.copy(alpha = 0.6f) else TvCrimsonRose.copy(alpha = 0.40f)
                        )
                    ) {
                        Text(
                            text = "%,d".format(category.streamCount),
                            color = if (isAllChannelsCard) Color.White else PaleGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open Category",
                    tint = TvCrimsonRose,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ChannelCard(
    channel: StreamEntity,
    livePulseAlpha: Float = 1f,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cleanTitle = remember(channel.name) {
        StreamMetadataParser.cleanDisplayTitle(channel.name)
    }
    val regionBadge = remember(channel.name) {
        StreamMetadataParser.extractRegionOrLangBadge(channel.name)
    }
    val qualityBadge = remember(channel.name) {
        StreamMetadataParser.extractQualityBadge(channel.name)
    }
    val isUltraHd = qualityBadge == "4K UHD"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TvTwilightIndigo),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                listOf(
                    if (channel.isFavorite) AmberGold.copy(alpha = 0.55f) else TvBroadcastScarlet.copy(alpha = 0.40f),
                    Color.White.copy(alpha = 0.08f)
                )
            )
        ),
        modifier = modifier
            .fillMaxWidth()
            .luxuryFocusableCard(
                accentColor = if (channel.isFavorite) AmberGold else TvBroadcastScarlet,
                shape = RoundedCornerShape(16.dp),
                onClick = onClick
            )
            .testTag("channel_item_${channel.streamId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            TvTwilightIndigo,
                            TvMidnightSlate.copy(alpha = 0.95f)
                        )
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Broadcast Control Room Status Spine
            Box(
                modifier = Modifier
                    .width(3.5.dp)
                    .height(68.dp)
                    .background(
                        Brush.verticalGradient(
                            if (channel.isFavorite) {
                                listOf(AmberGold, TvBroadcastScarlet)
                            } else {
                                listOf(TvBroadcastScarlet, TvCrimsonRose.copy(alpha = 0.45f))
                            }
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 11.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Recessed Frosted Logo Well
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    TvBroadcastScarlet.copy(alpha = 0.14f),
                                    Color(0xFF0A0D18)
                                )
                            )
                        )
                        .border(0.8.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (channel.streamIcon.isNotEmpty()) {
                        AsyncImage(
                            model = channel.streamIcon,
                            contentDescription = cleanTitle,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Tv,
                            contentDescription = null,
                            tint = TvCrimsonRose,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(11.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = cleanTitle,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (qualityBadge != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color.Black.copy(alpha = 0.40f),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(
                                    0.6.dp,
                                    if (isUltraHd) AmberGold.copy(alpha = 0.75f) else TvCrimsonRose.copy(alpha = 0.45f)
                                )
                            ) {
                                Text(
                                    text = qualityBadge,
                                    color = if (isUltraHd) AmberGold else TvCrimsonRose,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 4.5.dp, vertical = 1.dp)
                                )
                            }
                        }
                        if (channel.isLocked) {
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = LiveRed,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Authentic Inline Live Status, Region Badge & Catchup Metadata (Zero Fake Progress Bars)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(5.5.dp)
                                .alpha(livePulseAlpha)
                                .background(TvBroadcastScarlet, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (regionBadge != null) "$regionBadge • LIVE" else "LIVE STREAM",
                            color = TvCrimsonRose,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.3.sp
                        )

                        if (channel.tvArchive == 1) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Default.History,
                                contentDescription = null,
                                tint = CatchupGreen,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (channel.tvArchiveDuration > 0) "${channel.tvArchiveDuration}d Catchup" else "Catchup",
                                color = CatchupGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Favorite Star Icon Button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("channel_fav_${channel.streamId}")
                ) {
                    Icon(
                        imageVector = if (channel.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (channel.isFavorite) AmberGold else Champagne.copy(alpha = 0.65f),
                        modifier = Modifier.size(19.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Illuminated Broadcast Scarlet "(▶) WATCH" Capsule with Dark Play Medallion
                Surface(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(0.9.dp, Color.White.copy(alpha = 0.32f)),
                    modifier = Modifier
                        .height(31.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    TvBroadcastScarlet,
                                    TvCrimsonRose
                                )
                            )
                        )
                        .clickable(onClick = onClick)
                        .testTag("channel_play_${channel.streamId}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = 5.dp, end = 11.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0A0D18))
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = TvCrimsonRose,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "WATCH",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}
