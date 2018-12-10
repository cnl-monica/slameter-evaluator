Stručný opis
===================

 Modul na základe definovaných filtračných parametrov určí top 5 downloaderov. 

**Vstup môže obsahovať nasledujúce parametre - JSON**

 - name - má iba informatívny charakter. Nijako sa pri vykonaní programu nekontroluje, ani nepoužíva.
 - exporter_id - povinný atribút
 - time - povinný atribút

Pri vynechaní povinných atribútov, sa modul ukončí chybou. Pre správne použitie všetkých atribútov je potrebné držať sa konvencie uvedenej tu. Nepovinné atribúty sa odporúča vyplniť hodnotou null, ale je možné ich aj vynechať. 



 **Výstup - JSON**

Pri korektnom spracovaní hodnota response obsahuje obsahuje pole maximálne piatich IP adries spolu s počtom prenesených dát.

	{
        "name": "TopDownloader",
        "status":"ok",
        "response": [[125.26.12.3,45895],[124.56.23.1,42658],[125.25.35.45,152]] 
	}