# Edekadabra (Wiz-Mart V2)

Willkommen bei **Edekadabra**, oder auch Wiz-Mart V2, dem minimal besseren Shop für Zauberer.

In diesem Projekt entwickeln Sie eine bestehende JavaFX-Anwendung für einen kleinen Shop weiter. 
Der Shop funktioniert genau so, wie Sie ihn aus der ersten Version zurückgelassen haben. 

Der Shop soll nun um eine furchtbar praktische Funktion erweitert werden:
Der Warenkorb soll als Datei exportiert oder aus einer Datei importiert werden können. 
So kann ein Zauberer da weitermachen, wo er bei seinem letzten Shopping Trip aufgehört hat.

Sie werden in diesem Projekt folgende Konzepte kennenlernen:

* Records
* CSV-Dateien lesen/schreiben
* JSON-Dateien lesen/schreiben mit Java-Bibliothek Jackson

## Ausgangssituation und Zielbild

Der Shop unterstützt alle Funktionalitäten aus V1. 
Der Code entspricht weitestgehend dem finalen Stand des Projektes, d.h. das Observer-Pattern ist für Warenkorb-Änderungen implementiert.

Ihre Aufgaben:
* Die UI erweitern, sodass der Benutzer den Import und Export seines Warenkorbs anstoßen kann
* Beim Export soll der User den gewünschten Speicherort und Dateityp angeben (CSV oder JSON) 
und der Inhalt des Warenkorbs soll im gewünschten Dateiformat gespeichert werden
* Beim Import soll der User den Speicherort der Quelldatei angeben 
und der Warenkorb soll mit den Produkten aus der Datei befüllt werden

---

## Aufgabe 1 - UI erweitern
Der Benutzer soll nun in der UI die möglichkeit haben, den Export und den Import seines Warenkorbs mit einem Button auszulösen.

### Anforderungen
* Ergänzen zwei Buttons `Export Cart ...` und `Import Cart ...`:
  * Sie befinden sich unterhalb dem Button `Place Order`
  * Sie befinden sich nebeneinander
* Ergänzen Sie im Controller zwei leere Methoden `handleImport()` und `handleExport()`, die mit dem jeweiligen Button im FXML verknüpft sind.

### Tipps
* Überprüfen Sie den Layout-Baum im SceneBuilder. Mit einer `HBox` lassen sich Elemente nebeneinander platzieren.

---

## Aufgabe 2 - Warenkorb-Einträge mit Records verwalten
Um Warenkorbeinträge zu exportieren, muss man sich zunächst überlegen, welche Informationen ein Eintrag in der exportieren Datei haben soll.
Als einzelnen Eintrag könnte man die Klassen `Product` oder `CartItem` verwenden, diese sind aber eigentlich für die interne Businesslogik reserviert.
Falls sich an diesen Klassen etwas ändert, hätte das evtl. unerwünschte Auswirkungen auf die exportierten Dateien: 
Die Serialisierung würde zu stark von der internen Klassenstruktur abhängen.

Daher wird für solche Anwendungsfälle eher eine eigene Klasse angelegt, die nur für den Datenaustausch verwendet wird.
Dies ist auch ein häufiger Einsatzzweck für **Records**.

Records sind spezielle Java-Klassen, die hauptsächlich zur kompakten Darstellung unveränderlicher Daten dienen.
Sie verfügen automatisch über Methoden wie Konstruktor und Zugriffsmethoden in Ihrer "Standardausführung", ohne dass sie manuell definiert werden müssen.

Ein Record `Entry` mit dem einzigen Attribut `id` wird folgendermaßen definiert:
```java
public record Entry(
        int id
) {}
```
Die äquivalente Java-Klasse dazu sähe folgendermaßen aus:
```java
public final class Entry {

    private final int id;

    public Entry(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    @Override
    public String toString() {
        return "Entry[id=" + id + "]";
    }
    //Weitere Override-Klassen equals(), hashCode()
}
```
Die Arbeit mit Records kann uns viel Schreiberei ersparen, sie sind jedoch weniger mächtig als normale Klassen:
Ein Record ist **immutable** (engl. unveränderbar), d.h. die Werte seiner Attribute lassen sich nicht nachträglich ändern (d.h. es gibt keine Setter).

### Anforderungen
* Definieren Sie im Package `model` ein Record `CartEntryData`.
* Überlegen Sie sich, welche Attribute der Record haben soll. Ein Attribut entspricht einer Information zu einem Eintrag in der Warenkorb-Datei. Behalten Sie dabei zwei Ziele im Auge:
  * Aus der Warenkorb-Datei muss sich der Warenkorb auch wieder vollständig importieren lassen. Welche Informationen brauchen Sie hierzu?
  * Die Warenkorb-Datei soll auch für Menschen lesbar sein. Welche Felder tragen zur Lesbarkeit für einen Menschen bei?
* Ergänzen Sie in der Klasse `ShopService` eine Methode `getCartEntryData()`: 
  * Diese Methode hat Zugriff auf das Attribut `cart`, das den aktuellen Warenkorb enthält.
  * Diese Methode soll als Rückgabetyp `List<CartEntryData>` haben: Eine Liste aus Records mit einem Record pro Warenkorbeintrag.
  * Verwenden Sie für das Konvertieren von `List<CartItem>` zu `List<CartEntryData>` eine Schleife (einfacher) oder zur besseren Übung einen Stream (schwieriger).


### Tipps
* Die benötigten Attribute, um einen Warenkorb zu importieren, sind `id` und `quantity`.
* Hilfreich für einen Menschen zum Lesen eines Warenkorbs sind vermutlich die Felder `name` und `unitPrice`.
* Beim Umwandeln eines Objektes `CartItem` zu einem Record `CartEntryData` benötigen Sie den automatischen Konstruktor des Records.
Jedes Attribut, das Sie dem Record gegeben haben, muss hier als Argument aufgeführt werden.
* Wenn Sie zum Umwandeln einen Stream benutzen, hilft Ihnen die Stream-Methode `.map()`, um für jedes Element im Stream ein neues Objekt anzulegen.
---

## Aufgabe 3 - Export mit FileChooser auslösen

Mithilfe des angelegten Records soll nun ein Warenkorb in eine CSV-Datei exportiert werden können.
Bevor der Inhalt in eine CSV-Datei geschrieben wird, soll der User jedoch den Speicherort über die GUI auswählen können.

Der `Controller` wird mit `handleExport()` den Export des Warenkorbs anstoßen. In dieser Methode soll der Reihe nach Folgendes geschehen:
1. Sie zeigt dem User in der GUI einen `FileChooser` zur Auswahl der Zieldatei an und weist seine Auswahl einer Variable zu.
2. Sie wandelt den Warenkorb mithilfe der neuen Service-Methode `getCartEntryData()` in eine Liste aus Records um.
3. Sie stößt den Export in eine CSV-Datei an
4. Sie zeigt dem Nutzer eine Nachricht über Erfolg oder Misserfolg des Exports an.

Folgendes Code-Snippet zeigt (ohne Imports), wie mit JavaFX ein Datei-Dialog umgesetzt wird:
```java
FileChooser fileChooser = new FileChooser();

fileChooser.setTitle("Choose File");
fileChooser.setInitialFileName("default.txt");

FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT Files", "*.txt");
fileChooser.getExtensionFilters().addAll(txtFilter);

File file = fileChooser.showSaveDialog(productPane.getScene().getWindow());
```
Die schlussendliche Auswahl des Users ist ein Objekt vom Typ `File`. Bricht der User den Dialog ab, ist das Objekt `null`.

### Anforderungen
* Setzen Sie die Schritte 1 und 2 der Methode `handleExport()` um.
  * Passen Sie das Code-Snippet zum `FileChooser` für Ihre Zwecke an. Überlegen Sie sich aussagekräftige Inhalte für Titel und initialen Dateinamen.
  * Fangen Sie den Fall ab, dass der User den Datei-Dialog abbricht. In diesem Fall kann die Methode einfach vorzeitig enden.
  * Am Ende von Schritt 2 sollten Sie je eine Variable vom Typ `File` und eine vom Typ `List<CartEntryData>` haben.
* Legen Sie im Package `service` eine neue Klasse `CartCsvService` an. Legen Sie dort eine neue Methode mit folgendem Methodenkopf an:
`public void exportCart(List<CartEntryData> entries, File file) throws IOException `
  * Lassen Sie den Methodenkörper zunächst leer (Inhalt folgt in der nächsten Aufgabe)
  * Rufen Sie diese Methode in der Controller-Methode auf, um die Umsetzung von Schritt 3 zu starten. 
Welche Kontrollstruktur benötigen Sie, um diese Methode aufrufen zu können? Schreiben Sie zunächst eine Art Platzhalter-Umsetzung, die Sie später noch verfeinern.

### Tipps
* Die Methode `handleExport()` wird verhältnismäßig groß werden -- lassen Sie sich davon nicht abschrecken. Versehen Sie Ihren Code zur Hilfe mit Kommentaren.
* Das Code-Snippet arbeitet mit txt-Dateien. Sie müssen einige Stellen zu csv anpassen.
* Den Abbruch-Fall durch den User können Sie mit einem einfachen `if (file == null)` prüfen.
* Um die gegebene Methode aus der neu angelegten Service-Klasse aufrufen zu können, benötigen Sie einen `try-catch`-Block.

---

## Aufgabe 4 - In eine CSV-Datei schreiben

Wir entscheiden uns hier bewusst gegen einen vorgefertigten CSV-Parser, sondern schreiben die CSV-Datei mit purem Java selbst.

---

## Aufgabe 5 - Import mit CSV-Parser und Warenkorb-Wiederherstellung

---

## Aufgabe 6 - Maven-Abhängigkeiten verwalten am Beispiel Jackson

---

## Aufgabe 7 - Export mit Jackson

---

## Aufgabe 8 - Import mit Jackson
