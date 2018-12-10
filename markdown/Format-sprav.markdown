Komunikácia smerujúca k Modulom Vyhodnocovača
===================

Podľa návrhu komunikácie medzi Vyhodnocovačom a Webovým rozhraním sa zaviedlo, že požiadavky smerujúce k modulom budú vyskladané z dielov. Týmto sa dosiahla unnifikácia v komunikácii, čo uľahčuje použitie a vývoj modulov. Komunikácia modulov môže pozostávať z nasledujúcich elementov.

**Meno**

Meno modulu v konvencii CamelCase
	
	"name": "MenoModulu"

**Identifikácia meracieho bodu**

Identifikačné číslo exportéra. Tento atribút je vo všetkých moduloch povinný.

	"exporter id": 124

**Časový interval**

Pole dvoch časových značiek v milisekundách reprezentujúce časový interval. Tento atribút je vo všetkých moduloch povinný.

	"time": [1295478600000, 1322087140070]

**Identifikácia klienta**

IP adresa klienta.

	"client ip": "192.168.3.3"

**Identifikácia množiny zariadení**

Vyberie sa obojsmerná komunikácia voči istému zariadeniu, skupine zariadení, alebo voči nejakej sieti. Do poľa je možné zadať až dve množiny zariadení, pre výber komunikácie medzi uvedenými množinami. Štruktúra pre výber množiny zariadení sa skladá z dvoch atribútov reprezentujúcich meno dátového typu (array - pole alebo range - rozsah) a samotnú vyhľadávanú množinu zariadení.

	"host_ip": [
	{
        "type": "array",
        "ips": ["10.1.1.1","10.10.2.1", "10.10.1.100"]
	},
	{ 
        "type": "range",
        "ips": ["192.168.0.1","1192.168.255.255"]
	}
	]

**Identifikácia zdrojových zariadení**

Vyberie sa komunikácia, ktorej zdrojová IP adresa je definovaná v požiadavke. Štruktúra pre výber množiny zariadení sa skladá z dvoch atribútov reprezentujúcich meno dátového typu (array - pole alebo range - rozsah) a samotnú vyhľadávanú množinu zariadení.

	"source_ip": 
	{
        "type": "array",
		"ips": ["192.168.1.1", "192.168.1.2"]
	}

**Identifikácia cieľových zariadení**

Vyberie sa komunikácia, ktorej cieľová IP adresa je definovaná v požiadavke. Štruktúra pre výber množiny zariadení sa skladá z dvoch atribútov reprezentujúcich meno dátového typu (array - pole alebo range - rozsah) a samotnú vyhľadávanú množinu zariadení.

	"destination_ip": 
	{
        "type": "array",
        "ips": ["192.168.8.1", "192.168.8.2"]
	}

**Spôsob kombinovania pri vymedzovaní zariadení na základe definovania IP adrie**

***Klient***

Požiadavka od klienta bude vždy obsahovať element client_ip. Pri požiadavke pre filtrovanie jednosmernej prevádzky bude požiadavka obsahovať jeden z elementov source_ip, destination_ip. Pri požiadavke na filtrovanie obojsmernej prevádzky voči množine zariadení bude požiadavka obsahovať element host_ip.

***Provajder*** 

Pri požiadavke pre filtrovanie jednosmernej prevádzky bude požiadavka obsahovať jeden z elementov source_ip, destination_ip. Druhú stranu komunikácie je možné dodefinovať elementom host_ip. Pri požiadavke na filtrovanie obojsmernej prevádzky voči množinám zariadení bude požiadavka obsahovať dve štruktúry v poli v elemente host_ip.

**Definovanie portov**

Porty je možné definovať tak isto ako IP adresy. Samozrejmosťou je oddelenie zdrojových portov od cieľových. Ale aj použitie elementu host_port, ktorého použitie pre filtráciu portov je implicitné použitiu elementu host_ip pri filtrácii IP adries. Štruktúra a rôznorodosť definície požadovaných portov (zdrojových a cieľových) je zhodná s definíciou zdrojových a cieľových IP adries.

	"source_port":
	{
        "type": "array",
        "ports": [80, 88, 8080]
	}

	"source_port":
	{
        "type": "range",
        "ports": [49152, 65535]
	}

	"source_port":
	{
        "type": "array",
        "ports": [28960]
	}

**Reprezentácia elementov bez hodnôt**

Ak v požiadavke nie je potrebné filtrovať záznamy v databáze na základe niektorého z elementov, potom hodnota v elemente je null.

	"source_port": null




Komunikácia od Modulov Vyhodnocovača
===================

Odpoveď Vyhodnocovača je značne jednoduchšia ako požiadavka. Obsahuje tri jednoduché elementy. Prvým je meno modulu, ktorý odpovedá. Jeho reprezentácia je textová a tak ako požiadavka používa konvenciu CamelCase. Ďalej element status, ktorý používa 3 textové hodnoty: **ok, unavailable** a **error**. Podľa tohto elementu obsahuje element response nasledovné odpovede:

**ok** - korektnú odpoveď modulu vyjadrujúcu určitú sieťovú charakteristiku. V elemente response sa nachádza hodnota/hodnoty sieťovej charakteristiky.

**unavailable** - modul sa síce vykonal korektne, ale v databáze sa nenachádzajú záznamy zodpovedajúce požiadavkám a nie je možné vypočítať požadovanú charakteristiku. V tomto prípade sa v elemente response nachádaza null.

**error** - odpoveď obsahuje chybové hlásenie modulu. V tomto prípade sa bude v elemente response nachádzať správa o vzniknutej situácii. 

	{
        name: "MenoModulu",
        status : "ok",
        response: "123456789"
	}

***Pozn. :***
Niektoré sieťové charakteristiky je možné vyjadriť aj v prípade, že sa z databázy nevybral nijaký záznam. Napríklad množstvo prenesených dát, kde odpoveď modulu bude 0 alebo charakteristiky vyjadrené grafmi s odpoveďou prázdneho poľa. Ale nie je možné určiť napríklad odozvu zariadenia, pretože ak nie sú dostupné údaje o odozve, bolo by matematicky nesprávne vrátiť používateľovi 0 ako odpoveď. Používateľ by sa totiž domnieval, že odozva je tak malá, že bola aproximovaná na hodnotu 0. 