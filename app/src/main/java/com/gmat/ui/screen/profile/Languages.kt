package com.gmat.ui.screen.profile

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.gmat.R
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Languages(
    modifier: Modifier=Modifier
){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(
                        "Languages",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            ListLanguages()
        }

    }
}

@Composable
private fun ListLanguages() {
    val context = LocalContext.current
    val languages = getSupportedLanguages(context)

    LazyColumn {
        items(languages.entries.toList()) { entry ->
            LanguageItem(languageName = entry.key) {
                changeAppLanguage(context, entry.value)
            }
        }
    }
}

@Composable
fun LanguageItem(
    languageName: String,
    onClick: () -> Unit) {
    Text(
        text = languageName,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    )
}

fun changeAppLanguage(context: Context, languageCode: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
}

private fun getLocaleListFromXml(context: Context): LocaleListCompat {
    val tagsList = mutableListOf<CharSequence>()
    try {
        val xpp: XmlPullParser = context.resources.getXml(R.xml.locales_config)
        while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
            if (xpp.eventType == XmlPullParser.START_TAG && xpp.name == "locale") {
                tagsList.add(xpp.getAttributeValue(0))
            }
            xpp.next()
        }
    } catch (e: XmlPullParserException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return LocaleListCompat.forLanguageTags(tagsList.joinToString(","))
}

fun getSupportedLanguages(context: Context): Map<String, String> {
    val localeList = getLocaleListFromXml(context)
    val map = mutableMapOf<String, String>()

    for (a in 0 until localeList.size()) {
        localeList[a]?.let {
            map[it.getDisplayName(it)] = it.toLanguageTag()
        }
    }
    return map
}
