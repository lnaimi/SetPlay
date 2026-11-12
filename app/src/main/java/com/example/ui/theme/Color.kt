package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// =========================================================================
// SetPlay Luxury Brand DNA — Core Foundation Tokens
// =========================================================================
val DarkGraphite = Color(0xFF141619)   // Global luxury base & high-contrast dark text on bright pills
val MetalCharcoal = Color(0xFF25282E)  // Shared modal/chrome surface
val AmberGold = Color(0xFFD4A238)      // Brand DNA gold signature
val PaleGold = Color(0xFFF6E8C8)       // Luminous warm ivory-gold headline typography
val Champagne = Color(0xFFDFC69E)      // Warm champagne secondary metadata & subtle borders

// Supporting surface & status tokens harmonized with Brand DNA
val MetalCharcoalElevated = Color(0xFF2F333B)
val ChampagneSubtleBorder = Color(0x4DDFC69E) // 30% Champagne for 0.5dp-1dp luxury borders
val ChampagneMuted = Color(0xFF9E9078)
val LiveRed = Color(0xFFE5484D)
val CatchupGreen = Color(0xFF2EBD85)

// One-look support tokens (prototype): recessed wells, hairlines and glows on the Graphite base
val GraphiteWell = Color(0xFF1B1E22)          // Recessed wells inside cards (score boxes, stat tracks)
val GraphiteHairline = Color(0xFF3A3E46)      // Solid hairline for containers on Metal Charcoal
val GraphiteHairlineSoft = Color(0x14F6E8C8)  // 8% Pale Gold row dividers
val AmberGlow = Color(0x33D4A238)             // 20% Amber Gold focus halo / active glow
val LiveRedGlow = Color(0x33E5484D)           // 20% Live Red in-play aura
// Match Center team sides: home takes the gold accent, away a steel blue so the two
// differ in hue, not only in brightness (momentum wave, shot map, stat bars, goals).
val HomeTeamAccent = AmberGold
val AwayTeamAccent = Color(0xFF6FA8DC)

// =========================================================================
// 5-Section Bespoke Brand Kit Palettes (From User's 5 Section Color Sheets)
// =========================================================================
// One look (redesign): every section now renders on the Royal Gold scheme. The section
// token names below are kept so the screens keep compiling, but each one now points at
// the Royal Gold token for its role (canvas, card, raised, text, accent, live). The
// former value is kept in the comment. League, broadcaster and pitch colours are unchanged.

// 1. KICKOFF ARENA PALETTE (Legacy tokens preserved for backward compatibility + Official 6-Color Architectural Palette)
val KickoffCeruleanBlue = AmberGold   // was 0xFF0081A7 — #0081A7 — Legacy Primary Cerulean Ocean Blue
val KickoffTropicalTeal = AmberGold   // was 0xFF00AFB9 — #00AFB9 — Legacy Vibrant Tropical Teal-Cyan
val KickoffPeachCream = PaleGold   // was 0xFFFED9B7 — #FED9B7 — Legacy Warm Peach-Champagne highlight & badge text
val KickoffCoralRed = LiveRed   // was 0xFFF07167 — #F07167 — Legacy Vibrant Coral-Red live & action accent

// Official Kickoff 6-Color Architectural Palette
val KickoffTerracotta = AmberGold   // was 0xFFD9541E — #D9541E — Burnt Terracotta / Signal Orange (Live badges, primary CTAs, active pillar, focus rings)
val KickoffImperialNavy = MetalCharcoalElevated   // was 0xFF0F1B3D — #0F1B3D — Deep Royal Imperial Navy (Hero match cards, active day capsule, competition group headers, 2D pitch)
val KickoffObsidianBlack = DarkGraphite   // was 0xFF111111 — #111111 — Obsidian Carbon Black (Primary screen canvas, modal backdrop, recessed scorebox wells)
val KickoffAlabasterIvory = PaleGold   // was 0xFFF3EEE3 — #F3EEE3 — Warm Alabaster Ivory (Primary high-contrast typography, team names, live tabular scores)
val KickoffAgedParchment = Champagne   // was 0xFFD8CBB3 — #D8CBB3 — Aged Parchment / Warm Sand (Secondary metadata, TV channel pills, kickoff times, subtle borders)
val KickoffCharcoalSlate = MetalCharcoal   // was 0xFF1E1E1C — #1E1E1C — Warm Charcoal Slate (Match row cards, elevated containers, filter pills, bottom sheets)

// Controlled Shade & Alpha Derivations
val KickoffNavyElevated = MetalCharcoalElevated   // was 0xFF162654 — Elevated Imperial Navy for hero gradients & pitch stripes
val KickoffCharcoalElevated = MetalCharcoalElevated   // was 0xFF272724 — Elevated Charcoal for inner pills & unselected controls
val KickoffTerracottaGlow = AmberGlow   // was 0x33D9541E — 20% #D9541E for live match card border aura
val KickoffParchmentBorder = ChampagneSubtleBorder   // was 0x2ED8CBB3 — 18% #D8CBB3 for crisp 0.8dp architectural borders
val KickoffParchmentMuted = ChampagneMuted   // was 0xB3D8CBB3 — 70% #D8CBB3 for secondary captions

// =========================================================================
// KICKOFF BRAND COLOR KIT — "FLOODLIGHT NOIR & PITCH EMERALD"
// =========================================================================

// 1. 4-Layer Stadium Depth Surfaces
val KickoffPitchBlack = DarkGraphite   // was 0xFF070B0A — Level 0: Root nocturnal stadium canvas & nav bar base
val KickoffTurfSurface = MetalCharcoal   // was 0xFF0E1614 — Level 1: Primary competition group & match card surface
val KickoffCanopyElevated = MetalCharcoalElevated   // was 0xFF15211E — Level 2: Sticky ribbon, active filter pods & modal headers
val KickoffGlassWell = GraphiteWell   // was 0xFF1C2C28 — Level 3: Recessed center scorebugs, time pills & stat tracks

// 2. Primary, Secondary & Signal Brand Accents
val KickoffPitchEmerald = AmberGold   // was 0xFF10B981 — Primary Brand Identity: Watch CTAs, active pillar/tab fills, confirmed XI
val KickoffFloodlightVolt = AmberGold   // was 0xFFD4FF33 — Kinetic Highlight: Active date laser bar, TV D-Pad focus rim, MVP crown
val KickoffStadiumForest = Color(0xFF0B3B2D)    // Deep Secondary Surface: Hero live match gradient & 2D pitch turf base
val KickoffStadiumTurfAlt = Color(0xFF0F4C3A)   // Alternating 2D tactical pitch stripe
val KickoffLiveCrimson = LiveRed   // was 0xFFFF2E54 — Exclusive Live Signal: Pulsing LIVE badge, live minute clock & red cards
val KickoffTrophyGold = AmberGold   // was 0xFFF59E0B — Championship & Warning: Pinned items, HT/Stoppage badge & quota alerts

// 3. Typography, Borders & Interactive State Tokens
val KickoffFloodlightWhite = PaleGold   // was 0xFFF2F7F5 — Primary high-contrast team names, tabular scores & headlines
val KickoffMistSilver = Champagne   // was 0xFF99AEA7 — Secondary metadata, broadcaster labels, stadium & referee text
val KickoffSlateMuted = ChampagneMuted   // was 0xFF5C736B — Muted finished losing team, disabled controls & subtle captions
val KickoffEmeraldBorder = ChampagneSubtleBorder   // was 0x2910B981 — 16% Pitch Emerald for 0.8dp card & container borders
val KickoffVoltGlow = AmberGlow   // was 0x33D4FF33 — 20% Floodlight Volt for D-Pad focus halo & active date glow
val KickoffCrimsonGlow = LiveRedGlow   // was 0x33FF2E54 — 20% Live Crimson for in-play match aura
val KickoffRowDivider = GraphiteHairlineSoft   // was 0x14F2F7F5 — 8% Floodlight White for crisp 0.5dp match row dividers

// 4. Competition-Specific Signature Accents
val CompUclBlue = Color(0xFF3B82F6)
val CompUelOrange = Color(0xFFF97316)
val CompEplViolet = Color(0xFFA855F7)
val CompLaLigaCoral = Color(0xFFFF4B4B)
val CompSerieACyan = Color(0xFF06B6D4)
val CompBundesligaRed = Color(0xFFEF4444)
val CompLigue1Gold = Color(0xFFEAB308)
val CompCafEmerald = Color(0xFF10B981)
val CompBotolaCrimson = Color(0xFFE11D48)
val CompSaudiJade = Color(0xFF14B8A6)
val CompIntlGold = Color(0xFFF59E0B)

// 5. Broadcast Provider Identity Tokens (Match Cards, Studio Dock & TV Guide)
val ProviderBeinSurface = Color(0xFF2E1065)
val ProviderBeinBorder = Color(0xFF8B5CF6)
val ProviderBeinText = Color(0xFFEDE9FE)

val ProviderArryadiaSurface = Color(0xFF3B0A14)
val ProviderArryadiaBorder = Color(0xFFF43F5E)
val ProviderArryadiaText = Color(0xFFFFE4E6)

val ProviderSscSurface = Color(0xFF3B2106)
val ProviderSscBorder = Color(0xFFF59E0B)
val ProviderSscText = Color(0xFFFEF3C7)

val ProviderAdSportsSurface = Color(0xFF0C2442)
val ProviderAdSportsBorder = Color(0xFF38BDF8)
val ProviderAdSportsText = Color(0xFFE0F2FE)

val ProviderCanalDaznSurface = Color(0xFF111C24)
val ProviderCanalDaznBorder = Color(0xFF2DD4BF)
val ProviderCanalDaznText = Color(0xFFCCFBF1)

val ProviderSkyTntSurface = Color(0xFF2A0F2D)
val ProviderSkyTntBorder = Color(0xFFE879F9)
val ProviderSkyTntText = Color(0xFFFAE8FF)

// Dark stadium companion surfaces mapped to Floodlight Noir & Pitch Emerald
val KickoffOceanBase = KickoffPitchBlack
val KickoffOceanSurface = KickoffTurfSurface
val KickoffOceanElevated = KickoffCanopyElevated

// 2. LIVE TV PALETTE (TV-colors.jpg: #1F313B, #383852, #784259, #B94E56, #BE4039, #683536)
val TvMidnightSlate = DarkGraphite   // was 0xFF1F313B — #1F313B — Deep Midnight Slate-Navy background base
val TvTwilightIndigo = MetalCharcoal   // was 0xFF383852 — #383852 — Twilight Indigo-Slate elevated surface
val TvPlumMagenta = MetalCharcoalElevated   // was 0xFF784259 — #784259 — Plum Magenta mid-surface & border accent
val TvCrimsonRose = LiveRed   // was 0xFFB94E56 — #B94E56 — Crimson Rose secondary accent
val TvBroadcastScarlet = LiveRed   // was 0xFFBE4039 — #BE4039 — Vibrant Broadcast Scarlet primary accent
val TvDeepMaroon = MetalCharcoalElevated   // was 0xFF683536 — #683536 — Deep Maroon-Wine card/banner container

// 3. MOVIES PALETTE (Movies-colors.jpg: #80EE98, #46DFB1, #09D1C7, #15919B, #0C6478, #213A58)
val MoviesMintSpring = PaleGold   // was 0xFF80EE98 — #80EE98 — Electric Mint-Spring highlight accent
val MoviesAquamarine = AmberGold   // was 0xFF46DFB1 — #46DFB1 — Aquamarine Turquoise bright accent
val MoviesCyanTeal = AmberGold   // was 0xFF09D1C7 — #09D1C7 — Vibrant Cyan-Teal primary action accent
val MoviesOceanTeal = MetalCharcoalElevated   // was 0xFF15919B — #15919B — Ocean Teal secondary surface & border
val MoviesAtlanticTeal = MetalCharcoal   // was 0xFF0C6478 — #0C6478 — Deep Atlantic Teal card surface
val MoviesSapphireNavy = DarkGraphite   // was 0xFF213A58 — #213A58 — Midnight Sapphire-Navy background base

// 4. SERIES PALETTE (Sereis-colors.jpg: #113B3A, #184948, #235E5D, #317978, #599D9C, #B7E6E5)
val SeriesPineBase = DarkGraphite   // was 0xFF113B3A — #113B3A — Deepest Pine-Teal background base
val SeriesForestSurface = MetalCharcoal   // was 0xFF184948 — #184948 — Dark Teal-Forest surface
val SeriesCypressCard = MetalCharcoalElevated   // was 0xFF235E5D — #235E5D — Elevated Cypress-Teal card container
val SeriesSeaJade = ChampagneMuted   // was 0xFF317978 — #317978 — Rich Sea-Jade mid-tone accent & border
val SeriesMistTeal = Champagne   // was 0xFF599D9C — #599D9C — Soft Mist-Teal secondary accent
val SeriesIceAqua = PaleGold   // was 0xFFB7E6E5 — #B7E6E5 — Luminous Ice-Aqua highlight & primary text/pill accent

// 5. FAVORITES PALETTE (Fvoretes-colors.jpg: #051F20, #0B2B26, #163832, #235347, #8EB69B, #DAF1DE)
val FavoritesObsidianBase = DarkGraphite   // was 0xFF051F20 — #051F20 — Obsidian Forest-Black background base
val FavoritesEvergreenSurface = MetalCharcoal   // was 0xFF0B2B26 — #0B2B26 — Deep Evergreen-Pine dark surface
val FavoritesEmeraldCard = MetalCharcoalElevated   // was 0xFF163832 — #163832 — Rich Emerald-Moss card surface
val FavoritesBotanicalBorder = GraphiteHairline   // was 0xFF235347 — #235347 — Lush Botanical Green elevated container & border
val FavoritesSagePrimary = AmberGold   // was 0xFF8EB69B — #8EB69B — Soft Sage-Celadon primary accent
val FavoritesPaleMint = PaleGold   // was 0xFFDAF1DE — #DAF1DE — Pale Mint-Sage luminous text/badge highlight

// =========================================================================
// Legacy & Harmonized Section Tokens
// =========================================================================
val LiveCrimson = LiveRed   // was 0xFFE63946
val LiveCrimsonGlow = TvDeepMaroon
val LiveCoralAccent = TvCrimsonRose
val LiveBroadcastGold = AmberGold

val CinemaRuby = LiveRed   // was 0xFFC1121F
val CinemaVelvetSurface = MoviesAtlanticTeal
val CinemaMarqueeGold = AmberGold   // was 0xFFE5A93C
val CinemaSpotlight = MoviesMintSpring

val SeriesEmerald = AmberGold   // was 0xFF1B998B
val SeriesJadeAccent = AmberGold   // was 0xFF2EC4B6
val SeriesDeepVault = SeriesForestSurface
val SeriesVaultGold = SeriesIceAqua

// KickOff Arena tokens mapped to the Official 6-Color Architectural Palette
val ArenaPitchGreen = KickoffTerracotta
val ArenaTurfDark = KickoffCharcoalSlate
val ArenaTurfElevated = KickoffImperialNavy
val ArenaFloodlightLime = KickoffAlabasterIvory
val ArenaTrophyGold = KickoffAgedParchment

// MENA Sports Broadcaster Accents
val MenaBeinPurple = Color(0xFF5C2D91)
val MenaBeinDark = Color(0xFF39195C)
val MenaBeinLavender = Color(0xFFE6D4FF)
val MenaArryadiaRed = Color(0xFFC1272D)
val MenaArryadiaDark = Color(0xFF3D1014)
val MenaSscTeal = Color(0xFF0D9488)
val MenaSscDark = Color(0xFF0F2E2B)
val MenaAdSportsBlue = Color(0xFF2563EB)
val MenaAdSportsDark = Color(0xFF132347)

// Compatibility aliases used across secondary screens so the entire app shares the SetPlay DNA
val CinemaBlack = DarkGraphite
val CinemaDarkBackground = DarkGraphite
val CinemaSurface = MetalCharcoal
val CinemaSurfaceVariant = MetalCharcoal
val CinemaSurfaceElevated = MetalCharcoalElevated
val CinemaBorder = Champagne

val NeonCyan = AmberGold
val NeonCyanVariant = AmberGold
val ElectricBlue = Champagne
val NeonAmber = PaleGold

val TextPrimary = PaleGold
val TextSecondary = Champagne
val TextMuted = ChampagneMuted
