# PROJEKT do předmětu MULTIMÉDIA 2018/2019
Projekt do předmětu Multimédia je rozdělen na 2 části, termín odevzdání je dán v Informačním systému. Projekt bude obhajován v zápočtovém týdnu

## Část 1: Povinná pro všechny – `povinná – minimum 5 bodů`.
Cílem první části je implementovat určité části MPEG kodéru i dekodéru. Při implementaci můžete využít Vámi naprogramované metody z první poloviny semestru nebo metody dostupné v e- learningu. Vstupem budou obrázky z videosekvence dostupné v e-learningu,
První obrázek bude kódován jako snímek I (bez predikce), druhý obrázek bude predikován ze snímku I za pomoci odhadu a kompenzace pohybu – Snímek P

### Kodér:
* Převod RGB na YCbCr + zobrazení složek Y, Cb, Cr obou obrázků
* Vzorkování bude ponecháno původní 4:4:4 - DCT po blocích 8x8
* Kvantizace shodná s JPEG kodérem
* DPCM (chyba predikce) - mezi snímky 2 a 1 bez odhadu a kompenzace pohybu + zobrazení chyby predikce složek Y, Cb, Cr. <span style="color:red">**(1b)**</span>
* Odhad pohybu metodou Full-Search (prováděno na Y složce) + kompenzace pohybu pro složky Y, Cb, Cr: ke kompenzaci budou požity vektory složky Y + zobrazení odhadnutých složek Y, Cb, Cr obou obrázků **(4b)**
* DPCM (chyba predikce) - mezi snímky 2 a 1 po odhadu a kompenzaci pohybu + zobrazení chyby predikce složek Y, Cb, Cr. **(1b)**
* Výpočet SAD (Sum of Absolute Difference) - chyba bez predikce vs. chyba s predikcí **(1b)**

### Dekodér:
* Inverzní kvantizace shodná s JPEG dekodérem
* IDCT po blocích 8x8
* Kompenzace pohybu **(2b)**
* Převod YCbCr na RGB

### Vyhledávání pohybu:
* Vyhledávání bude prováděno bez hranic (tzn. snímek pro vyhledávání bude rozšířený o 8 pixelů na každou stranu).
* Vyhledávácí oblast bude v oblasti 32x32 px. - Vyhledávat se bude vždy makroblok (16x16 px).
* Vyhledávací oblast bude volitelná (vždy dvojnásobná oproti zadané velikosti vyhledávaného bloku. Blok pro vyhledávání bude volitelný (2x2, 4x4, 8x8, 16x16). **(3b)**

## 2. Část: volitelná
### Implementujte níže uvedené metody a proveďte porovnání s metodou Full Search.
* V kodéru i dekodéru implementujte odhad pohybu metodou Three step Search za stejných podmínek, jako tomu bylo u předchozího. **(2b)**
* V kodéru i dekodéru implementujte odhad pohybu metodou One-at-time search za stejných podmínek, jako tomu bylo u předchozího. **(2b)**
* Implementujte vyhledávání s půl-pixelovou přesností pohybu. **(4b)**