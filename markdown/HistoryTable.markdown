Stručný opis
===================

 Modul HistoryTable slúži na výpočet priemerných a maximálnych rýchlostí za posledné dni. A to v smere Upload aj Download. Modul pomocou zadaných parametrov (viď nižšie "povinné atribúty"), ktoré dostane ako vstup (JSON) vytvorí ďalšie JSON-y, ktoré použije ako parametre pri cyklickom volaní modulov AverageDownloadUpload a MaximumDownloadUpload. Ako výstup je použitý JSON. 

**Vstup môže obsahovať nasledujúce parametre - JSON**

 - name - má iba informatívny charakter. Nijako sa pri vykonaní programu nekontroluje, ani nepoužíva.
 - exporter_id - povinný atribút
 - time - povinný atribút
 - client_ip

Pri vynechaní povinných atribútov, sa modul ukončí chybou. Pre správne použitie všetkých atribútov je potrebné držať sa konvencie uvedenej tu. Nepovinné atribúty sa odporúča vyplniť hodnotou null, ale je možné ich aj vynechať. 



 **Výstup - JSON**

 Výstup je tvorený zloženým elementov s poliami vypočítaných rýchlostí. 

	{
        "name": "AmountMiniTable", 
        "status":"ok",
        "response": {
                "maximumDownload":[[1396994400000, 789],[1397080800000, 987]],
                "averageDownload":[[1396994400000, 123],[1397080800000, 456]],
                "maximumUpload":[[1396994400000, 888],[1397080800000, 777]],
                "averageUpload":[[1396994400000, 321 ],[1397080800000, 654]]
        }
        
	}