Stručný opis
===================

Modul na základe definovaných filtračných parametrov vypočíta premernú rýchlosť prenosu údajov v paket/s v určitom časovom intervale. Komunikuje prestredníctvom JSON správ. 

**Vstup môže obsahovať nasledujúce parametre - JSON**

 - name - má iba informatívny charakter. Nijako sa pri vykonaní programu nekontroluje, ani nepoužíva.
 - exporter_id - povinný atribút
 - time - povinný atribút
 - client_ip
 - host_ip
 - source_ip
 - destination_ip
 - host_port
 - source_port
 - destination_port 

Pri vynechaní povinných atribútov, sa modul ukončí chybou. Pre správne použitie všetkých atribútov je potrebné držať sa konvencie uvedenej tu. Nepovinné atribúty sa odporúča vyplniť hodnotou null, ale je možné ich aj vynechať. 



**Výstup - JSON**

Pri korektnom spracovaní hodnota response obsahuje číslo reprezentujúce priemernú rýchlosť prenosu údajov v paket/s.

	{
        "name": "AverageOfTransferredData",
        "status":"ok",
        "response": 87654321 
	}