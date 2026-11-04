# ⏱️ Zeitgefühl

> **Ein minimalistisches, modernes Reaktions- und Zeitwahrnehmungs-Spiel für Android, entwickelt mit Kotlin und Jetpack Compose.**

Wie gut ist dein inneres Zeitgefühl? Bei **Zeitgefühl** stellst du eine Zielzeit ein, hältst den Button gedrückt und musst genau im richtigen Moment loslassen. Der Clou: Nach nur einer Sekunde blendet sich der Timer vollständig aus – ab dann zählt nur noch deine innere Uhr!

---

## ✨ Features & Spielprinzip

1. **🎯 Flexible Zielzeit-Einstellung**
   - Der untere Button zeigt jederzeit die aktuell gewählte Zielzeit an (z. B. `🎯 Zielzeit: 5,0 s`).
   - Über das Einstellungs-Popup kann die Zielzeit über **Schnellauswahl-Chips** (`3s`, `5s`, `10s`, `15s`, `20s`, `30s`), einen **stufenlosen Slider** oder präzise **Stepper-Tasten (`+` / `-`)** im Bereich von 1 bis 30 Sekunden angepasst werden.

2. **🔘 Intuitiver Hold-Button („Gedrückt halten“)**
   - Ein großer, zentrierter Kreis-Button mit sanfter Feder-Skalierung (`animateFloatAsState`).
   - Taktiles Erlebnis durch abgestimmtes **haptisches Feedback** beim Antippen und Loslassen.
   - Ein leuchtender, pulsierender Ring signalisiert visuell die aktive Messung.

3. **🙈 Automatische Blind-Phase nach 1 Sekunde**
   - Beim Start läuft der Timer kurz sichtbar an.
   - Nach exakt **1.000 Millisekunden** blendet sich die Zeitanzeige sanft aus.
   - Der Spieler muss die restliche Zeit rein nach Gefühl schätzen.

4. **📊 Hochpräzise Millisekunden-Auswertung**
   - Stoppt sofort beim Loslassen des Buttons.
   - Ein übersichtlicher Ergebnis-Dialog liefert:
     - **Gemessene Zeit** (z. B. `5,142 s`)
     - **Eingestellte Zielzeit** (z. B. `5,000 s`)
     - **Genaue Abweichung** mit Vorzeichen im Millisekundenbereich (z. B. `+142 ms` oder `-38 ms`) mit Farbkodierung (Grün, Gelb, Rot)
     - **Genauigkeits-Score in Prozent** & **5-Sterne-Bewertung**
     - Motivierendes Feedback (z. B. *„Perfektes Zeitgefühl! 🎯“*, *„Unmenschliche Präzision! ⚡“*)

5. **🌓 Dynamic Color (Material You) & Dark-/Light-Mode**
   - Nutzt die **persönliche Farbpalette deines Smartphones** (aus Hintergrundbild und Systemdesign abgeleitet).
   - Ein Icon oben rechts in der Kopfzeile erlaubt den nahtlosen Wechsel zwischen dunklem und hellem Modus.
   - Orientiert sich initial an den Systemeinstellungen des Smartphones.

6. **🎨 Modernes Adaptive App-Icon**
   - Maßgeschneidertes Vektor-Icon mit Stoppuhr-Design, leuchtendem Zeitbogen und Berührungswelle.
   - Unterstützt auch Android 13+ **Material You Themed Icons** (Monochrome-Ebene).

---

## 🏗️ Technische Details & Architektur

- **UI-Framework:** 100% [Jetpack Compose](https://developer.android.com/jetpack/compose) mit [Material 3](https://m3.material.io/)
- **Architektur:** MVVM (Model-View-ViewModel) mit unidirektionalem Datenfluss (`StateFlow<GameUiState>`)
- **Zeitmessung:** Präzise, unbeeinflussbar von Systemzeit-Änderungen über `SystemClock.elapsedRealtime()`
- **Gesten:** Zuverlässiges Touch-Tracking via `awaitEachGesture` mit `try/finally`-Absicherung
- **Design:** Vollständige Edge-to-Edge Unterstützung (`enableEdgeToEdge()`)

---

## 📂 Projektstruktur

```text
app/src/main/java/de/tfc/zeitgefuehl/
├── MainActivity.kt                  # Einstiegspunkt mit Edge-to-Edge & Theme-Scoping
├── model/
│   └── GameState.kt                 # GameStatus, GameResult & GameUiState
├── viewmodel/
│   └── GameViewModel.kt             # Spiellogik, Timer-Coroutine & StateFlow
├── ui/
│   ├── components/
│   │   ├── HoldButton.kt            # Interaktiver Kreis-Button mit Haptik & Puls-Effekt
│   │   ├── TimerDisplay.kt          # Monospace-Timer mit sanfter Alpha-Animation
│   │   ├── TargetTimeDialog.kt      # Zielzeit-Popup mit Chips, Stepper & Slider
│   │   └── ResultDialog.kt          # Detaillierte Ergebnisdarstellung & Sterne-Rating
│   ├── screens/
│   │   └── GameScreen.kt            # Hauptbildschirm inklusive Theme-Toggle & Previews
│   └── theme/
│       ├── Color.kt                 # Farbpaletten für Dark & Light Mode
│       ├── Theme.kt                 # Material 3 Theme Definition
│       └── Type.kt                  # Monospace-Typografie für Stoppuhr-Ziffern
```

---

## 🚀 Setup & Ausführung in Android Studio

1. **Projekt öffnen:**
   - Android Studio öffnen und `Open` wählen.
   - Den Ordner `Zeitgefuehl` auswählen.

2. **Gradle Sync:**
   - Android Studio synchronisiert die Abhängigkeiten automatisch über Version Catalogs (`gradle/libs.versions.toml`).

3. **Starten:**
   - Ein Android-Gerät (ab Android 12 / API 31 empfohlen) oder einen Emulator auswählen.
   - Auf **Run (▶)** klicken oder `Shift + F10` drücken.

---

Viel Spaß beim Testen deines Zeitgefühls! ⏱️🎯
