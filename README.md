# Tricoloring Puzzle

Dieses Projekt ist im 7. Bachelorsemester im Modul "Modellierung" entstanden. Ziel war es, die Architektur Model-View-Controller (MVC) an einer kleinen Swing-Anwendung praktisch umzusetzen.

## Das Puzzle

Gespielt wird auf einer Pyramide aus Feldern mit frei wählbarer Höhe (2 bis 18). Jedes Feld wird mit einer von drei Farben eingefärbt. Für jedes Dreieck aus einem Feld und den beiden direkt darunterliegenden Feldern gilt: Die drei Farben sind entweder alle gleich oder alle verschieden. Mathematisch entspricht das der Bedingung, dass die Summe der drei Farbwerte modulo 3 gleich 0 ist.

Die Anwendung kennt zwei Modi:

- **Spielmodus:** Es werden so viele Felder vorgegeben, wie die Pyramide hoch ist (schwarz umrandet). Die übrigen Felder müssen so gefärbt werden, dass alle Bedingungen erfüllt sind. Über "Check" wird die Lösung geprüft, "Solve" zeigt eine gültige Lösung, "New Game" erzeugt neue Vorgaben und "Reset" setzt die eigenen Eingaben zurück.
- **Testmodus:** Hier wählt man selbst so viele Felder aus, wie die Pyramide hoch ist, und färbt sie ein. "Check" prüft, ob diese Auswahl das Puzzle eindeutig bestimmt.

Intern wird das Puzzle als lineares Gleichungssystem über dem Restklassenkörper modulo 3 behandelt. Lösungen werden per Rückwärtseinsetzen berechnet, die Eindeutigkeit im Testmodus über den Rang einer aus dem Nullraum gebildeten Matrix (Gauß-Elimination modulo 3).

## Aufbau

```
src/
  Anwendung.java            Einstiegspunkt, verbindet Model, Controller und View
  observer/                 Subjekt- und Beobachter-Schnittstelle
  model/                    Spiellogik (TricoloringPuzzle), Modi und Statusmeldungen
  controller/               Strategy-Schnittstelle und Controller, der Eingaben an das Model weitergibt
  view/                     View (Beobachter) und Swing-Oberfläche (Gui)
test/
  model/                    JUnit-Tests für TricoloringPuzzle
```

Die Anwendung folgt dem MVC-Prinzip:

- **Model:** `TricoloringPuzzle` enthält den Spielzustand und die gesamte Logik. Es kennt weder View noch Controller.
- **View:** `View` und `Gui` stellen den Zustand dar und nehmen Eingaben entgegen, verändern das Model aber nicht selbst.
- **Controller:** `Controller` nimmt die Eingaben entgegen und ruft die passenden Methoden des Models auf.

Model und View sind über das Beobachter-Muster verbunden: `TricoloringPuzzle` implementiert neben `Model` auch `Subjekt`, die `View` registriert sich als `Beobachter` und wird nach jeder Zustandsänderung über `aktualisiere(...)` benachrichtigt. View und Controller sind über das Strategie-Muster verbunden: Die Oberfläche kennt den Controller nur über die Schnittstelle `Strategy`, sodass er sich austauschen lässt, ohne die View zu ändern.
