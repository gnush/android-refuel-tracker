package io.github.gnush.refueltracker.ui.settings

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.gnush.refueltracker.BuildConfig
import io.github.gnush.refueltracker.R
import io.github.gnush.refueltracker.ui.theme.RefuelTrackerTheme
import androidx.core.net.toUri
import io.github.gnush.refueltracker.CommonTopAppBar
import io.github.gnush.refueltracker.ui.navigation.NavigationDestination

object AboutDestination: NavigationDestination {
    override val route: String = "about"
    @StringRes override val titleRes: Int = R.string.about_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateUp: () -> Unit,
    navigateToLibraries: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            CommonTopAppBar(
                title = stringResource(AboutDestination.titleRes),
                onNavigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            AboutRefuelTracker(onLibrariesClick = navigateToLibraries)
        }
    }
}

@Composable
fun AboutRefuelTracker(
    onLibrariesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(
            start = dimensionResource(R.dimen.padding_medium),
            end = dimensionResource(R.dimen.padding_medium)
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.ic_launcher_foreground),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.app_icon_description),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .background(
                    colorResource(R.color.ic_launcher_background),
                    MaterialTheme.shapes.medium
                )
        )
        Text(
            text = stringResource(R.string.about_app_description),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        )

        InfoRow(
            type = R.string.about_app_version_label,
            value = BuildConfig.VERSION_NAME,
            leadingIcon = Icons.Outlined.Info,
            leadingIconDescription = R.string.about_info_icon_description
        )
        InfoRow(
            type = R.string.about_app_author_label,
            valueId = R.string.about_app_author_name,
            leadingIcon = Icons.Outlined.Person,
            leadingIconDescription = R.string.about_person_icon_description
        )
        val uri = stringResource(R.string.about_app_source_code_location)
        InfoRow(
            type = R.string.about_app_source_code_label,
            value = uri,
            leadingIcon = Icons.Outlined.Code,
            leadingIconDescription = R.string.about_code_icon_description,
            onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, uri.toUri())
                )
            }
        )
        InfoRow(
            type = R.string.about_app_license_label,
            valueId = R.string.about_app_license_name,
            leadingIcon = Icons.Outlined.Description,
            leadingIconDescription = R.string.about_info_icon_description,
            onClick = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://www.gnu.org/licenses/gpl-3.0.html".toUri()
                    )
                )
            }
        )
        InfoRow(
            type = R.string.about_libs_label,
            valueId = R.string.about_libs_aim,
            leadingIcon = Icons.Outlined.Book,
            leadingIconDescription = R.string.about_book_icon_description,
            onClick = onLibrariesClick
        )
    }
}

@Composable
private fun InfoRow(
    @StringRes type: Int,
    value: String,
    leadingIcon: ImageVector,
    @StringRes leadingIconDescription: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    var rowModifier = modifier.padding(dimensionResource(R.dimen.padding_tiny))
    if (onClick != null)
        rowModifier = rowModifier.clickable(onClick = onClick)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = rowModifier
    ) {
        Image(
            imageVector = leadingIcon,
            contentDescription = stringResource(leadingIconDescription),
            Modifier.padding(dimensionResource(R.dimen.padding_tiny))
        )
        Column(
            Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(type),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_extra_tiny))
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_extra_tiny))
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun InfoRow(
    @StringRes type: Int,
    @StringRes valueId: Int,
    leadingIcon: ImageVector,
    @StringRes leadingIconDescription: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    InfoRow(
        type,
        stringResource(valueId),
        leadingIcon,
        leadingIconDescription,
        modifier,
        onClick
    )
}

@Preview(showBackground = true)
@Composable
fun AboutRefuelTrackerPreview() {
    RefuelTrackerTheme {
        AboutRefuelTracker({})
    }
}