Stručný opis
===================

 Modul AmountMiniTabel slúži na výpočet množstva prenesených dát pre danú IP v oboch smeroch (aj Download aj Upload). Modul pomocou zadaných parametrov, ktoré dostane ako vstup (JSON) vytvorí ďalšie 2 JSON-y, ktoré použije ako parametre pri volaní modulu AmountOfTransferredData. Ako výstup je použitý JSON. 

**Vstup môže obsahovať nasledujúce parametre - JSON**

 - name - má iba informatívny charakter. Nijako sa pri vykonaní programu nekontroluje, ani nepoužíva.
 - exporter_id - povinný atribút
 - time - povinný atribút
 -  client_ip - povinný atribút 

Pri vynechaní povinných atribútov, sa modul ukončí chybou. Pre správne použitie všetkých atribútov je potrebné držať sa konvencie uvedenej tu. Nepovinné atribúty sa odporúča vyplniť hodnotou null, ale je možné ich aj vynechať. 



 **Výstup - JSON**

Výstup je tvorený zloženým elementov, ktorého prvky reprezentujú:

    

 - počet prenesených dát v bajtoch v smere upload
 - počet prenesených dát v bajtoch v smere download

Príklad korektného výstupu

	{
        "name": "AmountMiniTable", 
        "status":"ok",
        "response":  {
                "amountUpload":12345678,
                "amountDownload":98765432
        }
        
	}