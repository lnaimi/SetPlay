package com.example.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlaylistEntity
import com.example.data.sync.SyncRole
import com.example.data.sync.TvSyncSessionState
import com.example.ui.theme.AmberGold
import com.example.ui.theme.Champagne
import com.example.ui.theme.ChampagneSubtleBorder
import com.example.ui.theme.DarkGraphite
import com.example.ui.theme.MetalCharcoal
import com.example.ui.theme.PaleGold
import com.example.ui.theme.tvFocusableControl
import com.example.util.SetPlayEdition

/**
 * Settings: one place for everything that used to live in the phone drawer
 * sheet, the top-bar icons and the extra TV rail entries.
 */
@Composable
fun SettingsScreen(
    activePlaylist: PlaylistEntity?,
    tvSyncState: TvSyncSessionState,
    activeEdition: SetPlayEdition,
    selectedZoneLabel: String,
    onOpenAccounts: () -> Unit,
    onOpenParentalLock: () -> Unit,
    onOpenTvPairing: () -> Unit,
    onOpenBroadcastZone: () -> Unit,
    onSwitchEdition: (SetPlayEdition) -> Unit,
    modifier: Modifier = Modifier
) {
    val isTv = activeEdition == SetPlayEdition.TV
    val pairingSubtitle = when {
        tvSyncState.connectedPeerName.isNotBlank() -> "Connected to ${tvSyncState.connectedPeerName}"
        tvSyncState.role == SyncRole.TV_HOST && tvSyncState.pairingPin.isNotBlank() ->
            "Pairing code ${tvSyncState.pairingPin} · ${tvSyncState.localIpAddress.ifBlank { "Wi-Fi" }}"
        tvSyncState.isSyncActive -> "Connected"
        else -> "Pair with a code or QR · send streams to the TV"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkGraphite)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isTv) 28.dp else 16.dp, vertical = 12.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SettingsRow(
            title = "Accounts & servers",
            subtitle = activePlaylist?.let { "Xtream · ${it.name} · active" } ?: "No account connected",
            icon = Icons.Default.Dns,
            testTag = "top_playlists_btn",
            onClick = onOpenAccounts
        )
        SettingsRow(
            title = "Parental lock",
            subtitle = "PIN and locked categories",
            icon = Icons.Default.Security,
            testTag = "settings_parental_lock",
            onClick = onOpenParentalLock
        )
        SettingsRow(
            title = "Phone ↔ TV",
            subtitle = pairingSubtitle,
            icon = Icons.Default.Tv,
            testTag = "settings_tv_pairing",
            onClick = onOpenTvPairing
        )
        SettingsRow(
            title = "Broadcast zone & time",
            subtitle = selectedZoneLabel,
            icon = Icons.Default.Public,
            testTag = "settings_zone",
            onClick = onOpenBroadcastZone
        )
        SettingsRow(
            title = "Edition",
            subtitle = if (isTv) "TV layout · switch to phone layout" else "Phone layout · switch to TV layout",
            icon = Icons.Default.Smartphone,
            testTag = "settings_edition",
            onClick = { onSwitchEdition(if (isTv) SetPlayEdition.PHONE else SetPlayEdition.TV) }
        )
    }
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        color = MetalCharcoal,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, ChampagneSubtleBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .tvFocusableControl(
                shape = RoundedCornerShape(16.dp),
                focusedScale = 1.0f,
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AmberGold, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = DarkGraphite, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = PaleGold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = Champagne, fontSize = 12.sp)
            }
        }
    }
}
