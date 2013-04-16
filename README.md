### Term Project### University of Zurich, Department of InformaticsRecommender System based on Hierarchical 2-Way Agglomerative Clustering======This is the implementation of a recommender system based on a novel clustering algorithmus. With a single training data set containing instances of the type content-user-rating the clustering algorithm creates one cluster hierarchy for the users and one for the content items. While the two hierarchchies are build from bottom up, clustering information is transferred between the hierarchies (2-way clustering). We show that with this exchange of information the recommender system generates more accurate rating predictions (MAE: 1.79, on a rating scale of 0 to 10) than without (MAE: 1.98). ### Responsibilities:[alessandramacri](https://github.com/alessandramacri) | [danihegglin](https://github.com/danihegglin) | [faberchri](https://github.com/faberchri):----------------:|:-----------:|:----------:Unit tests | ch.uzh.agglorecommender.client | ch.uzh.agglorecommender.client|| ch.uzh.agglorecommender.recommender (and subpackages) | ch.uzh.agglorecommender.clusterer (and subpackages)|| ch.uzh.agglorecommender.util | ch.uzh.agglorecommender.util|| ch.uzh.agglorecommender.visu | ch.uzh.agglorecommender.visu


## Anleitung zur Applikation
### Datensatzdefinition
Zum starten der Applikation muss eine XML-Datei die die Struktur des zu berechnenden Datensatz spezifiziert als Kommandozeilenargument übergeben werden. [Die XML-Datei muss diesem XML-Schema entsprechen](https://github.com/faberchri/tree-builder-2/blob/release/TreeBuilder/manual/explanation-dataset-prop-file/prototype-input-properties-attBased.xsd). Grundsätzlich wird angenommen, dass der Datensatz in Form von CSV-Dateien vorliegt, wobei der Feldseperator ein beliebiges (jedoch nicht mehrere) Zeichen sein kann. Die Struktur des zu übergebenden XMLs ist in der folgenden Abbildung dargestellt.

![alt text](https://raw.github.com/faberchri/tree-builder-2/release/TreeBuilder/manual/explanation-dataset-prop-file/img/prototype-input-properties-attBased_xsd_Element_arin_Input.jpeg "Wurzelelement mit benötigten und optionalen Attributelementen der Datensatzdefinitionsdatei.")

Für jedes Attribut des Datensatzes muss ein neues Attributelement als Kind vom Input-Element eingefügt werden. Es können beliebig viele Attributelemente definiert werden, mindestens ein Attribut muss allerdings vom Typ Rating sein.

Hiernach werden die in der XML-Schema-Datei definierten XML-Elemente beschrieben. Eine [detailliertere Darstellung des erwarteten XML-Aufbaus](https://github.com/faberchri/tree-builder-2/tree/release/TreeBuilder/manual/explanation-dataset-prop-file) sowie [Datensatzdefinitionsdateien](https://github.com/faberchri/tree-builder-2/tree/release/TreeBuilder/manual/example-dataset-prop-files) für die ebenfalls im Respository enthaltenen [Grouplens-Datensätze](https://github.com/faberchri/tree-builder-2/tree/release/TreeBuilder/datasets) befinden sich im Repository.

* __Attribute__ Jedes Attributelement hat genau ein Attribute-Element als Kind. Beschreibt die Attributeigenschaften. Beinhaltet Tag, useForClustering und PreProcessingRegex.
* __Category__ In Typ NominalAttribute: Soll ein Metaattribut nominal verarbeitet werden können die Attributwerte Kategorien (CategoryTag) zugeteilt werden. Zum Beispiel können Postleitzahlen gemäss der ersten Ziffer des Attributwerts zehn Kategorien zugeordnet werden. Die Selektion der Kategorie erfolgt durch IdentificationRegex.
* __CategoryTag (String)__ In Typ Category: Der eindeutige Schlüssel der Attributwertekategorie.
* __ColumnSeparator (char)__ In Typ File: Das Zeichen (genau eines) das die Spalten der CSV-Datei trennt.
* __ContentIdColumnNumber (positive int)__ In Typ RatingFile: Die Spaltennummer (erste Spalte hat Index 1) der CSV-Datei welche die Inhalts-Id beinhaltet.
* __contentNumericalAttribute (optional)__ Ist Kind von Input: Beschreibt ein Metaattribut eines Inhalts (z.B. Toy Story) das numerisch verarbeitet werden soll. Hat ein NumericalAttribute und ein oder mehrere MetaFiles.
* __contentNominalAttribute (optional)__ Ist Kind von Input: Beschreibt ein Metaattribut eines Inhalts (z.B. Toy Story) das nominal verarbeitet werden soll. Hat ein NominalAttribute und ein oder mehrere MetaFiles.
* __contentNominalMultivaluedAttribute (optional)__ Ist Kind von Input: Beschreibt ein Metaattribut eines Inhalts (z.B. Toy Story) das nominal verarbeitet werden soll und mehrere Werte für einen Inhalt haben kann (z.B. Genre). Hat ein NominalMultivaluedAttribute und ein oder mehrere MetaFiles.
* __EndLine (positive int, optional)__ In Typ File: Letzte Zeile (erste Zeile in Datei hat Index 1) die für das entsprechende Attribut von der CSV-Datei gelesen wird.
* __File__ Jedes Attributelement hat mindestens ein File-Element als Kind. Spezifiziert die Eigenschaften der dazugehörenden CSV-Datei. Hat die Parameter ColumnSeparator, Location und ValueColumnNumber. Zusätzlich kann StartLine und EndLine spezifiziert werden. 
* __IdColumnNumber (positive int)__ In Typ MetaFile: Die Spaltennummer (erste Spalte hat index 1) der CSV-Datei welche die Entitäts-Id beinhaltet (z.B. Benutzer-Id oder Film-Id).
* __IdentificationRegex (String)__ In Typ Category: Nach diesem Regex werden die Attributwerte für die entsprechende Attributwertekategorie ausgewählt.
* __Input__ XML-Wurzelelement des Datensatzbeschreibungsdokuments. Benötigt mindestens ein Rating-Element.
* __Location (String)__ In Typ File: Speicherort der CSV-Datei als absoluter Pfad in Dateisystem.
* __MetaFile__ Erbt von File: Beschreibt eine CSV-Datei die Metaattribute speichert. Hat zusätzlich zu den File-Parametern IdColumnNumber.
* __minValue (double)__ In Typ NumericalAttribute: Der kleinste Attributwert im Datensatz. Wird benötigt für die Normalisierung der numerischen Attributwerte.
* __maxValue (double)__ In Typ NumericalAttribute: Der grösste Attributwert im Datensatz. Wird benötigt für die Normalisierung der numerischen Attributwerte.
* __NominalAttribute__ Erbt von Attribute: Ein Metaattribut das nominal verarbeitet werden soll (z.B: Postleitzahl oder Genre). Ist die Anzahl möglicher Werte des Attributs gross kann deren Zahl durch das Verwenden von Categories reduziert werden.
* __NominalMultivaluedAttribute__ Erbt von NominalAttribute: Beschreibt ein Metaattribut das nominal verarbeitet werden soll und dessen Attributwerte mehrer Werte haben kann. Das Genre des Films "Toy Story" könnte beispielsweise "Children" und "Animation" sein.
* __NumericalAttribute__ Erbt von Attribute: Ein Attribut das numerisch verarbeitet werden soll. Können sowohl Ratings als auch Metaattribute sein. Benötigt die Angabe des Wertebereichs mit minValue und maxValue für Normalisierung.
* __Rating__ Ist Kind von Input: Hat ein NumericalAttribute und ein oder mehrere RatingFiles.
* __RatingFile__ Erbt von File: Beschreibt eine CSV-Datei die Rating-Attribute speichert. Hat zusätzlich zu den File-Parametern die Angaben UserIdColumnNumber, ContentIdColumnNumber und useForTestOnly.
* __StartLine (positive int, optional)__ In Typ File: Erste Zeile (erste Zeile in Datei hat Index 1) die für das entsprechende Attribut von der CSV-Datei gelesen wird.
* __PreProcessingRegex (String, optional)__ In Typ Attribute: Alle eingelesenen Attributwerte werden mit `attributeValueString.replaceAll(<der-hier-definierte-regex>, emptyString) gefiltert.
* __Tag (String)__ In Type Attribute: Jedes Attribut benötigt einen eindeutigen Identifizierungsschlüssel. 
* __useForClustering (boolean)__ In Typ Attribute: Definiert ob das Attribute im Klassenbildungsprozess berücksichtigt werden soll.
* __useForTestOnly (boolean)__ In Typ File: Definiert ob die  Einträge in der CSV-Datei nur als Testdatensatz verwendet werden sollen.
* __UserIdColumnNumber (positive int)__ In Typ RatingFile: Die Spaltennummer (erste Spalte hat Index 1) der CSV-Datei welche die Benutzer-Id beinhaltet.
* __userNumericalAttribute (optional)__ Ist Kind von Input: Beschreibt ein Metaattribut eines Benutzers das numerisch verarbeitet werden soll. Hat ein NumericalAttribute und ein oder mehrere MetaFiles.
* __userNominalAttribute (optional)__ Ist Kind von Input: Beschreibt ein Metaattribut eines Benutzers das nominal verarbeitet werden soll. Hat ein NominalAttribute und ein oder mehrere MetaFiles.
* __userNominalMultivaluedAttribute (optional)__ Ist Kind von Input: Beschreibt ein Metaattribut eines Benutzers das nominal verarbeitet werden soll und mehrere Werte für einen Benutzer haben kann. Hat ein NominalMultivaluedAttribute und ein oder mehrere MetaFiles.
* __ValueColumnNumber (positive int)__ In Typ File: Die Spaltennummer (erste Spalte hat Index 1) der CSV-Datei welche die Attributwerte für das entsprechende Attribut beinhaltet.
* __ValueSeparator (String)__ In Typ NominalMultivaluedAttribute: Unterteilt den Attributwert für ein Attribut mit mehreren Werten (z.B: ein Film der mehreren Genres angehört) in einzelne Attributwerte. Darf nicht gleich sein wie der ColumnSeparator in der entsprechenden Datei.

### Kommandozeilenargumente
Je nach Grösse des zu verarbeitenden Datensatzes sollte die maximal erlaubte Heap- und Stackspeicherbelegung der JVM erhöht werden. Für den kleineren Grouplens-Datensatz mit 100'000 Bewertungen wurde die maximale Stackgrösse pro Thread auf 100 MB (-Xss100m) und die maximale Heapspeichergrösse auf ca. 2 GB (-Xmx2048M) gesetzt. Insbesondere der Heapspeicher sollte für grössere Datensätze deutlich erhöht werden.

Die Applikation kann dann mit dem folgenden Kommandozeilenaufruf gestartet werden:
```bash 
java -Xss100m -Xmx2048M -jar Agglorecommender.jar -p </path/to/your/dataset-properties-file.xml>
```
Den Parameter  `-p` oder `-datasetProperties` mit dem darauf folgendem Pfad zu einer gültigen Datensatzdefinitionsdatei (siehe Abschnitt Datensatzdefinition) muss bei jedem Start der Applikation mitgegeben werden. Für Testzwecke kann anstelle einer Datensatzdefinitionsdatei auch das Schlüsselwort `random` angegeben werden. Damit wird ein Datensatz zufällig generiert. Es besteht auch die Möglichkeit die Grösse des zufällig generierten Datensatzes zu bestimmen:
```bash 
java -Xss100m -Xmx2048M -jar Agglorecommender.jar -p random <number-of-users> <number-of-content-items> <approx. percentage of null entries in User-Content-Rating-Matrix>
```
Zusätzlich kann die Applikation mit folgenden optionalen Kommandozeilenargumenten gestartet werden:

* `-nodeUpdater, `-nU`, `-updater` Erwartet den Klassennamen einer Klasse des Typs INodeUpdater. Standardwert: `SaveAttributeNodeUpdater` (siehe Projektbericht für eine Erklärung der Funktionsweise), Erlaubte Werte: `ExtendedNodeUpdater`: Ergibt das gleiche Klassifizierungsergebnis wie `SaveAttributeNodeUpdater`, `NullUpdater`: Es werden keine Klassifizierungsinformationen zwischen den zwei Klassenhierarchien ausgetauscht, `SimpleNodeUpdater`: Neue Klassen werden als Attribute in die Klassen der nicht erweiterten Klassenhierarchie eingebaut. Die Kinder der neuen Klasse werden aber nicht als Attribute der Klassen der nicht erweiterten Klassenhiererchie entfernt.
* `-noGUI` Clustering läuft ohne graphische Darstellung der Klassenbäume und des Klassenhierarchiegenerierungfortschritts ab.
* `-client` Erwartet den Klassennamen einer Klasse des Typs IClient. Standardwert: `DefaultClient`. Erlaubte Werte: `WebClient`: Öffnet eine Empfehlungswebsite welche über http://localhost:8081 aufgerufen werden kann. 
* `-resume`, `-r` Erwartet den Dateipfad zum Speicherort eines serialisierten TreeBuilder-Objekts. Lädt den TreeBuilder von der Festplatte und setzt bei Bedarf eine unterbrochenen Klassenhierarchiegenerierung fort.
* `-serialize`, `-s` Erwartet einen beschreibbaren Speicherort im Dateisystem. Schreibt eine Kopie des TreeBuilder-Objekts nach Abschluss und während der Klassenhierarchiegenerierung alle 10 min in den persistenten Speicher. Ist diese Option nicht ausgewählt wird keine persistente Kopie der Klassenhierachiebäume erstellt.
* `--help`, `-help`, `--usage`, `-usage` Sendet Erläuterungen zu den Kommandozeilenargumenten über die Standardausgabe an den Benutzer.


### Recommendation System
Wurde kein Client über die Kommandozeile definiert wird im Anschluss an die Klassenbildung der DefaultClient instanziiert. Der DefaultClient bietet die Möglichkeit über die Kommandozeile Befehle einzugeben. Der Befehl enthält folgende Elemente.

* `action` Erwartet die gewünschte Interaktion mit dem Cluster. Erlaubte Werte: `recommend`: Resultiert in einer Recommendation, `insert`: Fügt den definierten Input in den Baum ein, `evaluate`: Evaluiert die Güte der aus dem definierten Input resultierenden Empfehlung.
* `type` Erwartet den Typ des Inputs. Erlaubte Werte: `user`: Der Input besteht aus Nutzerdaten, `content`: Der Input besteht aus Inhaltsdaten.
* `ratingFile`  Erwartet den absoluten Pfad zur Datei die die Bewertungen enthält.
* `metaFile`  Erwartet den absoluten Pfad zur Datei die die Metadaten enthält.   

Der Befehl muss folgendermassen strukturiert sein.
```bash 
action type -r ratingFile -m metaFile
``` 
Dieser Beispiel-Befehl löst eine Empfehlungsgenerierung für einen User aus. Das definierte Meta-File muss die User Metadaten enthalten. 
```bash 
recommend user -r /User/Desktop/ratings -m /User/Desktop/usermeta
``` 
Falls der WebClient über den Kommandozeilen-Parameter `-client WebClient` aufgerufen wird, startet ein integrierter Webserver und bindet an Port 8081. Die Empfehlungswebsite ist daher anschliessend unter http://localhost:8081 zu finden. Startet man den WebClient ist der DefaultClient nicht verfügbar.