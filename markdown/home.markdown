Vyhodnocovač
===================


> - Verzia: 3.0
> -  Stav verzie: vyvíjaná
> - Autor: Pavol Beňko 
> - Licencia: GNU GPLv3
> - Implemetačné prostredie: java version "1.7.0_75"
> - GIT: https://git.cnl.sk/monica/slameter_evaluator 

**Používateťská príručka aplikácie :**
`https://git.cnl.sk/pavol.benko/Public_space/raw/master/modules_pp.pdf`

**Systémová príručka aplikácie**
`https://git.cnl.sk/pavol.benko/Public_space/raw/master/modules_sp.pdf` 

----------
A. Charakteristika vyhodnocovača
-------------
 Vyhodnocovač na základe požiadaviek spracováva IPFIX záznamy a vytvára tak štatistické a analytické údaje o charaktere meranej sieťovej prevádzky. Jeho architektúra je modulárna, čiže pozostáva z rámca a do neho vkladateľných komponentov. Komponenty vypočítavajú jednotlivé údaje a poskytujú ich v jednoduchej číselnej podobe na základe, ktorej rámec následne vytvára odpovede na požiadavky. 

B. Architektúra vyhodnocovača
-------------
Architektúra vyhodnocovača pozostáva z rámca do ktorého sú umiestňované vyhodnocovacie moduly určené na výpočet sieťovej charakteristiky. V rámci práce došlo k oddeleniu modulov, ktoré komunikujú pomocou protokolu ACP z dôvodu zaťažovania vyhodnocovača. Taktiež by mohlo dôjsť k možnému skorému vyčerpaniu zdrojov pridelených aplikácii Evaluatorik. 

![>](https://git.cnl.sk/uploads/monica/slameter_documentation/229b2989ad/aktVyhRoz.png )

> Vyhodnocovacie moduly
---------------------

   

>  -   [AmountOfTransferredData][1]
 -   [AmountOfTransferredDataPacket][2]
 -   [AmountMiniTable][3]
 -   [AverageOfTransferredData][4]
 -   [AverageOfTransferredDataPacket][5]
 -   [AverageMiniTable][6]
 -   [BandwidthHistoryTrend][7]
 -   [BandwidthHistoryTrendPacket][8]
 -   [HistoryTable][9]
 -   [HistoryTrendFlows][10]
 -   [MaximumDownloadUpload][11]
 -   [NumberOfFlows][12]
 -   [PingTime][13]
 -   [TopUploader][14]
 -   [TopDownloader][15]


C. Komunikácia vyhodnocovača a web rozhrania 
-------------
Komunikácia medzi vyhodnocovačom a webovým rozhraním je realizovaná pomocou databázovej služby Redis. Pomocou vebového rozhrania dochádza ku generovaniu požiadaviek podľa toho, ktorú stránku si užívateľ žiada zobraziť. Po generovaní sú tieto uložené v Redis databáze pričom túto skutočnosť zaznamená vyhodnocovacia aplikácia. 

![komunikacia2](https://git.cnl.sk/uploads/monica/slameter_documentation/ec654f0312/komunikacia2.png)

Po zaznamenaní požiadavky vyhodnocovacia aplikácia notifikuje konkrétny modul pre ktorý je požiadavka generovaná. Po vyhodnotení požiadavky modulom je výsledok spätne zaslaný do Redis databázy a následne dôjde k prevzatiu výsledku webovým rozhraním. 

![komunikacia1](https://git.cnl.sk/uploads/monica/slameter_documentation/244ee15538/komunikacia1.png)

Formát komunikačných správ je vo forme JSON objektov. Bližší popis správ je uvedený [tu][16].

D. Požiadavky pre spustenie a preklad
-------------

>**D1. Požiadavky na technické prostriedky pri preklade**

>Program Evaluatorik bol implementovaný a testovaný na systéme s uvedenými technickými parametrami.

>     Procesor Intel® Core™ i5-3230M (2,60 GHz),
    operačná pamät 2GB,
    pevný disk so 500GB,
    integrovaná grafická karta Intel® HD Graphics 4000,
    sieťová karta 100 Mbit/s. 

>Pre spustenie však postačujú prostriedky s nižšou výkonnosťou, a to podľa nárokov nasledujúcich programových požiadaviek. 

>**D2. Požiadavky na programové prostriedky**

>Pre spustenie aplikácie sú potrebné nasledujúce programové závislostí

>     operačný systém s Java Virtual Machine podporou,
    Java Runtime Environment (JRE) verzie 1.7 a vyššej
    redis-server pre komunikáciu s web rozhraním 

>**D3. Náväznosť na iné programové produkty**

>Pre otestovanie vyhodnocovacej aplikácie je potrebné realizovať minimálne spustenie komponentov na nižšej úrovni a to :

>1. exportér

>2. kolektor + databáza 

E. Inštalácia potrebných balíkov
-------------

Pre inštalaciu redis úložiska nainštalujte balík pomocou nasledujúceho príkazu

	 apt-get install redis-server

F. Vlastný preklad
-------------

Preklad aplikácie pozostáva zo stiahnutia zdrojových textov. Po stiahnutí sú dostupné všetky knižnice potrebné na vykonanie prekladu. Po vykonaní prekladu napríklad v prostredí NetBeans je v priečinku /dist spustiteľný súbor evaluatorik.jar. Po spustení tohto súboru je potrebné realizovať zasielanie požiadaviek na tento vyhodnocovač kde dôjde k následnému spracovaniu a odoslaniu odpovede.

Aktuálnu verziu evaluatorik.jar je možné stiahnuť na nasledujúcej adrese:

	 wget https://git.cnl.sk/pavol.benko/Public_space/raw/master/dist.zip --no-check-certificate

Pre extrahovanie suboru potrebujeme nainštalovať naskedujúci balík

	 sudo apt-get install p7zip-full

Po nainštalovaní vykonávame extrahovanie nasledujúcim príkazom

	 7z x dist.zip

Po stiahnutí a rozbalení je potrebné zmeniť v konfiguračnom súbore parametre pre pripojenie na databázu a Redis databázovú službu. 

G. Postup spustenia vyhodnocovacej aplikácie
-------------

Ako prvé je potrebné realizovať spustenie redis služby nasledujúcim príkazom:

	 sudo /etc/init.d/redis-server start

Pre spustenie samotného vyhodnocovača je potrebné zadať nasledujúci príkaz:

	 java -jar evaluatorik.jar

Ako doplnkový parameter môže byť cesta k upravenému konfiguračnému súboru.

	 java -jar evaluatorik.jar evaluator.xml

V prípade vynechania tohto parametra sú pre spustenie použité defaultné hodnoty uvedene v konfiguračnom súbore.

>**Opis konfiguračného súboru**


>     <evaluator>
	 <database>
	 <dbHost>localhost</dbHost> <!--Hostiteľ databázy (default localhost)-->
	 <dbPort>27017</dbPort> <!--Port pre pripojenie k databáze (default MongoDB 27017)-->
	 <dbName>monica</dbName> <!--Názov databázy (default monica)-->
	 </database>

>     <redis>
	 <redisHost>localhost</redisHost> <!--Hostiteľ Redis služby (default localhost)-->
	 <redisPort>6379</redisPort> <!--Port pre pripojenie k službe Redis (default 6379)-->
	 <redisPoolSize>20</redisPoolSize> <!--Veľkosť zásobníka pre pripojenie (default 20)-->
	 </redis>

>     <modules>
	 PRÍZNAKY SPUSTENÉHO/VYPNUTÉHO MODULU
	 <AmountMiniTable>true</AmountMiniTable>
	 <AmountOfTransferredData>true</AmountOfTransferredData>
	 <AmountOfTransferredDataPacket>true</AmountOfTransferredDataPacket>
	 <AverageMiniTable>true</AverageMiniTable>
	 <AverageOfTransferredData>true</AverageOfTransferredData>
	 <AverageOfTransferredDataPacket>true</AverageOfTransferredDataPacket>
	 <BandwidthHistoryTrend>true</BandwidthHistoryTrend>
	 <BandwidthHistoryTrendPacket>true</BandwidthHistoryTrendPacket>
	 <HistoryTable>true</HistoryTable>
	 <HistoryTrendFlows>true</HistoryTrendFlows>
	 <MaximumDownloadUpload>true</MaximumDownloadUpload>
	 <NumberOfFlows>true</NumberOfFlows>
	 <PingTime>true</PingTime>
	 <TopUploader>true</TopUploader>
	 <TopDownloader>true</TopDownloader>
	 </modules>
	 </evaluator>

  [1]: https://git.cnl.sk/monica/slameter_evaluator/wikis/AmountOfTransferredData
  [2]: https://git.cnl.sk/monica/slameter_evaluator/wikis/AmountOfTransferredDataPacket
  [3]: https://git.cnl.sk/monica/slameter_evaluator/wikis/AmountMiniTable
  [4]: https://git.cnl.sk/monica/slameter_evaluator/wikis/AverageOfTransferredData
  [5]: https://git.cnl.sk/monica/slameter_evaluator/wikis/AverageOfTransferredDataPacket
  [6]: https://git.cnl.sk/monica/slameter_evaluator/wikis/AverageMiniTable
  [7]: https://git.cnl.sk/monica/slameter_evaluator/wikis/BandwidthHistoryTrend
  [8]: https://git.cnl.sk/monica/slameter_evaluator/wikis/BandwidthHistoryTrendPacket
  [9]: https://git.cnl.sk/monica/slameter_evaluator/wikis/HistoryTable
  [10]: https://git.cnl.sk/monica/slameter_evaluator/wikis/HistoryTrendFlows
  [11]: https://git.cnl.sk/monica/slameter_evaluator/wikis/MaximumDownloadUpload
  [12]: https://git.cnl.sk/monica/slameter_evaluator/wikis/NumberOfFlows
  [13]: https://git.cnl.sk/monica/slameter_evaluator/wikis/PingTime
  [14]: https://git.cnl.sk/monica/slameter_evaluator/wikis/TopUploader
  [15]: https://git.cnl.sk/monica/slameter_evaluator/wikis/TopDownloader
  [16]: https://git.cnl.sk/monica/slameter_evaluator/wikis/Format-sprav
