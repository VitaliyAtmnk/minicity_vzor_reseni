# MiniCity

## Cíl

Vytvořte konzolovou aplikaci pro jednoduchou správu města.

Město je 2D čtverec tvořen z políček `Tile`

##### Příklad

| $_y$ \ $^x$ | 1   | 2   | 3   | 4   | 5   |
| ----------- | --- | --- | --- | --- | --- |
| 1           | P   |     |     |     |     |
| 2           | R   | H   | H   | P   |     |
| 3           | R   | P   | R   |     |     |
| 4           |     |     |     | F   |     |
| 5           |     | P   | R   | R   | R   |

> P - Park, H - House, R - Road, F - Factory

Uživatel může pomoci příkazu interagovat s městem.

Uživatel má za cíl maximalizovat své skóre.

Jednotlivé políčka mají svou cenu a skóre, viz [tabulka](#tabulka-cen-a-hodnocení).

Každé kolo (jedno zadání příkazu) se přepočítá celkové skóre.

### Příkazy

- **Add tileType x y** : přidá na souřadnice `x`, `y` políčko typu `tileType`, pokud na to má uživatel dostatek `budget`
  - příklad: add House 3 2 -> přidá dům (H) na souřadnice x = 3, y = 2 
  - pokud je políčko obsazené, přepíše se na nový tile

- **Remove x y** : odebere políčko na pozici `x`, `y`, pokud na políčku nic není, nic se nestane.
- **Info x y** : vypíše popisek `Tile` na pozici `x`, `y` (pomocí metody `getDescription`)
- **Upgrade x y** : Vylepší políčko na pozici `x`, `y`. (Pokud to jde - kdy to jde je vysvětleno později)
- **Next** : *přeskočí* jedno kolo, tj. přepočítá se skóre a aplikace čeká na další vstup.
- **Show** : Zobrazí mapu města (podobně jako [v ukázce](#příklad))
- **Budget** : Vypíše na konzoli hodnotu `budget`
- **Exit** : Vypíše celkové skóre a ukončí aplikaci

>Pro úspěšné odevzdání je potřeba vytvořit a implementovat rozumný návrh aplikace.
>
>Neopakujte části kódu, které lze nějak sjednotit. Vytvářejte si pomocné metody pro lepší čitelnost. Nejlépe by každá metoda měla dělat **jednu** věc a ta by měla být zřejmá s názvu.
>
>Aplikace se bude spouštět voláním metody `run()` z metody `main()`

## Základní model

Zde je výpis **základních** požadavků na aplikaci. Můžete do aplikace přidat cokoliv dalšího, co uznáte za **vhodné**.

### Tabulka cen a hodnocení

| TileType | Price | Starting score |
| -------- | ----- | -------------- |
| Road     | 100   | 100            |
| Park     | 150   | 200            |
| House    | 200   | 250            |
| Factory  | 300   | 300            |

### `Coordinate`

Souřadnice má atributy `x: int` a `y: int`. Umožňuje zjistit manhattanskou vzdálenost od jiné souřadnice metodou `distanceTo(other: Coordinate): int`.

##### Vzoreček
**Manhattanská vzdálenost** : Vzdálenost dvou míst od sebe na čtvercové ploše, zde je vzorec: `Math.abs(bodA.x - bodB.x) + Math.abs(bodA.y - bodB.y)`

### `Tile`

`Tile` je společný abstraktní základ všech políček mapy. 

Atributy 
- `coordinate: Coordinate`
- `createdAt: LocalDate` - při vytvoření objektu se nastaví na LocalDate.now();

Metody 
- `getCoordinate(): Coordinate`.
- `getSymbol(): char` - `P` - Park, `H` - House, `R` - Road, `F` - Factory
- `getDescription(): String` - Vrátí užitečné informace o políčku v jednom řetězci. Např.: `Typ - (x, y) | Score:`  
- `getScore(): int`

## "Schopnosti" políček

### `Upgradable`

Objekt, který lze vylepšit, umí sdělit cenu dalšího vylepšení metodou `getUpgradePrice(): int` a provést vylepšení metodou `upgrade(): void`. Vylepšení zvyšuje `level` políčka. Skóre políčka je tímto atributem násobené. 

>Příklad: políčko s levelem 5 a základním skóre 200 má celkovou hodnotu 5 * 200 = 1000.

### `Producing`

Objekt, který umí produkovat energii, poskytuje metodu `produce(): int`. Výsledkem je množství energie vytvořené v jednom tahu.
Každý tah se provede metoda `produce` a její návratová hodnota se přičte k rozpočtu (`budgetu`) uživatele.

## Konkrétní prvky mapy

### `RoadTile`

`RoadTile` je `Tile`. Má atribut `level: int`. Lze vylepšit (*je Upgradable*).

| Price | Starting score |
| ----- | -------------- |
| 100   | 100            |

### `ParkTile`

`ParkTile` je zvláštní typ `RoadTile`. Má atribut `attractivenessBonus: int`. Lze vylepšit (*je Upgradable*).
metoda `upgrade()` zvýší level o 1 a `attractivenessBonus` o 25.

| Price | Starting score | Starting attractivenessBonus |
| ----- | -------------- | ---------------------------- |
| 150   | 200            | 50                           |

### `HouseTile`

`HouseTile` je `Tile`. Má atributy `population: int` a `level: int`. Dům lze vylepšit (*je Upgradable*).

| Price | Starting score | Population                     |
| ----- | -------------- | ------------------------------ |
| 200   | 250            | náhodné číslo z rozmezí 1 až 8 |


### `FactoryTile`

`FactoryTile` je `Tile`. Má atribut `production: int`. Továrna umí produkovat energii (*je Producing*).
metoda `produce()` náhodně vygeneruje 25 až 75 energie. Metoda hodnotu vrátí a to se přičte `budgetu` (budget je kapitál uživatele, vysvětleno později) dle vzorečku `energie * 4`.

| Price | Starting score |
| ----- | -------------- |
| 300   | 300            |

## Řazení podle vzdálenosti

### `TileDistanceFromCenterComparator`

`TileDistanceFromCenterComparator` umí porovnávat objekty typu `Tile`. Má atribut `center: Coordinate`, který obdrží při vytvoření. Poskytuje metodu `compare(first: Tile, second: Tile): int`.

Prvky se řadí podle manhattanské vzdálenosti od středu mapy.

## Mapa města

### `CityMap`

Uchovává: 
- mapu města způsobem, který uznáte za vhodný, 
- souřadnici středu `center: Coordinate` 
- a velikost `size: int` 

Mapa spravuje umístění prvků a poskytuje tyto metody:

- `add(tile: Tile): void`: přidá prvek do mapy na pozici uvedenou v `tile`
- `getTile(coordinate: Coordinate): Tile`: vrátí referenci na políčko `tile` na pozici v `coordinate`
- `remove(coordinate: Coordinate): void`: smaže prvek na pozici v `coordinate`
- `getTiles(): List<Tile>`: vrátí všechny prvky mapy města. Uspořádané tak, jak uznáte za vhodné
- `getCenter(): Coordinate`: vrátí souřadnice středu mapy. Například pro mapu 5x5 vrátí (x: 3 | y: 3)
- `printMap(): void`: vykreslí mapu na konzoli. Podobně jako [zde](#příklad)

Mapa má rozlišovat prázdná a obsazená pole. Při práci se souřadnicemi zohledněte hranice mapy.

## Správa hry

### `CityManager`

`CityManager` má `cityMap: CityMap` a `budget: int`. Koordinuje práci s mapou, rozpočtem a průběhem tahů. Poskytuje tyto metody:

- `addTile(...): void`
- `removeTile(coordinate: Coordinate): void`
- `upgradeTile(coordinate: Coordinate): void`
- `nextTurn(): void`

Při přidání prvku se má zohlednit jeho cena a dostupný rozpočet. Při vylepšení má `CityManager` pracovat pouze s prvkem, který lze vylepšit. Na konci tahu se provede metoda `produce()` u políček, co produkují energii. 

Konkrétní pořizovací ceny, výchozí hodnoty atributů a pravidla růstu zvolte konzistentně a přehledně. Jejich volba nesmí narušit výše uvedené vztahy mezi třídami.

## Konzolová aplikace

### `ConsoleApplication`

`ConsoleApplication` má `cityManager: CityManager` a `scanner: Scanner`. Zajišťuje komunikaci s uživatelem. Poskytuje metody `run(): void` a `processCommand(command: String): void`.

Aplikace má umožnit alespoň:

- vypsat mapu,
- zobrazit rozpočet,
- přidat prvek na souřadnici,
- odebrat prvek ze souřadnice,
- vylepšit prvek na souřadnici,
- přejít do dalšího tahu,
- ukončit aplikaci.

## Doporučené ověření

Ověřte alespoň následující scénáře:

1. Do prázdné mapy lze přidat různé typy prvků.
2. Rozpočet se po přidání prvku sníží.
3. Vylepšení funguje pouze u prvků, které lze vylepšit.
4. Další tah zohlední prvky, které umí produkovat energii.