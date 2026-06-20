package app.papermarks.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.papermarks.domain.PrivacyPolicy
import app.papermarks.domain.annotation.TextRangeAnnotationEngine
import app.papermarks.domain.book.BookIdentity

@Composable
fun PapermarksApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var tab by remember { mutableIntStateOf(0) }
            Column {
                TabRow(selectedTabIndex = tab) {
                    listOf("Buch", "Seite", "Relays", "Login").forEachIndexed { index, title ->
                        Tab(selected = tab == index, onClick = { tab = index }, text = { Text(title) })
                    }
                }
                when (tab) {
                    0 -> BookScreen()
                    1 -> PageAnnotationScreen()
                    2 -> RelayScreen()
                    3 -> LoginScreen()
                }
            }
        }
    }
}

@Composable
private fun BookScreen() {
    var ean by remember { mutableStateOf("9783442178582") }
    var title by remember { mutableStateOf("Beispielbuch") }
    var author by remember { mutableStateOf("Beispielautor") }
    val valid = BookIdentity.isValidEan13(ean)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Papermarks", style = MaterialTheme.typography.headlineMedium)
        Text("Bücher werden per EAN/ISBN angelegt. Fotos werden nicht gespeichert.")
        OutlinedTextField(value = ean, onValueChange = { ean = it }, label = { Text("EAN-13 / ISBN-13") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Titel") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = author, onValueChange = { author = it }, label = { Text("Autor(en)") }, modifier = Modifier.fillMaxWidth())
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Book-ID: ${BookIdentity.bookIdFromIsbn13(ean)}")
                Text("EAN gültig: ${if (valid) "ja" else "nein"}")
                Text("Titel: $title")
                Text("Autor: $author")
            }
        }
        Button(onClick = { /* camera barcode screen should be connected here */ }) { Text("EAN scannen") }
    }
}

@Composable
private fun PageAnnotationScreen() {
    var pageNumber by remember { mutableStateOf("42") }
    var text by remember {
        mutableStateOf("Der Mensch ist das einzige Wesen, das über sich selbst nachdenken kann. Diese Beispielseite zeigt, wie Papermarks Markierungen als Textbereiche speichert.")
    }
    var start by remember { mutableStateOf("15") }
    var end by remember { mutableStateOf("32") }
    var note by remember { mutableStateOf("Wichtiger Gedanke") }
    val engine = remember { TextRangeAnnotationEngine() }
    val annotation = remember(text, start, end, note) {
        runCatching {
            engine.highlight(
                pageId = "page_demo",
                ocrText = text,
                startOffset = start.toInt(),
                endOffset = end.toInt(),
                note = note
            )
        }.getOrNull()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("OCR-Seite", style = MaterialTheme.typography.headlineSmall)
        Text("Seitennummer wird pro Seite gespeichert. Das Foto wird nach OCR verworfen.")
        OutlinedTextField(value = pageNumber, onValueChange = { pageNumber = it.filter(Char::isDigit) }, label = { Text("Seitennummer") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("OCR-Text") }, modifier = Modifier.fillMaxWidth(), minLines = 5)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = start, onValueChange = { start = it.filter(Char::isDigit) }, label = { Text("Start") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = end, onValueChange = { end = it.filter(Char::isDigit) }, label = { Text("Ende") }, modifier = Modifier.weight(1f))
        }
        OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Notiz") }, modifier = Modifier.fillMaxWidth())
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Vorschau")
                Spacer(Modifier.height(8.dp))
                if (annotation != null) {
                    Text(buildAnnotatedString {
                        val s = annotation.target.startOffset
                        val e = annotation.target.endOffset
                        append(text.substring(0, s))
                        withStyle(SpanStyle(background = Color.Yellow)) { append(text.substring(s, e)) }
                        append(text.substring(e))
                    })
                    Spacer(Modifier.height(8.dp))
                    Text("Annotation-ID: ${annotation.annotationId}")
                    Text("Selected Text: ${annotation.target.selectedText}")
                } else {
                    Text("Ungültiger Textbereich")
                }
            }
        }
    }
}

@Composable
private fun RelayScreen() {
    var relays by remember {
        mutableStateOf(
            listOf(
                "wss://relay.damus.io",
                "wss://nos.lol",
                "wss://relay.nostr.band",
                "wss://nostr.wine",
                "wss://relay.primal.net"
            ).joinToString("\n")
        )
    }
    val count = relays.lines().filter { it.isNotBlank() }.take(PrivacyPolicy.MAX_RELAYS).size
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Relays", style = MaterialTheme.typography.headlineSmall)
        Text("Bis zu ${PrivacyPolicy.MAX_RELAYS} Relays. Alternativ kann später NIP-65 ausgelesen werden.")
        OutlinedTextField(value = relays, onValueChange = { relays = it }, label = { Text("Relay-Liste") }, modifier = Modifier.fillMaxWidth(), minLines = 6)
        Text("Aktiv: $count/${PrivacyPolicy.MAX_RELAYS}")
        Button(onClick = { /* NIP-65 import should be connected here */ }) { Text("Relayliste aus NIP-65 lesen") }
    }
}

@Composable
private fun LoginScreen() {
    var loginMode by remember { mutableStateOf("NIP-46 / Nostr Connect") }
    var value by remember { mutableStateOf("bunker://...") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Login", style = MaterialTheme.typography.headlineSmall)
        Text("Papermarks nutzt Nostr-Schlüssel statt E-Mail/Passwort.")
        Button(onClick = { loginMode = "NIP-46 / Nostr Connect" }) { Text("NIP-46 / Nostr Connect") }
        Button(onClick = { loginMode = "Lokaler nsec-Import" }) { Text("nsec importieren") }
        Button(onClick = { loginMode = "npub Lesemodus" }) { Text("npub-only Lesemodus") }
        OutlinedTextField(value = value, onValueChange = { value = it }, label = { Text(loginMode) }, modifier = Modifier.fillMaxWidth())
        Text("Empfohlen: NIP-46, damit der Private Key nicht in Papermarks liegen muss.")
    }
}
