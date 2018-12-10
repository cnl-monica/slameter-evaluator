Stručný opis
===================

 Modul pre výpočet priebehu množstva tokov pretekajúcich danou sieťou vo vybranom čase. Na základe odpovede modulu je Web rozhranie schopné zobraziť graf. 

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

Pri korektnom spracovaní hodnota response je zložená z poľa elementov. Každý element predstavuje jeden bod grafu. Prvá hodnota je časová značka timestamp, druhá hodnota predstavuje množstvo tokov pretekajúcich sieťou v tom čase.

	{
        "name": "HistoryTrendFlows",
        "status":"ok",
        "response": [
        [1397815200000, 15]
        [1397815201567,12],
        [1397815210639,18],
        [1397815216645,23],
        [1397815230088,20]
        ]
        
	}