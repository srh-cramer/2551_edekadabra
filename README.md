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
3. Sie stößt den Export in eine CSV-Datei an. (Aufgabe 4)
4. Sie zeigt dem Nutzer eine Nachricht über Erfolg oder Misserfolg des Exports an. (Aufgabe 4)

Folgendes Code-Snippet zeigt (ohne Imports), wie mit JavaFX ein Dialog zur Auswahl einer txt-Datei umgesetzt wird:
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
Was benötigen Sie, um diese Methode aufrufen zu können? Schreiben Sie zunächst eine Art Platzhalter-Umsetzung, die Sie später noch verfeinern.

### Tipps
* Die Methode `handleExport()` wird verhältnismäßig groß werden -- lassen Sie sich davon nicht abschrecken. Versehen Sie Ihren Code zur Hilfe mit Kommentaren.
* Das Code-Snippet arbeitet mit txt-Dateien. Sie müssen einige Stellen zu csv anpassen.
* Den Abbruch-Fall durch den User können Sie mit einem einfachen `if (file == null)` prüfen.
* Um die gegebene Methode aus der neu angelegten Service-Klasse aufrufen zu können, benötigen Sie:
  * eine Instanz der Klasse `CartCsvService`. Orientieren Sie sich dazu an der Instanziierung von `ShopService` 
  * einen `try-catch`-Block beim Aufruf der Methode

---

## Aufgabe 4 - In eine CSV-Datei schreiben

Im Rahmen der letzten Aufgabe haben Sie die neue Klasse `CartCsvService` mit der Methode `exportCart()` aufgerufen, die jedoch noch keinen Inhalt hat.
Das soll nun geändert werden. In dieser Methode soll die tatsächliche CSV-Datei mit Inhalt befüllt werden.

Zum Schreiben von CSV-Dateien existieren vorgefertigte CSV-Parser, die das Verarbeiten der Datei vereinfachen. 
Wir entscheiden uns hier bewusst gegen einen Parser, sondern schreiben die CSV-Datei mit purem Java selbst.
Hierzu steht uns die Java-Library `Files` zur Verfügung, mit der wir Dateien lesend und schreibend öffnen können.

Folgendes Code-Snippet schreibt eine Menge an Textzeilen (`List<String> lines`) in eine Datei (`File file`):
```java
import java.nio.file.Files;

Files.write(file.toPath(), lines);
```

### Anforderungen
* Schreiben Sie den Methodenkörper der Methode `exportCart(List<CartEntryData> entries, File file)` in der Klasse `CartCsvService`. Die Methode tut Folgendes:
  1. Sie erstellt einen String, der den CSV-Header enthält (dieser darf hardgecoded sein)
  2. Sie erstellt pro Eintrag in `entries` einen String, der CSV-konform ist (Kommas als Seperator) und zum CSV-Header passt
  3. Sie schreibt alle erstellten Strings in die gegebene Datei `file`
* Überprüfen Sie den Aufruf der Methode in der Controller-Methode `handleExport()`. Setzen Sie nun den letzten Schritt von `handleExport()` um:
Sie zeigt dem Nutzer eine Nachricht über Erfolg oder Misserfolg des Exports an:
  * Bei Misserfolg soll dem Benutzer der Fehler angezeigt werden.
  * Überlegen Sie sich hierzu, wann der Misserfolg eintritt. Die nötige Kontrollstruktur haben Sie schon in der vorherigen Aufgabe geschrieben.
  * Die Anzeige soll mithilfe von Alert-Fenstern erfolgen. Hierfür stehen Ihnen bereits Methoden im Controller zur Verfügung!
* Am Ende dieser Aufgabe soll ein vollständiger Export in eine CSV-Datei möglich sein.

### Tipps
* Zum Erstellen der CSV-Textzeilen brauchen Sie bloß über ihre Records zu iterieren. 
Behalten Sie die Reihenfolge der Einträge im Blick -- die Reihenfolge muss der im Header entsprechen!
* Wenn Sie Ihre Variablen entsprechend benennen, können Sie das Code-Snippet direkt in Ihre Methode aufnehmen.
* Der Misserfolg in `handleExport()` tritt bei einer `IOException` auf. Sie können diese Exception `catch`en und ihren Inhalt mit `.getMessage()` erhalten.
* Mit den vorhandenen Methoden `showMessage()` und `showError()` können Sie verschiedene Nachrichtentypen auf der GUI als Alert anzeigen lassen.

---

## Aufgabe 5 - Import mit CSV-Parser und Warenkorb-Wiederherstellung

Nach dem Export beschäftigen wir uns nun mit dem Import aus einer CSV-Datei. 
Hierfür müssen wir eine CSV-Datei lesen, was im Verhältnis zum Schreiben deutlich komplizierter ist.

Mögliche Fehlerquellen sind u.A.:
* Die Datei entspricht nicht dem gewünschten Format CSV
* Die CSV-Datei benutzt ein anderes Trennzeichen als erwartet
* Die Einträge enthalten ein Komma, das nicht als Trennzeichen fungiert
* Die Datei enthält leere Zeilen

Ein dedizierter CSV-Parser würde für uns einige dieser Probleme lösen, wir bleiben jedoch beim puren Java.
Der Einfachheit halber ignorieren wir die meisten dieser potenziellen Fehlerquellen und geben sie im Zweifelsfall an den Benutzer als Alert durch.

Am Ende dieser Aufgabe soll es dem Benutzer möglich sein, einen Warenkorb aus einer CSV-Datei zu importieren.
Der existierende Warenkorb soll dabei komplett **überschrieben** werden, d.h. sie müssen nicht alte Daten mit neuen zusammenführen.

Folgendes Code-Snippet liest eine Datei `file` ein und speichert ihren Inhalt zeilenweise in einer Liste. 
Anschließend splittet sie die erste Zeile an den Kommas und speichert die Teile in einem String-Array:
```java
// Speichere Zeilen in einer Liste:
List<String> lines = Files.readAllLines(file.toPath());

//Splitte die erste Zeile an den Kommas: "A,B,C" wird zu {"A", "B", "C"} 
String[] headerParts = lines[0].split(",");
```

### Anforderungen
* Implementieren Sie die Methode `importCart(File file)` in der Klasse `CartCsvService`:
  1. Diese Methode hat den Rückgabetyp `List<CartEntryData>` und kann eine `IOException` verursachen. 
Passen Sie den Methodenkopf entsprechend an.
  2. Sie liest die gegebene Datei zeilenweise ein und erstellt aus jeder Zeile (außer dem Header!) mithilfe einer Schleife eine Instanz der Klasse `CartEntryData`.
  3. Hierzu wird jede Zeile mit `.split(",")` in ihre Bestandteile aufgesplittet, die dann als Input für die einzelnen Record-Attribute dienen. 
Achten Sie auch hier auf die Reihenfolge der Felder in der CSV-Datei!
  3. Am Ende wird eine Liste der erstellten Records zurückgegeben.
  4. Es wird **keine** Überprüfung auf ungültige Kommas/Trennzeichen, leere Zeilen, oä. durchgeführt!
* Implementieren Sie die Methode `handleImport()` in der Klasse `ShopController`:
  1. Diese Methode ist sehr ähnlich zu `handleExport()`: Sie verwendet auch einen `FileChooser`, um den Nutzer eine Quelldatei auswählen zu lassen.
Die Methode `showSaveDialog()` ist hier aber vermutlich nicht die richtige Wahl ...
  2. Anschließend ruft sie die neue CartCsvService-Methode `importCart()` mit der vom User festgelegten Datei auf
  3. Die so erstellte Liste aus Records `List<CartEntryData>` muss nun in den Warenkorb importiert werden. 
Versuchen Sie sich dazu entweder selbst an einer `ShopService`-Methode `importCartEntries()` (deutlich schwieriger) oder übernehmen Sie sie aus den Tipps und platzieren Sie sie im `ShopService` (einfacher).
  4. Rufen Sie die erstellte Methode in `handleImport()` auf, um Ihren Warenkorb mit den Einträgen zu befüllen. 
Denken Sie daran, den Warenkorb vorher zu leeren!
  5. Am Ende soll auch wieder eine (Miss)erfolgs-Meldung ausgegeben werden.
* Am Ende dieser Aufgabe ist der User in der Lage, den Warenkorb aus einer CSV-Datei zu importieren.

### Tipps
* Um beim Lesen der CSV-Zeilen die erste Zeile zu überspringen, können Sie den Zähler Ihrer for-Schleife bei 1 starten lassen.
* Die einzelnen Teile einer gesplitteten Zeile sind vom Datentyp `String`. 
Mit `Integer.parseInt(s)` können Sie einen `String s` in ein `int` umwandeln. 
* Die `ShopService`-Methode `importCartEntries()` sieht folgendermaßen aus:
```java
public void importCartEntries(List<CartEntryData> entries){
  for (CartEntryData entry : entries){
    Optional<Product> product = products.findById(entry.id());
    if (product.isPresent()){
      for (int i=0; i < entry.quantity(); i++){
        cart.addProduct(product.get());
      }
    }
  }
}
```
* Den Großteil der Methode `handleImport()` können Sie aus `handleExport()` mit nur sehr kleinen Anpassungen übernehmen. 
Dazu gehören der Aufbau des FileChooser, das Prüfen auf eine fehlende Datei und das Exception Handling!
* Verwenden Sie beim FileChooser zum Öffnen einer Datei statt `showSaveDialog()` die Methode `showOpenDialog()` mit demselben Argument.
* Der `ShopService` bietet die Methode `clearCart()` an, die den Warenkorb komplett leert.

---

## Aufgabe 6 - Abhängigkeiten verwalten am Beispiel Jackson

Nachdem der CSV-Import und -Export umgesetzt wurde, soll nun das gleiche für JSON-Dateien möglich sein.
Da JSON-Dateien deutlich komplexer als CSV-Dateien sind, ist es sinnvoll, hier mit einem Parser zu arbeiten.

Es gibt verschiedene externe Libraries, die das Verarbeiten von JSON-Dateien anbieten. 
Eine sehr populäre Library dafür heißt **Jackson**.

Da Jackson eine externe Abhängigkeit (engl: **Dependency**) darstellt, muss diese zusätzlich installiert werden.
Die Verwaltung von Dependencies übernimmt für uns in diesem Projekt der Package Manager **Maven**.
Jede Dependency wird als Eintrag in der Datei `pom.xml` (POM: Project Object Model) verwaltet.
Diese Datei enthält Projektinformationen, die Build-Konfiguration und Abhängigkeiten.

Weiterhin gibt es in vielen Java-Anwendungen die Datei `module-info.java`, die unabhängig von Maven Informationen darüber hat, 
welche Module die Anwendung benötigt und welche Pakete sie anderen Modulen zugänglich macht.
Auch hier müssen bestimmte Abhängigkeiten aufgeführt werden.

Die Datei `module-info.java` existiert dann, wenn man das Java Module System verwendet, was bei der Nutzung von JavaFX standardmäßig der Fall ist.

### Anforderungen
* Fügen Sie Jackson als Dependency in der `pom.xml` hinzu:
```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.22.4</version>
</dependency> 
```
* Laden Sie anschließend die Dependencies neu. Hierfür gibt es einen Button, der bei Änderungen an der `pom.xml` oben rechts im Editor erscheint.
* Fügen Sie in der Datei `module-info.java` folgende Einträge hinzu:
```java
requires com.fasterxml.jackson.databind;
opens com.cramer._2551_wiz_mart.model to com.fasterxml.jackson.databind;
exports com.cramer._2551_wiz_mart.model;
```
* Testen Sie, ob sich die Anwendung weiterhin fehlerfrei ausführen lässt
---

## Aufgabe 7 - Export mit Jackson

Wieder wollen wir uns zunächst mit dem Export des Warenkorbs zu JSON beschäftigen.

Viele Voraussetzungen haben wir dafür im Rahmen des CSV-Exports schon umgesetzt.
Wir können z.B. weiterhin den Record `CartEntryData` und die die damit verbundenen Methoden `getCartEntryData()` und `importCartEntries()` im `ShopService` nutzen.

Zur klaren Trennung wird der JSON-Export in einer eigenen Service-Klasse `CartJsonService` umgesetzt, die noch nicht existiert.
Auch diese Klasse wird die Methoden `exportCart()` und `importCart()` bekommen.

In dieser Aufgabe beschäftigen wir uns mit `exportCart()`. Diese Methode wird sehr überschaubar bleiben:
Jackson ist in der Lage, Records automatisch zu serialisieren und erspart uns damit viel Schreibarbeit.

Folgendes Code-Snippet zeigt, wie man mit Jackson eine Liste aus Objekten `List<Object> entries` in eine Datei `File file` schreibt:
```java
import com.fasterxml.jackson.databind.ObjectMapper;

ObjectMapper mapper = new ObjectMapper();
mapper.writerWithDefaultPrettyPrinter().writeValue(file, entries);
```

Anschließend sind Anpassungen in der Controller-Klasse `handleExport()` nötig, da diese momentan pauschal den CSV-Export anstößt.
Hier muss eine Prüfung eingebaut werden, zu welchem Dateityp exportiert werden soll, damit anschließend die entsprechende `exportCart()`-Methode ausgefürt wird.


### Anforderungen
* Ergänzen Sie die neue Service-Klasse `CartJsonService`. Legen Sie hier die Methode `exportCart()` an. 
Orientieren Sie sich beim Methodenkopf an der Umsetzung in `CartCsvService`
* Implementieren Sie die Methode `exportCart()`. Orientieren Sie sich eng am obigen Code Snippet.
* Fügen Sie dem Controller eine `CartJsonService`-Instanz als Attribut hinzu, so wie Sie es auch schon für `CartCsvService` gemacht haben.
* Passen Sie die Methode `handleExport()` im Controller an:
  * Ergänzen Sie einen zweiten `ExtensionFilter` für JSON-Dateien und fügen Sie ihn dem `FileChooser` hinzu.
  * Ergänzen Sie an der richtigen Stelle eine Prüfung: Wenn der Nutzer eine JSON-Datei ausgewählt hat, soll die JSON-Export-Methode aufgerufen werden, sonst die CSV-Export-Methode.
Überlegen Sie sich dazu, wie Sie prüfen können, für welchen Dateityp sich der Benutzer entschieden hat.
* Am Ende dieser Aufgabe kann der Benutzer erfolgreich seinen Warenkorb in eine JSON-Datei exportieren.

### Tipps
* Die Methode `exportCart()` wird nur 2 Zeilen Code umfassen. Den Methodenkopf können Sie 1 zu 1 aus `CartCsvService` übernehmen.
* Es gibt mehrere Wege, zu prüfen, für welchen Dateityp der Benutzer sich entschieden hat.
Ein Weg ist die Methode `fileChooser.getSelectedExtensionFilter()`.
* Die Prüfung platzieren Sie unmittelbar an der Stelle, an der bisher `cartCsvService.exportCart()` aufgerufen wird, d.h. **im** `try`-Block.

---

## Aufgabe 8 - Import mit Jackson

Zum Schluss wird der Import mit Jackson ergänzt. 
Hierfür muss die Methode `importCart()` im `CartJsonService` implementiert werden und die Methode `handleImport()` im Controller angepasst werden.

Der Import mit Jackson gestaltet sich nur unwesentlich schwieriger als der Export.
Um aus einer JSON-Datei eine Liste aus Java-Objekten zu generieren, ist eine Jackson-`TypeReference` notwendig: 
Diese sagt Jackson, in was für ein spezifisches Objekt der JSON-Text umgewandelt werden soll.
Die Syntax dazu ist etwas speziell:

```java
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

ObjectMapper mapper = new ObjectMapper();

// Erstelle eine Typ-Referenz für eine Liste aus CartEntryData:
TypeReference<List<CartEntryData>> reference = new TypeReference<List<CartEntryData>>() {};

// Benutze diese Referenz beim Lesen der Datei:
return mapper.readValue(file, reference);
```

### Anforderungen
* Setzen Sie die Methode `importCart()` in `CartJsonService` um. 
Sie können das Code-Snippet aus der Einleitung dazu komplett übernehmen.
* Passen Sie die Methode `handleImport()` im Controller an:
  * Fügen Sie dem bestehenden `ExtensionFilter` ein weiteres Argument `*.json` hinzu, damit dieser eine Filter sowohl CSV- als auch JSON-Objekte anzeigt.
  * Ergänzen Sie erneut eine Prüfung darauf, welchen Dateityp der Nutzer ausgewählt hat.
Der Lösungsvorschlag aus der letzten Aufgabe funktioniert hier nicht!
  * **Optional:** Fangen Sie den Fall ab, dass der Benutzer eine Datei ungültigen Typs bereitstellt.
In diesem Fall soll eine `IOException` ausgelöst werden.
* Am Ende dieser Aufgabe kann der Benutzer erfolgreich den Warenkorb aus einer JSON-Datei importieren.

### Tipps
* Da hier **ein** Filter für **beide** Dateitypen fungiert, können Sie diesmal nicht den ausgewählten Filter überprüfen.
Prüfen Sie stattdessen einfach die Dateiendung der Datei selbst:
Mit `file.getName()` bekommen Sie den Namen der Datei als String und können hiermit Überprüfungen anstellen.
* Um zu überprüfen, ob ein String eine bestimmte Endung hat, hilft Ihnen die String-Methode `.endsWith()`
