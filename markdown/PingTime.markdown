Stručný opis
===================

Modul na základe definovaných filtračných parametrov vypočíta priemernú hodnotu RTT (Round Trip Time) v milisekundách. RTT je možné určiť len pri TCP packetoch. Komunikuje prestredníctvom JSON správ. 

**Vstup môže obsahovať nasledujúce parametre - JSON**

 - name - má iba informatívny charakter. Nijako sa pri vykonaní programu nekontroluje, ani nepoužíva.
 - exporter_id - povinný atribút
 - time - povinný atribút
 - client_ip

Pri vynechaní povinných atribútov, sa modul ukončí chybou. Pre správne použitie všetkých atribútov je potrebné držať sa konvencie uvedenej tu. Nepovinné atribúty sa odporúča vyplniť hodnotou null, ale je možné ich aj vynechať. 



 **Výstup - JSON**

Pri korektnom spracovaní hodnota response obsahuje číslo reprezentujúce priemernú hodnotu RTT v milisekundách.

	{
        "name": "PingTime",
        "status":"ok",
        "response": 321 
	}