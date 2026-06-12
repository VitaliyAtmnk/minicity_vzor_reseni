# MiniCity – část 2

Nyní pokračujete již v hotové architektuře, vaším úkolem bude zpracovat vstupní data, spočítat nad nimi informace a následně je vyexportovat.

## Cíl


Navazujete na hotový projekt **MiniCity** z první části.

V této části již **nenavrhujete nový model města**. V projektu už existují základní třídy jako `Coordinate`, `Tile`, `RoadTile`, `ParkTile`, `HouseTile`, `FactoryTile`, `CityMap`, `CityManager`, `Upgradable` a `Producing`.

Vaším úkolem je doplnit do projektu dávkové zpracování vstupních souborů, výpočet statistik nad hotovým městem a export výsledků do souborů.

> Cílem této části je ověřit především práci se soubory, adresáři, kolekcemi, mapami, řazením a streamy.


Začněte tím, že si ideálně vytvoříte vlastní variantu projektu pomocí **fork**. V projektu u sebe nyní máte složku **input** a v ní vstupní data. 
---

## Základní pravidla

Do existující architektury města z první části **nezasahujte zbytečně**.

Svoje řešení můžete zpracovat do jedné nebo více tříd, nicméně dbejte toho, aby kód byl vhodně

---

## Výchozí město

Pro zpracování vytvořte ve své aplikaci jedno společné město:

```java
CityMap cityMap = new CityMap(5);
CityManager cityManager = new CityManager(cityMap, 5000);
```

Všechny vstupní soubory se zpracovávají nad tímto jedním společným objektem `CityManager`.

---

## Vstupní složka

Aplikace načte příkazy ze složky:

```text
inputs/
```

### Pravidla pro výběr souborů

Zpracovávejte pouze soubory, které splňují všechna pravidla:

- jsou to běžné soubory, 
- nejsou to adresáře,
- mají příponu `.txt`,
- začínají jako `city_`
- mají velikost maximálně **1 MB**

Jiné položky v adresáři ignorujte.


Každý vstupní soubor obsahuje příkazy pro město, jeden příkaz na jeden řádek.

---


### Podporované příkazy

```text
Add tileType x y
Remove x y
Upgrade x y
Next
```

### Příklady

```text
Add Road 3 3
Add House 2 1
Add Factory 5 5
Upgrade 3 3
Next
Remove 2 1
```

V připravených vstupních souborech jsou příkazy validní. Není tedy potřeba nějaké dodatečné kontroly.

---

## Zpracování příkazů

Příkazy používejte nad existující třídou `CityManager`.

Například:

```java
cityManager.addTile("Road", new Coordinate(3, 3));
cityManager.nextTurn();
```

Nepřepisujte logiku přidávání, odebírání, vylepšování ani produkce energie. Tato pravidla už řeší existující projekt.

---

## Statistiky města

Po zpracování všech vstupních souborů vypočítejte statistiky nad výsledným stavem města.

Pracujte s kolekcí dlaždic získanou z existujícího projektu, například:

```java
List<Tile> tiles = cityManager.getTiles();
```

### Povinné statistiky

Vypočítejte následovné:

1. tři nejlepší dlaždice podle skóre
2. % dlaždic, které produkují energi
3. celkovou populaci ve všech domech
4. obsazení všech dlaždic

Vypočtěte následující mapy:

1. počet každého typu dlaždic
2. celková cena za každý typ dlaždice
3. mapa typu dlaždic, který je upgradeable a průmerného skóre
4. mapa affordable - mapa typu dlaždic a kolik si jich mohu s budgetem dovolit postavit
5. Mapa vlastní kategorie rozdělení na near (vzdálenost <=2) a further (jinak)

---

## Výstup 1 – report

Vytvořte soubor:

```text
/report.txt
```

Soubor bude obsahovat čitelný textový souhrn výsledného města.

### Příklad obsahu

```text
MINICITY REPORT

Map size: 5
Budget: 3940
Total score: 2850
Total tiles: 10

Tiles by type:
RoadTile: 3
ParkTile: 2
HouseTile: 3
FactoryTile: 2

Special tiles:
Upgradable tiles: 8
Producing tiles: 2
Total house population: 14

Top 3 tiles by score:
1. ParkTile at (3, 2), score 500
2. HouseTile at (2, 1), score 450
3. FactoryTile at (5, 5), score 300
```

Hodnoty ve vašem výstupu se mohou lišit kvůli náhodné populaci domů a náhodné produkci továren.

---

## Výstup 2 – export dlaždic

Vytvořte soubor:

```text
tiles.csv
```

Soubor bude obsahovat všechny dlaždice ve výsledném městě.

### Povinná hlavička

```text
x;y;type;symbol;score;distanceFromCenter;createdAt
```

### Příklad obsahu

```text
x;y;symbol;score;distanceFromCenter;price
3;3;R;200;0;120
3;2;P;425;1;330
4;3;H;250;1;200
5;5;F;300;4;2000
```

Dlaždice v souboru seřaďte podle vzdálenosti od středu mapy.

---

## Doporučené ověření

Ověřte alespoň tyto scénáře:

1. Program načte pouze vhodné soubory ze složky `inputs/`.
2. Příkazy se aplikují na jedno společné město.
3. Po zpracování existuje soubor `outputs/report.txt`.
4. Po zpracování existuje soubor `outputs/tiles.csv`.
5. `tiles.csv` obsahuje hlavičku a všechny dlaždice výsledného města.
6. Statistiky v reportu odpovídají výslednému stavu města.

---

## Odevzdání

Bez bodového bonusu u odevzdání stačí poslat gist/zip na mail.

S body i za odevzdání vytvořite pull request k repsitory na základě svého fork.