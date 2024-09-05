package com.gmat.ui.screen.profile

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.gmat.R
import com.gmat.ui.components.CenterBar
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.Locale

@Composable
fun Languages(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterBar(
                navController = navController,
                title = {
                    Text(
                        text = stringResource(id = R.string.language),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
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

    // State to track the selected language
    var selectedLanguage by remember { mutableStateOf(getCurrentLanguage(context)) }

    LazyColumn {
        items(languages.entries.toList()) { entry ->
            LanguageItem(
                languageName = entry.key,
                isSelected = entry.value == selectedLanguage,
                onClick = {
                    selectedLanguage = entry.value
                    changeAppLanguage(context, entry.value)
                }
            )
        }
    }
}

fun getCurrentLanguage(context: Context): String {
    val currentLocale = AppCompatDelegate.getApplicationLocales()[0]
    return currentLocale?.toLanguageTag() ?: Locale.getDefault().toLanguageTag()
}

@Composable
fun LanguageItem(
    languageName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 0.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = languageName,
                modifier = Modifier.weight(1f)
            )
            RadioButton(
                selected = isSelected,
                onClick = null // null because the parent Row is clickable
            )
        }
    }
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
