Stručný opis
===================
Modul AverageMiniTable slúži na výpočet množstva prenesených dát pre danú IP v oboch smeroch (aj Download aj Upload). Modul pomocou zadaných parametrov, ktoré dostane ako vstup (JSON) vytvorí ďalšie 2 JSON-y, ktoré použije ako parametre pri volaní modulu AverageOfTransferredData. Ako výstup je použitý JSON.  

**Vstup môže obsahovať nasledujúce parametre - JSON**

 - name - má iba informatívny charakter. Nijako sa pri vykonaní programu nekontroluje, ani nepoužíva.
 - exporter_id - povinný atribút
 - time - povinný atribút
 - client_ip

**Výstup - JSON**

Výstup je tvorený zloženým elementov, ktorého prvky reprezentujú:

 - priemerné množstvo prenesených dát v bajtoch v smere upload
 - priemerné množstvo prenesených dát v bajtoch v smere download 

Príklad korektného výstupu

	{
        "name": "AverageMiniTable", 
        "status":"ok",
        "response": {
                "averageUpload":123456,
                "averageDownload":987654
        }
	}