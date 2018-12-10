Stručný opis
===================

 Modul na základe definovaných filtračných parametrov vypočíta priebeh rýchlosti prenosu dát v B/s v požadovanom časovom intervale. Na základe odpovede modulu je Web rozhranie schopné zobraziť graf. 

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

Odpoveď v elemente response je zložená u poľa elementov. Každý element predstvuje jeden bod grafu. Prvá hodnota elementu je časová značka - timestamp, druhá hodnota predstavuje rýchlosť prenosu dát.

	{
        "name": "BandwidthHistorickyTrend",
        "status":"ok",
        "response": [
        [1397815200000, 60184]
        [1397815201567,73595],
        [1397815210639,298266],
        [1397815216645,85447],
        [1397815230088,100563]
        ]
        
	}